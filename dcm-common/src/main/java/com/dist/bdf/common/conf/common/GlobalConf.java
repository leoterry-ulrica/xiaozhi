package com.dist.bdf.common.conf.common;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import io.github.xdiamond.client.XDiamondConfig;
import io.github.xdiamond.client.annotation.EnableConfigListener;

/**
 * 全局配置信息
 * @author weifj
 * @version 1.0，2016/05/13，weifj
 *
 */
@Configuration
@EnableConfigListener
public class GlobalConf {
	
	private static Logger logger = LoggerFactory.getLogger(GlobalConf.class);
	
	@Autowired
	private XDiamondConfig xconf;
	   /**
     * 获取model的值
     * @return
     */
	//@Value(value = "${mode}")
    private String debugMode; 
  /*  @OneKeyListener(key = "mode")
    public void modeListener(ConfigEvent event) {
    	logger.info("modeListener, mode, event : [{}] ", event);
        this.setDebugMode(event.getValue());
    }*/
    
    // @Value(value = "${clearDirectories}")
    // private String clearDirectories; 
    /*@OneKeyListener(key = "clearDirectories")
    public void clearDirectoriesListener(ConfigEvent event) {
    	logger.info("clearDirectoriesListener, clearDirectories, event : [{}] ", event);
        this.setClearDirectories(event.getValue());
    }*/
    
    // private String defaultEncoding;
   /* @OneKeyListener(key = "defaultEncoding")
    public void defaultEncodingListener(ConfigEvent event) {
    	logger.info("defaultEncodingListener, defaultEncoding, event : [{}] ", event);
        this.setDefaultEncoding(event.getValue());
    }*/
    
	public String getDebugMode() {
		
		return this.xconf.getProperty("mode");
		// return debugMode;
	}
/*	public void setDebugMode(String debugModel) {
		this.debugMode = debugModel;
	}*/
	
	 /**
     * 是否debug模式
     * @return
     */
    public boolean isDebugMode(){
    	
    	this.debugMode =  this.getDebugMode();
    	if(null == this.debugMode) return false;
    	
    	return this.debugMode.equalsIgnoreCase("debug");
    }


	public String getClearDirectories() {
		return this.xconf.getProperty("clearDirectories");
		// return clearDirectories;
	}


	/*public void setClearDirectories(String clearDirectories) {
		this.clearDirectories = clearDirectories;
	}*/

	public String getDefaultEncoding() {
		return this.xconf.getProperty("defaultEncoding");
		
		// return defaultEncoding;
	}

	/*public void setDefaultEncoding(String defaultEncoding) {
		this.defaultEncoding = defaultEncoding;
	}*/
	/**
	 * 获取有效期间距值
	 * @return
	 */
	public Long getExpiresValue() {
		
		try {
			String valueStr = this.xconf.getProperty("expires");
			ScriptEngineManager manager = new ScriptEngineManager();
			ScriptEngine engine = manager.getEngineByName("js");
			Object result = engine.eval(valueStr);

			return Long.parseLong(result.toString());
			
		} catch (Exception ex) {
			logger.error(ex.getLocalizedMessage());
		}
		logger.info(">>>使用默认值，七天有效期");
		return 7 * 24 * 60 * 60 * 1000L;
	}

	/**
	 * 判断是否启动缓存
	 * @return
	 */
	public boolean openCache(){
		
		String cacheValue = this.xconf.getProperty("common.cache");
		if(StringUtils.hasLength(cacheValue)){
			if(cacheValue.equals("1"))
				return true;
			
			return Boolean.parseBoolean(cacheValue);
		}	
		
		return false;
	}
}
