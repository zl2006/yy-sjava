/*
* 文 件 名:  SerializableFactory.java
* 版    权:  YY Technologies Co., Ltd. Copyright 2012-2013,  All rights reserved
* 描    述:  序列化工厂
* 修 改 人:  zhouliang
* 修改时间:  2015年10月16日
* 修改内容:  <修改内容>
*/
package org.yy.framework.serialization;

import java.util.HashMap;
import java.util.Map;

/**
* 序列化工厂
* 
* @author  zhouliang
* @version  [1.0, 2015年10月16日]
* @since  [framework-serialization/1.0]
*/
public final class SerializableFactory {
    
    //序列化类合集
    private static Map<SERIALIZATIONTYPE, Serializable> serializables = new HashMap<SERIALIZATIONTYPE, Serializable>();
    
    //初始化
    static {
        serializables.put(SERIALIZATIONTYPE.JSON_FASTJSON, FastJsonSerializable.create());
        serializables.put(SERIALIZATIONTYPE.PROTOSTUFF, ProtostuffSerializable.create());
        serializables.put(SERIALIZATIONTYPE.JDK, JdkSerializable.create());
    }
    
    private SerializableFactory() {
    };
    
    public static Serializable build(SERIALIZATIONTYPE serializationType) {
        return serializables.get(serializationType);
    }
}
