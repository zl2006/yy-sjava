/*
* 文 件 名:  DocumentAction.java
* 版    权:  YY Technologies Co., Ltd. Copyright 2012-2013,  All rights reserved
* 描    述:  服务化文档 
* 修 改 人:  zhouliang
* 修改时间:  2013年11月8日
*/
package org.yy.framework.document.controller;

import java.io.StringWriter;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
import org.yy.framework.base.controller.AbstractController;
import org.yy.framework.document.builder.SpringMVCXDocumentBuilder;
import org.yy.framework.document.builder.XDocumentBuilder;
import org.yy.framework.document.desc.ModelDesc;
import org.yy.framework.document.desc.ServiceDesc;

/**
* 服务化文档
* 
* @author  zhouliang
* @version  [1.0, 2013年11月8日]
* @since  [framework-base/1.0]
*/
public class DocumentController extends AbstractController implements Controller {
    
    //文档数据模型所在包
    private List<String> scanPackages = null;
    
    private XDocumentBuilder builder = null;
    
    static {
        Properties p = new Properties();
        p.put("input.encoding", "UTF-8");
        p.put("output.encoding", "UTF-8");
        p.put("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        Velocity.init(p);
    }
    
    public DocumentController(List<String> scanPackages) {
        this.scanPackages = scanPackages;
    }
    
    /**
     *   //获取DispatcherServlet中spring的applicationContext,主要针对spring mvc
        //WebApplicationContextUtils.getWebApplicationContext(request.getSession().getServletContext(), "org.springframework.web.servlet.FrameworkServlet.CONTEXT.springmvc");
        
        //获取ContextLoaderListener中spring的applicationContext,主要针对dao, service等公共 bean
        //WebApplicationContextUtils.getWebApplicationContext(request.getSession().getServletContext());
     */
    /** {@inheritDoc} */
    @Override
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response)
        throws Exception {
        
        if (builder == null) {
            WebApplicationContext wc =
                WebApplicationContextUtils.getWebApplicationContext(request.getSession().getServletContext(),
                    "org.springframework.web.servlet.FrameworkServlet.CONTEXT.springmvc");
            builder = new SpringMVCXDocumentBuilder(scanPackages, wc);
        }
        
        String method = request.getParameter("method"); //请求方法,list和detail 
        String refresh = request.getParameter("refresh"); //是否刷新
        
        //document.do?refresh=true
        if ("true".equals(refresh)) {
            builder.regenerate();
        }
        
        //document.do?method=detail
        if ("detail".equals(method)) {
            return detail(request, response);
        }
        else if ("list".equals(method)) {
            return list(request, response);
        }
        else if ("model".equals(method)) {
            return model(request, response);
        }
        
        //默认定位到列表
        return list(request, response);
        
    }
    
    //显示服务文档列表
    public ModelAndView list(HttpServletRequest request, HttpServletResponse response) {
        Collection<ServiceDesc> serviceDescs = builder.fetchAllServiceDesc();
        processTemplate("/resources_support/tpl/list.tpl", serviceDescs, request, response);
        return null;
    }
    
    //显示具体的服务接口
    public ModelAndView detail(HttpServletRequest request, HttpServletResponse response) {
        
        String className = request.getParameter("className");
        String url = request.getParameter("url");
        if (url == null || url.trim().length() == 0) {
            processTemplate("/resources_support/tpl/detail.tpl", new HashMap<Object,Object>(), request, response);
            return  null;
        }
        
        ServiceDesc serviceDesc = builder.fetchServiceDesc(className);
        if (serviceDesc == null || serviceDesc.getMethods() == null || serviceDesc.getMethods().size() == 0) {
            processTemplate("/resources_support/tpl/detail.tpl", new HashMap<Object,Object>(), request, response);
            return  null;
        }
        
        Map<String, Object> results = new HashMap<String, Object>();
        results.put("service", serviceDesc);
        
        for (int i = 0; i < serviceDesc.getMethods().size(); ++i) {
            if (url.equals(serviceDesc.getMethods().get(i).getUrl())) {
                results.put("method", serviceDesc.getMethods().get(i));
                processTemplate("/resources_support/tpl/detail.tpl", results, request, response);
                return  null;
            }
        }
        
        processTemplate("/resources_support/tpl/detail.tpl", new HashMap<Object,Object>(), request, response);
        return  null;
    }
    
    // 显示实体模型描述 
    public ModelAndView model(HttpServletRequest request, HttpServletResponse response) {
        String className = request.getParameter("className");
        ModelDesc modelDesc = builder.fetchModelDesc(className);
        processTemplate("/resources_support/tpl/model.tpl", modelDesc, request, response);
        return null;
    }
    
    protected void processTemplate(String tplName, Object data, HttpServletRequest request, HttpServletResponse response) {
        try {
            request.setCharacterEncoding("UTF-8");
            response.setContentType("text/html;charset=UTF-8");
            Template t = Velocity.getTemplate(tplName, "UTF-8");
            //取得VelocityContext对象  
            VelocityContext context = new VelocityContext();
            //向context中放入要在模板中用到的数据对象  
            context.put("data", data);
            context.put("basePath", request.getContextPath());
            StringWriter sw = new StringWriter();
            t.merge(context, sw);
            response.getWriter().write(sw.toString());
            response.flushBuffer();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    /** {@inheritDoc} */
    @Override
    protected void setModuleName() {
    }
}
