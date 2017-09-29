package com.dist.bdf.base.constants;

/**
 * 类描述： 用于组织后台的提示信息，比如：账户/密码错误、账户已经被锁定等 <br />
 * 常量命名规则如下：模块名称_提示信息简称，如：FAVORITE_NOT_UNIQUE_NAME<br />
 * 常量注释规则如下：模块名称:提示信息描述，如：收藏夹:文档信息已存在 <br />
 * 如果该提示为通用，命名和注释均无须加上模块名称。
 * @创建人：沈宇汀 
 * @创建时间：2014-12-30 下午1:08:01<br />
 * 
 */
public class MessageContants {
	/**
	 * 通用：您未登陆
	 */
	public static final String NO_LOGIN = "您未登陆";

	/**
	 * 收藏夹：已经收藏
	 */
	public static final String FAVORITE_HAD_OBJECT = "您已经收藏";

	/**
	 * 收藏夹：您未选择需要收藏的文件
	 */
	public static final String FAVORITE_NO_DOCUMENT = "您未选择需要收藏的文件";

	/**
	 * 收藏夹：您未选择需要收藏的文件夹
	 */
	public static final String FAVORITE_NO_FOLDER = "您未选择需要收藏的文件夹";
	
	/**
	 * 收藏夹：请选中需要取消收藏的文件/文件夹
	 */
	public static final String FAVORITE_NO_OBJECT = "请选中需要取消收藏的文件/文件夹";
}
