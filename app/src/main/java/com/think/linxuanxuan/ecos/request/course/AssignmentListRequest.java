package com.think.linxuanxuan.ecos.request.course;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request.Method;
import com.think.linxuanxuan.ecos.constants.RequestUrlConstants;
import com.think.linxuanxuan.ecos.model.Course.Assignment;
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
 * @ClassName: AssignmentListRequest
 * @Description: 获取教程作业列表
 * @date 2015年7月26日 上午11:00:45
 */
public class AssignmentListRequest extends BaseRequest {

    //请求参数键
    /**
     * 教程id
     */
    public static final String COURSE_ID = "courseId";


    //响应参数键
    IAssignmentListResponse mAssignmentListRespnce;


    public void request(IAssignmentListResponse assignmentListRespnce, final String courseId, final int pages) {
        super.initBaseRequest(assignmentListRespnce);
        mAssignmentListRespnce = assignmentListRespnce;

        //		responceSuccess("");
        MyStringRequest stringRequest = new MyStringRequest(Method.POST, RequestUrlConstants.GET_ASSIGNMENT_LIST_URL, this, this) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = getRequestBasicMap();

                map.put(COURSE_ID, courseId);

                map.put(KEY_PAGE_INDEX, String.valueOf(pages));
                map.put(KEY_PAGE_SIZE, String.valueOf(DEFAULT_PAGE_SIZE));

                traceNormal(TAG, map.toString());
                traceNormal(TAG, AssignmentListRequest.this.getUrl(RequestUrlConstants.GET_ASSIGNMENT_LIST_URL, map));
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

            JSONArray assignmentJA = json.getJSONArray("assignments");
            int length = assignmentJA.length();

            List<Assignment> assignmentList = new ArrayList<Assignment>();
            for (int i = 0; i < length; i++) {
                JSONObject assignmentJO = assignmentJA.getJSONObject(i);

                Assignment assignment = new Assignment();
                assignment.assignmentId = getString(assignmentJO, "assignmentId");
                assignment.userId = getString(assignmentJO, "userId");
                assignment.author = getString(assignmentJO, "nickname");
                assignment.authorAvatarUrl = getString(assignmentJO, "authorAvatarUrl");

                assignment.imageUrl = getString(assignmentJO, "imgUrl");
                assignment.content = getString(assignmentJO, "description");
                assignment.issueTimeStamp = Long.valueOf(getString(assignmentJO, "issueTimeStamp")).longValue();
                assignment.hasPraised = assignmentJO.getBoolean("hasPraised");
                assignmentList.add(assignment);

            }

            if (mAssignmentListRespnce != null) {
                mAssignmentListRespnce.success(assignmentList);
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
     * @ClassName: AssignmentListRespnce
     * @Description: 获取教程作业列表
     * @date 2015年7月26日 下午5:23:17
     */
    public interface IAssignmentListResponse extends IBaseResponse {

        /**
         * 请求成功，返回教程列表
         */
        public void success(List<Assignment> assignmentList);

    }

}

