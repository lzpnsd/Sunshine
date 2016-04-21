package com.lzpnsd.sunshine.view;

import com.lzpnsd.sunshine.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

public class ShijingView {

	private Context mContext;

	private RelativeLayout mView;
	
	public ShijingView(Context mContext) {
		super();
		this.mContext = mContext;
	}
	
	public RelativeLayout initView(){
		LayoutInflater inflater = LayoutInflater.from(mContext);
		mView = (RelativeLayout) inflater.inflate(R.layout.view_shijing,null);
		setViews();
		return mView;
	}
	
	private void setViews(){
		
	}
}
