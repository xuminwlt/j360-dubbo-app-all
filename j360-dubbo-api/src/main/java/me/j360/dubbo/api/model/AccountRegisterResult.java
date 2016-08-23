package me.j360.dubbo.model;

import me.j360.dubbo.model.base.BaseResult;

import java.io.Serializable;

/**
 *
 */
public class AccountRegisterResult extends BaseResult implements Serializable {
    private AccountRegisterResult(int returnCode, String msg) {
        super(returnCode, msg, false);
    }

    private AccountRegisterResult(int returnCode, String msg, boolean isSuccess) {
        super(returnCode, msg, isSuccess);
    }

    public static final int                 C_SUCCESS = 0000;
    public static final AccountRegisterResult SUCCESS   = new AccountRegisterResult(C_SUCCESS, "成功", true);

    public static final int                 C_PARAM_NULL = 0001;
    public static final AccountRegisterResult PARAM_NULL   = new AccountRegisterResult(C_PARAM_NULL, "输入参数错误:空参数");

    public static final int                 C_PARAM_STARTTIME_0 = 0002;
    public static final AccountRegisterResult PARAM_STARTTIME_0   = new AccountRegisterResult(C_PARAM_STARTTIME_0, "输入参数错误:开始时间为0");

    public static final int                 C_PARAM_ENDTIME_0 = 0003;
    public static final AccountRegisterResult PARAM_ENDTIME_0   = new AccountRegisterResult(C_PARAM_ENDTIME_0, "输入参数错误:结束时间为0");

    public static final int                 C_PARAM_DATA_OVERLAP = 0004;
    public static final AccountRegisterResult PARAM_DATA_OVERLAP         = new AccountRegisterResult(C_PARAM_DATA_OVERLAP, "输入日期交叉");


}
