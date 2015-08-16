package com.online.market.beans;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;

public class MyUser extends BmobUser {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String nickname;
	private BmobFile avatar;
//	private String DEVICE_ID;
	
	public BmobFile getAvatar() {
		return avatar;
	}
	public void setAvatar(BmobFile avatar) {
		this.avatar = avatar;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
//	public String getDEVICE_ID() {
//		return DEVICE_ID;
//	}
//	public void setDEVICE_ID(String dEVICE_ID) {
//		DEVICE_ID = dEVICE_ID;
//	}
	
}
