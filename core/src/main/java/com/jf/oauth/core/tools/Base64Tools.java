package com.jf.oauth.core.tools;

import org.apache.commons.codec.binary.Base64;

/**
 * RFC文档：https://tools.ietf.org/html/rfc4648#page-7<br>
 * <pre>
 * 考虑到金蝶使用的base64是1.3版本，所以采用自定义改造方案。base64的1.10版本自带了URL-SAFE编码模块。
 * </pre>
 * base64工具类<br>
 * 普通base64编码字母表
      0 A            17 R            34 i            51 z
         1 B            18 S            35 j            52 0
         2 C            19 T            36 k            53 1
         3 D            20 U            37 l            54 2
         4 E            21 V            38 m            55 3
         5 F            22 W            39 n            56 4
         6 G            23 X            40 o            57 5
         7 H            24 Y            41 p            58 6
         8 I            25 Z            42 q            59 7
         9 J            26 a            43 r            60 8
        10 K            27 b            44 s            61 9
        11 L            28 c            45 t            62 +
        12 M            29 d            46 u            63 /
        13 N            30 e            47 v
        14 O            31 f            48 w         (pad) =
        15 P            32 g            49 x
        16 Q            33 h            50 y
 * <br>
 * URL_SAFE的base64编码字母表
  0 A            17 R            34 i            51 z
         1 B            18 S            35 j            52 0
         2 C            19 T            36 k            53 1
         3 D            20 U            37 l            54 2
         4 E            21 V            38 m            55 3
         5 F            22 W            39 n            56 4
         6 G            23 X            40 o            57 5
         7 H            24 Y            41 p            58 6
         8 I            25 Z            42 q            59 7
         9 J            26 a            43 r            60 8
        10 K            27 b            44 s            61 9
        11 L            28 c            45 t            62 - (minus)
        12 M            29 d            46 u            63 _
        13 N            30 e            47 v           (underline)
        14 O            31 f            48 w
        15 P            32 g            49 x
        16 Q            33 h            50 y         (pad) =
 * @author qiph
 * @version 1.0
 */
public class Base64Tools {
	
	/**
	 *  base64编码
	 * @param oriData
	 * @return
	 */
	public static String encode(byte[] oriData) {
		try {
			//return Base64.encodeBase64String(oriData);
			return StringUtils.newStringUtf8(Base64.encodeBase64(oriData, false));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	
	/**
	 * base64编码
	 * @param oriStr
	 * @return string
	 */
	public static String encode(String oriStr) {
		try {
			byte[] binaryData = oriStr.getBytes("utf8");
			//return new String(Base64.encodeBase64(binaryData));
			//return Base64.encodeBase64String(binaryData);
			return StringUtils.newStringUtf8(Base64.encodeBase64(binaryData, false));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return oriStr;
	}
	
	/**
	 * base64编码(URL安全)
	 * @param oriData
	 * @return
	 */
	public static String encodeUrlSafe(byte[] oriData) {
		try {
			byte[] encodeBytes = Base64.encodeBase64(oriData, false);
			for (int i=0; encodeBytes!=null&&i<encodeBytes.length; i++) {
				if (encodeBytes[i]=='+')
					encodeBytes[i] = '-';
				else if (encodeBytes[i]=='/') {
					encodeBytes[i] = '_';
				}
			}
			return StringUtils.newStringUtf8(encodeBytes);
			//return Base64.encodeBase64URLSafeString(oriData);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	
	/**
	 * base64编码(URL安全)
	 * @param oriStr
	 * @return string
	 */
	public static String encodeUrlSafe(String oriStr) {
		try {
			byte[] binaryData = oriStr.getBytes("utf8");
			byte[] encodeBytes = Base64.encodeBase64(binaryData, false);
			for (int i=0; encodeBytes!=null&&i<encodeBytes.length; i++) {
				if (encodeBytes[i]=='+')
					encodeBytes[i] = '-';
				else if (encodeBytes[i]=='/') {
					encodeBytes[i] = '_';
				}
			}
			return StringUtils.newStringUtf8(encodeBytes);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return oriStr;
	}
	
	/**
	 * base64解码
	 * @param destOri
	 * @return String
	 */
	public static String decode(byte[] destOri) {
		try {
			for (int i=0; destOri!=null&&i<destOri.length; i++) {
				if (destOri[i]=='-')
					destOri[i] = '+';
				else if (destOri[i]=='_') {
					destOri[i] = '/';
				}
			}
			byte[] binaryData = Base64.decodeBase64(destOri);
			return StringUtils.newStringUtf8(binaryData);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	
	/**
	 * base64解码
	 * @param destStr
	 * @return string
	 */
	public static String decode(String destStr) {
		try {
			byte[] destData = destStr.getBytes();
			for (int i=0; destData!=null&&i<destData.length; i++) {
				if (destData[i]=='-')
					destData[i] = '+';
				else if (destData[i]=='_') {
					destData[i] = '/';
				}
			}
			byte[] binaryData = Base64.decodeBase64(destData);
			return StringUtils.newStringUtf8(binaryData);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return destStr;
	}
	
	/**
	 * base64解码
	 * @param destStr
	 * @return 
	 */
	public static byte[] decodeByte(String destStr) {
		try {
			byte[] destData = destStr.getBytes();
			for (int i=0; destData!=null&&i<destData.length; i++) {
				if (destData[i]=='-')
					destData[i] = '+';
				else if (destData[i]=='_') {
					destData[i] = '/';
				}
			}
			byte[] binaryData = Base64.decodeBase64(destData);
			return binaryData;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void main(String[] args) {
		String test = "123456;xdafda";
		String encodeStr = encode(test);
		String ntest = decode(encodeStr);
		System.out.println(encodeStr);
		System.out.println(ntest);
	}
}
