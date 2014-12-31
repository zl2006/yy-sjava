/*
 * 文 件 名:  ExceptionAnno.java
 * 版    权:  YY Technologies Co., Ltd. Copyright 2012-2013,  All rights reserved
 * 描    述:  服务文档方法异常描述
 * 修 改 人:  zhouliang
 * 修改时间:  2013年11月14日
 * 修改内容:  
 */
package org.yy.framework.document.anno;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 服务文档方法异常描述
 * 
 * @author zhouliang
 * @version [1.0, 2013年11月14日]
 * @since [framework-basedata/1.0]
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Exception {
    
    /**
     * 错误编码
     */
    String code() default "";
    
    /**
     * 错误消息
     */
    String message() default "";
    
    /**
     * 错误详细说明 
     */
    String desc() default "";
}
