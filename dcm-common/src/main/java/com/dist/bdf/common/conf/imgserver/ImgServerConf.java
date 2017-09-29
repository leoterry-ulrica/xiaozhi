package com.dist.bdf.common.conf.imgserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import io.github.xdiamond.client.XDiamondConfig;
import io.github.xdiamond.client.annotation.EnableConfigListener;

/**
 * img server的配置文件
 * 注意：注解方式不支持静态变量的注入，但可通过对象方法注入静态变量
 */
@Configuration
@EnableConfigListener
public class ImgServerConf {

	private static Logger logger  = LoggerFactory.getLogger(ImgServerConf.class);
	
	@Autowired
	private XDiamondConfig xconf;
    
	public String getServerURI() {
		
		String uri = this.xconf.getProperty("imgser.uri");
		logger.debug(">>>img server uri :[{}]", uri);
		return uri;
	}
}
