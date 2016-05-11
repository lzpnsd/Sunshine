package com.lzpnsd.sunshine.view;

import com.lzpnsd.sunshine.R;
import com.lzpnsd.sunshine.bean.ChartPointBean;
import com.lzpnsd.sunshine.util.AdaptationUtil;
import com.lzpnsd.sunshine.util.LogUtil;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.View;

public class CustomChartView extends View {

	private LogUtil log = LogUtil.getLog(getClass());

	private Context mContext;

	private String mHeightTemperature;
	private String mLowTemperature;

	private Paint mPointPaint;
	private Paint mTextPaint;

	private float mXPosition;
	private float mYPosition;
	private float mYHeightPosition;
	private float mYLowPosition;

	private boolean mCanDraw = false;

	public CustomChartView(Context context) {
		super(context);
		this.mContext = context;
	}

	public CustomChartView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
	}

	public CustomChartView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		this.mContext = context;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int width = getMeasuredWidth();
		mXPosition = width / 2;
		mYPosition = AdaptationUtil.dip2px(mContext, 20);
		initPaint();

	}

	private void initPaint() {
		if (mPointPaint == null) {
			log.d("create paint");
			mPointPaint = new Paint();
			mPointPaint.setColor(getResources().getColor(R.color.white));
			mPointPaint.setAntiAlias(true);
			mPointPaint.setStrokeWidth(AdaptationUtil.dip2px(mContext, 4));
			mPointPaint.setStyle(Style.FILL);
		}
		if (mTextPaint == null) {
			mTextPaint = new Paint();
			mTextPaint.setColor(getResources().getColor(R.color.white));
			mTextPaint.setAntiAlias(true);
			mTextPaint.setStrokeWidth(AdaptationUtil.dip2px(mContext, 1));
			mTextPaint.setTextSize(AdaptationUtil.dip2px(mContext, 12));
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (mCanDraw) {
//			log.d("xposition = " + mXPosition);
			canvas.drawCircle(mXPosition, mYHeightPosition, AdaptationUtil.dip2px(mContext, 3), mPointPaint);
			canvas.drawCircle(mXPosition, mYLowPosition, AdaptationUtil.dip2px(mContext, 3), mPointPaint);
			float measureHeightText = mTextPaint.measureText(mHeightTemperature);
			float measureLowText = mTextPaint.measureText(mLowTemperature);
			float textXHeight = mXPosition - measureHeightText / 2;
			float textXLow = mXPosition - measureLowText / 2;
			canvas.drawText(mHeightTemperature, textXHeight, mYHeightPosition - AdaptationUtil.dip2px(mContext, 10), mTextPaint);
			canvas.drawText(mLowTemperature, textXLow, mYLowPosition + AdaptationUtil.dip2px(mContext, 16), mTextPaint);
		}
	}

	public void setData(float highTem, float lowTem, int heightTemperature, int lowTemperature) {
		int height = (int) (getResources().getDimension(R.dimen.item_height_chart_item_custom) - AdaptationUtil.dip2px(mContext, 38));
		float item = height / (highTem - lowTem);
		this.mYHeightPosition = (int) (mYPosition + (highTem - heightTemperature) * item);
		this.mYLowPosition = (int) (mYPosition + (highTem - lowTemperature) * item);
		this.mHeightTemperature = heightTemperature + "℃";
		this.mLowTemperature = lowTemperature + "℃";
		mCanDraw = true;
		invalidate();
	}

	public ChartPointBean getPoint() {
		ChartPointBean chartPointBean = new ChartPointBean();
		chartPointBean.setxPosition(mXPosition);
		chartPointBean.setyHighPosition(mYHeightPosition);
		chartPointBean.setyLowPosition(mYLowPosition);
//		log.d(chartPointBean.toString());
		return chartPointBean;
	}

}
