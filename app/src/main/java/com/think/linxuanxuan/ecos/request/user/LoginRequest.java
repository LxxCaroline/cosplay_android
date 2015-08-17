package com.think.linxuanxuan.ecos.request.user;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request.Method;
import com.think.linxuanxuan.ecos.activity.MyApplication;
import com.think.linxuanxuan.ecos.constants.RequestUrlConstants;
import com.think.linxuanxuan.ecos.model.AccountDataService;
import com.think.linxuanxuan.ecos.model.User;
import com.think.linxuanxuan.ecos.model.User.Gender;
import com.think.linxuanxuan.ecos.model.User.RoleType;
import com.think.linxuanxuan.ecos.model.UserDataService;
import com.think.linxuanxuan.ecos.request.BaseRequest;
import com.think.linxuanxuan.ecos.request.MyStringRequest;
import com.think.linxuanxuan.ecos.request.NorResponce;
import com.think.linxuanxuan.ecos.utils.StringUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/***
 *
 * @ClassName: LoginRequest
 * @Description: 登录
 * @author enlizhang
 * @date 2015年7月26日 上午10:30:53
 *
 */
public class LoginRequest extends BaseRequest{

	//请求参数键
	/*** 手机号 */
	public static final String PHONE = "phone";

	/*** 密码 */
	public static final String PASSWORD = "pwd";


	//响应参数键
	NorResponce mNorResponce;

	public String mPhone;

	public void request(NorResponce norResponce , final String phone, final String password)
	{
		super.initBaseRequest(norResponce);
		mNorResponce = norResponce;
		mPhone = phone;


		MyStringRequest stringRequest = new MyStringRequest(Method.POST, RequestUrlConstants.LOLGIN_URL,  this, this) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> map = new HashMap<String, String>();

				map.put(PHONE, phone);
				map.put(PASSWORD, StringUtils.hashKeyForDisk(password));

				traceNormal(TAG, map.toString());
				traceNormal(TAG, LoginRequest.this.getUrl(RequestUrlConstants.LOLGIN_URL, map));
				return map;
			}

		};

		stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000,0,0));

		getQueue().add(stringRequest);

	}

	@Override
	public void responceSuccess(String jstring) {
		//		traceNormal(TAG, jstring);

		try {
			JSONObject usreJO = new JSONObject(jstring).getJSONObject(KEY_DATA);

			User user = new User();
			user.userId = getString(usreJO,"userId");
			user.imId = getString(usreJO,"imId");
			user.imToken = getString(usreJO,"imToken");
			user.avatarUrl = getString(usreJO,"avatarUrl");
			user.nickname = getString(usreJO,"nickname");

			user.characterSignature = getString(usreJO,"characterSignature");
			user.coverUrl = getString(usreJO,"coverUrl");
			user.avatarUrl = getString(usreJO,"avatarUrl");
			user.fansNum = getString(usreJO,"fansNum");
			user.followOtherNum = getString(usreJO,"followOtherNum");

			if(usreJO.has("gender") && !usreJO.isNull("gender")){
				user.gender = Gender.getGender( usreJO.getString("gender") );
			}

			if(usreJO.has("roles") && !usreJO.isNull("roles")){
				JSONArray rolesJA = new JSONArray(getString(usreJO, "roles"));
				Set<RoleType> roleTypeSet = new LinkedHashSet<RoleType>();
				for(int i=0;i<rolesJA.length();i++){
					roleTypeSet.add( RoleType.getRoleTypeByValue(rolesJA.getString(i)) );
				}
				user.roleTypeSet = roleTypeSet;
			}


			UserDataService.getSingleUserDataService(MyApplication.getContext()).saveUser(user);
			Log.i("登录成功","用户信息:" + user.toString());
			AccountDataService accountService = AccountDataService.getSingleAccountDataService(MyApplication.getContext());

			//保存userId、imId(云信id)、imtoken(云信token)
			accountService.saveUserId(user.userId);
			accountService.saveUserAccId(user.imId);
			accountService.saveUserImToken(user.imToken);
			accountService.savePhone(mPhone);

			Log.e("登录存储后查询用户信息", UserDataService.getSingleUserDataService(getContext()).getUser().toString());
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


	public void testLogin(){

		User user = new User();

		user.userId = "123";
		user.imId = "321";
		user.avatarUrl = "http://img5.imgtn.bdimg.com/it/u=2797503391,4239472514&fm=21&gp=0.jpg";
		user.nickname = "小张";

		user.characterSignature = "网易遇见最美年华";
		user.coverUrl = "http://img4.imgtn.bdimg.com/it/u=2501638931,3725345607&fm=21&gp=0.jpg";
		user.fansNum = "100";
		user.followOtherNum = "5";
		user.gender = Gender.getGender("1");

		Set<RoleType> roleTypeSet = new LinkedHashSet<RoleType>();
		roleTypeSet.add(RoleType.妆娘);
		roleTypeSet.add(RoleType.后期);
		roleTypeSet.add(RoleType.妆娘);
		user.roleTypeSet = roleTypeSet;

		UserDataService.getSingleUserDataService(MyApplication.getContext()).saveUser(user);
		AccountDataService.getSingleAccountDataService(MyApplication.getContext()).saveUserAccId(user.imId);
		AccountDataService.getSingleAccountDataService(MyApplication.getContext()).saveUserId(user.userId);
	}

}

