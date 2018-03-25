package me.j360.dubbo.cache.domain;

import lombok.Getter;
import lombok.ToString;
import me.j360.dubbo.api.constant.J360Keys;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: min_xu
 * @date: 2017/11/29 上午11:01
 * 说明：
 */

@ToString
public enum CacheModelEnum {

    USER_FOLLOW_COUNT(1004, J360Keys.USER_FOLLOW_COUNT, "user_follow_info", "follow_count", "uid", false),
    USER_FOLLOWER_COUNT(1005, J360Keys.USER_FOLLOWER_COUNT, "user_follow_info", "follower_count", "uid", false);

    @Getter
    private final int index;
    @Getter
    private final String redisKey;
    @Getter
    private final String table;
    @Getter
    private final String column;
    @Getter
    private final String idColumn;
    @Getter
    private final boolean forceUpdate;

    /**
     * enum lookup map
     */
    private static final Map<Integer, CacheModelEnum> lookup = new HashMap<Integer, CacheModelEnum>();
    static {
        for (CacheModelEnum s : EnumSet.allOf(CacheModelEnum.class)) {
            lookup.put(s.getIndex(), s);
        }
    }

    CacheModelEnum(int index, String redisKey, String table, String column, String idColumn, boolean forceUpdate) {
        this.index = index;
        this.redisKey = redisKey;
        this.table = table;
        this.column = column;
        this.idColumn = idColumn;
        this.forceUpdate = forceUpdate;
    }

    public static CacheModelEnum getByIndex(int index){
        for (CacheModelEnum cacheModelEnum : CacheModelEnum.values()){
            if (cacheModelEnum.getIndex() == index){
                return cacheModelEnum;
            }
        }
        return null;
    }

    public static CacheModelEnum lookup(int index) {
        return lookup.get(index);
    }

}
