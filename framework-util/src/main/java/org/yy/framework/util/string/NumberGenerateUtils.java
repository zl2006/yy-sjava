/*
* 文 件 名:  BarcodeUtils.java
* 版    权:  YY Technologies Co., Ltd. Copyright 2012-2013,  All rights reserved
* 描    述:  <描述>
* 修 改 人:  zhouliang
* 修改时间:  2015年12月24日
* 修改内容:  <修改内容>
*/
package org.yy.framework.util.string;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.yy.framework.util.lang.RadixUtils;
import org.yy.framework.util.lang.StringUtils;

/**
 * 1.条形数字码生成工具类<br>
 * 2.避免生成多个实例,会占用大量内存,支持多线程<br>
 * 3.使用完后请把this设置为空<br>
 * 4,每个批次支持100W个数据<br>
 * 
 * @author zhouliang
 * @version [1.0, 2015年12月24日]
 * @since [framework-util/1.0]
 */
public final class NumberGenerateUtils {

	// 充当锁
	private static Object lock = new Object();

	// 单例
	private static NumberGenerateUtils numberUtils;

	// 数字字符串的最大总长度
	private static int MAX_NUM_LENGTH = 50;

	// 最大初始化的随机数长度为6, 0-999999
	private static int MAX_INITDATA_LEN = 6;

	// 固定批次
	private int fixBatch;

	// 批次在数据字符串中的位置
	private int[] fixBatchPosi;

	// 原始组
	private BATCHEACHBIT_GROUP_NUM orgiGroupNum;

	// 批次中每位数字可变化的组数
	private int groupNum;

	// 返回数据的总长度
	private int length;

	// 批次长度
	private int fixBatchLen;

	// 随机数长度
	private int randomLen;

	// 剩余数量
	private int remain;

	// 随机数据, 最大只允许100W数据
	private int[] data;

	// 分组
	private char[][] group;

	private NumberGenerateUtils() {
	}

	/**
	 * 获取一个实例, 只要批次重复就会利用旧的实例
	 * 
	 * @param fixBatch
	 *            批次
	 * @param fixBatchPosi
	 *            批次占用数字字符串位置
	 * @param groupNum
	 *            批次中每位数字的变化组数
	 * @param length
	 *            数据总长度,最大支持50
	 */
	public static NumberGenerateUtils getInstance(int fixBatch, int[] fixBatchPosi, BATCHEACHBIT_GROUP_NUM groupNum,
			int length) {
		synchronized (lock) {
			if (numberUtils == null) {
				numberUtils = new NumberGenerateUtils(fixBatch, fixBatchPosi, groupNum, length);
				return numberUtils;
			}
			if (fixBatch == numberUtils.fixBatch) {
				return numberUtils;
			} else {
				numberUtils.reset(fixBatch, fixBatchPosi, groupNum, length);
				return numberUtils;
			}
		}
	}

	/**
	 * 获取数据, 返回null时表示已无数据
	 */
	public String next() {
		synchronized (lock) {
			if (remain == 0) {
				return null;
			}

			// 获取批次字符串
			String fixBatchStr = StringUtils.leftPad(RadixUtils.convertNumber(this.fixBatch, groupNum),
					fixBatchPosi.length, "0");

			// 获取随机数字字符串
			int leftPad = 0;
			if (randomLen > MAX_INITDATA_LEN) {
				leftPad = MAX_INITDATA_LEN;
			} else {
				leftPad = randomLen;
			}
			int index = (int) (Math.random() * remain);
			int temp = data[index];
			data[index] = data[remain - 1];
			remain--;
			String random = StringUtils.leftPad("" + temp, leftPad, "0");

			// 获取扩展字符串
			int extLen = 0;
			if (randomLen > MAX_INITDATA_LEN) {
				extLen = randomLen - MAX_INITDATA_LEN;
			}
			String extRandomStr = random(extLen);

			random = random + extRandomStr;
			StringBuilder result = new StringBuilder();
			result.append(StringUtils.leftPad("", this.length, " "));
			for (int i = 0; i < fixBatchStr.length(); ++i) {
				int d = Integer.parseInt(fixBatchStr.substring(i, i + 1));
				result.setCharAt(fixBatchPosi[i] - 1, group[d][(int) (Math.random() * group[d].length)]);
			}

			for (int i = 0, j = 0; i < this.length; ++i) {
				if (result.charAt(i) == ' ') {
					result.setCharAt(i, random.charAt(j));
					++j;
				}
			}
			return result.toString();
		}
	}

	/**
	 * 获取一组数据, 返回null时表示已无数据
	 */
	public String[] next(int num) {
		synchronized (lock) {
			if (remain < num) {
				return null;
			}
			String[] s = new String[num];
			for (int i = 0; i < num; ++i) {
				s[i] = next();
			}
			return s;
		}
	}

