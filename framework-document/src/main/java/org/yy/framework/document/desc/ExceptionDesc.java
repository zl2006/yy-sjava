/*
 * 文 件 名:  ExceptionDesc.java
 * 版    权:  YY Technologies Co., Ltd. Copyright 2012-2013,  All rights reserved
 * 描    述:  服务文档异常描述
 * 修 改 人:  zhouliang
 * 修改时间:  2013年11月14日
 * 修改内容:  
 */
package org.yy.framework.document.desc;

/**
 * 服务文档异常描述
 * 
 * @author zhouliang
 * @version [1.0, 2013年11月14日]
 * @since [framework-basedata/1.0]
 */
public class ExceptionDesc {
    
    /**
     *  异常编码
     */
    private String code;
    
    /**
     * 异常描述
     */
    private String message;
    
    /**
     * @return 返回 code
     */
    public String getCode() {
        return code;
    }
    
    /**
     * @param 对code进行赋值
     */
    public void setCode(String code) {
        this.code = code;
    }
    
    /**
     * @return 返回 message
     */
    public String getMessage() {
        return message;
    }
    
    /**
     * @param 对message进行赋值
     */
    public void setMessage(String message) {
        this.message = message;
    }
    
    /** {@inheritDoc} */
    @Override
    public String toString() {
        return "ExceptionDesc [code=" + code + ", message=" + message + "]";
    }
    
}
