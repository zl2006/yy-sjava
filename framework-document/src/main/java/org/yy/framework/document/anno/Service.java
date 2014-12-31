/*
* 文 件 名:  ServiceAnno.java
* 版    权:  YY Technologies Co., Ltd. Copyright 1993-2012,  All rights reserved
* 描    述:  服务文档服务类描述 
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
* 服务文档服务类描述 
* 
* @author  zhouliang
* @version  [1.0, 2013年11月10日]
*/
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Service {
    /**
     * 服务名称
     */
    String name() default "";
    
    /**
     * 服务角色
     */
    String role() default "";
    
    /**
     *  服务描述
     */
    String desc() default "";
    
    /**
     *  服务涉及到的业务模型
     */
    Model[] models() default {};
}
