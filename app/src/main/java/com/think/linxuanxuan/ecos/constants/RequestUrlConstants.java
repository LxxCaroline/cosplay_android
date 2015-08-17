package com.think.linxuanxuan.ecos.constants;

public class RequestUrlConstants {


    private static final String HOST = "http://223.252.223.141:80";

    private static final String HOST_COURSE = "http://223.252.223.141:80";
//    private static final String HOST_COURSE = "http://10.240.34.181:8080/ecos-mainsite-web";




    private static final String HOST_USER = HOST;

    public static final String TEST_TOKEN = "e5daf20fc3b64b9cb85ccb38a08d8f19";

    public static final String HOUZHE_HOST = "http://10.240.34.189:8080/ecos-mainsite-web";
//    public static final String HOUZHE_HOST = "http://223.252.223.141:80";


    /**------------------------------    通用模块    -------------------------------------------*/
    /**
     * 获取初始数据
     */
    public static final String INITIAL_DATA_URL = HOST + "application/init?";


    /**------------------------------    用户模块    -------------------------------------------*/

    /**
     * 获取与核对验证码
     */
    public static final String AUTO_CODE_URL = HOST_USER + "/m/user/authcode?";

    /**
     * 注册
     */
    public static final String REGIST_URL = HOST_USER + "/m/user/register?";

    /**
     * 登录
     */
    public static final String LOLGIN_URL = HOST_USER + "/m/user/login?";

    /**
     * 登出
     */
    public static final String LOGOUT = HOST_USER + "/m/user/logout?";

    /**
     * 用户城市定位
     */
    public static final String SEND_LOCATON_URL = HOST_USER + "/m/user/location?";

    /**
     * 忘记密码
     */
    public static final String RESET_PASSWORD_URL = HOST_USER + "/m/user/pwd/reset";

    /**
     * 修改密码
     */
    public static final String MODIFY_PASSWORD = HOST_USER + "/m/user/pwd/modify";

    /**
     * 获取用户信息
     */
    public static final String GET_USER_INFO = HOST_USER + "/m/user/info?";

    /**
     * 修改用户信息
     */
    public static final String UPDATE_USER_INFO = HOST_USER + "/m/user/update?";

    /**
     * 关注其他用户
     */
    public static final String FOLLOW_USER_INFO = HOST_USER + "/m/user/follow?";

    /**
     * 获取关注的用户列表
     */
    public static final String GET_FOLLED_USER_LIST = HOST_USER + "/m/user/follow/list?";


    /**------------------------------    教程模块    -------------------------------------------*/
    /**|**/
    /**|**/
    /**|**/
    /**|**/
    /**------------------------------    教程模块    -------------------------------------------*/
    /**
     * 获取营销banner
     */
    public static final String GET_BANNER = HOST_COURSE + "/m/course/banner?";

    /**
     * 创建教程
     */
    public static final String CREATE_COURSE_URL = HOST_COURSE + "/m/course/create?";

    /**
     * 获取教程列表
     */
    public static final String GET_COURSE_LIST_URL = HOST_COURSE + "/m/course/list?";

    /**
     * 获取教程详情
     */
    public static final String GET_COURSE_DETAIL_URL = HOST_COURSE + "/m/course/detail?";

    /**
     * 删除教程
     */
    public static final String DELETE_COURSE_URL = HOST_COURSE + "/m/course/delete?";

    /**
     * 创建教程作业
     */
    public static final String CREATE_ASSIGNMENT_URL = HOST_COURSE + "/m/course/assignment/create?";

    /**
     * 获取教程作业列表
     */
    public static final String GET_ASSIGNMENT_LIST_URL = HOST_COURSE + "/m/course/assignment/list?";

    /**
     * 获取教程作业详情
     */
    public static final String GET_ASSIGNMENT_DETAIL_URL = HOST_COURSE + "/m/course/assignment/detail?";

    /**
     * 删除教程作业
     */
    public static final String DELETE_ASSIGNMENT_URL = HOST_COURSE + "/m/course/assignment/delete?";


    /**------------------------------    评论模块    -------------------------------------------*/
    /**|**/
    /**|**/
    /**|**/
    /**|**/
    /**------------------------------    评论模块    -------------------------------------------*/

