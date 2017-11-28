package me.j360.dubbo.base.model.domian;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;


public abstract class BaseModel<ID> implements Serializable {

    @Setter
    @Getter
    private ID id;
    @Setter
    @Getter
    private Integer version;
    @Setter
    @Getter
    private Long createTime;
    @Setter
    @Getter
    private Long updateTime;

}
