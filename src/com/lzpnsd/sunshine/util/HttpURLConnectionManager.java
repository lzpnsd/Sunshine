package com.lzpnsd.sunshine.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

public class HttpURLConnectionManager {

	private static final HttpURLConnectionManager mHttpURLConnecyionManager = new HttpURLConnectionManager();

	private int count;

	private ExecutorService executor;

	private static final String TAG = "HttpURLConnectionManager";

	public static final int RESULT_CODE_IOEXCEPTION = 10010;
	public static final int RESULT_CODE_JSONEXCEPTION = 10011;

	private final static int MESSAGE_CODE_SUCCESS = 1110;
	private static final int MESSAGE_CODE_FAILURE = 1111;
	private final static int MESSAGE_CODE_DOWNLOADING = 1112;
	private final static int MESSAGE_TYPE_ASYNC_REQUEST = 100;
	private final static int MESSAGE_TYPE_ASYNC_DOWNLOAD = 101;

	private static MyHandler mHandler;
	
	private HttpURLConnectionManager() {
		count = Runtime.getRuntime().availableProcessors() * 3 + 2;
		Log.i(TAG, "process.size()=" + count);
		executor = Executors.newFixedThreadPool(count);
	}

	public static HttpURLConnectionManager getInstance(Looper looper) {
		if(mHandler==null){
			mHandler = new MyHandler(looper); 
		}
		return mHttpURLConnecyionManager;
	}

	/**
	 * GET请求
	 * 
	 * @param path
	 *            请求路径
	 * @param timeoutMillis
	 *            请求连接超时
	 * @param isInstanceFollowRedirects
	 *            是否重定向
	 * @param params
	 *            请求参数
	 * @param callBack
	 *            请求返回结果后的回调接口，只有正确返回时会调用onSuccess()方法，其他情况调用onFailure()方法
	 */
	public void get(final String path, final int timeoutMillis, final boolean isInstanceFollowRedirects, 
			final HashMap<String, Object> params, final RequestCallBack callBack) {
		executor.execute(new Runnable() {

			@Override
			public void run() {
				HttpURLConnection conn = null;
				BufferedReader reader = null;
				StringBuilder sb = new StringBuilder();
				JSONObject json = new JSONObject();
				try {
					conn = getConnectionGet(path, timeoutMillis, isInstanceFollowRedirects, params, sb);
					reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
					parseResult(callBack, conn, reader, json);
				} catch (IOException e) {
					Log.e(TAG, e.getMessage());
					try {
						json.put("ResultCode", RESULT_CODE_IOEXCEPTION);
						json.put("ResultDesc", e.getMessage());
						json.put("ResponseBody", "");
						callBack.onFailure(json.toString());
					} catch (JSONException e1) {
						Log.e(TAG, e1.getMessage());
					}
				} catch (JSONException e) {
					try {
						json.put("ResultCode", RESULT_CODE_IOEXCEPTION);
						json.put("ResultDesc", e.getMessage());
						json.put("ResponseBody", "");
						callBack.onFailure(json.toString());
					} catch (JSONException e1) {
						Log.e(TAG, e1.getMessage());
					}
				} finally {
					try {
						if (conn != null) {
							conn.disconnect();
						}
						if (reader != null) {
							reader.close();
						}
					} catch (IOException e) {
						Log.d(TAG, e.getMessage());
					}
				}
			}
		});
	}

	/**
	 * GET请求
	 * 
	 * @param path
	 *            请求路径
	 * @param timeoutMillis
	 *            请求连接超时
	 * @param isInstanceFollowRedirects
	 *            是否重定向
	 * @param callBack
	 *            请求返回结果后的回调接口，只有正确返回时会调用onSuccess()方法，其他情况调用onFailure()方法
	 */
	public void get(String path, int timeoutMillis, boolean isInstanceFollowRedirects, RequestCallBack callBack) {
		get(path, timeoutMillis, isInstanceFollowRedirects, new HashMap<String, Object>(), callBack);
	}

	/**
	 * GET请求
	 * 
	 * @param path
	 *            请求路径
	 * @param isInstanceFollowRedirects
	 *            是否重定向
	 * @param params
	 *            请求参数
	 * @param callBack
	 *            请求返回结果后的回调接口，只有正确返回时会调用onSuccess()方法，其他情况调用onFailure()方法
	 */
	public void get(String path, boolean isInstanceFollowRedirects, HashMap<String, Object> params, RequestCallBack callBack) {
		get(path, 5000, isInstanceFollowRedirects, params, callBack);
	}

	/**
	 * GET请求
	 * 
	 * @param path
	 *            请求路径
	 * @param params
	 *            请求参数
	 * @param callBack
	 *            请求返回结果后的回调接口，只有正确返回时会调用onSuccess()方法，其他情况调用onFailure()方法
	 */
	public void get(String path, HashMap<String, Object> params, RequestCallBack callBack) {
		get(path, 5000, false, params, callBack);
	}

	/**
	 * GET请求
	 * 
	 * @param path
	 *            请求路径
	 * @param connectTimeout
	 *            请求连接超时
	 * @param params
	 *            请求参数
	 * @param callBack
	 *            请求返回结果后的回调接口，只有正确返回时会调用onSuccess()方法，其他情况调用onFailure()方法
	 */
	public void get(String path, int connectTimeout, HashMap<String, Object> params, RequestCallBack callBack) {
		get(path, connectTimeout, false, params, callBack);
	}

	/**
	 * GET请求
	 * 
	 * @param path
	 *            请求路径
	 * @param connectTimeout
	 *            请求连接超时
	 * @param callBack
	 *            请求返回结果后的回调接口，只有正确返回时会调用onSuccess()方法，其他情况调用onFailure()方法
	 */
	public void get(String path, int connectTimeout, RequestCallBack callBack) {
		get(path, connectTimeout, false, new HashMap<String, Object>(), callBack);
	}

	/**
	 * GET请求
	 * 
	 * @param path
	 *            请求路径
	 * @param callBack
	 *            请求返回结果后的回调接口，只有正确返回时会调用onSuccess()方法，其他情况调用onFailure()方法
	 */
	public void get(String path, RequestCallBack callBack) {
		get(path, 5000, false, new HashMap<String, Object>(), callBack);
	}

