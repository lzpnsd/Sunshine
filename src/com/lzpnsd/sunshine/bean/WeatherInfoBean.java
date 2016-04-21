package com.lzpnsd.sunshine.bean;

/**
 * 天气情况
 *
 * @author lize
 *
 * 2016年2月3日
 */
public class WeatherInfoBean {

	/**
	 * 日期  如：2日星期二
	 */
	private String date;
	/**
	 * 最高温度   如：
	 */
	private String highTemperature;
	/**
	 * 最低温度   如： -11
	 */
	private String lowTemperature;
	/**
	 * 白天天气情况  如：晴
	 */
	private String dayType;
	/**
	 * 白天风向   如：东南风
	 */
	private String dayWindDirection;
	/**
	 * 白天风力   如：微风
	 */
	private String dayWindPower;
	/**
	 * 晚上天气情况  如：晴
	 */
	private String nightType;
	/**
	 * 晚上风向   如：西北风
	 */
	private String nightWindDirection;
	/**
	 * 晚上风力   如：微风
	 */
	private String nightWindPower;
	
	private String error;
	
	public WeatherInfoBean() {
		super();
	}

	public WeatherInfoBean(String date, String highTemperature, String lowTemperature, String dayType, String dayWindDirection, String dayWindPower, String nightType, String nightWindDirection, String nightWindPower, String error) {
		super();
		this.date = date;
		this.highTemperature = highTemperature;
		this.lowTemperature = lowTemperature;
		this.dayType = dayType;
		this.dayWindDirection = dayWindDirection;
		this.dayWindPower = dayWindPower;
		this.nightType = nightType;
		this.nightWindDirection = nightWindDirection;
		this.nightWindPower = nightWindPower;
		this.error = error;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getHighTemperature() {
		return highTemperature;
	}

	public void setHighTemperature(String highTemperature) {
		this.highTemperature = highTemperature;
	}

	public String getLowTemperature() {
		return lowTemperature;
	}

	public void setLowTemperature(String lowTemperature) {
		this.lowTemperature = lowTemperature;
	}

	public String getDayType() {
		return dayType;
	}

	public void setDayType(String dayType) {
		this.dayType = dayType;
	}

	public String getDayWindDirection() {
		return dayWindDirection;
	}

	public void setDayWindDirection(String dayWindDirection) {
		this.dayWindDirection = dayWindDirection;
	}

	public String getDayWindPower() {
		return dayWindPower;
	}

	public void setDayWindPower(String dayWindPower) {
		this.dayWindPower = dayWindPower;
	}

	public String getNightType() {
		return nightType;
	}

	public void setNightType(String nightType) {
		this.nightType = nightType;
	}

	public String getNightWindDirection() {
		return nightWindDirection;
	}

	public void setNightWindDirection(String nightWindDirection) {
		this.nightWindDirection = nightWindDirection;
	}

	public String getNightWindPower() {
		return nightWindPower;
	}

	public void setNightWindPower(String nightWindPower) {
		this.nightWindPower = nightWindPower;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	@Override
	public String toString() {
		return "WeatherInfoBean [date=" + date + ", highTemperature=" + highTemperature + ", lowTemperature=" + lowTemperature + ", dayType=" + dayType + ", dayWindDirection=" + dayWindDirection + ", dayWindPower=" + dayWindPower + ", nightType=" + nightType + ", nightWindDirection=" + nightWindDirection + ", nightWindPower=" + nightWindPower + ", error=" + error + "]";
	}

}
