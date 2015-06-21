package com.online.market.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

import com.lidroid.xutils.bitmap.PauseOnScrollListener;
import com.lidroid.xutils.exception.DbException;
import com.online.market.R;
import com.online.market.adapter.CommodityAdapter;
import com.online.market.beans.CommodityBean;
import com.online.market.beans.ShopCartaBean;
import com.online.market.config.SystemConfig;
import com.online.market.fragment.base.BaseFragment;
import com.online.market.utils.BitmapHelp;
import com.online.market.view.AutoScrollTextView;
import com.online.market.view.xlist.XListView;
import com.online.market.view.xlist.XListView.IXListViewListener;
public class CommodityFragment extends BaseFragment {
	
	private XListView xlv;
//	private AutoScrollTextView autoScrollTextView;

	private CommodityAdapter adapter;
	
	public static final int FINISH_REFRESHING=0;
	public static final int FINISH_LOADING=1;
	
	private int skip;
	private List<CommodityBean> commodityBeans=new ArrayList<CommodityBean>();
	
	private int oldSize=0;
//	private MyBroadCastReceiver receiver;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.frag_commodity, null);
		xlv=(XListView) view.findViewById(R.id.xlv);
		xlv.setOnScrollListener(new PauseOnScrollListener(BitmapHelp.getBitmapUtils(getActivity()), false, true));
		xlv.setOnScrollListener(new PauseOnScrollListener(BitmapHelp.getBitmapUtils(getActivity()), false, true));
//		autoScrollTextView=(AutoScrollTextView) view.findViewById(R.id.autoscroll_tv);
//		autoScrollTextView.setVisibility(View.GONE);
//		 autoScrollTextView.initScrollTextView(getActivity().getWindowManager(), 
//	                "购物车为空"); 
//	        autoScrollTextView.starScroll();
		setAdapter();
		setListeners();
		queryCommoditys(FINISH_REFRESHING);
//		registerMyReceiver();
		return view;
	}
	
	private void setListeners(){
		
		xlv.setXListViewListener(new IXListViewListener() {
			
			@Override
			public void onRefresh() {
				refresh();
			}
			
			@Override
			public void onLoadMore() {
				
			}
		});
		
	}
	
	private void refresh(){
		commodityBeans.clear();
		skip=0;
		queryCommoditys(FINISH_REFRESHING);
	}
	
	private void queryCommoditys(final int handle){
		BmobQuery<CommodityBean> focusQuery	 = new BmobQuery<CommodityBean>();
		focusQuery.setLimit(10);
		focusQuery.setSkip(skip);
		focusQuery.findObjects(getActivity(), new FindListener<CommodityBean>() {

			@Override
			public void onSuccess(List<CommodityBean> object) {
				oldSize=commodityBeans.size();
				skip+=object.size();
				commodityBeans.addAll(object);
				handle(handle);
			}

			@Override
			public void onError(int code, String msg) {
				handle(handle);
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
	
//	private void registerMyReceiver(){
//		IntentFilter filter=new IntentFilter();
//		filter.addAction(SystemConfig.INTENT_ADD_SHOPCART);
//		receiver=new MyBroadCastReceiver();
//		getActivity().registerReceiver(receiver, filter);
//	}
	
//	private void unregisterMyReceiver(){
//		if(receiver!=null){
//			getActivity().unregisterReceiver(receiver);
//		}
//	}
	
//	class MyBroadCastReceiver extends BroadcastReceiver{
//
//		@Override
//		public void onReceive(Context context, Intent intent) {
//			List<ShopCartaBean> cartaBeans = null;
//			try {
//				cartaBeans = dbUtils.findAll(ShopCartaBean.class);
//			} catch (DbException e) {
//				e.printStackTrace();
//			}
//			if(cartaBeans==null||cartaBeans.size()==0){
//				autoScrollTextView.setVisibility(View.GONE);
//				return;
//			}
//			String cartsStr="";
//			for(ShopCartaBean bean:cartaBeans){
//				cartsStr=cartsStr+bean.getName();
//			}
//			autoScrollTextView.setVisibility(View.VISIBLE);
//			autoScrollTextView.setText(cartsStr+"加入了购物车！");
//		}
//		
//	}
	
//	@Override
//	public void onDestroy() {
//		super.onDestroy();
//		unregisterMyReceiver();
//	}
	
}
