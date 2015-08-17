package com.think.linxuanxuan.ecos.database;


import com.think.linxuanxuan.ecos.model.City;

import java.util.List;


/***
 * 城市服务接口，包括添加城市，删除城市，获取城市名、获取城市id、获取城市信息列表
 * @author enlizhang
 *
 * 由{@link CityService)实现
 */
public interface ICityService {

	/***
	 * 添加城市到数据库
	 * @param city 要添加的城市信息
	 */
	public void addCity(List<City> cityList);
	
	
	/***
	 * 删除所有城市
	 * @return
	 */
//	public void deleteAll();
	
	
	
	/***
	 * 获取城市列表
	 * @return List<City>，城市列表
	 */
	public List<City> getCityList();
	
	/**
	 * 根据城市名获取城市id，若cityName最后一个字为“市”，则先去掉最后一个字
	 *
	 * @param cityName 城市名
	 * @return 该城市名对应的城市id，若无则返回-1
	 */
	public String getCityId(String cityName);
	
	
	/**
	 * 根据城cityId获取城市名
	 *
	 * @param cityId 城市id
	 * @return 该城市名对应的城市id，若无则返回-1
	 */
	public String getCityName(String cityId);
	
	
	
	/**
	 * 根据省id获取城市立列表
	 *
	 * @param provinceId 省id
	 * @return 城市列表
	 */
	public List<City> getCityListByProvinceId(String provinceId);
}
