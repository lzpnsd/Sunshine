package com.lzpnsd.sunshine.db;

import com.lzpnsd.sunshine.contants.Contants;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SavedCityDatabaseHelper extends SQLiteOpenHelper {

	public static final String NAME_SAVES_CITY = "saved_city"; 
	
	public SavedCityDatabaseHelper(Context context) {
		super(context, "saved_city.db", null, Contants.DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String sql = "create table if not exists "+NAME_SAVES_CITY +" ("
				+ "_id integer primary key autoincrement,"
				+ "area_id text,"
				+ "name_cn text"
				+")";
		db.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}

}
