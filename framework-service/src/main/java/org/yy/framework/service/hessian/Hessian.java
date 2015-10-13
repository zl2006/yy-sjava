package org.yy.framework.service.hessian;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *      Hessian服务描述, 用于描述spring中的beanDefine<br>
 *     服务地址：http://localhost:8004/testService
 *  
 *      1，手动Bean定义模式
          definition.getPropertyValues().add("serviceInterface", definition.getBeanClassName());
          definition.getPropertyValues().add("service", new RuntimeBeanReference(beanNameRef));
          definition.setBeanClass(HessianServiceExporter.class);
          
          
          <beans xmlns="http://www.springframework.org/schema/beans"  
                 xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"  
                 xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">  
                   
                  <bean id="testService" class="org.yy.demo.service.TestServiceImpl" />  
                  
                  <bean name="/addService.htm" class="org.springframework.remoting.caucho.HessianServiceExporter">  
                    <property name="service" ref="testService" />  
                    <property name="serviceInterface" value="org.yy.demo.service.TestService" />  
                  </bean>  
          </beans> 
 *
 * @author zhouliang
 * @version [1.0, 2015年10月11日]
 * @since [framework-service/1.0]
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Hessian {
    
    /**
     * 具体实现bean引用<br>
     * 例如：
     * 1，<property name="service" ref="addService" />  
     * 2，definition.getPropertyValues().add("service", new RuntimeBeanReference(beanNameRef));
     */
    String beanRef() default "";
    
    /**
     *  hessian名称，也是客户端访问链接的后半部分 配置。如: /testService
     */
    String uri(); //
    
    /**
     *  客户端使用
     * 当接口服务有重载方法时，是否能正常调用。注意如果有重载方法，hessian描述需要把它设置成true
     */
    boolean overloadEnabled() default false; // 
    
    /**
     *  客户端使用
     *  客户端访问链接前半部分配置 如 http://localhost:8004/
     */
    Context context();
}