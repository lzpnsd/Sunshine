package com.lzpnsd.sunshine.receiver;

import com.lzpnsd.sunshine.R;
import com.lzpnsd.sunshine.activity.SettingsActivity;
import com.lzpnsd.sunshine.manager.DataManager;
import com.lzpnsd.sunshine.service.RefreshWeatherService;
import com.lzpnsd.sunshine.util.LogUtil;
import com.lzpnsd.sunshine.widget.provider.WidgetProvider;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class BootCompletedReceive extends BroadcastReceiver {

	private final LogUtil log = LogUtil.getLog(getClass());
	
	@Override
	public void onReceive(Context context, Intent intent) {
		log.d("receive boot completed");
		AppWidgetManager manager = AppWidgetManager.getInstance(context);
		int[] appWidgetIds = manager.getAppWidgetIds(new ComponentName(context, WidgetProvider.class));
		if(appWidgetIds.length > 0){
			AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
			Intent updateWidget = new Intent("android.appwidget.action.APPWIDGET_UPDATE");
			PendingIntent operation = PendingIntent.getBroadcast(context, 1, updateWidget, PendingIntent.FLAG_UPDATE_CURRENT);
			alarmManager.setRepeating(AlarmManager.RTC, System.currentTimeMillis()+3000, 3000, operation);
		}
		if(0 != DataManager.getInstance().getCurrentCityId()){
			SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
			final String rateValue = sharedPreferences.getString(SettingsActivity.NAME_REFRESH_WEATHER, context.getString(R.string.text_settings_rate_default));
			final int time = Integer.parseInt(rateValue)*60*60*1000;
			AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
			Intent refreshWeatherService = new Intent(context,RefreshWeatherService.class);
			PendingIntent operation = PendingIntent.getService(context, 1, refreshWeatherService, PendingIntent.FLAG_UPDATE_CURRENT);
			alarmManager.setRepeating(AlarmManager.RTC, System.currentTimeMillis(), time, operation);
		}
	}

}
