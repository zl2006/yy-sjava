/*
* 文 件 名:  ServiceClassPathScanner.java
* 版    权:  YY Technologies Co., Ltd. Copyright 2012-2013,  All rights reserved
* 描    述:  扫描服务元注解标识的类，并形成初步定义BeanDefinition
* 修 改 人:  zhouliang
* 修改时间:  2015年10月14日
* 修改内容:  <修改内容>
*/
package org.yy.framework.service;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.AnnotationConfigUtils;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.context.annotation.ScannedGenericBeanDefinition;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.yy.framework.service.anno.Hessian;
import org.yy.framework.service.parser.HessianParser;
import org.yy.framework.service.parser.Parser;

/**
* 扫描服务元注解标识的接口，并形成初步定义BeanDefinition.
* 
* 要增加新的注解服务，只需要在initAnnoClasses和parers中注册对应类和解析器就可
* 
 * @author zhouliang
 * @version [1.0, 2015年10月11日]
 * @since [framework-service/1.0]
*/
public class ServiceClassPathBeanDefinitionScanner extends ClassPathBeanDefinitionScanner {
    
    //定义服务元注解
	protected List<Class<? extends Annotation>> annoClasses = new ArrayList<Class<? extends Annotation>>();
    
    //元注解解析器
    protected Map<String,  Parser> parsers = new HashMap<String, Parser>();
    
    //扫服务注解配置
    private ServiceScannerConfigurer scannerConfigurer;
    
    public ServiceClassPathBeanDefinitionScanner(BeanDefinitionRegistry registry, ServiceScannerConfigurer scannerConfigurer) {
        super(registry, false);
        this.scannerConfigurer = scannerConfigurer;
        this.initAnnoClasses();
        this.initParers();
        this.initIncludeFilter();
        this.initExcludeFilter();
    }
    
    /** {@inheritDoc} */
    @Override
    protected Set<BeanDefinitionHolder> doScan(String... basePackages) {
        
        Set<BeanDefinitionHolder> beanDefinitions = new LinkedHashSet<BeanDefinitionHolder>();
        for (String basePackage : basePackages) {
        	
            //Step 1， 获取服务元注解标识的BeanDefinition，未注册到spring容器
            Set<BeanDefinition> candidates = findCandidateComponents(basePackage);
            
            //Step 2，生成指定Service的BeanDefinition
            for (BeanDefinition temp : candidates) {
                
                ScannedGenericBeanDefinition srcBeanDefinition = (ScannedGenericBeanDefinition)temp;
                
                //Step 2.1：遍历所有的元注解，每个注解生成一个BeanDefinition并注册
                int i = 0;
                for (String annotationType : annotationTypes(srcBeanDefinition)) {
                	
                	//Step 2.1.1 :设置范围
                	Parser parser = parsers.get(annotationType);							//获取元注解对应的解析器
                	GenericBeanDefinition targetBeanDefinition = new GenericBeanDefinition(srcBeanDefinition);
                    targetBeanDefinition.setScope(ConfigurableBeanFactory.SCOPE_SINGLETON); //范围设置
                    String beanName = ""; 
                    
                    
                    //Step 2.1.2  服务器端与客户端个性配置,名称设置
                    if (ServiceScannerConfigurer.SERVER_WHERE.equals(scannerConfigurer.getWhere())) {
                    	beanName = parser.buildServerBeanName(srcBeanDefinition); 		
                        processServerBeanDefinition(targetBeanDefinition,beanName);
                    }
                    else {
                    	beanName = parser.buildClientBeanName(srcBeanDefinition); 	
                    	beanName = processClientBeanName(beanName, annotationType, i);
                        processClientBeanDefinition(targetBeanDefinition,beanName);
                    }
                    
                    
                    //Step 2.1.3 注册BeanDefinition
                    if (checkCandidate(beanName, targetBeanDefinition)) {
                        BeanDefinitionHolder definitionHolder = new BeanDefinitionHolder(targetBeanDefinition, beanName);
                        beanDefinitions.add(definitionHolder);
                        if (ServiceScannerConfigurer.SERVER_WHERE.equals(scannerConfigurer.getWhere())) {
                        	parser.parseServerBeanDefine(srcBeanDefinition, targetBeanDefinition);	//解析
                        }
                        else {
                        	parser.parseClientBeanDefine(srcBeanDefinition, targetBeanDefinition);	//解析
                        }
                        registerBeanDefinition(definitionHolder, this.getRegistry()); 				//注册
                    }
                    ++i;
                }
                //Step 2.2：删除通过扫包产生的默认BeanFinition
                beanDefinitions.remove(srcBeanDefinition);
            }
        }
        
        return beanDefinitions;
    }
    
    //处理服务端的注解个性定义
    protected void processServerBeanDefinition(GenericBeanDefinition targetBeanDefinition, String beanName) {
        if (targetBeanDefinition instanceof AbstractBeanDefinition) {
            postProcessBeanDefinition((AbstractBeanDefinition)targetBeanDefinition, beanName);
        }
        if (targetBeanDefinition instanceof AnnotatedBeanDefinition) {
            AnnotationConfigUtils.processCommonDefinitionAnnotations((AnnotatedBeanDefinition)targetBeanDefinition);
        }
    }
    
    //处理客户端的注解个性定义
    protected void processClientBeanDefinition(GenericBeanDefinition targetBeanDefinition,String beanName){

    }
    
    //当多个客户端协议时，名称加类型后辍
    protected String processClientBeanName(String srcBeanName,String annotationType, int order){
    	//第一个客户端协议名称使用默认的。
    	if(order == 0){
    		return srcBeanName;
    	}
    	
    	//后续客户端会在默认名称上加上后辍
    	int index  = annotationType.lastIndexOf(".");
    	if( index >= 0){
    		srcBeanName += annotationType.substring(index+1);
    	}else{
    		srcBeanName += annotationType;
    	}
    	
    	return srcBeanName;
    }
    
    //判断BeanDefine是否为接口
    @Override
    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        return (beanDefinition.getMetadata().isInterface() && beanDefinition.getMetadata().isIndependent());
    }
    
    //判断BeanDefine是否重名
    @Override
    protected boolean checkCandidate(String beanName, BeanDefinition beanDefinition)
        throws IllegalStateException {
        if (!this.getRegistry().containsBeanDefinition(beanName) && super.checkCandidate(beanName, beanDefinition)) {
            return true;
        }
        else {
            logger.warn("Skipping HessianServiceExporter with name '" + beanName + "' and '"
                + beanDefinition.getBeanClassName() + "' serviceInterface "
                + ". Bean already defined with the same name!");
            return false;
        }
    }

    
    /**
     * 初始化要使用的注解
     */
    protected void initAnnoClasses(){
    	annoClasses.add(Hessian.class);
    }
    
    /**
     * 初始化元注解解析器
     */
    protected void initParers() {
        parsers.put(Hessian.class.getName(), new HessianParser(this.scannerConfigurer));
    }
    
    /**
     * 包含元注解，这部分类将生成BeanDefinition
     */
    protected void initIncludeFilter() {
        for (int i = 0; i < annoClasses.size(); ++i) {
            addIncludeFilter(new AnnotationTypeFilter(annoClasses.get(i)));
        }
    }
    
    /**
     * 排除元注解，这部分类不会生成BeanDefinition
     */
    protected void initExcludeFilter(){
    	
    }
    
    
    /**
     * 获取所有的元注解类型
     */
    protected Set<String> annotationTypes(ScannedGenericBeanDefinition defintion) {
        return defintion.getMetadata().getAnnotationTypes();
    }
    
}
