package com.think.linxuanxuan.ecos.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.think.linxuanxuan.ecos.R;
import com.think.linxuanxuan.ecos.dialog.SetPhotoDialog;
import com.think.linxuanxuan.ecos.model.AccountDataService;
import com.think.linxuanxuan.ecos.model.User;
import com.think.linxuanxuan.ecos.model.UserDataService;
import com.think.linxuanxuan.ecos.request.BaseResponceImpl;
import com.think.linxuanxuan.ecos.request.NorResponce;
import com.think.linxuanxuan.ecos.request.user.UpdateUserInfoRequest;
import com.think.linxuanxuan.ecos.utils.RoundAngleImageView;
import com.think.linxuanxuan.ecos.utils.RoundImageView;
import com.think.linxuanxuan.ecos.utils.SDImageCache;
import com.think.linxuanxuan.ecos.utils.SetPhotoHelper;
import com.think.linxuanxuan.ecos.utils.UploadImageTools;
import com.think.linxuanxuan.ecos.views.RoundedNetworkImageView;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.Set;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class PersonalInfoSettingActivity extends BaseActivity {

    private static final String[] gender = {"男", "女", "保密"};

    //title
    @InjectView(R.id.lly_right_action)
    LinearLayout title_right;
    @InjectView(R.id.tv_right_text)
    TextView title_right_text;
    @InjectView(R.id.tv_title)
    TextView title_text;
    @InjectView(R.id.lly_left_action)
    LinearLayout title_left;

    //show
    private LinearLayout mReturn;
    private RoundImageView mAvatarImg;
    private ImageView mSetAvatar;
    private TextView mSetName;
    private TextView mSetGender;
    private TextView mSetIntro;
    private CheckBox mSetMsgAlert;
    private Button mLogOut;
    private LinearLayout ll_tagsList;

    //click
    @InjectView(R.id.ll_name)
    LinearLayout ll_name;
    @InjectView(R.id.ll_gender)
    LinearLayout ll_gender;
    @InjectView(R.id.ll_signature)
    LinearLayout ll_signature;
    @InjectView(R.id.ll_password)
    LinearLayout ll_password;
    @InjectView(R.id.ll_tags)
    LinearLayout ll_tags;
    @InjectView(R.id.personal_info_set_pic)
    LinearLayout personal_info_set_pic;
    @InjectView(R.id.personal_info_set_avatar_pic)
    RoundAngleImageView personal_info_set_avatar_pic;
    @InjectView(R.id.tv_tag1)
    TextView tv_tag1;
    @InjectView(R.id.tv_tag2)
    TextView tv_tag2;
    @InjectView(R.id.tv_tag3)
    TextView tv_tag3;
    @InjectView(R.id.tv_tag4)
    TextView tv_tag4;
    @InjectView(R.id.tv_tag5)
    TextView tv_tag5;
    @InjectView(R.id.tv_tag6)
    TextView tv_tag6;
    @InjectView(R.id.tv_phone_num)
    TextView tv_phone_num;

//    private RoundAngleImageView iv;

    private SetPhotoHelper mSetPhotoHelper;
    private User user;
    private UpdateUserInfoRequest request;


    public boolean isSettingAvatart = false;

    public String mAvatarLocalPath = "";

    public String mAvatarUrl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info_setting);
        ButterKnife.inject(this);
        initTitle();
        onBoundView();
        onBoundLinster();

        mSetPhotoHelper = new SetPhotoHelper(this, null);
        user = UserDataService.getSingleUserDataService(this).getUser();
        request = new UpdateUserInfoRequest();
