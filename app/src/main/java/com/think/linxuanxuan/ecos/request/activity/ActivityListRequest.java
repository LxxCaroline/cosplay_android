package com.think.linxuanxuan.ecos.request.activity;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request.Method;
import com.think.linxuanxuan.ecos.constants.RequestUrlConstants;
import com.think.linxuanxuan.ecos.database.CityDBService;
import com.think.linxuanxuan.ecos.database.ProvinceDBService;
import com.think.linxuanxuan.ecos.model.ActivityModel;
import com.think.linxuanxuan.ecos.model.ActivityModel.ActivityType;
import com.think.linxuanxuan.ecos.request.BaseRequest;
import com.think.linxuanxuan.ecos.request.IBaseResponse;
import com.think.linxuanxuan.ecos.request.MyStringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author enlizhang
 * @ClassName: ActivityListRequest
 * @Description: 活动列表
 * @date 2015年7月26日 下午12:58:47
 */
public class ActivityListRequest extends BaseRequest {

    //请求参数键


    //响应参数键
    public IActivityListResponse mActivityListResponse;
    ProvinceDBService provinceDBService;
    CityDBService cityDBService;

    /**
     * @param baseresponce
     * @param provinceId   省id
     * @param activityType {@link ActivityType}活动类型
     * @param pageIndex    请求页数
     */
    public void request(IActivityListResponse activityListResponse, final String provinceId, final ActivityType activityType,
                        final int pageIndex) {
        super.initBaseRequest(activityListResponse);
        mActivityListResponse = activityListResponse;

        MyStringRequest stringRequest = new MyStringRequest(Method.POST, RequestUrlConstants.GET_ACTIVITY_LIST_URL, this, this) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = getRequestBasicMap();

                map.put("isMySelf", "false");
                map.put("provinceId", provinceId);
                if (activityType != null)
                    map.put("activityType", activityType.getValue());
                map.put(KEY_PAGE_SIZE, String.valueOf(DEFAULT_PAGE_SIZE));
                map.put(KEY_PAGE_INDEX, String.valueOf(pageIndex));

                traceNormal(TAG, map.toString());
                traceNormal(TAG, ActivityListRequest.this.getUrl(RequestUrlConstants.GET_ACTIVITY_LIST_URL, map));
                return map;
            }

        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        getQueue().add(stringRequest);

    }


    public static final String VALUE_MY_ACTIVITYS = "6";


    /**
     * 获取个人活动列表
     *
     * @param activityListResponse
     * @param pageIndex
     */
    public void requestMySelf(IActivityListResponse activityListResponse, final int pageIndex) {
        super.initBaseRequest(activityListResponse);
        mActivityListResponse = activityListResponse;

        MyStringRequest stringRequest = new MyStringRequest(Method.POST, RequestUrlConstants.GET_ACTIVITY_LIST_URL, this, this) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = getRequestBasicMap();

                //活动类别为个人
                map.put("isMyself", "true");
                map.put(KEY_PAGE_SIZE, String.valueOf(DEFAULT_PAGE_SIZE));
                map.put(KEY_PAGE_INDEX, String.valueOf(pageIndex));

                traceNormal(TAG, map.toString());
                traceNormal(TAG, ActivityListRequest.this.getUrl(RequestUrlConstants.GET_ACTIVITY_LIST_URL, map));
                return map;
            }

        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        getQueue().add(stringRequest);

    }

    /**
     * 获取其他人活动列表
     *
     * @param activityListResponse
     * @param otherUserId          要查询对象的userId
     * @param pageIndex
     */
    public void requestOtherActivityList(IActivityListResponse activityListResponse, final String otherUserId, final int pageIndex) {
        super.initBaseRequest(activityListResponse);
        mActivityListResponse = activityListResponse;

        MyStringRequest stringRequest = new MyStringRequest(Method.POST, RequestUrlConstants.GET_ACTIVITY_LIST_URL, this, this) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();

                if (otherUserId == null)
                    map.put(KEY_USER_ID, getUserId());
                else
                    map.put(KEY_USER_ID, otherUserId);

                //活动类别为个人
                map.put("isMyself", "true");
                map.put(KEY_PAGE_SIZE, String.valueOf(DEFAULT_PAGE_SIZE));
                map.put(KEY_PAGE_INDEX, String.valueOf(pageIndex));

                traceNormal(TAG, map.toString());
                traceNormal(TAG, ActivityListRequest.this.getUrl(RequestUrlConstants.GET_ACTIVITY_LIST_URL, map));
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
            JSONObject activitJO = json;

            JSONArray activityJA = activitJO.getJSONArray("activitys");

            int length = activityJA.length();

            List<ActivityModel> activityList = new ArrayList<ActivityModel>();
            for (int i = 0; i < length; i++) {
                JSONObject activityJO = activityJA.getJSONObject(i);
                ActivityModel activity = new ActivityModel();
                activity.activityId = activityJO.getString("activityId");
                activity.coverUrl = getString(activityJO, "logoUrl");
                activity.title = activityJO.getString("title");
                activity.fee = activityJO.getString("fee");

                activity.activityTime.startDateStamp = Long.valueOf(activityJO.getString("startDateStamp")).longValue();
                activity.activityTime.endDateStamp = Long.valueOf(activityJO.getString("endDateStamp")).longValue();
                activity.activityTime.dayStartTime = activityJO.getString("dayStartTime");
                activity.activityTime.dayEndTime = activityJO.getString("dayEndTime");

                activity.location.province.provinceCode = activityJO.getString("provinceCode");
                activity.location.city.cityCode = activityJO.getString("cityCode");
                if (provinceDBService == null)
                    provinceDBService = ProvinceDBService.getProvinceDBServiceInstance(getContext());
                if (cityDBService == null)
                    cityDBService = CityDBService.getCityDBServiceInstance(getContext());
                activity.location.province.provinceName = provinceDBService.getProvinceName(activity.location.province.provinceCode);
                activity.location.city.cityName = cityDBService.getCityName(activity.location.city.cityCode);
                activity.location.address = activityJO.getString("address");

                activity.issueTimeStamp = Long.valueOf(activityJO.getString("issueTimeStamp")).longValue();
                activity.activityType = ActivityType.getActivityTypeByValue(activityJO.getString("activityType"));

                activityList.add(activity);
            }

            if (mActivityListResponse != null) {
                mActivityListResponse.success(activityList);
            } else {
                traceError(TAG, "回调接口为null");
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
     * @ClassName: IActivityListResponse
     * @Description: 获取活动列表回调接口
     * @date 2015年7月30日 下午1:41:06
     */
    public interface IActivityListResponse extends IBaseResponse {

        /**
         * 请求成功，返回教程列表
         */
        public void success(List<ActivityModel> activityList);

    }


    public List<ActivityModel> getTestActivityList() {

        List<ActivityModel> activityList = new ArrayList<ActivityModel>();
        for (int i = 0; i < 2; i++) {

            ActivityModel acitivty = new ActivityModel();
            acitivty.activityId = "" + i;
            acitivty.coverUrl = "http://images.china.cn/attachement/jpg/site1000/20120711/001f1632fbfd116783094c.jpg";
            acitivty.title = "活动列表测试" + i;

            acitivty.activityTime.startDateStamp = System.currentTimeMillis() - 24 * 60 * 60 * 1000;
            acitivty.activityTime.endDateStamp = System.currentTimeMillis();

            acitivty.activityTime.dayStartTime = "10:00";
            acitivty.activityTime.dayEndTime = "17:00";


            acitivty.location.city.cityName = "杭州市";
            acitivty.location.city.cityCode = "12";
            acitivty.location.address = "滨江区网商路599号网易";

            acitivty.activityType = ActivityType.同人展;

            activityList.add(acitivty);

        }

        return activityList;
    }

}

