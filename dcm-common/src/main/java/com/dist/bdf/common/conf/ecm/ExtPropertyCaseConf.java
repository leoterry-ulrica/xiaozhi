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
 * case中扩展的属性配置文件
 * 注意：注解方式不支持静态变量的注入
 */
@Configuration
@EnableConfigListener
@Component("ExtPropertyCaseConf")
public class ExtPropertyCaseConf extends ExtPropertyConf{
	
	// private static Logger logger = LoggerFactory.getLogger(ExtPropertyCaseConf.class);
	@Autowired
	private XDiamondConfig xconf;
    /**
     * 项目名称
     * @return
     */
    /*@Value(value = "${ecm.case.extproperty.projectName}")
    private  String projectName;
	@OneKeyListener(key = "ecm.case.extproperty.projectName")
    public void projectNameListener(ConfigEvent event) {
    	logger.info("projectNameListener, ecm.case.extproperty.projectName, event : [{}] ", event);
        this.setProjectName(event.getValue());
    }*/
	/**
	 * 项目编号
	 */
	/*@Value(value = "${ecm.case.extproperty.projectNo}")
    private  String projectNo;
	@OneKeyListener(key = "ecm.case.extproperty.projectNo")
    public void projectNoListener(ConfigEvent event) {
    	logger.info("projectNoListener, ecm.case.extproperty.projectNo, event : [{}] ", event);
        this.setProjectNo(event.getValue());
    }*/
	/**
	 * 项目类型
	 */
	/*@Value(value = "${ecm.case.extproperty.projectType}")
    private  String projectType;
	@OneKeyListener(key = "ecm.case.extproperty.projectType")
    public void projectTypeListener(ConfigEvent event) {
    	logger.info("projectTypeListener, ecm.case.extproperty.projectType, event : [{}] ", event);
        this.setProjectType(event.getValue());
    }*/
	/**
	 * 专业类别
	 */
	/*@Value(value = "${ecm.case.extproperty.majorType}")
    private  String majorType;
	@OneKeyListener(key = "ecm.case.extproperty.majorType")
    public void majorTypeListener(ConfigEvent event) {
    	logger.info("majorTypeListener, ecm.case.extproperty.majorType, event : [{}] ", event);
        this.setMajorType(event.getValue());
    }*/
	/**
	 * 规划类别
	 */
	/*@Value(value = "${ecm.case.extproperty.planType}")
    private  String planType;
	@OneKeyListener(key = "ecm.case.extproperty.planType")
    public void planTypeListener(ConfigEvent event) {
    	logger.info("planTypeListener, ecm.case.extproperty.planType, event : [{}] ", event);
        this.setPlanType(event.getValue());
    }*/
	
	/**
	 * 委托单位
	 */
	/*@Value(value = "${ecm.case.extproperty.wtdw}")
    private  String wtdw;
	@OneKeyListener(key = "ecm.case.extproperty.wtdw")
    public void wtdwListener(ConfigEvent event) {
    	logger.info("wtdwListener, ecm.case.extproperty.wtdw, event : [{}] ", event);
        this.setWtdw(event.getValue());
    }*/
	/**
	 * 牵头部门
	 */
	/*@Value(value = "${ecm.case.extproperty.qtbm}")
    private  String qtbm;
	@OneKeyListener(key = "ecm.case.extproperty.qtbm")
    public void qtbmListener(ConfigEvent event) {
    	logger.info("qtbmListener, ecm.case.extproperty.qtbm, event : [{}] ", event);
        this.setQtbm(event.getValue());
    }*/
	
	/**
	 * 配合部门
	 */
	/*@Value(value = "${ecm.case.extproperty.phbm}")
    private  String phbm;
	@OneKeyListener(key = "ecm.case.extproperty.phbm")
    public void phbmListener(ConfigEvent event) {
    	logger.info("phbmListener, ecm.case.extproperty.phbm, event : [{}] ", event);
        this.setPhbm(event.getValue());
    }*/
	
