package com.dist.bdf.model.dto.system;

import java.io.Serializable;

import org.hibernate.validator.constraints.NotEmpty;

public class TestParaValidDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@NotEmpty(message = "name can not empty")
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
