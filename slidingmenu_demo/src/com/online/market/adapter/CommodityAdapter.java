package com.online.market.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.callback.BitmapLoadCallBack;
import com.lidroid.xutils.bitmap.callback.BitmapLoadFrom;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.online.market.R;
import com.online.market.adapter.base.MyBaseAdapter;
import com.online.market.adapter.base.ViewHolder;
import com.online.market.beans.CommodityBean;
import com.online.market.beans.ShopCartaBean;
import com.online.market.utils.BitmapUtil;

public class CommodityAdapter extends MyBaseAdapter<CommodityBean> {

	public CommodityAdapter(Context context, List<CommodityBean> beans) {
		super(context,beans);
	}

	@Override
	public View getView(int arg0, View convertView, ViewGroup arg2) {
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.commodity_item, null);
		}
		final ImageView ivCommodityPic =ViewHolder.get(convertView, R.id.commodity_pic);
		TextView commodityName =ViewHolder.get(convertView,R.id.commodity_name);
		TextView commoditySold =ViewHolder.get(convertView,R.id.commodity_sold);
		TextView commodityPrice = ViewHolder.get(convertView,R.id.commodity_price);
		ImageView ivAdd =ViewHolder.get(convertView, R.id.iv_add);

		final CommodityBean bean=list.get(arg0);
		commodityName.setText(bean.getName());
		commoditySold.setText("已售  "+bean.getSold());
		commodityPrice.setText(bean.getPrice()+ " 元");
		bitmapUtils.display(ivCommodityPic, bean.getPics().getFileUrl(mContext),config,new BitmapLoadCallBack<View>() {

			@Override
			public void onLoadCompleted(View arg0, String arg1, Bitmap bitmap,
					BitmapDisplayConfig arg3, BitmapLoadFrom arg4) {
				int min=Math.min(bitmap.getWidth(), bitmap.getHeight());
				bitmap=BitmapUtil.getCanvasBitmap(bitmap, min, min);
				ivCommodityPic.setImageBitmap(bitmap);
			}

			@Override
			public void onLoadFailed(View arg0, String arg1, Drawable arg2) {
				
			}
		});
		
		ivAdd.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				
				try {
					ShopCartaBean p=dbUtils.findFirst(Selector.from(ShopCartaBean.class).where("id","=",bean.getObjectId()));
					if(p!=null){
						toastMsg("购物车已有该物品");
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
				toastMsg(bean.getName()+" 已加入购物车,可进入我的购物车查看");
			}
		});

		return convertView;
	}
	

}
