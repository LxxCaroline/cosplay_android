package com.think.linxuanxuan.ecos.fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.think.linxuanxuan.ecos.R;
import com.think.linxuanxuan.ecos.activity.MyApplication;
import com.think.linxuanxuan.ecos.activity.NormalListViewActivity;
import com.think.linxuanxuan.ecos.activity.NotificationActivity;
import com.think.linxuanxuan.ecos.activity.PersonageDetailActivity;
import com.think.linxuanxuan.ecos.activity.PersonalInfoSettingActivity;
import com.think.linxuanxuan.ecos.database.ContactDBService;
import com.think.linxuanxuan.ecos.model.User;
import com.think.linxuanxuan.ecos.model.UserDataService;
import com.think.linxuanxuan.ecos.request.BaseResponceImpl;
import com.think.linxuanxuan.ecos.request.user.GetUserInfoRequest;
import com.think.linxuanxuan.ecos.utils.RoundImageView;
import com.think.linxuanxuan.ecos.utils.SDImageCache;

/**
 * Fragment used for managing interactions for and presentation of a navigation drawer.
 * See the <a href="https://developer.android.com/design/patterns/navigation-drawer.html#Interaction">
 * design guidelines</a> for a complete explanation of the behaviors implemented here.
 */

/**
 * 侧滑栏
 */
public class NavigationDrawerFragment extends Fragment implements View.OnClickListener {

    /**
     * Remember the position of the selected item.
     */
    private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";

    /**
     * Per the design guidelines, you should show the drawer on launch until the user manually
     * expands it. This shared preference tracks this.
     */
    private static final String PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned";

    /**
     * A pointer to the current callbacks instance (the ActivityModel).
     */
    private NavigationDrawerCallbacks mCallbacks;

    /**
     * Helper component that ties the action bar to the navigation drawer.
     */
    private ActionBarDrawerToggle mDrawerToggle;

    private DrawerLayout mDrawerLayout;  //父控件
    /**
     * 侧边栏Layout
     */
    private View mDrawerView;

    /**
     * 侧边栏ListView
     */
//    private ListView mDrawerListView;


    /**
     * 父控件的FragmentContainer
     */
    private View mFragmentContainerView;

    /**
     * 当前选中的item的序号，从0开始
     */
    private int mCurrentSelectedPosition = 0;

    private boolean mFromSavedInstanceState;

    private boolean mUserLearnedDrawer;

    private TextView btPersonageDetail = null;
    private TextView btPersonageSetting = null;

    private UserDataService mUserDataService;
    private User mUserData;

    private GetUserInfoRequest getUserInfoRequest;
    private GetuserInfoResponse getuserInfoResponse;


    private LinearLayout ll_notification, ll_contact, ll_course, ll_display, ll_activity, ll_recruite, ll_personcenter, ll_setting;
    private TextView tv_notificationNum, tv_contactNum, tv_courseNum, tv_displayNum, tv_activityNum, tv_recruiteNum, tv_personcenterNum, tv_block;

    private LinearLayout ll_attention, ll_fans;

    private LinearLayout ll_person;

    public NavigationDrawerFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Read in the flag indicating whether or not the user has demonstrated awareness of the
        // drawer. See PREF_USER_LEARNED_DRAWER for details.
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mUserLearnedDrawer = sp.getBoolean(PREF_USER_LEARNED_DRAWER, false);

        if (savedInstanceState != null) {
            mCurrentSelectedPosition = savedInstanceState.getInt(STATE_SELECTED_POSITION);
            mFromSavedInstanceState = true;
        }

