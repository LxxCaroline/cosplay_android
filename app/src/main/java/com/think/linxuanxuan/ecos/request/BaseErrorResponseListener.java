package com.think.linxuanxuan.ecos.request;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.think.linxuanxuan.ecos.activity.MyApplication;

public class BaseErrorResponseListener implements ErrorListener{

	private final static String TAG = "BaseErrorResponse";
	
	private final static String NETWORK_ERROR = "网络出错，请重新操作";
	
	private final static String SERVER_ERROR = "服务器异常，请稍后再试";
	
	private final static String NO_CONNECTION_ERROR = "无可用的网络";
	
	private final static String TIMEOUT_ERROR = "连接超时，请确定网络可用";
	
	
	private final static String PARSE_ERROR = "响应不能被解析";
	
	private final static String AUTH_FAILURE_ERROR = "权限错误";
	
	private final static String OTHER_NETWORK_ERROR = "未知网络错误";
	
	private Context mContext;
	
//	LittleMessageDialog mLittleMessageDialog;
	
	public BaseErrorResponseListener(){} 
	

	public BaseErrorResponseListener(Context context)
	{
		mContext = context;
	} 
	
	public static BaseErrorResponseListener createErrorResponseListener()
	{
		return new BaseErrorResponseListener( MyApplication.getContext());
	}
	
	public static BaseErrorResponseListener createErrorResponseListener(Context context)
	{
		return new BaseErrorResponseListener(context);
	}
	
	@Override
	public void onErrorResponse(VolleyError error) {
		
		if (error instanceof NetworkError) {
			Log.e(TAG, "NetworkError.");
			showDialog(NETWORK_ERROR);
		} 
		
		else if (error instanceof ServerError) {
			Log.e(TAG, "ServerError.");
			showDialog(SERVER_ERROR);
		} 
		
		else if (error instanceof AuthFailureError) {
			Log.e(TAG, "AuthFailureError.");
			showDialog(AUTH_FAILURE_ERROR);
		}
		
		else if (error instanceof ParseError) {
			Log.e(TAG, "ParseError.");
			showDialog(PARSE_ERROR);
		}
		
		else if (error instanceof NoConnectionError) {
			Log.e(TAG, "NoConnectionError.");
			showDialog(NO_CONNECTION_ERROR);
		}
		
		else if (error instanceof TimeoutError) {
			Log.e(TAG, "TimeoutError.");
			showDialog(TIMEOUT_ERROR);
		} 
		
		else {
			Log.e(TAG, "Other NetworkError.");
			showDialog(OTHER_NETWORK_ERROR);
		}
		
	}
	
	//显示消息对话框
	public void showDialog(String message) {
		Toast.makeText(MyApplication.getContext(), message, Toast.LENGTH_LONG).show();
		
//		mLittleMessageDialog = mLittleMessageDialog = DialogFactory.createLittleMessageDialog(message);
//		mLittleMessageDialog.setContent(message);
//		mLittleMessageDialog.showDialog();
	}
	
	
	
	
}
