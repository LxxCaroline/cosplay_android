package com.think.linxuanxuan.ecos.model;

import com.think.linxuanxuan.ecos.model.Course.Assignment;

import java.util.Date;

/**
 * 评论，包括教程、作业额、分享、招募
 *
 * @author enlizhang
 */
public class Comment {

    /**
     * 评论id
     */
    public String commentId;

    /**
     * 评论头像
     */
    public String avatarUrl;

    /**
     * 评论类型{@link CommentType}
     */
    public CommentType commentType;

    /**
     * 某种评论类型{@link CommentType}的id，不能为空
     */
    public String commentTypeId;

    /**
     * 评论者用户id
     */
    public String fromId;

    /**
     * 评论者用户名称
     */
    public String fromNickName;

    /**
     * 回复对象的用户id
     */
    public String targetId;


    /**
     * 回复对象的用户昵称
     */
    public String targetNickname;

    /**
     * 内容
     */
    public String content;

    /**
     * 评论时间,是一个时间戳,可以通过{@
     */
    public long commitTimeStamp;


    /**
     * 根据{@link #issueTimeStamp}获取发布时间描述
     *
     * @return
     */
    public String getDateDescription() {

        Date currentDay = new Date(System.currentTimeMillis());
        Date commentDay = new Date(commitTimeStamp);

        //当天评论
        if (currentDay.getDate() == commentDay.getDate() &&
                (currentDay.getTime() - commentDay.getTime()) < 24 * 60 * 60 * 1000) {
            if(commentDay.getMinutes()>=10)
               return commentDay.getHours() + ":" + commentDay.getMinutes();
            else
                return commentDay.getHours() + ":0" + commentDay.getMinutes();
        }
        //7天内评论
        if ((currentDay.getTime() - commentDay.getTime()) < 7 * 24 * 60 * 60 * 1000) {
            if (currentDay.getDate() > commentDay.getDate())
                return "" + (currentDay.getDate() - commentDay.getDate()) + "天前";
            else
                return "" + Math.ceil((currentDay.getTime() - commentDay.getTime()) / (24 * 60 * 60 * 1000)) + "天前";
        }

        //很前评论
        return "" + commentDay.getYear() + "-" + commentDay.getMonth() + "-" + commentDay.getDate();

    }

    /**
     * @author enlizhang
     * @ClassName: CommentType
     * @Description: 评论归属，包括教程{@link Course}、作业{@link Assignment}、分享{@link Share}、
     * 招募{@link Recruitment}
     * @date 2015年7月25日 下午11:28:50
     */
    public static enum CommentType {

        教程("0"),
        分享("1"),
        活动("2"),
        招募("3"),
        作业("4");

        private String value;

        CommentType(String _value) {
            this.value = _value;
        }

        public String getBelongs() {
            return value;
        }

        public static CommentType getCommentTypeByValue(String value) {

            for (CommentType commentType : CommentType.values()) {
                if (commentType.getBelongs().equals(value))
                    return commentType;
            }

            return null;
        }

    }
}
