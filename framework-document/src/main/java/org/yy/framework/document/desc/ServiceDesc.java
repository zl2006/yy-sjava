/*
* 文 件 名:  ServiceDesc.java
* 版    权:  YY Technologies Co., Ltd. Copyright 1993-2012,  All rights reserved
* 描    述:  服务化类描述
* 修 改 人:  zhouliang
* 修改时间:  2013年11月10日
*/
package org.yy.framework.document.desc;

import java.util.ArrayList;
import java.util.List;

/**
* 服务化类描述
* 
* @author  zhouliang
* @version  [1.0, 2013年11月10日]
* @since  [framework-basedata/1.0]
*/
public class ServiceDesc {
    
    /**
     * 服务名称 
     */
    private String name;
    
    /**
     * 服务描述
     */
    private String description;
    
    /**
     * 服务类名
     */
    private String className;
    
    /**
     * 使用角色
     */
    private String role;
    
    /**
     * 方法描述
     */
    private List<MethodDesc> methods = new ArrayList<MethodDesc>();
    
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
    * @return 返回 className
    */
    public String getClassName() {
        return className;
    }
    
    /**
    * @param 对className进行赋值
    */
    public void setClassName(String className) {
        this.className = className;
    }
    
    /**
    * @return 返回 role
    */
    public String getRole() {
        return role;
    }
    
    /**
    * @param 对role进行赋值
    */
    public void setRole(String role) {
        this.role = role;
    }
    
    /**
    * @return 返回 methods
    */
    public List<MethodDesc> getMethods() {
        return methods;
    }
    
    /**
    * @param 对methods进行赋值
    */
    public void setMethods(List<MethodDesc> methods) {
        this.methods = methods;
    }
    
    /**
    * @return 返回 hrefName
    */
    public String getHrefName() {
        if (className == null) {
            return "";
        }
        return className.replace(".", "");
    }
    
    /** {@inheritDoc} */
    @Override
    public String toString() {
        return "ServiceDesc [name=" + name + ", description=" + description + ", className=" + className + ", role="
            + role + ", methods=" + methods + "]";
    }
    
    public static void main(String[] args) {
        ServiceDesc s = new ServiceDesc();
        s.setClassName("com.tyidc.com");
        System.out.println(s.getHrefName());
    }
    
}
