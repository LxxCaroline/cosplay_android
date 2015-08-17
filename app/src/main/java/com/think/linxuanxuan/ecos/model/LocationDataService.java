package com.think.linxuanxuan.ecos.model;

import android.content.Context;
import android.content.SharedPreferences;

/***
 * 百度定位数据存取
 * @author Administrator
 *
 */
public class LocationDataService {

	private final static String TAG = "BDLocationDataService";
	
	private final String PREFERENCE_NAME = "BDLocationData";
	private final int READ_MODE = Context.MODE_WORLD_READABLE;
	private final int WRITE_MODE = Context.MODE_WORLD_WRITEABLE;
	
	private final static String LATITUDE = "latitude";
	private final static String LONGTITUDE = "longitude";
	private final static String LOC_CITY = "loc_city";
	private final static String LOC_DISTRICT = "loc_district";
	private final static String LOC_STREET = "loc_street";
	
	private String DEFAULT_VALUE = "empty";
	
	private static LocationDataService singleLocationDataService = null;
	
	private  static Context mContext;
	
	
	private LocationDataService(Context context)
	{
		mContext = context;
	}
	
	/***
	 * 返回LocationDataService类单例操作对象
	 * @param context 若mContext为null，则根据context进行创建，此时必须保证context!=null.<br>
	 * 					否则，context不进行使用
	 * @return
	 */
	public static LocationDataService getBDLocationDataService(Context context) {
		if(singleLocationDataService == null && mContext==null)
		{
			singleLocationDataService = new LocationDataService(context);
		}
		return singleLocationDataService;
	}
	
	/**
	 * 保存LocationData中的数据到SharePreference
	 * @param locationData
	 */
	public void save(LocationData locationData )
	{
		SharedPreferences sharedPreferences = mContext.getSharedPreferences(PREFERENCE_NAME,WRITE_MODE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		
		
		editor.putString(LATITUDE, locationData.getLatitude());
		editor.putString(LONGTITUDE, locationData.getLongitude());
		
		editor.putString(LOC_CITY, locationData.getLocCity());
		editor.putString(LOC_DISTRICT, locationData.getLocDistrict());
		editor.putString(LOC_STREET, locationData.getLocStreet());
		
		editor.commit();
	}
	
	/***
	 * 从SharePreference获取定位数据
	 * @return LocationData 定位数据,若无则返回null
	 */
	public LocationData getLocationData()
	{
		SharedPreferences sharedPreferences = mContext.getSharedPreferences(PREFERENCE_NAME,READ_MODE);
		
		LocationData locationData = new LocationData();
		
		locationData.setLatitude( sharedPreferences.getString(LATITUDE, DEFAULT_VALUE) );
		locationData.setLongitude( sharedPreferences.getString(LONGTITUDE, DEFAULT_VALUE) );
		locationData.setLocCity( sharedPreferences.getString(LOC_CITY, DEFAULT_VALUE) );
		locationData.setLocDistrict( sharedPreferences.getString(LOC_DISTRICT, DEFAULT_VALUE) );
		locationData.setgetLocStreet( sharedPreferences.getString(LOC_STREET, DEFAULT_VALUE) );
		
		if( locationData.getLatitude().equals(DEFAULT_VALUE))
			return null;
		else
			return locationData;
	}
}
