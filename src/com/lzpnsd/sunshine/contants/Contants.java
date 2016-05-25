package com.lzpnsd.sunshine.contants;

import com.lzpnsd.sunshine.SunshineApplication;
import com.lzpnsd.sunshine.util.AdaptationUtil;

public class Contants {

//	public static final int HEIGHT_TITLE_TEXT = AdaptationUtil.dip2px(SunshineApplication.getContext(), 25);
//	public static final int HEIGHT_TEXT = AdaptationUtil.dip2px(SunshineApplication.getContext(), 20);
//	public static final int HEIGHT_BITMAP = AdaptationUtil.dip2px(SunshineApplication.getContext(), 30);
//	public static final int HEIGHT_CHART_TEMPERATURE = AdaptationUtil.dip2px(SunshineApplication.getContext(), 100);
//	public static final int WIDTH_CHART_ITEM = AdaptationUtil.dip2px(SunshineApplication.getContext(), 70);
//	public static final int SPACE_CHART_TEMPERATURE = AdaptationUtil.dip2px(SunshineApplication.getContext(), 10);
	
	public static final String NAME_AREA_ID = "area_id";
	
	public static final int DATABASE_VERSION = 1;
	
	public static final String PATH_CACHE_WEATHER_FILE = SunshineApplication.getContext().getFilesDir().getAbsolutePath();
	public static final String PATH_DATABASES_FILE = SunshineApplication.getContext().getDatabasePath("sunshine.db").getAbsolutePath();
	
	public static final String URL_HOST = "http://115.28.4.67";
	
	public static final String NAME_SELECT_IMAGE_URI = "uri";
	public static final String NAME_SELECT_IMAGE_PATH = "path";
	
	public static final String VALUE_USER_ID = "1";
	
	public static final String ACTION_REFRESH_WEATHER_SERVICE = "com.lzpnsd.sunshine.service.REFRESH_WEATHER_SERVICE";
	
}
