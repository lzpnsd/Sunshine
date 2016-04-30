package com.lzpnsd.sunshine.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CityListDatabaseHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "sunshine.db";
	public static final String TABLE_NAME = "city";
	private static final int VERSION = 1;
	
	public CityListDatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String sql = "create table if not exists "+ TABLE_NAME + " (_id integer primary key autoincrement,"
				+ "area_id text,"
				+ "name_en text,"
				+ "name_cn text,"
				+ "district_en text,"
				+ "district_cn text,"
				+ "prov_en text,"
				+ "prov_cn text,"
				+ "nation_en text,"
				+ "nation_cn text"
				+ ")";
		db.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

}
