package me.j360.dubbo.api.service;

/**
 * Package: me.j360.dubbo.api
 * User: min_xu
 * Date: 16/8/23 下午2:39
 * 说明：
 */
public class DubboReturnCode {

    private DubboReturnCode(String desc, int code) {
        super(desc, code);
    }

    private DubboReturnCode(int code, DubboReturnCode display) {
        super(code, display);
    }

    public static final int            C_CREDIT_NOT_ENOUGH = 3510020;
    public static final DubboReturnCode CREDIT_NOT_ENOUGH   = new DubboReturnCode("积分不足", C_CREDIT_NOT_ENOUGH);

    public static final int            C_SCENARIO_NOT_OPEN = 3510030;
    public static final DubboReturnCode SCENARIO_NOT_OPEN   = new DubboReturnCode("关卡没有开启", C_SCENARIO_NOT_OPEN);

    public static final int            C_NOT_PART_ACTIVITY = 3510035;
    public static final DubboReturnCode NOT_PART_ACTIVITY   = new DubboReturnCode("你还没有参加此活动，不能挑战此关卡", C_NOT_PART_ACTIVITY);

    public static final int            C_SCENARIO_RUNNING = 3510040;
    public static final DubboReturnCode SCENARIO_RUNNING   = new DubboReturnCode("您已经参加次关卡了，不能重复参加", C_SCENARIO_RUNNING);

    public static final int            C_SCENARIO_SUCCESS_ClOSE = 3510045;
    public static final DubboReturnCode SCENARIO_SUCCESS_ClOSE   = new DubboReturnCode("关卡已过关", C_SCENARIO_SUCCESS_ClOSE);

    public static final int            C_ACTIVITY_NOT_FOUND = 3510050;
    public static final DubboReturnCode ACTIVITY_NOT_FOUND   = new DubboReturnCode("活动不存在或已过期", C_ACTIVITY_NOT_FOUND);

    public static final int            C_ACTIVITY_HAVE_CONCERN = 3510060;
    public static final DubboReturnCode ACTIVITY_HAVE_CONCERN   = new DubboReturnCode("活动已关注，去体验一下吧！", C_ACTIVITY_HAVE_CONCERN);

    public static final int            C_ACTIVITY_UNKNOW_ERROR = 3510065;
    public static final DubboReturnCode ACTIVITY_UNKNOW_ERROR   = new DubboReturnCode("活动未知错误", C_ACTIVITY_UNKNOW_ERROR);

    public static final int            C_NO_FAMILY_RELATION = 3700010;
    public static final DubboReturnCode NO_FAMILY_RELATION   = new DubboReturnCode("不存在关注关系", C_NO_FAMILY_RELATION);

    public static final int            C_EXIST_FAMILY_RELATION = 3700020;
    public static final DubboReturnCode EXIST_FAMILY_RELATION   = new DubboReturnCode("已存在关注关系", C_EXIST_FAMILY_RELATION);

    public static final int            C_BIRTHDAY_QUESTION_ERROR = 3700030;
    public static final DubboReturnCode BIRTHDAY_QUESTION_ERROR   = new DubboReturnCode("生日问题回答错误", C_BIRTHDAY_QUESTION_ERROR);

    public static final int            C_FINALEVALUATION_ANSWER_ERROR = 3700040;
    public static final DubboReturnCode FINALEVALUATION_ANSWER_ERROR   = new DubboReturnCode("终极评测答案提交错误", C_FINALEVALUATION_ANSWER_ERROR);

    public static final int            C_WEATHER_API_ERROR = 3000010;
    public static final DubboReturnCode WEATHER_API_ERROR   = new DubboReturnCode("天气获取异常", C_WEATHER_API_ERROR);

    public static final int            C_PARSE_CITYCODE_ERROR = 3000020;
    public static final DubboReturnCode PARSE_CITYCODE_ERROR   = new DubboReturnCode("无法获取对应城市城市代码", C_PARSE_CITYCODE_ERROR);

