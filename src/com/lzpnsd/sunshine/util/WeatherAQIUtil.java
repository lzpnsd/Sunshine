package com.lzpnsd.sunshine.util;

import com.lzpnsd.sunshine.R;

public class WeatherAQIUtil {

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
		} else if (aqi > 300) {
			return R.drawable.notif_level6;
		}
		return R.drawable.notif_level7;
	}

	public static int getAQIBackgroundImage(){
		if(!WeatherIconUtil.isDayOrNight()){
			return R.drawable.selector_aqi_night;
		}
		return R.drawable.selector_aqi_night;
	}
	
}
