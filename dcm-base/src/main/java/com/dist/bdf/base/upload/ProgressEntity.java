package com.dist.bdf.base.upload;

import java.io.Serializable;

import com.dist.bdf.base.utils.JSONUtil;
import com.dist.bdf.base.utils.NumUtil;

/**
 * 文件上传进度信息
 * @author weifj
 * @version 1.0，2016/05/24，weifj，创建进度条实体
 */
public class ProgressEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 已读字节
	 */
	private long bytesRead = 0L;
	/**
	 * 已读MB
	 */
	private String mbRead = "0";
	/**
	 * 总长度
	 */
	private long contentLength = 0L;
	/****/
	private int items;
	/**
	 * 已读百分比
	 */
	private String percent;
	/**
	 * 读取速度
	 */
	private String speed;
	/**
	 * 开始读取的时间
	 */
	private long startReatTime = System.currentTimeMillis();

	public long getBytesRead() {
		return bytesRead;
	}

	public void setBytesRead(long bytesRead) {
		this.bytesRead = bytesRead;
	}

	public long getContentLength() {
		return contentLength;
	}

	public void setContentLength(long contentLength) {
		this.contentLength = contentLength;
	}

	public int getItems() {
		return items;
	}

	public void setItems(int items) {
		this.items = items;
	}

	public String getPercent() {
		percent = NumUtil.getPercent(bytesRead, contentLength);
		return percent;
	}

	public void setPercent(String percent) {
		this.percent = percent;
	}

	public String getSpeed() {
		speed = NumUtil.divideNumber(
								NumUtil.divideNumber(bytesRead * 1000, System.currentTimeMillis()- startReatTime), 
								1000);
		return speed;
	}

	public void setSpeed(String speed) {
		this.speed = speed;
	}

	public long getStartReatTime() {
		return startReatTime;
	}

	public void setStartReatTime(long startReatTime) {
		this.startReatTime = startReatTime;
	}

	public String getMbRead() {
		mbRead = NumUtil.divideNumber(bytesRead, 1000000);
		return mbRead;
	}

	public void setMbRead(String mbRead) {
		this.mbRead = mbRead;
	}

	@Override
	public String toString() {
		return JSONUtil.toJSONString(this);
	}
}