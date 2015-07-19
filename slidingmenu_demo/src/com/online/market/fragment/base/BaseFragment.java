package com.online.market.fragment.base;

import java.io.File;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.online.market.utils.BitmapHelp;
import com.online.market.utils.FileUtils;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.Toast;

public class BaseFragment extends Fragment {
	protected BitmapUtils bitmapUtils;
	protected BitmapDisplayConfig config;
	protected DbUtils dbUtils;
	protected String dir;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		bitmapUtils=BitmapHelp.getBitmapUtils(getActivity());
		config=BitmapHelp.getDisplayConfig(getActivity(), 50, 50);
		dbUtils=DbUtils.create(getActivity());
		dir=FileUtils.getSDCardRoot()+getActivity().getPackageName()+File.separator;
		FileUtils.mkdirs(dir);
	}
	
	/** toast **/
	protected void toastMsg(int msgId) {
		toastMsg(getString(msgId));
	}
	
	/** toast **/
	public void toastMsg(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
	}

}
