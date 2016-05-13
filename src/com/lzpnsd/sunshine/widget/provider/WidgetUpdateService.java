package com.lzpnsd.sunshine.widget.provider;

import com.lzpnsd.sunshine.util.LogUtil;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

/**
 * 服务处理 更新小组件
 * 
 * @since 2016/4/20
 * 
 * @author liuqing
 * 
 */
public class WidgetUpdateService extends Service {

	private final LogUtil log = LogUtil.getLog(getClass());
	private final String ACTION_UPDATE_ALL = "com.lzpnsd.sunshine.widget.UPDATE_ALL";
	private static final int UPDATE_TIME = 5000;
	private UpdateThread mUpdateThread;
	private Context mContext;
	private int count = 0;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		log.d("onCreate()");
		mUpdateThread = new UpdateThread();
		mUpdateThread.start();
		mContext = getApplicationContext();
		super.onCreate();
	}
	

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		log.d("onStartCommand()");	
		/*try {
			PackageManager mPackageManager = getPackageManager();
			Intent intentapk = mPackageManager.getLaunchIntentForPackage("com.lzpnsd.sunshine");
			startActivity(intentapk);
		} catch (Exception e) {
			e.printStackTrace();
		}*/
		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		log.d("onDestroy()");
		if(mUpdateThread != null){
			mUpdateThread.interrupt();
		}
		super.onDestroy();
	}
	
	private class UpdateThread extends Thread{
		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			try {
				count = 0;
				while(true){
					log.d("run...count: "+count);
					count++;
					Intent intent = new Intent(ACTION_UPDATE_ALL);
					mContext.sendBroadcast(intent);
					Thread.sleep(UPDATE_TIME);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
