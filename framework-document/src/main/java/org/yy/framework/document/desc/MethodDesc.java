/*
* 文 件 名:  MethodDesc.java
* 版    权:  YY Technologies Co., Ltd. Copyright 1993-2012,  All rights reserved
* 描    述:  服务文档请求服务方法描述
* 修 改 人:  zhouliang
* 修改时间:  2013年11月10日
*/
package org.yy.framework.document.desc;

import java.util.ArrayList;
import java.util.List;

/**
* 服务文档请求服务方法描述
* 
* @author  zhouliang
* @version  [1.0, 2013年11月10日]
* @since  [framework-basedata/1.0]
*/
public class MethodDesc {
    
    /**
     * 服务接口方法CN名称
     */
    private String name = "";
    
    /**
     * resetful请求路径
     */
    private String url = "";
    
    /**
     * 类名
     */
    private String className = "";
    
    /**
     * 方法名
     */
    private String methodName = "";
    
    /**
     * 方法参数
     */
    private String methodParams = "";
    
    /**
     * 方式请求,GET,POST
     */
    private String requestType = "";
    
    /**
     * 服务接口方法中文描述
     */
    private String description = "";
    
    /**
     * 是否包含文件上传
     */
    private boolean hasfile = false;
    
    /**
     * 参数描述
     */
    private List<FieldDesc> requestParams = new ArrayList<FieldDesc>();
    
    /**
     * 方法异常
     */
    private List<ExceptionDesc> exceptions = new ArrayList<ExceptionDesc>();
    
    /**
     * 返回结果
     */
    private List<ReturnDesc> returns = new ArrayList<ReturnDesc>();
    
    /**
    * @return 返回 name
    */
    public String getName() {
        return name;
    }
    
    /**
    * @param 对name进行赋值
    */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
    * @return 返回 url
    */
    public String getUrl() {
        return url;
    }
    
    /**
    * @param 对url进行赋值
    */
    public void setUrl(String url) {
        this.url = url;
    }
    
    /**
    * @return 返回 className
    */
    public String getClassName() {
        return className;
    }
    
    /**
    * @param 对className进行赋值
    */
    public void setClassName(String className) {
        this.className = className;
    }
    
    /**
    * @return 返回 methodName
    */
    public String getMethodName() {
        return methodName;
    }
    
    /**
    * @param 对methodName进行赋值
    */
    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }
    
    /**
    * @return 返回 requestParams
    */
    public List<FieldDesc> getRequestParams() {
        return requestParams;
    }
    
    /**
    * @param 对requestParams进行赋值
    */
    public void setRequestParams(List<FieldDesc> requestParams) {
        this.requestParams = requestParams;
    }
    
    /**
    * @return 返回 exceptions
    */
    public List<ExceptionDesc> getExceptions() {
        return exceptions;
    }
    
    /**
    * @param 对exceptions进行赋值
    */
    public void setExceptions(List<ExceptionDesc> exceptions) {
        this.exceptions = exceptions;
    }
    
    /**
    * @return 返回 returns
    */
    public List<ReturnDesc> getReturns() {
        return returns;
    }
    
    /**
    * @param 对returns进行赋值
    */
    public void setReturns(List<ReturnDesc> returns) {
        this.returns = returns;
    }
    
    /**
    * @return 返回 methodParams
    */
    public String getMethodParams() {
        return methodParams;
    }
    
    /**
    * @param 对methodParams进行赋值
    */
    public void setMethodParams(String methodParams) {
        this.methodParams = methodParams;
    }
    
    /**
    * @return 返回 requestType
    */
    public String getRequestType() {
        return requestType;
    }
    
    /**
    * @param 对requestType进行赋值
    */
    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }
    
    /**
    * @return 返回 description
    */
    public String getDescription() {
        return description;
    }
    
    /**
    * @param 对description进行赋值
    */
    public void setDescription(String description) {
        this.description = description;
    }
    
    /**
    * @return 返回 hasfile
    */
    public boolean isHasfile() {
        return hasfile;
    }
    
    /**
    * @param 对hasfile进行赋值
    */
    public void setHasfile(boolean hasfile) {
        this.hasfile = hasfile;
    }
    
    /** {@inheritDoc} */
    @Override
    public String toString() {
        return "MethodDesc [name=" + name + ", url=" + url + ", className=" + className + ", methodName=" + methodName
            + ", methodParams=" + methodParams + ", requestType=" + requestType + ", description=" + description
            + ", hasfile=" + hasfile + ", requestParams=" + requestParams + ", exceptions=" + exceptions + ", returns="
            + returns + "]";
    }
    
    //处理
    //private String produces = "";
    
    //返回值
    //private String returnType = "";
    
    //注解
    //private String annotationName = "";
    
    //需求
    //private String consumes = "";
    
    // 自定义
    //private String custom = "";
    
    //头信息
    //private String headers = "";
    
}
