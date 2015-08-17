package com.think.linxuanxuan.ecos.activity;

import android.app.Application;
import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.GeofenceClient;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.think.linxuanxuan.ecos.R;
import com.think.linxuanxuan.ecos.model.AccountDataService;
import com.think.linxuanxuan.ecos.model.LocationData;
import com.think.linxuanxuan.ecos.model.LocationDataService;
import com.think.linxuanxuan.ecos.model.UserDataService;
import com.think.linxuanxuan.ecos.utils.MyMediaScanner;
import com.think.linxuanxuan.ecos.utils.yunxin.ScreenUtil;
import com.think.linxuanxuan.ecos.utils.yunxin.SystemUtil;
import com.netease.nimlib.sdk.AbortableFuture;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.SDKOptions;
import com.netease.nimlib.sdk.StatusBarNotificationConfig;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.auth.LoginInfo;

/**
 * 类描述：应用类
 * Created by enlizhang on 2015/7/15.
 */
public class MyApplication extends Application {

    private static final String TAG = "MyApplication";

    /***
     * 程序当前的Activity，在每次启动activity时，在onCreate()中赋值。
     * 在继承BaseActivity后，由于BaseActivity中已经进行设置，所以在调用super.onCreate()时相当于设置了
     *
     * 对于此变量设置的用途:由于应用中很多地方需要当前的activity引用，例如最多的就是对话框，因此该静态变量的设定
     * 使得在非activity类中创建对话框这些控件前，无需额外传入activity对象，例如Adapter。
     */
    public static BaseActivity msCurrentActivity;

    /***主页面activity*/
    public static MainActivity msMainActivity;

    /***
     * 当前Application对象引用
     */
    private static Application mApplication;


    //百度定位---------------------开始
    public static LocationClient mLocationClient;
    public GeofenceClient mGeofenceClient;
    public MyLocationListener mMyLocationListener;

    private static boolean firstTime = true;

    public static LocationCallBack mLocationCallBack;
    //百度定位---------------------结束

    @Override
    public void onCreate() {
        super.onCreate();

        mApplication = this;

        Log.e("初始化", "初始化");
        NIMClient.init(this, getLoginInfo(), null);

        //模拟存储用户数据.
//        saveTestUserData();

        Log.e("tag","---------------" + UserDataService.getSingleUserDataService(this).getUser().toString());

        //百度定位---------------------开始
        Log.i("百度", "application start");
        // 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext
        //		SDKInitializer.initialize(this);

        if(firstTime){
            mLocationClient = new LocationClient(this.getApplicationContext());
            mMyLocationListener = new MyLocationListener();
            mLocationClient.registerLocationListener(mMyLocationListener);
            mGeofenceClient = new GeofenceClient(getApplicationContext());
            mLocationClient.setDebug(false);
            InitLocation();
            mLocationClient.start();
            firstTime = false;
            //百度定位---------------------结束
        }


    }


    /***
     * 设置当前activity
     * @param currentActivity 当前activity
     */
    public static void setCurrentActivity(BaseActivity currentActivity){
        msCurrentActivity = currentActivity;
    }

    /***
     * 获取当前Activity对象
     * @return
     */
    public static BaseActivity getCurrentActivity(){
        return msCurrentActivity;
    }

    /****
     * 获取当前Context对象，与{@link #msCurrentActivity}有点类似，后续用到Context对象时，
     *     例如SharePreference，数据库等地方可以直接用本方法获取Context对象，减少代码复杂度
     * @return
     */
    public static Context getContext()
    {
        return mApplication.getApplicationContext();
    }


    public static Application getDemoApplication()
    {
        return mApplication;
    }


    public static RequestQueue volleyQueue;

    public static synchronized RequestQueue getRequestQueue()
    {
        if(volleyQueue == null)
        {
            volleyQueue = Volley.newRequestQueue(mApplication.getApplicationContext());
        }
        return volleyQueue;
    }


    /***
     * 多媒体扫描对象，用于保存图片到图库后，图库无法自动刷新
     */
    private static MyMediaScanner myMediaScanner;

