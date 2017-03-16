package me.j360.dubbo.base.model.query;

import java.io.Serializable;
import java.util.List;

/**
 * 类描述：通用分页模型
 * 
 * @author: zouwei@pajk.cn
 * @version: 2014年5月9日 下午5:18:10
 */
public class BaseQuery<T> implements Serializable {
	
	/* serialVersionUID: serialVersionUID */
	private static final long serialVersionUID = -1414220665147970278L;

	/** 默认分页大小 */
	private static final int DEFAULT_PAGE_SIZE = 20;
	
	/** 默认分页码 */
	private static final int DEFAULT_CURRENT_PAGE = 1;
	
	/** 当前页第一条数据在List中的位置,从0开始 */
	private Integer startRow = 0;
	
	/** 当前页  */
	private Integer currentPage = DEFAULT_CURRENT_PAGE;

	/** 每页的记录数 */
	private Integer pageSize = DEFAULT_PAGE_SIZE;
	
	/** 总页数 */
	private Long totalPage; // 总计有多少页
	
	/** 总记录数 */
	private Long totalCount;

	/** 查询结果集 */
	private List<T> list;
	
	
	public Integer getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(Integer currentPage) {
       this.currentPage = currentPage;   
	}

	public Integer getStartRow() {
		if(this.currentPage>0){
			startRow=(currentPage-1)*pageSize; 
		}
		return startRow;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Long getTotalPage() {
		return totalPage;
	}

	public Long getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Long totalCount) {
		this.totalCount=totalCount;   
        totalPage = totalCount % pageSize > 0 ? totalCount/pageSize+1:totalCount/pageSize;   
        startRow = (currentPage-1)*pageSize;   
	}

	public List<T> getList() {
		return list;
	}

	public void setList(List<T> list) {
		this.list = list;
	}
	
}
