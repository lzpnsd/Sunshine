package com.lzpnsd.sunshine.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.lzpnsd.sunshine.bean.AlarmBean;
import com.lzpnsd.sunshine.bean.CityWeatherBean;
import com.lzpnsd.sunshine.bean.EnvironmentBean;
import com.lzpnsd.sunshine.bean.LifeIndexBean;
import com.lzpnsd.sunshine.bean.WeatherInfoBean;
import com.lzpnsd.sunshine.contants.Contants;
import com.lzpnsd.sunshine.manager.DataManager;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;

public class WeatherUtil {

	private static LogUtil log = LogUtil.getLog(WeatherUtil.class);

	private static MyHandler mHandler;

	private List<AlarmBean> mAlarmBeans = new ArrayList<AlarmBean>();
	private List<CityWeatherBean> mCityWeatherBeans = new ArrayList<CityWeatherBean>();
	private List<EnvironmentBean> mEnvironmentBeans = new ArrayList<EnvironmentBean>();
	private List<LifeIndexBean> mLifeIndexBeans = new ArrayList<LifeIndexBean>();
	private List<WeatherInfoBean> mWeatherInfoBeans = new ArrayList<WeatherInfoBean>();

	private AlarmBean mAlarmBean;
	private CityWeatherBean mCityWeatherBean;
	private EnvironmentBean mEnvironmentBean;
	private LifeIndexBean mLifeIndexBean;
	private WeatherInfoBean mWeatherInfoBean;

	private ExecutorService executor;
	
	private static final int TIME_CONNECT_TIMEOUT = 5000;
	private static final int WHAT_GETASYNC_SUCCESS = 10000;
	private static final int WHAT_GETASYNC_FAILED = 10001;

	private static final WeatherUtil sInstance = new WeatherUtil(); 
	
	public static WeatherUtil getInstance(){
		return sInstance;
	}
	
	private WeatherUtil() {
		if (mHandler == null) {
			mHandler = new MyHandler();
		}
		executor = Executors.newSingleThreadExecutor();
	}

	private static class MyHandler extends Handler {

		public MyHandler() {
			super(Looper.getMainLooper());
		}

		@Override
		public void handleMessage(Message msg) {
			MsgObj obj = (MsgObj) msg.obj;
			switch (msg.what) {
				case WHAT_GETASYNC_SUCCESS:
					log.d("get weather success");
					obj.callBack.onSuccess(obj.weatherInfoBeans);
					break;
				case WHAT_GETASYNC_FAILED:
					log.d("get weather failuew");
					if(obj.weatherInfoBeans == null){
						obj.callBack.onFailure("请求服务器失败");
					}else{
						String error = obj.weatherInfoBeans.get(0).getError();
						log.d("error = "+error);
						if("no data 4  such city".equals(error)){
							error = "暂时没有该城市天气信息";
						}
						obj.callBack.onFailure(error);
					}
					break;
			}
		}
	}

	private static class MsgObj implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = 5935338679654718190L;

		private CallBack callBack;
		private List<WeatherInfoBean> weatherInfoBeans;

