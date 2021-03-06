package com.lzpnsd.sunshine.util;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;

public class BitmapUtil {

	private static final LogUtil log = LogUtil.getLog(BitmapUtil.class);
	
	public static Bitmap compressBitmap(String path){
		BitmapFactory.Options newOpts = new BitmapFactory.Options();  
        newOpts.inJustDecodeBounds = true;//只读边,不读内容  
        Bitmap bitmap = BitmapFactory.decodeFile(path, newOpts);  
//        log.d("bitmap size = "+bitmap.getByteCount());
        newOpts.inJustDecodeBounds = false;  
        int w = newOpts.outWidth;  
        int h = newOpts.outHeight;  
        float hh = 500f;//  
        float ww = 500f;// 
        int be = 1;  
        if (w > h && w > ww) {  
            be = (int) (newOpts.outWidth / ww);  
        } else if (w < h && h > hh) {  
            be = (int) (newOpts.outHeight / hh);  
        }  
        if (be <= 0)  
            be = 1;  
        newOpts.inSampleSize = be;//设置采样率  
          
        newOpts.inPreferredConfig = Config.ARGB_8888;//该模式是默认的,可不设  
        newOpts.inPurgeable = true;// 同时设置才会有效  
        newOpts.inInputShareable = true;//当系统内存不够时候图片自动被回收  
        
        bitmap = BitmapFactory.decodeFile(path, newOpts);
        log.d("bitmap height = "+bitmap.getHeight() +",width = "+bitmap.getWidth());
        log.d("bitmap new size = "+bitmap.getByteCount());
        return bitmap;  
	}
	
}
