/*
* 文 件 名:  XStreamSerializableTest.java
* 版    权:  YY Technologies Co., Ltd. Copyright 2012-2013,  All rights reserved
* 描    述:  <描述>
* 修 改 人:  zhouliang
* 修改时间:  2015年10月16日
* 修改内容:  <修改内容>
*/
package org.yy.framework.serialization;


import org.junit.Before;
import org.junit.Test;

/**
*/
public class XStreamSerializableTest {
    
    private Serializable fastjson = SerializableFactory.build(SERIALIZATIONTYPE.XML_XSTREAM);
    
    private User user = new User();
    
    @Before
    public void init() {
        user.getContacts().add("Contact1");
        user.getContacts().add("Contact2");
        user.getAddresses().add(new Address("岳阳", "湖南"));
        user.getAddresses().add(new Address("长沙", "湖南"));
        user.getAddresses().add(new Address("湘潭", "湖南"));
    }
    
    /**
     * Test method for {@link org.yy.framework.serialization.FastJsonSerializable#serializeToByte(java.lang.Object)}.
     */
    @Test
    public void testSerializeToByte() {
        fastjson.serializeToByte(user);
    }
    
    /**
     * Test method for {@link org.yy.framework.serialization.FastJsonSerializable#deserializeFromByte(byte[], java.lang.Class)}.
     */
    @Test
    public void testDeserializeFromByte() {
        byte[] result = fastjson.serializeToByte(user);
        User u = fastjson.deserializeFromByte(result, User.class);
        System.out.println("testDeserializeFromByte user object:" + u);
    }
    
    /**
     * Test method for {@link org.yy.framework.serialization.FastJsonSerializable#serializeToString(java.lang.Object)}.
     */
    @Test
    public void testSerializeToString() {
        System.out.println("testSerializeToString user object:" + user);
        System.out.println("testSerializeToString user json string:" + fastjson.serializeToString(user));
    }
    
    /**
     * Test method for {@link org.yy.framework.serialization.FastJsonSerializable#deserializeFromString(java.lang.String, java.lang.Class)}.
     */
    @Test
    public void testDeserializeFromString() {
        String result = fastjson.serializeToString(user);
        User u = fastjson.deserializeFromString(result, User.class);
        System.out.println("testDeserializeFromString user object:" + u);
    }
}
