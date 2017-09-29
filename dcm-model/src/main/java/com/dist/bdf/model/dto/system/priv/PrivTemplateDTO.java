package com.dist.bdf.model.dto.system.priv;

import java.io.Serializable;
/**
 * 权限模板DTO
 * @author weifj
 *
 */
public class PrivTemplateDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String privCode;
	private Integer scope;
	public String getPrivCode() {
		return privCode;
	}
	public void setPrivCode(String privCode) {
		this.privCode = privCode;
	}
	public Integer getScope() {
		return scope;
	}
	public void setScope(Integer scope) {
		this.scope = scope;
	}
	@Override
	public String toString() {
		return "PrivTemplateDTO [privCode=" + privCode + ", scope=" + scope + "]";
	}

}
