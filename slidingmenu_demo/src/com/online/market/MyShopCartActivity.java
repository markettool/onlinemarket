package com.online.market;

import com.online.market.adapter.MyShopCartAdapter;
import com.online.market.view.xlist.XListView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MyShopCartActivity extends BaseActivity {
	
	private XListView xlv;
	private Button btCheckout;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_myshopcart);
		
		initView();
		initData();
		setListeners();
	}

	@Override
	protected void initView() {
		mBtnTitleMiddle.setVisibility(View.VISIBLE);
		mBtnTitleMiddle.setText("我的购物车");
		mBtnTitleMiddle.setTextColor(getResources().getColor(R.color.white));
		
		mImgLeft.setVisibility(View.VISIBLE);
		mImgLeft.setBackgroundResource(R.drawable.back_bg_selector);
		mImgLeft.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		
		xlv=(XListView) findViewById(R.id.xlv);
		btCheckout=(Button) findViewById(R.id.checkout);

	}

	@Override
	protected void initData() {
		
		MyShopCartAdapter adapter=new MyShopCartAdapter(this);
		xlv.setAdapter(adapter);
	}
	
	@Override
	protected void setListeners() {
		btCheckout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				startActivity(CheckoutActivity.class);
			}
		});
	} 

}
