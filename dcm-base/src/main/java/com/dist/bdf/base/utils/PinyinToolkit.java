package com.dist.bdf.base.utils;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

/**
 * 拼音工具类
 * @author weifj
 * @version 1.0，weifj，2016/06/16，创建
 */
public class PinyinToolkit {

	/** 
	 * 获取汉字串拼音首字母，英文字符不变 
	 * 
	 * @param chinese 汉字串 
	 * @return 汉语拼音首字母 
	 */
	public static String cn2FirstSpell(String chinese) {
		StringBuffer pybf = new StringBuffer();
		char[] arr = chinese.toCharArray();
		HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
		defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		for (int i = 0; i < arr.length; i++) {
			if (arr[i] > 128) {
				try {
					String[] _t = PinyinHelper.toHanyuPinyinStringArray(arr[i], defaultFormat);
					if (_t != null) {
						pybf.append(_t[0].charAt(0));
					}
				} catch (BadHanyuPinyinOutputFormatCombination e) {
					return "";
				}
			} else {
				pybf.append(arr[i]);
			}
		}
		return pybf.toString().replaceAll("\\W", "").trim();
	}

	/** 
	 * 获取汉字串拼音，英文字符不变 
	 * 
	 * @param chinese 汉字串 
	 * @return 汉语拼音 
	 */
	public static String cn2Spell(String chinese) {
		if (chinese == null || chinese.equals("")) {
			return "";
		} else {
			StringBuffer pybf = new StringBuffer();
			char[] arr = chinese.toCharArray();
			HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
			defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
			defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
			for (int i = 0; i < arr.length; i++) {
				if (arr[i] > 128) {
					try {
						String[] str = PinyinHelper.toHanyuPinyinStringArray(arr[i], defaultFormat);
						if (str == null || str.length == 0) {
							break;
						}
						pybf.append(PinyinHelper.toHanyuPinyinStringArray(arr[i], defaultFormat)[0]);
					} catch (BadHanyuPinyinOutputFormatCombination e) {
						break;
					}
				} else {
					pybf.append(arr[i]);
				}
			}
			return pybf.toString();
		}
	}

}
