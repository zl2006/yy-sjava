/*
* 文 件 名:  QrcodeUtil.java
* 版    权:  YY Technologies Co., Ltd. Copyright 2012-2013,  All rights reserved
* 描    述:  <描述>
* 修 改 人:  zhouliang
* 修改时间:  2015年12月24日
* 修改内容:  <修改内容>
*/
package org.yy.framework.util.string;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.math.BigDecimal;
import java.util.Date;

import org.yy.framework.util.lang.EncryptUtils;
import org.yy.framework.util.lang.EncryptUtils.ALGORITHM_TYPE;
import org.yy.framework.util.lang.RadixUtils;
import org.yy.framework.util.lang.StringUtils;
import org.yy.framework.util.string.NumberGenerateUtils.BATCHEACHBIT_GROUP_NUM;

/**
 * 字符串生成工具类
 * 
 * @author zhouliang
 * @version [1.0, 2015年12月24日]
 * @since [framework-util/1.0]
 */
public final class StringGenerateUtils {

	private static BigDecimal base = new BigDecimal(GenerateConstants.GENERATE_RADIX);

	private static int CACHE_SIZE = 1000; // 缓存大小

	private StringGenerateUtils() {
	}

	/**
	 * 生成一个随机字符串
	 * 
	 * @param length
	 *            字符串长度
	 */
	public static String generateRandomStr(int length) {
		char[] chArray = new char[length];
		int idx = 0;
		for (int i = 0; i < length; ++i) {
			idx = (int) (Math.random() * GenerateConstants.GENERATE_RADIX);
			chArray[i] = GenerateConstants.RADIX_STR[idx];
		}
		return String.valueOf(chArray);
	}

	/**
	 * 生成一组随机字符串, 不保证重复性
	 * 
	 * @param length
	 *            字符串长度
	 * @param num
	 *            数量
	 */
	public static String[] generateRandomString(int length, int num) {
		String[] result = new String[num];
		for (int i = 0; i < num; ++i) {
			result[i] = generateRandomStr(length);
		}
		return result;
	}

	/**
	 * 根据指定的序列生成字符串,只支持十位长度的字符串. 因为long.MAX_VALUE变成64位字符串,最大只能为7++++++++++
	 * 当seq唯一时,产生的字符串不重复
	 * 
	 * @param seq
	 *            序列, seq > 0 and seq <= long.MAX_VALUE
	 * @param length
	 *            字符串长度, length> 0 and length <= 10
	 */
	public static String generateReqStr(long seq, int length) {
		if (seq <= 0) {
			throw new IllegalArgumentException("序列必须大于零");
		}
		if (length > 10 || length <= 0) {
			throw new IllegalArgumentException("不支持的字符串长度,只支持1-10");
		}

		// Step 1: 根据字符串的长度，确认其对应的最大十进制数为多少位,减1防止生成的字符串超出长度
		int maxTenRadixNumLen = ("" + (long) Math.pow(GenerateConstants.GENERATE_RADIX, length)).length() - 1;

		// Step 2: 将提供的数字填充后反转，例如1，填充后为000000001，然后反转为100000000
		String temp = "" + seq;
		temp = StringUtils.leftPad(temp, maxTenRadixNumLen, "0");
		temp = new StringBuilder(temp).reverse().toString();

		// Step3:转换成63进制
		String hex63 = RadixUtils.convertNumber(Long.parseLong(temp), GenerateConstants.GENERATE_RADIX);
		hex63 = StringUtils.leftPad(hex63, length, "0");

		// Step 4:使用加密表加密
		return encryptStr(hex63);
	}

	/**
	 * 根据序列产生一组不重复的字符串
	 * 
	 * @param startSeq
	 *            起始序列, startSeq> 0 and startSeq+num <= long.MAX_VALUE
	 * @param length
	 *            字符串长度, length > 0 and length<= 10
	 * @param num
	 *            数量
	 */
	public static String[] generateReqStr(long startSeq, int length, int num) {
		String[] result = new String[num];
		for (int i = 0; i < num; ++i) {
			result[i] = generateReqStr(startSeq + i, length);
		}
		return result;
	}

