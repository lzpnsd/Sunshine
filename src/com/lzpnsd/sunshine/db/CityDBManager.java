package com.lzpnsd.sunshine.db;

import java.util.ArrayList;
import java.util.List;

import com.lzpnsd.sunshine.SunshineApplication;
import com.lzpnsd.sunshine.bean.CityBean;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class CityDBManager {

	private CityListDatabaseHelper mDatabaseHelper;

	private static CityDBManager sInstance = new CityDBManager();

	private CityDBManager() {
		mDatabaseHelper = new CityListDatabaseHelper(SunshineApplication.getContext());
	}

	public static CityDBManager getInstance() {
		return sInstance;
	}

	public void insertIntoCity(CityBean cityBean) {
		SQLiteDatabase database = mDatabaseHelper.getReadableDatabase();
		String sql = "insert into " + CityListDatabaseHelper.TABLE_NAME_CITY + " ("
				+ "area_id,name_en,name_cn,district_en," + "district_cn,prov_en,prov_cn,nation_en,nation_cn) values ('" 
				+ cityBean.getAreaId() + "','" 
				+ cityBean.getNameEn() + "','" 
				+ cityBean.getNameCn() + "','" 
				+ cityBean.getDistrictEn() + "','" 
				+ cityBean.getDistrictCn() + "','" 
				+ cityBean.getProvEn() + "','" 
				+ cityBean.getProvCn() + "','" 
				+ cityBean.getNationEn() + "','" 
				+ cityBean.getNationCn() + "'" 
				+ ")";
		database.execSQL(sql);
		database.close();
	}

	public void insertIntoCity(List<CityBean> cityBeans){
		SQLiteDatabase database = mDatabaseHelper.getReadableDatabase();
		database.beginTransaction();
		String sql = "insert into " + CityListDatabaseHelper.TABLE_NAME_CITY + " ("
				+ "area_id,name_en,name_cn,district_en,district_cn,prov_en,prov_cn,nation_en,nation_cn) "
				+ "values (?,?,?,?,?,?,?,?,?)";
		for(CityBean cityBean : cityBeans){
			database.execSQL(sql, new String[]{cityBean.getAreaId(),cityBean.getNameEn(),cityBean.getNameCn(),
					cityBean.getDistrictEn(),cityBean.getDistrictCn(),cityBean.getProvEn(),cityBean.getProvCn(),
					cityBean.getNameEn(),cityBean.getDistrictCn()});
		}
		database.setTransactionSuccessful();
		database.endTransaction();
		database.close();
	}
	
	public void insertIntoHotCity() {
//		 select * from city where name_cn='北京' or name_cn='天津' or name_cn='上海'
//		 or name_cn='重庆' or name_cn='沈阳' or name_cn='大连' or name_cn='长春'
//		 or name_cn='哈尔滨' or name_cn='郑州' or name_cn='武汉' or name_cn='长沙' or
//		 name_cn='广州' or name_cn='深圳' or name_cn='南京';
		String[] hotCities = new String[] { 
				"101010100", 
				"101030100", 
				"101020100", 
				"101040100", 
				"101070101", 
				"101070201", 
				"101060101", 
				"101050101", 
				"101180101", 
				"101200101", 
				"101250101", 
				"101280101", 
				"101280601", 
				"101190101" };
		String[] hotCityNames = new String[]{
				"北京",
				"天津",
				"上海",
				"重庆",
				"沈阳",
				"大连",
				"长春",
				"哈尔滨",
				"郑州",
				"武汉",
				"长沙",
				"广州",
				"深圳",
				"南京"};
		SQLiteDatabase database = mDatabaseHelper.getReadableDatabase();
		database.beginTransaction();
		for (int i = 0; i < 14; i++) {
			String sql = "insert into " + CityListDatabaseHelper.TABLE_NAME_HOT_CITY + " (area_id,name_cn)" + "values ('" + hotCities[i] + "','" + hotCityNames[i] + "')";
			database.execSQL(sql);
		}
		database.setTransactionSuccessful();
		database.endTransaction();
		database.close();
	}

	public CityBean queryCityByName(String cityName) throws Exception {
		if(cityName.endsWith("市") || cityName.endsWith("县") || cityName.endsWith("区")){
			cityName = cityName.substring(0, cityName.length()-1);
		}
		String sql = "select * from city where name_cn='" + cityName + "'";
		SQLiteDatabase database = mDatabaseHelper.getReadableDatabase();
		Cursor cursor = database.rawQuery(sql, null);
		cursor.moveToFirst();
		CityBean cityBean = new CityBean(cursor.getString(1), cursor.getString(2), cursor.getString(3), 
				cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7), 
				cursor.getString(8), cursor.getString(9));
		cursor.close();
		database.close();
		return cityBean;
	}

	private CityBean queryCityByAreaId(String area_id){
		String sql = "select * from city where area_id='" + area_id + "'";
		SQLiteDatabase database = mDatabaseHelper.getReadableDatabase();
		Cursor cursor = database.rawQuery(sql, null);
		cursor.moveToFirst();
		CityBean cityBean = new CityBean(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getString(8));
		cursor.close();
		database.close();
		return cityBean;
	}
	
	public List<CityBean> queryCity(String cityName) throws Exception {
		List<CityBean> cityBeans = new ArrayList<CityBean>();
		SQLiteDatabase database = mDatabaseHelper.getReadableDatabase();
		String sql = "select * from city where name_cn like '%" + cityName + "%' or district_cn like '%" + cityName + "%' or prov_cn like '%" + cityName + "%'";
		Cursor cursor = database.rawQuery(sql, null);
		while(cursor.moveToNext()){
			CityBean cityBean = new CityBean(cursor.getString(1), cursor.getString(2), cursor.getString(3), 
					cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7), 
					cursor.getString(8), cursor.getString(9));
			cityBeans.add(cityBean);
		}
		cursor.close();
		database.close();
		return cityBeans;
	}
	
	/**
	 * 返回的CityBean中只有area_id和name_cn
	 * @return
	 */
	public List<CityBean> queryHotCity(){
		List<CityBean> cityBeans = new ArrayList<CityBean>();
		String sql = "select * from " + CityListDatabaseHelper.TABLE_NAME_HOT_CITY+"";
		SQLiteDatabase database = mDatabaseHelper.getReadableDatabase();
		Cursor cursor = database.rawQuery(sql, null);
		while(cursor.moveToNext()){
			String area_id = cursor.getString(cursor.getColumnIndex("area_id"));
			String name_cn = cursor.getString(cursor.getColumnIndex("name_cn"));
			CityBean cityBean = new CityBean();
			cityBean.setAreaId(area_id);
			cityBean.setNameCn(name_cn);
			cityBeans.add(cityBean);
		}
		database.close();
		cursor.close();
		return cityBeans;
	}
	
}
