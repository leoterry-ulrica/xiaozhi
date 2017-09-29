package com.dist.bdf.facade.service.biz.domain.system;

import java.util.List;

import com.dist.bdf.base.domain.GenericDmn;
import com.dist.bdf.model.entity.system.DcmRegion;

public interface DcmRegionDmn  extends GenericDmn<DcmRegion, Long>{

	/**
	 * 获取所有省
	 * @return
	 */
	public List<String> getProvinces();
	/**
	 * 根据省，获取所有市
	 * @param provinceName
	 * @return
	 */
	public List<String> getCities(String provinceName);
	/**
	 * 获取县列表
	 * @param provinceName
	 * @param cityName
	 * @return
	 */
	public List<String> getCounties(String provinceName, String cityName);

}
