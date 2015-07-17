package com.online.market.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.util.Log;

public class DateUtil {
	
	public static String getDate(String time){
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date date=format.parse(time);
			Log.d("majie", ""+date.getHours());
			date.setTime(date.getTime()+30*60*1000);
			return format.format(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

}
