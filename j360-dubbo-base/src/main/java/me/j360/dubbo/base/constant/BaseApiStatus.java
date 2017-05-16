package me.j360.dubbo.base.constant;

/**
 * Package: com.app.constant
 * User: min_xu
 * Date: 16/8/19 上午11:08
 * 说明：该类原则上只能由web端调用（返回时需要结合properties文件加上相应的message信息）
 *
 * Web API的返回值定义，使用静态属性定义，具体描述信息使用properties文件映射
 *      code长度为标准6位长度，新老版本兼容使用逐步替换策略
 *      每个模块的code由该模块开发人员维护
 *
 */
public abstract class BaseApiStatus {


    public static final int SUCCESS = 0;

    public static final String FAIL_MESSAGE = "系统异常...";


    public static final int SYST_SYSTEM_ERROR = 10000;
    public static final int SYST_SERVICE_UNAVAILABLE = 10001;
    public static final int SYST_ILLEGAL_REQUEST = 10002;
    public static final int SYST_INVALID_WEIBO_USER = 10003;

    public static final int SYST_NOT_SUPPORT_HTTP_METHOD = 10006;
    public static final int SYST_IP_REQUESTS_OUT_OF_RATE_LIMIT = 10007;
    public static final int SYST_USER_REQUESTS_OUT_OF_RATE_LIMIT = 10008;

    public static final int SYST_VERSION_NOT_SUPPORT = 10010;
    public static final int SYST_VERSION_TOO_LOW = 10011;





}