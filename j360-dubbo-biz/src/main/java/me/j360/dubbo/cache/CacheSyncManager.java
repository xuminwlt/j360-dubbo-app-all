package me.j360.dubbo.cache;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import me.j360.dubbo.base.constant.AppConfig;
import me.j360.dubbo.cache.dao.CacheDao;
import me.j360.dubbo.cache.domain.CacheModelEnum;
import me.j360.dubbo.cache.event.CacheSyncEvent;
import me.j360.dubbo.common.concurrent.DefaultAsyncEventBus;
import me.j360.dubbo.mq.CacheSyncProducer;
import me.j360.dubbo.mq.domain.CacheSyncMessage;
import me.j360.dubbo.util.RedisLock;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RedissonClient;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * @author: min_xu
 * @date: 2017/11/29 上午10:54
 * 说明：提供4个方法分别满足不同场景下的cache同步机制
 *
 * 使用RedisLock伪分布式锁(不带notify功能)实现穿透回源的并发控制
 *
 */

@Slf4j
public class CacheSyncManager {

    @Setter
    private RedissonClient redissonClient;
    @Setter
    private CacheDao cacheDao;
    @Setter
    private DefaultAsyncEventBus defaultAsyncEventBus;
    @Setter
    private CacheSyncProducer cacheSyncProducer;

    /**
     * 读取count
     * @param id
     * @param cacheModelEnum
     * @return
     */
    public Long readBizCount(Long id, CacheModelEnum cacheModelEnum) {
        String redisKey = String.format(cacheModelEnum.getRedisKey(), id);
        RAtomicLong rCacheLong = redissonClient.getAtomicLong(redisKey);
        RAtomicLong rResultLong = getSourceToCacheOnce(id, cacheModelEnum, rCacheLong);
        if (Objects.isNull(rResultLong)) {
            return 0L;
        }
        rCacheLong.expireAsync(AppConfig.COMMON_COUNT_CACHE_DAYS, TimeUnit.DAYS);
        return rResultLong.get();
    }


    /**
     * 写入count
     * @param id
     * @param cacheModelEnum
     * @return
     */
    public Long writeBizCount(boolean add,Long id, CacheModelEnum cacheModelEnum) {
        String redisKey = String.format(cacheModelEnum.getRedisKey(), id);
        RAtomicLong rCacheLong = redissonClient.getAtomicLong(redisKey);
        RAtomicLong rResultLong = getSourceToCacheOnce(id, cacheModelEnum, rCacheLong);
        if (Objects.isNull(rResultLong)) {
            //TODO 进入MQ模式
            CacheSyncMessage cacheSyncMessage = new CacheSyncMessage();
            cacheSyncMessage.setTable(cacheModelEnum.getTable());
            cacheSyncMessage.setColumn(cacheModelEnum.getColumn());
            cacheSyncMessage.setCacheKey(redisKey);
            cacheSyncMessage.setId(id);
            cacheSyncMessage.setIndex(cacheModelEnum.getIndex());
            cacheSyncProducer.send(cacheSyncMessage);
            return 0L;
        }

        //执行cache同步
        long cacheCount = add?rResultLong.incrementAndGet():rResultLong.decrementAndGet();
        if (cacheCount < 0) {
            rCacheLong.set(0L);
        }

        //TODO 进入步长模式,调用Event
        CacheSyncEvent event = new CacheSyncEvent();
        event.setTable(cacheModelEnum.getTable());
        event.setColumn(cacheModelEnum.getColumn());
        event.setCacheKey(redisKey);
        event.setIndex(cacheModelEnum.getIndex());
        event.setCacheValue(cacheCount);
        event.setId(id);
        defaultAsyncEventBus.post(event);

        return cacheCount;
    }

    /**
     * 异步队列写入count
     * 无需修改cache的值
     * @param cacheSyncEvent
     */
    public void writeStepCount(CacheSyncEvent cacheSyncEvent) {
        log.info("writeStepCount:{}", cacheSyncEvent);
        RAtomicLong rCacheLong = redissonClient.getAtomicLong(cacheSyncEvent.getCacheKey());
        //获取步长是否满足补偿条件
        if (checkStepCondition(cacheSyncEvent, rCacheLong)) {
            //封装
            CacheModelEnum cacheModelEnum = CacheModelEnum.lookup(cacheSyncEvent.getIndex());
            if (retryCountToCache(cacheSyncEvent.getId(), 0, cacheModelEnum, rCacheLong)) {
                //回写DB
                if (rCacheLong.get() > 0) {
                    writeValue(rCacheLong.get(), cacheSyncEvent.getId(), cacheModelEnum);
                }
            }
        }
        rCacheLong.expire(AppConfig.COMMON_COUNT_CACHE_DAYS, TimeUnit.DAYS);
    }

