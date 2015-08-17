package com.think.linxuanxuan.ecos.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.think.linxuanxuan.ecos.R;
import com.think.linxuanxuan.ecos.model.ConfigurationService;

import java.util.HashMap;
import java.util.Map;

/**
 * 类描述：
 * Created by enlizhang on 2015/8/11.
 */
public class NotifyUtils {


    /**
     * *
     * 存储设置过的通知，键为通知对应的id,值为通知对应的imid,后续根据imid进行状态栏的管理(通知移除).
     */
    private static Map<Integer, String> mNotifyMap = new HashMap<Integer, String>();

    private static int mNotifyId = 0;

    /**
     * 设置通知
     *
     * @param context
     * @param intent       包含点击通知后启动activity所需的信息
     * @param ticker
     * @param contentTitle 通知标题
     * @param contentText  通知内容
     * @param fromAccount  通知标签，用于后续移除状态栏中的通知，
     */
    public static void notifyMessage(Context context, Intent intent,
                                     String ticker, String contentTitle, String contentText, String fromAccount) {

        Log.e("tag", "notifyJobstaffMessage:");

        //如果是设置了不提醒只显示数目的群组(这个是app里保存这个数据的，demo里不做判断)
        //以及设置了setShowNotificationInbackgroup:false(设为false后，后台时sdk也发送广播)

        int notifiId = mNotifyId++;


        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_notification)
                .setWhen(System.currentTimeMillis()).setAutoCancel(true);

        //设置状态栏提示
        mBuilder.setTicker(ticker).setContentTitle(contentTitle).setContentText(contentText);


        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        mBuilder.setContentIntent(pendingIntent);
        Notification notification = mBuilder.build();

        ConfigurationService service = ConfigurationService.getConfigurationService(context);
        //若通知开启震动提示
        if (service.isNotificationVibrator())
            notification.defaults = notification.defaults | Notification.DEFAULT_VIBRATE;
        //若通知开启声音提示
        if (service.isNotificationSound())
            notification.defaults = notification.defaults | Notification.DEFAULT_SOUND;


        notification.tickerText = ticker;
        notification.when = System.currentTimeMillis();

        notificationManager.notify(notifiId, notification);

        mNotifyMap.put(notifiId, fromAccount);
    }

    /**
     * 根据tag移除通知栏的通知
     *
     * @param context
     */
    public static void cancelNotification(Context context, String fromAccount) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        for (int notifyId : mNotifyMap.keySet()) {

            if (mNotifyMap.get(notifyId).equals(fromAccount)) {
                Log.e("取消通知", "取消通知," + "id:" + notifyId + "对方云信id:" + fromAccount);
                notificationManager.cancel(notifyId);
            }
        }
    }


    /**
     * *
     * 取消所有通知，特别是在退出账号时，取消所有通知。
     *
     * @param context
     */
    public static void cancelAllNotification(Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.cancelAll();
    }
}
