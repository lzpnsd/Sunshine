package com.lzpnsd.sunshine.view;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.lzpnsd.sunshine.R;
import com.lzpnsd.sunshine.SunshineApplication;
import com.lzpnsd.sunshine.activity.CityAddActivity;
import com.lzpnsd.sunshine.activity.CityListActivity;
import com.lzpnsd.sunshine.adapter.LifeIndexAdapter;
import com.lzpnsd.sunshine.bean.CityBean;
import com.lzpnsd.sunshine.bean.CityWeatherBean;
import com.lzpnsd.sunshine.bean.EnvironmentBean;
import com.lzpnsd.sunshine.bean.LifeIndexBean;
import com.lzpnsd.sunshine.bean.WeatherInfoBean;
import com.lzpnsd.sunshine.db.CityDBManager;
import com.lzpnsd.sunshine.manager.DataManager;
import com.lzpnsd.sunshine.model.ILocationListener;
import com.lzpnsd.sunshine.model.OnCustomItemClickListener;
import com.lzpnsd.sunshine.util.AdaptationUtil;
import com.lzpnsd.sunshine.util.LocationUtil;
import com.lzpnsd.sunshine.util.LogUtil;
import com.lzpnsd.sunshine.util.ToastUtil;
import com.lzpnsd.sunshine.util.WeatherBackgroundUtil;
import com.lzpnsd.sunshine.util.WeatherIconUtil;
import com.lzpnsd.sunshine.util.WeatherUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 * code:100
 *
 * @author lize
 *
 * 2016年5月9日
 */
public class WeatherView {

	private LogUtil log = LogUtil.getLog(getClass());

	public static final int CODE_WEATHERVIEW_REQUEST = 1001;
	
	private Context mContext;

	private LifeIndexAdapter mLifeIndexAdapter;
	
	private List<LifeIndexBean> mLifeIndexBeans;
	
	private RelativeLayout mView;
	private CustomHorizontalView mHorizontalChartView;
	private ScrollView mSvContains;
	private RelativeLayout mRlMain;
	
	private LinearLayout mLlAqi;
	private ImageView mIvAqi;
	private TextView mTvAqi;
	private TextView mTvWeatherUpdateTime;
	private ImageButton mIbCityList;
	private ImageButton mIbShare;
	private TextView mTvCityName;
	private ImageView mIvWeather;
	private TextView mTvWeatherType;
	private TextView mTvWeatherTem;
	private TextView mTvDayType;
	private TextView mTvDayTem;
	private TextView mTvWeatherDampness;
	private TextView mTvWeatherWind;
	private TextView mTvTodaySunRise;
	private TextView mTvTodaySunSet;
	private CustomGridView mGvIndex;
	
	public WeatherView(Context mContext) {
		this.mContext = mContext;
	}

	public RelativeLayout initView() {
		LayoutInflater inflater = LayoutInflater.from(mContext);
		mView = (RelativeLayout) inflater.inflate(R.layout.view_weather, null);
		mLifeIndexBeans = new ArrayList<LifeIndexBean>();
		mLifeIndexAdapter = new LifeIndexAdapter(mLifeIndexBeans);
		setViews();
		if(!SunshineApplication.isFirst){
			refreshData();
		}else{
			startLocation();
		}
		return mView;
	}

	private void showLastInfo() {
		try {
			WeatherUtil.getInstance().getCurrentCityWeatherInfo();
		} catch (FileNotFoundException e) {
			return;
		} catch (IOException e) {
			return;
		}
		List<WeatherInfoBean> currentWeatherInfoBeans = DataManager.getInstance().getCurrentWeatherInfoBeans();
		if(null != currentWeatherInfoBeans && currentWeatherInfoBeans.size()>1){
			log.d("currentWeatherInfoBeans = "+currentWeatherInfoBeans.toString());
			initData();
			mHorizontalChartView.addChildView(currentWeatherInfoBeans);
			mHorizontalChartView.notifyDataChanged();
		}
		List<LifeIndexBean> currentLifeIndexBeans = DataManager.getInstance().getCurrentLifeIndexBeans();
		if(null != currentLifeIndexBeans && currentLifeIndexBeans.size()>0){
			mLifeIndexBeans.clear();
			mLifeIndexBeans.addAll(currentLifeIndexBeans);
			mLifeIndexAdapter.notifyDataSetChanged();
		}
	}

