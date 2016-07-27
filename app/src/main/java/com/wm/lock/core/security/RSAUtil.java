package com.wm.lock.core.security;

import android.annotation.SuppressLint;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigInteger;
import java.security.Key;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.crypto.Cipher;


public class RSAUtil {
	
	public static String PUBLICExponent = "3";
	public static String RSAModulus = "954ea90c7396975d65ef2200d0edee555df12c0b85ceadcbb9bb83822f67a21413c537027a800a372e3d094f0b5e17ebc4b5fa03eaec75f32144e39ebe1f626ccb083e99ebda046daf3bbe950363ac36e6bb017ec042c60db51385b402f066c6bab91fcbff7593e2ebf4b9e95fdf03ea2277015d13fd00f9ab4928d6c2b8cf93";
	
	/**
	 * 密码json名
	 */
	public final static String JK_PASSWORD = "P";
	
	/**
	 * 时间戳json名
	 */
	public final static String JK_TIME = "T";
	
	/**
	 * 公钥
	 */
	public PublicKey pubKey = null;
	
	/**
	 * 单例
	 */
	private static RSAUtil instance = null;
	
	
	/**
	 * 构造函数，初始化密钥
	 */
	private RSAUtil(){
		pubKey = getPublicKey();
	}
	
	/**
	 * 获取实例
	 * @return
	 */
	public static RSAUtil getInstance(){
		if (instance == null) {
			instance = new RSAUtil();
		}
		return instance;
	}
	
	/**
	 * 加密
	 * 
	 * @param message
	 * @return
	 */
	public String encrypt(String message) {
		byte[] result = null;
		try {
			result = encrypt(formatParam(message), pubKey);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return StringHexUtil.byte2hex(result);
	}

	/**
	 * 解密
	 * 
	 * @param message
	 * @return
	 */
	public String decrypt(String message) {
		byte[] result = null;
		try {
			result = decrypt(message, pubKey);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new String(result);
	}

	/**
	 * 加密(公钥加密、私钥加密)
	 * 
	 * @param message
	 *            待加密的消息
	 * @param key
	 *            公钥或私钥
	 * @return
	 * @throws Exception
	 */
	private static byte[] encrypt(String message, Key key) throws Exception {
		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		cipher.init(Cipher.ENCRYPT_MODE, key);
		// 注意中文的处理
		return cipher.doFinal(message.getBytes());
	}

	/**
	 * 解密(如果公钥加密，则用私钥解密；如果私钥加密，则用公钥解密)
	 * 
	 * @param message
	 *            待解密的消息
	 * @param key
	 *            公钥或私钥
	 * @return
	 * @throws Exception
	 */
	private byte[] decrypt(String message, Key key) throws Exception {
		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		cipher.init(Cipher.DECRYPT_MODE, key);
		return cipher.doFinal(StringHexUtil.hex2byte(message));
	}

	/**
	 * 通过模长和公钥指数获取公钥
	 * 
	 * @param modulus
	 *            模长
	 * @param publicExponent
	 *            公钥指数
	 * @return 
	 * @return
	 * @throws Exception
	 */
	public PublicKey getPublicKey() {
		PublicKey publicKey = null;
		BigInteger m = new BigInteger(RSAModulus,16);
		BigInteger e = new BigInteger(PUBLICExponent,16);
		RSAPublicKeySpec keySpec = new RSAPublicKeySpec(m, e);
		try {
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			publicKey = keyFactory.generatePublic(keySpec);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return publicKey;
	}
	
	@SuppressLint("SimpleDateFormat")
	private String formatParam(String password) throws JSONException{
		JSONObject json = new JSONObject();
		json.put(JK_PASSWORD, password);
		json.put(JK_TIME, new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
//		json.put(JK_TIME, ImppConfig.SYSTEM_TIME);
		return json.toString();
	}
}