    /***
     * 采用单例获取可用的{@link #myMediaScanner}
     * @return
     */
    public static synchronized MyMediaScanner getMediaScanner()
    {
        if(myMediaScanner == null )
        {
            myMediaScanner =new MyMediaScanner(getContext());
        }

        return myMediaScanner;
    }


    /***
     * ------------------------------------------------------
     * 云信SDK初始化开始------------------------------------------
     * ------------------------------------------------------
     */

    private LoginInfo getLoginInfo() {
        String account = AccountDataService.getSingleAccountDataService(this).getUserAccId();
        String token = AccountDataService.getSingleAccountDataService(this).getImToken();

        Log.e(TAG,"account:" + account);
        Log.e(TAG,"token:" + token);

        if (!TextUtils.isEmpty(account) && !TextUtils.isEmpty(token)) {
            Log.e(TAG,"!TextUtils.isEmpty(account) && !TextUtils.isEmpty(token)");
            // open db
            return new LoginInfo(account, token);
        }

        return null;
    }


    private SDKOptions getOptions() {
        SDKOptions options = new SDKOptions();

        // 如果将新消息通知提醒托管给SDK完成，需要添加以下配置。
        // 其中notificationSmallIconId必须提供
        StatusBarNotificationConfig config = new StatusBarNotificationConfig();
        config.notificationEntrance = NotificationActivity.class;

        config.notificationSmallIconId = R.mipmap.icon;
        // 通知铃声的uri字符串
        config.notificationSound = "android.resource://com.think.linxuanxuan.ecos/raw/msg";
        options.statusBarNotificationConfig = config;

        // 配置保存图片，文件，log等数据的目录
        String sdkPath = Environment.getExternalStorageDirectory() + "/" + getPackageName() + "/nim";
        options.sdkStorageRootPath = sdkPath;

        // 配置数据库加密秘钥
        options.databaseEncryptKey = "NETEASE";

        // 配置是否需要预下载附件缩略图
        options.preloadAttach = true;

        // 配置附件缩略图的尺寸大小，
        options.thumbnailSize = ScreenUtil.getScreenMin() / 2;
        return options;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    public boolean inMainProcess() {
        String packageName = getPackageName();
        String processName = SystemUtil.getProcessName(this);
        return packageName.equals(processName);
    }

    /***
     * ------------------------------------------------------
     * 云信SDK初始化结束------------------------------------------
     * ------------------------------------------------------
     */


    //百度定位---------------------开始
    /**
     * 实现实位回调监听
     */
    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {

            Log.i(TAG, "location.getLocType()"+location.getLocType());
            Log.i(TAG, "location.TypeCacheLocation"+location.TypeCacheLocation);
            Log.i(TAG, "location.TypeGpsLocation"+location.TypeGpsLocation);
            Log.i(TAG, "location.TypeNetWorkLocation"+location.TypeNetWorkLocation);

            Log.i(TAG, "TypeNetWorkException"+location.TypeNetWorkException);
            Log.i(TAG, "TypeNone"+location.TypeNone);
            Log.i(TAG, "TypeServerError"+location.TypeServerError);

            String latitude= String.valueOf(location.getLatitude());
            String longitude = String.valueOf(location.getLongitude());
            String locCity = location.getCity();
            String locDistrict = location.getDistrict();
            String locStree = location.getStreet()+location.getStreetNumber() ;

            LocationData locationData = new LocationData(latitude, longitude,
                    locCity, locDistrict, locStree);

            if(location.getLocType() == location.TypeOffLineLocationNetworkFail || locCity==null)
            {
                Log.w(TAG, "定位失败");
                if(mLocationCallBack!=null)
                    mLocationCallBack.locationFailed("定位失败");
            }
            if(locCity!=null)
            {
                Log.w(TAG, "locCity!=null");
                if(mLocationCallBack!=null)
                {
                    Log.w(TAG, "mLocationCallBack!=null");
                    mLocationCallBack.locationSuccess(locationData);

                }
                LocationDataService.getBDLocationDataService(getApplicationContext()).save(locationData);

                /*new SendLocationRequest().request(null,String.valueOf(location.getLatitude()),
                        String.valueOf(location.getLongitude()));*/
            }
            else
            {
                System.out.println("无法定位");
            }


            StringBuffer sb = new StringBuffer(256);
            sb.append("\nlatitude : ");
            sb.append(location.getLatitude());
            sb.append("\nlontitude : ");
            sb.append(location.getLongitude());
            if (location.getLocType() == BDLocation.TypeGpsLocation){
                sb.append("\nspeed : ");
                sb.append(location.getSpeed());
                sb.append("\nsatellite : ");
                sb.append(location.getSatelliteNumber());
                sb.append("\ndirection : ");
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                sb.append(location.getDirection());
            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation){
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                //运营商信息
                sb.append("\noperationers : ");
                sb.append("\ncityCode:"+location.getCityCode());
                sb.append(location.getOperators());
                sb.append("\ncity:"+location.getCity()+"  district"+location.getDistrict()+"  street"+location.getStreet());
                sb.append("\nNetworkLocationType:"+location.getNetworkLocationType());
                sb.append("\nfloor:"+location.getFloor());
            }
            Log.i("百度地图回调函数", sb.toString());
            Log.i("百度地图回调函数", ""+location.describeContents());
            Log.i("百度地图回调函数", " LOCATION_WHERE_IN_CN"+BDLocation.LOCATION_WHERE_IN_CN );
            Log.i("百度地图回调函数", " LOCATION_WHERE_OUT_CN"+BDLocation.LOCATION_WHERE_OUT_CN );
            Log.i("百度地图回调函数", " LOCATION_WHERE_UNKNOW"+BDLocation.LOCATION_WHERE_UNKNOW );

            mLocationClient.stop();
            mLocationCallBack = null;
        }


    }

