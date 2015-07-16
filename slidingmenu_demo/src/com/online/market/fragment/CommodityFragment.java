package com.online.market.fragment;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

import com.lidroid.xutils.bitmap.PauseOnScrollListener;
import com.online.market.CommodityDetailActivity;
import com.online.market.R;
import com.online.market.adapter.CategoryAdapter;
import com.online.market.adapter.CommodityAdapter;
import com.online.market.beans.CommodityBean;
import com.online.market.fragment.base.BaseFragment;
import com.online.market.utils.BitmapHelp;
import com.online.market.utils.ProgressUtil;
import com.online.market.view.ClearEditText;
import com.online.market.view.xlist.XListView;
import com.online.market.view.xlist.XListView.IXListViewListener;
public class CommodityFragment extends BaseFragment {
	public static final int FINISH_REFRESHING=0;
	public static final int FINISH_LOADING=1;
	
	private XListView xlv;
	private ListView categoryLv;
	private ClearEditText cet;
	private CommodityAdapter adapter;
	private int skip;
	private String category;
	private List<CommodityBean> commodityBeans=new ArrayList<CommodityBean>();
	private int oldSize=0;
	
	private TextView lastTv;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.frag_commodity, null);
		categoryLv=(ListView) view.findViewById(R.id.lv_category);
		xlv=(XListView) view.findViewById(R.id.xlv);
		xlv.setOnScrollListener(new PauseOnScrollListener(BitmapHelp.getBitmapUtils(getActivity()), false, true));
		xlv.setOnScrollListener(new PauseOnScrollListener(BitmapHelp.getBitmapUtils(getActivity()), false, true));
		cet=(ClearEditText)view.findViewById(R.id.et_msg_search);
		
		setAdapter();
		setCategoryAdapter();
		setListeners();
		ProgressUtil.showProgress(getActivity(), "");
		queryCommoditys(FINISH_REFRESHING,null,null);
		return view;
	}
	
	private void setCategoryAdapter(){
		CategoryAdapter adapter=new CategoryAdapter(getActivity());
		categoryLv.setAdapter(adapter);
	}
	
	private void setListeners(){
		categoryLv.setOnItemClickListener(new OnItemClickListener() {

			@SuppressLint("ResourceAsColor") @Override
			public void onItemClick(AdapterView<?> arg0, View view, int arg2,
					long arg3) {
				TextView tv=(TextView) view;
				String text=tv.getText().toString();
				reinit();
				ProgressUtil.showProgress(getActivity(), "");
				if(text.equals("所有")){
					category=null;
					queryCommoditys(FINISH_REFRESHING, null, null);
				}else{
					category=text;
					queryCommoditys(FINISH_REFRESHING, "category", text);
				}
				if(lastTv!=null){
					lastTv.setTextColor(R.color.text_gray);
				}
				tv.setTextColor(R.color.ble_blue);
				lastTv=tv;
			}
		});
	
		xlv.setXListViewListener(new IXListViewListener() {
			
			@Override
			public void onRefresh() {
				reinit();
				queryCommoditys(FINISH_REFRESHING,null,null);
			}
			
			@Override
			public void onLoadMore() {
			
				if(category==null){
					queryCommoditys(FINISH_LOADING, null, null);
				}else{
					queryCommoditys(FINISH_LOADING, "category", category);
				}
			}
		});
		
		xlv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent=new Intent(getActivity(), CommodityDetailActivity.class);
				intent.putExtra("commodity", commodityBeans.get(arg2-1));
				startActivity(intent);
			}
		});
		
		cet.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int arg1, int arg2, int arg3) {
				reinit();
				ProgressUtil.showProgress(getActivity(), "");
				if(TextUtils.isEmpty(s)){
					queryCommoditys(FINISH_REFRESHING, null,null);
				}else{
					queryCommoditys(FINISH_REFRESHING,"name", s.toString());
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				
			}
			
			@Override
			public void afterTextChanged(Editable arg0) {
				
			}
		});
		
	}
	
	private void reinit(){
		commodityBeans.clear();
		skip=0;
		
	}
	
	private void queryCommoditys(final int method,String value,String key){
		BmobQuery<CommodityBean> query	 = new BmobQuery<CommodityBean>();
		if(key!=null){
			query.addWhereContains(value, key);
		}
		query.order("-sold");
		query.setLimit(10);
		query.setSkip(skip);
		query.findObjects(getActivity(), new FindListener<CommodityBean>() {

			@Override
			public void onSuccess(List<CommodityBean> object) {
				ProgressUtil.closeProgress();
				oldSize=commodityBeans.size();
				skip+=object.size();
				commodityBeans.addAll(object);
				if(commodityBeans.size()==0){
					toastMsg("暂无相关商品");
				}
				handle(method);
			}

			@Override
			public void onError(int code, String msg) {
				ProgressUtil.closeProgress();
				toastMsg(msg);
				handle(method);
				
			}
		});	
		
	}
	
	private void setAdapter(){
		adapter=new CommodityAdapter(getActivity(), commodityBeans);
		xlv.setAdapter(adapter);
	}
	
	private void handle(int handle){
		switch (handle) {
		case FINISH_REFRESHING:
			xlv.stopRefresh();
			break;

		case FINISH_LOADING:
			xlv.stopLoadMore();
			if(oldSize+1<commodityBeans.size()){
				xlv.setSelection(oldSize+1);
			}
			break;
			
		}
		adapter.notifyDataSetChanged();	
			
	}
	
}
