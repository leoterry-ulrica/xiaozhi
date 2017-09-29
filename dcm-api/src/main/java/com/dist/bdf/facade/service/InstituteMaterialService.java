package com.dist.bdf.facade.service;


/**
 * 所级资料服务
 * @author weifj
 *
 */
public interface InstituteMaterialService {

	/**
	 * 只有院管的角色才有权限获取
	 * @param realm
	 * @param userId
	 * @return
	 */
	Object getRootFolderAdmin(String realm, String userId);
	/**
	 * 只有院管的角色才有权限获取
	 * @param realm 域
	 * @param userId
	 * @param pwd
	 * @return
	 */
	@Deprecated
	Object getRootFolderAdmin(String realm, String userId, String pwd);
	/**
	 * 只有院管的角色才有权限获取
	 * @param realm
	 * @param userId
	 * @return
	 */
	Object getRootFolderAdminEx(String realm, String userId);
}
