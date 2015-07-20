package com.online.market;

import java.util.ArrayList;
import java.util.List;

import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

import com.bmob.pay.tool.BmobPay;
import com.bmob.pay.tool.PayListener;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.online.market.beans.CommodityBean;
import com.online.market.beans.OrderBean;
import com.online.market.beans.ReceiveAddress;
import com.online.market.beans.ShopCartaBean;
import com.online.market.utils.ProgressUtil;

public class CheckoutActivity extends BaseActivity {
	public static final int PAYMETHOD_ALIPAY=0;
	public static final int PAYMETHOD_WEIXINPAY=1;
	public static final int PAYMETHOD_CASH=2;
	
	private String [] paymethods={"支付宝支付","微信支付","货到付款"};
	private String [] addresses={"海淀","昌平","太原","五台"};
	private int paymethod;
	
	private EditText etReceiver;
	private Spinner addressSpinner;
	private EditText etPhoneNum;
	private Spinner payMethodSpinner;
	
	private List<ShopCartaBean> shopcarts=null;
	
	private String receiver;
	private String address;
	private String phonenum;
	
	protected void onCreate(android.os.Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_checkout);
		
		initView();
		initData();
		setListeners();
	};

	@Override
	protected void initView() {
		mBtnTitleMiddle.setVisibility(View.VISIBLE);
		mBtnTitleMiddle.setText("结算页");
		mBtnTitleMiddle.setTextColor(getResources().getColor(R.color.white));
		
		mBtnTitleRight.setText("确定");
		mBtnTitleRight.setVisibility(View.VISIBLE);
		mBtnTitleRight.setTextColor(getResources().getColor(R.color.white));
		
		mImgLeft.setVisibility(View.VISIBLE);
		mImgLeft.setBackgroundResource(R.drawable.back_bg_selector);

		etReceiver=(EditText) findViewById(R.id.name);
		addressSpinner=(Spinner) findViewById(R.id.address);
		etPhoneNum=(EditText) findViewById(R.id.phonenum);
		payMethodSpinner=(Spinner) findViewById(R.id.pay_method);
		
		ArrayAdapter< String> paymethodAdapter=new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, paymethods);
		paymethodAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); 
		payMethodSpinner.setAdapter(paymethodAdapter);
		
		ArrayAdapter< String> addressAdapter=new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, addresses);
		addressAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); 
		addressSpinner.setAdapter(addressAdapter);
		
		ReceiveAddress ra=getReceiveAddress();
		if(ra!=null){
			etReceiver.setText(ra.getName());
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
		
		try {
			shopcarts=dbUtils.findAll(Selector.from(ShopCartaBean.class));
		} catch (DbException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void setListeners() {
        mImgLeft.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		mBtnTitleRight.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				receiver=etReceiver.getText().toString();
				if(TextUtils.isEmpty(receiver)){
					receiver=etReceiver.getHint().toString();
				}
				if(TextUtils.isEmpty(address)){
					toastMsg("address is null");
					return;
				}
				phonenum=etPhoneNum.getText().toString();
				if(TextUtils.isEmpty(phonenum)){
					phonenum=etPhoneNum.getHint().toString();
				}

				String detail="";
				double price=0;
				if(shopcarts!=null){
					for(ShopCartaBean cart:shopcarts){
						if(cart.getNumber()!=0){
							
							detail=detail+cart.getName()+" and ";
							price+=(cart.getPrice()*cart.getNumber());
						}
						
					}
				}
				int index=detail.lastIndexOf(" and ");
				detail=detail.substring(0, index);
				
				ProgressUtil.showProgress(CheckoutActivity.this, "");
				saveReceiveAddress(receiver, address, phonenum);
				if(paymethod==PAYMETHOD_CASH){
                    submitOrder(OrderBean.PAYMETHOD_CASHONDELIVEY);
				}else if(paymethod==PAYMETHOD_ALIPAY){
					payByAlipay(price, detail);
				}else if(paymethod==PAYMETHOD_WEIXINPAY){
					payByWeixin(price, detail);
				}

			}
		});
		
		payMethodSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				paymethod=arg2;
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				
			}
		});
		
		addressSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				address=addresses[arg2];
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				
			}
		});
	}
	
	@Override
	protected void onPause() {
		super.onStop();
		ProgressUtil.closeProgress();
	}
	
	/**alipay*/
	private void payByAlipay(double fund,String detail){
		new BmobPay(this).pay(fund, detail, payListener);
	}
	
	private void payByWeixin(double fund,String detail){
		new BmobPay(this).payByWX(fund, detail, payListener);
	}
	
	private PayListener payListener=new PayListener() {
		
		@Override
		public void unknow() {
			toastMsg("未知错误");
		}
		
		@Override
		public void succeed() {
			if(paymethod==PAYMETHOD_ALIPAY){
				submitOrder(OrderBean.PAYMETHOD_ALIPAY_PAYED);
			}else if(paymethod==PAYMETHOD_WEIXINPAY){
				submitOrder(OrderBean.PAYMETHOD_WEIXIN_PAYED);
			}
		}
		
		@Override
		public void orderId(String arg0) {
			
		}
		
		@Override
		public void fail(int arg0, String arg1) {
			toastMsg("付款失败");
		}
	};
	
	private void submitOrder(int paymethod){
		OrderBean bean=new OrderBean();
		bean.setReceiver(receiver);
		bean.setUsername(user.getUsername());
		bean.setAddress(address);
		bean.setDispatcher("untreated");
		bean.setPacker("untreated");
		bean.setPhonenum(phonenum);
		bean.setShopcarts(shopcarts);
		bean.setPayMethod(paymethod);
	    float price=0;
	    for(ShopCartaBean p:shopcarts){
	    	price+=p.getPrice();
	    }
	    bean.setPrice(price);    
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
	
	/**得到保存在数据库的收货地址*/
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
//				Log.d("majie", "success");
			}
			
			@Override
			public void onFailure(int arg0, String arg1) {
//				Log.d("majie", "fail");
			}
		});
		
	}

}
