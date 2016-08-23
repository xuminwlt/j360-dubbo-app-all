package me.j360.dubbo.common.api;

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
public class ApiStatus {

    public static final int SUCCESS = 0;

    /**
     * SYSTEM + 通用异常
     */
    public static final int SY_API_REQUEST_PARAM_ERROR = 100001;

    public static final int SY_SERVICE_NOT_AVALABLE = 100002;


    /**
     * ACCOUNT
     */
    public static final int AC_ILLGUAL_ACCOUNT = 200001;


    /**
     * USER
     */
    public static final int US_ILLGUAL_ACCOUNT = 300001;


    /**
     * HOME(post/video/album)
     */
    public static final int HO_ILLGUAL_ACCOUNT = 400001;



    /**
     * MESSAGE(notice/im)
     */
    public static final int ME_ILLGUAL_ACCOUNT = 500001;






}