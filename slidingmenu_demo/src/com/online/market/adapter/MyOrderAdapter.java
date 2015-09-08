package com.online.market.adapter;

import java.util.List;

import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import cn.bmob.v3.listener.DeleteListener;

import com.online.market.R;
import com.online.market.adapter.base.MyBaseAdapter;
import com.online.market.adapter.base.ViewHolder;
import com.online.market.beans.OrderBean;
import com.online.market.beans.ShopCartaBean;
import com.online.market.utils.DateUtil;
import com.online.market.utils.DialogUtil;
import com.online.market.utils.ProgressUtil;

public class MyOrderAdapter extends MyBaseAdapter<OrderBean> {

	public MyOrderAdapter(Context context,List<OrderBean > orderBeans) {
		super(context,orderBeans);
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
		TextView tvOrderPaymethod=ViewHolder.get(convertView, R.id.orderstatus);
		TextView tvOrderdeliveTime=ViewHolder.get(convertView, R.id.orderdelivetime);
		ImageView ivMenu=ViewHolder.get(convertView, R.id.iv_menu);
		ivMenu.setVisibility(View.GONE);
		
		final OrderBean bean=list.get(arg0);
        tvOrderName.setText("收货人： "+bean.getReceiver());
        tvOrderAddress.setText("收货地址： "+bean.getAddress());
        tvOrderPhonenum.setText("联系方式： "+bean.getPhonenum());
        String time=DateUtil.getDate(bean.getCreatedAt());
        tvOrderdeliveTime.setText(time);
        if(bean.getPayMethod()==OrderBean.PAYMETHOD_PAYFAILED){
        	tvOrderPaymethod.setText("付款失败");
        }else if(bean.getPayMethod()==OrderBean.PAYMETHOD_CASHONDELIVEY){
        	tvOrderPaymethod.setText("货到付款：需支付 "+bean.getPrice()+" 元");
        }else {
        	tvOrderPaymethod.setText("在线已支付");
        }
        if(bean.getState()==OrderBean.STATE_DELIVED){
        	//when status is delived,set all gray
        	tvOrderPaymethod.setText("订单已完成");
        	tvOrderDetail.setTextColor(mContext.getResources().getColor(R.color.text_gray));
        	tvOrderName.setTextColor(mContext.getResources().getColor(R.color.text_gray));
        	tvOrderAddress.setTextColor(mContext.getResources().getColor(R.color.text_gray));
        	tvOrderPhonenum.setTextColor(mContext.getResources().getColor(R.color.text_gray));
        	tvOrderPaymethod.setTextColor(mContext.getResources().getColor(R.color.text_gray));
        	ivMenu.setVisibility(View.GONE);
        }else
        //when status is not delived,set default
        {
        	tvOrderDetail.setTextColor(mContext.getResources().getColor(R.color.ble_blue));
        	tvOrderName.setTextColor(mContext.getResources().getColor(R.color.black));
        	tvOrderAddress.setTextColor(mContext.getResources().getColor(R.color.black));
        	tvOrderPhonenum.setTextColor(mContext.getResources().getColor(R.color.black));
        	tvOrderPaymethod.setTextColor(mContext.getResources().getColor(R.color.orange));
        }
		final int pos=arg0;
        ivMenu.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
//				Intent intent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+bean.getPhonenum()));  
//                mContext.startActivity(intent); 
                DialogUtil.dialog(mContext, "你确认取消订单吗？", "确认", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int arg1) {
//						OrderBean bean=orderBeans.get(pos);
						ProgressUtil.showProgress(mContext, "");
						bean.delete(mContext, new DeleteListener() {
							
							@Override
							public void onSuccess() {
								ProgressUtil.closeProgress();
								list.remove(pos);
								notifyDataSetChanged();
							}
							
							@Override
							public void onFailure(int arg0, String arg1) {
								ProgressUtil.closeProgress();
								toastMsg(arg1);
							}
						});
						dialog.dismiss();
					}
				}, "取消",new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int arg1) {
						dialog.dismiss();
					}
				});
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
