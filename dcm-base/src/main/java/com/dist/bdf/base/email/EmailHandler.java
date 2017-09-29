package com.dist.bdf.base.email;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 邮件发送接口
 * @author
 *
 */
@Deprecated
@Transactional(propagation = Propagation.REQUIRED)
public interface EmailHandler {
	/**
	 * 发送电子邮件到指定账户
	 * @param emailAddress:目标电子邮件地址
	 * @param subject:主题
	 * @param emailContext：邮件内容（纯文本）
	 * @return
	 */
	public void sendEmail(String emailAddress, String subject, String emailContext);
}
