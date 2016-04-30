package com.lzpnsd.sunshine.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import com.lzpnsd.sunshine.bean.WeatherInfoBean;
import com.lzpnsd.sunshine.db.CityListDatabaseHelper;
import com.lzpnsd.sunshine.util.LogUtil;
import com.lzpnsd.sunshine.util.WeatherUtil;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

public class CityWeatherInfoTest {

	private final LogUtil log = LogUtil.getLog(getClass());
	
	private Context context;
	private File mSuccessFile;
	private File mFailureFile;
	
	private static CityWeatherInfoTest instance;
	
	public static CityWeatherInfoTest getInstance(Context context){
		if(instance == null){
			 instance = new CityWeatherInfoTest(context);
		}
		return instance;
	}
	
	private CityWeatherInfoTest(Context context) {
		super();
		this.context = context;
		mSuccessFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+File.separator+"sunshine"+File.separator+"success.txt");
		mFailureFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+File.separator+"sunshine"+File.separator+"failure.txt");
		try {
			if(!mSuccessFile.getParentFile().exists()){
				mSuccessFile.getParentFile().mkdirs();
			}
			boolean createNewFile = mSuccessFile.createNewFile();
			log.d("createNewFile result = "+createNewFile);
			mFailureFile.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void test(){
		CityListDatabaseHelper databaseHelper = new CityListDatabaseHelper(context);
		SQLiteDatabase database = databaseHelper.getReadableDatabase();
		Cursor cursor = database.query(CityListDatabaseHelper.TABLE_NAME, new String[]{"area_id"}, null, null, null, null, null);
		while (cursor.moveToNext()) {
			mSuccessFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+File.separator+"sunshine"+File.separator+"success.txt");
			mFailureFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+File.separator+"sunshine"+File.separator+"failure.txt");
			final String cityId = cursor.getString(0);
			WeatherUtil.getInstance().getWeather(Integer.parseInt(cityId), new WeatherUtil.CallBack() {
				
				@Override
				public void onSuccess(List<WeatherInfoBean> weatherInfoBeans) {
					try {
						FileWriter fileWriter = new FileWriter(mSuccessFile,true);
						fileWriter.write("cityId = "+cityId+","+weatherInfoBeans.toString()+";\n");
						fileWriter.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
					
				}
				
				@Override
				public void onFailure(String result) {
					try {
						FileWriter fileWriter = new FileWriter(mFailureFile,true);
						fileWriter.write("cityId = "+cityId+";\n");
						fileWriter.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			});
		}
		
	}
	
	
}
