package com.online.market;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import cn.bmob.v3.listener.UpdateListener;

import com.online.market.beans.MyUser;
import com.online.market.utils.ProgressUtil;
/***
 * 输入邀请码
 * @author majie
 *
 */
public class InviteCodeActivity extends BaseActivity {
	
	private Button btSubmit;
	private EditText etByInviteCode;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_invitecode);
		
		initView();
		setListeners();
		initData();
	}

	@Override
	protected void initView() {
		mBtnTitleMiddle.setVisibility(View.VISIBLE);
		mBtnTitleMiddle.setText("输入推荐码");
		mBtnTitleMiddle.setTextColor(getResources().getColor(R.color.white));
		
		mImgLeft.setVisibility(View.VISIBLE);
		mImgLeft.setBackgroundResource(R.drawable.back_bg_selector);
		
		etByInviteCode=(EditText) findViewById(R.id.et_byinvitecode);
		btSubmit=(Button) findViewById(R.id.bt_submit);
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
			public void onClick(View arg0) {
				String byInviteCode=etByInviteCode.getText().toString();
				if(TextUtils.isEmpty(byInviteCode)){
					toastMsg("邀请码输入为空");
					return;
				}
				update(byInviteCode);
			}
		});
	}
	
	private void update(String byInviteCode) {
		final MyUser myUser = new MyUser();
		myUser.setUsername(user.getUsername());
		myUser.setNickname(user.getNickname());
		myUser.setPassword(user.getPassword());
		myUser.setInviteCode(user.getInviteCode());
		myUser.setByInviteCode(byInviteCode);
		myUser.setCousumed(user.isCousumed());
		myUser.setObjectId(user.getObjectId());
		myUser.update(this, new UpdateListener() {
			
			@Override
			public void onSuccess() {
 
				toastMsg("提交成功");
				ProgressUtil.closeProgress();
				finish();
			}
			
			@Override
			public void onFailure(int arg0, String arg1) {
				toastMsg(arg1);
				ProgressUtil.closeProgress();
				
			}
		});
		
	}

}
