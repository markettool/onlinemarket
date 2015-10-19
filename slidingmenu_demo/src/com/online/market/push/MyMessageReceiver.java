package com.online.market.push;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import cn.bmob.push.PushConstants;

import com.online.market.R;
import com.online.market.notify.MyNotify;

public class MyMessageReceiver extends BroadcastReceiver {

	@SuppressWarnings("deprecation")
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		// 获取推送消息
		String message = intent.getStringExtra(PushConstants.EXTRA_PUSH_MESSAGE_STRING);
		Log.i("onlinemarket", "收到的推送消息："+message);
		// 发送通知
//		NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//		
//		Notification n = new Notification();  
//        n.icon = R.drawable.ic_launcher;  
//        n.tickerText = "天天在线";  
//        n.when = System.currentTimeMillis();  
//        //n.flags=Notification.FLAG_ONGOING_EVENT;  
//        Intent i = new Intent();  
//        PendingIntent pi = PendingIntent.getActivity(context, 0, i, 0);  
//        n.setLatestEventInfo(context, "消息", message, pi);  
//        n.defaults |= Notification.DEFAULT_SOUND;
//        n.flags = Notification.FLAG_AUTO_CANCEL;
//        nm.notify(1, n);
		try {
			JSONObject object=new JSONObject(message);
			String alert=object.getString("alert");
			MyNotify.notification(context, alert);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

}
