package com.think.linxuanxuan.ecos.request.course;

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
* @ClassName: DeleteAssignmentRequest 
* @Description: 删除教程作业
* @author enlizhang
* @date 2015年7月26日 上午11:05:11 
*
 */
public class DeleteAssignmentRequest extends BaseRequest{
	
	//请求参数键
	/** 教程作业id */
	public static final String ASSIGNMENT_ID = "assignment_id";
	
	
	//响应参数键
	
	
	
	public void request(IBaseResponse baseresponce, final String assignmentId)
	{
		super.initBaseRequest(baseresponce);
		
		MyStringRequest stringRequest = new MyStringRequest(Method.POST, RequestUrlConstants.DELETE_ASSIGNMENT_URL,  this, this) {  
	        @Override  
	        protected Map<String, String> getParams() throws AuthFailureError {  
	        	Map<String, String> map = getRequestBasicMap();
	            
	        	map.put(ASSIGNMENT_ID, assignmentId);
	        	
	            traceNormal(TAG, map.toString());
	            traceNormal(TAG, DeleteAssignmentRequest.this.getUrl(RequestUrlConstants.DELETE_ASSIGNMENT_URL, map));
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

