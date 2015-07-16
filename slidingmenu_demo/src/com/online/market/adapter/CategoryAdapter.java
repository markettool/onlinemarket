package com.online.market.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.online.market.R;
import com.online.market.adapter.base.MyBaseAdapter;

public class CategoryAdapter extends MyBaseAdapter {
	
	private String [] categorys={"所有","吃的","喝的","床上用品","用的","其他"};
//	private TextView lastView;

	public CategoryAdapter(Context context) {
		super(context);
	}

	@Override
	public int getCount() {
		return categorys.length;
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
		View view= mInflater.inflate(R.layout.category_item, null);
		TextView tv=(TextView) view.findViewById(R.id.tv_category);
		tv.setText(categorys[arg0]);
		return view;
	}

}
