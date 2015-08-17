package com.think.linxuanxuan.ecos.request.situation;

import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request.Method;
import com.think.linxuanxuan.ecos.constants.RequestUrlConstants;
import com.think.linxuanxuan.ecos.request.BaseRequest;
import com.think.linxuanxuan.ecos.request.IBaseResponse;
import com.think.linxuanxuan.ecos.request.MyStringRequest;

/***
 * 
* @ClassName: SituationListRequest 
* @Description: 获取活动实况列表
* @author enlizhang
* @date 2015年7月26日 下午1:57:42 
*
 */
public class SituationListRequest extends BaseRequest{
	
	//请求参数键
	
	//响应参数键
	
	
	
	public void request(IBaseResponse baseresponce, final String mobile, final String password)
	{
		super.initBaseRequest(baseresponce);
		
		MyStringRequest stringRequest = new MyStringRequest(Method.POST, RequestUrlConstants.GET_SITUATION_LIST_URL,  this, this) {  
	        @Override  
	        protected Map<String, String> getParams() throws AuthFailureError {  
	        	Map<String, String> map = getRequestBasicMap();
	            
	        	
	            traceNormal(TAG, map.toString());
	            traceNormal(TAG, SituationListRequest.this.getUrl(RequestUrlConstants.GET_SITUATION_LIST_URL, map));
	            return map;  
	        }  
	        
	    }; 
	    
	    stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000,0,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
	    
	    getQueue().add(stringRequest);
	    
	}
	
	@Override
	public void responceSuccess(String jstring) {
		traceNormal(TAG, jstring);
		
		try {
			JSONObject json = new JSONObject(jstring);
			
			
			if(mBaseResponse!=null)
			{
			}
			else
			{
				traceError(TAG,"回调接口为null");
			}
			
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			if(mBaseResponse!=null)
			{
				mBaseResponse.doAfterFailedResponse("json异常");
			}
		}
		
	}
	
}

