package com.online.market.beans;

import cn.bmob.v3.BmobObject;

public class FeedbackBean extends BmobObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String username;
	private String feedback;
	public String getUserName() {
		return username;
	}
	public void setUserName(String username) {
		this.username = username;
	}
	public String getFeedback() {
		return feedback;
	}
	public void setFeedback(String feedback) {
		this.feedback = feedback;
	}

}