	/**
	 * 异步GET请求
	 * 
	 * @param path
	 *            请求路径
	 * @param timeoutMillis
	 *            请求连接超时
	 * @param isInstanceFollowRedirects
	 *            是否重定向
	 * @param params
	 *            请求参数
	 * @param callBack
	 *            请求返回结果后的回调接口，只有正确返回时会调用onSuccess()方法，其他情况调用onFailure()方法
	 */
	public void getAsync(final String path, final int timeoutMillis, final boolean isInstanceFollowRedirects, 
			final HashMap<String, Object> params, final RequestCallBack callBack) {
		executor.execute(new Runnable() {

			@Override
			public void run() {
				HttpURLConnection conn = null;
				BufferedReader reader = null;
				StringBuilder sb = new StringBuilder();
				JSONObject json = new JSONObject();
				Message msg = mHandler.obtainMessage();
				msg.arg1 = MESSAGE_TYPE_ASYNC_REQUEST;
				MessageObj obj = new MessageObj();
				try {
					conn = getConnectionGet(path, timeoutMillis, isInstanceFollowRedirects, params, sb);
					reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
					parseAsyncResult(callBack, conn, reader, json, msg, obj, sb);
				} catch (IOException e) {
					Log.e(TAG, e.getMessage());
					try {
						json.put("ResultCode", RESULT_CODE_IOEXCEPTION);
						json.put("ResultDesc", e.getMessage());
						json.put("ResponseBody", "");
						msg.what = MESSAGE_CODE_FAILURE;
						obj.callBack = callBack;
						obj.result = json.toString();
						msg.obj = obj;
						mHandler.sendMessage(msg);
					} catch (JSONException e1) {
						Log.e(TAG, e1.getMessage());
					}
				} catch (JSONException e) {
					try {
						json.put("ResultCode", RESULT_CODE_IOEXCEPTION);
						json.put("ResultDesc", e.getMessage());
						json.put("ResponseBody", "");
						msg.what = MESSAGE_CODE_FAILURE;
						obj.callBack = callBack;
						obj.result = json.toString();
						msg.obj = obj;
						mHandler.sendMessage(msg);
					} catch (JSONException e1) {
						Log.e(TAG, e1.getMessage());
					}
				} finally {
					try {
						if (conn != null) {
							conn.disconnect();
						}
						if (reader != null) {
							reader.close();
						}
					} catch (IOException e) {
						Log.d(TAG, e.getMessage());
					}
				}
			}
		});
	}

	/**
	 * 异步GET请求
	 * 
	 * @param path
	 *            请求路径
	 * @param timeoutMillis
	 *            请求连接超时
	 * @param isInstanceFollowRedirects
	 *            是否重定向
	 * @param params
	 *            请求参数
	 * @param callBack
	 *            请求返回结果后的回调接口，只有正确返回时会调用onSuccess()方法，其他情况调用onFailure()方法
	 */
	public void getAsyncWeather(final String path, final HashMap<String, Object> params, final RequestCallBack callBack) {
		executor.execute(new Runnable() {
			
			@Override
			public void run() {
				HttpURLConnection conn = null;
				BufferedReader reader = null;
				StringBuilder sb = new StringBuilder();
				JSONObject json = new JSONObject();
				Message msg = mHandler.obtainMessage();
				msg.arg1 = MESSAGE_TYPE_ASYNC_REQUEST;
				MessageObj obj = new MessageObj();
				try {
					conn = getConnectionGet(path, 5000, false, params, sb);
					reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
					parseAsyncResult(callBack, conn, reader, json, msg, obj, sb);
				} catch (IOException e) {
					Log.e(TAG, e.getMessage());
					try {
						json.put("ResultCode", RESULT_CODE_IOEXCEPTION);
						json.put("ResultDesc", e.getMessage());
						json.put("ResponseBody", "");
						msg.what = MESSAGE_CODE_FAILURE;
						obj.callBack = callBack;
						obj.result = json.toString();
						msg.obj = obj;
						mHandler.sendMessage(msg);
					} catch (JSONException e1) {
						Log.e(TAG, e1.getMessage());
					}
				} catch (JSONException e) {
					try {
						json.put("ResultCode", RESULT_CODE_IOEXCEPTION);
						json.put("ResultDesc", e.getMessage());
						json.put("ResponseBody", "");
						msg.what = MESSAGE_CODE_FAILURE;
						obj.callBack = callBack;
						obj.result = json.toString();
						msg.obj = obj;
						mHandler.sendMessage(msg);
					} catch (JSONException e1) {
						Log.e(TAG, e1.getMessage());
					}
				} finally {
					try {
						if (conn != null) {
							conn.disconnect();
						}
						if (reader != null) {
							reader.close();
						}
					} catch (IOException e) {
						Log.d(TAG, e.getMessage());
					}
				}
			}
		});
	}

	
	
	/**
	 * 异步GET请求
	 * 
	 * @param path
	 *            请求路径
	 * @param timeoutMillis
	 *            请求连接超时
	 * @param isInstanceFollowRedirects
	 *            是否重定向
	 * @param callBack
	 *            请求返回结果后的回调接口，只有正确返回时会调用onSuccess()方法，其他情况调用onFailure()方法
	 */
	public void getAsync(String path, int timeoutMillis, boolean isInstanceFollowRedirects, RequestCallBack callBack) {
		getAsync(path, timeoutMillis, isInstanceFollowRedirects, new HashMap<String, Object>(), callBack);
	}

	/**
	 * 异步GET请求
	 * 
	 * @param path
	 *            请求路径
	 * @param isInstanceFollowRedirects
	 *            是否重定向
	 * @param params
	 *            请求参数
	 * @param callBack
	 *            请求返回结果后的回调接口，只有正确返回时会调用onSuccess()方法，其他情况调用onFailure()方法
	 */
	public void getAsync(String path, boolean isInstanceFollowRedirects, HashMap<String, Object> params, 
			RequestCallBack callBack) {
		getAsync(path, 5000, isInstanceFollowRedirects, params, callBack);
	}

	/**
	 * 异步GET请求
	 * 
	 * @param path
	 *            请求路径
	 * @param params
	 *            请求参数
	 * @param callBack
	 *            请求返回结果后的回调接口，只有正确返回时会调用onSuccess()方法，其他情况调用onFailure()方法
	 */
	public void getAsync(String path, HashMap<String, Object> params, RequestCallBack callBack) {
		getAsync(path, 5000, false, params, callBack);
	}

	/**
	 * 异步GET请求
	 * 
	 * @param path
	 *            请求路径
	 * @param connectTimeout
	 *            请求连接超时
	 * @param params
	 *            请求参数
	 * @param callBack
	 *            请求返回结果后的回调接口，只有正确返回时会调用onSuccess()方法，其他情况调用onFailure()方法
	 */
	public void getAsync(String path, int connectTimeout, HashMap<String, Object> params, RequestCallBack callBack) {
		getAsync(path, connectTimeout, false, params, callBack);
	}

	/**
	 * 异步GET请求
	 * 
	 * @param path
	 *            请求路径
	 * @param connectTimeout
	 *            请求连接超时
	 * @param callBack
	 *            请求返回结果后的回调接口，只有正确返回时会调用onSuccess()方法，其他情况调用onFailure()方法
	 */
	public void getAsync(String path, int connectTimeout, RequestCallBack callBack) {
		getAsync(path, connectTimeout, false, new HashMap<String, Object>(), callBack);
	}

	/**
	 * 异步GET请求
	 * 
	 * @param path
	 *            请求路径
	 * @param callBack
	 *            请求返回结果后的回调接口，只有正确返回时会调用onSuccess()方法，其他情况调用onFailure()方法
	 */
	public void getAsync(String path, RequestCallBack callBack) {
		getAsync(path, 5000, false, new HashMap<String, Object>(), callBack);
	}

