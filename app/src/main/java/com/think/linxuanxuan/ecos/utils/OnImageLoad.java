package com.think.linxuanxuan.ecos.utils;

import android.graphics.Bitmap;

/**
 * 图片异步成功读入后的回调接口
 * @author yanbin
 *
 */
public interface OnImageLoad {
	/**
	 * 实现数据更新
	 * @param bitmap 待显示图像
	 * @param path 图像path，用于寻找待显示的ImageView
	 */
	void onLoadSucc(Bitmap bitmap, String path);
}
