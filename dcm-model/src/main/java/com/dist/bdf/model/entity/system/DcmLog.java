package com.dist.bdf.model.entity.system;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 
 * 日志记录
 * @author weifj
 * 
 */
@Entity
@Table(name = "DCM_LOG")
public class DcmLog implements Serializable, Cloneable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String category;
	private String eventName;
	private Date dateTime;
	private String machineAddress;
	private String handlers;
	private String description;
	private String systemName;
	
	/**
	 * <pre>主键
	 * 将主键设置为protect，方便其子类访问。
	 * allocationSize：序列每次增长步长
	 * </pre>
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "idGenerator") 
	@SequenceGenerator(name = "idGenerator",  sequenceName="SEQ_DCM_LOG", allocationSize=1)
	protected Long id;

	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 */
	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(name = "CATEGORY")
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	@Column(name = "EVENTNAME")
	public String getEventName() {
		return eventName;
	}
	public void setEventName(String eventName) {
		this.eventName = eventName;
	}
	@Column(name = "DATETIME")
	public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	@Column(name = "EVENTNAME")
	public String getMachineAddress() {
		return machineAddress;
	}
	public void setMachineAddress(String machineAddress) {
		this.machineAddress = machineAddress;
	}
	@Column(name = "HANDLERS")
	public String getHandlers() {
		return handlers;
	}
	public void setHandlers(String handlers) {
		this.handlers = handlers;
	}
	@Column(name = "DESCRIPTION")
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	@Column(name = "SYSTEMNAME")
	public String getSystemName() {
		return systemName;
	}
	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}
	
	@Override
	public Object clone() {
		try {   
            return super.clone();   
        } catch (CloneNotSupportedException e) {   
            return null;   
        }  
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "DcmLog [category=" + category + ", eventName=" + eventName + ", dateTime=" + dateTime
				+ ", machineAddress=" + machineAddress + ", handlers=" + handlers + ", description=" + description
				+ ", systemName=" + systemName + ", id=" + id + "]";
	}
}
