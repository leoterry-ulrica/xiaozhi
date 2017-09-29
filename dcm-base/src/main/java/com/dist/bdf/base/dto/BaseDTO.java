package com.dist.bdf.base.dto;

import java.io.Serializable;

/**
 * DTO公用类
 * @author weifj
 * @version 1.0，2016/04/11，weifj
 */
public class BaseDTO implements Serializable {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected Long id;
    protected String guid;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

	public String getGuid() {
		return guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}

}
