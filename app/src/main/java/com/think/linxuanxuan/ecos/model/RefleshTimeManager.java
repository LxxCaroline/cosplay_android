package com.think.linxuanxuan.ecos.model;
import android.content.Context;

import android.content.SharedPreferences;

/***
 * 列表刷新时间管理类
 */
public class RefleshTimeManager {

	private final static String TAG = "RefleshTimeManager";

	private final static String PREFERENCE_NAME = "RefleshTimeManager";
	private final static int READ_MODE = Context.MODE_WORLD_READABLE;
	private final static int WRITE_MODE = Context.MODE_WORLD_WRITEABLE;


	private final static String NEW_APPLY_REFLESH_TIME = "newApplyRefleshTime";

	private Context mContext;

	/*** 存储的键值 */
	private String mName;

	public RefleshTimeManager(Context context, String _name)
	{
		mContext = context;
		mName = _name;
	}

	/***
	 * 根据{@link #mName}更新刷新时间
	 * @param time 新的刷新时间
	 */
	public void updateRefleshTime(Long time)
	{
		SharedPreferences sharedPreferences = mContext.getSharedPreferences(PREFERENCE_NAME,WRITE_MODE);
		SharedPreferences.Editor editor = sharedPreferences.edit();

		editor.putLong(mName, time);
		editor.commit();

	}

	/***
	 * 鏍规嵁{@link #mName}鑾峰彇涓婃鍒锋柊鏃堕棿
	 * @return
	 */
	public Long getRefleshTime()
	{
		SharedPreferences sharedPreferences = mContext.getSharedPreferences(PREFERENCE_NAME,READ_MODE);
		
		return sharedPreferences.getLong(mName, 0);
	}
	
	public static void clearAll(Context currentContext)
	{
		SharedPreferences sharedPreferences = currentContext.getSharedPreferences(PREFERENCE_NAME,WRITE_MODE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		
		editor.clear();
		editor.commit();
	}
}