	/**
	 * 生成一组字符串, 由序列+随机部分组成
	 * 
	 * @param startSeq
	 *            序列值, > 0 and startSeq + num <= long.MAX_VALUE
	 * @param seqStrLen
	 *            序列部分长度, > 0 and <= 10
	 * @param num
	 *            数量
	 * @param dataHandler
	 *            数据处理器
	 */
	public static void generateSeqStr(long startSeq, int seqStrLen, int num, StringDataHandler dataHandler) {

		int times = num / CACHE_SIZE;
		int mod = num % CACHE_SIZE;
		for (int i = 0; i < times; ++i) {
			String[] s = generateReqStr(startSeq + i * CACHE_SIZE, seqStrLen, CACHE_SIZE);
			dataHandler.handle(s);
		}

		if (mod != 0) {
			String[] s = generateReqStr(startSeq + times * CACHE_SIZE, seqStrLen, mod);
			dataHandler.handle(s);
		}
	}

	/**
	 * 根据一个指定的序列生成字符串,支持任意长度的字符串.
	 * 
	 * @param seq
	 *            序列, seq > 0
	 * @param length
	 *            字符串长度, length > 0
	 */
	public static String generateReqStr(BigDecimal seq, int length) {

		if (seq.compareTo(BigDecimal.ZERO) <= 0) {
			throw new IllegalArgumentException("序列必须大于零");
		}
		if (length <= 0) {
			throw new IllegalArgumentException("不支持的字符串长度,必须大于0");
		}

		// Step 1: 根据字符串的长度，确认其对应的最大十进制数为多少位,减1防止生成的字符串超出长度
		int maxTenRadixNumLen = (base.pow(length).toString()).length() - 1;

		// Step 2: 将提供的数字填充后反转，例如1，填充后为000000001，然后反转为100000000
		String temp = seq.toString();
		temp = StringUtils.leftPad(temp, maxTenRadixNumLen, "0");
		temp = new StringBuilder(temp).reverse().toString();

		// Step 3:转换成63进制
		String hex63 = RadixUtils.convertBigDecimal(temp, GenerateConstants.GENERATE_RADIX);
		hex63 = StringUtils.leftPad(hex63, length, "0");

		// Step 4:使用加密表加密
		return encryptStr(hex63);
	}

	/**
	 * 根据序列产生一组不重复的字符串
	 * 
	 * @param startSeq
	 *            起始序列, startSeq> 0
	 * @param length
	 *            字符串长度, length> 0
	 * @param num
	 *            数量
	 */
	public static String[] generateReqStr(BigDecimal startSeq, int length, int num) {
		String[] result = new String[num];
		for (int i = 0; i < num; ++i) {
			result[i] = generateReqStr(startSeq.add(BigDecimal.ONE), length);
		}
		return result;
	}

	/**
	 * 生成一个字符串, 由序列+随机部分组成
	 * 
	 * @param seq
	 *            序列值,seq > 0 and seq <= long.MAX_VALUE
	 * @param seqStrLen
	 *            序列部分长度, seqStrLen > 0 and seqStrLen <= 10
	 * @param randomStrLen
	 *            随机部分长度
	 */
	public static String generateSeqAndRandomStr(long seq, int seqStrLen, int randomStrLen) {
		StringBuilder result = new StringBuilder();
		result.append(generateReqStr(seq, seqStrLen));
		result.append(generateRandomStr(randomStrLen));
		return result.toString();
	}

	/**
	 * 生成一组字符串, 由序列+随机部分组成
	 * 
	 * @param startSeq
	 *            序列值, > 0 and startSeq + num <= long.MAX_VALUE
	 * @param seqStrLen
	 *            序列部分长度, > 0 and <= 10
	 * @param randomStrLen
	 *            随机部分长度
	 * @param num
	 *            数量
	 */
	public static String[] generateSeqAndRandomStr(long startSeq, int seqStrLen, int randomStrLen, int num) {
		String[] result = new String[num];
		for (int i = 0; i < num; ++i) {
			result[i] = generateSeqAndRandomStr(startSeq + i, seqStrLen, randomStrLen);
		}
		return result;
	}

