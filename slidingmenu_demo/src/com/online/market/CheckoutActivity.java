package com.online.market;

import java.util.List;

import cn.bmob.v3.listener.SaveListener;

import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.online.market.beans.OrderBean;
import com.online.market.beans.ShopCartaBean;
import com.online.market.utils.ProgressUtil;

import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class CheckoutActivity extends BaseActivity {
	
	private EditText etName;
	private EditText etAddress;
	private EditText etPhoneNum;
	private Button btSubmit;
	
	protected void onCreate(android.os.Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_checkout);
		
		initView();
		initData();
		setListeners();
	};

	@Override
	protected void initView() {

		etName=(EditText) findViewById(R.id.name);
		etAddress=(EditText) findViewById(R.id.address);
		etPhoneNum=(EditText) findViewById(R.id.phonenum);
		btSubmit=(Button) findViewById(R.id.submit);

	}

	@Override
	protected void initData() {
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
	}

	@Override
	protected void setListeners() {
		btSubmit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				String name=etName.getText().toString();
				if(TextUtils.isEmpty(name)){
					toastMsg("name is null");
					return;
				}
				String address=etAddress.getText().toString();
				if(TextUtils.isEmpty(address)){
					toastMsg("address is null");
					return;
				}
				String phonenum=etPhoneNum.getText().toString();
				if(TextUtils.isEmpty(phonenum)){
					toastMsg("phonenum is null");
					return;
				}
				OrderBean bean=new OrderBean();
				bean.setName(name);
				bean.setAddress(address);
				bean.setPhonenum(phonenum);
				try {
					List<ShopCartaBean> shopcarts=dbUtils.findAll(Selector.from(ShopCartaBean.class));
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
				bean.save(CheckoutActivity.this, new SaveListener() {
					
					@Override
					public void onSuccess() {
						toastMsg("submit success");
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

}
