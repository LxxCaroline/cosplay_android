package com.think.linxuanxuan.ecos.request.user;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request.Method;
import com.think.linxuanxuan.ecos.constants.RequestUrlConstants;
import com.think.linxuanxuan.ecos.model.User;
import com.think.linxuanxuan.ecos.model.User.RoleType;
import com.think.linxuanxuan.ecos.model.UserDataService;
import com.think.linxuanxuan.ecos.request.BaseRequest;
import com.think.linxuanxuan.ecos.request.MyStringRequest;
import com.think.linxuanxuan.ecos.request.NorResponce;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/***
 *
 * @ClassName: UpdateUserInfoRequest
 * @Description: 更新用户信息
 * @author enlizhang
 * @date 2015年7月26日 上午10:38:52
 *
 */
public class UpdateUserInfoRequest extends BaseRequest{

	//请求参数键
	/*** 用户信息JO键 */
	public static final String USER_JSON = "userInfo";

	//响应参数键
	NorResponce mNorResponce;

	User mUser;

	public void request(NorResponce norResponce, final User user)
	{
		super.initBaseRequest(norResponce);
		mNorResponce = norResponce;
		mUser = user;

		MyStringRequest stringRequest = new MyStringRequest(Method.POST, RequestUrlConstants.UPDATE_USER_INFO,  this, this) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> map = getRequestBasicMap();

				map.put(USER_JSON, getUserJSonDescription(user));

				traceNormal(TAG, map.toString());
				traceNormal(TAG, UpdateUserInfoRequest.this.getUrl(RequestUrlConstants.UPDATE_USER_INFO, map));
				return map;
			}

		};
		stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000, 0, 0));

		getQueue().add(stringRequest);

	}

	@Override
	public void responceSuccess(String jstring) {
		traceNormal(TAG, jstring);

		try {
			JSONObject json = new JSONObject(jstring).getJSONObject(KEY_DATA);

			UserDataService.getSingleUserDataService(getContext()).saveUser(mUser);

			Log.e("修改后配置文件数据",mUser.toString());
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

	/***
	 * 根据User对象获取请求Json串,包括avatarUrl、characterSignature、coverUrl、fansNum、followOtherNum、gender、nickname、roles
	 * @return
	 */
	private String getUserJSonDescription(User user) {
		Map<Object,Object> userMap = new HashMap<Object,Object>();
		addProIntoMap(userMap,"avatarUrl",user.avatarUrl);
		addProIntoMap(userMap,"characterSignature",user.characterSignature);
		addProIntoMap(userMap,"coverUrl",user.coverUrl);
		addProIntoMap(userMap,"fansNum",user.fansNum);
		addProIntoMap(userMap,"followOtherNum",user.followOtherNum);
		addProIntoMap(userMap,"gender",user.gender.getValue());
		addProIntoMap(userMap,"nickname",user.nickname);

		List<String> roles = new ArrayList<String>();
		if(user.roleTypeSet!=null && user.roleTypeSet.size()!=0){

			for(RoleType role:user.roleTypeSet){
				roles.add(role.getBelongs());
			}

			userMap.put("roles", roles);
		}


		return new JSONObject(userMap).toString();
	}

	public void addProIntoMap(Map<Object,Object> userMap, String key, String value){
		if( null!=value && !"".equals(value))
			userMap.put(key, value);

	}


}

