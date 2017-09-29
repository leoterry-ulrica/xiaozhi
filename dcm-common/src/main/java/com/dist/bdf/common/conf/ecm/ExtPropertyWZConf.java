package com.dist.bdf.common.conf.ecm;

import java.lang.reflect.Field;

import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import io.github.xdiamond.client.XDiamondConfig;
import io.github.xdiamond.client.annotation.EnableConfigListener;
/*import io.github.xdiamond.client.annotation.OneKeyListener;
import io.github.xdiamond.client.event.ConfigEvent;
*/
/**
 * 微作中扩展的属性配置文件
 */
@Configuration
@EnableConfigListener
@Component("ExtPropertyWZConf")
public class ExtPropertyWZConf extends ExtPropertyConf{

	// private static Logger logger = LoggerFactory.getLogger(ExtPropertyWZConf.class);
	@Autowired
	private XDiamondConfig xconf;
	/**
	 * 关联任务
	 */
	/*@Value(value = "${ecm.wz.extproperty.associateTask}")
	private  String associateTask;
	@OneKeyListener(key = "ecm.wz.extproperty.associateTask")
    public void associateTaskListener(ConfigEvent event) {
    	logger.info("provinceListener, ecm.wz.extproperty.associateTask, event : [{}] ", event);
        this.setAssociateTask(event.getValue());
    }*/
	
	/**
	 * 关联环节
	 */
	/*@Value(value = "${ecm.wz.extproperty.associateTache}")
	private  String associateTache;
	@OneKeyListener(key = "ecm.wz.extproperty.associateTache")
    public void associateTacheListener(ConfigEvent event) {
    	logger.info("associateTacheListener, ecm.wz.extproperty.AssociateTache, event : [{}] ", event);
        this.setAssociateTache(event.getValue());
    }*/
	
	/**
	 * 关联项目
	 */
	/*@Value(value = "${ecm.wz.extproperty.associateProject}")
	private  String associateProject;
	@OneKeyListener(key = "ecm.wz.extproperty.associateProject")
    public void associateProjectListener(ConfigEvent event) {
    	logger.info("associateProjectListener, ecm.wz.extproperty.associateProject, event : [{}] ", event);
        this.setAssociateProject(event.getValue());
    }*/
	
	/**
	 * 相关人员
	 */
	/*@Value(value = "${ecm.wz.extproperty.associatePerson}")
	private  String associatePerson;
	@OneKeyListener(key = "ecm.wz.extproperty.associatePerson")
    public void associatePersonListener(ConfigEvent event) {
    	logger.info("associatePersonListener, ecm.wz.extproperty.associatePerson, event : [{}] ", event);
        this.setAssociatePerson(event.getValue());
    }
	*/
	
	
	/**
	 * 地图链接
	 */
	/*@Value(value = "${ecm.wz.extproperty.associateMapLink}")
	private  String associateMapLink;
	@OneKeyListener(key = "ecm.wz.extproperty.associateMapLink")
    public void associateMapLinkListener(ConfigEvent event) {
    	logger.info("associateMapLinkListener, ecm.wz.extproperty.associateMapLink, event : [{}] ", event);
        this.setAssociateMapLink(event.getValue());
    }*/
	
	/**
	 * 微作内容
	 */
/*	@Value(value = "${ecm.wz.extproperty.content}")
	private  String content;
	@OneKeyListener(key = "ecm.wz.extproperty.content")
    public void contentListener(ConfigEvent event) {
    	logger.info("contentListener, ecm.wz.extproperty.content, event : [{}] ", event);
        this.setContent(event.getValue());
    }*/
	
	/**
	 * 文档链接
	 */
	/*@Value(value = "${ecm.wz.extproperty.associateFileLink}")
	private  String associateFileLink;
	@OneKeyListener(key = "ecm.wz.extproperty.associateFileLink")
    public void associateFileLinkListener(ConfigEvent event) {
    	logger.info("associateFileLinkListener, ecm.wz.extproperty.associateFileLink, event : [{}] ", event);
        this.setAssociateFileLink(event.getValue());
    }*/
	
