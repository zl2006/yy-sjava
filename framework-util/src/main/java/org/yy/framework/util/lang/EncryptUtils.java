/*
 * 文 件 名:  EncryptUtils.java
 * 版    权:  YY Technologies Co., Ltd. Copyright 2012-2013,  All rights reserved
 * 描    述:  加解密工具类
 * 修 改 人:  zhouliang
 * 修改时间:  2015年12月24日
 * 修改内容:  <修改内容>
 */
package org.yy.framework.util.lang;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

/**
 * 加解密工具类
 * 
 * @author zhouliang
 * @version [1.0, 2015年12月24日]
 * @since [framework-util/1.0]
 */
public class EncryptUtils {

	public static void main(String[] args) throws Exception {
		String content = "我爱你";
		System.out.println("加密前：" + content);

		String key = "123456";
		System.out.println("加密密钥和解密密钥：" + key);

		String encrypt = encrypt(content, key, ALGORITHM_TYPE.AES);
		System.out.println("加密后：" + encrypt);

		String decrypt = decrypt(encrypt, key, ALGORITHM_TYPE.AES);
		System.out.println("解密后：" + decrypt);
	}

	/**
	 * AES加密
	 * 
	 * @param content
	 *            待加密的内容
	 * @param encryptKey
	 *            加密密钥
	 * @return 加密后的byte[]
	 * @throws Exception
	 */
	public static byte[] encrypt(byte[] content, String encryptKey,
			ALGORITHM_TYPE type) throws Exception {
		SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
		secureRandom.setSeed(encryptKey.getBytes());

		KeyGenerator kgen = KeyGenerator.getInstance(type.value);
		kgen.init(secureRandom);

		Cipher cipher = Cipher.getInstance(type.value);
		cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(kgen.generateKey()
				.getEncoded(), type.value));

		return cipher.doFinal(content);
	}

	/**
	 * AES加密为base 64 code
	 * 
	 * @param content
	 *            待加密的内容
	 * @param encryptKey
	 *            加密密钥
	 * @return 加密后的base 64 code
	 * @throws Exception
	 */
	public static String encrypt(String content, String encryptKey,
			ALGORITHM_TYPE type) throws Exception {
		return Base64.encodeBase64String(encrypt(content.getBytes("utf-8"),
				encryptKey, type));
	}

	/**
	 * AES解密
	 * 
	 * @param encryptBytes
	 *            待解密的byte[]
	 * @param decryptKey
	 *            解密密钥
	 * @return 解密后的String
	 * @throws Exception
	 */
	public static String decrypt(byte[] encryptBytes, String decryptKey,
			ALGORITHM_TYPE type) throws Exception {
		SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
		secureRandom.setSeed(decryptKey.getBytes());

		KeyGenerator kgen = KeyGenerator.getInstance(type.value);
		kgen.init(secureRandom);

		Cipher cipher = Cipher.getInstance(type.value);
		cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(kgen.generateKey()
				.getEncoded(), type.value));
		byte[] decryptBytes = cipher.doFinal(encryptBytes);

		return new String(decryptBytes);
	}

	/**
	 * 将base 64 code AES解密
	 * 
	 * @param encryptStr
	 *            待解密的base 64 code
	 * @param decryptKey
	 *            解密密钥
	 * @return 解密后的string
	 * @throws Exception
	 */
	public static String decrypt(String encryptStr, String decryptKey,
			ALGORITHM_TYPE type) throws Exception {
		return decrypt(Base64.decodeBase64(encryptStr), decryptKey, type);
	}

	// 算法类型
	public enum ALGORITHM_TYPE {

		DES("DES"), AES("AES");
		private String value;

		private ALGORITHM_TYPE(String value) {
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}
	}

}
