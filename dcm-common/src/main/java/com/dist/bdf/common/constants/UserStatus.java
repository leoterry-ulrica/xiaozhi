package com.dist.bdf.common.constants;
/**
 * 用户报名状态
 * @author weifj
 *
 */
public final class UserStatus {

	/**
	 * 项目中未报名
	 */
	public static final int PROJECT_NOT_REGISTER = -2; 
	/**
	 * 被项目拒绝
	 */
	public static final int PROJECT_REFUSE = -1;
	/**
	 * 待审核
	 */
	public static final int PROJECT_IN_REVIEW = 0;
	/**
	 * 参与
	 */
	public static final int PROJECT_JOININ = 1;
	/**
	 * 待定（审核中）
	 */
	public static final int PROJECT_UNDETERMINED = 2;

}
