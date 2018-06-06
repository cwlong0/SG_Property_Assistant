package com.softgrid.shortvideo.tool;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;
import java.util.Random;

public class DeviceInfoTool {

	private static String APP_NAME = null;
	private static String APP_VERSION_NAME = null;
	private static String PACKAGE_NAME = null;
	private static String MAC_ADDRESS = null;
	private static String ANDROID_ID = null;
	private static int    SCREEN_WIDTH  = 0;
	private static int    SCREEN_HEIGHT = 0;
	private static float    SCREEN_DENSITY = 0;
	


	/**获取应用名称*/
	public static String getAppName(Context context) {
		if(APP_NAME == null){
			try {
				APP_NAME = context.getPackageManager().getApplicationLabel(
						context.getPackageManager().
						getApplicationInfo(getPageName(context), PackageManager.GET_META_DATA)).
						toString();
				return APP_NAME;
			} catch (NameNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else{
			return APP_NAME;
		}
		return "";
	}
	
	/**获取应用版本名称*/
	public static String getAppVersionName(Context context){
		if(APP_VERSION_NAME == null){
			try {
				APP_VERSION_NAME = context.getPackageManager().
						getPackageInfo(getPageName(context), 0).versionName;
				return APP_VERSION_NAME;
			} catch (NameNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else{
			return APP_VERSION_NAME;
		}
		return "";
	}
	
	/**获取应用包名*/
	public static String getPageName(Context context){
		if(PACKAGE_NAME == null){
			PACKAGE_NAME = context.getApplicationContext().getPackageName();
		}
		if(PACKAGE_NAME != null){
			return PACKAGE_NAME;
		}
		else{
			return "";
		}
	}

	/**获取手机mac地址*/
	public static String getMac(Context context) {
//		if(MAC_ADDRESS == null){
//			WifiManager wifiMgr = null;
//			WifiInfo info = null;
//			try {
//				wifiMgr = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
//				info = (null == wifiMgr ? null : wifiMgr.getConnectionInfo());
//				if (null != info) {
//					MAC_ADDRESS = info.getMacAddress();
//				}
//				if (MAC_ADDRESS != null  || !MAC_ADDRESS.equals("")) {
//					return MAC_ADDRESS;
//				}
//			} catch (Exception e) {
//				// TODO: handle exception
//				e.printStackTrace();
//				MAC_ADDRESS = null;
//
//			}
//		}
		if (MAC_ADDRESS == null || MAC_ADDRESS.equals("")) {
			String defineMac = null;
			SharedPreferences sp = context.getSharedPreferences("config_info", Context.MODE_PRIVATE);
			defineMac = sp.getString("uuid", null);
			
			//配置文件中没有数据
			if (defineMac == null) {
				String path = Environment.getExternalStorageDirectory().toString()
						+ "/.SystemInfo";
				if (sdCheck()) {
					String filePath = path + "/.uuid";
					defineMac = getDataFromFile(filePath);
				}
				if (defineMac == null) {
					defineMac = getSysVersion() + getLanguage() + getProduct() + 
							getModel() + String.valueOf(System.currentTimeMillis());
					defineMac = hashToMD5Hex(defineMac);
					for (int i = 0; i < 16; i++) {
						defineMac = defineMac + String.valueOf(randomNumber(9));
					}
					defineMac = hashToMD5Hex(defineMac);
					if (sdCheck()) {
						saveDataToFile(path, ".uuid", defineMac);
					}
				}
				Editor editor = sp.edit();
				editor.putString("uuid", defineMac);
				editor.commit();
			}
			
			MAC_ADDRESS = defineMac;
		}
		return MAC_ADDRESS;
	}
	
	/**获取product*/
	public static String getProduct(){
		return Build.PRODUCT;
	}
	
	/**获取设备系统版本*/
	public static String getSysVersion(){
		return Build.VERSION.RELEASE;
	}
	
	/**获取设备使用的语言*/
	public static String getLanguage() {
		return Locale.getDefault().getLanguage();
	}
	
	/**获取设备所在的国家地区*/
	public static String getLocale() {
		return Locale.getDefault().getCountry();
	}
	
	public static String getModel(){
		return Build.MODEL;
	}
	
	/**获取设备唯一标示（AndroidId）*/
	public static String getAndroidId(Context context) {
		ANDROID_ID = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        if (ANDROID_ID == null || ANDROID_ID.length() == 0) {
        	return "";
        }
        return ANDROID_ID;
        
	}
	
	/**获取屏幕宽度*/
	public static int getScreenWidth(Activity activity) {
		if (SCREEN_WIDTH == 0) {
			DisplayMetrics dm = new DisplayMetrics();
			activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
			SCREEN_WIDTH = dm.widthPixels;
			SCREEN_HEIGHT = dm.heightPixels;
			SCREEN_DENSITY = dm.density;
		}
		return SCREEN_WIDTH;
	}
	
	/**获取屏幕高度*/
	public static int getScreenHeight(Activity activity) {
		if (SCREEN_HEIGHT == 0) {
			DisplayMetrics dm = new DisplayMetrics();
			activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
			SCREEN_WIDTH = dm.widthPixels;
			SCREEN_HEIGHT = dm.heightPixels;
			SCREEN_DENSITY = dm.density;
		}
		return SCREEN_HEIGHT;
	}
	
	/**获取屏幕密度*/
	public static float getScreenDensity(Activity activity) {
		if (SCREEN_WIDTH == 0) {
			DisplayMetrics dm = new DisplayMetrics();
			activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
			SCREEN_WIDTH = dm.widthPixels;
			SCREEN_HEIGHT = dm.heightPixels;
			SCREEN_DENSITY = dm.density;
		}
		return SCREEN_DENSITY;
	}
	
	/**获取一个随机数*/
	private static int randomNumber(int max){
		int random = 0;
		random = new Random().nextInt(max + 1);
		return random;
	}
	
	/**
	 * 获取指定字符串的MD5码
	 * 
	 * @param data 指定字符串
	 * 
	 * */
    public static String hashToMD5Hex(String data){
    	MessageDigest messagedigest = null;
    	String result = null;
    	try {
			messagedigest = MessageDigest.getInstance("MD5");
			
			try {
				messagedigest.update(data.getBytes("utf-8"));
				
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			} 
			result = bufferToHex(messagedigest.digest());
			return result;
			
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
			
		}finally{
			messagedigest = null;
		}
    }
    
    private static String bufferToHex(byte bytes[]){
    	
    	return bufferToHex(bytes, 0, bytes.length);
    }
    
    private static String bufferToHex(byte bytes[], int m, int n){
    	
    	StringBuffer stringbuffer = new StringBuffer(2 * n);
    	int k = m + n;
    	for (int l = m; l < k; l++)
    	{
    		appendHexPair(bytes[l], stringbuffer);
    	}
    	return stringbuffer.toString();
    }
    
    private static void appendHexPair(byte bt, StringBuffer stringbuffer){
    	 char md5Chars[] =
    		 { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd',
    		   'e', 'f' };
    	char c0 = md5Chars[(bt & 0xf0) >> 4];
    	char c1 = md5Chars[bt & 0xf];
    	stringbuffer.append(c0);
    	stringbuffer.append(c1);
    }
    
	/**
	 * 将数据保存到文件中
	 * 
	 * @param path
	 *            文件保存路径(该目录必须存在，否则会抛出异常)
	 * 
	 * @param fileName
	 *            文件名
	 * 
	 * @param data
	 *            待保存的数据
	 * 
	 * */
	private static boolean saveDataToFile(String path, String fileName, String data) {
		try {
			boolean done = false;
			File file = null;
			OutputStream out = null;
			file = new File(path);
			if (!file.exists()) {
				file.mkdirs();
			}
			file = new File(path + "/" + fileName);
			if (!file.exists()) {
				done = file.createNewFile();
			}
			byte b[] = data.getBytes();
			out = new FileOutputStream(file);
			out.write(b);
			if (out != null) {
				out.close();
				out = null;
			}
			done = true;
			return done;

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;

		}
	}
	
	/**
	 * 从文件中获取字符串数据
	 * 
	 * @param filePath
	 *            文件路径
	 * 
	 * */
	@SuppressWarnings("resource")
	private static String getDataFromFile(String filePath) {
		String data = null;
		File file = null;
		file = new File(filePath);
		InputStream in = null;
		if (!file.exists()) {
			return null;
		}
		try {
			in = new FileInputStream(file);
			byte[] buffer = new byte[in.available()];
			in.read(buffer);
			data = new String(buffer);
		} catch (Exception e) {
			// TODO: handle exception\
			e.printStackTrace();
			return null;
		}

		return data;
	}
	
	/**检查sd卡状态*/
	private static boolean sdCheck(){
		try {
			String sdStatus = Environment.getExternalStorageState();// 获取SD卡状态
			// SD卡识别正常
			if (sdStatus.equals(Environment.MEDIA_MOUNTED)) {
				return true;
			}
			// SD卡被移除或者未插入
			else if (sdStatus.equals(Environment.MEDIA_REMOVED)
					|| sdStatus.equals(Environment.MEDIA_BAD_REMOVAL)) {
				return false;
			}
			// 正在检查SD卡
			else if (sdStatus.equals(Environment.MEDIA_CHECKING)) {
				return false;
			}
			// sd卡无法被识别
			else {
				return false;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
		
	}
}

