package com.lzpnsd.sunshine.bean;

/**
 * 生活指数
 *
 * @author lize
 *
 * 2016年2月3日
 */
public class LifeIndexBean {

	/**
	 * 指数名称  如：晨练指数
	 */
	private String name;
	/**
	 * 指数值  如：较不宜
	 */
	private String value;
	/**
	 * 指数详情   如：较不宜晨练，早晨天气凉，室外锻炼注意保暖防寒。年老体弱人群请减少晨练时间。
	 */
	private String detail;
	
	public LifeIndexBean() {
		super();
	}

	public LifeIndexBean(String name, String value, String detail) {
		super();
		this.name = name;
		this.value = value;
		this.detail = detail;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * @return the detail
	 */
	public String getDetail() {
		return detail;
	}

	/**
	 * @param detail the detail to set
	 */
	public void setDetail(String detail) {
		this.detail = detail;
	}

	@Override
	public String toString() {
		return "LifeIndexBean [name=" + name + ", value=" + value + ", detail=" + detail + "]";
	}

}
