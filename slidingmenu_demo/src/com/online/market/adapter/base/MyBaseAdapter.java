package com.online.market.adapter.base;


import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;
import android.widget.Toast;
import cn.bmob.v3.BmobUser;

import com.bmob.utils.BmobLog;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.DbUtils;
import com.online.market.beans.MyUser;
import com.online.market.opera.utils.BitmapHelp;

public abstract class MyBaseAdapter extends BaseAdapter {
	protected Context mContext;
	protected LayoutInflater mInflater;
	protected BitmapUtils bitmapUtils;
	protected DbUtils dbUtils;
	protected MyUser user;
	
	protected MyBaseAdapter(Context context) {
		this.mContext=context;
		mInflater=LayoutInflater.from(context);
		user=BmobUser.getCurrentUser(context, MyUser.class);
		bitmapUtils=BitmapHelp.getBitmapUtils(context);
		dbUtils=DbUtils.create(context);
	}
	
	Toast mToast;

	public void ShowToast(final String text) {
		if (!TextUtils.isEmpty(text)) {
			((Activity) mContext).runOnUiThread(new Runnable() {

				@Override
				public void run() {
					if (mToast == null) {
						mToast = Toast.makeText(mContext, text,
								Toast.LENGTH_SHORT);
					} else {
						mToast.setText(text);
					}
					mToast.show();
				}
			});

		}
	}

	public void ShowLog(String msg) {
		BmobLog.i(msg);
	}
	
//	protected void push(User targetUser ,String json){
//		String installationId = targetUser.getInstallId();
//		BmobPushManager bmobPush = new BmobPushManager(mContext);
//		BmobQuery<BmobInstallation> query = BmobInstallation.getQuery();
//		query.addWhereEqualTo("installationId", installationId);
//		bmobPush.setQuery(query);
//		bmobPush.pushMessage(json);		
//	}
	
//	protected void queryUserByName(String searchName,final String msg){
//		BmobQuery<User> query = new BmobQuery<User>();
//		
//		query.addWhereEqualTo("username", searchName);
//		query.findObjects(mContext, new FindListener<User>() {
//
//			@Override
//			public void onSuccess(List<User> object) {
//
//				if(object.size()!=0){
//					action(object.get(0), msg);
//				}
//			}
//
//			@Override
//			public void onError(int code, String msg) {
//			}
//		});
//	}
	
//	/**��ѯ���û��ĺ��������д������*/
//	public void action(User user,String msg){
//		
//	}
	

}
