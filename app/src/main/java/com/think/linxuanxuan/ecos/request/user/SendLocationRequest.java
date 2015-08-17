package com.think.linxuanxuan.ecos.request.user;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request.Method;
import com.think.linxuanxuan.ecos.constants.RequestUrlConstants;
import com.think.linxuanxuan.ecos.request.BaseRequest;
import com.think.linxuanxuan.ecos.request.MyStringRequest;
import com.think.linxuanxuan.ecos.request.NorResponce;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 *
 * @ClassName: SendLocationRequest
 * @Description: 发送用户当前地理位置
 * @author enlizhang
 * @date 2015年7月26日 上午10:38:19
 *
 */
public class SendLocationRequest extends BaseRequest{

	//请求参数键
	/*** 精度 */
	public static final String LATITUDE = "latitude";

	/*** 维度 */
	public static final String LONGITUDE = "longitude";


	//响应参数键

	NorResponce mNorResponce;

	public void request(NorResponce norResponce, final String latitude, final String longitude)
	{
		super.initBaseRequest(norResponce);
		mNorResponce = norResponce;

		MyStringRequest stringRequest = new MyStringRequest(Method.POST, RequestUrlConstants.SEND_LOCATON_URL,  this, this) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> map = getRequestBasicMap();

				map.put(LATITUDE, latitude);
				map.put(LONGITUDE, longitude);

				traceNormal(TAG, map.toString());
				traceNormal(TAG, SendLocationRequest.this.getUrl(RequestUrlConstants.SEND_LOCATON_URL, map));
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


			if(mNorResponce!=null)
			{
				mNorResponce.success();
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

