package com.lzpnsd.sunshine.activity;

import com.lzpnsd.sunshine.R;
import com.lzpnsd.sunshine.util.LogUtil;

import android.app.Activity;
import android.os.Bundle;
import android.preference.PreferenceFragment;

public class SettingsActivity extends Activity {

	private final LogUtil log = LogUtil.getLog(getClass());
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		getFragmentManager().beginTransaction().replace(R.id.rl_settings_content, new SettingsPrefFragment()).commit();
	}
	
	private class SettingsPrefFragment extends PreferenceFragment{
		
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			addPreferencesFromResource(R.xml.preference_settings);
		}
	}
	
}
