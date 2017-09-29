package com.dist.bdf.model.entity.system;

import javax.persistence.Column;

import com.dist.bdf.base.entity.BaseEntity;

public class DcmRegion  extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String province;
	private String city;
	private String county;
	
	/**
	 * 省
	 * @return
	 */
	@Column(name="PROVINCE")
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	/**
	 * 市
	 * @return
	 */
	@Column(name="CITY")
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	/**
	 * 县
	 * @return
	 */
	@Column(name="COUNTY")
	public String getCounty() {
		return county;
	}
	public void setCounty(String county) {
		this.county = county;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "DcmRegion [province=" + province + ", city=" + city + ", county=" + county + "]";
	}
}
