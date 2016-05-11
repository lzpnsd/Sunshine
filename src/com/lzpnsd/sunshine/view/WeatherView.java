package com.lzpnsd.sunshine.view;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.lzpnsd.sunshine.R;
import com.lzpnsd.sunshine.SunshineApplication;
import com.lzpnsd.sunshine.activity.CityAddActivity;
import com.lzpnsd.sunshine.activity.CityListActivity;
import com.lzpnsd.sunshine.bean.CityBean;
import com.lzpnsd.sunshine.bean.CityWeatherBean;
import com.lzpnsd.sunshine.bean.EnvironmentBean;
import com.lzpnsd.sunshine.bean.WeatherInfoBean;
import com.lzpnsd.sunshine.db.CityDBManager;
import com.lzpnsd.sunshine.manager.DataManager;
import com.lzpnsd.sunshine.model.ILocationListener;
import com.lzpnsd.sunshine.model.OnCustomItemClickListener;
import com.lzpnsd.sunshine.util.LocationUtil;
import com.lzpnsd.sunshine.util.LogUtil;
import com.lzpnsd.sunshine.util.ToastUtil;
import com.lzpnsd.sunshine.util.WeatherBackgroundUtil;
import com.lzpnsd.sunshine.util.WeatherUtil;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
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
	
	public WeatherView(Context mContext) {
		this.mContext = mContext;
	}

	public RelativeLayout initView() {
		LayoutInflater inflater = LayoutInflater.from(mContext);
		mView = (RelativeLayout) inflater.inflate(R.layout.view_weather, null);
		setViews();
		showLastInfo();
		if(!SunshineApplication.isFirst){
			refreshData();
		}else{
			startLocation();
		}
		return mView;
	}

	private void showLastInfo() {
		List<WeatherInfoBean> currentWeatherInfoBeans = DataManager.getInstance().getCurrentWeatherInfoBeans();
		if(null != currentWeatherInfoBeans && currentWeatherInfoBeans.size()>0){
			initData();
			mHorizontalChartView.addChildView(currentWeatherInfoBeans);
			mHorizontalChartView.notifyDataChanged();
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
		mHorizontalChartView = (CustomHorizontalView) mView.findViewById(R.id.chv_mine);
		mLlAqi = (LinearLayout) mView.findViewById(R.id.ll_weather_aqi);
		mIvAqi = (ImageView) mView.findViewById(R.id.iv_weather_aqi);
		mTvAqi = (TextView) mView.findViewById(R.id.tv_weather_aqi);
		mTvWeatherUpdateTime = (TextView) mView.findViewById(R.id.tv_weather_update_time);
		mIbCityList = (ImageButton) mView.findViewById(R.id.ib_weather_title_city);
		mIbShare = (ImageButton) mView.findViewById(R.id.ib_weather_title_share);
		mTvCityName = (TextView) mView.findViewById(R.id.tv_weather_city_name);
		mIbCityList.setOnClickListener(mOnClickListener);
		mHorizontalChartView.setOnItemClickListener(new OnCustomItemClickListener() {

			@Override
			public void onClick(View v, int position) {
				
			}
		});
	}
	
	public void refreshData(){
		WeatherUtil.getInstance().getWeather(DataManager.getInstance().getCurrentCityId(), new WeatherUtil.CallBack() {

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
	
	/**
	 * 初始化数据
	 */
	private void initData() {
		mRlMain.setBackgroundResource(WeatherBackgroundUtil.getWeatherMainBackground());
		mTvCityName.setText(DataManager.getInstance().getCurrentCityWeatherBeans().get(0).getCity());
		setAqiData();
		List<CityWeatherBean> cityWeatherBeans = DataManager.getInstance().getCurrentCityWeatherBeans();
		if(cityWeatherBeans == null || cityWeatherBeans.size() <=0){
			mTvWeatherUpdateTime.setText("");
		}else{
			mTvWeatherUpdateTime.setText("更新于"+DataManager.getInstance().getCurrentCityWeatherBeans().get(0).getUpdateTime());
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
	
}
