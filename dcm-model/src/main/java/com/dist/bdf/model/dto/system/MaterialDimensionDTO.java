package com.dist.bdf.model.dto.system;

import java.util.ArrayList;
import java.util.List;

import com.dist.bdf.base.dto.BaseDTO;
import com.dist.bdf.model.entity.system.DcmMaterialDimension;

/**
 * 
 * @author weifj
 *
 */
public class MaterialDimensionDTO extends BaseDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long parentId;
	private String name;
	private Long depth;
	private Long orderId;
	
	private List<MaterialDimensionDTO> children = new ArrayList<MaterialDimensionDTO>();
	
	/**
	 * 父节点id
	 * @return
	 */
	public Long getParentId() {
		return parentId;
	}
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	/**
	 * 名字
	 * @return
	 */
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * 级别深度
	 * @return
	 */
	public Long getDepth() {
		return depth;
	}
	public void setDepth(Long depth) {
		this.depth = depth;
	}
	/**
	 * 排序号
	 * @return
	 */
	public Long getOrderId() {
		return orderId;
	}
	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}
	
	public MaterialDimensionDTO clone(DcmMaterialDimension md){
		
		this.id = md.getId();
		this.parentId = md.getParentId();
		this.name = md.getName();
		this.depth = md.getDepth();
		this.orderId = md.getOrderId();
		
		return this;
	}
	public List<MaterialDimensionDTO> getChildren() {
		return children;
	}
	public void setChildren(List<MaterialDimensionDTO> children) {
		this.children = children;
	}
}
