package com.think.linxuanxuan.ecos.request;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;

public class VolleyErrorParser {
	
	private final static String TAG = "VolleyErrorParser";
	
	private final static String NETWORK_ERROR = "网络出错，请重新操作";
	
	private final static String SERVER_ERROR = "服务器异常，请稍后再试";
	
	private final static String NO_CONNECTION_ERROR = "无可用的网络";
	
	private final static String TIMEOUT_ERROR = "连接超时，请稍候重试";
	
	
	private final static String PARSE_ERROR = "响应不能被解析";
	
	private final static String AUTH_FAILURE_ERROR = "权限错误";
	
	private final static String OTHER_NETWORK_ERROR = "未知网络错误";
	
	
	
	public static String parseVolleyError(VolleyError error)
	{
		if (error instanceof NetworkError) {
			Log.e(TAG, "NetworkError.");
			return NETWORK_ERROR;
		} 
		
		if (error instanceof ServerError) {
			Log.e(TAG, "ServerError.");
			return SERVER_ERROR;
		} 
		
		if (error instanceof AuthFailureError) {
			Log.e(TAG, "AuthFailureError.");
			return AUTH_FAILURE_ERROR;
		}
		
		if (error instanceof ParseError) {
			Log.e(TAG, "ParseError.");
			return PARSE_ERROR;
		}
		
		if (error instanceof NoConnectionError) {
			Log.e(TAG, "NoConnectionError.");
			return NO_CONNECTION_ERROR;
		}
		
		if (error instanceof TimeoutError) {
			Log.e(TAG, "TimeoutError.");
			return TIMEOUT_ERROR;
		} 
		
		Log.e(TAG, "Other NetworkError.");
		return OTHER_NETWORK_ERROR;
	}

}
