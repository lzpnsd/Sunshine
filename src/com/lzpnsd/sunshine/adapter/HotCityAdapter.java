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
import android.widget.Button;

public class HotCityAdapter extends BaseAdapter {

	private List<CityBean> mCityBeans;
	private LayoutInflater mLayoutInflater;
	
	public HotCityAdapter(List<CityBean> mCityBeans) {
		this.mCityBeans = mCityBeans;
		mLayoutInflater = (LayoutInflater) SunshineApplication.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if(null == convertView){
			convertView = mLayoutInflater.inflate(R.layout.item_add_city_hot_city, null);
			viewHolder = new ViewHolder();
			viewHolder.btnHotCity = (Button) convertView.findViewById(R.id.btn_hot_city);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		if(0 == position){
		}else{
			CityBean cityBean = mCityBeans.get(position);
			
		}
		return null;
	}
	
	private class ViewHolder{
		Button btnHotCity;
	}
	

}
