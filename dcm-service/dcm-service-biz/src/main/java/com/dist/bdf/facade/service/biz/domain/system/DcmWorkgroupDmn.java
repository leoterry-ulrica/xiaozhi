package com.dist.bdf.facade.service.biz.domain.system;

import java.util.List;

import com.dist.bdf.base.domain.GenericDmn;
import com.dist.bdf.model.entity.system.DcmWgOrg;
import com.dist.bdf.model.entity.system.DcmWorkGroup;

public interface DcmWorkgroupDmn  extends GenericDmn<DcmWorkGroup, Long>{

	/**
	 * 添加机构到工作组
	 * @param wgOrg
	 * @return
	 */
	DcmWgOrg saveOrUpdateOrgToWorkgroup(DcmWgOrg wgOrg);
	/**
	 * 删除工作组
	 * @param workgroupId
	 */
	void deleteWorkgroup(Long workgroupId);
	/**
	 * 从工作组删除机构
	 * @param workgroupId
	 * @param orgGuid
	 * @return
	 */
	void deleteOrgFromWorkgroup(Long workgroupId, String orgGuid);
	/**
	 * 根据项目唯一标识删除工作组
	 * @param projectGuid
	 */
	void deleteWorkgroupsByProjectGuid(String projectGuid);
	/**
	 * 根据工作组id获取关联的机构
	 * @param wgId
	 * @return
	 */
	List<DcmWgOrg> getRefOrgs(Long wgId);
	/**
	 * 删除工作组下所有的关联机构
	 * @param workgroupId
	 */
	void deleteOrgsFromWorkgroup(Long workgroupId);
}