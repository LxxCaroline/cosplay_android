package com.think.linxuanxuan.ecos.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.think.linxuanxuan.ecos.R;
import com.think.linxuanxuan.ecos.adapter.ContactAdapter;
import com.think.linxuanxuan.ecos.database.ContactDBService;
import com.think.linxuanxuan.ecos.model.AccountDataService;
import com.think.linxuanxuan.ecos.model.Contact;
import com.think.linxuanxuan.ecos.model.ModelUtils;
import com.think.linxuanxuan.ecos.model.User;
import com.think.linxuanxuan.ecos.model.UserDataService;
import com.think.linxuanxuan.ecos.utils.NotifyUtils;
import com.think.linxuanxuan.ecos.views.sweet_alert_dialog.SweetAlertDialog;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.constant.MsgDirectionEnum;
import com.netease.nimlib.sdk.msg.constant.MsgStatusEnum;
import com.netease.nimlib.sdk.msg.constant.MsgTypeEnum;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.msg.model.RecentContact;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ContactActivity extends BaseActivity implements View.OnClickListener {
    private final String TAG = "Ecos---Contact";
    public static final String TargetUserID = "TargetUserID";
    public static final String TargetUserName = "TargetUserName";
    public static final String TargetUserAvatar = "TargetUserAvatar";
    public static final String TargetUserIMID = "TargetUserIMID";

    @InjectView(R.id.lly_right_action)
    LinearLayout title_right;
    @InjectView(R.id.tv_right_text)
    TextView title_right_text;
    @InjectView(R.id.tv_title)
    TextView title_text;
    @InjectView(R.id.lly_left_action)
    LinearLayout title_left;

    @InjectView(R.id.lv_list)
    ListView lv_list;
    @InjectView(R.id.tv_send)
    TextView tv_send;
    @InjectView(R.id.et_input)
    EditText et_input;

    /**
     * 对方userId
     */
    public String targetUserID;
    /**
     * 对方昵称
     */
    public String targetUserName;
    /**
     * 对方头像
     */
    public String targetUserAvatar;
    public String targetUserIMID = "";

    private List<IMMessage> messageList = new ArrayList<>();
    private ContactAdapter contactAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        ButterKnife.inject(this);

        initTitle();
        initListener();

//        Log.i("聊天界面","设置信息已读" + targetUserID);
        NIMClient.getService(MsgService.class).setChattingAccount(targetUserIMID, SessionTypeEnum.P2P);

        regeisterObserver();

        initData();

        //取消MainActivity全局监听
        if (MyApplication.msMainActivity!=null){
            MyApplication.msMainActivity.unregistObserver();
        }

        //取消通知栏对应通知
        NotifyUtils.cancelNotification(this, targetUserIMID);

    }

    @Override
    public void onNewIntent(Intent intent){
        super.onNewIntent(intent);

        Log.i("onNewIntent","intent:" + intent.toString());
        Bundle bundle = intent.getExtras();
        targetUserID = bundle.getString(TargetUserID);
        targetUserAvatar = bundle.getString(TargetUserAvatar);
        targetUserName = bundle.getString(TargetUserName);
        targetUserIMID = bundle.getString(TargetUserIMID);

        Log.i("onNewIntent","targetUserID" + targetUserID);
        Log.i("onNewIntent","targetUserAvatar" + targetUserAvatar);
        Log.i("onNewIntent","targetUserName" + targetUserName);

        title_text.setText(targetUserName);

        initData();
    }


    /***
     *
     */
    public void onResume(){
        super.onResume();

        //取消MainActivity全局监听
//        MyApplication.msMainActivity.unregistObserver();
        //取消通知栏对应通知
        NotifyUtils.cancelNotification(this, targetUserIMID);
    }


    private void initTitle() {
        title_left.setOnClickListener(this);
        title_right.setVisibility(View.INVISIBLE);
        title_text.setText("");

        try {
            Bundle bundle = getIntent().getExtras();
            targetUserID = bundle.getString(TargetUserID);
            targetUserAvatar = bundle.getString(TargetUserAvatar);
            targetUserName = bundle.getString(TargetUserName);
            targetUserIMID = bundle.getString(TargetUserIMID);
            Log.v("contact", "targetIMID--------   " + targetUserIMID);
            Log.v("contact", "targetID--------   " + targetUserID);
            Log.v("contact", "MyIMID--------   " + UserDataService.getSingleUserDataService(this).getUser().imId);
            Log.v("contact", "MyID--------   " + UserDataService.getSingleUserDataService(this).getUser().userId);
        } catch (Exception e) {
            e.printStackTrace();
            SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(ContactActivity.this, SweetAlertDialog.ERROR_TYPE);
            sweetAlertDialog.setTitle("错误");
            sweetAlertDialog.setContentText("错误的用户信息");
            sweetAlertDialog.setConfirmText("朕知道了");
            sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    finish();
                }
            });

