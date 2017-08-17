package com.jf.oauth.core.tools;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;

/**
 * RSA加密解密工具<br>
 * 
 * @author henrybit
 * @version 1.0
 */
public class RsaTools {
	public static final String KEY_ALGORITHM = "RSA";
	/** 貌似默认是RSA/NONE/PKCS1Padding，未验证 */
	public static final String CIPHER_ALGORITHM = "RSA/ECB/PKCS1Padding";
	public static final String PUBLIC_KEY = "publicKey";
	public static final String PRIVATE_KEY = "privateKey";
	/** RSA密钥长度必须是64的倍数，在512~65536之间。默认是1024 */
	public static final int KEY_SIZE = 2048;

	/**
	 * 生成密钥对。注意这里是生成密钥对KeyPair，再由密钥对获取公私钥
	 * @return
	 */
	public static Map<String, byte[]> generateKeyBytes() {
		try {
			KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(KEY_ALGORITHM);
			keyPairGenerator.initialize(KEY_SIZE);
			KeyPair keyPair = keyPairGenerator.generateKeyPair();
			RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
			RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
			Map<String, byte[]> keyMap = new HashMap<String, byte[]>();
			keyMap.put(PUBLIC_KEY, publicKey.getEncoded());
			keyMap.put(PRIVATE_KEY, privateKey.getEncoded());
			return keyMap;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static PublicKey restorePublicKey(byte[] keyBytes) {
		X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(keyBytes);
		try {
			KeyFactory factory = KeyFactory.getInstance(KEY_ALGORITHM);
			PublicKey publicKey = factory.generatePublic(x509EncodedKeySpec);
			return publicKey;
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static PrivateKey restorePrivateKey(byte[] keyBytes) {
		PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(keyBytes);
		try {
			KeyFactory factory = KeyFactory.getInstance(KEY_ALGORITHM);
			PrivateKey privateKey = factory.generatePrivate(pkcs8EncodedKeySpec);
			return privateKey;
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 加密，三步走。
	 * 
	 * @param key
	 * @param ori
	 * @return
	 */
	public static byte[] rsaEncode(PublicKey key, byte[] ori) {
		try {
			Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
			cipher.init(Cipher.ENCRYPT_MODE, key);
			return cipher.doFinal(ori);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 解密，三步走
	 * @param key
	 * @param encodeOri
	 * @return
	 */
	public static String rsaDecode(PrivateKey key, byte[] encodeOri) {
		try {
			Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
			cipher.init(Cipher.DECRYPT_MODE, key);
			return new String(cipher.doFinal(encodeOri));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void main(String[] args) throws Exception {
		String data = "hello world";
		Map<String, byte[]> keyMap = generateKeyBytes();
		PublicKey publicKey = restorePublicKey(keyMap.get(PUBLIC_KEY));
		System.out.println("公钥："+Base64Tools.encode(publicKey.getEncoded()));

		byte[] encodedText = rsaEncode(publicKey, data.getBytes());
		PrivateKey privateKey = restorePrivateKey(keyMap.get(PRIVATE_KEY));
		System.out.println("私钥："+Base64Tools.encode(privateKey.getEncoded()));
//		
//		String ndata = rsaDecode(privateKey, encodedText);
//		System.out.println("encode=" + new String(encodedText));
//		System.out.println("ndata=" + ndata);
		
//		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
//
//		X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(Base64Tools.decodeByte(SecurityTools.rsaPublicKeyStr));
//		PublicKey publicKey1 = keyFactory.generatePublic(x509EncodedKeySpec);
//		byte[] encodedText1 = rsaEncode(publicKey1, data.getBytes());
//		System.out.println(Base64Tools.encode(encodedText1));
//
//		PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(Base64Tools.decodeByte(SecurityTools.rsaPrivateKeyStr));
//		PrivateKey privateKey1 = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
//		String ndata1 = rsaDecode(privateKey1, encodedText1);
//		System.out.println("ndata="+ndata1);
	}
}
