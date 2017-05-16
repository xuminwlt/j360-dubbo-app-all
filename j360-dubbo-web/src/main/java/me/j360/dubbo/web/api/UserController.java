package me.j360.dubbo.web.api;

import lombok.extern.slf4j.Slf4j;
import me.j360.dubbo.api.model.domain.UserDO;
import me.j360.dubbo.api.model.param.user.UserDTO;
import me.j360.dubbo.api.service.UserService;
import me.j360.dubbo.base.model.result.DefaultResult;
import me.j360.dubbo.common.api.ApiResponse;
import me.j360.dubbo.common.api.ApiStatus;
import me.j360.dubbo.web.response.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Package: me.j360.dubbo.web.api
 * UserVo: min_xu
 * Date: 16/8/23 下午2:05
 * 说明：
 */


@Slf4j
@Controller
@RequestMapping("/api/user/")
public class UserController {


    @Autowired
    private UserService userService;

    @RequestMapping(value = "user/{id}", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponse getUser(@PathVariable Long id) {

        UserDTO dto = new UserDTO();

        DefaultResult<UserDO> result = userService.getUserInfo(dto);
        if (result.isSuccess()) {

            return ApiResponse.success(result.getData());
        } else if (1 == result.getCode()) {
            //假设定义1
            UserVo response = new UserVo();
            response.setId(id);
            return ApiResponse.success(response);
        }

        return ApiResponse.fail(ApiStatus.wrapperException(result));

    }
}
