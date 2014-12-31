/*
 * 文 件 名:  ResultDTO.java
 * 版    权:  YY Technologies Co., Ltd. Copyright 2012-2013,  All rights reserved
 * 描    述:  返回结果集
 * 修 改 人:  zhouliang
 * 修改时间:  2012-9-10
 */

package org.yy.framework.basedata.query;

import java.io.Serializable;
import java.util.List;

/**
 * 返回的列表结果集，主要使用在分页结果集的返回
 * 
 * @author zhouliang
 * @version [1.0, 2012-9-10]
 * @since [framework-basedata/1.0]
 */
public class ResultDto<T extends Object> implements Serializable {
    
    /**
     * 注释内容
     */
    private static final long serialVersionUID = -1238189601132676624L;
    
    private List<T> result;
    
    private Pagination pagination = new Pagination();
    
    public List<T> getResult() {
        return result;
    }
    
    public void setResult(List<T> result) {
        this.result = result;
    }
    
    public Pagination getPagination() {
        return pagination;
    }
    
    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }
    
}
