package com.lzpnsd.sunshine.view;

import java.util.List;


import com.lzpnsd.sunshine.SunshineApplication;
import com.lzpnsd.sunshine.bean.WeatherInfoBean;
import com.lzpnsd.sunshine.contants.Contants;
import com.lzpnsd.sunshine.util.AdaptationUtil;
import com.lzpnsd.sunshine.util.LogUtil;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

public class ChartView extends View {

	private LogUtil log = LogUtil.getLog(getClass());
	
	private Context mContext;

	private int mTopTitleTextHeight;
	private int mTopTextHeight;
	private int mTopBitmapHeight;
	private int mTemperatureHeight;
	private int mBottomBitmapHeight;
	private int mBottomTextHeight;
	private int mBottomTitleTextHeight;

	private int mTemperatureItemWidth;

	private List<WeatherInfoBean> weatherInfoBeans;

	private Paint mPointPaint;
	private Path mHeighTemperaturePath;
	private Path mLowTemperaturePath;
	private Paint mTitleTextPaint;
	private Paint mTextPaint;
	private Paint mLinePaint;

	private boolean isFirst;

	private Canvas mCanvas;

	private int mHeighTem = 0;
	private int mLowTem = 0;
	
	private int mWidthSpace;
	
	private float mViewHeight;
	private float mViewWidth;

	public ChartView(Context context) {
		super(context);
		setWillNotDraw(false);
		this.mContext = context;
		setBackgroundColor(Color.TRANSPARENT);
		setWillNotDraw(false);
	}

