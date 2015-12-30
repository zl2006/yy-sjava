/*
* 文 件 名:  EncryptUtils.java
* 版    权:  YY Technologies Co., Ltd. Copyright 2012-2013,  All rights reserved
* 描    述:  <描述>
* 修 改 人:  zhouliang
* 修改时间:  2015年12月24日
* 修改内容:  <修改内容>
*/
package org.yy.framework.util.lang;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.yy.framework.util.string.GenerateConstants;

/**
 * 签名工具类
 * 
 * @author zhouliang
 * @version [1.0, 2015年12月24日]
 * @since [framework-util/1.0]
 */
public final class SignUtils {

	private SignUtils() {
	}

	/**
	 * 获取一位校验和
	 */
	public static String oneCheckSum(String content) {
		long total = 0;
		for (int i = 0; i < content.length(); ++i) {
			total += content.charAt(i);
		}
		long mod = total % GenerateConstants.RADIX;
		return RadixUtils.convertNumber(mod, GenerateConstants.RADIX);
	}

	/**
	 * 获取二位校验和
	 */
	public static String twoCheckSum(String content) {
		long total = 0;
		for (int i = 0; i < content.length(); ++i) {
			total += content.charAt(i);
		}
		long mod = total % (GenerateConstants.RADIX * GenerateConstants.RADIX);
		String result = RadixUtils.convertNumber(mod, GenerateConstants.RADIX);
		return StringUtils.leftPad(result, 2, "0");
	}

	/**
	 * 签名
	 */
	public static String sign(String decript, ALGORITHM_TYPE type) {
		MessageDigest digest = null;
		try {
			digest = java.security.MessageDigest.getInstance(type.value);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("签名失败", e);
		}
		digest.update(decript.getBytes());
		return StringUtils.byteArrayToHex(digest.digest());
	}

	/**
	 * 签名
	 */
	public static String sign(File file, ALGORITHM_TYPE type) {
		MessageDigest digest = null;
		FileInputStream in = null;
		try {
			digest = java.security.MessageDigest.getInstance(type.value);
			in = new FileInputStream(file);
			MappedByteBuffer byteBuffer = in.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, file.length());
			digest.update(byteBuffer);
			return StringUtils.byteArrayToHex(digest.digest());
		} catch (Exception e) {
			throw new RuntimeException("签名失败", e);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					// /e.printStackTrace();
				}
			}
		}
	}

	// 算法类型
	public enum ALGORITHM_TYPE {

		MD5("MD5"), SHA1("SHA-1"), SHA("SHA");
		private String value;

		private ALGORITHM_TYPE(String value) {
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}
	}

	public static void main(String[] args) {
		System.out.println(SignUtils.oneCheckSum(""));
		System.out.println(SignUtils.oneCheckSum("abcadsfasdfasde"));
		System.out.println(SignUtils.twoCheckSum(""));
		System.out.println(SignUtils.twoCheckSum("abcdefadsfasdfasfasdfdasfddsddfafdasdfdddfffd"));

		System.out.println(SignUtils.sign("sss", ALGORITHM_TYPE.MD5));
		System.out.println(
				SignUtils.sign(new File("/data/test.txt"), ALGORITHM_TYPE.SHA1));
	}
}
