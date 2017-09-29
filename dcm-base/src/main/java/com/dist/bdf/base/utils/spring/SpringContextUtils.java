package com.dist.bdf.base.utils.spring;

import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 *  获取spring上下文工具类
 * @author weifj
 *
 */
@Component
public final class SpringContextUtils implements  ApplicationContextAware {

	private static ApplicationContext applicationContext;
	
	@Override
    public void setApplicationContext(ApplicationContext context)
            throws BeansException {
        applicationContext = context;
    }
 
    /**
     * 获取applicationContext对象
     * @return
     */
    public static ApplicationContext getApplicationContext(){
        return applicationContext;
    }
     
    /**
     * 根据bean的id来查找对象
     * @param id
     * @return
     */
    @SuppressWarnings("unchecked")
	public static <T> T getBeanById(String id){
        return (T)applicationContext.getBean(id);
    }
     
    /**
     * 根据bean的class来查找对象
     * @param c
     * @return
     */
    public static Object getBeanByClass(Class<?> c){
        return applicationContext.getBean(c);
    }
     
    /**
     * 根据bean的class来查找所有的对象(包括子类)
     * @param c
     * @return
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public static Map getBeansByClass(Class c){
        return applicationContext.getBeansOfType(c);
    }
    
}
