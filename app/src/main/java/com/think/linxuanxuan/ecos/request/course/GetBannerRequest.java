package com.think.linxuanxuan.ecos.request.course;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.think.linxuanxuan.ecos.constants.RequestUrlConstants;
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
 * @ClassName: GetBannerRequest
 * @Description: 获取运营广告
 * @date 2015年7月26日 上午11:03:52
 */
public class GetBannerRequest extends BaseRequest {


    IGetBannerResponse mGetBannerResponce;

    public void request(IGetBannerResponse getBannerResponce) {
        super.initBaseRequest(getBannerResponce);
        mGetBannerResponce = getBannerResponce;

        MyStringRequest stringRequest = new MyStringRequest(Request.Method.POST, RequestUrlConstants.GET_BANNER, this, this) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = getRequestBasicMap();
                traceNormal(TAG, map.toString());
                traceNormal(TAG, GetBannerRequest.this.getUrl(RequestUrlConstants.GET_BANNER, map));
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
            List<String> bannerList = new ArrayList<String>();
            JSONArray bannerJA = json.getJSONArray("urls");
            int length = bannerJA.length();
            for (int i = 0; i < length; i++) {
                String url = bannerJA.getString(i);
                bannerList.add(url);
            }
            if (mGetBannerResponce != null) {
                mGetBannerResponce.success(bannerList);
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
     * @ClassName: GetBannerResponce
     * @Description: 请求获取营销广告banner图片url
     * @date 2015年7月26日 下午4:24:12
     */
    public interface IGetBannerResponse extends IBaseResponse {
        /**
         * 请求成功返回banner图片url
         */
        public void success(List<String> bannerList);
    }


}

