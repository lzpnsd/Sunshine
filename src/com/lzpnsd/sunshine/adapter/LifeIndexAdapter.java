package com.lzpnsd.sunshine.adapter;

import java.util.List;

import com.lzpnsd.sunshine.R;
import com.lzpnsd.sunshine.SunshineApplication;
import com.lzpnsd.sunshine.bean.LifeIndexBean;
import com.lzpnsd.sunshine.util.LogUtil;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class LifeIndexAdapter extends BaseAdapter {

	private LogUtil log = LogUtil.getLog(getClass());
	
	private List<LifeIndexBean> mLifeIndexBeans;
	private LayoutInflater mLayoutInflater;
	
	public LifeIndexAdapter(List<LifeIndexBean> mLifeIndexBeans) {
		this.mLifeIndexBeans = mLifeIndexBeans;
		mLayoutInflater = LayoutInflater.from(SunshineApplication.getContext());
	}

	@Override
	public int getCount() {
		log.d("mLifeIndexBeans.size = "+mLifeIndexBeans.size());
		return mLifeIndexBeans.size();
	}

	@Override
	public Object getItem(int position) {
		return mLifeIndexBeans.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if(null == convertView){
			convertView = mLayoutInflater.inflate(R.layout.item_weather_lifeindex, null);
			viewHolder = new ViewHolder();
			viewHolder.tvIndexName = (TextView) convertView.findViewById(R.id.tv_index_name);
			viewHolder.tvIndexValue = (TextView) convertView.findViewById(R.id.tv_index_value);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		LifeIndexBean lifeIndexBean = mLifeIndexBeans.get(position);
		viewHolder.tvIndexName.setText(lifeIndexBean.getName());
		viewHolder.tvIndexValue.setText(lifeIndexBean.getValue());
		return convertView;
	}
	
	private class ViewHolder{
		TextView tvIndexName;
		TextView tvIndexValue;
	}

}
