package com.dist.bdf.model.dto.system;

import java.util.LinkedList;
import java.util.List;

import com.dist.bdf.base.dto.BaseDTO;
import com.dist.bdf.model.dto.dcm.CaseDTO;
import com.dist.bdf.model.dto.dcm.DocumentDTO;
/**
 * 微作信息
 * @author weifj
 * @version 1.0，2016/04/25，weifj，创建
 */
public class WzInfoDTO extends BaseDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String documentTitle;
	private String creator;
	private String associateTache;
	private String content;
	private CaseDTO associateProject;
	private String associateTask;
	private Integer upvoteCount; // 
	private String dateCreated; 
	private String dateLastModified;
	private String lastModifier;
	private String name;
	private String owner;
	private Object[] associatePersons;
	/**
	 * 置顶属性
	 */
	private Long sticky;
	/**
	 * 发布者
	 */
	private String publisher;
	/**
	 * 评论数，对应属性【XZ_CommentCount】
	 */
	private Integer commentCount;
	/**
	 * 调研微作的位置信息
	 */
	private String location;
	/**
	 * 城市
	 */
	private String city;
	
	private Integer type;
	/**
	 * 任务类型的微作扩展属性
	 */
	private TaskResponseDTO taskPropEx;
	
	public Integer getCommentCount() {
		return commentCount;
	}
	public void setCommentCount(Integer commentCount) {
		this.commentCount = commentCount;
	}
	/**
	 * 对于移动，特指除了图片格式的文档
	 */
	private List<DocumentDTO> associateFileLinks = new LinkedList<DocumentDTO>();
	private List<ThumbnailDTO> thumbnails = new LinkedList<ThumbnailDTO>();
	
	/**
	 * 文档标题
	 * @return
	 */
	public String getDocumentTitle() {
		return documentTitle;
	}
	public void setDocumentTitle(String documentTitle) {
		this.documentTitle = documentTitle;
	}
	/**
	 * 创建者
	 * @return
	 */
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	/**
	 * 关联环节
	 * @return
	 */
	public String getAssociateTache() {
		return associateTache;
	}
	public void setAssociateTache(String associateTache) {
		this.associateTache = associateTache;
	}
	/**
	 * 微作内容
	 * @return
	 */
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * 关联任务
	 * @return
	 */
	public String getAssociateTask() {
		return associateTask;
	}
	public void setAssociateTask(String associateTask) {
		this.associateTask = associateTask;
	}
	/**
	 * 点赞数
	 * @return
	 */
	public Integer getUpvoteCount() {
		return this.upvoteCount;
	}
	public void setUpvoteCount(Integer upvoteCount) {
		this.upvoteCount = upvoteCount;
	}
	/**
	 * 创建时间
	 * @return
	 */
	public String getDateCreated() {
		return dateCreated;
	}
	public void setDateCreated(String dateCreated) {
		this.dateCreated = dateCreated;
	}
	/**
	 * 上一次修改时间
	 * @return
	 */
	public String getDateLastModified() {
		return dateLastModified;
	}
	public void setDateLastModified(String dateLastModified) {
		this.dateLastModified = dateLastModified;
	}
	/**
	 * 上一个修改者
	 * @return
	 */
	public String getLastModifier() {
		return lastModifier;
	}
	public void setLastModifier(String lastModifier) {
		this.lastModifier = lastModifier;
	}
	/**
	 * 名称，一般跟DocumentTitle相同
	 * @return
	 */
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * 所有者
	 * @return
	 */
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	public Object[] getAssociatePersons() {
		return associatePersons;
	}
	public void setAssociatePersons(Object[] associatePersons) {
		this.associatePersons = associatePersons;
	}

	public void addAssociateFileLink(DocumentDTO associateFileLink) {
		this.associateFileLinks.add(associateFileLink);
	}
	public List<DocumentDTO> getAssociateFileLinks() {
		return associateFileLinks;
	}
	public void setAssociateFileLinks(List<DocumentDTO> associateFileLinks) {
		this.associateFileLinks = associateFileLinks;
	}
	/**
	 * 关联项目（case）
	 * @return
	 */
	public CaseDTO getAssociateProject() {
		return associateProject;
	}
	public void setAssociateProject(CaseDTO associateProject) {
		this.associateProject = associateProject;
	}
	public List<ThumbnailDTO> getThumbnails() {
		return thumbnails;
	}
	public void setThumbnails(List<ThumbnailDTO> thumbnails) {
		this.thumbnails = thumbnails;
	}
	/**
	 * 添加缩略图
	 * @param id
	 * @param imgBase64Code
	 */
	public void addThumbnails(String docId, String name, String mimeType, String imgBase64Code) {
		
		ThumbnailDTO thum = new ThumbnailDTO();
		thum.setId(docId);
		thum.setName(name);
		thum.setMimeType(mimeType);
		thum.setImg(imgBase64Code);
		
		this.thumbnails.add(thum);
	}
	/**
	 * 添加缩略图
	 * @param id
	 * @param imgBase64Code
	 */
	public void addThumbnails(ThumbnailDTO thumDTO) {
		
		this.thumbnails.add(thumDTO);
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	/**
	 * 0：一般微作；1：调研微作；2：任务微作
	 */
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	/**
	 * 获取 #{bare_field_comment} 
	 * @return the publisher
	 */
	public String getPublisher() {
		return publisher;
	}
	/**
	 * 设置 #{bare_field_comment}
	 * @param publisher the publisher to set
	 */
	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}
	public TaskResponseDTO getTaskPropEx() {
		return taskPropEx;
	}
	public void setTaskPropEx(TaskResponseDTO taskPropEx) {
		this.taskPropEx = taskPropEx;
	}
	public Long getSticky() {
		return sticky;
	}
	public void setSticky(Long sticky) {
		this.sticky = sticky;
	}
}
