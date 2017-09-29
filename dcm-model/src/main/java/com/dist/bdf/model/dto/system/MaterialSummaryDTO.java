package com.dist.bdf.model.dto.system;

import java.io.Serializable;
import java.util.Date;
/**
 * 资料汇总模型
 * @author weifj
 *
 */
public class MaterialSummaryDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 总文件数
	 */
	private Double contentElementCount = 0.0;
	/**
	 * 总文件大小，以kb为单位
	 */
	private Double contentElementKBytes = 0.0;
	/**
	 * 总文件大小，以mb为单位
	 */
	private Double contentElementMBytes = 0.0;
	/**
	 * 总文件大小，以gb为单位
	 */
	private Double contentElementGBytes = 0.0;
	/**
	 * 总文件大小，以tb为单位
	 */
	private Double contentElementTBytes= 0.0;
	/**
	 * 最后修改时间
	 */
	private Date dateLastModified;
	/**
	 * 资料来源数
	 */
	private String materialSourceCount;
	
	public Double getContentElementCount() {
		return contentElementCount;
	}
	public void setContentElementCount(Double contentElementCount) {
		this.contentElementCount = contentElementCount;
	}
	public Double getContentElementKBytes() {
		return contentElementKBytes;
	}
	public void setContentElementKBytes(Double contentElementKBytes) {
		this.contentElementKBytes = contentElementKBytes;
	}
	public Double getContentElementMBytes() {
		return contentElementMBytes;
	}
	public void setContentElementMBytes(Double contentElementMBytes) {
		this.contentElementMBytes = contentElementMBytes;
	}
	public Double getContentElementGBytes() {
		return contentElementGBytes;
	}
	public void setContentElementGBytes(Double contentElementGBytes) {
		this.contentElementGBytes = contentElementGBytes;
	}
	public Double getContentElementTBytes() {
		return contentElementTBytes;
	}
	public void setContentElementTBytes(Double contentElementTBytes) {
		this.contentElementTBytes = contentElementTBytes;
	}
	public Date getDateLastModified() {
		return dateLastModified;
	}
	public void setDateLastModified(Date dateLastModified) {
		this.dateLastModified = dateLastModified;
	}
	public String getMaterialSourceCount() {
		return materialSourceCount;
	}
	public void setMaterialSourceCount(String materialSourceCount) {
		this.materialSourceCount = materialSourceCount;
	}
	@Override
	public String toString() {
		return "MaterialSummaryDTO [contentElementCount=" + contentElementCount + ", contentElementKBytes="
				+ contentElementKBytes + ", contentElementMBytes=" + contentElementMBytes + ", contentElementGBytes="
				+ contentElementGBytes + ", contentElementTBytes=" + contentElementTBytes + ", dateLastModified="
				+ dateLastModified + ", materialSourceCount=" + materialSourceCount + "]";
	}
}