	/**
	 * POST请求
	 * 
	 * @param path
	 *            请求路径
	 * @param timeoutMillis
	 *            请求连接超时
	 * @param headers
	 *            请求消息头
	 * @param requestBody
	 *            请求消息体
	 * @param callBack
	 *            请求返回结果后的回调接口，只有正确返回时会调用onSuccess()方法，其他情况调用onFailure()方法
	 */
	public void post(final String path, final int timeoutMillis, final HashMap<String, String> headers, final boolean isUseCaches,
			final String requestBody, final RequestCallBack callBack) {
		executor.execute(new Runnable() {

			@Override
			public void run() {
				HttpURLConnection conn = null;
				PrintWriter printWriter = null;
				BufferedReader bufferedReader = null;
				JSONObject json = new JSONObject();
				try {
					conn = getConnectionPost(path, timeoutMillis, headers,isUseCaches);
					printWriter = new PrintWriter(conn.getOutputStream());
					printWriter.write(requestBody);
					printWriter.flush();
					bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
					parseResult(callBack, conn, bufferedReader, json);
				} catch (IOException e) {
					Log.e(TAG, e.getMessage());
					try {
						json.put("ResultCode", RESULT_CODE_IOEXCEPTION);
						json.put("ResultDesc", e.getMessage());
						json.put("ResponseBody", "");
						callBack.onFailure(json.toString());
					} catch (JSONException e1) {
						Log.e(TAG, e1.getMessage());
					}
				} catch (JSONException e) {
					Log.e(TAG, e.getMessage());
					try {
						json.put("ResultCode", RESULT_CODE_JSONEXCEPTION);
						json.put("ResultDesc", e.getMessage());
						json.put("ResponseBody", "");
						callBack.onFailure(json.toString());
					} catch (JSONException e1) {
						Log.e(TAG, e1.getMessage());
					}
				} finally {
					try {
						if (conn != null) {
							conn.disconnect();
						}
						if (printWriter != null) {
							printWriter.close();
						}
						if (bufferedReader != null) {
							bufferedReader.close();
						}
					} catch (IOException e) {
						Log.d(TAG, e.getMessage());
					}
				}
			}
		});
	}

	/**
	 * POST请求
	 * 
	 * @param path
	 *            请求路径
	 * @param headers
	 *            请求消息头
	 * @param requestBody
	 *            请求消息体
	 * @param callBack
	 *            请求返回结果后的回调接口，只有正确返回时会调用onSuccess()方法，其他情况调用onFailure()方法
	 */
	public void post(String path, HashMap<String, String> headers, String requestBody,boolean isUseCaches, RequestCallBack callBack) {
		post(path, 5000, headers, isUseCaches, requestBody, callBack);
	}

	/**
	 * POST请求
	 * 
	 * @param path
	 *            请求路径
	 * @param timeoutMillis
	 *            请求连接超时
	 * @param requestBody
	 *            请求消息体
	 * @param callBack
	 *            请求返回结果后的回调接口，只有正确返回时会调用onSuccess()方法，其他情况调用onFailure()方法
	 */
	public void post(String path, int timeoutMillis, String requestBody, RequestCallBack callBack) {
		post(path, timeoutMillis, new HashMap<String, String>(), false, requestBody, callBack);
	}

	/**
	 * POST请求
	 * 
	 * @param path
	 *            请求路径
	 * @param requestBody
	 *            请求消息体
	 * @param callBack
	 *            请求返回结果后的回调接口，只有正确返回时会调用onSuccess()方法，其他情况调用onFailure()方法
	 */
	public void post(String path, String requestBody, RequestCallBack callBack) {
		post(path, 5000, new HashMap<String, String>(), false,requestBody, callBack);
	}

	/**
	 * 异步POST请求
	 * 
	 * @param path
	 *            请求路径
	 * @param timeoutMillis
	 *            请求连接超时
	 * @param headers
	 *            请求消息头
	 * @param requestBody
	 *            请求消息体
	 * @param callBack
	 *            请求返回结果后的回调接口，只有正确返回时会调用onSuccess()方法，其他情况调用onFailure()方法
	 */
	public void postAsync(final String path, final int timeoutMillis, final HashMap<String, String> headers, final boolean isUseCaches,
			final String requestBody, final RequestCallBack callBack) {
		executor.execute(new Runnable() {

			@Override
			public void run() {
				HttpURLConnection conn = null;
				PrintWriter printWriter = null;
				BufferedReader bufferedReader = null;
				JSONObject json = new JSONObject();
				Message msg = mHandler.obtainMessage();
				MessageObj obj = new MessageObj();
				msg.arg1 = MESSAGE_TYPE_ASYNC_REQUEST;
				try {
					conn = getConnectionPost(path, timeoutMillis, headers,isUseCaches);
					printWriter = new PrintWriter(conn.getOutputStream());
					printWriter.write(requestBody);
					printWriter.flush();
					StringBuilder sb = new StringBuilder();
					bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
					parseAsyncResult(callBack, conn, bufferedReader, json, msg, obj, sb);
				} catch (IOException e) {
					Log.e(TAG, e.getMessage());
					try {
						json.put("ResultCode", RESULT_CODE_IOEXCEPTION);
						json.put("ResultDesc", e.getMessage());
						json.put("ResponseBody", "");
						msg.what = MESSAGE_CODE_FAILURE;
						obj.callBack = callBack;
						obj.result = json.toString();
						msg.obj = obj;
						mHandler.sendMessage(msg);
					} catch (JSONException e1) {
						Log.e(TAG, e1.getMessage());
					}
				} catch (JSONException e) {
					Log.e(TAG, e.getMessage());
					try {
						json.put("ResultCode", RESULT_CODE_JSONEXCEPTION);
						json.put("ResultDesc", e.getMessage());
						json.put("ResponseBody", "");
						msg.what = MESSAGE_CODE_FAILURE;
						obj.callBack = callBack;
						obj.result = json.toString();
						msg.obj = obj;
						mHandler.sendMessage(msg);
					} catch (JSONException e1) {
						Log.e(TAG, e1.getMessage());
					}
				} finally {
					try {
						if (conn != null) {
							conn.disconnect();
						}
						if (printWriter != null) {
							printWriter.close();
						}
						if (bufferedReader != null) {
							bufferedReader.close();
						}
					} catch (IOException e) {
						Log.d(TAG, e.getMessage());
					}
				}
			}
		});
	}

	/**
	 * 异步POST请求
	 * 
	 * @param path
	 *            请求路径
	 * @param headers
	 *            请求消息头
	 * @param requestBody
	 *            请求消息体
	 * @param callBack
	 *            请求返回结果后的回调接口，只有正确返回时会调用onSuccess()方法，其他情况调用onFailure()方法
	 */
	public void postAsync(String path, HashMap<String, String> headers, String requestBody,boolean isUseCaches, RequestCallBack callBack) {
		postAsync(path, 5000, headers,isUseCaches, requestBody, callBack);
	}

