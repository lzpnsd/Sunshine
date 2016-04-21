package com.lzpnsd.sunshine.manager;

import com.lzpnsd.sunshine.SunshineApplication;

public class DataManager {
	
	public static final DataManager sInstance = new DataManager();
	
	public static DataManager getInstance(){
		return sInstance;
	}
	
	private DataManager(){
		
	}
	
	public int getScreenWidth(){
		return SunshineApplication.getInstance().getScreenWidth();
	}
	
	public int getScreenHeight(){
		return SunshineApplication.getInstance().getScreenHeight();
	}
	
	public int getCityId(){
		return 101010100;
	}
	
}
