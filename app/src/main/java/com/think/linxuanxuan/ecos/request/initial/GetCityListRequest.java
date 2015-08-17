package com.think.linxuanxuan.ecos.request.initial;

import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.think.linxuanxuan.ecos.constants.RequestUrlConstants;
import com.think.linxuanxuan.ecos.database.CityDBService;
import com.think.linxuanxuan.ecos.model.City;
import com.think.linxuanxuan.ecos.model.ConfigurationService;
import com.think.linxuanxuan.ecos.request.MyStringRequest;
import com.think.linxuanxuan.ecos.activity.SplashActivity.InitialInfoResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/***
 * 
* @ClassName: GetCityListRequest 
* @Description: 获取城市数据
* @author enlizhang
* @date 2015年8月5日 下午1:29:52 
*
 */
public class GetCityListRequest extends InitialRequest implements ErrorListener{
	
private final String TAG = "GetGeographyRequest";
	
	private boolean success = false;;
	private VolleyError mError = null;
	
	
	//市响应数据
	public String KEY_CITY_ID = "cityId";
	public String KEY_CITY_NAME = "cityName";
	public String KEY_PROVINCE_ID_IN_CITY = "provinceId";
	
	private boolean successRequestConfiguration=false;
	
	/*** 请求结束回调接口*/
	private InitialInfoResponse mInitialInfoResponse;
	

	public boolean requestCityList(InitialInfoResponse initialInfoResponse)
	{
		mInitialInfoResponse = initialInfoResponse;
	    
		MyStringRequest stringRequest = new MyStringRequest(Method.GET, RequestUrlConstants.CITY_LIST_URL,  this, this);
	    
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
			
			//处理市数据
			JSONArray cityArray = json.getJSONArray("cities");
			
			final List<City> cityList = getCityList(cityArray);
			CityDBService.getCityDBServiceInstance(getContext()).addCity(cityList);
			ConfigurationService.getConfigurationService(getContext()).setCityDataDownloaded();


			if(mInitialInfoResponse!=null)
			{
				mInitialInfoResponse.doAfterResponse();
			}
			else
			{
				traceError(TAG,"回调接口为null");
			}
			
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
	 * 根据cityArray获取城市数据
	 * @param cityArray
	 * @return
	 */
	private List<City> getCityList(JSONArray cityArray)
	{
		int length = cityArray.length();
		System.out.println("城市长度:"+length);
		City city; 
		List<City> cityList = new ArrayList<City>(cityArray.length());
		JSONObject json;
		try {
		for(int i=0; i<length;i++)
		{
			json =  cityArray.getJSONObject(i);
			
			city = new City();
			city.cityCode = json.getString(KEY_CITY_ID);
			city.cityName = json.getString(KEY_CITY_NAME);
			city.provinceId = json.getString(KEY_PROVINCE_ID_IN_CITY);
			
			cityList.add(city);
		}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
		
		return cityList;
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
