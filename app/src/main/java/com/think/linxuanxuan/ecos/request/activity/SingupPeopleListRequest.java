package com.think.linxuanxuan.ecos.request.activity;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request.Method;
import com.think.linxuanxuan.ecos.constants.RequestUrlConstants;
import com.think.linxuanxuan.ecos.model.User;
import com.think.linxuanxuan.ecos.model.User.RoleType;
import com.think.linxuanxuan.ecos.request.BaseRequest;
import com.think.linxuanxuan.ecos.request.IBaseResponse;
import com.think.linxuanxuan.ecos.request.MyStringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class SingupPeopleListRequest extends BaseRequest {

    //请求参数键

    //响应参数键

    public ISignupPeopleListResponce mSignupPeopleListResponce;

    public void request(ISignupPeopleListResponce signupPeopleListResponce,
                        final String activityId, final int pageIndex) {
        super.initBaseRequest(signupPeopleListResponce);
        mSignupPeopleListResponce = signupPeopleListResponce;

        MyStringRequest stringRequest = new MyStringRequest(Method.POST, RequestUrlConstants.ACTIVITY_SIGNUP_PEOPLE_URL, this, this) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = getRequestBasicMap();

                map.put("activityId", activityId);
                map.put(KEY_PAGE_SIZE, String.valueOf(DEFAULT_PAGE_SIZE));
                map.put(KEY_PAGE_INDEX, String.valueOf(pageIndex));

                traceNormal(TAG, map.toString());
                traceNormal(TAG, SingupPeopleListRequest.this.getUrl(RequestUrlConstants.ACTIVITY_SIGNUP_PEOPLE_URL, map));
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
            JSONObject json = new JSONObject(jstring).getJSONObject(KEY_DATA);
            JSONObject userJO = json;

            JSONArray userJA = userJO.getJSONArray("users");
            int length = userJA.length();

            List<User> userList = new ArrayList<User>(length);
            boolean[] hasFollowEd = new boolean[length];
            boolean[] beFollowed = new boolean[length];

            for (int i = 0; i < length; i++) {
                JSONObject usreJO = userJA.getJSONObject(i);

                User user = new User();
                user.userId = usreJO.getString("userId");
                user.imId = getString(usreJO, "im_id");
                user.characterSignature = getString(usreJO, "characterSignature");
                user.nickname = usreJO.getString("nickName");
                user.avatarUrl = getString(usreJO, "avatarUrl");

                if (usreJO.has("roleType") && !usreJO.isNull("roleType")) {
                    JSONArray rolesJA = new JSONArray(getString(usreJO, "roleType"));
                    Set<RoleType> roleTypeSet = new LinkedHashSet<RoleType>();
                    for (int index = 0; index < rolesJA.length(); index++) {
                        roleTypeSet.add(RoleType.getRoleTypeByValue(rolesJA.getString(index)));
                    }
                    user.roleTypeSet = roleTypeSet;
                }

                hasFollowEd[i] = Boolean.valueOf(usreJO.getString("hasFollowed"));
                beFollowed[i] = Boolean.valueOf(usreJO.getString("beFollowed"));
                userList.add(user);
            }

            if (mSignupPeopleListResponce != null) {
                mSignupPeopleListResponce.success(userList, hasFollowEd, beFollowed);
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


    /**
     * @author enlizhang
     * @ClassName: ISignupPeopleListResponce
     * @Description: 获取报名用户列表回调接口
     * @date 2015年7月30日 下午3:06:47
     */
    public interface ISignupPeopleListResponce extends IBaseResponse {
        /**
         * 请求成功回掉函数，其中usrList.get(index)、hasFollowEd[index],hanBeenFollowed[index]一一对应
         *
         * @param userList    用户列表
         * @param hasFollowEd 本人是否已经关注该用户，true:已关注，false:没关注
         * @param beFollowed  本人是否被该人关注, true:已被关注，false:没被关注
         */
        public void success(List<User> userList, boolean[] hasFollowEd, boolean[] beFollowed);
    }
}

