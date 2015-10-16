/*
* 文 件 名:  FastJsonSerializable.java
* 版    权:  YY Technologies Co., Ltd. Copyright 2012-2013,  All rights reserved
* 描    述:  FastJson序列化
* 修 改 人:  zhouliang
* 修改时间:  2015年10月16日
* 修改内容:  <修改内容>
*/
package org.yy.framework.serialization;

import com.alibaba.fastjson.JSON;

/**
* FastJson序列化
* 
* @author  zhouliang
* @version  [1.0, 2015年10月16日]
* @since  [framework-serialization/1.0]
*/
public class FastJsonSerializable implements Serializable {
    
    private static FastJsonSerializable fastJsonSerializable = new FastJsonSerializable();
    
    private FastJsonSerializable() {
    }
    
    public static FastJsonSerializable create() {
        return fastJsonSerializable;
    }
    
    /** {@inheritDoc} */
    @Override
    public <T> byte[] serializeToByte(T object) {
        return JSON.toJSONBytes(object);
    }
    
    /** {@inheritDoc} */
    @Override
    public <T> T deserializeFromByte(byte[] data, Class<T> targetClass) {
        return JSON.parseObject(data, targetClass);
    }
    
    /** {@inheritDoc} */
    @Override
    public <T> String serializeToString(T object) {
        return JSON.toJSONString(object);
    }
    
    /** {@inheritDoc} */
    @Override
    public <T> T deserializeFromString(String data, Class<T> targetClass) {
        return JSON.parseObject(data, targetClass);
    }
    
}
