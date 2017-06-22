package me.j360.dubbo.web.request;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * Package: me.j360.dubbo.web.request
 * User: min_xu
 * Date: 2017/6/22 下午2:57
 * 说明：
 */
@Data
public class AddUserRequest {


    @NotNull
    private String username;
}
