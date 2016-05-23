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

	public ShijingBean() {
		super();
	}

	public ShijingBean(int cityId, String picUrl) {
		super();
		this.cityId = cityId;
		this.picUrl = picUrl;
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

	@Override
	public String toString() {
		return "ShijingBean [cityId=" + cityId + ", picUrl=" + picUrl + "]";
	}

}
