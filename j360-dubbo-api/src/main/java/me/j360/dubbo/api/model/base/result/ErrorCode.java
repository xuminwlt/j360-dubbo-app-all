package me.j360.dubbo.api.model.base.result;


public class ErrorCode {
	public static final ErrorCode SYSTEM_ERROR = new ErrorCode(-1,"系统错误");
	public static final ErrorCode ITEM_NOT_FOUND = new ErrorCode(1,"商品未找到");
	
	public static final ErrorCode DB_ERROR = new ErrorCode(1001,"数据库错误");
	public static final ErrorCode SYS_ERROR= new ErrorCode(1002,"系统错误");
	public static final ErrorCode PARAM_ERROR=new ErrorCode(1003,"参数异常:");
	public static final ErrorCode GOODS_FIND_ERROR= new ErrorCode(3,"goods can't find!");
	public static final ErrorCode GOODS_FIND_SIZE_ERROR= new ErrorCode(4,"size of goods is > 1");

    public static final ErrorCode CARD_NOT_FOUND = new ErrorCode(5,"卡不存在");
    public static final ErrorCode CARD_NOT_AVAILABLE = new ErrorCode(6,"卡不可用");

    public static final ErrorCode USER_SERVE_BAL_NOT_ENOUGH = new ErrorCode(7,"用户剩余次数不足");
    
    public static final ErrorCode VOUCHER_NOT_FOUND = new ErrorCode(8,"券不存在");
    public static final ErrorCode VOUCHER_NOT_AVAILABLE = new ErrorCode(9,"券不可用");
    public static final ErrorCode VOUCHER_ENABLE_NOT_UPDATE = new ErrorCode(10,"券已启用，不能更改");
    public static final ErrorCode VOUCHER_BIND=new ErrorCode(11, "卡已经被绑定，无法再次绑定");
    public static final ErrorCode VOUCHER_PASS_ERROR=new ErrorCode(12, "券密码错误");
	
	
	private int errorCode;
	
	private String errorMsg;
	
	public ErrorCode(int errorCode,String errorMsg){
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
