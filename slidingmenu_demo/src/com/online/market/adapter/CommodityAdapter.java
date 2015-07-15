package com.online.market.adapter;

import java.util.List;



import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.online.market.R;
import com.online.market.adapter.base.MyBaseAdapter;
import com.online.market.adapter.base.ViewHolder;
import com.online.market.beans.CommodityBean;
import com.online.market.beans.ShopCartaBean;
import com.online.market.config.SystemConfig;

public class CommodityAdapter extends MyBaseAdapter {

	private List<CommodityBean> beans;
	
	public CommodityAdapter(Context context, List<CommodityBean> beans) {
		super(context);
		this.beans = beans;
	}

	@Override
	public int getCount() {
		return beans.size();
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
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.commodity_item, null);
		}
		ImageView commodityPic =ViewHolder.get(convertView, R.id.commodity_pic);
		TextView commodityName =ViewHolder.get(convertView,R.id.commodity_name);
		TextView commoditySold =ViewHolder.get(convertView,R.id.commodity_sold);
		TextView commodityPrice = ViewHolder.get(convertView,R.id.commodity_price);
		ImageView ivAdd =ViewHolder.get(convertView, R.id.iv_add);

		final CommodityBean bean=beans.get(arg0);
		commodityName.setText(bean.getName());
		commoditySold.setText("已售  "+bean.getSold());
		commodityPrice.setText(bean.getPrice()+ " 元");
		bitmapUtils.display(commodityPic, bean.getPics().getFileUrl(mContext),config);
		
		ivAdd.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				
				try {
					ShopCartaBean p=dbUtils.findFirst(Selector.from(ShopCartaBean.class).where("id","=",bean.getObjectId()));
					if(p!=null){
						ShowToast("购物车已有该物品");
						return;
					}
				} catch (DbException e1) {
					e1.printStackTrace();
				}

				ShopCartaBean p=new ShopCartaBean();
				p.setPrice(bean.getPrice());
				p.setId(bean.getObjectId());
				p.setName(bean.getName());
				p.setSold(bean.getSold());
				p.setPic(bean.getPics().getFileUrl(mContext));
				p.setNumber(1);
				try {
					dbUtils.save(p);
				} catch (DbException e) {
					e.printStackTrace();
				}
				ShowToast(bean.getName()+" 已加入购物车,可进入我的购物车查看");
				Intent intent=new Intent(SystemConfig.INTENT_ADD_SHOPCART);
				mContext.sendBroadcast(intent);
			}
		});

		return convertView;
	}
	

}