		public MsgObj(CallBack callBack, List<WeatherInfoBean> weatherInfoBeans) {
			super();
			this.callBack = callBack;
			this.weatherInfoBeans = weatherInfoBeans;
		}
	}

	public void getWeather(int cityId){
		getWeather(cityId, null);
	}
	
	public void getWeather(final int cityId, final WeatherUtil.CallBack callBack) {
		executor.execute(new Runnable() {

			@Override
			public void run() {
				HttpURLConnection conn = null;
				try {
					log.d("cityId = "+cityId);
					URL url = new URL("http://wthrcdn.etouch.cn/WeatherApi?citykey="+cityId);
					conn = (HttpURLConnection) url.openConnection();
					conn.setInstanceFollowRedirects(false);
					conn.setConnectTimeout(TIME_CONNECT_TIMEOUT);
					conn.setRequestMethod("GET");
					conn.connect();
					if (HttpURLConnection.HTTP_OK == conn.getResponseCode()) {
						log.d("connect success");
						List<WeatherInfoBean> weatherInfoBeans = new ArrayList<WeatherInfoBean>();
						InputStream inputStream = conn.getInputStream();
						weatherInfoBeans.addAll(parseWeather(inputStream));
						saveWeatherInfo(cityId);
						if(DataManager.getInstance().getCurrentCityId() == cityId){//如果当前城市id和查询的城市id一样，保存这次查询的信息
							saveCurrentWeatherInfo();
						}
						if(callBack != null){
							Message msg = Message.obtain();
							msg.obj = new MsgObj(callBack,weatherInfoBeans);
							String error = weatherInfoBeans.get(0).getError();
							if(!TextUtils.isEmpty(error)){
								msg.what = WHAT_GETASYNC_FAILED;
							}else{
								msg.what = WHAT_GETASYNC_SUCCESS;
							}
							mHandler.sendMessage(msg);
						}
					}else{
						log.d("connect false");
						if(callBack!=null){
							Message msg = Message.obtain();
							msg.what = WHAT_GETASYNC_FAILED;
							msg.obj = new MsgObj(callBack, null);
							mHandler.sendMessage(msg);
						}
					}
				} catch (IOException e) {
					log.d(e.getMessage());
				}finally{
					if(conn!=null){
						conn.disconnect();
					}
				}
			}
		});
	}
	
	private void saveCurrentWeatherInfo(){
		DataManager.getInstance().setCurrentAlarmBeans(mAlarmBeans);
		DataManager.getInstance().setCurrentCityWeatherBeans(mCityWeatherBeans);
		DataManager.getInstance().setCurrentEnvironmentBeans(mEnvironmentBeans);
		DataManager.getInstance().setCurrentLifeIndexBeans(mLifeIndexBeans);
		DataManager.getInstance().setCurrentWeatherInfoBeans(mWeatherInfoBeans);
	}
	
	public void deleteWeatherInfo(List<Integer> deleteCityIds){
		for(Integer cityId : deleteCityIds){
			File file = new File(Contants.PATH_CACHE_WEATHER_FILE+File.separator+cityId+".xml");
			file.deleteOnExit();
		}
	}
	
	private void saveWeatherInfo(int cityId) {
		File file = new File(Contants.PATH_CACHE_WEATHER_FILE+File.separator+cityId+".xml");
		log.d("file path = "+file.getAbsolutePath());
		if(!file.getParentFile().exists())
			file.getParentFile().mkdirs();
		if(!file.exists()){
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			FileWriter fileWriter = new FileWriter(file,false);
			Gson gson = new Gson();
			fileWriter.write(gson.toJson(mAlarmBeans) +"\n"
					+gson.toJson(mCityWeatherBeans) +"\n"
					+gson.toJson(mEnvironmentBeans) + "\n"
					+gson.toJson(mLifeIndexBeans) + "\n"
					+gson.toJson(mWeatherInfoBeans) + "\n");
			fileWriter.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 如果该城市没有保存最后的天气情况，则返回null
	 * @param cityId
	 * @param T
	 * @return
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public <T> List<T> getWeatherInfo(int cityId, Class T) throws IOException, FileNotFoundException {
		File file = new File(Contants.PATH_CACHE_WEATHER_FILE + File.separator + cityId + ".xml");
		if(!file.exists()){
			return null;
		}
		FileReader fileReader = new FileReader(file);
		BufferedReader bufferedReader = new BufferedReader(fileReader);
		String name = T.getName();
		List<T> list = null;
		if (AlarmBean.class.getName().equals(name)) {
			list = (List<T>) new ArrayList<AlarmBean>();
		} else if (CityWeatherBean.class.getName().equals(name)) {
			bufferedReader.readLine();
			list = (List<T>) new ArrayList<CityWeatherBean>();
		} else if (EnvironmentBean.class.getName().equals(name)) {
			bufferedReader.readLine();
			bufferedReader.readLine();
			list = (List<T>) new ArrayList<EnvironmentBean>();
		} else if (LifeIndexBean.class.getName().equals(name)) {
			bufferedReader.readLine();
			bufferedReader.readLine();
			bufferedReader.readLine();
			list = (List<T>) new ArrayList<LifeIndexBean>();
		} else if (WeatherInfoBean.class.getName().equals(name)) {
			bufferedReader.readLine();
			bufferedReader.readLine();
			bufferedReader.readLine();
			bufferedReader.readLine();
			list = (List<T>) new ArrayList<WeatherInfoBean>();
		}
		String string = bufferedReader.readLine();
		Gson gson = new Gson();
		JsonArray array = new JsonParser().parse(string).getAsJsonArray();
//		JsonArray fromJson = gson.fromJson(string,new TypeToken<List<T>>(){}.getType());
		for(final JsonElement elem : array){
			list.add((T) new Gson().fromJson(elem, T));
		}
		return list;
	}
	
	public void getCurrentCityWeatherInfo() throws FileNotFoundException,IOException{
		File file = new File(Contants.PATH_CACHE_WEATHER_FILE + File.separator + DataManager.getInstance().getCurrentCityId() + ".xml");
		if(!file.exists()){
			throw new FileNotFoundException();
		}
		FileReader fileReader = new FileReader(file);
		BufferedReader bufferedReader = new BufferedReader(fileReader);
		Gson gson = new Gson();
		String alarmLine = bufferedReader.readLine();
		List<AlarmBean> alarmList = gson.fromJson(alarmLine,new TypeToken<List<AlarmBean>>() {}.getType());
		DataManager.getInstance().setCurrentAlarmBeans(alarmList);
		String cityWeatherLine = bufferedReader.readLine();
		List<CityWeatherBean> cityWeatherList = gson.fromJson(cityWeatherLine,new TypeToken<List<CityWeatherBean>>() {}.getType());
		DataManager.getInstance().setCurrentCityWeatherBeans(cityWeatherList);
		String environmentLine = bufferedReader.readLine();
		List<EnvironmentBean> environmentList = gson.fromJson(environmentLine,new TypeToken<List<EnvironmentBean>>() {}.getType());
		DataManager.getInstance().setCurrentEnvironmentBeans(environmentList);
		String lifeIndexLine = bufferedReader.readLine();
		List<LifeIndexBean> lifeIndexList = gson.fromJson(lifeIndexLine,new TypeToken<List<LifeIndexBean>>() {}.getType());
		DataManager.getInstance().setCurrentLifeIndexBeans(lifeIndexList);
		String weatherInfoLine = bufferedReader.readLine();
		List<WeatherInfoBean> weatherInfoList = gson.fromJson(weatherInfoLine,new TypeToken<List<WeatherInfoBean>>() {}.getType());
		DataManager.getInstance().setCurrentWeatherInfoBeans(weatherInfoList);
	}
	
	private List<WeatherInfoBean> parseWeather(InputStream inputStream) {

		try {
			XmlPullParserFactory instance = XmlPullParserFactory.newInstance();
			XmlPullParser parser = instance.newPullParser();
			parser.setInput(inputStream, "UTF-8");
			int eventType = parser.getEventType();
			mAlarmBeans.clear();;
			mCityWeatherBeans.clear();
			mEnvironmentBeans.clear();
			mLifeIndexBeans.clear();
			mWeatherInfoBeans.clear();
			mAlarmBean = null;
			mCityWeatherBean = null;
			mEnvironmentBean = null;
			mLifeIndexBean = null;
			mWeatherInfoBean = null;
			while (XmlPullParser.END_DOCUMENT != eventType) {
				String nodeName = parser.getName();
				switch (eventType) {
					case XmlPullParser.START_DOCUMENT:
						break;
					case XmlPullParser.START_TAG:
						if ("resp".equals(nodeName))
							break;
						if ("environment".equals(nodeName))
							break;
						if ("alarm".equals(nodeName))
							break;
						if ("yesterday".equals(nodeName))
							break;
						if ("forecast".equals(nodeName))
							break;
						if ("weather".equals(nodeName))
							break;
						if ("city".equals(nodeName)) {
							String city = parser.nextText();
							mCityWeatherBean = new CityWeatherBean();
							mCityWeatherBean.setCity(city);
						} else if ("updatetime".equals(nodeName)) {
							String updateTime = parser.nextText();
							mCityWeatherBean.setUpdateTime(updateTime);
						} else if ("wendu".equals(nodeName)) {
							String temperature = parser.nextText();
							mCityWeatherBean.setTemperature(temperature);
						} else if ("fengli".equals(nodeName)) {
							String windPower = parser.nextText();
							mCityWeatherBean.setWindPower(windPower);
						} else if ("shidu".equals(nodeName)) {
							String dampness = parser.nextText();
							mCityWeatherBean.setDampness(dampness);
						} else if ("fengxiang".equals(nodeName)) {
							String windDirection = parser.nextText();
							mCityWeatherBean.setWindDirection(windDirection);
						} else if ("sunrise_1".equals(nodeName)) {
							String sunRise = parser.nextText();
							mCityWeatherBean.setSunRise(sunRise);
						} else if ("sunset_1".equals(nodeName)) {
							String sunSet = parser.nextText();
							mCityWeatherBean.setSunSet(sunSet);
						} else if ("aqi".equals(nodeName)) {
							String aqi = parser.nextText();
							mEnvironmentBean = new EnvironmentBean();
							mEnvironmentBean.setAqi(Integer.parseInt(aqi));
						} else if ("pm25".equals(nodeName)) {
							String pm25 = parser.nextText();
							mEnvironmentBean.setPm25(Integer.parseInt(pm25));
						} else if ("suggest".equals(nodeName)) {//environment的suggest
							if(mEnvironmentBean!=null){
								String suggest = parser.nextText();
								mEnvironmentBean.setSuggest(suggest);
							}
						} else if ("quality".equals(nodeName)) {
							String quality = parser.nextText();
							mEnvironmentBean.setQuality(quality);
						} else if ("MajorPollutants".equals(nodeName)) {
							String majorPollutants = parser.nextText();
							mEnvironmentBean.setMajorPollutants(majorPollutants);
						} else if ("o3".equals(nodeName)) {
							String o3 = parser.nextText();
							mEnvironmentBean.setO3(Integer.parseInt(o3));
						} else if ("co".equals(nodeName)) {
							String co = parser.nextText();
							mEnvironmentBean.setCo(Integer.parseInt(co));
						} else if ("pm10".equals(nodeName)) {
							String pm10 = parser.nextText();
							mEnvironmentBean.setPm10(Integer.parseInt(pm10));
						} else if ("so2".equals(nodeName)) {
							String so2 = parser.nextText();
							mEnvironmentBean.setSo2(Integer.parseInt(so2));
						} else if ("no2".equals(nodeName)) {
							String no2 = parser.nextText();
							mEnvironmentBean.setNo2(Integer.parseInt(no2));
						} else if ("time".equals(nodeName)) {
							String time = parser.nextText();
							mEnvironmentBean.setTime(time);
						} else if ("cityKey".equals(nodeName)) {
							String cityKey = parser.nextText();
							mAlarmBean = new AlarmBean();
							mAlarmBean.setCityKey(Integer.parseInt(cityKey));
						} else if ("cityName".equals(nodeName)) {
							String cityName = parser.nextText();
							mAlarmBean.setCityName(cityName);
						} else if ("alarmType".equals(nodeName)) {
							String alarmType = parser.nextText();
							mAlarmBean.setAlarmType(alarmType);
						} else if ("alarmDegree".equals(nodeName)) {
							String alarmDegree = parser.nextText();
							mAlarmBean.setAlarmDegree(alarmDegree);
						} else if ("alarmText".equals(nodeName)) {
							String alarmText = parser.nextText();
							mAlarmBean.setAlarmText(alarmText);
						} else if ("alarm_details".equals(nodeName)) {
							String alarmDetails = parser.nextText();
							mAlarmBean.setAlarmDetails(alarmDetails);
						} else if ("standard".equals(nodeName)) {
							String standard = parser.nextText();
							mAlarmBean.setStandard(standard);
						} else if ("suggest".equals(nodeName)) {//alarm的suggest
							if(mAlarmBean!=null){
								String suggest = parser.nextText();
								mAlarmBean.setSuggest(suggest);
							}
						} else if ("imgUrl".equals(nodeName)) {
							String imgUrl = parser.nextText();
							mAlarmBean.setImgUrl(imgUrl);
							parser.next();
							String time = parser.nextText();
							mAlarmBean.setTime(time);
						} else if ("date_1".equals(nodeName)) {
							String date = parser.nextText();
							if(date.contains("星期天")){
								date = date.replace("星期天", "星期日");
							}
							if(date.contains("星期")){
								date = date.replace("星期", "周");
							}
							log.d("  ----date = "+date);
							mWeatherInfoBean = new WeatherInfoBean();
							mWeatherInfoBean.setDate(date);
						} else if ("high_1".equals(nodeName)) {
							String highTemperature = parser.nextText();
							highTemperature = highTemperature.replace("高温", "").trim().replace("℃", "").trim();
							mWeatherInfoBean.setHighTemperature(highTemperature);
						} else if ("low_1".equals(nodeName)) {
							String lowTemperature = parser.nextText();
							lowTemperature = lowTemperature.replace("低温", "").trim().replace("℃", "").trim();
							mWeatherInfoBean.setLowTemperature(lowTemperature);
						} else if ("day_1".equals(nodeName)) {
							int next = parser.next();
							if (next == XmlPullParser.START_TAG) {
								String dayType = parser.nextText();
								mWeatherInfoBean.setDayType(dayType);
								parser.next();
								String dayWindDirection = parser.nextText();
								mWeatherInfoBean.setDayWindDirection("无持续风向".equals(dayWindDirection)?"微风":dayWindDirection);
								parser.next();
								String dayWindPower = parser.nextText();
								mWeatherInfoBean.setDayWindPower(dayWindPower);
								parser.next();
								parser.next();
								parser.next();
								String nightType = parser.nextText();
								mWeatherInfoBean.setNightType(nightType);
								parser.next();
								String nightWindDirection = parser.nextText();
								mWeatherInfoBean.setNightWindDirection("无持续风向".equals(nightWindDirection)?"微风":nightWindDirection);
								parser.next();
								String nightWindPower = parser.nextText();
								mWeatherInfoBean.setNightWindPower(nightWindPower);
							}
						} else if ("date".equals(nodeName)) {
							String date = parser.nextText();
							if(date.contains("星期天")){
								date = date.replace("星期天", "星期日");
							}
							if(date.contains("星期")){
								date = date.replace("星期", "周");
							}
							mWeatherInfoBean = new WeatherInfoBean();
							mWeatherInfoBean.setDate(date);
						} else if ("high".equals(nodeName)) {
							String highTemperature = parser.nextText();
							highTemperature = highTemperature.replace("高温", "").trim().replace("℃", "").trim();
							mWeatherInfoBean.setHighTemperature(highTemperature);
						} else if ("low".equals(nodeName)) {
							String lowTemperature = parser.nextText();
							lowTemperature = lowTemperature.replace("低温", "").trim().replace("℃", "").trim();
							mWeatherInfoBean.setLowTemperature(lowTemperature);
						} else if ("day".equals(nodeName)) {
							int next = parser.next();
							if (next == XmlPullParser.START_TAG) {
								String dayType = parser.nextText();
								mWeatherInfoBean.setDayType(dayType);
								parser.next();
								String dayWindDirection = parser.nextText();
								mWeatherInfoBean.setDayWindDirection("无持续风向".equals(dayWindDirection)?"微风":dayWindDirection);
								parser.next();
								String dayWindPower = parser.nextText();
								mWeatherInfoBean.setDayWindPower(dayWindPower);
								parser.next();
								parser.next();
								parser.next();
								String nightType = parser.nextText();
								mWeatherInfoBean.setNightType(nightType);
								parser.next();
								String nightWindDirection = parser.nextText();
								mWeatherInfoBean.setNightWindDirection("无持续风向".equals(nightWindDirection)?"微风":nightWindDirection);
								parser.next();
								String nightWindPower = parser.nextText();
								mWeatherInfoBean.setNightWindPower(nightWindPower);
							}
						} else if ("name".equals(nodeName)) {
							String name = parser.nextText();
							mLifeIndexBean = new LifeIndexBean();
							mLifeIndexBean.setName(name);
						} else if ("value".equals(nodeName)) {
							String value = parser.nextText();
							mLifeIndexBean.setValue(value);
						} else if ("detail".equals(nodeName)) {
							String detail = parser.nextText();
							mLifeIndexBean.setDetail(detail);
						}else if("error".equals(nodeName)){
							String error = parser.nextText();
							log.d("start error,error = "+error);
							mWeatherInfoBean = new WeatherInfoBean();
							mWeatherInfoBean.setError(error);
						}
						break;
					case XmlPullParser.END_TAG:
//						log.d("nodeName = "+nodeName);
						if ("resp".equals(nodeName)) {
							if(null != mCityWeatherBean){
								mCityWeatherBeans.add(mCityWeatherBean);
							}
							if(null != mWeatherInfoBean && !TextUtils.isEmpty(mWeatherInfoBean.getError())){
								log.d("end error,mWeatherInfoBean = "+mWeatherInfoBean.toString());
								mWeatherInfoBeans.add(mWeatherInfoBean);
							}
						} else if ("environment".equals(nodeName)) {
							if(null != mEnvironmentBean){
								mEnvironmentBeans.add(mEnvironmentBean);
							}
						} else if ("alarm".equals(nodeName)) {
							if(null != mAlarmBean){
								mAlarmBeans.add(mAlarmBean);
							}
						} else if ("yesterday".equals(nodeName)) {
							if(null != mWeatherInfoBean){
								mWeatherInfoBeans.add(mWeatherInfoBean);
							}
						} else if ("weather".equals(nodeName)) {
							if(null != mWeatherInfoBean){
								mWeatherInfoBeans.add(mWeatherInfoBean);
							}
						} else if ("zhishu".equals(nodeName)) {
							if(null != mLifeIndexBean){
								mLifeIndexBeans.add(mLifeIndexBean);
							}
						}
						break;
					default:
						break;
				}
				eventType = parser.next();
			}
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
//		catch (Exception e) {
//			log.d(e.toString());
//		}
		
		return mWeatherInfoBeans;
	}

	/**
	 * 获得天气信息后的回调接口
	 *
	 * @author lize
	 *
	 * 2016年4月18日
	 */
	public interface CallBack {
		/**
		 * 成功获得天气信息后调用
		 * @param weatherInfoBeans
		 */
		void onSuccess(List<WeatherInfoBean> weatherInfoBeans);
		/**
		 * 获得天气信息失败后调用
		 * @param result
		 */
		void onFailure(String result);
	}

//	public List<AlarmBean> getmAlarmBeans() {
//		return mAlarmBeans;
//	}
//
//	public void setmAlarmBeans(List<AlarmBean> mAlarmBeans) {
//		this.mAlarmBeans = mAlarmBeans;
//	}
//
//	public List<CityWeatherBean> getmCityWeatherBeans() {
//		return mCityWeatherBeans;
//	}
//
//	public void setmCityWeatherBeans(List<CityWeatherBean> mCityWeatherBeans) {
//		this.mCityWeatherBeans = mCityWeatherBeans;
//	}
//
//	public List<EnvironmentBean> getmEnvironmentBeans() {
//		return mEnvironmentBeans;
//	}
//
//	public void setmEnvironmentBeans(List<EnvironmentBean> mEnvironmentBeans) {
//		this.mEnvironmentBeans = mEnvironmentBeans;
//	}
//
//	public List<LifeIndexBean> getmLifeIndexBeans() {
//		return mLifeIndexBeans;
//	}
//
//	public void setmLifeIndexBeans(List<LifeIndexBean> mLifeIndexBeans) {
//		this.mLifeIndexBeans = mLifeIndexBeans;
//	}
//
//	public List<WeatherInfoBean> getmWeatherInfoBeans() {
//		Log.i("WeatherUtil", "getmWeatherInfoBeans");
//		return mWeatherInfoBeans;
//	}
//
//	public void setmWeatherInfoBeans(List<WeatherInfoBean> mWeatherInfoBeans) {
//		this.mWeatherInfoBeans = mWeatherInfoBeans;
//	}

}
