/*
* 文 件 名:  SpringMVCXDocumentBuilder.java
* 版    权:  YY Technologies Co., Ltd. Copyright 2012-2013,  All rights reserved
* 描    述:  根据SpringMVC生成restful服务文档
* 修 改 人:  zhouliang
* 修改时间:  2013年11月27日
* 修改内容:  <修改内容>
*/
package org.yy.framework.document.builder;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.BeanUtils;
import org.springframework.core.MethodParameter;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.yy.framework.document.desc.ExceptionDesc;
import org.yy.framework.document.desc.FieldDesc;
import org.yy.framework.document.desc.MethodDesc;
import org.yy.framework.document.desc.ModelDesc;
import org.yy.framework.document.desc.ReturnDesc;
import org.yy.framework.document.desc.ServiceDesc;
import org.yy.framework.document.anno.Field;
import org.yy.framework.document.anno.Method;
import org.yy.framework.document.anno.Model;
import org.yy.framework.document.anno.Param;
import org.yy.framework.document.anno.Return;
import org.yy.framework.document.anno.Service;

/**
* 根据SpringMVC生成restful服务文档
* 
* @author  zhouliang
* @version  [1.0, 2013年11月27日]
* @since  [framework-webframe/1.0]
*/
public class SpringMVCXDocumentBuilder extends XDocumentBuilder {
    
    protected WebApplicationContext wc;
    
    /** 
    <默认构造函数>
    */
    public SpringMVCXDocumentBuilder(List<String> scanPackages, WebApplicationContext wc) {
        super(scanPackages);
        this.wc = wc;
        generate(this.wc);
    }
    
    @Override
    public void regenerate() {
        generate(wc);
    }
    
    /**
     * 生成整个服务化文档
     * @param wc
     */
    protected void generate(WebApplicationContext wc) {
        
        //临时存储服务文档数据
        Map<String, ServiceDesc> servicesDescMapTemp = new HashMap<String, ServiceDesc>();
        Map<String, ModelDesc> modelDescsTemp = new HashMap<String, ModelDesc>();
        
        //处理服务实体
        processModel(modelDescsTemp);
        
        //获取Spring中定义的所有mvc方法(RequestMappingInfo)
        RequestMappingHandlerMapping rmhp = wc.getBean(RequestMappingHandlerMapping.class);
        Map<RequestMappingInfo, HandlerMethod> map2 = rmhp.getHandlerMethods();
        
        //遍历RequestMappingInfo
        for (Iterator<RequestMappingInfo> iterator = map2.keySet().iterator(); iterator.hasNext();) {
            RequestMappingInfo info = iterator.next();
            HandlerMethod method = map2.get(info);
            
            //处理服务类
            ServiceDesc serviceDesc = processService(method.getBeanType().getName(), servicesDescMapTemp);
            if (serviceDesc == null) {
                continue;
            }
            else {
                servicesDescMapTemp.put(serviceDesc.getClassName(), serviceDesc);
            }
            
            //处理服务方法描述
            MethodDesc methodDesc = processMethod(method, info, modelDescsTemp);
            if (methodDesc == null) {
                continue;
            }
            else {
                serviceDesc.getMethods().add(methodDesc);
            }
            
        }
        
        this.servicesDescMap = servicesDescMapTemp;
        this.modelDescs = modelDescsTemp;
    }
    
