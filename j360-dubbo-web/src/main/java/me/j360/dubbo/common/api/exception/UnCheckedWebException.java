package me.j360.dubbo.common.api.exception;

/**
 * Package: com.app.exception.web
 * UserVo: min_xu
 * Date: 16/8/19 下午3:01
 * 说明：非受检的web api异常，只能在war层面使用，不走rpc，在异常收集器中code会转化为api code
 */
public class UnCheckedWebException extends RuntimeException {

    private static final long serialVersionUID = -6438755184394143413L;

    protected int exceptionCode = -1;

    public int getExceptionCode() {
        return this.exceptionCode;
    }

    public UnCheckedWebException(int exceptionCode) {
        super();
        this.exceptionCode = exceptionCode;
    }

    public UnCheckedWebException(int exceptionCode, String message) {
        super(message);
        this.exceptionCode = exceptionCode;
    }

    public UnCheckedWebException(int exceptionCode, String message, Throwable cause) {
        super(message, cause);
        this.exceptionCode = exceptionCode;
    }
}