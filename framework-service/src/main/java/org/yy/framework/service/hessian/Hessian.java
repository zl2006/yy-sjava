package org.yy.framework.service.hessian;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义注解，用于hessian接口
 * 
 * @author zhouliang
 * @version [1.0, 2015年10月11日]
 * @since [framework-service/1.0]
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Hessian {
    
    String description() default "";
    
    boolean overloadEnabled() default false; // 是否支持方法重载
    
    String uri(); // 用于服务端bean名称，也是客户端访问链接的后半部分 配置。如: /talentService
    
    Context context(); // 客户端访问链接前半部分配置 如 http://localhost:8004/remote
}