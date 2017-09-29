package com.dist.bdf.common.conf.common;

/*import org.slf4j.Logger;
import org.slf4j.LoggerFactory;*/
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import io.github.xdiamond.client.XDiamondConfig;
import io.github.xdiamond.client.annotation.EnableConfigListener;
/*import io.github.xdiamond.client.annotation.OneKeyListener;
import io.github.xdiamond.client.event.ConfigEvent;*/

/**
 * 邮箱配置
 * 
 * @author weifj
 *
 */
@Configuration
@EnableConfigListener
public class MailConf {

	// private static Logger logger = LoggerFactory.getLogger(MailConf.class);

	@Autowired
	private XDiamondConfig xconf;
	
	// private String userName;

	/*@OneKeyListener(key = "mail.sender.userName")
	public void userNameListener(ConfigEvent event) {
		logger.info("userNameListener, mail.sender.userName, event : [{}] ", event);
		this.setUserName(event.getValue());
	}*/

	// private String password;

	/*@OneKeyListener(key = "mail.sender.password")
	public void passwordListener(ConfigEvent event) {
		logger.info("passwordListener, mail.sender.password, event : [{}] ", event);
		this.setPassword(event.getValue());
	}*/

	// private String mailSmtpAuth;

	/*@OneKeyListener(key = "mail.smtp.auth")
	public void mailSmtpAuthListener(ConfigEvent event) {
		logger.info("mailSmtpAuthListener, mail.smtp.auth, event : [{}] ", event);
		this.setMailSmtpAuth(event.getValue());
	}*/

	// private String mailDebug;

	/*@OneKeyListener(key = "mail.debug")
	public void mailDebugListener(ConfigEvent event) {
		logger.info("mailDebugListener, mail.debug, event : [{}] ", event);
		this.setMailDebug(event.getValue());
	}*/

	// private String mailSmtpTimeout;

	/*@OneKeyListener(key = "mail.smtp.timeout")
	public void mailSmtpTimeoutListener(ConfigEvent event) {
		logger.info("mailSmtpTimeoutListener, mail.smtp.timeout, event : [{}] ", event);
		this.setMailSmtpTimeout(event.getValue());
	}*/

	public String getUserName() {
		return xconf.getProperty("mail.sender.userName");
	}

	/*public void setUserName(String userName) {
		this.userName = userName;
	}*/

	public String getPassword() {
		return this.xconf.getProperty("mail.sender.password");
	}

	/*public void setPassword(String password) {
		this.password = password;
	}
*/
	public String getMailSmtpAuth() {
		return this.xconf.getProperty("mail.smtp.auth");
	}

/*	public void setMailSmtpAuth(String mailSmtpAuth) {
		this.mailSmtpAuth = mailSmtpAuth;
	}*/

	public String getMailDebug() {
		return this.xconf.getProperty("mail.debug");
	}

	/*public void setMailDebug(String mailDebug) {
		this.mailDebug = mailDebug;
	}*/

	public String getMailSmtpTimeout() {
		return this.xconf.getProperty("mail.smtp.timeout");
	}

/*	public void setMailSmtpTimeout(String mailSmtpTimeout) {
		this.mailSmtpTimeout = mailSmtpTimeout;
	}*/
	
}
