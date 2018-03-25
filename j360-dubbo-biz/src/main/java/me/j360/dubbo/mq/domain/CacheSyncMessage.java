package me.j360.dubbo.mq.domain;

import lombok.Data;
import me.j360.dubbo.base.model.domian.BaseDO;

/**
 * @author: min_xu
 * @date: 2017/11/29 下午2:34
 * 说明：
 */

@Data
public class CacheSyncMessage extends BaseDO {

    private int index;
    private String cacheKey;
    private String table;
    private String column;
    private Long id;

    private long cacheValue;
    private boolean add;

}
