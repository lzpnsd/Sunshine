package com.lzpnsd.sunshine.view;

import com.lzpnsd.sunshine.R;
import com.lzpnsd.sunshine.activity.SettingsActivity;
import com.lzpnsd.sunshine.util.LogUtil;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;

public class MineView {

	private LogUtil log = LogUtil.getLog(getClass());
	
	private Context mContext;

	private RelativeLayout mView;
	private Button mBtnSettings;
	
	public MineView(Context mContext) {
		super();
		this.mContext = mContext;
	}
	
	public RelativeLayout initView(){
		LayoutInflater inflater = LayoutInflater.from(mContext);
		mView = (RelativeLayout) inflater.inflate(R.layout.view_mine, null);
		initData();
		setView();
		return mView;
	}

	private void initData() {
	}

	private void setView() {
		mBtnSettings = (Button) mView.findViewById(R.id.btn_mine_settings);
		mBtnSettings.setOnClickListener(mClickListener);
	};
	
	private OnClickListener mClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
				case R.id.btn_mine_settings:
					mContext.startActivity(new Intent(mContext,SettingsActivity.class));
					break;
			}
		}
	};
	
}