	/**
	 * 异步POST请求
	 * 
	 * @param path
	 *            请求路径
	 * @param timeoutMillis
	 *            请求连接超时
	 * @param requestBody
	 *            请求消息体
	 * @param callBack
	 *            请求返回结果后的回调接口，只有正确返回时会调用onSuccess()方法，其他情况调用onFailure()方法
	 */
	public void postAsync(String path, int timeoutMillis, String requestBody, RequestCallBack callBack) {
		postAsync(path, timeoutMillis, new HashMap<String, String>(),false, requestBody, callBack);
	}

	/**
	 * 异步POST请求
	 * 
	 * @param path
	 *            请求路径
	 * @param requestBody
	 *            请求消息体
	 * @param callBack
	 *            请求返回结果后的回调接口，只有正确返回时会调用onSuccess()方法，其他情况调用onFailure()方法
	 */
	public void postAsync(String path, String requestBody, RequestCallBack callBack) {
		postAsync(path, 5000, new HashMap<String, String>(),false, requestBody, callBack);
	}

	/**
	 * 使用GET方式下载文件
	 * 
	 * @param path
	 *            要下载文件的URL
	 * @param timeoutMillis
	 *            连接超时
	 * @param isInstanceFollowRedirects
	 *            是否重定向
	 * @param params
	 *            需要携带的参数
	 * @param bufferLength
	 *            写文件时的字节长度
	 * @param filePath
	 *            要保存的文件路径
	 * @param callBack
	 *            下载文件请求返回的回调接口
	 */
	public void getDownloadFile(final String path, final int timeoutMillis, final boolean isInstanceFollowRedirects, 
			final HashMap<String, Object> params, final int bufferLength, final String filePath, final RequestCallBack callBack) {
		executor.execute(new Runnable() {

			@Override
			public void run() {

				HttpURLConnection conn = null;
				StringBuilder sb = new StringBuilder();
				JSONObject json = new JSONObject();
				FileOutputStream fileOutputStream = null;
				BufferedInputStream inputStream = null;
				try {
					conn = getConnectionGet(path, timeoutMillis, isInstanceFollowRedirects, params, sb);
					inputStream = new BufferedInputStream(conn.getInputStream());
					fileOutputStream = downloadFile(bufferLength, filePath, callBack, conn, json, fileOutputStream, inputStream);
				} catch (IOException e) {
					Log.e(TAG, e.getMessage());
					try {
						json.put("ResultCode", RESULT_CODE_IOEXCEPTION);
						json.put("ResultDesc", e.getMessage());
						json.put("ResponseBody", "");
						callBack.onFailure(json.toString());
					} catch (JSONException e1) {
						Log.e(TAG, e1.getMessage());
					}
				} catch (JSONException e) {
					Log.e(TAG, e.getMessage());
					try {
						json.put("ResultCode", RESULT_CODE_JSONEXCEPTION);
						json.put("ResultDesc", e.getMessage());
						json.put("ResponseBody", "");
						callBack.onFailure(json.toString());
					} catch (JSONException e1) {
						Log.e(TAG, e1.getMessage());
					}
				} finally {
					try {
						if (conn != null) {
							conn.disconnect();
						}
						if (fileOutputStream != null) {
							fileOutputStream.close();
						}
						if (inputStream != null) {
							inputStream.close();
						}
					} catch (IOException e2) {
						Log.e(TAG, e2.getMessage());
					}
				}
			}
		});
	}

	/**
	 * 使用GET方式下载文件
	 * 
	 * @param path
	 *            要下载文件的URL
	 * @param isInstanceFollowRedirects
	 *            是否重定向
	 * @param params
	 *            需要携带的参数
	 * @param bufferLength
	 *            写文件时的字节长度
	 * @param filePath
	 *            要保存的文件路径
	 * @param callBack
	 *            下载文件请求返回的回调接口
	 */
	public void getDownloadFile(final String path, final boolean isInstanceFollowRedirects, 
			final HashMap<String, Object> params, final int bufferLength, final String filePath, final RequestCallBack callBack) {
		getDownloadFile(path, 5000, isInstanceFollowRedirects, params, bufferLength, filePath, callBack);
	}

	/**
	 * 使用GET方式下载文件
	 * 
	 * @param path
	 *            要下载文件的URL
	 * @param timeoutMillis
	 *            连接超时
	 * @param params
	 *            需要携带的参数
	 * @param bufferLength
	 *            写文件时的字节长度
	 * @param filePath
	 *            要保存的文件路径
	 * @param callBack
	 *            下载文件请求返回的回调接口
	 */
	public void getDownloadFile(final String path, final int timeoutMillis, final HashMap<String, Object> params, 
			final int bufferLength, final String filePath, final RequestCallBack callBack) {
		getDownloadFile(path, timeoutMillis, false, params, bufferLength, filePath, callBack);
	}

	/**
	 * 使用GET方式下载文件
	 * 
	 * @param path
	 *            要下载文件的URL
	 * @param params
	 *            需要携带的参数
	 * @param bufferLength
	 *            写文件时的字节长度
	 * @param filePath
	 *            要保存的文件路径
	 * @param callBack
	 *            下载文件请求返回的回调接口
	 */
	public void getDownloadFile(final String path, final HashMap<String, Object> params, final int bufferLength, 
			final String filePath, final RequestCallBack callBack) {
		getDownloadFile(path, 5000, false, params, bufferLength, filePath, callBack);
	}

	/**
	 * 使用GET方式下载文件
	 * 
	 * @param path
	 *            要下载文件的URL
	 * @param params
	 *            需要携带的参数
	 * @param filePath
	 *            要保存的文件路径
	 * @param callBack
	 *            下载文件请求返回的回调接口
	 */
	public void getDownloadFile(final String path, final HashMap<String, Object> params, final String filePath, 
			final RequestCallBack callBack) {
		getDownloadFile(path, 5000, false, params, 1024, filePath, callBack);
	}

