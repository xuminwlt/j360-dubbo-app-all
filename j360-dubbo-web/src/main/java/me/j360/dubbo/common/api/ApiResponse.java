package me.j360.dubbo.common.api;

import com.app.exception.UnCheckedWebException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.servlet.support.RequestContext;

import javax.servlet.http.HttpServletRequest;

@Slf4j
public class ApiResponse<D> extends BaseResponse {

    @Getter
    protected D data;

    public static Builder newBuilder() {
        return new Builder();
    }

    public static ApiResponse createResponse() {
        return newBuilder().data(null).status(ApiStatus.SUCCESS).error("").build();
    }

    public static <D> ApiResponse createResponse(D data) {
        return newBuilder().data(data).status(ApiStatus.SUCCESS).error("").build();
    }

    protected ApiResponse(D data, int status, String error) {
        super(status, error);
        this.data = data;
    }

    public static class Builder<D> extends BaseResponse.Builder {

        private D data;

        @Override
        protected Builder getThis() {
            return this;
        }

        @Override
        public ApiResponse build() {
            return new ApiResponse(data, status, error);
        }

        public Builder data(D data) {
            this.data = data;
            return this;
        }

        @Override
        public Builder status(Integer status) {
            this.status = status;
            return this;
        }
        @Override
        public Builder error(String error) {
            this.error = error;
            return this;
        }
    }


    /**
     * web端异常返回结果
     * @param exception
     * @param request
     * @return
     */
    public static ApiResponse createRestResponse(UnCheckedWebException exception, HttpServletRequest request) {
        Builder builder = newBuilder();
        if (null != exception) {
            builder.status(exception.getExceptionCode());
            builder.error(getMessage(String.valueOf(builder.getThis().status), request));
        }
        return builder.data(null).build();
    }

    /**
     * 参数绑定错误响应
     *
     * @param exception 异常
     * @return 响应
     */
    public static ApiResponse createRestResponse(BindException exception, HttpServletRequest request) {
        Builder builder = newBuilder();
        if (null != exception && exception.hasErrors()) {
            StringBuilder error = new StringBuilder("");
            for (ObjectError objectError : exception.getAllErrors()) {
                error.append(objectError.getDefaultMessage() + ";");
            }
            log.error(error.toString());
            builder.status(ApiStatus.SY_API_REQUEST_PARAM_ERROR);
            builder.error(getMessage(String.valueOf(builder.getThis().status),request));
        }
        return builder.data(null).build();
    }

    private static String getMessage(String code,HttpServletRequest request){
        RequestContext requestContext = new RequestContext(request);
        return requestContext.getMessage(String.valueOf(code));
    }

}
