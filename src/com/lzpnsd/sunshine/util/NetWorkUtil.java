package com.lzpnsd.sunshine.util;

import com.lzpnsd.sunshine.SunshineApplication;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetWorkUtil {
	
	public static boolean isWifi(){
		ConnectivityManager connectivityManager = (ConnectivityManager) SunshineApplication.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		if(null != activeNetworkInfo){
			int type = activeNetworkInfo.getType();
			if(ConnectivityManager.TYPE_WIFI == type){
				return true;
			}
		}
		return false;
	}

}
