package com.lzpnsd.sunshine.view;

import java.util.List;

import com.lzpnsd.sunshine.R;
import com.lzpnsd.sunshine.bean.WeatherInfoBean;
import com.lzpnsd.sunshine.manager.DataManager;
import com.lzpnsd.sunshine.model.OnCustomItemClickListener;
import com.lzpnsd.sunshine.util.LogUtil;
import com.lzpnsd.sunshine.util.WeatherAQIUtil;
import com.lzpnsd.sunshine.util.WeatherUtil;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class WeatherView {

	private LogUtil log = LogUtil.getLog(getClass());

	private Context mContext;

	private RelativeLayout mView;
	private CustomHorizontalView mHorizontalChartView;
	private ScrollView mSvContains;
	
	private LinearLayout mLlAqi;
	private ImageView mIvAqi;
	private TextView mTvAqi;
	private TextView mTvWeatherUpdateTime;
//	private AQIView mAQIView;
	
	public WeatherView(Context mContext) {
		this.mContext = mContext;
	}

	public RelativeLayout initView() {
		LayoutInflater inflater = LayoutInflater.from(mContext);
		mView = (RelativeLayout) inflater.inflate(R.layout.view_weather, null);
		setViews();
		return mView;
	}

	private void setViews() {
		mSvContains = (ScrollView) mView.findViewById(R.id.sv_weather_content);
		mHorizontalChartView = (CustomHorizontalView) mView.findViewById(R.id.chv_mine);
		mLlAqi = (LinearLayout) mView.findViewById(R.id.ll_weather_aqi);
		mIvAqi = (ImageView) mView.findViewById(R.id.iv_weather_aqi);
		mTvAqi = (TextView) mView.findViewById(R.id.tv_weather_aqi);
//		mAQIView = (AQIView) mView.findViewById(R.id.custom_aqi);
		
		mTvWeatherUpdateTime = (TextView) mView.findViewById(R.id.tv_weather_update_time);
		WeatherUtil.getInstance().getWeather(DataManager.getInstance().getCityId(), new WeatherUtil.CallBack() {

			@Override
			public void onSuccess(List<WeatherInfoBean> weatherInfoBeans) {
				initData();
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
				Toast.makeText(mContext, "v = " + v + ",position = " + position, Toast.LENGTH_LONG).show();
			}
		});
	}
	
	/**
	 * 初始化数据
	 */
	private void initData() {
		mLlAqi.setBackgroundResource(WeatherAQIUtil.getAQIBackgroundImage());
		mIvAqi.setImageResource(WeatherAQIUtil.getAQIImage(WeatherUtil.getInstance().getmEnvironmentBeans().get(0).getAqi()));
		mTvAqi.setText(WeatherUtil.getInstance().getmEnvironmentBeans().get(0).getAqi() +"  "+WeatherUtil.getInstance().getmEnvironmentBeans().get(0).getQuality());
//		mAQIView.setData(WeatherAQIUtil.getAQIImage(WeatherUtil.getInstance().getmEnvironmentBeans().get(0).getAqi()), WeatherUtil.getInstance().getmEnvironmentBeans().get(0).getAqi() +"  "+WeatherUtil.getInstance().getmEnvironmentBeans().get(0).getQuality());
		mTvWeatherUpdateTime.setText("更新于"+WeatherUtil.getInstance().getmCityWeatherBeans().get(0).getUpdateTime());
	}
}
