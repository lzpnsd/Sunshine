package com.lzpnsd.sunshine.contants;

import com.lzpnsd.sunshine.SunshineApplication;
import com.lzpnsd.sunshine.util.AdaptationUtil;

public class Contants {

	public static final int HEIGHT_TITLE_TEXT = AdaptationUtil.dip2px(SunshineApplication.getContext(), 25);
	public static final int HEIGHT_TEXT = AdaptationUtil.dip2px(SunshineApplication.getContext(), 20);
	public static final int HEIGHT_BITMAP = AdaptationUtil.dip2px(SunshineApplication.getContext(), 30);
	public static final int HEIGHT_CHART_TEMPERATURE = AdaptationUtil.dip2px(SunshineApplication.getContext(), 100);
	
	public static final int WIDTH_CHART_ITEM = AdaptationUtil.dip2px(SunshineApplication.getContext(), 70);
	
	public static final int SPACE_CHART_TEMPERATURE = AdaptationUtil.dip2px(SunshineApplication.getContext(), 10);
	
	public static final String NAME_AREA_ID = "area_id";
	
	public static final int DATABASE_VERSION = 1;
	
	public static final String PATH_CACHE_WEATHER_FILE = SunshineApplication.getContext().getFilesDir().getAbsolutePath();
	
}
