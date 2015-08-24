package com.online.market.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.callback.BitmapLoadCallBack;
import com.lidroid.xutils.bitmap.callback.BitmapLoadFrom;
import com.lidroid.xutils.exception.DbException;
import com.online.market.R;
import com.online.market.adapter.base.MyBaseAdapter;
import com.online.market.adapter.base.ViewHolder;
import com.online.market.beans.ShopCartaBean;
import com.online.market.utils.BitmapHelp;
import com.online.market.utils.BitmapUtil;

public class MyShopCartAdapter extends MyBaseAdapter<ShopCartaBean> {
	
	public MyShopCartAdapter(Context context,List<ShopCartaBean> cartaBeans){
		super(context,cartaBeans);
	}

	@Override
	public View getView(int arg0, View convertView, ViewGroup arg2) {
		if(convertView==null){
			convertView=mInflater.inflate(R.layout.myshopcart_item, null);
			
		}
		final ImageView ivShopcartPic=ViewHolder.get(convertView, R.id.shopcart_pic);
		TextView tvShopcartName=ViewHolder.get(convertView, R.id.shopcart_name);
		TextView tvShopcartPrice=ViewHolder.get(convertView, R.id.shopcart_price);
		EditText tvShopcartNum=ViewHolder.get(convertView, R.id.shopcart_num);
		ImageView ivDelete=ViewHolder.get(convertView, R.id.iv_delete);
		ImageView ivAdd=ViewHolder.get(convertView, R.id.iv_add);
		
		if(list==null||list.size()==0){
			return convertView;
		}
		final ShopCartaBean bean=list.get(arg0);
		tvShopcartName.setText(bean.getName());
		tvShopcartPrice.setText(bean.getPrice()+" 元");
		tvShopcartNum.setText(""+bean.getNumber());

		bitmapUtils.display(ivShopcartPic, bean.getPic(),BitmapHelp.getDisplayConfig(mContext, 50, 50),new BitmapLoadCallBack<View>() {

			@Override
			public void onLoadCompleted(View arg0, String arg1, Bitmap bitmap,
					BitmapDisplayConfig arg3, BitmapLoadFrom arg4) {
				int min=Math.min(bitmap.getWidth(), bitmap.getHeight());
				bitmap=BitmapUtil.getCanvasBitmap(bitmap, min, min);
				ivShopcartPic.setImageBitmap(bitmap);
			}

			@Override
			public void onLoadFailed(View arg0, String arg1, Drawable arg2) {
				
			}
		});

		ivDelete.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(bean.getNumber()>0){
					bean.setNumber(bean.getNumber()-1);
					updateDb(bean);
					notifyDataSetChanged();
				}
			}
		});
		ivAdd.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				bean.setNumber(bean.getNumber()+1);
				updateDb(bean);
				notifyDataSetChanged();

			}
		});
		return convertView;
	}
	
	private void updateDb(ShopCartaBean bean){
		try {
			dbUtils.update(bean, "number");
		} catch (DbException e) {
			e.printStackTrace();
		}
	}
	
}
