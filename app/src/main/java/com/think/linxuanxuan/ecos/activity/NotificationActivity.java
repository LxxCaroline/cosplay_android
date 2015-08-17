package com.think.linxuanxuan.ecos.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.think.linxuanxuan.ecos.R;
import com.think.linxuanxuan.ecos.adapter.NotificationContactAdapter;
import com.think.linxuanxuan.ecos.database.ContactDBService;
import com.think.linxuanxuan.ecos.model.AccountDataService;
import com.think.linxuanxuan.ecos.model.Contact;
import com.think.linxuanxuan.ecos.model.ModelUtils;
import com.think.linxuanxuan.ecos.model.UserDataService;
import com.think.linxuanxuan.ecos.views.XListView;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.attachment.ImageAttachment;
import com.netease.nimlib.sdk.msg.constant.MsgDirectionEnum;
import com.netease.nimlib.sdk.msg.constant.MsgTypeEnum;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.msg.model.RecentContact;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by hzjixinyu on 2015/7/29.
 */
public class NotificationActivity extends BaseActivity implements View.OnClickListener {

    @InjectView(R.id.lly_right_action)
    LinearLayout title_right;
    @InjectView(R.id.tv_right_text)
    TextView title_right_text;
    @InjectView(R.id.tv_title)
    TextView title_text;
    @InjectView(R.id.lly_left_action)
    LinearLayout title_left;

    //    @InjectView(R.id.tv_notice)
//    TextView tv_notice; //通知
    @InjectView(R.id.lv_list)
    ListView lv_list; //显示列表
    @InjectView(R.id.resultImageView)
    ImageView resultImageView;

    private List<Contact> contactList = new ArrayList<Contact>();

    private NotificationContactAdapter contactAdapter; //私信Adapter

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        ButterKnife.inject(this);

        regeisterObserver();

        NIMClient.getService(MsgService.class).setChattingAccount(MsgService.MSG_CHATTING_ACCOUNT_ALL, SessionTypeEnum.None);

        initTitle();
        initListener();
        initData();

        Log.v("contact", "MyID" + UserDataService.getSingleUserDataService(this).getUser().imId);

