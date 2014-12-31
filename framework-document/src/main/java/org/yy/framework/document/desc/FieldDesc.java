/*
* 文 件 名:  FieldDesc.java
* 版    权:  YY Technologies Co., Ltd. Copyright 2012-2013,  All rights reserved
* 描    述:  服务模型字段描述
* 修 改 人:  zhouliang
* 修改时间:  2013年11月16日
* 修改内容: 
*/
package org.yy.framework.document.desc;

/**
* 服务模型字段描述
* 
* @author  zhouliang
* @version  [1.0, 2013年11月16日]
* @since  [framework-basedata/1.0]
*/
public class FieldDesc {
    
    /**
     * 中文名称
     */
    private String name;
    
    /**
     * 字段名称
     */
    private String fieldName;
    
    /**
     * 描述
     */
    private String description;
    
    /**
     *  字段类名
     */
    private String classname;
    
    /**
     * 长度限制
     */
    private Integer length;
    
    /**
     * 参数类型
     */
    private String type;
    
    /**
     * 是否允许为空
     */
    private boolean nullable;
    
    /**
     * 配置格式 
     */
    private String format;
    
    /**
     * 是否为数组, 用于复杂对象在生成表单时加上下标
     */
    private boolean islist;
    
    
    /**
     * 字段所属性的模型，当作为方法参数定义时此字段为空，例如:usercontroller.save的参数
     */
    private ModelDesc modelDesc;
    
    
    public boolean isShow(){
        return "object".equals( type );
    }
    
    public boolean isFile(){
        return "file".equals(type);
    }
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
    * @return 返回 fieldName
    */
    public String getFieldName() {
        
        return fieldName;
    }
    
    /**
    * @param 对fieldName进行赋值
    */
    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
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
    
    /**
    * @return 返回 length
    */
    public Integer getLength() {
        return length;
    }
    
    /**
    * @param 对length进行赋值
    */
    public void setLength(Integer length) {
        this.length = length;
    }
    
    /**
    * @return 返回 type
    */
    public String getType() {
        return type;
    }
    
    /**
    * @param 对type进行赋值
    */
    public void setType(String type) {
        this.type = type;
    }
    
    /**
    * @return 返回 nullable
    */
    public boolean isNullable() {
        return nullable;
    }
    
    /**
    * @param 对nullable进行赋值
    */
    public void setNullable(boolean nullable) {
        this.nullable = nullable;
    }
    
    /**
    * @return 返回 format
    */
    public String getFormat() {
        return format;
    }
    
    /**
    * @param 对format进行赋值
    */
    public void setFormat(String format) {
        this.format = format;
    }
    
    /**
    * @return 返回 islist
    */
    public boolean isIslist() {
        return islist;
    }
    
    /**
    * @param 对islist进行赋值
    */
    public void setIslist(boolean islist) {
        this.islist = islist;
    }
    
    /**
    * @return 返回 modelDesc
    */
    public ModelDesc getModelDesc() {
        return modelDesc;
    }
    
    /**
    * @param 对modelDesc进行赋值
    */
    public void setModelDesc(ModelDesc modelDesc) {
        this.modelDesc = modelDesc;
    }
    
    /** {@inheritDoc} */
    @Override
    public String toString() {
        return "FieldDesc [name=" + name + ", fieldName=" + fieldName + ", description=" + description + ", classname="
            + classname + ", length=" + length + ", type=" + type + ", nullable=" + nullable + ", format=" + format
            + ", islist=" + islist + "]";
    }
    
}
