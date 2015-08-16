package com.online.market;

import java.util.ArrayList;
import java.util.List;

import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

import com.bmob.pay.tool.BmobPay;
import com.bmob.pay.tool.PayListener;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.online.market.adapter.CheckCouponAdapter;
import com.online.market.beans.CommodityBean;
import com.online.market.beans.CouponBean;
import com.online.market.beans.OrderBean;
import com.online.market.beans.ShopCartaBean;
import com.online.market.utils.ProgressUtil;
import com.online.market.utils.SharedPrefUtil;

public class CheckoutActivity extends BaseActivity {
//	public static final String ADDRESS="address";
	public static final String RECEIVER="receiver";
	public static final String PHONENUM="phonenum";
	public static final String ROOMNUM="roomnum";
	
	public static final int PAYMETHOD_ALIPAY=0;
	public static final int PAYMETHOD_WEIXINPAY=1;
	public static final int PAYMETHOD_CASH=2;
	
	private String [] paymethods={"支付宝支付","微信支付","货到付款"};
	private String [] addresses={"海淀","昌平","太原","五台"};
	private int paymethod;
	
	private EditText etReceiver;
	private Spinner spAddress;
	private EditText etPhoneNum;
	private EditText etRoomNumber;
	private Spinner payMethodSpinner;
	private Button btSubmit;
	private Spinner spCoupon;
	
	private List<ShopCartaBean> shopcarts=null;
	
	private String receiver;
	private String address;
	private String phonenum;
	private String roomnum;
	/**折扣*/
	private int discountIndex;
	private float price=0;
	
	private SharedPrefUtil su;
	
	private List<CouponBean> coupons=new ArrayList<CouponBean>();
	
	protected void onCreate(android.os.Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_checkout);
		
		su=new SharedPrefUtil(this, "tiantianonline");
		initView();
		initData();
		setListeners();
	};

	@Override
	protected void initView() {
		mBtnTitleMiddle.setVisibility(View.VISIBLE);
		mBtnTitleMiddle.setText("结算页");
		mBtnTitleMiddle.setTextColor(getResources().getColor(R.color.white));
		
		mImgLeft.setVisibility(View.VISIBLE);
		mImgLeft.setBackgroundResource(R.drawable.back_bg_selector);

		etReceiver=(EditText) findViewById(R.id.name);
		spAddress=(Spinner) findViewById(R.id.address);
		etPhoneNum=(EditText) findViewById(R.id.phonenum);
		etRoomNumber=(EditText) findViewById(R.id.et_roomnumber);

		payMethodSpinner=(Spinner) findViewById(R.id.pay_method);
		btSubmit=(Button) findViewById(R.id.bt_submit);
		spCoupon=(Spinner) findViewById(R.id.sp_coupon);
		
		ArrayAdapter< String> paymethodAdapter=new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, paymethods);
		paymethodAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); 
		payMethodSpinner.setAdapter(paymethodAdapter);
		
		ArrayAdapter< String> addressAdapter=new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, addresses);
		addressAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); 
		spAddress.setAdapter(addressAdapter);
		
		receiver=su.getValueByKey(RECEIVER, "");
		phonenum=su.getValueByKey(PHONENUM, "");
		roomnum=su.getValueByKey(ROOMNUM, "");

		etReceiver.setText(receiver);
		etPhoneNum.setText(phonenum);
		etRoomNumber.setText(roomnum);
	}

	@Override
	protected void initData() {
		if(user==null){
			startActivity(LoginActivity.class);
			finish();
			return;
		}
		
		queryCoupons();
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
		btSubmit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				receiver=etReceiver.getText().toString();
				if(TextUtils.isEmpty(receiver)){
					toastMsg("收货人姓名为空");
					return;	
				}
				if(TextUtils.isEmpty(address)){
					toastMsg("收货地址为空");
					return;
				}
				phonenum=etPhoneNum.getText().toString();
				if(TextUtils.isEmpty(phonenum)){
					toastMsg("手机号为空");
					return;	
				}

				String detail="";
				
				if(shopcarts!=null){
					for(ShopCartaBean cart:shopcarts){
						if(cart.getNumber()!=0){
							
							detail=detail+cart.getName()+" and ";
							price+=(cart.getPrice()*cart.getNumber());
						}
						
					}
				}
				CouponBean coupon=coupons.get(discountIndex);
				if(coupon.getType()==CouponBean.COUPON_TYPE_ONSALE&&price<coupon.getLimit()){
					toastMsg("您购买商品不足"+coupon.getLimit()+"元，不可以使用该折扣券");
					return;
				}else{
					price=price-coupon.getAmount();
				}
				
				int index=detail.lastIndexOf(" and ");
				detail=detail.substring(0, index);
				
				ProgressUtil.showProgress(CheckoutActivity.this, "");
				saveReceiveAddress();
				if(paymethod==PAYMETHOD_CASH){
                    submitOrder(OrderBean.PAYMETHOD_CASHONDELIVEY);
				}else if(paymethod==PAYMETHOD_ALIPAY){
					payByAlipay(detail);
				}else if(paymethod==PAYMETHOD_WEIXINPAY){
					payByWeixin(detail);
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
		
		spAddress.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				address=addresses[arg2];
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				
			}
		});
		
		spCoupon.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				discountIndex=arg2;
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
	private void payByAlipay(String detail){
		new BmobPay(this).pay(price, detail, payListener);
	}
	
	private void payByWeixin(String detail){
		new BmobPay(this).payByWX(price, detail, payListener);
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
		bean.setAddress(address+" "+roomnum);
		bean.setDispatcher("untreated");
		bean.setPacker("untreated");
		bean.setPhonenum(phonenum);
		bean.setShopcarts(shopcarts);
		bean.setPayMethod(paymethod);
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
	private void saveReceiveAddress(){
		su.putValueByKey(PHONENUM, phonenum);
		su.putValueByKey(RECEIVER, receiver);
		su.putValueByKey(ROOMNUM, roomnum);
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
			}
			
			@Override
			public void onFailure(int arg0, String arg1) {
			}
		});
		
	}
	
	private void queryCoupons(){
		ProgressUtil.showProgress(this, "");
		BmobQuery<CouponBean> query	 = new BmobQuery<CouponBean>();
		query.addWhereEqualTo("username", user.getUsername());
		query.addWhereEqualTo("status", CouponBean.COUPON_STATUS_UNCONSUMED);
		query.setLimit(10);
		query.findObjects(this, new FindListener<CouponBean>() {

			@Override
			public void onSuccess(List<CouponBean> object) {
				ProgressUtil.closeProgress();
				coupons.addAll(object);
				
				CouponBean coupon=new CouponBean();
				coupon.setAmount(0);
				coupon.setType(CouponBean.COUPON_TYPE_DONTUSE);
				coupons.add(0, coupon);
				
				CheckCouponAdapter couponAdapter=new CheckCouponAdapter(CheckoutActivity.this, android.R.layout.simple_spinner_item, coupons);
				couponAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); 
				spCoupon.setAdapter(couponAdapter);
			}

			@Override
			public void onError(int code, String msg) {
				ProgressUtil.closeProgress();
				toastMsg(msg);
			}
		});	
		
	}

}
