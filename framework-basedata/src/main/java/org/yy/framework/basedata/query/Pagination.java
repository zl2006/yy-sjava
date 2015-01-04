/*
* 文 件 名:  Pagination.java
* 版    权:  YY Technologies Co., Ltd. Copyright 2012-2013,  All rights reserved
* 描    述:  分页信息
* 修 改 人:  zhouliang
* 修改时间:  2012-5-24
*/
package org.yy.framework.basedata.query;

import java.io.Serializable;

/**
 * 
 * 分页信息
 * 
 * @author  zhouliang
 * @version  [1.0, 2012-5-28]
 * @since  [framework-basedata/1.0]
 */
public class Pagination implements Serializable {
    
    private static final long serialVersionUID = 3004917241397351782L;
    
    // 总共的数据量
    private int total = 0;
    
    // 每页显示多少条
    private int pageSize = 10;
    
    // 当前是第几页
    private int index = 0;
    
    public Pagination() {
    }
    
    /**
     * param index 页下标，从0开始计数
     */
    public Pagination(int index) {
        this.index = index;
    }
    
    /**
    * @param pageSize 页大小，也称返回记录数
    * @param index 页下标，从0开始计数
    */
    public Pagination(int pageSize, int index) {
        this.pageSize = pageSize;
        this.index = index;
    }
    
    public int getTotal() {
        return total;
    }
    
    public void setTotal(int total) {
        this.total = total;
    }
    
    public int getPageSize() {
        return pageSize;
    }
    
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
    
    /**
     * 总页面
     * 
     * @return
     */
    public int getTotalPage() {
        int totalPage = total / pageSize;
        return totalPage + (total % pageSize == 0 ? 0 : 1);
    }
    
    /**
     * 查询当前页的起始记录数
     * 
     * @return
     */
    public int getStartNum() {
        return (index) * pageSize;
    }
    
    public int getEndNum() {
        return (index + 1) * pageSize;
    }
    
    public int getIndex() {
        return index;
    }
    
    public void setIndex(int index) {
        this.index = index;
    }
    
    @Override
    public String toString() {
        return "Pagination [total=" + total + ", pageSize=" + pageSize + ", index=" + index + "]";
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + index;
        result = prime * result + pageSize;
        result = prime * result + total;
        return result;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Pagination other = (Pagination)obj;
        if (index != other.index)
            return false;
        if (pageSize != other.pageSize)
            return false;
        if (total != other.total)
            return false;
        return true;
    }
}