//        iv = (RoundAngleImageView) findViewById(R.id.picasso_test);
//        iv.setImageFromUrl("http://pic4.nipic.com/20090803/2618170_095921092_2.jpg");

    }

    private void initTitle() {
        title_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        title_right.setVisibility(View.INVISIBLE);
        title_right_text.setText("发布");
        title_text.setText("设置");
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUserData();
    }

    private void onBoundView() {
        mReturn = (LinearLayout) findViewById(R.id.lly_left_action);
        mSetName = (TextView) findViewById(R.id.personal_info_set_name);
        mSetGender = (TextView) findViewById(R.id.personal_info_set_gender);
        mSetIntro = (TextView) findViewById(R.id.personal_info_set_intro);
        mSetMsgAlert = (CheckBox) findViewById(R.id.personal_info_set_Msg_alert);
        mLogOut = (Button) findViewById(R.id.personal_info_logout);
        ll_tagsList = (LinearLayout) findViewById(R.id.ll_tagsList);  //add tags in this layout
    }

    private void onBoundLinster() {
        //bound button linster
        ButtonListener listener = new ButtonListener();
        mReturn.setOnClickListener(listener);
        mLogOut.setOnClickListener(listener);

        ll_name.setOnClickListener(listener);
        ll_gender.setOnClickListener(listener);
        ll_signature.setOnClickListener(listener);
        ll_password.setOnClickListener(listener);
        ll_tags.setOnClickListener(listener);
        personal_info_set_pic.setOnClickListener(listener);


        //bound spinner linster
        bonudSpinner();
        //bound swith linster
        mSetMsgAlert.setOnCheckedChangeListener(new SwitchCheckedListener());

    }

    private void bonudSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, gender);
        //设置下拉列表的风格
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case SetPhotoHelper.REQUEST_BEFORE_CROP:
                    mSetPhotoHelper.setmSetPhotoCallBack(new SetPhotoHelper.SetPhotoCallBack() {

                        @Override
                        public void success(String imagePath) {
                            mAvatarLocalPath = imagePath;
                            setAndUploadAvatar();
                        }
                    });
                    mSetPhotoHelper.handleActivityResult(SetPhotoHelper.REQUEST_BEFORE_CROP, data);
                    return;

                case SetPhotoHelper.REQUEST_AFTER_CROP:
                    mSetPhotoHelper.handleActivityResult(SetPhotoHelper.REQUEST_AFTER_CROP, data);
                    break;
                default:
                    Log.e("CLASS_TAG", "onActivityResult() 无对应");
            }


        } else {
            Log.e(CLASS_TAG, "操作取消");
        }
    }

    class ButtonListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.lly_left_action:
                    finish();
                    break;
                case R.id.personal_info_set_pic:
                    //TODO 调出相册和相机
                    SetPhotoDialog dialog = new SetPhotoDialog(PersonalInfoSettingActivity.this, new SetPhotoDialog.ISetPhoto() {

                        @Override
                        public void choosePhotoFromLocal() {
                            mSetPhotoHelper.choosePhotoFromLocal();
                        }

                        @Override
                        public void takePhoto() {
                            mSetPhotoHelper.takePhoto(true);
                        }
                    });
                    dialog.showSetPhotoDialog();
                    mSetPhotoHelper.setAspect(1, 1);
                    mSetPhotoHelper.setOutput(200, 200);
                    break;
                case R.id.ll_name:
                    Intent intent2 = new Intent(PersonalInfoSettingActivity.this, PersonSetInformationNormalActivity.class);
                    Bundle bundle2 = new Bundle();
                    bundle2.putInt(PersonSetInformationNormalActivity.ACTICITY_TYPE, PersonSetInformationNormalActivity.TYPE_NAME);
                    intent2.putExtras(bundle2);
                    startActivity(intent2);
                    break;
                case R.id.ll_gender:
                    startActivity(new Intent(PersonalInfoSettingActivity.this, PersonSetGenderActivity.class));
                    break;
                case R.id.ll_tags:
                    startActivity(new Intent(PersonalInfoSettingActivity.this, PersonSetTagsActivity.class));
                    break;
                case R.id.ll_signature:
                    Intent intent3 = new Intent(PersonalInfoSettingActivity.this, PersonSetInformationNormalActivity.class);
                    Bundle bundle3 = new Bundle();
                    bundle3.putInt(PersonSetInformationNormalActivity.ACTICITY_TYPE, PersonSetInformationNormalActivity.TYPE_SIGNATURE);
                    intent3.putExtras(bundle3);
                    startActivity(intent3);
                    break;
                case R.id.ll_password:
                    Intent intent1 = new Intent(PersonalInfoSettingActivity.this, PersonSetInformationNormalActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt(PersonSetInformationNormalActivity.ACTICITY_TYPE, PersonSetInformationNormalActivity.TYPE_PASSWORD);
                    intent1.putExtras(bundle);
                    startActivity(intent1);
                    break;
                case R.id.personal_info_logout:

                    if (MyApplication.msMainActivity!=null){
                        MyApplication.msMainActivity.unregistObserver();
                    }


                    AccountDataService.getSingleAccountDataService(PersonalInfoSettingActivity.this).clearAllDataExceptUsername();
                    UserDataService.getSingleUserDataService(PersonalInfoSettingActivity.this).clearAllData();

                    if(MyApplication.msMainActivity!=null)
                        MyApplication.msMainActivity.unregistObserver();

                    Intent intent = new Intent(PersonalInfoSettingActivity.this, SplashActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                    break;
            }
        }
    }

    class SpinnerSelectedListener implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
            //TODO 选中后要做的事情
        }

        public void onNothingSelected(AdapterView<?> arg0) {
        }
    }

    class SwitchCheckedListener implements CompoundButton.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                //TODO 接收消息
            } else {
                //TODO 关闭接收
            }
        }
    }

    private void sendUser(User user) {
        request.request(new NorResponce() {
            @Override
            public void success() {
                Toast.makeText(PersonalInfoSettingActivity.this, "success", Toast.LENGTH_LONG).show();
            }

            @Override
            public void doAfterFailedResponse(String message) {
                Toast.makeText(PersonalInfoSettingActivity.this, "网络异常，个人信息更新失败", Toast.LENGTH_LONG).show();
            }

            @Override
            public void responseNoGrant() {

            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }, user);
    }

    private void setUserData() {
        User setUser = UserDataService.getSingleUserDataService(PersonalInfoSettingActivity.this).getUser();
        user = UserDataService.getSingleUserDataService(this).getUser();
        mAvatarUrl = setUser.avatarUrl;
//        personal_info_set_avatar_pic.setErrorImageResId(R.mipmap.bg_female_default);
//        personal_info_set_avatar_pic.setDefaultImageResId(R.mipmap.bg_female_default);
        if (mAvatarUrl != null && !TextUtils.isEmpty(mAvatarUrl)) {
            Picasso.with(PersonalInfoSettingActivity.this).load(mAvatarUrl).placeholder(R.mipmap.bg_female_default).error(R.mipmap.bg_female_default).into(personal_info_set_avatar_pic);
        } else
            personal_info_set_avatar_pic.setImageResource(R.mipmap.bg_female_default);

        Log.w("nickname", setUser.nickname);

        mSetName.setText(setUser.nickname);
        if (setUser.gender.getValue().equals("0"))
            mSetGender.setText("暂无");
        else if (setUser.gender.getValue().equals("1"))
            mSetGender.setText("男");
        else if (setUser.gender.getValue().equals("2"))
            mSetGender.setText("女");
        setTagVisiable(user.roleTypeSet);

        String phone = AccountDataService.getSingleAccountDataService(this).getPhone();
        if (phone != null && !phone.equals(""))
            tv_phone_num.setText(phone);
        else {
            tv_phone_num.setText("");
            Log.d("ZYW00000000", "phone num is null");
        }

        if (setUser.characterSignature.equals(PersonSetInformationNormalActivity.NineSpace))
            mSetIntro.setText("这家伙很懒什么都没留下");
        else
            mSetIntro.setText(setUser.characterSignature);
    }

    void setTagVisiable(Set<User.RoleType> roleTypeSet) {
        Log.w("roleType Size", roleTypeSet.size() + "");
        tv_tag1.setVisibility(View.GONE);
        tv_tag2.setVisibility(View.GONE);
        tv_tag3.setVisibility(View.GONE);
        tv_tag4.setVisibility(View.GONE);
        tv_tag5.setVisibility(View.GONE);
        tv_tag6.setVisibility(View.GONE);

        for (User.RoleType roleType : roleTypeSet) {
            if (roleType == null) {
                continue;//in case of  roletype null.
            }
            switch (roleType.getBelongs()) {
                case "0":
                    tv_tag1.setVisibility(View.VISIBLE);
                    break;
                case "1":
                    tv_tag2.setVisibility(View.VISIBLE);
                    break;
                case "2":
                    tv_tag3.setVisibility(View.VISIBLE);
                    break;
                case "3":
                    tv_tag4.setVisibility(View.VISIBLE);
                    break;
                case "4":
                    tv_tag5.setVisibility(View.VISIBLE);
                    break;
                case "5":
                    tv_tag6.setVisibility(View.VISIBLE);
                    break;
            }
            Log.w("roleType", roleType.getBelongs());
        }
    }


    /**
     * 设置并上传头像
     */
    public void setAndUploadAvatar() {

        if (mAvatarLocalPath != null && !"".equals(mAvatarLocalPath)) {
            UploadImageTools.uploadImageSys(new File(mAvatarLocalPath), new UploadImageTools.UploadCallBack() {

                @Override
                public void success(String originUrl, String thumbUrl) {
                    Log.e(TAG, "图片url:" + originUrl);

                    mAvatarUrl = originUrl;
                    User user = UserDataService.getSingleUserDataService(PersonalInfoSettingActivity.this)
                            .getUser();
                    user.avatarUrl = originUrl;

                    UpdateUserInfoRequest request = new UpdateUserInfoRequest();
                    request.request(new UpdateAvatarResponse(), user);

                }

                @Override
                public void fail() {
                    dismissProcessBar();
                }

                @Override
                public void onProcess(Object fileParam, long current, long total) {

                }
            }, PersonalInfoSettingActivity.this, false);
        }

    }


    class UpdateAvatarResponse extends BaseResponceImpl implements NorResponce {

        @Override
        public void success() {
            if (mAvatarUrl != null) {
                ImageLoader imageLoader = new ImageLoader(MyApplication.getRequestQueue(), new SDImageCache());
                if (mAvatarUrl != null && !TextUtils.isEmpty(mAvatarUrl)){
                    Picasso.with(PersonalInfoSettingActivity.this).load(mAvatarUrl).placeholder(R.mipmap.bg_female_default).error(R.mipmap.bg_female_default).into(personal_info_set_avatar_pic);
                }else {
                    personal_info_set_avatar_pic.setImageResource(R.mipmap.bg_female_default);
                }

            }

        }

        @Override
        public void doAfterFailedResponse(String message) {
            Toast.makeText(PersonalInfoSettingActivity.this, "error happens:" + message, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onErrorResponse(VolleyError volleyError) {

        }
    }
}
