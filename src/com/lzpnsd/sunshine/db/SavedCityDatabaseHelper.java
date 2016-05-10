package com.lzpnsd.sunshine.db;

import com.lzpnsd.sunshine.contants.Contants;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SavedCityDatabaseHelper extends SQLiteOpenHelper {

	private static final String NAME_SAVES_CITY = "saved_city"; 
	
	public SavedCityDatabaseHelper(Context context) {
		super(context, NAME_SAVES_CITY, null, Contants.DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}

}