	/**
	 * 使用GET方式异步下载文件，下载文件时会返回正在下载的状态
	 * 
	 * @param path
	 *            要下载文件的URL
	 * @param timeoutMillis
	 *            连接超时
	 * @param isInstanceFollowRedirects
	 *            是否重定向
	 * @param params
	 *            需要携带的参数
	 * @param bufferLength
	 *            写文件时的字节长度
	 * @param filePath
	 *            要保存的文件路径
	 * @param callBack
	 *            下载文件请求返回的回调接口
	 */
	public void getAsyncDownloadFile(final String path, final int timeoutMillis, final boolean isInstanceFollowRedirects, 
			final HashMap<String, Object> params, final int bufferLength, final String filePath, final FileDownloadCallBack callBack) {
		executor.execute(new Runnable() {

			@Override
			public void run() {
				Log.i(TAG, "thread="+Thread.currentThread().getName());
				HttpURLConnection conn = null;
				StringBuilder sb = new StringBuilder();
				JSONObject json = new JSONObject();
				FileOutputStream fileOutputStream = null;
				BufferedInputStream inputStream = null;
				Message msg = mHandler.obtainMessage();
				FileDownloadMessageObj obj = new FileDownloadMessageObj();
				msg.arg1 = MESSAGE_TYPE_ASYNC_DOWNLOAD;
				try {
					conn = getConnectionGet(path, timeoutMillis, isInstanceFollowRedirects, params, sb);
					inputStream = new BufferedInputStream(conn.getInputStream());
					fileOutputStream = downloadFileAsync(bufferLength, filePath, callBack, conn, json, fileOutputStream, 
							inputStream, msg, obj);
				} catch (IOException e) {
					Log.e(TAG, e.getMessage());
					try {
						json.put("ResultCode", RESULT_CODE_IOEXCEPTION);
						json.put("ResultDesc", e.getMessage());
						json.put("ResponseBody", "");
						msg.what = MESSAGE_CODE_FAILURE;
						obj.callBack = callBack;
						obj.result = json.toString();
						msg.obj = obj;
						mHandler.sendMessage(msg);
					} catch (JSONException e1) {
						Log.e(TAG, e1.getMessage());
					}
				} catch (JSONException e) {
					Log.e(TAG, e.getMessage());
					try {
						json.put("ResultCode", RESULT_CODE_JSONEXCEPTION);
						json.put("ResultDesc", e.getMessage());
						json.put("ResponseBody", "");
						msg.what = MESSAGE_CODE_FAILURE;
						obj.callBack = callBack;
						obj.result = json.toString();
						msg.obj = obj;
						mHandler.sendMessage(msg);
					} catch (JSONException e1) {
						Log.e(TAG, e1.getMessage());
					}
				} finally {
					try {
						if (conn != null) {
							conn.disconnect();
						}
						if (fileOutputStream != null) {
							fileOutputStream.close();
						}
						if (inputStream != null) {
							inputStream.close();
						}
					} catch (IOException e2) {
						Log.e(TAG, e2.getMessage());
					}
				}
			}
		});
	}

	/**
	 * 使用GET方式异步下载文件，下载文件时会返回正在下载的状态
	 * 
	 * @param path
	 *            要下载文件的URL
	 * @param isInstanceFollowRedirects
	 *            是否重定向
	 * @param params
	 *            需要携带的参数
	 * @param bufferLength
	 *            写文件时的字节长度
	 * @param filePath
	 *            要保存的文件路径
	 * @param callBack
	 *            下载文件请求返回的回调接口
	 */
	public void getAsyncDownloadFile(final String path, final boolean isInstanceFollowRedirects, 
			final HashMap<String, Object> params, final int bufferLength, final String filePath, final FileDownloadCallBack callBack) {
		getAsyncDownloadFile(path, 5000, isInstanceFollowRedirects, params, bufferLength, filePath, callBack);
	}

	/**
	 * 使用GET方式异步下载文件，下载文件时会返回正在下载的状态
	 * 
	 * @param path
	 *            要下载文件的URL
	 * @param timeoutMillis
	 *            连接超时
	 * @param params
	 *            需要携带的参数
	 * @param bufferLength
	 *            写文件时的字节长度
	 * @param filePath
	 *            要保存的文件路径
	 * @param callBack
	 *            下载文件请求返回的回调接口
	 */
	public void getAsyncDownloadFile(final String path, final int timeoutMillis, final HashMap<String, Object> params, 
			final int bufferLength, final String filePath, final FileDownloadCallBack callBack) {
		getAsyncDownloadFile(path, timeoutMillis, false, params, bufferLength, filePath, callBack);
	}

	/**
	 * 使用GET方式异步下载文件，下载文件时会返回正在下载的状态
	 * 
	 * @param path
	 *            要下载文件的URL
	 * @param params
	 *            需要携带的参数
	 * @param bufferLength
	 *            写文件时的字节长度
	 * @param filePath
	 *            要保存的文件路径
	 * @param callBack
	 *            下载文件请求返回的回调接口
	 */
	public void getAsyncDownloadFile(final String path, final HashMap<String, Object> params, final int bufferLength, 
			final String filePath, final FileDownloadCallBack callBack) {
		getAsyncDownloadFile(path, 5000, false, params, bufferLength, filePath, callBack);
	}

	/**
	 * 使用GET方式异步下载文件，下载文件时会返回正在下载的状态
	 * 
	 * @param path
	 *            要下载文件的URL
	 * @param params
	 *            需要携带的参数
	 * @param filePath
	 *            要保存的文件路径
	 * @param callBack
	 *            下载文件请求返回的回调接口
	 */
	public void getAsyncDownloadFile(final String path, final HashMap<String, Object> params, final String filePath, 
			final FileDownloadCallBack callBack) {
		getAsyncDownloadFile(path, 5000, false, params, 1024, filePath, callBack);
	}

	/**
	 * 通过POST方式下载文件
	 * 
	 * @param path
	 *            请求路径
	 * @param timeoutMillis
	 *            连接超时
	 * @param isUseCaches
	 *            是否使用缓存
	 * @param headers
	 *            消息头
	 * @param requestBody
	 *            请求体
	 * @param bufferLength
	 *            下载文件时的字节长度
	 * @param filePath
	 *            文件保存路径
	 * @param callBack
	 *            下载请求访问后的回调接口
	 */
	public void postDownloadFile(final String path, final int timeoutMillis, final boolean isUseCaches, 
			final HashMap<String, String> headers, final String requestBody, final int bufferLength, 
			final String filePath, final RequestCallBack callBack) {
		executor.execute(new Runnable() {

			@Override
			public void run() {
				HttpURLConnection conn = null;
				PrintWriter printWriter = null;
				BufferedInputStream inputStream = null;
				JSONObject json = new JSONObject();
				FileOutputStream fileOutputStream = null;
				try {
//					URL url = new URL(path);
//					conn = (HttpURLConnection) url.openConnection();
//					conn.setConnectTimeout(timeoutMillis);
//					conn.setDoOutput(true);
//					conn.setDoInput(true);
//					conn.setRequestMethod("POST");
//					conn.setUseCaches(isUseCaches);
//					Iterator<Entry<String, String>> iterator = headers.entrySet().iterator();
//					while (iterator.hasNext()) {
//						Entry<String, String> element = iterator.next();
//						conn.setRequestProperty(element.getKey(), element.getValue());
//					}
					conn = getConnectionPost(path, timeoutMillis, headers,isUseCaches);
					printWriter = new PrintWriter(conn.getOutputStream());
					printWriter.write(requestBody);
					printWriter.flush();
					conn.connect();
					inputStream = new BufferedInputStream(conn.getInputStream());
					fileOutputStream = downloadFile(bufferLength, filePath, callBack, conn, json, fileOutputStream, inputStream);
				} catch (IOException e) {
					Log.e(TAG, e.getMessage());
					try {
						json.put("ResultCode", RESULT_CODE_IOEXCEPTION);
						json.put("ResultDesc", e.getMessage());
						json.put("ResponseBody", "");
						callBack.onFailure(json.toString());
					} catch (JSONException e1) {
						Log.e(TAG, e1.getMessage());
					}
				} catch (JSONException e) {
					Log.e(TAG, e.getMessage());
					try {
						json.put("ResultCode", RESULT_CODE_JSONEXCEPTION);
						json.put("ResultDesc", e.getMessage());
						json.put("ResponseBody", "");
						callBack.onFailure(json.toString());
					} catch (JSONException e1) {
						Log.e(TAG, e1.getMessage());
					}
				} finally {
					try {
						if (conn != null) {
							conn.disconnect();
						}
						if (fileOutputStream != null) {
							fileOutputStream.close();
						}
						if (inputStream != null) {
							inputStream.close();
						}
						if (printWriter != null) {
							printWriter.close();
						}
					} catch (IOException e2) {
						Log.e(TAG, e2.getMessage());
					}
				}
			}
		});
	}