        // Select either the default item (0) or the last selected item.
//        selectItem(mCurrentSelectedPosition);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Indicate that this fragment would like to influence the set of actions in the action bar.
        setHasOptionsMenu(true);


    }


    public View mContainer;

    /**
     * 侧边栏Layout
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mDrawerView = (View) inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
//        mDrawerListView = (ListView) mDrawerView.findViewById(R.id.lv_list);


        bindView();

        initListener();
        mUserDataService = UserDataService.getSingleUserDataService(getActivity());
        mUserData = mUserDataService.getUser();


//        mDrawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                selectItem(position);
//            }
//        });
//
//
//        //给mDrawerListView进行数据绑定
////        mDrawerListView.setAdapter(new ArrayAdapter<String>(getActionBar().getThemedContext(), android.R.layout.simple_list_item_activated_1, android.R.id.text1, new String[]{getString(R.string.title_section1), getString(R.string.title_section2), getString(R.string.title_section3), getString(R.string.title_section4),}));
//        mDrawerListView.setAdapter(new MyAdapter(mDrawerView.getContext()));
//        //设置侧边栏默认记录的选项
////        mDrawerListView.setItemChecked(mCurrentSelectedPosition, true);

        //初始化用户信息
        mContainer = mDrawerView;
        //initUserData(mDrawerView);

        return mDrawerView;
    }


    private void bindView() {
        btPersonageDetail = (TextView) mDrawerView.findViewById(R.id.bt_personage_name);
        btPersonageSetting = (TextView) mDrawerView.findViewById(R.id.bt_personage_setting);

        ll_notification = (LinearLayout) mDrawerView.findViewById(R.id.ll_notification);
        ll_contact = (LinearLayout) mDrawerView.findViewById(R.id.ll_contact);
//        ll_course = (LinearLayout) mDrawerView.findViewById(R.id.ll_course);
//        ll_display = (LinearLayout) mDrawerView.findViewById(R.id.ll_display);
//        ll_activity = (LinearLayout) mDrawerView.findViewById(R.id.ll_activity);
//        ll_recruite = (LinearLayout) mDrawerView.findViewById(R.id.ll_recruite);
        ll_personcenter = (LinearLayout) mDrawerView.findViewById(R.id.ll_personcenter);
        ll_setting = (LinearLayout) mDrawerView.findViewById(R.id.ll_setting);

        tv_notificationNum = (TextView) mDrawerView.findViewById(R.id.tv_notificationNum);
        tv_contactNum = (TextView) mDrawerView.findViewById(R.id.tv_contactNum);
        tv_contactNum.setVisibility(View.VISIBLE);

//        tv_courseNum = (TextView) mDrawerView.findViewById(R.id.tv_courseNum);
//        tv_displayNum = (TextView) mDrawerView.findViewById(R.id.tv_displayNum);
//        tv_activityNum = (TextView) mDrawerView.findViewById(R.id.tv_activityNum);
//        tv_recruiteNum = (TextView) mDrawerView.findViewById(R.id.tv_recruiteNum);
        tv_personcenterNum = (TextView) mDrawerView.findViewById(R.id.tv_personcenterNum);
        tv_block = (TextView) mDrawerView.findViewById(R.id.tv_block);

        ll_person = (LinearLayout) mDrawerView.findViewById(R.id.ll_person);
        ll_attention = (LinearLayout) mDrawerView.findViewById(R.id.ll_attention);
        ll_fans = (LinearLayout) mDrawerView.findViewById(R.id.ll_fans);

    }

    private void initListener() {

        ll_person.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PersonageDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(PersonageDetailActivity.UserID, mUserData.userId);
                bundle.putBoolean(PersonageDetailActivity.IsOwn, true);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        btPersonageSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PersonalInfoSettingActivity.class);
                startActivity(intent);
            }
        });

        ll_notification.setOnClickListener(this);
        ll_contact.setOnClickListener(this);
//        ll_course.setOnClickListener(this);
//        ll_display.setOnClickListener(this);
//        ll_activity.setOnClickListener(this);
//        ll_recruite.setOnClickListener(this);
        ll_personcenter.setOnClickListener(this);
        ll_setting.setOnClickListener(this);
        tv_block.setOnClickListener(this);

        ll_attention.setOnClickListener(this);
        ll_fans.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        Intent intent;
        Bundle bundle;
        switch (v.getId()) {
            case R.id.ll_notification:
                Toast.makeText(getActivity(), getResources().getString(R.string.noPage), Toast.LENGTH_SHORT).show();
                return;
            case R.id.ll_contact:
                intent = new Intent(getActivity(), NotificationActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_attention:
                intent = new Intent(getActivity(), NormalListViewActivity.class);
                bundle = new Bundle();
                bundle.putInt(NormalListViewActivity.LISTVIEW_TYPE, NormalListViewActivity.TYPE_EVENT_ATTENTION);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.ll_fans:
                intent = new Intent(getActivity(), NormalListViewActivity.class);
                bundle = new Bundle();
                bundle.putInt(NormalListViewActivity.LISTVIEW_TYPE, NormalListViewActivity.TYPE_EVENT_FANS);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.ll_setting:
                intent = new Intent(getActivity(), PersonalInfoSettingActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_personcenter:
                intent = new Intent(getActivity(), PersonageDetailActivity.class);
                bundle = new Bundle();
                bundle.putString(PersonageDetailActivity.UserID, mUserData.userId);
                bundle.putBoolean(PersonageDetailActivity.IsOwn, true);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
        }

    }


    public boolean isDrawerOpen() {
        return mDrawerLayout != null && mDrawerLayout.isDrawerOpen(mFragmentContainerView);
    }

    /**
     * Users of this fragment must call this method to set up the navigation drawer interactions.
     *
     * @param fragmentId   The android:id of this fragment in its activity's layout.
     * @param drawerLayout The DrawerLayout containing this fragment's UI.
     */
    public void setUp(int fragmentId, DrawerLayout drawerLayout) {
        mFragmentContainerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;

        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.mipmap.drawer_shadow, GravityCompat.START);
        // set up the drawer's list view with items and click listener

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the navigation drawer and the action bar app icon.
        mDrawerToggle = new ActionBarDrawerToggle(
                getActivity(),                    /* host ActivityModel */
                mDrawerLayout,                    /* DrawerLayout object */
                R.mipmap.icon,             /* nav drawer image to replace 'Up' caret */
                R.string.navigation_drawer_open,  /* "open drawer" description for accessibility */
                R.string.navigation_drawer_close  /* "close drawer" description for accessibility */
        ) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                if (!isAdded()) {
                    return;
                }

                getActivity().supportInvalidateOptionsMenu(); // calls onPrepareOptionsMenu()
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                resetUserData();
                if (!isAdded()) {
                    return;
                }

                if (!mUserLearnedDrawer) {
                    // The user manually opened the drawer; store this flag to prevent auto-showing
                    // the navigation drawer automatically in the future.
                    mUserLearnedDrawer = true;
                    SharedPreferences sp = PreferenceManager
                            .getDefaultSharedPreferences(getActivity());
                    sp.edit().putBoolean(PREF_USER_LEARNED_DRAWER, true).apply();
                }

                getActivity().supportInvalidateOptionsMenu(); // calls onPrepareOptionsMenu()
            }
        };

        // If the user hasn't 'learned' about the drawer, open it to introduce them to the drawer,
        // per the navigation drawer design guidelines.
        if (!mUserLearnedDrawer && !mFromSavedInstanceState) {
            mDrawerLayout.openDrawer(mFragmentContainerView);
        }

        // Defer code dependent on restoration of previous instance state.
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });

        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    /**
     * 外部接口显示NavigationDrawer
     * 设置隐藏ActionBar
     * 因此增加此接口
     */
    public void openNavigationDrawer() {
        //打开NavigationDrawer
        mDrawerLayout.openDrawer(GravityCompat.START);
        //resetUserData();
    }

    /**
     * 外部接口关闭NavigationDrawer
     */
    public void closeNavigationDrawer() {
        //关闭NavigationDrawer
        mDrawerLayout.closeDrawer(GravityCompat.START);
    }

    /**
     * 点击之后关闭侧边栏，回调父类接口
     *
     * @param position
     */
    private void selectItem(int position) {
        mCurrentSelectedPosition = position;
//        if (mDrawerListView != null) {
//            mDrawerListView.setItemChecked(position, true);
//        }
        if (mDrawerLayout != null) {
            mDrawerLayout.closeDrawer(mFragmentContainerView);
        }
        if (mCallbacks != null) {
            mCallbacks.onNavigationDrawerItemSelected(position);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallbacks = (NavigationDrawerCallbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("ActivityModel must implement NavigationDrawerCallbacks.");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUnReadNum();
    }

    public void updateUnReadNum(){
        int unreadNums = ContactDBService.getInstance(getActivity()).getUnReadNums();
        if(unreadNums==0)
            tv_contactNum.setVisibility(View.GONE);
        else
        {
            tv_contactNum.setText(String.valueOf(unreadNums));
            tv_contactNum.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d("MSG", "test for on detach!");
        mCallbacks = null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_SELECTED_POSITION, mCurrentSelectedPosition);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Forward the new configuration the drawer toggle component.
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // If the drawer is open, show the global app actions in the action bar. See also
        // showGlobalContextActionBar, which controls the top-left area of the action bar.
        if (mDrawerLayout != null && isDrawerOpen()) {
//            inflater.inflate(R.menu.global, menu);
//            showGlobalContextActionBar();
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Per the navigation drawer design guidelines, updates the action bar to show the global app
     * 'context', rather than just what's in the current screen.
     */
    private void showGlobalContextActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setTitle(R.string.app_name);
    }

    private ActionBar getActionBar() {
        return ((ActionBarActivity) getActivity()).getSupportActionBar();
    }

    /**
     * Callbacks interface that all activities using this fragment must implement.
     */
    public static interface NavigationDrawerCallbacks {
        /**
         * Called when an item in the navigation drawer is selected.
         */
        void onNavigationDrawerItemSelected(int position);
    }

    private void initUserData(View v) {
        mUserDataService = UserDataService.getSingleUserDataService(getActivity());
        mUserData = mUserDataService.getUser();

        Log.e("侧边栏", "用户数据:" + mUserData.toString());

        RoundImageView user_avatar = (RoundImageView) v.findViewById(R.id.iv_personage_portrait);
        TextView user_name = (TextView) v.findViewById(R.id.bt_personage_name);
        ImageView user_gender = (ImageView) v.findViewById(R.id.riv_personage_gender);
        TextView user_attention = (TextView) v.findViewById(R.id.tv_personage_attention);
        TextView user_fans = (TextView) v.findViewById(R.id.tv_personage_fans);
        TextView user_description = (TextView) v.findViewById(R.id.tv_personage_description);

        //设置默认图片
        user_avatar.setDefaultImageResId(R.mipmap.bg_female_default);
        //设置加载出错图片
        user_avatar.setErrorImageResId(R.mipmap.bg_female_default);

        RequestQueue queue = MyApplication.getRequestQueue();

        ImageLoader.ImageCache imageCache = new SDImageCache();
        ImageLoader imageLoader = new ImageLoader(queue, imageCache);

        if (mUserData.avatarUrl != null && !"".equals(mUserData.avatarUrl)){
            user_avatar.setImageUrl(mUserData.avatarUrl, imageLoader);
        }else {
            user_avatar.setImageResource(R.mipmap.bg_female_default);
        }


        user_name.setText(mUserData.nickname);
        if (mUserData.gender == User.Gender.女) {
            user_gender.setBackgroundResource(R.mipmap.ic_gender_female);
        } else {
            user_gender.setBackgroundResource(R.mipmap.ic_gender_male);
        }
        user_attention.setText("" + mUserData.followOtherNum);
        user_fans.setText("" + mUserData.fansNum);
        user_description.setText(mUserData.characterSignature);

    }

    public void resetUserData() {
        if (mContainer != null) {
            getUserInfoRequest = new GetUserInfoRequest();
            getuserInfoResponse = new GetuserInfoResponse();
            getUserInfoRequest.requestOtherUserInfo(getuserInfoResponse, mUserData.userId);
            initUserData(mContainer);
        }
    }

    private class GetuserInfoResponse extends BaseResponceImpl implements GetUserInfoRequest.IGetUserInfoResponse {

        @Override
        public void doAfterFailedResponse(String message) {
        }

        @Override
        public void onErrorResponse(VolleyError error) {
        }

        @Override
        public void success(User user,boolean hasFollowed) {
            if (user.userId.equals(mUserData.userId)) {
                mUserData = user;
                Log.d("ZYW请求侧边栏", mUserData.toString());
                initUserData(mContainer);
            }
        }
    }
}
