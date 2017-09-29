package com.dist.bdf.common.conf.ecm;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import io.github.xdiamond.client.XDiamondConfig;
import io.github.xdiamond.client.annotation.EnableConfigListener;
/*import io.github.xdiamond.client.annotation.OneKeyListener;
import io.github.xdiamond.client.event.ConfigEvent;*/

/**
 * ECM的配置文件
 * 注意：注解方式不支持静态变量的注入
 */
@Configuration
@EnableConfigListener
public class ECMConf {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	/*private static volatile ECMConf confInst = null;
	public static ECMConf getInstance() {
		ECMConf temp = confInst; // 创建临时变量
		if(null == confInst){
			synchronized (ECMConf.class) {
				temp = new ECMConf();
				confInst = temp;
			}
		}
		// 返回临时变量
		return temp;
	}*/
	@Autowired
	private XDiamondConfig xconf;
    /**
     * 获取ECM的服务器地址
     * @return
     */
   /* @Value(value = "${ecm.server.site}")
    private  String ecmServerSite;
    @OneKeyListener(key = "ecm.server.site")
    public void ecmServerSiteListener(ConfigEvent event) {
    	logger.info("ecmServerSiteListener, ecm.server.site, event : [{}] ", event);
        this.setEcmServerSite(event.getValue());
    }*/
    /**
     * 获取ECM的登陆账户
     * @return
     */
    /*@Value(value = "${ecm.server.username}")
    private  String ecmUserName;
    @OneKeyListener(key = "ecm.server.username")
    public void ecmUserNameListener(ConfigEvent event) {
    	logger.info("ecmUserNameListener, ecm.server.username, event : [{}] ", event);
        this.setEcmUserName(event.getValue());
    }*/
    
    /**
     * 获取ECM的登陆密码
     * @return
     */
    /*@Value(value = "${ecm.server.password}")
    private  String ecmUserPassword;
    @OneKeyListener(key = "ecm.server.password")
    public void ecmUserPwdListener(ConfigEvent event) {
    	logger.info("ecmUserNameListener, ecm.server.password, event : [{}] ", event);
        this.setEcmUserPassword(event.getValue());
    }*/
    
    
    /**
     * 获取ECM的DOMAIN名称
     * @return
     */
   /* @Value(value = "${ecm.domain}")
    private  String ecmDomainName;
    @OneKeyListener(key = "ecm.domain")
    public void ecmDomainNameListener(ConfigEvent event) {
    	logger.info("ecmDomainNameListener, ecm.domain, event : [{}] ", event);
        this.setEcmDomainName(event.getValue());
    }*/
 

    /**
     * 获取ECM的目标存储库的名称
     * @return
     */
   /* @Value(value = "${ecm.targetobjectStore}")
    private  String ecmTargetObjectStoreName;
    @OneKeyListener(key = "ecm.targetobjectStore")
    public void ecmTargetOSNameListener(ConfigEvent event) {
    	logger.info("ecmTargetOSNameListener, ecm.targetobjectStore, event : [{}] ", event);
        this.setEcmTargetObjectStoreName(event.getValue());
    }*/
 
    /**
     * 获取当前使用的解决方案标签前缀
     * @return
     */
    /*@Value(value = "${ecm.solution.prefix}")
    private  String solutionPrefix;
    @OneKeyListener(key = "ecm.solution.prefix")
    public void ecmSolutionPrefixListener(ConfigEvent event) {
    	logger.info("ecmSolutionPrefixListener, ecm.solution.prefix, event : [{}] ", event);
        this.setSolutionPrefix(event.getValue());
    }*/

    /**
     * 获取JAAS
     * @return
     */
    /*@Value(value = "${ecm.jaas}")
    private  String jaas;
    @OneKeyListener(key = "ecm.jaas")
    public void ecmJAASListener(ConfigEvent event) {
    	logger.info("ecmJAASListener, ecm.jaas, event : [{}] ", event);
        this.setJaas(event.getValue());
    }*/
    
