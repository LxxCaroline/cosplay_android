package com.think.linxuanxuan.ecos.database;


import android.content.Context;

import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.think.linxuanxuan.ecos.model.Province;

import java.util.List;

/***
 * 
* @ClassName: ProvinceDBService 
* @Description: TODO(省信息操作类，采用单例模式) 
* @author enlizhang
* @date 2015年1月24日 下午2:35:12 
*
 */
public class ProvinceDBService implements IProvinceService{

	private static final String TAG = "ProvinceDBService";
	private RuntimeExceptionDao<Province, String> mProvinceDAO;
	
	public static ProvinceDBService singleProvinceDBService;
	private static Context mContext;
	
	private ProvinceDBService(Context context)
	{
		mContext = context;
		DatabaseHelper dbHelper = new DatabaseHelper(context);
		mProvinceDAO = dbHelper.getProvinceDao();
	}
	

	/**
	 * 获取当前类实例
	 * @param context
	 * @return 当前类实例对象
	 */
	public static ProvinceDBService getProvinceDBServiceInstance(Context context)
	{
		//若当前类实例不存在，则创建新类实例对象
		if(singleProvinceDBService == null)
		{
			singleProvinceDBService = new ProvinceDBService(context);
		}
		else //若当前类实例存在，但当前上下文对象已经产生变化，也重新创建类实例对象
			if( mContext == null)
			{
				singleProvinceDBService = new ProvinceDBService(context);
			}
		
		return singleProvinceDBService;
	}
	
	
	
	@Override
	public void addProvince(List<Province> provinceList) {
		// TODO Auto-generated method stub
		for(Province province:provinceList)
			mProvinceDAO.createOrUpdate(province);
	}


	/*@Override
	public void deleteAll() {
		
		mProvinceDAO.delete(getProvinceList());
		
	}*/


	@Override
	public List<Province> getProvinceList() {
		// TODO Auto-generated method stub
		return mProvinceDAO.queryForAll();
	}


	
	@Override
	public String getProvinceId(String provinceName) {
		
		if(provinceName == null || "".equals(provinceName))
			return "-1";
		
		Province province = new Province();
		
		//以省名作为检索条件
		province.setProvinceName(provinceName);
		
		List<Province> provinceList=mProvinceDAO.queryForMatchingArgs(province);
		
		return provinceList.size()>0?provinceList.get(0).getProvinceCode():"-1";
	}


	@Override
	public String getProvinceName(String provinceId) {
		Province province = new Province();
		province.setProvinceCode(provinceId);
		
		List<Province> provinceList=mProvinceDAO.queryForMatchingArgs(province);
		
		return provinceList.size()>0?provinceList.get(0).getProvinceName():"";
	}


}
