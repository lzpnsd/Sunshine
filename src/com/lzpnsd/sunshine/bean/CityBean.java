package com.lzpnsd.sunshine.bean;

/**
 * 城市信息
 *
 * @author lize
 *
 * 2016年2月3日
 */
public class CityBean {

	/**
	 * 区域ID
	 */
	private String areaId;
	/**
	 * 城市英文名字
	 */
	private String nameEn;
	/**
	 * 城市中文名字
	 */
	private String nameCn;
	/**
	 * 城市所属区域英文名字
	 */
	private String districtEn;
	/**
	 * 城市所属区域中文名字
	 */
	private String districtCn;
	/**
	 * 城市所在省份英文名称
	 */
	private String provEn;
	/**
	 * 城市所在省份中文名称
	 */
	private String provCn;
	/**
	 * 城市所在国家英文名称
	 */
	private String nationEn;
	/**
	 * 城市所在国家中文名称
	 */
	private String nationCn;
	
	
	public CityBean() {
		super();
	}

	public CityBean(String areaId, String nameEn, String nameCn, String districtEn, String districtCn, String provEn, String provCn, String nationEn, String nationCn) {
		super();
		this.areaId = areaId;
		this.nameEn = nameEn;
		this.nameCn = nameCn;
		this.districtEn = districtEn;
		this.districtCn = districtCn;
		this.provEn = provEn;
		this.provCn = provCn;
		this.nationEn = nationEn;
		this.nationCn = nationCn;
	}


	public String getAreaId() {
		return areaId;
	}


	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}


	public String getNameEn() {
		return nameEn;
	}


	public void setNameEn(String nameEn) {
		this.nameEn = nameEn;
	}


	public String getNameCn() {
		return nameCn;
	}


	public void setNameCn(String nameCn) {
		this.nameCn = nameCn;
	}


	public String getDistrictEn() {
		return districtEn;
	}


	public void setDistrictEn(String districtEn) {
		this.districtEn = districtEn;
	}


	public String getDistrictCn() {
		return districtCn;
	}


	public void setDistrictCn(String districtCn) {
		this.districtCn = districtCn;
	}


	public String getProvEn() {
		return provEn;
	}


	public void setProvEn(String provEn) {
		this.provEn = provEn;
	}


	public String getProvCn() {
		return provCn;
	}


	public void setProvCn(String provCn) {
		this.provCn = provCn;
	}


	public String getNationEn() {
		return nationEn;
	}


	public void setNationEn(String nationEn) {
		this.nationEn = nationEn;
	}


	public String getNationCn() {
		return nationCn;
	}


	public void setNationCn(String nationCn) {
		this.nationCn = nationCn;
	}


	@Override
	public String toString() {
		return "CityBean [areaId=" + areaId + ", nameEn=" + nameEn + ", nameCn=" + nameCn + ", districtEn=" + districtEn + ", districtCn=" + districtCn + ", provEn=" + provEn + ", provCn=" + provCn + ", nationEn=" + nationEn + ", nationCn=" + nationCn + "]";
	}
	
}
