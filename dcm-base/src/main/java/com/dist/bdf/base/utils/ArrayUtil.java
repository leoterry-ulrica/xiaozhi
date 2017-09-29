package com.dist.bdf.base.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.dist.bdf.base.entity.BaseEntity;
import com.dist.bdf.base.exception.BusinessException;

/**
 * 数组处理的工具类
 * @author HeShun
 * 2014年9月11日
 */
@SuppressWarnings({ "rawtypes" })
public class ArrayUtil {

	/**
	 * 获取多个实体的id字符串，id之间用“,”隔开。
	 * @author 何顺
	 * @param coll 实体列表
	 * @return
	 */
	public static String getIdString(Collection<?> coll) {
		Iterator it = coll.iterator();
		String ids = "";
		while (it.hasNext()) {
			BaseEntity entity = (BaseEntity) it.next();
			Long id = entity.getId();
			ids += ",'" + id + "'";
		}
		if (!StringUtils.isBlank(ids)) {
			ids = ids.substring(1);
		}
		return ids;
	}

	/**
	 * 获取多个实体的id字符串，id之间用“,”隔开。
	 * @author 何顺
	 * @param coll 实体列表
	 * @return
	 */
	public static String getIdString(List<String> coll) {
		Iterator it = coll.iterator();
		String ids = "";
		while (it.hasNext()) {
			String id = (String) it.next();
			ids += "," + id;
		}
		if (!StringUtils.isBlank(ids)) {
			ids = ids.substring(1);
		}
		return ids;
	}

	/**
	 * 获取多个实体的id字符串，id之间用“,”隔开。
	 * @author 何顺
	 * @param ids id数组
	 * @return id字符串，id之间用“,”隔开。
	 */
	public static String getIdString(String[] ids) {

		String idStr = "";
		for (String id : ids) {
			if (!StringUtils.isBlank(id)) {
				//解决有时id带单引号的问题。
				if (id.startsWith("'")) {
					idStr += "," + id;
				} else {
					idStr += ",'" + id + "'";
				}
			}
		}
		if (!StringUtils.isBlank(idStr)) {
			idStr = idStr.substring(1);
		}
		return idStr;
	}

	/**
	 * 将逗号分隔的数字字符串转换为Long数组
	 * @param str 以splitter分隔的字符串，每个分隔的内容将会转为一个Long
	 * @param splitter 分隔字符（串）
	 * @return Long[]。
	 * @author 李其云 2015-9-17
	 */
	public static Long[] string2LongArray(String str, String splitter) {
		Long[] elements;
		if (str == null) {
			elements = new Long[0];
		} else {
			String[] arr = str.split(splitter);
			elements = new Long[arr.length];
			for (int i = 0; i < arr.length; i++) {
				try {
					elements[i] = Long.parseLong(arr[i]);
				} catch (NumberFormatException e) {
					throw new BusinessException("字符串[{}]无法转换为Long型", arr[i]);
				}
			}
		}
		return elements;
	}

	/**
	 * 将逗号分隔的数字字符串转换为Long数组
	 * @param str 以逗号","分隔的字符串，每个逗号分隔的内容将会转为一个Long
	 * @return Long[]。
	 * @author 李其云
	 */
	public static Long[] string2LongArray(String str) {
		return string2LongArray(str, ",");
	}

	/**
	 * 判断集合是否为空
	 * @author 何顺
	 * @param coll
	 * @return true 表示集合为空
	 */
	public static boolean isEmpty(Collection<?> coll) {
		if (coll != null) {
			if (coll.isEmpty() || coll.size() == 0) {
				return true;
			} else {
				return false;
			}
		}
		return true;
	}

	/**
	 * 判断集合是否为空
	 * @author 何顺
	 * @param coll
	 * @return true 表示集合为空
	 */
	public static boolean notEmpty(Collection<?> coll) {
		return !isEmpty(coll);
	}
	
	/**
	 * 去除Long数组中的重复数据
	 * @param array Long数组
	 * @return
	 */
	public static Long[] distinct(Long[] array){
		if (array==null)
			return null;
		if (array.length==0)
			return array;
		List<Long> list = new ArrayList<Long>();
		for (Long l : array) {
			if (list.contains(l))
				break;
			list.add(l);
		}
		Long[] ret=new Long[list.size()];
		return list.toArray(ret);
	}
}
