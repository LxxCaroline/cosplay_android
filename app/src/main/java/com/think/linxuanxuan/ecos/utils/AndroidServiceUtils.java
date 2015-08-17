package com.think.linxuanxuan.ecos.utils;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import java.util.List;

public class AndroidServiceUtils {

	public static final String TAG = "AndroidServiceUtils";
	 /**
	* 发送短信
	* @param phone 短信接收者的号码
	* @param smsBody 要发送的短信内容
	*/
	public static void sendSMS(Context context,String phone,String smsBody)
	{
		if(phone.equals(phone))
		{
			Log.e(TAG, "号码为空无法发送");
			return;
		}
		
		Uri smsToUri = Uri.parse("smsto:" + phone);

		Intent intent = new Intent(Intent.ACTION_SENDTO, smsToUri);

		intent.putExtra("sms_body", smsBody);

		context.startActivity(intent);

	}
	
	
	 /****
	  * 判断当前应用是否在前台运行
	  * @param context
	  * @return
	  */
	 public static boolean isTopActivity(Context context){
	        String packageName = "com.jianzhi.company";
	        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
	        List<RunningTaskInfo> tasksInfo = activityManager.getRunningTasks(1);
	        if(tasksInfo.size() > 0){  
	            System.out.println("---------------包名-----------"+tasksInfo.get(0).topActivity.getPackageName());
	            //应用程序位于堆栈的顶层  
	            if(packageName.equals(tasksInfo.get(0).topActivity.getPackageName())){  
	                return true;  
	            }  
	        }  
	        return false;
	    }
}