	public void startLocation() {
		log.d("start location, isFirst = "+SunshineApplication.isFirst);
		if(SunshineApplication.isFirst){
			ToastUtil.showToast(mContext.getString(R.string.location_start), ToastUtil.LENGTH_LONG);
			LocationUtil.getInstance().startLocation(new ILocationListener() {
				
				@Override
				public void onReceiveLocation(String location) {
					try {
						log.d("onReceive location,location = "+location);
						JSONObject jsonObject = new JSONObject(location);
						String cityName = (String) jsonObject.get(LocationUtil.NAME_CITY_NAME);
						CityBean cityBean = CityDBManager.getInstance().queryCityByName(cityName);
						if(cityBean != null){
							DataManager.getInstance().setCurrentCityBean(cityBean);
							CityDBManager.getInstance().insertIntoSaved(Integer.parseInt(cityBean.getAreaId()), cityBean.getNameCn());
							ToastUtil.showToast(mContext.getString(R.string.location_success), ToastUtil.LENGTH_LONG);
							refreshData();
						}else{
							handleLocationFailed(mContext.getString(R.string.location_failed));
						}
					} catch (JSONException e) {
						handleLocationFailed(mContext.getString(R.string.location_failed));
					} catch (Exception e) {
						e.printStackTrace();
						handleLocationFailed(mContext.getString(R.string.location_failed));
					}
				}
				
				@Override
				public void onFailed(String result) {
					handleLocationFailed(result);
				}
			});
		}
	}
	
	private void handleLocationFailed(String message) {
		ToastUtil.showToast(message, ToastUtil.LENGTH_LONG);
		Intent cityAddIntent = new Intent(mContext,CityAddActivity.class);
		((Activity) mContext).startActivityForResult(cityAddIntent, CODE_WEATHERVIEW_REQUEST);
	}
	
	private void setViews() {
		mRlMain = (RelativeLayout) mView.findViewById(R.id.rl_weather_main);
		mSvContains = (ScrollView) mView.findViewById(R.id.sv_weather_content);
		mSvContains.scrollTo(0, 0);
		mHorizontalChartView = (CustomHorizontalView) mView.findViewById(R.id.chv_mine);
		mLlAqi = (LinearLayout) mView.findViewById(R.id.ll_weather_aqi);
		mIvAqi = (ImageView) mView.findViewById(R.id.iv_weather_aqi);
		mTvAqi = (TextView) mView.findViewById(R.id.tv_weather_aqi);
		mTvWeatherUpdateTime = (TextView) mView.findViewById(R.id.tv_weather_update_time);
		mIbCityList = (ImageButton) mView.findViewById(R.id.ib_weather_title_city);
		mIbShare = (ImageButton) mView.findViewById(R.id.ib_weather_title_share);
		mTvCityName = (TextView) mView.findViewById(R.id.tv_weather_city_name);
		mIvWeather = (ImageView) mView.findViewById(R.id.iv_weather_weather);
		mTvWeatherType = (TextView) mView.findViewById(R.id.tv_weather_daytype);
		mTvWeatherTem = (TextView) mView.findViewById(R.id.tv_weather_temperature);
		mTvDayType = (TextView) mView.findViewById(R.id.tv_weather_today_type);
		mTvDayTem = (TextView) mView.findViewById(R.id.tv_weather_today_temperature);
		mTvWeatherDampness = (TextView) mView.findViewById(R.id.tv_weather_dampness);
		mTvWeatherWind = (TextView) mView.findViewById(R.id.tv_weather_wind);
		mTvTodaySunRise = (TextView) mView.findViewById(R.id.tv_weather_today_sunrise);
		mTvTodaySunSet = (TextView) mView.findViewById(R.id.tv_weather_today_sunset);
		mGvIndex = (CustomGridView) mView.findViewById(R.id.gv_weather_lifeindex);
		mGvIndex.setAdapter(mLifeIndexAdapter);
		mIbCityList.setOnClickListener(mOnClickListener);
		mHorizontalChartView.setOnItemClickListener(new OnCustomItemClickListener() {

			@Override
			public void onClick(View v, int position) {
				
			}
		});
		mGvIndex.setOnItemClickListener(mOnItemClickListener);
	}
	
	public void refreshData(){
		showLastInfo();
		int currentCityId = DataManager.getInstance().getCurrentCityId();
		log.d("currentCityId = " + currentCityId);
		if(0 == currentCityId){
			ToastUtil.showToast(mContext.getString(R.string.no_saved_city), ToastUtil.LENGTH_LONG);
			Intent intent = new Intent(mContext,CityAddActivity.class);
			((Activity) mContext).startActivityForResult(intent, CODE_WEATHERVIEW_REQUEST);
		}else{
			WeatherUtil.getInstance().getWeather(currentCityId, new WeatherUtil.CallBack() {
	
				@Override
				public void onSuccess(List<WeatherInfoBean> weatherInfoBeans) {
					initData();
					mHorizontalChartView.addChildView(weatherInfoBeans);
					mHorizontalChartView.notifyDataChanged();
				}
				
				@Override
				public void onFailure(String result) {
					ToastUtil.showToast(result, ToastUtil.LENGTH_LONG);
				}
			});
		}
	}
	
