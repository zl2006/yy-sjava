/*
 * 文 件 名:  DAOException.java
 * 版    权:  YY Technologies Co., Ltd. Copyright 1993-2012,  All rights reserved
 * 描    述:  数据访问层异常
 * 修 改 人:  zhouliang
 * 修改时间:  2012-9-11
 */
package org.yy.framework.basedata.exception;

import java.lang.RuntimeException;

/**
 * 数据访问层异常，运行时异常
 * 
 * @author zhouliang
 * @version [1.0, 2012-9-11]
 * @since [framework-basedata/1.0]
 */
public class DaoException extends RuntimeException {
    
    /**
    * 注释内容
    */
    private static final long serialVersionUID = -4306611193031135336L;
    
    /**
     * 异常编码
     */
    private String code;
    
    /**
     * 构造函数
     * @param code 异常编码
     * @param message 异常消息
     */
    public DaoException(String code, String message) {
        super(message);
        this.code = code;
    }
    
    /**
     * 构造函数
     * @param code 异常编码
     * @param message 异常消息
     * @param ex 异常
     */
    public DaoException(String code, String message, Throwable ex) {
        super(message, ex);
        this.code = code;
    }
    
    public String getCode() {
        return code;
    }
    
}
