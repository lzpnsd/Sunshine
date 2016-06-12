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
import com.lzpnsd.sunshine.manager.DataManager;

public class ShijingUtil {

	private final LogUtil log = LogUtil.getLog(getClass());

	private static final ShijingUtil mInstance = new ShijingUtil();

	private String mPath = Contants.URL_HOST + "/weather/getUserCityPictureList";
	private List<ShijingBean> shijingBeans = new ArrayList<ShijingBean>();

	public static ShijingUtil getInstance() {
		return mInstance;
	}

	private ShijingUtil() {

	}

	public void getShijingList(final CallBack callBack) {
		int cityId = DataManager.getInstance().getCurrentCityId();
		HttpURLConnectionManager connectionManager = HttpURLConnectionManager
				.getInstance(SunshineApplication.getContext().getMainLooper());
		HashMap<String, String> heard = new HashMap<String, String>();
//		heard.put("cityId", cityId + "");
//		String path = mPath+"?cityId="+cityId;
//		log.d("path = " + path);
//		connectionManager.get(mPath+"?cityId="+cityId, new HttpURLConnectionManager.RequestCallBack() {
//
//			@Override
//			public void onSuccess(String result) {
//				// {"message":"查询成功","success":true,
//				// "entity":[{"id":10,"cityId":1,"userId":1,"imgUrl":"1"},{"id":9,"cityId":1,"userId":1,"imgUrl":"1"}]}
//				try {
//					log.d("result = " + result);
//					JSONObject jsonObject = new JSONObject(result);
//					// 由于返回的字符串中有反斜杠，需要先转换成String才能解析成JSONObject
//					String responseBody = jsonObject.getString("ResponseBody");
//					JSONArray jsonArray = new JSONObject(responseBody).getJSONArray("entity");
//					int length = jsonArray.length();
//					shijingBeans.clear();
//					for (int i = 0; i < length; i++) {
//						JSONObject object = (JSONObject) jsonArray.get(i);
//						int id = object.getInt("cityId");
//						String imgUrl = object.getString("imgUrl");
//						String location = object.getString("describe");
//						ShijingBean shijingBean = new ShijingBean(id, imgUrl, location);
//						shijingBeans.add(shijingBean);
//					}
//					log.d("shijingBeans = "+shijingBeans.toString());
//					callBack.onSuccess(shijingBeans);
//				} catch (JSONException e) {
//					shijingBeans.clear();
//					callBack.onFailure(e.getMessage());
//				}
//			}
//
//			@Override
//			public void onFailure(String response) {
//				shijingBeans.clear();
//				callBack.onFailure(response);
//			}
//		});
		connectionManager.post(mPath, heard, "cityId="+cityId, false, new HttpURLConnectionManager.RequestCallBack() {

			@Override
			public void onSuccess(String result) {
				// {"message":"查询成功","success":true,
				// "entity":[{"id":10,"cityId":1,"userId":1,"imgUrl":"1"},{"id":9,"cityId":1,"userId":1,"imgUrl":"1"}]}
				try {
					log.d("result = " + result);
					JSONObject jsonObject = new JSONObject(result);
					// 由于返回的字符串中有反斜杠，需要先转换成String才能解析成JSONObject
					String responseBody = jsonObject.getString("ResponseBody");
					JSONArray jsonArray = new JSONObject(responseBody).getJSONArray("entity");
					int length = jsonArray.length();
					shijingBeans.clear();
					for (int i = 0; i < length; i++) {
						JSONObject object = (JSONObject) jsonArray.get(i);
						int id = object.getInt("cityId");
						String imgUrl = object.getString("imgUrl");
						String location = object.getString("describe");
						ShijingBean shijingBean = new ShijingBean(id, imgUrl, location);
						shijingBeans.add(shijingBean);
					}
					log.d("shijingBeans = "+shijingBeans.toString());
					callBack.onSuccess(shijingBeans);
				} catch (JSONException e) {
					shijingBeans.clear();
					callBack.onFailure(e.getMessage());
				}
			}

			@Override
			public void onFailure(String response) {
				shijingBeans.clear();
				callBack.onFailure(response);
			}
		});
	}

	public interface CallBack {
		void onSuccess(List<ShijingBean> shijingBeans);

		void onFailure(String result);
	}

}
