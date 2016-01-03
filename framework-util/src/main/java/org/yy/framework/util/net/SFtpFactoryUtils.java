/*
 * 文 件 名:  FtpUtils.java
 * 版    权:  YY Technologies Co., Ltd. Copyright 2012-2015,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  zhouliang
 * 修改时间:  2015年12月23日
 * 修改内容:  <修改内容>
 */
package org.yy.framework.util.net;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yy.framework.util.lang.StringUtils;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpProgressMonitor;

/**
 * FTP工具类
 * 
 * @author zhouliang
 * @version [1.0, 2015年12月24日]
 * @since [framework-util/1.0]
 */
public final class SFtpFactoryUtils {

	private static final Logger LOG = LoggerFactory
			.getLogger(SFtpFactoryUtils.class);

	/**
	 * 生产一个channelSftp
	 * 
	 * @param ftpHost
	 *            主机
	 * @param port
	 *            端口
	 * @param ftpUserName
	 *            用户名
	 * @param ftpPassword
	 *            密码
	 * @param timeout
	 *            超时
	 * @return
	 * @throws JSchException
	 */
	public static ChannelSftp newChannelSftp(String ftpHost, int ftpPort,
			String ftpUserName, String ftpPassword, int timeout)
			throws JSchException {

		JSch jsch = new JSch(); // 创建JSch对象
		Session session = jsch.getSession(ftpUserName, ftpHost, ftpPort); // 根据用户名，主机ip，端口获取一个Session对象
		if (StringUtils.areNotEmpty(ftpPassword)) {
			session.setPassword(ftpPassword); // 设置密码
		}
		Properties config = new Properties();
		config.put("StrictHostKeyChecking", "no");
		session.setConfig(config); // 为Session对象设置properties
		session.setTimeout(timeout); // 设置timeout时间
		session.connect(); // 通过Session建立链接
		Channel channel = session.openChannel("sftp"); // 打开SFTP通道
		channel.connect(); // 建立SFTP通道的连接
		if (LOG.isDebugEnabled()) {
			LOG.debug("Connected successfully to ftpHost = " + ftpHost
					+ ",as ftpUserName = " + ftpUserName + ", returning: "
					+ channel);
		}
		return (ChannelSftp) channel;
	}

	/**
	 * 关闭
	 */
	public static void closeChannel(Channel channel) throws Exception {
		Session session = null;
		if (channel != null) {
			session = channel.getSession();
			channel.disconnect();
		}
		if (session != null) {
			session.disconnect();
		}
	}

	public static void main(String[] args) throws Exception {
		ChannelSftp sftpChannel = SFtpFactoryUtils.newChannelSftp(
				"120.27.133.42", 22, "root", "dic@300047123456", 10000);
		System.out.println(sftpChannel.pwd());
		sftpChannel.lcd("/Users/zhouliang/Downloads");
		sftpChannel
				.cd("/myws/sftp/mywljcode/.encrypt_code/20151218_50W_950W_1000W");
		sftpChannel.get("data-1.txt", "data-1.txt", new SftpProgressMonitor() {

			private long transfered;

			@Override
			public void init(int op, String src, String dest, long max) {
				System.out.println("Transferring begin.");
			}

			@Override
			public void end() {
				System.out.println("Transferring done.");
			}

			@Override
			public boolean count(long count) {
				transfered = transfered + count;
				System.out.println("Currently transferred total size: "
						+ transfered + " bytes");
				return true;
			}
		}, ChannelSftp.OVERWRITE);
		Thread.currentThread().join();
		SFtpFactoryUtils.closeChannel(sftpChannel);

	}
}
