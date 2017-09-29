package com.dist.bdf.base.constants;

/**
 * Created by ShenYuTing on 20-11-2014.
 */
public class SessionContants {
	/**
	 * session中的当前用户
	 */
	public final static String USER_KEY = "CURRENT_USER";
	/**
	 * 企业版本session中的当前用户
	 */
	public final static String USER_KEY_2B = "CURRENT_USER_2B";
	/**
	 * 公众版版本session中的当前用户
	 */
	public final static String USER_KEY_2C = "CURRENT_USER_2C";

	/**
	 * session中的当前用户ID
	 */
	public final static String USER_ID_KEY = "CURRENT_USER_ID";

	/**
	 * 放在session中的可用菜单的key
	 */
	public static final String ENABLE_MENU_KEY = "ENABLE_MENU";

	/**
	 * 所有的访问资源点（被纳入系统权限管理的资源点）
	 */
	public static final String ALL_RESOURCE_KEY = "ALL_RESOURCE";
	/**
	 * 能否使用的按钮信息
	 */
	public static final String ENABLE_BUTTONS = "ENABLE_BUTTONS";

	/**
	 * 上传文件的百分比
	 */
	public static final String UPLOAD_FILE = "uploadFileProgress";
	/**
	 * 文件名称
	 */
	public static String DOCUMENT_NAME = "documentName";
}
