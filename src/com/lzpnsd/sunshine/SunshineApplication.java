package com.lzpnsd.sunshine;

import java.util.List;

import com.lzpnsd.sunshine.bean.CityBean;
import com.lzpnsd.sunshine.db.CityDBManager;
import com.lzpnsd.sunshine.manager.DataManager;
import com.lzpnsd.sunshine.service.LocationService;
import com.lzpnsd.sunshine.util.CityUtil;
import com.lzpnsd.sunshine.util.LogUtil;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.Application;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

public class SunshineApplication extends Application {

	private LogUtil log = LogUtil.getLog(getClass());
	
	private static Context mContext;
	
	private static final SunshineApplication sInstance = new SunshineApplication();
	
	private int mScreenWidth;
	private int mScreenHeight;
	
	public static LocationService locationService;
	public static boolean isFirst;
	
	public static SunshineApplication getInstance(){
		return sInstance;
	}
	
	public SunshineApplication() {
		super();
	}
	
	/**
	 * 由于使用了多进程(如：在配置文件中service的process设置为:remote),会导致多次执行onCreate方法，所以需要在执行
	 * 方法是加判断
	 */
	@Override
	public void onCreate() {
		super.onCreate();
		log.d("onCreate");
		String processName = getProcessName(this, android.os.Process.myPid());
        if (processName != null) {
            boolean defaultProcess = processName.equals(getPackageName());
            log.d("default process = "+getPackageName()+",processName = "+processName);
            if (defaultProcess) {
				mContext = getApplicationContext();
				locationService = new LocationService(getApplicationContext());
				getWindowScreenMetric();
				initImageLoader();
				insertData();
//				CityWeatherInfoTest.getInstance(mContext).test();
			}
        }
	}

	private void insertData() {
		isFirst = DataManager.getInstance().isFirst();
		log.d("isFirst = "+isFirst);
		if(isFirst){
			new Thread(){
				@Override
				public void run() {
					log.d("insertData");
					CityDBManager cityDBManager = CityDBManager.getInstance();
					CityUtil cityUtil = new CityUtil();
					List<CityBean> cityBeans = cityUtil.parseExcel(mContext);
					if(cityBeans != null){
						cityDBManager.insertIntoCity(cityBeans);
					}
					cityDBManager.insertIntoHotCity();
					DataManager.getInstance().setIsFirst(false);
					log.d("isFirst = "+isFirst);
				}
			}.start();
		}
	}
	
	private void initImageLoader() {
		ImageLoaderConfiguration config = new ImageLoaderConfiguration  
			    .Builder(mContext)  
			    .memoryCacheExtraOptions(480, 800) // max width, max height，即保存的每个缓存文件的最大长宽  
			    .threadPoolSize(3)//线程池内加载的数量  
			    .threadPriority(Thread.NORM_PRIORITY - 2)  
			    .denyCacheImageMultipleSizesInMemory()  
			    .memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024)) // You can pass your own memory cache implementation/你可以通过自己的内存缓存实现  
			    .memoryCacheSize(2 * 1024 * 1024)    
			    .tasksProcessingOrder(QueueProcessingType.LIFO)  
			    .defaultDisplayImageOptions(DisplayImageOptions.createSimple())  
			    .imageDownloader(new BaseImageDownloader(mContext, 5 * 1000, 30 * 1000)) // connectTimeout (5 s), readTimeout (30 s)超时时间  
			    .writeDebugLogs() // Remove for release app  
			    .build();//开始构建  
		ImageLoader.getInstance().init(config);
		
//		DisplayImageOptions options;  
//		options = new DisplayImageOptions.Builder()  
//		 .showImageOnLoading(R.drawable.ic_launcher) //设置图片在下载期间显示的图片  
//		 .showImageForEmptyUri(R.drawable.ic_launcher)//设置图片Uri为空或是错误的时候显示的图片  
//		.showImageOnFail(R.drawable.ic_launcher)  //设置图片加载/解码过程中错误时候显示的图片
//		.cacheInMemory(true)//设置下载的图片是否缓存在内存中  
//		.considerExifParams(true)  //是否考虑JPEG图像EXIF参数（旋转，翻转）
//		.imageScaleType(ImageScaleType.EXACTLY_STRETCHED)//设置图片以如何的编码方式显示  
//		.bitmapConfig(Bitmap.Config.RGB_565)//设置图片的解码类型//  
//		//.delayBeforeLoading(int delayInMillis)//int delayInMillis为你设置的下载前的延迟时间
//		//设置图片加入缓存前，对bitmap进行设置  
//		//.preProcessor(BitmapProcessor preProcessor)  
//		.resetViewBeforeLoading(true)//设置图片在下载前是否重置，复位  
//		.displayer(new RoundedBitmapDisplayer(20))//是否设置为圆角，弧度为多少  
//		.displayer(new FadeInBitmapDisplayer(100))//是否图片加载好后渐入的动画时间  
//		.build();//构建完成  
	}

	private void getWindowScreenMetric() {
		DisplayMetrics metric = new DisplayMetrics(); 
		WindowManager windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
		windowManager.getDefaultDisplay().getMetrics(metric);  
		mScreenWidth = metric.widthPixels;     // 屏幕宽度（像素）  
		mScreenHeight = metric.heightPixels;   // 屏幕高度（像素）  
		
//		log.d("mScreenWidth = "+mScreenWidth);
//		log.d("mScreenHeight = "+mScreenHeight);
//		log.d("mScreenWidthDip = "+metric.xdpi);
//		log.d("mScreenHeightDip = "+metric.ydpi);
//		log.d("density"+metric.density);
//		log.d("densityDpi = "+metric.densityDpi);
//		log.d("scaledDensity = "+metric.scaledDensity);
//		log.d(""+metric.widthPixels * metric.density + 0.5f);
//		log.d(""+metric.heightPixels * metric.density + 0.5f);
		
		float density = metric.density;      // 屏幕密度（0.75 / 1.0 / 1.5）  
		int densityDpi = metric.densityDpi;  // 屏幕密度DPI（120 / 160 / 240） 
	}

	public static Context getContext(){
		return mContext;
	}
	
	public int getScreenWidth(){
		return mScreenWidth;
	}
	
	public int getScreenHeight(){
		return mScreenHeight;
	}
	
	/**
	 * 获得进程的名字
	 * @param cxt Context
	 * @param pid pid,can use android.os.Process.myPid()
	 * @return if the process of param's pid not run,return null,else,return process's name
	 */
	private String getProcessName(Context cxt, int pid) {
        ActivityManager am = (ActivityManager) cxt.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();
        if (runningApps == null) {
            return null;
        }
        for (RunningAppProcessInfo procInfo : runningApps) {
            if (procInfo.pid == pid) {
                return procInfo.processName;
            }
        }
        return null;
    }
	
}
