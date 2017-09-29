
package com.dist.bdf.facade.service.biz.domain.dcm;

import java.util.List;

import com.filenet.api.collection.AccessPermissionList;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.security.AccessPermission;

/**
 * @author weifj
 * @version 1.0，2016/02/17，创建ce操作领域接口
 * @version 1.1，2016/03/04，添加接口方法：getValidAccessMasks(String grantee, String id)，获取资源在CE中有效权限，去除AccessType.Deny类型的权限
 * @version 1.2，2016/03/05
 *     1. 添加接口方法：getValidAccessMasks(String grantee, AccessPermissionList apl)，获取资源在CE中有效的权限，去除AccessType.Deny类型的权限
 *     2. 添加接口方法：getCEAccessDmnInstance，通过资源类型，获取ce操作的领域对象
 *  @version 1.3，2016/03/07，weifj
 *     1. 添加接口方法： authorize(int inheritableDepth ,String grantee, String resId, Long masks)，对资源进行授权
 *     2. 添加接口方法：AccessPermission getValidAccessPermission(String grantee, String id)
 *     3. 添加接口方法：AccessPermission getAccessPermission(String grantee, AccessPermissionList apl)
 */
public interface CEAccessDmn {

	/**
	 * 获取ce对象的权限列表
	 * @param id
	 * @return
	 */
	public AccessPermissionList getPermissions(ObjectStore os,String id);
	/**
	 * 获取资源在CE中有效的权限mask值，去除AccessType.Deny类型的权限
	 * @param grantee 用户组或者用户
	 * @param id 资源id
	 * @return
	 */
	public Long getValidAccessMasks(ObjectStore os,String grantee, String id);
	public Long getValidAccessMasks(ObjectStore os,List<String> grantees, String id);
	/**
	 * 获取有效的许可操作对象，包括AccessType.Deny类型的权限
	 * @param grantee
	 * @param id
	 * @return
	 */
	public AccessPermission getAccessPermission(ObjectStore os,String grantee, String id);
	public List<AccessPermission> getAccessPermission(ObjectStore os,List<String> grantees, String id);
	/**
	 * 获取资源在CE中有效的权限，去除AccessType.Deny类型的权限
	 * @param grantee
	 * @param apl
	 * @return
	 */
	public Long getValidAccessMasks(String grantee, AccessPermissionList apl);
	public Long getValidAccessMasks(List<String> grantees, AccessPermissionList apl);
	/**
	 * 获取有效的许可操作对象，包括AccessType.Deny类型的权限
	 * @param grantee
	 * @param apl
	 * @return
	 */
	public AccessPermission getAccessPermission(String grantee, AccessPermissionList apl);
	/**
	 * 通过资源类型，获取ce操作的领域对象
	 * @param ceResType
	 * @return
	 */
	//public CEAccessDmn getCEAccessDmnInstance(String ceResType);
	/**
	 * 授权
	 * @param inheritableDepth 继承深度
	 * @param grantee 被授权的对象
	 * @param resId ce资源id
	 * @param masks 权限mask值
	 */
	public void authorize(ObjectStore os, int inheritableDepth ,String grantee, String resId, Long masks);
}