	/**
	 * 当前批次
	 */
	public int currentBatch() {
		synchronized (lock) {
			return this.fixBatch;
		}
	}

	/**
	 */
	private NumberGenerateUtils(int fixBatch, int[] fixBatchPosi, BATCHEACHBIT_GROUP_NUM groupNum, int length) {
		reset(fixBatch, fixBatchPosi, groupNum, length);
	}

	/**
	 * 切换到下一个批次
	 */
	public void switchBatch(int fixBatch) {
		reset(fixBatch, this.fixBatchPosi, this.orgiGroupNum, this.length);
	}

	/**
	 * 重置批次
	 */
	private void reset(int fixBatch, int[] fixBatchPosi, BATCHEACHBIT_GROUP_NUM argGroupNum, int length) {
		if (argGroupNum == null) {
			throw new IllegalArgumentException("批次中每位数字的变化组数不能为空");
		}

		this.fixBatchPosi = fixBatchPosi;
		this.groupNum = argGroupNum.value;
		this.orgiGroupNum = argGroupNum;
		this.length = length;
		this.fixBatch = fixBatch;

		validLength();
		validfixBatch();
		validFixBatchPosi();

		this.fixBatchLen = (fixBatchPosi == null ? 0 : fixBatchPosi.length);
		this.randomLen = this.length - fixBatchLen;

		// 初始化:this.remain this.data;
		initData();
		// this.group
		splitGroup();
		// this.fixBatchPosi
		sort();
	}

	// 分组
	private void splitGroup() {
		group = new char[groupNum][];
		int div = 10 / groupNum;
		int mod = 10 % groupNum;

		int num = 0;
		for (int i = 0; i < groupNum; ++i) {
			if (i < mod) {
				group[i] = new char[div + 1];
			} else {
				group[i] = new char[div];
			}

			for (int j = 0; j < group[i].length; ++j, ++num) {
				group[i][j] = GenerateConstants.NUMBER_STR[num];
			}
		}
	}

	// 排序批准占位
	private void sort() {
		if (fixBatchPosi == null || fixBatchPosi.length == 0) {
			return;
		}
		List<Integer> temp = new ArrayList<Integer>();
		for (int i = 0; i < fixBatchPosi.length; ++i) {
			temp.add(fixBatchPosi[i]);
		}
		Collections.sort(temp);
		fixBatchPosi = new int[temp.size()];
		for (int i = 0; i < temp.size(); ++i) {
			fixBatchPosi[i] = temp.get(i);
		}
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		if (randomLen <= 0) {
			return;
		}

		if (randomLen >= MAX_INITDATA_LEN) {
			this.remain = (int) Math.pow(10, MAX_INITDATA_LEN);
		} else {
			this.remain = (int) Math.pow(10, randomLen);
		}
		data = new int[remain];

		for (int i = 0; i < remain; ++i) {
			data[i] = i;
		}
	}

	/**
	 * 随机一个数字字符串
	 */
	private String random(int len) {
		char[] chArray = new char[len];
		int idx = 0;
		for (int i = 0; i < len; ++i) {
			idx = (int) (Math.random() * GenerateConstants.NUMBER_STR.length);
			chArray[i] = GenerateConstants.NUMBER_STR[idx];
		}
		return String.valueOf(chArray);
	}

	/**
	 * 验证批次是否合法
	 */
	private void validfixBatch() {
		// 批次不能为负数, 例如-1
		if (fixBatch < 0) {
			throw new IllegalArgumentException("批次(" + fixBatch + ")必须大于等于0");
		}
		// 批次转化为组数位变化后不能超出长度
		int fixBatchLen = RadixUtils.convertNumber(fixBatch, groupNum).length();
		if (fixBatchLen > length) {
			throw new IllegalArgumentException("批次的长度" + fixBatch + "(" + fixBatchLen + ")必须小于等于数据总长度");
		}
	}

	/**
	 * 验证数据总长度
	 */
	private void validLength() {
		// 数据总长度必须大于0并且小于等于50, 例如0, -1
		if (length <= 0 || length > MAX_NUM_LENGTH) {
			throw new IllegalArgumentException("数据总长度" + length + "必须大于0小于等于50");
		}
	}

