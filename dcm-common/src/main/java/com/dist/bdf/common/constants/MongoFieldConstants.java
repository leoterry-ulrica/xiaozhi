package com.dist.bdf.common.constants;

/**
 * mongodb属性字段命名
 * mongodb 的 schema free 导致了每笔数据都要存储它的 key 以及属性，这导致了这些数据的大量冗余，所以必须精简。
 * @author weifj
 *
 */
public final class MongoFieldConstants {

	/**
	 * 名称
	 */
	public static final String NAME = "n";
	/**
	 * 描述信息
	 */
	public static final String DESCRIPTION = "desc";
	/**
	 * 数据库类型
	 */
	public static final String DATABASE_TYPE = "dt";
	/**
	 * 服务器
	 */
	public static final String SERVER = "sv";
	/**
	 * 数据库名称
	 */
	public static final String DATABASE_NAME = "dn";
	/**
	 * 数据库用户
	 */
	public static final String DATABASE_USER = "du";
	/**
	 * 数据库密码
	 */
	public static final String DATABASE_PASSWORD = "dp";
	/**
	 * 登录用户名称
	 */
	public static final String LOGIN_USER = "lu";
	/**
	 * 登录用户密码
	 */
	public static final String LOGIN_PASSWORD = "lp";
	/**
	 * MD5编码
	 */
	public static final String MD5_CODE = "md5";
	/**
	 * 扩展名
	 */
	public static final String EXTENSION_NAME = "ext";
	/**
	 * 方案复数
	 */
	public static final String PROPOSAL_COMPLEX = "pros";
	/**
	 * 当前方案文件名称
	 */
	public static final String CURRENT_PROPOSAL_FILE = "cpf";
	/**
	 * ecm中文档id
	 */
	public static final String CE_DOC_ID = "cid";
	/**
	 * ecm中文档版本系列id
	 */
	public static final String CE_VERSION_ID = "vid";
	/**
	 * 域
	 */
	public static final String REALM = "rm";
	/**
	 * mongodb默认主键
	 */
	public static final String ID = "_id";
}
