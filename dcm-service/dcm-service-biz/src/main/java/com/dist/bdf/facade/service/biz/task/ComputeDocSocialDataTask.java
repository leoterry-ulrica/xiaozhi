package com.dist.bdf.facade.service.biz.task;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.RecursiveTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dist.bdf.base.utils.StringUtil;
import com.dist.bdf.facade.service.GroupService;
import com.dist.bdf.facade.service.UserOrgService;
import com.dist.bdf.common.constants.ResourceConstants;
import com.dist.bdf.manager.ecm.security.ConnectionService;
import com.dist.bdf.manager.ecm.utils.DistSocialUtil;
import com.dist.bdf.model.dto.dcm.DocumentDTO;
import com.dist.bdf.model.dto.dcm.DocumentSortDTO;
import com.dist.bdf.model.dto.dcm.SocialSummaryDTO;
import com.dist.bdf.model.entity.system.DcmGroup;
import com.dist.bdf.model.entity.system.DcmOrganization;
import com.dist.bdf.model.entity.system.DcmUser;
import com.ibm.ecm.util.p8.P8Connection;

/**
 * 
 * 计算文档的社交化数据
 * @author weifj
 *
 */
public class ComputeDocSocialDataTask extends RecursiveTask<List<DocumentSortDTO>> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Logger logger = LoggerFactory.getLogger(getClass());
	private static final int SEQUENTIAL_THRESHOLD = 5;

	private final int start;
	private final int end;
	private String targetObjectStore;
	private String keyword;
	
	private ConnectionService conn;
	private DistSocialUtil socialUtil;
	private GroupService groupService;
	private UserOrgService userService;
	private List<DocumentDTO> docs;
	
	public ComputeDocSocialDataTask(String targetObjectStore, ConnectionService conn, DistSocialUtil socialUtil, UserOrgService userService, GroupService groupService, List<DocumentDTO> docs, String keyword) {

		this.start = 0;
		this.end = docs.size();
		this.targetObjectStore = targetObjectStore;
		this.conn = conn;
		this.socialUtil = socialUtil;
		this.docs = docs;
		this.keyword = keyword;
		this.groupService = groupService;
		this.userService = userService;
	}

	public ComputeDocSocialDataTask(String targetObjectStore, ConnectionService conn, DistSocialUtil socialUtil, UserOrgService userService, GroupService groupService, List<DocumentDTO> docs, int start, int end, String keyword) {

		this.start = start;
		this.end = end;
		this.targetObjectStore = targetObjectStore;
		this.conn = conn;
		this.socialUtil = socialUtil;
		this.docs = docs;
		this.keyword = keyword;
		this.groupService = groupService;
		this.userService = userService;
	}

	@Override
	protected List<DocumentSortDTO> compute() {

		final int length = end - start;
		if (length < SEQUENTIAL_THRESHOLD) {
			return computeDirectly();
		}
		List<DocumentSortDTO> sortList = new ArrayList<DocumentSortDTO>();
		final int split = length / 2;
		final ComputeDocSocialDataTask left = new ComputeDocSocialDataTask(this.targetObjectStore, this.conn, this.socialUtil, this.userService, this.groupService, this.docs, this.start, this.start + split, this.keyword);
		left.fork();
		final ComputeDocSocialDataTask right = new ComputeDocSocialDataTask(this.targetObjectStore, this.conn, this.socialUtil, this.userService, this.groupService, this.docs, this.start + split, this.end, this.keyword);
		sortList.addAll(right.compute()); // 右边任务继续拆分
		sortList.addAll(left.join());
		
		return sortList;
	}

	private List<DocumentSortDTO>  computeDirectly() {

		List<DocumentSortDTO> subSortList = new ArrayList<DocumentSortDTO>();
		Date dateStart = new Date();
		SocialSummaryDTO ss = null;
		for (int i = start; i < end; i++) {
			
			DocumentDTO dto = this.docs.get(i);
			DocumentSortDTO sortModel = new DocumentSortDTO();
			// 把owner的dn换成用户的显示名称
			DcmUser owner = this.userService.getUserEntityByDN(dto.getOwner().toLowerCase());
			logger.debug(">>>通过dn [{}]获取用户信息......", dto.getOwner());
			dto.setOwner(null == owner? dto.getOwner() : owner.getUserName());
			sortModel.setDoc(dto);
			sortModel.setKeyword(keyword);
			logger.info(">>>当前文档资源类型为 [{}], 空间域为 [{}]", dto.getResourceType(), dto.getDomain());
			if (!StringUtil.isNullOrEmpty(dto.getDomain())) {
				if (dto.getResourceType().equals(ResourceConstants.ResourceType.RES_PCK_PROJECT)) {

					DcmGroup group = this.groupService.getGroupByGuid(dto.getDomain());
					if (group != null) {
						sortModel.setSource(group.getGroupName());
					}
				} else if (dto.getResourceType().equals(ResourceConstants.ResourceType.RES_PCK_PERSON)) {
					DcmUser user = userService.getUserEntityByCode(dto.getDomain());
					if (user != null) {
						sortModel.setSource(user.getUserName());
					}
				} else if (dto.getResourceType().equals(ResourceConstants.ResourceType.RES_PCK_DEPARTMENT)
						|| dto.getResourceType().equals(ResourceConstants.ResourceType.RES_PCK_INSTITUTE)) {

					DcmOrganization org = this.userService.getOrgByCode(dto.getDomain());
					if (org != null) {
						sortModel.setSource(org.getAlias());
					}
				}

			}
			
			try {
				logger.info(">>>通过文件的guid获取下载数......");
				P8Connection p8conn = this.conn.getP8Connection(targetObjectStore);
				ss = this.socialUtil.retrieveSocialData(p8conn.getObjectStore(), dto.getId());
				
			} catch (Exception ex) {
				logger.error(">>>无法获取资源id[{}]的下载信息......", dto.getId());
				logger.error(ex.getMessage());
				//ex.printStackTrace();
			}
			if(ss != null){
				sortModel.setDownloadCount(ss.getDownloadCount());
			}
			
			logger.info(">>>通过文件的系列id获取收藏数......");
			try {
				
				P8Connection p8conn = this.conn.getP8Connection(targetObjectStore);
				ss = this.socialUtil.retrieveSocialData(p8conn.getObjectStore(), dto.getVersionSeriesId());
				
			} catch (Exception ex) {
				logger.error(">>>无法获取资源系列id[{}]的收藏信息......", dto.getVersionSeriesId());
				logger.error(ex.getMessage());
				//ex.printStackTrace();
			}
			if(ss != null){
				sortModel.setFavorites(ss.getFavorites());
			}
			
			//sortModel.setUpvoteCount(ss.getUpvoteCount()); // 文件没有点赞操作
			
			subSortList.add(sortModel);
	
		}
		Date dateEnd = new Date();
		logger.info(">>>任务消耗时间：[{}] ms", (dateEnd.getTime() - dateStart.getTime()));
		return subSortList;
	}

}
