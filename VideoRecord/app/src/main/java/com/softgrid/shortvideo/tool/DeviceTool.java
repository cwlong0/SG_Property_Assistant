package com.softgrid.shortvideo.tool;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class DeviceTool {

	private static final String TAG = "DeviceTool";
	
	private static DeviceTool tool;
	
	private DeviceTool(){
		
	}
	
	public static DeviceTool getInstance() {
		if (tool == null) {
			tool = new DeviceTool();
		}
		return tool;
	}
	
	/**
	 * 判断本机是否安装了指定包名对应的应用
	 * 
	 * @param packageName 指定包名
	 * 
	 * */
	public boolean apkInstalled(Context context, String packageName) {
		PackageInfo packageInfo = null;
		try {
			packageInfo = context.getPackageManager().getPackageInfo(packageName, 0);
			if(packageInfo != null){
				packageInfo = null;
				return true;
			}
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			packageInfo = null;
		}
		LOG.e(TAG, "本机未安装包名为'" + packageName + "'的应用");
		return false;
	}
	
	/**显示键盘*/
	public void showKeybord(EditText editText){
//		editText.setFocusable(true);
//        editText.setFocusableInTouchMode(true);
//        editText.requestFocus();
		InputMethodManager inputManager = (InputMethodManager)editText.getContext()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
	}
	
	/**隐藏键盘*/
	public void hideKeybord(EditText editText){
		editText.clearFocus();
		InputMethodManager imm = (InputMethodManager)editText.getContext().
				getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
	}
}
