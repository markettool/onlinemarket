package com.online.market.dialog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;

import com.online.market.utils.DialogUtil;
import com.online.market.utils.FileUtils;

public class InstallWxDialog {
	
	public static void show(final Context context){
		DialogUtil.dialog(context, "微信支付需要安装插件，确认现在安装吗？", "确定", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int arg1) {
				try {
					String sd=FileUtils.getSDCardRoot();
					String f=sd+"com.online.market"+File.separator+"plugin.apk";
					copyFileToSD(context, f);
					Intent intent = new Intent(Intent.ACTION_VIEW);
					intent.setDataAndType(Uri.fromFile(new File(f)), "application/vnd.android.package-archive");
					context.startActivity(intent);
				} catch (IOException e) {
					e.printStackTrace();
				}
				dialog.dismiss();
			}
		}, "取消", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int arg1) {
				dialog.dismiss();
			}
		});
	}
	
	private static void copyFileToSD(Context context,String outFileName) throws IOException 
    {  
        OutputStream myOutput = new FileOutputStream(outFileName);  
        InputStream myInput = context.getAssets().open("plugin.apk");  
        byte[] buffer = new byte[1024];  
        int length = myInput.read(buffer);
        while(length > 0)
        {
            myOutput.write(buffer, 0, length); 
            length = myInput.read(buffer);
        }
        
        myOutput.flush();  
        myInput.close();  
        myOutput.close();        
    }  

}
