package me.j360.dubbo.common.api;

import me.j360.dubbo.common.api.exception.UnCheckedWebException;
import me.j360.dubbo.common.api.exception.ValidationException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

/**
 * Package: me.j360.dubbo.common.api
 * UserVo: min_xu
 * Date: 16/8/23 下午2:05
 * 说明：TODO 整合web异常
 */
public abstract class BaseController {



    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ApiResponse exHandler(Exception e) {
        return createExResult(new RuntimeException(e));
    }

    @ExceptionHandler({ServletRequestBindingException.class,
            HttpMediaTypeNotAcceptableException.class,
            HttpMediaTypeNotSupportedException.class,
            HttpMessageNotReadableException.class,
            HttpRequestMethodNotSupportedException.class,
            MissingServletRequestParameterException.class,
            MissingServletRequestPartException.class,
            TypeMismatchException.class
    })
    @ResponseBody
    public ApiResponse ServletRequestBindingExHandler(Exception ex) {
        return createExResult(new ValidationException(ex));
    }

    /**
     * 数据绑定异常处理
     *
     * @param bindEx 数据绑定异
     * @return Result
     */
    @ExceptionHandler(BindException.class)
    @ResponseBody
    public ApiResponse bindExHandler(BindException bindEx) {
        return createExResult(new ValidationException(bindEx, bindEx.getBindingResult()));
    }

    /**
     * 方法参数无效异常处理
     *
     * @param methodArgumentNotValidEx 方法参数无效异常
     * @return Result
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ApiResponse methodArgumentNotValidExHandler(MethodArgumentNotValidException methodArgumentNotValidEx) {
        return createExResult(new ValidationException(methodArgumentNotValidEx, methodArgumentNotValidEx.getBindingResult()));
    }

    /**
     * 非检测异常处理
     *
     * @param unCheckedEx 非检测异常
     * @return Result
     */
    @ExceptionHandler(UnCheckedWebException.class)
    @ResponseBody
    public ApiResponse unCheckedExHandler(UnCheckedWebException unCheckedEx) {
        return createExResult(unCheckedEx);
    }


    /**
     * 创建异常结果
     *
     * @param unCheckedEx 异常
     * @return Result
     */
    public ApiResponse createExResult(RuntimeException unCheckedEx) {
        Integer statusCode = getStatusCode(unCheckedEx);

        return ApiResponse.fail(ApiStatus.wrapperException(unCheckedEx));
    }

    /**
     * 获取状态码
     *
     * @param unCheckedEx 异常
     * @return 状态码
     */
    private Integer getStatusCode(RuntimeException unCheckedEx) {
        Integer statusCode = doGetStatusCode(unCheckedEx);
        if (statusCode == null) {
            //statusCode = getStatusCode(unCheckedEx, ApiStatus.class);
        }
        return statusCode;
    }

    /**
     * 获取状态码
     *
     * @param unCheckedEx 异常
     * @return 状态码
     */
    public abstract Integer doGetStatusCode(RuntimeException unCheckedEx);



}
