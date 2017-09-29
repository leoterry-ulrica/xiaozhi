package com.dist.bdf.model.dto.wechat;

import java.io.Serializable;
/**
 * 错误返回信息
 {"errcode":40029,"errmsg":"invalid code"}
 
 * @author weifj
 *
 */
public class ErrorMsg implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int errcode;
	private String errmsg;
	
	public int getErrcode() {
		return errcode;
	}
	public void setErrcode(int errcode) {
		this.errcode = errcode;
	}
	public String getErrmsg() {
		return errmsg;
	}
	public void setErrmsg(String errmsg) {
		this.errmsg = errmsg;
	}
	
}
