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
import com.lzpnsd.sunshine.R;
import com.lzpnsd.sunshine.contants.Contants;
import com.lzpnsd.sunshine.util.LogUtil;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("HandlerLeak")
public class MineLoginActivity extends BaseActivity implements OnClickListener {

	private final LogUtil log = LogUtil.getLog(getClass());

	private TextView mTvUserRegister;
	private EditText mEtUserName;
	private EditText mEtUserPassward;
	private Button mBtNameClean;
	private Button mBtPasswordClean;
	private CheckBox mCbRemberData;
	private CheckBox mCbShowPassword;
	private Button mBtUserLogin;
	private Context mContext;
	private SharedPreferences preferences;
	private Dialog loginDialog;
	private static final int REQUEST_CODE = 0;
	private String responseMsg;
	private String username, password;
	private static final int REQUEST_TIMEOUT = 5 * 1000;
	private static final int SO_TIMEOUT = 10 * 1000;
	private final String LoginPath = "/weather/login";
	private static final int LOGIN_SUCCESS = 0;
	private static final int USER_ERROR = 1;
	private static final int LOGIN_SERVER_FAILED = 2;

	public MineLoginActivity(Context context) {
		super();
		this.mContext = context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_login_page);
		preferences = getSharedPreferences("userdata", 0);
		initView();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == REQUEST_CODE || resultCode == REQUEST_CODE) {
			Bundle extras = data.getExtras();
			mEtUserName.setText(extras.getString("account"));
			mEtUserPassward.setText(extras.getString("password"));
		}
	}

	private void initView() {
		mTvUserRegister = (TextView) findViewById(R.id.tv_user_register);
		mEtUserName = (EditText) findViewById(R.id.et_user_name);
		mEtUserPassward = (EditText) findViewById(R.id.et_user_password);
		mBtNameClean = (Button) findViewById(R.id.bt_login_name_clean);
		mBtPasswordClean = (Button) findViewById(R.id.bt_login_password_clean);
		mCbRemberData = (CheckBox) findViewById(R.id.cb_rember_password);
		mCbShowPassword = (CheckBox) findViewById(R.id.cb_look_password);
		mBtUserLogin = (Button) findViewById(R.id.bt_user_login);

		username = mEtUserName.getText().toString();
		password = mEtUserPassward.getText().toString();
		log.d("username=" + username + "------" + "password=" + password);

		mTvUserRegister.setOnClickListener(this);
		mBtNameClean.setOnClickListener(this);
		mBtPasswordClean.setOnClickListener(this);
		mBtUserLogin.setOnClickListener(this);
		mCbRemberData.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					Editor editor = preferences.edit();
					editor.putString("username", mEtUserName.getText().toString());
					editor.putString("password", mEtUserPassward.getText().toString());
					editor.commit();
					mEtUserName.setText(preferences.getString("username", null));
					mEtUserPassward.setText(preferences.getString("password", null));
				}
			}
		});

		mCbShowPassword.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					mEtUserPassward.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
				} else {
					mEtUserPassward.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
				}
			}
		});
		;
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.tv_user_register:
				Intent registerintent = new Intent(mContext, MineRegisterActivity.class);
				startActivityForResult(registerintent, REQUEST_CODE);
				break;
			case R.id.bt_login_name_clean:
				mEtUserName.setText("");
				break;
			case R.id.bt_login_password_clean:
				mEtUserPassward.setText("");
				break;
			case R.id.bt_user_login:
				UserLogin();
				break;
			default:
				break;
		}
	}

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case LOGIN_SUCCESS:
					cancelRegisterDialog();
					Toast.makeText(mContext, R.string.text_login_success, Toast.LENGTH_LONG).show();
					break;
				case USER_ERROR:
					cancelRegisterDialog();
					Toast.makeText(mContext, R.string.text_login_password_error, Toast.LENGTH_LONG).show();
					break;
				case LOGIN_SERVER_FAILED:
					cancelRegisterDialog();
					Toast.makeText(mContext, R.string.text_login_server_failed, Toast.LENGTH_LONG).show();
					break;
				default:
					break;
			}
		}
	};

	private void UserLogin() {
		showRegisterDialog();
		Thread loginThread = new Thread(new LoginThread());
		loginThread.start();
	}

	class LoginThread implements Runnable {

		String validataName = username;
		String validataPassword = password;

		@Override
		public void run() {
			boolean validate = loginServer(username, password);
			Message msg = handler.obtainMessage();
			if (validate) {
				if (responseMsg.equals("success")) {
					msg.what = LOGIN_SUCCESS;
					handler.sendMessage(msg);
				} else {
					msg.what = USER_ERROR;
					handler.sendMessage(msg);
				}
			} else {
				msg.what = LOGIN_SERVER_FAILED;
				handler.sendMessage(msg);
			}
		}

	}

	private boolean loginServer(String username, String password) {
		boolean loginValidate = false;
		String url = Contants.URL_HOST + LoginPath;
		HttpPost request = new HttpPost(url);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("account", username));
		params.add(new BasicNameValuePair("password", password));
		try {
			request.setEntity(new UrlEncodedFormEntity(params));
			HttpClient client = getHttpClient();
			HttpResponse response = client.execute(request);
			if (response.getStatusLine().getStatusCode() == 200) {
				loginValidate = true;
				responseMsg = EntityUtils.toString(response.getEntity());
				log.d("responseMsg=" + responseMsg);
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return loginValidate;
	}

	public HttpClient getHttpClient() {
		BasicHttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, REQUEST_TIMEOUT);
		HttpConnectionParams.setSoTimeout(httpParams, SO_TIMEOUT);
		HttpClient client = new DefaultHttpClient(httpParams);
		return client;
	}

	private void showRegisterDialog() {
		if (loginDialog == null) {
			loginDialog = new Dialog(this, R.style.progress_dialog);
		}
		loginDialog.setContentView(R.layout.dialog_logining);
		loginDialog.setCancelable(true);// 不可按"返回"键
		loginDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
		loginDialog.show();
	}

	private void cancelRegisterDialog() {
		if (loginDialog != null) {
			loginDialog.cancel();
		}
	}
}