	/**
	 * 生成一组字符串, 由序列+随机部分组成
	 * 
	 * @param startSeq
	 *            序列值, > 0 and startSeq + num <= long.MAX_VALUE
	 * @param seqStrLen
	 *            序列部分长度, > 0 and <= 10
	 * @param randomStrLen
	 *            随机部分长度
	 * @param num
	 *            数量
	 * @param dataHandler
	 *            数据处理器
	 */
	public static void generateSeqAndRandomStr(long startSeq, int seqStrLen, int randomStrLen, int num,
			StringDataHandler dataHandler) {

		int times = num / CACHE_SIZE;
		int mod = num % CACHE_SIZE;
		for (int i = 0; i < times; ++i) {
			String[] s = generateSeqAndRandomStr(startSeq + i * CACHE_SIZE, seqStrLen, randomStrLen, CACHE_SIZE);
			dataHandler.handle(s);
		}

		if (mod != 0) {
			String[] s = generateSeqAndRandomStr(startSeq + times * CACHE_SIZE, seqStrLen, randomStrLen, mod);
			dataHandler.handle(s);
		}
	}

	/**
	 * 使用加密表加密字符串
	 */
	protected static String encryptStr(String str) {
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < str.length(); ++i) {
			int posi = RadixUtils.getCharIndexNum(str.charAt(i));
			result.append(GenerateConstants.ENCRY_RADIX_STR[i][posi]);
		}
		return result.toString();
	}

	// 批量生成字符串时, 每到达一个缓存量执行一次,默认缓存量为1000
	public static interface StringDataHandler {

		/**
		 * 处理字符串组,可以写文件等
		 * 
		 * @param data
		 *            字符串组
		 */
		public void handle(String[] data);
	}

	public static void main(String[] args) throws Exception {

		// 生成一个随机字符串
//		System.out.println("单个随机字符串:" + StringGenerateUtils.generateRandomStr(8));

		// 生成 一组随机字符串
		//String[] result = StringGenerateUtils.generateRandomString(8, 9);
//		System.out.println("一组随机字符串:");
//		for (int i = 0; i < result.length; ++i) {
//			System.out.println(result[i]);
//		}
//
//		// 利用数字序列生成一个字符串
//		System.out.println("利用一个long数字生成 一个字符串:" + StringGenerateUtils.generateReqStr(1, 5));
//		System.out.println("利用bigdecimal数字生成一个字符串:"
//				+ StringGenerateUtils.generateReqStr(new BigDecimal("123456789098765432123456"), 15));
//		// 利用数字生成一组字符串
//		result = StringGenerateUtils.generateReqStr(1, 8, 3);
//		System.out.println("利用数字生成一组字符串:");
//		for (int i = 0; i < result.length; ++i) {
//			System.out.println(result[i]);
//		}
		// 生成一组利用数字和随机数的字符串
//		result = StringGenerateUtils.generateSeqAndRandomStr(1, 8, 0, 3);
//		System.out.println("生成一组利用数字和随机数的字符串:");
//		for (int i = 0; i < result.length; ++i) {
//			System.out.println(result[i]);
//		}
//		// 生成后预处理
//		StringGenerateUtils.generateSeqAndRandomStr(1, 5, 5, 3, new StringDataHandler() {
//			@Override
//			public void handle(String[] data) {
//				for (int i = 0; i < data.length; ++i) {
//					System.out.println("加工后:" + "QR" + data[i]);
//				}
//			}
//		});
//
		// 综合实例
		File f = new File("/Volumes/workspace/test.txt");
		if (!f.exists()) {
			f.createNewFile();
		}
		long start1 = new Date().getTime();
		final BufferedWriter w = new BufferedWriter(new FileWriter(f));
		// 数字码工具类
		final NumberGenerateUtils numberUtils = NumberGenerateUtils.getInstance(0, new int[] { 1, 2, 3, 4 },
				BATCHEACHBIT_GROUP_NUM.FIVE, 10);
		
		// 二维码工具类，生成1000W码，从数字1开始生成，随机码有3位
		StringGenerateUtils.generateSeqAndRandomStr(20000, 7, 3, 10000000, new StringGenerateUtils.StringDataHandler() {
			@Override

			// 生成后加工处理
			public void handle(String[] data) {
				// 数字码切换批次
				StringBuilder sb = new StringBuilder();
				String[] number = numberUtils.next(data.length);
				if (number == null) {
					numberUtils.switchBatch(numberUtils.currentBatch() + 1);
					number = numberUtils.next(data.length);
				}
				// 写文件
				try {
					for (int i = 0; i < data.length; ++i) {
					    //System.out.println(data[i]);
						sb.append(data[i]);
						sb.append(EncryptUtils.encrypt(data[i], "password", ALGORITHM_TYPE.DES));
						sb.append(GenerateConstants.FIELD_SEP);
						sb.append(number[i]);
						sb.append(GenerateConstants.NEW_LINE);
					}
					w.write(sb.toString());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		w.close();
		long end1 = new Date().getTime();
		System.out.println((end1 - start1) / 1000);
		end1 = 0;

		// long start = new Date().getTime();
		/*
		 * for (int i = 1; i < 100000000; ++i) {
		 * //StringGenerateUtils.generateRandomStr(12);
		 * //StringGenerateUtils.generateReqStr(i, 10);
		 * StringGenerateUtils.generateReqStr(BigDecimal.valueOf(i), 10);
		 * //System.out.println(StringGenerateUtils.generateRandomStr(12)); }
		 */
		/*
		 * StringGenerateUtils.generateReqStr(BigDecimal.valueOf(10000001), 20);
		 * long end = new Date().getTime(); System.out.println((end - start) /
		 * 1000);
		 * 
		 * System.out.println(StringGenerateUtils.generateRandomString(12, 20));
		 * System.out.println(StringGenerateUtils.generateReqStr(1, 10, 50));
		 * System.out.println(StringGenerateUtils.generateReqStr(BigDecimal.ONE,
		 * 20, 50)); System.out.println(RadixUtils.unconvertBigDecimal(
		 * "4Q9xANsrU6CIAAAg0000", 64));
		 * 
		 * System.out.println(StringGenerateUtils.generateReqStr(1, 7));
		 * System.out.println(StringGenerateUtils.generateSeqAndRandomStr(1, 7,
		 * 5));
		 * 
		 * File f = new File("/data/test.txt"); if (!f.exists()) {
		 * f.createNewFile(); }
		 * 
		 * long start1 = new Date().getTime(); final BufferedWriter w = new
		 * BufferedWriter(new FileWriter(f)); final NumberGenerateUtils
		 * numberUtils = NumberGenerateUtils.getInstance(0, new int[] { 1, 2, 3,
		 * 4 }, BATCHEACHBIT_GROUP_NUM.FIVE, 10);
		 * StringGenerateUtils.generateSeqAndRandomStr(1, 10, 3, 10000000, new
		 * StringGenerateUtils.StringDataHandler() {
		 * 
		 * @Override public void handle(String[] data) { StringBuilder sb = new
		 * StringBuilder(); String[] number = numberUtils.next(data.length); if
		 * (number == null) { numberUtils.switchBatch(numberUtils.currentBatch()
		 * + 1); number = numberUtils.next(data.length); }
		 * 
		 * for (int i = 0; i < data.length; ++i) { sb.append(data[i]);
		 * sb.append(GenerateConstants.FIELD_SEP); sb.append(number[i]);
		 * sb.append(GenerateConstants.FIELD_SEP); sb.append("aaa");
		 * sb.append(GenerateConstants.FIELD_SEP); sb.append(new
		 * Date().getTime()); sb.append(GenerateConstants.NEW_LINE); } try {
		 * w.write(sb.toString()); } catch (IOException e) {
		 * e.printStackTrace(); } } }); w.close(); long end1 = new
		 * Date().getTime(); System.out.println((end1 - start1) / 1000); end1 =
		 * 0;
		 */

		// long[] a = new long[1000000];

		// a = null;
	}
}
