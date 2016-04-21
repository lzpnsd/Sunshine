package com.lzpnsd.sunshine;

import com.lzpnsd.sunshine.util.AdaptationUtil;
import com.lzpnsd.sunshine.view.MineView;
import com.lzpnsd.sunshine.view.ShijingView;
import com.lzpnsd.sunshine.view.WeatherView;

import android.app.Activity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.ViewFlipper;

public class MainActivity extends Activity {

	private ViewFlipper mViewFlipper;
	private View mWeatherView;
	private View mIndexView;
	private View mMineView;
	private RadioGroup mRgBottomSelector;
	private RadioButton mRbWeather;
	private RadioButton mRbindex;
	private RadioButton mRbMine;

	private MineView mMine;
	private ShijingView mShijing;
	private WeatherView mWeather;
	
	private GestureDetector mGestureDetector;

	private int mSlideDistance;

	private int mCurrentIndex = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		initData();

		initViews();

		setListener();

	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		mSlideDistance = AdaptationUtil.dip2px(getBaseContext(), 120);
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
		mIndexView = mShijing.initView();
		mMineView = mMine.initView();
		mRgBottomSelector = (RadioGroup) findViewById(R.id.rg_main_bottom_selector);
		mRbWeather = (RadioButton) findViewById(R.id.rb_main_bottom_weather);
		mRbindex = (RadioButton) findViewById(R.id.rb_main_bottom_index);
		mRbMine = (RadioButton) findViewById(R.id.rb_main_bottom_mine);
		mViewFlipper.addView(mWeatherView);
		mViewFlipper.addView(mIndexView);
		mViewFlipper.addView(mMineView);
		
		
	}

	/**
	 * 给控件设置坚挺时间
	 */
	private void setListener() {
		mGestureDetector = new GestureDetector(getBaseContext(), mGestureListener);
		mRgBottomSelector.setOnCheckedChangeListener(mCheckedChangeListener);
	}

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
			case R.id.rb_main_bottom_index:
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
		//添加动画
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
			return true;
		}

		@Override
		public boolean onDown(MotionEvent e) {
			return false;
		}
	};

	/**
	 * 当页面滚动时设置选中的Button
	 * @param operate 向前滑-1，向后滑+1
	 */
	private void setCheckedButton(int operate) {
		if(mCurrentIndex==2&&operate==1){
			mCurrentIndex = 0;
		}else if(mCurrentIndex == 0&& operate == -1){
			mCurrentIndex = 2;
		}else{
			mCurrentIndex+=operate;
		}
		switch (mCurrentIndex) {
		case 0:
			mRbWeather.setChecked(true);
			break;
		case 1:
			mRbindex.setChecked(true);
			break;
		case 2:
			mRbMine.setChecked(true);
			break;
		default:
			break;
		}
	}
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		mGestureDetector.onTouchEvent(ev);
		return super.dispatchTouchEvent(ev);
	}

	public boolean onTouchEvent(MotionEvent event) {
		// 将该activity上的触碰事件交给GestureDetector处理
		return mGestureDetector.onTouchEvent(event);
	};

}
