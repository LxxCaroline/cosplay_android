package com.think.linxuanxuan.ecos.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author enlizhang
 * @ClassName: Course
 * @Description: 教程
 * @date 2015年7月25日 下午11:31:19
 */
public class Course {

    /**
     * 教程id
     */
    public String courseId;

    /**
     * 发布者id
     */
    public String userId;

    /**
     * 类别
     */
    public CourseType courseType;

    /**
     * 标题
     */
    public String title;

    /**
     * 发布者
     */
    public String author;

    /**
     * 发布者头像url
     */
    public String authorAvatarUrl;

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
     * 教程步骤{@link Step}列表
     */
    public List<Step> stepList = new ArrayList();

    /**
     * 点赞数
     */
    public int praiseNum;

    /**
     * 作业{@link Assignment}列表
     */
    public List<Assignment> assignmentList = new ArrayList();

    /**
     * 教程下的作品{@link Assignment}个数
     */
    public int assignmentNum;

    /**
     * 评论数
     */
    public int commentNum;

    /**
     * 是否已点赞，true:是 false:否
     */
    public boolean hasPraised;


    public void addStep(Step step) {
        stepList.add(step);
    }

    public void addStep(Assignment assignment) {
        assignmentList.add(assignment);
    }


    /**
     * 返回教程类别名称列表
     *
     * @return
     */
    public static List<String> getCouserTypeNameList() {
        List<String> list = new ArrayList<String>();
        for (CourseType ct : CourseType.values()) {
            list.add(ct.name());
        }

        return list;
    }

    /**
     * 根据{@link #issueTimeStamp}获取发布时间描述
     *
     * @return
     */
    public String getDateDescription() {

        return ModelUtils.getDateDesByTimeStamp(issueTimeStamp);
    }


    /**
     * @author enlizhang
     * @ClassName: Assignment
     * @Description: 教程课后作业
     * @date 2015年7月25日 下午11:28:29
     */
    public static class Assignment {

        /**
         * 教程id
         */
        public String courseId;

        /**
         * 用户id
         */
        public String userId;

        /**
         * 作业id
         */
        public String assignmentId;

        /**
         * 作者头像url
         */
        public String authorAvatarUrl;

        /**
         * 发布者
         */
        public String author;

        /**
         * 发布时间,是一个时间戳
         */
        public long issueTimeStamp;

        /**
         * 图片url
         */
        public String imageUrl;

        /**
         * 内容
         */
        public String content;

        /**
         * 评论数
         */
        public int commentNum;

        /**
         * 点赞数
         */
        public int praiseNum;

        /**
         * 是否点赞
         */
        public boolean hasPraised;

        /**
         * 根据{@link #issueTimeStamp}获取发布时间描述
         *
         * @return
         */
        public String getDateDescription() {
            return ModelUtils.getDateDesByTimeStamp(issueTimeStamp);
        }

        @Override
        public String toString() {
            return "Assignment [courseId=" + courseId + ", userId=" + userId
                    + ", assignmentId=" + assignmentId + ", authorAvatarUrl="
                    + authorAvatarUrl + ", author=" + author + ", issueTimeStamp="
                    + issueTimeStamp + ", imageUrl=" + imageUrl + ", content="
                    + content + ", praiseNum=" + praiseNum + ",hasPraised=" + hasPraised + "]";
        }

    }


    /**
     * 教程步骤
     */
    public static class Step implements Parcelable {

        public Step(int index) {
            stepIndex = index;
        }

        /**
         * 步骤序号
         */
        public int stepIndex;

        /**
         * 步骤对应的图片存储路径
         */
        public String imagePath;

        /**
         * 步骤对应的图片url
         */
        public String imageUrl;

        /**
         * 步骤描述
         */
        public String description;


        @Override
        public void writeToParcel(Parcel out, int flags) {
            out.writeInt(stepIndex);
            out.writeString(imagePath);
            out.writeString(imageUrl);
            out.writeString(description);

        }

        public static final Parcelable.Creator<Step> CREATOR = new Creator<Step>() {
            @Override
            public Step[] newArray(int size) {
                return new Step[size];
            }

            @Override
            public Step createFromParcel(Parcel in) {
                return new Step(in);
            }
        };

        public Step(Parcel in) {
            stepIndex = in.readInt();
            imagePath = in.readString();
            imageUrl = in.readString();
            description = in.readString();

        }

        @Override
        public int describeContents() {
            // TODO Auto-generated method stub
            return 0;
        }

        /**
         * {@link #stepIndex}、{@link #imagePath}、{@link #description}是否有为空的(null或空串)
         *
         * @return true:除{@link #imageUrl}外数据都不为空 false:除{@link #photoUrl}外数据有数据为空
         */
        public boolean isSomeEmpty() {

            boolean result = (imagePath == null || "".equals(imagePath)
                    || description == null || "".equals(description));

            return result;
        }

        @Override
        public String toString() {
            return "Step [stepIndex=" + stepIndex + ", imagePath=" + imagePath
                    + ", imageUrl=" + imageUrl + ", description=" + description
                    + "]";
        }

    }


    /**
     * @author enlizhang
     * @ClassName: CourseType
     * @Description: 教程类别，包括妆娘、摄影、后期、服装、道具、假发、新的、其他
     * @date 2015年7月25日 下午11:37:26
     */
    public static enum CourseType {

        化妆("1"),
        摄影("2"),
        后期("3"),
        服装("4"),
        道具("5"),
        假发("6"),
        心得("7"),
        其他("8");

        private String value;

        private CourseType(String _value) {
            this.value = _value;
        }

        public String getBelongs() {
            return value;
        }

        public static CourseType getCourseType(String value) {
            for (CourseType courseType : CourseType.values()) {
                if (courseType.getBelongs().equals(value))
                    return courseType;
            }

            return null;
        }

    }

    /**
     * 返回变量的json串
     * <p>
     * type:类别
     * title:标题
     * cover_url:封面图URL
     * user_id:发布者id
     * img_urls:图片列表(JSON Arrayt)
     * descriptions:内容列表(JSON Array)
     *
     * @return
     */
    public String getRequestJson() {
        Map<String, String> jsonMap = new HashMap<String, String>();

        return new JSONObject(jsonMap).toString();
    }

    @Override
    public String toString() {
        return "Course [courseId=" + courseId + ", userId=" + userId
                + ", courseType=" + courseType + ", title=" + title
                + ", author=" + author + ", authorAvatarUrl=" + authorAvatarUrl
                + ", issueTimeStamp=" + issueTimeStamp + ", coverLocalPath="
                + coverLocalPath + ", coverUrl=" + coverUrl + ", stepList="
                + stepList + ", praiseNum=" + praiseNum + ", assignmentList="
                + assignmentList + ", assignmentNum=" + assignmentNum
                + ", commentNum=" + commentNum + "]";
    }


}
