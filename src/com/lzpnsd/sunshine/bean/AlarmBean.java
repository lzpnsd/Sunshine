package com.lzpnsd.sunshine.bean;

/**
 * 天气预警信息
 * 只有在有预警时才有
 *
 * @author lize
 *
 * 2016年2月3日
 */
public class AlarmBean {

	/**
	 * 城市编号  如：1010902
	 */
	private int cityKey;
	/**
	 * 城市名称  如：河北省保定市
	 */
	private String cityName;
	/**
	 * 预警类型  如：霾
	 */
	private String alarmType;
	/**
	 * 预警程度  如：黄色
	 */
	private String alarmDegree;
	/**
	 * 预警简要   如：河北省保定市气象台发布霾黄色预警
	 */
	private String alarmText;
	/**
	 * 预警详情  如：保定市气象台2016年02月03日08时56分继续发布霾黄色预警信号：预计今天白天到夜间，
	 * 我市仍有中到重度霾，气象条件不利于空气污染物的稀释、扩散和清除，空气质量较差，请注意防范。
	 */
	private String alarmDetails;
	/**
	 * 标准  如：
	 * 该属性暂时为空
	 */
	private String standard;
	/**
	 * 建议  如：
	 * 该属性暂时为空
	 */
	private String suggest;
	/**
	 * 预警图片URL  如：http://static.etouch.cn/apps/weather/alarm_icon-1/mai_yellow-1.png
	 */
	private String imgUrl;
	/**
	 * 预警发布时间   如：2016-02-03 08:56:00
	 */
	private String time;
	
	
	public AlarmBean() {
		super();
	}

	public AlarmBean(int cityKey, String cityName, String alarmType, String alarmDegree, String alarmText, String alarmDetails, String imgUrl, String time) {
		super();
		this.cityKey = cityKey;
		this.cityName = cityName;
		this.alarmType = alarmType;
		this.alarmDegree = alarmDegree;
		this.alarmText = alarmText;
		this.alarmDetails = alarmDetails;
		this.imgUrl = imgUrl;
		this.time = time;
	}

	public int getCityKey() {
		return cityKey;
	}

	public void setCityKey(int cityKey) {
		this.cityKey = cityKey;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getAlarmType() {
		return alarmType;
	}

	public void setAlarmType(String alarmType) {
		this.alarmType = alarmType;
	}

	public String getAlarmDegree() {
		return alarmDegree;
	}

	public void setAlarmDegree(String alarmDegree) {
		this.alarmDegree = alarmDegree;
	}

	public String getAlarmText() {
		return alarmText;
	}

	public void setAlarmText(String alarmText) {
		this.alarmText = alarmText;
	}

	public String getAlarmDetails() {
		return alarmDetails;
	}

	public void setAlarmDetails(String alarmDetails) {
		this.alarmDetails = alarmDetails;
	}

	public String getStandard() {
		return standard;
	}

	public void setStandard(String standard) {
		this.standard = standard;
	}

	public String getSuggest() {
		return suggest;
	}

	public void setSuggest(String suggest) {
		this.suggest = suggest;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	@Override
	public String toString() {
		return "AlarmBean [cityKey=" + cityKey + ", cityName=" + cityName + ", alarmType=" + alarmType + ", alarmDegree=" + alarmDegree + ", alarmText=" + alarmText + ", alarmDetails=" + alarmDetails + ", standard=" + standard + ", suggest=" + suggest + ", imgUrl=" + imgUrl + ", time=" + time + "]";
	}
	
}
