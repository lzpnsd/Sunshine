package com.lzpnsd.sunshine.view;

import com.lzpnsd.sunshine.R;
import com.lzpnsd.sunshine.bean.ChartPointBean;
import com.lzpnsd.sunshine.bean.WeatherInfoBean;
import com.lzpnsd.sunshine.util.CommonUtil;
import com.lzpnsd.sunshine.util.LogUtil;
import com.lzpnsd.sunshine.util.WeatherIconUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CustomItemView extends LinearLayout{

	private LogUtil log = LogUtil.getLog(getClass());
	
	private Context mContext;
	
	private TextView mTvWeek;
	private TextView mTvDayWeather;
	private ImageView mIvDayPic;
	private CustomChartView mCustomChartView;
	private ImageView mIvNightPic;
	private TextView mTvNightWeather;
	private TextView mTvDate;
	private TextView mTvWindDirection;
	private TextView mTvWindPower;
	
	private WeatherInfoBean mWeatherInfoBean;

	private float mHighTem;
	private float mLowTem;
	
	private int mXPosition;
	private int mYPosition;
	
	private int mCallCount = 0;
	
	public CustomItemView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.mContext = context;
		initView();
	}

	public CustomItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
		initView();
	}

	public CustomItemView(Context context) {
		super(context);
		this.mContext = context;
		initView();
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		setOrientation(LinearLayout.VERTICAL);
		showPoint(mHighTem,mLowTem);
		mCallCount++;
		
		int[] location = new int[2];
		getLocationOnScreen(location);
		mXPosition = location[0];
		mYPosition = location[1];
		log.d("mXPosition = "+mXPosition);
		log.d("mYPosition = "+mYPosition);
	}
	
	private void initView(){
		LinearLayout chartItemView = (LinearLayout) ((LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.view_chart_item, null);
		mTvWeek = (TextView) chartItemView.findViewById(R.id.tv_chart_item_week);
		mTvDayWeather = (TextView) chartItemView.findViewById(R.id.tv_chart_item_day_weather);
		mIvDayPic = (ImageView) chartItemView.findViewById(R.id.iv_chart_item_day_pic);
		mCustomChartView = (CustomChartView) chartItemView.findViewById(R.id.ccv_chart_item_chart);
		mIvNightPic = (ImageView) chartItemView.findViewById(R.id.iv_chart_item_night_pic);
		mTvNightWeather = (TextView) chartItemView.findViewById(R.id.tv_chart_item_night_weather);
		mTvDate = (TextView) chartItemView.findViewById(R.id.tv_chart_item_date);
		mTvWindDirection = (TextView) chartItemView.findViewById(R.id.tv_chart_item_wind_direction);
		mTvWindPower = (TextView) chartItemView.findViewById(R.id.tv_chart_item_wind_power);
		this.addView(chartItemView);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		log.d("onDraw");
	}
	
	@Override
	protected void dispatchDraw(Canvas canvas) {
		super.dispatchDraw(canvas);
		log.d("dispatchDraw");
	}
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		log.d("dispatchTouchEvent");
		switch (ev.getAction()) {
			case MotionEvent.ACTION_DOWN:
				setBackgroundColor(getResources().getColor(R.color.trans_44));
				break;
			case MotionEvent.ACTION_CANCEL:
				setBackgroundColor(getResources().getColor(R.color.trans));
				break;
			case MotionEvent.ACTION_UP:
				setBackgroundColor(getResources().getColor(R.color.trans));
				break;
		}
		return super.dispatchTouchEvent(ev);
	}
	
	public void initData(WeatherInfoBean weatherInfoBean){
		this.mWeatherInfoBean = weatherInfoBean;
		String date = weatherInfoBean.getDate();
		mTvWeek.setText(date.substring(date.length()-2, date.length()));
		mTvDayWeather.setText(weatherInfoBean.getDayType());
		log.d("weatherInfoBean.getDayType = "+weatherInfoBean.getDayType()+"image resource = "+WeatherIconUtil.getSmallImageResource(weatherInfoBean.getDayType()));
		log.d("weatherInfoBean.getNightType = "+weatherInfoBean.getNightType()+"image resource = "+WeatherIconUtil.getSmallImageResource(weatherInfoBean.getNightType()));
		ImageLoader.getInstance().displayImage("drawable://"+WeatherIconUtil.getSmallImageResource(weatherInfoBean.getDayType()), mIvDayPic);
		ImageLoader.getInstance().displayImage("drawable://"+WeatherIconUtil.getSmallImageResource(weatherInfoBean.getNightType()), mIvNightPic);
//		mIvDayPic.setImageResource(WeatherIconUtil.getImageResource(weatherInfoBean.getDayType()));
//		mIvNightPic.setImageResource(WeatherIconUtil.getImageResource(weatherInfoBean.getNightType()));
		mTvNightWeather.setText(weatherInfoBean.getNightType());
		mTvDate.setText(date.subSequence(0, date.length()-2));
		mTvWindDirection.setText(weatherInfoBean.getDayWindDirection());
		mTvWindPower.setText(weatherInfoBean.getDayWindPower());
	}
	
	public void showPoint(float highTem, float lowTem) {
		this.mHighTem = highTem;
		this.mLowTem = lowTem;
		if(mCallCount >=3){
			log.d("setData()");
			mCustomChartView.setData(mHighTem, mLowTem, Integer.parseInt(mWeatherInfoBean.getHighTemperature()), Integer.parseInt(mWeatherInfoBean.getLowTemperature()));
		}
	}
	
	public CustomChartView getCustomChartView(){
		return mCustomChartView;
	}
	
	public ChartPointBean getChartPoint(){
		ChartPointBean chartPointBean = mCustomChartView.getPoint();
		chartPointBean.setxChartView(mCustomChartView.getLeft());
		chartPointBean.setyChartView(mCustomChartView.getTop());
		return chartPointBean;
	}
	
}
