package com.think.linxuanxuan.ecos.request.user;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request.Method;
import com.think.linxuanxuan.ecos.constants.RequestUrlConstants;
import com.think.linxuanxuan.ecos.request.BaseRequest;
import com.think.linxuanxuan.ecos.request.MyStringRequest;
import com.think.linxuanxuan.ecos.request.NorResponce;
import com.think.linxuanxuan.ecos.utils.StringUtils;

import java.util.HashMap;
import java.util.Map;

/***
 *
 * @ClassName: ResetPasswordRequest
 * @Description: 重置密码
 * @author enlizhang
 * @date 2015年7月26日 上午10:37:23
 *
 */
public class ResetPasswordRequest extends BaseRequest{

	//请求参数键
	/*** 手机号 */
	public static final String PHONE = "phone";

	/*** 密码 */
	public static final String PASSWORD = "pwd";


	//响应参数键
	NorResponce mNorResponce;


	public void request(NorResponce norResponce, final String phone, final String password)
	{
		super.initBaseRequest(norResponce);
		mNorResponce = norResponce;

		MyStringRequest stringRequest = new MyStringRequest(Method.POST, RequestUrlConstants.RESET_PASSWORD_URL,  this, this) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> map = new HashMap<String, String>();

				map.put(PHONE, phone);
				map.put(PASSWORD, StringUtils.hashKeyForDisk(password));

				traceNormal(TAG, map.toString());
				traceNormal(TAG, ResetPasswordRequest.this.getUrl(RequestUrlConstants.RESET_PASSWORD_URL, map));
				return map;
			}

		};

		stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000, 0, 0));

		getQueue().add(stringRequest);

	}

	@Override
	public void responceSuccess(String jstring) {
		traceNormal(TAG, jstring);
		if(mNorResponce!=null)
		{
			mNorResponce.success();
		}
		else
		{
			traceError(TAG,"回调接口为null");
		}

	}

}

