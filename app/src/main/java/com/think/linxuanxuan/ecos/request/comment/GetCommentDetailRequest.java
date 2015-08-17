package com.think.linxuanxuan.ecos.request.comment;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request.Method;
import com.think.linxuanxuan.ecos.constants.RequestUrlConstants;
import com.think.linxuanxuan.ecos.model.Comment;
import com.think.linxuanxuan.ecos.model.Comment.CommentType;
import com.think.linxuanxuan.ecos.model.Course;
import com.think.linxuanxuan.ecos.model.Course.Assignment;
import com.think.linxuanxuan.ecos.model.Recruitment;
import com.think.linxuanxuan.ecos.model.Share;
import com.think.linxuanxuan.ecos.request.BaseRequest;
import com.think.linxuanxuan.ecos.request.IBaseResponse;
import com.think.linxuanxuan.ecos.request.MyStringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * @author enlizhang
 * @ClassName: GetCommentDetailRequest
 * @Description: 获取评论详情
 * @date 2015年7月26日 下午12:33:34
 */
public class GetCommentDetailRequest extends BaseRequest {

    //请求参数键
    /**
     * 评论类型{@link CommentType}、包括{@link Course}、作业{@link Assignment}、分享{@link Share}、招募{@link Recruitment}
     */
    public static final String COMMENT_TYPE = "comment_type";

    /**
     * 某种评论类型{@link CommentType}的id，不能为空
     */
    public static final String COMMENT_TYPE_ID = "comment_type_id";

    //响应参数键
    GetCommentDetailResponse mGetCommentDetailResponse;


    public void request(GetCommentDetailResponse getCommentDetailResponse, final String mobile, final String password) {
        super.initBaseRequest(getCommentDetailResponse);
        mGetCommentDetailResponse = getCommentDetailResponse;


        MyStringRequest stringRequest = new MyStringRequest(Method.POST, RequestUrlConstants.GET_COMMENT_DETAIL_URL, this, this) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = getRequestBasicMap();


                traceNormal(TAG, map.toString());
                traceNormal(TAG, GetCommentDetailRequest.this.getUrl(RequestUrlConstants.GET_COMMENT_DETAIL_URL, map));
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


            if (mGetCommentDetailResponse != null) {
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
     * @ClassName: GetCommentDetailResponse
     * @Description: 获取请求详情回调接口
     * @date 2015年7月26日 下午8:06:46
     */
    interface GetCommentDetailResponse extends IBaseResponse {

        /**
         * 请求成功回掉函数，并返回请求详情
         */
        public void success(Comment comment);

    }
}

