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
import com.online.market.beans.ReceiveAddress;
import com.online.market.beans.ShopCartaBean;
import com.online.market.utils.ProgressUtil;

public class CheckoutActivity extends BaseActivity {
	
	private EditText etReceiver;
	private EditText etAddress;
	private EditText etPhoneNum;
	
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
		
		ReceiveAddress ra=getReceiveAddress();
		if(ra!=null){
			etReceiver.setText(ra.getName());
			etAddress.setText(ra.getAddress());
			etPhoneNum.setText(ra.getPhonenum());
		}
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
				saveReceiveAddress(receiver, address, phonenum);

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
						toastMsg("您的订单已经提交成功，半小时将送达");
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
						toastMsg("提交失败");
						ProgressUtil.closeProgress();

					}
				});
			}
		});
	}
	
	/**保存收货地址*/
	private void saveReceiveAddress(String name,String address,String phonenum){
		ReceiveAddress add=getReceiveAddress();
		if(add!=null&&add.getAddress().equals(address)&&add.getName().equals(name)&&add.getPhonenum().equals(phonenum)){
			//existed,dont save
			return;
		}
		ReceiveAddress ra=new ReceiveAddress();
		ra.setName(name);
		ra.setAddress(address);
		ra.setPhonenum(phonenum);
		try {
			dbUtils.save(ra);
		} catch (DbException e) {
			e.printStackTrace();
		}
		
	}
	
	private ReceiveAddress getReceiveAddress(){
		try {
			List<ReceiveAddress> addresses=dbUtils.findAll(Selector.from(ReceiveAddress.class));
	        if(addresses!=null&&addresses.size()!=0){
	        	return addresses.get(0);
	        }
			return null;
		} catch (DbException e) {
			e.printStackTrace();
		}
		return null;
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
