package me.j360.dubbo.api.constant;

import me.j360.dubbo.base.constant.AppConfig;

/**
 * @author: min_xu
 * @date: 2018/3/25 上午10:25
 * 说明：
 */
public class J360Keys {


    //关注的人
    public static final String USER_FOLLOW_COUNT = AppConfig.REDIS_PREFIX_FLODER + "u:follow:%d";
    public static final String USER_FOLLOWER_COUNT = AppConfig.REDIS_PREFIX_FLODER + "u:follower:%d";

}
