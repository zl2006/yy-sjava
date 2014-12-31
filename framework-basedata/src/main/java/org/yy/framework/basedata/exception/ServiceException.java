/*
* 文 件 名:  ServiceException.java
* 版    权:  YY Technologies Co., Ltd. Copyright 1993-2012,  All rights reserved
* 描    述:  服务层异常
* 修 改 人:  zhouliang
* 修改时间:  2012-9-11
*/
package org.yy.framework.basedata.exception;

/**
* 服务层异常
* 
* @author  zhouliang
* @version  [1.0, 2012-9-11]
* @since  [framework-basedata/1.0]
*/
public class ServiceException extends Exception {
    
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
     * @param message 异常消息
     */
    public ServiceException(String message) {
        super(message);
    }
    
    /**
     * 构造函数
     * @param code 异常编码
     * @param message 异常消息
     */
    public ServiceException(String code, String message) {
        super(message);
        this.code = code;
    }
    
    /**
     * 构造函数
     * @param ex 异常
     */
    public ServiceException(Throwable ex) {
        super(ex);
    }
    
    /**
     * 构造函数
     * @param message 异常消息
     * @param ex 异常
     */
    public ServiceException(String message, Throwable ex) {
        super(message, ex);
    }
    
    /**
     * 构造函数
     * @param code 异常编码
     * @param message 异常消息
     * @param ex 异常
     */
    public ServiceException(String code, String message, Throwable ex) {
        super(message, ex);
        this.code = code;
    }
    
    public String getCode() {
        return code;
    }
    
    public void setCode(String code) {
        this.code = code;
    }
    
}
