package com.lzpnsd.sunshine.bean;

/**
 * 
 *
 * @author lize
 *
 *         2016年4月26日
 */
public class CityListItemBean {

	private String type;
	private String cityName;
	private int highTemperature;
	private int lowTemperature;
	private boolean isShowDelete;
	
	public CityListItemBean() {
	}

	public CityListItemBean(String type, String cityName, int highTemperature, int lowTemperature, boolean isShowDelete) {
		super();
		this.type = type;
		this.cityName = cityName;
		this.highTemperature = highTemperature;
		this.lowTemperature = lowTemperature;
		this.isShowDelete = isShowDelete;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
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
		return "CityListItemBean [type=" + type + ", cityName=" + cityName + ", highTemperature=" + highTemperature + ", lowTemperature=" + lowTemperature + ", isShowDelete=" + isShowDelete + "]";
	}

}
