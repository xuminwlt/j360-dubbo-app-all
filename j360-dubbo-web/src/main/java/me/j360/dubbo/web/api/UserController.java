package me.j360.dubbo.web.api;

import lombok.extern.slf4j.Slf4j;
import me.j360.dubbo.common.api.ApiResponse;
import me.j360.dubbo.web.request.AddUserRequest;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
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


    /*@Autowired
    private UserService userService;

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
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

    }*/


    @RequestMapping(value = "add", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponse addUser(@Validated AddUserRequest request) {

        return ApiResponse.success();

    }
}
