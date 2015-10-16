/*
* 文 件 名:  FastJsonSerializable.java
* 版    权:  YY Technologies Co., Ltd. Copyright 2012-2013,  All rights reserved
* 描    述:  FastJson序列化
* 修 改 人:  zhouliang
* 修改时间:  2015年10月16日
* 修改内容:  <修改内容>
*/
package org.yy.framework.serialization;

import org.apache.commons.codec.binary.Base64;

import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;

/**
* FastJson序列化
* 
* @author  zhouliang
* @version  [1.0, 2015年10月16日]
* @since  [framework-serialization/1.0]
*/
public class ProtostuffSerializable implements Serializable {
    
    private static ProtostuffSerializable protostuffSerializable = new ProtostuffSerializable();
    
    private ProtostuffSerializable() {
    }
    
    public static ProtostuffSerializable create() {
        return protostuffSerializable;
    }
    
    /** {@inheritDoc} */
    @SuppressWarnings("unchecked")
    @Override
    public <T> byte[] serializeToByte(T object) {
        Schema<T> schema = (Schema<T>)RuntimeSchema.getSchema(object.getClass());
        LinkedBuffer buffer = getApplicationBuffer();
        byte[] protostuff = new byte[0];
        try {
            protostuff = ProtostuffIOUtil.toByteArray(object, schema, buffer);
        }
        finally {
            buffer.clear();
        }
        return protostuff;
    }
    
    /** {@inheritDoc} */
    @Override
    public <T> T deserializeFromByte(byte[] data, Class<T> targetClass) {
        Schema<T> schema = RuntimeSchema.getSchema(targetClass);
        T object = schema.newMessage();
        ProtostuffIOUtil.mergeFrom(data, object, schema);
        return object;
    }
    
    /** {@inheritDoc} */
    @Override
    public <T> String serializeToString(T object) {
        byte[] data = serializeToByte(object);
        return Base64.encodeBase64String(data);
    }
    
    /** {@inheritDoc} */
    @Override
    public <T> T deserializeFromString(String data, Class<T> targetClass) {
        byte[] temp = Base64.decodeBase64(data);
        return deserializeFromByte(temp, targetClass);
    }
    
    private LinkedBuffer getApplicationBuffer() {
        return LinkedBuffer.allocate(1024);
    }
}
