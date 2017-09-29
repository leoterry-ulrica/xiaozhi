package com.dist.bdf.model.dto.system;

import java.io.Serializable;

import com.dist.bdf.model.dto.dcm.PropertiesExDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
/**
 * 
 * @author weifj
 * @version 1.0，2016/05/10，weifj，作为新建微作传入的参数信息
 */
public class WzInfoParaDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String userId;
	@Deprecated
	private String pwd;
	private String caseIdentifier;
	private String caseId;
	private String tache;
	private String content;
	private String task;
	private String[] atPersons;
	/**
	 * 域，desktop值
	 */
	private String realm;
	/**
	 * 微作类型
	 * 0：普通微作，1：调研微作，2：任务微作
	 */
	private Integer type;

	/**
	 * 扩展属性
	 */
	private PropertiesExDTO propertiesEx;
	/**
	 * 是否执行真正上传。true/false
	 */
	private boolean upload;
	/**
	 * 文件版本系列id
	 */
	private String version;
	/**
	 * 维度，-90~90，负数表示南纬
	 */
	private Double latitude;
	/**
	 * 经度，-180~180，负数表示西经
	 */
	private Double longitude;
	
	private String locationName;
	private String address;
	/**
	 * 用户
	 * @return
	 */
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	/**
	 * 密码
	 * @return
	 */
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	/**
	 * 案例标识
	 * @return
	 */
	public String getCaseIdentifier() {
		return caseIdentifier;
	}
	public void setCaseIdentifier(String caseIdentifier) {
		this.caseIdentifier = caseIdentifier;
	}
	/**
	 * 关联环节
	 * @return
	 */
	public String getTache() {
		return tache;
	}
	public void setTache(String tache) {
		this.tache = tache;
	}
	/**
	 * 微作内容
	 * @return
	 */
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	/**
	 * 关联的任务
	 * @return
	 */
	public String getTask() {
		return task;
	}
	public void setTask(String task) {
		this.task = task;
	}
	/**
	 * @的人
	 * @return
	 */
	public String[] getAtPersons() {
		return atPersons;
	}
	public void setAtPersons(String[] atPersons) {
		this.atPersons = atPersons;
	}
	public PropertiesExDTO getPropertiesEx() {
		return propertiesEx;
	}
	public void setPropertiesEx(PropertiesExDTO propertiesEx) {
		this.propertiesEx = propertiesEx;
	}
	public String getCaseId() {
		return caseId;
	}
	public void setCaseId(String caseId) {
		this.caseId = caseId;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public String getRealm() {
		return realm;
	}
	public void setRealm(String realm) {
		this.realm = realm;
	}
	public boolean getUpload() {
		return upload;
	}
	public void setUpload(boolean upload) {
		this.upload = upload;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public Double getLatitude() {
		return latitude;
	}
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	public Double getLongitude() {
		return longitude;
	}
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	/**
	 * 位置名称
	 * @return
	 */
	public String getLocationName() {
		return locationName;
	}
	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}
	/**
	 * 详细地址
	 * @return
	 */
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
}
