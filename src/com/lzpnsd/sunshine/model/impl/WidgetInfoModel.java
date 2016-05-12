package com.lzpnsd.sunshine.model.impl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateFormat;
import android.util.Log;

import com.lzpnsd.sunshine.bean.WeatherInfoBean;
import com.lzpnsd.sunshine.bean.WidgetInfoBean;
import com.lzpnsd.sunshine.model.IWidgetInfoModel;
import com.lzpnsd.sunshine.util.LunarDataUtil;
import com.lzpnsd.sunshine.util.WeatherIconUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

public class WidgetInfoModel implements IWidgetInfoModel {

	private static final String TAG = "WidgetInfoModel";
	private static final int TIME_START = 1;
	private Context mContext;
	private Handler timeHandler;
	private WidgetInfoBean mWidgetInfoBean;

	public WidgetInfoModel(Context context) {
		this.mContext = context;
	}

	@Override
	public void UpdataWidgetInfo() {

	}

	@Override
	public WidgetInfoBean getWidgetInfo() {
		mWidgetInfoBean = new WidgetInfoBean();
		// 时间格式   10:20
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
		String nowTime = simpleDateFormat.format(new Date());
		mWidgetInfoBean.setmWidgetTime(nowTime);
		Log.d(TAG, "newtime=" + nowTime);
		// 日期格式   04/25
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd");
		String dateString = formatter.format(currentTime);
		mWidgetInfoBean.setmWidgetData(dateString);
		Log.d(TAG, "dateString=" + dateString);
		//月份格式  三月初一
		Calendar today = Calendar.getInstance();
		LunarDataUtil mouth = new LunarDataUtil(today);
		String widgetmouth = mouth.toString();
		mWidgetInfoBean.setmWidgetMouth(widgetmouth);
		Log.d(TAG, "mouth=" + mouth);
		/*timeHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
					case TIME_START:
						removeMessages(TIME_START);
						
						sendEmptyMessageDelayed(TIME_START, 5000);
						break;

					default:
						break;
				}
			}
		};
		Message timemsg = new Message();
		timemsg.what = TIME_START;
		timeHandler.handleMessage(timemsg);	*/
		
		return mWidgetInfoBean;
	}
}
