package me.j360.dubbo.common.api;

import me.j360.dubbo.base.constant.BaseApiStatus;
import me.j360.dubbo.base.model.result.BaseResultSupport;
import me.j360.dubbo.common.api.exception.UnCheckedWebException;

/**
 * Package: com.app.constant
 * UserVo: min_xu
 * Date: 16/8/19 上午11:08
 * 说明：扩展性业务API CODE
 *
 */
public class ApiStatus extends BaseApiStatus{


    /**
     * ACCOUNT
     */
    public static final int AC_ILLGUAL_ACCOUNT = 20001;


    /**
     * USER
     */
    public static final int US_ILLGUAL_ACCOUNT = 30001;


    public static UnCheckedWebException wrapperException(BaseResultSupport resultSupport) {
        int code = resultSupport.getCode();
        String msg = resultSupport.getMsg();
        return new UnCheckedWebException(code,msg);
    }


    public static UnCheckedWebException wrapperException(RuntimeException ex) {
        return new UnCheckedWebException(SYST_SYSTEM_ERROR,FAIL_MESSAGE,ex);
    }


}