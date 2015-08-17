package com.think.linxuanxuan.ecos.request.share;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request.Method;
import com.think.linxuanxuan.ecos.constants.RequestUrlConstants;
import com.think.linxuanxuan.ecos.model.Image;
import com.think.linxuanxuan.ecos.model.Share;
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
 * @ClassName: CreateShareRequest
 * @Description: 创建分享
 * @date 2015年7月26日 下午12:41:05
 */
public class CreateShareRequest extends BaseRequest {

    //请求参数键
    /**
     * Course对象数据json串，空的属性用"null"
     */
    public static final String JSON_STRING = "shareJson";

    ICreateShareResponse mCreateShareResponse;

    public void request(ICreateShareResponse createShareResponse, final Share share) {
        super.initBaseRequest(createShareResponse);
        mCreateShareResponse = createShareResponse;

        MyStringRequest stringRequest = new MyStringRequest(Method.POST, RequestUrlConstants.CREATE_SHARE_URL, this, this) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = getRequestBasicMap();

                map.put(JSON_STRING, getRequestShareJSon(share));

                traceNormal(TAG, map.toString());
                traceNormal(TAG, CreateShareRequest.this.getUrl(RequestUrlConstants.CREATE_SHARE_URL, map));
                return map;
            }

        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        getQueue().add(stringRequest);

    }


    @Override
    public void responceSuccess(String jstring) {
        //		traceNormal(TAG, jstring);

        try {
            JSONObject json = new JSONObject(jstring).getJSONObject(KEY_DATA);
            JSONObject shareJO = json;

            Share share = new Share();
            share.shareId = getString(shareJO, "shareId");
            share.title = getString(shareJO, "title");
            share.coverUrl = getString(shareJO, "coverUrl");
            share.avatarUrl = getString(shareJO, "authorAvatarUrl");
            share.nickname = getString(shareJO, "nickname");
            share.content = getString(shareJO, "description");
            Log.e("发布时间戳", getString(shareJO, "issueTimeStamp"));

            String time = getString(shareJO, "issueTimeStamp");
            Log.e(TAG, time);

            String imgUrlJAString = shareJO.getString("imgUrl");
            JSONArray imageJA = new JSONArray(imgUrlJAString);
            int length = imageJA.length();
            for (int i = 0; i < length; i++) {
                Image image = new Image();
                image.originUrl = imageJA.getString(i);
            }

            if (mCreateShareResponse != null) {
                mCreateShareResponse.success(share);
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
     * @ClassName: CreateShareResponse
     * @Description: 创建教程响应回调函数
     * @date 2015年7月26日 下午7:31:21
     */
    public interface ICreateShareResponse extends IBaseResponse {

        /**
         * 创建成功后回调，并返回创建的分享
         */
        public void success(Share share);

    }

    /**
     * 获取Share的JSON串
     *
     * @param share
     * @return
     */
    public String getRequestShareJSon(Share share) {
        Map<Object, Object> jsonMap = new HashMap<Object, Object>();

        jsonMap.put("title", share.title);
        jsonMap.put("coverUrl", share.coverUrl);
        jsonMap.put("description", share.content);
        jsonMap.put("totalImages", share.totalPageNumber);
        jsonMap.put("type", share.tags.getTagValues());


        List<String> imageList = new ArrayList<String>();
        for (int i = 0; i < share.imageList.size(); i++) {
            Image image = share.imageList.get(i);
            if (image.originUrl != null && !"".equals(image.originUrl))
                imageList.add(image.originUrl);
        }
        jsonMap.put("imgUrls", new JSONArray(imageList));

        return new JSONObject(jsonMap).toString();
    }


}

