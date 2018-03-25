package me.j360.dubbo.cache.event;

import lombok.Data;
import me.j360.dubbo.base.model.domian.BaseDO;

/**
 * @author: min_xu
 * @date: 2017/11/28 下午4:16
 * 说明：
 */

@Data
public class CacheSyncEvent extends BaseDO {

    private String cacheKey;

    private int index;
    private String table;
    private String column;
    private Long id;

    private long cacheValue;
}
