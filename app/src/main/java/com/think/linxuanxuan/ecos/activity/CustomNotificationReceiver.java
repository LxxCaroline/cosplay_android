package com.think.linxuanxuan.ecos.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.netease.cloud.nos.android.utils.LogUtil;
import com.netease.nimlib.sdk.NimIntent;
import com.netease.nimlib.sdk.msg.model.CustomNotification;

/**
 * 自定义通知消息广播接收器
 */
public class CustomNotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("CustomNotificationReceiver","CustomNotificationReceiver");


        String action = context.getPackageName() + NimIntent.ACTION_RECEIVE_CUSTOM_NOTIFICATION;
        if (action.equals(intent.getAction())) {

            // 从intent中取出自定义通知
            CustomNotification notification = (CustomNotification) intent.getSerializableExtra(NimIntent.EXTRA_BROADCAST_MSG);
            try {
                JSONObject obj = JSONObject.parseObject(notification.getContent());
                if (obj != null && obj.getInteger("id") == 2) {
                    // Toast
                    String content = obj.getString("content");
                    String tip = String.format("自定义消息[%s]：%s", notification.getFromAccount(), content);
                    Toast.makeText(context, tip, Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                LogUtil.e("demo", e.getMessage());
            }

            // 处理自定义通知消息
            LogUtil.i("demo", "receive custom notification: " + notification.getContent() + " from :" + notification.getSessionId() + "/" + notification.getSessionType());
        }
    }
}
