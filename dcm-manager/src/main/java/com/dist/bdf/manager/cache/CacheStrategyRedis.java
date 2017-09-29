package com.dist.bdf.manager.cache;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

/**
 * redis cache 
 * @author weifj
 *
 */
public final class CacheStrategyRedis implements CacheStrategy {
	
	private Logger logger = LoggerFactory.getLogger(CacheStrategyRedis.class);

	private RedisTemplate<Serializable, Object> redisTemplate;

	/**
	 * 批量删除对应的value
	 * 
	 * @param keys
	 */
	public void remove(final String... keys) {
	
		logger.debug(">>>remove cache...");
		for (String key : keys) {
			remove(key);
		}
	}

	/**
	 * 批量删除key，参数为正则表达式，否则删除失败
	 * 
	 * @param pattern
	 */
	public void removePattern(final String pattern) {
		Set<Serializable> keys = redisTemplate.keys(pattern);
		if (keys.size() > 0)
			redisTemplate.delete(keys);
	}
	/**
	 * 批量删除所有
	 */
	public void removeAll() {
		
		removePattern("*");
	}

	/**
	 * 删除对应的value
	 * 
	 * @param key
	 */
	public void remove(final String key) {
		if (exists(key)) {
			redisTemplate.delete(key);
		}
	}

	/**
	 * 判断缓存中是否有对应的value
	 * 
	 * @param key
	 * @return
	 */
	public boolean exists(final String key) {
		return redisTemplate.hasKey(key);
	}

	/**
	 * 读取缓存，单值类型
	 * 
	 * @param key
	 * @return
	 */
	public Object get(final String key) {

		Object result = null;
		ValueOperations<Serializable, Object> operations = redisTemplate
				.opsForValue();
		// result = redisTemplate.boundValueOps(key).get();
		result = operations.get(key);
		return result;
	}
	/**
	 * 读取缓存，list类型
	 * 
	 * @param key
	 * @return
	 */
	public Object getList(final String key) {
		
		Object result = null;
		ListOperations<Serializable, Object> opsForList = redisTemplate.opsForList();
		result = opsForList.index(key, 0);//..rightPop(key);pop是返回并移除
		
		return result;
	}
	/**
	 * 批量读取缓存
	 * @param keys
	 * @return
	 */
	public Object multiGet(final Collection<Serializable> keys) {
		List<Object> result = null;
		ValueOperations<Serializable, Object> operations = redisTemplate
				.opsForValue();
		result = operations.multiGet(keys);
		return result;
	}

	/**
	 * 写入缓存
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean set(final String key, Object value) {
		boolean result = false;
		try {
			ValueOperations<Serializable, Object> operations = redisTemplate
					.opsForValue();
			operations.set(key, value);
			result = true;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return result;
	}
	
	public boolean setList(final String key, Object value) {
		
		boolean result = false;
		try {
			ListOperations <Serializable, Object> opsForList = redisTemplate.opsForList();
			opsForList.rightPush(key, value);
			result = true;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return result;
	}

	/**
	 * 写入缓存
	 * 
	 * @param key
	 * @param value
	 * @param expireTime 单位：秒，过期时间段
	 * @return
	 */
	public boolean set(final String key, Object value, Long expireTime) {
		boolean result = false;
		try {
			ValueOperations<Serializable, Object> operations = redisTemplate
					.opsForValue();
			operations.set(key, value);
			redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
			result = true;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return result;
	}

	public void setRedisTemplate(
			RedisTemplate<Serializable, Object> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}
}