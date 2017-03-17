package me.j360.dubbo.base.model.result;

import java.io.Serializable;

public class PageDO<T> implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private int recordCount;
	
	private int recordSize;
	
	private int pageNo;
	
	private int pageSize;
	
	private T data;

	public int getRecordCount() {
		return recordCount;
	}

	public void setRecordCount(int recordCount) {
		this.recordCount = recordCount;
	}

	public int getRecordSize() {
		return recordSize;
	}

	public void setRecordSize(int recordSize) {
		this.recordSize = recordSize;
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}
}
