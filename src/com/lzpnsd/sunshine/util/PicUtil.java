package com.lzpnsd.sunshine.util;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class PicUtil {

	/**
	 * 
	 * @param picPath
	 * @return ByteArrayOutputStream
	 */
	public static OutputStream compressBitmap(String picPath) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(picPath, options);
		int height = options.outHeight;
		int width = options.outWidth;

		int inSampleSize = 1;
		int reqHeight = 800;
		int reqWidth = 480;
		if (height > reqHeight || width > reqWidth) {
			final int heightRatio = Math.round((float) height / (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}

		// 在内存中创建bitmap对象，这个对象按照缩放大小创建的
		options.inSampleSize = inSampleSize;
		options.inJustDecodeBounds = false;
		Bitmap bitmap = BitmapFactory.decodeFile(picPath, options);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 60, baos);
		return baos;
	}

}
