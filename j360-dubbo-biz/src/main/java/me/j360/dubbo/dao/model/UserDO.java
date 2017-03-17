package me.j360.dubbo.dao.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Package: me.j360.dubbo.dao.model
 * User: min_xu
 * Date: 2017/3/16 下午6:03
 * 说明：
 */

@Getter
@Setter
public class UserDO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String name;

}
