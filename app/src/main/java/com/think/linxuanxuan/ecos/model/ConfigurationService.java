package com.think.linxuanxuan.ecos.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class ConfigurationService {

	private final static String TAG = "ConfigurationService";
	
	private final String PREFERENCE_NAME = "ConfigurationService";
	
	private final int READ_MODE = Context.MODE_WORLD_READABLE;
	private final int WRITE_MODE = Context.MODE_WORLD_WRITEABLE;
	
	
	/*** 省分份数据是否加载过，true:加载过；false:未加载过。 只需加载一次 */
	private final static String IS_PROVINCE_DATA_DOWNLOADED = "ispRrovinceDataDownloaded";

	/*** 城市数据是否加载过，true:加载过；false:未加载过。 只需加载一次 */
	private final static String IS_CITY_DATA_DOWNLOADED = "isCityDataDownloaded";
	
	/*** 是否有新报名 */
	private final static String IS_THERE_NEW_APPLICATION = "isThereNewApplication";
	
	private static ConfigurationService singleConfigurationService = null;
	
	/*** 个推clientId */
	private final static String GETUI_CLIENT_ID = "clientId";
	
	
	
	/*** 通知是否开启震动,true：是，false：否 */
	private final static String IS_NOTIFTACTION_VIBRATOR = "isNotificationVibrator";
	
	/*** 通知震动模式默认为开启 */
	private final static boolean DEFAULT_NOTIFTACTION_VIBRATOR = true;
	
	/*** 通知是否开启声音,true：是，false：否 */
	private final static String IS_NOTIFTACTION_SOUND = "isNotificationSound";
	
	/*** 通知声音模式默认为开启 */
	private final static boolean DEFAULT_NOTIFTACTION_SOUND = true;
	
	/*** 新报名微信通知,true：是，false：否 */
	private final static String IS_WEIXIN_NOTIFTACTION = "isWeixinNotify";
	
	/*** 新报名微信通知模式默认为关闭 */
	private final static boolean DEFAULTWEIXIN_NOTIFTACTION = false;
	
	private  static Context mContext;
	
	
	private ConfigurationService(Context context)
	{
		mContext = context;
	}
	
	/***
	 * 返回ConfigurationService类单例操作对象
	 * @param context 若mContext为null，则根据context进行创建，此时必须保证context!=null.<br>
	 * 					否则，context不进行使用
	 * @return
	 */
	public static ConfigurationService getConfigurationService(Context context) {
		if(singleConfigurationService == null && mContext==null)
		{
			singleConfigurationService = new ConfigurationService(context);
		}
		return singleConfigurationService;
	}
	
	
	/***
	 * 标志省数据已经加载过了
	 */
	public void setProvinceDataDownloaded()
	{
		SharedPreferences sharedPreferences = mContext.getSharedPreferences(PREFERENCE_NAME,WRITE_MODE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		
		editor.putBoolean(IS_PROVINCE_DATA_DOWNLOADED, true);
		editor.commit();
	}
	
	
	/****
	 * 省市数据是否已经加载
	 * @return true 已经加载；false 为加载；
	 */
	public boolean getProvinceDataDownloaded()
	{
		SharedPreferences sharedPreferences = mContext.getSharedPreferences(PREFERENCE_NAME,READ_MODE);
		Log.e(TAG, "identity:"+sharedPreferences.getBoolean(IS_PROVINCE_DATA_DOWNLOADED, false));
		
		return sharedPreferences.getBoolean(IS_PROVINCE_DATA_DOWNLOADED, false);
	}

	/***
	 * 标志城市数据已经加载过了
	 */
	public void setCityDataDownloaded()
	{
		SharedPreferences sharedPreferences = mContext.getSharedPreferences(PREFERENCE_NAME,WRITE_MODE);
		SharedPreferences.Editor editor = sharedPreferences.edit();

		editor.putBoolean(IS_CITY_DATA_DOWNLOADED, true);
		editor.commit();
	}


	/****
	 * 城市市数据是否已经加载
	 * @return true 已经加载；false 为加载；
	 */
	public boolean getCityDataDownloaded()
	{
		SharedPreferences sharedPreferences = mContext.getSharedPreferences(PREFERENCE_NAME,READ_MODE);
		Log.e(TAG, "identity:"+sharedPreferences.getBoolean(IS_CITY_DATA_DOWNLOADED, false));

		return sharedPreferences.getBoolean(IS_CITY_DATA_DOWNLOADED, false);
	}
	
	

	/***
	 * 设置现在有新报名
	 */
	public void setExistNewApplication()
	{
		SharedPreferences sharedPreferences = mContext.getSharedPreferences(PREFERENCE_NAME,WRITE_MODE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		
		editor.putBoolean(IS_THERE_NEW_APPLICATION, true);
		editor.commit();
	}
	
	/***
	 * 是否有新报名
	 * @return true,有新报名;false，无新报名；
	 */
	public boolean isThereNewApplication()
	{
		SharedPreferences sharedPreferences = mContext.getSharedPreferences(PREFERENCE_NAME,READ_MODE);
		Log.e(TAG, "identity:"+sharedPreferences.getBoolean(IS_THERE_NEW_APPLICATION, false));
		
		return sharedPreferences.getBoolean(IS_THERE_NEW_APPLICATION, false);
	}
	
	/***
	 * 设置目前无未新报名
	 */
	public void resetNewApplicationState()
	{
		SharedPreferences sharedPreferences = mContext.getSharedPreferences(PREFERENCE_NAME,WRITE_MODE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		
		editor.putBoolean(IS_THERE_NEW_APPLICATION, false);
		editor.commit();
	}
	
	
	public void saveClientId(String clientId){
		SharedPreferences sharedPreferences = mContext.getSharedPreferences(PREFERENCE_NAME,WRITE_MODE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		
		editor.putString(GETUI_CLIENT_ID, clientId);
		editor.commit();
	}
	
	
	public String getClientId(){
		SharedPreferences sharedPreferences = mContext.getSharedPreferences(PREFERENCE_NAME,READ_MODE);
		return sharedPreferences.getString(GETUI_CLIENT_ID, "-1");
	}
	
	public boolean isClientIdValid(){
		SharedPreferences sharedPreferences = mContext.getSharedPreferences(PREFERENCE_NAME,READ_MODE);
		
		return !sharedPreferences.getString(GETUI_CLIENT_ID, "-1").equals("-1");
	}
	
	/****
	 * 设置通知震动模式
	 * @param isVibrator 通知时是否震动，true：是， false:否
	 */
	public void setNotificationVibrator(boolean isVibrator){
		SharedPreferences sharedPreferences = mContext.getSharedPreferences(PREFERENCE_NAME,WRITE_MODE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		
		Log.i(TAG, "震动模式" + isVibrator);
		editor.putBoolean(IS_NOTIFTACTION_VIBRATOR, isVibrator);
		editor.commit();
	}
	
	/***
	 * 获取通知震动模式,默认为震动
	 * @return true:震动 false:不震动
	 */
	public boolean isNotificationVibrator(){
		
		SharedPreferences sharedPreferences = mContext.getSharedPreferences(PREFERENCE_NAME,READ_MODE);
		
		return sharedPreferences.getBoolean(IS_NOTIFTACTION_VIBRATOR, DEFAULT_NOTIFTACTION_VIBRATOR);
	}
	
	
	/****
	 * 设置通知声音模式
	 * @param isSound 通知时是否播放声音，true：是， false:否
	 */
	public void setNotificationSound(boolean isSound){
		SharedPreferences sharedPreferences = mContext.getSharedPreferences(PREFERENCE_NAME,WRITE_MODE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		Log.i(TAG, "声音模式" + isSound);
		editor.putBoolean(IS_NOTIFTACTION_SOUND, isSound);
		editor.commit();
	}
	
	/***
	 * 获取通知震动模式，默认为震动
	 * @return true:震动 false:不震动
	 */
	public boolean isNotificationSound(){
		
		SharedPreferences sharedPreferences = mContext.getSharedPreferences(PREFERENCE_NAME,READ_MODE);
		
		return sharedPreferences.getBoolean(IS_NOTIFTACTION_SOUND, DEFAULT_NOTIFTACTION_SOUND);
	}
	
	/****
	 * 设置新消息微信通知模式
	 */
	public void setWeixinNotification(boolean isWeixinNotified){
		SharedPreferences sharedPreferences = mContext.getSharedPreferences(PREFERENCE_NAME,WRITE_MODE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		Log.i(TAG, "微信通知模式" + isWeixinNotified);
		editor.putBoolean(IS_WEIXIN_NOTIFTACTION, isWeixinNotified);
		editor.commit();
	}
	
	/***
	 * 获取新消息微信通知模式
	 * @return true:通知 ，false:不通知
	 */
	public boolean isWeixinNotified(){
		
		SharedPreferences sharedPreferences = mContext.getSharedPreferences(PREFERENCE_NAME,READ_MODE);
		
		return sharedPreferences.getBoolean(IS_WEIXIN_NOTIFTACTION, DEFAULTWEIXIN_NOTIFTACTION);
	}
	
}
