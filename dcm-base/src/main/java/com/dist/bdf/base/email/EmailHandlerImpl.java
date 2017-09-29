package com.dist.bdf.base.email;

import java.io.InputStream;
import java.util.Date;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


import org.springframework.stereotype.Service;

@Deprecated
@Service("emailHandler")
public class EmailHandlerImpl implements EmailHandler {

	public void sendEmail(String emailAddress, String subject, String emailContext) {
		Properties p = new Properties();
		InputStream in = this.getClass().getClassLoader().getResourceAsStream("email.properties");
		try {
			p.load(in);
			String host = p.getProperty("host");
			String userName = p.getProperty("username");
			String password = p.getProperty("password");
			String account = p.getProperty("account");
			String accountDisplayName = p.getProperty("accountDisplayName");

			Properties props = new Properties(); // 获取系统环境
			Authenticator auth = new EmailAutherticator(userName, password); // 进行邮件服务器用户认证
			props.put("mail.smtp.host", host);
			props.put("mail.smtp.auth", "true");
			Session session = Session.getDefaultInstance(props, auth); // 设置session,和邮件服务器进行通讯。
			MimeMessage message = new MimeMessage(session); // 设置邮件格式
			message.setSubject(subject); // 设置邮件主题
			message.setText(emailContext); // 设置邮件正文
			message.setHeader(subject, subject); // 设置邮件标题
			message.setSentDate(new Date()); // 设置邮件发送日期
			Address address = new InternetAddress(account, accountDisplayName);
			message.setFrom(address); // 设置邮件发送者的地址
			Address toAddress = new InternetAddress(emailAddress); // 设置邮件接收方的地址
			message.addRecipient(Message.RecipientType.TO, toAddress);
			Transport.send(message); // 发送邮件
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 用来进行服务器对用户的认证
	 */
	private class EmailAutherticator extends Authenticator {
		String username;
		String password;

		public EmailAutherticator(String user, String pwd) {
			super();
			username = user;
			password = pwd;
		}

		public PasswordAuthentication getPasswordAuthentication() {
			return new PasswordAuthentication(username, password);
		}
	}
}
