
package com.dist.bdf.facade.service.biz.domain.system;

import java.util.List;

import com.dist.bdf.base.domain.GenericDmn;
import com.dist.bdf.model.dto.dcm.DocumentDTO;
import com.dist.bdf.model.entity.system.DcmPersonalmaterial;
import com.filenet.api.core.Folder;
import com.filenet.api.core.ObjectStore;

/**
 * 资源类型领域层
 * @author weifj
 * @version 1.0，2016/01/28，创建资源类型领域层
 */
public interface DcmPersonalMaterialDmn extends GenericDmn<DcmPersonalmaterial, Long> {

	/**
	 * 根据资源id获取
	 * @param resId
	 * @return
	 */
	DcmPersonalmaterial getByResId(String resId);
	/**
	 * 获取个人包
	 * @param userCode 用户编码
	 * @return
	 */
	List<DocumentDTO> getPersonalPackages(ObjectStore os, String userCode);
	/**
	 * 获取个人空间路径
	 * @param userCode 用户编码
	 * @return
	 */
	String getPersonalPath(String userCode);
	/**
	 * 获取个人根文件夹
	 * @param userCode
	 * @return
	 */
	Folder getPersonalFolder(ObjectStore os, String userCode);

}
