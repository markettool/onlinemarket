package com.online.market.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.online.market.R;
import com.online.market.adapter.base.MyBaseAdapter;
import com.online.market.adapter.base.ViewHolder;
import com.online.market.beans.OrderBean;
import com.online.market.beans.ShopCartaBean;
import com.online.market.utils.DateUtil;

public class MyOrderAdapter extends MyBaseAdapter {
	private List<OrderBean > orderBeans;

	public MyOrderAdapter(Context context,List<OrderBean > orderBeans) {
		super(context);
		this.orderBeans=orderBeans;
	}

	@Override
	public int getCount() {
		return orderBeans.size();
	}

	@Override
	public Object getItem(int arg0) {
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int arg0, View convertView, ViewGroup arg2) {
		if(convertView==null){
			convertView=mInflater.inflate(R.layout.myorder_item, null);
		}
		TextView tvOrderDetail=ViewHolder.get(convertView, R.id.orderdetail);
		TextView tvOrderName=ViewHolder.get(convertView, R.id.ordername);
		TextView tvOrderAddress=ViewHolder.get(convertView, R.id.orderaddress);
		TextView tvOrderPhonenum=ViewHolder.get(convertView, R.id.orderphonenum);
		TextView tvOrderStatus=ViewHolder.get(convertView, R.id.orderstatus);
		TextView tvOrderdeliveTime=ViewHolder.get(convertView, R.id.orderdelivetime);
		ImageView ivCall=ViewHolder.get(convertView, R.id.iv_call);
		
		final OrderBean bean=orderBeans.get(arg0);
        tvOrderName.setText("收货人： "+bean.getReceiver());
        tvOrderAddress.setText("收货地址： "+bean.getAddress());
        tvOrderPhonenum.setText("联系方式： "+bean.getPhonenum());
        String time=DateUtil.getDate(bean.getCreatedAt());
        tvOrderdeliveTime.setText(time);
        if(bean.getPayMethod()==OrderBean.PAYMETHOD_PAYFAILED){
        	tvOrderStatus.setText("付款失败");
        }else if(bean.getPayMethod()==OrderBean.PAYMETHOD_CASHONDELIVEY){
        	tvOrderStatus.setText("货到付款：需支付 "+bean.getPrice()+" 元");
        }else {
        	tvOrderStatus.setText("在线已支付");
        }
        if(bean.getState()==OrderBean.STATE_DELIVED){
        	//when status is delived,set all gray
        	tvOrderStatus.setText("订单已完成");
        	tvOrderDetail.setTextColor(mContext.getResources().getColor(R.color.text_gray));
        	tvOrderName.setTextColor(mContext.getResources().getColor(R.color.text_gray));
        	tvOrderAddress.setTextColor(mContext.getResources().getColor(R.color.text_gray));
        	tvOrderPhonenum.setTextColor(mContext.getResources().getColor(R.color.text_gray));
        	tvOrderStatus.setTextColor(mContext.getResources().getColor(R.color.text_gray));
        }else
        //when status is not delived,set default
        {
        	tvOrderDetail.setTextColor(mContext.getResources().getColor(R.color.ble_blue));
        	tvOrderName.setTextColor(mContext.getResources().getColor(R.color.black));
        	tvOrderAddress.setTextColor(mContext.getResources().getColor(R.color.black));
        	tvOrderPhonenum.setTextColor(mContext.getResources().getColor(R.color.black));
        	tvOrderStatus.setTextColor(mContext.getResources().getColor(R.color.orange));
        }
        ivCall.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+bean.getPhonenum()));  
                mContext.startActivity(intent); 
			}
		});
        String detail="";
        for(ShopCartaBean p:bean.getShopcarts()){
        	detail+=p.getName()+" X "+p.getNumber()+"\n";
        }
    	tvOrderDetail.setText("商品： "+detail);

		return convertView;
	}

}
