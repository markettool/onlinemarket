package com.online.market.fragment;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

import com.lidroid.xutils.bitmap.PauseOnScrollListener;
import com.online.market.R;
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
	private ClearEditText cet;
	private CommodityAdapter adapter;
	private int skip;
	private List<CommodityBean> commodityBeans=new ArrayList<CommodityBean>();
	private int oldSize=0;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.frag_commodity, null);
		xlv=(XListView) view.findViewById(R.id.xlv);
		xlv.setOnScrollListener(new PauseOnScrollListener(BitmapHelp.getBitmapUtils(getActivity()), false, true));
		xlv.setOnScrollListener(new PauseOnScrollListener(BitmapHelp.getBitmapUtils(getActivity()), false, true));
		cet=(ClearEditText)view.findViewById(R.id.et_msg_search);
		
		setAdapter();
		setListeners();
		ProgressUtil.showProgress(getActivity(), "");
		queryCommoditys(FINISH_REFRESHING,null,null);
		return view;
	}
	
	private void setListeners(){
		
		xlv.setXListViewListener(new IXListViewListener() {
			
			@Override
			public void onRefresh() {
				reinit();
				queryCommoditys(FINISH_REFRESHING,null,null);
			}
			
			@Override
			public void onLoadMore() {
				
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
	
	public void reinit(){
		commodityBeans.clear();
		skip=0;
		
	}
	
	public void queryCommoditys(final int method,String value,String key){
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
				toastMsg("error "+msg);
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
