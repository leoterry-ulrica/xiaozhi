package com.dist.bdf.model.dto.system;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class ThumbnailDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private String vid;
	private String name;
	private String mimeType;
	private String img;
	@JsonIgnore
	private byte[] imgByte;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	public String getMimeType() {
		return mimeType;
	}
	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public byte[] getImgByte() {
		return imgByte;
	}
	public void setImgByte(byte[] imgByte) {
		this.imgByte = imgByte;
	}
	public String getVid() {
		return vid;
	}
	public void setVid(String vid) {
		this.vid = vid;
	}
}
