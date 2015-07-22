package com.online.market;


import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import cn.bmob.v3.BmobUser;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.online.market.R;
import com.online.market.beans.MyUser;
import com.online.market.utils.ActivityControl;
import com.online.market.utils.BitmapHelp;
import com.online.market.utils.FileUtils;
import com.umeng.analytics.MobclickAgent;

/**
 * base activity
 * @author majie
 *
 */
public abstract class BaseActivity extends Activity {
	protected View mTitleView;
	protected Button mBtnTitleLeft;
	protected Button mBtnTitleMiddle;
	public Button mBtnTitleRight;
	protected ImageView mImgLeft;
	protected ImageView mImgRight;
	protected BitmapUtils bitmapUtils;
	protected MyUser user;
	protected String dir;
	protected BitmapDisplayConfig config;
	protected DbUtils dbUtils;
	protected LayoutInflater mInflater;
	protected static final int STYLE_TITLE_C8 = 0x22;
	protected static final int STYLE_TITLE_DC = 0x23;
	
	private View titleView;
	protected View titleSplitView;
	
	public View getTitleView() {
		return titleView;
	}

	/** Activity 生命周期  **/
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		mInflater = LayoutInflater.from(this);
		bitmapUtils=BitmapHelp.getBitmapUtils(this);
		config=BitmapHelp.getDisplayConfig(this, 50, 50);
		dbUtils=DbUtils.create(this);
		user=BmobUser.getCurrentUser(this, MyUser.class);
		dir=FileUtils.getSDCardRoot()+getPackageName()+File.separator;
		FileUtils.mkdirs(dir);
		ActivityControl.getInstance().addActivity(this);
	}

	@Override
	public void setContentView(int layoutResID) {
		View view = mInflater.inflate(layoutResID, null);
		setTitleStyle(true, view, null);
	}
	
	protected void setContentView(int layoutResID, boolean showTitle) {
		View view = mInflater.inflate(layoutResID, null);
		setTitleStyle(showTitle, view, null);
	}
		
	
	@Override
	public void setContentView(View view) {
		setTitleStyle(true, view, null);
	}
	
	protected void setContentView(View view, boolean showTitle){
		setTitleStyle(showTitle, view, null);
	}

	@Override
	public void setContentView(View view, LayoutParams params) {
		setTitleStyle(true, view, params);
	}
	
	protected void setContentView(View view, LayoutParams params, boolean showTitle) {
		setTitleStyle(showTitle, view, params);
	}
	
	private void setTitleStyle(boolean showTitle, View content, LayoutParams params){
		LinearLayout rootLayout = new LinearLayout(this);
		rootLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		rootLayout.setOrientation(LinearLayout.VERTICAL);
		
		if(showTitle){
			titleView = mInflater.inflate(R.layout.layout_base_title, null);
			rootLayout.addView(titleView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			mBtnTitleLeft = (Button) titleView.findViewById(R.id.button_left);
			mBtnTitleRight = (Button) titleView.findViewById(R.id.button_right);
			mBtnTitleMiddle = (Button) titleView.findViewById(R.id.button_middle);
			titleSplitView = titleView.findViewById(R.id.title_split);
			mImgLeft = (ImageView) titleView.findViewById(R.id.image_left);
			mImgRight = (ImageView) titleView.findViewById(R.id.image_right);
			mTitleView = titleView;
			titleView.setVisibility(View.VISIBLE);
		}
		rootLayout.addView(content, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		setRootView(rootLayout);
	}
	
	private void setRootView(View view){
		super.setContentView(view);
	}
	
	
	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		user=BmobUser.getCurrentUser(this, MyUser.class);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		ActivityControl.getInstance().removeActivity(this);
	}
	
	/** 自定义方法 **/
	
	/** 初始化view, 该方法做findViewById, setOnClickListener 事情 */
	protected abstract void initView();
	
	/** 初始view数据, 做setText, setAdapter 事情*/
	protected abstract void initData();
	
	/** toast **/
	protected void toastMsg(int msgId) {
		toastMsg(getString(msgId));
	}
	
	/** toast **/
	public void toastMsg(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
	}
	
	private int titleHeight = 0;
	//原来位置 得到高度为0
	protected int getTitleHeight(){
		return mTitleView.getHeight();
	}
	
	/**
	 * 跳转
	 * @param action
	 * @param extras
	 */
	protected void startActivity(String action, Bundle extras) {
		Intent intent = new Intent();
		intent.setAction(action);
		if (null != extras) {
			intent.putExtras(extras);
		}
		startActivity(intent);
	}
	
	protected void setTitleStyle(int style){
//		if(style == STYLE_TITLE_C8){
//			titleSplitView.setBackgroundColor(getResources().getColor(R.color.cor_c8c8c8));
//		} else if(style == STYLE_TITLE_DC){
//			titleSplitView.setBackgroundColor(getResources().getColor(R.color.cor_dcdcdc));
//		}
	}
	
	/**
	 * 跳转
	 * @param c
	 */
	protected void startActivity(Class c) {
		startActivity(c, null);
	}
	
	protected abstract void setListeners();
	
	/**
	 * 跳转
	 * @param c
	 * @param extras
	 */
	protected void startActivity(Class c, Bundle extras) {
		Intent intent = new Intent();
		intent.setClass(this, c);
		if (null != extras) {
			intent.putExtras(extras);
		}
		startActivity(intent);
	}
	
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}
	
}