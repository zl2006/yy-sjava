/*
* 文 件 名:  FrameworkExceptoin.java
* 版    权:  YY Technologies Co., Ltd. Copyright 2012-2013,  All rights reserved
* 描    述:  框架异常
* 修 改 人:  zhouliang
* 修改时间:  2013年11月14日
* 修改内容:  
*/
package org.yy.framework.basedata.exception;

/**
* 框架异常
* 
* @author  zhouliang
* @version  [1.0, 2013年11月14日]
* @since  [framework-basedata/1.0]
*/
public class FrameworkExceptoin extends Exception {
    
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
    public FrameworkExceptoin(String message) {
        super(message);
    }
    
    /**
     * 构造函数
     * @param code 异常编码
     * @param message 异常消息
     */
    public FrameworkExceptoin(String code, String message) {
        super(message);
        this.code = code;
    }
    
    /**
     * 构造函数
     * @param ex 异常
     */
    public FrameworkExceptoin(Throwable ex) {
        super(ex);
    }
    
    /**
     * 构造函数
     * @param message 异常消息
     * @param ex 异常
     */
    public FrameworkExceptoin(String message, Throwable ex) {
        super(message, ex);
    }
    
    /**
     * 构造函数
     * @param code 异常编码
     * @param message 异常消息
     * @param ex 异常
     */
    public FrameworkExceptoin(String code, String message, Throwable ex) {
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
