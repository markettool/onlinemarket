package com.online.market;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import cn.bmob.v3.AsyncCustomEndpoints;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.CloudCodeListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

import com.bmob.pay.tool.PayListener;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.online.market.adapter.CheckCouponAdapter;
import com.online.market.beans.CouponBean;
import com.online.market.beans.OrderBean;
import com.online.market.beans.ShopCartaBean;
import com.online.market.dialog.InstallWxDialog;
import com.online.market.pay.Payment;
import com.online.market.utils.MobileUtil;
import com.online.market.utils.ProgressUtil;
import com.online.market.utils.SharedPrefUtil;
import com.online.market.view.BottomView;

public class CheckoutActivity extends BaseActivity {
	public static final String PAYMETHOD="paymethod";
	public static final String UNIT="unit";
	public static final String RECEIVER="receiver";
	public static final String PHONENUM="phonenum";
	public static final String ROOMNUM="roomnum";
	
	public static final int PAYMETHOD_ALIPAY=0;
	public static final int PAYMETHOD_WEIXINPAY=1;
	public static final int PAYMETHOD_CASH=2;
	
	private String [] paymethods={"支付宝支付","微信支付","货到付款"};
	/**单元*/
	private String [] units={"海淀","昌平","太原","五台"};
	private int paymethod;
	private int unit;
	
	private EditText etReceiver;
	private Spinner spUnit;
	private EditText etPhoneNum;
	private EditText etRoomNumber;
	private Spinner spPayMethod;
//	private Button btSubmit;
	private BottomView bView;
	private Spinner spCoupon;
	
	private List<ShopCartaBean> shopcarts=null;
	
	private String receiverStr;
	private String unitStr;
	private String phonenumStr;
	private String roomnumStr;
	/**使用的折扣*/
	private int discountIndex;
	private float price=0;
	private float finalPrice;
	private String detail="";
	
	private SharedPrefUtil su;
	
	private List<CouponBean> coupons=new ArrayList<CouponBean>();
	
	private Payment payment;
	
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
		spUnit=(Spinner) findViewById(R.id.address);
		etPhoneNum=(EditText) findViewById(R.id.phonenum);
		etRoomNumber=(EditText) findViewById(R.id.et_roomnumber);

		spPayMethod=(Spinner) findViewById(R.id.pay_method);
		bView=(BottomView) findViewById(R.id.bottom_layout);
		spCoupon=(Spinner) findViewById(R.id.sp_coupon);
		
