package com.think.linxuanxuan.ecos.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.think.linxuanxuan.ecos.R;
import com.think.linxuanxuan.ecos.database.ContactDBService;
import com.think.linxuanxuan.ecos.fragment.CommunityFragment;
import com.think.linxuanxuan.ecos.fragment.CourseFragment;
import com.think.linxuanxuan.ecos.fragment.DisplayFragment;
import com.think.linxuanxuan.ecos.fragment.NavigationDrawerFragment;
import com.think.linxuanxuan.ecos.fragment.TransactionFragment;
import com.think.linxuanxuan.ecos.model.AccountDataService;
import com.think.linxuanxuan.ecos.model.Contact;
import com.think.linxuanxuan.ecos.model.User;
import com.think.linxuanxuan.ecos.model.UserDataService;
import com.think.linxuanxuan.ecos.utils.NotifyUtils;
import com.think.linxuanxuan.ecos.utils.RoundImageView;
import com.think.linxuanxuan.ecos.utils.SDImageCache;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.model.RecentContact;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class MainActivity extends BaseActivity implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    private final String TAG = "Ecos---Main";

    @InjectView(R.id.btn_open)
    RoundImageView btn_open;
    @InjectView(R.id.radio_group)
    RadioGroup mRadioGroup;
    @InjectView(R.id.pager)
    ViewPager mViewPager;
    @InjectView(R.id.iv_tag1)
    ImageView iv_tag1;
    @InjectView(R.id.iv_tag2)
    ImageView iv_tag2;
    @InjectView(R.id.iv_tag3)
    ImageView iv_tag3;
    @InjectView(R.id.iv_tag4)
    ImageView iv_tag4;


    /**
     * 教程tab下标
     */
    public static final int TAB_COURSE_INDEX = 0;

    /**
     * 分享tab下标
     */
    public static final int TAB_DISPLAY_INDEX = 1;

    /**
     * 活动tab下标
     */
    public static final int TAB_COMMUCITY_INDEX = 2;

    /**
     * 招募tab下标
     */
    public static final int TAB_TRANSACTION_INDEX = 3;


    /**
     * mFragments[0]对应{@link CourseFragment}
     * mFragments[1]对应{@link CommunityFragment}
     * mFragments[2]对应{@link TransactionFragment}
     */
    private Fragment mFragments[] = new Fragment[4];

    /**
     * 当前所处的tab
     */
    private int mCurrentTab = 0;
    /**
     * {@link #mViewPager}适配器
     */
    private TabFragmentPagerAdapter mPagerAdapter;

    /**
     * 侧滑栏
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * 用户信息
     */
    private UserDataService mUserDataService;
    private User mUserData;

    /**
     * for netWorkImageView
     *
     * @param savedInstanceState
     */
    //for NetWorkImageView
    static ImageLoader.ImageCache imageCache;
    ImageLoader imageLoader;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MyApplication.msMainActivity = this;

        //注解工具初始化
        ButterKnife.inject(this);
        //隐藏ActionBar
        getSupportActionBar().hide();
        initViews();
        if (!isNetworkConnected(MainActivity.this)) {
            Toast.makeText(MainActivity.this, getResources().getString(R.string.noNetwork), Toast.LENGTH_SHORT).show();
        } else {

        }

        initData();

        Log.e(TAG, "-----------------------------------------------------------------------------");
        Log.e(TAG, UserDataService.getSingleUserDataService(this).getUser().toString());
        Log.e(TAG, "userId:" + UserDataService.getSingleUserDataService(this).getUser().userId);
        Log.e(TAG, UserDataService.getSingleUserDataService(this).getUser().imId);
        Log.e(TAG, "-----------------------------------------------------------------------------");

        //注册云信监听
        registObserver();

    }

    @Override
    public void onResume() {
        super.onResume();

        mUserDataService = UserDataService.getSingleUserDataService(this);
        mUserData = mUserDataService.getUser();
        //init the data for NetWorkImageView
        btn_open.setDefaultImageResId(R.mipmap.bg_female_default);
        //设置加载出错图片
        btn_open.setErrorImageResId(R.mipmap.bg_female_default);
        imageCache = new SDImageCache();
        imageLoader = new ImageLoader(MyApplication.getRequestQueue(), imageCache);
        if (mUserData.avatarUrl != null && !mUserData.avatarUrl.equals(""))
            btn_open.setImageUrl(mUserData.avatarUrl, imageLoader);
    }

    @Override
    public void onStop() {
        mNavigationDrawerFragment.closeNavigationDrawer();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        unregistObserver();
    }

    /**
     * 初始化数据
     */
    public void initData() {
        //初始化侧滑栏
        mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);

        //侧滑栏宽度
        ViewGroup.LayoutParams params = mNavigationDrawerFragment.getView().getLayoutParams();
        params.width = DisplayWidth * 2 / 3;
        mNavigationDrawerFragment.getView().setLayoutParams(params);
        //初始化侧滑栏
        mNavigationDrawerFragment.setUp(R.id.navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout));
        mNavigationDrawerFragment.closeNavigationDrawer();

        btn_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /** 用于关闭活动界面的popupWindow */
                if (mFragments[TAB_COMMUCITY_INDEX] != null)
                    ((CommunityFragment) mFragments[TAB_COMMUCITY_INDEX]).execute();

                mNavigationDrawerFragment.openNavigationDrawer();
            }
        });
    }

    /**
     * 初始化视图
     */
    private void initViews() {

        mUserDataService = UserDataService.getSingleUserDataService(this);
        mUserData = mUserDataService.getUser();
        //init the data for NetWorkImageView
        btn_open.setDefaultImageResId(R.mipmap.bg_female_default);
        //设置加载出错图片
        btn_open.setErrorImageResId(R.mipmap.bg_female_default);
        imageCache = new SDImageCache();
        imageLoader = new ImageLoader(MyApplication.getRequestQueue(), imageCache);
        if (mUserData.avatarUrl != null && !mUserData.avatarUrl.equals(""))
            btn_open.setImageUrl(mUserData.avatarUrl, imageLoader);

        mPagerAdapter = new TabFragmentPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setCurrentItem(mCurrentTab);

        //任意一个页面两边应持久化的fragment为2
        mViewPager.setOffscreenPageLimit(2);

        //设置换页监听
        mViewPager.setOnPageChangeListener(mOnPageChangeListener);

        //设置按钮监听
        mRadioGroup.setOnCheckedChangeListener(mOnCheckedChangeListener);

        //选中第mCurrentTab个按钮
        ((RadioButton) mRadioGroup.getChildAt(mCurrentTab)).setChecked(true);
    }

    /**
     * implements NavigationDrawerFragment.NavigationDrawerCallbacks
     * <p>
     * 点击侧滑栏(NavigationDrawerFragment)item时的回掉函数
     *
     * @param position 点击的item序号，从0开始
     */
    @Override
    public void onNavigationDrawerItemSelected(int position) {
        Log.e("点击侧滑栏", "点击第" + (position + 1) + "栏");
        Toast.makeText(this, "点击第" + (position + 1) + "栏", Toast.LENGTH_LONG).show();
    }

    /**
     * {@link #mRadioGroup}的按钮监听器
     */
    private RadioGroup.OnCheckedChangeListener mOnCheckedChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            /**用于在fragment切换的时候关闭CommunityFragment中的popupWindow*/
            if (mFragments[TAB_COMMUCITY_INDEX] != null)
                ((CommunityFragment) mFragments[TAB_COMMUCITY_INDEX]).execute();

            switch (checkedId) {
                //点击教程tab
                case R.id.radio_1:
                    setCurrentTab(TAB_COURSE_INDEX);
                    break;
                //点击分享tab
                case R.id.radio_2:
                    setCurrentTab(TAB_DISPLAY_INDEX);
                    break;
                //点击活动tab
                case R.id.radio_3:
                    setCurrentTab(TAB_COMMUCITY_INDEX);
                    break;
                //点击招募tab
                case R.id.radio_4:
                    setCurrentTab(TAB_TRANSACTION_INDEX);
                    break;
            }
        }
    };

    /**
     * {@link #mViewPager}选中第index个页面
     *
     * @param index
     */
    private void setCurrentTab(int index) {
        if (mViewPager != null && (index >= 0 && index <= 3)) {
            mCurrentTab = index;
            mViewPager.setCurrentItem(index);
            Log.i(TAG, "mCurrentTab:" + mCurrentTab);
        }
        Log.i(TAG, "mCurrentTab:" + mCurrentTab);
        iv_tag1.setImageResource(R.mipmap.ic_crouse);
        iv_tag2.setImageResource(R.mipmap.ic_share);
        iv_tag3.setImageResource(R.mipmap.ic_activity);
        iv_tag4.setImageResource(R.mipmap.ic_recruite);

        switch (index) {
            case TAB_COURSE_INDEX:
                iv_tag1.setImageResource(R.mipmap.ic_crouse_check);
                break;
            case TAB_DISPLAY_INDEX:
                iv_tag2.setImageResource(R.mipmap.ic_share_check);
                break;
            case TAB_COMMUCITY_INDEX:
                iv_tag3.setImageResource(R.mipmap.ic_activity_check);
                break;
            case TAB_TRANSACTION_INDEX:
                iv_tag4.setImageResource(R.mipmap.ic_recruite_check);
                break;
        }
    }

    /**
     * {@link #mViewPager}换页适配器
     */
    private ViewPager.OnPageChangeListener mOnPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrollStateChanged(int status) {
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageSelected(int index) {
            //选中第index个tab按钮，来触发对应tab选中效果(按钮下方会出现指示线)
            ((RadioButton) mRadioGroup.getChildAt(index)).setChecked(true);

           /* if (index != TAB_COURSE_INDEX && mFragments[TAB_COURSE_INDEX] != null) {
                ((CourseFragment) mFragments[TAB_COURSE_INDEX]).releaseMemory();
            } else {
                if (mFragments[TAB_COURSE_INDEX] == null)
                    Log.i(TAG, "教程碎片为空");
                else {
                    ((CourseFragment) mFragments[TAB_COURSE_INDEX]).reloadData();
                    Log.i(TAG, "mCurrentTab==TAB_COURSE_INDEX");
                }
            }*/

            if (index != TAB_DISPLAY_INDEX && mFragments[TAB_DISPLAY_INDEX] != null) {
                ((DisplayFragment) mFragments[TAB_DISPLAY_INDEX]).releaseMemory();
            } else {
                if (mFragments[TAB_DISPLAY_INDEX] == null)
                    Log.i(TAG, "分享碎片为空");
                else {
                    ((DisplayFragment) mFragments[TAB_DISPLAY_INDEX]).reloadData();
                    Log.i(TAG, "mCurrentTab==TAB_DISPLAY_INDEX");
                }
            }


            if (index != TAB_COMMUCITY_INDEX && mFragments[TAB_COMMUCITY_INDEX] != null) {
                ((CommunityFragment) mFragments[TAB_COMMUCITY_INDEX]).releaseMemory();
            } else {
                if (mFragments[TAB_COMMUCITY_INDEX] == null)
                    Log.i(TAG, "活动碎片为空");
                else {
                    ((CommunityFragment) mFragments[TAB_COMMUCITY_INDEX]).reloadData();
                    Log.i(TAG, "mCurrentTab==TAB_COMMUCITY_INDEX");
                }
            }
        }
    };


    class TabFragmentPagerAdapter extends FragmentPagerAdapter {
        public TabFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int tabIndex) {

            switch (tabIndex) {
                //点击教程tab，要显示教程页面
                case TAB_COURSE_INDEX:
                    if (mFragments[TAB_COURSE_INDEX] == null)
                        mFragments[TAB_COURSE_INDEX] = new CourseFragment();
                    return mFragments[TAB_COURSE_INDEX];
                //点击分享tab，要显示分享页面
                case TAB_DISPLAY_INDEX:
                    if (mFragments[TAB_DISPLAY_INDEX] == null)
                        mFragments[TAB_DISPLAY_INDEX] = new DisplayFragment();
                    return mFragments[TAB_DISPLAY_INDEX];
                //点击活动tab，要显示活动页面
                case TAB_COMMUCITY_INDEX:
                    if (mFragments[TAB_COMMUCITY_INDEX] == null)
                        mFragments[TAB_COMMUCITY_INDEX] = new CommunityFragment();
                    else {
                        ((CommunityFragment) mFragments[TAB_COMMUCITY_INDEX]).reloadData();
                    }
                    return mFragments[TAB_COMMUCITY_INDEX];
                //点击招募tab，要显示招募页面
                case TAB_TRANSACTION_INDEX:
                    if (mFragments[TAB_TRANSACTION_INDEX] == null)
                        mFragments[TAB_TRANSACTION_INDEX] = new TransactionFragment();
                    return mFragments[TAB_TRANSACTION_INDEX];
            }
            return null;
        }

        @Override
        public int getCount() {
            return 4;
        }
    }


    private long mExitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        /**
         * when back button is pressed, to check whether the person page is show.
         */
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mNavigationDrawerFragment.isVisible()) {
                mNavigationDrawerFragment.closeNavigationDrawer();
                return true;
            }
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();
            } else {
                finish();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    public void registObserver() {
        //最近联系人列表监听
        NIMClient.getService(MsgServiceObserve.class)
                .observeRecentContact(messageObserver, true);
    }

    public void unregistObserver() {
        //最近联系人列表监听
        NIMClient.getService(MsgServiceObserve.class)
                .observeRecentContact(messageObserver, false);
    }


    Observer<List<RecentContact>> messageObserver = new Observer<List<RecentContact>>() {
        @Override
        public void onEvent(List<RecentContact> messages) {

            for (RecentContact msg : messages) {

                Contact contact = new Contact();
                String myImId = AccountDataService.getSingleAccountDataService(MainActivity.this).getUserAccId();

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

                ContactDBService.getInstance(MainActivity.this).addContact(contact);
                String ticker = contact.contactNickName + "发来信息";
                String title = contact.contactNickName;
                String content = contact.messageContent;

                Intent intent = new Intent();
                intent.setClass(MyApplication.getContext(), ContactActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(ContactActivity.TargetUserID, contact.contactUserId);
                bundle.putString(ContactActivity.TargetUserAvatar, contact.avatarUrl);
                bundle.putString(ContactActivity.TargetUserName, contact.contactNickName);
                bundle.putString(ContactActivity.TargetUserIMID, contact.fromAccount);
                Log.v("mainActivity", "targetIMID--------   " + contact.fromAccount);
                Log.v("mainActivity", "targetID--------   " + contact.contactUserId);
                Log.v("mainActivity", "targetAvatar--------   " + contact.avatarUrl);
                intent.putExtras(bundle);

                if(!msg.getFromAccount().equals(myImId)){
                    NotifyUtils.notifyMessage(MyApplication.getContext(), intent, ticker, title, content, contact.fromAccount);
                }

                Log.i("总未读数", "总未读数-------------" + ContactDBService.getInstance(MainActivity.this).getUnReadNums());

                mNavigationDrawerFragment.updateUnReadNum();
//                Log.e("最近会话信息", "联系人id：" + msg.getContactId());
//                Log.e("最近会话信息", "会话内容：" + msg.getContent());
//                Log.e("最近会话信息", "会话账号：" + msg.getFromAccount());
//                Log.e("最近会话信息", "messageId：" + msg.getRecentMessageId());
//                Log.e("最近会话信息", "时间：" + ModelUtils.getDateDetailByTimeStamp(msg.getTime()));
//                Log.e("最近会话信息", "未读数：" + msg.getUnreadCount());
//                Log.e("最近会话信息", "信息状态：" + msg.getMsgStatus());
//                Log.e("最近会话信息", "会话类型：" + (msg.getSessionType() == SessionTypeEnum.Team ? "群聊" : "单聊"));
//                Log.i("最近会话信息", "----------------------------------------");
                    /*NIMClient.getService(MsgService.class).setChattingAccount(
                            msg.getContactId(),
                            SessionTypeEnum.P2P
                            );*/
            }

//            List<Contact> contactList = ContactDBService.getInstance(MainActivity.this).getContactList();
//
//            for (Contact contact : contactList) {
//                Log.e("数据库读取", "contact: --" + contact.toString());
//            }

        }
    };


}
