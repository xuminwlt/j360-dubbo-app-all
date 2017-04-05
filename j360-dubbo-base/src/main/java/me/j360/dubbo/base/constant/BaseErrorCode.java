package me.j360.dubbo.base.constant;

/**
 * Package: me.j360.dubbo.base.api
 * User: min_xu
 * Date: 2017/3/15 下午8:04
 * 说明：
 */
public abstract class BaseErrorCode {

    public static int SUCCESS_CODE = 0;

    private int errorCode;

    private String errorMsg;

    public BaseErrorCode(int errorCode, String errorMsg){
        this.errorCode = errorCode;
        this.errorMsg  = errorMsg;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
