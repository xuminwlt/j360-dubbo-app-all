package me.j360.dubbo.api.model.base.param;

import java.io.Serializable;

/**
 * Created by menhaihao372 on 2014/7/24.
 *
 */
public class PageQuery implements Serializable {
    private static final long serialVersionUID = -7507646548498597628L;
    private int pageSize = 15;
    private int pageNo = 1;

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getStartRow() {
        return (pageNo - 1) * pageSize;
    }
}
