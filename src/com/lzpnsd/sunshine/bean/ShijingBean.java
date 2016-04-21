package com.lzpnsd.sunshine.bean;

import java.io.Serializable;

public class ShijingBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8987998797721635940L;

	private String picUrl;
	private String local;
	private int localNum;
	private long time;

	public ShijingBean() {
		super();
	}

	public ShijingBean(String picUrl, String local, int localNum, long time) {
		super();
		this.picUrl = picUrl;
		this.local = local;
		this.localNum = localNum;
		this.time = time;
	}

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	public String getLocal() {
		return local;
	}

	public void setLocal(String local) {
		this.local = local;
	}

	public int getLocalNum() {
		return localNum;
	}

	public void setLocalNum(int localNum) {
		this.localNum = localNum;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	@Override
	public String toString() {
		return "ShijingBean [picUrl=" + picUrl + ", local=" + local + ", localNum=" + localNum + ", time=" + time + "]";
	}
}
