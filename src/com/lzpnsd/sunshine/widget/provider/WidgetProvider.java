package com.lzpnsd.sunshine.widget.provider;

import com.lzpnsd.sunshine.R;
import com.lzpnsd.sunshine.bean.CityBean;
import com.lzpnsd.sunshine.bean.WeatherInfoBean;
import com.lzpnsd.sunshine.bean.WidgetInfoBean;
import com.lzpnsd.sunshine.manager.DataManager;
import com.lzpnsd.sunshine.model.impl.WidgetInfoModel;
import com.lzpnsd.sunshine.util.WeatherIconUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.R.integer;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.RemoteViews;

/**
 * 
 * 小组件广播接收处理
 * 
 * @since 2016/4/20
 * 
 * @author liuqing
 */
public class WidgetProvider extends AppWidgetProvider {
	private static final String TAG = "WidgetProvider";
	private final Intent INTENT_SERVICE = new Intent("android.appwidget.action.WIDGET_SERVICE");
	private final String UPDATA = "android.appwidget.action.APPWIDGET_UPDATE";
	private Context mContext;
	private WidgetInfoBean mWidgetInfoBean;
	private WidgetInfoModel mWidgetInfoModel;
	private WeatherInfoBean mWeatherInfoBean;
	private CityBean mCityBean;
	private Handler timeHandler;
	private RemoteViews remoteView;
	private static final int BUTTON_SHOW = 1;
	private static final int TIME_START = 2;
	private boolean isStop = true;
	private int count = 0;

	// 接收广播的回调函数
	@Override
	public void onReceive(Context context, Intent intent) {
		super.onReceive(context, intent);
		mContext = context;
		final String action = intent.getAction();
		Log.d(TAG, "onReceive:action:" + action);
		if (intent.hasCategory(Intent.CATEGORY_ALTERNATIVE)) {// 点击widget
			Uri data = intent.getData();
			int buttonId = Integer.parseInt(data.getSchemeSpecificPart());
			Log.d(TAG, "buttonId="+buttonId);
			if (buttonId == BUTTON_SHOW) {
				Log.d(TAG, "==Button widget goto apk==");
				mContext.startService(INTENT_SERVICE);
			}
		}
		if(intent.getAction().equals(UPDATA)){
			ShowWidgetInfo(mContext);
		}
	}

	private void ShowWidgetInfo(Context context) {
		mContext = context;
		mWidgetInfoBean = new WidgetInfoBean();
		mWidgetInfoModel = new WidgetInfoModel(mContext);

		timeHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
					case TIME_START:
						removeMessages(TIME_START);
						mWeatherInfoBean = DataManager.getInstance().getCurrentWeatherInfoBeans().get(1);
						mCityBean = DataManager.getInstance().getCurrentCityBean();
						mWidgetInfoBean = mWidgetInfoModel.getWidgetInfo();
						String time = mWidgetInfoBean.getmWidgetTime();
						String data = mWidgetInfoBean.getmWidgetData();
						String month = mWidgetInfoBean.getmWidgetMouth();
						String lowtem = mWeatherInfoBean.getLowTemperature();
						String hightem = mWeatherInfoBean.getHighTemperature();
						String weather = null;
						int igweather = 0;
						if(WeatherIconUtil.isDayOrNight()){
							weather = mWeatherInfoBean.getDayType();
							igweather = WeatherIconUtil.getDaySmallImageResource(weather);
						}else{
							weather = mWeatherInfoBean.getNightType();
							igweather = WeatherIconUtil.getNightSmallImageResource(weather);
						}
						String city = mCityBean.getNameCn();
//						ImageLoader.getInstance().displayImage("drawable://"+WeatherIconUtil.getSmallImageResource(mWeatherInfoBean.getDayType()), R.id.iv_widget_weather);
						Log.d(TAG, "time=" + time + " " + "data=" + data + " " + "month=" + month + "lowtem="
+ lowtem + "hightem=" + hightem + "weather=" + weather + "city=" +city);

						remoteView = new RemoteViews(mContext.getPackageName(), R.layout.widget_info_item);
						remoteView.setTextViewText(R.id.tv_widget_time, time);
						remoteView.setTextViewText(R.id.tv_widget_data, data);
						remoteView.setTextViewText(R.id.tv_widget_month, month);
						remoteView.setTextViewText(R.id.tv_widget_temperature,lowtem+"℃"+"~"+hightem+"℃");//温度
						remoteView.setTextViewText(R.id.tv_widget_weather, weather);//天气
						remoteView.setImageViewResource(R.id.iv_widget_weather, igweather);//天气图片
						remoteView.setTextViewText(R.id.tv_widget_area, city);//城市
						remoteView.setOnClickPendingIntent(R.id.Li_widget_layout, getPendingIntent(mContext, BUTTON_SHOW));
						
						AppWidgetManager manager = AppWidgetManager.getInstance(mContext);
						int[] appWidgetIds = manager.getAppWidgetIds(new ComponentName(mContext, WidgetProvider.class));
						manager.updateAppWidget(appWidgetIds, remoteView);// 更新所有实例
						break;

					default:
						break;
				}
			}
		};
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				while(isStop){
					count++;
					Log.d(TAG, "count="+count);
					Message timemsg = new Message();
					timemsg.what = TIME_START;
					timeHandler.handleMessage(timemsg);
					try {
						Thread.sleep(20000);// 20秒更新一次
					} catch (InterruptedException e) {
						// TODO: handle exception
						e.getStackTrace();
					}
				}	
			}
		}).start();
	}

	private PendingIntent getPendingIntent(Context context, int buttonId) {
		Intent intent = new Intent();
		intent.setClass(context, WidgetProvider.class);
		intent.addCategory(Intent.CATEGORY_ALTERNATIVE);
		intent.setData(Uri.parse("custom:" + buttonId));
		PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);
		return pi;
	}

	// 每添加一次 widget 时，被执行
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		Log.d(TAG, "onUpdate():appWidgetIds.length==" + appWidgetIds.length);
		mContext = context;
		ShowWidgetInfo(mContext);
	}

	// 第一个widget被创建时调用
	@Override
	public void onEnabled(Context context) {
		mContext = context;
		Log.d(TAG, "onEnabled()");
		ShowWidgetInfo(mContext);
		super.onEnabled(mContext);
	}

	// 最后一个widget被删除时调用
	@Override
	public void onDisabled(Context context) {
		mContext = context;
		Log.d(TAG, "onDisabled()");
		mContext.stopService(INTENT_SERVICE);
		super.onDisabled(mContext);
	}

	// widget被删除时调用
	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {
		mContext = context;
		Log.d(TAG, "onDeleted():appWidgetIds.length=" + appWidgetIds.length);
		super.onDeleted(mContext, appWidgetIds);
	}

}
