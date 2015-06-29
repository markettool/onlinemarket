package com.online.market;

import java.util.ArrayList;
import java.util.List;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.online.market.beans.CommodityBean;
import com.online.market.beans.OrderBean;
import com.online.market.beans.ShopCartaBean;
import com.online.market.utils.ProgressUtil;

public class CheckoutActivity extends BaseActivity {
	
	private EditText etReceiver;
	private EditText etAddress;
	private EditText etPhoneNum;
//	private Button btSubmit;
	
	protected void onCreate(android.os.Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_checkout);
		
		initView();
		initData();
		setListeners();
	};

	@Override
	protected void initView() {

		etReceiver=(EditText) findViewById(R.id.name);
		etAddress=(EditText) findViewById(R.id.address);
		etPhoneNum=(EditText) findViewById(R.id.phonenum);
		
		if(user!=null){
			etReceiver.setHint(user.getNickname());
			etPhoneNum.setHint(user.getUsername());
		}
//		btSubmit=(Button) findViewById(R.id.submit);

	}

	@Override
	protected void initData() {
		if(user==null){
			startActivity(LoginActivity.class);
			finish();
			return;
		}
		mBtnTitleMiddle.setVisibility(View.VISIBLE);
		mBtnTitleMiddle.setText("结算页");
		mBtnTitleMiddle.setTextColor(getResources().getColor(R.color.white));
		
		mImgLeft.setVisibility(View.VISIBLE);
		mImgLeft.setBackgroundResource(R.drawable.back_bg_selector);
		mImgLeft.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		
		mBtnTitleRight.setText("确定");
		mBtnTitleRight.setVisibility(View.VISIBLE);
		mBtnTitleRight.setTextColor(getResources().getColor(R.color.white));
	}

	@Override
	protected void setListeners() {
		mBtnTitleRight.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				String receiver=etReceiver.getText().toString();
				if(TextUtils.isEmpty(receiver)){
					receiver=etReceiver.getHint().toString();
				}
				String address=etAddress.getText().toString();
				if(TextUtils.isEmpty(address)){
					toastMsg("address is null");
					return;
				}
				String phonenum=etPhoneNum.getText().toString();
				if(TextUtils.isEmpty(phonenum)){
					phonenum=etPhoneNum.getHint().toString();
				}
				OrderBean bean=new OrderBean();
				bean.setReceiver(receiver);
				bean.setUsername(user.getUsername());
				bean.setAddress(address);
				bean.setPhonenum(phonenum);
				List<ShopCartaBean> shopcarts=null;
				try {
					shopcarts=dbUtils.findAll(Selector.from(ShopCartaBean.class));
				    bean.setShopcarts(shopcarts);
				    float price=0;
				    for(ShopCartaBean p:shopcarts){
				    	price+=p.getPrice();
				    }
				    bean.setPrice(price);
				} catch (DbException e) {
					e.printStackTrace();
				}
				ProgressUtil.showProgress(getApplicationContext(), "");
				final 	List< ShopCartaBean> carts=shopcarts;

				bean.save(CheckoutActivity.this, new SaveListener() {
					
					@Override
					public void onSuccess() {
						toastMsg("submit success");
						updateSold(carts);
						try {
							dbUtils.deleteAll(carts);
						} catch (DbException e) {
							e.printStackTrace();
						}
						finish();
						ProgressUtil.closeProgress();
					}
					
					@Override
					public void onFailure(int arg0, String arg1) {
						toastMsg("submit fail");
						ProgressUtil.closeProgress();

					}
				});
			}
		});
	}
	
	private void updateSold(List<ShopCartaBean> carts){
		List<BmobObject > commodityBeans=new ArrayList<BmobObject>();
		for(ShopCartaBean cart:carts){
			CommodityBean cb=new CommodityBean();
			cb.setObjectId(cart.getId());
			cb.setSold(cart.getSold()+cart.getNumber());
			cb.setPrice(cart.getPrice());
			commodityBeans.add(cb);
		}
		new BmobObject().updateBatch(this, commodityBeans, new UpdateListener() {
			
			@Override
			public void onSuccess() {
				Log.d("majie", "success");
			}
			
			@Override
			public void onFailure(int arg0, String arg1) {
				Log.d("majie", "fail");
			}
		});
		
	}

}
