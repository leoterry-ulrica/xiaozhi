package com.dist.bdf.model.dto.system;

import java.io.Serializable;

import com.dist.bdf.model.dto.system.user.BaseUserDTO;

/**
 * 项目统计DTO
 * @author weifj
 * @version 1.0，2016/05/17，weifj，创建
 */
public class ProjectStatDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 角色
	 */
	//private DcmRole role;
	private BaseUserDTO user;
	/**
	 * 用户编码
	 */
	private String userCode;

	/**
	 * 指标信息
	 */
	private IndexInfo index;
	/**
	 * 积分 = 微作数*0.7 + 被点赞数*0.2 + 评论数*0.1
	 */
	private Double point;
	
	/*public DcmRole getRole() {
		return role;
	}
	public void setRole(DcmRole role) {
		this.role = role;
	}*/
	
	public IndexInfo getIndex() {
		return index;
	}
	public void setIndex(IndexInfo index) {
		this.index = index;
	}
	
	public void setIndexInfo(int countOfWz, int countOfLike, int countOfComment, int countOfDoc){
		if(null == this.index){
			IndexInfo info = new IndexInfo(countOfWz, countOfLike, countOfComment, countOfDoc);
			this.setIndex(info);
		}else{
			this.index.setCountOfComment(this.index.getCountOfComment() + countOfComment);
			this.index.setCountOfDoc(this.index.getCountOfDoc() + countOfDoc);
			this.index.setCountOfLike(this.index.getCountOfLike() + countOfLike);
			this.index.setCountOfWz(this.index.getCountOfWz() + countOfWz);
		}
		
	}
	public BaseUserDTO getUser() {
		return user;
	}
	public void setUser(BaseUserDTO user) {
		this.user = user;
	}
	public Double getPoint() {
		return point;
	}
	public void setPoint(Double point) {
		this.point = point;
	}
	public String getUserCode() {
		return userCode;
	}
	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}
}

