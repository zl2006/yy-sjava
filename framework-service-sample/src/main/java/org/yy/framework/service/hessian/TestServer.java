/*
* 文 件 名:  HessianServer.java
* 版    权:  YY Technologies Co., Ltd. Copyright 2012-2013,  All rights reserved
* 描    述:  <描述>
* 修 改 人:  zhouliang
* 修改时间:  2015年10月13日
* 修改内容:  <修改内容>
*/
package org.yy.framework.service.hessian;

import org.yy.framework.service.anno.Hessian;

/**
* <一句话功能简述>
* <功能详细描述>
* 
* @author  zhouliang
* @version  [版本号, 2015年10月13日]
* @since  [产品/模块版本]
*/
@Hessian(app = "test_app", uri = "/demo/testServer", bean = "testServer", overloadEnabled = false)
public interface TestServer {
    public void sayHello();
}
