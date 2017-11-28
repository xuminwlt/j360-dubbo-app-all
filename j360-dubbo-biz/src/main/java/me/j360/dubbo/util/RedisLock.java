package me.j360.dubbo.util;

import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * @author: min_xu
 * @date: 2017/11/28 下午5:05
 * 说明：
 */
public class RedisLock  {

    private static Logger logger = LoggerFactory.getLogger(RedisLock.class);

    private RedissonClient redissonClient;

    private static final int DEFAULT_ACQUIRY_RESOLUTION_MILLIS = 100;

    /**
     * Lock key path.
     */
    private String lockKey;

    /**
     * 锁超时时间，防止线程在入锁以后，无限的执行等待
     */
    private int expireMsecs = 10 * 1000;

    /**
     * 锁等待时间，防止线程饥饿
     */
    private int timeoutMsecs = 3 * 1000;


    private volatile boolean locked = false;


    /**
     * Detailed constructor with default acquire timeout 10000 msecs and lock expiration of 60000 msecs.
     *
     * @param lockKey lock key (ex. account:1, ...)
     */
    public RedisLock(RedissonClient redissonClient, String lockKey) {
        this.redissonClient = redissonClient;
        this.lockKey = lockKey + ":lock";
    }

    /**
     * Detailed constructor with default lock expiration of 60000 msecs.
     *
     */
    public RedisLock(RedissonClient redissonClient, String lockKey, int timeoutMsecs) {
        this(redissonClient, lockKey);
        this.timeoutMsecs = timeoutMsecs;
    }

    /**
     * Detailed constructor.
     *
     */
    public RedisLock(RedissonClient redissonClient, String lockKey, int timeoutMsecs, int expireMsecs) {
        this(redissonClient, lockKey, timeoutMsecs);
        this.expireMsecs = expireMsecs;
    }

    /**
     * @return lock key
     */
    public String getLockKey() {
        return lockKey;
    }

    private void expire(final String key, long time, TimeUnit timeUnit) {
        RBucket<String> keyBucket =  redissonClient.getBucket(key);
        keyBucket.expire(time, timeUnit);
    }

    private boolean setNX(final String key, final String value) {
        return redissonClient.getBucket(key).trySet(value);
    }


    /**
     * 获得 lock.
     * 实现思路: 主要是使用了redis 的setnx命令,缓存了锁.
     * reids缓存的key是锁的key,所有的共享, value是锁的到期时间(注意:这里把过期时间放在value了,没有时间上设置其超时时间)
     * 执行过程:
     *
     * 1.通过setnx尝试设置某个key的值,成功(当前没有这个锁)则返回,成功获得锁
     * 2.锁已经存在则获取锁的到期时间,和当前时间比较,超时的话,则设置新的值
     * 3.增加重入锁的机制,本线程有效
     *
     * @return true if lock is acquired, false acquire timeouted
     * @throws InterruptedException in case of thread interruption
     */
    public boolean lock(int timeoutMillis) throws InterruptedException {
        int timeout = timeoutMsecs;
        if (timeoutMillis > 0) {
            timeout = timeoutMillis;
        }
        while (timeout >= 0) {
            long expires = System.currentTimeMillis() + expireMsecs + 1;
            String expiresStr = String.valueOf(expires); //锁到期时间
            if (this.setNX(lockKey, expiresStr)) {
                // lock acquired
                expire(lockKey, expireMsecs, TimeUnit.MICROSECONDS);

                locked = true;
                return true;
            }
            timeout -= DEFAULT_ACQUIRY_RESOLUTION_MILLIS;
            /*
                延迟100 毫秒,  这里使用随机时间可能会好一点,可以防止饥饿进程的出现,即,当同时到达多个进程,
                只会有一个进程获得锁,其他的都用同样的频率进行尝试,后面有来了一些进行,也以同样的频率申请锁,这将可能导致前面来的锁得不到满足.
                使用随机的等待时间可以一定程度上保证公平性
            */
            if (timeout <=0) {
                return false;
            }
            synchronized (this) {
                this.wait(DEFAULT_ACQUIRY_RESOLUTION_MILLIS); //未能获取到lock，进行指定时间的wait再重试.
            }
        }
        return false;
    }


    /**
     * Acqurired lock release.
     */
    public void unlock() {
        if (locked) {
            redissonClient.getBucket(lockKey).delete();
            locked = false;
        }
    }

}