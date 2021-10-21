package com.cake.task.master.core.common.utils.jinfupay;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;
import java.util.Map.Entry;

public class SecurityUtil {
	public static String encryptByAES(String key, String iv, String content) throws Exception {
		return encrypt("AES/CFB/PKCS5Padding", getSecretKey(key, "AES"), getIv(iv), content);
	}

	public static Key getSecretKey(String key, String algorithm) {
		return new SecretKeySpec(Base64.decodeBase64(key), algorithm);
	}

	public static IvParameterSpec getIv(String iv) {
		return new IvParameterSpec(Base64.decodeBase64(iv));
	}

	private static String encrypt(String algorithm, Key key, AlgorithmParameterSpec param, String text)
			throws Exception {
		Cipher cipher = initCipher(algorithm, 1, key, param);

		try {
			byte[] data = cipher.doFinal(text.getBytes("UTF-8"));
			return Base64.encodeBase64String(data);
		} catch (Exception var6) {
			var6.printStackTrace();
			throw new Exception();
		}
	}

	private static Cipher initCipher(String algorithm, int mode, Key key, AlgorithmParameterSpec param)
			throws Exception {
		try {
			Cipher cipher = Cipher.getInstance(algorithm);
			if (param == null) {
				cipher.init(mode, key);
			} else {
				cipher.init(mode, key, param);
			}

			return cipher;
		} catch (Exception var5) {
			var5.printStackTrace();
			throw new Exception();
		}
	}

	public static String generateAESKey() throws Exception {
		KeyGenerator kgen = getAESKeyGenerator();
		kgen.init(128);
		SecretKey skey = kgen.generateKey();
		return Base64.encodeBase64String(skey.getEncoded());
	}

	public static KeyGenerator getAESKeyGenerator() throws Exception {
		try {
			return KeyGenerator.getInstance("AES");
		} catch (NoSuchAlgorithmException var1) {
			var1.printStackTrace();
			throw new Exception();
		}
	}

	public static String generateAESIv() throws Exception {
		try {
			Cipher cipher = Cipher.getInstance("AES/CFB/PKCS5Padding");
			int blockSize = cipher.getBlockSize();
			byte[] array = new byte[16];
			for (int i = 0; i < array.length; i++) {
				int ra = (int) (Math.random() * 10);
				array[i] = Byte.parseByte(String.valueOf(ra));
			}
			return Base64.encodeBase64String(array);
		} catch (Exception var2) {
			var2.printStackTrace();
			throw new Exception();
		}
	}

	public static PublicKey getPublicKey(KeyFactory keyFactory, String key) throws Exception {
		try {
			X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(Base64.decodeBase64(key));
			return keyFactory.generatePublic(x509KeySpec);
		} catch (Exception var3) {
			var3.printStackTrace();
			throw new Exception();
		}
	}

	public static String encryptByRSA(PublicKey key, String content) throws Exception {
		return encrypt("RSA/ECB/PKCS1Padding", key, (AlgorithmParameterSpec) null, content);
	}

	public static String sign(String priKey, String content) throws Exception {
		PrivateKey privateKey = getPrivateKey(getRSAKeyFactory(), priKey);
		return sign("SHA256withRSA", privateKey, content);
	}

	public static KeyFactory getRSAKeyFactory() throws Exception {
		try {
			return KeyFactory.getInstance("RSA");
		} catch (NoSuchAlgorithmException var1) {
			var1.printStackTrace();
			throw new Exception();
		}
	}

	public static PrivateKey getPrivateKey(KeyFactory keyFactory, String key) throws Exception {
		try {
			PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(Base64.decodeBase64(key));
			return keyFactory.generatePrivate(pkcs8EncodedKeySpec);
		} catch (Exception var3) {
			var3.printStackTrace();
			throw new Exception();
		}
	}

	private static String sign(String algorithm, PrivateKey priKey, String text) throws Exception {
		try {
			Signature signature = Signature.getInstance(algorithm);
			signature.initSign(priKey);
			signature.update(text.getBytes("UTF-8"));
			return Base64.encodeBase64String(signature.sign());
		} catch (Exception var4) {
			var4.printStackTrace();
			throw new Exception();
		}
	}

	public static String map2str(Map<String, String> map) {
		TreeMap<String, String> treeMap = new TreeMap(map);
		List<String> values = new ArrayList();
		Iterator var3 = treeMap.entrySet().iterator();
		StringBuffer sb = new StringBuffer();
		while (var3.hasNext()) {
			Entry<String, String> entry = (Entry) var3.next();
			String key = (String) entry.getKey();
			String value = (String) entry.getValue();
			if (StringUtils.isNotBlank(key) && StringUtils.isNotBlank(value)) {
				sb.append(key + "=" + value + "&");
				// values.add(StringUtils.join("=", new Object[]{key, value}));
			}
		}
		return sb.toString().substring(0, sb.length() - 1);
	}


	public static String decryptByRSA(PrivateKey key, String content) throws Exception {
		return decrypt("RSA/ECB/PKCS1Padding", key, (AlgorithmParameterSpec) null, content);
	}

	public static boolean verifySign(String pubKey, String content, String sign) throws Exception {
		PublicKey publicKey = getPublicKey(getRSAKeyFactory(), pubKey);
		return verifySign("SHA256withRSA", publicKey, content, sign);
	}

	public static String decryptByAES(String key, String iv, String content) throws Exception {
		return decrypt("AES/CFB/PKCS5Padding", getSecretKey(key, "AES"), getIv(iv), content);
	}

	private static String decrypt(String algorithm, Key key, AlgorithmParameterSpec param, String text)
			throws Exception {
		Cipher cipher = initCipher(algorithm, 2, key, param);

		try {
			byte[] data = cipher.doFinal(Base64.decodeBase64(text));
			return new String(data, "UTF-8");
		} catch (Exception var6) {
			var6.printStackTrace();
			throw new Exception();
		}
	}

	private static boolean verifySign(String algorithm, PublicKey pubKey, String text, String sign) throws Exception {
		try {
			Signature signature = Signature.getInstance(algorithm);
			signature.initVerify(pubKey);
			signature.update(text.getBytes("UTF-8"));
			return signature.verify(Base64.decodeBase64(sign));
		} catch (Exception var5) {
			var5.printStackTrace();
			throw new Exception();
		}
	}
}
