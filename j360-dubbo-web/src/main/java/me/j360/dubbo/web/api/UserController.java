package me.j360.dubbo.web.api;

import lombok.extern.slf4j.Slf4j;
import me.j360.dubbo.common.api.ApiResponse;
import me.j360.dubbo.web.response.UserVo;
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



    @RequestMapping(value = "user/{id}", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponse getUser(@PathVariable Long id) {


        //返回订单信息给App
        UserVo response = new UserVo();
        response.setId(id);
        return ApiResponse.success(response);
    }
}
