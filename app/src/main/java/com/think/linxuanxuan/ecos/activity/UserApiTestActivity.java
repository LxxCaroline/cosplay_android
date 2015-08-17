package com.think.linxuanxuan.ecos.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.think.linxuanxuan.ecos.R;
import com.think.linxuanxuan.ecos.model.ActivityModel;
import com.think.linxuanxuan.ecos.model.ActivityModel.ActivityType;
import com.think.linxuanxuan.ecos.model.Comment;
import com.think.linxuanxuan.ecos.model.Comment.CommentType;
import com.think.linxuanxuan.ecos.model.Course;
import com.think.linxuanxuan.ecos.model.Course.Assignment;
import com.think.linxuanxuan.ecos.model.Course.CourseType;
import com.think.linxuanxuan.ecos.model.Course.Step;
import com.think.linxuanxuan.ecos.model.Image;
import com.think.linxuanxuan.ecos.model.LocationDataService;
import com.think.linxuanxuan.ecos.model.ModelUtils;
import com.think.linxuanxuan.ecos.model.Recruitment;
import com.think.linxuanxuan.ecos.model.Recruitment.RecruitType;
import com.think.linxuanxuan.ecos.model.Share;
import com.think.linxuanxuan.ecos.model.User;
import com.think.linxuanxuan.ecos.model.UserDataService;
import com.think.linxuanxuan.ecos.request.BaseResponceImpl;
import com.think.linxuanxuan.ecos.request.NorResponce;
import com.think.linxuanxuan.ecos.request.activity.ActivityListRequest;
import com.think.linxuanxuan.ecos.request.activity.ActivityListRequest.IActivityListResponse;
import com.think.linxuanxuan.ecos.request.activity.CreateActivityRequest;
import com.think.linxuanxuan.ecos.request.activity.CreateActivityRequest.ICreateActivityResponce;
import com.think.linxuanxuan.ecos.request.comment.CreateCommentRequest;
import com.think.linxuanxuan.ecos.request.course.CourseListRequest;
import com.think.linxuanxuan.ecos.request.course.CourseListRequest.ICourseListResponse;
import com.think.linxuanxuan.ecos.request.course.CourseListRequest.SortRule;
import com.think.linxuanxuan.ecos.request.course.CourseListRequest.Type;
import com.think.linxuanxuan.ecos.request.course.GetCourseDetailRequest;
import com.think.linxuanxuan.ecos.request.course.GetCourseDetailRequest.ICourseDetailResponse;
import com.think.linxuanxuan.ecos.request.recruitment.GetRecruitmentDetailRequest;
import com.think.linxuanxuan.ecos.request.recruitment.GetRecruitmentDetailRequest.IGetRecruitmentLDetailResponse;
import com.think.linxuanxuan.ecos.request.recruitment.RecruitmentListRequest;
import com.think.linxuanxuan.ecos.request.recruitment.RecruitmentListRequest.IRecruitmentListResponse;
import com.think.linxuanxuan.ecos.request.share.GetShareDetailRequest;
import com.think.linxuanxuan.ecos.request.share.GetShareDetailRequest.IGetShareResponse;
import com.think.linxuanxuan.ecos.request.share.ShareListRequest;
import com.think.linxuanxuan.ecos.request.user.CheckAutoRequest;
import com.think.linxuanxuan.ecos.request.user.CheckAutoRequest.ICheckAutocodeResponse;
import com.think.linxuanxuan.ecos.request.user.FollowUserRequest;
import com.think.linxuanxuan.ecos.request.user.FollowUserRequest.IFollowResponce;
import com.think.linxuanxuan.ecos.request.user.FollowedUserListRequest;
import com.think.linxuanxuan.ecos.request.user.FollowedUserListRequest.IFollowUserListResponce;
import com.think.linxuanxuan.ecos.request.user.GetUserInfoRequest;
import com.think.linxuanxuan.ecos.request.user.GetUserInfoRequest.IGetUserInfoResponse;
import com.think.linxuanxuan.ecos.request.user.LoginRequest;
import com.think.linxuanxuan.ecos.request.user.RegistRequest;
import com.think.linxuanxuan.ecos.request.user.ResetPasswordRequest;
import com.think.linxuanxuan.ecos.request.user.SendAutocodeRequest;
import com.think.linxuanxuan.ecos.request.user.SendAutocodeRequest.ISendAutocodeResponse;
import com.think.linxuanxuan.ecos.request.user.SendLocationRequest;
import com.think.linxuanxuan.ecos.request.user.UpdateUserInfoRequest;

