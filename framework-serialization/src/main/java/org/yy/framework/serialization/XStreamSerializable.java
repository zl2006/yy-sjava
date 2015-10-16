/*
* 文 件 名:  XStreamSerializable.java
* 版    权:  YY Technologies Co., Ltd. Copyright 2012-2013,  All rights reserved
* 描    述:   XStream序列化
* 修 改 人:  zhouliang
* 修改时间:  2015年10月16日
* 修改内容:  <修改内容>
*/
package org.yy.framework.serialization;

import java.io.StringWriter;
import java.io.UnsupportedEncodingException;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.CompactWriter;
import com.thoughtworks.xstream.io.xml.DomDriver;

/**
*  XStream序列化
* 
* @author  zhouliang
* @version  [1.0, 2015年10月16日]
* @since  [framework-serialization/1.0]
*/
public class XStreamSerializable implements Serializable {
    
    private static XStreamSerializable xStreamSerializable = new XStreamSerializable();
    
    private XStreamSerializable() {
    }
    
    public static XStreamSerializable create() {
        return xStreamSerializable;
    }
    
    /** {@inheritDoc} */
    @Override
    public <T> byte[] serializeToByte(T object) {
        String result = serializeToString(object);
        try {
            return result.getBytes(Serializable.CHARSET);
        }
        catch (UnsupportedEncodingException e) {
            //e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
    
    /** {@inheritDoc} */
    @Override
    public <T> T deserializeFromByte(byte[] data, Class<T> targetClass) {
        String temp;
        try {
            temp = new String(data, Serializable.CHARSET);
        }
        catch (UnsupportedEncodingException e) {
            //e.printStackTrace();
            throw new RuntimeException(e);
        }
        return deserializeFromString(temp, targetClass);
    }
    
    /** {@inheritDoc} */
    @Override
    public <T> String serializeToString(T object) {
        XStream xstream = new XStream(new DomDriver(Serializable.CHARSET));
        xstream.processAnnotations(object.getClass()); // 识别obj类中的注解
        
        // 以压缩的方式输出XML
        StringWriter sw = new StringWriter();
        xstream.marshal(object, new CompactWriter(sw));
        return sw.toString();
        
        // 以格式化的方式输出XML
        // return xstream.toXML(object);
    }
    
    /** {@inheritDoc} */
    @SuppressWarnings("unchecked")
    @Override
    public <T> T deserializeFromString(String data, Class<T> targetClass) {
        XStream xstream = new XStream(new DomDriver(Serializable.CHARSET));
        xstream.processAnnotations(targetClass);
        return (T)xstream.fromXML(data);
    }
    
}
