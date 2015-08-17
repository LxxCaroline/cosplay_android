package com.think.linxuanxuan.ecos.model;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author enlizhang
 * @ClassName: Share
 * @Description: 作品分享
 * @date 2015年7月25日 下午11:30:51
 */
public class Share {

    /**
     * 分享id
     */
    public String shareId;

    /**
     * 标题
     */
    public String title;

    /**
     * 用户id
     */
    public String userId;

    /**
     * 发布者昵称
     */
    public String nickname;

    /**
     * 用户头像url
     */
    public String avatarUrl;

    /**
     * 图片总数
     */
    public int totalPageNumber;

    /**
     * 点赞数
     */
    public int praiseNum;

    /**
     * 评论数
     */
    public int commentNum;

    /**
     * 是否已经关注,true:是 false:否
     */
    public boolean hasAttention;

    /**
     * 是否已经点赞,true:是 false:否
     */
    public boolean hasPraised;

    /**
     * 发布时间,是一个时间戳,可以通过{@link #getDateDescription()}获取日期描述
     */
    public Long issueTimeStamp;

    /**
     * 封面图本地路径
     */
    public String coverLocalPath;

    /**
     * 封面图url
     */
    public String coverUrl;

    /**
     * 图片列表
     */
    public List<Image> imageList = new ArrayList<Image>();

    /**
     * 评论列表
     */
    public List<Comment> commentList = new ArrayList<Comment>();

    /**
     * 内容
     */
    public String content;

    /**
     * 分享标签，如果属于某一标签则置为true
     */
    public Tag tags = new Tag();

    /**
     * 根据{@link #issueTimeStamp}获取发布时间描述
     *
     * @return
     */
    public String getDateDescription() {

        return ModelUtils.getDateDesByTimeStamp(issueTimeStamp);
    }

    /**
     * 获取添加分享请求所需的Json串
     *
     * @return
     */
    public String getRequestJson() {
        Map<String, String> jsonMap = new HashMap<String, String>();

        return new JSONObject(jsonMap).toString();
    }


    public static class Tag {

        /**
         * 是否是妆娘
         */
        public boolean isMakeup = false;

        /**
         * 是否是摄影
         */
        public boolean isPhoto = false;

        /**
         * 是否是后期
         */
        public boolean isLater = false;

        /**
         * 是否是服装
         */
        public boolean isCloth = false;

        /**
         * 是否是道具
         */
        public boolean isProperty = false;

        /**
         * 是否是coser
         */
        public boolean isCoser = false;

		/*0 不选
        1 妆娘
		2 摄影
		4 后期
		8 服装
		16  道具
		32  coser
		*/

        public int getTagValues() {
            int tagValues = 0;
            if (isMakeup)
                tagValues += 1;
            if (isPhoto)
                tagValues += 2;
            if (isLater)
                tagValues += 4;
            if (isCloth)
                tagValues += 8;
            if (isProperty)
                tagValues += 16;
            if (isCoser)
                tagValues += 32;
            return tagValues;
        }


        public static Tag getTagByRecruitType(Recruitment.RecruitType recruitType) {

            Tag tags = new Tag();

            if (recruitType == Recruitment.RecruitType.妆娘)
                tags.isMakeup = true;
            if (recruitType == Recruitment.RecruitType.摄影)
                tags.isPhoto = true;
            if (recruitType == Recruitment.RecruitType.后期)
                tags.isLater = true;
            if (recruitType == Recruitment.RecruitType.服装)
                tags.isCloth = true;
            if (recruitType == Recruitment.RecruitType.道具)
                tags.isProperty = true;
            if (recruitType == Recruitment.RecruitType.其他)
                tags.isCoser = true;

            return tags;

        }
    }

    @Override
    public String toString() {
        return "Share{" +
                "shareId='" + shareId + '\'' +
                ", title='" + title + '\'' +
                ", userId='" + userId + '\'' +
                ", nickname='" + nickname + '\'' +
                ", avatarUrl='" + avatarUrl + '\'' +
                ", totalPageNumber=" + totalPageNumber +
                ", praiseNum=" + praiseNum +
                ", commentNum=" + commentNum +
                ", hasAttention=" + hasAttention +
                ", hasPraised=" + hasPraised +
                ", issueTimeStamp=" + issueTimeStamp +
                ", coverLocalPath='" + coverLocalPath + '\'' +
                ", coverUrl='" + coverUrl + '\'' +
                ", imageList=" + imageList +
                ", commentList=" + commentList +
                ", content='" + content + '\'' +
                '}';
    }
}
