package com.dist.bdf.base.utils.mail;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.internet.MimeMessage;

import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import com.dist.bdf.base.security.SecurityDefine;
import com.dist.bdf.base.utils.Coder;

/**
 * 邮件工具类
 * @author weifj
 *
 */
public final class MailUtil {

	private static String mailSmtpAuth = "true";
	private static String mailDebug = "true";
	private static String mailSmtpTimeout = "0";
	private static String regexStr = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";  
	
	/**
	 * 发送邮件
	 * @param encoding
	 * @param mailSmtpAuth
	 * @param mailDebug
	 * @param mailSmtpTimeout
	 * @param sender
	 * @param senderPwd
	 * @param subject
	 * @param content
	 * @param receiver
	 * @throws Exception
	 */
	public static void sendTextMessage(String encoding, String mailSmtpAuth, String mailDebug, String mailSmtpTimeout,
			String sender, String senderPwd, String subject, String content, String receiver) throws Exception {

		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setDefaultEncoding(encoding);
		mailSender.setUsername(sender);
		mailSender.setPassword(senderPwd);
		Properties properties = mailSender.getJavaMailProperties();
		properties.setProperty("mail.smtp.auth", mailSmtpAuth);
		properties.setProperty("mail.debug", mailDebug);
		properties.setProperty("mail.smtp.timeout", mailSmtpTimeout);
		mailSender.setHost(SMTPUtil.getSMTPAddress(mailSender.getUsername()));

		MimeMessage mailMessage = mailSender.createMimeMessage();
		MimeMessageHelper messageHelper = new MimeMessageHelper(mailMessage, true);
		messageHelper.setSubject(subject);

		//  true 表示启动HTML格式的邮件
		messageHelper.setText(content, true);
		messageHelper.setTo(receiver);
		messageHelper.setFrom(mailSender.getUsername());

		mailSender.send(mailMessage);
	}
	/**
	 * 发送邮件
	 * @param mailSmtpAuth
	 * @param mailDebug
	 * @param mailSmtpTimeout
	 * @param sender
	 * @param senderPwd
	 * @param subject
	 * @param content
	 * @param receiver
	 * @throws Exception
	 */
	public static void sendTextMessage(String mailSmtpAuth, String mailDebug, String mailSmtpTimeout, String sender,
			String senderPwd, String subject, String content, String receiver) throws Exception {

		sendTextMessage(SecurityDefine.Charset, mailSmtpAuth, mailDebug, mailSmtpTimeout, sender, senderPwd, subject,
				content, receiver);
	}
	/**
	 * 默认设置，发送邮件
	 * @param sender
	 * @param senderPwd
	 * @param subject
	 * @param content
	 * @param receiver
	 * @throws Exception
	 */
	public static void sendTextMessage(String sender,
			String senderPwd, String subject, String content, String receiver) throws Exception {

		sendTextMessage(SecurityDefine.Charset, mailSmtpAuth, mailDebug, mailSmtpTimeout, sender, senderPwd, subject,
				content, receiver);
	}
	
	/**
	 *  检测邮箱的有效性
	 *  合法E-mail地址：     
		1. 必须包含一个并且只有一个符号“@”     
		2. 第一个字符不得是“@”或者“.”     
		3. 不允许出现“@.”或者.@     
		4. 结尾不得是字符“@”或者“.”     
		5. 允许“@”前的字符中出现“＋”     
		6. 不允许“＋”在最前面，或者“＋@”     
		    
		正则表达式如下：     
		-----------------------------------------------------------------------     
		^(\w+((-\w+)|(\.\w+))*)\+\w+((-\w+)|(\.\w+))*\@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z0-9]+$     
		-----------------------------------------------------------------------     
		    
		字符描述：     
		^ ：匹配输入的开始位置。     
		\：将下一个字符标记为特殊字符或字面值。     
		* ：匹配前一个字符零次或几次。     
		+ ：匹配前一个字符一次或多次。     
		(pattern) 与模式匹配并记住匹配。     
		x|y：匹配 x 或 y。     
		[a-z] ：表示某个范围内的字符。与指定区间内的任何字符匹配。     
		\w ：与任何单词字符匹配，包括下划线。     
		$ ：匹配输入的结尾。    
	 */
	public static boolean checkEmail(String email) {

		Pattern regex = Pattern.compile(regexStr);
		Matcher matcher = regex.matcher(email);
		return matcher.matches();
	}
}
