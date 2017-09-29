package com.dist.bdf.base.utils.spring;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

/**
 * 扩展获取properties配置信息类
 * @author weifj
 *
 */
public class PropertyPlaceholder extends PropertyPlaceholderConfigurer {

	private static Map<String, String> propertyMap;
	private Properties properties = new Properties();

	@Override
	protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess, Properties props)
			throws BeansException {
		super.processProperties(beanFactoryToProcess, props);
		// 系统环境变量
		Properties sysProperties = System.getProperties();
		for (Entry<Object, Object> entry : sysProperties.entrySet()) {
			properties.put(entry.getKey(), entry.getValue());
		}
		for (Entry<Object, Object> entry : props.entrySet()) {
			properties.put(entry.getKey(), entry.getValue());
		}
		propertyMap = new HashMap<String, String>();
		for (Object key : props.keySet()) {
			String keyStr = key.toString();
			String value = props.getProperty(keyStr);
			propertyMap.put(keyStr, value);
		}
	}

	//static method for accessing context properties
	public static String getProperty(String name) {
		return propertyMap.get(name);
	}

	public Properties getProperties() {
		return properties;
	}

}
