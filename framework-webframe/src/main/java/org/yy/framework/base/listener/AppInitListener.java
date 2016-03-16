package org.yy.framework.base.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * 应用初始化
 * 
 * @author  zhouliang
 * @version  [1.0, 2016年3月16日]
 * @since [web-framework/1.0]
 */
public class AppInitListener implements ServletContextListener {
    
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        //1. 初始化页面配置项
        ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(sce.getServletContext());
        sce.getServletContext().setAttribute("pageConfig", ctx.getBean("pageConfig"));
    }
    
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        
    }
    
}
