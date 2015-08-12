package com.online.market.adapter.base;


import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;
import android.widget.Toast;
import cn.bmob.v3.BmobUser;

import com.bmob.utils.BmobLog;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.online.market.beans.MyUser;
import com.online.market.utils.BitmapHelp;

public abstract class MyBaseAdapter<T> extends BaseAdapter {
	protected List<T> list;
 	protected Context mContext;
	protected LayoutInflater mInflater;
	protected BitmapUtils bitmapUtils;
	protected BitmapDisplayConfig config;
	protected DbUtils dbUtils;
	protected MyUser user;
	
	protected MyBaseAdapter(Context context,List<T> list) {
		this.mContext=context;
		this.list=list;
		mInflater=LayoutInflater.from(context);
		user=BmobUser.getCurrentUser(context, MyUser.class);
		bitmapUtils=BitmapHelp.getBitmapUtils(context);
		config=BitmapHelp.getDisplayConfig(mContext, 150, 150);
		dbUtils=DbUtils.create(context);
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	public void ShowLog(String msg) {
		BmobLog.i(msg);
	}

	/** toast **/
	protected void toastMsg(int msgId) {
		toastMsg(mContext.getString(msgId));
	}
	
	/** toast **/
	public void toastMsg(String message) {
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
	}
}
