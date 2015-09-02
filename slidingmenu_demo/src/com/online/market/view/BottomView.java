package com.online.market.view;

import java.text.DecimalFormat;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.online.market.R;

public class BottomView extends LinearLayout {
	
	private TextView tvTotalPrice;
	private Button btSubmit;

	public BottomView(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater inflater=LayoutInflater.from(context);
		View view=inflater.inflate(R.layout.view_bottombar, null);
		setOrientation(LinearLayout.HORIZONTAL);
		addView(view);
		tvTotalPrice=(TextView) view.findViewById(R.id.tv_total_price);
		btSubmit=(Button) view.findViewById(R.id.bt_submit);
	}
	
	public void setText(String text){
		btSubmit.setText(text);
		btSubmit.setPadding(16, 0, 16, 0);
	}
	
	public void setTotalPrice(float totalPrice){
		DecimalFormat   df=new DecimalFormat("#.##");   
		String totalPriceStr=df.format(totalPrice);
		tvTotalPrice.setText("总计：￥"+totalPriceStr);
	}
	
	
	@Override
	public void setOnClickListener(OnClickListener l) {
		btSubmit.setOnClickListener(l);
	}

}
