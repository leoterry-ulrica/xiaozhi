package com.dist.bdf.common.conf.officeonline;

import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import io.github.xdiamond.client.XDiamondConfig;
import io.github.xdiamond.client.annotation.EnableConfigListener;
/*import io.github.xdiamond.client.annotation.OneKeyListener;
import io.github.xdiamond.client.event.ConfigEvent;*/

/**
 * officeonline的配置文件
 * 注意：注解方式不支持静态变量的注入，但可通过对象方法注入静态变量
 */
@Configuration
@EnableConfigListener
public class OfficeonlineConf {

	// private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private XDiamondConfig xconf;
    /**
     * 获取officeonline的服务器地址
     * @return
     */
   /* @Value(value = "${oo.server}")
    private  String server;
    @OneKeyListener(key = "oo.server")
    public void serverSiteListener(ConfigEvent event) {
    	logger.info("serverSiteListener, oo.server, event : [{}] ", event);
        this.setServer(event.getValue());
    }*/
    
	public String getServer() {
		
		return this.xconf.getProperty("oo.server");
		// return server;
	}
	/*public void setServer(String server) {
		this.server = server;
	}*/

}
