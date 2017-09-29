package com.dist.bdf.facade.service.biz.domain.dcm.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dist.bdf.facade.service.biz.domain.dcm.CEFolderAccessDmn;
import com.dist.bdf.manager.ecm.security.PrivilegeFactory;
import com.dist.bdf.manager.ecm.utils.FolderUtil;
import com.filenet.api.collection.AccessPermissionList;
import com.filenet.api.constants.AccessType;
import com.filenet.api.constants.RefreshMode;
import com.filenet.api.core.Factory;
import com.filenet.api.core.Folder;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.security.AccessPermission;

/**
 * 文档访问权限领域层实现类
 * @author 李其云
 * @version 1.0 2015.11.12
 * @version 1.1 2015.11.14
 * @version 1.2 2015.11.16 处理CE的异常
 * <p>
 * CE EngineRuntimeException:<br/>
 * errorid=[FNRCA0024],key=[API_PROPERTY_NOT_IN_CACHE]：属性不存在<br/>
 * errorid=[FNRCE0051],key=[E_OBJECT_NOT_FOUND]：对象不存在<br/>
 * </p>
 */
@Service
@Transactional(propagation = Propagation.REQUIRED) 
public class CEFolderAccessDmnImpl  extends CEAccessDmnImpl  implements CEFolderAccessDmn {
	protected final Logger logger = (Logger) LoggerFactory.getLogger(getClass());

	@Autowired
	private FolderUtil folderUtil;
	
	private Folder rootFolder = null;

	public CEFolderAccessDmnImpl() {
		//ConnectionService.initialize();
	}
	@Override
	public AccessPermissionList getPermissions(ObjectStore os, String id) {

		Folder folder = folderUtil.loadById(os, id);
		
		return folder.get_Permissions();
	}

	/**
	 * 获取应用的根文件夹对象。由于应用使用的RootFolder不一定是ce的RootFolder，因以此使用公共方法获取
	 * @return
	 */
	

	/**
	 * CE的rootFolder是不变的，因此可以缓存起来
	 */
	@Override
	public Folder getRootFolder(ObjectStore os) {
		
		if (this.rootFolder == null) {
	
			this.rootFolder = os.get_RootFolder();

		}
			
		return this.rootFolder;
	}

	@Override
	public Long getValidAccessMasks(ObjectStore os, String grantee, String id) {
		
		Long masksAllow = PrivilegeFactory.Priv_None.getMask();
		Long masksDeny = PrivilegeFactory.Priv_None.getMask();
		
		AccessPermissionList apl = this.getPermissions(os, id);
		for (Object object : apl) {
			AccessPermission ap = (AccessPermission) object;
			String granteeName = ap.get_GranteeName();
			
			if (granteeName.equalsIgnoreCase(grantee)) {
				
				if( ap.get_AccessType() == AccessType.ALLOW){
					// 允许类型
					masksAllow |= ap.get_AccessMask().longValue();
					
				}else {
					// 拒绝类型
					masksDeny |=  ap.get_AccessMask().longValue();
					
				}		
			}
		}
		// 拒绝类型优先
		return masksAllow & ~masksDeny;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void authorize(ObjectStore os, int inheritableDepth, String grantee, String resId, Long masks) {
	
		Folder folder = this.folderUtil.loadById(os, resId);
		AccessPermissionList apl = this.getPermissions(os, resId);
		AccessPermission ap = this.getAccessPermission(grantee, apl);
		if(null == ap){
			if (PrivilegeFactory.Priv_None.getMask() != masks){// masks不为空
				// 说明未给此grantee对此资源授予权限，需要添加
				ap = Factory.AccessPermission.createInstance();
				ap.set_GranteeName(grantee);
				ap.set_AccessType(AccessType.ALLOW);
				ap.set_AccessMask(Integer.parseInt(String.valueOf(masks)));
				ap.set_InheritableDepth(inheritableDepth);
				apl.add(ap);
			}
		} else {
			if (PrivilegeFactory.Priv_None.getMask() != masks){
				// 修改权限
				ap.set_AccessType(AccessType.ALLOW);
				ap.set_AccessMask(Integer.parseInt(String.valueOf(masks)));
				ap.set_InheritableDepth(inheritableDepth);
			} else {
				// 如果masks是空，则说明取消对grantee授权
				apl.remove(ap);
			}
		}
		folder.set_Permissions(apl);
		folder.save(RefreshMode.REFRESH);
	}

}
