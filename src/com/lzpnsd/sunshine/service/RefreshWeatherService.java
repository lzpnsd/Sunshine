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
//		new Thread(){
//			@Override
//			public void run() {
				List<CityBean> savedCity = CityDBManager.getInstance().querySavedCity(RefreshWeatherService.this);
				log.d("saved City = "+savedCity.toString()+",size = "+savedCity.size());
				for(CityBean cityBean : savedCity){
					WeatherUtil.getInstance().getWeather(Integer.parseInt(cityBean.getAreaId()));
				}
//			};
//		}.start();
		
		return Service.START_NOT_STICKY;
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		log.d("onDestroy");
	}
	
}
