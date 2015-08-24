package com.online.market.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.Log;

public class BitmapUtil {
	
	public static Bitmap getOriginBitmap(String path){
		try {
			Bitmap bitmap=BitmapFactory.decodeStream(new FileInputStream(path));
			return bitmap;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static Bitmap getThumbilBitmap(String srcPath,int width)
    {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        // 通过这个bitmap获取图片的宽和高&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, options);
        if (bitmap == null)
        {
//        	Log.e("majie", "bitmap为空");
//        	return null;
        }
        float realWidth = options.outWidth;
        float realHeight = options.outHeight;
//        Log.e("majie", "真实图片高度：" + realHeight + "宽度:" + realWidth);
        // 计算缩放比&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;
        int scale = (int) ((realHeight < realWidth ? realHeight : realWidth) / width);
        if (scale <= 0)
        {
            scale = 1;
        }
        options.inSampleSize = scale;
        options.inJustDecodeBounds = false;
        // 注意这次要把options.inJustDecodeBounds 设为 false,这次图片是要读取出来的。&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;
        bitmap = BitmapFactory.decodeFile(srcPath, options);
//        int w = bitmap.getWidth();
//        int h = bitmap.getHeight();
//        Log.e("majie", "缩略图高度：" + h + "宽度:" + w);
        return bitmap;
    }
	
	public static void saveBitmapToSdcard(Bitmap bitmap ,String desPath){
		File file=new File(desPath);
        try {
            FileOutputStream out=new FileOutputStream(file);
            if(bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)){
                out.flush();
                out.close();
            }
        } catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
		
	}
	
	   /***
     * 将图片画在画布中间
     * @param bm
     * @param width
     * @param height
     * @return
     */
    public static Bitmap getCanvasBitmap(Bitmap bm,int width,int height){
    	    int w=bm.getWidth();
    	    int h=bm.getHeight();
    	    if(w<width||h<height){
    	    	    Log.e("bitmaputils", "bitmap target size is not ");
    	    	    return bm;
    	    }
    	    Bitmap bitmap=Bitmap.createBitmap(bm, (w-width)/2, (h-height)/2, width, height);
    	    return bitmap;
    }
    
    public static Bitmap getBitmapFromRes(Context context,int resId){
    	Bitmap bitmap=BitmapFactory.decodeResource(context.getResources(), resId);
    	return bitmap;
    }
    
    /**
   	 * 缩放图片
   	 * @param bitmap	需要进行缩放的图片
   	 * @param wf		在宽度上缩放的比例
   	 * @param hf		在高度上缩放的比例
   	 * @return			返回缩放后的图片对象
   	 */
   	public static Bitmap zoom(Bitmap bitmap, float wf, float hf) {
   		Matrix matrix = new Matrix();
   		matrix.postScale(wf, hf);
   		return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
   	}
   	
   	/**
   	 * 缩放图片
   	 * @param bitmap	需要进行缩放的图片
   	 * @param newWidth	新图片的宽度，如果只按照高度进行缩放，请传入-1
   	 * @param newHeight	新图片的高度，如果只按照宽度进行缩放，请传入-1
   	 * @return			返回缩放后的图片对象
   	 */
   	public static Bitmap zoom(Bitmap bitmap, int newWidth, int newHeight){
   		float wf = 1.0f;
   		float hf = 1.0f;
   		int width = bitmap.getWidth();
   		int height = bitmap.getHeight();
   		if(newWidth <= 0){//按照高度进行缩放
   			hf = ((float)newHeight) / height;
   			wf = hf;
   		} else if(newHeight <= 0){//按照宽度进行缩放
   			wf = ((float)newWidth) / width;
   			hf = wf;
   		} else {
   			wf = ((float)newWidth) / width;
   			hf = ((float)newHeight) / height;
   		}
   		return zoom(bitmap, wf, hf);
   	}
   	
   	/**
   	 * 按照宽度等比例缩放图片
   	 * @param bitmap	需要进行缩放的图片
   	 * @param newWidth	新图片的宽度，如果只按照高度进行缩放，请传入-1
   	 * @return			返回缩放后的图片对象
   	 */
   	public static Bitmap zoomByWidth(Bitmap bitmap, int newWidth){
   		return zoom(bitmap, newWidth, -1);
   	}
   	
   	/**
   	 * 按照宽度等比例缩放图片
   	 * @param bitmap	需要进行缩放的图片
   	 * @param newHeight	新图片的宽度，如果只按照高度进行缩放，请传入-1
   	 * @return			返回缩放后的图片对象
   	 */
   	public static Bitmap zoomByHeight(Bitmap bitmap, int newHeight){
   		return zoom(bitmap, -1, newHeight);
   	}
   	
   	public static  Bitmap compressImage(Bitmap image) {  
   		  
           ByteArrayOutputStream baos = new ByteArrayOutputStream();  
           image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中  
           int options = 100;  
           while ( baos.toByteArray().length / 1024>100) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩         
               baos.reset();//重置baos即清空baos  
               image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中  
               options -= 10;//每次都减少10  
           }  
           ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中  
           Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片  
           return bitmap;  
       }  
   	
   	/***
   	 * 得到商品详情的bitmap
   	 * @param b
   	 * @return
   	 */
   	public static Bitmap getDetailBitmap(Bitmap b,int targetWidth,int targetHeight){
   	     Matrix m=new Matrix();
   	     int width=b.getWidth();
   	     int height=b.getHeight();
   	     float scale=0;
   	     if(width*3>height*4){
   	    	 scale=targetHeight*1.0f/height;
   	    	 m.postScale(scale, scale);
   	    	 int newWidth=(int) (targetWidth*1.0f/scale);
   	   	     b=Bitmap.createBitmap(b, (width-newWidth)/2, 0, newWidth, height, m, true);
   	   	     return b;
   	     }else{
   	    	 scale=targetWidth*1.0f/width;
   	    	 m.postScale(scale, scale);
   	    	 int newHeight=(int) (targetHeight*1.0f/scale);
   	   	     b=Bitmap.createBitmap(b, 0, (height-newHeight)/2, width, newHeight, m, true);
   	   	     return b;
   	     }
   	    
   	}

}
