package com.online.market;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.RequestSMSCodeListener;
import cn.bmob.v3.listener.ResetPasswordByCodeListener;

import com.online.market.utils.MobileUtil;
import com.online.market.utils.ProgressUtil;

public class ResetPswActivity extends BaseActivity {
	
	private String phonenum ;
	private String userpsw ;
	private String code;
	
	private Button btSubmit;
	private Button btVerify;
	
	private EditText etPhoneNum, etUserpsw,etCode;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_resetpsw);
		
		initView();
		setListeners();
		initData();
	}

	@Override
	protected void initView() {

		mBtnTitleMiddle.setVisibility(View.VISIBLE);
		mBtnTitleMiddle.setTextColor(getResources().getColor(R.color.white));
		mBtnTitleMiddle.setText("重置密码");
		
		mImgLeft.setVisibility(View.VISIBLE);
		mImgLeft.setBackgroundResource(R.drawable.back_bg_selector);
		
		etPhoneNum = (EditText) findViewById(R.id.et_phonenum);
		etUserpsw = (EditText) findViewById(R.id.userpsw);
		etCode =(EditText) findViewById(R.id.et_code);
		btSubmit=(Button) findViewById(R.id.bt_submit);
		btVerify=(Button) findViewById(R.id.btn_verify);
	}

	@Override
	protected void initData() {

	}

	@Override
	protected void setListeners() {
		 mImgLeft.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					finish();
				}
			});
		 
		 btSubmit.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					phonenum = etPhoneNum.getText().toString();
					userpsw = etUserpsw.getText().toString();
					code=etCode.getText().toString();
					
					if (TextUtils.isEmpty(phonenum) || TextUtils.isEmpty(userpsw) || TextUtils.isEmpty(code)
							) {
						toastMsg("请填写基本资料");
						return;
					}
					resetPsw();
				}
			});
		 
		 btVerify.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					String phonenum = etPhoneNum.getText().toString();
//					if(TextUtils.isEmpty(phonenum)){
//						toastMsg("手机号为空");
//						return;
//					}
					boolean isMoblieLogic=MobileUtil.isMobileNO(phonenum);
					if(!isMoblieLogic){
						toastMsg("手机号非法，请填写正确的手机号");
						return;
					}
					start();
					
					BmobSMS.requestSMSCode(ResetPswActivity.this, phonenum, "register",new RequestSMSCodeListener() {
						
						@Override
						public void done(Integer smsId, BmobException ex) {
							
							if(ex==null){//验证码发送成功
								Log.i("onlinemarket", "短信id："+smsId);
							}
						}
					});
				}
			});
	}
	
	private void start(){
		btVerify.setEnabled(false);
		btVerify.setBackgroundColor(getResources().getColor(R.color.text_gray));
		new Thread(){
			@Override
			public void run() {
				super.run();
				int i=120;
				while(i>0){
					i--;
					mHandler.sendEmptyMessage(i);
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			
		}.start();
	}
	
	private Handler mHandler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			if(msg.what==0){
				btVerify.setEnabled(true);
				btVerify.setText("验证");
				btVerify.setBackgroundResource(R.drawable.btn_orange_corner_selector);
				
			}else{
				btVerify.setText(msg.what+"秒");
			}
		};
	};
	
	private void resetPsw(){
		ProgressUtil.showProgress(this, "");
		BmobUser.resetPasswordBySMSCode(this, code, userpsw, new ResetPasswordByCodeListener() {
			
			@Override
			public void done(BmobException arg0) {
//				Log.e("resetpsw", arg0.getMessage());
				ProgressUtil.closeProgress();
				if(arg0==null){
					toastMsg("重置成功");
					finish();
				}else{
					toastMsg("重置失败");
				}
			}
		});
	}

}
