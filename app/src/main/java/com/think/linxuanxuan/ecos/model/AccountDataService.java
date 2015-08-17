package com.think.linxuanxuan.ecos.model;

import android.content.Context;
import android.content.SharedPreferences;

/***
 *
 * @ClassName: AccountDataService
 * @Description: TODO(管理帐号信息，保罗)
 * @author enlizhang
 * @date 2015年1月26日 下午2:20:42
 *
 */
public class AccountDataService {

	private final static String TAG = "AccountDataService";

	private final String PREFERENCE_NAME = "AccountData";
	private final int READ_MODE = Context.MODE_WORLD_READABLE;
	private final int WRITE_MODE = Context.MODE_WORLD_WRITEABLE;

	/****
	 * 存储{@link User#userId}
	 */
	private final static String USER_ID = "userId";

	/****
	 * 存储云信id{@link User#imId}
	 */
	private final static String USER_ACCID = "accid";

	/***
	 * 云信token
	 */
	private final static String IM_TOKEN = "imToken";

	/****
	 * 存储token
	 */
	private final static String TOKEN = "token";

	/*** 验证码 */
//	private final static String AUTOCODE = "autocode";

	/*** 验证码 */
	private final static String COOKIE = "cookie";

	private final static String PHONE = "phone";

	private String DEFAULT_VALUE = "";

	private static AccountDataService singleAccountDataService = null;

	private  static Context mContext;


	private AccountDataService(Context context)
	{
		mContext = context;
	}

	/***
	 * 返回AccountDataService类单例操作对象
	 * @param context 若mContext为null，则根据context进行创建，此时必须保证context!=null.<br>
	 * 					否则，context不进行使用
	 * @return
	 */
	public static AccountDataService getSingleAccountDataService(Context context) {
		if(singleAccountDataService == null && mContext==null)
		{
			singleAccountDataService = new AccountDataService(context);
		}
		return singleAccountDataService;
	}

	/***
	 * 保存userid
	 * @param userId 用户id
	 */
	public void saveUserId(String userId)
	{
		SharedPreferences sharedPreferences = mContext.getSharedPreferences(PREFERENCE_NAME,WRITE_MODE);
		SharedPreferences.Editor editor = sharedPreferences.edit();

		editor.putString(USER_ID, userId);
		editor.commit();
	}


	/***
	 * 保存云信accid
	 * @param accid 用户id
	 */
	public void saveUserAccId(String accid)
	{
		SharedPreferences sharedPreferences = mContext.getSharedPreferences(PREFERENCE_NAME,WRITE_MODE);
		SharedPreferences.Editor editor = sharedPreferences.edit();

		editor.putString(USER_ACCID, accid);
		editor.commit();
	}

	/***
	 * 保存云信token
	 * @param imToken 云信token
	 */
	public void saveUserImToken(String imToken)
	{
		SharedPreferences sharedPreferences = mContext.getSharedPreferences(PREFERENCE_NAME,WRITE_MODE);
		SharedPreferences.Editor editor = sharedPreferences.edit();

		editor.putString(IM_TOKEN, imToken);
		editor.commit();
	}

	/***
	 * 保存token
	 * @param token 请求令牌
	 */
	public void saveToken(String token)
	{
		SharedPreferences sharedPreferences = mContext.getSharedPreferences(PREFERENCE_NAME,WRITE_MODE);
		SharedPreferences.Editor editor = sharedPreferences.edit();

		editor.putString(TOKEN, token);
		editor.commit();
	}




	/***
	 * 保存验证码
	 * @param registValidateCode 注册验证码
	 */
	/*public void saveAutocode(String autocode)
	{
		SharedPreferences sharedPreferences = mContext.getSharedPreferences(PREFERENCE_NAME,WRITE_MODE);
		SharedPreferences.Editor editor = sharedPreferences.edit();

		editor.putString(AUTOCODE, autocode);
		editor.commit();
	}*/


	/***
	 * 保存验证码 cookie
	 * @param cookie 验证码cookie
	 */
	public void saveAutocodeCookie(String cookie)
	{
		SharedPreferences sharedPreferences = mContext.getSharedPreferences(PREFERENCE_NAME,WRITE_MODE);
		SharedPreferences.Editor editor = sharedPreferences.edit();

		editor.putString(COOKIE, cookie);
		editor.commit();
	}

	/***
	 * 获取验证码 cookie
	 */
	public String getAutocodeCookie()
	{
		SharedPreferences sharedPreferences = mContext.getSharedPreferences(PREFERENCE_NAME,READ_MODE);

		return sharedPreferences.getString(COOKIE, "");
	}


	/****
	 * 获取当前用户userId
	 * @return 若无则返回null
	 */
	public String getUserId()
	{
		SharedPreferences sharedPreferences = mContext.getSharedPreferences(PREFERENCE_NAME,READ_MODE);

		return sharedPreferences.getString(USER_ID, "");
	}


	/****
	 * 获取当前用户云信accid
	 * @return 若无则返回null
	 */
	public String getUserAccId()
	{
		SharedPreferences sharedPreferences = mContext.getSharedPreferences(PREFERENCE_NAME,READ_MODE);

		return sharedPreferences.getString(USER_ACCID, null);
	}

	/****
	 * 获取当前用户云信token
	 * @return 若无则返回null
	 */
	public String getImToken()
	{
		SharedPreferences sharedPreferences = mContext.getSharedPreferences(PREFERENCE_NAME,READ_MODE);

		return sharedPreferences.getString(IM_TOKEN, null);
	}

	/****
	 * 获取token
	 * @return 若无则返回null
	 */
	public String getToken()
	{
		SharedPreferences sharedPreferences = mContext.getSharedPreferences(PREFERENCE_NAME,READ_MODE);

		return sharedPreferences.getString(TOKEN, "");
	}

	/****
	 * 获取验证码
	 * @return 若无则返回"-1"
	 */
	/*public String getAutoCode()
	{
		SharedPreferences sharedPreferences = mContext.getSharedPreferences(PREFERENCE_NAME,READ_MODE);
		return sharedPreferences.getString(AUTOCODE, "");
	}*/

	/**
	 * 清除所有数据
	 */
	public void clearAllDataExceptUsername()
	{
		SharedPreferences sharedPreferences = mContext.getSharedPreferences(PREFERENCE_NAME,WRITE_MODE);
		SharedPreferences.Editor editor = sharedPreferences.edit();

		editor.putString(USER_ID,null);
		editor.putString(USER_ACCID,null);
		editor.putString(IM_TOKEN,null);

		editor.commit();
	}


	/***
	 * 保存电话
	 * @param phone 电话
	 */
	public void savePhone(String phone)
	{
		SharedPreferences sharedPreferences = mContext.getSharedPreferences(PREFERENCE_NAME,WRITE_MODE);
		SharedPreferences.Editor editor = sharedPreferences.edit();

		editor.putString(PHONE, phone);
		editor.commit();
	}

	/***
	 * 获取phone.返回号码
	 */
	public String getPhone()
	{
		SharedPreferences sharedPreferences = mContext.getSharedPreferences(PREFERENCE_NAME,READ_MODE);

		return sharedPreferences.getString(PHONE, "");
	}

}

