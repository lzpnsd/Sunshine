package com.lzpnsd.sunshine.bean;

/**
 * 
 *
 * @author lize
 *
 *         2016年4月26日
 */
public class CityListItemBean {

	private int area_id;
	private String cityName;
	private String weatherType;
	private int highTemperature;
	private int lowTemperature;
	private boolean isShowDelete;

	public CityListItemBean() {
	}

	public CityListItemBean(int area_id, String cityName, String weatherType, int highTemperature, int lowTemperature, boolean isShowDelete) {
		super();
		this.area_id = area_id;
		this.cityName = cityName;
		this.weatherType = weatherType;
		this.highTemperature = highTemperature;
		this.lowTemperature = lowTemperature;
		this.isShowDelete = isShowDelete;
	}

	public int getArea_id() {
		return area_id;
	}

	public void setArea_id(int area_id) {
		this.area_id = area_id;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getWeatherType() {
		return weatherType;
	}

	public void setWeatherType(String weatherType) {
		this.weatherType = weatherType;
	}

	public int getHighTemperature() {
		return highTemperature;
	}

	public void setHighTemperature(int highTemperature) {
		this.highTemperature = highTemperature;
	}

	public int getLowTemperature() {
		return lowTemperature;
	}

	public void setLowTemperature(int lowTemperature) {
		this.lowTemperature = lowTemperature;
	}

	public boolean isShowDelete() {
		return isShowDelete;
	}

	public void setShowDelete(boolean isShowDelete) {
		this.isShowDelete = isShowDelete;
	}

	@Override
	public String toString() {
		return "CityListItemBean [area_id=" + area_id + ", cityName=" + cityName + ", weatherType=" + weatherType + ", highTemperature=" + highTemperature + ", lowTemperature=" + lowTemperature + ", isShowDelete=" + isShowDelete + "]";
	}

}
