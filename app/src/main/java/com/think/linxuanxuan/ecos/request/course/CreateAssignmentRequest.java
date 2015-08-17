package com.think.linxuanxuan.ecos.request.course;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request.Method;
import com.think.linxuanxuan.ecos.constants.RequestUrlConstants;
import com.think.linxuanxuan.ecos.model.Course.Assignment;
import com.think.linxuanxuan.ecos.request.BaseRequest;
import com.think.linxuanxuan.ecos.request.IBaseResponse;
import com.think.linxuanxuan.ecos.request.MyStringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/***
 *
 * @ClassName: CreateAssignmentRequest
 * @Description: 创建教程作业
 * @author enlizhang
 * @date 2015年7月26日 上午11:06:05
 *
 */
public class CreateAssignmentRequest extends BaseRequest{

	//请求参数键
	/** 教程id */
	public static final String COURSE_ID = "courseId";

	/** 用户id */
	public static final String USER_ID = "userId";

	/** 图片url */
	public static final String IMAGE_URL = "imgUrl";

	/** 描述 */
	public static final String DESCRIPTION = "description";



	//响应参数键
	/*userId：作者id
	assignment_id:作业id
	course_id:教程id
	img：图片URL
	description :描述*/



	/** 作业作者id */
	public static final String KEY_AS_USER_ID= "userid";

	/** 作业id */
	public static final String KEY_AS_ID= "assignmentId";

	/** 教程id */
	public static final String KEY_COURSE_ID= "courseId";

	/** 作业作者昵称 */
	public static final String KEY_AS_NICKNAME = "nickname";

	/** 作业内容 */
	public static final String KEY_AS_CONTENT = "description";

	/** 作业图片url */
	public static final String KEY_AS_IMAGE = IMAGE_URL;

	/** 作业发布时间时间戳 */
	public static final String KEY_AS_ISSUE_TIMES = "issueTimeStamp";


	Assignment mAssignment;

	ICreateAssignmentResponce mCreateAssignmentResponce;

	public void request(ICreateAssignmentResponce createAssignmentResponce, final Assignment assignment)
	{
		super.initBaseRequest(createAssignmentResponce);
		mAssignment = assignment;
		mCreateAssignmentResponce = createAssignmentResponce;

		//		if(mCreateAssignmentResponce!=null)
		//		{
		//			mCreateAssignmentResponce.success(mAssignment);
		//		}

		MyStringRequest stringRequest = new MyStringRequest(Method.POST, RequestUrlConstants.CREATE_ASSIGNMENT_URL,  this, this) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> map = getRequestBasicMap();

				map.put(COURSE_ID, assignment.courseId);
				map.put(IMAGE_URL, assignment.imageUrl);
				map.put(DESCRIPTION, assignment.content);


				traceNormal(TAG, map.toString());
				traceNormal(TAG, CreateAssignmentRequest.this.getUrl(RequestUrlConstants.CREATE_ASSIGNMENT_URL, map));
				return map;
			}

		};

		stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000,0,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

		getQueue().add(stringRequest);
	}

	@Override
	public void responceSuccess(String jstring) {
		traceNormal(TAG, jstring);

		try {
			JSONObject json = new JSONObject(jstring).getJSONObject(KEY_DATA);

			JSONObject asJO = json;

//			mAssignment.issueTimeStamp = asJO.getLong(KEY_AS_ISSUE_TIMES);


			if(mCreateAssignmentResponce!=null)
			{
				mCreateAssignmentResponce.success(mAssignment);
			}
			else
			{
				traceError(TAG,"回调接口为null");
			}


		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			if(mBaseResponse!=null)
			{
				mBaseResponse.doAfterFailedResponse("json异常");
			}
		}

	}

	/***
	 *
	 * @ClassName: CreateAssignmentResponce
	 * @Description: 创建教程作业回调函数
	 *
	 */
	public interface ICreateAssignmentResponce extends IBaseResponse{

		/** 创建教程作业成功，并返回添加的教程作业 */
		public void success(Assignment assignment);
	}


	public String getRequestAssignmentJson(Assignment assignment){
		Map<Object,Object> jsonMap = new HashMap<Object,Object>();

		jsonMap.put("courseId", assignment.courseId);
		jsonMap.put("imgUrl", assignment.imageUrl);
		jsonMap.put("description", assignment.content);


		return new JSONObject(jsonMap).toString();
	}
}

