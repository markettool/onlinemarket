package com.online.market;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import cn.bmob.v3.listener.SaveListener;

import com.online.market.beans.MyUser;
import com.online.market.utils.MobileUtil;
import com.online.market.utils.ProgressUtil;

public class LoginActivity extends BaseActivity {
	private EditText etPhoneNum, etUserpsw;
	private Button signin;
	private TextView tvForgetPsw;
//	private Button btQQlogin;
	
//	private Tencent mTencent;
//	private UserInfo mInfo;
//	private String openid;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		initView();
		setListeners();
		
		initData();
	}

    protected void setListeners(){
    	   signin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String phonenum = etPhoneNum.getText().toString();
				String psw = etUserpsw.getText().toString();
				
				boolean isMoblieLogic=MobileUtil.isMobileNO(phonenum);
				if(!isMoblieLogic){
					toastMsg("手机号非法，请输入正确的手机号");
					return;
				}
				login(phonenum, psw);
			}
		});

		mBtnTitleRight.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				startActivity(RegisterActivity.class);
				finish();
			}
		});
		
//		btQQlogin.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View arg0) {
//				qqlogin();
//			}
//		});
		
        mImgLeft.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
        
        tvForgetPsw.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				startActivity(ResetPswActivity.class);
			}
		});
    }
    
//    private void qqlogin(){
//		mTencent = Tencent.createInstance("222222", this.getApplicationContext());
//		if (!mTencent.isSessionValid())
//		{
//		    mTencent.login(this, "", new BaseUiListener());
//		    ProgressUtil.showProgress(LoginActivity.this, "");
//		}
//	}

	@Override
	protected void initView() {
		signin = (Button) findViewById(R.id.signin);
		etPhoneNum = (EditText) findViewById(R.id.et_phonenum);
		etUserpsw = (EditText) findViewById(R.id.userpsw);
//		btQQlogin=(Button) findViewById(R.id.bt_qqlogin);
		
		tvForgetPsw=(TextView) findViewById(R.id.tv_forget_psw);
		tvForgetPsw.setText(Html.fromHtml("<u><font color=\"#023cfa\">忘记密码？</font></u>"));
		
		mBtnTitleMiddle.setVisibility(View.VISIBLE);
		mBtnTitleMiddle.setText("登录");
		mBtnTitleMiddle.setTextColor(getResources().getColor(R.color.white));
		
		mImgLeft.setVisibility(View.VISIBLE);
		mImgLeft.setBackgroundResource(R.drawable.back_bg_selector);
		
		mBtnTitleRight.setText("注册");
		mBtnTitleRight.setVisibility(View.VISIBLE);
		mBtnTitleRight.setTextColor(getResources().getColor(R.color.white));
	}

	@Override
	protected void initData() {

	}
	
	/**
	 * 登陆用户
	 */
	private void login(String phonenum,String psw) {
		ProgressUtil.showProgress(this, "");
		final MyUser bu = new MyUser();
		bu.setUsername(phonenum);
		bu.setPassword(psw);
		bu.login(this, new SaveListener() {

			@Override
			public void onSuccess() {
				toastMsg(bu.getUsername() + "登陆成功");
				ProgressUtil.closeProgress();
				finish();
			}

			@Override
			public void onFailure(int code, String msg) {
				ProgressUtil.closeProgress();
				toastMsg("登陆失败:" + msg);
				
			}
		});
	}
	
//	private class BaseUiListener implements IUiListener {
//
//		@Override
//		public void onCancel() {
//			ProgressUtil.closeProgress();
//		}
//
//		@Override
//		public void onComplete(Object object) {
////			Log.e("majie",object.toString());
//			try {
//				JSONObject jsonObject=new JSONObject(object.toString());
//				openid=jsonObject.getString("openid");
//			} catch (JSONException e) {
//				e.printStackTrace();
//			}
//			
//			mInfo = new UserInfo(LoginActivity.this, mTencent.getQQToken());
//			mInfo.getUserInfo(listener);
//		}
//
//		@Override
//		public void onError(UiError arg0) {
//			toastMsg(arg0.errorDetail);
//			ProgressUtil.closeProgress();
//		}
//	}
	
//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//	    mTencent.onActivityResult(requestCode, resultCode, data);
//	}
	
//	private IUiListener listener = new IUiListener() {
//
//		@Override
//		public void onError(UiError e) {
//			ProgressUtil.closeProgress();
//			toastMsg(e.errorDetail);
//		}
//
//		@Override
//		public void onComplete(final Object response) {
//			QQUser qqUser=QQUser.parse(response.toString());
//			final MyUser myUser=new MyUser();
//			myUser.setUsername(openid);
//			myUser.setPassword("m448279895");
//			myUser.setNickname(qqUser.nikename);
//			myUser.login(getApplicationContext(), new SaveListener() {
//				
//				@Override
//				public void onSuccess() {
//					toastMsg("登录成功");
//					ProgressUtil.closeProgress();
//					finish();
//				}
//				
//				@Override
//				public void onFailure(int arg0, String arg1) {
//                        myUser.signUp(getApplicationContext(), new SaveListener() {
//						
//						@Override
//						public void onSuccess() {
//							toastMsg("注册成功");
//							ProgressUtil.closeProgress();
//							finish();
//						}
//						
//						@Override
//						public void onFailure(int arg0, String arg1) {
//							ProgressUtil.closeProgress();
//						}
//					});
//				}
//			});
//		}
//
//		@Override
//		public void onCancel() {
//			ProgressUtil.closeProgress();
//		}
//	};
	
}
