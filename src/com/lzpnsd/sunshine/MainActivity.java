package com.lzpnsd.sunshine;

import com.lzpnsd.sunshine.activity.BaseActivity;
import com.lzpnsd.sunshine.activity.CityAddActivity;
import com.lzpnsd.sunshine.activity.CityListActivity;
import com.lzpnsd.sunshine.activity.UploadShijingActivity;
import com.lzpnsd.sunshine.contants.Contants;
import com.lzpnsd.sunshine.util.AdaptationUtil;
import com.lzpnsd.sunshine.util.LogUtil;
import com.lzpnsd.sunshine.util.ToastUtil;
import com.lzpnsd.sunshine.util.WeatherBackgroundUtil;
import com.lzpnsd.sunshine.view.MineView;
import com.lzpnsd.sunshine.view.ShijingView;
import com.lzpnsd.sunshine.view.WeatherView;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.AnimationUtils;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.ViewFlipper;

/**
 * code:10
 * @author lzpnsd
 * 2016年5月21日 下午8:47:05
 *
 */
public class MainActivity extends BaseActivity {

	private final LogUtil log = LogUtil.getLog(getClass());

	private ViewFlipper mViewFlipper;
	private View mWeatherView;
	private View mShijingView;
	private View mMineView;
	private RadioGroup mRgBottomSelector;
	private RadioButton mRbWeather;
	private RadioButton mRbShijing;
	private RadioButton mRbMine;

	private MineView mMine;
	private ShijingView mShijing;
	private WeatherView mWeather;

	private GestureDetector mGestureDetector;

	private int mSlideDistance;

	private int mCurrentIndex = 0;

	private float mRgButtomSelectorTopY = 0;
	private float mRlMainBottomY = 0;

	private RelativeLayout mContent;
	private RelativeLayout mContainer;
	
	public static final int CODE_UPLOAD_PIC_REQUEST = 101;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContent = (RelativeLayout) getLayoutInflater().inflate(R.layout.activity_main, null);
		setContentView(mContent);
		mContainer = (RelativeLayout) mContent.findViewById(R.id.rl_main);
		
		
		initData();

		initViews();