    /**
     * 获取默认文档类
     * @return
     */
    /*@Value(value = "${project.material.defaultDocument}")
    private  String defaultDocumentClass;
    @OneKeyListener(key = "project.material.defaultDocument")
    public void defaultDocClassListener(ConfigEvent event) {
    	logger.info("defaultDocClassListener, project.material.defaultDocument, event : [{}] ", event);
        this.setDefaultDocumentClass(event.getValue());
    }*/
   
    /**
     * 获取文种类型字段名称
     * @return
     */
    /*@Value(value = "${project.material.field.FileType}")
    private  String fileTypeField;
    @OneKeyListener(key = "project.material.field.FileType")
    public void fileTypeFieldListener(ConfigEvent event) {
    	logger.info("fileTypeFieldListener, project.material.field.FileType, event : [{}] ", event);
        this.setFileTypeField(event.getValue());
    }*/
   
    /**
     * 获取所属区域字段名称
     * @return
     */
    /*@Value(value = "${project.material.field.Region}")
    private  String regionField;
    @OneKeyListener(key = "project.material.field.Region")
    public void regionFieldListener(ConfigEvent event) {
    	logger.info("regionFieldListener, project.material.field.Region, event : [{}] ", event);
        this.setRegionField(event.getValue());
    }*/

    /**
     * 获取所属组织字段名称
     * @return
     */
    /*@Value(value = "${project.material.field.Organization}")
    private  String organizationField;
    @OneKeyListener(key = "project.material.field.Organization")
    public void organizationFieldListener(ConfigEvent event) {
    	logger.info("organizationFieldListener, project.material.field.Organization, event : [{}] ", event);
        this.setOrganizationField(event.getValue());
    }*/
    
    /**
     * 获取业务类型字段名称
     * @return
     */
    /*@Value(value = "${project.material.field.Business}")
    private  String businessField;
    @OneKeyListener(key = "project.material.field.Business")
    public void businessFieldListener(ConfigEvent event) {
    	logger.info("businessFieldListener, project.material.field.Business, event : [{}] ", event);
        this.setBusinessField(event.getValue());
    }*/
   
    /**
     * 检索的最大记录数
     * @return
     */
    /*@Value(value = "${search.MaxRecords}")
    private  int searchMaxRecords;
    @OneKeyListener(key = "search.MaxRecords")
    public void searchMaxRecordsListener(ConfigEvent event) {
    	logger.info("searchMaxRecordsListener, search.MaxRecords, event : [{}] ", event);
        this.setSearchMaxRecords(Integer.parseInt(event.getValue()));
    }*/
   
    /**
     * 分页检索，每页大小
     * @return
     */
    /*@Value(value = "${search.pageSize}")
    private  int pageSize;
    @OneKeyListener(key = "search.pageSize")
    public void pageSizeListener(ConfigEvent event) {
    	logger.info("pageSizeListener, search.pageSize, event : [{}] ", event);
        this.setPageSize(Integer.parseInt(event.getValue()));
    }*/

    /**
     * 微作根目录
     * @return
     */
   /* @Value(value = "${wz.root.path}")
    private  String wzRootPath;
    @OneKeyListener(key = "wz.root.path")
    public void wzRootPathListener(ConfigEvent event) {
    	logger.info("wzRootPathListener, wz.root.path, event : [{}] ", event);
        this.setWzRootPath(event.getValue());
    }*/
  
    /**
     * 获取移动设备上传文档存储默认文件夹名字
     * @return
     */
    /*@Value(value = "${mobile.fileStore.folderName}")
    private  String mobileFileStoreFolderName;
    @OneKeyListener(key = "mobile.fileStore.folderName")
    public void mobileFileStoreFolderNameListener(ConfigEvent event) {
    	logger.info("mobileFileStoreFolderNameListener, mobile.fileStore.folderName, event : [{}] ", event);
        this.setMobileFileStoreFolderName(event.getValue());
    }*/
 
