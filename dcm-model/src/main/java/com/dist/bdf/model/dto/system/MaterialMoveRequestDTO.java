package com.dist.bdf.model.dto.system;

import java.io.Serializable;
import java.util.List;

/**
 * 材料移动的请求参数
 * @author weifj
 *
 *
 {
    "source":[
        {
            "id":"{111000}",
            "type":"d"  // 文档类型，document
        },
        {
            "id":"{2222200}",
            "type":"f" // 文件夹，folder
        }
    ],
    "targetFolder":"{778886632}" // 目标文件夹id
    
}

 */
public class MaterialMoveRequestDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Deprecated
	private String userId;
	@Deprecated
	private String password;
	private String realm;
	/**
	 * 目标文件夹id
	 */
	private String targetFolder;
	/**
	 * 被移动的源信息
	 */
	private List<MaterialMoveSourceDTO> source;
	
	public String getTargetFolder() {
		return targetFolder;
	}
	public void setTargetFolder(String targetFolder) {
		this.targetFolder = targetFolder;
	}
	public List<MaterialMoveSourceDTO> getSource() {
		return source;
	}
	public void setSource(List<MaterialMoveSourceDTO> source) {
		this.source = source;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getRealm() {
		return realm;
	}
	public void setRealm(String realm) {
		this.realm = realm;
	}

}
