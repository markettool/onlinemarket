package com.online.market.popop;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.PopupWindow;

import com.online.market.R;

public class CategoryPopup extends PopupWindow {
	private Context mContext;
	private GridView gv;
	private OnClickListener listener;
	public static String [] categorys={"吃的","喝的","用的","文具","吃的","喝的","用的","文具"};
	
	public CategoryPopup(Context mContext){
		this.mContext=mContext;
		View contentView = LayoutInflater.from(mContext).inflate(
                R.layout.category_pop, null);
		gv=(GridView) contentView.findViewById(R.id.gv);
		
		setContentView(contentView);
		setWidth(LayoutParams.MATCH_PARENT);
		setHeight(LayoutParams.MATCH_PARENT);
//		setTouchable(true);
		setOutsideTouchable(true);
		setFocusable(true);
		setBackgroundDrawable(new BitmapDrawable());
		
		setAdapter();
		setListeners();
	}
	
	private void setAdapter(){
		ArrayAdapter adapter = new ArrayAdapter(mContext,R.layout.category_item,categorys);
        gv.setAdapter(adapter);	
	}
	
	private void setListeners(){
		gv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				listener.onClick(categorys[arg2]);
				dismiss();
			}
		});
	}
	
	public interface OnClickListener{
		public void onClick(String str);
	}
	
	public void setOnClickListener(OnClickListener listener){
		this.listener=listener;
	}

}