    /**
     * 获取存储域id
     * @return
     */
    /*@Value(value = "${ecm.storageareaid}")
    private  String storageareaId;
    @OneKeyListener(key = "ecm.storageareaid")
    public void storageareaIdListener(ConfigEvent event) {
    	logger.info("storageareaIdListener, ecm.storageareaid, event : [{}] ", event);
        this.setStorageareaId(event.getValue());
    }*/
  
    /**
     * 获取微作根文件夹id
     * @return
     */
   /* @Value(value = "${wz.root.FolderId}")
    private  String wzRootFolderId;
    @OneKeyListener(key = "wz.root.FolderId")
    public void wzRootFolderIdListener(ConfigEvent event) {
    	logger.info("wzRootFolderIdListener, wz.root.FolderId, event : [{}] ", event);
        this.setWzRootFolderId(event.getValue());
    }*/
   
    /**
     * 微作默认类名称
     * @return
     */
    /*@Value(value = "${wz.default.classId}")
    private  String wzDefaultClassId;
    @OneKeyListener(key = "wz.default.classId")
    public void wzDefaultClassIdIdListener(ConfigEvent event) {
    	logger.info("wzDefaultClassIdIdListener, wz.default.classId, event : [{}] ", event);
        this.setWzDefaultClassId(event.getValue());
    }*/
  
   /**
    * 个人文档存储根目录
    */
    /*@Value(value = "${personal.dir.root}")
    private  String personalDirRoot;
    @OneKeyListener(key = "personal.dir.root")
    public void personalDirRootListener(ConfigEvent event) {
    	logger.info("personalDirRootListener, personal.dir.root, event : [{}] ", event);
        this.setPersonalDirRoot(event.getValue());
    }*/
    /**
     * 院级文档存储根目录
     */
    /*@Value(value = "${institute.dir.root}")
    private  String instituteDirRoot;
    @OneKeyListener(key = "institute.dir.root")
    public void  instituteDirRootListener(ConfigEvent event) {
    	logger.info(" instituteDirRootListener, institute.dir.root, event : [{}] ", event);
        this.setInstituteDirRoot(event.getValue());
    }*/
    /**
     * 所级文档存储根目录
     */
   /* @Value(value = "${department.dir.root}")
    private  String departmentDirRoot;
    @OneKeyListener(key = "department.dir.root")
    public void  departmentDirRootListener(ConfigEvent event) {
    	logger.info(" departmentDirRootListener, department.dir.root, event : [{}] ", event);
        this.setDepartmentDirRoot(event.getValue());
    }*/
    
    /**
     * 统计信息默认类名称
     */
   /* @Value(value = "${summaryData.default.classId}")
    private  String summaryDataDefaultClassId;
    @OneKeyListener(key = "summaryData.default.classId")
    public void summaryDataDefaultClassIdIdListener(ConfigEvent event) {
    	logger.info("summaryDataDefaultClassIdIdListener, summaryData.default.classId, event : [{}] ", event);
        this.setSummaryDataDefaultClassId(event.getValue());
    }*/
    
    /**
     * 根据机构域获取对应的目标存储库
     * @param realm
     * @return
     */
     public String getTargetObjectStore(String realm) {
		
		if(StringUtils.isEmpty(realm)){
			logger.info("realm is null......, use default targetObjectStore name");
			return this.getEcmTargetObjectStoreName();
		}
		String targetObjectStore = xconf.getProperty("realm."+realm);
		if(StringUtils.isEmpty(targetObjectStore)){
			logger.info("xdiamond conf [{}] is null......, use default targetObjectStore name", realm);
			targetObjectStore = this.getEcmTargetObjectStoreName();
		}
		
		return targetObjectStore;
	}
     /**
      * 获取公共存储库
      * @param realm 如果为空，则返回默认的公共存储库，key为realm.distpub
      * @return
      */
    public String getPublicObjectStore(String realm) {
 		
 		if(StringUtils.isEmpty(realm)){
 			logger.info("realm is null......, use default publicObjectStore name");
 			return this.xconf.getProperty("realm.distpub");
 		}
 		String publicObjectStore = xconf.getProperty("realm."+realm);
 		if(StringUtils.isEmpty(publicObjectStore)){
 			logger.info("xdiamond conf [{}] is null......, use default publicObjectStore name", realm);
 			publicObjectStore = this.xconf.getProperty("realm.distpub");
 		}
 		
 		return publicObjectStore;
 	}
  
