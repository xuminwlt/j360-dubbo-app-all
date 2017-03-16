package me.j360.dubbo.api.model.result.user;


import me.j360.dubbo.api.constant.ErrorCode;
import me.j360.dubbo.base.model.result.ResultSupport;

import java.util.Map;


public class UserBindResult extends ResultSupport {

	private static final long serialVersionUID = -3325209355672286185L;
	private Map<Long, ErrorCode> errorMap;
	/**
	 * @return the errorMap
	 */
	public Map<Long, ErrorCode> getErrorMap() {
		return errorMap;
	}
	/**
	 * @param errorMap the errorMap to set
	 */
	public void setErrorMap(Map<Long, ErrorCode> errorMap) {
		this.errorMap = errorMap;
	}
}
