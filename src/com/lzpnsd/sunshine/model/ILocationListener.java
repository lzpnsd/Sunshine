package com.lzpnsd.sunshine.model;

public interface ILocationListener {

	public void onReceiveLocation(String location);
	public void onFailed(String result);
	
}