	/**
	 * 初始化数据
	 */
	private void initData() {
		log.d("initData");
		mSvContains.scrollTo(0, 0);
		int mainBackgroundResource = WeatherBackgroundUtil.getWeatherMainBackground();
		mRlMain.setBackgroundResource(mainBackgroundResource);
		WeatherInfoBean weatherInfoBean = DataManager.getInstance().getCurrentWeatherInfoBeans().get(1);
		ImageLoader.getInstance().displayImage("drawable://"+WeatherIconUtil.getDayBigImageResource(weatherInfoBean.getDayType()), mIvWeather);
		setAqiData();
		List<CityWeatherBean> cityWeatherBeans = DataManager.getInstance().getCurrentCityWeatherBeans();
		if(cityWeatherBeans == null || cityWeatherBeans.size() <=0){
			mTvCityName.setText("");
			mTvWeatherType.setText("");
			mTvWeatherTem.setText("");
			mTvDayType.setText("");
			mTvDayTem.setText("");
			mTvWeatherDampness.setText("");
			mTvWeatherWind.setText("");
			mTvTodaySunRise.setText("");
			mTvTodaySunSet.setText("");
			mTvWeatherUpdateTime.setText("");
		}else{
			CityWeatherBean cityWeatherBean = cityWeatherBeans.get(0);
			mTvCityName.setText(cityWeatherBean.getCity());
			mTvWeatherType.setText(weatherInfoBean.getDayType());
			mTvWeatherTem.setText(cityWeatherBean.getTemperature()+"℃");
			String dayType = weatherInfoBean.getDayType();
			String nightType = weatherInfoBean.getNightType();
			mTvDayType.setText(dayType.equals(nightType)?dayType:dayType+"转"+nightType);
			mTvDayTem.setText(weatherInfoBean.getLowTemperature()+"~"+weatherInfoBean.getHighTemperature()+"℃");
			mTvWeatherDampness.setText("湿度  "+cityWeatherBean.getDampness());
			mTvWeatherWind.setText(cityWeatherBean.getWindDirection() +"  "+cityWeatherBean.getWindPower());
			mTvTodaySunRise.setText("日出  "+cityWeatherBean.getSunRise());
			mTvTodaySunSet.setText("日落  "+cityWeatherBean.getSunSet());
			mTvWeatherUpdateTime.setText("更新于"+cityWeatherBean.getUpdateTime());
		}
		List<LifeIndexBean> currentLifeIndexBeans = DataManager.getInstance().getCurrentLifeIndexBeans();
		if(null != currentLifeIndexBeans && currentLifeIndexBeans.size()>0){
			mLifeIndexBeans.clear();
			mLifeIndexBeans.addAll(currentLifeIndexBeans);
			mLifeIndexAdapter.notifyDataSetChanged();
		}
	}

	/**
	 * 显示AQI数据
	 */
	private void setAqiData() {
		mLlAqi.setBackgroundResource(WeatherBackgroundUtil.getAQIBackgroundImage());
		List<EnvironmentBean> environmentBeans = DataManager.getInstance().getCurrentEnvironmentBeans();
		int aqi = 0;
		StringBuilder sbQuality = new StringBuilder();
		if(null == environmentBeans || environmentBeans.size()<=0){
			aqi =600;
			sbQuality.append("暂无数据");
		}else{
			aqi = environmentBeans.get(0).getAqi();
			sbQuality.append(aqi).append("  ").append(environmentBeans.get(0).getQuality());
		}
		mIvAqi.setImageResource(WeatherBackgroundUtil.getAQIImage(aqi));
		mTvAqi.setText(sbQuality);
	}
	
	private OnClickListener mOnClickListener = new OnClickListener(){

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
				case R.id.ib_weather_title_city:
					Intent intent = new Intent(mContext,CityListActivity.class);
					((Activity)mContext).startActivityForResult(intent,CODE_WEATHERVIEW_REQUEST);
					break;
				case R.id.ib_weather_title_share:
					// TODO 分享
					break;
				default:
					break;
			}
		}
	};
	
	private OnItemClickListener mOnItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			switch (parent.getId()) {
				case R.id.gv_weather_lifeindex:
					showIndexDialog(position);
					break;

				default:
					break;
			}
		}
	};
	
	private void showIndexDialog(int position) {
		final AlertDialog dialog = new AlertDialog.Builder(mContext).create();
		RelativeLayout content = (RelativeLayout) LayoutInflater.from(mContext).inflate(R.layout.dialog_lifeindex, null);
		TextView tvName = (TextView) content.findViewById(R.id.tv_index_name);
		TextView tvValue = (TextView) content.findViewById(R.id.tv_index_value);
		TextView tvDetail = (TextView) content.findViewById(R.id.tv_index_detail);
		LifeIndexBean lifeIndexBean = mLifeIndexBeans.get(position);
		tvName.setText(lifeIndexBean.getName());
		tvValue.setText(lifeIndexBean.getValue());
		tvDetail.setText(lifeIndexBean.getDetail());
		dialog.show();
		Window window = dialog.getWindow();
		window.setContentView(content);
		LayoutParams layoutParams = window.getAttributes();
		layoutParams.width = AdaptationUtil.dip2px(mContext, 250);
		layoutParams.height = AdaptationUtil.dip2px(mContext, 150);
//		window.addContentView(content, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		window.setAttributes(layoutParams);
		window.setGravity(Gravity.CENTER);
		content.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
	}
	
}
