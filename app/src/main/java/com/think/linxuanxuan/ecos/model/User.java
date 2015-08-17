package com.think.linxuanxuan.ecos.model;

import java.util.LinkedHashSet;
import java.util.Set;

/***
 *
 * @ClassName: User
 * @Description: 用户信息
 * @author enlizhang
 * @date 2015年7月25日 下午11:30:09
 *
 */
public class User {

	/*** 用户id */
	public String userId;

	/*** 云信id */
	public String imId;

	/*** 云信id */
	public String imToken;

	/** 手机号 */
	public String phone;

	/** 密码  */
	public String password;

	/** 昵称  */
	public String nickname;

	/** 头像 url */
	public String avatarUrl;

	/** 性别  */
	public Gender gender ;

	/** 个性签名 */
	public String characterSignature;

	/** 个人角色（可以多个） */
	public Set<RoleType> roleTypeSet;

	/** 城市名称  */
	public String cityName;

	/** 城市code  */
	public String cityCode;

	/** 主页封面(背景图）url  */
	public String coverUrl;

	/** 用户地理位置  */
	//	public Location ;

	/** 关注数 */
	public String followOtherNum ;

	/** 粉丝数  */
	public String fansNum;


	public User(){
		gender = Gender.女;
		roleTypeSet =  new LinkedHashSet<RoleType>();
	}

	public String getSortRoleString(){
		String string = "";
		for(RoleType role:roleTypeSet)
			string+=role.getBelongs()+",";

		return string;
	}

	public Set<RoleType> getRoleTypeByString(String string){

		for(String s:string.split(",")){
			if("".equals(s))
				continue;
			RoleType roleType = RoleType.getRoleTypeByValue(s);
			if(roleType!=null)
				roleTypeSet.add(roleType);
		}
		return roleTypeSet;
	}

	/***
	 *
	 * @ClassName: Gender
	 * @Description: 性别，包括男、女、暂无
	 * @author enlizhang
	 * @date 2015年7月25日 下午11:30:19
	 *
	 */
	public static enum Gender {

		暂无("0"),
		男("1"),
		女("2");

		private String value;

		private Gender(String _value ) {
			this.value = _value;
		}

		public String getValue() {
			return value;
		}


		public static Gender getGender(String value){

			for(Gender gender:Gender.values()){
				if(gender.getValue().equals(value))
					return gender;
			}

			return null;
		}

	}

	/***
	 *
	 * @ClassName: Role
	 * @Description: 角色类型，包括coser、摄影、妆娘、后期、裁缝、道具师
	 * @author enlizhang
	 * @date 2015年7月25日 下午11:30:42
	 *
	 */
	public static enum RoleType {

		coser("0"),
		妆娘("1"),
		摄影("2"),
		后期("3"),
		裁缝("4"),
		道具("5");

		private String value;

		private RoleType(String _value ) {
			this.value = _value;
		}

		public String getBelongs() {
			return value;
		}

		public static RoleType getRoleTypeByValue(String value){

			for(RoleType roleType:RoleType.values()){
				if(roleType.getBelongs().equals(value))
					return roleType;
			}

			return null;
		}
	}

	@Override
	public String toString() {
		return "User{" +
				"userId='" + userId + '\'' +
				", imId='" + imId + '\'' +
				", imToken='" + imToken + '\'' +
				", phone='" + phone + '\'' +
				", password='" + password + '\'' +
				", nickname='" + nickname + '\'' +
				", avatarUrl='" + avatarUrl + '\'' +
				", gender=" + gender +
				", characterSignature='" + characterSignature + '\'' +
				", roleTypeSet=" + roleTypeSet +
				", cityName='" + cityName + '\'' +
				", cityCode='" + cityCode + '\'' +
				", coverUrl='" + coverUrl + '\'' +
				", followOtherNum='" + followOtherNum + '\'' +
				", fansNum='" + fansNum + '\'' +
				'}';
	}
}