	/**
	 * 通过POST方式下载文件
	 * 
	 * @param path
	 *            请求路径
	 * @param isUseCaches
	 *            是否使用缓存
	 * @param headers
	 *            消息头
	 * @param requestBody
	 *            请求体
	 * @param bufferLength
	 *            下载文件时的字节长度
	 * @param filePath
	 *            文件保存路径
	 * @param callBack
	 *            下载请求访问后的回调接口
	 */
	public void postDownloadFile(String path, boolean isUseCaches, HashMap<String, String> headers, String requestBody, 
			int bufferLength, String filePath, RequestCallBack callBack) {
		postDownloadFile(path, 5000, isUseCaches, headers, requestBody, bufferLength, filePath, callBack);
	}

	/**
	 * 通过POST方式下载文件
	 * 
	 * @param path
	 *            请求路径
	 * @param timeoutMillis
	 *            连接超时
	 * @param headers
	 *            消息头
	 * @param requestBody
	 *            请求体
	 * @param bufferLength
	 *            下载文件时的字节长度
	 * @param filePath
	 *            文件保存路径
	 * @param callBack
	 *            下载请求访问后的回调接口
	 */
	public void postDownloadFile(String path, int timeoutMillis, HashMap<String, String> headers, String requestBody, 
			int bufferLength, String filePath, RequestCallBack callBack) {
		postDownloadFile(path, timeoutMillis, false, headers, requestBody, bufferLength, filePath, callBack);
	}

	/**
	 * 通过POST方式下载文件
	 * 
	 * @param path
	 *            请求路径
	 * @param headers
	 *            消息头
	 * @param requestBody
	 *            请求体
	 * @param bufferLength
	 *            下载文件时的字节长度
	 * @param filePath
	 *            文件保存路径
	 * @param callBack
	 *            下载请求访问后的回调接口
	 */
	public void postDownloadFile(String path, HashMap<String, String> headers, String requestBody, int bufferLength, 
			String filePath, RequestCallBack callBack) {
		postDownloadFile(path, 5000, false, headers, requestBody, bufferLength, filePath, callBack);
	}

	/**
	 * 通过POST方式下载文件
	 * 
	 * @param path
	 *            请求路径
	 * @param headers
	 *            消息头
	 * @param requestBody
	 *            请求体
	 * @param filePath
	 *            文件保存路径
	 * @param callBack
	 *            下载请求访问后的回调接口
	 */
	public void postDownloadFile(String path, HashMap<String, String> headers, String requestBody, String filePath, 
			RequestCallBack callBack) {
		postDownloadFile(path, 5000, false, headers, requestBody, 1024, filePath, callBack);
	}

	/**
	 * 通过POST方式异步下载文件，下载中会返回正在下载的状态
	 * 
	 * @param path
	 *            请求路径
	 * @param timeoutMillis
	 *            连接超时
	 * @param isUseCaches
	 *            是否使用缓存
	 * @param headers
	 *            消息头
	 * @param requestBody
	 *            请求体
	 * @param bufferLength
	 *            下载文件时的字节长度
	 * @param filePath
	 *            文件保存路径
	 * @param callBack
	 *            下载请求访问后的回调接口
	 */
	public void postAsyncDownloadFile(final String path, final int timeoutMillis, final boolean isUseCaches, 
			final HashMap<String, String> headers, final String requestBody, final int bufferLength, 
			final String filePath, final FileDownloadCallBack callBack) {
		executor.execute(new Runnable() {

			@Override
			public void run() {
				HttpURLConnection conn = null;
				PrintWriter printWriter = null;
				BufferedInputStream inputStream = null;
				JSONObject json = new JSONObject();
				FileOutputStream fileOutputStream = null;
				Message msg = mHandler.obtainMessage();
				FileDownloadMessageObj obj = new FileDownloadMessageObj();
				msg.arg1 = MESSAGE_TYPE_ASYNC_DOWNLOAD;
				try {
//					URL url = new URL(path);
//					conn = (HttpURLConnection) url.openConnection();
//					conn.setConnectTimeout(timeoutMillis);
//					conn.setDoOutput(true);
//					conn.setDoInput(true);
//					conn.setRequestMethod("POST");
//					conn.setUseCaches(isUseCaches);
//					Iterator<Entry<String, String>> iterator = headers.entrySet().iterator();
//					while (iterator.hasNext()) {
//						Entry<String, String> element = iterator.next();
//						conn.setRequestProperty(element.getKey(), element.getValue());
//					}
					conn = getConnectionPost(path, timeoutMillis, headers,isUseCaches);
					printWriter = new PrintWriter(conn.getOutputStream());
					printWriter.write(requestBody);
					printWriter.flush();
					conn.connect();
					inputStream = new BufferedInputStream(conn.getInputStream());
					fileOutputStream = downloadFileAsync(bufferLength, filePath, callBack, conn, json, fileOutputStream, 
							inputStream, msg, obj);
				} catch (IOException e) {
					Log.e(TAG, e.getMessage());
					try {
						json.put("ResultCode", RESULT_CODE_IOEXCEPTION);
						json.put("ResultDesc", e.getMessage());
						json.put("ResponseBody", "");
						msg.what = MESSAGE_CODE_FAILURE;
						obj.callBack = callBack;
						obj.result = json.toString();
						msg.obj = obj;
						mHandler.sendMessage(msg);
					} catch (JSONException e1) {
						Log.e(TAG, e1.getMessage());
					}
				} catch (JSONException e) {
					Log.e(TAG, e.getMessage());
					try {
						json.put("ResultCode", RESULT_CODE_JSONEXCEPTION);
						json.put("ResultDesc", e.getMessage());
						json.put("ResponseBody", "");
						msg.what = MESSAGE_CODE_FAILURE;
						obj.callBack = callBack;
						obj.result = json.toString();
						msg.obj = obj;
						mHandler.sendMessage(msg);
					} catch (JSONException e1) {
						Log.e(TAG, e1.getMessage());
					}
				} finally {
					try {
						if (conn != null) {
							conn.disconnect();
						}
						if (fileOutputStream != null) {
							fileOutputStream.close();
						}
						if (inputStream != null) {
							inputStream.close();
						}
						if (printWriter != null) {
							printWriter.close();
						}
					} catch (IOException e2) {
						Log.e(TAG, e2.getMessage());
					}
				}
			}
		});
	}

