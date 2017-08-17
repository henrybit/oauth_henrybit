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
	protected static String rsaPublicKeyStr = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAkSHTZjdMqJvmekpRS3eHo8wpmSJ+FN/xfQB97aihqk768l0Clzr5k9GbBDnJ6d7OXjy/x+OqFdaXiAGrHRXF/1NlUxzVHSqwlrfdNi3G6OkHmSjNJMY0dJ9QErlGVOEWP0l3Hr2p5WokG/qCn2gp5IbkDNiEQbKAxQQeVXKBYZ1e8ISzQW8k6ZjFWRG2StQnAfFEYOhyyJRrUCpls23hZCUVwATr1SpFhaeq75DLNURL+QH/ChHSGiB7hrv384MGiH01IFmqEdls8PEoglRAngxbc080ItwQ4+CD1jkLD5KlQTafJOAncNUVT7k1pAy9eE8LBQbl4DjlyX32hZiO+QIDAQAB";
	/**rsa私钥-解密端使用*/
	protected static String rsaPrivateKeyStr = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCRIdNmN0yom+Z6SlFLd4ejzCmZIn4U3/F9AH3tqKGqTvryXQKXOvmT0ZsEOcnp3s5ePL/H46oV1peIAasdFcX/U2VTHNUdKrCWt902Lcbo6QeZKM0kxjR0n1ASuUZU4RY/SXcevanlaiQb+oKfaCnkhuQM2IRBsoDFBB5VcoFhnV7whLNBbyTpmMVZEbZK1CcB8URg6HLIlGtQKmWzbeFkJRXABOvVKkWFp6rvkMs1REv5Af8KEdIaIHuGu/fzgwaIfTUgWaoR2Wzw8SiCVECeDFtzTzQi3BDj4IPWOQsPkqVBNp8k4Cdw1RVPuTWkDL14TwsFBuXgOOXJffaFmI75AgMBAAECggEAWhYNCp24z9A1uCL5Y0bGwz2vdIacjIiVdWogwKyz7OspideCDC1nTgIFnxHf63OJ1aUPxhvjL7sMRDgE5wDk7h/t/TEgeAJPqr5maYLwvRIozUEuVW1SkjFxmq22omk1TgNkFYjVNLo1v8g33Irjth6k3/Rl5AHRPHnZ+mAhP11nHojCc1Y1AiLrh8aWa0s+NZHMbiiRyVN2zOBZE44pNunWky+eSns/B0W5Q9JXNxqKoYGRUtmgS7G8NVnVjNOjhebCeHVZoR8qyuB3bG1REu0SXU+VHXY61N5H13tgUDCaDPCcuPRpBmQqPROLpxvDhk0nxpx+7eQPuFUGmCKAAQKBgQDXCxqGELY+I6VXlkvucQkazAGuikt7b+BwdzRTJT5h2V79LSqyrnCepzy5JwrWVAZ1rMw76o2N7KfbcnMBFuzqyXNAiSVh1yxZ2it8Y6sdpnr5d5cbZ5R/7Igm8Aan++SyvGkpOF268Qw4NdBerIcp8KEuzoLgnkh84eORq+9aAQKBgQCsxg0VgLcJW4YYj8K4larEg/ghmA3jctlVnalCnAj0dakQL1VbEexjpL5pZCoo+hm6u+Wye1Nppok9T0z5PHwHJVLm2gMVMJorvqY+PPAjN54JQwtmoe92UIxJ1TXpkvGYhNYPHHr13ucED00FHaLPRoePdvrMQMMqWYCGGGIE+QKBgAxTCpYcZxVwnY1Dg96Kg+cRQhdqvaC5bLeFXW5WeBjLiHIiIIxVCwdMtmI2JpzZ+/TKjnnwm2RC5+vfx0+MrM9X9E8CjS3qUxRjOJLMc4YjgHDDtdlQOmaKe9MfdoIyIY1M1woRvO112yltjB+g664vqE0ycLXG3IiruYsju3gBAoGBAJElPP6wiJR66DegJnTNf43iVEVcqWZIKN8tOXH1BezhH+ux6CI+93lqgHCnrmeIXK8shTahNp0ORENWaJMFxih2qaO4yVg24X4Yhy7IIPFlGwYq5dwN9M9PsduhG86jHQYFqpM9WOtPnIIjnI/Mquc5GE9lJCMN0Se10yxUNZx5AoGAEQIIhdxyObrHb/P0Apms6zST+F5jju9ZrUHpSsl2D6x/z2qYsWlEcqOkKyutO9lIqGCftZZotqi2BkISWtamZ4y2IfjRkQqm7ivJpeQuMUqCQCOtUpEIrZTiiUj0ViQSKBoF8sgRVz+Jzg29fidwVi/UlMjrAkjd3zbHabUrfWk=";
	
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
		encodeData = "IFAReVP8yeKCTBAq3ZC3eEkFDOVgLXkenOb6q7AOaASQLa1g6SJXPgDUPpsUxPe16obbgA/Fu6SVJiR3tEHvtIKwKEYAi1-6rCJOCGdKmH9QAT/Umuufdfc/I6d6mBgzZ4v8FL/vYpk-Ckp-oU3kFpLwvw5fd/8OPM4uJoSz6pTOcOzw2Q4oEhHm0zgq8RkUvBz468gT1WJzwBtSmqMaEN/QYWE4PW2l-zFjleB6x-HzDLTnVPpY/1wD/F89WfQfmo4a4Rpj2b/RR6HygP8yaniJFF6ud5-c5VJhWbxu1SLd8dg1kAeePVy0sFaMJepLPacc122j94pTamLNMpJ6qQ==";
		String decodeData = decode(encodeData);
		System.out.println(decodeData);
	}
}
