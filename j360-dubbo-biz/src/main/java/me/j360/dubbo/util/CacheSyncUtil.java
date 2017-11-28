package me.j360.dubbo.util;

import me.j360.dubbo.common.BaseDao;
import org.redisson.api.RedissonClient;

/**
 * @author: min_xu
 * @date: 2017/11/28 下午4:29
 * 说明：
 */
public class CacheSyncUtil {


    public static Long getCountFromSource(String lockKey, String column, BaseDao baseDao) {


        return 0L;
    }


    /**
     *
     * @param key
     * @param lockKey
     * @param retryTimes 0无线重试
     * @param redissonClient
     */
    public static void retryCountToCache(String key, String lockKey, int retryTimes, RedissonClient redissonClient) {



    }



    public static Long saveSourceToCacheOnce(String key, String lockKey, RedissonClient redissonClient) {
        //在锁内完成数据的读取和回写,释放锁


        return null;
    }


    public static Long getCount() {
        return null;
    }

    public static Long writeCount() {

        return null;
    }



}
