/*
* 文 件 名:  ModelDesc.java
* 版    权:  YY Technologies Co., Ltd. Copyright 2012-2013,  All rights reserved
* 描    述:  服务文档模型描述
* 修 改 人:  zhouliang
* 修改时间:  2013年11月14日
* 修改内容:  
*/
package org.yy.framework.document.desc;

import java.util.ArrayList;
import java.util.List;

/**
* 服务文档模型描述
* 
* @author  zhouliang
* @version  [1.0, 2013年11月14日]
* @since  [framework-basedata/1.0]
*/
public class ModelDesc {
    /**
     * 名称
     */
    private String name;
    
    /**
     * 描述
     */
    private String description;
    
    /**
     * 类
     */
    private String className;
    
    private List<FieldDesc> fields = new ArrayList<FieldDesc>();
    
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
    * @return 返回 fields
    */
    public List<FieldDesc> getFields() {
        return fields;
    }
    
    /**
    * @param 对fields进行赋值
    */
    public void setFields(List<FieldDesc> fields) {
        this.fields = fields;
    }
    
    public FieldDesc getFieldDesc(String name){
        
        if( fields == null || fields.size() == 0){
            return null;
        }
        for( FieldDesc field : fields){
            if( field.getFieldName() != null && field.getFieldName().equals(name)){
                return field;
            }
        }
        
        return null;
    }
    
    /** {@inheritDoc} */
    @Override
    public String toString() {
        return "ModelDesc [name=" + name + ", description=" + description + ", className=" + className + ", fields="
            + fields + "]";
    }
    
}
