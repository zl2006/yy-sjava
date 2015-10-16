/*
* 文 件 名:  Serailization.java
* 版    权:  YY Technologies Co., Ltd. Copyright 2012-2013,  All rights reserved
* 描    述:  接口
* 修 改 人:  zhouliang
* 修改时间:  2015年10月16日
* 修改内容:  <修改内容>
*/
package org.yy.framework.serialization;

/**
*  序列化类接口
* 
* @author  zhouliang
* @version  [1.0, 2015年10月16日]
* @since  [framework-serialization/1.0]
*/
public interface Serializable {
    
    //字符编码
    public static final String CHARSET = "UTF-8";
    
    /**
     * 对象序列化为字节数据
     *  
     * @param object 序列化对象
     * @return 字节数据
     */
    public <T>  byte[] serializeToByte(T object);
    
    /**
     * 字节数据序列化为对象
     * 
     * @param data 字节数据
     * @param targetClass 目标对象类型
     * @return 序列化对象
     */
    public <T> T deserializeFromByte(byte[] data, Class<T> targetClass);
    
    /**
     *  对象序列化为字符串
     * 
     * @param object  序列化对象
     * @return 字符串
     */
    public <T> String serializeToString(T object);
    
    /**
     * 字符串序列化为对象
     * 
     * @param data 字符串
     * @param targetClass 目标对象类型
     * @return 序列化对象
     */
    public <T> T deserializeFromString(String data, Class<T> targetClass);
    
}