    public static final int            C_PARAMETER_NULL_POINTER_EXCEPTION_ERROR = 3100010;
    public static final DubboReturnCode PARAMETER_NULL_POINTER_EXCEPTION_ERROR   = new DubboReturnCode("参数空指针错误",
            C_PARAMETER_NULL_POINTER_EXCEPTION_ERROR);
    public static final int            C_REMOTE_KNOWLEDGE_ERROR                 = 3100020;
    public static final DubboReturnCode REMOTE_KNOWLEDGE_ERROR                   = new DubboReturnCode("远程知识库连接错误", C_REMOTE_KNOWLEDGE_ERROR);

    public static final int            C_REMOTE_KNOWLEDGE_DETAIL_ERROR = 3100030;
    public static final DubboReturnCode REMOTE_KNOWLEDGE_DETAIL_ERROR   = new DubboReturnCode("远程知识库详情接口连接错误", C_REMOTE_KNOWLEDGE_DETAIL_ERROR);

    public static final int            C_REMOTE_SUGGESTION_ERROR = 3100040;
    public static final DubboReturnCode REMOTE_SUGGESTION_ERROR   = new DubboReturnCode("远程suggestion接口连接错误", C_REMOTE_SUGGESTION_ERROR);

    public static final int            C_USER_NOT_EXIST_ERROR = 3100050;
    public static final DubboReturnCode USER_NOT_EXIST_ERROR   = new DubboReturnCode("用户不存在", C_USER_NOT_EXIST_ERROR);

    public static final int            C_NOT_AUTHORIZATION_ERROR = 3100060;
    public static final DubboReturnCode NOT_AUTHORIZATION_ERROR   = new DubboReturnCode("该用户无权修改本条数据", C_NOT_AUTHORIZATION_ERROR);

    public static final int            C_PARAMETER_PARSE_ILLEGER_ERROR = 3100070;
    public static final DubboReturnCode PARAMETER_PARSE_ILLEGER_ERROR   = new DubboReturnCode("数据格式化转化非法错误", C_PARAMETER_PARSE_ILLEGER_ERROR);

    public static final int            C_REMOTE_HOT_KEY_ERROR = 3100080;
    public static final DubboReturnCode REMOTE_HOT_KEY_ERROR   = new DubboReturnCode("远程热点关键词连接错误", C_REMOTE_HOT_KEY_ERROR);

    public static final int            C_SYSTEM_ERROR = 3500010;
    public static final DubboReturnCode SYSTEM_ERROR   = new DubboReturnCode("系统错误", C_SYSTEM_ERROR);

    public static final int            C_ILLEGAL_PARAMS_ERROR = 3500020;
    public static final DubboReturnCode ILLEGAL_PARAMS_ERROR   = new DubboReturnCode("参数错误或无效", C_ILLEGAL_PARAMS_ERROR);

    public static final int            C_TAIR_FAULT_ERROR = 3500030;
    public static final DubboReturnCode TAIR_FAULT_ERROR   = new DubboReturnCode("系统繁忙", C_TAIR_FAULT_ERROR);

    public static final int            C_CONSULTING_STATUS_ERROR = 3600010;
    public static final DubboReturnCode CONSULTING_STATUS_ERROR   = new DubboReturnCode("健康管家有点累了，小憩一下，请退出应用后重试", C_CONSULTING_STATUS_ERROR);

    public static final int            C_NO_USER_IN_QUEUE_ERROR = 3600011;
    public static final DubboReturnCode NO_USER_IN_QUEUE_ERROR   = new DubboReturnCode("队列中没有有效用户", C_NO_USER_IN_QUEUE_ERROR);

    public static final int            C_NO_DOCTOR_IN_SERVICE_ERROR = 3600013;
    public static final DubboReturnCode NO_DOCTOR_IN_SERVICE_ERROR   = new DubboReturnCode("抱歉，医生已下班，换个医生试试吧！", C_NO_DOCTOR_IN_SERVICE_ERROR);

