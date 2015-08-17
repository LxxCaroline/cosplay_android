package com.think.linxuanxuan.ecos.database;

import android.content.Context;
import android.util.Log;

import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.think.linxuanxuan.ecos.activity.MyApplication;
import com.think.linxuanxuan.ecos.model.AccountDataService;
import com.think.linxuanxuan.ecos.model.Contact;

import java.util.ArrayList;
import java.util.List;

/**   
 * @Title: ContactDBService.java 
 * @Description: 联系人列表操作类
 * @author enlizhang   
 * @date 2015年7月29日 上午10:29:58 
 */

public class ContactDBService {
	private static final String TAG = "联系人最近会话存储";
	
	private RuntimeExceptionDao<Contact, String> mContactDAO;
	
	public static ContactDBService singleContactDBService;
	private static Context mContext;
	
	private ContactDBService(Context context)
	{
		mContext = context;
		DatabaseHelper dbHelper = new DatabaseHelper(context);
		mContactDAO = dbHelper.getContactDao();
	}
	

	/**
	 * 获取当前类实例
	 * @param context
	 * @return 当前类实例对象
	 */
	public static ContactDBService getInstance(Context context)
	{
		//若当前类实例不存在，则创建新类实例对象
		if(singleContactDBService == null)
		{
			singleContactDBService = new ContactDBService(context);
		}
		else //若当前类实例存在，但当前上下文对象已经产生变化，也重新创建类实例对象
			if( mContext == null)
			{
				singleContactDBService = new ContactDBService(context);
			}
		
		return singleContactDBService;
	}
	
	/***
	 * 添加联系人
	 */
	public void addContact(Contact contact){
		Log.e("插入数据",contact.toString());

		if(contact!=null)
			mContactDAO.createOrUpdate(contact);
		else
			Log.e(TAG, "addContact(Contact contact)" + "contact=null");
	}
	
	/***
	 * 获取会话列表
	 * @return
	 */
	public List<Contact> getContactList() {
		// TODO Auto-generated method stub
		List<Contact> list = mContactDAO.queryForAll();
		List<Contact> choosedlist = new ArrayList<Contact>();

		String myImId = AccountDataService.getSingleAccountDataService(MyApplication.getContext()).getUserAccId();
		for(Contact contact:list){
			Log.i("最近联系人列表","contact:" + contact.toString());
			if(contact.getMyImIdPlusContactImiId().contains(myImId)){
				choosedlist.add(contact);
			}
		}

		return choosedlist;
	}


	/***
	 * 根据会话id获取会话对象，若contactAccId为null或无与contactAccId对应的会话对象，则返回null
	 * @param contactAccId
	 * @return
	 */
	/*public Contact getContact(String contactAccId) {

		if(contactAccId == null || "".equals(contactAccId))
		{
			Log.e(TAG, "getContact(String contactAccId),contactAccId=null");
			return null;
		}

		Contact contact = new Contact();

		//以省名作为检索条件
		contact.contactAccid = contactAccId;

		List<Contact> contactList=mContactDAO.queryForMatchingArgs(contact);

		return contactList.size()>0?contactList.get(0):null;
	}*/

	public void resetUnreadNum(String myImIdPlusContactImiId) {

		Log.d("resetUnreadNum","myImIdPlusContactImiId:" + myImIdPlusContactImiId);
		Contact contact = getContactById(myImIdPlusContactImiId);
		if(contact == null){
			Log.e("重置信息未读数错误","无此" + myImIdPlusContactImiId + "的会话");
		}
		else{
			Log.d("重置信息未读数错误","---------已经重置---------"+ contact.toString());
			contact.unreadedNum = 0;
			mContactDAO.update(contact);
		}
	}


	public Contact getContactById(String id){
		Contact contact = new Contact();

		//以处理状态作为检索条件
		contact.setMyImIdPlusContactImiId(id);
		Log.e("getContactById", "id:" + id);
		List<Contact> contactList=mContactDAO.queryForMatchingArgs(contact);

		return (contactList.size()>0)?contactList.get(0):null;
	}


	/***
	 * 获取总未读数
	 * @return
	 */
	public int getUnReadNums(){
		int totalUnReadNum = 0;
		String myImId = AccountDataService.getSingleAccountDataService(MyApplication.getContext()).getUserAccId();

		List<Contact> list = mContactDAO.queryForAll();
		for(Contact contact:list){
			Log.i("最近联系人列表","contact:" + contact.toString());
			if(contact.getMyImIdPlusContactImiId().startsWith(myImId)){
				totalUnReadNum += contact.getUnreadedNum();
			}
		}

		return totalUnReadNum;
	}
}

