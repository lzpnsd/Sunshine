package com.lzpnsd.sunshine.db;

import com.lzpnsd.sunshine.bean.CityBean;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class DatabaseManager {

	private Context mContext;

	public DatabaseManager(Context context) {
		this.mContext = context;
	}
	
	public void insert(CityBean cityBean){
		CityListDatabaseHelper databaseHelper = new CityListDatabaseHelper(mContext);
		SQLiteDatabase database = databaseHelper.getReadableDatabase();
		String sql = "insert into "+CityListDatabaseHelper.TABLE_NAME + " (area_id,name_en,name_cn,district_en,"
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
	}
	
}