    private void InitLocation(){

        LocationClientOption.LocationMode tempMode = LocationClientOption.LocationMode.Hight_Accuracy;
        String tempcoor="bd09ll";
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(tempMode);//设置定位模式
        option.setCoorType(tempcoor);//返回的定位结果是百度经纬度，默认值gcj02
        option.setIsNeedAddress(true);
        option.setAddrType("all");
        int span=120000;
        option.setScanSpan(span);//设置发起定位请求的间隔时间为5000ms
        option.setIsNeedAddress(true);
        mLocationClient.setLocOption(option);

    }
    /***
     * 启动定位
     * @param locationCallBack,定位结束回调函数
     */
    public static void startLocation(LocationCallBack locationCallBack)
    {
        mLocationCallBack = locationCallBack;
        mLocationClient.start();
    }

    /***
     *
     * @ClassName: LocationCallBack
     * @Description: TODO(定位回调函数)
     * @author enlizhang
     * @date 2015年1月21日 上午11:37:41
     *
     */
    public interface LocationCallBack
    {
        /***
         * 定位成功
         * @param locationData，定位成功后返回的定位数据
         */
        public void locationSuccess(LocationData locationData);

        /***
         * 定位失败
         * @param message 失败信息
         */
        public void locationFailed(String message);
    }
    //百度定位---------------------结束


    private AbortableFuture<LoginInfo> loginRequest;
    public void login(){
        final String account = "2b584d0f3e0243008582579802d28901";
        final String token = "cc7be9877aacd5248b53d2e0b823096f";

        //用账号和token进行登录
        loginRequest = NIMClient.getService(AuthService.class).login(new LoginInfo(account, token));
        loginRequest.setCallback(new RequestCallback<LoginInfo>() {
            @Override
            public void onSuccess(LoginInfo param) {

                Log.e("tag","登录成功");


            }

            @Override
            public void onFailed(int code) {
                if (code == 302 || code == 404) {
                    Toast.makeText(getContext(),  getResources().getString(R.string.accountPasswordError), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "login error: " + code, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onException(Throwable exception) {
                Log.e("登录",exception.toString());
            }
        });
    }
}
