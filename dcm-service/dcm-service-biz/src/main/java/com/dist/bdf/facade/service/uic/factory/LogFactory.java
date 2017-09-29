package com.dist.bdf.facade.service.uic.factory;

import java.util.Date;

import org.springframework.stereotype.Component;

import com.dist.bdf.model.entity.system.DcmLog;
import com.dist.bdf.model.entity.system.DcmUser;

@Component
public class LogFactory {

	private static DcmLog logbean = new DcmLog();
	/**
	 * 创建登录日志对象
	 * @param user
	 * @param host
	 * @param systemName
	 * @param desc
	 * @return
	 */
	public DcmLog createLoginLog(DcmUser user, String host, String systemName, String desc) {
		
		DcmLog log = (DcmLog) logbean.clone();
		log.setCategory("system");
		log.setEventName("login");
		log.setDateTime(new Date());
		log.setDescription(desc);
		log.setHandlers(user.getUserCode());
		log.setMachineAddress(host);
		log.setSystemName(systemName);
		
		return log;
	}
	/**
	 * 创建登出日志对象
	 * @param user
	 * @param host
	 * @param systemName
	 * @param desc
	 * @return
	 */
    public DcmLog createLogoutLog(DcmUser user, String host, String systemName, String desc) {
		
		DcmLog log = (DcmLog) logbean.clone();
		log.setCategory("system");
		log.setEventName("logout");
		log.setDateTime(new Date());
		log.setDescription(desc);
		log.setHandlers(user.getUserCode());
		log.setMachineAddress(host);
		log.setSystemName(systemName);
		
		return log;
	}
}
