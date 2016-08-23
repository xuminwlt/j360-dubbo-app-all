package me.j360.dubbo.api.model.base.result;
/*import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;*/

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

/**
 * 
	 * 类描述：结果基类，用于返回外部系统调用结果
	 * @author: zouwei@pajk.cn
	 * @version: 2014年5月5日 下午11:05:56
 */
public class ResultDO<T> implements Serializable {
	
	/* serialVersionUID: serialVersionUID */
	private static final long serialVersionUID = 5927407897463583643L;

	/** 是否成功，默认失败 */
    private boolean           success          = false;
    
    /** 消息code */
    private int            errCode;
    /** 返回消息 */
    private String            errMsg;

    /** 返回结果封装器 */
    private T                 module;

    /**
     * Getter method for property <tt>success</tt>.
     * 
     * @return property value of success
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * Setter method for property <tt>success</tt>.
     * 
     * @param success
     *            value to be assigned to property success
     */
    public void setSuccess(boolean success) {
        this.success = success;
    }

    /**
     * Getter method for property <tt>errMsg</tt>.
     * 
     * @return property value of errMsg
     */
    public String getErrMsg() {
        return errMsg;
    }

    /**
     * Setter method for property <tt>errMsg</tt>.
     * 
     * @param errMsg
     *            value to be assigned to property errMsg
     */
    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    /**
     * Getter method for property <tt>module</tt>.
     * 
     * @return property value of module
     */
    public T getModule() {
        return module;
    }

    /**
     * Setter method for property <tt>module</tt>.
     * 
     * @param module
     *            value to be assigned to property module
     */
    public void setModule(T module) {
        this.module = module;
    }

	public int getErrCode() {
		return errCode;
	}

	public void setErrCode(int errCode) {
		this.errCode = errCode;
	}

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

}