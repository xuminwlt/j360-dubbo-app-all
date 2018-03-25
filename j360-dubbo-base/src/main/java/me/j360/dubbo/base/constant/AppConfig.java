package me.j360.dubbo.base.constant;

public class AppConfig {


    public static final String REDIS_PREFIX_FLODER = "j360";

    //初步确定用户层的缓存的天数
    public static final int COMMON_CACHE_DAYS = 300;
    public static final int COMMON_COUNT_CACHE_DAYS = 300;
    public static final int COMMON_COUNT_RANGE_DAYS = 1;

    public static final long CACHE_COUNT_RANGE = 100;
    public static final long CACHE_TIME_RANGE = 600000; //10分钟
    public static final long CACHE_TIME_RETAIN = 7200000; //2小时 低于2个小时则同步

}
