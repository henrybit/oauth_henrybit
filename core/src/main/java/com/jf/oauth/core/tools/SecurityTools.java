package com.jf.oauth.core.tools;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * 安全加密工具（封装Base64和RSA
 * @author henrybit
 * @version 1.0
 */
public class SecurityTools {
	/**rsa公钥-加密端使用*/
	protected static String rsaPublicKeyStr = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEApHlPQavBreMzbGV32dXBzSGyjhpy5OzLjBiM8Wv1qlB1xoFpFkI3G+Kv1SsOoLoPhDwk+OyoEqghKDLkP6VnUIrkoCDT/WcSb00YKGYlbCUDOcIeFTeqSI+jSax7iB9j0Yh7U+uhfkYtgHIQyXirURXdayns9ocJbrBLDjSteTG1MUhFJ69DMWkJJPnUJpOsl8HhkdnD3RRx9YT8ItqeRkdIg/2+j0nA7LmQghFtAWOPw29pYMEonPE7IiDw14u/FDcQLcWQW/XLbIXHKSS4NVGvSeuo+/U3XgrBQDpJQO8ZfRyuWb4Paz7vPJ0VC/Kc/nsDhu2XHb5SfOMGTRG0rwIDAQAB";
	/**rsa私钥-解密端使用*/
	protected static String rsaPrivateKeyStr = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCkeU9Bq8Gt4zNsZXfZ1cHNIbKOGnLk7MuMGIzxa/WqUHXGgWkWQjcb4q/VKw6gug+EPCT47KgSqCEoMuQ/pWdQiuSgINP9ZxJvTRgoZiVsJQM5wh4VN6pIj6NJrHuIH2PRiHtT66F+Ri2AchDJeKtRFd1rKez2hwlusEsONK15MbUxSEUnr0MxaQkk+dQmk6yXweGR2cPdFHH1hPwi2p5GR0iD/b6PScDsuZCCEW0BY4/Db2lgwSic8TsiIPDXi78UNxAtxZBb9ctshccpJLg1Ua9J66j79TdeCsFAOklA7xl9HK5Zvg9rPu88nRUL8pz+ewOG7ZcdvlJ84wZNEbSvAgMBAAECggEAfyVETskfPXSBIU/DegOUAScGqHHr1er0Y4XrFwrVa+ctVNpfyZRAk+8G7KyPb1LGgfwYvNH+EAYnM7M73bPZDVuB6APYvXE7wA0CGuD0Fwj52GGm7dINcnCP03liOdXCTY3LgRGL01ke/cewEH+XOGTFuX08sUus0AO60e2JGUFEHV3FmV9y3l3J/oNW329ep0mHwvPNJUbJnkpx0tO/voMC+Hhcr9waZJFYMIS+5gIsHcO3Ynpt/3/MRm1/p4wazqEfpJlCBXHkjnPSdrGoINEAwFJ4S3et/iBsunoPSSfJEKcbpbWAB1v+mSlg6XzejDZoWyR/11h7XzmFRoA9CQKBgQDjTFRIlcENKLn8geFE31kx+F1EH+cUhZqpf+srYZz0Y0oL7QWkjalDok1CRy+VZECLLvAMuW2B5IWsKkN8Icw9ObUjeJn3ZX+1OMoUNZ4B4QrDGggVnP9sUFlt7UvTi9TQTzNIu7sUvlieFVNPN8LI3SJVQUFlvFGjDtmvi1mqSwKBgQC5Ph0g1vgowW2bLj0XRUwFxhfPKLt10Wel+31i/1BlsfYRr/Wnt29P94zZHgzUvrVjLr8DhyKS+otxq3hN+TOmChY+b6/hwK7C2eTgVvlBJIu3Xxph5blEG+9aDMwFD1Qhpw++HH4SUGOoJSQJX3lLQmIU3Y0h1+fVUuCNumjgrQKBgHpxW9XeFijoj4gezyMyzfHFQTi5ltnPPwmgBydPZiWgdvsFq8yBI3VzsPrLLi7j2oEfv+Ca4wrd98RO4Bu2eTdiJtn81bQX0TvmY7WPTJbmi82d8Ez0TgQjJIm7EuXapcGxaIb92kHKaCKAimhaOWvK8/vJM1mCoNed4FZYUu0RAoGAa3NjNAxeEE37BRuyJhB1Q5gfcXPgITxhkf8qQUV13jd3U9UseqfFsq4Wjk6HBNTGpIyWEjbN0FHylvHuq9QUqIiLVJY2gI3jEXWQ9KyekrHfNXO9+vFlIOPPV/26TmkQ1SBx2kXuvzLQS9WXbLRryELuX8oPtoTlnnEffjaV5VUCgYEAwsWSWQrv8dlvP3/9CFgwz4cQ4ZGVG+yR47H+5C3zp+C7hWSkwtxXUCpUQXmwvei1ATbJOYwO5b2FPxjuVqCbrUaY0kOhV5jdmdRwRVEubltp4Hl751fQ62FnnT8JlUUjZqgYsdsblpDS7Flj72UgQt/GQ0lrbBUNU0KVn3fN2zs=";
	
	/**
	 * 加密数据
	 * @param ori
	 * @return string
	 */
	public static String encode(String ori) {
		try {
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(Base64Tools.decodeByte(rsaPublicKeyStr));
			PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);
			byte[] encodedBytes = RsaTools.rsaEncode(publicKey, ori.getBytes());
			return Base64Tools.encodeUrlSafe(encodedBytes);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ori;
	}
	
	/**
	 * 解密数据
	 * @param dest
	 * @return string
	 */
	public static String decode(String dest) {
		try {
			byte[] destBytes = Base64Tools.decodeByte(dest);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(Base64Tools.decodeByte(rsaPrivateKeyStr));
			PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
			String ori = RsaTools.rsaDecode(privateKey, destBytes);
			return ori;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dest;
	}
	
	public static void main(String[] args) {
		String data = "{\"name\":\"henrybit\"}";
		String encodeData = encode(data);
		System.out.println(encodeData);
		//encodeData = "IFAReVP8yeKCTBAq3ZC3eEkFDOVgLXkenOb6q7AOaASQLa1g6SJXPgDUPpsUxPe16obbgA/Fu6SVJiR3tEHvtIKwKEYAi1-6rCJOCGdKmH9QAT/Umuufdfc/I6d6mBgzZ4v8FL/vYpk-Ckp-oU3kFpLwvw5fd/8OPM4uJoSz6pTOcOzw2Q4oEhHm0zgq8RkUvBz468gT1WJzwBtSmqMaEN/QYWE4PW2l-zFjleB6x-HzDLTnVPpY/1wD/F89WfQfmo4a4Rpj2b/RR6HygP8yaniJFF6ud5-c5VJhWbxu1SLd8dg1kAeePVy0sFaMJepLPacc122j94pTamLNMpJ6qQ==";
		String decodeData = decode(encodeData);
		System.out.println(decodeData);
	}
}
