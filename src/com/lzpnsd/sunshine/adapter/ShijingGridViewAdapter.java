package com.lzpnsd.sunshine.adapter;

import java.util.List;

import com.lzpnsd.sunshine.R;
import com.lzpnsd.sunshine.bean.ShijingBean;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ShijingGridViewAdapter extends BaseAdapter {

	private List<ShijingBean> mShijingBeans;
	private Context mContext;
	private LayoutInflater mInflater;
	
	public ShijingGridViewAdapter(Context context,List<ShijingBean> shijingBeans) {
		this.mShijingBeans = shijingBeans;
		this.mContext = context;
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
			convertView = mInflater.inflate(R.layout.view_shijing_item_pic, null);
			viewHolder.ivPic = (ImageView) convertView.findViewById(R.id.iv_shijing_item_pic);
			viewHolder.tvCity = (TextView) convertView.findViewById(R.id.tv_shijing_item_local);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		ShijingBean shijingBean = mShijingBeans.get(position);
//		viewHolder.ivPic.setImageResource(shijingBean.getPicUrl());
		viewHolder.tvCity.setText(shijingBean.getLocal());
		return convertView;
	}

	private class ViewHolder{
		ImageView ivPic;
		TextView tvCity;
	}
	
}
