package com.dist.bdf.model.entity.system;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.dist.bdf.base.entity.BaseEntity;

/**
 * 资料维度模型
 * @author weifj
 * @version 1.0，2016/04/19，weifj，创建
 * 
 */
@Entity
@Table(name= "DCM_MATERIALDIMENSION")
public class DcmMaterialDimension extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long parentId;
	private String name;
	private Long depth;
	private Long isBuildIn;
	private Long orderId;
	
	@Column(name="PARENTID")
	public Long getParentId() {
		return parentId;
	}
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	@Column(name="NAME")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Column(name="DEPTH")
	public Long getDepth() {
		return depth;
	}
	public void setDepth(Long depth) {
		this.depth = depth;
	}
	@Column(name="ISBUILDIN")
	public Long getIsBuildIn() {
		return isBuildIn;
	}
	public void setIsBuildIn(Long isBuildIn) {
		this.isBuildIn = isBuildIn;
	}
	@Column(name="ORDERID")
	public Long getOrderId() {
		return orderId;
	}
	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "DcmMaterialDimension [parentId=" + parentId + ", name=" + name + ", depth=" + depth + ", isBuildIn="
				+ isBuildIn + ", orderId=" + orderId + "]";
	}
	
}
