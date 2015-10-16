/*
* 文 件 名:  JDKSerializable.java
* 版    权:  YY Technologies Co., Ltd. Copyright 2012-2013,  All rights reserved
* 描    述:   JDK序列化
* 修 改 人:  zhouliang
* 修改时间:  2015年10月16日
* 修改内容:  <修改内容>
*/
package org.yy.framework.serialization;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.apache.commons.codec.binary.Base64;

/**
* JDK序列化
* 
* @author  zhouliang
* @version  [1.0, 2015年10月16日]
* @since  [framework-serialization/1.0]
*/
public class JdkSerializable implements Serializable {
    
    private static JdkSerializable jdkSerializable = new JdkSerializable();
    
    private JdkSerializable() {
    }
    
    public static JdkSerializable create() {
        return jdkSerializable;
    }
    
    /** {@inheritDoc} */
    @Override
    public <T> byte[] serializeToByte(T object) {
        ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream(1024);
        ObjectOutputStream oos;
        try {
            oos = new ObjectOutputStream(byteOutputStream);
            oos.writeObject(object);
            oos.close();
            byteOutputStream.close();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        return byteOutputStream.toByteArray();
    }
    
    /** {@inheritDoc} */
    @SuppressWarnings("unchecked")
    @Override
    public <T> T deserializeFromByte(byte[] data, Class<T> targetClass) {
        ByteArrayInputStream byteInputStream = new ByteArrayInputStream(data);
        ObjectInputStream ois;
        T object = null;
        try {
            ois = new ObjectInputStream(byteInputStream);
            object = (T)ois.readObject();
            ois.close();
            byteInputStream.close();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
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
    
}
