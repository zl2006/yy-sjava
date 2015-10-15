package org.yy.framework.service.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 *  
   Hessian服务描述, 用于描述spring中的beanDefine
   @Hessian(app = "test_app", uri = "/hessian/testHessianService", bean = "testHessianService", overloadEnabled = false)
  
  1,服务器端配置
       <bean id="testHessianService" class="org.yy.demo.service.TestHessianServiceImpl" />  
       <bean name="/hessian/testHessianService" class="org.springframework.remoting.caucho.HessianServiceExporter">  
             <property name="service" ref="testHessianService" />  
             <property name="serviceInterface" value="org.yy.demo.service.hessian.TestHessianService" />  
       </bean> 
  2,客户端配置
        <bean id="testHessianService" class="org.springframework.remoting.caucho.HessianProxyFactoryBean">  
        	<!-- 请求代理Servlet路径 -->          
        	<property name="serviceUrl"><value>http://localhost:8080/hessian/testHessianService</value></property>  
        	<!-- 接口定义 -->  
        	<property name="serviceInterface"><value>org.yy.demo.service.hessian.TestHessianService</value>  </property>  
    	</bean>  
 *
 * @author zhouliang
 * @version [1.0, 2015年10月11日]
 * @since [framework-service/1.0]
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Hessian {
    
    /**
     *  1,服务器端扫描时，为引用的bean（<property name="service" ref="testHessianService" />  ）；
     *  2,客户端扫描时，为hessian的bean名称（<bean id="testHessianService" class="org.springframework.remoting.caucho.HessianProxyFactoryBean">   ）；
     */
    String bean();
    

    /**
     *  1,服务器端扫描时，为hessian的bean名称（ <bean name="/hessian/testHessianService" class="org.springframework.remoting.caucho.HessianServiceExporter"> ）
        2,客户端扫描时，为hessian请求的地址组成部分（<property name="serviceUrl"><value>http://localhost:8080/hessian/testHessianService</value></property>   ）
     */
    String uri();
    
    /**
     * 客户端使用，通过app名称指定远程调用地址（localhost:8004)
     */
    String app();
    
    /**
     *  客户端使用，是否支持Hessian服务接口的重载调用
     */
    boolean overloadEnabled() default false;
}