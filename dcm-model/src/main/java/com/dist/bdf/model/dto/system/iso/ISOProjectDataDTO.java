package com.dist.bdf.model.dto.system.iso;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ISOProjectDataDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 项目基本信息
	 */
	private ISOProjectInfoDTO info;
	/**
	 * 指标信息列表
	 */
	private List<ISOIndexDTO> index = new ArrayList<ISOIndexDTO>();
	/**
	 * 排名前几位的指标列表，用于生成统计图
	 */
	private List<ISOIndexDTO> indexTop = new ArrayList<ISOIndexDTO>();
	
	public List<ISOIndexDTO> getIndexTop() {
		return indexTop;
	}

	public void setIndexTop(List<ISOIndexDTO> indexTop) {
		this.indexTop = indexTop;
	}

	public ISOProjectInfoDTO getInfo() {
		return info;
	}

	public void setInfo(ISOProjectInfoDTO info) {
		this.info = info;
	}

	public List<ISOIndexDTO> getIndex() {
		return index;
	}

	public void setIndex(List<ISOIndexDTO> index) {
		this.index = index;
	}

	
}
