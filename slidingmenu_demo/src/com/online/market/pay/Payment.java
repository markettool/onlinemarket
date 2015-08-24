package com.online.market.pay;

import android.app.Activity;

import com.bmob.pay.tool.BmobPay;
import com.bmob.pay.tool.PayListener;

public class Payment {
	private static Payment payment;
	private BmobPay bmobPay;
//	private OnPayListener listener;
	
	private Payment(Activity activity){
		bmobPay=new BmobPay(activity);
	}
	
	public static Payment getPayment(Activity activity){
		if(payment==null){
			payment=new Payment(activity);
		}
		return payment;
	}
	
	public void payByAlipay(double price,String detail,PayListener l){
		bmobPay.pay(price, detail, l);
	}
	
	public void payByWeixin(double price,String detail,PayListener l){
		bmobPay.payByWX(price, detail, l);
	}
	
//    private PayListener l=new PayListener() {
//		
//		@Override
//		public void unknow() {
//			if(listener!=null){
//				listener.onFail();
//			}
//		}
//		
//		@Override
//		public void succeed() {
//			
//		}
//		
//		@Override
//		public void orderId(String arg0) {
//			
//		}
//		
//		@Override
//		public void fail(int arg0, String arg1) {
//			if(listener!=null){
//				listener.onFail();
//			}
//		}
//	};
//	
//	public void setOnPaylistener(OnPayListener l){
//		this.listener=l;
//	}
//	
//	public interface OnPayListener{
//		public void onSuccess();
//		public void onFail();
//	}

}
