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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * @author enlizhang
 * @ClassName: CommentListRequest
 * @Description: 评论列表
 * @date 2015年7月26日 下午12:32:46
 */
public class CommentListRequest extends BaseRequest {

    //请求参数键
    /**
     * 评论类型{@link CommentType}、包括{@link Course}、作业{@link Assignment}、分享{@link Share}、招募{@link Recruitment}
     */
    public static final String COMMENT_TYPE = "commentType";

    /**
     * 某种评论类型{@link CommentType}的id，不能为空
     */
    public static final String COMMENT_TYPE_ID = "commentTypeId";

    //请求响应列表
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
    public static final String KEY_COMMENT_TYPE = COMMENT_TYPE;

    /**
     * 某种评论类型{@link CommentType}的id，不能为空
     */
    public static final String KEY_COMMENT_TYPE_ID = COMMENT_TYPE_ID;


    /**
     * 评论者id
     */
    public static final String KEY_COMMENT_FROM_ID = "fromId";

    /**
     * 评论者名称
     */
    public static final String KEY_COMMENT_USER_NICKNAME = "fromNickname";

    /**
     * 父评论用户id
     */
    public static final String KEY_COMMENT_PARENT_ID = "parentId";

    /**
     * 父评论用户昵称
     */
    public static final String KEY_COMMENT_PARENT_NICKNAME = "parentNickname";

    /**
     * 评论时间时间戳
     */
    public static final String KEY_COMMENT_TIME_STAMP = "commentTimeStamp";

    //响应参数键
    ICommentListResponse mCommentListRespnce;


    Comment mComment;

    public void request(ICommentListResponse commentListRespnce, final Comment comment, final int pages) {
        super.initBaseRequest(commentListRespnce);
        mCommentListRespnce = commentListRespnce;
        mComment = comment;

        MyStringRequest stringRequest = new MyStringRequest(Method.POST, RequestUrlConstants.GET_COMMENT_LIST_URL, this, this) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = getRequestBasicMap();

                map.put(COMMENT_TYPE, comment.commentType.getBelongs());
                map.put(COMMENT_TYPE_ID, comment.commentTypeId);

                map.put(KEY_PAGE_INDEX, String.valueOf(pages));
                map.put(KEY_PAGE_SIZE, "1000");

                traceNormal(TAG, map.toString());
                traceNormal(TAG, CommentListRequest.this.getUrl(RequestUrlConstants.GET_COMMENT_LIST_URL, map));
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

            JSONObject commitJO = json;

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

            if (mCommentListRespnce != null) {
                mCommentListRespnce.success(commentList);
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
     * @ClassName: CommentListRespnce
     * @Description: 评论列表响应回调函数
     * @date 2015年7月26日 下午6:39:13
     */
    public interface ICommentListResponse extends IBaseResponse {

        /**
         * 请求成功，返回评论列表
         */
        public void success(List<Comment> commentList);

    }

}

