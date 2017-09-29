package com.dist.bdf.model.dto.system;

import java.io.Serializable;

/**
 * 材料移动源信息参数
 * @author weifj
 *
 *
 {
            "id":"{111000}",
            "type":"d"  // 文档类型，document
        }
        或者
        {
            "id":"{2222200}",
            "type":"f" // 文件夹，folder
        }

 */
public class MaterialMoveSourceDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	/**
	 * d：文档类型，document
	 * f: 文件夹，folder
	 */
	private String type;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
}
