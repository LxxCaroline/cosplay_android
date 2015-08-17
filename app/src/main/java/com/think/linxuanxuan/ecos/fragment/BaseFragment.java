package com.think.linxuanxuan.ecos.fragment;

import android.support.v4.app.Fragment;
import android.util.Log;

import com.think.linxuanxuan.ecos.R;
import com.think.linxuanxuan.ecos.views.sweet_alert_dialog.SweetAlertDialog;

/**
 * Created by Think on 2015/8/7.
 */
public class BaseFragment extends Fragment {

    private static final String TAG = "Ecos---BaseFragment";

    /**
     * 加载对话框
     */
    SweetAlertDialog mProgressDialog;

    /**
     * 初始化加载对话框
     */
    private void initProgressSweetDialog(String title) {

        if (mProgressDialog == null) {
            //mMyProgressDialog初始化
            mProgressDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE).setTitleText(title).setContentText(title);
            mProgressDialog.setCancelable(false);
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.bg_red));
        }
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
}
