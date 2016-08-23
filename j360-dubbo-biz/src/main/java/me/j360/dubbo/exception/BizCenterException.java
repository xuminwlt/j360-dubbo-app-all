/**
 * Tmall.com Inc.
 * Copyright (c) 2008-2013 All Rights Reserved.
 */
package me.j360.dubbo.exception;

/**
 * 该模块的异常
 */
public class BizCenterException extends Exception {
    
	/* serialVersionUID: serialVersionUID */
	private static final long serialVersionUID = 481236416397652603L;

	public BizCenterException() {
        super();
    }

    public BizCenterException(String msg) {
        super(msg);
    }

    public BizCenterException(Throwable cause) {
        super(cause);
    }

    public BizCenterException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
