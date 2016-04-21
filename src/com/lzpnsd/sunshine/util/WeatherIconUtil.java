package com.lzpnsd.sunshine.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.lzpnsd.sunshine.bean.CityWeatherBean;

import android.text.format.DateFormat;
import android.util.Log;

/**
 * 
 *
 * @author lize
 *
 *         2016年4月21日
 */
public class WeatherIconUtil {

	private static LogUtil log = LogUtil.getLog(WeatherIconUtil.class);
	
	/**
	 * 现在是白天还是晚上
	 * @return true：白天<br />
	 * 			false：晚上
	 */
	public static boolean isDayOrNight(){
		List<CityWeatherBean> cityWeatherBeans = WeatherUtil.getInstance().getmCityWeatherBeans();
		if(cityWeatherBeans!=null && cityWeatherBeans.size()>0){
			String sunRise = cityWeatherBeans.get(0).getSunRise();
			String sunSet = cityWeatherBeans.get(0).getSunSet();
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
			String nowTime = simpleDateFormat.format(new Date());
//			String nowTime = DateFormat.format("hh:mm", System.currentTimeMillis()).toString();
			log.d("current time is "+nowTime);
			return isSunRise(sunRise, sunSet, nowTime);
		}
		return true;
	}

	private static boolean isSunRise(String sunRise, String sunSet, String nowTime) {
		String[] sunRises = sunRise.split(":");
		String[] sunSets = sunSet.split(":");
		String[] nowTimes = nowTime.split(":");
		int sunRiseHour = Integer.parseInt(sunRises[0]);
		int sunSetHour = Integer.parseInt(sunSets[0]);
		int nowTimeHour = Integer.parseInt(nowTimes[0]);
		log.d("sunRiseHour"+sunRiseHour+"sunSetHour"+sunSetHour+"nowTimeHour"+nowTimeHour);
		if(nowTimeHour < sunRiseHour || nowTimeHour > sunSetHour){
			return false;
		}else if(nowTimeHour == sunRiseHour){
			int sunRiseMinute = Integer.parseInt(sunRises[1]);
			int nowTimeMinute = Integer.parseInt(nowTimes[1]);
			if(sunRiseMinute > nowTimeMinute){
				return false;
			}
		}else if(nowTimeHour == sunSetHour){
			int sunSetMinute = Integer.parseInt(sunSets[1]);
			int nowTimeMinute = Integer.parseInt(nowTimes[1]);
			if(sunSetMinute <= nowTimeMinute){
				return false;
			}
		}
		return true;
	}

}
