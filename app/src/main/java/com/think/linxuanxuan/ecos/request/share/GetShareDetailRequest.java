package com.think.linxuanxuan.ecos.request.share;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request.Method;
import com.think.linxuanxuan.ecos.constants.RequestUrlConstants;
import com.think.linxuanxuan.ecos.model.Comment;
import com.think.linxuanxuan.ecos.model.Comment.CommentType;
import com.think.linxuanxuan.ecos.model.Course;
import com.think.linxuanxuan.ecos.model.Course.Assignment;
import com.think.linxuanxuan.ecos.model.Image;
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
 * @ClassName: GetShareDetailRequest
 * @Description: 获取分享详情
 * @date 2015年7月26日 下午12:40:24
 */
public class GetShareDetailRequest extends BaseRequest {

    //请求参数键
    /**
     * 要删除的分享id
     */
    public static final String SHARE_ID = "shareId";


    //响应列表
    /**
     * 分享id
     */
    public static final String KEY_SHARE_ID = "shareId";

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
    public static final String KEY_NICKNAME = "nickname";

    /**
     * 作者id
     */
    public static final String KEY_USER_ID = "authorId";

    /**
     * 是否已评论，true:是 false:否
     */
    public static final String KEY_HAS_FOLLOWED = "hasFollowed";

    /**
     * 是否已点赞，true:是 false:否
     */
    public static final String KEY_HAS_Praised = "hasPraised";

    /**
     * 标题
     */
    public static final String KEY_TITLE = "title";

    /**
     * 描述
     */
    public static final String KEY_DESCRIPTION = "description";

    /**
     * 分享发布时间戳
     */
    public static final String KEY_ISSUE_TIME = "issueTimeStamp";

    /**
     * 点赞数
     */
    public static final String KEY_PRAISE_NUM = "praiseNum";

    /**
     * 评论数
     */
    public static final String KEY_COMMENT_NUM = "commentNum";

    /**
     * 分享图片JA
     */
    public static final String KEY_IMAGE_URLS = "imgUrls";

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
     * 评论时间时间戳
     */
    public static final String KEY_COMMENT_TIME_STAMP = "commentTimeStamp";

    /**
     * 父评论用户昵称
     */
    public static final String KEY_COMMENT_PARENT_NICKNAME = "parentNickName";


    IGetShareResponse mGetShareResponse;


    public void request(IGetShareResponse getShareResponse, final String shareId) {
        super.initBaseRequest(getShareResponse);
        mGetShareResponse = getShareResponse;

        //		mGetShareResponse.success(getTestShare());

        MyStringRequest stringRequest = new MyStringRequest(Method.POST, RequestUrlConstants.GET_SHARE_DETAIL_URL, this, this) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = getRequestBasicMap();
                map.put(SHARE_ID, shareId);
                traceNormal(TAG, map.toString());
                traceNormal(TAG, GetShareDetailRequest.this.getUrl(RequestUrlConstants.GET_SHARE_DETAIL_URL, map));
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

            share.shareId = getString(shareJO, KEY_SHARE_ID);
            share.coverUrl = getString(shareJO, KEY_COVER_URL);
            share.avatarUrl = getString(shareJO, KEY_AVATAR_URL);
            share.nickname = getString(shareJO, KEY_NICKNAME);
            share.hasAttention = Boolean.valueOf(getString(shareJO, KEY_HAS_FOLLOWED));
            share.hasPraised = Boolean.valueOf(getString(shareJO, KEY_HAS_Praised));
            share.userId = getString(shareJO, KEY_USER_ID);

            share.title = getString(shareJO, KEY_TITLE);
            share.content = getString(shareJO, KEY_DESCRIPTION);

            share.issueTimeStamp = Long.valueOf(shareJO.getString(KEY_ISSUE_TIME)).longValue();

            String praiseNum = getString(shareJO, KEY_PRAISE_NUM);
            share.praiseNum = "".equals(praiseNum) ? 0 : Integer.valueOf(praiseNum);

            String commentNum = getString(shareJO, KEY_COMMENT_NUM);
            share.commentNum = "".equals(commentNum) ? 0 : Integer.valueOf(commentNum);

            if (shareJO.has(KEY_IMAGE_URLS) && !shareJO.isNull(KEY_IMAGE_URLS) && !getString(shareJO, KEY_IMAGE_URLS).equals("")) {
                JSONArray imageUrlJA = new JSONArray(getString(shareJO, KEY_IMAGE_URLS));

                for (int i = 0; i < imageUrlJA.length(); i++) {
                    Image image = new Image();
                    image.originUrl = imageUrlJA.getString(i);
                    share.imageList.add(image);
                }
            }

            //设置评论数据
            List<Comment> commentList = new ArrayList<Comment>();

            if (shareJO.has(JA_COMMENTS)) {
                JSONArray commentJA = shareJO.getJSONArray(JA_COMMENTS);

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


            share.commentList = commentList;
            if (mGetShareResponse != null) {
                mGetShareResponse.success(share);
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
     * @ClassName: GetShareResponse
     * @Description: 获取分享详情成功后回调接口
     * @date 2015年7月26日 下午7:23:09
     */
    public interface IGetShareResponse extends IBaseResponse {

        /**
         * 获取分享详情成功后回调，并返回分享详情
         */
        public void success(Share share);

    }


}

