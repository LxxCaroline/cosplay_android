package com.think.linxuanxuan.ecos.request.initial;

import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.think.linxuanxuan.ecos.activity.MyApplication;
import com.think.linxuanxuan.ecos.constants.RequestUrlConstants;
import com.think.linxuanxuan.ecos.database.ProvinceDBService;
import com.think.linxuanxuan.ecos.model.ConfigurationService;
import com.think.linxuanxuan.ecos.model.Province;
import com.think.linxuanxuan.ecos.request.MyStringRequest;
import com.think.linxuanxuan.ecos.activity.SplashActivity.InitialInfoResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**   
 * @Title: GetPRrovinceList.java 
 * @Description: 获取省市列表请求
 * @author enlizhang   
 * @date 2015年8月5日 下午1:07:11 
 */

public class GetProvinceListRequest extends InitialRequest implements ErrorListener{
	
	private final String TAG = "GetGeographyRequest";
	
	private boolean success = false;;
	private VolleyError mError = null;
	
	
	//省响应数据
	public String KEY_PROVINCE_ID = "provinceId";
	public String KEY_PROVINCE_NAME = "provinceName";
	
	private boolean successRequestConfiguration=false;
	
	/*** 请求结束回调接口*/
	private InitialInfoResponse mInitialInfoResponse;
	
	
	public boolean requestCityList(InitialInfoResponse initialInfoResponse)
	{
		mInitialInfoResponse = initialInfoResponse;
	    
		MyStringRequest stringRequest = new MyStringRequest(Method.GET, RequestUrlConstants.PROVINCE_LIST_URL,  this, this);
	    
	    requestState = IS_REQUESTING;
	    success = false;
	    mError = null;
	    stringRequest.setRetryPolicy(new DefaultRetryPolicy(20000,0,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
	    
	    getQueue().add(stringRequest);
	    
	    
	    return true;
	}
	
	@Override
	public void onErrorResponse(VolleyError error) {
		
		Log.d(TAG, error.toString());
		requestState = FINISHED;
		success = false;
		mError = error;
		
		if(mInitialInfoResponse!=null)
		{
			mInitialInfoResponse.doAfterResponse();
		}
		else
		{
			traceError(TAG,"回调接口为null");
		}
		
	}

	@Override
	public void onResponse(String jstring) {
		
		traceNormal(TAG, jstring);
		requestState = FINISHED;
		success = true;
		
		try {
			
			JSONObject json = new JSONObject(jstring).getJSONObject(KEY_DATA);
			
			//处理省数据
			JSONArray provinceArray = json.getJSONArray("provinces");
			
			final List<Province> provinceList = getProvinceList(provinceArray);
			ProvinceDBService.getProvinceDBServiceInstance(MyApplication.getContext()).addProvince(provinceList);
			ConfigurationService.getConfigurationService(getContext()).setProvinceDataDownloaded();
			if(mInitialInfoResponse!=null)
			{
				mInitialInfoResponse.doAfterResponse();
			}
			else
			{
				traceError(TAG,"回调接口为null");
			}
			
			/*final Handler handler = new Handler()
			{
				@Override
				public void handleMessage(Message msg) {
					// TODO Auto-generated method stub
					super.handleMessage(msg);
					if(mInitialInfoResponse!=null)
					{
						mInitialInfoResponse.success();
					}
					else
					{
						traceError(TAG,"回调接口为null");
					}
					
				}
				
			};
			
			new Thread()
			{
				@Override
				public void run()
				{
					ProvinceDBService.getProvinceDBServiceInstance(getContext()).addProvince(provinceList);
					ConfigurationService.getConfigurationService(getContext()).setGrographyDataDownloaded();
					handler.handleMessage(null);
				}
			}.run();*/
			
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
		
	}
	
	
	public interface Callback
	{
		public void finish();
	}
	
	
	/***
	 * 根据provinceArray获取城市数据
	 * @param provinceArray
	 * @return
	 */
	private List<Province> getProvinceList(JSONArray provinceArray)
	{
		int length = provinceArray.length();
		List<Province> provinceList = new ArrayList<Province>(provinceArray.length());
		JSONObject json;
		Province province;
		try {
		for(int i=0; i<length;i++)
		{
			json =  provinceArray.getJSONObject(i);
			
			province = new Province();
			province.provinceCode = json.getString(KEY_PROVINCE_ID);
			province.provinceName = json.getString(KEY_PROVINCE_NAME);
			
			provinceList.add(province);
		}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			if(mInitialInfoResponse!=null)
			{
				mInitialInfoResponse.doAfterResponse();
			}
		}
		
		return provinceList;
	}
	
	public int getRequestState() {
		return requestState;
	}

	public boolean isSuccess() {
		return success;
	}
	
	public VolleyError getError() {
		return mError;
	}

	public boolean isFinished()
	{
		return requestState == FINISHED;
	}
	
	@Override
	public void requestAgain() {
		requestCityList(mInitialInfoResponse);
	}

	@Override
	protected void responceSuccess(String jstring) {
		//不进行任何操作
	}
}
