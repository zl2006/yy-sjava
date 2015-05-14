/*
 * 文 件 名:  AbstractIbatisDAO.java
 * 版    权:  YY Technologies Co., Ltd. Copyright 1993-2012,  All rights reserved
 * 描    述:  ibatis的数据库操作ＤＡＯ
 * 修 改 人:  zhouliang
 * 修改时间:  2012-9-4
 */
package org.yy.framework.base.dao;

import java.sql.Connection;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.yy.framework.basedata.query.AbstractQueryDto;
import org.yy.framework.basedata.query.ResultDto;

/**
 * ibatis的数据库操作ＤＡＯ
 * 
 * @author zhouliang
 * @version [1.0, 2012-9-4]
 * @since [framework-base/1.0]
 */
public abstract class AbstractMyBatisDao {
    
    // 分页时查记录数总和的SQL尾部命名
    private final String PAGINATION_SQL_SUFFIX = "_PAGE_COUNT";
    
    //默认版本控制KEY
    public static final String V_KEY = "V";
    
    //数据库查询
    protected SqlSession sqlSession;
    
    /**
     * 查询数据列表,并进行分页处理.SQL命名规则要符合：查总条数的SQL命名=查数据的SQL命名+_PAGE_COUNT
     * 
     * @param sqlName Ibatis中定义的SQL名称
     * @param condition 查询条件
     * @return  数据列表
     */
    @SuppressWarnings("unchecked")
    protected <T extends Object> ResultDto<T> findBypagination(String sqlName, AbstractQueryDto condition) {
        ResultDto<T> rs = new ResultDto<T>();
        rs.setResult((List<T>)sqlSession.selectList(sqlName, condition));
        if (condition != null && condition.getPagination() != null) {
            rs.setPagination(condition.getPagination());
            rs.getPagination().setTotal((Integer)sqlSession.selectOne(sqlName + PAGINATION_SQL_SUFFIX, condition));
        }
        return rs;
    }
    
    /**
     * 查询数据列表, 不处理分页
     * 
     * @param sqlName Ibatis中定义的SQL名称
     * @param condition 查询条件
     * @return  数据列表
     */
    @SuppressWarnings("unchecked")
    protected <T extends Object> ResultDto<T> findBypagination(String sqlName, Object condition) {
        ResultDto<T> rs = new ResultDto<T>();
        rs.setResult((List<T>)sqlSession.selectList(sqlName, condition));
        return rs;
    }
    
    /**
     * 多条件查询单个元素
     * 
     * @param sqlName Ibatis中定义的SQL名称
     * @param condition 查询条件
     * @return  数据列表
     */
    protected <T> T findByUniqe(String sqlName, Object condition) {
        List<T> rs = sqlSession.selectList(sqlName, condition);
        if (rs == null || rs.size() == 0) {
            return null;
        }
        return rs.get(0);
    }
    
    /**
     * 获取数据库连接
     * 
     * @return 数据库连接
     */
    public Connection getConnection() {
        return sqlSession.getConnection();
    }
    
    /**
    * @param 对sqlSession进行赋值
    */
    public void setSqlSession(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }
    
}
