package com.dist.bdf.model.dto.system.iso;

import java.io.Serializable;
import java.text.DecimalFormat;

public class ISOIndexDTO implements Serializable, Comparable<ISOIndexDTO> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 角色名称
	 */
	private String roleName;
	/**
	 * 索引号
	 */
	private int id;
	/**
	 * 微作数
	 */
	private int countOfWz;
	/**
	 * 点赞数
	 */
	private int countOfLike;
	/**
	 * 文档贡献数
	 */
	private int countOfDoc;
	/**
	 * 积分=[@微作数]*0.3+[@点赞数]*0.3+[@文档贡献数]*0.4
	 */
	private Double integral;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getCountOfWz() {
		return countOfWz;
	}
	public void setCountOfWz(int countOfWz) {
		this.countOfWz = countOfWz;
	}
	public int getCountOfLike() {
		return countOfLike;
	}
	public void setCountOfLike(int countOfLike) {
		this.countOfLike = countOfLike;
	}
	public int getCountOfDoc() {
		return countOfDoc;
	}
	public void setCountOfDoc(int countOfDoc) {
		this.countOfDoc = countOfDoc;
	}
	public Double getIntegral() {
		double result = this.countOfWz * 0.3+this.countOfLike * 0.3 + this.countOfDoc * 0.4;
		DecimalFormat decimalFormat=new DecimalFormat(".0");//构造方法的字符格式这里如果小数不足1位,会以0补足.
		String p=decimalFormat.format(result);
		this.integral = Double.valueOf(p);
		return this.integral;
	}
	public void setIntegral(double integral) {
		this.integral = integral;
	}

	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	@Override
	public int compareTo(ISOIndexDTO o) {
		// 升序
		// this.getIntegral().compareTo(o.getIntegral())
		// 降序
		return o.getIntegral().compareTo(this.getIntegral());
	}
	
	public static void main(String[]args){
		int count = 1;
		double result = count * 0.3;
		System.out.println(result);
	}
}
