package com.lzpnsd.sunshine.model.impl;

import com.lzpnsd.sunshine.model.CallBack;
import com.lzpnsd.sunshine.model.IOutdoorScene;

import android.graphics.Bitmap;

public class OutdoorSceneImpl implements IOutdoorScene{

	@Override
	public boolean upload(Bitmap bitmap) {
		return false;
	}

	@Override
	public Bitmap downloadPic(String picName) {
		return null;
	}

	@Override
	public String getPicList() {
		return null;
	}

}
