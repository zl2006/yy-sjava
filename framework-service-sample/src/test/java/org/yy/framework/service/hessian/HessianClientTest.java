package org.yy.framework.service.hessian;

import javax.annotation.Resource;

import org.junit.Test;
import org.yy.framework.base.test.AbstractSpringTest;
import org.yy.framework.service.hessian.TestServer;

/*
* 文 件 名:  HessianClient.java
* 版    权:  YY Technologies Co., Ltd. Copyright 2012-2013,  All rights reserved
* 描    述:  <描述>
* 修 改 人:  zhouliang
* 修改时间:  2015年10月13日
* 修改内容:  <修改内容>
*/

/**
* <一句话功能简述>
* <功能详细描述>
* 
* @author  zhouliang
* @version  [版本号, 2015年10月13日]
* @since  [产品/模块版本]
*/
public class HessianClientTest extends AbstractSpringTest {
    
    @Resource(name = "testServer")
    TestServer testServer;
    
    @Test
    public void testSayHello() {
        testServer.sayHello();
    }
}