	public ChartView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
		setWillNotDraw(false);
		setBackgroundColor(Color.TRANSPARENT);
		setWillNotDraw(false);
	}

	public ChartView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.mContext = context;
		setWillNotDraw(false);
		setBackgroundColor(Color.TRANSPARENT);
		setWillNotDraw(false);
	}

	private void initPaint() {
		mPointPaint = new Paint();
		mPointPaint.setColor(Color.WHITE);
		mPointPaint.setAntiAlias(true);
		mPointPaint.setStrokeWidth(6);
		mPointPaint.setStyle(Style.FILL);
		mHeighTemperaturePath = new Path();
		mLowTemperaturePath = new Path();
		mTitleTextPaint = new Paint();
		mTitleTextPaint.setColor(Color.WHITE);
		mTitleTextPaint.setAntiAlias(true);
		mTitleTextPaint.setStrokeWidth(1);
		mTitleTextPaint.setTextSize(20);
		mTextPaint = new Paint();
		mTextPaint.setColor(Color.WHITE);
		mTextPaint.setAntiAlias(true);
		mTextPaint.setStrokeWidth(1);
		mTextPaint.setTextSize(18);
		mLinePaint = new Paint();
		mLinePaint.setColor(Color.WHITE);
		mLinePaint.setAntiAlias(true);
		mLinePaint.setStrokeWidth(3);
		mLinePaint.setStyle(Style.STROKE);
	}

	private void getLowAndHeigh() {
		if (weatherInfoBeans != null && weatherInfoBeans.size() > 0) {
			for (WeatherInfoBean weatherInfoBean : weatherInfoBeans) {
				if (mHeighTem < Integer.parseInt(weatherInfoBean.getHighTemperature())) {
					mHeighTem = Integer.parseInt(weatherInfoBean.getHighTemperature());
				}
				if (mLowTem > Integer.parseInt(weatherInfoBean.getLowTemperature())) {
					mLowTem = Integer.parseInt(weatherInfoBean.getLowTemperature());
				}
			}
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		DisplayMetrics metric = new DisplayMetrics();
		WindowManager windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
		windowManager.getDefaultDisplay().getMetrics(metric);
		float x = metric.xdpi; // 屏幕宽度（像素）
//		log.d("x="+x);
		int dip2px = AdaptationUtil.dip2px(mContext, x-65);
//		log.d("dip2px="+dip2px);
		mWidthSpace = dip2px/5;
		int[] location = new int[2];
		getLocationOnScreen(location);
		mTemperatureItemWidth = location[0];
		mTopTitleTextHeight = location[1];
		mTopTextHeight = mTopTitleTextHeight + Contants.HEIGHT_TITLE_TEXT;
		mTopBitmapHeight = mTopTextHeight + Contants.HEIGHT_TEXT;
		mTemperatureHeight = mTopBitmapHeight + Contants.HEIGHT_BITMAP;
		mBottomBitmapHeight = mTemperatureHeight + Contants.HEIGHT_CHART_TEMPERATURE;
		mBottomTextHeight = mBottomBitmapHeight + Contants.HEIGHT_BITMAP;
		mBottomTitleTextHeight = mBottomTextHeight + Contants.HEIGHT_TEXT;
		initPaint();

	}
	
	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		mViewHeight = getHeight();
		mViewWidth = getWidth();
		mWidthSpace = (int) ((mViewWidth-AdaptationUtil.dip2px(mContext, 20))/5);
	}
	
	private void settingMeasure(int widthMeasureSpec,int heightMeasureSpec){
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);  
	    int widthSize = MeasureSpec.getSize(widthMeasureSpec);  
	    int heightMode = MeasureSpec.getMode(heightMeasureSpec);  
	    int heightSize = MeasureSpec.getSize(heightMeasureSpec);  
	    int width;  
	    int height ;  
	    if (widthMode == MeasureSpec.EXACTLY)  
	    {  
	        width = widthSize;  
	    } else  
	    {  
	        int desired = (int) (getPaddingLeft() + AdaptationUtil.dip2px(mContext, 310) + getPaddingRight());  
	        width = desired;  
	    }  
	  
	    if (heightMode == MeasureSpec.EXACTLY)  
	    {  
	        height = heightSize;  
	    } else  
	    {  
	        int desired = (int) (getPaddingTop() + AdaptationUtil.dip2px(mContext, Contants.SPACE_CHART_TEMPERATURE) + getPaddingBottom());  
	        height = desired;  
	    }  
	    setMeasuredDimension(width, height);  
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		mCanvas = canvas;
		log.d("on draw");
		if ((weatherInfoBeans != null) && (weatherInfoBeans.size() > 0)) {
			getLowAndHeigh();
			isFirst = true;
			log.d("weather beans >0");
			float heightSpace = (Contants.HEIGHT_CHART_TEMPERATURE / (mHeighTem - mLowTem));
			for (WeatherInfoBean weatherInfoBean : weatherInfoBeans) {
				float yHigh = mTemperatureHeight + ((mHeighTem - Integer
						.parseInt(weatherInfoBean.getHighTemperature()))
						* heightSpace);
				float yLow = mTemperatureHeight + ((mHeighTem - Integer
						.parseInt(weatherInfoBean.getLowTemperature()))
				* heightSpace);
				if (isFirst) {
					mHeighTemperaturePath
							.moveTo(mTemperatureItemWidth,yHigh);
					mLowTemperaturePath
							.moveTo(mTemperatureItemWidth,yLow);
					drawtPoint(mTemperatureItemWidth,yHigh);
					drawtPoint(mTemperatureItemWidth,yLow);
					
					float textWidth = mTitleTextPaint.measureText(weatherInfoBean.getDate());
					float textCenterX = mTemperatureItemWidth-textWidth/2;
					canvas.drawText(weatherInfoBean.getDate(), textCenterX, mTemperatureHeight - AdaptationUtil.dip2px(mContext, 15), mTitleTextPaint);
					
					
//					 canvas.drawCircle(mTemperatureItemWidth,
//					 mTemperatureHeight +
//					 ((mHeighTem-Integer.parseInt(weatherInfoBean.getHighTemperature().replace("℃",
//					 "").trim()))*heightSpace),
//					 AdaptationUtil.dip2px(SunshineApplication.getContext(),
//					 2), mPointPaint);
//					 canvas.drawCircle(mTemperatureItemWidth,
//					 mTemperatureHeight +
//					 ((mHeighTem-Integer.parseInt(weatherInfoBean.getLowTemperature().replace("℃",
//					 "").trim()))*heightSpace),
//					 AdaptationUtil.dip2px(SunshineApplication.getContext(),
//					 2), mPointPaint);
					isFirst = false;
				} else {
					mHeighTemperaturePath
							.lineTo(mTemperatureItemWidth,yHigh);
					mLowTemperaturePath
							.lineTo(mTemperatureItemWidth,yLow);
					drawtPoint(mTemperatureItemWidth,yHigh);
					drawtPoint(mTemperatureItemWidth,yLow);
					
//					FontMetrics fontMetrics = mTitleTextPaint.getFontMetrics();
//					float textCenterVerticalBaselineY = mViewHeight / 2 - fontMetrics.descent + (fontMetrics.descent - fontMetrics.ascent) / 2;
//					float textWidth = mTitleTextPaint.measureText(weatherInfoBean.getDate());
//					float textCenterX = mViewWidth - textWidth;
//					canvas.drawText(weatherInfoBean.getDate(), textCenterX, mTemperatureHeight - AdaptationUtil.dip2px(mContext, 15), mTitleTextPaint);
					canvas.drawText(weatherInfoBean.getDate(), mTemperatureItemWidth, mTemperatureHeight - AdaptationUtil.dip2px(mContext, 15), mTitleTextPaint);
					// canvas.drawCircle(mTemperatureItemWidth,
					// mTemperatureHeight +
					// ((mHeighTem-Integer.parseInt(weatherInfoBean.getHighTemperature().replace("℃",
					// "").trim()))*heightSpace),
					// AdaptationUtil.dip2px(SunshineApplication.getContext (),
					// 2), mPointPaint);
					// canvas.drawCircle(mTemperatureItemWidth,
					// mTemperatureHeight +
					// ((mHeighTem-Integer.parseInt(weatherInfoBean.getLowTemperature().replace("℃",
					// "").trim()))*heightSpace),
					// AdaptationUtil.dip2px(SunshineApplication.getContext(),
					// 2), mPointPaint);
				}
				mTemperatureItemWidth += mWidthSpace;
//						Contants.WIDTH_CHART_ITEM;
			}
			canvas.drawPath(mHeighTemperaturePath, mLinePaint);
			canvas.drawPath(mLowTemperaturePath, mLinePaint);
		}
	}

	private void drawtPoint(float x, float y) {
		mCanvas.drawCircle(x, y, AdaptationUtil.dip2px(SunshineApplication.getContext(), 3), mPointPaint);
	}

	private void drawLine(Path path) {
		mCanvas.drawPath(path, mLinePaint);

	}

	public void setWeatherInfoBeans(List<WeatherInfoBean> weatherInfoBeans) {
		this.weatherInfoBeans = weatherInfoBeans;
	}

	public void show() {
		postInvalidate();
		log.d("show");
	}

}
