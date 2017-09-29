package com.dist.bdf.model.dto.sga;

import java.util.Date;

import org.hibernate.validator.constraints.NotEmpty;

import com.dist.bdf.model.entity.sga.SgaFileRecord;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class FileRecordDTO extends SgaFileRecord {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@JsonIgnore
	public Long getId(){
		
		return 0L;
	}
	@JsonIgnore
	public String getOpType() {
		return "";
	}
	@JsonIgnore
	public Date getCreateTime() {
		return null;
	}
	@NotEmpty(message = "FileRecordDTO property [resId] can not be empty")
	public String getResId() {
		
		return super.getResId();
	}
}
