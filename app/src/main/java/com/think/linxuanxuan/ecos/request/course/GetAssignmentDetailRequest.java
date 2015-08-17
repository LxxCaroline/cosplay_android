package com.think.linxuanxuan.ecos.request.course;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author enlizhang
 * @ClassName: GetAssignmentDetailRequest
 * @Description: 获取教程作业详情
 * @date 2015年7月26日 上午11:04:21
 */
public class GetAssignmentDetailRequest extends BaseRequest {

    //请求参数键
    /**
     * 教程作业id
     */
    public static final String ASSIGNMENT_ID = "assignmentId";

    //响应参数键
    IAssignmentDetailRespnce mAssignmentDetailRespnce;

	/*userId：作者id
    assignment_id:作业id
	course_id:教程id
	img：图片URL
	description :描述
	usreAvatarUrl：作者头像url
	praiseNum:点赞数
	commentNum:评论总数*/


    /**
     * 作业id
     */
    public static final String KEY_AS_ID = "assignmentId";

    /**
     * 作业作者id
     */
    public static final String KEY_AS_USER_ID = "userid";

    /**
     * 作业作者头像url
     */
    public static final String KEY_AS_AVATAR_URL = "userAvatarUrl";

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
    public static final String KEY_AS_IMAGE = "imgUrl";

    /**
     * 点赞数
     */
    public static final String KEY_AS_PRAISE_NUM = "praiseNum";

    /**
     * 评论总数
     */
    public static final String KEY_AS_COMMENT_NUM = "commentNum";

    /**
     * 作业发布时间时间戳
     */
    public static final String KEY_AS_ISSUE_TIMES = "publishTime";

    /**
     * 评论列表JSONArray,内含JSONObject
     */
    public static final String JA_COMMENTS = "comments";

    /**
     * 评论id
     */
    public static final String KEY_COMMENT_ID = "commentId";

    /**
     * 评论者头像url
     */
    public static final String KEY_COMMENT_AVATAR_URL = "avatarUrl";

    /**
     * 评论内容
     */
    public static final String KEY_COMMENT_CONTENT = "content";

    /**
     * 评论类型{@link CommentType}、包括{@link Course}、作业{@link Assignment}、分享{@link Share}、招募{@link Recruitment}
     */
    public static final String KEY_COMMENT_TYPE = "commentType";

    /**
     * 某种评论类型{@link CommentType}的id，不能为空
     */
    public static final String KEY_COMMENT_TYPE_ID = "commentTypeId";


    /**
     * 评论者id
     */
    public static final String KEY_COMMENT_FROM_ID = "fromId";

    /**
     * 评论者名称
     */
    public static final String KEY_COMMENT_USER_NICKNAME = "fromNickname";

    /**
     * 父评论id
     */
    public static final String KEY_COMMENT_PARENT_ID = "parentId";

    /**
     * 父评论用户昵称
     */
    public static final String KEY_COMMENT_PARENT_NICKNAME = "parentNickName";

    /**
     * 评论时间时间戳
     */
    public static final String KEY_COMMENT_TIME_STAMP = "commentTimeStamp";

	
	   /*avatarUrl：评论者头像
	   content ：评论内容
	   commentType：评论类型
	   commentTypeId：评论类型id
	   commentId：评论id
	   
	   fromId：评论者id
	   fromNickName： 评论者名称
	   parentId ：父评论id
	   commitTimeStam：评论时间时间戳*/


    String mAssignmentId;

    public void request(IAssignmentDetailRespnce assignmentDetailRespnce, final String assignmentId) {
        super.initBaseRequest(assignmentDetailRespnce);
        mAssignmentDetailRespnce = assignmentDetailRespnce;

        mAssignmentId = assignmentId;
		
		/*if(mAssignmentDetailRespnce!=null)
		{
			mAssignmentDetailRespnce.success(getTestAssignmentDetail(), getTestCommentList());
		}*/

        MyStringRequest stringRequest = new MyStringRequest(Method.POST, RequestUrlConstants.GET_ASSIGNMENT_DETAIL_URL, this, this) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = getRequestBasicMap();

                map.put(ASSIGNMENT_ID, assignmentId);

                traceNormal(TAG, map.toString());
                traceNormal(TAG, GetAssignmentDetailRequest.this.getUrl(RequestUrlConstants.GET_ASSIGNMENT_DETAIL_URL, map));
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

            JSONObject asJO = json;

            Assignment assignment = new Assignment();

            assignment.imageUrl = getString(asJO, KEY_AS_IMAGE);
            assignment.assignmentId = mAssignmentId;
            assignment.author = getString(asJO, KEY_AS_NICKNAME);

            assignment.authorAvatarUrl = getString(asJO, KEY_AS_AVATAR_URL);
            assignment.content = getString(asJO, KEY_AS_CONTENT);

            String praiseNum = getString(asJO, KEY_AS_PRAISE_NUM);
            assignment.praiseNum = "".equals(praiseNum) ? 0 : Integer.valueOf(praiseNum);

            String commentNum = getString(asJO, KEY_AS_COMMENT_NUM);
            assignment.commentNum = "".equals(commentNum) ? 0 : Integer.valueOf(commentNum);

            assignment.hasPraised = asJO.getBoolean("hasPraised");
            assignment.issueTimeStamp = Long.valueOf(asJO.getString(KEY_AS_ISSUE_TIMES)).longValue();
            assignment.author = getString(asJO, "nickName");

            //设置评论数据
            List<Comment> commentList = new ArrayList<Comment>();

            if (json.has(JA_COMMENTS)) {
                JSONArray commentJA = json.getJSONArray(JA_COMMENTS);

                int commentsLength = commentJA.length();
                Comment comment;
                for (int i = 0; i < commentsLength; i++) {

                    JSONObject commentJO = commentJA.getJSONObject(i);

                    comment = new Comment();
                    comment.commentId = getString(commentJO, KEY_COMMENT_ID);
                    comment.avatarUrl = getString(commentJO, KEY_COMMENT_AVATAR_URL);
                    comment.content = getString(commentJO, KEY_COMMENT_CONTENT);
                    comment.commentType = CommentType.getCommentTypeByValue(getString(commentJO, KEY_COMMENT_TYPE));
                    comment.commentTypeId = getString(commentJO, KEY_COMMENT_TYPE_ID);
                    comment.fromId = getString(commentJO, KEY_COMMENT_FROM_ID);
                    comment.fromNickName = getString(commentJO, KEY_COMMENT_USER_NICKNAME);
                    comment.targetId = getString(commentJO, KEY_COMMENT_PARENT_ID);
                    comment.targetNickname = getString(commentJO, KEY_COMMENT_PARENT_NICKNAME);

                    comment.commitTimeStamp = Long.valueOf(commentJO.getString(KEY_COMMENT_TIME_STAMP)).longValue();
                    commentList.add(comment);
                }

            }

            if (mAssignmentDetailRespnce != null) {
                mAssignmentDetailRespnce.success(assignment, commentList);
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
     * @ClassName: AssignmentDetailRespnce
     * @Description: 获取教程详情回调
     * @date 2015年7月26日 下午6:30:48
     */
    public interface IAssignmentDetailRespnce extends IBaseResponse {

        /**
         * 获取教程详情成功回调函数，并返回教程详情和简单评论列表
         *
         * @param assignment  教程详情
         * @param commentList 简单评论列表
         */
        public void success(Assignment assignment, List<Comment> commentList);

    }

}

