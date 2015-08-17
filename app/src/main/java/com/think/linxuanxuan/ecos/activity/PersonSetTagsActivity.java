package com.think.linxuanxuan.ecos.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
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
public class PersonSetTagsActivity extends BaseActivity implements View.OnClickListener {

    @InjectView(R.id.lly_right_action)
    LinearLayout title_right;
    @InjectView(R.id.tv_right_text)
    TextView title_right_text;
    @InjectView(R.id.tv_title)
    TextView title_text;
    @InjectView(R.id.lly_left_action)
    LinearLayout title_left;

    @InjectView(R.id.checkbox1)
    CheckBox checkBox1;
    @InjectView(R.id.checkbox2)
    CheckBox checkBox2;
    @InjectView(R.id.checkbox3)
    CheckBox checkBox3;
    @InjectView(R.id.checkbox4)
    CheckBox checkBox4;
    @InjectView(R.id.checkbox5)
    CheckBox checkBox5;
    @InjectView(R.id.checkbox6)
    CheckBox checkBox6;

    private User user;
    private UpdateUserInfoRequest request;

    private User.RoleType roleType;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_person_set_tags);
        ButterKnife.inject(this);

        initTitle();
        initView();
        initListener();
        initData();
    }

    private void initTitle() {
        title_right_text.setText("确定");
        title_text.setText("标签");
    }

    private void initView() {
        user = UserDataService.getSingleUserDataService(this).getUser();
        title_text.setText("设置");
        request = new UpdateUserInfoRequest();
    }

    private void initListener() {
        title_left.setOnClickListener(this);
        title_right.setOnClickListener(this);
    }


    private void initData() {
        user = UserDataService.getSingleUserDataService(this).getUser();
        for (User.RoleType roleType : user.roleTypeSet){
            switch (roleType.getBelongs()){
                case "0":
                    checkBox1.setChecked(true);
                    break;
                case "1":
                    checkBox2.setChecked(true);
                    break;
                case "2":
                    checkBox3.setChecked(true);
                    break;
                case "3":
                    checkBox4.setChecked(true);
                    break;
                case "4":
                    checkBox5.setChecked(true);
                    break;
                case "5":
                    checkBox6.setChecked(true);
                    break;
            }
        }
        user.roleTypeSet.clear();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lly_left_action:
                finish();
                break;
            case R.id.lly_right_action:
                //TODO
                if (checkBox1.isChecked()) {
                    roleType = User.RoleType.coser;
                    user.roleTypeSet.add(roleType);
                    Log.w("inTag", "coser");
                }
                if (checkBox2.isChecked()) {
                    roleType = User.RoleType.妆娘;
                    user.roleTypeSet.add(roleType);
                    Log.w("inTag", "妆娘");
                }
                if (checkBox3.isChecked()) {
                    roleType = User.RoleType.摄影;
                    user.roleTypeSet.add(roleType);
                    Log.w("inTag", "摄影");
                }
                if (checkBox4.isChecked()) {
                    roleType = User.RoleType.后期;
                    user.roleTypeSet.add(roleType);
                    Log.w("inTag", "后期");
                }
                if (checkBox5.isChecked()) {
                    roleType = User.RoleType.裁缝;
                    user.roleTypeSet.add(roleType);
                    Log.w("inTag", "裁缝");
                }
                if (checkBox6.isChecked()) {
                    roleType = User.RoleType.道具;
                    user.roleTypeSet.add(roleType);
                    Log.w("inTag", "道具");
                }
                Log.w("roleType---Size", user.roleTypeSet.size() + "");
                sendUser(user);
                break;
        }
    }

    void sendUser(User user) {
        request.request(new NorResponce() {
            @Override
            public void success() {
                Toast.makeText(PersonSetTagsActivity.this, "success", Toast.LENGTH_LONG).show();
                finish();
            }

            @Override
            public void doAfterFailedResponse(String message) {
                Toast.makeText(PersonSetTagsActivity.this, getResources().getString(R.string.personalInformationLoadError) + message, Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void responseNoGrant() {
                Toast.makeText(PersonSetTagsActivity.this, getResources().getString(R.string.personalInformationLoadError), Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(PersonSetTagsActivity.this, getResources().getString(R.string.personalInformationLoadError), Toast.LENGTH_SHORT).show();
                finish();
            }
        }, user);
    }
}
