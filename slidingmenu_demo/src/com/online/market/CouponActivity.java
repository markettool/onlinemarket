package com.online.market;

import java.util.ArrayList;
import java.util.List;

import com.online.market.adapter.CouponAdapter;
import com.online.market.beans.CouponBean;
import com.online.market.view.xlist.XListView;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class CouponActivity extends BaseActivity {
	
	private XListView xlv;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_coupon);
		initView();
		setListeners();
		initData();
	}

	@Override
	protected void initView() {

		mBtnTitleMiddle.setText("优惠券");
		mBtnTitleMiddle.setVisibility(View.VISIBLE);
		mBtnTitleMiddle.setTextColor(getResources().getColor(R.color.white));
		mImgLeft.setVisibility(View.VISIBLE);
		mImgLeft.setBackgroundResource(R.drawable.back_bg_selector);
		xlv=(XListView) findViewById(R.id.xlv);
		xlv.setPullRefreshEnable(false);
	}

	@Override
	protected void initData() {

		List<CouponBean> list=new ArrayList<CouponBean>();
		CouponBean bean=new CouponBean();
		bean.setType(CouponBean.COUPON_TYPE_ONSALE);;
		bean.setAmount(5);
		bean.setLimit(25);
		bean.setStatus(CouponBean.COUPON_STATUS_CONSUMED);
		list.add(bean);
		
		CouponAdapter adapter=new CouponAdapter(this, list);
		xlv.setAdapter(adapter);
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
