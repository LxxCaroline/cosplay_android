package com.think.linxuanxuan.ecos.request.user;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request.Method;
import com.think.linxuanxuan.ecos.constants.RequestUrlConstants;
import com.think.linxuanxuan.ecos.model.User;
import com.think.linxuanxuan.ecos.request.BaseRequest;
import com.think.linxuanxuan.ecos.request.IBaseResponse;
import com.think.linxuanxuan.ecos.request.MyStringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * @author enlizhang
 * @ClassName: FollowedUserListRequest
 * @Description: 获取关注的人列表
 * @date 2015年7月26日 上午10:29:17
 */
public class FollowedUserListRequest extends BaseRequest {


    IFollowUserListResponce mFollowUserListResponce;

    /**
     * 请求我关注的人列表
     *
     * @param baseresponce
     */
    public void requestMyFollows(IFollowUserListResponce followUserListResponce, int pageIndex) {
        super.initBaseRequest(followUserListResponce);
        mFollowUserListResponce = followUserListResponce;
        String url = RequestUrlConstants.GET_FOLLED_USER_LIST + "type=follow" + "&" + "userId=" + getUserId()
                + "&" + "pageSize=20" + "&" + "pages=" + String.valueOf(pageIndex);
        traceNormal(TAG, url);

        MyStringRequest stringRequest = new MyStringRequest(Method.GET, url, this, this);

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        getQueue().add(stringRequest);

    }

    /**
     * 请求我的粉丝列表
     *
     * @param baseresponce
     */
    public void requestSomeOneFans(IFollowUserListResponce followUserListResponce, final String someoneUserId, final int pageIndex) {
        super.initBaseRequest(followUserListResponce);
        mFollowUserListResponce = followUserListResponce;

        String userId = someoneUserId;

        if (userId == null)
            userId = getUserId();

        String url = RequestUrlConstants.GET_FOLLED_USER_LIST + "type=fans" + "&" + "userId=" + userId
                + "&" + "pageSize=20" + "&" + "pages=" + String.valueOf(pageIndex);

        traceNormal(TAG, url);
        MyStringRequest stringRequest = new MyStringRequest(Method.GET, url, this, this);

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        getQueue().add(stringRequest);

    }

    @Override
    public void responceSuccess(String jstring) {
        traceNormal(TAG, jstring);

        try {
            JSONObject json = new JSONObject(jstring).getJSONObject(KEY_DATA);
            List<User> userList = new ArrayList<User>();
            if (json.has("users") && !json.isNull("users")) {
                JSONArray userJA = json.getJSONArray("users");
                int length = userJA.length();
                for (int i = 0; i < length; i++) {
                    JSONObject usreJO = userJA.getJSONObject(i);

                    User user = new User();
                    if (usreJO.has("roles") && !usreJO.isNull("roles")) {
                        JSONArray rolesJA = new JSONArray(getString(usreJO, "roles"));
                        Set<User.RoleType> roleTypeSet = new LinkedHashSet<User.RoleType>();
                        for (int j = 0; j < rolesJA.length(); j++) {
                            roleTypeSet.add(User.RoleType.getRoleTypeByValue(rolesJA.getString(j)));
                        }
                        user.roleTypeSet = roleTypeSet;
                    }
                    user.characterSignature = getString(usreJO, "characterSignature");
                    user.userId = getString(usreJO, "userId");
                    user.imId = getString(usreJO, "imId");
                    user.nickname = getString(usreJO, "nickname");
                    user.avatarUrl = getString(usreJO, "avatarUrl");

                    userList.add(user);
                }
            }
            if (mBaseResponse != null) {
                mFollowUserListResponce.success(userList);
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
     * @ClassName: IFollowUserListResponce
     * @Description: 本人关注的用户列表或粉丝列表请求响应回调接口
     * @date 2015年8月1日 上午9:25:18
     */
    public interface IFollowUserListResponce extends IBaseResponse {


        /**
         * 操作成功，并返回用户列表，该列表中的用户对象只包含userId、imId、nickname、avatarUrl
         *
         * @param userList
         */
        public void success(List<User> userList);
    }

}

