package com.online.market;

import java.io.File;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;

import com.online.market.beans.MyUser;
import com.online.market.utils.BitmapUtil;
import com.online.market.utils.FileUtils;
import com.online.market.utils.ProgressUtil;

public class RegisterActivity extends BaseActivity {
	int PICK_REQUEST_CODE = 0;
	private EditText etUsername, etUserpsw, eNickname;
	private RadioGroup group;
	private boolean gender = true;
	private ImageView userimg;
	private String avatarPath;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acitivity_register);

		initView();
        setListeners();
        
        initData();
	}
	
	private void getFileFromSD() {
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		// intent.setDataAndType(Uri.parse("/mnt/sdcard"), "text/plain");

		startActivityForResult(intent, PICK_REQUEST_CODE);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			Uri uri = data.getData();
			try {
				String[] pojo = { MediaStore.Images.Media.DATA };
				Cursor cursor = managedQuery(uri, pojo, null, null, null);
				if (cursor != null) {
					int colunm_index = cursor
							.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
					cursor.moveToFirst();
					String path = cursor.getString(colunm_index);

					if (path != null) {
					    Bitmap b= BitmapUtil.getThumbilBitmap(path,200);
					    userimg.setImageBitmap(b);
					    String dir=FileUtils.getSDCardRoot()+getPackageName()+File.separator;
					    FileUtils.mkdirs(dir);
					    avatarPath=dir+path.substring(path.lastIndexOf("/")+1);
					    BitmapUtil.saveBitmapToSdcard(b, avatarPath);
					    
					}
				}

			} catch (Exception e) {
			}

		}
	}

	@Override
	protected void initView() {
		
		etUsername = (EditText) findViewById(R.id.username);
		etUserpsw = (EditText) findViewById(R.id.userpsw);
		eNickname = (EditText) findViewById(R.id.nickname);
		userimg = (ImageView) findViewById(R.id.userimg);
		group = (RadioGroup) findViewById(R.id.sex);
		
		mBtnTitleMiddle.setVisibility(View.VISIBLE);
		mBtnTitleMiddle.setText("用户注册");
		mBtnTitleMiddle.setTextColor(getResources().getColor(R.color.white));
		
		mImgLeft.setVisibility(View.VISIBLE);
		mImgLeft.setBackgroundResource(R.drawable.back_bg_selector);
		
		mBtnTitleRight.setVisibility(View.VISIBLE);
		mBtnTitleRight.setText("提交");
		mBtnTitleRight.setTextColor(getResources().getColor(R.color.white));
		
	}

	@Override
	protected void initData() {
		
	}
	
	protected void setListeners(){
		mBtnTitleRight.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				String name = etUsername.getText().toString();
				String psw = etUserpsw.getText().toString();
				String realName = eNickname.getText().toString();
				if (name.equals("") || psw.equals("") || realName.equals("")
						) {
					toastMsg("请填写基本资料");
					return;
				}
				if(avatarPath==null){
					signUp(name, psw, realName, gender, null);
				}else{
					uploadAvatarFile(name, psw, realName, gender,new File(avatarPath));
				}
				
			}
		});

		
		group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (checkedId == R.id.male) {
					gender = true;
//					Toast.makeText(getApplicationContext(), "男", 2000).show();
				} else {
					gender = false;
				}

			}
		});

		userimg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getFileFromSD();
			}
		});
		
        mImgLeft.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
	}
	
	/**
	 * 注册用户
	 */
	private void signUp(final String name,String psw,final String realname,final boolean gender,BmobFile file) {
		ProgressUtil.showProgress(this, "");
		final MyUser myUser = new MyUser();
		myUser.setUsername(name);
		myUser.setPassword(psw);
		myUser.setNickname(realname);
		myUser.setGender(gender);
		myUser.setAvatar(file);
		myUser.signUp(this, new SaveListener() {

			@Override
			public void onSuccess() {
				toastMsg("注册成功:" + myUser.getUsername() + "-"
						+ myUser.getObjectId() + "-" + myUser.getCreatedAt()
						+ "-" + myUser.getSessionToken()+",是否验证："+myUser.getEmailVerified());
				ProgressUtil.closeProgress();
				finish();
			}

			@Override
			public void onFailure(int code, String msg) {
				ProgressUtil.closeProgress();
				toastMsg("注册失败:" + msg);
			}
		});
		
	}
	
	private void uploadAvatarFile(final String name,final String psw,final String  realname,final boolean gender,File file) {
		final BmobFile bmobFile = new BmobFile(file);
		
		bmobFile.uploadblock(this, new UploadFileListener() {

			@Override
			public void onSuccess() {
				Log.i("majie", "名称--"+bmobFile.getFileUrl(RegisterActivity.this)+"，文件名="+bmobFile.getFilename());
				signUp(name, psw, realname, gender,bmobFile);
			}

			@Override
			public void onProgress(Integer arg0) {
			}

			@Override
			public void onFailure(int arg0, String arg1) {
//				Log.i("majie", "-->uploadMovoieFile-->onFailure:" + arg0+",msg = "+arg1);
			}

		});

	}

}
