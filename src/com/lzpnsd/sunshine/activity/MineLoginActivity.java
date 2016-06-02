package com.lzpnsd.sunshine.activity;

import com.lzpnsd.sunshine.R;
import com.lzpnsd.sunshine.util.LogUtil;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_login_page);
		initView();
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

		mTvUserRegister.setOnClickListener(this);
		mBtNameClean.setOnClickListener(this);
		mBtPasswordClean.setOnClickListener(this);
		mCbRemberData.setOnClickListener(this);
		mCbShowPassword.setOnClickListener(this);
		mBtUserLogin.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
			case R.id.tv_user_register:

				break;
			case R.id.bt_login_name_clean:

				break;
			case R.id.bt_login_password_clean:

				break;
			case R.id.cb_rember_password:

				break;
			case R.id.cb_look_password:

				break;
			case R.id.bt_user_login:

				break;
			default:
				break;
		}
	}

}
