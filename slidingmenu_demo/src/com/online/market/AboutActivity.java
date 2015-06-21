package com.online.market;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import cn.bmob.v3.update.BmobUpdateAgent;

import com.online.market.R;

public class AboutActivity extends BaseActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		
		initView();
		initData();
		setListeners();
	}

	@Override
	protected void initView() {
		mBtnTitleMiddle.setVisibility(View.VISIBLE);
		mBtnTitleMiddle.setText("关于");
		mBtnTitleMiddle.setTextColor(getResources().getColor(R.color.white));
		
		mImgLeft.setVisibility(View.VISIBLE);
		mImgLeft.setBackgroundResource(R.drawable.back_bg_selector);
		
	}

	@Override
	protected void initData() {
		BmobUpdateAgent.update(this);
	}

	@Override
	protected void setListeners() {
        mImgLeft.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
	}

}
