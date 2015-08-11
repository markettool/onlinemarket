package com.online.market;

import java.util.List;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.online.market.adapter.MyShopCartAdapter;
import com.online.market.beans.ShopCartaBean;
import com.online.market.utils.DialogUtil;
import com.online.market.view.xlist.XListView;

public class MyShopCartActivity extends BaseActivity {
	
	private XListView xlv;
	private TextView tvNoOrder;
	
	private Button btSumbit;
	private MyShopCartAdapter adapter;
	private List<ShopCartaBean> cartaBeans;
	
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
		
		btSumbit=(Button) findViewById(R.id.bt_submit);
		
//		mBtnTitleRight.setVisibility(View.VISIBLE);
//		mBtnTitleRight.setText("结算");
//		mBtnTitleRight.setTextColor(getResources().getColor(R.color.white));
		
		xlv=(XListView) findViewById(R.id.xlv);
		xlv.setPullRefreshEnable(false);
		tvNoOrder=(TextView) findViewById(R.id.no_order);

	}

	@Override
	protected void initData() {
		try {
			cartaBeans = dbUtils.findAll(Selector.from(ShopCartaBean.class));
			if(cartaBeans==null||cartaBeans.size()==0){
				tvNoOrder.setVisibility(View.VISIBLE);
				btSumbit.setVisibility(View.GONE);
				return;
			}
			adapter=new MyShopCartAdapter(this,cartaBeans);
			xlv.setAdapter(adapter);
		} catch (DbException e) {
			e.printStackTrace();
		}
		
	}
	
	@Override
	protected void setListeners() {
		
		xlv.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				final int pos=arg2-1;
                DialogUtil.dialog(MyShopCartActivity.this, "确认删除该商品吗？", "确认", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int arg2) {
						try {
							dbUtils.delete(cartaBeans.get(pos));
						} catch (DbException e) {
							e.printStackTrace();
						}
						cartaBeans.remove(pos);
						
						adapter.notifyDataSetChanged();
						dialog.dismiss();
						if(cartaBeans.size()==0){
							tvNoOrder.setVisibility(View.VISIBLE);
						}
					}
				}, "取消", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int arg1) {
						dialog.dismiss();
					}
				});
				return false;
			}
		});
		
		btSumbit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				startActivity(CheckoutActivity.class);
				finish();
			}
		});
		
        mImgLeft.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
	} 

}
