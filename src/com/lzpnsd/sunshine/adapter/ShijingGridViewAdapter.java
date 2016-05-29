package com.lzpnsd.sunshine.adapter;

import java.util.List;

import com.lzpnsd.sunshine.R;
import com.lzpnsd.sunshine.bean.ShijingBean;
import com.lzpnsd.sunshine.util.LogUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ShijingGridViewAdapter extends BaseAdapter {

	private final LogUtil log = LogUtil.getLog(getClass());
	
	private List<ShijingBean> mShijingBeans;
	private Context mContext;
	private LayoutInflater mInflater;
	private DisplayImageOptions mOptions;
	
	public ShijingGridViewAdapter(Context context,List<ShijingBean> shijingBeans) {
		this.mShijingBeans = shijingBeans;
		this.mContext = context;
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mOptions = new DisplayImageOptions.Builder()
				.considerExifParams(true)
				.imageScaleType(ImageScaleType.IN_SAMPLE_INT)
				.build();
	}

	@Override
	public int getCount() {
		return mShijingBeans.size();
	}

	@Override
	public Object getItem(int position) {
		return mShijingBeans.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if(convertView == null){
			viewHolder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.item_shijing_pic, null);
			viewHolder.ivPic = (ImageView) convertView.findViewById(R.id.iv_shijing_item_pic);
			viewHolder.tvCity = (TextView) convertView.findViewById(R.id.tv_shijing_item_local);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		ShijingBean shijingBean = mShijingBeans.get(position);
//		viewHolder.ivPic.setImageResource(shijingBean.getPicUrl());
		viewHolder.tvCity.setText(shijingBean.getLocation());
		ImageLoader.getInstance().displayImage(shijingBean.getPicUrl(), viewHolder.ivPic,mOptions);
		return convertView;
	}

	private class ViewHolder{
		ImageView ivPic;
		TextView tvCity;
	}
	
}
