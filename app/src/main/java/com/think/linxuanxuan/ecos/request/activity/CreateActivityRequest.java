package com.think.linxuanxuan.ecos.request.activity;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.think.linxuanxuan.ecos.constants.RequestUrlConstants;
import com.think.linxuanxuan.ecos.model.ActivityModel;
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
 * @ClassName: CreateActivityRequest
 * @Description: 创建活动
 * @date 2015年7月26日 下午12:59:53
 */
public class CreateActivityRequest extends BaseRequest {

    //请求参数键
    /**
     * Activity对象数据json串，空的属性用"null"字符串
     */
    public static final String ACTIVITY_JSON = "activityJson";


    //响应参数键
    ActivityModel mActivity;

    ICreateActivityResponce mCreateActivityResponce;

    /**
     * 用于测试，验证请求数据完整性以及请求数据封装正确性，例如无null
     *
     * @param createActivityResponce
     * @param activity
     */
    public void testData(ICreateActivityResponce createActivityResponce, final ActivityModel activity) {
        Map<String, String> map = getRequestBasicMap();
        map.put(ACTIVITY_JSON, getRequestActivityJSon(activity));
        traceNormal(TAG, map.toString());
        mCreateActivityResponce = createActivityResponce;
        mCreateActivityResponce.success(activity);
    }

    /**
     * @param createActivityResponce 创建活动请求回掉接口
     * @param activity
     */
    public void request(ICreateActivityResponce createActivityResponce, final ActivityModel activity) {
        super.initBaseRequest(createActivityResponce);
        mCreateActivityResponce = createActivityResponce;
        mActivity = activity;
//        mCreateActivityResponce.success(mActivity);

        MyStringRequest stringRequest = new MyStringRequest(Request.Method.POST, RequestUrlConstants.CREATE_ACTIVITY_URL, this, this) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = getRequestBasicMap();

                map.put(ACTIVITY_JSON, getRequestActivityJSon(activity));

                traceNormal(TAG, map.toString());
                traceNormal(TAG, CreateActivityRequest.this.getUrl(RequestUrlConstants.CREATE_ACTIVITY_URL, map));
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
            JSONObject json = new JSONObject(jstring);


            if (mCreateActivityResponce != null) {
                mCreateActivityResponce.success(mActivity);
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
     * 获取Activity的JSON串
     *
     * @param activity
     * @return
     */
    public String getRequestActivityJSon(ActivityModel activity) {
        Map<Object, Object> jsonMap = new HashMap<Object, Object>();

        jsonMap.put("title", activity.title);
        jsonMap.put("activityType", activity.activityType.getValue());
        jsonMap.put("logoUrl", activity.coverUrl);

        jsonMap.put("startDateStamp", String.valueOf(activity.activityTime.startDateStamp));
        jsonMap.put("endDateStamp", String.valueOf(activity.activityTime.endDateStamp));
        jsonMap.put("dayStartTime",String.valueOf(activity.activityTime.dayStartTime));
        jsonMap.put("dayEndTime",String.valueOf(activity.activityTime.dayEndTime));

        jsonMap.put("description", activity.introduction);
        jsonMap.put("fee", activity.fee);


        jsonMap.put("provinceCode", activity.location.province.provinceCode);
        jsonMap.put("cityCode", activity.location.city.cityCode);
        jsonMap.put("address", activity.location.address);
        try {

            List<Object> contactWayList = new ArrayList<Object>();

            for (int i = 0; i < activity.contactWayList.size(); i++) {
                JSONObject contactWayJO = new JSONObject();
                contactWayJO.put("contactType", activity.contactWayList.get(i).contactWay.getType());
                contactWayJO.put("contactValue", activity.contactWayList.get(i).value);
                contactWayList.add(contactWayJO);
            }
            jsonMap.put("contacts", new JSONArray(contactWayList));


        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return new JSONObject(jsonMap).toString();
    }


    /**
     * @author enlizhang
     * @ClassName: CreateActivityResponce
     * @Description: 创建活动回调接口
     * @date 2015年7月26日 下午8:23:15
     */
    public interface ICreateActivityResponce extends IBaseResponse {
        /**
         * 请求成功回掉函数，并返回创建的活动
         */
        public void success(ActivityModel activity);
    }

}

