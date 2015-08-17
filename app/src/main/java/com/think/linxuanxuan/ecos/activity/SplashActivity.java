package com.think.linxuanxuan.ecos.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.think.linxuanxuan.ecos.R;
import com.think.linxuanxuan.ecos.model.AccountDataService;
import com.think.linxuanxuan.ecos.model.ConfigurationService;
import com.think.linxuanxuan.ecos.model.LocationData;
import com.think.linxuanxuan.ecos.request.VolleyErrorParser;
import com.think.linxuanxuan.ecos.request.initial.GetCityListRequest;
import com.think.linxuanxuan.ecos.request.initial.GetProvinceListRequest;
import com.think.linxuanxuan.ecos.request.initial.InitialRequest;
import com.think.linxuanxuan.ecos.request.user.SendLocationRequest;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by hzjixinyu on 2015/7/30.
 */
public class SplashActivity extends BaseActivity implements View.OnClickListener {

    @InjectView(R.id.tv_login)
    TextView tv_login;
    @InjectView(R.id.tv_regist)
    TextView tv_regist;

    private boolean initialDataLoaded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aplash);
        ButterKnife.inject(this);

        if (!isNetworkConnected(SplashActivity.this))
            Toast.makeText(SplashActivity.this, getResources().getString(R.string.noNetwork), Toast.LENGTH_LONG).show();
        initListener();
        initData();

    }

    private void startMainActivity() {
        startActivity(new Intent(SplashActivity.this, MainActivity.class));
        finish();
    }

    private void initListener() {
        tv_login.setOnClickListener(this);
        tv_regist.setOnClickListener(this);
    }

    private void initData() {

        if (!isInitialDataLoaded()) {
            Log.i(TAG, "请求初始数据");
            requestInitialData();
        } else {
            if (isLogined()) {

                //进行定位并发送定位数据
                MyApplication.startLocation(new MyApplication.LocationCallBack() {

                    @Override
                    public void locationSuccess(LocationData location) {

                        Log.i("开屏页-》主页面location", "定位成功:" + location.toString());
                        new SendLocationRequest().request(null, String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude()));
                    }

                    @Override
                    public void locationFailed(String message) {

                    }
                });


                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
            } else {
                //展示登录和注册
                showLoginAndRegist();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_login:
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                break;
            case R.id.tv_regist:
                startActivityForResult(new Intent(SplashActivity.this, VerifyCodeActivity.class), 0);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Intent intent = new Intent(SplashActivity.this, RegistActivity.class);
            intent.putExtra("phone", data.getStringExtra("phone"));
            startActivity(intent);
        }
    }

    List<InitialRequest> mInitialRequestList;

    /**
     * 从服务器请求基本的数据
     */
    private void requestInitialData() {

        mInitialRequestList = new ArrayList<InitialRequest>();

        if (!ConfigurationService.getConfigurationService(this).getProvinceDataDownloaded()) {
            GetProvinceListRequest getProvinceListRequest = new GetProvinceListRequest();
            mInitialRequestList.add(getProvinceListRequest);
            getProvinceListRequest.requestCityList(responseCallBack);

        } else {
            Log.i(TAG, "省市数据已经加载过了");
        }

        if (!ConfigurationService.getConfigurationService(this).getCityDataDownloaded()) {
            GetCityListRequest getCitysRequest = new GetCityListRequest();
            mInitialRequestList.add(getCitysRequest);
            getCitysRequest.requestCityList(responseCallBack);

        } else {
            Log.i(TAG, "省市数据已经加载过了");
        }


        //无基础数据需要加载
        if (mInitialRequestList.size() == 0) {
            initialDataLoaded = true;
        }
    }


    /**
     * 请求成功后的操作。检查是否其他请求都已经完成，若已经完成则进行登陆
     */
    public void doAfterInitialResponse() {
        if (mInitialRequestList == null)
            Toast.makeText(this, "mInitialRequestList == null", Toast.LENGTH_LONG).show();

        //若有请求未结束，则不进行后续操作
        for (InitialRequest request : mInitialRequestList) {
            if (!request.isFinished())
                return;
        }

        //若有请求未成功，则显示对话框提示相应信息
        for (InitialRequest request : mInitialRequestList) {
            if (!request.isSuccess()) {
                Toast.makeText(this, VolleyErrorParser.parseVolleyError(request.getError()), Toast.LENGTH_LONG).show();
                return;
            }
        }

        initialDataLoaded = true;

        showLoginAndRegist();


    }

    /**
     * 获取配置信息响应请求结束回调接口
     *
     * @author enlizhang
     *         2014.11.28
     */
    public interface InitialInfoResponse {
        public void doAfterResponse();

    }

    /**
     * 请求结束后的回调接口
     */
    InitialInfoResponse responseCallBack = new InitialInfoResponse() {
        @Override
        public void doAfterResponse() {
            doAfterInitialResponse();
        }
    };

    /**
     * 基础数据是否加载过
     *
     * @return
     */
    public boolean isInitialDataLoaded() {
        if (ConfigurationService.getConfigurationService(this).getProvinceDataDownloaded()
                && ConfigurationService.getConfigurationService(this).getCityDataDownloaded()) {
            return true;
        }

        return false;
    }


    /**
     * 展示登录和注册
     */
    public void showLoginAndRegist() {

        tv_login.setVisibility(View.VISIBLE);
        tv_regist.setVisibility(View.VISIBLE);

    }


    /**
     * 是否已经登陆过
     *
     * @return
     */
    public boolean isLogined() {
        //如果已经登录过则直接登陆
        AccountDataService accountService = AccountDataService.getSingleAccountDataService(this);
        String token = accountService.getToken();
        String imId = accountService.getUserAccId();
        String imToken = accountService.getImToken();
        //若存在token则直接登录
        if (token != null && imId != null && imToken != null &&
                !"".equals(token) && !"".equals(imId) && !"".equals(imToken)) {

            return true;
        } else {
            Log.i(TAG, "token：" + token);
            Log.i(TAG, "imId：" + imId);
            Log.i(TAG, "imToken：" + imToken);
            Log.i(TAG, "未登录");
            return false;
        }
    }
}
