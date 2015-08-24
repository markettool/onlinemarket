package com.online.market.utils;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;

public class DialogUtil {

	public static void dialog(Context context,String msg,String title,String positiveText,OnClickListener positiveListener,String negativeText,OnClickListener negativeListener) {
		AlertDialog.Builder builder = new Builder(context);
		builder.setMessage(msg);

		if(title!=null){
			builder.setTitle(title);
		}
		
		builder.setPositiveButton(positiveText, positiveListener);
		builder.setNegativeButton(negativeText, negativeListener);
		builder.create().show();
	}
	public static void dialog(Context context,String msg,String positiveText,OnClickListener positiveListener,String negativeText,OnClickListener negativeListener) {
		dialog(context, msg, null, positiveText, positiveListener, negativeText, negativeListener);
	}
	
}
