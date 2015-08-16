package com.online.market;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

import com.online.market.adapter.MyOrderAdapter;
import com.online.market.beans.OrderBean;
import com.online.market.utils.ProgressUtil;
import com.online.market.view.xlist.XListView;

public class MyOrderActivity extends BaseActivity {
	
	private XListView xlv;
	private List<OrderBean> orders=new ArrayList<OrderBean>();
	private TextView tvNoOrder;
	private MyOrderAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_myorder);
		
		initView();
		setListeners();
		initData();
	}

	@Override
	protected void initView() {

		xlv=(XListView) findViewById(R.id.xlv);
		xlv.setPullRefreshEnable(false);
		tvNoOrder=(TextView) findViewById(R.id.no_order);
	}

	@Override
	protected void initData() {
		if(user==null){
			startActivity(LoginActivity.class);
			finish();
			return;
		}

		mBtnTitleMiddle.setVisibility(View.VISIBLE);
		mBtnTitleMiddle.setText("我的订单");
		mBtnTitleMiddle.setTextColor(getResources().getColor(R.color.white));
		
		mImgLeft.setVisibility(View.VISIBLE);
		mImgLeft.setBackgroundResource(R.drawable.back_bg_selector);
		
		queryOrders();
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
	
	private void queryOrders(){
		ProgressUtil.showProgress(this, "");
		BmobQuery<OrderBean> query	 = new BmobQuery<OrderBean>();
		query.addWhereEqualTo("username", user.getUsername());
		query.order("-updatedAt");
		query.setLimit(10);
		query.findObjects(this, new FindListener<OrderBean>() {

			@Override
			public void onSuccess(List<OrderBean> object) {
				ProgressUtil.closeProgress();
				orders.addAll(object);
				setData();
				
			}

			@Override
			public void onError(int code, String msg) {
				ProgressUtil.closeProgress();
				toastMsg(msg);
			}
		});	
		
	}
	
	private void setData(){
		if(orders.size()==0){
			tvNoOrder.setVisibility(View.VISIBLE);
		}
		adapter=new MyOrderAdapter(this, orders);
		xlv.setAdapter(adapter);
	}

}
