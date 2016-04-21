package com.lzpnsd.sunshine.view;

import java.util.List;

import com.lzpnsd.sunshine.R;
import com.lzpnsd.sunshine.bean.WeatherInfoBean;
import com.lzpnsd.sunshine.manager.DataManager;
import com.lzpnsd.sunshine.model.OnCustomItemClickListener;
import com.lzpnsd.sunshine.util.LogUtil;
import com.lzpnsd.sunshine.util.WeatherIconUtil;
import com.lzpnsd.sunshine.util.WeatherUtil;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

public class WeatherView {
	
	private LogUtil log = LogUtil.getLog(getClass());

	private Context mContext;

	private RelativeLayout mView;
	private CustomHorizontalView mHorizontalChartView;
	private ScrollView mSvContains;
	private Button mBtnTest;
	
	public WeatherView(Context mContext) {
		this.mContext = mContext;
	}
	
	public RelativeLayout initView(){
		LayoutInflater inflater = LayoutInflater.from(mContext);
		mView = (RelativeLayout) inflater.inflate(R.layout.view_weather, null);
		setViews();
		return mView;
	}
	
	private void setViews(){
		mSvContains =  (ScrollView) mView.findViewById(R.id.sv_weather_content);
		mBtnTest = (Button) mView.findViewById(R.id.btn_weather_test);
		mBtnTest.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				log.d("current state is "+(WeatherIconUtil.isDayOrNight()?"day":"night"));
			}
		});
		mHorizontalChartView = (CustomHorizontalView) mView.findViewById(R.id.chv_mine);
		WeatherUtil.getInstance().getWeather(DataManager.getInstance().getCityId(), new WeatherUtil.CallBack() {
			
			@Override
			public void onSuccess(List<WeatherInfoBean> weatherInfoBeans) {
					mHorizontalChartView.addChildView(weatherInfoBeans);
					mHorizontalChartView.notifyDataChanged();
			}
			
			@Override
			public void onFailure(String result) {
				Toast.makeText(mContext, result, Toast.LENGTH_LONG).show();
			}
		});
		mHorizontalChartView.setOnItemClickListener(new OnCustomItemClickListener() {
			
			@Override
			public void onClick(View v, int position) {
				Toast.makeText(mContext, "v = "+v+",position = "+position, Toast.LENGTH_LONG).show();
			}
		});
	}
	
}
