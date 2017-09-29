package com.dist.bdf.common.conf.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import io.github.xdiamond.client.XDiamondConfig;
import io.github.xdiamond.client.annotation.EnableConfigListener;
//import io.github.xdiamond.client.annotation.OneKeyListener;
//import io.github.xdiamond.client.event.ConfigEvent;

/**
 * 
 * @author weifj
 * @version 1.0，2016/0513，weifj，创建officeonline配置信息。
 *
 */
@Configuration
@EnableConfigListener
public class LdapConf {

	private Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private XDiamondConfig xconf;
	  /**
     * 获取ldap url
     * @return
     */
	// @Value(value = "${ldap.url}")
    // private  String url; 
   /* @OneKeyListener(key = "ldap.url")
    public void urlListener(ConfigEvent event) {
    	logger.info("urlListener, ldap.url, event : [{}] ", event);
        this.setUrl(event.getValue());
    }*/
  
    /**
     * 获取ldap port
     * @return
     */
	/*@Value(value = "${ldap.port}")
    private  String portStr; 
    @OneKeyListener(key = "ldap.port")
    public void portListener(ConfigEvent event) {
    	logger.info("portListener, ldap.port, event : [{}] ", event);
        this.setPortStr(event.getValue());
    }
  */
    /**
     * 获取ldap basename
     * @return
     */
	/*@Value(value = "${ldap.bn}")
    private  String bn; 
    @OneKeyListener(key = "ldap.bn")
    public void bnListener(ConfigEvent event) {
    	logger.info("bnListener, ldap.bn, event : [{}] ", event);
        this.setBn(event.getValue());
    }*/
    
    /**
     * 获取连接ldap的用户名
     * @return
     */
	/*@Value(value = "${ldap.bindDn}")
    private  String bindDn; 
    @OneKeyListener(key = "ldap.bindDn")
    public void bindDnListener(ConfigEvent event) {
    	logger.info("bindDnListener, ldap.bindDn, event : [{}] ", event);
        this.setBindDn(event.getValue());
    }*/
    
    /**
     * 获取连接ldap的用户密码
     * @return
     */
	/*@Value(value = "${ldap.password}")
    private  String password; 
    @OneKeyListener(key = "ldap.password")
    public void passwordListener(ConfigEvent event) {
    	logger.info("passwordListener, ldap.password, event : [{}] ", event);
        this.setPassword(event.getValue());
    }*/
    
    /**
     * 系统是否启动了ldap，返回字符串
     * @return
     */
	/*@Value(value = "${ldap.isEnabled}")
    private  String isEnabled; 
    @OneKeyListener(key = "ldap.isEnabled")
    public void isEnabledListener(ConfigEvent event) {
    	logger.info("isEnabledListener, ldap.isEnabled, event : [{}] ", event);
        this.setIsEnabled(event.getValue());
    }*/
    
  
    /**
     * 系统是否启动了ldap，返回boolean类型
     * @return
     */
    public boolean isLdapEnabledBoolean(){
    	try{
    	return  Boolean.valueOf(this.getIsEnabled());
    	}catch(Exception ex){
    		logger.error(ex.getMessage());
    	}
    	return false;
    }
	public String getUrl() {
		
		return this.xconf.getProperty("ldap.url");
		// return url;
	}
/*	public void setUrl(String url) {
		this.url = url;
	}
*/
	public int getPort() {
		return Integer.valueOf(this.getPortStr());
	}

	public String getBn() {
		return this.xconf.getProperty("ldap.bn");
		// return bn;
	}

	/*public void setBn(String bn) {
		this.bn = bn;
	}*/



	public String getBindDn() {
		return this.xconf.getProperty("ldap.bindDn");
		// return bindDn;
	}

	/*public void setBindDn(String bindDn) {
		this.bindDn = bindDn;
	}*/

	public String getPassword() {
		return this.xconf.getProperty("ldap.password");
		// return password;
	}

	/*public void setPassword(String password) {
		this.password = password;
	}*/

	public String getIsEnabled() {
		return this.xconf.getProperty("ldap.isEnabled");
		// return isEnabled;
	}

/*	public void setIsEnabled(String isEnabled) {
		this.isEnabled = isEnabled;
	}*/


	public String getPortStr() {
		return this.xconf.getProperty("ldap.port");
		// return portStr;
	}


	/*public void setPortStr(String portStr) {
		this.portStr = portStr;
	}*/
}
