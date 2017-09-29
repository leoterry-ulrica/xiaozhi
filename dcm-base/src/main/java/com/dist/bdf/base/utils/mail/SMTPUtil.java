package com.dist.bdf.base.utils.mail;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 简单邮件传输协议（SMTP）工具类
 * @author weifj
 *
 */
public class SMTPUtil {

	/**
	 * 简单的smtp生成，大部分是有用的，建议自己建立smtp库....
	 * @param userName
	 * @return
	 */
	public static String SimpleMailSender(String userName) {
		return  "smtp." + getHost(userName);
	}

	/**
	 * 获取smtp地址
	 * @param userName 用户邮箱地址
	 * @return
	 */
	public static String getSMTPAddress(String userName){
		String smtpAddress = null;
		Properties props = new Properties();
		try {
			InputStream in = SMTPUtil.class.getResourceAsStream("smtp.properties");
			props.load(in);
			//读取properties的内容
			smtpAddress = props.getProperty(getHost(userName).trim());
			//没有获取到
			if(smtpAddress == null){
				//生成简单的
				smtpAddress = SimpleMailSender(userName);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return smtpAddress;
	}
	
	/**
	 * 得到 邮箱@后面得字符    
	 * @param userName
	 * @return
	 */
	public static String getHost(String userName){
		return userName.split("@")[1];
	}
	
	public static void main(String[] args) {
		String s = getSMTPAddress("distxiaozhi@dist.com.cn");
		System.out.println(s);
	}
}
