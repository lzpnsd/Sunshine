package com.lzpnsd.sunshine.activity;

import com.lzpnsd.sunshine.R;
import com.lzpnsd.sunshine.contants.Contants;
import com.lzpnsd.sunshine.service.RefreshWeatherService;
import com.lzpnsd.sunshine.util.LogUtil;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

public class SettingsActivity extends BaseActivity {

	private final LogUtil log = LogUtil.getLog(getClass());
	
	public static final String NAME_REFRESH_WEATHER = "rate_refresh_weather";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		SettingsPrefFragment settingsPrefFragment = new SettingsPrefFragment();
		getFragmentManager().beginTransaction().replace(R.id.rl_settings_content, settingsPrefFragment).commit();
	}
	
	private class SettingsPrefFragment extends PreferenceFragment{
		
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			addPreferencesFromResource(R.xml.preference_settings);
			SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(SettingsActivity.this);
			String value = sharedPreferences.getString(NAME_REFRESH_WEATHER, getString(R.string.text_settings_rate_default));
			Preference preference = findPreference(NAME_REFRESH_WEATHER);
			preference.setSummary(String.format(getString(R.string.text_settings_rate_weather_description), value));
		}
		
		@Override
		public void onResume() {
			super.onResume();
			getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(mSharedPreferenceChangeListener);
		}
		
		private OnSharedPreferenceChangeListener mSharedPreferenceChangeListener = new OnSharedPreferenceChangeListener() {
			
			@Override
			public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
				Preference preference = findPreference(key);
				preference.setSummary(String.format(getString(R.string.text_settings_rate_weather_description), sharedPreferences.getString(key, getString(R.string.text_settings_rate_default))));
//				Intent intent = new Intent(Contants.ACTION_REFRESH_WEATHER_SERVICE);
//				stopService(intent);
//				startService(intent);
				SharedPreferences sharedPreference = PreferenceManager.getDefaultSharedPreferences(SettingsActivity.this);
				final String rateValue = sharedPreference.getString(SettingsActivity.NAME_REFRESH_WEATHER, getString(R.string.text_settings_rate_default));
				final int time = Integer.parseInt(rateValue)*60*60*1000;
				AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
				Intent refreshWeatherService = new Intent(SettingsActivity.this,RefreshWeatherService.class);
				PendingIntent operation = PendingIntent.getService(SettingsActivity.this, 1, refreshWeatherService, PendingIntent.FLAG_UPDATE_CURRENT);
				alarmManager.cancel(operation);
				alarmManager.setRepeating(AlarmManager.RTC, System.currentTimeMillis(), time, operation);
			}
		};
		
		@Override
		public void onPause() {
			super.onPause();
			getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(mSharedPreferenceChangeListener);
		};
		
	}
	
}
