/*
* 文 件 名:  FrameworkXtreamView.java
* 版    权:  YY Technologies Co., Ltd. Copyright 2012-2013,  All rights reserved
* 描    述:   约定格式的XML处理
* 修 改 人:  zhouliang
* 修改时间:  2015年4月20日
* 修改内容:  <修改内容>
*/

package org.yy.framework.base.view;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.stream.StreamResult;

import org.springframework.oxm.Marshaller;
import org.springframework.util.Assert;
import org.springframework.web.servlet.view.AbstractView;
import org.yy.framework.basedata.Constants;

/**
 * 约定格式的XML处理
 */
public class FrameworkXtreamView extends AbstractView {
    public static final String DEFAULT_CONTENT_TYPE = "application/xml";
    
    private Marshaller marshaller;
    
    public FrameworkXtreamView() {
        setContentType(DEFAULT_CONTENT_TYPE);
        setExposePathVariables(false);
    }
    
    public FrameworkXtreamView(Marshaller marshaller) {
        this();
        Assert.notNull(marshaller, "Marshaller must not be null");
        this.marshaller = marshaller;
    }
    
    public void setMarshaller(Marshaller marshaller) {
        this.marshaller = marshaller;
    }
    
    @Override
    protected void initApplicationContext() {
        Assert.notNull(this.marshaller, "Property 'marshaller' is required");
    }
    
    @Override
    protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request,
        HttpServletResponse response)
        throws Exception {
        
        Object toBeMarshalled = locateToBeMarshalled(model);
        if (toBeMarshalled == null) {
            throw new IllegalStateException("Unable to locate object to be marshalled in model: " + model);
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);
        this.marshaller.marshal(toBeMarshalled, new StreamResult(baos));
        
        setResponseContentType(request, response);
        response.setContentLength(baos.size());
        baos.writeTo(response.getOutputStream());
    }
    
    /**
     * 获取需要转换成xml的属性对象
     */
    protected Object locateToBeMarshalled(Map<String, Object> model)
        throws IllegalStateException {
        Map<String, Object> result = new HashMap<String, Object>();
        for (String item : Constants.RESULT_NAMES) {
            result.put(item, model.get(item));
        }
        return result;
    }
    
}