    /**
     * MQ异步写去count
     * 需要修改cache的值
     * @param cacheSyncMessage
     */
    public void writeMQCount(CacheSyncMessage cacheSyncMessage) {
        //封装
        RAtomicLong rCacheLong = redissonClient.getAtomicLong(cacheSyncMessage.getCacheKey());
        CacheModelEnum cacheModelEnum = CacheModelEnum.lookup(cacheSyncMessage.getIndex());

        //同步redis+db
        if (retryCountToCache(cacheSyncMessage.getId(), 0, cacheModelEnum, rCacheLong)) {
            //修改redis数据
            long value = cacheSyncMessage.isAdd()?rCacheLong.incrementAndGet():rCacheLong.decrementAndGet();
            if (value > 0) {
                writeValue(value, cacheSyncMessage.getId(), cacheModelEnum);
            }
        }

        rCacheLong.expire(AppConfig.COMMON_COUNT_CACHE_DAYS, TimeUnit.DAYS);
    }

    public Long getCountTest(Integer index, Long id) {
        log.info("test");
        CacheModelEnum cacheModelEnum = CacheModelEnum.lookup(index);
        Long count = cacheDao.getCount(cacheModelEnum, id);
        return count;
    }

    /**
     * 用于重试级别的MQ模式的Consumer回写Redis
     * 返回false则进行业务重试,对于mq不返回ack重试
     * @return
     */
    private boolean retryCountToCache(Long id, int retryTimes, CacheModelEnum cacheModelEnum, RAtomicLong rCacheLong) {
        //默认锁10秒
        RedisLock redisLock = new RedisLock(redissonClient, rCacheLong.getName());
        if (retryTimes == 0) {
            retryTimes = Integer.MAX_VALUE;
        }
        while (retryTimes > 0) {
            retryTimes--;
            if (rCacheLong.isExists()) {
                return true;
            }
            try {
                //尝试等待锁100ms
                if (redisLock.lock(100)) {
                    try {
                        if (!rCacheLong.isExists()) {
                            Long count = cacheDao.getCount(cacheModelEnum, id);
                            rCacheLong.set(Optional.ofNullable(count).orElse(0L));
                            rCacheLong.expireAsync(AppConfig.COMMON_COUNT_CACHE_DAYS, TimeUnit.DAYS);
                        }
                    } finally {
                        redisLock.unlock();
                    }
                    return true;
                }
            } catch (InterruptedException e) {
                log.error("读取回源数据失败: [id={},model={}]", id, cacheModelEnum, e);
            }
        }
        return false;
    }


    private RAtomicLong getSourceToCacheOnce(Long id, CacheModelEnum cacheModelEnum, RAtomicLong rCacheLong) {
        //在锁内完成数据的读取和回写,释放锁
        if (rCacheLong.isExists()) {
            return rCacheLong;
        }
        //默认锁10秒
        RedisLock redisLock = new RedisLock(redissonClient, rCacheLong.getName());
        try {
            //尝试等待锁100ms
            int retry = 2;
            while (retry > 0) {
                retry--;
                if (redisLock.lock(100)) {
                    try {
                        if (!rCacheLong.isExists()) {
                            Long count = cacheDao.getCount(cacheModelEnum,id);
                            rCacheLong.set(Optional.ofNullable(count).orElse(0L));
                            rCacheLong.expireAsync(AppConfig.COMMON_COUNT_CACHE_DAYS, TimeUnit.DAYS);
                        }
                    } finally {
                        redisLock.unlock();
                    }
                    if (rCacheLong.isExists()) {
                        return rCacheLong;
                    }
                }
            }
        } catch (InterruptedException e) {
            log.error("读取回源数据失败: [id={},model={}]", id, cacheModelEnum, e);
        }
        //回源获取数据失败,需要进去MQ模式
        return null;
    }


    private void writeValue(Long value,Long id, CacheModelEnum cacheModelEnum) {
        if (Objects.nonNull(value)) {
            if (value > 0) {
                cacheDao.updateCountValue(cacheModelEnum, id, value);
            }
        }
    }


    private boolean checkStepCondition(CacheSyncEvent cacheSyncEvent, RAtomicLong rCacheLong) {
        //
        RAtomicLong rRanageAtomicLong = redissonClient.getAtomicLong(rCacheLong.getName() + "_range");
        long ttl = rRanageAtomicLong.remainTimeToLive();
        long value = rRanageAtomicLong.get();

        if (ttl < AppConfig.COMMON_COUNT_RANGE_DAYS * 24 * 3600 * 1000 - AppConfig.CACHE_TIME_RANGE) {
            rRanageAtomicLong.expire(AppConfig.COMMON_COUNT_RANGE_DAYS, TimeUnit.DAYS);
            return true;
        }
        if (cacheSyncEvent.getCacheValue()-value >= AppConfig.CACHE_COUNT_RANGE) {
            rRanageAtomicLong.set(cacheSyncEvent.getCacheValue());
            return true;
        }
        return false;
        //return true;
    }


}
