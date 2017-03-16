package me.j360.dubbo.base.exception;

/**
 * Package: me.j360.dubbo.base.exception
 * User: min_xu
 * Date: 16/8/23 下午2:01
 * 说明：
 */
public class ServiceException extends RuntimeException{
    private static final long serialVersionUID = -6438755184394143413L;

    protected int exceptionCode = -1;

    public int getExceptionCode() {
        return this.exceptionCode;
    }

    public ServiceException(int exceptionCode,String message) {
        super(message);
        this.exceptionCode = exceptionCode;
    }

    public ServiceException(int exceptionCode,String message,Throwable cause) {
        super(message, cause);
        this.exceptionCode = exceptionCode;
    }
}
