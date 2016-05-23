package com.lzpnsd.sunshine.activity;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.lzpnsd.sunshine.R;
import com.lzpnsd.sunshine.adapter.CustomCityListAdapter;
import com.lzpnsd.sunshine.bean.CityBean;
import com.lzpnsd.sunshine.bean.CityListItemBean;
import com.lzpnsd.sunshine.bean.WeatherInfoBean;
import com.lzpnsd.sunshine.contants.Contants;
import com.lzpnsd.sunshine.db.CityDBManager;
import com.lzpnsd.sunshine.manager.DataManager;
import com.lzpnsd.sunshine.util.LogUtil;
import com.lzpnsd.sunshine.util.WeatherUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;

/**
 * code:101
 *
 * @author lize
 *
 * 2016年5月9日
 */
public class CityListActivity extends BaseActivity {

	private final LogUtil log = LogUtil.getLog(getClass());
	
	public static final int CODE_GOBACK_RESULT = 1011;
	public static final int CODE_SELECT_CITY_RESULT = 1012;
	public static final int CODE_ADD_CITY_REQUEST = 1013;
	
	private ImageButton mIbBack;
	private ImageButton mIbEdit;
	private ImageButton mIbOk;
	private ImageButton mIbAdd;
	private ListView mLvCityList;
	
	private List<CityListItemBean> mCityListItemBeans = new ArrayList<CityListItemBean>();
	
	private CustomCityListAdapter mCustomCityListAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_city_list);
		initViews();
		setListener();
		setAdapter();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		List<CityBean> savedCity = CityDBManager.getInstance().querySavedCity();
		if(null != savedCity && savedCity.size() > 0){
			getSavedCityInfo(savedCity);
			mCustomCityListAdapter.notifyDataSetChanged();
		}
		mIbEdit.setVisibility(View.VISIBLE);
		mIbOk.setVisibility(View.GONE);
	}
	
	private void initViews(){
		mIbBack = (ImageButton) findViewById(R.id.ib_city_list_title_bar_back);
		mIbAdd = (ImageButton) findViewById(R.id.ib_city_list_title_bar_add);
		mIbEdit = (ImageButton) findViewById(R.id.ib_city_list_title_bar_edit);
		mIbOk = (ImageButton) findViewById(R.id.ib_city_list_title_bar_ok);
		mLvCityList = (ListView) findViewById(R.id.lv_city_list);
	}
	
	private void setListener(){
		mIbBack.setOnClickListener(clickListener);
		mIbAdd.setOnClickListener(clickListener);
		mIbEdit.setOnClickListener(clickListener);
		mIbOk.setOnClickListener(clickListener);
		mLvCityList.setOnItemClickListener(mOnItemClickListener);
	}
	
	private void setAdapter(){
		List<CityBean> savedCity = CityDBManager.getInstance().querySavedCity();
		if(null != savedCity && savedCity.size() > 0){
			getSavedCityInfo(savedCity);
			mCustomCityListAdapter = new CustomCityListAdapter(mCityListItemBeans);
			mLvCityList.setAdapter(mCustomCityListAdapter);
		}else{
			turnToAddCity();
		}
	}

	private void getSavedCityInfo(List<CityBean> savedCity) {
		mCityListItemBeans.clear();
		for(CityBean cityBean : savedCity){
			int areaId = Integer.parseInt(cityBean.getAreaId());
			List<WeatherInfoBean> weatherInfos = null;
			try {
				weatherInfos = WeatherUtil.getInstance().getWeatherInfo(areaId, WeatherInfoBean.class);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if(null != weatherInfos && weatherInfos.size() >1){
				WeatherInfoBean weatherInfoBean = weatherInfos.get(1);
				CityListItemBean cityListItemBean  = new CityListItemBean(
						areaId,
						cityBean.getNameCn(),
						weatherInfoBean.getDayType(),
						Integer.parseInt(weatherInfoBean.getHighTemperature()),
						Integer.parseInt(weatherInfoBean.getLowTemperature()),
						false
						);
				mCityListItemBeans.add(cityListItemBean);
			}else{
				mCityListItemBeans.add(new CityListItemBean(areaId, cityBean.getNameCn(), "", 0, 0, false));
			}
		}
	}
	
	private OnItemClickListener mOnItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			CityListItemBean cityListItemBean = mCityListItemBeans.get(position);
			log.d("cityListItemBean = "+cityListItemBean);
			int area_id = cityListItemBean.getArea_id();
			CityBean cityBean = new CityBean(area_id+"", cityListItemBean.getCityName(), null, null, null, null, null, null, null);
			DataManager.getInstance().setCurrentCityBean(cityBean);
			log.d("cityBean = "+cityBean);
			Intent intent = new Intent();
			intent.putExtra(Contants.NAME_AREA_ID, area_id);
			setResult(CODE_SELECT_CITY_RESULT, intent);
			finish();
		}
	};
	
	private OnClickListener clickListener = new OnClickListener(){

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
				case R.id.ib_city_list_title_bar_back:
					setResult(CODE_GOBACK_RESULT);
					finish();
					break;
				case R.id.ib_city_list_title_bar_edit:
					for(CityListItemBean cityListItemBean : mCityListItemBeans){
						cityListItemBean.setShowDelete(true);
					}
					mCustomCityListAdapter.notifyDataSetChanged();
					mIbEdit.setVisibility(View.GONE);
					mIbOk.setVisibility(View.VISIBLE);
					break;
				case R.id.ib_city_list_title_bar_ok:
					List<Integer> delectCitied = mCustomCityListAdapter.getDelectCity();
					//从保存的天气文件中删除这个城市的天气文件
					WeatherUtil.getInstance().deleteWeatherInfo(delectCitied);
					//删除保存的城市
					CityDBManager.getInstance().deleteSavedCity(delectCitied);
					mCustomCityListAdapter.notifyDataSetChanged();
					mIbEdit.setVisibility(View.VISIBLE);
					mIbOk.setVisibility(View.GONE);
					for(CityListItemBean cityListItemBean : mCityListItemBeans){
						cityListItemBean.setShowDelete(false);
					}
					if(null != mCityListItemBeans && mCityListItemBeans.size()>0){
						for(CityListItemBean cityListItem : mCityListItemBeans){
							if(cityListItem.getArea_id() == DataManager.getInstance().getCurrentCityId()){
								return;
							}
						}
						int area_id = mCityListItemBeans.get(mCityListItemBeans.size()-1).getArea_id();
						DataManager.getInstance().setCurrentCityId(area_id,false);
					}else{
						DataManager.getInstance().setCurrentCityId(DataManager.DEFAULT_CITYID,true);
						turnToAddCity();
					}
					break;
				case R.id.ib_city_list_title_bar_add:
					turnToAddCity();
					break;
			}
		}
	};
	
	private void turnToAddCity() {
		Intent cityAddIntent = new Intent(CityListActivity.this,CityAddActivity.class);
		startActivityForResult(cityAddIntent, CODE_ADD_CITY_REQUEST);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (resultCode) {
			case CityAddActivity.CODE_CITY_ADD_RESULT:
				setResult(CityAddActivity.CODE_CITY_ADD_RESULT,data);
				finish();
				break;
			default:
				break;
		}
	};
	
	@Override
	public void onBackPressed() {
		setResult(CODE_GOBACK_RESULT);
		finish();
		super.onBackPressed();
	}
	
}
