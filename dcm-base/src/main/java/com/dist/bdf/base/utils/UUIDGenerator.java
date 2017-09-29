package com.dist.bdf.base.utils;

import java.util.UUID;

public final class UUIDGenerator {

	/**
	 * 生成guid
	 * @param hyphens 是否带横杠“-”
	 * @param upper 是否大写
	 * @param braces 是否带有花括号“{}”
	 * @return
	 */
	public static String getUUID(boolean hyphens, boolean upper, boolean braces) {
		
		UUID uuid = UUID.randomUUID();
		String str = uuid.toString();
		if(!hyphens){
			str = str.replace("-", "");
		}
		if(upper){
			str = str.toUpperCase();
		}else {
			str = str.toLowerCase();
		}
		if(braces)
			str = "{"+str+"}";
		
		return str;
	}


}
