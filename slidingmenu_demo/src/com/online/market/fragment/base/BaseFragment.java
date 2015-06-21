package com.online.market.fragment.base;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.online.market.utils.BitmapHelp;

import android.os.Bundle;
import android.support.v4.app.Fragment;

public class BaseFragment extends Fragment {
	protected BitmapUtils bitmapUtils;
	protected BitmapDisplayConfig config;
	protected DbUtils dbUtils;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		bitmapUtils=BitmapHelp.getBitmapUtils(getActivity());
		config=BitmapHelp.getDisplayConfig(getActivity(), 50, 50);
		dbUtils=DbUtils.create(getActivity());
	}

}
