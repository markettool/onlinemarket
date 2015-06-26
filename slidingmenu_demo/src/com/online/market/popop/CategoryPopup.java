package com.online.market.popop;

import java.util.ArrayList;
import java.util.List;

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
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

import com.online.market.R;
import com.online.market.beans.Category;
import com.online.market.utils.ProgressUtil;

public class CategoryPopup extends PopupWindow {
	private Context mContext;
	private GridView gv;
	private OnClickListener listener;
	private List<String > strList=new ArrayList<String>();
	
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
		
//		setAdapter();
		ProgressUtil.showProgress(mContext, "");
		queryCategorys();
		
		setListeners();
	}
	
	private void setAdapter(){
		ArrayAdapter adapter = new ArrayAdapter(mContext,R.layout.category_item,strList);
        gv.setAdapter(adapter);	
	}
	
	private void setListeners(){
		gv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				listener.onClick(strList.get(arg2));
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
	
	private void queryCategorys(){
		BmobQuery<Category> query= new BmobQuery<Category>();
		query.findObjects(mContext, new FindListener<Category>() {

			@Override
			public void onSuccess(List<Category> object) {
				ProgressUtil.closeProgress();
				for(Category gory:object){
					strList.add(gory.getName());
					
				}
				setAdapter();
			}

			@Override
			public void onError(int code, String msg) {
				ProgressUtil.closeProgress();
				
			}
		});	
		
	}

}
