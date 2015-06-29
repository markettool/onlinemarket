package com.online.market.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.online.market.R;
import com.online.market.adapter.base.MyBaseAdapter;
import com.online.market.adapter.base.ViewHolder;
import com.online.market.beans.OrderBean;
import com.online.market.beans.ShopCartaBean;

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
		
		final OrderBean bean=orderBeans.get(arg0);
        tvOrderName.setText("收货人： "+bean.getReceiver());
        tvOrderAddress.setText("收货地址： "+bean.getAddress());
        tvOrderPhonenum.setText(bean.getPhonenum());
        if(bean.getStatus()==OrderBean.STATUS_PAYED){
        	tvOrderStatus.setText("付款失败");
        }else if(bean.getStatus()==OrderBean.STATUS_CASHONDELIVEY){
        	tvOrderStatus.setText("货到付款：需支付 "+bean.getPrice());
        }else if(bean.getStatus()==OrderBean.STATUS_PAYED){
        	tvOrderStatus.setText("在线已支付");
        }else if(bean.getStatus()==OrderBean.STAUTS_DELIVED){
        	tvOrderStatus.setText("订单已完成");
        }
        String detail="";
        for(ShopCartaBean p:bean.getShopcarts()){
        	detail+=p.getName()+" X "+p.getNumber()+"\n";
        }
    	tvOrderDetail.setText("商品： "+detail);

		return convertView;
	}

}