    public static final int            C_USER_STATUS_ERROR = 3600012;
    public static final DubboReturnCode USER_STATUS_ERROR   = new DubboReturnCode("用户状态错误", C_USER_STATUS_ERROR);

    public static final int            C_SEND_MSG_ERROR = 3600014;
    public static final DubboReturnCode SEND_MSG_ERROR   = new DubboReturnCode("消息不能送达", C_SEND_MSG_ERROR);

    public static final int            C_IM_JOIN_GROUP_USER_EXIST = 3600015;
    public static final DubboReturnCode IM_JOIN_GROUP_USER_EXIST   = new DubboReturnCode("用户已在聊天室", C_IM_JOIN_GROUP_USER_EXIST);

    public static final int            C_IM_GROUP_NOT_EXIST = 3600016;
    public static final DubboReturnCode IM_GROUP_NOT_EXIST   = new DubboReturnCode("聊天室不存在", C_IM_GROUP_NOT_EXIST);

    public static final int            C_IM_GROUP_USER_LIMIT_ERROR = 3600017;
    public static final DubboReturnCode IM_GROUP_USER_LIMIT_ERROR   = new DubboReturnCode("聊天室人数达到上限", C_IM_GROUP_USER_LIMIT_ERROR);

    public static final int            C_IM_JOIN_GROUP_UNKNOWN_ERROR = 3600018;
    public static final DubboReturnCode IM_JOIN_GROUP_UNKNOWN_ERROR   = new DubboReturnCode("加入聊天室发生未知错误", C_IM_JOIN_GROUP_UNKNOWN_ERROR);

    public static final int            C_PARAMS_NULL_ERROR = 3600020;
    public static final DubboReturnCode PARAMS_NULL_ERROR   = new DubboReturnCode("参数为null或不合法", C_PARAMS_NULL_ERROR);

    public static final int            C_TRANSFER_TREATMENT_PARAMTER_ERROR = 3600021;
    public static final DubboReturnCode TRANSFER_TREATMENT_PARAMTER_ERROR   = new DubboReturnCode("转诊参数错误", C_TRANSFER_TREATMENT_PARAMTER_ERROR);

    public static final int            C_UNKOWN_ITEM = 3600030;
    public static final DubboReturnCode UNKOWN_ITEM   = new DubboReturnCode("未知商品", C_UNKOWN_ITEM);

    public static final int            C_JOINACTIVITY_ERROR = 3600100;
    public static final DubboReturnCode JOINACTIVITY_ERROR   = new DubboReturnCode("参加活动出错", C_JOINACTIVITY_ERROR);

    public static final int            C_SIMS_USER_NICK_REPEAT = 3601000;
    public static final DubboReturnCode SIMS_USER_NICK_REPEAT   = new DubboReturnCode("用户昵称重复", C_SIMS_USER_NICK_REPEAT);

    public static final int            C_SIMS_GET_RESOURCE_ERROR = 3604000;
    public static final DubboReturnCode SIMS_GET_RESOURCE_ERROR   = new DubboReturnCode("获取通用资源失败", C_SIMS_GET_RESOURCE_ERROR);


    public static final int            C_HEALTH_PICTURE_EXIST = 3700010;
    public static final DubboReturnCode HEALTH_PICTURE_EXIST   = new DubboReturnCode("此健康档案图片已经存在", C_HEALTH_PICTURE_EXIST);

    /**
     * 系统级错误>3990001
     */
    protected static final int            C_SIMS_INTERNAL_SERVER_ERROR = 3990001;
    public static final    DubboReturnCode SIMS_INTERNAL_SERVER_ERROR   = new DubboReturnCode("SIMS系统内部异常", C_SIMS_INTERNAL_SERVER_ERROR);
    
}
