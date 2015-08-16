package com.online.market.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.online.market.R;
import com.online.market.adapter.base.ViewHolder;
import com.online.market.beans.CouponBean;

public class CheckCouponAdapter extends ArrayAdapter<CouponBean> {
   private List<CouponBean> list;
   private LayoutInflater mInflater;
	
	public CheckCouponAdapter(Context context, int resource,
			List<CouponBean> list) {
		super(context, resource, list);
		this.list=list;
		mInflater=LayoutInflater.from(context);
	}

	@Override
	public View getView(int arg0, View convertView, ViewGroup arg2) {
		if(convertView==null){
			convertView=mInflater.inflate(R.layout.item_checkcoupon, null);
		}
		TextView tv=ViewHolder.get(convertView, R.id.tv_text);
		CouponBean coupon=list.get(arg0);
		if(coupon.getType()==CouponBean.COUPON_TYPE_COMMON){
			tv.setText(list.get(arg0).getAmount()+"元 通用券");
		}else if(coupon.getType()==CouponBean.COUPON_TYPE_ONSALE){
			tv.setText(list.get(arg0).getAmount()+"元 折扣券");
		}else {
			tv.setText("不使用优惠券");
		}
		return convertView;
	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		return getView(position, convertView, parent);
	}

}
