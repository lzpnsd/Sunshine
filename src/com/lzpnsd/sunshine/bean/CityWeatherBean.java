package com.lzpnsd.sunshine.bean;

/**
 * 城市天气的基本信息
 *
 * @author lize
 *
 * 2016年2月3日
 */
public class CityWeatherBean {

	/**
	 * 城市名称 如： 北京
	 */
	private String city;
	/**
	 * 天气更新时间  如：13:45
	 */
	private String updateTime;
	/**
	 * 温度  如：7
	 */
	private String temperature;
	/**
	 * 风力  如：1级
	 */
	private String windPower;
	/**
	 * 湿度  如：19%
	 */
	private String dampness;
	/**
	 * 风向  如：南风
	 */
	private String windDirection;
	/**
	 * 日出时间   如：07:24
	 */
	private String sunRise;
	/**
	 * 日落时间  如：17:41
	 */
	private String sunSet;
	
	
	public CityWeatherBean() {
		super();
	}

	public CityWeatherBean(String city, String updateTime, String temperature, String windPower, String dampness, String windDirection, String sunRise, String sunSet) {
		super();
		this.city = city;
		this.updateTime = updateTime;
		this.temperature = temperature;
		this.windPower = windPower;
		this.dampness = dampness;
		this.windDirection = windDirection;
		this.sunRise = sunRise;
		this.sunSet = sunSet;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public String getTemperature() {
		return temperature;
	}

	public void setTemperature(String temperature) {
		this.temperature = temperature;
	}

	public String getWindPower() {
		return windPower;
	}

	public void setWindPower(String windPower) {
		this.windPower = windPower;
	}

	public String getDampness() {
		return dampness;
	}

	public void setDampness(String dampness) {
		this.dampness = dampness;
	}

	public String getWindDirection() {
		return windDirection;
	}

	public void setWindDirection(String windDirection) {
		this.windDirection = windDirection;
	}

	public String getSunRise() {
		return sunRise;
	}

	public void setSunRise(String sunRise) {
		this.sunRise = sunRise;
	}

	public String getSunSet() {
		return sunSet;
	}

	public void setSunSet(String sunSet) {
		this.sunSet = sunSet;
	}

	@Override
	public String toString() {
		return "CityWeatherBean [city=" + city + ", updateTime=" + updateTime + ", temperature=" + temperature + ", windPower=" + windPower + ", dampness=" + dampness + ", windDirection=" + windDirection + ", sunRise=" + sunRise + ", sunSet=" + sunSet + "]";
	}
	
}
