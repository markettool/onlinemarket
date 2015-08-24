package com.online.market.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

/**
 * @author majie
 *
 */
public class SharedPrefUtil {

	private SharedPreferences mPreferences;
	
	public SharedPrefUtil(Context context, String prereceName) {
		if(null == context || TextUtils.isEmpty(prereceName)) {
			return;
		}
		
		mPreferences = context.getSharedPreferences(prereceName, Context.MODE_PRIVATE);
	}
	
	public String getValueByKey(String key, String defaultValue){
		return mPreferences.getString(key, defaultValue);
	}
	
	public int getIntByKey(String key, int defaultValue){
		return mPreferences.getInt(key, defaultValue);
	}
	
	public synchronized void putValueByKey(String key, String value){
		mPreferences.edit().putString(key, value).commit();
	}
	
	public synchronized void putIntByKey(String key, int value){
		mPreferences.edit().putInt(key, value).commit();
	}

	public void clear(){
		mPreferences.edit().clear().commit();
	}
	
}