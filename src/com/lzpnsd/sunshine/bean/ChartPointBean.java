package com.lzpnsd.sunshine.bean;

/**
 * Chart的点
 *
 * @author lize
 *
 * 2016年4月19日
 */
public class ChartPointBean {

	private float xChartView;
	private float yChartView;
	private float xPosition;
	private float yHighPosition;
	private float yLowPosition;
	
	public ChartPointBean() {
		super();
	}

	public float getxChartView() {
		return xChartView;
	}

	public void setxChartView(float xChartView) {
		this.xChartView = xChartView;
	}

	public float getyChartView() {
		return yChartView;
	}

	public void setyChartView(float yChartView) {
		this.yChartView = yChartView;
	}

	public float getxPosition() {
		return xPosition;
	}

	public void setxPosition(float xPosition) {
		this.xPosition = xPosition;
	}

	public float getyHighPosition() {
		return yHighPosition;
	}

	public void setyHighPosition(float yHighPosition) {
		this.yHighPosition = yHighPosition;
	}

	public float getyLowPosition() {
		return yLowPosition;
	}

	public void setyLowPosition(float yLowPosition) {
		this.yLowPosition = yLowPosition;
	}

	@Override
	public String toString() {
		return "ChartPointBean [xChartView=" + xChartView + ", yChartView=" + yChartView + ", xPosition=" + xPosition + ", yHighPosition=" + yHighPosition + ", yLowPosition=" + yLowPosition + "]";
	}

}
