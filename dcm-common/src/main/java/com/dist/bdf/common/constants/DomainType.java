
package com.dist.bdf.common.constants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author weifj
 * @version 1.0，2016/03/23，weifj，创建空间域类型常量
 */
public final class DomainType {

	private final static Logger logger = LoggerFactory.getLogger(DomainType.class);
	/**
	 * 项目组
	 */
	public static final String PROJECT = "Domain_Project";
	/**
	 * 讨论组
	 */
	public static final String DISCUSSION = "Domain_Discussion";

	/**
	 * 所的空间域类型编码
	 */
	public static final String DEPARTMENT = "Domain_Department";
	/**
	 * 院的空间域类型编码
	 */
	public static final String INSTITUTE = "Domain_Institute";

	/**
	 * 个人的空间域类型编码
	 */
	public static final String PERSON = "Domain_Person";
	/**
	 * 系统域
	 */
	public static final String SYSTEM = "Domain_System";

	public static String getDomainTypeCode(int clientResType) {

		String code = "";
		switch (clientResType) {
		case 1:
			code = PERSON;
			break;
		case 2:
			code = PROJECT;
			break;
		case 3:
			code = DEPARTMENT;
			break;
		case 4:
			code = INSTITUTE;
			break;
		case 5:
			code = DISCUSSION;
			break;
		default:
			logger.warn("传入的资源类型int="+clientResType+"，在模型中没有找到对应的类型。");
			break;
		}
		return code;
	}
	
	/**
	 * 根据服务端资源编码映射成客户端所需的资源类型
	 * @param resTypeCode
	 * @return
	 */
   public static int getClientResType(String  domainTypeCode){
		
		int clientResType = -1;
		switch (domainTypeCode) {
		case PERSON:
			clientResType = 1;
			break;
		case PROJECT:
			clientResType = 2;
			break;
		case DEPARTMENT:
			clientResType = 3;
			break;
		case INSTITUTE:
			clientResType = 4;
			break;
		case DISCUSSION:
			clientResType = 5;
			break;
		default:
			logger.warn("传入的服务端资源类型String="+domainTypeCode+"，在没有找到对应的类型代码。");
			break;
		}
		return clientResType;
	}
}
