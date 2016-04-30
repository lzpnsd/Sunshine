package com.lzpnsd.sunshine.manager;

import java.util.List;

import com.lzpnsd.sunshine.SunshineApplication;
import com.lzpnsd.sunshine.bean.WeatherInfoBean;

public class DataManager {

	private List<WeatherInfoBean> mWeatherInfoBeans;
	
	public static final DataManager sInstance = new DataManager();
	
	public static DataManager getInstance(){
		return sInstance;
	}
	
	private DataManager(){
		
	}
	
	public int getScreenWidth(){
		return SunshineApplication.getInstance().getScreenWidth();
	}
	
	public int getScreenHeight(){
		return SunshineApplication.getInstance().getScreenHeight();
	}
	
	public int getCityId(){
		return 101200901;
	}
	
	public List<WeatherInfoBean> getWeatherInfoBeans(){
		return mWeatherInfoBeans;
	}
	
	public void setWeatherInfoBeans(List<WeatherInfoBean> weatherInfoBeans){
		this.mWeatherInfoBeans = weatherInfoBeans;
	}
	
}
