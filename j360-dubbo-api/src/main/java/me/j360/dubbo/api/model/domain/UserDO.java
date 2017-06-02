package me.j360.dubbo.api.model.domain;

import lombok.Data;

/**
 * Package: me.j360.dubbo.api.model.domain
 * User: min_xu
 * Date: 2017/5/16 下午8:05
 * 说明：
 */
@Data
public class UserDO {

    private Long uid;
    private String name;
    private boolean vip;

}
