package com.lzpnsd.sunshine.adapter;

import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

public class ShijingViewPagerAdapter extends PagerAdapter {

	private List<View> mPics;
	
	public ShijingViewPagerAdapter(List<View> pics) {
		this.mPics = pics;
	}
	
	@Override
	public int getCount() {
		return mPics.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView(mPics.get(position));
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
//		int pos = position % mPics.size();
		View view = mPics.get(position);
		container.addView(view);
		return view;
	}

	@Override
	public int getItemPosition(Object object) {
		return PagerAdapter.POSITION_NONE;
	}
	
}
