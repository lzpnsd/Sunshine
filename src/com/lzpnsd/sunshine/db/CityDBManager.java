package com.lzpnsd.sunshine.db;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import com.google.gson.JsonObject;
import com.lzpnsd.sunshine.SunshineApplication;
import com.lzpnsd.sunshine.bean.CityBean;
import com.lzpnsd.sunshine.contants.Contants;
import com.lzpnsd.sunshine.util.LocationUtil;
import com.lzpnsd.sunshine.util.LogUtil;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class CityDBManager {

	private LogUtil log = LogUtil.getLog(getClass());
	
//	private CityListDatabaseHelper mDatabaseHelper;

	private static CityDBManager sInstance = new CityDBManager();

	private CityDBManager() {
//		mDatabaseHelper = new CityListDatabaseHelper(SunshineApplication.getContext());
	}

	public static synchronized CityDBManager getInstance() {
		return sInstance;
	}

	public void insertIntoCity(CityBean cityBean) {
//		SQLiteDatabase database = mDatabaseHelper.getReadableDatabase();
		SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase(Contants.PATH_DATABASES_FILE, null);
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
		log.d("start insert into city");
//		SQLiteDatabase database = mDatabaseHelper.getReadableDatabase();
		SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase(Contants.PATH_DATABASES_FILE, null);
		database.beginTransaction();
		long startTime = System.currentTimeMillis();
		Cursor cursor = database.query(CityListDatabaseHelper.TABLE_NAME_CITY, null, null, null, null, null, null);
		if(null != cursor && cursor.getCount()>0){
			return;
		}
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
		long endTime = System.currentTimeMillis();
		log.d("end insert into city");
		log.d("insert into database used time is "+ (endTime - startTime));
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
//		SQLiteDatabase database = mDatabaseHelper.getReadableDatabase();
		SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase(Contants.PATH_DATABASES_FILE, null);
		database.beginTransaction();
		Cursor cursor = database.query(CityListDatabaseHelper.TABLE_NAME_HOT_CITY, null, null, null, null, null, null);
		if(null != cursor && cursor.getCount()>0){
			database.setTransactionSuccessful();
			database.endTransaction();
			database.close();
			return;
		}
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
//		SQLiteDatabase database = mDatabaseHelper.getReadableDatabase();
		SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase(Contants.PATH_DATABASES_FILE, null);
		Cursor cursor = database.rawQuery(sql, null);
		if(null == cursor || cursor.getCount() <=0){
			return null;
		}
		cursor.moveToFirst();
		CityBean cityBean = new CityBean(cursor.getString(1), cursor.getString(2), cursor.getString(3), 
				cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7), 
				cursor.getString(8), cursor.getString(9));
		cursor.close();
		database.close();
		return cityBean;
	}

	/**
	 * 定位后的查询城市
	 * @param jsonObject
	 * @return
	 * @throws Exception
	 */
	public CityBean queryCityByLocation(JSONObject jsonObject) throws Exception{
		String district = jsonObject.getString(LocationUtil.NAME_DISTRICT);
		String cityName = jsonObject.getString(LocationUtil.NAME_CITY_NAME);
		if(district.endsWith("市") || district.endsWith("县") || district.endsWith("区")){
			if("浦东新区".equals(district) || "滨海新区".equals(district) || "呼市郊区".equals(district) 
					|| "尖草坪区".equals(district) || "小店区".equals(district) || "淮阴区".equals(district) 
					|| "淮安区".equals(district) || "黄山区".equals(district) || "黄山风景区".equals(district)
					|| "赫山区".equals(district) || "呼市郊区".equals(district) || "沙市".equals(district) 
					|| "津市".equals(district) || "芒市".equals(district)){
			}else{
				district = district.substring(0, district.length()-1);
			}
		}
		String sql = "select * from city where name_cn='" + district + "'";
//		SQLiteDatabase database = mDatabaseHelper.getReadableDatabase();
		SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase(Contants.PATH_DATABASES_FILE, null);
		Cursor cursor = database.rawQuery(sql, null);
		if(null == cursor || cursor.getCount() <=0){
			if(cityName.endsWith("市")){
				cityName = cityName.substring(0, cityName.length()-1);
			}
			log.d("cityName = "+cityName);
			String selectByCity = "select * from city where district_cn='" + cityName + "'";
			Cursor query = database.rawQuery(selectByCity, null);
			if(null != query && query.getCount() >0){
				log.d("query = "+query.toString());
				query.moveToFirst();
				CityBean cityBean = new CityBean(query.getString(1), query.getString(2), query.getString(3), 
						query.getString(4), query.getString(5), query.getString(6), query.getString(7), 
						query.getString(8), query.getString(9));
				query.close();
				cursor.close();
				database.close();
				log.d("cityBean = "+cityBean.toString());
				return cityBean;
			}
			return null;
		}
		cursor.moveToFirst();
		CityBean cityBean = new CityBean(cursor.getString(1), cursor.getString(2), cursor.getString(3), 
				cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7), 
				cursor.getString(8), cursor.getString(9));
		cursor.close();
		database.close();
		return cityBean;
	}
	
	public CityBean queryCityByAreaId(String area_id){
		String sql = "select * from city where area_id='" + area_id + "'";
//		SQLiteDatabase database = mDatabaseHelper.getReadableDatabase();
		SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase(Contants.PATH_DATABASES_FILE, null);
		Cursor cursor = database.rawQuery(sql, null);
		cursor.moveToFirst();
		CityBean cityBean = new CityBean(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getString(8));
		cursor.close();
		database.close();
		return cityBean;
	}
	
	public List<CityBean> queryCity(String cityName) throws Exception {
		List<CityBean> cityBeans = new ArrayList<CityBean>();
//		SQLiteDatabase database = mDatabaseHelper.getReadableDatabase();
		SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase(Contants.PATH_DATABASES_FILE, null);
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
	 * 
	 * @return
	 */
	public List<CityBean> queryHotCity(){
		List<CityBean> cityBeans = new ArrayList<CityBean>();
		String sql = "select "+CityListDatabaseHelper.TABLE_NAME_HOT_CITY+".area_id as area_id,name_en," 
				+CityListDatabaseHelper.TABLE_NAME_HOT_CITY+".name_cn as name_cn,district_en,district_cn,prov_en,prov_cn,nation_en,nation_cn from " 
				+ CityListDatabaseHelper.TABLE_NAME_HOT_CITY+","+CityListDatabaseHelper.TABLE_NAME_CITY+" where " 
				+ CityListDatabaseHelper.TABLE_NAME_HOT_CITY+".area_id="+CityListDatabaseHelper.TABLE_NAME_CITY+".area_id";
//		String sql = "select * from "+CityListDatabaseHelper.TABLE_NAME_HOT_CITY;
//		SQLiteDatabase database = mDatabaseHelper.getReadableDatabase();
		SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase(Contants.PATH_DATABASES_FILE, null);
		Cursor cursor = database.rawQuery(sql, null);
		while(cursor.moveToNext()){
			String area_id = cursor.getString(cursor.getColumnIndex("area_id"));
			String name_cn = cursor.getString(cursor.getColumnIndex("name_cn"));
			String name_en = cursor.getString(cursor.getColumnIndex("name_en"));
			String district_en = cursor.getString(cursor.getColumnIndex("district_en"));
			String district_cn = cursor.getString(cursor.getColumnIndex("district_cn"));
			String prov_en = cursor.getString(cursor.getColumnIndex("prov_en"));
			String prov_cn = cursor.getString(cursor.getColumnIndex("prov_cn"));
			String nation_en = cursor.getString(cursor.getColumnIndex("nation_en"));
			String nation_cn = cursor.getString(cursor.getColumnIndex("nation_cn"));
			CityBean cityBean = new CityBean();
			cityBean.setAreaId(area_id);
			cityBean.setNameCn(name_cn);
			cityBean.setNameEn(name_en);
			cityBean.setDistrictCn(district_cn);
			cityBean.setDistrictEn(district_en);
			cityBean.setNationCn(nation_cn);
			cityBean.setNationEn(nation_en);
			cityBean.setProvCn(prov_cn);
			cityBean.setProvEn(prov_en);
			cityBeans.add(cityBean);
		}
		cursor.close();
		database.close();
		return cityBeans;
	}
	
	public void insertIntoSaved(Context context,int cityId,String cityName){
		log.d("city_id="+cityId+",cityName="+cityName);
		SavedCityDatabaseHelper savedCityDatabaseHelper = new SavedCityDatabaseHelper(context);
		SQLiteDatabase database = savedCityDatabaseHelper.getReadableDatabase();
		Cursor cursor = database.query(SavedCityDatabaseHelper.NAME_SAVES_CITY, null, null, null, null, null, null);
		while (cursor.moveToNext()) {
			String area_id = cursor.getString(cursor.getColumnIndex("area_id"));
			if(Integer.parseInt(area_id) == cityId){
				log.d("area_id="+area_id);
				cursor.close();
				database.close();
				return;
			}
		}
		String sql = "insert into " + SavedCityDatabaseHelper.NAME_SAVES_CITY+ " (area_id,name_cn)" + "values ('" + cityId + "','" + cityName + "')";
		database.execSQL(sql);
		database.close();
	}
	
	/**
	 * 
	 * @return 没有返回的List大小为0
	 */
	public List<CityBean> querySavedCity(Context context){
		List<CityBean> cityBeans = new ArrayList<CityBean>();
			SavedCityDatabaseHelper savedCityDatabaseHelper = new SavedCityDatabaseHelper(context);
			log.d("savedCityDatabaseHelper = "+savedCityDatabaseHelper);
			SQLiteDatabase database = savedCityDatabaseHelper.getReadableDatabase();
			String sql = "select * from " + SavedCityDatabaseHelper.NAME_SAVES_CITY;
			Cursor cursor = database.rawQuery(sql, null);
			while(cursor.moveToNext()){
				String area_id = cursor.getString(cursor.getColumnIndex("area_id"));
				String name_cn = cursor.getString(cursor.getColumnIndex("name_cn"));
				CityBean cityBean = new CityBean();
				cityBean.setAreaId(area_id);
				cityBean.setNameCn(name_cn);
				cityBeans.add(cityBean);
			}
			cursor.close();
			database.close();
		return cityBeans;
	}
	
	public void deleteSavedCity(Context context,List<Integer> delectCities){
		SavedCityDatabaseHelper savedCityDatabaseHelper = new SavedCityDatabaseHelper(context);
		SQLiteDatabase database = savedCityDatabaseHelper.getReadableDatabase();
		database.beginTransaction();
		String sql = "delete from "+SavedCityDatabaseHelper.NAME_SAVES_CITY +" where area_id=";
		for(Integer cityId : delectCities){
			database.execSQL(sql+cityId);
		}
		database.setTransactionSuccessful();
		database.endTransaction();
		database.close();
	}
	
}
