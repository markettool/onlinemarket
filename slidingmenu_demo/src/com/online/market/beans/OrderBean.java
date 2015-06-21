package com.online.market.beans;

import java.util.List;

import cn.bmob.v3.BmobObject;

public class OrderBean extends BmobObject {
	public static final int STATUS_UNDELIVED=0;
	public static final int STAUTS_DELIVED=1;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String name;
	private String address;
	private String phonenum;
	private float price;
	private List<ShopCartaBean> shopcarts;
	private int status;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getPhonenum() {
		return phonenum;
	}
	public void setPhonenum(String phonenum) {
		this.phonenum = phonenum;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public List<ShopCartaBean> getShopcarts() {
		return shopcarts;
	}
	public void setShopcarts(List<ShopCartaBean> shopcarts) {
		this.shopcarts = shopcarts;
	}
	public float getPrice() {
		return price;
	}
	public void setPrice(float price) {
		this.price = price;
	}

}
