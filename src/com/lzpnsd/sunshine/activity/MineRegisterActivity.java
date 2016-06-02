package com.lzpnsd.sunshine.activity;

import com.lzpnsd.sunshine.R;
import com.lzpnsd.sunshine.util.LogUtil;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MineRegisterActivity extends BaseActivity implements OnClickListener {

	private final LogUtil log = LogUtil.getLog(getClass());

	private TextView mTvUserReturn;
	private EditText mEtUserName;
	private EditText mEtUserPassword;
	private EditText mEtUserPasswordAgain;
	private Button mBtRegisterNameClean;
	private Button mBtPasswordClean;
	private Button mBtPasswordAgainClean;
	private Button mBtRegister;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
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

		mTvUserReturn.setOnClickListener(this);
		mBtRegisterNameClean.setOnClickListener(this);
		mBtPasswordClean.setOnClickListener(this);
		mBtPasswordAgainClean.setOnClickListener(this);
		mBtRegister.setOnClickListener(this);

	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
			case R.id.tv_user_return:

				break;
			case R.id.bt_register_name_clean:

				break;
			case R.id.bt_register_password_clean:

				break;
			case R.id.bt_register_passwprd_again_clean:

				break;
			case R.id.bt_user_register:

				break;
			default:
				break;
		}
	}

}
