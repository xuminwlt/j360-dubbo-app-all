package me.j360.dubbo.event;

import lombok.Data;
import me.j360.dubbo.base.model.domian.BaseDO;

/**
 * @author: min_xu
 * @date: 2017/11/28 下午4:16
 * 说明：
 */

@Data
public class CacheSyncEvent extends BaseDO {

    private String stepKey;
    private String countRangeStep;
    private String timeRangeStep;

    private String table;
    private String column;

    private long cacheCount;
}
