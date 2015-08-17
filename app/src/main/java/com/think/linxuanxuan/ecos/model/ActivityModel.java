package com.think.linxuanxuan.ecos.model;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author enlizhang
 * @ClassName: Activity
 * @Description: 活动详情
 * @date 2015年7月25日 下午11:28:08
 */
public class ActivityModel {

    /**
     * 活动id
     */
    public String activityId;

    /**
     * 标题
     */
    public String title;

    /**
     * 发布者
     */
    public String userId;

    /**
     * 发布者
     */
    public String avatarUrl;

    /**
     * 发布者
     */
    public String nickname;

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
     * 发布时间,是一个时间戳,可以通过{@link #getDateDescription()}获取日期描述
     */
    public long issueTimeStamp;

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
    public List<Image> imageList;

    /**
     * 活动介绍
     */
    public String introduction;

    /**
     * 费用
     */
    public String fee;

    /**
     * 活动日期
     */
    public ActivityTime activityTime;

    /**
     * 分类
     */
    public ActivityType activityType;

    /**
     * 想去的人数
     */
    public int loveNums;

    /**
     * 活动地址
     */
    public Location location;

    /**
     * 联系方式列表
     */
    public List<Contact> contactWayList;

    /**
     * 想去的人的用户列表
     */
    public List<com.think.linxuanxuan.ecos.model.User> signUpUseList;

    /**
     * 是否已经报名，true:是 false:否
     */
    public boolean hasSignuped;

    /**
     * 活动是否已经开始，true:是 false:否
     */
    public boolean hasStarted;

    /**
     * 活动是否已经结束，true:是 false:否
     */
    public boolean hasFinshed;

    public ActivityModel() {
        imageList = new ArrayList<Image>();
        activityType = ActivityType.同人展;
        location = new Location();
        contactWayList = new ArrayList<Contact>();
        signUpUseList = new ArrayList<com.think.linxuanxuan.ecos.model.User>();
        activityTime = new ActivityTime();
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
     * 返回招募类别名称列表
     *
     * @return
     */
    public static List<String> getActivityTypeNameList() {
        List<String> list = new ArrayList<String>();
        for (ActivityType at : ActivityType.values()) {
            list.add(at.name());
        }

        return list;
    }


    /**
     * @author enlizhang
     * @ClassName: Situation
     * @Description: 活动现场实况
     * @date 2015年7月25日 下午11:27:37
     */
    static class Situation {

        /**
         * 发布者用户{@link User}id
         */
        public String userId;

        /**
         * 发布时间,是一个时间戳
         */
        public Long issueTime;

        /**
         * 图片
         */
        List<Image> imageList;

        /**
         * 内容
         */
        public String content;
    }

    /**
     * @author enlizhang
     * @ClassName: Time
     * @Description: 活动时间
     * @date 2015年7月26日 上午8:45:01
     */
    public class ActivityTime {

        /**
         * 开始日期时间戳
         */
        public long startDateStamp;

        /**
         * 结束日期时间戳
         */
        public long endDateStamp;

        /**
         * 每日开始时间
         */
        public String dayStartTime;

        /**
         * 每日结束时间
         */
        public String dayEndTime;

        @Override
        public String toString() {
            return ModelUtils.getDateDesByTimeStamp(startDateStamp) + "-" + ModelUtils.getDateDesByTimeStamp(endDateStamp) + "\n" + dayStartTime.substring(0, 5) + "-" + dayEndTime.substring(0, 5);

        }

        public String getDate() {
            return ModelUtils.getDateDesByTimeStamp(startDateStamp) + "-" + ModelUtils.getDateDesByTimeStamp(endDateStamp);
        }
    }

    /**
     * @author enlizhang
     * @ClassName: ActivityType
     * @Description: 活动类别, 包括同人展、动漫节、官方活动、LIVE、舞台祭、赛事、主题ONLY、派对
     * @date 2015年7月25日 下午11:49:20
     */
    public static enum ActivityType {

        同人展("1"),
        动漫节("2"),
        官方活动("3"),
        LIVE("4"),
        舞台祭("5"),
        赛事("6"),
        主题ONLY("7"),
        派对("8"),
        个人("9");

        private String value;

        private ActivityType(String _value) {
            this.value = _value;
        }

        public String getValue() {
            return value;
        }

        public static ActivityType getActivityTypeByValue(String value) {

            for (ActivityType activityType : ActivityType.values()) {
                if (activityType.getValue().equals(value))
                    return activityType;
            }

            return null;
        }
    }

    /**
     * @author enlizhang
     * @ClassName: Location
     * @Description: 位置，包括省、市、区、详细地址
     * @date 2015年7月26日 上午8:50:25
     */
    public class Location {

        /**
         * 省份
         */
        public Province province;

        /**
         * 城市
         */
        public City city;

        /**
         * 详细地址
         */
        public String address;

        public Location() {
            province = new Province();
            city = new City();
        }

        @Override
        public String toString() {
            return province.provinceName + " " + city.cityName + " " + address;
        }
    }

    /**
     * 联系方式包装对象
     */
    public static class Contact {

        public ContactWay contactWay;

        public String value;

        public Contact() {
            contactWay = ContactWay.QQ;
            value = "";
        }
    }

    /**
     * @author enlizhang
     * @ClassName: ContactWays
     * @Description: 联系方式，包括QQ、QQ群、电话
     * @date 2015年7月26日 上午8:58:22
     */
    public static enum ContactWay {

        QQ("0"),
        QQ群("1"),
        微信("2"),
        电话("3");

        /**
         * 类型
         */
        private String type;


        private ContactWay(String _type) {
            this.type = _type;
        }

        public String getType() {
            return type;
        }

        public static ContactWay getContactWayByValue(String type) {

            for (ContactWay contactWay : ContactWay.values()) {
                if (contactWay.getType().equals(type))
                    return contactWay;
            }
            return null;
        }
    }


    /**
     * *
     * 活动数据对象JSON
     * {
     * title:标题
     * cover_url:封面图片
     * img_urls:图片列表（JSON Array）
     * description:活动介绍
     * time:{时间
     * start:开始时间(long)
     * keepup_times:持续时间
     * keepup_days:持续天数
     * <p/>
     * }
     * fee:费用
     * type：分类
     * address: {//活动地址
     * city_code:城市code
     * detail: 详细地址
     * }（省、市、区、详细地址）
     * contacts:{联系方式
     * qq:
     * qq_group:
     * weibo:
     * phone:
     * }
     * user_id:发布者
     * }
     *
     * @return
     */
    public String getRequestJson() {
        Map<String, String> jsonMap = new HashMap<String, String>();


        return new JSONObject(jsonMap).toString();


    }
}
