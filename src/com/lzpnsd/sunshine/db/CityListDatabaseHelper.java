package com.lzpnsd.sunshine.db;

import com.lzpnsd.sunshine.util.LogUtil;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CityListDatabaseHelper extends SQLiteOpenHelper {

	private LogUtil log = LogUtil.getLog(getClass());

	public static final String DATABASE_NAME = "sunshine.db";
	public static final String TABLE_NAME_CITY = "city";
	public static final String TABLE_NAME_HOT_CITY = "hot_city";
	private static final int VERSION = 1;

//	private final int BUFFER_SIZE = 4*1024;
//	private final String DATABASE_PATH = "/data" + Environment.getDataDirectory().getAbsolutePath() + File.separator
//			+ SunshineApplication.getContext().getPackageName() + File.separator + "databases";

	public CityListDatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		log.d("onCreate,thread = " + Thread.currentThread());
//		File file = new File(DATABASE_PATH);
//		if(!file.exists()){
//			try {
//				InputStream inputStream = SunshineApplication.getContext().getResources().openRawResource(R.raw.sunshine);
//				FileOutputStream fos = new FileOutputStream(file);
//				byte[] buffer = new byte[BUFFER_SIZE];
//				int len = 0;
//				while((len = inputStream.read(buffer)) >0){
//					fos.write(buffer, 0, len);
//				}
//				inputStream.close();
//				fos.close();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
		String CitySql = "create table if not exists " + TABLE_NAME_CITY + " (" 
				+ "_id integer primary key autoincrement,"
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
		String hotCitySql = "create table if not exists "+ TABLE_NAME_HOT_CITY + " (" 
				+ "_id integer primary key autoincrement,"
				+ "area_id text,"
				+")";
		db.execSQL(CitySql);
		db.execSQL(hotCitySql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		log.d("onUpgrade");
	}

}
