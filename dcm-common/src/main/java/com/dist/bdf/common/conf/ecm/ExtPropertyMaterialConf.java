package com.dist.bdf.common.conf.ecm;

import java.lang.reflect.Field;

import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import io.github.xdiamond.client.XDiamondConfig;
import io.github.xdiamond.client.annotation.EnableConfigListener;
/*import io.github.xdiamond.client.annotation.OneKeyListener;
import io.github.xdiamond.client.event.ConfigEvent;*/

/**
 * ecm中扩展的属性配置文件
 */
@Configuration
@EnableConfigListener
public class ExtPropertyMaterialConf {

	// private static Logger logger = LoggerFactory.getLogger(ExtPropertyMaterialConf.class);
	@Autowired
	private XDiamondConfig xconf;
	/**
	 * 是否可检索，布尔值 默认值 true
	 */
	/*@Value(value = "${ecm.material.extproperty.searchable}")
    private  String searchable;
	@OneKeyListener(key = "ecm.material.extproperty.searchable")
    public void searchableListener(ConfigEvent event) {
    	logger.info("searchableListener, ecm.material.extproperty.searchable, event : [{}] ", event);
        this.setSearchable(event.getValue());
    }*/
	
	/**
	 * 业务类型
	 */
	/*@Value(value = "${ecm.material.extproperty.business}")
	private  String business;
	@OneKeyListener(key = "ecm.material.extproperty.business")
    public void businessListener(ConfigEvent event) {
    	logger.info("businessListener, ecm.material.extproperty.business, event : [{}] ", event);
        this.setBusiness(event.getValue());
    }*/
	
	/**
	 * 关联项目
	 */
	/*@Value(value = "${ecm.material.extproperty.associateProject}")
	private  String associateProject;
	@OneKeyListener(key = "ecm.material.extproperty.associateProject")
    public void associateProjectListener(ConfigEvent event) {
    	logger.info("businessListener, ecm.material.extproperty.associateProject, event : [{}] ", event);
        this.setAssociateProject(event.getValue());
    }*/
	
	/**
	 * 所属区域
	 */
	/*@Value(value = "${ecm.material.extproperty.region}")
	private  String region;
	@OneKeyListener(key = "ecm.material.extproperty.region")
    public void regionListener(ConfigEvent event) {
    	logger.info("regionListener, ecm.material.extproperty.region, event : [{}] ", event);
        this.setRegion(event.getValue());
    }*/
	
	/**
	 * 所属组织
	 */
	/*@Value(value = "${ecm.material.extproperty.organization}")
	private  String organization;
	@OneKeyListener(key = "ecm.material.extproperty.organization")
    public void organizationListener(ConfigEvent event) {
    	logger.info("organizationListener, ecm.material.extproperty.organization, event : [{}] ", event);
        this.setOrganization(event.getValue());
    }*/
	
	/**
	 * 文档标签
	 */
	/*@Value(value = "${ecm.material.extproperty.tags}")
	private  String tags;
	@OneKeyListener(key = "ecm.material.extproperty.tags")
    public void tagsListener(ConfigEvent event) {
    	logger.info("tagsListener, ecm.material.extproperty.tags, event : [{}] ", event);
        this.setTags(event.getValue());
    }
	*/
	/**
	 * 文种类型
	 */
	/*@Value(value = "${ecm.material.extproperty.fileType}")
	private  String fileType;
	@OneKeyListener(key = "ecm.material.extproperty.fileType")
    public void fileTypeListener(ConfigEvent event) {
    	logger.info("fileTypeListener, ecm.material.extproperty.fileType, event : [{}] ", event);
        this.setFileType(event.getValue());
    }*/
	
	/**
	 * 空间域
	 */
	/*@Value(value = "${ecm.material.extproperty.spatialDomain}")
	private  String spatialDomain;
	@OneKeyListener(key = "ecm.material.extproperty.spatialDomain")
    public void spatialDomainListener(ConfigEvent event) {
    	logger.info("spatialDomainListener, ecm.material.extproperty.spatialDomain, event : [{}] ", event);
        this.spatialDomain = event.getValue();
    }*/
	
	/**
	 * 资料简介
	 */
	/*@Value(value = "${ecm.material.extproperty.describe}")
	private  String describe;
	@OneKeyListener(key = "ecm.material.extproperty.describe")
    public void describeListener(ConfigEvent event) {
    	logger.info("describeListener, ecm.material.extproperty.describe, event : [{}] ", event);
        this.describe = event.getValue();
    }*/
	
	/**
	 * 资源类型
	 */
	/*@Value(value = "${ecm.material.extproperty.resourceType}")
	private  String resourceType;
	@OneKeyListener(key = "ecm.material.extproperty.resourceType")
    public void resourceTypeListener(ConfigEvent event) {
    	logger.info("resourceTypeListener, ecm.material.extproperty.resourceType, event : [{}] ", event);
        this.resourceType = event.getValue();
    }*/
	
	
	
	public String getSearchable() {
		return this.xconf.getProperty("ecm.material.extproperty.searchable");
		// return searchable;
	}
//	public void setSearchable(String searchable) {
//		this.searchable = searchable;
//	}

	public String getBusiness() {
		return this.xconf.getProperty("ecm.material.extproperty.business");
		// return business;
	}

	/*public void setBusiness(String business) {
		this.business = business;
	}*/

	public String getAssociateProject() {
		return this.xconf.getProperty("ecm.material.extproperty.associateProject");
		// return associateProject;
	}

	/*public void setAssociateProject(String associateProject) {
		this.associateProject = associateProject;
	}*/

	public String getRegion() {
		return this.xconf.getProperty("ecm.material.extproperty.region");
		// return region;
	}

	/*public void setRegion(String region) {
		this.region = region;
	}*/

	public String getOrganization() {
		return this.xconf.getProperty("ecm.material.extproperty.organization");
		// return organization;
	}

	/*public void setOrganization(String organization) {
		this.organization = organization;
	}*/

	public String getTags() {
		return this.xconf.getProperty("ecm.material.extproperty.tags");
		// return tags;
	}

	/*public void setTags(String tags) {
		this.tags = tags;
	}*/

	public String getFileType() {
		return this.xconf.getProperty("ecm.material.extproperty.fileType");
		// return fileType;
	}

	/*public void setFileType(String fileType) {
		this.fileType = fileType;
	}*/

	public String getSpatialDomain() {
		return this.xconf.getProperty("ecm.material.extproperty.spatialDomain");
		// return spatialDomain;
	}

	/*public void setSpatialDomain(String spatialDomain) {
		this.spatialDomain = spatialDomain;
	}*/

	public String getDescribe() {
		return this.xconf.getProperty("ecm.material.extproperty.describe");
		// return describe;
	}

	/*public void setDescribe(String describe) {
		this.describe = describe;
	}*/

	public String getResourceType() {
		return this.xconf.getProperty("ecm.material.extproperty.resourceType");
		// return resourceType;
	}


	/*public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}*/
	
	/**
	 * 复写toString
	 */
	@Override
	public String toString() {
		
		StringBuilder buf = new StringBuilder();
		try{
		
		Field[] fields = this.getClass().getDeclaredFields();
		boolean flag = true;
		for(Field f : fields) {
			if(flag){
			  buf.append(f.get(this));	
			  flag = false;
			  continue;
			}
			buf.append(","+f.get(this));	
		}
		}catch(Exception ex){
			ex.printStackTrace();
		}
	
		return buf.toString();
	}

}
