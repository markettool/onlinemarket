package com.online.market.notify;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import cn.bmob.v3.AsyncCustomEndpoints;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.CloudCodeListener;

import com.online.market.MainActivity;
import com.online.market.R;
import com.online.market.beans.MyUser;
import com.umeng.socialize.utils.Log;
/***
 * notify的工具类
 * @author MAJIE
 *
 */
public class MyNotify {
	
	public static void couponNotify(final Context context){
		MyUser user=BmobUser.getCurrentUser(context, MyUser.class);
        if(user==null){
			return;		
	    }		
        
		AsyncCustomEndpoints ace = new AsyncCustomEndpoints();
		JSONObject object=new JSONObject();
		try {
			object.put("username", user.getUsername());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		//第一个参数是上下文对象，第二个参数是云端代码的方法名称，第三个参数是上传到云端代码的参数列表（JSONObject cloudCodeParams），第四个参数是回调类
		ace.callEndpoint(context, "coupon_notify", object, 
		    new CloudCodeListener() {
		            @Override
		            public void onSuccess(Object object) {
		            	    if("fail".equals(object.toString())){
			            	    Log.d("notify","alerdy access");
		            	    	    return;
		            	    }
//		            	    NotificationManager manager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE); 
//		            	    Notification notification = new Notification(R.drawable.ic_launcher,  
//		            	    		context.getString(R.string.app_name), System.currentTimeMillis()); 
//		            	    notification.flags = Notification.FLAG_AUTO_CANCEL;//点击后自动消失
//		            	    		 PendingIntent pendingintent = PendingIntent.getActivity(context, 0,  
//		            	    				 new Intent(context, CouponActivity.class), 0);  
//		            	    		 notification.setLatestEventInfo(context, "天天在线", "您邀请的好友第一次消费，奖励你2元通用券，可直接抵扣现金。",  
//		            	    				 pendingintent); 
//		            	    manager.notify(1, notification);
		            	    notification(context, "您邀请的好友第一次消费，奖励你2元通用券，可直接抵扣现金。");
		            }
		            @Override
		            public void onFailure(int code, String msg) {
		            }
		        });
	}
	
	public static void notification(Context context,String message){
		NotificationManager manager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE); 
	    Notification notification = new Notification(R.drawable.ic_launcher,  
	    		context.getString(R.string.app_name), System.currentTimeMillis()); 
	    notification.flags = Notification.FLAG_AUTO_CANCEL;//点击后自动消失
	    notification.icon = R.drawable.ic_launcher;  
//	    notification.tickerText = "天天在线";  
	    notification.when = System.currentTimeMillis();
	    		 PendingIntent pendingintent = PendingIntent.getActivity(context, 0,  
	    				 new Intent(context, MainActivity.class), 0);  
	    		 notification.setLatestEventInfo(context, "天天在线",  message,
	    				 pendingintent); 
	    manager.notify(1, notification);
	}

}
