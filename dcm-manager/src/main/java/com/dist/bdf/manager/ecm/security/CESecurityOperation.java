package com.dist.bdf.manager.ecm.security;

import com.filenet.api.collection.AccessPermissionDescriptionList;
import com.filenet.api.collection.AccessPermissionList;
import com.filenet.api.collection.GroupSet;
import com.filenet.api.collection.UserSet;
import com.filenet.api.constants.PrincipalSearchAttribute;
import com.filenet.api.constants.PrincipalSearchType;
import com.filenet.api.constants.PropertyNames;
import com.filenet.api.constants.RefreshMode;
import com.filenet.api.core.Connection;
import com.filenet.api.core.Document;
import com.filenet.api.core.Factory;
import com.filenet.api.core.Folder;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.meta.ClassDescription;
import com.filenet.api.security.AccessPermission;
import com.filenet.api.security.Realm;
import com.filenet.api.util.Id;

import java.util.Iterator;

/**
 * Created by kivi on 2014-8-14.
 * modified by weifj on 2016-1-18.
 *   1.把方法名[getPermission]修改为[getPermissions]
 *   2.添加方法：getPermission
 */
public class CESecurityOperation {

    /**
     * 获取权限信息
     * @param os
     */
    public  static AccessPermissionDescriptionList getPermissionDesc(ObjectStore os,String apdClass) {
        ClassDescription cd = Factory.ClassDescription.getInstance(os,apdClass);
        cd.fetchProperty(PropertyNames.PERMISSION_DESCRIPTIONS,null);
        AccessPermissionDescriptionList apdl = cd.get_PermissionDescriptions();
        return apdl;
    }

    /**
     * 获取权限列表
     * @param os
     * @param id
     */
    public  static AccessPermissionList getPermissions(ObjectStore os,String id) {
    	
        Id id1 = new Id(id);
        Document doc = Factory.Document.fetchInstance(os,id1,null);

        AccessPermissionList apl = doc.get_Permissions();

        for (Iterator it = apl.iterator();it.hasNext();) {
            AccessPermission ap = (AccessPermission)it.next();

            System.out.println("Grantee: " + ap.get_GranteeName());
            System.out.println("AccessType: " + ap.get_AccessType());
            System.out.println("AccessMask: " + ap.get_AccessMask());
        }

        return apl;
    }
    /**
     * 获取指定grantee的权限
     * @param os
     * @param id
     * @param grantee
     * @return
     */
    public  static AccessPermission getPermission(ObjectStore os,String id, String grantee) {
        Id id1 = new Id(id);
        Document doc = Factory.Document.fetchInstance(os,id1,null);

        AccessPermissionList apl = doc.get_Permissions();
        AccessPermission ap = null;
        for (Iterator it = apl.iterator();it.hasNext();) {
            ap = (AccessPermission)it.next();
            if(ap.get_GranteeName().equalsIgnoreCase(grantee)){
            	
            	 System.out.println("Grantee: " + ap.get_GranteeName());
                 System.out.println("AccessType: " + ap.get_AccessType());
                 System.out.println("AccessMask: " + ap.get_AccessMask());
            }

        }

        return ap;
    }
/**
 * 删除指定grantee的权限
 * @param os
 * @param id
 * @param grantee
 */
    public  static void deletePermission(ObjectStore os,String id,String grantee) {
        Id id1 = new Id(id);
        Document doc = Factory.Document.fetchInstance(os,id1,null);

        AccessPermissionList apl = doc.get_Permissions();

        for (Iterator it = apl.iterator();it.hasNext();) {
            AccessPermission ap = (AccessPermission)it.next();

            String granteeName = ap.get_GranteeName();
            if (granteeName.equals(grantee)) {
                it.remove();
                break;
            }
        }
        doc.set_Permissions(apl);
        doc.save(RefreshMode.REFRESH);
    }
/**
 * 修改指定grantee的权限
 * @param os
 * @param id
 * @param grantee
 * @param newPermission
 */
    public  static void alterPermission(ObjectStore os,String id,String grantee,int newPermission) {
        Id id1 = new Id(id);
        Document doc = Factory.Document.fetchInstance(os,id1,null);

        AccessPermissionList apl = doc.get_Permissions();

        AccessPermission newAccessPermission = null;
        for (Iterator it = apl.iterator();it.hasNext();) {
            AccessPermission ap = (AccessPermission)it.next();

            String granteeName = ap.get_GranteeName();
            if (granteeName.equals(grantee)) {
                ap.set_AccessMask(newPermission);
                newAccessPermission = ap;
                it.remove();
                break;
            }
        }
        apl.add(newAccessPermission);
        doc.set_Permissions(apl);
        doc.save(RefreshMode.REFRESH);
    }

    /**
     * 查找用户组
     * @param conn
     * @param searchString
     * @return
     */
    public  static GroupSet findGroups(Connection conn,String searchString) {
        Realm currentRealm = Factory.Realm.fetchCurrent(conn,null);
        /*搜索类型*/
        PrincipalSearchType searchType = PrincipalSearchType.getInstanceFromInt(PrincipalSearchType.CONTAINS_AS_INT);
        /*搜索字段*/
        PrincipalSearchAttribute searchAttribute = PrincipalSearchAttribute.getInstanceFromInt(PrincipalSearchAttribute.DISPLAY_NAME_AS_INT);

        return currentRealm.findGroups(searchString,searchType,searchAttribute,null,10,null);
    }

    /**
     * 查找用户
     * @param conn
     * @param searchString
     * @return
     */
    public  static UserSet findUsers(Connection conn,String searchString) {
        Realm currentRealm = Factory.Realm.fetchCurrent(conn,null);
        /*搜索类型*/
        PrincipalSearchType searchType = PrincipalSearchType.getInstanceFromInt(PrincipalSearchType.CONTAINS_AS_INT);
        /*搜索字段*/
        PrincipalSearchAttribute searchAttribute = PrincipalSearchAttribute.getInstanceFromInt(PrincipalSearchAttribute.DISPLAY_NAME_AS_INT);

        return currentRealm.findUsers(searchString,searchType,searchAttribute,null,10,null);
    }
    
    /**
     * 把直接父文件夹的权限赋予给包含其中的文件，CE本身不支持继承跨父文件夹
     * @param os
     * @param directParentFolderId
     * @param docIds
     */
    public  static void SetSecurityDirectParentFolderToDoc(ObjectStore os, String directParentFolderId, String docId) {
    	
    	Document doc = Factory.Document.fetchInstance(os, new Id(docId), null);
    	Folder directParentFolder = Factory.Folder.fetchInstance(os, new Id(directParentFolderId), null);
    	
    	doc.set_SecurityFolder(directParentFolder);
    	doc.save(RefreshMode.REFRESH);
    }
}
