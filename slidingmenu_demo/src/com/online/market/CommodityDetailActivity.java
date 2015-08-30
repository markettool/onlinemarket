package com.online.market;

import java.text.DecimalFormat;

import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.bmob.v3.AsyncCustomEndpoints;
import cn.bmob.v3.listener.CloudCodeListener;

import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.callback.BitmapLoadCallBack;
import com.lidroid.xutils.bitmap.callback.BitmapLoadFrom;
import com.lidroid.xutils.exception.DbException;
import com.online.market.beans.CommodityBean;
import com.online.market.beans.ShopCartaBean;
import com.online.market.utils.BitmapHelp;
import com.online.market.utils.BitmapUtil;

public class CommodityDetailActivity extends BaseActivity {
	
	private CommodityBean bean;
	private ImageView ivPic;
	private TextView tvName;
	private TextView tvPrice;
	private TextView tvOriginPrice;
	private EditText tvShopcartNum;
	private ImageView ivDelete;
	private ImageView ivAdd;
	private TextView tvTotalPrice;
	private Button btSubmit;
	private int number=1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activiity_detail);
		
		initView();
		setListeners();
		initData();
	}

	@Override
	protected void initView() {

		ivPic=(ImageView) findViewById(R.id.commodity_pic);
		ivPic.setLayoutParams(new RelativeLayout.LayoutParams(screenWidth, screenWidth));
		
		tvName=(TextView) findViewById(R.id.tv_name);
		tvPrice=(TextView) findViewById(R.id.tv_price);
		tvOriginPrice=(TextView) findViewById(R.id.tv_origin_price);
		
		tvShopcartNum=(EditText) findViewById(R.id.shopcart_num);
		ivDelete=(ImageView) findViewById(R.id.iv_delete);
		ivAdd=(ImageView) findViewById( R.id.iv_add);
		btSubmit=(Button) findViewById(R.id.bt_submit);
		tvTotalPrice=(TextView) findViewById(R.id.tv_total_price);
		
		mBtnTitleMiddle.setVisibility(View.VISIBLE);
		mBtnTitleMiddle.setTextColor(getResources().getColor(R.color.white));
		
		mImgLeft.setVisibility(View.VISIBLE);
		mImgLeft.setBackgroundResource(R.drawable.back_bg_selector);
		
	}

	@Override
	protected void initData() {
		bean=(CommodityBean) getIntent().getSerializableExtra("commodity");
		if(bean==null){
			finish();
			return;
		}
		config=BitmapHelp.getDisplayConfig(this, 350, 350);
		bitmapUtils.display(ivPic, bean.getPics().getFileUrl(this),config,new BitmapLoadCallBack<View>() {

			@Override
			public void onLoadCompleted(View arg0, String arg1, Bitmap bitmap,
					BitmapDisplayConfig arg3, BitmapLoadFrom arg4) {
				bitmap=BitmapUtil.getDetailBitmap(bitmap, screenWidth,screenWidth);
				ivPic.setImageBitmap(bitmap);
			}

			@Override
			public void onLoadFailed(View arg0, String arg1, Drawable arg2) {
				
			}
		});
		
		tvName.setText(bean.getName());
		float price=bean.getPrice();
		DecimalFormat   df=new DecimalFormat("#.#");   
		String originPrice=df.format(price*1.2f);
		tvPrice.setText("售价："+price+" 元");
		tvOriginPrice.setText("原价："+originPrice+" 元");
		
		mBtnTitleMiddle.setText(bean.getName());
		tvTotalPrice.setText("总计：￥"+price);
		updateScan(bean.getObjectId());
	}
	
	private void updateScan(String objectId){
		JSONObject object=new JSONObject();
		try {
			object.put("objectId", objectId);
			AsyncCustomEndpoints ace = new AsyncCustomEndpoints();
			//第一个参数是上下文对象，第二个参数是云端代码的方法名称，第三个参数是上传到云端代码的参数列表（JSONObject cloudCodeParams），第四个参数是回调类
			ace.callEndpoint(CommodityDetailActivity.this, "updatesold", object, 
			    new CloudCodeListener() {
			            @Override
			            public void onSuccess(Object object) {
			            	Log.e("commodity detail", object.toString());
			            }
			            @Override
			            public void onFailure(int code, String msg) {
			               Log.e("detail", msg);
			            }
			        });
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void setListeners() {
        mImgLeft.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		
        ivDelete.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(number>0){
					number--;
					tvShopcartNum.setText(""+number);
					tvTotalPrice.setText("总计：￥"+(bean.getPrice()*number));
				}
			}
		});
		ivAdd.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {

				number++;
				tvShopcartNum.setText(""+number);
				tvTotalPrice.setText("总计：￥"+(bean.getPrice()*number));
			}
		});
		
		btSubmit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				ShopCartaBean p=new ShopCartaBean();
				p.setPrice(bean.getPrice());
				p.setId(bean.getObjectId());
				p.setName(bean.getName());
				p.setSold(bean.getSold());
				p.setPic(bean.getPics().getFileUrl(CommodityDetailActivity.this));
				p.setNumber(number);
				try {
					dbUtils.save(p);
				} catch (DbException e) {
					e.printStackTrace();
				}
				toastMsg(bean.getName()+" 已加入购物车,可进入我的购物车查看");
				finish();
			}
		});
	}

}
