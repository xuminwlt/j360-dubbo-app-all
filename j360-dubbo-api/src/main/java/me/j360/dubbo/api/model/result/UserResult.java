package me.j360.dubbo.api.model.result;

import lombok.Data;

import java.io.Serializable;
import java.util.Set;

/**
 * @author: min_xu
 * @date: 2018/3/20 下午3:31
 * 说明：
 */

@Data
public class UserResult implements Serializable {

    private Set<Long> friends;

}
