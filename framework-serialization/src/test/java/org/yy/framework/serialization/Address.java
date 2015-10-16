/*
* 文 件 名:  Address.java
* 版    权:  YY Technologies Co., Ltd. Copyright 2012-2013,  All rights reserved
* 描    述:  <描述>
* 修 改 人:  zhouliang
* 修改时间:  2015年10月16日
* 修改内容:  <修改内容>
*/
package org.yy.framework.serialization;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
*/
@SuppressWarnings("serial")
@XStreamAlias("address")
public class Address  implements Serializable{
    
    private String city;
    
    private String province;
    
    /** 
    <默认构造函数>
    */
    public Address() {
        super();
    }
    
    /** 
    <默认构造函数>
    */
    public Address(String city, String province) {
        super();
        this.city = city;
        this.province = province;
    }
    
    /**
    * @return 返回 city
    */
    public String getCity() {
        return city;
    }
    
    /**
    * @param 对city进行赋值
    */
    public void setCity(String city) {
        this.city = city;
    }
    
    /**
    * @return 返回 province
    */
    public String getProvince() {
        return province;
    }
    
    /**
    * @param 对province进行赋值
    */
    public void setProvince(String province) {
        this.province = province;
    }
    
    /** {@inheritDoc} */
    @Override
    public String toString() {
        return "Address [city=" + city + ", province=" + province + "]";
    }
    
}
