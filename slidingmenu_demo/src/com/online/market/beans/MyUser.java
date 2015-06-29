package com.online.market.beans;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;

public class MyUser extends BmobUser {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String nickname;
	private Boolean gender;
	private BmobFile avatar;
	private int age;
	
	public BmobFile getAvatar() {
		return avatar;
	}
	public void setAvatar(BmobFile avatar) {
		this.avatar = avatar;
	}
	public Boolean getGender() {
		return gender;
	}
	public void setGender(Boolean gender) {
		this.gender = gender;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String realname) {
		this.nickname = realname;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	
}
