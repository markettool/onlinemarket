package com.online.market.beans;

import java.util.List;

import cn.bmob.v3.BmobObject;

public class OrderBean extends BmobObject {
	/**付款失败*/
	public static final int STATUS_PAYFAILED=0;
	/**支付宝已付款*/
	public static final int STATUS_ALIPAY_PAYED=1;
	/**微信已付款*/
	public static final int STATUS_WEIXIN_PAYED=2;
	/**货到付款*/
	public static final int STATUS_CASHONDELIVEY=3;
	/**已送达，订单结束*/
	public static final int STAUTS_DELIVED=4;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String username;
	private String receiver;
	private String address;
	private String phonenum;
	private float price;
	private List<ShopCartaBean> shopcarts;
	private int status;
	
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
	public String getReceiver() {
		return receiver;
	}
	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}

}
