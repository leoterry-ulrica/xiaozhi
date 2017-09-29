package com.dist.bdf.facade.service;

/**
 * 所级资料服务
 * @author weifj
 *
 */
public interface DepartmentMaterialService {

	/**
	 * 只有所管的角色才有权限获取
	 * @param userId
	 * @param realm 域
	 * @return
	 */
	//@Deprecated
	//Object getRootFolderAdmin(String userId, String pwd);
	Object getRootFolderAdmin(String realm, String userId);
	/**
	 * 只有所管的角色才有权限获取
	 * @param realm
	 * @param userId
	 * @param pwd
	 * @return
	 */
	Object getRootFolderAdmin(String realm, String userId, String pwd);
	/**
	 * 
	 * 只有所管的角色才有权限获取，包括部门下面的团队
	 * @param realm
	 * @param userId
	 * @return
	 */
	Object getRootAndTeamFolderAdmin(String realm, String userId);

}
