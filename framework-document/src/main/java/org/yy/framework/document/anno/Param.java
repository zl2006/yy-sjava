/*
* 文 件 名:  ParamAnno.java
* 版    权:  YY Technologies Co., Ltd. Copyright 1993-2012,  All rights reserved
* 描    述:  服务文档方法参数描述
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
* 服务文档方法参数描述
* @author  zhouliang
* @version  [1.0, 2013年11月10日]
* @since  [framework-basedata/1.0]
*/
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Param {
    String[] value() default {};
}
