package com.online.market;

import java.io.File;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

import com.online.market.beans.MyUser;
import com.online.market.utils.BitmapUtil;
import com.online.market.utils.FileUtils;
import com.online.market.utils.ProgressUtil;

public class MyDataActivity extends BaseActivity {
	int PICK_REQUEST_CODE = 0;
	
	private EditText  etUserage, eNickname;
	private RadioGroup rgSex;
	private ImageView userimg;
	private boolean gender=true;
	private String avatarPath;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mydata);
		initView();
		setListeners();
		initData();
	}

	@Override
	protected void initView() {
		eNickname = (EditText) findViewById(R.id.username);
		etUserage = (EditText) findViewById(R.id.userage);
		rgSex=(RadioGroup) findViewById(R.id.rg_sex);
		userimg = (ImageView) findViewById(R.id.userimg);
		
		mBtnTitleMiddle.setVisibility(View.VISIBLE);
		mBtnTitleMiddle.setText("我的资料");
		mBtnTitleMiddle.setTextColor(getResources().getColor(R.color.white));
		
		mImgLeft.setVisibility(View.VISIBLE);
		mImgLeft.setBackgroundResource(R.drawable.back_bg_selector);
		
		mBtnTitleRight.setVisibility(View.VISIBLE);
		mBtnTitleRight.setText("确认");
		mBtnTitleRight.setTextColor(getResources().getColor(R.color.white));
		
	}

	@Override
	protected void initData() {
		if(user==null){
			startActivity(LoginActivity.class);
			finish();
			return;
		}
		eNickname.setText(user.getNickname());
		etUserage.setText(""+user.getAge());
		if(user.getGender()){
			rgSex.check(R.id.male);
		}
		else{
			rgSex.check(R.id.female);
		}
		BmobFile avatar=user.getAvatar();
		avatar.loadImageThumbnail(this, userimg, 100, 100);

	}

	@Override
	protected void setListeners() {
		mBtnTitleRight.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				String nickname = eNickname.getText().toString();
				String ageS = etUserage.getText().toString();
				int age=new Integer(ageS);
				if(avatarPath==null){
					signUp(nickname,  age, gender, null);
				}else{
					uploadAvatarFile(nickname, age, gender,new File(avatarPath));
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
		  
        rgSex.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {

				switch (arg1) {
				case R.id.male:
					gender=true;
					break;
				case R.id.female:
					gender=false;
					break;
				}
			}
		});
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
	
	private void signUp(final String nickname,final int age,final boolean gender,BmobFile file) {
		ProgressUtil.showProgress(this, "");
		final MyUser myUser = new MyUser();
		myUser.setNickname(nickname);
		myUser.setObjectId(user.getObjectId());
		myUser.setGender(gender);
		myUser.setAge(age);
		myUser.setAvatar(file);
		myUser.update(this, new UpdateListener() {
			
			@Override
			public void onSuccess() {
 
				toastMsg("更新成功");
				ProgressUtil.closeProgress();
			}
			
			@Override
			public void onFailure(int arg0, String arg1) {
				toastMsg("更新fail");
				ProgressUtil.closeProgress();
				
			}
		});
		
	}
	
	private void uploadAvatarFile(final String nickname,final int  age,final boolean gender,File file) {
		final BmobFile bmobFile = new BmobFile(file);
		
		bmobFile.uploadblock(this, new UploadFileListener() {

			@Override
			public void onSuccess() {
//				Log.i("majie", "名称--"+bmobFile.getFileUrl(MyDataActivity.this)+"，文件名="+bmobFile.getFilename());
				signUp(nickname, age, gender, bmobFile);
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
