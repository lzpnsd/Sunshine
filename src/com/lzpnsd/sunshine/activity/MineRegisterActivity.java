package com.lzpnsd.sunshine.activity;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.lzpnsd.sunshine.R;
import com.lzpnsd.sunshine.contants.Contants;
import com.lzpnsd.sunshine.util.LogUtil;
import com.lzpnsd.sunshine.util.ToastUtil;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 
 * @author lzpnsd
 * 2016年6月12日 上午10:44:02
 * code:106
 */
public class MineRegisterActivity extends BaseActivity implements OnClickListener {

	private final LogUtil log = LogUtil.getLog(getClass());

	private EditText mEtUserName;
	private EditText mEtPassword;
	private Button mBtRegister;
	
	private Dialog registerDialog;
	
	private final String REGISTER_PATH = "/weather/register";
	
	public static final int CODE_REGISTER_SUCCESS_RESULT = 1061;
	public static final int CODE_REGISTER_FAILED_RESULT = 1062;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_register_page);
		initView();
	}

	private void initView() {
		mEtUserName = (EditText) findViewById(R.id.et_user_name);
		mEtPassword = (EditText) findViewById(R.id.et_user_password);
		mBtRegister = (Button) findViewById(R.id.bt_user_register);
		
		mBtRegister.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.bt_user_register:
				showRegisterDialog();
				RegisterUser(mEtUserName.getText().toString(),mEtPassword.getText().toString());
				break;
			default:
				break;
		}
	}

	private void RegisterUser(final String userName,final String password) {

		new Thread(){
			public void run() {
				OkHttpClient client = new OkHttpClient();
					RequestBody body = new MultipartBody.Builder()
							.addFormDataPart("account", userName)
							.addFormDataPart("password", password)
							.build();
					Request request = new Request.Builder().url(Contants.URL_HOST + REGISTER_PATH).post(body).build();
					log.d("request = " + request.toString());
					client.newCall(request).enqueue(new Callback() {

						@Override
						public void onFailure(Call arg0, final IOException arg1) {
							runOnUiThread(new Runnable() {

								@Override
								public void run() {
									log.d("arg1 = " + arg1.getMessage());
									dismissRegisterDialog();
									ToastUtil.showToast(getString(R.string.text_register_failed),
											ToastUtil.LENGTH_LONG);
								}
							});
						}

						@Override
						public void onResponse(Call arg0, final Response arg1) throws IOException {
							runOnUiThread(new Runnable() {

								@Override
								public void run() {
									String message = "";
									dismissRegisterDialog();
									try {
										message = arg1.body().string();
									} catch (IOException e1) {
										e1.printStackTrace();
									}
									log.d("message = " + message);
									try {
										JSONObject obj = new JSONObject(message);
										boolean result = obj.getBoolean("success");
										String msg = obj.getString("message");
										if (result) {
											ToastUtil.showToast(msg,
													ToastUtil.LENGTH_SHORT);
											Intent intent = new Intent();
											intent.putExtra("userName", userName);
											intent.putExtra("password", password);
											setResult(CODE_REGISTER_SUCCESS_RESULT,intent);
											finish();
										} else {
											ToastUtil.showToast(msg,
													ToastUtil.LENGTH_LONG);
										}
									} catch (JSONException e) {
										ToastUtil.showToast(getString(R.string.text_register_failed),
												ToastUtil.LENGTH_LONG);
									}
								}
							});
						}
					});
			};
		}.start();
	}

	private void showRegisterDialog() {
		if (registerDialog == null) {
			registerDialog = new Dialog(this, R.style.progress_dialog);
		}
		registerDialog.setContentView(R.layout.dialog_registering);
		registerDialog.setCancelable(true);// 不可按"返回"键
		registerDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
		registerDialog.show();
	}

	private void dismissRegisterDialog() {
		if (registerDialog != null) {
			registerDialog.cancel();
		}
	}

}
