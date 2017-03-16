package me.j360.dubbo.base.model.result;

public class PageResultSupport extends ResultSupport{
	
	private int recordCount;
	
	private int recordSize;
	
	private int pageNo;
	
	private int pageSize;
	
	public PageResultSupport(){
		super();
	}
	
	public PageResultSupport(boolean success,int resultCode,String resultMsg){
	    super();
	}
	
	public PageResultSupport(int pageNo,int pageSize,int recordCount,int recordSize){
		super();
		this.pageNo = pageNo;
		this.pageSize = pageSize;
		this.recordCount = recordCount;
		this.recordSize = recordSize;
	}
	
	public int getRecordCount() {
		return recordCount;
	}
	public void setRecordCount(int recordCount) {
		this.recordCount = recordCount;
	}
	public Integer getRecordSize() {
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
	
}
