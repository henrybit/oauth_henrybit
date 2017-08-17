package com.jf.oauth.core.tools;

/**
 * 数值工具<br>
 * @author qiph
 * @version 1.0
 */
public class NumberTools {
	
	/**
	 * 字符串转Long
	 * @param str
	 * @return long
	 */
	public static long parseLong(String str) {
		try {
			return Long.valueOf(str);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1L;
	}
	
	/**
	 * 字符串转Int
	 * @param str
	 * @return int
	 */
	public static int parseInt(String str) {
		try {
			return Integer.valueOf(str);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}
}
