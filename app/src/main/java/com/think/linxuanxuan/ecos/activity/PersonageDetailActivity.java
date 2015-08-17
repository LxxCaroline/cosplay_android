package com.think.linxuanxuan.ecos.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.think.linxuanxuan.ecos.R;
import com.think.linxuanxuan.ecos.adapter.PersonActivityAdapter;
import com.think.linxuanxuan.ecos.adapter.PersonCourseAdapter;
import com.think.linxuanxuan.ecos.adapter.PersonDisplayAdapter;
import com.think.linxuanxuan.ecos.adapter.PersonRecruitAdapter;
import com.think.linxuanxuan.ecos.model.ActivityModel;
import com.think.linxuanxuan.ecos.model.Course;
import com.think.linxuanxuan.ecos.model.Recruitment;
import com.think.linxuanxuan.ecos.model.Share;
import com.think.linxuanxuan.ecos.model.User;
import com.think.linxuanxuan.ecos.model.UserDataService;
import com.think.linxuanxuan.ecos.request.BaseResponceImpl;
import com.think.linxuanxuan.ecos.request.activity.ActivityListRequest;
import com.think.linxuanxuan.ecos.request.course.CourseListRequest;
import com.think.linxuanxuan.ecos.request.recruitment.RecruitmentListRequest;
import com.think.linxuanxuan.ecos.request.share.ShareListRequest;
import com.think.linxuanxuan.ecos.request.user.FollowUserRequest;
import com.think.linxuanxuan.ecos.request.user.GetUserInfoRequest;
import com.think.linxuanxuan.ecos.utils.RoundImageView;
import com.think.linxuanxuan.ecos.utils.SDImageCache;
import com.think.linxuanxuan.ecos.views.ExtensibleListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class PersonageDetailActivity extends BaseActivity {

    public static final String UserID = "UserID";
    public static final String IsOwn = "IsOwn";

    private boolean isOwn = true;
    private String userID = null;

    @InjectView(R.id.iv_personage_portrait)
    RoundImageView user_avatar;
    @InjectView(R.id.radio_group)
    RadioGroup mRadioGroup;
    @InjectView(R.id.bt_personage_name)
    TextView user_name;
    @InjectView(R.id.riv_personage_gender)
    ImageView user_gender;
    @InjectView(R.id.tv_personage_attention)
    TextView user_attention;
    @InjectView(R.id.tv_personage_fans)
    TextView user_fans;
    @InjectView(R.id.tv_personage_description)
    TextView user_description;
    @InjectView(R.id.iv_return)
    ImageView iv_return;
    @InjectView(R.id.btn_attention)
    Button btn_attention;
    @InjectView(R.id.btn_contact)
    Button btn_contact;
    @InjectView(R.id.lv_list)
    ExtensibleListView lv_list;
    @InjectView(R.id.contactLayout)
    LinearLayout contactLayout;
    @InjectView(R.id.ll_signature_attention)
    LinearLayout ll_signature_attention;
    @InjectView(R.id.ll_edit)
    LinearLayout ll_edit;
    @InjectView(R.id.ll_personage_tag)
    LinearLayout ll_personage_tag;
    @InjectView(R.id.resultImageView)
    ImageView resultImageView;


    private UserDataService mUserDataService;
    private User mUserData;
    //for request
    private GetUserInfoRequest getUserInfoRequest;
    private GetuserInfoResponse getuserInfoResponse;
    private FollowUserRequest followUserRequest;
    private FollowResponce followResponce;
    //course
    private CourseListRequest courseListRequest;
    private CourseListResponse courseListResponce;
    private List<Course> mCourse = null;
    private int mCoursePageIndex = 0;
    //share
    private ShareListRequest shareListRequest;
    private ShareListResponse shareListResponse;
    private List<Share> mShare;
    private int mSharePageIndex = 1;
    //activity
    private ActivityListRequest activityListRequest;
    private ActivityListResponse activityListResponse;
    private List<ActivityModel> mActivity;
    private int mActivityPageIndex = 0;
    //recruit
    private RecruitmentListRequest recruitmentListRequest;
    private RecruitmentListResponse recruitmentListResponse;
    private List<Recruitment> mRecruitment;
    private int mRecruitmentPageIndex = 1;

    private int mCurrentTab = 0;
    private Boolean mAttentionFlag = false;

    private PersonCourseAdapter personCourseAdapter;
    private PersonDisplayAdapter personDisplayAdapter;
    private PersonActivityAdapter personActivityAdapter;
    private PersonRecruitAdapter personRecruitAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personage_detail);
        showProcessBar("正在加载数据");
        ButterKnife.inject(this);
        initRequest();
        initUserData();
        initViews();
    }

    private void initRequest() {
        courseListRequest = new CourseListRequest();
        courseListResponce = new CourseListResponse();
        shareListRequest = new ShareListRequest();
        shareListResponse = new ShareListResponse();
        activityListRequest = new ActivityListRequest();
        activityListResponse = new ActivityListResponse();
        recruitmentListRequest = new RecruitmentListRequest();
        recruitmentListResponse = new RecruitmentListResponse();

        mCourse = new ArrayList<Course>();
        mShare = new ArrayList<Share>();
        mActivity = new ArrayList<ActivityModel>();
        mRecruitment = new ArrayList<Recruitment>();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getUserInfoRequest = new GetUserInfoRequest();
        getuserInfoResponse = new GetuserInfoResponse();
        if(isOwn) {
            getUserInfoRequest.requestOtherUserInfo(getuserInfoResponse, null);
        }else {
            getUserInfoRequest.requestOtherUserInfo(getuserInfoResponse, userID);
        }
    }

    private void initUserData() {
        userID = getIntent().getExtras().getString(UserID);

        mUserDataService = UserDataService.getSingleUserDataService(this);
        mUserData = mUserDataService.getUser();//默认用户是自己

        if (!userID.equals(mUserData.userId)) {
            isOwn = false;
        }
        //isOwn = getIntent().getExtras().getBoolean(IsOwn);//assist judge
        if (isOwn) {
            setData();
        } else {
            getUserInfoRequest = new GetUserInfoRequest();
            getuserInfoResponse = new GetuserInfoResponse();
            getUserInfoRequest.requestOtherUserInfo(getuserInfoResponse, userID);
        }
        //Toast.makeText(this, "userId is " + userID, Toast.LENGTH_SHORT).show();
        courseListRequest.requestOtherCourse(courseListResponce, userID, mCoursePageIndex);
        shareListRequest.requestOtherShareList(shareListResponse, userID, mSharePageIndex);
        activityListRequest.requestOtherActivityList(activityListResponse, userID, mActivityPageIndex);
        recruitmentListRequest.requestSomeone(recruitmentListResponse, userID, mRecruitmentPageIndex);
    }

    void setData() {
        RequestQueue queue = MyApplication.getRequestQueue();
        ImageLoader.ImageCache imageCache = new SDImageCache();
        ImageLoader imageLoader = new ImageLoader(queue, imageCache);
        if (mUserData.avatarUrl != null && !mUserData.avatarUrl.equals(""))
            user_avatar.setImageUrl(mUserData.avatarUrl, imageLoader);
        if (mUserData.roleTypeSet == null || mUserData.roleTypeSet.isEmpty()) {
            ll_personage_tag.setVisibility(View.GONE);
        } else {
            ll_personage_tag.setVisibility(View.VISIBLE);
            ll_personage_tag.removeAllViews();
            for (User.RoleType type : mUserData.roleTypeSet) {
                View v = View.inflate(this, R.layout.item_tag, null);
                ((TextView) v.findViewById(R.id.tv_tag)).setText(type + "");
                ll_personage_tag.addView(v);
            }
        }
        if (mUserData.characterSignature == null || mUserData.characterSignature.equals("")) {
            user_description.setVisibility(View.GONE);
            ll_signature_attention.setVisibility(isOwn ? View.GONE : View.VISIBLE);
        } else {
            user_description.setVisibility(View.VISIBLE);
        }
        contactLayout.setVisibility(isOwn ? View.GONE : View.VISIBLE);
        user_name.setText(mUserData.nickname);
        if (mUserData.gender == User.Gender.女) {
            user_gender.setImageDrawable(getResources().getDrawable(R.mipmap.ic_gender_female));
        } else {
            user_gender.setImageDrawable(getResources().getDrawable(R.mipmap.ic_gender_male));
        }
        user_attention.setText("" + mUserData.followOtherNum);
        user_fans.setText("" + mUserData.fansNum);
        user_description.setText(mUserData.characterSignature);
        if (!isOwn) {
            ll_edit.setVisibility(View.GONE);
        } else {
            ll_edit.setVisibility(View.VISIBLE);
        }
        if ((!isOwn) && mAttentionFlag) {
            btn_attention.setText(this.getString(R.string.focus));
            btn_attention.setBackgroundResource(R.drawable.btn_focus_gray);
        } else {
            btn_attention.setText(this.getString(R.string.notFocus));
            btn_attention.setBackgroundResource(R.drawable.btn_focus_pink);
        }

    }

    private void initViews() {
        user_avatar.setDefaultImageResId(R.mipmap.bg_female_default);
        user_avatar.setErrorImageResId(R.mipmap.bg_female_default);
        personCourseAdapter = new PersonCourseAdapter(this);
        personDisplayAdapter = new PersonDisplayAdapter(this);
        personActivityAdapter = new PersonActivityAdapter(this);
        personRecruitAdapter = new PersonRecruitAdapter(this);

        personCourseAdapter.SetCourseList(mCourse);
        personDisplayAdapter.setShareList(mShare);
        personActivityAdapter.setActivityList(mActivity);
        personRecruitAdapter.setRecruitmentList(mRecruitment);
        lv_list.setAdapter(personCourseAdapter);

        ((RadioButton) mRadioGroup.getChildAt(mCurrentTab)).setChecked(true);
        RadioGroup.OnCheckedChangeListener checkedChangeListener = new RadioGroupOnCheckedChangeListener();
        mRadioGroup.setOnCheckedChangeListener(checkedChangeListener);

        View.OnClickListener listener = new PersonalPageOnClickListener();
        iv_return.setOnClickListener(listener);
        btn_attention.setOnClickListener(listener);
        btn_contact.setOnClickListener(listener);
        ll_edit.setOnClickListener(listener);
    }

    private void setUnChecked() {
        ((RadioButton) findViewById(R.id.radio_1)).setTextColor(getResources().getColor(R.color.text_gray));
        ((RadioButton) findViewById(R.id.radio_2)).setTextColor(getResources().getColor(R.color.text_gray));
        ((RadioButton) findViewById(R.id.radio_3)).setTextColor(getResources().getColor(R.color.text_gray));
        ((RadioButton) findViewById(R.id.radio_4)).setTextColor(getResources().getColor(R.color.text_gray));
    }

    private class GetuserInfoResponse extends BaseResponceImpl implements GetUserInfoRequest.IGetUserInfoResponse {

        @Override
        public void doAfterFailedResponse(String message) {
            Toast.makeText(PersonageDetailActivity.this, "error happens:" + message, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onErrorResponse(VolleyError error) {
        }

        @Override
        public void success(User user, boolean hasFollowed) {
            mUserData = user;
            setData();
            mAttentionFlag = hasFollowed;
        }
    }

    private class FollowResponce extends BaseResponceImpl implements FollowUserRequest.IFollowResponce {

        @Override
        public void doAfterFailedResponse(String message) {
            Toast.makeText(PersonageDetailActivity.this, "关注错误" + message, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onErrorResponse(VolleyError error) {
        }

        @Override
        public void success(String userId, boolean follow) {
            if (mAttentionFlag) {
                btn_attention.setText(PersonageDetailActivity.this.getString(R.string.notFocus));
                btn_attention.setBackgroundResource(R.drawable.btn_focus_pink);
                mAttentionFlag = false;
            } else {
                btn_attention.setText(PersonageDetailActivity.this.getString(R.string.focus));
                btn_attention.setBackgroundResource(R.drawable.btn_focus_gray);
                mAttentionFlag = true;
            }
        }
    }

    private class CourseListResponse extends BaseResponceImpl implements CourseListRequest.ICourseListResponse {

        @Override
        public void success(List<Course> courseList) {
            //prevent gc
            if (personCourseAdapter == null) {
                personCourseAdapter = new PersonCourseAdapter(PersonageDetailActivity.this);
                personCourseAdapter.SetCourseList(mCourse);
            }
            personCourseAdapter.getCourseList().addAll(courseList);
            if (courseList.size() >= 5) {
                courseListRequest.requestOtherCourse(courseListResponce, userID, ++mCoursePageIndex);
            } else {
                if (personCourseAdapter.getCourseList().size() == 0) {
                    lv_list.setVisibility(View.GONE);
                    resultImageView.setVisibility(View.VISIBLE);
                    resultImageView.setImageResource(R.mipmap.no_data);
                } else {
                    lv_list.setVisibility(View.VISIBLE);
                    resultImageView.setVisibility(View.GONE);
                    personCourseAdapter.notifyDataSetChanged();
                }
            }

            dismissProcessBar();

        }

        @Override
        public void doAfterFailedResponse(String message) {
            Toast.makeText(PersonageDetailActivity.this, "error happens:" + message, Toast.LENGTH_SHORT).show();
            dismissProcessBar();
        }

        @Override
        public void onErrorResponse(VolleyError volleyError) {
            dismissProcessBar();
        }

    }

    private class ShareListResponse extends BaseResponceImpl implements ShareListRequest.IShareListResponse {
        @Override
        public void success(List<Share> shareList) {
            //prevent gc
            if (personDisplayAdapter == null) {
                personDisplayAdapter = new PersonDisplayAdapter(PersonageDetailActivity.this);
                personDisplayAdapter.setShareList(mShare);
            }
            personDisplayAdapter.getShareList().addAll(shareList);
            if (shareList.size() >= 5) {
                shareListRequest.requestOtherShareList(shareListResponse, userID, ++mSharePageIndex);
            } else {
                if (personDisplayAdapter.getShareList().size() == 0) {
                    lv_list.setVisibility(View.GONE);
                    resultImageView.setVisibility(View.VISIBLE);
                    resultImageView.setImageResource(R.mipmap.no_data);
                } else {
                    lv_list.setVisibility(View.VISIBLE);
                    resultImageView.setVisibility(View.GONE);
                    personDisplayAdapter.notifyDataSetChanged();
                }
            }

        }

        @Override
        public void doAfterFailedResponse(String message) {
            Toast.makeText(PersonageDetailActivity.this, "error happens:" + message, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onErrorResponse(VolleyError volleyError) {

        }
    }

    private class ActivityListResponse extends BaseResponceImpl implements ActivityListRequest.IActivityListResponse {
        @Override
        public void success(List<ActivityModel> activityList) {
            //prevent gc
            if (personActivityAdapter == null) {
                personActivityAdapter = new PersonActivityAdapter(PersonageDetailActivity.this);
                personActivityAdapter.setActivityList(mActivity);
            }
            personActivityAdapter.getActivityList().addAll(activityList);
            if (activityList.size() >= 5) {
                activityListRequest.requestOtherActivityList(activityListResponse, userID, ++mActivityPageIndex);
            } else {
                if (personActivityAdapter.getActivityList().size() == 0) {
                    lv_list.setVisibility(View.GONE);
                    resultImageView.setVisibility(View.VISIBLE);
                    resultImageView.setImageResource(R.mipmap.no_data);
                } else {
                    lv_list.setVisibility(View.VISIBLE);
                    resultImageView.setVisibility(View.GONE);
                    personActivityAdapter.notifyDataSetChanged();
                }
            }
        }

        @Override
        public void doAfterFailedResponse(String message) {
            Toast.makeText(PersonageDetailActivity.this, "error happens:" + message, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onErrorResponse(VolleyError volleyError) {

        }
    }

    private class RecruitmentListResponse extends BaseResponceImpl implements RecruitmentListRequest.IRecruitmentListResponse {
        @Override
        public void success(List<Recruitment> recruitmentList) {
            if (personRecruitAdapter == null) {
                personRecruitAdapter = new PersonRecruitAdapter(PersonageDetailActivity.this);
                personRecruitAdapter.setRecruitmentList(mRecruitment);
            }
            personRecruitAdapter.getRecruitmentList().addAll(recruitmentList);
            if (recruitmentList.size() >= 5)
                recruitmentListRequest.requestSomeone(recruitmentListResponse, userID, ++mRecruitmentPageIndex);
            else {
                if (personRecruitAdapter.getRecruitmentList().size() == 0) {
                    lv_list.setVisibility(View.GONE);
                    resultImageView.setVisibility(View.VISIBLE);
                    resultImageView.setImageResource(R.mipmap.no_data);
                } else {
                    lv_list.setVisibility(View.VISIBLE);
                    resultImageView.setVisibility(View.GONE);
                    personRecruitAdapter.notifyDataSetChanged();
                }
            }
        }

        @Override
        public void doAfterFailedResponse(String message) {
            Toast.makeText(PersonageDetailActivity.this, "error happens:" + message, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onErrorResponse(VolleyError volleyError) {

        }
    }

    private class RadioGroupOnCheckedChangeListener implements RadioGroup.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            setUnChecked();
            switch (checkedId) {
                case R.id.radio_1:
                    ((RadioButton) findViewById(R.id.radio_1)).setTextColor(getResources().getColor(R.color.text_red));
                    if (personCourseAdapter.getCourseList().size() == 0) {
                        lv_list.setVisibility(View.GONE);
                        resultImageView.setVisibility(View.VISIBLE);
                    } else {
                        lv_list.setVisibility(View.VISIBLE);
                        resultImageView.setVisibility(View.GONE);
                        lv_list.setAdapter(personCourseAdapter);
                    }
                    break;
                case R.id.radio_2:
                    ((RadioButton) findViewById(R.id.radio_2)).setTextColor(getResources().getColor(R.color.text_red));
                    if (personDisplayAdapter.getShareList().size() == 0) {
                        lv_list.setVisibility(View.GONE);
                        resultImageView.setVisibility(View.VISIBLE);
                    } else {
                        lv_list.setVisibility(View.VISIBLE);
                        resultImageView.setVisibility(View.GONE);
                        lv_list.setAdapter(personDisplayAdapter);
                    }
                    break;
                case R.id.radio_3:
                    ((RadioButton) findViewById(R.id.radio_3)).setTextColor(getResources().getColor(R.color.text_red));
                    if (personActivityAdapter.getActivityList().size() == 0) {
                        lv_list.setVisibility(View.GONE);
                        resultImageView.setVisibility(View.VISIBLE);
                    } else {
                        lv_list.setVisibility(View.VISIBLE);
                        resultImageView.setVisibility(View.GONE);
                        lv_list.setAdapter(personActivityAdapter);
                    }
                    break;
                case R.id.radio_4:
                    ((RadioButton) findViewById(R.id.radio_4)).setTextColor(getResources().getColor(R.color.text_red));
                    if (personRecruitAdapter.getRecruitmentList().size() == 0) {
                        lv_list.setVisibility(View.GONE);
                        resultImageView.setVisibility(View.VISIBLE);
                    } else {
                        lv_list.setVisibility(View.VISIBLE);
                        resultImageView.setVisibility(View.GONE);
                        lv_list.setAdapter(personRecruitAdapter);
                    }
                    break;
            }
        }
    }

    private class PersonalPageOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_return:
                    finish();
                    break;
                case R.id.btn_attention:
                    if (followUserRequest == null)
                        followUserRequest = new FollowUserRequest();
                    if (followResponce == null)
                        followResponce = new FollowResponce();
                    if (btn_attention.getText().equals("已关注")) {
                        followUserRequest.request(followResponce, userID, false);
                    } else {
                        followUserRequest.request(followResponce, userID, true);
                    }
                    break;
                case R.id.btn_contact:
                    Intent intent = new Intent(PersonageDetailActivity.this, ContactActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString(ContactActivity.TargetUserID, mUserData.userId);
                    bundle.putString(ContactActivity.TargetUserAvatar, mUserData.avatarUrl);
                    bundle.putString(ContactActivity.TargetUserName, mUserData.nickname);
                    bundle.putString(ContactActivity.TargetUserIMID, mUserData.imId);
                    Log.v("contact", "targetIMID--------   " + mUserData.imId);
                    Log.v("contact", "targetID--------   " + mUserData.userId);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    break;
                case R.id.ll_edit:
                    startActivity(new Intent(PersonageDetailActivity.this, PersonalInfoSettingActivity.class));
                    break;
            }
        }
    }
}