	public  String getEcmServerSite() {
		return this.xconf.getProperty("ecm.server.site");
		// return ecmServerSite;
	}

	/*public  void setEcmServerSite(String ecmServerSite) {
		this.ecmServerSite = ecmServerSite;
	}*/

	public  String getEcmUserName() {
		return this.xconf.getProperty("ecm.server.username");
		// return ecmUserName;
	}

	/*public  void setEcmUserName(String ecmUserName) {
		this.ecmUserName = ecmUserName;
	}*/

	public  String getEcmUserPassword() {
		return this.xconf.getProperty("ecm.server.password");
		// return ecmUserPassword;
	}

	/*public  void setEcmUserPassword(String ecmUserPassword) {
		this.ecmUserPassword = ecmUserPassword;
	}*/

	public  String getEcmDomainName() {
		return this.xconf.getProperty("ecm.domain");
		// return ecmDomainName;
	}

	/*public  void setEcmDomainName(String ecmDomainName) {
		this.ecmDomainName = ecmDomainName;
	}*/
	/**
	 * 获取默认目标存储库名称
	 * @return
	 */
	public  String getEcmTargetObjectStoreName() {
		return this.xconf.getProperty("ecm.targetobjectStore");
		// return ecmTargetObjectStoreName;
	}

	/*public  void setEcmTargetObjectStoreName(String ecmTargetObjectStoreName) {
		this.ecmTargetObjectStoreName = ecmTargetObjectStoreName;
	}*/

	/*public  void setSolutionPrefix(String solutionPrefix) {
		this.solutionPrefix = solutionPrefix;
	}*/

	public  String getSolutionPrefix() {
		return this.xconf.getProperty("ecm.solution.prefix");
		// return solutionPrefix;
	}

	public  String getJaas() {
		return this.xconf.getProperty("ecm.jaas");
		// return jaas;
	}

	/*public  void setJaas(String jaas) {
		this.jaas = jaas;
	}*/

	public  String getDefaultDocumentClass() {
		return this.xconf.getProperty("project.material.defaultDocument");
		// return defaultDocumentClass;
	}

	/*public  void setDefaultDocumentClass(String defaultDocumentClass) {
		this.defaultDocumentClass = defaultDocumentClass;
	}*/

	public  String getFileTypeField() {
		return this.xconf.getProperty("project.material.field.FileType");
		// return fileTypeField;
	}

	/*public  void setFileTypeField(String fileTypeField) {
		this.fileTypeField = fileTypeField;
	}*/

	public  String getRegionField() {
		return this.xconf.getProperty("project.material.field.Region");
		// return regionField;
	}

	/*public  void setRegionField(String regionField) {
		this.regionField = regionField;
	}*/

	public  String getOrganizationField() {
		return this.xconf.getProperty("project.material.field.Organization");
		// return organizationField;
	}

	/*public  void setOrganizationField(String organizationField) {
		this.organizationField = organizationField;
	}*/

	public  String getBusinessField() {
		return this.xconf.getProperty("project.material.field.Business");
		// return businessField;
	}

	/*public  void setBusinessField(String businessField) {
		this.businessField = businessField;
	}*/

	public  int getSearchMaxRecords() {
		return Integer.parseInt(this.xconf.getProperty("search.MaxRecords"));
		// return searchMaxRecords;
	}

	/*public  void setSearchMaxRecords(int searchMaxRecords) {
		this.searchMaxRecords = searchMaxRecords;
	}*/

	public  int getPageSize() {
		return Integer.parseInt(this.xconf.getProperty("search.pageSize"));
		//return pageSize;
	}

	/*public  void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}*/
	/**
	 * 获取微作根目录
	 * @return
	 */
	public  String getWzRootPath() {
		return this.xconf.getProperty("wz.root.path");
		// return wzRootPath;
	}

