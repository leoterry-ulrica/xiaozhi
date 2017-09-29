package com.dist.bdf.facade.service.biz.domain.dcm;

import com.filenet.api.collection.AccessPermissionList;
import com.filenet.api.core.Folder;
import com.filenet.api.core.ObjectStore;

public interface CEFolderAccessDmn  extends CEAccessDmn {

	AccessPermissionList getPermissions(ObjectStore os, String id);
	 Folder getRootFolder(ObjectStore os);

}
