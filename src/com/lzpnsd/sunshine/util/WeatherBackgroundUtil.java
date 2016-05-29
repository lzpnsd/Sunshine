package com.lzpnsd.sunshine.util;

import java.util.List;

import com.lzpnsd.sunshine.R;
import com.lzpnsd.sunshine.bean.WeatherInfoBean;
import com.lzpnsd.sunshine.manager.DataManager;

public class WeatherBackgroundUtil {

	public static int getAQIImage(int aqi) {
		if (0 <= aqi && aqi <= 50) {
			return R.drawable.notif_level1;
		} else if (51 <= aqi && aqi <= 100) {
			return R.drawable.notif_level2;
		} else if (101 <= aqi && aqi <= 150) {
			return R.drawable.notif_level3;
		} else if (151 <= aqi && aqi <= 200) {
			return R.drawable.notif_level4;
		} else if (201 <= aqi && aqi <= 300) {
			return R.drawable.notif_level5;
		} else if (301 <= aqi && aqi <= 500) {
			return R.drawable.notif_level6;
		} else {
			return R.drawable.notif_level7;
		}
	}

	public static int getAQIBackgroundImage() {
		if (!WeatherIconUtil.isDayOrNight()) {
			return R.drawable.selector_aqi_night;
		}
		return R.drawable.selector_aqi;
	}

	public static int getWeatherMainBackground() {
		List<WeatherInfoBean> weatherInfoBeans = DataManager.getInstance().getCurrentWeatherInfoBeans();
		if(weatherInfoBeans == null || weatherInfoBeans.size() <= 1){
			return R.drawable.bg_na;
		}
		String type = weatherInfoBeans.get(1).getDayType();
		if ("雾霾".equals(type)) {
			if (!WeatherIconUtil.isDayOrNight()) {
				return R.drawable.blur_bg_fog_and_haze;
			}
			return R.drawable.bg_fog_and_haze;
		} else if ("雾".equals(type)) {
			if (!WeatherIconUtil.isDayOrNight()) {
				return R.drawable.bg18_fog_night;
			}
			return R.drawable.bg_fog_day;
		} else if ("大雨".equals(type) || "暴雨".equals(type)) {
			if (!WeatherIconUtil.isDayOrNight()) {
				return R.drawable.blur_bg_heavy_rain_night;
			}
			return R.drawable.bg_heavy_rain_night;
		} else if ("小雨".equals(type) || "中雨".equals(type) || "雨夹雪".equals(type)) {
			if (!WeatherIconUtil.isDayOrNight()) {
				return R.drawable.blur_bg_moderate_rain_day;
			}
			return R.drawable.bg_moderate_rain_day;
		} else if (type.contains("雪")) {
			if (!WeatherIconUtil.isDayOrNight()) {
				return R.drawable.blur_bg_snow_night;
			}
			return R.drawable.bg_snow_night;
		} else if ("晴".equals(type)) {
			if (!WeatherIconUtil.isDayOrNight()) {
				return R.drawable.bg0_fine_night;
			}
			return R.drawable.bg0_fine_day;
		} else if (type.equals("沙")) {
			return R.drawable.blur_bg_fog_and_haze;
		} else if (type.contains("雾") || type.contains("霾")) {
			return R.drawable.blur_bg_fog_day;
		} else if (type.contains("晴")) {
			if (!WeatherIconUtil.isDayOrNight()) {
				return R.drawable.blur_bg0_fine_night;
			}
			return R.drawable.blur_bg0_fine_day;
		}  else if (type.contains("多云") || type.contains("阴")) {
			if (!WeatherIconUtil.isDayOrNight()) {
				return R.drawable.blur_bg0_fine_night;
			}
			return R.drawable.blur_bg0_fine_day;
		} else {
			if (!WeatherIconUtil.isDayOrNight()) {
				return R.drawable.blur_bg_na;
			}
			return R.drawable.bg_na;
		}
	}

}
