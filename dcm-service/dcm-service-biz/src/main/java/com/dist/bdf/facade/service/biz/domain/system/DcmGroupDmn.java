
package com.dist.bdf.facade.service.biz.domain.system;

import java.util.List;
import com.dist.bdf.base.domain.GenericDmn;
import com.dist.bdf.base.page.Pagination;
import com.dist.bdf.model.entity.system.DcmGroup;

/**
 * @author weifj
 * @version 1.0，2016/01/27，创建组领域
 */
public interface DcmGroupDmn extends GenericDmn<DcmGroup, Long> {

    /**
     * 添加组（项目组/讨论组）
     * @param domainType
     * @param groupCode
     * @param groupName
     * @param creator
     * @return
     */
/*	public DcmGroup addGroup(String domainType, String groupCode, String groupName, String creator);*/
	/**
	 * 根据组编码删除组信息
	 * @param groupCode
	 * @return
	 */
	public boolean deleteGroupByCode(String groupCode);
	/**
	 * 根据组guid删除组信息
	 * @param groupGuid
	 * @return
	 */
	public boolean deleteGroupByGuid(String groupGuid);
	/**
	 * 根据id获取组信息
	 * @param id
	 * @return
	 */
	public DcmGroup getGroupById(Long id);
	/**
	 * 根据编码获取组信息
	 * @param code
	 * @return
	 */
	public DcmGroup getGroupByCode(String code);
	/**
	 * 根据guid获取组
	 * @param guid
	 * @return
	 */
	public DcmGroup getGroupByGuid(String guid);
	/**
	 * 获取项目组列表
	 * @return
	 */
	public List<DcmGroup> listProjectGroups();
	/**
	 * 获取讨论组列表
	 * @return
	 */
	public List<DcmGroup> listDiscussionGroups();
	/**
	 * 更新组，只允许更新名称
	 * @param code 组编码
	 * @param name 组名称
	 * @return
	 */
	public DcmGroup updateGroup(String code, String name);
	List<DcmGroup> searchBySQL(String queryString);
	/**
	 * 根据域获取项目组
	 * @param realm
	 * @return
	 */
	public List<DcmGroup> getGroupByRealm(String realm);
	/**
	 * 根据域和类型前缀，分页获取项目组
	 * @param pageNo 页码
	 * @param pageSize 每页大小
	 * @param realm
	 * @param typePrefix
	 * @return
	 */
	Pagination getGroups(int pageNo, int pageSize, String realm, String[] typePrefix);
		
}