	/**
	 * 通过POST方式异步下载文件，下载中会返回正在下载的状态
	 * 
	 * @param path
	 *            请求路径
	 * @param isUseCaches
	 *            是否使用缓存
	 * @param headers
	 *            消息头
	 * @param requestBody
	 *            请求体
	 * @param bufferLength
	 *            下载文件时的字节长度
	 * @param filePath
	 *            文件保存路径
	 * @param callBack
	 *            下载请求访问后的回调接口
	 */
	public void postAsyncDownloadFile(final String path, final boolean isUseCaches, final HashMap<String, String> headers, 
			final String requestBody, final int bufferLength, final String filePath, final FileDownloadCallBack callBack) {
		postAsyncDownloadFile(path, 5000, isUseCaches, headers, requestBody, bufferLength, filePath, callBack);
	}

	/**
	 * 通过POST方式异步下载文件，下载中会返回正在下载的状态
	 * 
	 * @param path
	 *            请求路径
	 * @param timeoutMillis
	 *            连接超时
	 * @param headers
	 *            消息头
	 * @param requestBody
	 *            请求体
	 * @param bufferLength
	 *            下载文件时的字节长度
	 * @param filePath
	 *            文件保存路径
	 * @param callBack
	 *            下载请求访问后的回调接口
	 */
	public void postAsyncDownloadFile(final String path, final int timeoutMillis, final HashMap<String, String> headers, 
			final String requestBody, final int bufferLength, final String filePath, final FileDownloadCallBack callBack) {
		postAsyncDownloadFile(path, timeoutMillis, false, headers, requestBody, bufferLength, filePath, callBack);
	}

	/**
	 * 通过POST方式异步下载文件，下载中会返回正在下载的状态
	 * 
	 * @param path
	 *            请求路径
	 * @param headers
	 *            消息头
	 * @param requestBody
	 *            请求体
	 * @param bufferLength
	 *            下载文件时的字节长度
	 * @param filePath
	 *            文件保存路径
	 * @param callBack
	 *            下载请求访问后的回调接口
	 */
	public void postAsyncDownloadFile(final String path, final HashMap<String, String> headers, final String requestBody, 
			final int bufferLength, final String filePath, final FileDownloadCallBack callBack) {
		postAsyncDownloadFile(path, 5000, false, headers, requestBody, bufferLength, filePath, callBack);
	}

	/**
	 * 通过POST方式异步下载文件，下载中会返回正在下载的状态
	 * 
	 * @param path
	 *            请求路径
	 * @param headers
	 *            消息头
	 * @param requestBody
	 *            请求体
	 * @param filePath
	 *            文件保存路径
	 * @param callBack
	 *            下载请求访问后的回调接口
	 */
	public void postAsyncDownloadFile(final String path, final HashMap<String, String> headers, final String requestBody, 
			final String filePath, final FileDownloadCallBack callBack) {
		postAsyncDownloadFile(path, 5000, false, headers, requestBody, 1024, filePath, callBack);
	}

	/**
	 * 处理异步下载文件请求的请求结果
	 * @param bufferLength
	 * @param filePath
	 * @param callBack
	 * @param conn
	 * @param json
	 * @param fileOutputStream
	 * @param inputStream
	 * @param msg
	 * @param obj
	 * @return
	 * @throws IOException
	 * @throws FileNotFoundException
	 * @throws JSONException
	 */
	private FileOutputStream downloadFileAsync(final int bufferLength, final String filePath, 
			final FileDownloadCallBack callBack, HttpURLConnection conn, JSONObject json, 
			FileOutputStream fileOutputStream, BufferedInputStream inputStream, Message msg, FileDownloadMessageObj obj)
			throws IOException, FileNotFoundException, JSONException {
		json.put("ResultCode", conn.getResponseCode());
		if (HttpURLConnection.HTTP_OK == conn.getResponseCode()) {
			File file = new File(filePath);
			if (!file.getParentFile().exists()) {
				file.mkdirs();
			}
			file.createNewFile();
			fileOutputStream = new FileOutputStream(file);
			byte[] buffer = new byte[bufferLength];
			int len;
			int length = 0;
			JSONObject body = new JSONObject();
			JSONObject result = new JSONObject();
			FileDownloadMessageObj fileDownloadMessageObj = new FileDownloadMessageObj();
			while ((len = inputStream.read(buffer)) != -1) {
				fileOutputStream.write(buffer, 0, len);
				fileOutputStream.flush();
				Message msg1 = mHandler.obtainMessage();
				msg1.arg1 = MESSAGE_TYPE_ASYNC_DOWNLOAD;
				length += len;
				body.put("CurrentLength", length).put("TotalLength", conn.getContentLength());
				result.put("ResultDesc", conn.getResponseMessage());
				result.put("ResultDesc", "");
				result.put("ResponseBody", body.toString());
				msg1.what = MESSAGE_CODE_DOWNLOADING;
				fileDownloadMessageObj.callBack = callBack;
				fileDownloadMessageObj.result = result.toString();
				msg1.obj = fileDownloadMessageObj;
				mHandler.sendMessage(msg1);
			}
			json.put("ResultDesc", "");
			json.put("ResponseBody", "download file finish");
			msg.what = MESSAGE_CODE_SUCCESS;
			obj.callBack = callBack;
			obj.result = json.toString();
			msg.obj = obj;
			mHandler.sendMessage(msg);
		} else {
			json.put("ResultDesc", conn.getResponseMessage());
			json.put("ResponseBody", "");
			msg.what = MESSAGE_CODE_FAILURE;
			obj.callBack = callBack;
			obj.result = json.toString();
			msg.obj = obj;
			mHandler.sendMessage(msg);
		}
		return fileOutputStream;
	}

	/**
	 * 处理同步请求下载文件的请求结果
	 * @param bufferLength
	 * @param filePath
	 * @param callBack
	 * @param conn
	 * @param json
	 * @param fileOutputStream
	 * @param inputStream
	 * @return
	 * @throws IOException
	 * @throws FileNotFoundException
	 * @throws JSONException
	 */
	private FileOutputStream downloadFile(final int bufferLength, final String filePath, final RequestCallBack callBack, 
			HttpURLConnection conn, JSONObject json, FileOutputStream fileOutputStream, BufferedInputStream inputStream)
			throws IOException, FileNotFoundException, JSONException {
		json.put("ResultCode", conn.getResponseCode());
		if (HttpURLConnection.HTTP_OK == conn.getResponseCode()) {
			File file = new File(filePath);
			if (!file.getParentFile().exists()) {
				file.mkdirs();
			}
			file.createNewFile();
			fileOutputStream = new FileOutputStream(file);
			byte[] buffer = new byte[bufferLength];
			int len;
			while ((len = inputStream.read(buffer)) != -1) {
				fileOutputStream.write(buffer, 0, len);
				fileOutputStream.flush();
			}
			json.put("ResultDesc", "");
			json.put("ResponseBody", "ContentLength=" + conn.getContentLength());
			callBack.onSuccess(json.toString());
		} else {
			json.put("ResultDesc", conn.getResponseMessage());
			json.put("ResponseBody", "");
			callBack.onSuccess(json.toString());
		}
		return fileOutputStream;
	}

