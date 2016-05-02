package com.lzpnsd.sunshine.db;

import com.lzpnsd.sunshine.bean.CityBean;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class CityDBManager {

	private Context mContext;
	private CityListDatabaseHelper mDatabaseHelper;
	public CityDBManager(Context context) {
		this.mContext = context;
		mDatabaseHelper = new CityListDatabaseHelper(context);
	}
	
	public void insertIntoCity(CityBean cityBean){
		SQLiteDatabase database = mDatabaseHelper.getReadableDatabase();
		String sql = "insert into "+CityListDatabaseHelper.TABLE_NAME_CITY + " (area_id,name_en,name_cn,district_en,"
				+ "district_cn,prov_en,prov_cn,nation_en,nation_cn) values ('"
				+ cityBean.getAreaId() +"','"
				+ cityBean.getNameEn() +"','"
				+ cityBean.getNameCn() +"','"
				+ cityBean.getDistrictEn() +"','"
				+ cityBean.getDistrictCn() +"','"
				+ cityBean.getProvEn() +"','"
				+ cityBean.getProvCn() +"','"
				+ cityBean.getNationEn() +"','"
				+ cityBean.getNationCn() +"'"
				+ ")";
		database.execSQL(sql);
		database.close();
	}
	
	public void insertIntoHotCity(){
//		select * from city where name_cn='北京' or name_cn='天津' or name_cn='上海' or name_cn='重庆' or name_cn='沈阳' or name_cn='大连' or name_cn='长春' 
//				or name_cn='哈尔滨' or name_cn='郑州' or name_cn='武汉' or name_cn='长沙' or name_cn='广州' or name_cn='深圳' or name_cn='南京';
		String[] hotCities = new String[]{"101010100","101020100","101030100","101040100","101050101",
				"101060101","101070101","101070201","101180101","101190101",
				"101200101","101250101","101280101","101280601"};
		SQLiteDatabase database = mDatabaseHelper.getReadableDatabase();
		for(int i = 0;i < 14;i++){
			String sql = "insert into "+CityListDatabaseHelper.TABLE_NAME_HOT_CITY +" (area_id)"
					+ "values ('"+hotCities[i]+"')";
			database.execSQL(sql);
		}
		database.close();
	}
	
}
