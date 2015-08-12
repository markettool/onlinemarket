package com.online.market.beans;

import org.json.JSONException;
import org.json.JSONObject;

public class QQUser {
	
	public String nikename;
	public String gender;
	public String figureurl_qq_2;
	
	public static QQUser parse(String json){
		QQUser user=new QQUser();
		try {
			JSONObject object=new JSONObject(json);
			user.nikename=object.getString("nickname");
			user.gender=object.getString("gender");
			user.figureurl_qq_2=object.getString("figureurl_qq_2");

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return user;
	}

}
