package com.lzpnsd.sunshine.view;

import java.util.ArrayList;
import java.util.List;

import com.lzpnsd.sunshine.R;
import com.lzpnsd.sunshine.SunshineApplication;
import com.lzpnsd.sunshine.activity.CityListActivity;
import com.lzpnsd.sunshine.adapter.ShijingGridViewAdapter;
import com.lzpnsd.sunshine.bean.CityBean;
import com.lzpnsd.sunshine.bean.ShijingBean;
import com.lzpnsd.sunshine.manager.DataManager;
import com.lzpnsd.sunshine.util.AdaptationUtil;
import com.lzpnsd.sunshine.util.ShijingUtil;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * code:103
 * @author lzpnsd
 * 2016年5月18日 下午9:44:22
 *
 */
public class ShijingView {

	private Context mContext;

	private RelativeLayout mView;
	
	private CustomGridView mGvShijing;
	private ImageButton mIbCityList;
	private ImageButton mIbCamera;
	private TextView mTvCityName;
	
	private List<ShijingBean> mShijingBeans = new ArrayList<ShijingBean>();
	private ShijingGridViewAdapter mAdapter;
	
	public static final int CODE_SHIJING_REQUEST = 1031;
	public static final int CODE_SHIJING_REQUEST_CAMERA = 1032;
	public static final int CODE_SHIJING_REQUEST_GALLERY = 1033;
	
	public ShijingView(Context mContext) {
		super();
		this.mContext = mContext;
	}
	
	public RelativeLayout initView(){
		LayoutInflater inflater = LayoutInflater.from(mContext);
		mView = (RelativeLayout) inflater.inflate(R.layout.view_shijing,null);
		mAdapter = new ShijingGridViewAdapter(SunshineApplication.getContext(), mShijingBeans);
		setViews();
		refreshView();
		return mView;
	}
	
	private void setViews(){
		mGvShijing = (CustomGridView) mView.findViewById(R.id.gv_shijing_pic);
		mIbCityList = (ImageButton) mView.findViewById(R.id.ib_shijing_title_city);
		mIbCamera = (ImageButton) mView.findViewById(R.id.ib_shijing_title_camera);
		mTvCityName = (TextView) mView.findViewById(R.id.tv_shijing_city_name);
		mIbCityList.setOnClickListener(mOnClickListener);
		mIbCamera.setOnClickListener(mOnClickListener);
		mGvShijing.setAdapter(mAdapter);
	}
	
	public void refreshView(){
		initData();
		setData();
	}
	
	private void initData(){
		List<ShijingBean> shijingBeans = ShijingUtil.getInstance().getShijingBeans();
		if(null != shijingBeans && shijingBeans.size()>0){
			int cityId = shijingBeans.get(0).getCityId();
			if(DataManager.getInstance().getCurrentCityId() == cityId){
				mShijingBeans.clear();
				mShijingBeans.addAll(shijingBeans);
				mAdapter.notifyDataSetChanged();
			}else{
				mShijingBeans.clear();
			}
		}else{
			mShijingBeans.clear();
		}
	}
	
	private void setData(){
		CityBean cityBean = DataManager.getInstance().getCurrentCityBean();
		mTvCityName.setText(cityBean.getNameCn());
	}
	
	private OnClickListener mOnClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.ib_shijing_title_city:
				Intent intent = new Intent(mContext,CityListActivity.class);
				((Activity)mContext).startActivityForResult(intent,CODE_SHIJING_REQUEST);
				break;
			case R.id.ib_shijing_title_camera:
				showSelectTypeDialog();
				break;
			default:
				break;
			}
		}

	};
	
	private void showSelectTypeDialog() {
		final AlertDialog dialog = new AlertDialog.Builder(mContext).create();
		RelativeLayout selectTypeContent = (RelativeLayout) ((LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.dialog_select_cameraorphoto, null);
		TextView tvCamera = (TextView) selectTypeContent.findViewById(R.id.tv_select_camera);
		TextView tvGallery = (TextView) selectTypeContent.findViewById(R.id.tv_select_from_gallery);
		tvCamera.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intentCamera = new Intent("android.media.action.IMAGE_CAPTURE");
				((Activity) mContext).startActivityForResult(intentCamera,ShijingView.CODE_SHIJING_REQUEST_CAMERA);
				dialog.dismiss();
			}
		});
		tvGallery.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				/* 开启Pictures画面Type设定为image */
				intent.setType("image/*");
				/* 使用Intent.ACTION_GET_CONTENT这个Action */
				intent.setAction(Intent.ACTION_GET_CONTENT);
				/* 取得相片后返回本画面 */
				((Activity) mContext).startActivityForResult(intent, ShijingView.CODE_SHIJING_REQUEST_GALLERY);  
				dialog.dismiss();
			}
		});
		dialog.show();
		Window window = dialog.getWindow();
		window.setContentView(selectTypeContent);
		LayoutParams layoutParams = window.getAttributes();
		layoutParams.width = AdaptationUtil.dip2px(mContext, 250);
//		window.addContentView(content, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		window.setAttributes(layoutParams);
		window.setGravity(Gravity.CENTER);
		
	}
}