	/**
	 * 处理异步请求的请求结果
	 * @param callBack
	 * @param conn
	 * @param bufferedReader
	 * @param json
	 * @param msg
	 * @param obj
	 * @param sb
	 * @throws JSONException
	 * @throws IOException
	 */
	private void parseAsyncResult(final RequestCallBack callBack, HttpURLConnection conn, BufferedReader bufferedReader, 
			JSONObject json, Message msg, MessageObj obj, StringBuilder sb)
			throws JSONException, IOException {
		String line;
		json.put("ResultCode", conn.getResponseCode());
		if (HttpURLConnection.HTTP_OK == conn.getResponseCode()) {
			while ((line = bufferedReader.readLine()) != null) {
				sb.append(line);
			}
			json.put("ResultDesc", "");
			json.put("ResponseBody", sb.toString());
			msg.what = MESSAGE_CODE_SUCCESS;
			obj.callBack = callBack;
			obj.result = json.toString();
			msg.obj = obj;
			mHandler.sendMessage(msg);
		} else {
			String message = conn.getResponseCode() + "," + conn.getResponseMessage();
			json.put("ResultDesc", message);
			json.put("ResponseBody", "");
			msg.what = MESSAGE_CODE_FAILURE;
			obj.callBack = callBack;
			obj.result = json.toString();
			msg.obj = obj;
			mHandler.sendMessage(msg);
		}
	}

	/**
	 * 处理同步请求的请求结果
	 * @param callBack
	 * @param conn
	 * @param reader
	 * @param json
	 * @throws JSONException
	 * @throws IOException
	 */
	private void parseResult(final RequestCallBack callBack, HttpURLConnection conn, BufferedReader reader, JSONObject json) throws JSONException, IOException {
		json.put("ResultCode", conn.getResponseCode());
		if (HttpURLConnection.HTTP_OK == conn.getResponseCode()) {
			StringBuilder result = new StringBuilder();
			String readLine = "";
			while ((readLine = reader.readLine()) != null) {
				result.append(readLine);
			}
			json.put("ResultDesc", "");
			json.put("ResponseBody", result.toString());
			callBack.onSuccess(json.toString());
		} else {
			String message = conn.getResponseCode() + "," + conn.getResponseMessage();
			json.put("ResultDesc", message);
			json.put("ResponseBody", "");
			callBack.onFailure(json.toString());
		}
	}

	/**
	 * GET方式获得Connection
	 * @param path
	 * @param timeoutMillis
	 * @param isInstanceFollowRedirects
	 * @param params
	 * @param sb
	 * @return
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws ProtocolException
	 */
	private HttpURLConnection getConnectionGet(final String path, final int timeoutMillis, final boolean isInstanceFollowRedirects, final HashMap<String, Object> params, StringBuilder sb)
			throws MalformedURLException, IOException, ProtocolException {
		HttpURLConnection conn;
		Iterator<Entry<String, Object>> iterator = params.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<String, Object> next = iterator.next();
			sb.append(next.getKey());
			sb.append("=");
			sb.append(next.getValue());
			sb.append("&");
		}
		if (sb.length() > 0) {
			sb.deleteCharAt(sb.length() - 1);
		}
		URL url = new URL(path + "?" + sb.toString());
		conn = (HttpURLConnection) url.openConnection();
		conn.setInstanceFollowRedirects(isInstanceFollowRedirects);
		conn.setConnectTimeout(timeoutMillis);
		conn.setRequestMethod("GET");
		conn.connect();
		return conn;
	}

	/**
	 * POST方式获得Connection
	 * @param path
	 * @param timeoutMillis
	 * @param headers
	 * @param isUseCaches
	 * @return
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws ProtocolException
	 */
	private HttpURLConnection getConnectionPost(final String path, final int timeoutMillis, final HashMap<String, String> headers,boolean isUseCaches) throws MalformedURLException, IOException, ProtocolException {
		HttpURLConnection conn;
		URL url = new URL(path);
		conn = (HttpURLConnection) url.openConnection();
		conn.setConnectTimeout(timeoutMillis);
		conn.setDoOutput(true);
		conn.setDoInput(true);
		conn.setRequestMethod("POST");
		conn.setUseCaches(isUseCaches);
		Iterator<Entry<String, String>> iterator = headers.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<String, String> element = iterator.next();
			conn.setRequestProperty(element.getKey(), element.getValue());
		}
		return conn;
	}

	/**
	 * Http请求(普通文件下载)回调接口
	 *
	 * @author lize
	 *
	 *         2016年3月4日
	 */
	public interface RequestCallBack {

		/**
		 * 正确返回
		 * 
		 * @param result
		 *            {"ResultCode":200,"ResponseBody":"XXXX","ResultDesc":""}
		 */
		void onSuccess(String result);

		/**
		 * 其他情况
		 * 
		 * @param response
		 *            {"ResultCode":XXX,"ResponseBody":"","ResultDesc":"XXX"}
		 */
		void onFailure(String response);
	}

	/**
	 * Http请求异步下载文件回调接口
	 *
	 * @author lize
	 *
	 *         2016年3月4日
	 */
	public interface FileDownloadCallBack {
		/**
		 * 正在下载中，会返回当前下载进度和文件总大小
		 * 
		 * @param response
		 */
		void onDownloading(String response);

		/**
		 * 文件下载成功
		 * 
		 * @param result
		 */
		void onSuccess(String result);

		/**
		 * Http请求失败
		 * 
		 * @param response
		 */
		void onFailure(String response);
	}

	private class MessageObj implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 5722274927850302117L;
		RequestCallBack callBack;
		String result;
	}

	private class FileDownloadMessageObj implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = -1673994376392959685L;
		FileDownloadCallBack callBack;
		String result;
	}
	
	private static class MyHandler extends Handler{
		
		public MyHandler(Looper looper){
			super(looper);
		}
		
		@Override
		public void handleMessage(Message msg) {
			try {
				if (MESSAGE_TYPE_ASYNC_REQUEST == msg.arg1) {
					MessageObj messageObj = (MessageObj) msg.obj;
					if (messageObj != null) {
						switch (msg.what) {
							case MESSAGE_CODE_SUCCESS:
								messageObj.callBack.onSuccess(messageObj.result);
								break;
							case MESSAGE_CODE_FAILURE:
								messageObj.callBack.onFailure(messageObj.result);
								break;
						}
					}
				} else if (MESSAGE_TYPE_ASYNC_DOWNLOAD == msg.arg1) {
					FileDownloadMessageObj fileDownloadMessageObj = (FileDownloadMessageObj) msg.obj;
					if (fileDownloadMessageObj != null) {
						switch (msg.what) {
							case MESSAGE_CODE_SUCCESS:
								fileDownloadMessageObj.callBack.onSuccess(fileDownloadMessageObj.result);
								break;
							case MESSAGE_CODE_FAILURE:
								fileDownloadMessageObj.callBack.onFailure(fileDownloadMessageObj.result);
								break;
							case MESSAGE_CODE_DOWNLOADING:
								fileDownloadMessageObj.callBack.onDownloading(fileDownloadMessageObj.result);
								break;
						}
					}
				}
			} catch (Exception e) {
				Log.d(TAG, e.getMessage());
			}
		}
	}

}
