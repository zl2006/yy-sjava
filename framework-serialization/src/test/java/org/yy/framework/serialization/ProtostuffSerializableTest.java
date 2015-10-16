/*
* 文 件 名:  ProtostuffSerializableTest.java
* 版    权:  YY Technologies Co., Ltd. Copyright 2012-2013,  All rights reserved
* 描    述:  <描述>
* 修 改 人:  zhouliang
* 修改时间:  2015年10月16日
* 修改内容:  <修改内容>
*/
package org.yy.framework.serialization;

import org.junit.Before;
import org.junit.Test;
import org.yy.framework.serialization.SERIALIZATIONTYPE;
import org.yy.framework.serialization.Serializable;
import org.yy.framework.serialization.SerializableFactory;

/**
*/
public class ProtostuffSerializableTest {
    
    private Serializable protostuff = SerializableFactory.build(SERIALIZATIONTYPE.PROTOSTUFF);
    
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
     * Test method for {@link org.yy.framework.serialization.ProtostuffSerializable#serializeToByte(java.lang.Object)}.
     */
    @Test
    public void testSerializeToByte() {
        protostuff.serializeToByte(user);
    }
    
    /**
     * Test method for {@link org.yy.framework.serialization.ProtostuffSerializable#deserializeFromByte(byte[], java.lang.Class)}.
     */
    @Test
    public void testDeserializeFromByte() {
        byte[] result = protostuff.serializeToByte(user);
        User u = protostuff.deserializeFromByte(result, User.class);
        System.out.println("testDeserializeFromByte user object:" + u);
    }
    
    /**
     * Test method for {@link org.yy.framework.serialization.ProtostuffSerializable#serializeToString(java.lang.Object)}.
     */
    @Test
    public void testSerializeToString() {
        System.out.println("testSerializeToString user object:" + user);
        System.out.println("testSerializeToString user json string:" + protostuff.serializeToString(user));
    }
    
    /**
     * Test method for {@link org.yy.framework.serialization.ProtostuffSerializable#deserializeFromString(java.lang.String, java.lang.Class)}.
     */
    @Test
    public void testDeserializeFromString() {
        String result = protostuff.serializeToString(user);
        User u = protostuff.deserializeFromString(result, User.class);
        System.out.println("testDeserializeFromString user object:" + u);
    }
    
}
