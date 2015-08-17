package com.think.linxuanxuan.ecos.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;

/**
 * 该类封装了一些小工具
 */

public class Util {
	private static Util util;
	public static int flag = 0;	// 正在运行的异步线程个数
	private Util(){
		
	}
	
	public static Util getInstance(){
		if(util == null){
			util = new Util();
		}
		return util;
	}
	
	/**
	 * 判断是否有sdcard
	 * @return
	 */
	public boolean hasSDCard(){
		boolean b = false;
		if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
			b = true;
		}
		return b;
	}
	
	/**
	 * 得到sdcard路径
	 * @return
	 */
	public String getExtPath(){
		String path = "";
		if(hasSDCard()){
			path = Environment.getExternalStorageDirectory().getPath();
		}
		return path;
	}
	
	/**
	 * 得到/data/data/package包(yanbin.imagelazyload) 目录
	 * @param mActivity
	 * @return
	 */
	public String getPackagePath(Activity mActivity){
		return mActivity.getFilesDir().toString();
	}

	/**
	 * 根据path得到图片名
	 * @param url
	 * @return
	 */
	public String getImageName(String url) {
		String imageName = "";
		if(url != null){
			imageName = url.substring(url.lastIndexOf("/") + 1);
		}
		return imageName;
	}

	/**
	 * 设置View的Margins值
	 * @param v
	 * @param left
	 * @param top
	 * @param right
	 * @param button
	 */
	public static void setMargins (View v, int left, int top, int right, int button) {
		if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
			ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
			p.setMargins(left, top, right, button);
			v.requestLayout();
		}
	}

	/**
	 * dp转px
	 * @param context
	 * @param dpValue
	 * @return
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}
}
