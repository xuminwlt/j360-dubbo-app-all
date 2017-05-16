package me.j360.dubbo.api.service;

import me.j360.dubbo.api.model.domain.UserDO;
import me.j360.dubbo.api.model.param.user.UserDTO;
import me.j360.dubbo.base.model.result.DefaultPageResult;
import me.j360.dubbo.base.model.result.DefaultResult;

/**
 * Package: me.j360.dubbo.api
 * User: min_xu
 * Date: 16/8/23 下午2:26
 * 说明：
 */
public interface UserService {

    DefaultResult<UserDO> getUserInfo(UserDTO options);

    DefaultPageResult<UserDO> listUser(UserDTO options);

    DefaultResult<UserDO> saveUser(UserDTO options);
}
