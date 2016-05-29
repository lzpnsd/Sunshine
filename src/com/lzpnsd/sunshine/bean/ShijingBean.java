package com.lzpnsd.sunshine.bean;

import java.io.Serializable;

public class ShijingBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8987998797721635940L;

	private int cityId;
	/**
	 * 图片URL
	 */
	private String picUrl;
	private String location;

	public ShijingBean() {
		super();
	}

	public ShijingBean(int cityId, String picUrl, String location) {
		super();
		this.cityId = cityId;
		this.picUrl = picUrl;
		this.location = location;
	}

	public int getCityId() {
		return cityId;
	}

	public void setCityId(int cityId) {
		this.cityId = cityId;
	}

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	@Override
	public String toString() {
		return "ShijingBean [cityId=" + cityId + ", picUrl=" + picUrl + ", location=" + location + "]";
	}

}
