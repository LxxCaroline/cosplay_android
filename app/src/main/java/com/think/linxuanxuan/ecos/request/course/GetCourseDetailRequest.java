package com.think.linxuanxuan.ecos.request.course;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request.Method;
import com.think.linxuanxuan.ecos.constants.RequestUrlConstants;
import com.think.linxuanxuan.ecos.model.Course;
import com.think.linxuanxuan.ecos.model.Course.Assignment;
import com.think.linxuanxuan.ecos.model.Course.CourseType;
import com.think.linxuanxuan.ecos.model.Course.Step;
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
 * @ClassName: GetCourseDetailRequest
 * @Description: 获取教程详情
 * @date 2015年7月26日 上午11:03:17
 */
public class GetCourseDetailRequest extends BaseRequest {

    //请求参数键
    /**
     * 教程id
     */
    public static final String COURSE_ID = "courseId";

    //响应参数键
    ICourseDetailResponse mCourseDetailRespnce;

	/*{
        limg_urls:图片列表(JSON Arrayt)
		descriptions:内容列表(JSON Array)
		JSON ARRAY
		{
		     JSONObject
		    {
		       userid:作者id
		       nickname：作者名称
		       content：内容
		       imageUrl：图片url
		       authorAvatarUrl：作者头像url
		       issueTimeStamp：发布时间时间戳
		     }：作业
		}作业列表
		commentNum：评论总数
		}*/

    /**
     * 教程id
     */
    public static final String KEY_COURSE_ID = COURSE_ID;

    /**
     * 封面图url
     */
    public static final String KEY_COVER_URL = "coverUrl";

    /**
     * 作者头像url
     */
    public static final String KEY_AVATAR_URL = "authorAvatarUrl";

    /**
     * 作者昵称
     */
    public static final String KEY_NICKNAME = "author";

    /**
     * 作者id
     */
    public static final String KEY_USER_ID = "userId";

    /**
     * 教程类型
     */
    public static final String KEY_COURSE_TYPE = "type";

    /**
     * 标题
     */
    public static final String KEY_TITLE = "title";

    /**
     * 点赞数
     */
    public static final String KEY_PRAISE_NUM = "praiseNum";

    /**
     * 是否已点赞
     */
    public static final String KEY_HAS_PRAISED = "hasPraised";

    /**
     * 教程发布时间时间戳
     */
    public static final String KEY_ISSUE_TIME = "issueTimeStamp";

    /**
     * 步骤图片JA,其顺序与步骤序号一致
     */
    public static final String KEY_STEP_IMG_URLS = "imgUrls";

    /**
     * 步骤描述JA,其顺序与步骤序号一致
     */
    public static final String KEY_STEP_DESCRIPTIONS = "descriptions";


    /**
     * 作业列表JSONArray,内含JSONObject
     */
    public static final String JA_AS = "assigmentList";
    /*** 作业JSONObject */
    //	  public static final String JO_AS_JO = "assignments";

    /**
     * 作业id
     */
    public static final String KEY_AS_ID = "assigmentId";

    /**
     * 作业作者id
     */
    public static final String KEY_AS_USER_ID = "userid";

    /**
     * 作业作者头像url
     */
    public static final String KEY_AS_AVATAR_URL = "authorAvatarUrl";

    /**
     * 作业作者昵称
     */
    public static final String KEY_AS_NICKNAME = "nickname";

    /**
     * 作业内容
     */
    public static final String KEY_AS_CONTENT = "description";

    /**
     * 作业图片url
     */
    public static final String KEY_AS_IMAGE = "imageUrl";

    /**
     * 作业发布时间时间戳
     */
    public static final String KEY_AS_ISSUE_TIME = "issueTimeStamp";

    /**
     * 教程发布时间时间戳
     */
    public static final String KEY_COMMENT_MUN = "commentNum";


