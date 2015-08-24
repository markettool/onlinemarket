package com.online.market.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.online.market.R;
import com.online.market.adapter.base.MyBaseAdapter;
import com.online.market.adapter.base.ViewHolder;
import com.online.market.beans.CouponBean;

public class CouponAdapter extends MyBaseAdapter<CouponBean> {

	public CouponAdapter(Context context, List<CouponBean> list) {
		super(context, list);
	}

	@Override
	public View getView(int arg0, View convertView, ViewGroup arg2) {
		if(convertView==null){
			convertView=mInflater.inflate(R.layout.item_coupon, null);
		}
		TextView tvCouponName=ViewHolder.get(convertView, R.id.tv_promotion_type);
		TextView tvCouponAmount=ViewHolder.get(convertView, R.id.tv_promotion_amount);
		TextView tvCouponLimit=ViewHolder.get(convertView, R.id.tv_promotion_limit);

		CouponBean bean=list.get(arg0);
		if(bean.getType()==CouponBean.COUPON_TYPE_COMMON){
			tvCouponName.setText("通用券");
		}else if(bean.getType()==CouponBean.COUPON_TYPE_ONSALE){
			tvCouponName.setText("折扣券");
			tvCouponLimit.setText("1.消费满"+bean.getLimit()+"元可抵扣现金。\n2.最终解释权归天天在线所有。");
		}
		tvCouponAmount.setText("¥ "+bean.getAmount()+"元");
		if(bean.getStatus()==CouponBean.COUPON_STATUS_CONSUMED){
			tvCouponName.setTextColor(mContext.getResources().getColor(R.color.text_gray));
			tvCouponAmount.setTextColor(mContext.getResources().getColor(R.color.text_gray));

		}
		return convertView;
	}

}
