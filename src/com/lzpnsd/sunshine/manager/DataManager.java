package com.lzpnsd.sunshine.manager;

import java.util.List;

import com.lzpnsd.sunshine.SunshineApplication;
import com.lzpnsd.sunshine.bean.AlarmBean;
import com.lzpnsd.sunshine.bean.CityWeatherBean;
import com.lzpnsd.sunshine.bean.EnvironmentBean;
import com.lzpnsd.sunshine.bean.LifeIndexBean;
import com.lzpnsd.sunshine.bean.WeatherInfoBean;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class DataManager {

	private List<WeatherInfoBean> mWeatherInfoBeans;	
	private List<AlarmBean> mAlarmBeans;
	private List<CityWeatherBean> mCityWeatherBeans;
	private List<EnvironmentBean> mEnvironmentBeans;
	private List<LifeIndexBean> mLifeIndexBeans;
	
	private final int MODE_SHAREDPREFERENCES = Context.MODE_PRIVATE;
	private final String NAME_SHAREDPREFERENCES = "sunshine_common"; 
	private final String NAME_ISFIRST = "isFirst";
	private final String NAME_CITYID = "cityId";
	
	private final int DEFAULT_CITYID = 101010100;
	
	private static final DataManager sInstance = new DataManager();
	
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
	
	public boolean setCurrentCityId(int cityId){
		SharedPreferences preferences = SunshineApplication.getContext().getSharedPreferences(NAME_SHAREDPREFERENCES, MODE_SHAREDPREFERENCES);
		Editor editor = preferences.edit();
		editor.putInt(NAME_CITYID, cityId);
		return editor.commit();
	}
	
	public int getCurrentCityId(){
		SharedPreferences preferences = SunshineApplication.getContext().getSharedPreferences(NAME_SHAREDPREFERENCES, MODE_SHAREDPREFERENCES);
		return preferences.getInt(NAME_CITYID, DEFAULT_CITYID);
	}
	
	public List<WeatherInfoBean> getCurrentWeatherInfoBeans(){
		return mWeatherInfoBeans;
	}
	
	public void setCurrentWeatherInfoBeans(List<WeatherInfoBean> weatherInfoBeans){
		this.mWeatherInfoBeans = weatherInfoBeans;
	}
	
	public void setCurrentCityWeatherBeans(List<CityWeatherBean> cityWeatherBeans){
		this.mCityWeatherBeans = cityWeatherBeans;
	}
	
	public List<CityWeatherBean> getCurrentCityWeatherBeans(){
		return this.mCityWeatherBeans;
	}
	
	public void setCurrentEnvironmentBeans(List<EnvironmentBean> environmentBeans){
		this.mEnvironmentBeans = environmentBeans;
	}
	
	/**
	 * 返回的environmentBeans有可能为null或者size为0
	 * @return
	 */
	public List<EnvironmentBean> getCurrentEnvironmentBeans(){
		return this.mEnvironmentBeans;
	}
	
	public void setCurrentLifeIndexBeans(List<LifeIndexBean> lifeIndexBeans){
		this.mLifeIndexBeans = lifeIndexBeans;
	}
	
	public List<LifeIndexBean> getCurrentLifeIndexBeans(){
		return this.mLifeIndexBeans;
	}
	
	public void setCurrentAlarmBeans(List<AlarmBean> alarmBeans){
		this.mAlarmBeans = alarmBeans;
	}
	
	public List<AlarmBean> getCurrentAlarmBeans(){
		return this.mAlarmBeans;
	}
	
	public boolean setIsFirst(boolean isFirst){
		SharedPreferences preferences = SunshineApplication.getContext().getSharedPreferences(NAME_SHAREDPREFERENCES, MODE_SHAREDPREFERENCES);
		Editor editor = preferences.edit();
		editor.putBoolean(NAME_ISFIRST, isFirst);
		return editor.commit();
	}
	
	public boolean isFirst(){
		SharedPreferences preferences = SunshineApplication.getContext().getSharedPreferences(NAME_SHAREDPREFERENCES, MODE_SHAREDPREFERENCES);
		return preferences.getBoolean(NAME_ISFIRST, true);
	}
	
	public int getSaveCitySize(){
		return 1;
	}
	
}