    public void request(ICourseDetailResponse courseDetailRespnce, final String courseId) {
        super.initBaseRequest(courseDetailRespnce);
        mCourseDetailRespnce = courseDetailRespnce;

		/*if(mCourseDetailRespnce!=null)
        {
			mCourseDetailRespnce.success(getTestCourse());
		}		*/
        MyStringRequest stringRequest = new MyStringRequest(Method.POST, RequestUrlConstants.GET_COURSE_DETAIL_URL, this, this) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = getRequestBasicMap();
                map.put(COURSE_ID, courseId);
                traceNormal(TAG, map.toString());
                traceNormal(TAG, GetCourseDetailRequest.this.getUrl(RequestUrlConstants.GET_COURSE_DETAIL_URL, map));
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

            //获取course JSONObject
            JSONObject courseJO = json;

            Course course = new Course();

            course.courseId = getString(courseJO, KEY_COURSE_ID);
            course.userId = getString(courseJO, KEY_USER_ID);

            course.coverUrl = getString(courseJO, KEY_COVER_URL);
            course.authorAvatarUrl = getString(courseJO, KEY_AVATAR_URL);
            course.author = getString(courseJO, KEY_NICKNAME);
            course.title = getString(courseJO, KEY_TITLE);
            course.courseId = getString(courseJO, KEY_COURSE_ID);
            course.userId = getString(courseJO, KEY_USER_ID);
            course.courseType = CourseType.getCourseType(getString(courseJO, KEY_COURSE_TYPE));

            String praiseNum = getString(courseJO, KEY_PRAISE_NUM);
            course.praiseNum = "".equals(praiseNum) ? 0 : Integer.valueOf(praiseNum);

            course.hasPraised = courseJO.getBoolean("hasPraised");

            List<String> stepImageList = new ArrayList<String>();

            //获取步骤图片列表JSONArray
            JSONArray stepImageUrls = new JSONArray(getString(courseJO, KEY_STEP_IMG_URLS));
            //获取步骤描述JSONArray
            JSONArray stepDescriptions = new JSONArray(getString(courseJO, KEY_STEP_DESCRIPTIONS));

            int stepLength = stepImageUrls.length();
            Step step;
            for (int i = 0; i < stepLength; i++) {
                step = new Step(i + 1);
                step.imageUrl = stepImageUrls.getString(i);
                step.description = stepDescriptions.getString(i);

                course.addStep(step);
            }

            if (courseJO.has(JA_AS) && !courseJO.isNull(JA_AS)) {
                JSONArray asJA = courseJO.getJSONArray(JA_AS);
                int assignmentNum = asJA.length();

                Assignment assignment;
                for (int i = 0; i < assignmentNum; i++) {
                    JSONObject asJO = asJA.getJSONObject(i);

                    assignment = new Assignment();

                    assignment.assignmentId = asJO.getString(KEY_AS_ID);
                    assignment.author = asJO.getString(KEY_AS_NICKNAME);
                    assignment.content = asJO.getString(KEY_AS_CONTENT);
                    assignment.imageUrl = getString(asJO, KEY_AS_IMAGE);
                    assignment.authorAvatarUrl = getString(asJO, KEY_AS_AVATAR_URL);
                    assignment.issueTimeStamp = Long.valueOf(asJO.getString(KEY_AS_ISSUE_TIME)).longValue();

                    course.addStep(assignment);
                }
            }

            String commentNum = getString(courseJO, KEY_COMMENT_MUN);
            course.commentNum = "".equals(commentNum) ? 0 : Integer.valueOf(commentNum);

            if (mCourseDetailRespnce != null) {
                mCourseDetailRespnce.success(course);
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
     * @ClassName: CourseDetailRespnce
     * @Description: 请求教程详情回调函数
     * @date 2015年7月26日 下午5:06:49
     */
    public interface ICourseDetailResponse extends IBaseResponse {

        /**
         * 请求成功，返回教程详情
         */
        public void success(Course course);

    }

}

