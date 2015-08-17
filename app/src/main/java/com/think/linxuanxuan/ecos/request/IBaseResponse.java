package com.think.linxuanxuan.ecos.request;

import com.android.volley.Response.ErrorListener;


public interface IBaseResponse extends ErrorListener{

	public String MESSAEE_SUCCESS="操作成功";
	public String MESSAEE_LOGIN_AGAIN = "无操作权限，请重新登陆";
	public String MESSAEE_OTHER_ERROR = "请过段时间再进行操作";
	
	
	public void doAfterFailedResponse(String message);
	
	
	//无操作权限
	public void responseNoGrant();
	
}
