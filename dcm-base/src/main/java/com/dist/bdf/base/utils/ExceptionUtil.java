package com.dist.bdf.base.utils;

public class ExceptionUtil {

	/**
	 * 获取异常的所有信息形成的字符串，类似于e.printStackTrace
	 * @param e
	 * @return
	 */
	public static String getFullMessage(Exception e) {
		StringBuffer message = new StringBuffer();
		message.append(e.getClass().getName()).append(" : ").append(e.getMessage()).append("\n");

		StackTraceElement[] elements = e.getStackTrace();
		for (StackTraceElement element : elements) {
			message.append(element.toString()).append("\n");
		}
		return message.toString();
	}
}
