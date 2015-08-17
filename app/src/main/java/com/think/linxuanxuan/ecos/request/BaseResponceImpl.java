package com.think.linxuanxuan.ecos.request;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import com.think.linxuanxuan.ecos.activity.LoginActivity;
import com.think.linxuanxuan.ecos.activity.MyApplication;
import com.think.linxuanxuan.ecos.model.AccountDataService;
import com.think.linxuanxuan.ecos.views.sweet_alert_dialog.SweetAlertDialog;

public abstract class BaseResponceImpl implements IBaseResponse{

	public String MESSAEE_LOGIN_AGAIN = "无操作权限，请重新登陆";
	public String MESSAEE_OTHER_ERROR = "请过段时间再进行操作";
	
	private final static String TAG = "BaseResponceImpl";
	
	private Context mContext;
	
	public BaseResponceImpl()
	{
		mContext = MyApplication.getContext();
	}
	
	public BaseResponceImpl(Context context)
	{
		mContext = context;
	}
	
	/****
	 * 当前token失效，账号已经在其他地方登录
	 */
	@Override
	public void responseNoGrant()
	{
		AccountDataService.getSingleAccountDataService(mContext).clearAllDataExceptUsername();

		SweetAlertDialog dialog = new SweetAlertDialog(MyApplication.getCurrentActivity(), SweetAlertDialog.NORMAL_TYPE);
		dialog.setTitleText("提醒");
		dialog.setContentText("账号在其他地方登录，请重新登录");
		dialog.setCancelable(true);
		dialog.setCanceledOnTouchOutside(true);
		dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {

			@Override
			public void onDismiss(DialogInterface dialog) {
				Intent intent = new Intent();
				intent.setClass(MyApplication.getCurrentActivity(), LoginActivity.class);

				//清除所有activity后开启LoginActivity
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
				MyApplication.getCurrentActivity().startActivity(intent);
				MyApplication.getCurrentActivity().finish();
			}

		});
		dialog.show();
	}
	
}
