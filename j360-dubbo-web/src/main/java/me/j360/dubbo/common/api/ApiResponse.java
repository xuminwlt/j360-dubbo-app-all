package me.j360.dubbo.common.api;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.servlet.support.RequestContext;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
public class ApiResponse<D> extends Response {

    @Getter
    private Object data;

    private ApiResponse() {
    }

    private ApiResponse(D data) {
        this.data = data;
    }

    private ApiResponse(List<D> list) {
        this.data = new ListData<D>(list);
    }

    private ApiResponse(Integer total, List<D> list) {
        this.data = new ListData<D>(total, list);
    }

    /**
     * 创建成功响应
     *
     * @return 响应
     */
    public static ApiResponse createRestResponse() {
        return new ApiResponse();
    }

    /**
     * 创建包含数据的响应
     *
     * @param data 数据
     * @param <D>  类型
     * @return 响应
     */
    public static <D> ApiResponse createRestResponse(D data) {
        return new ApiResponse(data);
    }

    /**
     * 创建包含列表数据的响应
     *
     * @param list 列表数据
     * @param <D>  类型
     * @return 响应
     */
    public static <D> ApiResponse createRestResponse(List<D> list) {
        return new ApiResponse(list);
    }

    /**
     * 创建包含列表数据的响应
     *
     * @param total 总条数
     * @param list  列表数据
     * @param <D>   类型
     * @return 响应
     */
    public static <D> ApiResponse createRestResponse(Integer total, List<D> list) {
        return new ApiResponse(total, list);
    }


    /**
     * web端异常返回结果
     * @param exception
     * @param request
     * @return
     */
    public static ApiResponse createRestResponse(UnCheckedWebException exception, HttpServletRequest request) {
        ApiResponse restResponse = new ApiResponse();
        if (null != exception) {
            restResponse.setStatus(exception.getExceptionCode());
            restResponse.setError(getMessage(String.valueOf(restResponse.getStatus()),request));
        }
        return restResponse;
    }

    /**
     * 参数绑定错误响应
     *
     * @param exception 异常
     * @return 响应
     */
    public static ApiResponse createRestResponse(BindException exception, HttpServletRequest request) {
        ApiResponse restResponse = new ApiResponse();
        if (null != exception && exception.hasErrors()) {
            StringBuilder error = new StringBuilder("");
            for (ObjectError objectError : exception.getAllErrors()) {
                error.append(objectError.getDefaultMessage() + ";");
            }
            log.error(error.toString());
            restResponse.setStatus(ApiStatus.SY_API_REQUEST_PARAM_ERROR);
            restResponse.setError(getMessage(String.valueOf(restResponse.getStatus()),request));
        }

        return restResponse;
    }

    private static String getMessage(String code,HttpServletRequest request){
        RequestContext requestContext = new RequestContext(request);
        return requestContext.getMessage(String.valueOf(code));
    }

    private static class ListData<D> {

        @Getter
        private Integer total;

        @Getter
        private List<D> list;

        private ListData(List<D> list) {
            this(list.size(), list);
        }

        private ListData(Integer total, List<D> list) {
            this.total = total;
            this.list = list;
        }
    }

}