		ArrayAdapter< String> paymethodAdapter=new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, paymethods);
		paymethodAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); 
		spPayMethod.setAdapter(paymethodAdapter);
		
		ArrayAdapter< String> addressAdapter=new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, units);
		addressAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); 
		spUnit.setAdapter(addressAdapter);
		
		getReceiverInfo();

		etReceiver.setText(receiverStr);
		etPhoneNum.setText(phonenumStr);
		etRoomNumber.setText(roomnumStr);
		spPayMethod.setSelection(paymethod);
		spUnit.setSelection(unit);
	}
	
	private void getReceiverInfo(){
		paymethod=su.getIntByKey(PAYMETHOD, 0);
		receiverStr=su.getValueByKey(RECEIVER, "");
		phonenumStr=su.getValueByKey(PHONENUM, "");
		roomnumStr=su.getValueByKey(ROOMNUM, "");
		unit=su.getIntByKey(UNIT, 0);
	}

	@Override
	protected void initData() {
		if(user==null){
			startActivity(LoginActivity.class);
			finish();
			return;
		}
		
		payment=Payment.getPayment(this);
		
		queryCoupons();
		try {
			shopcarts=dbUtils.findAll(Selector.from(ShopCartaBean.class));
		} catch (DbException e) {
			e.printStackTrace();
		}
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
		finalPrice=price;
		bView.setTotalPrice(finalPrice);

	}

	@Override
	protected void setListeners() {
        mImgLeft.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
        bView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				receiverStr=etReceiver.getText().toString();
				if(TextUtils.isEmpty(receiverStr)){
					toastMsg("收货人姓名为空");
					return;	
				}
				if(TextUtils.isEmpty(unitStr)){
					toastMsg("收货地址为空");
					return;
				}
				phonenumStr=etPhoneNum.getText().toString();
				if(TextUtils.isEmpty(phonenumStr)){
					toastMsg("手机号为空");
					return;	
				}
				if(!MobileUtil.isMobileNO(phonenumStr)){
					toastMsg("手机号格式非法");
					return;	
				}
				roomnumStr=etRoomNumber.getText().toString();

				ProgressUtil.showProgress(CheckoutActivity.this, "");
				saveReceiveInfo();
				
				if(finalPrice<=0){
					finalPrice=0;
					submitOrder(OrderBean.PAYMETHOD_CASHONDELIVEY);
					return;
				}
				
				if(paymethod==PAYMETHOD_CASH){
                    submitOrder(OrderBean.PAYMETHOD_CASHONDELIVEY);
				}else if(paymethod==PAYMETHOD_ALIPAY){
					payByAlipay(detail);
				}else if(paymethod==PAYMETHOD_WEIXINPAY){
					payByWeixin(detail);
				}

			}
		});
		
		spPayMethod.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				paymethod=arg2;
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				
			}
		});
		
		spUnit.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				unit=arg2;
				unitStr=units[arg2];
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
				CouponBean coupon=coupons.get(discountIndex);
				if(coupon.getType()==CouponBean.COUPON_TYPE_ONSALE&&price<coupon.getLimit()){
					toastMsg("您购买商品不足"+coupon.getLimit()+"元，不可以使用该折扣券");
					spCoupon.setSelection(0);
					discountIndex=0;
					return;
				}else{
					finalPrice=price-coupon.getAmount();
					if(finalPrice<0){
						finalPrice=0;
					}
					bView.setTotalPrice(finalPrice);
				}
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
		payment.payByAlipay(finalPrice, detail, payListener);
	}
	
	private void payByWeixin(String detail){
		boolean isInstalled=isInstalled(CheckoutActivity.this, "com.bmob.app.sport");
		if(!isInstalled){
			InstallWxDialog.show(CheckoutActivity.this);
			return;
		}
		payment.payByWeixin(finalPrice, detail, payListener);
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
			ProgressUtil.closeProgress();
			toastMsg("付款失败");
		}
	};
	
	private void submitOrder(int paymethod){
		
		OrderBean bean=new OrderBean();
		bean.setReceiver(receiverStr);
		bean.setUsername(user.getUsername());
		bean.setAddress(unitStr+" "+roomnumStr);
		bean.setDispatcher("untreated");
		bean.setPacker("untreated");
		bean.setPhonenum(phonenumStr);
		bean.setShopcarts(shopcarts);
		bean.setPayMethod(paymethod);
	    bean.setPrice(finalPrice);    
		final 	List< ShopCartaBean> carts=shopcarts;
		bean.save(CheckoutActivity.this, new SaveListener() {
			
			@Override
			public void onSuccess() {
				toastMsg("您的订单已经提交成功，半小时将送达");
				updateCouponStatus();
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
	private void saveReceiveInfo(){
		su.putValueByKey(PHONENUM, phonenumStr);
		su.putValueByKey(RECEIVER, receiverStr);
		su.putValueByKey(ROOMNUM, roomnumStr);
		su.putIntByKey(PAYMETHOD, paymethod);
		su.putIntByKey(UNIT, unit);
	}
	
	private void updateCouponStatus(){
		JSONObject object=new JSONObject();
		try {
			JSONArray carts=new JSONArray();
			for(ShopCartaBean cart:shopcarts){
				JSONObject o=new JSONObject();
				o.put("id", cart.getId());
				o.put("name", cart.getName());
				o.put("number", cart.getNumber());
				carts.put(o);
			}
			object.put("carts", carts);
			object.put("couponId", coupons.get(discountIndex).getObjectId());
			JSONObject parms=new JSONObject();
			parms.put("json", object);
			String json=parms.toString();
			Log.e("checkout", json);
			AsyncCustomEndpoints ace = new AsyncCustomEndpoints();
			//第一个参数是上下文对象，第二个参数是云端代码的方法名称，第三个参数是上传到云端代码的参数列表（JSONObject cloudCodeParams），第四个参数是回调类
			ace.callEndpoint(CheckoutActivity.this, "submitorders", parms, 
			    new CloudCodeListener() {
			            @Override
			            public void onSuccess(Object object) {
			            	Log.e("checkout", object.toString());
			            }
			            @Override
			            public void onFailure(int code, String msg) {
			            }
			        });
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
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
				
				//添加一个不使用优惠券
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
	
	private boolean isInstalled( Context context, String packageName )
    {
        final PackageManager packageManager = context.getPackageManager();
        // 获取所有已安装程序的包信息
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        for ( int i = 0; i < pinfo.size(); i++ )
        {
            if(pinfo.get(i).packageName.equalsIgnoreCase(packageName)){
            	return true;
            }
        }
        return false;
    }

}