    /**
     * 创建评论
     */
    public static final String CREATE_COMMENT_URL = HOST + "/m/comment/create?";

    /**
     * 获取评论列表
     */
    public static final String GET_COMMENT_LIST_URL = HOST + "/m/comment/list?";

    /**
     * 获取评论详情
     */
    public static final String GET_COMMENT_DETAIL_URL = HOST + "/m/comment/detail?";

    /**
     * 删除评论
     */
    public static final String DELETE_COMMENT_URL = HOST + "/m/comment/delete?";


    /**------------------------------    分享模块    -------------------------------------------*/
    /**|**/
    /**|**/
    /**|**/
    /**|**/
    /**------------------------------    分享模块    -------------------------------------------*/

    /**
     * 创建分享
     */
    public static final String CREATE_SHARE_URL = HOST + "/m/share/create?";

    /**
     * 获取分享列表
     */
    public static final String GET_SHARE_LIST_URL = HOST + "/m/share/list?";

    /**
     * 获取分享详情
     */
    public static final String GET_SHARE_DETAIL_URL = HOST + "/m/share/detail?";

    /**
     * 删除分享
     */
    public static final String DELETE_SHARE_URL = HOST + "/m/share/delete?";


    /**------------------------------    活动模块    -------------------------------------------*/
    /**|**/
    /**|**/
    /**|**/
    /**|**/
    /**------------------------------    活动模块    -------------------------------------------*/

    /**
     * 创建活动
     */
    public static final String CREATE_ACTIVITY_URL = HOST + "/m/activity/create?";

    /**
     * 获取活动列表
     */
    public static final String GET_ACTIVITY_LIST_URL = HOST + "/m/activity/list?";

    /**
     * 获取活动详情
     */
    public static final String GET_ACTIVITY_DETAIL_URL = HOST + "/m/activity/detail?";

    /**
     * 删除活动
     */
    public static final String DELETE_ACTIVITY_URL = HOST + "/m/activity/delete?";

    /**
     * 活动报名
     */
    public static final String SIGNUP_ACTIVITY_URL = HOST + "/m/activity/signup?";

    /**
     * 获取活动报名人列表
     */
    public static final String ACTIVITY_SIGNUP_PEOPLE_URL = HOST + "/m/activity/signup/list?";


    /**------------------------------    活动实况模块    -------------------------------------------*/
    /**|**/
    /**|**/
    /**|**/
    /**|**/
    /**------------------------------    活动实况模块    -------------------------------------------*/

    /**
     * 创建活动实况
     */
    public static final String CREATE_SITUATION_URL = HOST + "/m/activity/photo/create?";

    /**
     * 获取活动实况列表
     */
    public static final String GET_SITUATION_DETAIL_URL = HOST + "/m/activity/photo/list?";

    /**
     * 获取活动实况详情
     */
    public static final String GET_SITUATION_LIST_URL = HOST + "/m/activity/photo/detail?";

    /**
     * 删除活动实况
     */
    public static final String DELETE_SITUATION_URL = HOST + "/m/activity/photo/delete?";


    /**------------------------------    招募模块    -------------------------------------------*/
    /**|**/
    /**|**/
    /**|**/
    /**|**/
    /**------------------------------    招募模块    -------------------------------------------*/
    /**
     * 创建招募
     */
    public static final String CREATE_RECRUITMENT_URL = HOST + "/m/recruit/create?";

    /**
     * 获取招募列表
     */
    public static final String GET_RECRUITMENT_LIST_URL = HOST + "/m/recruit/list?";

    /**
     * 获取招募详情
     */
    public static final String GET_RECRUITMENT_DETAIL_URL = HOST + "/m/recruit/detail?";


    /**
     * 点赞教程、分享
     */
    public static final String PRAISE_URL = HOST_COURSE + "/m/praise/create?";

    /**
     * 获取省份列表
     */
    public static final String PROVINCE_LIST_URL = HOST + "/m/activity/province/list";

    /**
     * 获取城市列表
     */
    public static final String CITY_LIST_URL = HOST + "/m/activity/city/list";

}
