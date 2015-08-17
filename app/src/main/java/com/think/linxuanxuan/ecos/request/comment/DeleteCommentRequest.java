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


/***
 *
 * @ClassName: DeleteCommentRequest
 * @Description: 删除评论
 * @author enlizhang
 * @date 2015年7月26日 下午12:33:05
 *
 */
public class DeleteCommentRequest extends BaseRequest{

	//请求参数键
	/** 评论类型{@link CommentType}、包括{@link Course}、作业{@link Assignment}、分享{@link Share}、招募{@link Recruitment}*/
	public static final String COMMENT_TYPE = "comment_type";

	/** 评论id */
	public static final String COMMENT_ID = "comment_id";



	DeleteCommentlResponse mDeleteCommentlResponse;


	Comment mComment;

	public void request(DeleteCommentlResponse deleteCommentlResponse, final Comment comment)
	{
		super.initBaseRequest(deleteCommentlResponse);
		mDeleteCommentlResponse = deleteCommentlResponse;
		mComment = comment;

		MyStringRequest stringRequest = new MyStringRequest(Method.POST, RequestUrlConstants.DELETE_COMMENT_URL,  this, this) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> map = getRequestBasicMap();

				map.put(COMMENT_TYPE, comment.commentType.getBelongs());
				map.put(COMMENT_ID, comment.commentId);

				traceNormal(TAG, map.toString());
				traceNormal(TAG, DeleteCommentRequest.this.getUrl(RequestUrlConstants.DELETE_COMMENT_URL, map));
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
			JSONObject json = new JSONObject(jstring);


			if(mDeleteCommentlResponse!=null)
			{
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
	 * @ClassName: DeleteCommentlResponse
	 * @Description: 删除评论回调接口
	 * @author enlizhang
	 * @date 2015年7月26日 下午8:00:08
	 *
	 */
	interface DeleteCommentlResponse extends IBaseResponse{

		/** 请求成功回掉函数，返回删除的评论id和评论类型 */
		public void success(String commentId,CommentType commentType);

	}

}

