package com.think.linxuanxuan.ecos.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.think.linxuanxuan.ecos.R;
import com.think.linxuanxuan.ecos.dialog.SetPhotoDialog;
import com.think.linxuanxuan.ecos.model.AccountDataService;
import com.think.linxuanxuan.ecos.model.InputLength;
import com.think.linxuanxuan.ecos.model.LocationData;
import com.think.linxuanxuan.ecos.request.BaseResponceImpl;
import com.think.linxuanxuan.ecos.request.NorResponce;
import com.think.linxuanxuan.ecos.request.VolleyErrorParser;
import com.think.linxuanxuan.ecos.request.user.LoginRequest;
import com.think.linxuanxuan.ecos.request.user.RegistRequest;
import com.think.linxuanxuan.ecos.request.user.SendLocationRequest;
import com.think.linxuanxuan.ecos.utils.SetPhotoHelper;
import com.think.linxuanxuan.ecos.utils.UploadImageTools;
import com.netease.nimlib.sdk.AbortableFuture;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.auth.LoginInfo;

import java.io.File;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by hzjixinyu on 2015/7/30.
 */
public class RegistActivity extends BaseActivity implements View.OnClickListener, TextWatcher {
    @InjectView(R.id.et_name)
    EditText et_name;
    @InjectView(R.id.et_password)
    EditText et_password;
    @InjectView(R.id.iv_avatar)
    ImageView iv_avatar;
    @InjectView(R.id.tv_complete)
    TextView tv_complete;
    @InjectView(R.id.iv_return)
    ImageView iv_return;
    @InjectView(R.id.logo)
    LinearLayout logo;
    @InjectView(R.id.main)
    LinearLayout main;


    public SetPhotoHelper mSetPhotoHelper;

    public boolean isSettingAvatart = false;

