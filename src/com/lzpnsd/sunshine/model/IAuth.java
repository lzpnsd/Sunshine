package com.lzpnsd.sunshine.model;

/**
 * 用户认证接口
 * @author lzpnsd
 * 2016年3月6日 上午11:43:46
 *
 */
public interface IAuth {

	/**
	 * 用户登录
	 * @param userName 用户名
	 * @param password 密码
	 * @return 登录成功返回true，否则返回false
	 */
	boolean login(String userName,String password);
	/**
	 * 用户登出
	 * @return 登出成功返回true，否则返回false
	 */
	boolean logout();
	
}
