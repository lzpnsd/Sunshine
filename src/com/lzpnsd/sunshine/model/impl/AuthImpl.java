package com.lzpnsd.sunshine.model.impl;

import com.lzpnsd.sunshine.model.IAuth;

public class AuthImpl implements IAuth {

	@Override
	public boolean login(String userName, String password) {
		return false;
	}

	@Override
	public boolean logout() {
		return false;
	}
}
