package me.j360.dubbo.api.service;

import me.j360.dubbo.api.model.param.user.UserDTO;
import me.j360.dubbo.api.model.result.user.UserAddResult;
import me.j360.dubbo.api.model.result.user.UserInfoResult;
import me.j360.dubbo.api.model.result.user.UserListResult;

/**
 * Package: me.j360.dubbo.api
 * User: min_xu
 * Date: 16/8/23 下午2:26
 * 说明：
 */
public interface UserService {

    UserInfoResult getUserInfo(UserDTO options);

    UserListResult listUser(UserDTO options);

    UserAddResult saveUser(UserDTO options);
}
