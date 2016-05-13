package com.lzpnsd.sunshine.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.lzpnsd.sunshine.R;
import com.lzpnsd.sunshine.bean.CityWeatherBean;
import com.lzpnsd.sunshine.manager.DataManager;

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
	 * 
	 * @return true：白天<br />
	 *         false：晚上
	 */
	public static boolean isDayOrNight() {
		List<CityWeatherBean> cityWeatherBeans = DataManager.getInstance().getCurrentCityWeatherBeans();
		if (cityWeatherBeans != null && cityWeatherBeans.size() > 0) {
			String sunRise = cityWeatherBeans.get(0).getSunRise();
			String sunSet = cityWeatherBeans.get(0).getSunSet();
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
			String nowTime = simpleDateFormat.format(new Date());
			// String nowTime = DateFormat.format("hh:mm",
			// System.currentTimeMillis()).toString();
//			log.d("current time is " + nowTime);
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
//		log.d("sunRiseHour" + sunRiseHour + "sunSetHour" + sunSetHour + "nowTimeHour" + nowTimeHour);
		if (nowTimeHour < sunRiseHour || nowTimeHour > sunSetHour) {
			return false;
		} else if (nowTimeHour == sunRiseHour) {
			int sunRiseMinute = Integer.parseInt(sunRises[1]);
			int nowTimeMinute = Integer.parseInt(nowTimes[1]);
			if (sunRiseMinute > nowTimeMinute) {
				return false;
			}
		} else if (nowTimeHour == sunSetHour) {
			int sunSetMinute = Integer.parseInt(sunSets[1]);
			int nowTimeMinute = Integer.parseInt(nowTimes[1]);
			if (sunSetMinute <= nowTimeMinute) {
				return false;
			}
		}
		return true;
	}

	public static int getDaySmallImageResource(String type){
//		log.d("type = "+type);
		String[] rains = new String[] {};
		String[] snows = new String[] {};
		if (type.contains("雨")) {
				return matchSmallType(0, type, rains);
		} else if (type.contains("雪")) {
				return matchSmallType(1, type, snows);
		}else if("晴".equals(type)){
			return R.drawable.ww0;
		}else if("多云".equals(type)){
			return R.drawable.ww1;
		}else if(type.contains("晴") && type.equals("多云")){
			return R.drawable.ww1;
		}else if("阴".equals(type)){
			return R.drawable.ww2;
		}else if("阵雨".equals(type)){
			return R.drawable.ww3;
		}else if("雷阵雨".equals(type) || type.contains("雷电")){
			return R.drawable.ww4;
		}else if("雷阵雪".equals(type)){
			return R.drawable.ww5;
		}else if("雾".equals(type)){
			return R.drawable.ww18;
		}else if("冰雹".equals(type) || type.equals("雹")){
			return R.drawable.ww20;
		}else if("霾".equals(type) || type.contains("霾")){
			return R.drawable.ww45;
		}else if("阵冰雹".equals(type)){
			return R.drawable.ww29;
		}else if("".equals(type)){
			return R.drawable.ww0;
		}else if("浮尘".equals(type)){
			return R.drawable.ww36;
		}
		return 0;
	}
	
	public static int getNightSmallImageResource(String type){
//		log.d("type = "+type);
		String[] rains = new String[] {};
		String[] snows = new String[] {};
		if ("晴".equals(type)) {
			return R.drawable.ww30;
		} else if ("多云".equals(type)) {
			return R.drawable.ww31;
		} else if (type.contains("晴") && type.equals("多云")) {
			return R.drawable.ww31;
		} else if ("阴".equals(type)) {
			return R.drawable.ww2;
		} else if ("阵雨".equals(type)) {
			return R.drawable.ww3;
		} else if ("雷阵雨".equals(type) || type.contains("雷电")) {
			return R.drawable.ww4;
		} else if ("雷阵雪".equals(type)) {
			return R.drawable.ww5;
		} else if ("雾".equals(type)) {
			return R.drawable.ww32;
		} else if ("冰雹".equals(type) || type.equals("雹")) {
			return R.drawable.ww36;
		} else if ("霾".equals(type) || type.contains("霾")) {
			return R.drawable.ww45;
		} else if ("阵冰雹".equals(type)) {
			return R.drawable.ww35;
		} else if ("".equals(type)) {
			return R.drawable.ww0;
		} else if (type.contains("雨")) {
			return R.drawable.ww33;
		} else if (type.contains("雪")) {
			return R.drawable.ww34;
		}else if("浮尘".equals(type)){
			return R.drawable.ww36;
		}
		return 0;
	}
	
	public static int getDayBigImageResource(String type){
//		log.d("type = "+type);
		String[] rains = new String[] {};
		String[] snows = new String[] {};
		if (type.contains("雨")) {
				return matchBigType(0, type, rains);
		} else if (type.contains("雪")) {
				return matchBigType(1, type, snows);
		}else if("晴".equals(type)){
			return R.drawable.org3_ww0;
		}else if("多云".equals(type)){
			return R.drawable.org3_ww1;
		}else if(type.contains("晴") && type.equals("多云")){
			return R.drawable.org3_ww1;
		}else if("阴".equals(type)){
			return R.drawable.org3_ww2;
		}else if("阵雨".equals(type)){
			return R.drawable.org3_ww3;
		}else if("雷阵雨".equals(type) || type.contains("雷电")){
			return R.drawable.org3_ww4;
		}else if("雷阵雪".equals(type)){
			return R.drawable.org3_ww5;
		}else if("雾".equals(type)){
			return R.drawable.org3_ww18;
		}else if("冰雹".equals(type) || type.equals("雹")){
			return R.drawable.org3_ww20;
		}else if("霾".equals(type) || type.contains("霾")){
			return R.drawable.org3_ww45;
		}else if("阵冰雹".equals(type)){
			return R.drawable.org3_ww29;
		}else if("".equals(type)){
			return R.drawable.org3_ww0;
		}else if("浮尘".equals(type)){
			return R.drawable.ww36;
		}
		return 0;
	}
	
	public static int getNightBigImageResource(String type){
//		log.d("type = "+type);
		String[] rains = new String[] {};
		String[] snows = new String[] {};
		if ("晴".equals(type)) {
			return R.drawable.org3_ww30;
		} else if ("多云".equals(type)) {
			return R.drawable.org3_ww31;
		} else if (type.contains("晴") && type.equals("多云")) {
			return R.drawable.org3_ww31;
		} else if ("阴".equals(type)) {
			return R.drawable.org3_ww2;
		} else if ("阵雨".equals(type)) {
			return R.drawable.org3_ww3;
		} else if ("雷阵雨".equals(type) || type.contains("雷电")) {
			return R.drawable.org3_ww4;
		} else if ("雷阵雪".equals(type)) {
			return R.drawable.org3_ww5;
		} else if ("雾".equals(type)) {
			return R.drawable.org3_ww32;
		} else if ("冰雹".equals(type) || type.equals("雹")) {
			return R.drawable.org3_ww36;
		} else if ("霾".equals(type) || type.contains("霾")) {
			return R.drawable.org3_ww45;
		} else if ("阵冰雹".equals(type)) {
			return R.drawable.org3_ww35;
		} else if ("".equals(type)) {
			return R.drawable.org3_ww0;
		} else if (type.contains("雨")) {
			return R.drawable.org3_ww33;
		} else if (type.contains("雪")) {
			return R.drawable.org3_ww34;
		}else if("浮尘".equals(type)){
			return R.drawable.ww36;
		}
		return 0;
	}
	
	/**
	 * 
	 * @param t 用来表示是雨还是雪，0表示雨，1表示雪
	 * @param type
	 * @param types
	 * @return
	 */
	private static int matchSmallType(int t,String type,String[] types){
		int length = types.length;
		int matchCount = 0;
		if(0 == t){
			if("阵雨".equals(type)){
				return R.drawable.ww3;
			}else if("雨夹雪".equals(type)){
				return R.drawable.ww6;
			}else if("小雨".equals(type)){
				return R.drawable.ww7;
			}else if("中雨".equals(type)){
				return R.drawable.ww8;
			}else if("大雨".equals(type)){
				return R.drawable.ww9;
			}else if("暴雨".equals(type)){
				return R.drawable.ww10;
			}else if("持续降雨".equals(type)){
				return R.drawable.ww19;
			}else if("雷阵雨".equals(type)){
				return R.drawable.ww4;
			}else{
				return R.drawable.ww19;
//				int l = type.length();
//				for(int i = 0;i < length;i++){
//					int len = types[i].length();
//					for(int k = 0;k < len;k++){
//						for(int j = 0;j < l;j++){
//							
//						}
//					}
//				}
			}
		}else if(1 == t){
			if("阵雪".equals(type)){
				return R.drawable.ww13;
			}else if("小雪".equals(type)){
				return R.drawable.ww14;
			}else if("中雪".equals(type)){
				return R.drawable.ww15;
			}else if("大雪".equals(type)){
				return R.drawable.ww16;
			}else if("暴雪".equals(type)){
				return R.drawable.ww17;
			}else if("雷阵雪".equals(type)){
				return R.drawable.ww5;
			}else{
				return R.drawable.ww15;
			}
		}
		return 0;
	}
	
	/**
	 * 
	 * @param t 用来表示是雨还是雪，0表示雨，1表示雪
	 * @param type
	 * @param types
	 * @return
	 */
	private static int matchBigType(int t,String type,String[] types){
		int length = types.length;
		int matchCount = 0;
		if(0 == t){
			if("阵雨".equals(type)){
				return R.drawable.org3_ww3;
			}else if("雨夹雪".equals(type)){
				return R.drawable.org3_ww6;
			}else if("小雨".equals(type)){
				return R.drawable.org3_ww7;
			}else if("中雨".equals(type)){
				return R.drawable.org3_ww8;
			}else if("大雨".equals(type)){
				return R.drawable.org3_ww9;
			}else if("暴雨".equals(type)){
				return R.drawable.org3_ww10;
			}else if("持续降雨".equals(type)){
				return R.drawable.org3_ww19;
			}else if("雷阵雨".equals(type)){
				return R.drawable.org3_ww4;
			}else{
				return R.drawable.org3_ww19;
//				int l = type.length();
//				for(int i = 0;i < length;i++){
//					int len = types[i].length();
//					for(int k = 0;k < len;k++){
//						for(int j = 0;j < l;j++){
//							
//						}
//					}
//				}
			}
		}else if(1 == t){
			if("阵雪".equals(type)){
				return R.drawable.org3_ww13;
			}else if("小雪".equals(type)){
				return R.drawable.org3_ww14;
			}else if("中雪".equals(type)){
				return R.drawable.org3_ww15;
			}else if("大雪".equals(type)){
				return R.drawable.org3_ww16;
			}else if("暴雪".equals(type)){
				return R.drawable.org3_ww17;
			}else if("雷阵雪".equals(type)){
				return R.drawable.org3_ww5;
			}else{
				return R.drawable.org3_ww15;
			}
		}
		return 0;
	}
	
}