	/**
	 * 管理级别
	 */
	/*@Value(value = "${ecm.case.extproperty.managementLevel}")
    private  String managementLevel;
	@OneKeyListener(key = "ecm.case.extproperty.managementLevel")
    public void managementLevelListener(ConfigEvent event) {
    	logger.info("managementLevelListener, ecm.case.extproperty.managementLevel, event : [{}] ", event);
        this.setManagementLevel(event.getValue());
    }*/
	/**
	 * 详细地址
	 */
	/*@Value(value = "${ecm.case.extproperty.addressDetail}")
    private  String addressDetail;
	@OneKeyListener(key = "ecm.case.extproperty.addressDetail")
    public void addressDetailListener(ConfigEvent event) {
    	logger.info("addressDetailListener, ecm.case.extproperty.addressDetail, event : [{}] ", event);
        this.setAddressDetail(event.getValue());
    }*/
	
	/**
	 * 项目规模
	 */
	/*@Value(value = "${ecm.case.extproperty.projectScale}")
    private  String projectScale;
	@OneKeyListener(key = "ecm.case.extproperty.projectScale")
    public void projectScaleListener(ConfigEvent event) {
    	logger.info("projectScaleListener, ecm.case.extproperty.projectScale, event : [{}] ", event);
        this.setProjectScale(event.getValue());
    }*/
	/**
	 * 院外配合部门
	 */
	/*@Value(value = "${ecm.case.extproperty.ywphbm}")
    private  String ywphbm;
	@OneKeyListener(key = "ecm.case.extproperty.ywphbm")
    public void ywphbmListener(ConfigEvent event) {
    	logger.info("ywphbmListener, ecm.case.extproperty.ywphbm, event : [{}] ", event);
        this.setYwphbm(event.getValue());
    }*/
   
	/**
	 * 项目状态
	 */
	/*@Value(value = "${ecm.case.extproperty.projectStatus}")
    private  String projectStatus;
	@OneKeyListener(key = "ecm.case.extproperty.projectStatus")
    public void projectStatusListener(ConfigEvent event) {
    	logger.info("projectStatusListener, ecm.case.extproperty.projectStatus, event : [{}] ", event);
        this.setProjectStatus(event.getValue());
    }*/
	
	/**
	 * 项目负责人
	 */
	/*@Value(value = "${ecm.case.extproperty.projectLeader}")
    private  String projectLeader;
	@OneKeyListener(key = "ecm.case.extproperty.projectLeader")
    public void projectLeaderListener(ConfigEvent event) {
    	logger.info("projectLeaderListener, ecm.case.extproperty.projectLeader, event : [{}] ", event);
        this.setProjectLeader(event.getValue());
    }*/

    public String getProjectName() {
    	return this.xconf.getProperty("ecm.case.extproperty.projectName");
		// return projectName;
	}
	/*public void setProjectName(String projectName) {
		this.projectName = projectName;
	}*/

	public String getProjectNo() {
		return this.xconf.getProperty("ecm.case.extproperty.projectNo");
		// return projectNo;
	}

//	public void setProjectNo(String projectNo) {
//		this.projectNo = projectNo;
//	}

	public String getProjectType() {
		return this.xconf.getProperty("ecm.case.extproperty.projectType");
		// return projectType;
	}

	/*public void setProjectType(String projectType) {
		this.projectType = projectType;
	}*/

	public String getMajorType() {
		return this.xconf.getProperty("ecm.case.extproperty.majorType");
		// return majorType;
	}

	/*public void setMajorType(String majorType) {
		this.majorType = majorType;
	}*/

	public String getPlanType() {
		return this.xconf.getProperty("ecm.case.extproperty.planType");
		// return planType;
	}

	/*public void setPlanType(String planType) {
		this.planType = planType;
	}*/

	public String getWtdw() {
		return this.xconf.getProperty("ecm.case.extproperty.wtdw");
		// return wtdw;
	}

	/*public void setWtdw(String wtdw) {
		this.wtdw = wtdw;
	}*/

