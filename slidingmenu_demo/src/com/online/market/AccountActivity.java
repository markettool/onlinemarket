package com.online.market;
//package org.markettool.opera;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import org.markettool.opera.R;
//import org.markettool.opera.beans.MyUser;
//import org.markettool.opera.beans.OperaBean;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.Button;
//import android.widget.TextView;
//import cn.bmob.v3.BmobObject;
//import cn.bmob.v3.BmobQuery;
//import cn.bmob.v3.BmobUser;
//import cn.bmob.v3.listener.FindListener;
//import cn.bmob.v3.listener.UpdateListener;
//
//public class AccountActivity extends BaseActivity {
//	
//	private TextView tvTotalFund;
////	private Button btGetCash;
//	private MyUser myUser;
//	private float totalFund;
//	
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_account);
//		
//		initView();
//		setListeners();
//		initData();
//	}
//
//	@Override
//	protected void initView() {
//		tvTotalFund=(TextView) findViewById(R.id.total_fund);
////		btGetCash=(Button) findViewById(R.id.getcash);
//		
//		mBtnTitleMiddle.setVisibility(View.VISIBLE);
//		mBtnTitleMiddle.setText("我的账户");
//		mBtnTitleMiddle.setTextColor(getResources().getColor(R.color.white));
//		
//		mImgLeft.setVisibility(View.VISIBLE);
//		mImgLeft.setBackgroundResource(R.drawable.bt_back_dark);
//		
//		mBtnTitleRight.setVisibility(View.VISIBLE);
//		mBtnTitleRight.setText("提现");
//		mBtnTitleRight.setTextColor(getResources().getColor(R.color.white));
//		
//	}
//	
//	private void setListeners(){
//		mBtnTitleRight.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View arg0) {
//				if(totalFund<10){
//					toastMsg("低于10元不能提现");
//					return;
//				}else{
////					startActivity(c);
//					Intent intent=new Intent(AccountActivity.this, GetCashActivity.class);
//					intent.putExtra("fund", totalFund);
//					startActivity(intent);
//				}
//			}
//		});
//		
//        mImgLeft.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View arg0) {
//				finish();
//			}
//		});
//	}
//
//	@Override
//	protected void initData() {
//		myUser=BmobUser.getCurrentUser(this, MyUser.class);
//		if(myUser==null){
//			startActivity(LoginActivity.class);
//			finish();
//			return;
//		}
//		totalFund=myUser.getFund();
//		
//	}
//	
//	@Override
//	protected void onResume() {
//		super.onResume();
//		myUser=BmobUser.getCurrentUser(this, MyUser.class);
//	}
//	
//	
//	/**
//	 * 批量更新
//	 */
//	private void batchUpdateOperas(List<BmobObject> object){
//		new BmobObject().updateBatch(AccountActivity.this, object, new UpdateListener() {
//			
//			@Override
//			public void onSuccess() {
//				Log.e("majie","批量更新成功");
//			}
//			
//			@Override
//			public void onFailure(int code, String msg) {
//				Log.e("majie","批量更新失败:"+msg);
//			}
//		});
//	}
//	
//	private void updateUserAccount() {
//		if (myUser != null) {
//			MyUser newUser = new MyUser();
//			newUser.setFund(totalFund);
//			newUser.setObjectId(myUser.getObjectId());
//			newUser.update(this,new UpdateListener() {
//
//				@Override
//				public void onSuccess() {
////					toastMsg("更新用户信息成功");
//				}
//
//				@Override
//				public void onFailure(int code, String msg) {
////					toastMsg("更新用户信息失败:" + msg);
//				}
//			});
//		} 
//	}
//
//
//}
