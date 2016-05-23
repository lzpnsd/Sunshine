package com.lzpnsd.sunshine.service;

import java.util.List;

import com.lzpnsd.sunshine.bean.CityBean;
import com.lzpnsd.sunshine.db.CityDBManager;
import com.lzpnsd.sunshine.util.LogUtil;
import com.lzpnsd.sunshine.util.WeatherUtil;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class RefreshWeatherService extends Service {

	private final LogUtil log = LogUtil.getLog(getClass());
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		log.d("onStartCommand");
		new Thread(){
			@Override
			public void run() {
				List<CityBean> savedCity = CityDBManager.getInstance().querySavedCity();
				for(CityBean cityBean : savedCity){
					WeatherUtil.getInstance().getWeather(Integer.parseInt(cityBean.getAreaId()));
				}
			};
		}.start();
//		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(RefreshWeatherService.this);
//		final String rateValue = sharedPreferences.getString(SettingsActivity.NAME_REFRESH_WEATHER, getString(R.string.text_settings_rate_default));
//		final int time = Integer.parseInt(rateValue)*60*60*1000;
//		
//		final Handler handler = new Handler();
//		handler.postDelayed(new Runnable() {
//			
//			@Override
//			public void run() {
//				
//				handler.postDelayed(this, time);
//			}
//		}, time);
		
		return Service.START_STICKY;
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		log.d("onDestroy");
	}
	
}
