package com.lzpnsd.sunshine.bean;

public class PageViewData {

	private int num1;
	private int date;
	private int pageViewValue;
	public PageViewData(int num1, int date, int pageViewValue) {
		super();
		this.num1 = num1;
		this.date = date;
		this.pageViewValue = pageViewValue;
	}
	public int getNum1() {
		return num1;
	}
	public void setNum1(int num1) {
		this.num1 = num1;
	}
	public int getDate() {
		return date;
	}
	public void setDate(int date) {
		this.date = date;
	}
	public int getPageViewValue() {
		return pageViewValue;
	}
	public void setPageViewValue(int pageViewValue) {
		this.pageViewValue = pageViewValue;
	}
	
}