	public String getQtbm() {
		return this.xconf.getProperty("ecm.case.extproperty.qtbm");
		// return qtbm;
	}

	/*public void setQtbm(String qtbm) {
		this.qtbm = qtbm;
	}*/

	public String getPhbm() {
		return this.xconf.getProperty("ecm.case.extproperty.phbm");
		// return phbm;
	}

	/*public void setPhbm(String phbm) {
		this.phbm = phbm;
	}*/

	public String getManagementLevel() {
		return this.xconf.getProperty("ecm.case.extproperty.managementLevel");
		// return managementLevel;
	}

	/*public void setManagementLevel(String managementLevel) {
		this.managementLevel = managementLevel;
	}*/

	public String getAddressDetail() {
		return this.xconf.getProperty("ecm.case.extproperty.addressDetail");
		// return addressDetail;
	}

	/*public void setAddressDetail(String addressDetail) {
		this.addressDetail = addressDetail;
	}*/

	public String getProjectScale() {
		return this.xconf.getProperty("ecm.case.extproperty.projectScale");
		// return projectScale;
	}

	/*public void setProjectScale(String projectScale) {
		this.projectScale = projectScale;
	}*/

	public String getYwphbm() {
		return this.xconf.getProperty("ecm.case.extproperty.ywphbm");
		// return ywphbm;
	}

	/*public void setYwphbm(String ywphbm) {
		this.ywphbm = ywphbm;
	}*/

	public String getProjectStatus() {
		return this.xconf.getProperty("ecm.case.extproperty.projectStatus");
		// return projectStatus;
	}

	/*public void setProjectStatus(String projectStatus) {
		this.projectStatus = projectStatus;
	}*/

	/**
	 * 项目负责人
	 * @return
	 */
	public String getProjectLeader() {
		return this.xconf.getProperty("ecm.case.extproperty.projectLeader");
	}
	/**
	 * 项目助理
	 * @return
	 */
	public String getProjectAssistant() {
		return this.xconf.getProperty("ecm.case.extproperty.projectAssistant");
	}

	/*public void setProjectLeader(String projectLeader) {
		this.projectLeader = projectLeader;
	}*/
	
	/**
	 * 获取业务类别
	 * @return
	 */
	public String getBusinessType() {
		return this.xconf.getProperty("ecm.case.extproperty.businessType");
	}
	/**
	 * 其它配合部门
	 * @return
	 */
	public String getQtphbm() {
		return this.xconf.getProperty("ecm.case.extproperty.qtphbm");
	}
	/**
	 * 
	    * @Title: getZBJE
	    * @Description: 投标金额
	    * @param @return    参数
	    * @return String    返回类型
	    * @throws
	 */
	public String getZBJE() {
		return this.xconf.getProperty("ecm.case.extproperty.zbje");
	}
	/**
	 * 
	    * @Title: getZBJG
	    * @Description: 中标结果
	    * @param @return    参数
	    * @return String    返回类型
	    * @throws
	 */
	public String getZBJG() {
		return this.xconf.getProperty("ecm.case.extproperty.zbjg");
	}
	/**
	 * 
	    * @Title: getZBDW
	    * @Description: 中标单位
	    * @param @return    参数
	    * @return String    返回类型
	    * @throws
	 */
	public String getZBDW() {
		return this.xconf.getProperty("ecm.case.extproperty.zbdw");
	}
	/**
	 * 
	    * @Title: getZBSJ
	    * @Description: 中标时间
	    * @param @return    参数
	    * @return String    返回类型
	    * @throws
	 */
	public String getZBSJ() {
		return this.xconf.getProperty("ecm.case.extproperty.zbsj");
	}
	/**
	 * 是否热门
	 * @return
	 */
	public String getSfrm() {
		return this.xconf.getProperty("ecm.case.extproperty.sfrm");
	}
	/**
	 * 获取团队类型，里面存储如：中方、外方等
	 * @return
	 */
	public String getTeamType() {
		return this.xconf.getProperty("ecm.case.extproperty.teamType");
	}
}
