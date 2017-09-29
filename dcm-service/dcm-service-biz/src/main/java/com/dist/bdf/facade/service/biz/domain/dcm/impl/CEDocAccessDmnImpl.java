
package com.dist.bdf.facade.service.biz.domain.dcm.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dist.bdf.facade.service.biz.domain.dcm.CEDocAccessDmn;
import com.dist.bdf.manager.ecm.security.CESecurityOperation;
import com.dist.bdf.manager.ecm.security.PrivilegeFactory;
import com.dist.bdf.manager.ecm.utils.DocumentUtil;
import com.filenet.api.collection.AccessPermissionList;
import com.filenet.api.constants.AccessType;
import com.filenet.api.constants.RefreshMode;
import com.filenet.api.core.Document;
import com.filenet.api.core.Factory;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.security.AccessPermission;

/**
 * @author weifj
 * @version 1.0，2016/02/17，weifj，创建ce文档操作领域
 * @version 1.1，2016/03/03，weifj，添加接口方法inheritDirectParentFolderSecurity的具体实现
 * @version 1.2，2016/03/04，weifj，添加接口方法getValidAccessMasks的具体实现
 */
@Service
@Transactional(propagation = Propagation.REQUIRED) 
public class CEDocAccessDmnImpl extends CEAccessDmnImpl implements CEDocAccessDmn {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private DocumentUtil docUtil;

	@Override
	public AccessPermissionList getPermissions(ObjectStore os, String id) {
		
		Document doc = docUtil.loadById(os, id);

		return doc.get_Permissions();
	}

	@Override
	public void inheritDirectParentFolderSecurity(ObjectStore os,String directParentFolderId, String targetDocId) {
		
		CESecurityOperation.SetSecurityDirectParentFolderToDoc(os, directParentFolderId, targetDocId);	
	}

	@SuppressWarnings("unchecked")
	@Override
	public void authorize(ObjectStore os, int inheritableDepth, String grantee, String resId, Long masks) {
		
		Document doc = docUtil.loadById(os, resId);

		AccessPermissionList apl = this.getPermissions(os, resId);
		AccessPermission ap = this.getAccessPermission(grantee, apl);
		if(null == ap) {
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
		
		doc.set_Permissions(apl);
		doc.save(RefreshMode.REFRESH);
	}

}
