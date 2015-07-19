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
	
	public BmobFile getAvatar() {
		return avatar;
	}
	public void setAvatar(BmobFile avatar) {
		this.avatar = avatar;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String realname) {
		this.nickname = realname;
	}
	
}
