package me.j360.dubbo.web.api;

import java.util.Map;

/**
 * Package: me.j360.dubbo.web.api
 * User: min_xu
 * Date: 16/8/23 下午2:05
 * 说明：
 */
public class UserController {

    @RequestMapping(value = "i18n", method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> i18n() {
        RequestContext requestContext = new RequestContext(request);
        Map<String,Object> result = Maps.newHashMap();
        result.put("result",requestContext.getMessage("hello"));
        result.put("e",requestContext.getMessage("100001"));
        return result;
    }

    @RequestMapping(value = "api", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponse<User> api() {
        RequestContext requestContext = new RequestContext(request);
        Ks3Stream stream = new Ks3Stream();
        stream.setCodec_type(requestContext.getMessage("hello"));
        stream.setR_frame_rate(requestContext.getMessage("100001"));

        return ApiResponse.createResponse(stream);
    }
    @RequestMapping(value = "api-page", method = RequestMethod.GET)
    @ResponseBody
    public ApiPageResponse<User> apiPage() {
        RequestContext requestContext = new RequestContext(request);
        User stream = new User();
        stream.setName(requestContext.getMessage("hello"));
        stream.setId(requestContext.getMessage("100001"));

        return ApiPageResponse.createResponse(1, Lists.newArrayList(stream));
    }


    @RequestMapping(value = "api-exception", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponse<String> apiException() {
        if(1==1) {
            throw new UnCheckedWebException(ApiStatus.SY_API_REQUEST_PARAM_ERROR, "错误的账号");
        }
        return ApiResponse.createResponse("");
    }


    @RequestMapping(value = "api-exception2", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponse<String> apiExceptio2() {
        return ApiResponse.createRestResponse(new UnCheckedWebException(ApiStatus.SY_API_REQUEST_PARAM_ERROR, "错误的账号"),request);
    }
}
