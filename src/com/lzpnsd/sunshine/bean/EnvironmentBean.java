package com.lzpnsd.sunshine.bean;

/**
 * 环境信息
 * 有的可能没有
 *
 * @author lize
 *
 * 2016年2月3日
 */
public class EnvironmentBean {
	
	/**
	 * 空气质量指数  如：108
	 */
	private int aqi;
	/**
	 * pm2.5值  如：80
	 */
	private int pm25;
	/**
	 * 建议  如：儿童、老年人及心脏、呼吸系统疾病患者人群应减少长时间或高强度户外锻炼
	 */
	private String suggest;
	/**
	 * 空气质量  如：轻度污染
	 */
	private String quality;
	/**
	 * 大气主要污染物  如：颗粒物(PM2.5)
	 */
	private String majorPollutants;
	/**
	 * 臭氧含量  如：61
	 */
	private int o3;
	/**
	 * 一氧化碳含量  如：2
	 */
	private int co;
	/**
	 * pm10含量   如：110
	 */
	private int pm10;
	/**
	 * 二氧化硫含量  如：83
	 */
	private int so2;
	/**
	 * 二氧化氮含量  如：66
	 */
	private int no2;
	/**
	 * 信息更新时间  如：13:00:00
	 */
	private String time;
	
	
	public EnvironmentBean() {
		super();
	}

	public EnvironmentBean(int aqi, int pm25, String suggest, String quality, String majorPollutants, int o3, int co, int pm10, int so2, int no2, String time) {
		super();
		this.aqi = aqi;
		this.pm25 = pm25;
		this.suggest = suggest;
		this.quality = quality;
		this.majorPollutants = majorPollutants;
		this.o3 = o3;
		this.co = co;
		this.pm10 = pm10;
		this.so2 = so2;
		this.no2 = no2;
		this.time = time;
	}

	public int getAqi() {
		return aqi;
	}

	public void setAqi(int aqi) {
		this.aqi = aqi;
	}

	public int getPm25() {
		return pm25;
	}

	public void setPm25(int pm25) {
		this.pm25 = pm25;
	}

	public String getSuggest() {
		return suggest;
	}

	public void setSuggest(String suggest) {
		this.suggest = suggest;
	}

	public String getQuality() {
		return quality;
	}

	public void setQuality(String quality) {
		this.quality = quality;
	}

	public String getMajorPollutants() {
		return majorPollutants;
	}

	public void setMajorPollutants(String majorPollutants) {
		this.majorPollutants = majorPollutants;
	}

	public int getO3() {
		return o3;
	}

	public void setO3(int o3) {
		this.o3 = o3;
	}

	public int getCo() {
		return co;
	}

	public void setCo(int co) {
		this.co = co;
	}

	public int getPm10() {
		return pm10;
	}

	public void setPm10(int pm10) {
		this.pm10 = pm10;
	}

	public int getSo2() {
		return so2;
	}

	public void setSo2(int so2) {
		this.so2 = so2;
	}

	public int getNo2() {
		return no2;
	}

	public void setNo2(int no2) {
		this.no2 = no2;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	@Override
	public String toString() {
		return "EnvironmentBean [aqi=" + aqi + ", pm25=" + pm25 + ", suggest=" + suggest + ", quality=" + quality + ", majorPollutants=" + majorPollutants + ", o3=" + o3 + ", co=" + co + ", pm10=" + pm10 + ", so2=" + so2 + ", no2=" + no2 + ", time=" + time + "]";
	}
	
}
