package com.online.market.fragment;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.online.market.LoginActivity;
import com.online.market.MainActivity;
import com.online.market.MyDataActivity;
import com.online.market.MyOrderActivity;
import com.online.market.MyShopCartActivity;
import com.online.market.R;
import com.online.market.SettingActivity;
import com.online.market.beans.MyUser;
import com.online.market.fragment.base.BaseFragment;
import com.online.market.utils.BitmapUtil;
import com.online.market.utils.FileUtils;
/**
 * @date 2014/11/14
 * @author majie
 * @description 侧边栏菜单
 */
public class LeftFragment extends BaseFragment implements OnClickListener{
	private View myshopcart;
	private View myorder;
	private View settings;
	private View tvShare;
	private RelativeLayout myData;
	private TextView username;
	private ImageView userimg;
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.layout_menu, null);
		findViews(view);
		downloadUserPic();
		return view;
	}
		
	public void findViews(View view) {
		myshopcart = view.findViewById(R.id.tvToday);
		myorder = view.findViewById(R.id.tvLastlist);
		settings = view.findViewById(R.id.tvMySettings);
		tvShare= view.findViewById(R.id.tvShare);
		
		myData=(RelativeLayout) view.findViewById(R.id.my_data);
		
		username=(TextView) view.findViewById(R.id.user_name);
		userimg=(ImageView) view.findViewById(R.id.avatar_pic);
		userimg.setOnClickListener(this);
		myshopcart.setOnClickListener(this);
		myorder.setOnClickListener(this);
		settings.setOnClickListener(this);
		myData.setOnClickListener(this);
		tvShare.setOnClickListener(this);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		refresh();
	};
	
	private void refresh(){
		if(user!=null){
			 username.setText(user.getNickname());
			 if(user.getAvatar()!=null){
				 downloadPic(user.getAvatar());
			 }
		}
		else{
			username.setText("未登录");
		}
	}
	
	private void downloadPic(BmobFile avatar){
		HttpUtils http=new HttpUtils();
		final String path=dir+user.getUsername()+".png";
		if(FileUtils.isFileExist(path)){
			userimg.setImageBitmap(BitmapUtil.getThumbilBitmap(path, 100));
            return;
		}
		http.download(avatar.getFileUrl(getActivity()), path, new RequestCallBack<File>() {
			
			@Override
			public void onSuccess(ResponseInfo<File> arg0) {
				userimg.setImageBitmap(BitmapUtil.getThumbilBitmap(path, 100));
			}
			
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				
			}
		});
	}

	
	@Override
	public void onClick(View v) {
		Fragment newContent = null;
		String title = null;
		switch (v.getId()) {
		case R.id.tvToday: // account
			getActivity().startActivity(new Intent(getActivity(), MyShopCartActivity.class));
			break;
		case R.id.tvLastlist:// myorder
			getActivity().startActivity(new Intent(getActivity(), MyOrderActivity.class));
			break;
		case R.id.tvMySettings: // 设置
			getActivity().startActivity(new Intent(getActivity(), SettingActivity.class));
			break;
			
		case R.id.tvShare:
			onClickShare();
			break;
		case R.id.my_data:
		case R.id.avatar_pic:
			if(user==null){
				getActivity().startActivity(new Intent(getActivity(), LoginActivity.class));
			}else{
				getActivity().startActivity(new Intent(getActivity(), MyDataActivity.class));
			}
			break;
		default:
			break;
		}
		if (newContent != null) {
			switchFragment(newContent, title);
		}
	}
	
	/**
	 * 切换fragment
	 * @param fragment
	 */
	private void switchFragment(Fragment fragment, String title) {
		if (getActivity() == null) {
			return;
		}
		if (getActivity() instanceof MainActivity) {
			MainActivity fca = (MainActivity) getActivity();
			fca.switchContent(fragment, title);
		}
	}
	
	private void downloadUserPic(){
		String dir=FileUtils.getSDCardRoot()+getActivity().getPackageName()+File.separator;
		File dirFile=new File(dir);
		if(!dirFile.exists()){
			dirFile.mkdirs();
		}
		MyUser myUser=BmobUser.getCurrentUser(getActivity(), MyUser.class);
		if(myUser==null){
			return;
		}
	}
	
	private void onClickShare() {  
		  
        Intent intent=new Intent(Intent.ACTION_SEND);   
        intent.setType("text/plain");   
        intent.putExtra(Intent.EXTRA_SUBJECT, "天天在线");   
        intent.putExtra(Intent.EXTRA_TEXT, "天天在线，您掌上的超市!" +
        		"");    
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);   
        startActivity(Intent.createChooser(intent, getActivity().getTitle()));   
  
    } 
	
}
