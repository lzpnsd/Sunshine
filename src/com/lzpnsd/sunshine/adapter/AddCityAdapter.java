package com.lzpnsd.sunshine.adapter;

import java.util.List;

import com.lzpnsd.sunshine.R;
import com.lzpnsd.sunshine.SunshineApplication;
import com.lzpnsd.sunshine.bean.CityBean;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class AddCityAdapter extends BaseAdapter {

	private List<CityBean> mCityBeans;
	private LayoutInflater mInflater;
	
	public AddCityAdapter(List<CityBean> mCityBeans) {
		this.mCityBeans = mCityBeans;
		mInflater = (LayoutInflater) SunshineApplication.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return mCityBeans.size();
	}

	@Override
	public Object getItem(int position) {
		return mCityBeans.get(position);
	}

	@Override
	public long getItemId(int position) {
		return Long.parseLong(mCityBeans.get(position).getAreaId());
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if(null == convertView){
			viewHolder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.item_add_city, null);
			viewHolder.mTvCityName = (TextView) convertView.findViewById(R.id.tv_item_add_city_name);
			viewHolder.mTvDistrict = (TextView) convertView.findViewById(R.id.tv_item_add_city_district);
			viewHolder.mTvProvince = (TextView) convertView.findViewById(R.id.tv_item_add_city_province);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		CityBean cityBean = mCityBeans.get(position);
		viewHolder.mTvCityName.setText(cityBean.getNameCn());
		viewHolder.mTvDistrict.setText(cityBean.getDistrictCn());
		viewHolder.mTvProvince.setText(cityBean.getProvCn());
		return convertView;
	}

	private class ViewHolder{
		TextView mTvCityName;
		TextView mTvDistrict;
		TextView mTvProvince;
	}
	
}