import java.util.List;

/**
 * @author enlizhang
 * @Title: UserApiTestActivity.java
 * @Description: 用户测试Api接口
 * @date 2015年7月27日 下午1:30:53
 */

public class UserApiTestActivity extends BaseActivity {

    String items[] = {"", "发送验证码", "核对验证码", "注册", "登录", "定位", "请求他人用户信息", "请求个人用户信息", "更新个人信息", "重置密码",
            "获取关注列表", "获取粉丝列表", "重置密码", "关注", "取消关注"};

    public boolean isFirst = true;

    TextView tv_display;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_api);

        MyApplication.setCurrentActivity(this);

        tv_display = (TextView) findViewById(R.id.tv_display);
        Spinner spinner = (Spinner) findViewById(R.id.spinner);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

                switch (position) {
                    case 0:
                        return;
                    case 1:
                        sendAutoCode();
                        break;
                    case 2:
                        checkAutoCode();
                        break;
                    case 3:
                        regist();
                        break;
                    case 4:
                        login();
                        break;
                    case 5:
                        sendLocation();
                        break;
                    case 6:
                        getUserInfo();
                    case 7:
                        getMySelfInfo();
                        break;
                    case 8:
                        updatePersonalInfo();
                        break;
                    case 9:
                        resetPwd();
                        break;
                    case 10:
                        getFollewsUserList();
                        break;
                    case 11:
                        getFansUserList();
                        break;
                    case 12:
                        resetPwd();
                        break;
                    case 13:
                        follow();
                        break;
                    case 14:
                        cancelFollow();
                        break;
                }
            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }

        });

    }

    /**
     * 发送验证码
     */
    public void sendAutoCode() {
        SendAutocodeRequest request = new SendAutocodeRequest();
        request.requestSend(new SendAutocodeResponse(), "18868816564");
    }

    /**
     * @author enlizhang
     * @ClassName: CreateAssignmentResponse
     * @Description: 发送验证码响应回掉接口
     * @date 2015年7月27日 下午2:12:55
     */
    class SendAutocodeResponse extends BaseResponceImpl implements ISendAutocodeResponse {

        @Override
        public void doAfterFailedResponse(String message) {
            Toast.makeText(UserApiTestActivity.this, message, Toast.LENGTH_LONG).show();
            ;
        }

        @Override
        public void onErrorResponse(VolleyError error) {

        }

        @Override
        public void success() {
        }
    }


    /**
     * 核对验证码
     */
    public void checkAutoCode() {
        CheckAutoRequest request = new CheckAutoRequest();
        String phone = "18868816564";
        String autocode = "";
        request.requestCheck(new CheckAutocodeResponse(), phone, autocode);
    }

    /**
     * @author enlizhang
     * @ClassName: CheckAutocodeResponse
     * @Description:
     * @date 2015年7月31日 下午2:10:31
     */
    class CheckAutocodeResponse extends BaseResponceImpl implements ICheckAutocodeResponse {

        @Override
        public void doAfterFailedResponse(String message) {
            Toast.makeText(UserApiTestActivity.this, message, Toast.LENGTH_LONG).show();
            ;
        }

        @Override
        public void onErrorResponse(VolleyError error) {

        }

        @Override
        public void success() {
            Toast.makeText(UserApiTestActivity.this, "验证成功", Toast.LENGTH_LONG).show();
        }

    }

    /**
     * 注册
     */
    public void regist() {
        RegistRequest request = new RegistRequest();

//        request.request(new RegistResponse(), "12312312311", "123456", "zhangenli",
//                "http://img5.imgtn.bdimg.com/it/u=3692347433,431191650&fm=21&gp=0.jpg");
    }

    /**
     * @author enlizhang
     * @ClassName: GetAssignmetnDetailResponse
     * @Description: 注册回调接口
     * @date 2015年7月27日 下午2:20:59
     */
    class RegistResponse extends BaseResponceImpl implements NorResponce {

        @Override
        public void doAfterFailedResponse(String message) {

        }

        @Override
        public void onErrorResponse(VolleyError error) {

        }

        public void success() {
            tv_display.setText("");

            User user = UserDataService.getSingleUserDataService(UserApiTestActivity.this).getUser();
            tv_display.append("账号: " + user.phone);
            tv_display.append("  userId: " + user.userId);
            tv_display.append("  imId: " + user.imId);
            tv_display.append("  avatarUrl: " + user.avatarUrl);
            tv_display.append("  nickname: " + user.nickname);

        }
    }


    /**
     * 登录
     */
    public void login() {
        LoginRequest request = new LoginRequest();
        request.request(new LoginResponse(), "18868816564", "123456");
    }

    /**
     * @author enlizhang
     * @ClassName: LoginResponse
     * @Description: 登录回调接口
     * @date 2015年7月31日 下午3:31:40
     */
    class LoginResponse extends BaseResponceImpl implements NorResponce {

        @Override
        public void doAfterFailedResponse(String message) {

        }

        @Override
        public void onErrorResponse(VolleyError error) {

        }

        public void success() {
            tv_display.setText("");

            User user = UserDataService.getSingleUserDataService(UserApiTestActivity.this).getUser();
            tv_display.append("用户id: " + user.userId + "\n");
            tv_display.append("  云信id: " + user.imId + "\n");
            tv_display.append("  头像url: " + user.avatarUrl + "\n");
            tv_display.append("  昵称: " + user.nickname + "\n");

            tv_display.append("  个性签名: " + user.characterSignature + "\n");
            tv_display.append("  封面图: " + user.coverUrl + "\n");
            tv_display.append("  粉丝数: " + user.fansNum + "\n");
            tv_display.append("  性别: " + user.gender.name() + "\n");
            tv_display.append("  角色: " + user.roleTypeSet + "\n");

        }
    }


    /**
     * 发送定位
     */
    public void sendLocation() {

        SendLocationRequest sendLocationRequest = new SendLocationRequest();
        LocationDataService service = LocationDataService.getBDLocationDataService(this);
        sendLocationRequest.request(new SendLocationResponse(),
                service.getLocationData().latitude, service.getLocationData().longitude);

        tv_display.setText("精度：" + service.getLocationData().getLatitude());
        tv_display.setText("维度：" + service.getLocationData().getLongitude());
        tv_display.setText("");

    }

    class SendLocationResponse extends BaseResponceImpl implements NorResponce {

        @Override
        public void doAfterFailedResponse(String message) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onErrorResponse(VolleyError error) {
            // TODO Auto-generated method stub

        }

        @Override
        public void success() {

        }

    }


    /**
     * 获取其他用户信息
     */
    public void getUserInfo() {

        GetUserInfoRequest request = new GetUserInfoRequest();
        request.requestOtherUserInfo(new GetuserInfoResponse(), "125");

    }

    /**
     * 获取个人信息
     */
    public void getMySelfInfo() {
        GetUserInfoRequest request = new GetUserInfoRequest();
        request.requestOtherUserInfo(new GetuserInfoResponse(),null);
    }

    /**
     * @author enlizhang
     * @ClassName: GetBannerResponse
     * @Description: 获取banner请求响应
     * @date 2015年7月27日 下午4:50:51
     */
    class GetuserInfoResponse extends BaseResponceImpl implements IGetUserInfoResponse {

        @Override
        public void doAfterFailedResponse(String message) {

        }

        @Override
        public void onErrorResponse(VolleyError error) {

        }

        @Override
        public void success(User user,boolean hasFollowed) {
            tv_display.setText("");

            tv_display.append("用户id: " + user.userId + "\n");
            tv_display.append("  云信id: " + user.imId + "\n");
            tv_display.append("  头像url: " + user.avatarUrl + "\n");
            tv_display.append("  昵称: " + user.nickname + "\n");

            tv_display.append("  个性签名: " + user.characterSignature + "\n");
            tv_display.append("  封面图: " + user.coverUrl + "\n");
            tv_display.append("  粉丝数: " + user.fansNum + "\n");
            tv_display.append("  性别: " + user.gender.name() + "\n");
            tv_display.append("  角色: " + user.roleTypeSet + "\n");
        }

    }

    /**
     * 更新个人信息
     */
    public void updatePersonalInfo() {
        UpdateUserInfoRequest request = new UpdateUserInfoRequest();

        User user = new User();

        user.avatarUrl = "http://img5.imgtn.bdimg.com/it/u=2797503391,4239472514&fm=21&gp=0.jpg";
        user.nickname = "小张";

        request.request(new UpdatePersonalInfoResponse(), user);
    }

    /**
     * @author enlizhang
     * @ClassName: UpdatePersonalInfoResponse
     * @Description: 更新个人信息
     * @date 2015年7月31日 下午10:09:10
     */
    class UpdatePersonalInfoResponse extends BaseResponceImpl implements NorResponce {

        @Override
        public void doAfterFailedResponse(String message) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onErrorResponse(VolleyError error) {
            // TODO Auto-generated method stub

        }

        @Override
        public void success() {

        }

    }


    public void getCourseListByFilter() {
        Log.e("获取筛选教程列表", "getCourseListByFilter()");

        CourseListRequest request = new CourseListRequest();
        request.request(new CourseListResponse(), Type.筛选, CourseType.化妆, "鸣人", SortRule.时间, 0);
    }


    /**
     * @author enlizhang
     * @ClassName: ICourseListRespnce
     * @Description: 教程列表响应回掉
     * @date 2015年7月27日 下午4:58:40
     */
    class CourseListResponse extends BaseResponceImpl implements ICourseListResponse {

        @Override
        public void doAfterFailedResponse(String message) {

        }

        @Override
        public void onErrorResponse(VolleyError error) {

        }

        @Override
        public void success(List<Course> courseList) {
            tv_display.setText("");

            for (Course course : courseList) {

                tv_display.append("网友昵称:" + course.author + "\n");
                tv_display.append("    教程标题:" + course.title + "\n");
                tv_display.append("    作者头像url:" + course.authorAvatarUrl + "\n");
                tv_display.append("    教程封面url:" + course.coverUrl + "\n");
                tv_display.append("    点赞数:" + course.praiseNum + "\n");

            }

        }

    }


    /**
     * 获取教程详情
     */
    public void getCourseDetail() {
        GetCourseDetailRequest request = new GetCourseDetailRequest();

        request.request(new CourseDetailtResponse(), "1");
    }

    /**
     * @author enlizhang
     * @ClassName: CourseDetailtResponse
     * @Description: 获取教程详情
     * @date 2015年7月27日 下午5:21:16
     */
    class CourseDetailtResponse extends BaseResponceImpl implements ICourseDetailResponse {

        @Override
        public void doAfterFailedResponse(String message) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onErrorResponse(VolleyError error) {
            // TODO Auto-generated method stub

        }

        @Override
        public void success(Course course) {
            tv_display.setText("");

            tv_display.append("教程封面url:" + course.coverUrl + "\n");
            tv_display.append("    教程标题:" + course.title + "\n");
            tv_display.append("    作者名字:" + course.author + "\n");
            tv_display.append("    作者头像url:" + course.authorAvatarUrl + "\n");
            tv_display.append("    教程封面url:" + course.coverUrl + "\n");
            tv_display.append("    点赞数:" + course.praiseNum + "\n");
            tv_display.append("    评论数:" + course.commentNum + "\n");
            tv_display.append("\n");

            for (Step step : course.stepList) {

                tv_display.append("教程步骤序号:" + step.stepIndex + "\n");
                tv_display.append("    教程图片url:" + step.imageUrl + "\n");
                tv_display.append("    评论内容:" + step.description + "\n");
            }
            tv_display.append("\n");

            tv_display.append("作业数:" + course.assignmentNum + "\n");

            for (Assignment assignment : course.assignmentList) {
                tv_display.append("作业作者:" + assignment.author + "\n");
                tv_display.append("    作业图片url:" + assignment.imageUrl + "\n");
                tv_display.append("    作业描述:" + assignment.content + "\n");
                tv_display.append("    作者头像url:" + assignment.authorAvatarUrl + "\n");
                tv_display.append("    作业发布时间:" + assignment.getDateDescription() + "\n");
            }
            tv_display.append("\n");
            tv_display.append("评论数:" + course.commentNum + "\n");

        }
    }


    /**
     * 获取分享列表
     */
    public void getShareList() {
        ShareListRequest request = new ShareListRequest();

        request.request(new ShareListResponse(), null, "keyWordk", 0);
    }

    /**
     * @author enlizhang
     * @ClassName: GetAssignmetnDetailResponse
     * @Description:
     * @date 2015年7月28日 下午5:24:35
     */
    class ShareListResponse extends BaseResponceImpl implements ShareListRequest.IShareListResponse {

        @Override
        public void doAfterFailedResponse(String message) {

        }

        @Override
        public void onErrorResponse(VolleyError error) {

        }

        @Override
        public void success(List<Share> shareList) {
            tv_display.setText("");

            for (int i = 0; i < shareList.size(); i++) {

                Share share = shareList.get(i);

                tv_display.append("分享名称:" + share.title + "\n");
                tv_display.append("  分享id:" + share.shareId + "\n");
                tv_display.append("  分享者头像url:" + share.avatarUrl + "\n");
                tv_display.append("  分享者昵称:" + share.nickname + "\n");
                tv_display.append("  已关注分享着:" + share.hasAttention + "\n");
                tv_display.append("  已点赞:" + share.hasPraised + "\n");
                tv_display.append("  分享封面图url:" + share.coverUrl + "\n");
                tv_display.append("  分享时间:" + share.getDateDescription() + "\n");
                tv_display.append("  分享点赞数:" + share.praiseNum + "\n");
                tv_display.append("  分享评论数:" + share.commentNum + "\n");
                tv_display.append("  图片总数:" + share.totalPageNumber + "\n");
                tv_display.append("\n");

                tv_display.append("\n");
                tv_display.append("\n");

            }


        }
    }


    /**
     * 获取分享详情
     */
    public void getShareDetail() {
        GetShareDetailRequest request = new GetShareDetailRequest();

        request.request(new GetShareResponse(), "0");
    }

    /**
     * @author enlizhang
     * @ClassName: GetShareResponse
     * @Description: 获取分享详情回掉接口
     * @date 2015年7月28日 下午7:13:18
     */
    class GetShareResponse extends BaseResponceImpl implements IGetShareResponse {

        @Override
        public void doAfterFailedResponse(String message) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onErrorResponse(VolleyError error) {

        }

        @Override
        public void success(Share share) {
            tv_display.setText("");

            tv_display.append("分享名称:" + share.title + "\n");
            tv_display.append("  分享者头像url:" + share.avatarUrl + "\n");
            tv_display.append("  分享者昵称:" + share.nickname + "\n");
            tv_display.append("  已关注分享着:" + share.hasAttention + "\n");
            tv_display.append("  已点赞:" + share.hasPraised + "\n");
            tv_display.append("  分享封面图url:" + share.coverUrl + "\n");
            tv_display.append("  分享时间:" + share.getDateDescription() + "\n");
            tv_display.append("  分享点赞数:" + share.praiseNum + "\n");
            tv_display.append("  分享评论数:" + share.commentNum + "\n");
            tv_display.append("  图片总数:" + share.totalPageNumber + "\n");

            List<Image> imageList = share.imageList;
            for (int i = 0; i < imageList.size(); i++) {

                tv_display.append("  图片ur;:" + imageList.get(i).originUrl + "\n");
            }

            tv_display.append("\n");

            for (Comment comment : share.commentList) {

                tv_display.append("网友名称:" + comment.fromNickName + "\n");
                tv_display.append("    网友头像:" + comment.avatarUrl + "\n");
                tv_display.append("    评论内容:" + comment.content + "\n");

            }
            tv_display.append("\n");
            tv_display.append("\n");
        }

    }


    public void testCreateComment() {
        Comment comment = new Comment();
        comment.commentType = CommentType.作业;
        comment.commentId = "1";
        comment.targetId = "2";
        comment.content = "你好，houzhe";

        CreateCommentRequest request = new CreateCommentRequest();
        request.request(null, comment);
    }

    /**
     * 获取活动列表
     */
    public void getActivityList() {
        ActivityListRequest request = new ActivityListRequest();

        request.request(new ActivityListResponse(), "12", ActivityType.同人展, 0);
    }

    /**
     * @author enlizhang
     * @ClassName: ActivityListResponse
     * @Description: 活动列表响应回掉接口
     * @date 2015年7月30日 下午7:14:25
     */
    class ActivityListResponse extends BaseResponceImpl implements IActivityListResponse {

        @Override
        public void doAfterFailedResponse(String message) {

        }

        @Override
        public void onErrorResponse(VolleyError error) {

        }

        @Override
        public void success(List<ActivityModel> activityList) {
            tv_display.setText("");

            for (int i = 0; i < activityList.size(); i++) {

                ActivityModel activity = activityList.get(i);

                tv_display.append("活动名称:" + activity.title + "\n");
                tv_display.append("  活动封面图:" + activity.coverUrl + "\n");
                tv_display.append("  活动开始日期:" + ModelUtils.getDateDesByTimeStamp(activity.activityTime.startDateStamp) + "\n");
                tv_display.append("  活动结束日期:" + ModelUtils.getDateDesByTimeStamp(activity.activityTime.endDateStamp) + "\n");
                tv_display.append("  每日开始时间:" + activity.activityTime.dayStartTime + "\n");
                tv_display.append("  每日结束时间:" + activity.activityTime.dayEndTime + "\n");
                tv_display.append("  城市名称:" + activity.location.city.cityName + "\n");
                tv_display.append("  详细地址:" + activity.location.address + "\n");
                tv_display.append("  活动类型:" + activity.activityType.name() + "\n");
                tv_display.append("\n");

            }


        }

    }

    /**
     * 创建活动
     */
    public void createActivity() {
        CreateActivityRequest request = new CreateActivityRequest();
        request.testData(new CreateActivityResponce(), new ActivityModel());
    }

    /**
     * @author enlizhang
     * @ClassName: CreateActivityResponce
     * @Description:
     * @date 2015年7月30日 下午8:26:40
     */
    class CreateActivityResponce extends BaseResponceImpl implements ICreateActivityResponce {

        @Override
        public void doAfterFailedResponse(String message) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onErrorResponse(VolleyError error) {
            // TODO Auto-generated method stub

        }

        @Override
        public void success(ActivityModel activity) {
            Toast.makeText(UserApiTestActivity.this, "创建成功", Toast.LENGTH_LONG).show();

        }

    }


    /**
     * 获取招募列表
     */
    public void getRecruitmentList() {
        RecruitmentListRequest request = new RecruitmentListRequest();

        //请求妆娘招募类别，城市id为12，排序方式为最受欢迎的招募列表的第0页数据
        request.request(new RecruitmentListResponse(), RecruitType.妆娘, "12", RecruitmentListRequest.SortRule.最受欢迎, 0);
    }


    /**
     * @author enlizhang
     * @ClassName: RecruitmentListResponse
     * @Description: 招募列表响应回掉接口
     * @date 2015年7月31日 上午8:32:33
     */
    class RecruitmentListResponse extends BaseResponceImpl implements IRecruitmentListResponse {

        @Override
        public void doAfterFailedResponse(String message) {

        }

        @Override
        public void onErrorResponse(VolleyError error) {

        }

        @Override
        public void success(List<Recruitment> recruitList) {
            tv_display.setText("");

            for (int i = 0; i < recruitList.size(); i++) {

                Recruitment recruit = recruitList.get(i);

//				tv_display.append("招募标题:" + recruit.title + "\n");
                tv_display.append("招募封面图url:" + recruit.coverUrl + "\n");
                tv_display.append("  招募id:" + recruit.recruitmentId + "\n");
                tv_display.append("  发起者用户id:" + recruit.userId + "\n");
                tv_display.append("  发起者云信id:" + recruit.imId + "\n");
                tv_display.append("  发起者头像:" + recruit.avatarUrl + "\n");
                tv_display.append("  发起者昵称:" + recruit.nickname + "\n");
                tv_display.append("  发起者性别:" + recruit.gender.name() + "\n");
                tv_display.append("  发起时间:" + ModelUtils.getDateDesByTimeStamp(recruit.issueTimeStamp) + "\n");
                tv_display.append("  与发起者距离:" + recruit.distanceKM + "km" + "\n");
                tv_display.append("  与发起者距离:" + "均价 " + recruit.averagePrice + "\n");
                tv_display.append("\n");

            }


        }

    }

    /**
     * 获取招募详情
     */
    public void getRecruitmentDetail() {
        GetRecruitmentDetailRequest request = new GetRecruitmentDetailRequest();

        //请求招募id为1的招募详情
        request.request(new GetRecruitmentLDetailResponse(), "1");

    }


    /**
     * @author enlizhang
     * @ClassName: RecruitmentListResponse
     * @Description: 招募列表响应回掉接口
     * @date 2015年7月31日 上午8:32:33
     */
    class GetRecruitmentLDetailResponse extends BaseResponceImpl implements IGetRecruitmentLDetailResponse {

        @Override
        public void doAfterFailedResponse(String message) {

        }

        @Override
        public void onErrorResponse(VolleyError error) {

        }

        @Override
        public void success(Recruitment recruit) {
            tv_display.setText("");


            tv_display.append("招募标题:" + recruit.title + "\n");
            tv_display.append("  招募封面图url:" + recruit.coverUrl + "\n");
            tv_display.append("  招募描述:" + recruit.description + "\n");
            tv_display.append("  招募id:" + recruit.recruitmentId + "\n");
            tv_display.append("  发起者用户id:" + recruit.userId + "\n");
            tv_display.append("  发起者云信id:" + recruit.imId + "\n");
            tv_display.append("  发起者头像:" + recruit.avatarUrl + "\n");
            tv_display.append("  发起者昵称:" + recruit.nickname + "\n");
            tv_display.append("  发起者性别:" + recruit.gender.name() + "\n");
            tv_display.append("  发起时间:" + ModelUtils.getDateDesByTimeStamp(recruit.issueTimeStamp) + "\n");
            tv_display.append("  与发起者距离:" + recruit.distanceKM + "km" + "\n");
            tv_display.append("  与发起者距离:" + "均价 " + recruit.averagePrice + "\n");
            tv_display.append("\n");

            List<Share> shareList = recruit.shareList;
            int length = shareList.size();
            for (int i = 0; i < length; i++) {

                Share share = recruit.shareList.get(i);

                tv_display.append("分享名称:" + share.title + "\n");
                tv_display.append(" 分享id:" + share.shareId + "\n");
                tv_display.append("  已关注分享着:" + share.hasAttention + "\n");
                tv_display.append("  已点赞:" + share.hasPraised + "\n");
                tv_display.append("  分享封面图url:" + share.coverUrl + "\n");
                tv_display.append("  分享时间:" + share.getDateDescription() + "\n");
                tv_display.append("  分享点赞数:" + share.praiseNum + "\n");
                tv_display.append("  分享评论数:" + share.commentNum + "\n");
                tv_display.append("  图片总数:" + share.totalPageNumber + "\n");

                tv_display.append("\n");
            }
        }
    }


    public void resetPwd() {
        ResetPasswordRequest reqeust = new ResetPasswordRequest();
        reqeust.request(new ResetPwdResponse(), "12312312310", "123456789");
    }

    class ResetPwdResponse extends BaseResponceImpl implements NorResponce {

        @Override
        public void doAfterFailedResponse(String message) {

        }

        @Override
        public void onErrorResponse(VolleyError error) {

        }

        @Override
        public void success() {
            tv_display.setText("修改成功");

        }
    }

    public void getFollewsUserList() {
        FollowedUserListRequest request = new FollowedUserListRequest();
        request.requestMyFollows(new FollowUserListResponce(), 1);
    }

    public void getFansUserList() {
        FollowedUserListRequest request = new FollowedUserListRequest();
        request.requestSomeOneFans(new FollowUserListResponce(),null, 1);
    }


    /**
     * @author enlizhang
     * @ClassName: FollowUserListResponce
     * @Description:获取关注列表
     * @date 2015年8月1日 下午3:59:03
     */
    class FollowUserListResponce extends BaseResponceImpl implements IFollowUserListResponce {

        @Override
        public void doAfterFailedResponse(String message) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onErrorResponse(VolleyError error) {
            // TODO Auto-generated method stub

        }

        @Override
        public void success(List<User> userList) {
            tv_display.setText("");

            for (int i = 0; i < userList.size(); i++) {
                User user = userList.get(i);

                tv_display.append("用户id: " + user.userId + "\n");
                tv_display.append("  云信id: " + user.imId + "\n");
                tv_display.append("  昵称: " + user.nickname + "\n");
                tv_display.append("  头像url: " + user.avatarUrl + "\n");

            }
        }

    }

    /**
     * 关注
     */
    public void follow() {
        FollowUserRequest request = new FollowUserRequest();
        request.request(new FollowResponce(), "126", true);
    }

    /**
     * 取消关注
     */
    public void cancelFollow() {
        FollowUserRequest request = new FollowUserRequest();
        request.request(new FollowResponce(), "126", false);
    }

    class FollowResponce extends BaseResponceImpl implements IFollowResponce {

        @Override
        public void doAfterFailedResponse(String message) {
        }

        @Override
        public void onErrorResponse(VolleyError error) {
        }

        @Override
        public void success(String userId, boolean follow) {
            tv_display.setText("");
            tv_display.append("操作对象userId:" + userId + "\n");
            tv_display.append("关注状态:" + follow + "\n");
        }
    }
}

