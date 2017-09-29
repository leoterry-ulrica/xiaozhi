
package com.dist.bdf.facade.service.biz.domain.dcm.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.dist.bdf.facade.service.biz.domain.dcm.CEAccessDmn;
import com.dist.bdf.manager.ecm.security.PrivilegeFactory;
import com.filenet.api.collection.AccessPermissionList;
import com.filenet.api.constants.AccessType;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.security.AccessPermission;

/**
 * 创建ce操作领域
 * @author weifj
 * @version 1.0，2016/03/04，weifj，创建ce操作领域
 * @version 1.1，2016/03/04，weifj，添加接口方法：getValidAccessMasks(String grantee, String id)，获取资源在CE中有效权限，去除AccessType.Deny类型的权限
 * @version 1.2，2016/03/05，weifj
 *    1. 添加接口方法：getValidAccessMasks(String grantee, AccessPermissionList apl)
 *    2. 修改方法getValidAccessMasks(String grantee, String id)
 *    3. 添加方法getCEAccessDmnInstance(String ceResType)实现
 *  @version 1.3，2016/03/07，weifj
 *    1. 添加方法实现：authorize(int inheritableDepth ,String grantee, String resId, Long masks)
 *    2. 添加方法实现：AccessPermission getValidAccessPermission(String grantee, String id)
 *    3. 添加方法实现：AccessPermission getAccessPermission(String grantee, AccessPermissionList apl) 
 */
@Service
public abstract class CEAccessDmnImpl implements CEAccessDmn {

/*	
	@Resource
	private CEFolderAccessDmn ceFolderAccessDmn;
	@Resource
	private CEDocAccessDmn ceDocAccessDmn;*/
	
	/*public abstract Object loadById(String id);*/

	/**
	 * 
	 */
	public abstract AccessPermissionList getPermissions(ObjectStore os, String id) ;
	
	public abstract void authorize(ObjectStore os,int inheritableDepth ,String grantee, String resId, Long masks);

	@Override
	public Long getValidAccessMasks(ObjectStore os, String grantee, String id) {
			
		AccessPermissionList apl = this.getPermissions(os, id);
		
		return this.getValidAccessMasks(grantee, apl);

	}
	@Override
	public Long getValidAccessMasks(ObjectStore os,List<String> grantees, String id) {
		
      AccessPermissionList apl = this.getPermissions(os, id);
		
		return this.getValidAccessMasks(grantees, apl);
	}
	
	@Override
	public Long getValidAccessMasks(String grantee, AccessPermissionList apl) {
		
		Long masksAllow = PrivilegeFactory.Priv_None.getMask();

		for (Object object : apl) {
			AccessPermission ap = (AccessPermission) object;
			String granteeName = ap.get_GranteeName();
			
			if (granteeName.equalsIgnoreCase(grantee) ) {
				
				if( ap.get_AccessType() == AccessType.ALLOW){
					// 允许类型
					masksAllow |= ap.get_AccessMask().longValue();
				}	
			}
		}
		// 拒绝类型优先，需要把拒绝类型去掉
		return masksAllow;
	}
	
	@Override
	public Long getValidAccessMasks(List<String> grantees, AccessPermissionList apl) {
		
		Long masksAllow = PrivilegeFactory.Priv_None.getMask();

		for (Object object : apl) {
			AccessPermission ap = (AccessPermission) object;
			String granteeName = ap.get_GranteeName();
			
			if (grantees.contains(granteeName)) {
				
				if( ap.get_AccessType() == AccessType.ALLOW){
					// 允许类型
					masksAllow |= ap.get_AccessMask().longValue();
				}	
			}
		}
		// 拒绝类型优先，需要把拒绝类型去掉
		return masksAllow;
		
	}
	
	@Override
	public AccessPermission getAccessPermission(ObjectStore os,String grantee, String id) {
	
		AccessPermissionList apl = this.getPermissions(os, id);
		AccessPermission ap = null;
		
		for (Object object : apl) {
			ap = (AccessPermission) object;
			String granteeName = ap.get_GranteeName();
			
			if (granteeName.equalsIgnoreCase(grantee)) {
				return ap;
			}
		}
		return ap;
	}
	@Override
	public List<AccessPermission> getAccessPermission(ObjectStore os,List<String> grantees, String id) {
		
		AccessPermissionList apl = this.getPermissions(os, id);
		List<AccessPermission> aps = new ArrayList<AccessPermission>();
		AccessPermission ap = null;
		for (Object object : apl) {
			ap = (AccessPermission) object;
			String granteeName = ap.get_GranteeName();
			
			if (grantees.contains(granteeName)) {
				aps.add(ap);
			}
		}
		return aps;
		
	}
	@Override
	public AccessPermission getAccessPermission(String grantee, AccessPermissionList apl) {
		
		AccessPermission ap = null;
		
		for (Object object : apl) {
			ap = (AccessPermission) object;
			String granteeName = ap.get_GranteeName();
			
			if (granteeName.equalsIgnoreCase(grantee)) {
				return ap;
			}
		}
		return ap;
	}

	/*@Override
	public CEAccessDmn getCEAccessDmnInstance(String ceResType) {
		
		if(ClassNames.DOCUMENT.equalsIgnoreCase(ceResType)){
			
			return this.ceDocAccessDmn;
			
		}else if(ClassNames.FOLDER.equalsIgnoreCase(ceResType)){
			
			return this.ceFolderAccessDmn;
		}
		return null;
	}*/
}
