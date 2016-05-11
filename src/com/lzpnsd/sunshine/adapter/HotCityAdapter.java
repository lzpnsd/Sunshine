package com.lzpnsd.sunshine.adapter;

import java.util.List;

import com.lzpnsd.sunshine.R;
import com.lzpnsd.sunshine.SunshineApplication;
import com.lzpnsd.sunshine.bean.CityBean;
import com.lzpnsd.sunshine.manager.DataManager;
import com.lzpnsd.sunshine.util.AdaptationUtil;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;

public class HotCityAdapter extends BaseAdapter {

	private List<CityBean> mCityBeans;
	private LayoutInflater mLayoutInflater;
	
	public HotCityAdapter(List<CityBean> mCityBeans) {
		this.mCityBeans = mCityBeans;
		mLayoutInflater = (LayoutInflater) SunshineApplication.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return mCityBeans.size()+1;
	}

	@Override
	public Object getItem(int position) {
		if(position >= 1){
			return mCityBeans.get(position-1);
		}
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		convertView = mLayoutInflater.inflate(R.layout.item_hot_city, null);
		Button btnHotCity = (Button) convertView.findViewById(R.id.btn_hot_city);
		Button btnLocationCity = (Button) convertView.findViewById(R.id.btn_location_city);
		if(0 == position){
			btnHotCity.setVisibility(View.GONE);
			btnLocationCity.setVisibility(View.VISIBLE);
		}else{
			CityBean cityBean = mCityBeans.get(position-1);
			btnHotCity.setVisibility(View.VISIBLE);
			btnLocationCity.setVisibility(View.GONE);
			if(DataManager.getInstance().getCurrentCityId() == Integer.parseInt(cityBean.getAreaId())){
				btnHotCity.setSelected(true);
			}
			btnHotCity.setText(mCityBeans.get(position-1).getNameCn());
		}
		return convertView;
	}
	
}
