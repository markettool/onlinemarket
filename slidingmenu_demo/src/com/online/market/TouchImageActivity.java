package com.online.market;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;

import com.lidroid.xutils.BitmapUtils;
import com.online.market.view.TouchImageView;

public class TouchImageActivity extends Activity {
	private TouchImageView tiv;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_touchimageview);
		
		tiv=(TouchImageView) findViewById(R.id.tiv);
		tiv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		
		initData();
		
	}
	
	private void initData(){
		String picUrl=getIntent().getStringExtra("pic_url");
		BitmapUtils bitmapUtils=new BitmapUtils(this);
		bitmapUtils.display(tiv, picUrl);
	}
	

}