        //取消MainActivity全局监听
        if (MyApplication.msMainActivity!=null){
            MyApplication.msMainActivity.unregistObserver();
        }

    }

    private void initTitle() {
        title_left.setOnClickListener(this);
        title_right.setVisibility(View.INVISIBLE);
        title_right_text.setText("");
        title_text.setText("我的私信");
    }

    @Override
    public void onResume() {
        super.onResume();

        Log.i("联系人列表", "联系人列表刷新");
        contactList = ContactDBService.getInstance(NotificationActivity.this).getContactList();

        if (contactList.size() == 0)
            Log.e("notification", "联系人列表为空");
        for (Contact contact : contactList) {
            Log.e("数据库读取", "contact: --" + contact.toString());
        }

        contactAdapter = new NotificationContactAdapter(this, contactList);
        lv_list.setAdapter(contactAdapter);

        checkBlock();
    }

    private void checkBlock() {
        if (lv_list.getCount()==0){
            resultImageView.setVisibility(View.VISIBLE);
        }else {
            resultImageView.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();


        //注册接收信息监听器
        NIMClient.getService(MsgServiceObserve.class)
                .observeReceiveMessage(incomingMessageObserver, false);

        //最近联系人列表监听
        NIMClient.getService(MsgServiceObserve.class)
                .observeRecentContact(messageObserver, false);

        NIMClient.getService(MsgService.class).setChattingAccount(MsgService.MSG_CHATTING_ACCOUNT_NONE, SessionTypeEnum.None);
        //开启MainActivity全局监听
        if (MyApplication.msMainActivity!=null){
            MyApplication.msMainActivity.registObserver();
        }
    }

    private void initListener() {
        lv_list.setDividerHeight(0);
    }

    private void initData() {
//        contactAdapter=new NotificationContactAdapter(this);
//        lv_list.setAdapter(contactAdapter);

        contactList = ContactDBService.getInstance(NotificationActivity.this).getContactList();

        if (contactList.size() == 0)
            Log.e("notification", "联系人列表为空");
        for (Contact contact : contactList) {
            Log.e("数据库读取", "contact: --" + contact.toString());
        }

        contactAdapter = new NotificationContactAdapter(this, contactList);
        lv_list.setAdapter(contactAdapter);
        checkBlock();

    }

    private void freshData() {
        contactAdapter = new NotificationContactAdapter(this, contactList);
        lv_list.setAdapter(contactAdapter);
        checkBlock();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lly_left_action:
                finish();
                break;
        }
    }


    /**
     * |||||||||||||
     * ---------------------------------------------------yunxing api test----------------------------------------
     * |||||||||||
     */

    private void regeisterObserver() {

        //发送消息状态监听器
        NIMClient.getService(MsgServiceObserve.class).observeMsgStatus(new Observer<IMMessage>() {

            public void onEvent(IMMessage message) {
                // 参数为有状态发生改变的消息对象，其msgStatus和attachStatus均为最新状态。
                // 发送消息和接收消息的状态监听均可以通过此接口完成。
                Log.i("发送消息状态回掉", "消息内容：" + message.getContent());
                Log.i("发送消息状态回掉", "消息来自：" + message.getFromAccount());
                Log.i("发送消息状态回掉", "消息接收：" + message.getSessionId());
                Log.i("发送消息状态回掉", "会话类型：" + message.getSessionType());
                Log.i("发送消息状态回掉", "消息类型：" + message.getMsgType().name());
                Log.i("发送消息状态回掉", "消息状态：" + message.getStatus());

                Log.i("发送消息状态回掉", "消息方向：" + message.getDirect());
                Log.i("发送消息状态回掉", "当前是发出去:" + message.getDirect().compareTo(MsgDirectionEnum.Out));
                Log.i("发送消息状态回掉", "当前是收到:" + message.getDirect().compareTo(MsgDirectionEnum.In));
                Log.i("发送消息状态回掉", "消息类型：" + message.getMsgType().name());

            }
        }
                , true);


        //注册接收信息监听器
        NIMClient.getService(MsgServiceObserve.class)
                .observeReceiveMessage(incomingMessageObserver, true);

        //最近联系人列表监听
        NIMClient.getService(MsgServiceObserve.class)
                .observeRecentContact(messageObserver, true);


    }

    //信息收听接收器
    Observer<List<IMMessage>> incomingMessageObserver =
            new Observer<List<IMMessage>>() {
                @Override
                public void onEvent(List<IMMessage> messages) {

                    for (IMMessage message : messages) {

                        Log.i("收到消息", "----------------------------------------------------------");
                        if (message.getMsgType().compareTo(MsgTypeEnum.text) == 0) {
//                            ((TextView)findViewById(R.id.tv_received_text)).setText(message.getContent());

                            Log.i("发送消息状态回掉", "消息内容：" + message.getContent());
                            Log.i("发送消息状态回掉", "消息来自：" + message.getFromAccount());
                            Log.i("发送消息状态回掉", "消息接收：" + message.getSessionId());
                            Log.i("发送消息状态回掉", "会话类型：" + message.getSessionType());
                            Log.i("发送消息状态回掉", "消息类型：" + message.getMsgType().name());
                            Log.i("发送消息状态回掉", "消息状态：" + message.getStatus());
                            Log.i("发送消息状态回掉", "时间：" + ModelUtils.getDateDetailByTimeStamp(message.getTime()));

                            Log.i("发送消息状态回掉", "消息方向：" + message.getDirect());
                            Log.i("发送消息状态回掉", "当前是发出去:" + message.getDirect().compareTo(MsgDirectionEnum.Out));
                            Log.i("发送消息状态回掉", "当前是收到:" + message.getDirect().compareTo(MsgDirectionEnum.In));
                            Log.i("发送消息状态回掉", "消息类型：" + message.getMsgType().name());
                        }
                        if (message.getMsgType().compareTo(MsgTypeEnum.image) == 0) {

                            ImageAttachment imageAttachment = (ImageAttachment) message.getAttachment();

                            Log.v("收到的图片信息", "文件名" + imageAttachment.getFileName());
                            Log.v("收到的图片信息", "显示名称" + imageAttachment.getDisplayName());
                            Log.v("收到的图片信息", "图片扩扩展名" + imageAttachment.getExtension());
                            Log.v("收到的图片信息", "图片路径" + imageAttachment.getPath());
                            Log.v("收到的图片信息", "图片存储路径" + imageAttachment.getPathForSave());
                            Log.v("收到的图片信息", "图片缩略图路径" + imageAttachment.getThumbPath());
                            Log.v("收到的图片信息", "图片缩略图存储路径" + imageAttachment.getThumbPathForSave());
                            Log.v("收到的图片信息", "图片宽度" + imageAttachment.getWidth());
                            Log.v("收到的图片信息", "图片高度" + imageAttachment.getHeight());
                            Log.v("收到的图片信息", "图片url" + imageAttachment.getUrl());

                            File srcFile = new File(imageAttachment.getPathForSave());
                            System.out.println("原图图片父路径:" + srcFile.getParent());
                            if (srcFile.exists()) {
                                Bitmap bitmap = BitmapFactory.decodeFile(imageAttachment.getPathForSave());
                                BitmapFactory.Options options = new BitmapFactory.Options();
                                options.inJustDecodeBounds = true;
                                BitmapFactory.decodeFile(imageAttachment.getPathForSave(), options);
                                Log.e("展示图片", "drawable原图宽度:" + options.outWidth);
                                Log.e("展示图片", "drawable原图高度:" + options.outHeight);
                                ImageView iv_received_origin_image = (ImageView) findViewById(R.id.iv_received_origin_image);
                                iv_received_origin_image.setImageBitmap(bitmap);
                            } else {
                                Log.e("图片打开错误", "原图不在");
                            }

                            File thumbFile = new File(imageAttachment.getThumbPathForSave());
                            System.out.println("缩略图图片父路径:" + thumbFile.getParent());
                            if (thumbFile.exists()) {
                                Bitmap bitmap = BitmapFactory.decodeFile(imageAttachment.getThumbPathForSave());
                                BitmapFactory.Options options = new BitmapFactory.Options();
                                options.inJustDecodeBounds = true;
                                BitmapFactory.decodeFile(imageAttachment.getThumbPathForSave(), options);
                                Log.e("展示图片", "drawable缩略图宽度:" + options.outWidth);
                                Log.e("展示图片", "drawable缩略图高度:" + options.outHeight);

                                ImageView iv_received_thumb_image = (ImageView) findViewById(R.id.iv_received_thumb_image);
                                iv_received_thumb_image.setImageBitmap(bitmap);
                            } else {
                                Log.e("图片打开错误", "缩略图不在");
                            }
                            /*BitmapDrawable drawable = new BitmapDrawable(getResources(), imageAttachment.getPathForSave());
                            BitmapFactory.Options options = new BitmapFactory.Options();
			                options.inJustDecodeBounds = true;
			                BitmapFactory.decodeFile(imageAttachment.getPathForSave(), options);
			        		Log.e("展示图片", "drawable原图宽度:" + options.outWidth);
			        		Log.e("展示图片", "drawable原图高度:" + options.outHeight);

			        		ImageView iv_received_origin_image = (ImageView) findViewById(R.id.iv_received_origin_image);
			        		iv_received_origin_image.setImageDrawable(drawable);*/

			        		/*Log.e("展示图片", "" );

			        		BitmapDrawable drawable2 = new BitmapDrawable(getResources(), imageAttachment.getThumbPath());
			        		options = new BitmapFactory.Options();
			                options.inJustDecodeBounds = true;
			                BitmapFactory.decodeFile(imageAttachment.getPathForSave(), options);
			                Log.e("展示图片", "drawable缩略图宽度:" + options.outWidth);
			        		Log.e("展示图片", "drawable缩略图高度:" + options.outHeight);

			        		ImageView iv_received_thumb_image = (ImageView) findViewById(R.id.iv_received_thumb_image);
			        		iv_received_thumb_image.setImageDrawable(drawable2);*/


                        }
                    }
                }
            };

    Observer<List<RecentContact>> messageObserver =
            new Observer<List<RecentContact>>() {
                @Override
                public void onEvent(List<RecentContact> messages) {

                    for (RecentContact msg : messages) {

                        Contact contact = new Contact();
                        String myImId = AccountDataService.getSingleAccountDataService(NotificationActivity.this).getUserAccId();

                        contact.setId(myImId, msg.getContactId());
                        contact.contactAccid = msg.getContactId();

                        try {

                            JSONObject content = new JSONObject(msg.getContent());
                            contact.contactNickName = content.getString("nickname");
                            contact.contactUserId = content.getString("userId");
                            contact.avatarUrl = content.getString("avatarUrl");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        contact.fromAccount = msg.getFromAccount();
                        contact.messageContent = ContactActivity.getMessageContentByJSONString(msg.getContent());
                        contact.messgeId = msg.getRecentMessageId();
                        contact.time = msg.getTime();
                        contact.unreadedNum = msg.getUnreadCount();

                        ContactDBService.getInstance(NotificationActivity.this).addContact(contact);


                        Log.e("最近会话信息", "联系人id：" + msg.getContactId());
                        Log.e("最近会话信息", "会话内容：" + msg.getContent());
                        Log.e("最近会话信息", "会话账号：" + msg.getFromAccount());
                        Log.e("最近会话信息", "messageId：" + msg.getRecentMessageId());
                        Log.e("最近会话信息", "时间：" + ModelUtils.getDateDetailByTimeStamp(msg.getTime()));
                        Log.e("最近会话信息", "未读数：" + msg.getUnreadCount());
                        Log.e("最近会话信息", "信息状态：" + msg.getMsgStatus());
                        Log.e("最近会话信息", "会话类型：" + (msg.getSessionType() == SessionTypeEnum.Team ? "群聊" : "单聊"));
                        Log.i("最近会话信息", "----------------------------------------");
                            /*NIMClient.getService(MsgService.class).setChattingAccount(
	    	                		msg.getContactId(),
	    	                	    SessionTypeEnum.P2P
	    	                	    );*/
                    }

                    contactList = ContactDBService.getInstance(NotificationActivity.this).getContactList();
                    checkBlock();
                    /**Add**/
                    freshData();
                    for (Contact contact : contactList) {
                        Log.e("数据库读取", "contact: --" + contact.toString());
                    }

//                        tv_message_history.setText("");

//                    testMessageHistory(MyUserIMID);
                }
            };


    public void testMessageHistory(String accid) {

        IMMessage endMessage = MessageBuilder.createEmptyMessage(accid, SessionTypeEnum.P2P, 0);

        NIMClient.getService(MsgService.class).pullMessageHistory(endMessage, 80, true)
                .setCallback(new RequestCallback<List<IMMessage>>() {
                    @Override
                    public void onSuccess(List<IMMessage> msgList) {

                        Log.e("拉取信息", "拉去信息的条数" + msgList.size());

                        Log.e("历史信息", "聊天------");
                        for (int i = 0; i < msgList.size(); i++) {

                            IMMessage message = msgList.get(i);

                            Log.e("历史记录", message.getFromAccount().equals("test1") ? "我：" : "  蓝天：");
                            Log.e("历史记录", message.getContent());
                            Log.e("历史记录", ModelUtils.getDateDetailByTimeStamp(message.getTime()));
                            Log.e("历史记录", ("\n"));

                        }

                    }

                    @Override
                    public void onFailed(int code) {

                        Log.e("拉取信息", "拉取失败");
                    }

                    @Override
                    public void onException(Throwable exception) {
                        Log.e("拉取信息", "拉取异常");
                    }
                });
    }
}
