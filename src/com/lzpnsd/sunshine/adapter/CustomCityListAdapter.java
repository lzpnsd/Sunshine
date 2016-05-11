package com.lzpnsd.sunshine.adapter;

import java.util.List;

import com.lzpnsd.sunshine.R;
import com.lzpnsd.sunshine.SunshineApplication;
import com.lzpnsd.sunshine.bean.CityListItemBean;
import com.lzpnsd.sunshine.util.WeatherIconUtil;
import com.lzpnsd.sunshine.view.CircleImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CustomCityListAdapter extends BaseAdapter {

	private List<CityListItemBean> mCityListItemBeans;
	private LayoutInflater mLayoutInflater;
	
	public CustomCityListAdapter(List<CityListItemBean> cityListItemBeans) {
		this.mCityListItemBeans = cityListItemBeans;
		mLayoutInflater = (LayoutInflater) SunshineApplication.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return mCityListItemBeans.size();
	}

	@Override
	public Object getItem(int position) {
		return mCityListItemBeans.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if(null == convertView){
			viewHolder = new ViewHolder();
			convertView = mLayoutInflater.inflate(R.layout.item_city_list, null);
			viewHolder.mCivCityDelete = (CircleImageView) convertView.findViewById(R.id.civ_city_list_delete);
			viewHolder.mCivCityWeatherIcon = (CircleImageView) convertView.findViewById(R.id.civ_city_list_weather);
			viewHolder.mTvCityName = (TextView) convertView.findViewById(R.id.tv_city_list_name);
			viewHolder.mTvCityWeather = (TextView) convertView.findViewById(R.id.tv_city_list_weather);
			viewHolder.mCivCityDelete.setTag(position);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		CityListItemBean cityListItemBean = mCityListItemBeans.get(position);
		viewHolder.mCivCityDelete.setVisibility(cityListItemBean.isShowDelete()?View.VISIBLE:View.GONE);
		viewHolder.mCivCityDelete.setOnClickListener(clickListener);
		ImageLoader.getInstance().displayImage("drawable://"+WeatherIconUtil.getDaySmallImageResource(cityListItemBean.getWeatherType()), viewHolder.mCivCityWeatherIcon);
		viewHolder.mTvCityName.setText(cityListItemBean.getCityName());
		viewHolder.mTvCityWeather.setText(cityListItemBean.getLowTemperature()+"/"+cityListItemBean.getHighTemperature()+"℃");
		return convertView;
	}

	private class ViewHolder{
		CircleImageView mCivCityDelete;
		CircleImageView mCivCityWeatherIcon;
		TextView mTvCityName;
		TextView mTvCityWeather;
	}
	
	private OnClickListener clickListener = new OnClickListener(){

		@Override
		public void onClick(View v) {
			mCityListItemBeans.remove(v.getTag());
			notifyDataSetChanged();
		}
	};
	
}