	/**
	 * 点赞列表
	 */
	/*@Value(value = "${ecm.wz.extproperty.upvoteList}")
	private  String upvoteList;
	@OneKeyListener(key = "ecm.wz.extproperty.upvoteList")
    public void upvoteListListener(ConfigEvent event) {
    	logger.info("upvoteListListener, ecm.wz.extproperty.upvoteList, event : [{}] ", event);
        this.setUpvoteList(event.getValue());
    }*/
	
	
	/**
	 * 置顶
	 */
	/*@Value(value = "${ecm.wz.extproperty.sticky}")
	private  String sticky;
	@OneKeyListener(key = "ecm.wz.extproperty.sticky")
    public void stickyListener(ConfigEvent event) {
    	logger.info("stickyListener, ecm.wz.extproperty.sticky, event : [{}] ", event);
        this.setSticky(event.getValue());
    }*/
	
	/**
	 * 评论数
	 */
	/*@Value(value = "${ecm.wz.extproperty.commentCount}")
    private  String commentCount;
	@OneKeyListener(key = "ecm.wz.extproperty.commentCount")
    public void commentCountListener(ConfigEvent event) {
    	logger.info("commentCountListener, ecm.wz.extproperty.commentCount, event : [{}] ", event);
        this.setCommentCount(event.getValue());
    }*/
	
	public String getAssociateTask() {
		return this.xconf.getProperty("ecm.wz.extproperty.associateTask");
		// return associateTask;
	}
	/*public void setAssociateTask(String associateTask) {
		this.associateTask = associateTask;
	}*/


	public String getAssociateTache() {
		return this.xconf.getProperty("ecm.wz.extproperty.associateTache");
		// return associateTache;
	}


	/*public void setAssociateTache(String associateTache) {
		this.associateTache = associateTache;
	}*/


	public String getAssociateProject() {
		return this.xconf.getProperty("ecm.wz.extproperty.associateProject");
		// return associateProject;
	}


	/*public void setAssociateProject(String associateProject) {
		this.associateProject = associateProject;
	}*/

	public String getAssociateMapLink() {
		return this.xconf.getProperty("ecm.wz.extproperty.associateMapLink");
		// return associateMapLink;
	}

	/*public void setAssociateMapLink(String associateMapLink) {
		this.associateMapLink = associateMapLink;
	}*/

	public String getContent() {
		return this.xconf.getProperty("ecm.wz.extproperty.content");
		// return content;
	}

	/*public void setContent(String content) {
		this.content = content;
	}*/

	public String getAssociateFileLink() {
		return this.xconf.getProperty("ecm.wz.extproperty.associateFileLink");
		// return associateFileLink;
	}
/*
	public void setAssociateFileLink(String associateFileLink) {
		this.associateFileLink = associateFileLink;
	}*/

	public String getUpvoteList() {
		return this.xconf.getProperty("ecm.wz.extproperty.upvoteList");
		// return upvoteList;
	}

/*	public void setUpvoteList(String upvoteList) {
		this.upvoteList = upvoteList;
	}*/

	public String getSticky() {
		return this.xconf.getProperty("ecm.wz.extproperty.sticky");
		// return sticky;
	}

	/*public void setSticky(String sticky) {
		this.sticky = sticky;
	}*/
	/**
	 * 获取评论个数属性名称
	 * @return
	 */
	public String getCommentCount() {
		return this.xconf.getProperty("ecm.wz.extproperty.commentCount");
		// return commentCount;
	}

	/*public void setCommentCount(String commentCount) {
		this.commentCount = commentCount;
	}*/
	/**
	 * 获取微作类型属性
	 * @return
	 */
	public String getWZType(){
		
		return this.xconf.getProperty("ecm.wz.extproperty.wzType");
	}
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

	/**
	 * 获取关联的人的属性名称
	 * @return
	 */
	public String getAssociatePerson() {
		return this.xconf.getProperty("ecm.wz.extproperty.associatePerson");
		// return associatePerson;
	}
	/**
	 * 获取微作位置属性名称
	 * @return
	 */
	public String getLocation() {
		return this.xconf.getProperty("ecm.wz.extproperty.location");
		// return associatePerson;
	}
	/*public void setAssociatePerson(String associatePerson) {
		this.associatePerson = associatePerson;
	}*/
	
}
