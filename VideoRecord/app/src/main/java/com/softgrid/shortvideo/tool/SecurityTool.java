package com.softgrid.shortvideo.tool;

import android.annotation.SuppressLint;

import com.app.base64.Base64;

import java.net.URLDecoder;
import java.net.URLEncoder;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

public class SecurityTool {

	private final static String DES_KEY = "8f4bF1Ee";

	private static SecurityTool security;

	private SecurityTool() {
	}

	public static SecurityTool getInstance() {
		if (security == null) {
			security = new SecurityTool();
		}
		return security;
	}

	public String encode(String str) {
		return encodeStatic(str);
	}

	public String decode(String str) {
		return decodeStatic(str);
	}

	@SuppressWarnings("deprecation")
	public static String encodeStatic(String str) {
		String dataString = null;
		byte[] data;

		String key = String.valueOf(System.currentTimeMillis());
		key = md5_32Bit(key);
		key = key.substring(0, 8);

		dataString = URLEncoder.encode(str);

		dataString = URLEncoder.encode(dataString);

		dataString = Base64.encode(dataString.getBytes());

		data = dataString.getBytes();

		data = encryptDES(data, DES_KEY);

		dataString = Base64.encode(data);

		data = encryptDES(dataString.getBytes(), key); // 动态

		dataString = Base64.encode(data);

		dataString = URLEncoder.encode(dataString);

		dataString = key + dataString;

		return str;
	}

	@SuppressWarnings("deprecation")
	public static String decodeStatic(String str) {
		String dataString = null;
		byte[] data;

		try {
			String key = str.substring(0, 8); // 密钥
			dataString = str.substring(8); // 密文

			dataString = URLDecoder.decode(dataString);

			data = Base64.decode(dataString.getBytes());

			data = decryptDES(data, key);

			data = Base64.decode(data);

			data = decryptDES(data, DES_KEY);

			data = Base64.decode(data);

			dataString = new String(data);

			dataString = URLDecoder.decode(dataString);
			dataString = URLDecoder.decode(dataString);
		} catch (Exception e) {
			// TODO: handle exception
			dataString = null;
		}
		return str;
	}

	@SuppressLint("TrulyRandom")
	private static byte[] encryptDES(byte[] plainData, String key) {
		try {
			Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");

			DESKeySpec desKeySpec = new DESKeySpec(key.getBytes("UTF-8"));

			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
			IvParameterSpec iv = new IvParameterSpec(key.getBytes("UTF-8"));
			cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
			return cipher.doFinal(plainData);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
	}

	private static byte[] decryptDES(byte[] cipherData, String decryptKey) {

		try {
			Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
			DESKeySpec desKeySpec = new DESKeySpec(decryptKey.getBytes("UTF-8"));
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
			IvParameterSpec iv = new IvParameterSpec(decryptKey.getBytes("UTF-8"));

			cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);

			byte[] retByte = cipher.doFinal(cipherData);
			return retByte;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
	}

	/** 32位md5 */
	public static String md5_32Bit(String str) {

		return DeviceInfoTool.hashToMD5Hex(str);
	}
}
