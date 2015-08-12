package com.online.market.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.online.market.R;
import com.online.market.adapter.base.MyBaseAdapter;

public class CategoryAdapter extends MyBaseAdapter<String> {
	
	public CategoryAdapter(Context context,List<String> list) {
		super(context,list);
	}

	@Override
	public View getView(int arg0, View convertView, ViewGroup arg2) {
		View view= mInflater.inflate(R.layout.category_item, null);
		TextView tv=(TextView) view.findViewById(R.id.tv_category);
		tv.setText(list.get(arg0));
		return view;
	}

}