	/**
	 * 验证批次占用哪些位
	 */
	private void validFixBatchPosi() {

		if (fixBatchPosi == null) {
			return;
		}

		// 批次占位的长度不能大于数据总长度, 例如, fixBatchPosi[1,4,5,9,11], 而length为4时.
		if (fixBatchPosi.length > length) {
			throw new IllegalArgumentException("批次占位的长度必须小于等于数据总长度");
		}

		// 批次中占位的值不能大于总长度,例如fixBatchPosi[1,4,41,1],而length为40时
		for (int i = 0; i < fixBatchPosi.length; ++i) {
			if (fixBatchPosi[i] > length || fixBatchPosi[i] < 0) {
				throw new IllegalArgumentException("批次中占位的值必须小于等于数据总长度,同时大于0");
			}
		}

		// 批次的占位不能有同样的位置,例如fixBatchPosi[1,1,2,3]
		int[] temp = new int[MAX_NUM_LENGTH + 1];
		for (int i = 0; i < fixBatchPosi.length; ++i) {
			temp[fixBatchPosi[i]] = ++temp[fixBatchPosi[i]];
			if (temp[fixBatchPosi[i]] > 1) {
				throw new IllegalArgumentException("批次中占位的值重复.");
			}
		}

		// 批次长度不能走出占位长度
		int fixBatchLen = RadixUtils.convertNumber(fixBatch, groupNum).length();
		if (fixBatchLen > fixBatchPosi.length) {
			throw new IllegalArgumentException("批次太大(" + fixBatch + "), 不能正常生成数字.");
		}
	}

	// 批次中第一位可变化的组数
	// 例如:
	// 组数为7时, 对应的组为(0,1)(2,3)(4,5)(6)(7)(8)(9)
	// 当批次上某位数为0时表示可以随机为(0,1),为1时可以随机为(2,3), 为6,7,8,9时值不变
	// 批次123时,生成的数字可能为024, 025, 034,035, 124,125,134, 135
	public enum BATCHEACHBIT_GROUP_NUM {

		TWO(2), THREE(3), FOUR(4), FIVE(5), SIX(6), SEVEN(7), EIGHT(8), NINE(9), TEN(10);

		private int value;

		private BATCHEACHBIT_GROUP_NUM(int value) {
			this.value = value;
		}

		public int getValue() {
			return this.value;
		}
	}

	public static void main(String[] args) {
//		 NumberGenerateUtils.getInstance(-1, new int[] {1, 2, 11},
//		 BATCHEACHBIT_GROUP_NUM.FIVE, 10);
//		 NumberGenerateUtils.getInstance(0, new int[] {1, 2, 3},
//		 BATCHEACHBIT_GROUP_NUM.FIVE, 10);
//		 NumberGenerateUtils.getInstance(124, new int[] {1, 2, 3},
//		 BATCHEACHBIT_GROUP_NUM.FIVE, 10);
//		 NumberGenerateUtils.getInstance(125, new int[] {1, 2, 3},
//		 BATCHEACHBIT_GROUP_NUM.FIVE, 3);
//
//		 NumberGenerateUtils.getInstance(1, new int[] {1, 2, 3,4,5,6},
//		 BATCHEACHBIT_GROUP_NUM.FIVE, 5);
//		 NumberGenerateUtils.getInstance(1, new int[] {1, 2, 11},
//		 BATCHEACHBIT_GROUP_NUM.FIVE, 5);
//		 NumberGenerateUtils.getInstance(1, new int[] {1, 2, 1},
//		 BATCHEACHBIT_GROUP_NUM.FIVE, 5);
//		 NumberGenerateUtils.getInstance(125, new int[] {1, 2, 3},
//		 BATCHEACHBIT_GROUP_NUM.FIVE, 10);
//		 NumberGenerateUtils.getInstance(1, new int[] {1, 1, 3},
//		 BATCHEACHBIT_GROUP_NUM.FIVE, 10);

		NumberGenerateUtils numberUtils = NumberGenerateUtils.getInstance(1, new int[] {3, 6, 9 },
				BATCHEACHBIT_GROUP_NUM.FIVE, 12);
		String[] s = numberUtils.next(12);
		for(int i = 0; i < s.length; ++i){
			System.out.println(s[i]);
		}
		numberUtils.switchBatch(numberUtils.currentBatch() + 1);
		s = numberUtils.next(12);
		for(int i = 0; i < s.length; ++i){
			System.out.println(s[i]);
		}
//		System.out.println("生成长度为4的随机数字，3位为批次, 数字用完后返回null");
//		NumberGenerateUtils numberUtils = NumberGenerateUtils.getInstance(111, new int[] { 1, 3, 4, },
//				BATCHEACHBIT_GROUP_NUM.FIVE, 4);
//		for (int i = 0; i < 11; ++i) {
//			System.out.println(numberUtils.next());
//		}
//
//		numberUtils = NumberGenerateUtils.getInstance(112, new int[] { 1, 2, 3, }, BATCHEACHBIT_GROUP_NUM.FIVE, 13);
//		System.out.println(numberUtils.next());
		// String[] data = numberUtils.next(10);
		// data = null;
	}
}
