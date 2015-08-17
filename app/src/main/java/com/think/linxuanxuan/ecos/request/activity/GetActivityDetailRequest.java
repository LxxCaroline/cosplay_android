package com.think.linxuanxuan.ecos.request.activity;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request.Method;
import com.think.linxuanxuan.ecos.constants.RequestUrlConstants;
import com.think.linxuanxuan.ecos.database.CityDBService;
import com.think.linxuanxuan.ecos.database.ProvinceDBService;
import com.think.linxuanxuan.ecos.model.ActivityModel;
import com.think.linxuanxuan.ecos.model.ActivityModel.ActivityType;
import com.think.linxuanxuan.ecos.model.ActivityModel.ContactWay;
import com.think.linxuanxuan.ecos.model.User;
import com.think.linxuanxuan.ecos.request.BaseRequest;
import com.think.linxuanxuan.ecos.request.IBaseResponse;
import com.think.linxuanxuan.ecos.request.MyStringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author enlizhang
 * @ClassName: GetActivityDetailRequest
 * @Description: 获取活动详情
 * @date 2015年7月26日 下午1:00:05
 */
public class GetActivityDetailRequest extends BaseRequest {

    //请求参数键

    //响应参数键
    IActivityDetailResponse mActivityDetailResponse;
    ProvinceDBService provinceDBService;
    CityDBService cityDBService;


    public void request(IActivityDetailResponse activityDetailResponse, final String activityId) {
        super.initBaseRequest(activityDetailResponse);
        mActivityDetailResponse = activityDetailResponse;

        MyStringRequest stringRequest = new MyStringRequest(Method.POST, RequestUrlConstants.GET_ACTIVITY_DETAIL_URL, this, this) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = getRequestBasicMap();

                map.put("activityId", activityId);

                traceNormal(TAG, map.toString());
                traceNormal(TAG, GetActivityDetailRequest.this.getUrl(RequestUrlConstants.GET_ACTIVITY_DETAIL_URL, map));
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
            JSONObject activityJO = json;

            ActivityModel activity = new ActivityModel();
            activity.activityId = activityJO.getString("activityId");
            activity.title = activityJO.getString("title");
            activity.activityType = ActivityType.getActivityTypeByValue(activityJO.getString("activityType"));
            activity.coverUrl = getString(activityJO, "logoUrl");
            activity.userId = activityJO.getString("userId");

            activity.activityTime.startDateStamp = Long.valueOf(activityJO.getString("startDateStamp")).longValue();
            activity.activityTime.endDateStamp = Long.valueOf(activityJO.getString("endDateStamp")).longValue();
            activity.activityTime.dayStartTime = activityJO.getString("dayStartTime");
            activity.activityTime.dayEndTime = activityJO.getString("dayEndTime");

            activity.issueTimeStamp = Long.valueOf(activityJO.getString("issueTimeStamp")).longValue();

            activity.introduction = activityJO.getString("description");
            activity.fee = activityJO.getString("fee");
            //发布者头像url
            activity.avatarUrl = getString(activityJO, "avatarUrl");
            //发布者姓名
            activity.nickname = activityJO.getString("nickName");

            activity.location.province.provinceCode = activityJO.getString("provinceCode");
            activity.location.city.cityCode = activityJO.getString("cityCode");
            if (provinceDBService == null)
                provinceDBService = ProvinceDBService.getProvinceDBServiceInstance(getContext());
            if (cityDBService == null)
                cityDBService = CityDBService.getCityDBServiceInstance(getContext());
            activity.location.province.provinceName = provinceDBService.getProvinceName(activity.location.province.provinceCode);
            activity.location.city.cityName = cityDBService.getCityName(activity.location.city.cityCode);
            activity.location.address = activityJO.getString("address");

            JSONArray contactWayJA = activityJO.getJSONArray("contacts");

            List<ActivityModel.Contact> contactWayList = new ArrayList<ActivityModel.Contact>();
            for (int i = 0; i < contactWayJA.length(); i++) {
                JSONObject contactWayJO = contactWayJA.getJSONObject(i);

                ActivityModel.Contact contact = new ActivityModel.Contact();
                if (contactWayJO.has("contactValue") && !contactWayJO.isNull("contactValue")) {
                    contact.contactWay = ContactWay.getContactWayByValue(contactWayJO.getString("contactType"));
                    contact.value = getString(contactWayJO, "contactValue");
                    contactWayList.add(contact);
                }
            }
            activity.contactWayList = contactWayList;

            JSONArray signUpUserJA = activityJO.getJSONArray("signUpUsers");
            for (int i = 0; i < signUpUserJA.length(); i++) {
                JSONObject signUpUserJO = signUpUserJA.getJSONObject(i);
                User user = new User();
                user.userId = signUpUserJO.getString("userId");
                user.avatarUrl = getString(signUpUserJO, "avatarUrl");
                user.nickname = signUpUserJO.getString("nickName");
                activity.signUpUseList.add(user);
            }
            activity.loveNums = activity.signUpUseList.size();

            activity.hasSignuped = activityJO.getBoolean("hasSignuped");
            activity.hasStarted = activityJO.getBoolean("hasStarted");
            activity.hasFinshed = activityJO.getBoolean("hasFinished");


            if (mActivityDetailResponse != null) {
                mActivityDetailResponse.success(activity);
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
     * @ClassName: IActivityDetailResponse
     * @Description: 活动详情请求回掉接口
     * @date 2015年7月30日 下午4:56:28
     */
    public interface IActivityDetailResponse extends IBaseResponse {

        /**
         * 请求成功回调函数，并返回活动详情
         *
         * @param activity
         */
        public void success(ActivityModel activity);

    }

}

