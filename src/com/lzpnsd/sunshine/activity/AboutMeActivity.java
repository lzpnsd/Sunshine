package com.lzpnsd.sunshine.activity;

import com.lzpnsd.sunshine.R;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.TextView;

public class AboutMeActivity extends BaseActivity {
	private TextView mTvVersion;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about_me);
		mTvVersion = (TextView) findViewById(R.id.tv_about_me_version);
		try {
			mTvVersion.setText("Version "+getVersionName());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String getVersionName() throws Exception {
		PackageManager packageManager = getPackageManager();
		// getPackageName()是你当前类的包名，0代表是获取版本信息
		PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(), 0);
		String version = packInfo.versionName;
		return version;
	}
}
