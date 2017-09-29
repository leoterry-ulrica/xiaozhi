package com.dist.bdf.model.dto.system.iso;

import java.io.Serializable;

/**
 * 
 * @author weifj
 * @version 1.0，2016/05/20，weifj，创建项目统计源数据
 */
public class ISOProjectInfoDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 项目名称，XMXX_XMMC
	 */
	private String XMXX_XMMC;
	/**
	 * 规划类型，XMXX_XMGHLB
	 */
	private String XMXX_XMGHLB;
/*	*//**
	 * 项目类型，XMXX_XMLX
	 *//*
	private String XMXX_XMLX;*/
	
	/**
	 * 招标单位
	 */
	private String XMXX_YBYX_ZBDW2;
	/**
	 * 招标机构
	 */
	private String XMXX_YBYX_ZBJG;
	/**
	 * 项目规模
	 */
	private String XMXX_XMGM2;
	/**
	 * 项目负责人
	 */
	private String XMXX_RY_XMFZR;
	/**
	 * 项目区域，XMXX_XMQY（包括了省市县）
	 */
	private String XMXX_XMQY;
	/**
	 * 项目详细地址
	 */
/*	private String XMXX_XMXXDZ;*/
	/**
	 * 应标部门
	 */
	private String XMXX_TB_YBBM;
	/**
	 * 项目立项时间
	 */
	private String XMXX_XMLXSJ;

	/**
	 * 环节和安全目标
	 */
	private String XMXX_XMHJYAQMB;
	/**
	 * 项目基本情况
	 */
	private String XMXX_YBYX_XMJBQK;
	/**
	 * 工程地址
	 */
	private String XMXX_YBYX_MXZBGCDZ;
	/**
	 * 创建时间
	 */
	private String createTime;
	
	public String getXMXX_XMMC() {
		return XMXX_XMMC;
	}
	public void setXMXX_XMMC(String xMXX_XMMC) {
		XMXX_XMMC = xMXX_XMMC;
	}
	public String getXMXX_XMGHLB() {
		return XMXX_XMGHLB;
	}
	public void setXMXX_XMGHLB(String xMXX_XMGHLB) {
		XMXX_XMGHLB = xMXX_XMGHLB;
	}

	public String getXMXX_YBYX_ZBDW2() {
		return XMXX_YBYX_ZBDW2;
	}
	public void setXMXX_YBYX_ZBDW2(String xMXX_YBYX_ZBDW2) {
		XMXX_YBYX_ZBDW2 = xMXX_YBYX_ZBDW2;
	}
	public String getXMXX_YBYX_ZBJG() {
		return XMXX_YBYX_ZBJG;
	}
	public void setXMXX_YBYX_ZBJG(String xMXX_YBYX_ZBJG) {
		XMXX_YBYX_ZBJG = xMXX_YBYX_ZBJG;
	}
	public String getXMXX_XMGM2() {
		return XMXX_XMGM2;
	}
	public void setXMXX_XMGM2(String xMXX_XMGM2) {
		XMXX_XMGM2 = xMXX_XMGM2;
	}
	public String getXMXX_RY_XMFZR() {
		return XMXX_RY_XMFZR;
	}
	public void setXMXX_RY_XMFZR(String xMXX_RY_XMFZR) {
		XMXX_RY_XMFZR = xMXX_RY_XMFZR;
	}
	public String getXMXX_XMQY() {
		return XMXX_XMQY;
	}
	public void setXMXX_XMQY(String xMXX_XMQY) {
		XMXX_XMQY = xMXX_XMQY;
	}
/*	public String getXMXX_XMXXDZ() {
		return XMXX_XMXXDZ;
	}
	public void setXMXX_XMXXDZ(String xMXX_XMXXDZ) {
		XMXX_XMXXDZ = xMXX_XMXXDZ;
	}*/
	public String getXMXX_TB_YBBM() {
		return XMXX_TB_YBBM;
	}
	public void setXMXX_TB_YBBM(String xMXX_TB_YBBM) {
		XMXX_TB_YBBM = xMXX_TB_YBBM;
	}
	public String getXMXX_XMLXSJ() {
		return XMXX_XMLXSJ;
	}
	public void setXMXX_XMLXSJ(String xMXX_XMLXSJ) {
		XMXX_XMLXSJ = xMXX_XMLXSJ;
	}
	public String getXMXX_XMHJYAQMB() {
		return XMXX_XMHJYAQMB;
	}
	public void setXMXX_XMHJYAQMB(String xMXX_XMHJYAQMB) {
		XMXX_XMHJYAQMB = xMXX_XMHJYAQMB;
	}
	public String getXMXX_YBYX_XMJBQK() {
		return XMXX_YBYX_XMJBQK;
	}
	public void setXMXX_YBYX_XMJBQK(String xMXX_YBYX_XMJBQK) {
		XMXX_YBYX_XMJBQK = xMXX_YBYX_XMJBQK;
	}
	public String getXMXX_YBYX_MXZBGCDZ() {
		return XMXX_YBYX_MXZBGCDZ;
	}
	public void setXMXX_YBYX_MXZBGCDZ(String xMXX_YBYX_MXZBGCDZ) {
		XMXX_YBYX_MXZBGCDZ = xMXX_YBYX_MXZBGCDZ;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	
}
