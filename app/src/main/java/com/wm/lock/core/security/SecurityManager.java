package com.wm.lock.core.security;

import android.text.TextUtils;

import org.apache.commons.codec.android.binary.Base64;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

public class SecurityManager {

	private final static String DES = "DES";
	private final static String MODE = "DES/ECB/PKCS5Padding";
	private static final String DEFAULT_KEY = "JAYXMFAA";

	private SecurityManager() {
	}

	/**
	 * 加密
	 * 
	 * @param key
	 *            密钥
	 * @param input
	 *            加密前的字符串
	 * @return 加密后的字符串
	 * @throws Exception
	 */
	public static String encodeDes(String key, String input) {
		if (TextUtils.isEmpty(input)) {
			return input;
		}
		byte[] data = encryptDes(key, input);
		// return Hex.encodeHexString(data);
		return byte2HexString(data);
	}

	/**
	 * 加密
	 * 
	 * @param key
	 *            密钥
	 * @param input
	 *            加密前的字符串
	 * @return
	 * @throws Exception
	 */
	public static byte[] encryptDes(String key, String input) {
		return doFinal(key, Cipher.ENCRYPT_MODE, input.getBytes());
	}

	/**
	 * 解密
	 * 
	 * @param key
	 *            密钥
	 * @param input
	 *            解密前的字符串
	 * @return encode方法返回的字符串
	 * @throws Exception
	 */
	public static String decodeDes(String key, String input) {
		if (TextUtils.isEmpty(input)) {
			return input;
		}
		byte[] data = String2Byte(input);
		return new String(decrypt(key, data));
	}

	/**
	 * 解密
	 * 
	 * @param key
	 *            密钥
	 * @param input
	 *            encrypt方法返回的字节数组
	 * @return
	 * @throws Exception
	 */
	public static byte[] decrypt(String key, byte[] input) {
		return doFinal(key, Cipher.DECRYPT_MODE, input);
	}

	/**
	 * 执行加密解密操作
	 * 
	 * @param key
	 *            密钥
	 * @param opmode
	 *            操作类型：Cipher.ENCRYPT_MODE-加密，Cipher.DECRYPT_MODE-解密
	 * @param input
	 *            加密解密前的字节数组
	 * @return
	 * @throws Exception
	 */
	private static byte[] doFinal(String key, int opmode, byte[] input) {
		try {
			key = key != null ? key : DEFAULT_KEY;
			// DES算法要求有一个可信任的随机数源
			SecureRandom sr = new SecureRandom();
			// 从原始密匙数据创建一个DESKeySpec对象
			DESKeySpec dks = new DESKeySpec(key.getBytes());
			// 创建一个密匙工厂，然后用它把DESKeySpec对象转换成 一个SecretKey对象
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
			SecretKey securekey = keyFactory.generateSecret(dks);
			// Cipher对象实际完成解密操作
			Cipher cipher = Cipher.getInstance(MODE);
			// 用密匙初始化Cipher对象
			// IvParameterSpec param = new IvParameterSpec(IV);
			// cipher.init(Cipher.DECRYPT_MODE, securekey, param, sr);
			cipher.init(opmode, securekey, sr);
			// 执行加密解密操作
			return cipher.doFinal(input);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * byte[]转换成字符串
	 * 
	 * @param b
	 * @return
	 */
	public static String byte2HexString(byte[] b) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			String stmp = Integer.toHexString(b[i] & 0xff);
			if (stmp.length() == 1)
				sb.append("0" + stmp);
			else
				sb.append(stmp);
		}
		return sb.toString();
	}

	/**
	 * 16进制转换成byte[]
	 * 
	 * @param hexString
	 * @return
	 */
	public static byte[] String2Byte(String hexString) {
		if (hexString.length() % 2 == 1)
			return null;
		byte[] ret = new byte[hexString.length() / 2];
		for (int i = 0; i < hexString.length(); i += 2) {
			ret[i / 2] = Integer.decode("0x" + hexString.substring(i, i + 2))
					.byteValue();
		}
		return ret;
	}

	/**
	 * md5加密
	 *
	 * @param inputText
	 * @return
	 */
	public static String md5(String inputText) {
		return encrypt(inputText, "md5");
	}

	/**
	 * sha加密
	 *
	 * @param inputText
	 * @return
	 */
	public static String sha(String inputText) {
		return encrypt(inputText, "sha-1");
	}

	/**
	 * md5或者sha-1加密
	 *
	 * @param inputText
	 *            要加密的内容
	 * @param algorithmName
	 *            加密算法名称：md5或者sha-1，不区分大小写
	 * @return
	 */
	private static String encrypt(String inputText, String algorithmName) {
		if (inputText == null || "".equals(inputText.trim())) {
			throw new IllegalArgumentException("请输入要加密的内容");
		}
		if (algorithmName == null || "".equals(algorithmName.trim())) {
			algorithmName = "md5";
		}
		String encryptText = null;
		try {
			MessageDigest m = MessageDigest.getInstance(algorithmName);
			m.update(inputText.getBytes("UTF8"));
			byte s[] = m.digest();
			// m.digest(inputText.getBytes("UTF8"));
			return hex(s);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return encryptText;
	}

	// 返回十六进制字符串
	private static String hex(byte[] arr) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < arr.length; ++i) {
			sb.append(Integer.toHexString((arr[i] & 0xFF) | 0x100).substring(1, 3));
		}
		return sb.toString();
	}

	public static String base64Encode(String input) {
		if (TextUtils.isEmpty(input)) {
			return input;
		}
		return Base64.encodeBase64String(input.getBytes());
	}

	public static String base64Decode(String input) {
		if (TextUtils.isEmpty(input)) {
			return input;
		}
		return new String(Base64.decodeBase64(input));
	}

}
