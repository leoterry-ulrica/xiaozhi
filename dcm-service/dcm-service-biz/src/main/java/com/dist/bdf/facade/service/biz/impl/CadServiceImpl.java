package com.dist.bdf.facade.service.biz.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.dist.bdf.base.page.Pagination;
import com.dist.bdf.base.utils.DocumentUtil;
import com.dist.bdf.facade.service.MaterialService;
import com.dist.bdf.facade.service.biz.domain.cad.CadTemplateDmn;
import com.dist.bdf.facade.service.biz.domain.system.DcmSocialResourceDmn;
import com.dist.bdf.facade.service.cad.CadService;
import com.dist.bdf.common.conf.ecm.ECMConf;
import com.dist.bdf.common.constants.ResourceConstants;
import com.dist.bdf.manager.ecm.security.ConnectionService;
import com.dist.bdf.manager.ecm.utils.FolderUtil;
import com.dist.bdf.manager.ecm.utils.SearchEngine;
import com.dist.bdf.model.dto.dcm.DocumentDTO;
import com.dist.bdf.model.dto.system.page.PageSimple;
import com.dist.bdf.model.entity.cad.ProposalEntity;
import com.dist.bdf.model.entity.cad.TemplateEntity;
import com.dist.bdf.model.entity.system.DcmSocialResource;
import com.filenet.api.core.Folder;
import com.ibm.ecm.util.p8.P8Connection;
/**
 * cad相关服务
 * @author weifj
 *
 */
@Service("CadService")
@Transactional(propagation = Propagation.REQUIRED)
public class CadServiceImpl implements CadService {

	private static Logger logger = LoggerFactory.getLogger(CadServiceImpl.class);
	@Autowired
	private CadTemplateDmn cadTemplateDmn;
	@Autowired
	private com.dist.bdf.manager.ecm.utils.DocumentUtil ceDocUtil;
	@Autowired
	private ConnectionService connService;
	@Autowired
	private ECMConf ecmConf;
	@Autowired
	private DcmSocialResourceDmn socialResDmn;
	@Autowired
	private SearchEngine searchEngine;
	@Autowired
	private FolderUtil folderUtil;
	@Autowired
	@Qualifier("CommonMaterialService")
	private MaterialService materialService;
	
	@Override
	public TemplateEntity saveOrUpdateTemplateMetadata(TemplateEntity entity) {
		 
		if(StringUtils.isEmpty(entity.getDocId())){
			// 没有文件上传
			TemplateEntity findTemplateEntity = this.cadTemplateDmn.getMetadataByName(entity.getName());
			if(findTemplateEntity != null) {
				entity.setDocId(findTemplateEntity.getDocId());
				entity.setId(findTemplateEntity.getId());
			}
		}
	
		// logger.info(">>>删除原有元数据条目");
		// this.cadTemplateDmn.deleteMetadataByName(entity.getName());
		logger.info(">>>添加或保存元数据条目");
		return this.cadTemplateDmn.saveMetadata(entity);
	}
	
	@Override
	public List<TemplateEntity> listTemplateMetadata() {
		
		return this.cadTemplateDmn.listTemplateMetadata();
	}
	
