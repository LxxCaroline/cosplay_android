package com.think.linxuanxuan.ecos.request.user;

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
import com.think.linxuanxuan.ecos.request.IBaseResponse;
import com.think.linxuanxuan.ecos.request.MyStringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author enlizhang
 * @ClassName: GetUserInfoRequest
 * @Description: 获取用户信息
 * @date 2015年7月26日 上午10:30:34
 */

public class GetUserInfoRequest extends BaseRequest {


    public IGetUserInfoResponse mGetUserInfoResponse;

    public String mUserId;


    /**
     * 请求其他用户信息
     *
     * @param getUserInfoResponse
     * @param userId              其他用户id
     */
    public void requestOtherUserInfo(IGetUserInfoResponse getUserInfoResponse, final String userId) {
        super.initBaseRequest(getUserInfoResponse);
        mGetUserInfoResponse = getUserInfoResponse;
        mUserId = userId;
        //mGetUserInfoResponse.success(getTestUser());

        MyStringRequest stringRequest = new MyStringRequest(Method.POST, RequestUrlConstants.GET_USER_INFO, this, this) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = getRequestBasicMap();
                if (userId != null && !userId.equals(AccountDataService.getSingleAccountDataService(getContext()).getUserId())) {
                    map.put("toUserId", userId);
                    map.put("type", "other");
                } else {
                    map.put("type", "self");
                }
                traceNormal(TAG, map.toString());
                traceNormal(TAG, GetUserInfoRequest.this.getUrl(RequestUrlConstants.GET_USER_INFO, map));
                return map;
            }

        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000, 0, 0));

        getQueue().add(stringRequest);
    }



    /*public void requestPersonalInfo(IGetUserInfoResponse getUserInfoResponse) {
        super.initBaseRequest(getUserInfoResponse);
        mGetUserInfoResponse = getUserInfoResponse;
        //		mGetUserInfoResponse.success(getTestUser());

        MyStringRequest stringRequest = new MyStringRequest(Method.POST, RequestUrlConstants.GET_USER_INFO, this, this) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = getRequestBasicMap();

                traceNormal(TAG, map.toString());
                traceNormal(TAG, GetUserInfoRequest.this.getUrl(RequestUrlConstants.GET_USER_INFO, map));
                return map;
            }

        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        getQueue().add(stringRequest);
    }*/


    @Override
    public void responceSuccess(String jstring) {
        traceNormal(TAG, jstring);

        try {
            JSONObject usreJO = new JSONObject(jstring).getJSONObject(KEY_DATA);

            User user = new User();
            user.userId = getString(usreJO, "userId");
            user.imId = getString(usreJO, "imId");
            user.imToken = getString(usreJO, "imToken");
            user.avatarUrl = getString(usreJO, "avatarUrl");
            user.nickname = getString(usreJO, "nickname");

            user.characterSignature = getString(usreJO, "characterSignature");
            user.coverUrl = getString(usreJO, "coverUrl");
            user.avatarUrl = getString(usreJO, "avatarUrl");
            user.fansNum = getString(usreJO, "fansNum");
            user.followOtherNum = getString(usreJO, "followOtherNum");

            if (usreJO.has("gender") && !usreJO.isNull("gender")) {
                user.gender = Gender.getGender(usreJO.getString("gender"));
            }

            if (usreJO.has("roles") && !usreJO.isNull("roles")) {
                JSONArray rolesJA = new JSONArray(getString(usreJO, "roles"));
                Set<RoleType> roleTypeSet = new LinkedHashSet<RoleType>();
                for (int i = 0; i < rolesJA.length(); i++) {
                    roleTypeSet.add(RoleType.getRoleTypeByValue(rolesJA.getString(i)));
                }
                user.roleTypeSet = roleTypeSet;
            }


            //如果用户信息对应当前用户，则刷新用户信息
            if (getUserId().equals(user.userId)) {
                UserDataService.getSingleUserDataService(MyApplication.getContext()).saveUser(user);
                AccountDataService accountService = AccountDataService.getSingleAccountDataService(MyApplication.getContext());

                //保存userId、imId(云信id)、imtoken(云信token)
                accountService.saveUserId(user.userId);
                accountService.saveUserAccId(user.imId);
                accountService.saveUserImToken(user.imToken);
            }

            //请求个人
            if (mUserId == null) {
                if (mGetUserInfoResponse != null) {
                    mGetUserInfoResponse.success(user, false);
                } else {
                    traceError(TAG, "回调接口为null");
                }
            }
            //请求其他人
            else {
                if (mGetUserInfoResponse != null) {
                    if (usreJO.has("hasBeFollowed") && !usreJO.isNull("hasBeFollowed")) {
                        boolean hasFollowed = usreJO.getBoolean("hasFollowed");
                        mGetUserInfoResponse.success(user, hasFollowed);
                    }
                } else {
                    traceError(TAG, "回调接口为null");
                }
            }


        } catch (JSONException e) {
            e.printStackTrace();

            if (mBaseResponse != null) {
                mBaseResponse.doAfterFailedResponse("json异常");
            }
        }

    }

    /**
     * @author enlizhang
     * @ClassName: IGetUserInfoResponse
     * @Description: 获取个人信息响应回调接口
     * @date 2015年7月31日 下午8:16:08
     */
    public interface IGetUserInfoResponse extends IBaseResponse {

        /**
         */
        public void success(User user, boolean hasFollowed);

    }

}

