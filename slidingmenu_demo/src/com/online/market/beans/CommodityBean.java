package com.online.market.beans;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

public class CommodityBean extends BmobObject{
	public static int STATUS_NORMAL=0;
	/**下架*/
	public static int STATUS_OFFSALE=1;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String name;
	private float price;
	private BmobFile pics;
	private int sold;
	private int status;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public float getPrice() {
		return price;
	}
	public void setPrice(float price) {
		this.price = price;
	}
	public BmobFile getPics() {
		return pics;
	}
	public void setPics(BmobFile pics) {
		this.pics = pics;
	}
	public int getSold() {
		return sold;
	}
	public void setSold(int sold) {
		this.sold = sold;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}

}
