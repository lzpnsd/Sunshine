package com.lzpnsd.sunshine.activity;

import java.util.ArrayList;
import java.util.List;

import com.lzpnsd.sunshine.R;
import com.lzpnsd.sunshine.adapter.CustomCityListAdapter;
import com.lzpnsd.sunshine.bean.CityListItemBean;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ListView;

/**
 * code:101
 *
 * @author lize
 *
 * 2016年5月9日
 */
public class CityListActivity extends Activity {

	private ImageButton mIbBack;
	private ImageButton mIbEdit;
	private ImageButton mIbOk;
	private ImageButton mIbAdd;
	private ListView mLvCityList;
	
	private List<CityListItemBean> mCityListItemBeans = new ArrayList<CityListItemBean>();
	
	public static final int CODE_GOBACK_RESULT = 1011;
	public static final int CODE_SELECT_CITY_RESULT = 1012;
	public static final int CODE_ADD_CITY_REQUEST = 1013;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_city_list);
		initViews();
		setListener();
		setAdapter();
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
	}
	
	private void setAdapter(){
		CityListItemBean cityListItemBean  = new CityListItemBean("晴", "背景", 200, -1, false);
		mCityListItemBeans.add(cityListItemBean);
		mCityListItemBeans.add(cityListItemBean);
		mCityListItemBeans.add(cityListItemBean);
		CustomCityListAdapter adapter = new CustomCityListAdapter(CityListActivity.this, mCityListItemBeans);
		mLvCityList.setAdapter(adapter);
	}
	
	private OnClickListener clickListener = new OnClickListener(){

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
				case R.id.ib_city_list_title_bar_back:
					setResult(CODE_GOBACK_RESULT);
					finish();
					break;
				case R.id.ib_city_list_title_bar_edit:
					
					break;
				case R.id.ib_city_list_title_bar_ok:
					
					break;
				case R.id.ib_city_list_title_bar_add:
					Intent cityAddIntent = new Intent(CityListActivity.this,CityAddActivity.class);
					startActivityForResult(cityAddIntent, CODE_ADD_CITY_REQUEST);
					break;
			}
		}
		
	};
	
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
	
}
