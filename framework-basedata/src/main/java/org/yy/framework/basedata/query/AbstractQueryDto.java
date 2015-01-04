/*
* 文 件 名:  AbstractQueryDTO.java
* 版    权:  YY Technologies Co., Ltd. Copyright 2012-2013,  All rights reserved
* 描    述:  分页查询时，数据传输对象基类
* 修 改 人:  zhouliang
* 修改时间:  2012-5-24
*/
package org.yy.framework.basedata.query;

import java.io.Serializable;

import com.google.code.ssm.api.CacheKeyMethod;

/**
 * 
 * 分页查询时或其它复合查询时，查询条件的基类
 * 
 * @author  zhouliang
 * @version  [1.0, 2012-5-28]
 * @since  [framework-basedata/1.0]
 */
public abstract class AbstractQueryDto implements Serializable {
    
    private static final long serialVersionUID = 5358612476304143534L;
    
    /**
     * 分页条件
     */
    private Pagination pagination = new Pagination();
    
    public Pagination getPagination() {
        return pagination;
    }
    
    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }
    
    @Override
    public String toString() {
        return "AbstractQueryDTO [pagination=" + pagination + "]";
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((pagination == null) ? 0 : pagination.hashCode());
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
        AbstractQueryDto other = (AbstractQueryDto)obj;
        if (pagination == null) {
            if (other.pagination != null)
                return false;
        }
        else if (!pagination.equals(other.pagination))
            return false;
        return true;
    }
    
    /**
     * 列表查询时，此方法的返回值将做为缓存的主键
     */
    @CacheKeyMethod
    public String cacheMethod() {
        return String.valueOf(this.hashCode());
    }
    
}
