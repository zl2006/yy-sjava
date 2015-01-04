/*
* 文 件 名:  XDocumentBuilder.java
* 版    权:  YY Technologies Co., Ltd. Copyright 2012-2013,  All rights reserved
* 描    述:  服务化文档生成器
* 修 改 人:  zhouliang
* 修改时间:  2013年11月27日
* 修改内容:  <修改内容>
*/
package org.yy.framework.document.builder;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.yy.framework.document.desc.ModelDesc;
import org.yy.framework.document.desc.ServiceDesc;
import org.yy.framework.util.reflect.ClassPathScanHandler;

/**
* 服务化文档生成器
* 
* @author  zhouliang
* @version  [1.0, 2013年11月27日]
* @since  [framework-webframe/1.0]
*/
public abstract class XDocumentBuilder {
    
    /**
     * 服务接口描述
     */
    protected Map<String, ServiceDesc> servicesDescMap = new HashMap<String, ServiceDesc>();
    
    /**
     * 业务实体描述
     */
    protected Map<String, ModelDesc> modelDescs = new HashMap<String, ModelDesc>();
    
    /**
     * 类扫描器
     */
    protected ClassPathScanHandler scanHandler = new ClassPathScanHandler(true, true, null);
    
    /**
     * 扫描包
     */
    protected List<String> scanPackages = null;
    
    /**
     * @param scanPackages XDocumentBuilder
     */
    public XDocumentBuilder(List<String> scanPackages) {
        this.scanPackages = scanPackages;
    }
    
    /**
     * 文档重新生成
     */
    public abstract void regenerate();
    
    /**
     * 获取所有的服务接口描述
     * @return List<ServiceDesc>
     */
    public Collection<ServiceDesc> fetchAllServiceDesc() {
        return servicesDescMap.values();
    }
    
    /**
     * 获取具体的某个服务接口描述
     * @param serviceKey 服务接口标识，一般以类名做为标识，参考实现
     * @return ServiceDesc
     */
    public ServiceDesc fetchServiceDesc(String serviceKey) {
        return servicesDescMap.get(serviceKey);
    }
    
    /**
     * 获取获取某个业务实体描述
     * @param modelKey 实体标识，一般以类名做为标识，参考实现
     * @return ModelDesc
     */
    public ModelDesc fetchModelDesc(String modelKey) {
        return modelDescs.get(modelKey);
    }
}