	@Override
	public Pagination listTemplates(String realm, int pageNo, int pageSize) {
		
		P8Connection p8conn = this.connService.getP8Connection(this.ecmConf.getTargetObjectStore(realm));
		Folder cadTemplateRoot = this.folderUtil.loadAndCreateByPath(p8conn.getObjectStore(), this.ecmConf.getCADTemplateDir());
		
		return this.searchEngine.findSubDocsPage(p8conn.getObjectStore(), cadTemplateRoot.get_Id().toString(), pageNo, pageSize);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Pagination listTemplates(String realm, String userCode, int pageNo, int pageSize) {
		
		P8Connection p8conn = this.connService.getP8Connection(this.ecmConf.getTargetObjectStore(realm));
		Folder cadTemplateRoot = this.folderUtil.loadAndCreateByPath(p8conn.getObjectStore(), this.ecmConf.getCADTemplateDir());
		Pagination page = this.searchEngine.findSubDocsPage(p8conn.getObjectStore(), cadTemplateRoot.get_Id().toString(), pageNo, pageSize);
		List<DocumentDTO> docs = (List<DocumentDTO>) page.getData();
		if(docs != null && !docs.isEmpty()) {
			for(DocumentDTO doc : docs) {
				DcmSocialResource sr = this.socialResDmn.getByResIdAndCreator(doc.getVersionSeriesId(), userCode);
				if(sr != null && 1 == sr.getIsFavorite()) {
					doc.setIsFavorite(true);
				} else {
					doc.setIsFavorite(false);
				}
			}
		}
		return page;
		
	}
	@Override
	public String listTemplateMetadataXML() {
		
		List<TemplateEntity> templates = this.cadTemplateDmn.listTemplateMetadata();
		return transforToTemplateMetadataXML(templates);
	}

	private String transforToTemplateMetadataXML(List<TemplateEntity> templates) {
		
		Document doc = DocumentUtil.createDocument();
		Element root = doc.createElement("Configurations");
		doc.appendChild(root);
		if(null == templates || templates.isEmpty()) {
			logger.info(">>>没有找到相关模板信息......");
			return DocumentUtil.convertXMLToString(doc);
		}
		logger.info(">>>找到相关模板信息：[{}]条", templates.size());
		for(TemplateEntity template : templates) {
			Element templateNode = doc.createElement("Template");
			templateNode.setAttribute("Name", template.getName());
			templateNode.setAttribute("CurrentProposalFile", template.getCurrentProposalFile());
			templateNode.setAttribute("Md5Code", template.getMd5Code());
			templateNode.setAttribute("FileNameExt", template.getSuffix());
			// 模板id
			templateNode.setAttribute("Id", template.getId().toHexString());
			// 文档id
			templateNode.setAttribute("DocId", template.getDocId());
			// 文档版本系列id
			templateNode.setAttribute("DocVId", template.getDocVId());
			root.appendChild(templateNode);
			
			ProposalEntity[] proposals = template.getProposals();
			if(null == proposals || 0 == proposals.length){
				logger.info(">>>找到相关方案信息......");
				continue;
			}
			logger.info(">>>找到相关方案信息：[{}]条", proposals.length);
			for(ProposalEntity proposal : proposals) {
				Element proposalNode = doc.createElement("Proposal");
				proposalNode.setAttribute("Name", proposal.getName());
				proposalNode.setAttribute("Description", proposal.getDescription());
				proposalNode.setAttribute("DbType", proposal.getDbType());
				proposalNode.setAttribute("DbName", proposal.getDbName());
				proposalNode.setAttribute("Server", proposal.getServer());
				proposalNode.setAttribute("DbUser", proposal.getDbUser());
				proposalNode.setAttribute("DbPassword", proposal.getDbPassword());
				proposalNode.setAttribute("LoginUser", proposal.getLoginUser());
				proposalNode.setAttribute("LoginPassword", proposal.getLoginPassword());
				
				templateNode.appendChild(proposalNode);
			}
		}
		
		return DocumentUtil.convertXMLToString(doc);
	}

	@Override
	public Object deleteTemplate(String realm, String templateId, String docId) {
		
		this.cadTemplateDmn.deleteMetadataById(templateId);
		P8Connection p8con = this.connService.getP8Connection(this.ecmConf.getTargetObjectStore(realm));
		this.ceDocUtil.deleteDocument(p8con.getObjectStore(), docId);
		
		return true;
	}
	@Override
	public Object deleteTemplate(String realm, String templateId, String docId, String docVId) {
	
		this.cadTemplateDmn.deleteMetadataById(templateId);
		this.materialService.deleteDocByVIdAdmin(realm, docVId);
		return true;
	}
	
	@Override
	public Object getFavoriteOfCadTemplate(PageSimple pageInfo) {

		Map<String, Object[]> queryPara = new HashMap<String, Object[]>();
		queryPara.put("creator", new Object[] { pageInfo.getUser() });
		queryPara.put("resTypeCode",
				new Object[] { ResourceConstants.ResourceType.RES_CAD_TEMPLATE});
		queryPara.put("isFavorite", new Object[] { 1L });// 注意：需要传入long型

		Pagination page = this.socialResDmn.findByPropertiesAndValues( pageInfo.getPageNo(), pageInfo.getPageSize(), queryPara, "timeFavorite",
				Boolean.FALSE);
		List<DocumentDTO> resultData = new ArrayList<DocumentDTO>();
		@SuppressWarnings("unchecked")
		List<DcmSocialResource> socialResList = (List<DcmSocialResource>) page.getData();
		if (socialResList != null && socialResList.size() > 0) {
			P8Connection p8conn = this.connService.getP8Connection(this.ecmConf.getTargetObjectStore(pageInfo.getRealm()));
			DocumentDTO tempDocDTO = null;
			for (DcmSocialResource sr : socialResList) {
				tempDocDTO = this.ceDocUtil.loadDocDTOByVersionSeriesId(p8conn.getObjectStore(), sr.getResId());
				if(null == tempDocDTO) continue;
				
				tempDocDTO.setIsFavorite(true);
				resultData.add(tempDocDTO);
			}
		}
		page.setData(resultData);
		return page;
	}
	@Override
	public String getFavoriteOfCadTemplateForBJT(String userCode) {
		
		Map<String, Object[]> queryPara = new HashMap<String, Object[]>();
		queryPara.put("creator", new Object[] { userCode });
		queryPara.put("resTypeCode",
				new Object[] { ResourceConstants.ResourceType.RES_CAD_TEMPLATE});
		queryPara.put("isFavorite", new Object[] { 1L });// 注意：需要传入long型
		
		List<DcmSocialResource> socialResources = this.socialResDmn.findByProperties(new String[]{"creator", "resTypeCode", "isFavorite"},  
				new Object[]{userCode, ResourceConstants.ResourceType.RES_CAD_TEMPLATE, 1L}, "timeFavorite", Boolean.FALSE);//.findByPropertiesAndValues(queryPara, );
	
		List<String> versionIds = new ArrayList<String>();
		for(DcmSocialResource socialResource : socialResources) {
			versionIds.add(socialResource.getResId());
		}
	
		List<TemplateEntity> templateEntities = this.cadTemplateDmn.getTemplateMetadata(versionIds.toArray());
		return this.transforToTemplateMetadataXML(templateEntities);
	}
}
