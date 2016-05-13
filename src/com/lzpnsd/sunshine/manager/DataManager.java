package com.lzpnsd.sunshine.manager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import com.lzpnsd.sunshine.SunshineApplication;
import com.lzpnsd.sunshine.bean.AlarmBean;
import com.lzpnsd.sunshine.bean.CityBean;
import com.lzpnsd.sunshine.bean.CityWeatherBean;
import com.lzpnsd.sunshine.bean.EnvironmentBean;
import com.lzpnsd.sunshine.bean.LifeIndexBean;
import com.lzpnsd.sunshine.bean.WeatherInfoBean;
import com.lzpnsd.sunshine.contants.Contants;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;

public class DataManager {

	private List<WeatherInfoBean> mWeatherInfoBeans;
	private List<AlarmBean> mAlarmBeans;
	private List<CityWeatherBean> mCityWeatherBeans;
	private List<EnvironmentBean> mEnvironmentBeans;
	private List<LifeIndexBean> mLifeIndexBeans;
	
	private final int MODE_SHAREDPREFERENCES = Context.MODE_PRIVATE;
	private final String NAME_COMMON_SHAREDPREFERENCES = "sunshine_common"; 
	private final String NAME_CURRENT_CITY_SP = "current_city";
	private final String NAME_ISFIRST = "isFirst";
	private final String NAME_VERSION_CODE = "version_code";
	private final String NAME_CITYID = "cityId";
	
	private final int DEFAULT_CITYID = 0;
	
	private final String NAME_AREAID = "areaId";
	private final String NAME_NAMEEN = "nameEn";
	private final String NAME_NAMECN = "nameCn";
	private final String NAME_DISTRICTEN = "districtEn";
	private final String NAME_DISTRICTCN = "districtCn";
	private final String NAME_PROVEN = "provEn";
	private final String NAME_PROVCN = "provCn";
	private final String NAME_NATIONEN = "nationEn";
	private final String NAME_NATIONCN = "nationCn";
	
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
	
	public boolean setCurrentCityBean(CityBean cityBean){
		setCurrentCityId(Integer.parseInt(cityBean.getAreaId()));
		SharedPreferences preferences = SunshineApplication.getContext().getSharedPreferences(NAME_CURRENT_CITY_SP, MODE_SHAREDPREFERENCES);
		Editor editor = preferences.edit();
		editor.clear();
		editor.commit();
		editor.putString(NAME_AREAID, cityBean.getAreaId());
		editor.putString(NAME_NAMEEN, cityBean.getNameEn());
		editor.putString(NAME_NAMECN, cityBean.getNameCn());
		editor.putString(NAME_DISTRICTEN, cityBean.getDistrictEn());
		editor.putString(NAME_DISTRICTCN, cityBean.getDistrictCn());
		editor.putString(NAME_PROVEN, cityBean.getProvEn());
		editor.putString(NAME_PROVCN, cityBean.getProvCn());
		editor.putString(NAME_NATIONEN, cityBean.getNationEn());
		editor.putString(NAME_NATIONCN, cityBean.getNationCn());
		return editor.commit();
	}
	
	public CityBean getCurrentCityBean(){
		CityBean cityBean = new CityBean();
		SharedPreferences sharedPreferences = SunshineApplication.getContext().getSharedPreferences(NAME_CURRENT_CITY_SP, MODE_SHAREDPREFERENCES);
		cityBean.setAreaId(sharedPreferences.getString(NAME_AREAID, "101010100"));
		cityBean.setNameEn(sharedPreferences.getString(NAME_NAMEEN, "beijing"));
		cityBean.setNameCn(sharedPreferences.getString(NAME_NAMECN, "北京"));
		cityBean.setDistrictEn(sharedPreferences.getString(NAME_DISTRICTCN, "beijing"));
		cityBean.setDistrictCn(sharedPreferences.getString(NAME_DISTRICTCN, "北京"));
		cityBean.setProvEn(sharedPreferences.getString(NAME_PROVEN, "北京"));
		cityBean.setProvCn(sharedPreferences.getString(NAME_PROVCN, "北京"));
		cityBean.setNationEn(sharedPreferences.getString(NAME_NATIONEN, "zhongguo"));
		cityBean.setNationCn(sharedPreferences.getString(NAME_NATIONCN, "中国"));
		return cityBean;
	}
	
	public boolean setCurrentCityId(int cityId){
		SharedPreferences preferences = SunshineApplication.getContext().getSharedPreferences(NAME_COMMON_SHAREDPREFERENCES, MODE_SHAREDPREFERENCES);
		Editor editor = preferences.edit();
		editor.putInt(NAME_CITYID, cityId);
		return editor.commit();
	}
	
	public int getCurrentCityId(){
		SharedPreferences preferences = SunshineApplication.getContext().getSharedPreferences(NAME_COMMON_SHAREDPREFERENCES, MODE_SHAREDPREFERENCES);
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
		SharedPreferences preferences = SunshineApplication.getContext().getSharedPreferences(NAME_COMMON_SHAREDPREFERENCES, MODE_SHAREDPREFERENCES);
		Editor editor = preferences.edit();
		try {
			editor.putBoolean(NAME_ISFIRST, isFirst);
			PackageInfo packageInfo = SunshineApplication.getContext().getPackageManager().getPackageInfo(SunshineApplication.getContext().getPackageName(), 0);
			editor.putInt(NAME_VERSION_CODE, packageInfo.versionCode);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return editor.commit();
	}
	
	public boolean isFirst(){
		SharedPreferences preferences = SunshineApplication.getContext().getSharedPreferences(NAME_COMMON_SHAREDPREFERENCES, MODE_SHAREDPREFERENCES);
		boolean isFirst = false;
		try {
			PackageInfo packageInfo = SunshineApplication.getContext().getPackageManager().getPackageInfo(SunshineApplication.getContext().getPackageName(), 0);
			int currentVersion = packageInfo.versionCode;
			if (currentVersion > preferences.getInt(NAME_VERSION_CODE, 0)) {
				isFirst = true;
			}
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return isFirst;
	}
	
	public int getSaveCitySize(){
		return 1;
	}
	
	public void saveWeatherIntoFile(int cityId,List<AlarmBean> alarmBeans,List<CityWeatherBean> cityWeatherBeans,
			List<EnvironmentBean> environmentBeans,List<LifeIndexBean> lifeIndexBeans,List<WeatherInfoBean> weatherInfoBeans){
		File file = new File(Contants.PATH_CACHE_WEATHER_FILE+File.separator+cityId+".xml");
		if(!file.getParentFile().exists())
			file.getParentFile().mkdirs();
		if(!file.exists()){
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		FileOutputStream fos;
//		try {
//			fos = new FileOutputStream(file);
//			byte[] buffer = new byte[3*1024];
//			int length;
//			while((length = inputStream.read(buffer)) != -1){
//				log.d("length = "+length);
//				fos.write(buffer, 0, length);
//			}
//			log.d("length = "+length);
//			log.d("save weather info to file success");
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	}
	
}
