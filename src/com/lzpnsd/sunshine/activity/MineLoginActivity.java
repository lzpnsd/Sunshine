package com.lzpnsd.sunshine.activity;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import com.lzpnsd.sunshine.R;
import com.lzpnsd.sunshine.contants.Contants;
import com.lzpnsd.sunshine.manager.DataManager;
import com.lzpnsd.sunshine.util.LogUtil;
import com.lzpnsd.sunshine.util.ToastUtil;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
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
 * 2016年6月12日 上午9:38:37
 * code:105
 */
public class MineLoginActivity extends BaseActivity implements OnClickListener {

	private final LogUtil log = LogUtil.getLog(getClass());

	private TextView mTvUserRegister;
	private EditText mEtUserName;
	private EditText mEtPassword;
	private CheckBox mCbRemberData;
	private Button mBtUserLogin;
	
	private Dialog loginDialog;
	
	private final String LOGIN_PATH = "/weather/login";

	public static final  int CODE_LOGIN_ACTIVITY_REQUEST = 1051;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_login_page);
		initView();
		initData();
	}

	private void initView() {
		mTvUserRegister = (TextView) findViewById(R.id.tv_user_register);
		mEtUserName = (EditText) findViewById(R.id.et_user_name);
		mEtPassword = (EditText) findViewById(R.id.et_user_password);
		mCbRemberData = (CheckBox) findViewById(R.id.cb_rember_password);
		mBtUserLogin = (Button) findViewById(R.id.bt_user_login);

		mTvUserRegister.setOnClickListener(this);
		mBtUserLogin.setOnClickListener(this);
	}

	private void initData() {
		boolean isLogin = DataManager.getInstance().isLogin();
		log.d("isLogin = "+isLogin);
		if(isLogin){
			String userName = DataManager.getInstance().getSavedUserName();
			String password = DataManager.getInstance().getSavedPassword();
			log.d("userName = "+userName+",password = "+password);
			mEtUserName.setText(userName);
			mEtPassword.setText(password);
		}
	}
	
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.tv_user_register:
				Intent registerintent = new Intent(MineLoginActivity.this, MineRegisterActivity.class);
				startActivityForResult(registerintent, CODE_LOGIN_ACTIVITY_REQUEST);
				break;
			case R.id.bt_user_login:
				showLoginDialog();
				String userName = mEtUserName.getText().toString();
				String password = mEtPassword.getText().toString();
				if(mCbRemberData.isChecked()){
					DataManager.getInstance().saveUserInfo(userName,password);
				}else{
					DataManager.getInstance().saveUserInfo("", "");
				}
				login(userName,password);
				break;
			default:
				break;
		}
	}

	private void login(final String userName,final String password) {
		new Thread(){
			public void run() {
				OkHttpClient client = new OkHttpClient();
					RequestBody body = new MultipartBody.Builder()
							.addFormDataPart("account", userName)
							.addFormDataPart("password", password)
							.build();
					Request request = new Request.Builder().url(Contants.URL_HOST + LOGIN_PATH).post(body).build();
					log.d("request = " + request.toString());
					client.newCall(request).enqueue(new Callback() {

						@Override
						public void onFailure(Call arg0, final IOException arg1) {
							runOnUiThread(new Runnable() {

								@Override
								public void run() {
									dismissLoginDialog();
									log.d("arg1 = " + arg1.getMessage());
									DataManager.getInstance().setIsLogin(false);
									ToastUtil.showToast(getString(R.string.text_login_password_error),
											ToastUtil.LENGTH_LONG);
								}
							});
						}

						@Override
						public void onResponse(Call arg0, final Response arg1) throws IOException {
							runOnUiThread(new Runnable() {

								@Override
								public void run() {
									dismissLoginDialog();
									String message = "";
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
											DataManager.getInstance().setIsLogin(true);
											ToastUtil.showToast(msg,
													ToastUtil.LENGTH_LONG);
											finish();
										} else {
											DataManager.getInstance().setIsLogin(false);
											ToastUtil.showToast(msg,
													ToastUtil.LENGTH_LONG);
										}
									} catch (JSONException e) {
										DataManager.getInstance().setIsLogin(false);
										ToastUtil.showToast(getString(R.string.text_login_password_error),
												ToastUtil.LENGTH_LONG);
									}
								}
							});
						}
					});
			};
		}.start();
	}

	private void showLoginDialog() {
		if (loginDialog == null) {
			loginDialog = new Dialog(this, R.style.progress_dialog);
		}
		loginDialog.setContentView(R.layout.dialog_logining);
		loginDialog.setCancelable(true);// 不可按"返回"键
		loginDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
		loginDialog.show();
	}

	private void dismissLoginDialog() {
		if (loginDialog != null) {
			loginDialog.cancel();
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(CODE_LOGIN_ACTIVITY_REQUEST == requestCode){
			if(MineRegisterActivity.CODE_REGISTER_SUCCESS_RESULT == resultCode){
				String userName = data.getStringExtra("userName");
				String password = data.getStringExtra("password");
				DataManager.getInstance().saveUserInfo(userName, password);
				login(userName, password);
			}
		}
	}
	
}
