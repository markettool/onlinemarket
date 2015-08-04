package com.online.market.beans;

import cn.bmob.v3.BmobObject;

public class PromotionBean extends BmobObject{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private float amount;
	private String username;
	private String name;
	private float limit; 
	
	public float getAmount() {
		return amount;
	}
	public void setAmount(float amount) {
		this.amount = amount;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public float getLimit() {
		return limit;
	}
	public void setLimit(float limit) {
		this.limit = limit;
	}

}
