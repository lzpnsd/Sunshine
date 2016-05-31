package com.lzpnsd.sunshine.adapter;

import com.lzpnsd.sunshine.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

public class SkinShowAdapter extends BaseAdapter {

	private Context mContext;
	private int mGalleryItem;
	
	// 图片
	private static int[] images = { R.drawable.bg0_fine_day, R.drawable.bg0_fine_night, 
			R.drawable.blur_bg0_fine_day, R.drawable.blur_bg0_fine_night, 
			R.drawable.bg_moderate_rain_day, R.drawable.blur_bg_moderate_rain_day, 
			R.drawable.bg_heavy_rain_night, R.drawable.blur_bg_heavy_rain_night,
			R.drawable.bg_snow_night, R.drawable.blur_bg_snow_night,
			R.drawable.bg_fog_and_haze, R.drawable.blur_bg_fog_and_haze };
	// 文字
//	private static int[] text = { R.string.text_weather_sun_day, R.string.text_weather_sun_night, 
//			R.string.text_weather_blur_day, R.string.text_weather_blur_night, 
//			R.string.text_weather_smallrain_day, R.string.text_weather_smallrain_night,
//			R.string.text_weather_bigrain_day, R.string.text_weather_bigrain_night,
//			R.string.text_weather_snow_day, R.string.text_weather_snow_night,
//			R.string.text_weather_fog_day, R.string.text_weather_fog_night };

	public SkinShowAdapter(Context context) {
		this.mContext = context;
		//自定义Gallery属性
		TypedArray array = context.obtainStyledAttributes(R.styleable.Gallery);
		mGalleryItem = array.getResourceId(R.styleable.Gallery_android_galleryItemBackground, 0);
		array.recycle();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return Integer.MAX_VALUE;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		 ImageView imageView = new ImageView(mContext);
		 imageView.setImageResource(images[position%images.length]);    // 设定图片给imageView对象  
		 imageView.setScaleType(ImageView.ScaleType.FIT_XY);            //重新设定图片的宽高  
		 imageView.setLayoutParams(new Gallery.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));// 重新设定Layout的宽高   
		 imageView.setBackgroundResource(mGalleryItem);       // 设定Gallery背景图  
	     return imageView; 
	}
}
