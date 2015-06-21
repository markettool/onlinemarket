package com.online.market.fragment;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

import com.lidroid.xutils.bitmap.PauseOnScrollListener;
import com.online.market.R;
import com.online.market.adapter.CommodityAdapter;
import com.online.market.beans.CommodityBean;
import com.online.market.utils.BitmapHelp;
import com.online.market.view.xlist.XListView;
import com.online.market.view.xlist.XListView.IXListViewListener;
public class CommodityFragment extends Fragment {
	
	private XListView xlv;
	private CommodityAdapter adapter;
	
	public static final int FINISH_REFRESHING=0;
	public static final int FINISH_LOADING=1;
	
	private int focusSkip;
	private List<CommodityBean> commodityBeans=new ArrayList<CommodityBean>();
	
	private int oldSize=0;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.frag_commodity, null);
		xlv=(XListView) view.findViewById(R.id.xlv);
		xlv.setOnScrollListener(new PauseOnScrollListener(BitmapHelp.getBitmapUtils(getActivity()), false, true));
	        
		setAdapter();
		setListeners();
		queryCommoditys(FINISH_REFRESHING);
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
		
		xlv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
//				Intent intent=new Intent(getActivity(), CommentActivity.class);
//				intent.putExtra("operaBean", operaBeans.get(arg2));
//				getActivity().startActivity(intent);
			}
		});
	}
	
	private void refresh(){
		commodityBeans.clear();
		focusSkip=0;
		queryCommoditys(FINISH_REFRESHING);
	}
	
	private void queryCommoditys(final int handle){
		BmobQuery<CommodityBean> focusQuery	 = new BmobQuery<CommodityBean>();
//		focusQuery.order("-commentNum,-likeNum");
		focusQuery.setLimit(10);
		focusQuery.setSkip(focusSkip);
		focusQuery.findObjects(getActivity(), new FindListener<CommodityBean>() {

			@Override
			public void onSuccess(List<CommodityBean> object) {
				oldSize=commodityBeans.size();
				focusSkip+=object.size();
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
	
}
