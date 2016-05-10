package com.lzpnsd.sunshine.util;

import com.lzpnsd.sunshine.SunshineApplication;

import android.widget.Toast;

public class ToastUtil {
	
	public static final int LENGTH_LONG = Toast.LENGTH_LONG;
	public static final int LENGTH_SHORT = Toast.LENGTH_SHORT;

	public static void showToast(String message,int  duration){
		Toast.makeText(SunshineApplication.getContext(), message, duration).show();
	}
	
}
