package com.think.linxuanxuan.ecos.database;


import com.think.linxuanxuan.ecos.model.Province;

import java.util.List;


/***
 * 
* @ClassName: IProvinceService 
* @Description: TODO(省服务接口，包括添加省，删除省，获取省名、获取省id、获取省信息列表) 
* @author enlizhang
* @date 2015年1月24日 下午2:26:03 
*
 */
public interface IProvinceService {

	/***
	 * 添加省到数据库
	 * @param province 要添加的省信息
	 */
	public void addProvince(List<Province> province);
	
	
	/***
	 * 删除所有省
	 * @return
	 */
//	public void deleteAll();
	
	
	
	/***
	 * 获取省列表
	 * @return List<Province>，省列表
	 */
	public List<Province> getProvinceList();
	
	/**
	 * 根据省名获取省id
	 *
	 * @param provinceName 省名
	 * @return 该省名对应的省id，若无则返回-1
	 */
	public String getProvinceId(String provinceName);
	
	
	/**
	 * 根据城provinceId获取省名
	 *
	 * @param provinceId 省id
	 * @return 该省名对应的省id，若无则返回-1
	 */
	public String getProvinceName(String provinceId);
}
