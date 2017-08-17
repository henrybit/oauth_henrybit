package com.jf.oauth.core.tools;

/**
 * API参数封装安全工具(AES加密版本)
 * @author qiph
 * @version 1.0
 */
public class SecurityApiTools {
	//金蝶交互密码
	protected static String EAS_PASSWORD = "@321eas20170807!";
	
	/**
	 * 加密金蝶交互信息
	 * @param ori
	 * @return string
	 */
	public static String encodeEas(String ori) {
		try {
			byte[] encrypt = AESTools.encrypt(ori.getBytes(), EAS_PASSWORD);
			return Base64Tools.encodeUrlSafe(encrypt);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ori;
	}
	
	/**
	 * 解密金蝶交互信息
	 * @param dest
	 * @return string
	 */
	public static String decodeEas(String dest) {
		try {
			byte[] destBytes = Base64Tools.decodeByte(dest);
			byte[] decrypt = AESTools.decrypt(destBytes, EAS_PASSWORD);
			return new String(decrypt);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dest;
	}
	
	public static void main(String[] args) throws Exception{
		//时间戳+";"+客户端ID+";"+登陆账号
		String str = System.currentTimeMillis()+";10001;qiph@chinacdc.com";
		String encode = encodeEas(str);
		//System.out.println(URLEncoder.encode(encode, "utf-8"));
		System.out.println("加密："+encode);
		String decode = decodeEas(encode);
		System.out.println("解密："+decode);
	}
}
