package com.dist.bdf.model.dto.system.iso;

import java.io.Serializable;
import java.util.List;

import com.dist.bdf.model.dto.system.ProjectStatDTO;

/**
 * 
 * @author weifj
 * @version 1.0，2016/05/20，weifj，创建项目统计源数据
 */
public class ISOPrjStatSourceDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 项目基本信息
	 */
	private ISOProjectInfoDTO info;
	/**
	 * 项目的统计信息
	 */
	private List<ProjectStatDTO> stats;
	
	public ISOProjectInfoDTO getInfo() {
		return info;
	}
	public void setInfo(ISOProjectInfoDTO info) {
		this.info = info;
	}
	public List<ProjectStatDTO> getStats() {
		return stats;
	}
	public void setStats(List<ProjectStatDTO> stats) {
		this.stats = stats;
	}
}