	/*public  void setWzRootPath(String wzRootPath) {
		this.wzRootPath = wzRootPath;
	}*/
	/**
	 * 获取移动存储文件夹名称
	 * @return
	 */
	public  String getMobileFileStoreFolderName() {
		return this.xconf.getProperty("mobile.fileStore.folderName");
		// return mobileFileStoreFolderName;
	}

	/*public  void setMobileFileStoreFolderName(String mobileFileStoreFolderName) {
		this.mobileFileStoreFolderName = mobileFileStoreFolderName;
	}*/

	public  String getStorageareaId() {
		return this.xconf.getProperty("ecm.storageareaid");
		// return storageareaId;
	}

	/*public  void setStorageareaId(String storageareaId) {
		this.storageareaId = storageareaId;
	}*/

	@Deprecated
	public  String getWzRootFolderId() {
		return this.xconf.getProperty("wz.root.FolderId");
		// return wzRootFolderId;
	}

/*	public  void setWzRootFolderId(String wzRootFolderId) {
		this.wzRootFolderId = wzRootFolderId;
	}*/


	public  String getWzDefaultClassId() {
		return this.xconf.getProperty("wz.default.classId");
		// return wzDefaultClassId;
	}


	/*public  void setWzDefaultClassId(String wzDefaultClassId) {
		this.wzDefaultClassId = wzDefaultClassId;
	}*/
	/**
	 * 获取个人资料根目录
	 * @return
	 */
	public String getPersonalDirRoot() {
		return this.xconf.getProperty("personal.dir.root");
		// return personalDirRoot;
	}
	/*public void setPersonalDirRoot(String personalDirRoot) {
		this.personalDirRoot = personalDirRoot;
	}*/
	/**
	 * 获取院级资料根目录
	 * @return
	 */
	public String getInstituteDirRoot() {
		return this.xconf.getProperty("institute.dir.root");
		// return instituteDirRoot;
	}

	/*public void setInstituteDirRoot(String instituteDirRoot) {
		this.instituteDirRoot = instituteDirRoot;
	}*/
	/**
	 * 获取所级资料根目录
	 * @return
	 */
	public String getDepartmentDirRoot() {
		return this.xconf.getProperty("department.dir.root");
		// return departmentDirRoot;
	}
	/**
	 * 获取咨询根目录
	 * @return
	 */
	public String getInformationDirRoot() {
		return this.xconf.getProperty("information.dir.root");
		// return departmentDirRoot;
	}
	/**
	 * 获取项目资料根目录
	 * @return
	 */
	public String getProjectDirRoot() {
		return this.xconf.getProperty("project.dir.root");
		// return personalDirRoot;
	}
	/*public void setDepartmentDirRoot(String departmentDirRoot) {
		this.departmentDirRoot = departmentDirRoot;
	}*/
	/**
	 * 获取汇总数据默认类标识
	 * @return
	 */
	public String getSummaryDataDefaultClassId() {
		return this.xconf.getProperty("summaryData.default.classId");
		// return summaryDataDefaultClassId;
	}
	/**
	 * 项目业务类别
	 * @param realm 域，如thupdi
	 * @return
	 */
	public String getBusinessType(String realm){
		return this.xconf.getProperty("ecm.case.dic.businessType."+realm);
	}
	/**
	 * 获取通用的项目类型
	 * @return
	 */
	public String getProjectTypeCommon(){
		return this.xconf.getProperty("ecm.case.projectType.common");
	}
	/**
	 * 获取团队的项目类型
	 * @return
	 */
	public String getProjectTypeTeam(){
		return this.xconf.getProperty("ecm.case.projectType.team");
	}
	/**
	 * 获取CAD模板目录
	 * @return
	 */
	public String getCADTemplateDir() {
		return this.xconf.getProperty("cad.template.dir");
	}
	/**
	 * 获取个人简历跟目录
	 * @return
	 */
	public String getPersonalResumeDir() {
		return this.xconf.getProperty("resume.dir.root");
	}

}
