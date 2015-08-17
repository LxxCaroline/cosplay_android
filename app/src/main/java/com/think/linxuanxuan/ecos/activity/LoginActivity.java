package com.think.linxuanxuan.ecos.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.think.linxuanxuan.ecos.R;
import com.think.linxuanxuan.ecos.model.AccountDataService;
import com.think.linxuanxuan.ecos.model.InputLength;
import com.think.linxuanxuan.ecos.model.LocationData;
import com.think.linxuanxuan.ecos.request.BaseResponceImpl;
import com.think.linxuanxuan.ecos.request.NorResponce;
import com.think.linxuanxuan.ecos.request.VolleyErrorParser;
import com.think.linxuanxuan.ecos.request.user.LoginRequest;
import com.think.linxuanxuan.ecos.request.user.SendLocationRequest;
import com.netease.nimlib.sdk.AbortableFuture;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.auth.LoginInfo;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by hzjixinyu on 2015/7/30.
 */
public class LoginActivity extends BaseActivity implements TextWatcher, View.OnClickListener {
    @InjectView(R.id.et_phone)
    EditText et_phone;
    @InjectView(R.id.et_password)
    EditText et_password;
    @InjectView(R.id.tv_login)
    TextView tv_login;
    @InjectView(R.id.tv_forgetPassword)
    TextView tv_forgetPassword;
    @InjectView(R.id.iv_return)
    ImageView iv_return;
    @InjectView(R.id.logo)
    ImageView logo;
    @InjectView(R.id.main)
    LinearLayout main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);


        initListener();
        initData();

        iv_return.requestFocus();
    }

    private void initListener() {
        et_phone.addTextChangedListener(this);
        et_password.addTextChangedListener(this);
        main.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if ((main.getRootView().getHeight() - main.getHeight()) > 100) {
                    logo.setVisibility(View.GONE);
                } else {
                    logo.setVisibility(View.VISIBLE);
                }
            }
        });

        tv_forgetPassword.setOnClickListener(this);
        tv_login.setOnClickListener(this);
        iv_return.setOnClickListener(this);
        main.setOnClickListener(this);
    }

    private void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_login:
                login();
                break;
            case R.id.tv_forgetPassword:
                startActivityForResult(new Intent(LoginActivity.this, VerifyCodeActivity.class), 0);
                break;
            case R.id.iv_return:
                finish();
                break;
        }
    }

    private void login() {
        showProcessBar("登录中");
        LoginRequest request = new LoginRequest();
        request.request(new LoginResponse(), et_phone.getText().toString(), et_password.getText().toString());
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (et_password.getText().toString().length() >= InputLength.Password_min && et_password.getText().toString().length() <= InputLength.Password_max && !TextUtils.isEmpty(et_phone.getText().toString())) {
//        if (!TextUtils.isEmpty(et_phone.getText().toString())){
            tv_login.setEnabled(true);
        } else {
            tv_login.setEnabled(false);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.v("code", "enter " + keyCode);
        return super.onKeyDown(keyCode, event);
    }


    class LoginResponse extends BaseResponceImpl implements NorResponce {

        @Override
        public void success() {

            //云信登录
            String imId = AccountDataService.getSingleAccountDataService(LoginActivity.this).getUserAccId();
            String imtoken = AccountDataService.getSingleAccountDataService(LoginActivity.this).getImToken();

            Log.e("云信登录", "imId:" + imId);
            Log.e("云信令牌", "imtoken:" + imtoken);

            AbortableFuture<LoginInfo> loginRequest;
            loginRequest = NIMClient.getService(AuthService.class).login(new LoginInfo(imId, imtoken));
            loginRequest.setCallback(new RequestCallback<LoginInfo>() {
                @Override
                public void onSuccess(LoginInfo param) {

                    dismissProcessBar();
                    Toast.makeText(LoginActivity.this, getResources().getString(R.string.loginSuccess), Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();

                }

                @Override
                public void onFailed(int code) {
                    dismissProcessBar();
                    Log.i("登录", "登录失败");
                    if (code == 302 || code == 404) {
                        Toast.makeText(LoginActivity.this, getResources().getString(R.string.accountPasswordError), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(LoginActivity.this, "login error: " + code, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onException(Throwable exception) {

                    dismissProcessBar();
                    Log.i("登录", "登录异常");
                }

            });


            //进行定位并发送定位数据
            MyApplication.startLocation(new MyApplication.LocationCallBack() {

                @Override
                public void locationSuccess(LocationData location) {

                    Log.i("登录location", "定位成功:" + location.toString());
                    new SendLocationRequest().request(null, String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude()));
                }

                @Override
                public void locationFailed(String message) {

                }
            });
        }

        @Override
        public void doAfterFailedResponse(String message) {
            dismissProcessBar();
            Toast.makeText(LoginActivity.this,  getResources().getString(R.string.accountPasswordError), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onErrorResponse(VolleyError volleyError) {
            dismissProcessBar();
            Toast.makeText(LoginActivity.this, VolleyErrorParser.parseVolleyError(volleyError), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Intent intent = new Intent(LoginActivity.this, ResetPasswordActivity.class);
            intent.putExtra("phone", data.getStringExtra("phone"));
            startActivity(intent);
        }
    }
}
