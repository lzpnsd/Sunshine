package com.lzpnsd.sunshine.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.lzpnsd.sunshine.R;
import com.lzpnsd.sunshine.adapter.AddCityAdapter;
import com.lzpnsd.sunshine.adapter.HotCityAdapter;
import com.lzpnsd.sunshine.bean.CityBean;
import com.lzpnsd.sunshine.contants.Contants;
import com.lzpnsd.sunshine.db.CityDBManager;
import com.lzpnsd.sunshine.manager.DataManager;
import com.lzpnsd.sunshine.model.ILocationListener;
import com.lzpnsd.sunshine.util.LocationUtil;
import com.lzpnsd.sunshine.util.LogUtil;
import com.lzpnsd.sunshine.util.ToastUtil;

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
public class CityAddActivity extends BaseActivity{

	private LogUtil log = LogUtil.getLog(getClass());
	
	public static final int CODE_CITY_ADD_RESULT = 2;
	
	private EditText mEtSearchCity;
	private ImageButton mIbSearch;
	private ListView mLvSearchResult;
	private GridView mGvHotCity;
	
	private List<CityBean> mCityBeans;
	private List<CityBean> mHotCityBeans;
	
	private AddCityAdapter mAddCityAdapter;
	private HotCityAdapter mHotCityAdapter;
	
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
		mHotCityBeans = CityDBManager.getInstance().queryHotCity();
		mHotCityAdapter = new HotCityAdapter(mHotCityBeans);
	}

	private void initViews(){
		mEtSearchCity = (EditText) findViewById(R.id.et_add_city_search);
		mIbSearch = (ImageButton) findViewById(R.id.ib_add_city_search);
		mLvSearchResult = (ListView) findViewById(R.id.lv_add_city_search_result);
		mGvHotCity = (GridView) findViewById(R.id.gv_add_city_hot_city);
	}
	
	private void setListener(){
		mEtSearchCity.addTextChangedListener(new TextChangeListener());
		mEtSearchCity.setOnFocusChangeListener(mOnFocusChangeListener);
		mIbSearch.setOnClickListener(mOnClickListener);
		mLvSearchResult.setAdapter(mAddCityAdapter);
		mLvSearchResult.setOnItemClickListener(mOnItemClickListener);
		mLvSearchResult.setVisibility(View.GONE);
		mGvHotCity.setAdapter(mHotCityAdapter);
		mGvHotCity.setOnItemClickListener(mOnItemClickListener);
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
			mLvSearchResult.setVisibility(View.VISIBLE);
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
					if (hasFocus){
						mIbSearch.setVisibility(View.GONE);
						mGvHotCity.setVisibility(View.GONE);
					}
					else{
						mIbSearch.setVisibility(View.VISIBLE);
						mGvHotCity.setVisibility(View.VISIBLE);
					}
					break;
			}
		}
	};
	
	private OnItemClickListener mOnItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			log.d("parent view is "+parent.toString()+",view is "+view+",position = "+position+",id = "+id);
			switch (parent.getId()) {
				case R.id.lv_add_city_search_result:
					final CityBean cityBean = mCityBeans.get(position);
					CityDBManager.getInstance().insertIntoSaved(CityAddActivity.this,Integer.parseInt(cityBean.getAreaId()), cityBean.getNameCn());
					turnToWeatherView(cityBean);
					break;
				case R.id.gv_add_city_hot_city:
					if(0 == position){
						ToastUtil.showToast(getString(R.string.location_start), ToastUtil.LENGTH_LONG);
						LocationUtil.getInstance().startLocation(new ILocationListener() {
							
							@Override
							public void onReceiveLocation(String location) {
								try {
									log.d("onReceive location,location = "+location);
									JSONObject jsonObject = new JSONObject(location);
									CityBean cityBean = CityDBManager.getInstance().queryCityByLocation(jsonObject);
									if(cityBean != null){
										DataManager.getInstance().setCurrentCityBean(cityBean);
										CityDBManager.getInstance().insertIntoSaved(CityAddActivity.this,Integer.parseInt(cityBean.getAreaId()), cityBean.getNameCn());
										ToastUtil.showToast(getString(R.string.location_success), ToastUtil.LENGTH_LONG);
										turnToWeatherView(cityBean);
									}else{
										handleLocationFailed(getString(R.string.location_failed));
									}
								} catch (JSONException e) {
									handleLocationFailed(getString(R.string.location_failed));
								} catch (Exception e) {
									handleLocationFailed(getString(R.string.location_failed));
								}
							}
							
							@Override
							public void onFailed(String result) {
								handleLocationFailed(result);
							}
						});
					}else{
						CityBean city = mHotCityBeans.get(position-1);
						if(DataManager.getInstance().getCurrentCityId() == Integer.parseInt(city.getAreaId()))
							ToastUtil.showToast(getString(R.string.city_cant_add_repeat_city),ToastUtil.LENGTH_LONG);
						else{
							CityDBManager.getInstance().insertIntoSaved(CityAddActivity.this,Integer.parseInt(city.getAreaId()), city.getNameCn());
							turnToWeatherView(mHotCityBeans.get(position-1));
						}
					}
					break;
			}
		}

	};
	
	private void turnToWeatherView(CityBean cityBean) {
		DataManager.getInstance().setCurrentCityBean(cityBean);
		Intent intent = new Intent();
		intent.putExtra(Contants.NAME_AREA_ID, Integer.parseInt(cityBean.getAreaId()));
		CityAddActivity.this.setResult(CODE_CITY_ADD_RESULT,intent);
		CityAddActivity.this.finish();
	}
	
	private void handleLocationFailed(String message) {
		ToastUtil.showToast(message, ToastUtil.LENGTH_LONG);
	}
	
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
	
	@Override
	public void onBackPressed() {
		List<CityBean> savedCity = CityDBManager.getInstance().querySavedCity(CityAddActivity.this);
		if(null == savedCity || savedCity.size()<=0){
			ToastUtil.showToast(getString(R.string.no_saved_city), ToastUtil.LENGTH_LONG);
			return;
		}
		super.onBackPressed();
	}
	
}