    //处理服务类
    @SuppressWarnings({"rawtypes", "unchecked"})
    protected ServiceDesc processService(String className, Map<String, ServiceDesc> servicesDescMapTemp) {
        
        // 1, 判断是否已处理过此服务类
        ServiceDesc serviceDesc = servicesDescMapTemp.get(className);
        if (serviceDesc != null) {
            return serviceDesc;
        }
        
        // 2, 读取服务文档元数据配置
        serviceDesc = new ServiceDesc();
        try {
            
            Class clazz = this.getClass().getClassLoader().loadClass(className);
            Service serviceAnno = (Service)clazz.getAnnotation(Service.class);
            if (serviceAnno == null) { //为空时表示此类为非服务类
                return null;
            }
            
            // 2.1, 读取基础信息 
            serviceDesc.setClassName(className);
            serviceDesc.setName(serviceAnno.name());
            serviceDesc.setDescription(serviceAnno.desc());
            serviceDesc.setRole(serviceAnno.role());
            
            // 2.2, 读取关联实体
            Model[] modelAnnos = serviceAnno.models();
            if (modelAnnos != null && modelAnnos.length > 0) {
                for (Model item : modelAnnos) {
                    ModelDesc modelDesc = new ModelDesc();
                    modelDesc.setName(item.name());
                    modelDesc.setDescription(item.desc());
                    modelDesc.setClassName(item.clazz().getName());
                }
            }
            
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        
        return serviceDesc;
    }
    
    //处理服务方法描述
    protected MethodDesc processMethod(HandlerMethod method, RequestMappingInfo info,
        Map<String, ModelDesc> modelDescsTemp) {
        
        // 1,为空时表示此方法为非服务方法
        Method methodAnno = method.getMethodAnnotation(Method.class);
        if (methodAnno == null) {
            return null;
        }
        
        MethodDesc methodDesc = new MethodDesc();
        
        /*m.setConsumes(String.valueOf(info.getConsumesCondition()));
        m.setCustom(String.valueOf(info.getCustomCondition()));
        m.setHeaders(String.valueOf(info.getHeadersCondition()));
        m.setParams(String.valueOf(info.getParamsCondition()));
        m.setProduces(String.valueOf(info.getProducesCondition()));*/
        
        // 2, 读取方法描述基础信息
        methodDesc.setName(methodAnno.name());
        methodDesc.setDescription(methodAnno.desc());
        methodDesc.setUrl(info.getPatternsCondition().toString());
        if (methodDesc.getUrl() != null && methodDesc.getUrl().trim().length() > 2) {
            if (methodDesc.getUrl().startsWith("[")) {
                methodDesc.setUrl(methodDesc.getUrl().substring(1));
            }
            if (methodDesc.getUrl().endsWith("]")) {
                methodDesc.setUrl(methodDesc.getUrl().substring(0, methodDesc.getUrl().length() - 1));
            }
        }
        methodDesc.setMethodName(method.getMethod().getName());
        methodDesc.setClassName(method.getBeanType().getName());
        methodDesc.setRequestType(String.valueOf(info.getMethodsCondition()));
        
        // 3, 处理方法参数描述
        methodDesc.setRequestParams(processParam(methodAnno, methodDesc, method, modelDescsTemp));
        //m.setReturnType(method.getReturnType().getParameterType().toString());\
        
        MethodParameter[] parameters = method.getMethodParameters();
        List<String> list2 = new ArrayList<String>();
        for (MethodParameter methodParameter : parameters) {
            list2.add(methodParameter.getParameterType().getName());
        }
        methodDesc.setMethodParams(String.valueOf(list2));
        
        //4, 处理异常和结果
        methodDesc.setExceptions(processException(methodAnno));
        methodDesc.setReturns(processReturns(methodAnno));
        /*ResponseBody annotationClass = method.getMethodAnnotation(ResponseBody.class);
        if (annotationClass != null) {
            m.setAnnotationName(annotationClass.toString()); 
        }*/
        return methodDesc;
    }
    
    //处理服务方法参数描述
    @SuppressWarnings("rawtypes")
    protected List<FieldDesc> processParam(Method methodAnno, MethodDesc methodDesc, HandlerMethod method,
        Map<String, ModelDesc> modelDescsTemp) {
        
        //获取方法元注解
        List<FieldDesc> params = new ArrayList<FieldDesc>();
        
        //处理自定义参数
        Field[] fieldAnnos = methodAnno.inputParams();
        for (int i = 0; i < fieldAnnos.length; ++i) {
            FieldDesc fieldDesc = new FieldDesc();
            fieldToFieldDesc(fieldDesc, fieldAnnos[i]);
            if (fieldDesc.isFile()) {
                methodDesc.setHasfile(true);
            }
            fieldDesc.setClassname(fieldAnnos[i].clazz().getName());
            params.add(fieldDesc);
        }
        
        //处理参数来源于业务实体的情况
        Class[] paramClass = method.getMethod().getParameterTypes();
        Annotation[][] a = method.getMethod().getParameterAnnotations();
        
        if (paramClass == null || paramClass.length == 0) {
            return params;
        }
        for (int i = 0; i < a.length; ++i) { //遍历方法参数
            for (int j = 0; j < a[i].length; ++j) { //遍历方法中参数的每个元注解 
                if (a[i][j] instanceof Param) { //判断是否为参数
                
                    Param param = (Param)a[i][j];
                    
                    String[] inputParams = param.value();
                    // 取出参数实体描述
                    ModelDesc modelDesc = modelDescsTemp.get(paramClass[i].getName());
                    if (modelDesc == null) {
                        continue;
                    }
                    
                    //将实体类全部加入 
                    if (inputParams == null || inputParams.length == 0) {
                        for (FieldDesc item : modelDesc.getFields()) {
                            params.add(item);
                            if (item.isFile()) {
                                methodDesc.setHasfile(true);
                            }
                        }
                    }
                    else {//获取指定的部分
                        for (String temp : inputParams) {
                            FieldDesc fieldDesc = processNestedParam(modelDesc, temp, modelDescsTemp);
                            if (fieldDesc != null) {
                                params.add(fieldDesc);
                            }
                        }
                    }
                    
                }
            }
        }
        return params;
    }
    
    //处理嵌套参数
    protected FieldDesc processNestedParam(ModelDesc modelDesc, String paramStr, Map<String, ModelDesc> modelDescsTemp) {
        
        String[] strs = paramStr.split("\\.");
        if (strs == null || strs.length == 1) {
            return modelDesc.getFieldDesc(paramStr);
        }
        
        String fieldName = "";
        ModelDesc tempModel = modelDesc;
        FieldDesc tempField;
        boolean nullable = false;
        if (tempModel.getFieldDesc(strs[0]) != null) {
            nullable = tempModel.getFieldDesc(strs[0]).isNullable();
        }
        
        for (int i = 0; i < strs.length - 1; ++i) {
            tempField = tempModel.getFieldDesc(strs[i]);
            if (tempField == null || tempModel == null) {
                return null;
            }
            if (tempField.isIslist()) {
                fieldName = fieldName + strs[i] + "[0].";
            }
            else {
                fieldName = fieldName + strs[i] + ".";
            }
            tempModel = modelDescsTemp.get(tempField.getClassname());
        }
        
        tempField = tempModel.getFieldDesc(strs[strs.length - 1]);
        
        FieldDesc result = new FieldDesc();
        BeanUtils.copyProperties(tempField, result);
        result.setFieldName(fieldName + strs[strs.length - 1]);
        result.setNullable(nullable);
        return result;
    }
    
    //处理服务方法异常描述
    protected List<ExceptionDesc> processException(Method methodAnno) {
        
        //1, 获取方法元注解
        List<ExceptionDesc> exceptions = new ArrayList<ExceptionDesc>();
        if (methodAnno == null) {
            return exceptions;
        }
        
        //2, 处理参数
        org.yy.framework.document.anno.Exception[] excepAnnos = methodAnno.exceps();
        for (int i = 0; i < excepAnnos.length; ++i) {
            ExceptionDesc excepDesc = new ExceptionDesc();
            excepDesc.setCode(excepAnnos[i].code());
            excepDesc.setMessage(excepAnnos[i].message());
            exceptions.add(excepDesc);
        }
        return exceptions;
    }
    
    //处理处理返回结果集
    protected List<ReturnDesc> processReturns(Method methodAnno) {
        
        //获取方法元注解
        List<ReturnDesc> returnDescs = new ArrayList<ReturnDesc>();
        if (methodAnno == null) {
            return returnDescs;
        }
        
        Return[] returnAnnos = methodAnno.returns();
        //处理参数
        for (Return item : returnAnnos) {
            ReturnDesc returnDesc = new ReturnDesc();
            returnDesc.setName(item.name());
            returnDesc.setDescription(item.desc());
            returnDesc.setClassname(item.clazz().getName());
            returnDescs.add(returnDesc);
        }
        
        return returnDescs;
    }
    
    // 处理模型实体的文档
    protected void processModel(Map<String, ModelDesc> modelDescsTemp) {
        
        //1, 判定是否定义了实体所在包
        if (scanPackages == null || scanPackages.size() == 0) {
            return;
        }
        
        //2, 处理业务实体模型的文档描述
        for (String item : scanPackages) {
            Set<Class<?>> calssList = scanHandler.getPackageAllClasses(item, true);
            for (Class<?> cla : calssList) {
                Model modelAnno = cla.getAnnotation(Model.class);
                if (modelAnno == null) {
                    continue;
                }
                
                //2.1, 处理实体中的文档信息
                ModelDesc modelDesc = new ModelDesc();
                modelDesc.setClassName(cla.getName());
                modelDesc.setDescription(modelAnno.desc());
                modelDesc.setName(modelAnno.name());
                
                //2.2, 处理字段描述文档
                modelDesc.getFields().addAll(processField(cla, modelDesc));
                modelDescsTemp.put(modelDesc.getClassName(), modelDesc);
            }
        }
    }
    
    // 处理模型实体的字段文档
    @SuppressWarnings("rawtypes")
    protected List<FieldDesc> processField(Class clazz, ModelDesc modelDesc) {
        
        List<FieldDesc> fieldDescList = new ArrayList<FieldDesc>();
        java.lang.reflect.Field[] fields = clazz.getDeclaredFields();
        
        for (java.lang.reflect.Field field : fields) {
            
            Field fieldAnno = field.getAnnotation(Field.class);
            if (fieldAnno == null) {
                continue;
            }
            
            //处理字段描述
            FieldDesc fieldDesc = new FieldDesc();
            if (fieldAnno.clazz().equals(Class.class)) {
                fieldDesc.setClassname(field.getType().getName());
            }
            else {
                fieldDesc.setClassname(fieldAnno.clazz().getName());
            }
            
            fieldToFieldDesc(fieldDesc, fieldAnno);
            fieldDesc.setFieldName(field.getName());
            fieldDesc.setModelDesc(modelDesc);
            
            fieldDescList.add(fieldDesc);
        }
        
        return fieldDescList;
    }
    
    //
    protected void fieldToFieldDesc(FieldDesc fieldDesc, Field fieldAnno) {
        fieldDesc.setName(fieldAnno.cnname());
        fieldDesc.setFieldName(fieldAnno.name());
        fieldDesc.setDescription(fieldAnno.desc());
        fieldDesc.setType(fieldAnno.type().getCode());
        fieldDesc.setFormat(fieldAnno.format());
        fieldDesc.setIslist(fieldAnno.islist());
        fieldDesc.setLength(fieldAnno.length());
        fieldDesc.setNullable(fieldAnno.nullable());
    }
}