    public String mAvatarLocalPath = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);
        ButterKnife.inject(this);

        initListener();
        initData();

        iv_return.requestFocus();
    }

    private void initListener() {
        et_name.addTextChangedListener(this);
        et_password.addTextChangedListener(this);

        iv_avatar.setOnClickListener(this);
        tv_complete.setOnClickListener(this);
        iv_return.setOnClickListener(this);

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
    }


    private void initData() {
        mSetPhotoHelper = new SetPhotoHelper(this, null);
        //图片裁剪后输出宽度
        final int outPutWidth = 200;
        //图片裁剪后输出高度
        final int outPutHeight = 200;
        mSetPhotoHelper.setAspect(1, 1);
        mSetPhotoHelper.setOutput(outPutWidth, outPutHeight);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case SetPhotoHelper.REQUEST_BEFORE_CROP:
                    //当前是设置封面
                    if (isSettingAvatart) {
                        mSetPhotoHelper.setmSetPhotoCallBack(new SetPhotoHelper.SetPhotoCallBack() {

                            @Override
                            public void success(String imagePath) {
                                Log.i("裁剪后图片路径", "-----------path:" + imagePath);
                                mAvatarLocalPath = imagePath;
                                Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
                                iv_avatar.setImageBitmap(bitmap);
                                isSettingAvatart = false;

                            }
                        });
                        mSetPhotoHelper.handleActivityResult(SetPhotoHelper.REQUEST_BEFORE_CROP, data);
                        return;
                    }


                    break;
                case SetPhotoHelper.REQUEST_AFTER_CROP:
                    mSetPhotoHelper.handleActivityResult(SetPhotoHelper.REQUEST_AFTER_CROP, data);
                    break;
                default:
                    isSettingAvatart = false;
                    Log.e("CLASS_TAG", "onActivityResult() 无对应");
            }
        } else {
            isSettingAvatart = false;
            Log.e(CLASS_TAG, "操作取消");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_complete:
                if (et_name.getText().length() > InputLength.PersonName_max) {
                    Toast.makeText(RegistActivity.this, "昵称限制 " + InputLength.PersonName_max + " 字", Toast
                            .LENGTH_SHORT).show();
                    return;
                }

                showProcessBar("注册...");
                //先上传文件
                if (mAvatarLocalPath != null && !"".equals(mAvatarLocalPath)) {
                    UploadImageTools.uploadImageSys(new File(mAvatarLocalPath), new UploadImageTools.UploadCallBack() {

                        @Override
                        public void success(String originUrl, String thumbUrl) {
                            Log.e(TAG, "图片url:" + originUrl);

                            RegistRequest request = new RegistRequest();
                            request.request(new RegistResponse(), getIntent().getStringExtra("phone"), et_password
                                            .getText().toString(), et_name.getText().toString(),
                                    originUrl);
                        }

                        @Override
                        public void fail() {
                            dismissProcessBar();
                        }

                        @Override
                        public void onProcess(Object fileParam, long current, long total) {

                        }
                    }, RegistActivity.this, false);
                } else {
                    RegistRequest request = new RegistRequest();
                    request.request(new RegistResponse(), getIntent().getStringExtra("phone"),
                            et_password.getText().toString(), et_name.getText().toString(), "");
                }

                break;
            case R.id.iv_avatar:
                //TODO 选择头像

                SetPhotoDialog dialog = new SetPhotoDialog(RegistActivity.this,
                        new SetPhotoDialog.ISetPhoto() {

                            @Override
                            public void choosePhotoFromLocal() {
                                Toast.makeText(RegistActivity.this, "选择本地图片", Toast.LENGTH_LONG).show();
                                mSetPhotoHelper.choosePhotoFromLocal();
                                isSettingAvatart = true;
                            }

                            @Override
                            public void takePhoto() {
                                Toast.makeText(RegistActivity.this, "拍照", Toast.LENGTH_LONG).show();
                                mSetPhotoHelper.takePhoto(true);
                                isSettingAvatart = true;

                            }
                        });
                dialog.showSetPhotoDialog();

                break;
            case R.id.iv_return:
                finish();
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (et_password.getText().toString().length() >= InputLength.Password_min && et_password.getText().toString()
                .length() <= InputLength.Password_max && !TextUtils.isEmpty(et_name.getText().toString())) {
            tv_complete.setEnabled(true);
        } else {
            tv_complete.setEnabled(false);
        }
    }


    @Override
    public void afterTextChanged(Editable s) {
    }

    class RegistResponse extends BaseResponceImpl implements RegistRequest.IRegistResponse {

        @Override
        public void success(String phone, String pwd) {

            LoginRequest request = new LoginRequest();
            request.request(new LoginResponse(), phone, pwd);
            Toast.makeText(RegistActivity.this, getResources().getString(R.string.registerSuccess), Toast
                    .LENGTH_LONG).show();

            /*Toast.makeText(RegistActivity.this, "注册成功", Toast.LENGTH_LONG).show();
            //云信登录
            loginYunXin();

            //进行定位并发送定位数据
            MyApplication.startLocation(new MyApplication.LocationCallBack() {

                @Override
                public void locationSuccess(LocationData location) {

                    Log.i("注册location","定位成功:" + location.toString());
                    new SendLocationRequest().request(null, String.valueOf(location.getLatitude()), String.valueOf
                    (location.getLongitude()));
                }

                @Override
                public void locationFailed(String message) {

                }
            });

            Intent intent = new Intent(RegistActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
            dismissProcessBar();*/

        }

        @Override
        public void doAfterFailedResponse(String message) {
            dismissProcessBar();
            Toast.makeText(RegistActivity.this, "注册失败", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onErrorResponse(VolleyError volleyError) {
            dismissProcessBar();
            Toast.makeText(RegistActivity.this, VolleyErrorParser.parseVolleyError(volleyError), Toast.LENGTH_LONG)
                    .show();

        }
    }

    /*public void loginYunXin(){

        String imId = AccountDataService.getSingleAccountDataService(RegistActivity.this).getUserAccId();
        String imtoken = AccountDataService.getSingleAccountDataService(RegistActivity.this).getImToken();

        Log.e("云信登录","imId:" + imId);
        Log.e("云信令牌","imtoken:" + imtoken);

        AbortableFuture<LoginInfo> loginRequest;
        loginRequest = NIMClient.getService(AuthService.class).login(new LoginInfo(imId, imtoken));
        loginRequest.setCallback(new RequestCallback<LoginInfo>() {
            @Override
            public void onSuccess(LoginInfo param) {
                Log.e("注册-登录云信", "登录成功");
            }

            @Override
            public void onFailed(int code) {
                Log.i("登录", "登录失败");
                if (code == 302 || code == 404) {
                    Log.e("注册-登录云信", "帐号或密码错误");
                } else {
                    Log.e("注册-登录云信","login error: " + code);
                }
            }

            @Override
            public void onException(Throwable exception) {
                Log.e("注册-登录云信", "登录异常");
            }

        });
    }*/


    class LoginResponse extends BaseResponceImpl implements NorResponce {

        @Override
        public void success() {

            //云信登录
            String imId = AccountDataService.getSingleAccountDataService(RegistActivity.this).getUserAccId();
            String imtoken = AccountDataService.getSingleAccountDataService(RegistActivity.this).getImToken();

            Log.e("云信登录", "imId:" + imId);
            Log.e("云信令牌", "imtoken:" + imtoken);

            AbortableFuture<LoginInfo> loginRequest;
            loginRequest = NIMClient.getService(AuthService.class).login(new LoginInfo(imId, imtoken));
            loginRequest.setCallback(new RequestCallback<LoginInfo>() {
                @Override
                public void onSuccess(LoginInfo param) {

                    dismissProcessBar();
                    Toast.makeText(RegistActivity.this, getResources().getString(R.string.loginSuccess), Toast
                            .LENGTH_SHORT).show();

                    Intent intent = new Intent(RegistActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();

                }

                @Override
                public void onFailed(int code) {
                    dismissProcessBar();
                    Log.i("登录", "登录失败");
                    if (code == 302 || code == 404) {
                        Toast.makeText(RegistActivity.this, getResources().getString(R.string.accountPasswordError),
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(RegistActivity.this, "login error: " + code, Toast.LENGTH_SHORT).show();
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
                    new SendLocationRequest().request(null, String.valueOf(location.getLatitude()), String.valueOf
                            (location.getLongitude()));
                }

                @Override
                public void locationFailed(String message) {

                }
            });
        }

        @Override
        public void doAfterFailedResponse(String message) {
            dismissProcessBar();
            Toast.makeText(RegistActivity.this, getResources().getString(R.string.accountPasswordError), Toast
                    .LENGTH_SHORT).show();
        }

        @Override
        public void onErrorResponse(VolleyError volleyError) {
            dismissProcessBar();
            Toast.makeText(RegistActivity.this, VolleyErrorParser.parseVolleyError(volleyError), Toast.LENGTH_SHORT).show();
        }
    }
}
