package com.softgrid.shortvideo.http;

import android.content.Context;

import com.softgrid.shortvideo.info.UserInfo;
import com.softgrid.shortvideo.tool.DeviceInfoTool;

import org.json.JSONObject;

public class HttpRequestInfo {

	public static final String HOST = "http://www.baidu.com";

	public static final String MEDIA_TYPE_DEF = "text/x-markdown; charset=utf-8";

	private static HttpRequestInfo info;

	private HttpRequestInfo(){

	}

	public static HttpRequestInfo getInstance(){

		if (info == null){
			info = new HttpRequestInfo();
		}

		return info;
	}

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
	public String getRequestParams(Context context, String act, String params){
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

			JSONObject r = new JSONObject();
			if (UserInfo.getInstance().getUserId(context) != null){
				r.put("id", UserInfo.getInstance().getUserId(context));
				r.put("token", UserInfo.getInstance().getToken(context));
			}

			if (params != null && !params.equals("")) {
				r.put("d", new JSONObject(params));
			}
			else{
				r.put("d", "");
			}
			requestParamsJson.put("r", r);
			
			requestParams = requestParamsJson.toString();
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return requestParams;
	}
}
