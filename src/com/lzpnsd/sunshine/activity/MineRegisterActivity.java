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
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("HandlerLeak")
public class MineRegisterActivity extends BaseActivity implements OnClickListener {

	private final LogUtil log = LogUtil.getLog(getClass());

	private Context mContext;
	private TextView mTvUserReturn;
	private EditText mEtUserName;
	private EditText mEtUserPassword;
	private EditText mEtUserPasswordAgain;
	private Button mBtRegisterNameClean;
	private Button mBtPasswordClean;
	private Button mBtPasswordAgainClean;
	private Button mBtRegister;
	private Dialog registerDialog;
	private final String registerPath = "/weather/register";
	private String name, password, Passwordagain;
	private String responseMsg;
	private static final int REQUEST_TIMEOUT = 5 * 1000;// 设置请求超时5秒钟
	private static final int SO_TIMEOUT = 10 * 1000; // 设置等待数据超时时间10秒钟
	private static final int REGISTER_SUCCESS = 0;
	private static final int REGISTER_FAILED = 1;
	private static final int REFISTER_SERVER_FAILED = 2;

	public MineRegisterActivity(Context context) {
		super();
		this.mContext = context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_register_page);
		initView();
	}

	private void initView() {
		mEtUserName = (EditText) findViewById(R.id.et_user_name);
		mEtUserPassword = (EditText) findViewById(R.id.et_user_password);
		mEtUserPasswordAgain = (EditText) findViewById(R.id.et_user_password_again);
		mTvUserReturn = (TextView) findViewById(R.id.tv_user_return);
		mBtRegisterNameClean = (Button) findViewById(R.id.bt_register_name_clean);
		mBtPasswordClean = (Button) findViewById(R.id.bt_register_password_clean);
		mBtPasswordAgainClean = (Button) findViewById(R.id.bt_register_passwprd_again_clean);
		mBtRegister = (Button) findViewById(R.id.bt_user_register);
		
		name = mEtUserName.getText().toString();
		password = mEtUserPassword.getText().toString();
		Passwordagain = mEtUserPasswordAgain.getText().toString();

		mTvUserReturn.setOnClickListener(this);
		mBtRegisterNameClean.setOnClickListener(this);
		mBtPasswordClean.setOnClickListener(this);
		mBtPasswordAgainClean.setOnClickListener(this);
		mBtRegister.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.tv_user_return:
				this.finish();
				break;
			case R.id.bt_register_name_clean:
				mEtUserName.setText("");
				break;
			case R.id.bt_register_password_clean:
				mEtUserPassword.setText("");
				break;
			case R.id.bt_register_passwprd_again_clean:
				mEtUserPasswordAgain.setText("");
				break;
			case R.id.bt_user_register:
				RegisterUser();
				break;
			default:
				break;
		}
	}

	private void RegisterUser() {
		if (!password.equals(Passwordagain)) {
			Toast.makeText(mContext, R.string.prompt_password_again, Toast.LENGTH_LONG).show();
		} else {
			showRegisterDialog();
			Thread registerThread = new Thread(new RegisterThread());
			registerThread.start();
		}
	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case REGISTER_SUCCESS:
					cancelRegisterDialog();
					Bundle bundle = new Bundle();
					bundle.putString("account", name);
					bundle.putString("password", password);
					Intent intent = new Intent();
					intent.putExtras(bundle);
					setResult(RESULT_OK,intent);
					MineRegisterActivity.this.finish();
					Toast.makeText(mContext,R.string.text_register_success, Toast.LENGTH_LONG).show();
					break;
				case REGISTER_FAILED:
					cancelRegisterDialog();
					Toast.makeText(mContext, R.string.text_register_failed, Toast.LENGTH_LONG).show();
					break;
				case REFISTER_SERVER_FAILED:
					cancelRegisterDialog();
					Toast.makeText(mContext, R.string.text_register_server_failed, Toast.LENGTH_LONG).show();
					break;
				default:
					break;
			}
		}
	};

	private class RegisterThread implements Runnable {

		String validataName = name;
		String validataPassword = password;

		@Override
		public void run() {	
			
			boolean validate = registerServer(validataName, validataPassword);
			log.d("registerValidate=" + validate);
			Message msg = handler.obtainMessage();
			if (validate) {
				if (responseMsg.equals("success")) {
					msg.what = REGISTER_SUCCESS;
					handler.sendMessage(msg);
				} else {
					msg.what = REGISTER_FAILED;
					handler.sendMessage(msg);
				}
			} else {
				msg.what = REFISTER_SERVER_FAILED;
				handler.sendMessage(msg);
			}
		}
	}

	private boolean registerServer(String username, String password) {
		boolean registerValidate = false;
		String url = Contants.URL_HOST + registerPath;
		HttpPost request = new HttpPost(url);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("account", username));
		params.add(new BasicNameValuePair("password", password));
		try {
			// 设置请求参数项
			request.setEntity(new UrlEncodedFormEntity(params));
			HttpClient client = getHttpClient();
			HttpResponse response = client.execute(request);
			if (response.getStatusLine().getStatusCode() == 200) {
				registerValidate = true;
				responseMsg = EntityUtils.toString(response.getEntity());
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return registerValidate;
	}

	// 设置超时
	private HttpClient getHttpClient() {
		BasicHttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, REQUEST_TIMEOUT);
		HttpConnectionParams.setSoTimeout(httpParams, SO_TIMEOUT);
		HttpClient client = new DefaultHttpClient(httpParams);
		return client;
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

	private void cancelRegisterDialog() {
		if (registerDialog != null) {
			registerDialog.cancel();
		}
	}

}
