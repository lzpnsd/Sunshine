package com.lzpnsd.sunshine.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;

public class BaseActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		int API_LEVEL =  android.os.Build.VERSION.SDK_INT;

		//沉浸式状态栏
		if (API_LEVEL >= 19)
		{
		    getWindow().addFlags( WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS );     
		}
		
//		//透明状态栏
//		getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//		//透明导航栏
//		getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
	}
	
}
