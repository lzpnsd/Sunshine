package com.lzpnsd.sunshine.widget.provider;

import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.util.Log;

/**
 * 服务处理 启动apk
 * 
 * @since 2016/4/20
 * 
 * @author liuqing
 * 
 */
public class WidgetService extends Service {

	private static final String TAG = "WidgetService";

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		Log.d(TAG, "onCreate()");
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d(TAG, "onStartCommand()");
		try {
			PackageManager mPackageManager = getPackageManager();
			Intent intentapk = mPackageManager.getLaunchIntentForPackage("com.lzpnsd.sunshine");
			startActivity(intentapk);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		Log.d(TAG, "onDestroy()");
		super.onDestroy();
	}
}
