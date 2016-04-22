package com.lzpnsd.sunshine.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lzpnsd.sunshine.R;
import com.lzpnsd.sunshine.bean.ChartPointBean;
import com.lzpnsd.sunshine.bean.WeatherInfoBean;
import com.lzpnsd.sunshine.model.OnCustomItemClickListener;
import com.lzpnsd.sunshine.util.AdaptationUtil;
import com.lzpnsd.sunshine.util.LogUtil;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

public class CustomHorizontalView extends HorizontalScrollView {
	
	private LogUtil log = LogUtil.getLog(getClass());
	
	private Context mContext;
	
	private LinearLayout mLinearLayout;
	
	private List<WeatherInfoBean> mWeatherInfoBeans = new ArrayList<WeatherInfoBean>();
	private Map<CustomItemView,Integer> mViewPos = new HashMap<CustomItemView,Integer>();
	private List<CustomItemView> mCustomItemViews = new ArrayList<CustomItemView>();
	private Map<CustomItemView,WeatherInfoBean> mViewAndWeatherInfo = new HashMap<CustomItemView, WeatherInfoBean>();
	
	private float mHighTem;
	private float mLowTem;
	
	private int mCurrentCount = 0;
	private int mHorizontalPadding;
	private int mVerticalPadding;
	
	private Paint mLinePaint;
	private Path mHeighTemperaturePath;
	private Path mLowTemperaturePath;
	
	private OnCustomItemClickListener mOnCustomItemClickListener;
	
	public CustomHorizontalView(Context context) {
		super(context);
		this.mContext = context;
	}

	public CustomHorizontalView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
	}
	
	public CustomHorizontalView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.mContext = context;
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		initView();
		initDrawTools();
	}

	private void initDrawTools() {
		mHeighTemperaturePath = new Path();
		mLowTemperaturePath = new Path();
		mLinePaint = new Paint();
		mLinePaint.setColor(getResources().getColor(R.color.white));
		mLinePaint.setAntiAlias(true);
		mLinePaint.setStrokeWidth(4);
		mLinePaint.setStyle(Style.STROKE);
	}
	
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		log.d("onDraw(");
	}
	
	@Override
	protected void dispatchDraw(Canvas canvas) {
		super.dispatchDraw(canvas);
		int count = mCustomItemViews.size();
		log.d("count = "+ count);
		for(int i = 0;i < count;i++){
			CustomItemView customItemView = mCustomItemViews.get(i);
			ChartPointBean chartPointBean = customItemView.getChartPoint();
			log.d("chartPointBean = "+chartPointBean.toString());
			float xPos = chartPointBean.getxPosition()+chartPointBean.getxChartView()+customItemView.getLeft()+mHorizontalPadding;
			float yHighPos = chartPointBean.getyHighPosition()+chartPointBean.getyChartView()+customItemView.getTop()+mVerticalPadding;
			float yLowPos = chartPointBean.getyLowPosition()+chartPointBean.getyChartView()+customItemView.getTop()+mVerticalPadding;
			if(i == 0){
				mHeighTemperaturePath.moveTo(xPos, yHighPos);
				mLowTemperaturePath.moveTo(xPos, yLowPos);
			} else {
				mHeighTemperaturePath.lineTo(xPos, yHighPos);
				mLowTemperaturePath.lineTo(xPos, yLowPos);
			}
		}
		canvas.drawPath(mHeighTemperaturePath, mLinePaint);
		canvas.drawPath(mLowTemperaturePath, mLinePaint);
	}
	
	private void initView(){
		setHorizontalScrollBarEnabled(false);
		mHorizontalPadding = AdaptationUtil.dip2px(mContext, 5);
		mVerticalPadding = AdaptationUtil.dip2px(mContext, 10);
		setPadding(mHorizontalPadding, mVerticalPadding, mHorizontalPadding, mVerticalPadding);
		mLinearLayout = (LinearLayout)getChildAt(0);
	}
	
	private void getLowAndHeigh() {
		if (mWeatherInfoBeans != null && mWeatherInfoBeans.size() > 0) {
			mHighTem = Integer.parseInt(mWeatherInfoBeans.get(0).getHighTemperature());
			mLowTem = Integer.parseInt(mWeatherInfoBeans.get(0).getLowTemperature());
			for (WeatherInfoBean weatherInfoBean : mWeatherInfoBeans) {
				if (mHighTem < Integer.parseInt(weatherInfoBean.getHighTemperature())) {
					mHighTem = Integer.parseInt(weatherInfoBean.getHighTemperature());
				}
				if (mLowTem > Integer.parseInt(weatherInfoBean.getLowTemperature())) {
					mLowTem = Integer.parseInt(weatherInfoBean.getLowTemperature());
				}
			}
		}
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		log.d("onTouchEvent");
		super.onTouchEvent(ev);
		return true;
	}
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		log.d("dispatchTouchEvent");
		super.dispatchTouchEvent(ev);
		return true;
	}
	
	public void addChildView(List<WeatherInfoBean> weatherInfoBeans){
		initView();
		mWeatherInfoBeans.clear();
		mCustomItemViews.clear();
		mViewPos.clear();
		mViewAndWeatherInfo.clear();
		mLinearLayout.removeAllViews();
		mCurrentCount = 0;
		for(WeatherInfoBean weatherInfoBean : weatherInfoBeans){
			CustomItemView customItemView = new CustomItemView(mContext);
			customItemView.initData(weatherInfoBean);
			mWeatherInfoBeans.add(weatherInfoBean);
			mCustomItemViews.add(customItemView);
			mViewPos.put(customItemView, mCurrentCount);
			mViewAndWeatherInfo.put(customItemView, weatherInfoBean);
			mLinearLayout.addView(customItemView);
			customItemView.setOnClickListener(mOnClickListener);
			mCurrentCount++;
		}
	}
	
	private OnClickListener mOnClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			Integer clickPosition = mViewPos.get(v);
			if(null != mOnCustomItemClickListener){
				mOnCustomItemClickListener.onClick(v, clickPosition);
			}
		}
	};
	
	public void notifyDataChanged(){
		getLowAndHeigh();
		log.d("notify data changed");
		for (CustomItemView customItemView : mCustomItemViews) {
			customItemView.initData(mViewAndWeatherInfo.get(customItemView));
			customItemView.showPoint(mHighTem, mLowTem);
		}
//			强制刷新，调用onDraw()方法
//			方法1：
//			((View)view.getParnet()).invalidate();
//			方法2：
//			view.invalidate();
//			view.forceLayout();
//			view.requestLayout();
		invalidate();
		forceLayout();
		requestLayout();
	}
	
	public void setOnItemClickListener(OnCustomItemClickListener onCustomItemClickListener){
		this.mOnCustomItemClickListener = onCustomItemClickListener;
	}
	
}
