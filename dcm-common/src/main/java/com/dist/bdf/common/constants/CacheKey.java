package com.dist.bdf.common.constants;

/**
 * 
 * 缓存key管理
 * 尽量保持key值够小
 * @author weifj
 *
 */
public final  class CacheKey {

	/**
	 * 匹配用户key的模式
	 */
	public static final String PREFIX_USER_PATTERN = "User_*";
	/**
	 * 用户
	 */
	public static final String PREFIX_USER = "User_";
	
	/**
	 * 匹配机构key的模式
	 */
	public static final String PREFIX_ORG_PATTERN = "Org_*";
	/**
	 * 机构
	 */
	public static final String PREFIX_ORG = "Org_";
	/**
	 * 机构用户树数据缓存key
	 */
	public final static String PREFIX_ORG_USER_TREE_LOADUSER = "OrgUserTree_LoadUser";
	/**
	 * 机构树数据缓存key
	 */
	public final static String PREFIX_ORG_USER_TREE_NOTLOADUSER = "OrgUserTree_NotLoadUser";
	/**
	 * 域下用户的缓存key
	 */
	public final static String CACHEKEY_REALM_USER = "Realm_Users_";
	/**
	 * 域下外部用户的缓存key
	 */
	public final static String CACHEKEY_REALM_SGA_USER = "Realm_SGA_Users_";
	/**
	 * 匹配域-用户key的模式
	 */
	public static final String PREFIX_REALM_USER_PATTERN = "Realm_Users_*";
	/**
	 * 域下外部用户key的模式
	 */
	public final static String PREFIX_REALM_SGA_USER_PATTERN = "Realm_SGA_Users_*";
	
	/**
	 * 匹配角色key的模式
	 */
	public static final String PREFIX_ROLE_PATTERN = "Role_*";
	/**
	 * 角色
	 */
	public static final String PREFIX_ROLE = "Role_";
	
	/**
	 * 行政区域树
	 */
	public static final String PREFIX_REGION_TREE = "Region_Tree_";
	/**
	 * 项目
	 */
	public static final String PREFIX_PROJECT = "Project_";
	
	/**
	 * 公共用户前缀key
	 */
	public static final String PREFIX_SGA_USER = "User_";
	/**
	 * 公共用户id前缀key
	 */
	public static final String PREFIX_SGA_USER_ID = "User_ID_";

}
