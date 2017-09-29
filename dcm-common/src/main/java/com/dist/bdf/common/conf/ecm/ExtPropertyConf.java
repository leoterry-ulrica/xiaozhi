package com.dist.bdf.common.conf.ecm;

import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import io.github.xdiamond.client.XDiamondConfig;
import io.github.xdiamond.client.annotation.EnableConfigListener;
/*import io.github.xdiamond.client.annotation.OneKeyListener;
import io.github.xdiamond.client.event.ConfigEvent;*/

/**
 * ecm中扩展的属性配置文件
 */
@Configuration
@EnableConfigListener
@Component("ExtPropertyConf")
public class ExtPropertyConf {

	// private static Logger logger = LoggerFactory.getLogger(ExtPropertyConf.class);
	@Autowired
	private XDiamondConfig xconf;
	/**
	 * 省
	 */
	/*@Value(value = "${ecm.case.extproperty.province}")
    protected  String province;
	@OneKeyListener(key = "ecm.case.extproperty.province")
    public void provinceListener(ConfigEvent event) {
    	logger.info("provinceListener, ecm.case.extproperty.province, event : [{}] ", event);
        this.setProvince(event.getValue());
    }*/
	
	/**
	 * 市
	 */
	/*@Value(value = "${ecm.case.extproperty.city}")
	protected  String city;
	@OneKeyListener(key = "ecm.case.extproperty.city")
    public void cityListener(ConfigEvent event) {
    	logger.info("cityListener, ecm.case.extproperty.city, event : [{}] ", event);
        this.setCity(event.getValue());
    }*/
	/**
	 * 县
	 */
	/*@Value(value = "${ecm.case.extproperty.county}")
	protected  String county;
	@OneKeyListener(key = "ecm.case.extproperty.county")
    public void countyListener(ConfigEvent event) {
    	logger.info("cityListener, ecm.case.extproperty.county, event : [{}] ", event);
        this.setCounty(event.getValue());
    }*/
	
	/**
	 * 收藏数
	 */
	/*@Value(value = "${ecm.common.extproperty.favorites}")
	private  String favorites;
	@OneKeyListener(key = "ecm.common.extproperty.favorites")
    public void favoritesListener(ConfigEvent event) {
    	logger.info("favoritesListener, ecm.common.extproperty.favorites, event : [{}] ", event);
        this.setFavorites(event.getValue());
    }*/
	
	/**
	 * 点赞数
	 */
	/*@Value(value = "${ecm.common.extproperty.upvoteCount}")
	private  String upvoteCount;
	@OneKeyListener(key = "ecm.common.extproperty.upvoteCount")
    public void upvoteCountListener(ConfigEvent event) {
    	logger.info("upvoteCountListener, ecm.common.extproperty.upvoteCount, event : [{}] ", event);
        this.setUpvoteCount(event.getValue());
    }*/
	
	/*@Value(value = "${ecm.common.extproperty.objectIdType}")
	private  String objectIdType;
	@OneKeyListener(key = "ecm.common.extproperty.objectIdType")
    public void objectIdTypeListener(ConfigEvent event) {
    	logger.info("objectIdTypeListener, ecm.common.extproperty.objectIdType, event : [{}] ", event);
        this.setObjectIdType(event.getValue());
    }*/
	
	public String getUpvoteCount() {
		return this.xconf.getProperty("ecm.common.extproperty.upvoteCount");
		// return upvoteCount;
	}

	/*public void setUpvoteCount(String upvoteCount) {
		this.upvoteCount = upvoteCount;
	}*/
	
	public String getFavorites() {
		return this.xconf.getProperty("ecm.common.extproperty.favorites");
		// return favorites;
	}

	/*public void setFavorites(String favorites) {
		this.favorites = favorites;
	}*/
	
	public String getProvince() {
		return this.xconf.getProperty("ecm.case.extproperty.province");
		// return province;
	}

	/*public void setProvince(String province) {
		this.province = province;
	}*/

	public String getCity() {
		return this.xconf.getProperty("ecm.case.extproperty.city");
		// return city;
	}

	/*public void setCity(String city) {
		this.city = city;
	}*/

	public String getCounty() {
		return this.xconf.getProperty("ecm.case.extproperty.county");
		// return county;
	}

	/*public void setCounty(String county) {
		this.county = county;
	}*/
	/**
	 * ObjectId类型属性
	 * @return
	 */
	public String getObjectIdType() {
		return this.xconf.getProperty("ecm.common.extproperty.objectIdType");
		// return objectIdType;
	}

	/*public void setObjectIdType(String objectIdType) {
		this.objectIdType = objectIdType;
	}*/
	/**
	 * 获取发布者属性
	 * @return
	 */
	public String getPublisher() {
		return this.xconf.getProperty("ecm.common.extproperty.publisher");
	}
	
}
