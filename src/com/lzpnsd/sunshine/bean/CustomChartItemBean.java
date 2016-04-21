package com.lzpnsd.sunshine.bean;

public class CustomChartItemBean {
	 private String x;
	    private float y;
	 
	    public CustomChartItemBean(String vx, float vy) {
	        this.x = vx;
	        this.y = vy;
	    }
	 
	    public String getX() {
	        return x;
	    }
	 
	    public void setX(String x) {
	        this.x = x;
	    }
	 
	    public float getY() {
	        return y;
	    }
	 
	    public void setY(float y) {
	        this.y = y;
	    }
}
