package me.j360.dubbo.util;

import lombok.extern.slf4j.Slf4j;
import me.j360.dubbo.base.constant.AppConfig;
import me.j360.dubbo.common.BaseDao;
import me.j360.dubbo.common.concurrent.DefaultAsyncEventBus;
import me.j360.dubbo.event.CacheSyncEvent;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RedissonClient;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * @author: min_xu
 * @date: 2017/11/28 下午4:29
 * 说明：
 */

@Slf4j
public class CacheSyncUtil {



    /**
     * 用于重试级别的MQ模式的回写Redis
     * @param id
     * @param column
     * @param key
     * @param retryTimes
     * @param rPostLong
     * @param redissonClient
     * @param baseDao
     * @return
     */
    public static boolean retryCountToCache(Long id, String column, String key, int retryTimes, RAtomicLong rPostLong, RedissonClient redissonClient,BaseDao baseDao) {
        //默认锁10秒
        RedisLock redisLock = new RedisLock(redissonClient, key);
        while (retryTimes > 0) {
            retryTimes--;
            if (rPostLong.isExists()) {
                return true;
            }
            try {
                //尝试等待锁100ms
                if (redisLock.lock(100)) {
                    try {
                        if (!rPostLong.isExists()) {
                            Long count = baseDao.getCount(column,id);
                            rPostLong.set(Optional.ofNullable(count).orElse(0L));
                            rPostLong.expireAsync(AppConfig.COMMON_COUNT_CACHE_DAYS, TimeUnit.DAYS);
                        }
                    } finally {
                        redisLock.unlock();
                    }
                    return true;
                }
            } catch (InterruptedException e) {
                log.error("读取回源数据失败: [id={},column={},key={}]", id, column, key, e);
                rPostLong = null;
            }
        }
        return false;
    }


    public static RAtomicLong getSourceToCacheOnce(Long id, String column,String key, RAtomicLong rCacheLong, RedissonClient redissonClient,BaseDao baseDao) {
        //在锁内完成数据的读取和回写,释放锁
        if (rCacheLong.isExists()) {
            return rCacheLong;
        }
        //默认锁10秒
        RedisLock redisLock = new RedisLock(redissonClient, key);
        try {
            //尝试等待锁100ms
            int retry = 2;
            while (retry > 0) {
                retry--;
                if (redisLock.lock(100)) {
                    try {
                        if (!rCacheLong.isExists()) {
                            Long count = baseDao.getCount(column,id);
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
            log.error("读取回源数据失败: [id={},column={},key={}]", id, column, key, e);
        }
        //回源获取数据失败,需要进去MQ模式
        return null;
    }


    /**
     * 回写补偿数据
     * @param id
     * @param column
     * @param key
     * @param redissonClient
     * @param baseDao
     */
    public static void writeCompensateCount(Long id, String column,String key, RedissonClient redissonClient,BaseDao baseDao) {
        RAtomicLong rCacheLong = redissonClient.getAtomicLong(key);
        if (retryCountToCache(id, column, key, 0, rCacheLong, redissonClient, baseDao)) {
            //回写DB加减1
            //baseDao.updateCountValue(column, id);
        }
    }



    public static void writeOne(boolean add, Long id, String column, BaseDao baseDao) {
        //baseDao.updateCountOne();
    }

    public static void writeValue(Long value,Long id, String column,BaseDao baseDao) {
        //baseDao.updateCountValue();
    }



    public static Long readCount(Long id, String column, String key,RedissonClient redissonClient,BaseDao baseDao) {
        RAtomicLong rCacheLong = redissonClient.getAtomicLong(key);
        RAtomicLong rResultLong = getSourceToCacheOnce(id, column, key, rCacheLong, redissonClient, baseDao);
        if (Objects.isNull(rResultLong)) {
            return 0L;
        }
        return rResultLong.get();
    }


    public static Long writeCount(DefaultAsyncEventBus eventBus,Long id, String column,String key, RedissonClient redissonClient,BaseDao baseDao) {
        RAtomicLong rCacheLong = redissonClient.getAtomicLong(key);
        RAtomicLong rResultLong = getSourceToCacheOnce(id, column, key, rCacheLong, redissonClient, baseDao);
        if (Objects.isNull(rResultLong)) {
            //TODO 进入MQ模式

            return 0L;
        }

        long cacheCount = rResultLong.incrementAndGet();
        //进入步长模式,调用Event
        CacheSyncEvent event = new CacheSyncEvent();
        event.setCacheCount(cacheCount);
        eventBus.post(event);

        return cacheCount;
    }









}
