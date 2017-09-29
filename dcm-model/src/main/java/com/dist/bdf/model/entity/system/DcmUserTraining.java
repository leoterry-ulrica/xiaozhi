package com.dist.bdf.model.entity.system;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.dist.bdf.base.entity.BaseEntity;

@Entity
@Table(name = "DCM_USER_TRAINING")
public class DcmUserTraining extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	private Date trainTime;
	private String address;
	private String organizer;
	private Long userId;
	
	@Column(name = "NAME")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "USERID")
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}

	@Column(name = "TRAINTIME")
	public Date getTrainTime() {
		return trainTime;
	}
	public void setTrainTime(Date trainTime) {
		this.trainTime = trainTime;
	}
	@Column(name = "ADDRESS")
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	@Column(name = "ORGANIZER")
	public String getOrganizer() {
		return organizer;
	}
	public void setOrganizer(String organizer) {
		this.organizer = organizer;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "DcmUserTraining [name=" + name + ", trainTime=" + trainTime + ", address=" + address + ", organizer="
				+ organizer + ", userId=" + userId + "]";
	}

}
