package com.lzpnsd.sunshine.bean;

/**
 * 小组件数据
 * 
 * @author liuqing
 * 
 */
public class WidgetInfoBean {

	private String mWidgetTime;// 时间
	private String mWidgetArea;// 地区
	private String mWidgetData;// 日期
	private String mWidgetMouth;// 月份（阴历）
	private String mImWidgetWeather;// 天气（图片）
	private String mWidgetTem;// 温度
	private String mTvWidgetWeather;// 天气

	public String getmWidgetTime() {
		return mWidgetTime;
	}

	public void setmWidgetTime(String mWidgetTime) {
		this.mWidgetTime = mWidgetTime;
	}

	public String getmWidgetArea() {
		return mWidgetArea;
	}

	public void setmWidgetArea(String mWidgetArea) {
		this.mWidgetArea = mWidgetArea;
	}

	public String getmWidgetData() {
		return mWidgetData;
	}

	public void setmWidgetData(String mWidgetData) {
		this.mWidgetData = mWidgetData;
	}

	public String getmWidgetMouth() {
		return mWidgetMouth;
	}

	public void setmWidgetMouth(String mWidgetMouth) {
		this.mWidgetMouth = mWidgetMouth;
	}

	public String getmImWidgetWeather() {
		return mImWidgetWeather;
	}

	public void setmImWidgetWeather(String mImWidgetWeather) {
		this.mImWidgetWeather = mImWidgetWeather;
	}

	public String getmWidgetTem() {
		return mWidgetTem;
	}

	public void setmWidgetTem(String mWidgetTem) {
		this.mWidgetTem = mWidgetTem;
	}

	public String getmTvWidgetWeather() {
		return mTvWidgetWeather;
	}

	public void setmTvWidgetWeather(String mTvWidgetWeather) {
		this.mTvWidgetWeather = mTvWidgetWeather;
	}
	@Override
	public String toString() {
		return "WidgetInfoBean:[mWidgetTime"+mWidgetTime+"mWidgetArea"+mWidgetArea+"mWidgetData"+mWidgetData+"mWidgetMouth"+mWidgetMouth+"...]";
	}
}
