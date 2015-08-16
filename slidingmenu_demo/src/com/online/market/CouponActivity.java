package com.online.market;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

import com.online.market.adapter.CouponAdapter;
import com.online.market.beans.CouponBean;
import com.online.market.utils.ProgressUtil;
import com.online.market.view.xlist.XListView;

public class CouponActivity extends BaseActivity {
	
	private XListView xlv;
	private List<CouponBean> coupons=new ArrayList<CouponBean>();
	
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
		queryCoupons();
	}
	
	private void queryCoupons(){
		ProgressUtil.showProgress(this, "");
		BmobQuery<CouponBean> query	 = new BmobQuery<CouponBean>();
		query.addWhereEqualTo("username", user.getUsername());
		query.order("status");
		query.setLimit(10);
		query.findObjects(this, new FindListener<CouponBean>() {

			@Override
			public void onSuccess(List<CouponBean> object) {
				ProgressUtil.closeProgress();
				coupons.addAll(object);
				CouponAdapter adapter=new CouponAdapter(CouponActivity.this, coupons);
				xlv.setAdapter(adapter);
				
			}

			@Override
			public void onError(int code, String msg) {
				ProgressUtil.closeProgress();
				toastMsg(msg);
			}
		});	
		
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