//            Toast.makeText(ContactActivity.this, "Error Intent", Toast.LENGTH_SHORT).show();
            Log.v("contact", "targetIMID--------" + "Error Intent");
        }

//        if (TextUtils.isEmpty(targetUserID)) {
//            Toast.makeText(ContactActivity.this, "targetID为空", Toast.LENGTH_SHORT).show();
//        }
//        if (TextUtils.isEmpty(targetUserIMID)) {
//            Toast.makeText(ContactActivity.this, "targetIMID为空", Toast.LENGTH_SHORT).show();
//        }
//        if (TextUtils.isEmpty(targetUserName)) {
//            Toast.makeText(ContactActivity.this, "targetName为空", Toast.LENGTH_SHORT).show();
//        }
//        if (TextUtils.isEmpty(targetUserAvatar)) {
//            Toast.makeText(ContactActivity.this, "targetAvartar为空", Toast.LENGTH_SHORT).show();
//        }

        Log.v("contact", "targetID" + targetUserIMID);

        title_text.setText(targetUserName);

        String myImId = AccountDataService.getSingleAccountDataService(this).getUserAccId();


        Log.i("我的云信id1","------------------------" + myImId);
        String myImId2 =UserDataService.getSingleUserDataService(this).getUser().imId;
        Log.i("我的云信id2","---------------------------" + myImId2);


        String myImIdPlusContactImiId = myImId + targetUserIMID;


        List<Contact> contactList = ContactDBService.getInstance(ContactActivity.this).getContactList();

        if (contactList.size() == 0)
            Log.e("notification", "联系人列表为空");
        for (Contact contact : contactList) {
            Log.e("数据库读取", "contact: --" + contact.toString());
            if (contact.getMyImIdPlusContactImiId().equals(myImId)) {

                Log.d("id对比", "id一样----");
                Log.d("id对比", "id一样" + contact.getMyImIdPlusContactImiId());
                Log.d("id对比", "id一样" + myImId);
                Log.d("id对比", "id一样---");
            } else
                Log.d("id对比", "id一样");
        }

        ContactDBService.getInstance(this).resetUnreadNum(myImIdPlusContactImiId);

    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        NIMClient.getService(MsgServiceObserve.class)
                .observeReceiveMessage(incomingMessageObserver, false);

        NIMClient.getService(MsgService.class).setChattingAccount(MsgService.MSG_CHATTING_ACCOUNT_NONE, SessionTypeEnum.None);

        //最近联系人列表监听
        NIMClient.getService(MsgServiceObserve.class)
                .observeRecentContact(messageObserver, false);


        //开启MainActivity全局监听
        if(MyApplication.msMainActivity!=null){
            MyApplication.msMainActivity.registObserver();
        }
    }

    private void initListener() {
        tv_send.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lly_left_action:
                finish();
                break;
            case R.id.tv_send:
                if (TextUtils.isEmpty(et_input.getText().toString())) {
                    Toast.makeText(ContactActivity.this, "请输入聊天内容", Toast.LENGTH_SHORT).show();
                } else {
                    IMMessage message = MessageBuilder.createTextMessage(
                            targetUserIMID, // 聊天对象的ID，如果是单聊，为用户账号，如果是群聊，为群组ID
                            SessionTypeEnum.P2P, // 聊天类型，单聊或群组
                            getMessageJSON(et_input.getText().toString()) // 文本内容
                    );
                    NIMClient.getService(MsgService.class).sendMessage(message, false);
                }
                break;
        }
    }


    public static String getMessageJSON(String content) {
        User user = UserDataService.getSingleUserDataService(MyApplication.getContext()).getUser();
        Map<String, Object> messageMap = new HashMap<String, Object>();
        messageMap.put("userId", user.userId);
        messageMap.put("nickname", user.nickname);
        messageMap.put("avatarUrl", user.avatarUrl);
        messageMap.put("message", content);

        return new JSONObject(messageMap).toString();
    }

    public static String getMessageContentByJSONString(String jsonString) {

        try {
            JSONObject json = new JSONObject(jsonString);
            return json.has("message") && !json.isNull("message") ? json.getString("message") : jsonString;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("contact", "--------非json message-----------");
        return jsonString;
    }

    private void initData() {
        messageList = new ArrayList<IMMessage>();
        testMessageHistory(targetUserIMID);
    }

    private void initList() {
        contactAdapter = new ContactAdapter(this, messageList, targetUserIMID, targetUserAvatar);
        lv_list.setAdapter(contactAdapter);
        lv_list.setSelection(contactAdapter.getCount() + 1);
    }

    private void addList(IMMessage message) {
        if (contactAdapter == null) {
            initList();
        }
        contactAdapter.add(message);
        contactAdapter.notifyDataSetChanged();
        lv_list.setSelection(contactAdapter.getCount() + 1);
        Log.i("contact", "add：----" + message.getFromAccount());
    }


    private void regeisterObserver() {

        //注册接收信息监听器
        NIMClient.getService(MsgServiceObserve.class)
                .observeReceiveMessage(incomingMessageObserver, true);

        //发送消息状态监听器
        NIMClient.getService(MsgServiceObserve.class).observeMsgStatus(new Observer<IMMessage>() {

            public void onEvent(IMMessage message) {
                // 参数为有状态发生改变的消息对象，其msgStatus和attachStatus均为最新状态。
                // 发送消息和接收消息的状态监听均可以通过此接口完成。
                Log.i("发送消息状态回掉", "消息内容：----" + message.getContent());
                Log.i("发送消息状态回掉", "消息来自：----" + message.getFromAccount());
                Log.i("发送消息状态回掉", "消息接收：----" + message.getSessionId());
                Log.i("发送消息状态回掉", "会话类型：----" + message.getSessionType());
                Log.i("发送消息状态回掉", "消息类型：----" + message.getMsgType().name());
                Log.i("发送消息状态回掉", "消息状态：----" + message.getStatus());

                Log.i("发送消息状态回掉", "消息方向：----" + message.getDirect());
                Log.i("发送消息状态回掉", "当前是发出去:----" + message.getDirect().compareTo(MsgDirectionEnum.Out));
                Log.i("发送消息状态回掉", "当前是收到:----" + message.getDirect().compareTo(MsgDirectionEnum.In));
                Log.i("发送消息状态回掉", "消息类型：----" + message.getMsgType().name());

                /**Add**/
                if (message.getStatus() == MsgStatusEnum.success) {
                    addList(message);
                    et_input.setText("");
                } else {
                    Toast.makeText(ContactActivity.this, message.getStatus().toString(), Toast.LENGTH_SHORT).show();
                }

            }
        }, true);

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

                        /**Add**/
                        Toast.makeText(ContactActivity.this, "收到一条信息", Toast.LENGTH_SHORT).show();
                        String myImId = AccountDataService.getSingleAccountDataService(ContactActivity.this).getUserAccId();
                        //信息来自当前聊天对象或自己
                        if(message.getFromAccount().equals(targetUserIMID) || message.equals(myImId))
                        {
                            addList(message);
                        }
                        else{
                            Intent intent = new Intent();
                            intent.setClass(MyApplication.getContext(), ContactActivity.class);
                            Bundle bundle = new Bundle();

                            Contact contact = new Contact();

                            try {
                                JSONObject content = new JSONObject(message.getContent());
                                contact.contactNickName = content.getString("nickname");
                                contact.contactUserId = content.getString("userId");
                                contact.avatarUrl = content.getString("avatarUrl");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            contact.fromAccount = message.getFromAccount();
                            bundle.putString(ContactActivity.TargetUserID, contact.contactUserId);
                            bundle.putString(ContactActivity.TargetUserAvatar, contact.avatarUrl);
                            bundle.putString(ContactActivity.TargetUserName, contact.contactNickName);
                            bundle.putString(ContactActivity.TargetUserIMID, contact.fromAccount);
                            Log.v("mainActivity,收到信息", "targetIMID--------   " + contact.fromAccount);
                            Log.v("mainActivity,收到信息", "targetID--------   " + contact.contactUserId);
                            Log.v("mainActivity,收到信息", "targetAvatar--------   " + contact.avatarUrl);
                            intent.putExtras(bundle);
                            String ticker = contact.contactNickName + "发来信息";
                            String title = contact.contactNickName;
                            String content = contact.messageContent;
                            NotifyUtils.notifyMessage(MyApplication.getContext(), intent, ticker, title, content, contact.fromAccount);


                        }

                        Log.i("收到消息", "----------------------------------------------------------");
                        if (message.getMsgType().compareTo(MsgTypeEnum.text) == 0) {

                            Log.i("发送消息状态回掉", "消息内容：----" + message.getContent());
                            Log.i("发送消息状态回掉", "消息来自：----" + message.getFromAccount());
                            Log.i("发送消息状态回掉", "消息接收：----" + message.getSessionId());
                            Log.i("发送消息状态回掉", "会话类型：----" + message.getSessionType());
                            Log.i("发送消息状态回掉", "消息类型：----" + message.getMsgType().name());
                            Log.i("发送消息状态回掉", "消息状态：----" + message.getStatus());
                            Log.i("发送消息状态回掉", "时间：----" + ModelUtils.getDateDetailByTimeStamp(message.getTime()));

                            Log.i("发送消息状态回掉", "消息方向：----" + message.getDirect());
                            Log.i("发送消息状态回掉", "当前是发出去:----" + message.getDirect().compareTo(MsgDirectionEnum.Out));
                            Log.i("发送消息状态回掉", "当前是收到:----" + message.getDirect().compareTo(MsgDirectionEnum.In));
                            Log.i("发送消息状态回掉", "消息类型：----" + message.getMsgType().name());
                        }

                    }
                }
            };


    public void testMessageHistory(String toAccid) {

        IMMessage endMessage = MessageBuilder.createEmptyMessage(toAccid, SessionTypeEnum.P2P, 0);

        Log.i("contact--------", "history");

        NIMClient.getService(MsgService.class).pullMessageHistory(endMessage, 20, true)
                .setCallback(new RequestCallback<List<IMMessage>>() {
                    @Override
                    public void onSuccess(List<IMMessage> msgList) {

                        /**Add**/
                        messageList = msgList;
                        Collections.reverse(messageList);
                        initList();

                        Log.e("拉取信息", "拉去信息的条数" + msgList.size());

                        Log.e("历史信息", "聊天------");
                        for (int i = 0; i < messageList.size(); i++) {

                            IMMessage message = msgList.get(i);

//                          Log.e("历史记录", message.getFromAccount().equals("test1") ? "我：" : "  蓝天：");
                            Log.e("历史记录", message.getContent());
                            Log.e("历史记录", ModelUtils.getDateDetailByTimeStamp(message.getTime()));
                            Log.e("历史记录", ("\n"));
                            Log.e("历史记录", message.getFromAccount());
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


    Observer<List<RecentContact>> messageObserver =
            new Observer<List<RecentContact>>() {
                @Override
                public void onEvent(List<RecentContact> messages) {

                    for (RecentContact msg : messages) {

                        Contact contact = new Contact();
                        String myImId = AccountDataService.getSingleAccountDataService(ContactActivity.this).getUserAccId();

                        contact.setId(myImId, msg.getContactId());
                        contact.contactAccid = msg.getContactId();

                        //本人发起的
                        if (msg.getFromAccount().equals(myImId)) {
                            contact.contactNickName = targetUserName;
                            contact.contactUserId = targetUserID;
                            contact.avatarUrl = targetUserAvatar;
                        }
                        //对方发来的
                        else {
                            try {
                                JSONObject content = new JSONObject(msg.getContent());
                                contact.contactNickName = content.getString("nickname");
                                contact.contactUserId = content.getString("userId");
                                contact.avatarUrl = content.getString("avatarUrl");


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }


                        contact.fromAccount = msg.getFromAccount();
                        contact.messageContent = getMessageContentByJSONString(msg.getContent());
                        contact.messgeId = msg.getRecentMessageId();
                        contact.time = msg.getTime();
                        contact.unreadedNum = msg.getUnreadCount();

                        ContactDBService.getInstance(ContactActivity.this).addContact(contact);

                        Log.e("最近会话信息", "联系人id：" + msg.getContactId());
                        Log.e("最近会话信息", "会话内容：" + msg.getContent());
                        Log.e("最近会话信息", "会话来自账号：" + msg.getFromAccount());
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

                    List<Contact> contactList = ContactDBService.getInstance(ContactActivity.this).getContactList();
                    for (Contact contact : contactList) {
                        Log.e("数据库读取", "contact: --" + contact.toString());
                    }
                }
            };
}
