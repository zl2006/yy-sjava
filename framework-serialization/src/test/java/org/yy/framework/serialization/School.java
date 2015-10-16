/*
* 文 件 名:  School.java
* 版    权:  YY Technologies Co., Ltd. Copyright 2012-2013,  All rights reserved
* 描    述:  <描述>
* 修 改 人:  zhouliang
* 修改时间:  2015年10月16日
* 修改内容:  <修改内容>
*/
package org.yy.framework.serialization;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;


@SuppressWarnings("serial")
@XStreamAlias("school")
public class School implements Serializable{
    
    private String name = "湖南理工学院";
    
    private String master = "周树人";
    
    /**
    * @return 返回 name
    */
    public String getName() {
        return name;
    }
    
    /**
    * @param 对name进行赋值
    */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
    * @return 返回 master
    */
    public String getMaster() {
        return master;
    }
    
    /**
    * @param 对master进行赋值
    */
    public void setMaster(String master) {
        this.master = master;
    }
    
    /** {@inheritDoc} */
    @Override
    public String toString() {
        return "School [name=" + name + ", master=" + master + "]";
    }
    
}
