package com.online.market;

import cn.bmob.v3.listener.SaveListener;

import com.online.market.beans.FeedbackBean;
import com.online.market.utils.ProgressUtil;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

public class FeedbackActivity extends BaseActivity{
	
	private EditText etFeedback;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feedback);
		
		initView();
		setListeners();
		initData();
	}

	@Override
	protected void initView() {
		
		etFeedback=(EditText) findViewById(R.id.et_feedback);
		mBtnTitleMiddle.setVisibility(View.VISIBLE);
		mBtnTitleMiddle.setText("投诉建议");
		mBtnTitleMiddle.setTextColor(getResources().getColor(R.color.white));
		
		mBtnTitleRight.setText("确定");
		mBtnTitleRight.setVisibility(View.VISIBLE);
		mBtnTitleRight.setTextColor(getResources().getColor(R.color.white));
		
		mImgLeft.setVisibility(View.VISIBLE);
		mImgLeft.setBackgroundResource(R.drawable.back_bg_selector);

	}

	@Override
	protected void initData() {
		if(user==null){
			startActivity(LoginActivity.class);
			finish();
			return;
		}
		
	}

	@Override
	protected void setListeners() {
		
		mImgLeft.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		mBtnTitleRight.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				String feedback=etFeedback.getText().toString();
				if(TextUtils.isEmpty(feedback)){
					toastMsg("输入为空");
					return;
				}
				ProgressUtil.showProgress(getApplicationContext(), "");
				FeedbackBean fb=new FeedbackBean();
				fb.setUserName(user.getUsername());
				fb.setFeedback(feedback);
				fb.save(FeedbackActivity.this, new SaveListener() {
					
					@Override
					public void onSuccess() {
						toastMsg("谢谢您的建议");
						ProgressUtil.closeProgress();
						finish();
					}
					
					@Override
					public void onFailure(int arg0, String arg1) {
						toastMsg("网络异常");
						ProgressUtil.closeProgress();
					}
				});
			}
		});
	}

}
