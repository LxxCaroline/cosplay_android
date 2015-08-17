package com.think.linxuanxuan.ecos.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.think.linxuanxuan.ecos.activity.MyApplication;

/**   
 * @Title: Contact.java 
 * @Description: 
 * @author enlizhang   
 * @date 2015年7月29日 上午9:34:16 
 */
@DatabaseTable(tableName = "contact")
public class Contact {

	/*** 本人imid和对方imid */
	@DatabaseField(id = true)
	private String MyImIdPlusContactImiId;

	/*** 联系人云信((聊天对象))accid */
	@DatabaseField
	public String contactAccid;
	
	/*** 联系人(聊天对象)用户id */
	@DatabaseField
	public String contactUserId;
	
	/*** 联系人(聊天对象)用户昵称 */
	@DatabaseField
	public String contactNickName;

	/*** 联系人(聊天对象)头像url */
	@DatabaseField
	public String avatarUrl;
	
	/*** 聊天信息id */
	@DatabaseField
	public String messgeId;
	
	/** 最近一条聊天信息*/
	@DatabaseField
	public String messageContent;
	
	/** 最近一条聊天信息发送者id */
	@DatabaseField
	public String fromAccount;
	
	/** 该条信息聊天时间 */
	@DatabaseField
	public long time;
	
	/*** 与该联系人会话的信息未读数 */
	@DatabaseField
	public int unreadedNum;
	
	/***
	 * 判断最近一条信息是否来自自己
	 * @return
	 */
	public boolean isFrmeMySelf(){
		String myAccid = AccountDataService.getSingleAccountDataService(MyApplication.getContext()).getUserAccId();
		return contactAccid.equals(myAccid);
	}


	@Override
	public String toString() {
		return "Contact{" +
				"MyImIdPlusContactImiId='" + MyImIdPlusContactImiId + '\'' +
				", contactAccid='" + contactAccid + '\'' +
				", contactUserId='" + contactUserId + '\'' +
				", contactNickName='" + contactNickName + '\'' +
				", messgeId='" + messgeId + '\'' +
				", messageContent='" + messageContent + '\'' +
				", avatarUrl='" + avatarUrl + '\'' +
				", messgeId='" + messgeId + '\'' +
				", messageContent='" + messageContent + '\'' +
				", fromAccount='" + fromAccount + '\'' +
				", time=" + time +
				", unreadedNum=" + unreadedNum +
				'}';
	}

	public void setId(String myImId,String contactImId){

		MyImIdPlusContactImiId = myImId + contactImId;
	}

	public String getMyImIdPlusContactImiId() {
		return MyImIdPlusContactImiId;
	}

	public void setMyImIdPlusContactImiId(String myImIdPlusContactImiId) {
		MyImIdPlusContactImiId = myImIdPlusContactImiId;
	}

	public String getContactAccid() {
		return contactAccid;
	}

	public void setContactAccid(String contactAccid) {
		this.contactAccid = contactAccid;
	}

	public String getContactUserId() {
		return contactUserId;
	}

	public void setContactUserId(String contactUserId) {
		this.contactUserId = contactUserId;
	}

	public String getContactNickName() {
		return contactNickName;
	}

	public void setContactNickName(String contactNickName) {
		this.contactNickName = contactNickName;
	}

	public String getAvatarUrl() {
		return avatarUrl;
	}

	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}

	public String getMessgeId() {
		return messgeId;
	}

	public void setMessgeId(String messgeId) {
		this.messgeId = messgeId;
	}

	public String getMessageContent() {
		return messageContent;
	}

	public void setMessageContent(String messageContent) {
		this.messageContent = messageContent;
	}

	public String getFromAccount() {
		return fromAccount;
	}

	public void setFromAccount(String fromAccount) {
		this.fromAccount = fromAccount;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public int getUnreadedNum() {
		return unreadedNum;
	}

	public void setUnreadedNum(int unreadedNum) {
		this.unreadedNum = unreadedNum;
	}

	public Contact(){
	}


}

