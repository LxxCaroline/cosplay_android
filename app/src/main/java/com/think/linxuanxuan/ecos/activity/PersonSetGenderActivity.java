package com.think.linxuanxuan.ecos.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.think.linxuanxuan.ecos.R;
import com.think.linxuanxuan.ecos.model.User;
import com.think.linxuanxuan.ecos.model.UserDataService;
import com.think.linxuanxuan.ecos.request.NorResponce;
import com.think.linxuanxuan.ecos.request.user.UpdateUserInfoRequest;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by hzjixinyu on 2015/8/5.
 */
public class PersonSetGenderActivity extends BaseActivity implements View.OnClickListener {
    @InjectView(R.id.lly_right_action)
    LinearLayout title_right;
    @InjectView(R.id.tv_right_text)
    TextView title_right_text;
    @InjectView(R.id.tv_title)
    TextView title_text;
    @InjectView(R.id.lly_left_action)
    LinearLayout title_left;

    @InjectView(R.id.ll_male)
    LinearLayout ll_male;
    @InjectView(R.id.ll_female)
    LinearLayout ll_female;
    @InjectView(R.id.iv_male)
    ImageView iv_male;
    @InjectView(R.id.iv_female)
    ImageView iv_female;

    private User user;
    private UpdateUserInfoRequest request;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_person_set_gender);
        ButterKnife.inject(this);

        initTitle();
        initView();
        initListener();
        initData();
    }

    private void initTitle() {
        title_left.setOnClickListener(this);
        title_right.setOnClickListener(this);
        title_right_text.setText("确定");
        title_text.setText("性别");
    }

    private void initView() {

        user = UserDataService.getSingleUserDataService(this).getUser();
        request = new UpdateUserInfoRequest();
        title_text.setText("性别");
    }

    private void initData() {
        //TODO
        if (user.gender.getValue().equals("1")) {  //if male
            iv_male.setVisibility(View.VISIBLE);
            iv_female.setVisibility(View.GONE);
        } else {   //if femlale
            iv_male.setVisibility(View.GONE);
            iv_female.setVisibility(View.VISIBLE);
        }
    }

    private void initListener() {
        title_left.setOnClickListener(this);
        title_right.setOnClickListener(this);
        ll_male.setOnClickListener(this);
        ll_female.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lly_left_action:
                finish();
                break;
            case R.id.lly_right_action:
                //TODO change 确定事件
                sendUser(user);
                break;
            case R.id.ll_male:
                iv_male.setVisibility(View.VISIBLE);
                iv_female.setVisibility(View.GONE);
                user.gender = User.Gender.男;
                break;
            case R.id.ll_female:
                iv_male.setVisibility(View.GONE);
                iv_female.setVisibility(View.VISIBLE);
                user.gender = User.Gender.女;
                break;
        }
    }

    void sendUser(User user) {
        request.request(new NorResponce() {
            @Override
            public void success() {
                Toast.makeText(PersonSetGenderActivity.this, "success", Toast.LENGTH_LONG).show();
                finish();
            }

            @Override
            public void doAfterFailedResponse(String message) {
                Toast.makeText(PersonSetGenderActivity.this, getResources().getString(R.string.personalInformationLoadError) + message, Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void responseNoGrant() {
                Toast.makeText(PersonSetGenderActivity.this, getResources().getString(R.string.personalInformationLoadError), Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(PersonSetGenderActivity.this, getResources().getString(R.string.personalInformationLoadError), Toast.LENGTH_SHORT).show();
                finish();
            }
        }, user);
    }
}
