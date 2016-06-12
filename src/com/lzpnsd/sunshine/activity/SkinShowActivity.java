package com.lzpnsd.sunshine.activity;

import com.lzpnsd.sunshine.R;
import com.lzpnsd.sunshine.adapter.SkinShowAdapter;

import android.content.Context;
import android.os.Bundle;
import android.widget.Gallery;

public class SkinShowActivity extends BaseActivity{
	private Context mContext;	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_skin_show);
		mContext = SkinShowActivity.this;
		Gallery gallery = (Gallery) findViewById(R.id.gl_skin_show);
		SkinShowAdapter skinShowAdapter = new SkinShowAdapter(mContext);
		gallery.setAdapter(skinShowAdapter);
	}

}
