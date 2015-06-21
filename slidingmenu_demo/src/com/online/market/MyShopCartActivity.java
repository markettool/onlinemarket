package com.online.market;

import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.online.market.adapter.MyShopCartAdapter;
import com.online.market.beans.ShopCartaBean;
import com.online.market.view.xlist.XListView;

public class MyShopCartActivity extends BaseActivity {
	
	private XListView xlv;
//	private Button btCheckout;
	private TextView tvNoOrder;
	
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
		
		mBtnTitleRight.setVisibility(View.VISIBLE);
		mBtnTitleRight.setText("结算");
		mBtnTitleRight.setTextColor(getResources().getColor(R.color.white));
		
		xlv=(XListView) findViewById(R.id.xlv);
//		btCheckout=(Button) findViewById(R.id.checkout);
		tvNoOrder=(TextView) findViewById(R.id.no_order);

	}

	@Override
	protected void initData() {
		try {
			List<ShopCartaBean> cartaBeans = dbUtils.findAll(Selector.from(ShopCartaBean.class));
			if(cartaBeans==null||cartaBeans.size()==0){
				tvNoOrder.setVisibility(View.VISIBLE);
				mBtnTitleRight.setVisibility(View.GONE);
				return;
			}
			MyShopCartAdapter adapter=new MyShopCartAdapter(this,cartaBeans);
			xlv.setAdapter(adapter);
		} catch (DbException e) {
			e.printStackTrace();
		}
		
	}
	
	@Override
	protected void setListeners() {
		mBtnTitleRight.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				startActivity(CheckoutActivity.class);
				finish();
			}
		});
	} 

}
