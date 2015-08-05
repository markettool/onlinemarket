package com.online.market;

import java.text.DecimalFormat;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.exception.DbException;
import com.online.market.beans.CommodityBean;
import com.online.market.beans.ShopCartaBean;
import com.online.market.utils.BitmapHelp;

public class CommodityDetailActivity extends BaseActivity {
	
	private CommodityBean bean;
	private ImageView ivPic;
	private TextView tvName;
	private TextView tvPrice;
	private TextView tvOriginPrice;
	private EditText tvShopcartNum;
	private ImageView ivDelete;
	private ImageView ivAdd;
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
		tvName=(TextView) findViewById(R.id.tv_name);
		tvPrice=(TextView) findViewById(R.id.tv_price);
		tvOriginPrice=(TextView) findViewById(R.id.tv_origin_price);
		
		tvShopcartNum=(EditText) findViewById(R.id.shopcart_num);
		ivDelete=(ImageView) findViewById(R.id.iv_delete);
		ivAdd=(ImageView) findViewById( R.id.iv_add);
		btSubmit=(Button) findViewById(R.id.bt_submit);
		
		mBtnTitleMiddle.setVisibility(View.VISIBLE);
		mBtnTitleMiddle.setTextColor(getResources().getColor(R.color.white));
		
		mImgLeft.setVisibility(View.VISIBLE);
		mImgLeft.setBackgroundResource(R.drawable.back_bg_selector);
		
//		mBtnTitleRight.setVisibility(View.VISIBLE);
//		mBtnTitleRight.setText("加入");
//		mBtnTitleRight.setTextColor(getResources().getColor(R.color.white));
	}

	@Override
	protected void initData() {

		bean=(CommodityBean) getIntent().getSerializableExtra("commodity");
		if(bean==null){
			finish();
			return;
		}
		config=BitmapHelp.getDisplayConfig(this, 150, 150);
		bitmapUtils.display(ivPic, bean.getPics().getFileUrl(this),config);
		
		tvName.setText(bean.getName());
		float price=bean.getPrice();
		DecimalFormat   df=new DecimalFormat("#.#");   
		String originPrice=df.format(price*1.2f);
		tvPrice.setText("售价： "+price+" 元");
		tvOriginPrice.setText("原价： "+originPrice+" 元");
		
		mBtnTitleMiddle.setText(bean.getName());
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
				}
			}
		});
		ivAdd.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {

				number++;
				tvShopcartNum.setText(""+number);
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
