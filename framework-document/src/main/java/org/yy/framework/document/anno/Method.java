/*
* 文 件 名:  MethodAnno.java
* 版    权:  YY Technologies Co., Ltd. Copyright 1993-2012,  All rights reserved
* 描    述:  服务文档方法描述
* 修 改 人:  zhouliang
* 修改时间:  2013年11月10日
*/
package org.yy.framework.document.anno;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
* 服务文档方法描述
* 
* @author  zhouliang
* @version  [1.0, 2013年11月10日]
* @since  [framework-basedata/1.0]
*/
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Method {
    
    /**
     * 服务方法名称
     */
    String name() default "";
    
    /**
     * 服务方法描述 
     */
    String desc() default "";
    
    /**
     * 服务输入参数描述
     */
    Field[] inputParams() default {};
    
    /**
     * 返回参数
     */
    Return[] returns() default {};
    
    /**
     * 异常描述
     */
    Exception[] exceps() default {};
}
