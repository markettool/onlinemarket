package com.online.market.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
	
	public static String getDate(String str){
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date date=format.parse(str);
//			Log.d("majie", ""+date.getHours());
			date.setTime(date.getTime()+30*60*1000);
			Date currentDate=new Date();
			long currentTime=currentDate.getTime();
			long time=date.getTime()-currentTime;
			if(time<0){
				
				return "订单已超时 下单时间 "+str;
			}else{
				date.setTime(time);
				SimpleDateFormat f=new SimpleDateFormat("预计在 mm分ss秒 之内送达");
				return f.format(date);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

}
