package com.lzpnsd.sunshine.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.lzpnsd.sunshine.SunshineApplication;
import com.lzpnsd.sunshine.bean.ShijingBean;
import com.lzpnsd.sunshine.contants.Contants;

public class ShijingUtil {

	private static final ShijingUtil mInstance = new ShijingUtil();
	
	private String mPath = Contants.URL_HOST+"/weather/getUserCityPictureList"; 
	private List<ShijingBean> shijingBeans = new ArrayList<ShijingBean>();
	
	public static ShijingUtil getInstance(){
		return mInstance;
	}
	
	private ShijingUtil(){
		
	}
	
	public void getShijingList(int cityId) {
		HttpURLConnectionManager connectionManager = HttpURLConnectionManager.getInstance(SunshineApplication.getContext().getMainLooper());
		HashMap<String, String> heard = new HashMap<String, String>();
		heard.put("cityId", cityId+"");
		connectionManager.post(mPath, heard, "", false, new HttpURLConnectionManager.RequestCallBack() {
			
			@Override
			public void onSuccess(String result) {
//				{"message":"查询成功","success":true,
//				"entity":[{"id":10,"cityId":1,"userId":1,"imgUrl":"1"},{"id":9,"cityId":1,"userId":1,"imgUrl":"1"}]}
				try {
					 JSONObject jsonObject = new JSONObject(result);
					 JSONArray jsonArray = jsonObject.getJSONArray("entity");
					 int length = jsonArray.length();
					 shijingBeans.clear();
					 for(int i = 0;i < length;i++){
						 JSONObject object = (JSONObject) jsonArray.get(i);
						 int id = object.getInt("cityId");
						 String imgUrl = object.getString("imgUrl");
						 ShijingBean shijingBean = new ShijingBean(id, imgUrl);
						 shijingBeans.add(shijingBean);
					 }
				} catch (JSONException e) {
					shijingBeans.clear();
				}
			}
			
			@Override
			public void onFailure(String response) {
				shijingBeans.clear();
			}
		});
	}

	public List<ShijingBean> getShijingBeans() {
		return shijingBeans;
	}
}
