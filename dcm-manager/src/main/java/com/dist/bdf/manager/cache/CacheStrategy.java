package com.dist.bdf.manager.cache;

import java.io.Serializable;
import java.util.Collection;

/**
 * 缓存策略
 * @author weifj
 *
 */
public interface CacheStrategy {

	/**
	 * 批量删除对应的value
	 * 
	 * @param keys
	 */
	public void remove(final String... keys);

	/**
	 * 批量删除key
	 * 
	 * @param pattern
	 */
	public void removePattern(final String pattern);
	/**
	 * 批量删除所有
	 */
	public void removeAll();

	/**
	 * 删除对应的value
	 * 
	 * @param key
	 */
	public void remove(final String key);

	/**
	 * 判断缓存中是否有对应的value
	 * 
	 * @param key
	 * @return
	 */
	public boolean exists(final String key);

	/**
	 * 读取缓存，单值类型
	 * 
	 * @param key
	 * @return
	 */
	public Object get(final String key);
	/**
	 * 读取缓存，list类型
	 * 
	 * @param key
	 * @return
	 */
	public Object getList(final String key);
	/**
	 * 批量读取缓存
	 * @param keys
	 * @return
	 */
	public Object multiGet(final Collection<Serializable> keys);

	/**
	 * 写入缓存
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean set(final String key, Object value);
	/**
	 * 存储list对象
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean setList(final String key, Object value);

	/**
	 * 写入缓存
	 * 
	 * @param key
	 * @param value
	 * @param expireTime 单位：秒，过期时间段
	 * @return
	 */
	public boolean set(final String key, Object value, Long expireTime);

}
