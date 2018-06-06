package com.softgrid.shortvideo.http;

import android.content.Context;

import com.softgrid.shortvideo.tool.DeviceInfoTool;

import org.json.JSONObject;

public class HttpRequestInfo {

	/**
	 * 获取请求参数
	 * 
	 * @param context 上下文
	 * 
	 * @param act 接口名称
	 * 
	 * @param params 扩展参数
	 * 
	 * */
	public static String getRequestParams(Context context, String act, String params){
		String requestParams = null;
		JSONObject requestParamsJson = new JSONObject();
		try {
			requestParamsJson.put("a", act);
			requestParamsJson.put("i", DeviceInfoTool.getMac(context));
			requestParamsJson.put("d", DeviceInfoTool.getModel());
			requestParamsJson.put("m", DeviceInfoTool.getMac(context));
			requestParamsJson.put("l", DeviceInfoTool.getLanguage());
			requestParamsJson.put("o", "Android " + DeviceInfoTool.getSysVersion());
			requestParamsJson.put("n", DeviceInfoTool.getPageName(context));
			requestParamsJson.put("v", DeviceInfoTool.getAppVersionName(context));
			if (params != null && !params.equals("")) {
				requestParamsJson.put("r", new JSONObject(params));
			}
			else{
				requestParamsJson.put("r", "");
			}
			
			requestParams = requestParamsJson.toString();
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return requestParams;
	}
}
