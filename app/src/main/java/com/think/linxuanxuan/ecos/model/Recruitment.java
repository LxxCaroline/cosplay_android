package com.think.linxuanxuan.ecos.model;

import com.think.linxuanxuan.ecos.model.User.Gender;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @ClassName: Recruitment
 * @Description: 招募
 * @author enlizhang
 * @date 2015年7月25日 下午11:31:09
 *
 */
public class Recruitment {

    /*** 招募id */
    public String recruitmentId;

    /*** 标题 */
    public String  title;

    /*** 城市码 */
    public String cityCode;

    /*** 介绍 */
    public String description;

    /*** 发起者的用户id{@link User} */
    public String userId;

    /*** 发起者的用户昵称 */
    public String nickname;

    /*** 发起者云信id */
    public String imId;

    /*** 发起者性别 */
    public Gender gender;

    /*** 发起者头像url */
    public String avatarUrl;

    /*** 均价 */
    public String averagePrice;

    /*** 均价单位 */
    public String priceUnit;

    /*** 封面图本地路径 */
    public String coverLocalPath;

    /*** 封面图url */
    public String  coverUrl;

    /** 发布时间,是一个时间戳 */
    public long issueTimeStamp ;

    /*** 距离，单位千米 */
    public String distanceKM;

    /*** 作品{@link Share}}列表 */
    public List<com.think.linxuanxuan.ecos.model.Share> shareList;

    /*** 招募类别 */
    public RecruitType recruitType;

    public Recruitment(){
        gender = Gender.男;
        recruitType =RecruitType.妆娘;
        shareList = new ArrayList<com.think.linxuanxuan.ecos.model.Share>();
    }


    /***
     *
     * @ClassName: RecruitType
     * @Description: 招募类别，包括妆娘、摄影、后期、服装、道具、其他
     * @author enlizhang
     * @date 2015年7月30日 下午9:37:51
     *
     */
    public static enum RecruitType
    {
        妆娘("0","元/人"),
        摄影("1","元/小时"),
        后期("2","元/张"),
        服装("3","元/套"),
        道具("4","元/套"),
        其他("5","元");

        public String value;

        public String priceUnit;

        RecruitType(String _value,String _priceUnit){
            value = _value;
            priceUnit = _priceUnit;
        }

        public String getValue(){
            return value;
        }

        public String getPriceUnit(){
            return priceUnit;
        }

        /***
         * 根据值获取对应枚举
         * @param value
         * @return
         */
        public static RecruitType getRecruitTypeByValue(String value){

            for(RecruitType recruitType:RecruitType.values()){
                if(recruitType.getValue().equals(value))
                    return recruitType;
            }

            return null;
        }
    }




    @Override
    public String toString() {
        return "Recruitment [recruitmentId=" + recruitmentId + ", title="
                + title + ", cityCode=" + cityCode + ", description="
                + description + ", userId=" + userId + ", nickname=" + nickname
                + ", imId=" + imId + ", gender=" + gender + ", avatarUrl="
                + avatarUrl + ", averagePrice=" + averagePrice + ", priceUnit="
                + priceUnit + ", coverLocalPath=" + coverLocalPath
                + ", coverUrl=" + coverUrl + ", issueTimeStamp="
                + issueTimeStamp + ", distanceKM=" + distanceKM
                + ", shareList=" + shareList + ", recruitType=" + recruitType
                + "]";
    }



}
