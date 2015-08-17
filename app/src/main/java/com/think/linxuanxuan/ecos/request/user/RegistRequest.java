package com.think.linxuanxuan.ecos.request.user;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request.Method;
import com.think.linxuanxuan.ecos.activity.MyApplication;
import com.think.linxuanxuan.ecos.constants.RequestUrlConstants;
import com.think.linxuanxuan.ecos.model.AccountDataService;
import com.think.linxuanxuan.ecos.model.User;
import com.think.linxuanxuan.ecos.model.UserDataService;
import com.think.linxuanxuan.ecos.request.BaseRequest;
import com.think.linxuanxuan.ecos.request.IBaseResponse;
import com.think.linxuanxuan.ecos.request.MyStringRequest;
import com.think.linxuanxuan.ecos.utils.StringUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * @author enlizhang
 * @ClassName: RegistRequest
 * @Description: 注册
 * @date 2015年7月26日 上午10:31:18
 */
public class RegistRequest extends BaseRequest {

    //请求参数键
    /**
     * 手机号
     */
    public static final String PHONE = "phone";

    /**
     * 昵称
     */
    public static final String NICK_NAME = "name";

    /**
     * 密码
     */
    public static final String PASSWORD = "pwd";

    /**
     * 头像URL
     */
    public static final String AVATAR_URL = "avatarUrl";

    //响应参数键
    IRegistResponse mRegistResponse;


    public String mPhone;
    public String mPwd;

    /**
     * 请求结束后把userId,imId,avatarUrl存入SharePreference
     *
     * @param registResponse
     * @param phone
     * @param password
     * @param nickName
     * @param avatarUrl
     */
    public void request(IRegistResponse registResponse, final String phone, final String password,
                        final String nickName, final String avatarUrl) {
        super.initBaseRequest(registResponse);
        mRegistResponse = registResponse;
        mPhone = phone;
        mPwd = password;

        MyStringRequest stringRequest = new MyStringRequest(Method.POST, RequestUrlConstants.REGIST_URL, this, this) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();

                map.put(PHONE, phone);
                map.put(PASSWORD, StringUtils.hashKeyForDisk(password));
                map.put(NICK_NAME, nickName);
                if (avatarUrl != null && !"".equals(avatarUrl))
                    map.put(AVATAR_URL, avatarUrl);

                traceNormal(TAG, map.toString());
                traceNormal(TAG, RegistRequest.this.getUrl(RequestUrlConstants.REGIST_URL, map));
                return map;
            }

        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        getQueue().add(stringRequest);
    }

    @Override
    public void responceSuccess(String jstring) {
        traceNormal(TAG, jstring);

        try {
            JSONObject usreJO = new JSONObject(jstring).getJSONObject(KEY_DATA);

            User user = new User();
            user.userId = getString(usreJO, "userId");
            user.imId = getString(usreJO, "imId");
            user.avatarUrl = getString(usreJO, "avatarUrl");
            user.nickname = getString(usreJO, "nickname");

            UserDataService userService = UserDataService.getSingleUserDataService(MyApplication.getContext());
            userService.saveUser(user);

            AccountDataService service = AccountDataService.getSingleAccountDataService(MyApplication.getContext());
            service.saveUserId(user.userId);
            service.saveUserAccId(user.imId);
            service.savePhone(mPhone);

            if (mRegistResponse != null) {
                mRegistResponse.success(mPhone,mPwd);
            } else {
                traceError(TAG, "回调接口为null");
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

            if (mBaseResponse != null) {
                mBaseResponse.doAfterFailedResponse("json异常");
            }
        }

    }


    public interface IRegistResponse extends IBaseResponse{

        public void success(String phone, String pwd);
    }
}

