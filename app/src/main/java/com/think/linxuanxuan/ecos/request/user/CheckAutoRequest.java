package com.think.linxuanxuan.ecos.request.user;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request.Method;
import com.think.linxuanxuan.ecos.constants.RequestUrlConstants;
import com.think.linxuanxuan.ecos.request.BaseRequest;
import com.think.linxuanxuan.ecos.request.IBaseResponse;
import com.think.linxuanxuan.ecos.request.MyStringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * @author enlizhang
 * @Title: CheckAutoRequest.java
 * @Description:
 * @date 2015年7月31日 下午1:06:30
 */

public class CheckAutoRequest extends BaseRequest {

    //请求参数键
    /**
     * 请求类型,  get或者check
     */
    public static final String TYPE = "type";

    /**
     * 手机号
     */
    public static final String PHONE = "phone";

    /**
     * 验证码
     */
    public static final String AUTOCODE = "code";


    /**
     * 请求结束回掉函数
     */
    ICheckAutocodeResponse mCheckAutocodeResponse;

    /**
     * 核对验证码
     *
     * @param checkAutocodeResponse
     * @param phone
     * @param autocode              验证码
     */
    public void requestCheck(ICheckAutocodeResponse checkAutocodeResponse, final String phone, final String autocode) {
        super.initBaseRequest(checkAutocodeResponse);
        mCheckAutocodeResponse = checkAutocodeResponse;


        MyStringRequest stringRequest = new MyStringRequest(Method.POST, RequestUrlConstants.AUTO_CODE_URL, this, this) {

			/*public Map<String, String> getHeaders() throws AuthFailureError {
	            HashMap localHashMap = new HashMap();  
	            String cookie = AccountDataService.getSingleAccountDataService(getContext()).getAutocodeCookie();
	            Log.e(TAG, "cookieHead---------"  + cookie);
	            
	            localHashMap.put("Cookie", cookie);  
	            return localHashMap;  
	        }*/

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();

                map.put(TYPE, "check");
                map.put(PHONE, phone);
                map.put(AUTOCODE, autocode);

                traceNormal(TAG, map.toString());
                traceNormal(TAG, CheckAutoRequest.this.getUrl(RequestUrlConstants.AUTO_CODE_URL, map));
                return map;
            }

        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000, 0, 0));

        getQueue().add(stringRequest);

    }

    @Override
    public void responceSuccess(String jstring) {
        traceNormal(TAG, jstring);

        if (mCheckAutocodeResponse != null) {
            mCheckAutocodeResponse.success();
        } else {
            traceError(TAG, "回调接口为null");
        }
		/*try {
			JSONObject json = new JSONObject(jstring).getJSONObject(KEY_DATA);
			
			
			if(mCheckAutocodeResponse!=null)
			{
				mCheckAutocodeResponse.success();
			}
			else
			{
				traceError(TAG,"回调接口为null");
			}
			
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			if(mCheckAutocodeResponse!=null)
			{
				mCheckAutocodeResponse.doAfterFailedResponse("json异常");
			}
		}*/

    }

    /**
     * @author enlizhang
     * @ClassName: ICheckAutocodeResponse
     * @Description: 核对验证码回掉接口
     * @date 2015年7月31日 下午12:50:12
     */
    public interface ICheckAutocodeResponse extends IBaseResponse {

        /**
         * 验证成功回调函数
         */
        public void success();

    }
}

