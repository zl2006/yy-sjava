/*
* 文 件 名:  FieldAnno.java
* 版    权:  YY Technologies Co., Ltd. Copyright 2012-2013,  All rights reserved
* 描    述:  服务模型字段描述
* 修 改 人:  zhouliang
* 修改时间:  2013年11月16日
* 修改内容:  
*/
package org.yy.framework.document.anno;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
*  服务模型字段描述, 
* 
* @author  zhouliang
* @version  [1.0, 2013年11月16日]
* @since  [framework-basedata/1.0]
*/
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Field {
    
    /**
     * 参数en名称 
     */
    String name() default "";
    
    /**
     * 参数名称
     */
    String cnname() default "";
    
    /**
     * 参数描述
     */
    String desc() default "";
    
    /**
     * 参数类型
     */
    TypeEnum type() default TypeEnum.STRING;
    
    /**
     * 是否允许为空
     */
    boolean nullable() default true;
    
    /**
     * 长度限制
     */
    int length() default 0;
    
    /**
     * 配置格式 
     */
    String format() default "";
    
    /**
     * 是否为数组, 用于复杂对象在生成表单时加上下标
     */
    boolean islist() default false;
    
    /**
     * 需要配合type使用，当type=TypeEnum.OBJECT时此标签才会生效
     * 主要当字段为集合类型时指定，并且集合中的对象为复杂对象，因为此时不能判断泛型中的类型, 例如：List<User> users;
     * 简单的对象不需要声明, 例如：List<String> addresses;
     */
    @SuppressWarnings("rawtypes")
    Class clazz() default Class.class;
}
