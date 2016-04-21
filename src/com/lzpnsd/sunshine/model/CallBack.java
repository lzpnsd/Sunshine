package com.lzpnsd.sunshine.model;

import java.io.InputStream;
import java.net.HttpURLConnection;

/**
 * http请求回调接口
 * @author lzpnsd
 * 2016年3月6日 下午9:45:21
 *
 */
public interface CallBack {
	/**
	 * 成功
	 * 
	 * @param response
	 */
	void onSuccess(HttpURLConnection conn);

	/**
	 * 失败
	 * 
	 * @param result
	 */
	void onFailure(InputStream inputStream);
}
