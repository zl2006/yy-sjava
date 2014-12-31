/*
* 文 件 名:  ErrorMsg.java
* 版    权:  YY Technologies Co., Ltd. Copyright 2012-2013,  All rights reserved
* 描    述:   错误消息
* 修 改 人:  zhouliang
* 修改时间:  2013年12月1日
* 修改内容:  <修改内容>
*/
package org.yy.framework.base.validator;

/**
*  错误消息
* 
* @author  zhouliang
* @version  [1.0, 2013年12月1日]
* @since  [framework-webframe/1.0]
*/
public class ValidateError {
    
    /**
     * 错误属性名
     */
    private String propertyName;
    
    /**
     * 错误消息
     */
    private String message;
    
    /** {@inheritDoc} */
    @Override
    public String toString() {
        return propertyName + ":" + message;
    }
    
    /**
    * @return 返回 propertyName
    */
    public String getPropertyName() {
        return propertyName;
    }
    
    /**
    * @param 对propertyName进行赋值
    */
    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
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
    
    /** 
    <默认构造函数>
    */
    public ValidateError(String propertyName, String message) {
        super();
        this.propertyName = propertyName;
        this.message = message;
    }
    
}
