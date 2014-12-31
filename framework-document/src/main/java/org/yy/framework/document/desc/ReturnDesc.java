/*
* 文 件 名:  ReturnDesc.java
* 版    权:  YY Technologies Co., Ltd. Copyright 2012-2013,  All rights reserved
* 描    述:  服务返回描述
* 修 改 人:  zhouliang
* 修改时间:  2013年11月16日
* 修改内容:  
*/
package org.yy.framework.document.desc;

/**
* 服务返回描述
* 
* @author  zhouliang
* @version  [1.0, 2013年11月16日]
* @since  [framework-basedata/1.0]
*/
public class ReturnDesc {
    
    /**
     * 名称
     */
    private String name;
    
    /**
     *  描述 
     */
    private String description;
    
    /**
     * 类名
     */
    private String classname;
    
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
    * @return 返回 description
    */
    public String getDescription() {
        return description;
    }
    
    /**
    * @param 对description进行赋值
    */
    public void setDescription(String description) {
        this.description = description;
    }
    
    /**
    * @return 返回 classname
    */
    public String getClassname() {
        return classname;
    }
    
    /**
    * @param 对classname进行赋值
    */
    public void setClassname(String classname) {
        this.classname = classname;
    }
    
    /** {@inheritDoc} */
    @Override
    public String toString() {
        return "ReturnDesc [name=" + name + ", description=" + description + ", classname=" + classname + "]";
    }
    
}
