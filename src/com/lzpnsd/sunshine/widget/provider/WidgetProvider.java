package com.lzpnsd.sunshine.widget.provider;

import java.util.List;

import com.lzpnsd.sunshine.MainActivity;
import com.lzpnsd.sunshine.R;
import com.lzpnsd.sunshine.bean.CityWeatherBean;
import com.lzpnsd.sunshine.bean.WeatherInfoBean;
import com.lzpnsd.sunshine.bean.WidgetInfoBean;
import com.lzpnsd.sunshine.manager.DataManager;
import com.lzpnsd.sunshine.model.impl.WidgetInfoModel;
import com.lzpnsd.sunshine.util.LogUtil;
import com.lzpnsd.sunshine.util.WeatherIconUtil;

import android.app.AlarmManager;
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

	private RemoteViews remoteView;
	private static final int BUTTON_SHOW = 1;

	// 接收广播的回调函数
	@Override
	public void onReceive(Context context, Intent intent) {
		super.onReceive(context, intent);
		final String action = intent.getAction();
		log.d("onReceive:action:" + action);
		showWidgetInfo(context);
		if (intent.hasCategory(Intent.CATEGORY_ALTERNATIVE)) {// 点击事件
			Uri data = intent.getData();
			int buttonId = Integer.parseInt(data.getSchemeSpecificPart());
			log.d("buttonId=" + buttonId);
			if (buttonId == BUTTON_SHOW) {
				log.d("==Button widget goto apk==");
				startUpgrade(context);
				Intent intentTurnToMain = new Intent();
				intentTurnToMain.setComponent(new ComponentName(context, MainActivity.class));
				intentTurnToMain.setAction(Intent.ACTION_VIEW);
				intentTurnToMain.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(intentTurnToMain);
			}
		}
	}

	private void showWidgetInfo(Context context) {
		List<WeatherInfoBean> currentWeatherInfoBeans = DataManager.getInstance().getCurrentWeatherInfoBeans();
		if (currentWeatherInfoBeans.size() <= 1) {
			return;
		}
		WeatherInfoBean weatherInfoBean = currentWeatherInfoBeans.get(1);
		CityWeatherBean cityWeatherBean = DataManager.getInstance().getCurrentCityWeatherBeans().get(0);
		WidgetInfoBean widgetInfoBean = new WidgetInfoModel(context).getWidgetInfo();
		String time = widgetInfoBean.getmWidgetTime();
		String data = widgetInfoBean.getmWidgetData();
		String month = widgetInfoBean.getmWidgetMouth();
		String tem = cityWeatherBean.getTemperature();
		String weather = weatherInfoBean.getDayType();
		int igweather = WeatherIconUtil.getDayBigImageResource(weather);
		String city = cityWeatherBean.getCity();
		log.d("time=" + time + " " + "data=" + data + " " + "month=" + month + "tem=" + tem + "weather=" + weather
				+ "city=" + city);

		remoteView = new RemoteViews(context.getPackageName(), R.layout.widget_info_item);
		remoteView.setTextViewText(R.id.tv_widget_time, time);
		remoteView.setTextViewText(R.id.tv_widget_data, data);
		remoteView.setTextViewText(R.id.tv_widget_month, month);
		remoteView.setTextViewText(R.id.tv_widget_temperature, tem + "℃");// 温度
		remoteView.setTextViewText(R.id.tv_widget_weather, weather);// 天气
		remoteView.setImageViewResource(R.id.iv_widget_weather, igweather);// 天气图片
		remoteView.setTextViewText(R.id.tv_widget_area, city);// 城市
		remoteView.setOnClickPendingIntent(R.id.Li_widget_layout, getPendingIntent(context, BUTTON_SHOW));

		AppWidgetManager manager = AppWidgetManager.getInstance(context);
		int[] appWidgetIds = manager.getAppWidgetIds(new ComponentName(context, WidgetProvider.class));
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
		super.onEnabled(context);
		// context.startService(INTENT_SERVICE);
		log.d("onEnabled");
		startUpgrade(context);
		showWidgetInfo(context);
	}

	private void startUpgrade(Context context) {
		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent("android.appwidget.action.APPWIDGET_UPDATE");
		PendingIntent operation = PendingIntent.getBroadcast(context, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 3000, 3000, operation);
	}

	// 最后一个widget被删除时调用
	@Override
	public void onDisabled(Context context) {
		super.onDisabled(context);
		log.d("onDisabled");
		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent("android.appwidget.action.APPWIDGET_UPDATE");
		PendingIntent operation = PendingIntent.getBroadcast(context, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		alarmManager.cancel(operation);
	}

	// 每添加一次 widget 时，被执行
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		log.d("onUpdate():appWidgetIds.length==" + appWidgetIds.length);
		super.onUpdate(context, appWidgetManager, appWidgetIds);
		showWidgetInfo(context);
		startUpgrade(context);
	}

	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {
		super.onDeleted(context, appWidgetIds);
	}

}
