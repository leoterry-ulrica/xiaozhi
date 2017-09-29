package com.dist.bdf.facade.service;

/**
 * 管理员服务
 * @author weifj
 *
 */
public interface AdminService {

	/**
	 * 根据条件，获取全局的统计信息
	 * @param realm 域
	 * @param admin 管理员账号
	 * @param pwd 管理员密码
	 * @param operate  可以是：'<>' | '<' | '>' | '<=' | '>='
	 * @param timeStart 开始时间，以毫秒（ms）为单位
	 * @param timeEnd 结束时间，以毫秒（ms）为单位
	 * @return
	 */
	Object getGlobalStat(String realm, String admin, String pwd, String operate, Long timeStart, long timeEnd);
}
