package com.think.linxuanxuan.ecos.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.think.linxuanxuan.ecos.R;
import com.think.linxuanxuan.ecos.R.id;
import com.think.linxuanxuan.ecos.database.ContactDBService;
import com.think.linxuanxuan.ecos.model.Contact;
import com.think.linxuanxuan.ecos.model.ModelUtils;
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

import java.io.File;
import java.util.List;

public class YunXinApiActivity extends Activity {

    EditText etv_accid;

    EditText etv_content;

    TextView tv_message_history;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yunxin_api_test_layout);

        etv_accid = (EditText) findViewById(id.etv_accid);
        etv_content = (EditText) findViewById(id.etv_content);
        tv_message_history = (TextView) findViewById(id.tv_message_history);

        findViewById(id.btn_send_message).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                testSendTextMessage();
            }
        });

        findViewById(id.btn_send_image).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                testSendImageMessage();
            }
        });


        regeisterObserver();


    }

    private void regeisterObserver() {

        //发送消息状态监听器
        /*NIMClient.getService(MsgServiceObserve.class).observeMsgStatus(new Observer<IMMessage>(){
			
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
		, true);*/


        //注册接收信息监听器
        NIMClient.getService(MsgServiceObserve.class)
                .observeReceiveMessage(incomingMessageObserver, true);

        //最近联系人列表监听
        Observer<List<RecentContact>> messageObserver =
                new Observer<List<RecentContact>>() {
                    @Override
                    public void onEvent(List<RecentContact> messages) {

                        for (RecentContact msg : messages) {

                            Contact contact = new Contact();
                            contact.contactAccid = msg.getContactId();
                            contact.fromAccount = msg.getFromAccount();
                            contact.messageContent = msg.getContent();
                            contact.messgeId = msg.getRecentMessageId();
                            contact.time = msg.getTime();
                            contact.unreadedNum = msg.getUnreadCount();
                            ContactDBService.getInstance(YunXinApiActivity.this).addContact(contact);

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

                        List<Contact> contactList = ContactDBService.getInstance(YunXinApiActivity.this).getContactList();
                        for (Contact contact : contactList) {
                            Log.e("数据库读取", "contact: --" + contact.toString());
                        }

                        tv_message_history.setText("");

                        testMessageHistory("test2");
                    }
                };
        //  注册/注销观察者
        NIMClient.getService(MsgServiceObserve.class)
                .observeRecentContact(messageObserver, true);

    }


    public void testSendTextMessage() {
        String sessionId = etv_accid.getText().toString();
        String content = etv_content.getText().toString();

        IMMessage message = MessageBuilder.createTextMessage(
                sessionId, // 聊天对象的ID，如果是单聊，为用户账号，如果是群聊，为群组ID
                SessionTypeEnum.P2P, // 聊天类型，单聊或群组
                content // 文本内容
        );

        NIMClient.getService(MsgService.class).sendMessage(message, false);

    }

    public void testSendImageMessage() {
        String sessionId = etv_accid.getText().toString();
        String content = etv_content.getText().toString();
        File file = new File("/storage/emulated/0/com.think.linxuanxuan.ecos/nim/thumb/188939b191018f57f3f7ef3a4ace6fd4");
        IMMessage message = MessageBuilder.createImageMessage(
                sessionId, // 聊天对象的ID，如果是单聊，为用户账号，如果是群聊，为群组ID
                SessionTypeEnum.P2P, // 聊天类型，单聊或群组
                file,// 图片文件对象
                "图片哦" // 文件显示名字，如果第三方APP不关注，可以为null
        );

        NIMClient.getService(MsgService.class).sendMessage(message, false);
    }

    //信息收听接收器
    Observer<List<IMMessage>> incomingMessageObserver =
            new Observer<List<IMMessage>>() {
                @Override
                public void onEvent(List<IMMessage> messages) {

                    for (IMMessage message : messages) {

                        Log.i("收到消息", "----------------------------------------------------------");
                        if (message.getMsgType().compareTo(MsgTypeEnum.text) == 0) {
                            ((TextView) findViewById(id.tv_received_text)).setText(message.getContent());

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
                                ImageView iv_received_origin_image = (ImageView) findViewById(id.iv_received_origin_image);
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

                                ImageView iv_received_thumb_image = (ImageView) findViewById(id.iv_received_thumb_image);
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

    public void testMessageHistory(String accid) {

        IMMessage endMessage = MessageBuilder.createEmptyMessage(accid, SessionTypeEnum.P2P, 0);

        NIMClient.getService(MsgService.class).pullMessageHistory(endMessage, 80, true)
                .setCallback(new RequestCallback<List<IMMessage>>() {
                    @Override
                    public void onSuccess(List<IMMessage> msgList) {

                        Log.e("拉取信息", "拉去信息的条数" + msgList.size());

                        Log.e("历史信息", "聊天------");
           	 /*for(int i=0;i<10;i++){
           		 
           		IMMessage message = msgList.get(i);
           		
           		tv_message_history.append(message.getFromAccount().equals("test1")?"我：":"  蓝天：");
           		tv_message_history.append(message.getContent());
           		tv_message_history.append(ModelUtils.getDateDetailByTimeStamp(message.getTime()));
           		tv_message_history.append("\n");
           		
           	 }*/

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
