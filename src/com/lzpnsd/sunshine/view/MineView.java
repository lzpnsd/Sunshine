package com.lzpnsd.sunshine.view;

import com.lzpnsd.sunshine.R;
import com.lzpnsd.sunshine.activity.AboutMeActivity;
import com.lzpnsd.sunshine.activity.MineLoginActivity;
import com.lzpnsd.sunshine.activity.SettingsActivity;
import com.lzpnsd.sunshine.activity.SkinShowActivity;
import com.lzpnsd.sunshine.manager.DataCleanManager;
import com.lzpnsd.sunshine.util.LogUtil;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MineView {

	private LogUtil log = LogUtil.getLog(getClass());

	private Context mContext;
	private RelativeLayout mView;
	private LinearLayout mLiSettings;
	private LinearLayout mLiBackground;
	private LinearLayout mLiClean;
	private LinearLayout mLiMine;
	private TextView mTvClean;
	private TextView mTvVersion;
	private ImageButton mIbLogin;
	private ImageButton mIbLetter;
	private String CacheData;
	private UpdataHandler handler;
	private static final int CLEAN = 1;

	public MineView(Context mContext) {
		super();
		this.mContext = mContext;
	}

	public RelativeLayout initView() {
		LayoutInflater inflater = LayoutInflater.from(mContext);
		mView = (RelativeLayout) inflater.inflate(R.layout.view_mine, null);
		handler = new UpdataHandler();
		initData();
		setView();
		return mView;
	}

	private void initData() {
		try {
			CacheData = DataCleanManager.getInstance().getCacheSize(mContext.getCacheDir());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private class UpdataHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
				case CLEAN:
					try {
						CacheData = DataCleanManager.getInstance().getCacheSize(mContext.getCacheDir());
						log.d("CacheData=" + CacheData);
						mTvClean.setText(CacheData);
					} catch (Exception e) {
						e.printStackTrace();
					}
					break;
				default:
					break;
			}
		}
	}

	private void setView() {
		mLiSettings = (LinearLayout) mView.findViewById(R.id.li_settings_setting);
		mLiBackground = (LinearLayout) mView.findViewById(R.id.li_settings_background);
		mLiClean = (LinearLayout) mView.findViewById(R.id.li_settings_clean);
		mLiMine = (LinearLayout) mView.findViewById(R.id.li_settings_me);
		mTvClean = (TextView) mView.findViewById(R.id.tv_text_mine_clean);
		mTvVersion = (TextView) mView.findViewById(R.id.tv_text_mine_version);
		mIbLogin = (ImageButton) mView.findViewById(R.id.ib_settings_login);
		mIbLetter = (ImageButton) mView.findViewById(R.id.ib_settings_letter);
		
		mTvClean.setText(CacheData);
		try {
			mTvVersion.setText(getVersionName());
			log.d("version=" + getVersionName());
		} catch (Exception e) {
			e.printStackTrace();	
		}
		mLiSettings.setOnClickListener(mClickListener);
		mLiBackground.setOnClickListener(mClickListener);
		mLiClean.setOnClickListener(mClickListener);
		mLiMine.setOnClickListener(mClickListener);
		mIbLogin.setOnClickListener(mClickListener);
		mIbLetter.setOnClickListener(mClickListener);
	};

	private OnClickListener mClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
				case R.id.li_settings_setting:
					mContext.startActivity(new Intent(mContext, SettingsActivity.class));
					break;
				case R.id.li_settings_background:
					mContext.startActivity(new Intent(mContext, SkinShowActivity.class));
					break;
				case R.id.li_settings_clean:
					DataCleanManager.getInstance().cleanInternalCache(mContext);
					Message msg = new Message();
					msg.what = CLEAN;
					handler.sendMessage(msg);
					break;
				case R.id.li_settings_me:
					mContext.startActivity(new Intent(mContext, AboutMeActivity.class));
					break;
				case R.id.ib_settings_login://登录
					Intent intent = new Intent(mContext, MineLoginActivity.class);				
					intent.putExtra("type", 0);
					mContext.startActivity(intent);
					break;
				case R.id.ib_settings_letter://消息

					break;
			}
		}
	};

	private String getVersionName() throws Exception {
		PackageManager packageManager = mContext.getPackageManager();
		// getPackageName()是你当前类的包名，0代表是获取版本信息
		PackageInfo packInfo = packageManager.getPackageInfo(mContext.getPackageName(), 0);
		String version = packInfo.versionName;
		return version;
	}
}
