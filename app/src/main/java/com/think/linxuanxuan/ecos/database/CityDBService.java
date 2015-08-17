package com.think.linxuanxuan.ecos.database;

import android.content.Context;

import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.think.linxuanxuan.ecos.model.City;

import java.util.List;

/***
 * 采用单例模式。
 * 城市信息操作类
 * 
 * @author enlizhang
 *
 */
public class CityDBService implements ICityService{

	private static final String TAG = "CityDBService";
	private RuntimeExceptionDao<City, String> mCityDAO;
	
	public static CityDBService singleCityDBService;
	private static Context mContext;
	
	private CityDBService(Context context)
	{
		mContext = context;
		DatabaseHelper dbHelper = new DatabaseHelper(context);
		mCityDAO = dbHelper.getCityDao();
	}
	

	/**
	 * 获取当前类实例
	 * @param context
	 * @return 当前类实例对象
	 */
	public static CityDBService getCityDBServiceInstance(Context context)
	{
		//若当前类实例不存在，则创建新类实例对象
		if(singleCityDBService == null)
		{
			singleCityDBService = new CityDBService(context);
		}
		else //若当前类实例存在，但当前上下文对象已经产生变化，也重新创建类实例对象
			if( mContext == null)
			{
				singleCityDBService = new CityDBService(context);
			}
		
		return singleCityDBService;
	}
	
	
	
	@Override
	public void addCity(List<City> cityList) {
		// TODO Auto-generated method stub
		for(City city:cityList)
			mCityDAO.createOrUpdate(city);
	}


	/*@Override
	public void deleteAll() {
		
		mCityDAO.delete(getCityList());
		
	}*/


	@Override
	public List<City> getCityList() {
		// TODO Auto-generated method stub
		return mCityDAO.queryForAll();
	}


	
	
	@Override
	public String getCityId(String cityName) {
		
		if(cityName == null || "".equals(cityName))
			return "-1";
		
		City city = new City();
		
		if((cityName.lastIndexOf("市")) == (cityName.length()-1))
		{
			System.out.println("该城市名内含市");
			cityName = cityName.substring(0, cityName.length()-1);
			System.out.println("裁剪后，city:"+cityName);
		}
		
		//以市名作为检索条件
		city.setCityName(cityName);
		
		List<City> cityList=mCityDAO.queryForMatchingArgs(city);
		
		return cityList.size()>0?cityList.get(0).getCityCode():"-1";
	}


	@Override
	public String getCityName(String cityId) {
		City city = new City();
		city.setCityCode(cityId);
		
		List<City> cityList=mCityDAO.queryForMatchingArgs(city);
		
		return cityList.size()>0?cityList.get(0).getCityName():"";
	}


	@Override
	public List<City> getCityListByProvinceId(String provinceId) {
		City city = new City();
		city.setProvinceId(provinceId);
		
		List<City> cityList=mCityDAO.queryForMatchingArgs(city);
		
		return cityList;
	}


}
