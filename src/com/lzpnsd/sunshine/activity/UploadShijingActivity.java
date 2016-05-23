package com.lzpnsd.sunshine.activity;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import com.lzpnsd.sunshine.R;
import com.lzpnsd.sunshine.contants.Contants;
import com.lzpnsd.sunshine.manager.DataManager;
import com.lzpnsd.sunshine.util.AdaptationUtil;
import com.lzpnsd.sunshine.util.BitmapUtil;
import com.lzpnsd.sunshine.util.LogUtil;
import com.lzpnsd.sunshine.util.ShijingUtil;
import com.lzpnsd.sunshine.util.ToastUtil;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * code: 104
 * 
 * @author lzpnsd 2016年5月21日 下午6:07:37
 *
 */
public class UploadShijingActivity extends BaseActivity {

	private final LogUtil log = LogUtil.getLog(getClass());

	private ImageButton mIbUpload;
	private EditText mEtDescription;
	private ImageView mIvShijingPic;

	private Intent mIntent;

	private AlertDialog mDialogUploading;

	private final String PATH = "/weather/uploadPicture";

	public static final int CODE_UPLOAD_RESULT = 1041;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_upload_shijing);

		initView();
		setListener();
		setData();

	}

	private void initView() {
		mIbUpload = (ImageButton) findViewById(R.id.ib_upload_title_upload);
		mEtDescription = (EditText) findViewById(R.id.et_pic_description);
		mIvShijingPic = (ImageView) findViewById(R.id.iv_upload_pic);
	}

	private void setListener() {
		mIbUpload.setOnClickListener(mOnClickListener);
	}

	private void setData() {
		mIntent = getIntent();
		String selectedImagePath = mIntent.getStringExtra(Contants.NAME_SELECT_IMAGE_PATH);
		log.d("selectedImageUri = " + selectedImagePath);
		mIvShijingPic.setImageBitmap(BitmapUtil.compressBitmap(selectedImagePath));
	}

	private OnClickListener mOnClickListener = new OnClickListener() {

		@Override
		public void onClick(android.view.View v) {
			// 如果是wifi，这上传整张图片，否则压缩上传
			// Bitmap bitmap = null;
			final String path = mIntent.getStringExtra(Contants.NAME_SELECT_IMAGE_PATH);
			log.d("path = " + path);
			// if(NetWorkUtil.isWifi()){
			// bitmap = BitmapFactory.decodeFile(path);
			// }else{
			// bitmap = BitmapUtil.compressBitmap(path);
			// }

			showUploadingDialog();
			new Thread() {

				@Override
				public void run() {
					OkHttpClient client = new OkHttpClient();
					RequestBody fileBody = RequestBody.create(MediaType.parse("image/jpg"), path);
					RequestBody body = new MultipartBody.Builder()
							.addFormDataPart("cityId", DataManager.getInstance().getCurrentCityId() + "")
							.addFormDataPart("userId", Contants.VALUE_USER_ID)
							.addFormDataPart("describe", mEtDescription.getText().toString()).addPart(fileBody).build();
					Request request = new Request.Builder().url(Contants.URL_HOST + PATH).post(body).build();
					client.newCall(request).enqueue(new Callback() {

						@Override
						public void onFailure(Call arg0, final IOException arg1) {
							runOnUiThread(new Runnable() {

								@Override
								public void run() {
									log.d("arg1 = "+arg1.getMessage());
									if (null != mDialogUploading && mDialogUploading.isShowing()) {
										mDialogUploading.dismiss();
									}
									ToastUtil.showToast(getString(R.string.text_upload_failed), ToastUtil.LENGTH_LONG);
								}
							});
						}

						@Override
						public void onResponse(Call arg0, final Response arg1) throws IOException {
							runOnUiThread(new Runnable() {

								@Override
								public void run() {
									String message = null;
									try {
										message = arg1.body().string();
									} catch (IOException e1) {
										e1.printStackTrace();
									}
									
									log.d("message = " + message);
									try {
										JSONObject obj = new JSONObject(message);
										boolean result = obj.getBoolean("success");
										if (result) {

											if (null != mDialogUploading && mDialogUploading.isShowing()) {
												mDialogUploading.dismiss();
											}
											ShijingUtil.getInstance()
													.getShijingList(DataManager.getInstance().getCurrentCityId());
											ToastUtil.showToast(getString(R.string.text_upload_success),
													ToastUtil.LENGTH_LONG);
											setResult(CODE_UPLOAD_RESULT);
											finish();
										} else {
											if (null != mDialogUploading && mDialogUploading.isShowing()) {
												mDialogUploading.dismiss();
											}
											ToastUtil.showToast(getString(R.string.text_upload_failed),
													ToastUtil.LENGTH_LONG);
										}
									} catch (JSONException e) {
										if (null != mDialogUploading && mDialogUploading.isShowing()) {
											mDialogUploading.dismiss();
										}
										ToastUtil.showToast(getString(R.string.text_upload_failed),
												ToastUtil.LENGTH_LONG);
									}
								}
							});
						}
					});
				};
			}.start();
		};
	};

	private void showUploadingDialog() {
		mDialogUploading = new AlertDialog.Builder(UploadShijingActivity.this).create();
		RelativeLayout content = (RelativeLayout) getLayoutInflater().inflate(R.layout.dialog_uploading, null);
		Window window = mDialogUploading.getWindow();
		window.setContentView(content);
		LayoutParams layoutParams = window.getAttributes();
		layoutParams.width = AdaptationUtil.dip2px(UploadShijingActivity.this, 250);
		window.setAttributes(layoutParams);
		window.setGravity(Gravity.CENTER);
	}

}
