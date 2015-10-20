package com.online.market.push;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import cn.bmob.push.PushConstants;
import cn.bmob.v3.BmobUser;

import com.online.market.CouponActivity;
import com.online.market.MainActivity;
import com.online.market.beans.CouponBean;
import com.online.market.beans.MyUser;
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
		try {
			JSONObject object=new JSONObject(message);
			String type=object.getString("type");
			String extra=object.getString("extra");
			//赠送代金券
			if("give".equals(type)){ 
				MyUser myUser=BmobUser.getCurrentUser(context, MyUser.class); 
				if(myUser!=null){
					CouponBean c=new CouponBean();
					c.setUsername(myUser.getUsername());
					c.setAmount(1);
					c.setLimit(8);
					c.setViewed(0);
					c.setType(CouponBean.COUPON_TYPE_ONSALE);
					c.save(context);
					MyNotify.notification(context, extra,CouponActivity.class);
				}
			}
			//消息提示
			else if("alert".equals(type)){
				MyNotify.notification(context, extra,MainActivity.class);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

}
