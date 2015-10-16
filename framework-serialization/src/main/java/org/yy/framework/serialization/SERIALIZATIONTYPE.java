/*
* 文 件 名:  SERIALIZATIONTYPE.java
* 版    权:  YY Technologies Co., Ltd. Copyright 2012-2013,  All rights reserved
* 描    述:  序列化类型 
* 修 改 人:  zhouliang
* 修改时间:  2015年10月16日
* 修改内容:  <修改内容>
*/
package org.yy.framework.serialization;

/**
*  序列化类型 
* 
* @author  zhouliang
* @version  [1.0, 2015年10月16日]
* @since  [framework-serialization/1.0]
*/
public enum SERIALIZATIONTYPE {
    
    JSON_FASTJSON("fastjson"), PROTOSTUFF("protostuff"), JDK("jdk");
    
    private String type;
    
    private SERIALIZATIONTYPE(String type) {
        this.type = type;
    }
    
    /** {@inheritDoc} */
    @Override
    public String toString() {
        return this.type;
    }
}