		setListener();
		
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) 
    { 
        super.onSaveInstanceState(outState); 
    } 
	
	@Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) 
    { 
        super.onRestoreInstanceState(savedInstanceState); 
    } 

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		System.exit(0);
	}
	
	/**
	 * 初始化数据
	 */
	private void initData() {
		mSlideDistance = AdaptationUtil.dip2px(getBaseContext(), 130);
	}

	/**
	 * 初始化空间
	 */
	private void initViews() {
		mViewFlipper = (ViewFlipper) findViewById(R.id.vf_main_content);
		mMine = new MineView(this);
		mShijing = new ShijingView(this);
		mWeather = new WeatherView(this);
		mWeatherView = mWeather.initView();
		mShijingView = mShijing.initView();
		mMineView = mMine.initView();
		mRgBottomSelector = (RadioGroup) findViewById(R.id.rg_main_bottom_selector);
		mRbWeather = (RadioButton) findViewById(R.id.rb_main_bottom_weather);
		mRbShijing = (RadioButton) findViewById(R.id.rb_main_bottom_shijing);
		mRbMine = (RadioButton) findViewById(R.id.rb_main_bottom_mine);
		mViewFlipper.addView(mWeatherView);
		mViewFlipper.addView(mShijingView);
		mViewFlipper.addView(mMineView);
	}

	/**
	 * 给控件设置监听事件
	 */
	private void setListener() {
		mGestureDetector = new GestureDetector(getBaseContext(), mGestureListener);
		mRgBottomSelector.setOnCheckedChangeListener(mCheckedChangeListener);
		mRgBottomSelector.setOnTouchListener(mOnTouchListener);
		setCheckedButtonAndBg();
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		// 该方法在onStart方法之后调用
		log.d("onWindowFocusChanged");
		int[] rgSelectorLocation = new int[2];
		mRgBottomSelector.getLocationOnScreen(rgSelectorLocation);
		mRgButtomSelectorTopY = rgSelectorLocation[1];
	}

	private OnTouchListener mOnTouchListener = new OnTouchListener() {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			switch (v.getId()) {
			case R.id.rg_main_bottom_selector:
				return true;
			}
			return false;
		}

	};

	private OnCheckedChangeListener mCheckedChangeListener = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			switch (checkedId) {
			case R.id.rb_main_bottom_weather:
				if (1 == mCurrentIndex) {
					showPrevious();
				} else if (2 == mCurrentIndex) {
					showNext();
				}
				mCurrentIndex = 0;
				break;
			case R.id.rb_main_bottom_shijing:
				if (0 == mCurrentIndex) {
					showNext();
				} else if (2 == mCurrentIndex) {
					showPrevious();
				}
				mCurrentIndex = 1;
				break;
			case R.id.rb_main_bottom_mine:
				if (0 == mCurrentIndex) {
					showPrevious();
				} else if (1 == mCurrentIndex) {
					showNext();
				}
				mCurrentIndex = 2;
				break;
			default:
				break;
			}
			setCheckedButtonAndBg();
		}
	};

	/**
	 * 显示下一页
	 */
	private void showNext() {
		mViewFlipper.setInAnimation(AnimationUtils.loadAnimation(getBaseContext(), R.anim.push_left_in));
		mViewFlipper.setOutAnimation(AnimationUtils.loadAnimation(getBaseContext(), R.anim.push_left_out));
		mViewFlipper.showNext();
	}

	/**
	 * 显示上一页
	 */
	private void showPrevious() {
		// 添加动画
		mViewFlipper.setInAnimation(AnimationUtils.loadAnimation(getBaseContext(), R.anim.push_right_in));
		mViewFlipper.setOutAnimation(AnimationUtils.loadAnimation(getBaseContext(), R.anim.push_right_out));
		mViewFlipper.showPrevious();
	}

	private OnGestureListener mGestureListener = new OnGestureListener() {

		@Override
		public boolean onSingleTapUp(MotionEvent e) {
			return false;
		}

		@Override
		public void onShowPress(MotionEvent e) {
		}

		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
			return false;
		}

		@Override
		public void onLongPress(MotionEvent e) {
		}

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
			// 对手指滑动的距离进行了计算，如果滑动距离大于mSlideDistance，就做切换动作，否则不做任何切换动作。
			// 从左向右滑动
			try {
				
			
			if (e1.getX() - e2.getX() > mSlideDistance) {
				// 添加动画
				showNext();
				setCheckedButton(1);
				return true;
			} // 从右向左滑动
			else if (e1.getX() - e2.getX() < -mSlideDistance) {
				showPrevious();
				setCheckedButton(-1);
				return true;
			}
			} catch (Exception e) {
				log.d(e.getMessage());
			}
			return true;
		}

		@Override
		public boolean onDown(MotionEvent e) {
			return false;
		}
	};
	
	@Override
	protected void onRestart() {
		super.onRestart();
		setCheckedButtonAndBg();
		log.d("refreshView on onRestart");
		mShijing.refreshView();
		mWeather.refreshData();
	};

	/**
	 * 当页面滚动时设置选中的Button
	 * 
	 * @param operate
	 *            向前滑-1，向后滑+1
	 */
	private void setCheckedButton(int operate) {
		if (mCurrentIndex == 2 && operate == 1) {
			mCurrentIndex = 0;
		} else if (mCurrentIndex == 0 && operate == -1) {
			mCurrentIndex = 2;
		} else {
			mCurrentIndex += operate;
		}
		setCheckedButtonAndBg();
	}

	/**
	 * 用于设置当前选中的按钮，设置当前的背景，用于沉浸式状态栏
	 */
	private void setCheckedButtonAndBg() {
		switch (mCurrentIndex) {
			case 0:
				mRbWeather.setChecked(true);
				int mainBackgroundResource = WeatherBackgroundUtil.getWeatherMainBackground();
				mContainer.setBackgroundResource(mainBackgroundResource);
				break;
			case 1:
				mRbShijing.setChecked(true);
				mContainer.setBackgroundColor(getResources().getColor(R.color.black));
				break;
			case 2:
				mRbMine.setChecked(true);
				mContainer.setBackgroundResource(R.drawable.personal_back);
				break;
			default:
				break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (resultCode) {
			case CityAddActivity.CODE_CITY_ADD_RESULT:
			case CityListActivity.CODE_SELECT_CITY_RESULT:
				if (WeatherView.CODE_WEATHERVIEW_REQUEST == requestCode) {
					log.d("requestCode = " + requestCode + ",resultCode = " + resultCode + ",data="
							+ data.getIntExtra(Contants.NAME_AREA_ID, 0));
					if (data != null) {
						int area_id = data.getIntExtra(Contants.NAME_AREA_ID, 0);
						log.d("area_id = " + area_id);
						if (area_id != 0) {
							mWeather.refreshData();
						}
					}
				} else if (ShijingView.CODE_SHIJING_REQUEST == requestCode) {
					if (data != null) {
						int area_id = data.getIntExtra(Contants.NAME_AREA_ID, 0);
						log.d("area_id = " + area_id);
						if (area_id != 0) {
							log.d("refreshView on CODE_SHIJING_REQUEST");
							mShijing.refreshView();
						}
					}
				}
				break;
			case CityListActivity.CODE_GOBACK_RESULT:
				if (WeatherView.CODE_WEATHERVIEW_REQUEST == requestCode) {
					mWeather.refreshData();
				} else if (ShijingView.CODE_SHIJING_REQUEST == requestCode) {
					log.d("refreshView at CODE_SHIJING_REQUEST");
					mShijing.refreshView();
				}
				break;
			case Activity.RESULT_OK:
				if(ShijingView.CODE_SHIJING_REQUEST_CAMERA == requestCode 
						|| ShijingView.CODE_SHIJING_REQUEST_GALLERY == requestCode){
					if(null == data){
						ToastUtil.showToast(getString(R.string.text_select_image_failed), ToastUtil.LENGTH_LONG);
						return;
					}
					Uri selectedImageUri = data.getData();//缩略图Uri
					log.d("uri = "+ selectedImageUri);
		            String[] filePathColumn = { MediaStore.Images.Media.DATA };
		  
		            Cursor cursor = getContentResolver().query(selectedImageUri,
		                    filePathColumn, null, null, null);
		            cursor.moveToFirst();
		  
		            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
		            String picturePath = cursor.getString(columnIndex);//图片的路径
		            cursor.close();
		            
		            Intent uploadIntent = new Intent(MainActivity.this,UploadShijingActivity.class);
		            uploadIntent.putExtra(Contants.NAME_SELECT_IMAGE_URI, selectedImageUri.toString());
		            uploadIntent.putExtra(Contants.NAME_SELECT_IMAGE_PATH, picturePath);
		            startActivityForResult(uploadIntent,CODE_UPLOAD_PIC_REQUEST);
				}
				break;
			case UploadShijingActivity.CODE_UPLOAD_RESULT:
				log.d("refreshView on CODE_UPLOAD_RESULT");
				mShijing.refreshView();
				break;
			default:
				break;
		}
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		float eventY = ev.getY();
		// log.d("touch y = "+eventY);
		// log.d("rg Y = "+mRgButtomSelectorTopY);
		if (eventY >= mRgButtomSelectorTopY || eventY <= AdaptationUtil.dip2px(getApplicationContext(), 50)) {
			return super.dispatchTouchEvent(ev);
		}
		// if(eventY >= mRlMain.getBottom() && eventY <=
		// mRgBottomSelector.getY()){
		// }
		mGestureDetector.onTouchEvent(ev);
		return super.dispatchTouchEvent(ev);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// 将该activity上的触碰事件交给GestureDetector处理
		float eventY = event.getY();
		if (eventY >= mRgButtomSelectorTopY || eventY <= AdaptationUtil.dip2px(getApplicationContext(), 50)) {
			return super.onTouchEvent(event);
		}
		return mGestureDetector.onTouchEvent(event);
	};

}
