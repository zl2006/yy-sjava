/*
* 文 件 名:  AbstraceParser.java
* 版    权:  YY Technologies Co., Ltd. Copyright 2012-2013,  All rights reserved
* 描    述: 抽象解析器
* 修 改 人:  zhouliang
* 修改时间:  2015年10月15日
* 修改内容:  <修改内容>
*/
package org.yy.framework.service.parser;

import java.lang.annotation.Annotation;

import org.yy.framework.service.ServiceScannerConfigurer;

/**
* 抽象解析器
* 
 * @author zhouliang
 * @version [1.0, 2015年10月11日]
 * @since [framework-service/1.0]
 */
public abstract class AbstraceParser  implements Parser{
    
    //扫描配置
    protected ServiceScannerConfigurer scannerConfigurer;
    
    protected Class<? extends Annotation> annoClass;
    
    public AbstraceParser(ServiceScannerConfigurer scannerConfigurer, Class<? extends Annotation> annoClass) {
        this.scannerConfigurer = scannerConfigurer;
        this.annoClass = annoClass;
    }
}
