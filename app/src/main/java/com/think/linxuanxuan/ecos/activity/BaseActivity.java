package com.think.linxuanxuan.ecos.activity;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Display;

import com.think.linxuanxuan.ecos.R;
import com.think.linxuanxuan.ecos.views.sweet_alert_dialog.SweetAlertDialog;


/**
 * Activity基类，所有activity必须继承该方法
 * Created by enlizhang on 2015/7/15.
 */
public class BaseActivity extends ActionBarActivity {

    protected static String TAG = "BaseActivity";


    public static int DisplayWidth;
    public static int DisplayHeight;


    /**
     * 提示对话框
     */
    public static SweetAlertDialog mAlertDialog;

    /**
     * 加载对话框
     */
    SweetAlertDialog mProgressDialog;

    protected String CLASS_TAG;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);

        CLASS_TAG = getClass().getSimpleName();

        //设置当前activity
        MyApplication.setCurrentActivity(this);
        Display display = getWindowManager().getDefaultDisplay();
        DisplayWidth = display.getWidth();
        DisplayHeight = display.getHeight();

        //隐藏ActionBar
        getSupportActionBar().hide();

        //初始化提示对话框

    }


    @Override
    protected void onResume() {
        super.onResume();

        //设置当前activity
        MyApplication.setCurrentActivity(this);

        //初始化提示对话框
        initAlertSweetDialog();

    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    /**
     * 初始化加载对话框
     */
    private void initProgressSweetDialog(String title) {

        if (mProgressDialog == null) {
            //mMyProgressDialog初始化
            mProgressDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE).setTitleText(title).setContentText(title);
            mProgressDialog.setCancelable(false);
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.bg_red));
        }
    }

    /**
     * 初始化提示对话框
     */
    private void initAlertSweetDialog() {
        mAlertDialog = new SweetAlertDialog(this, SweetAlertDialog.NORMAL_TYPE);
        mAlertDialog.setCancelable(true);
        mAlertDialog.setCanceledOnTouchOutside(true);
    }


    /**
     * 显示加载框
     */
    public void showProcessBar(String title) {
        Log.i(TAG, "显示加载框");
        initProgressSweetDialog(title);

        //如果加载框存在并未未显示，则进行显示
        if (!mProgressDialog.isShowing()) {
            mProgressDialog.show();
        }
    }

    /**
     * 移除界面上的加载框
     */
    public void dismissProcessBar() {
        Log.i(TAG, "销毁加载框");
        //如果加载框存在并显示，则进行销毁
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }

    }

//    /***
//     * 移除界面上的成功提示框
//     */
//    public void dismissSuccessBar()
//    {
//        Log.i(TAG, "销毁成功提示框");
//        //如果加载框存在并显示，则进行销毁
//        if(mSuccessDialog != null && mSuccessDialog.isShowing())
//        {
//            mSuccessDialog.dismiss();
//        }
//
//    }


    /***
     * 显示成功提示
     *
     */
//    public void showSuccessBar(String title, String content,SweetAlertDialog.OnSweetClickListener listener)
//    {
//        Log.i(TAG, "显示成功提示框");
//        initSuccessSweetDialog(title, content,listener);
//
//        //如果加载框存在并未未显示，则进行显示
//        if( !mSuccessDialog.isShowing())
//        {
//            mSuccessDialog.show();
//        }
//    }


    /**
     * 获取可用的提示对话框。
     * (由于提示对话框在app中比较常见，而不局限于特定activity，因此我将其写这里，大家也可以自己创建)
     *
     * @return
     */
    public static SweetAlertDialog getAlertDialog() {
        Log.i(TAG, "获取对话框");

        return mAlertDialog;
    }

    /**
     * 回收视图资源，例如关闭未关闭对话框
     */
    public synchronized void recycle() {

        if (mAlertDialog != null && mAlertDialog.isShowing()) {
            mAlertDialog.dismiss();
        }

        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            dismissProcessBar();
        }
    }


    /**
     * 检测网络是否可用
     *
     * @param context
     * @return true:是 false:否
     */
    public boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }
}
