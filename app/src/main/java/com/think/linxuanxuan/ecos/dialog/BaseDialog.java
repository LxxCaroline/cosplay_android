package com.think.linxuanxuan.ecos.dialog;

import android.content.Context;

public class BaseDialog {

	private final Context mContext;

	/*** 对话框是否是铺满屏幕的*/
	private boolean isDialogFullScreen;
	
	public BaseDialog(Context context)
	{
		mContext = context;
	}

	
	/***
	 * 当前对话框是否可用
	 * @return true，对话框仍可用；false，不可用
	 */
	public boolean isAvailable()
	{
		return (mContext == null);
	}

	
	public void setDialogFullScreen(boolean isDialogFullScreen) {
		this.isDialogFullScreen = isDialogFullScreen;
	}

	public Context getContext() {
		return mContext;
	}
	
	
	
}
