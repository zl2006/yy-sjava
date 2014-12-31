/*
 * 文 件 名:  InitData.java
 * 版    权:  Tydic Technologies Co., Ltd. Copyright 1993-2012,  All rights reserved
 * 描    述:  DAO层单元测试时的数据初始化
 * 修 改 人:  agan.zhoul
 * 修改时间:  2012-9-4
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package org.yy.framework.base.test;

/**
 * 数据初始化接口
 * 
 * @author zhouliang
 * 
 */
public interface InitDB {
	/**
	 * 初始化数据
	 */
	public void initDB();

	/**
	 * 数据清理
	 */
	public void cleanDB();
}
