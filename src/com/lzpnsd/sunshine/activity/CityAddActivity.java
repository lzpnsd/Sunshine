package com.lzpnsd.sunshine.activity;

import java.util.ArrayList;
import java.util.List;

import com.lzpnsd.sunshine.R;
import com.lzpnsd.sunshine.adapter.AddCityAdapter;
import com.lzpnsd.sunshine.bean.CityBean;
import com.lzpnsd.sunshine.bean.WeatherInfoBean;
import com.lzpnsd.sunshine.contants.Contants;
import com.lzpnsd.sunshine.db.CityDBManager;
import com.lzpnsd.sunshine.manager.DataManager;
import com.lzpnsd.sunshine.util.LogUtil;
import com.lzpnsd.sunshine.util.ToastUtil;
import com.lzpnsd.sunshine.util.WeatherUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListView;

/**
 * code:102
 *
 * @author lize
 *
 * 2016年5月9日
 */
public class CityAddActivity extends Activity{

	private LogUtil log = LogUtil.getLog(getClass());
	
	public static final int CODE_CITY_ADD_RESULT = 2;
	
	private EditText mEtSearchCity;
	private ImageButton mIbSearch;
	private ListView mLvSearchResult;
	private GridView mGvhotCity;
	
	private List<CityBean> mCityBeans;
	
	private AddCityAdapter mAddCityAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_city);
		
		initData();
		
		initViews();
		setListener();
	}
	
	private void initData() {
		mCityBeans = new ArrayList<CityBean>();
		mAddCityAdapter = new AddCityAdapter(mCityBeans);
	}

	private void initViews(){
		mEtSearchCity = (EditText) findViewById(R.id.et_add_city_search);
		mIbSearch = (ImageButton) findViewById(R.id.ib_add_city_search);
		mLvSearchResult = (ListView) findViewById(R.id.lv_add_city_search_result);
		mGvhotCity = (GridView) findViewById(R.id.gv_add_city_hot_city);
	}
	
	private void setListener(){
		mEtSearchCity.addTextChangedListener(new TextChangeListener());
		mEtSearchCity.setOnFocusChangeListener(mOnFocusChangeListener);
		mIbSearch.setOnClickListener(mOnClickListener);
		mLvSearchResult.setAdapter(mAddCityAdapter);
		mLvSearchResult.setOnItemClickListener(mOnItemClickListener);
	}
	
	private class TextChangeListener implements TextWatcher{

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			
		}

		@Override
		public void afterTextChanged(Editable s) {
			searchCity();
		}
	}
	
	private OnClickListener mOnClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
				case R.id.ib_add_city_search:
					searchCity();
					break;
			}
		}
	};
	
	private OnFocusChangeListener mOnFocusChangeListener = new OnFocusChangeListener() {
		
		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			switch (v.getId()) {
				case R.id.et_add_city_search:
					if (hasFocus)
						mIbSearch.setVisibility(View.GONE);
					else
						mIbSearch.setVisibility(View.VISIBLE);
					break;
			}
		}
	};
	
	private OnItemClickListener mOnItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			final CityBean cityBean = mCityBeans.get(position);
			DataManager.getInstance().setCurrentCityBean(cityBean);
			Intent intent = new Intent();
			intent.putExtra(Contants.NAME_AREA_ID, Integer.parseInt(cityBean.getAreaId()));
			CityAddActivity.this.setResult(CODE_CITY_ADD_RESULT,intent);
			CityAddActivity.this.finish();
		}
	};
	
	private void searchCity() {
		String cityName = mEtSearchCity.getText().toString();
		log.d("search city is "+cityName);
		if(!TextUtils.isEmpty(cityName)){
			List<CityBean> cityBeans;
			try {
				cityBeans = CityDBManager.getInstance().queryCity(cityName);
				mCityBeans.clear();
				mCityBeans.addAll(cityBeans);
				mAddCityAdapter.notifyDataSetChanged();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
}
