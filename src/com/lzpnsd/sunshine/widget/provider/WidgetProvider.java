package com.lzpnsd.sunshine.widget.provider;

import com.lzpnsd.sunshine.MainActivity;
import com.lzpnsd.sunshine.R;
import com.lzpnsd.sunshine.bean.CityBean;
import com.lzpnsd.sunshine.bean.WeatherInfoBean;
import com.lzpnsd.sunshine.bean.WidgetInfoBean;
import com.lzpnsd.sunshine.manager.DataManager;
import com.lzpnsd.sunshine.model.impl.WidgetInfoModel;
import com.lzpnsd.sunshine.util.LogUtil;
import com.lzpnsd.sunshine.util.WeatherIconUtil;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
	private final LogUtil log = LogUtil.getLog(getClass());
	private final Intent INTENT_SERVICE = new Intent("android.appwidget.action.WIDGET_SERVICE");
	private final String ACTION_UPDATE_ALL = "com.lzpnsd.sunshine.widget.UPDATE_ALL";
	
	private Context mContext;
	private WidgetInfoBean mWidgetInfoBean;
	private WidgetInfoModel mWidgetInfoModel;
	private WeatherInfoBean mWeatherInfoBean;
	private CityBean mCityBean;
	private RemoteViews remoteView;
	private static final int BUTTON_SHOW = 1;

	// 接收广播的回调函数
	@Override
	public void onReceive(Context context, Intent intent) {
		super.onReceive(context, intent);
		mContext = context;
		final String action = intent.getAction();
		log.d("onReceive:action:" + action);	
		if(ACTION_UPDATE_ALL.equals(action)){
			ShowWidgetInfo(mContext);
		}else if(intent.hasCategory(Intent.CATEGORY_ALTERNATIVE)){//点击事件
			Uri data = intent.getData();
			int buttonId = Integer.parseInt(data.getSchemeSpecificPart());
			log.d("buttonId=" + buttonId);
			if (buttonId == BUTTON_SHOW) {
				log.d("==Button widget goto apk==");
				Intent mIntent = new Intent();
				intent.setComponent(new ComponentName(mContext,MainActivity.class));
				intent.setAction(Intent.ACTION_VIEW);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
				mContext.startActivity(intent);
			}
		}
	}

	private void ShowWidgetInfo(Context context) {
		mContext = context;
		mWidgetInfoBean = new WidgetInfoBean();
		mWidgetInfoModel = new WidgetInfoModel(mContext);
		mWeatherInfoBean = DataManager.getInstance().getCurrentWeatherInfoBeans().get(1);
		mCityBean = DataManager.getInstance().getCurrentCityBean();
		mWidgetInfoBean = mWidgetInfoModel.getWidgetInfo();
		String time = mWidgetInfoBean.getmWidgetTime();
		String data = mWidgetInfoBean.getmWidgetData();
		String month = mWidgetInfoBean.getmWidgetMouth();
		String lowtem = mWeatherInfoBean.getLowTemperature();
		String hightem = mWeatherInfoBean.getHighTemperature();
		String weather = mWeatherInfoBean.getDayType();
		int igweather = WeatherIconUtil.getDaySmallImageResource(weather);
		String city = mCityBean.getNameCn();
		// ImageLoader.getInstance().displayImage("drawable://"+WeatherIconUtil.getSmallImageResource(mWeatherInfoBean.getDayType()),
		// R.id.iv_widget_weather);
		log.d("time=" + time + " " + "data=" + data + " " + "month=" + month + "lowtem=" + lowtem + "hightem=" + hightem + "weather=" + weather + "city=" + city);

		remoteView = new RemoteViews(mContext.getPackageName(), R.layout.widget_info_item);
		remoteView.setTextViewText(R.id.tv_widget_time, time);
		remoteView.setTextViewText(R.id.tv_widget_data, data);
		remoteView.setTextViewText(R.id.tv_widget_month, month);
		remoteView.setTextViewText(R.id.tv_widget_temperature, lowtem + "℃" + "~" + hightem + "℃");// 温度
		remoteView.setTextViewText(R.id.tv_widget_weather, weather);// 天气
		remoteView.setImageViewResource(R.id.iv_widget_weather, igweather);// 天气图片
		remoteView.setTextViewText(R.id.tv_widget_area, city);// 城市
		remoteView.setOnClickPendingIntent(R.id.Li_widget_layout, getPendingIntent(mContext, BUTTON_SHOW));

		AppWidgetManager manager = AppWidgetManager.getInstance(mContext);
		int[] appWidgetIds = manager.getAppWidgetIds(new ComponentName(mContext, WidgetProvider.class));
		manager.updateAppWidget(appWidgetIds, remoteView);// 更新所有实例

	}

	private PendingIntent getPendingIntent(Context context, int buttonId) {
		Intent intent = new Intent();
		intent.setClass(context, WidgetProvider.class);
		intent.addCategory(Intent.CATEGORY_ALTERNATIVE);
		intent.setData(Uri.parse("custom:" + buttonId));
		PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);
		return pi;
	}


	// 第一个widget被创建时调用
	@Override
	public void onEnabled(Context context) {
		mContext = context;
		log.d("onEnabled()");
		mContext.startService(INTENT_SERVICE);
		super.onEnabled(mContext);
	}

	// 最后一个widget被删除时调用
	@Override
	public void onDisabled(Context context) {
		mContext = context;
		log.d("onDisabled()");
		mContext.stopService(INTENT_SERVICE);
//		mContext.stopService(JUMP_SERVICE);
		super.onDisabled(mContext);
	}

	// 每添加一次 widget 时，被执行
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		log.d("onUpdate():appWidgetIds.length==" + appWidgetIds.length);
		mContext = context;
		super.onUpdate(mContext, appWidgetManager, appWidgetIds);
	}
	
	// widget被删除时调用
	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {
		mContext = context;
		log.d("onDeleted():appWidgetIds.length=" + appWidgetIds.length);
		super.onDeleted(mContext, appWidgetIds);
	}

}
