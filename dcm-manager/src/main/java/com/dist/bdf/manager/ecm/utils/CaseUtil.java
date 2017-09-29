package com.dist.bdf.manager.ecm.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.dist.bdf.base.constants.MimeType;
import com.dist.bdf.base.exception.BusinessException;
import com.dist.bdf.base.page.Pagination;
import com.dist.bdf.base.utils.AddressUtil;
import com.dist.bdf.base.utils.DateUtil;
import com.dist.bdf.common.conf.ecm.ECMConf;
import com.dist.bdf.common.conf.ecm.ExtPropertyCaseConf;
import com.dist.bdf.common.conf.ecm.ExtPropertyMaterialConf;
import com.dist.bdf.common.conf.ecm.ExtPropertyWZConf;
import com.dist.bdf.manager.ecm.define.DataType;
import com.dist.bdf.manager.ecm.define.SimplePropertyFilter;
import com.dist.bdf.model.dto.dcm.CaseDTO;
import com.dist.bdf.model.dto.dcm.CaseTypeDTO;
import com.dist.bdf.model.dto.dcm.DocumentDTO;
import com.dist.bdf.model.dto.system.ThumbnailDTO;
import com.dist.bdf.model.dto.system.WzInfoDTO;
import com.filenet.api.collection.IndependentObjectSet;
import com.filenet.api.collection.PageIterator;
import com.filenet.api.collection.StringList;
import com.filenet.api.constants.RefreshMode;
import com.filenet.api.core.Document;
import com.filenet.api.core.Folder;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.property.Properties;
import com.filenet.api.property.PropertyFilter;
import com.filenet.api.query.SearchSQL;
import com.filenet.api.query.SearchScope;
import com.filenet.apiimpl.core.SubSetImpl;
import com.ibm.casemgmt.api.CaseType;
import com.ibm.casemgmt.api.DeployedSolution;

@Component
public class CaseUtil {

	private static Logger logger = LoggerFactory.getLogger(CaseUtil.class);
	@Autowired
	private DocumentUtil docUtil;
	@Autowired
	private SearchEngine searchEngine;
    @Autowired
    private ECMConf ecmConf;
    @Autowired
	@Qualifier("ExtPropertyCaseConf")
    private ExtPropertyCaseConf extPropCaseConf;
    @Autowired
	@Qualifier("ExtPropertyWZConf")
    private ExtPropertyWZConf extPropWZConf;
	@Autowired
	private ExtPropertyMaterialConf extPropMaterialConf;
	/**
	 * 构建案例查询SQL
	 * @return
	 */
	private StringBuilder createCaseSelectQueryBuilder() {
		StringBuilder buf = new StringBuilder();
		buf.append("SELECT  * ");
		/*buf.append(
				" CmAcmCaseIdentifier, CmAcmCaseState, Creator, DateCreated, DateLastModified, FolderName, Id, LastModifier, Name, Owner,");
		buf.append(this.extPropCaseConf.getProjectName()+",");
		buf.append(this.extPropCaseConf.getWtdw()+",");
		buf.append(this.extPropCaseConf.getAddressDetail()+",");
		buf.append(this.extPropCaseConf.getMajorType()+",");
		buf.append(this.extPropCaseConf.getProjectType()+",");
		buf.append(this.extPropCaseConf.getManagementLevel()+",");
		buf.append(this.extPropCaseConf.getQtbm()+",");
		buf.append(this.extPropCaseConf.getPlanType()+",");
		buf.append(this.extPropCaseConf.getProjectLeader()+",");
		buf.append(this.extPropCaseConf.getProjectAssistant()+",");
		buf.append(this.extPropCaseConf.getProjectScale()+",");
		buf.append(this.extPropCaseConf.getYwphbm()+",");
		buf.append(this.extPropCaseConf.getProjectStatus()+",");
		buf.append(this.extPropCaseConf.getProvince()+",");
		buf.append(this.extPropCaseConf.getCity()+",");
		buf.append(this.extPropCaseConf.getCounty()+",");
		buf.append(this.extPropCaseConf.getBusinessType());*/
		
		//buf.append(extPropCaseConf.toString());
	/*	buf.append(
				" XZ_XMMC, XZ_XMWTDW, XZ_XXDZ, XZ_ZYLB, XZ_XMLX, XZ_GLJB,XZ_QTBM,XZ_GHLB, ");
		buf.append(" XZ_RY_XMFZR, XZ_XMGM, XZ_YWPHBM, XZ_XMZT, ");
		buf.append(" XZ_XMQY_SHENG,XZ_XMQY_SHI,XZ_XMQY_XIAN ");*/
		buf.append(" FROM CmAcmCaseFolder");
		return buf;
	}

	/**
	 * 构建微作查询SQL
	 * @return
	 */
	private StringBuilder createWZSelectQueryBuilder() {

		StringBuilder buf = new StringBuilder();
		buf.append("SELECT * ");
		/*buf.append("SELECT DocumentTitle, Creator, DateCreated, DateLastModified, Id, LastModifier, Name, Owner ,");
	    buf.append(this.extPropWZConf.getAssociateTache()+",");
	    buf.append(this.extPropWZConf.getContent()+",");
	    buf.append(this.extPropWZConf.getAssociateProject()+",");
	    buf.append(this.extPropWZConf.getAssociateTask()+",");
	    buf.append(this.extPropWZConf.getUpvoteCount()+",");
	    buf.append(this.extPropWZConf.getAssociatePerson()+",");
	    buf.append(this.extPropWZConf.getAssociateFileLink()+",");
	    buf.append(this.extPropWZConf.getCommentCount()+",");
	    buf.append(this.extPropWZConf.getLocation()+",");
	    buf.append(this.extPropWZConf.getWZType());*/
	    
		/*buf.append(
				"XZ_AssociateTache,XZ_Content,XZ_AssociateProject, XZ_AssociateTask,XZ_UpvoteCount,XZ_AssociatePerson,XZ_AssociateFileLink, XZ_CommentCount");
		*/
	    buf.append(" FROM "+this.ecmConf.getWzDefaultClassId());

		return buf;
	}

	
	/**
	 * 获取案例类型
	 * 
	 * @param solution
	 * @return
	 */
	public List<CaseTypeDTO> getCaseTypes(DeployedSolution solution) {

		logger.debug("获取solution [{}]下的所有案例类型", solution);
		// connectionService.initialize();
		List<CaseType> cts = solution.getCaseTypes();
		List<CaseTypeDTO> links = new LinkedList<CaseTypeDTO>();
		for (CaseType ct : cts) {
			CaseTypeDTO ctd = new CaseTypeDTO();
			ctd.setGuid(ct.getId().toString());
			ctd.setName(ct.getName());
			ctd.setDisplayName(ct.getDisplayName());
			links.add(ctd);
		}

		return links;
	}

	/**
	 * 根据类型获取案例实例
	 * 
	 * @param typePrefix
	 *            案例类型前缀
	 * @return
	 */
	public List<CaseDTO> getCasesByType(ObjectStore os, String typePrefix) {

		List<CaseDTO> linklist = new LinkedList<CaseDTO>();
		StringBuilder buf = createCaseSelectQueryBuilder();
		buf.append("  WHERE CmAcmCaseIdentifier LIKE'");
		buf.append(typePrefix);
		buf.append("%'");

		SearchScope ss = new SearchScope(os);
		SearchSQL sql = new SearchSQL(buf.toString());
		IndependentObjectSet ios = ss.fetchObjects(sql, (Integer) null, (PropertyFilter) null, Boolean.FALSE);
		Folder caseFolder = null;
		Iterator<?> it = ios.iterator();
		logger.debug("获取到[CaseTyep={}]下的Case：", typePrefix);
		while (it.hasNext()) {
			caseFolder = (Folder) it.next();
			Properties properties = caseFolder.getProperties();
			CaseDTO dto = convertToCaseDTO(properties);

			linklist.add(dto);
		}

		return linklist;
	}

	/**
	 * 根据案例标识获取案例实体
	 * @param os
	 * @param caseIdentifier
	 * @return
	 */
	public CaseDTO getCaseByIdentifier(ObjectStore os, String caseIdentifier) {

		StringBuilder buf = createCaseSelectQueryBuilder();
		buf.append("  WHERE CmAcmCaseIdentifier ='");
		buf.append(caseIdentifier);
		buf.append("'");

		SearchScope ss = new SearchScope(os);
		SearchSQL sql = new SearchSQL(buf.toString());
		IndependentObjectSet ios = ss.fetchObjects(sql, (Integer) null, (PropertyFilter) null, Boolean.FALSE);
		Folder caseFolder = null;
		Iterator it = ios.iterator();
		CaseDTO dto = null;
		while (it.hasNext()) {
			caseFolder = (Folder) it.next();

			Properties properties = caseFolder.getProperties();
			dto = convertToCaseDTO(properties);

			break;
		}

		return dto;
	}
	/**
	 * 批量获取
	 * @param os
	 * @param caseIds
	 * @return
	 */
	public List<CaseDTO> getCasesById(ObjectStore os, String[] caseIds) {

		Assert.notEmpty(caseIds);
		
		boolean flag = true;
		StringBuilder buf = createCaseSelectQueryBuilder();
		buf.append("  WHERE Id in (");
		for(String caseId : caseIds) {
			if(flag) {
				buf.append("'"+caseId+"'");
				flag = false;
				continue;
			}
			buf.append(",'"+caseId+"'");
		}
		buf.append(")");

		return getCasesByQueryString(os, buf.toString());
	}
	/**
	 * 
	 * @param os
	 * @param queryStr
	 * @return
	 */
	public List<CaseDTO> getCasesByQueryString(ObjectStore os, String queryStr) {
		SearchScope ss = new SearchScope(os);
		SearchSQL sql = new SearchSQL(queryStr);
		IndependentObjectSet ios = ss.fetchObjects(sql, (Integer) null, (PropertyFilter) null, Boolean.FALSE);
		Folder caseFolder = null;
		Iterator it = ios.iterator();
		List<CaseDTO> dtos = new ArrayList<CaseDTO>();
		CaseDTO tempCaseDTO = null;
		while (it.hasNext()) {
			caseFolder = (Folder) it.next();
			Properties properties = caseFolder.getProperties();
			tempCaseDTO = this.convertToCaseDTO(properties);
			if(tempCaseDTO != null) {
				dtos.add(tempCaseDTO);
			}
		}
		return dtos;
	}
	/**
	 * 根据case的id查询
	 * @param os
	 * @param caseId
	 * @return
	 */
	public CaseDTO getCaseById(ObjectStore os, String caseId) {

		List<CaseDTO> caseDTOs = this.getCasesById(os, new String[]{caseId});
		if(null == caseDTOs || 0 == caseDTOs.size()) {
			return null;
		}
		return caseDTOs.get(0);
	/*	StringBuilder buf = createCaseSelectQueryBuilder();
		buf.append("  WHERE Id ='");
		buf.append(caseId);
		buf.append("'");

		SearchScope ss = new SearchScope(os);
		SearchSQL sql = new SearchSQL(buf.toString());
		IndependentObjectSet ios = ss.fetchObjects(sql, (Integer) null, (PropertyFilter) null, Boolean.FALSE);
		Folder caseFolder = null;
		Iterator it = ios.iterator();
		CaseDTO dto = null;
		while (it.hasNext()) {
			caseFolder = (Folder) it.next();

			Properties properties = caseFolder.getProperties();
			dto = convertToCaseDTO(properties);

			break;
		}
		return dto;*/
	}
	public List<CaseDTO> getCasesById(ObjectStore os, List<String> caseIds) {

		String[] caseIdArray = new String[caseIds.size()];
		return this.getCasesById(os, caseIds.toArray(caseIdArray));
	}
	/**
	 * 根据过滤器检索case，默认不排序
	 * @param os
	 * @param filters
	 * @return
	 */
	public List<CaseDTO> getCasesByFilter(ObjectStore os, List<SimplePropertyFilter> filters) {
		
		Assert.notEmpty(filters);
		boolean flag = false;
		boolean isString = false;
		StringBuilder buf = createCaseSelectQueryBuilder();
		buf.append(" WHERE 1=1 ");
		// XZ_AssociatePerson  INTERSECTS('FA8458EB-9D85-42D6-A234-8D22489C6859') 
		for(SimplePropertyFilter filter : filters) {
			if(filter.getPropertyType().equalsIgnoreCase(DataType.ListOfString)){
				buf.append(" AND "+ filter.getPropertyName()+" INTERSECTS(");	
			}else{
				buf.append(" AND "+ filter.getPropertyName()+" IN(");	
			}
			for(Object val : filter.getValues()){
				if(!isString && val instanceof String){
					isString = true;
				}
				if(!flag){
					buf.append(isString? "'"+val+"'" : val);
					flag = true;
					continue;
				}
				buf.append(isString? ",'"+val+"'" : val);
			}
			buf.append(")");
			flag = false;
			isString = false;
		}
		return this.getCasesByQueryString(os, buf.toString());
	}
	/**
	 * 根据过滤器检索case，默认以最后修改时间（DateLastModified）倒序
	 * @param os
	 * @param filters
	 * @param orderProperty 排序属性名称
	 * @param asc 是否升序，true：升序；false：倒序
	 * @return
	 */
	public List<CaseDTO> getCasesByFilter(ObjectStore os, List<SimplePropertyFilter> filters, String orderProperty, boolean asc) {
		
		Assert.notEmpty(filters);
		boolean flag = false;
		boolean isString = false;
		StringBuilder buf = createCaseSelectQueryBuilder();
		buf.append(" WHERE 1=1 ");
		// XZ_AssociatePerson  INTERSECTS('FA8458EB-9D85-42D6-A234-8D22489C6859') 
		for(SimplePropertyFilter filter : filters) {
			if(filter.getPropertyType().equalsIgnoreCase(DataType.ListOfString)){
				buf.append(" AND "+ filter.getPropertyName()+" INTERSECTS(");	
			}else{
				buf.append(" AND "+ filter.getPropertyName()+" IN(");	
			}
			for(Object val : filter.getValues()){
				if(!isString && val instanceof String){
					isString = true;
				}
				if(!flag){
					buf.append(isString? "'"+val+"'" : val);
					flag = true;
					continue;
				}
				buf.append(isString? ",'"+val+"'" : val);
			}
			buf.append(")");
			flag = false;
			isString = false;
		}
		buf.append(asc? " order by " + orderProperty : " order by " +orderProperty+" desc");
		return this.getCasesByQueryString(os, buf.toString());
	}
	
     public List<WzInfoDTO> getWzsByFilter(ObjectStore os, List<SimplePropertyFilter> filters) {
		
		Assert.notEmpty(filters);
		boolean flag = false;
		boolean isString = false;
		StringBuilder buf = createWZSelectQueryBuilder();
		buf.append(" WHERE 1=1 ");
		// XZ_AssociatePerson  INTERSECTS('FA8458EB-9D85-42D6-A234-8D22489C6859') 
		for(SimplePropertyFilter filter : filters) {
			if(null == filter.getValues()){
				// 过滤属性为null的记录
				buf.append(" AND "+ filter.getPropertyName()+" IS NULL ");	
				continue;
			}
			if(filter.getPropertyType().equalsIgnoreCase(DataType.ListOfString)){
				buf.append(" AND "+ filter.getPropertyName()+" INTERSECTS(");	
			}else{
				buf.append(" AND "+ filter.getPropertyName()+" IN(");	
			}
			for(Object val : filter.getValues()){
				if(!isString && val instanceof String){
					isString = true;
				}
				if(!flag){
					buf.append(isString? "'"+val+"'" : val);
					flag = true;
					continue;
				}
				buf.append(isString? ",'"+val+"'" : val);
			}
			buf.append(")");
			flag = false;
			isString = false;
		}
		return this.getWzInfosByQueryString(os, buf.toString());
	}
	
	private List<WzInfoDTO> getWzInfosByQueryString(ObjectStore os, String queryStr) {
		SearchScope ss = new SearchScope(os);
		SearchSQL sql = new SearchSQL(queryStr);
		IndependentObjectSet ios = ss.fetchObjects(sql, (Integer) null, (PropertyFilter) null, Boolean.FALSE);
		Document caseFolder = null;
		Iterator it = ios.iterator();
		List<WzInfoDTO> dtos = new ArrayList<WzInfoDTO>();
		WzInfoDTO tempCaseDTO = null;
		while (it.hasNext()) {
			caseFolder = (Document) it.next();
			tempCaseDTO = this.document2WzDto(os, caseFolder, false, false);
			if(tempCaseDTO != null) {
				dtos.add(tempCaseDTO);
			}
		}
		return dtos;
	}

	/**
	 * 转换成CaseDTO
	 * 
	 * @param properties
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private CaseDTO convertToCaseDTO(Properties properties) {

		CaseDTO dto = new CaseDTO();
		dto.setCaseIdentifier(properties.getObjectValue("CmAcmCaseIdentifier").toString());
		dto.setCaseState(properties.getObjectValue("CmAcmCaseState").toString());
		dto.setCreator(properties.getObjectValue("Creator").toString());
		dto.setDateCreated(properties.getDateTimeValue("DateCreated").getTime());
		dto.setDateLastModified(properties.getDateTimeValue("DateLastModified").getTime());
		dto.setModifiedOn(properties.getDateTimeValue("DateLastModified").getTime());
		dto.setFolderName(properties.getObjectValue("FolderName").toString());
		dto.setGuid(properties.getObjectValue("Id").toString());
		dto.setLastModifier(properties.getObjectValue("LastModifier").toString());
		dto.setName(properties.getStringValue("Name"));
		//DcmUser user = distCacheManager.getUser(properties.getStringValue("Owner").toLowerCase());
		/*if(user != null){
			dto.setOwner(user.getUserName());
		}else {
			dto.setOwner(properties.getStringValue("Owner"));
		}*/
		dto.setOwner(properties.getStringValue("Owner"));
		// dto.setClassDescription(properties.getStringValue("ClassDescription"));
		// dto.setCaseType(properties.getStringValue("CmAcmCaseTypeFolder"));
  
		dto.setProjectName(properties.isPropertyPresent(this.extPropCaseConf.getProjectName()) ? properties.getStringValue(this.extPropCaseConf.getProjectName()) : "");
        dto.setProjectCode(properties.isPropertyPresent(this.extPropCaseConf.getProjectNo()) ? properties.getStringValue(this.extPropCaseConf.getProjectNo()) : "");
		dto.setDelegateUnit(
				properties.isPropertyPresent(this.extPropCaseConf.getWtdw()) ? properties.getStringValue(this.extPropCaseConf.getWtdw()) : "");

		dto.setProjectAddress(
				properties.isPropertyPresent(this.extPropCaseConf.getAddressDetail()) ? properties.getStringValue(this.extPropCaseConf.getAddressDetail()) : "");

		dto.setMajorType(properties.isPropertyPresent(this.extPropCaseConf.getMajorType()) ? properties.getStringValue(this.extPropCaseConf.getMajorType()) : "");

		dto.setProjectType(properties.isPropertyPresent(this.extPropCaseConf.getProjectType()) ? properties.getStringValue(this.extPropCaseConf.getProjectType()) : "");

		dto.setManagementLevel(
				properties.isPropertyPresent(this.extPropCaseConf.getManagementLevel()) ? properties.getStringValue(this.extPropCaseConf.getManagementLevel()) : "");

		dto.setOrg(properties.isPropertyPresent(this.extPropCaseConf.getQtbm()) ? properties.getStringValue(this.extPropCaseConf.getQtbm()) : "");

		dto.setBusiness(properties.isPropertyPresent(this.extPropCaseConf.getPlanType()) ? properties.getStringValue(this.extPropCaseConf.getPlanType()) : "");

		dto.setProjectManager(
				properties.isPropertyPresent(this.extPropCaseConf.getProjectLeader()) ? properties.getStringValue(this.extPropCaseConf.getProjectLeader()) : "");

		dto.setScale(
				properties.isPropertyPresent(this.extPropCaseConf.getProjectScale()) ? properties.getStringValue(this.extPropCaseConf.getProjectScale()) : "");
		dto.setYwphbm(
				properties.isPropertyPresent(this.extPropCaseConf.getYwphbm()) ? properties.getStringValue(this.extPropCaseConf.getYwphbm()) : "");
		
		//dto.setWtdw(properties.isPropertyPresent(this.extPropCaseConf.getWtdw()) ? properties.getStringValue(this.extPropCaseConf.getWtdw()) : "");
		if(properties.isPropertyPresent(this.extPropCaseConf.getBusinessType())){
			// 多值属性
			StringList businessTypes = properties.getStringListValue(this.extPropCaseConf.getBusinessType());
			if(businessTypes != null && !businessTypes.isEmpty()){
				Object[] array = businessTypes.toArray();
				dto.setBusinessType(org.apache.commons.lang3.StringUtils.join(array, ","));
			}
		}
	
		dto.setProjectAssistant(properties.isPropertyPresent(this.extPropCaseConf.getProjectAssistant()) ? properties.getStringValue(this.extPropCaseConf.getProjectAssistant()) : "");
		
		dto.setProjectStatus(
				properties.isPropertyPresent(this.extPropCaseConf.getProjectStatus()) ? properties.getStringValue(this.extPropCaseConf.getProjectStatus()) : "");

		dto.setOrgCoo(properties.isPropertyPresent(this.extPropCaseConf.getPhbm()) ? properties.getStringValue(this.extPropCaseConf.getPhbm()) : "");
		if(properties.isPropertyPresent(this.extPropCaseConf.getQtphbm())) {
			StringList orgOtherCooList = properties.getStringListValue(this.extPropCaseConf.getQtphbm());
			if(orgOtherCooList != null && !orgOtherCooList.isEmpty()) {
				dto.setOrgOtherCoo((String[]) orgOtherCooList.toArray(new String[orgOtherCooList.size()]));
			}
		}

		String province = properties.isPropertyPresent(this.extPropCaseConf.getProvince())
				? (null == properties.getStringValue(this.extPropCaseConf.getProvince()) ? ""
						: properties.getStringValue(this.extPropCaseConf.getProvince()))
				: "";
		dto.setProvince(province);
		String city = properties.isPropertyPresent(this.extPropCaseConf.getCity())
				? (null == properties.getStringValue(this.extPropCaseConf.getCity()) ? ""
						: properties.getStringValue(this.extPropCaseConf.getCity()))
				: "";
		dto.setCity(city);
		String county = properties.isPropertyPresent(this.extPropCaseConf.getCounty())
				? (null == properties.getStringValue(this.extPropCaseConf.getCounty()) ? ""
						: properties.getStringValue(this.extPropCaseConf.getCounty()))
				: "";
        dto.setCounty(county);
		dto.setProjectRegion(province +","+ city +"," + county);
		// 中标金额
		if(properties.isPropertyPresent(this.extPropCaseConf.getZBJE())) {
			dto.setZbje(properties.getFloat64Value(this.extPropCaseConf.getZBJE()));
		}
		// 中标结果
		if(properties.isPropertyPresent(this.extPropCaseConf.getZBJG())) {
			dto.setZbjg(properties.getBooleanValue(this.extPropCaseConf.getZBJG()));
		}
		// 中标单位
		if(properties.isPropertyPresent(this.extPropCaseConf.getZBDW())) {
			dto.setZbdw(properties.getStringValue(this.extPropCaseConf.getZBDW()));
		}
		// 中标时间
		if(properties.isPropertyPresent(this.extPropCaseConf.getZBSJ())) {
			dto.setZbsj(properties.getDateTimeValue(this.extPropCaseConf.getZBSJ()));
		}
		return dto;
	}
	/**
	 * 转换成CaseDTO
	 * 
	 * @param properties
	 * @return
	 */
	@Deprecated
	private CaseDTO convertToCaseDTOEx(Properties properties) {

		CaseDTO dto = new CaseDTO();
		dto.setCaseIdentifier(properties.getObjectValue("CmAcmCaseIdentifier").toString());
		dto.setCaseState(properties.getObjectValue("CmAcmCaseState").toString());
		dto.setCreator(properties.getObjectValue("Creator").toString());
		dto.setDateCreated(properties.getDateTimeValue("DateCreated").getTime());
		dto.setDateLastModified(properties.getDateTimeValue("DateLastModified").getTime());
		dto.setFolderName(properties.getObjectValue("FolderName").toString());
		dto.setGuid(properties.getObjectValue("Id").toString());
		dto.setLastModifier(properties.getObjectValue("LastModifier").toString());
		dto.setName(properties.getStringValue("Name"));
		dto.setOwner(properties.getStringValue("Owner"));
		// dto.setClassDescription(properties.getStringValue("ClassDescription"));
		// dto.setCaseType(properties.getStringValue("CmAcmCaseTypeFolder"));

		dto.setProjectName(properties.isPropertyPresent(this.extPropCaseConf.getProjectName()) ? properties.getStringValue(this.extPropCaseConf.getProjectName()) : "");

		dto.setDelegateUnit(
				properties.isPropertyPresent(this.extPropCaseConf.getWtdw()) ? properties.getStringValue(this.extPropCaseConf.getWtdw()) : "");

		dto.setProjectAddress(
				properties.isPropertyPresent(this.extPropCaseConf.getAddressDetail()) ? properties.getStringValue(this.extPropCaseConf.getAddressDetail()) : "");

		dto.setMajorType(properties.isPropertyPresent(this.extPropCaseConf.getMajorType()) ? properties.getStringValue(this.extPropCaseConf.getMajorType()) : "");

		dto.setProjectType(properties.isPropertyPresent(this.extPropCaseConf.getProjectType()) ? properties.getStringValue(this.extPropCaseConf.getProjectType()) : "");

		dto.setManagementLevel(
				properties.isPropertyPresent(this.extPropCaseConf.getManagementLevel()) ? properties.getStringValue(this.extPropCaseConf.getManagementLevel()) : "");

		dto.setOrg(properties.isPropertyPresent(this.extPropCaseConf.getQtbm()) ? properties.getStringValue(this.extPropCaseConf.getQtbm()) : "");

		dto.setBusiness(properties.isPropertyPresent(this.extPropCaseConf.getPlanType()) ? properties.getStringValue(this.extPropCaseConf.getPlanType()) : "");

		dto.setProjectManager(
				properties.isPropertyPresent(this.extPropCaseConf.getProjectLeader()) ? properties.getStringValue(this.extPropCaseConf.getProjectLeader()) : "");

		dto.setScale(
				properties.isPropertyPresent(this.extPropCaseConf.getProjectScale()) ? properties.getStringValue(this.extPropCaseConf.getProjectScale()) : "");
		dto.setScale(
				properties.isPropertyPresent(this.extPropCaseConf.getYwphbm()) ? properties.getStringValue(this.extPropCaseConf.getYwphbm()) : "");

		dto.setProjectStatus(
				properties.isPropertyPresent(this.extPropCaseConf.getProjectStatus()) ? properties.getStringValue(this.extPropCaseConf.getProjectStatus()) : "");

		String province = properties.isPropertyPresent(this.extPropCaseConf.getProvince())
				? (null == properties.getStringValue(this.extPropCaseConf.getProvince()) ? ""
						: properties.getStringValue(this.extPropCaseConf.getProvince()))
				: "";
		dto.setProvince(province);
		String city = properties.isPropertyPresent(this.extPropCaseConf.getCity())
				? (null == properties.getStringValue(this.extPropCaseConf.getCity()) ? ""
						: properties.getStringValue(this.extPropCaseConf.getCity()))
				: "";
		dto.setCity(city);
		String county = properties.isPropertyPresent(this.extPropCaseConf.getCounty())
				? (null == properties.getStringValue(this.extPropCaseConf.getCounty()) ? ""
						: properties.getStringValue(this.extPropCaseConf.getCounty()))
				: "";
		dto.setCounty(county);
		dto.setProjectRegion(province + city + county);

		return dto;
	}

	private List<WzInfoDTO> document2WzDto(ObjectStore os, Object[] documents) {

		return this.document2WzDto(os, documents, true, true, true, false);
		// Map<String, CaseDTO> caseMap = new HashMap<String, CaseDTO>();
	/*	List<WzInfoDTO> data = new LinkedList<WzInfoDTO>();

		WzInfoDTO tempWZ = null;
		logger.info(">>>如果是图片，则直接存储为缩略图");
		for (Object p : documents) {
			tempWZ = this.document2WzDto(os,  (Document)p, true, true);// document2WzDto(os, (Document) p);
			data.add(tempWZ);
		}
		return data;*/
	}
	private List<WzInfoDTO> document2WzDto(ObjectStore os, Object[] documents, boolean hasCaseInfo, boolean hasFileLink, boolean haveThumb, boolean thumbnailByte) {

		// Map<String, CaseDTO> caseMap = new HashMap<String, CaseDTO>();
		List<WzInfoDTO> data = new LinkedList<WzInfoDTO>();

		// WzInfoDTO tempWZ = null;
		logger.info(">>>如果是图片，则直接存储为缩略图");
		for (Object p : documents) {

			// String cacheKey = "WZ_"+ ((Document)p).get_Id().toString();
		/*	tempWZ =  (WzInfoDTO) this.redisCache.get(cacheKey);
			if(null == tempWZ) {
				tempWZ = this.document2WzDto(os,  (Document)p, hasCaseInfo, hasFileLink, haveThumb, thumbnailByte);// document2WzDto(os, (Document) p);
				this.redisCache.set(cacheKey, tempWZ);
			} */
			data.add(this.document2WzDto(os,  (Document)p, hasCaseInfo, hasFileLink, haveThumb, thumbnailByte));
		}
		return data;
	}

	private List<WzInfoDTO> document2WzDto(ObjectStore os, Object[] documents, Boolean haveThumb) {

		// Map<String, CaseDTO> caseMap = new HashMap<String, CaseDTO>();
		List<WzInfoDTO> data = new LinkedList<WzInfoDTO>();

		WzInfoDTO tempWZ = null;
		logger.info(">>>如果是图片，则直接存储为缩略图");
		for (Object p : documents) {
			tempWZ = document2WzDto(os, (Document) p, haveThumb);
			data.add(tempWZ);
		}
		return data;
	}
	public WzInfoDTO document2WzDto(ObjectStore os, Document doc, Boolean haveThumb) {

	    return this.document2WzDto(os, doc, true, true, haveThumb, false);
	}
	/*public WzInfoDTO document2WzDto(ObjectStore os, Document doc) {

	   
	}*/

	/**
	 * 
	 * @param os
	 * @param doc
	 * @param hasCaseInfo 是否有case信息
	 * @param hasFileLink 是否需要有文件链接
	 * @return
	 */
	public WzInfoDTO document2WzDto(ObjectStore os, Document doc, boolean hasCaseInfo, boolean hasFileLink) {

		Map<String, CaseDTO> caseMap = new HashMap<String, CaseDTO>();

		WzInfoDTO wz = new WzInfoDTO();
		Properties properties = doc.getProperties();
		wz.setDocumentTitle(properties.getStringValue("DocumentTitle"));
		wz.setCreator(properties.getStringValue("Creator"));
		wz.setDateCreated(DateUtil.toDateTimeStr(properties.getDateTimeValue("DateCreated")));
		wz.setDateLastModified(DateUtil.toDateTimeStr(properties.getDateTimeValue("DateLastModified")));
		wz.setGuid(properties.getObjectValue("Id").toString());// 不能使用properties.getStringValue("Id")，否则抛出找不到属性的异常
		wz.setLastModifier(properties.getStringValue("LastModifier"));
		wz.setName(properties.getStringValue("Name"));
		wz.setOwner(properties.getStringValue("Owner"));
		wz.setAssociateTache(properties.getStringValue(this.extPropWZConf.getAssociateTache()));
		wz.setContent(properties.getStringValue(this.extPropWZConf.getContent()));
		wz.setPublisher(properties.isPropertyPresent(this.extPropWZConf.getPublisher())? properties.getStringValue(this.extPropWZConf.getPublisher()) : "");
		wz.setSticky(properties.isPropertyPresent("XZ_Sticky")? properties.getDateTimeValue("XZ_Sticky").getTime() : null);
		String associateProjectNO = properties.getStringValue(this.extPropWZConf.getAssociateProject());
		if(hasCaseInfo){
			CaseDTO caseDTO = caseMap.get(associateProjectNO);
			if (null == caseDTO) {
				caseDTO = (CaseDTO) this.getCaseById(os, associateProjectNO);
				if (caseDTO != null && !caseMap.containsKey(associateProjectNO)) {
					caseMap.put(associateProjectNO, caseDTO);
					wz.setAssociateProject(caseDTO);
				}
			} else {
				wz.setAssociateProject(caseDTO);
			}
		}
		// 根据微作类型
		this.getWzLocation(doc, wz);
		wz.setAssociateTask(properties.getStringValue(this.extPropWZConf.getAssociateTask()));
		wz.setUpvoteCount(properties.getInteger32Value(this.extPropWZConf.getUpvoteCount()));
		wz.setCommentCount(properties.getInteger32Value(this.extPropWZConf.getCommentCount()));
		StringList users = properties.getStringListValue(this.extPropWZConf.getAssociatePerson());
		wz.setAssociatePersons((null == users) ? null : users.toArray());
		if(hasFileLink) {
			StringList fileLinks = properties.getStringListValue(this.extPropWZConf.getAssociateFileLink());
			this.getWzFileLinks(os, wz, fileLinks, true,false);
		}
		return wz;
	}
	public WzInfoDTO document2WzDto(ObjectStore os, Document doc, boolean hasCaseInfo, boolean hasFileLink, boolean haveThumb, boolean thumbnailByte) {

		Map<String, CaseDTO> caseMap = new HashMap<String, CaseDTO>();

		WzInfoDTO wz = new WzInfoDTO();
		Properties properties = doc.getProperties();
		wz.setDocumentTitle(properties.getStringValue("DocumentTitle"));
		wz.setCreator(properties.getStringValue("Creator"));
		wz.setDateCreated(DateUtil.toDateTimeStr(properties.getDateTimeValue("DateCreated")));
		wz.setDateLastModified(DateUtil.toDateTimeStr(properties.getDateTimeValue("DateLastModified")));
		wz.setGuid(properties.getObjectValue("Id").toString());// 不能使用properties.getStringValue("Id")，否则抛出找不到属性的异常
		wz.setLastModifier(properties.getStringValue("LastModifier"));
		wz.setName(properties.getStringValue("Name"));
		wz.setOwner(properties.getStringValue("Owner"));
		wz.setAssociateTache(properties.getStringValue(this.extPropWZConf.getAssociateTache()));
		wz.setContent(properties.getStringValue(this.extPropWZConf.getContent()));
		wz.setPublisher(properties.isPropertyPresent(this.extPropWZConf.getPublisher())? properties.getStringValue(this.extPropWZConf.getPublisher()) : "");
		wz.setSticky(properties.isPropertyPresent("XZ_Sticky")? properties.getDateTimeValue("XZ_Sticky").getTime() : null);
		String associateProjectNO = properties.getStringValue(this.extPropWZConf.getAssociateProject());
		if(hasCaseInfo){
			CaseDTO caseDTO = caseMap.get(associateProjectNO);
			if (null == caseDTO) {
				caseDTO = (CaseDTO) this.getCaseById(os, associateProjectNO);
				if (caseDTO != null && !caseMap.containsKey(associateProjectNO)) {
					caseMap.put(associateProjectNO, caseDTO);
					wz.setAssociateProject(caseDTO);
				}
			} else {
				wz.setAssociateProject(caseDTO);
			}
		}
		// 根据微作类型
		this.getWzLocation(doc, wz);
		wz.setAssociateTask(properties.getStringValue(this.extPropWZConf.getAssociateTask()));
		wz.setUpvoteCount(properties.getInteger32Value(this.extPropWZConf.getUpvoteCount()));
		wz.setCommentCount(properties.getInteger32Value(this.extPropWZConf.getCommentCount()));
		StringList users = properties.getStringListValue(this.extPropWZConf.getAssociatePerson());
		wz.setAssociatePersons((null == users) ? null : users.toArray());
		if(hasFileLink) {
			StringList fileLinks = properties.getStringListValue(this.extPropWZConf.getAssociateFileLink());
			this.getWzFileLinks(os, wz, fileLinks, haveThumb, haveThumb, thumbnailByte);
		}
		return wz;
	}
	
	/**
	 * 获取微作的地理位置
	 * 
	 * @param doc
	 * @param wz
	 */
	private void getWzLocation(Document doc, WzInfoDTO wz) {

		try {
			if (!doc.getProperties().isPropertyPresent(this.extPropWZConf.getWZType())) {
				logger.warn(">>>请检查检索器是否包括此属性：" + this.extPropWZConf.getWZType());
				return;
			}
			wz.setType(doc.getProperties().getInteger32Value(this.extPropWZConf.getWZType()));
			if (1 != doc.getProperties().getInteger32Value(this.extPropWZConf.getWZType()))
				return;

			logger.info(">>>属于调研微作：" + doc.get_Name());
			if (!doc.getProperties().isPropertyPresent(this.extPropWZConf.getLocation())) {
				logger.warn(">>>请检查检索器是否包括此属性：" + this.extPropWZConf.getLocation());
				return;
			}

			String location = doc.getProperties().getStringValue(this.extPropWZConf.getLocation());
			if (!StringUtils.hasLength(location))
				return;

			wz.setLocation(location);

			JSONObject json = JSONObject.parseObject(location);
			wz.setCity(AddressUtil.getCity(json.getString("longitude"), json.getString("latitude")));
		} catch (Exception ex) {
			logger.error(">>>根据经纬度获取市区信息失败，详情：" + ex.getMessage());
		}
	}
	/**
	 * 获取微作的关联文件列表
	 * @param os
	 * @param wz
	 * @param thumbnailsAll 是否处理所有缩略图，true/false。false：表示只处理图片文件类型的缩略图
	 * @param fileLinks
	 */
/*	private void getWzFileLinks(ObjectStore os, WzInfoDTO wz, StringList fileLinks, boolean thumbnailsAll) {
		
		this.getWzFileLinks(os, wz, fileLinks, true, thumbnailsAll);
	}*/
	/**
	 * 获取微作的关联文件列表
	 * @param os
	 * @param wz
	 * @param fileLinks
	 * @param haveThumbnails 是否有缩略图
	 * @param thumbnailsAll 是否处理所有缩略图，true/false。false：表示只处理图片文件类型的缩略图
	 */
	private void getWzFileLinks(ObjectStore os, WzInfoDTO wz, StringList fileLinks, boolean haveThumbnails, boolean thumbnailsAll) {
		
		this.getWzFileLinks(os, wz, fileLinks, haveThumbnails, thumbnailsAll, false);
	}
/**
 * 
 * @param os
 * @param wz
 * @param fileLinks
 * @param haveThumbnails 是否有缩略图
 * @param thumbnailsAll 是否处理所有缩略图，true/false。false：表示只处理图片文件类型的缩略图
 * @param thumbnailByte 是否返回字节流，否则返回base64编码
 */
private void getWzFileLinks(ObjectStore os, WzInfoDTO wz, StringList fileLinks, boolean haveThumbnails, boolean thumbnailsAll, boolean thumbnailByte) {
		
		if (fileLinks != null) {
			@SuppressWarnings("rawtypes")
			Iterator fileIterator = fileLinks.iterator();

			while (fileIterator.hasNext()) {
				String docId = String.valueOf(fileIterator.next());
				Document docLink = null;
				try {
					docLink = docUtil.loadByVersionSeriesId(os, docId);// 以文档的版本系列号作为关联。(os, docId);
				} catch (Exception e) {
					logger.error(">>>警告，文档版本系列id：[{}] 不存在", docId);
					continue;
				}
				if (null == docLink)
					continue;

				DocumentDTO dto = new DocumentDTO();
				dto.setId(docLink.get_Id().toString());
				dto.setName(docLink.getProperties().getStringValue("DocumentTitle"));
				dto.setModifiedBy(docLink.get_LastModifier());
	
				dto.setModifiedOn(docLink.get_DateLastModified().getTime());
				// dto.setFolder(false);
				dto.setMajorVersion(docLink.get_MajorVersionNumber());
				dto.setMinorVersion(docLink.get_MinorVersionNumber());
				dto.setSize(docLink.get_ContentSize());
				dto.setIsCheckout(docLink.get_IsReserved());
				dto.setVersionStatus(docLink.get_VersionStatus().toString());
				// 项目扩展的文档类字段
				dto.setFileType(docLink.getProperties().isPropertyPresent(this.extPropMaterialConf.getFileType())
						? docLink.getProperties().getStringValue(this.extPropMaterialConf.getFileType()) : "");
				dto.setOrganization(docLink.getProperties().isPropertyPresent(this.extPropMaterialConf.getOrganization())
						? docLink.getProperties().getStringValue(this.extPropMaterialConf.getOrganization()) : "");
				dto.setRegion(docLink.getProperties().isPropertyPresent(this.extPropMaterialConf.getRegion())
						? docLink.getProperties().getStringValue(this.extPropMaterialConf.getRegion()) : "");
				dto.setBusiness(docLink.getProperties().isPropertyPresent(this.extPropMaterialConf.getBusiness())
						? docLink.getProperties().getStringValue(this.extPropMaterialConf.getBusiness()) : "");
				// 获取documen的content的类型。
				dto.setMimeType(docLink.get_MimeType());
				dto.setVersionSeriesId(docLink.get_VersionSeries().get_Id().toString());
				wz.addAssociateFileLink(dto);
				ThumbnailDTO thumDto = null;
				if(haveThumbnails) {
					if(thumbnailsAll) {
						thumDto = docUtil.getThumbnail(docLink, thumbnailByte);
						if(thumbnailByte && thumDto.getImgByte() != null) {
							wz.addThumbnails(thumDto);
						} else if(!StringUtils.isEmpty(thumDto.getImg())) {
							wz.addThumbnails(thumDto);
						}
					} else {
						if (MimeType.isImg(docLink.get_MimeType())) {
							thumDto = docUtil.getThumbnail(docLink, thumbnailByte);
							if(!StringUtils.isEmpty(thumDto.getImg())) {
								wz.addThumbnails(thumDto);
							}
						}
					}
				}
			}
		}
	}
	
	/**
	 * 获取案例包
	 * 
	 * @param os
	 * @param caseIdentifier 案例标识
	 * @return
	 */
	public List<DocumentDTO> getCasePackages(ObjectStore os, String caseIdentifier) {

		CaseDTO dto = this.getCaseByIdentifier(os, caseIdentifier);
		if (null == dto)
			throw new BusinessException("案例标识[{0}] 输入有误。", caseIdentifier);

		List<DocumentDTO> data = this.searchEngine.findSubFoldersAndDocumentsByFolderId(os, dto.getGuid());

		return data;
	}
	/**
	 * 根据案例id获取资源包
	 * @param os
	 * @param caseId
	 * @return
	 */
	public List<DocumentDTO> getCasePackagesById(ObjectStore os, String caseId) {

		//CaseDTO dto = this.getCaseById(os, caseId);
		/*if (null == dto)
			throw new BusinessException("案例id[{0}] 输入有误。", caseId);
*/
		List<DocumentDTO> data = this.searchEngine.findSubFoldersAndDocumentsByFolderId(os, caseId);

		return data;
	}

	/**
	 * 获取case下的所有微作
	 * 
	 * @param os
	 * @param caseIdentifier 案例标识或者案例id都可以，依据项目业务需求而存入的值
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public Pagination getWZsOfCase(ObjectStore os, String caseIdentifier, int pageNo, int pageSize) {

		return this.getWZsOfCasePage(os, new String[]{this.extPropWZConf.getAssociateProject()}, new String[]{caseIdentifier}, pageNo, pageSize);
		
	}
	/**
	 * 根据属性集合筛选，检索微作。根据创建时间倒序，使用ecm底层分页机制
	 * @param os
	 * @param properties
	 * @param values
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public Pagination getWZsOfCase(ObjectStore os, String[] properties, Object[] values, int pageNo, int pageSize) {

		return this.getWZsOfCase(os, properties, values, pageNo, pageSize, true, true, true, false);
	}
	public Pagination getWZsOfCaseThumbnailByte(ObjectStore os, String[] properties, Object[] values, int pageNo, int pageSize) {

		return this.getWZsOfCase(os, properties, values, pageNo, pageSize, true, true, true, true);
	}
	public Pagination getWZsOfCase(ObjectStore os, String[] properties, Object[] values, int pageNo, int pageSize, boolean hasCaseInfo, boolean hasFileLink, boolean haveThumb, boolean thumbnailByte) {

		Assert.notEmpty(properties);
		Assert.notEmpty(values);

		StringBuilder buf = createWZSelectQueryBuilder();
		buf.append(" WHERE 1=1 ");
		boolean multiValue = false;
		for (int i = 0; i < properties.length; i++) {

			// 分两种类型判断：字符和数字
			if (values[i] instanceof String) {
				if (!multiValue) {
					buf.append(" AND " + properties[i] + " ='");
					buf.append(values[i]);
					buf.append("'");
					multiValue = true;
					continue;
				}
				buf.append(" AND " + properties[i] + " ='");
				buf.append(values[i]);
				buf.append("'");

			} else {
				if (!multiValue) {
					buf.append(" AND " + properties[i] + " =" + values[i]);
					multiValue = true;
					continue;
				}
				buf.append(" AND " + properties[i] + " =" + values[i]);
			}
		}
		buf.append(" ORDER BY DateCreated DESC");
		SearchScope ss = new SearchScope(os);
		SearchSQL sql = new SearchSQL(buf.toString());
		// 设置为false的情况下，才能获取到总条目
		IndependentObjectSet oset = ss.fetchObjects(sql, pageSize, null, Boolean.FALSE);
		int totalCount = ((SubSetImpl) oset).getList().size();
		// 再次分页查询，设置continuable=true
		PageIterator pi = ss.fetchObjects(sql, pageSize, null, Boolean.TRUE).pageIterator();
		//pi.setPageSize(pageSize);
		int i = 0;
		while (pi.nextPage()) {
			i++;
			if (i == pageNo) {
				Object[] page = pi.getCurrentPage();
				List<WzInfoDTO> data = document2WzDto(os, page, hasCaseInfo, hasFileLink, haveThumb, thumbnailByte);
				Pagination pagination = new Pagination(pageNo, pageSize, totalCount, data);

				return pagination;
			}
		}

		return new Pagination(pageNo, pageSize, totalCount, null);
	}
	
	
	/**
	 * 
	 * @param os
	 * @param properties
	 * @param values
	 * @param pageNo
	 * @param pageSize
	 * @param haveThumb 是否需要缩略图
	 * @return
	 */
	public Pagination getWZsOfCase(ObjectStore os, String[] properties, Object[] values, int pageNo, int pageSize, Boolean haveThumb) {

		Assert.notEmpty(properties);
		Assert.notEmpty(values);

		StringBuilder buf = createWZSelectQueryBuilder();
		buf.append(" WHERE 1=1 ");
		boolean multiValue = false;
		for (int i = 0; i < properties.length; i++) {

			// 分两种类型判断：字符和数字
			if (values[i] instanceof String) {
				if (!multiValue) {
					buf.append(" AND " + properties[i] + " ='");
					buf.append(values[i]);
					buf.append("'");
					multiValue = true;
					continue;
				}
				buf.append(" AND " + properties[i] + " ='");
				buf.append(values[i]);
				buf.append("'");

			} else {
				if (!multiValue) {
					buf.append(" AND " + properties[i] + " =" + values[i]);
					multiValue = true;
					continue;
				}
				buf.append(" AND " + properties[i] + " =" + values[i]);
			}
		}
		buf.append(" ORDER BY DateCreated DESC");
		SearchScope ss = new SearchScope(os);
		SearchSQL sql = new SearchSQL(buf.toString());
		// 设置为false的情况下，才能获取到总条目
		IndependentObjectSet oset = ss.fetchObjects(sql, pageSize, null, Boolean.FALSE);
		int totalCount = ((SubSetImpl) oset).getList().size();
		// 再次分页查询，设置continuable=true
		PageIterator pi = ss.fetchObjects(sql, pageSize, null, Boolean.TRUE).pageIterator();
		//pi.setPageSize(pageSize);
		int i = 0;
		while (pi.nextPage()) {
			i++;
			if (i == pageNo) {
				Object[] page = pi.getCurrentPage();
				List<WzInfoDTO> data = document2WzDto(os, page, haveThumb);
				Pagination pagination = new Pagination(pageNo, pageSize, totalCount, data);

				return pagination;
			}
		}

		return new Pagination(pageNo, pageSize, totalCount, null);
	}
	/**
	 * 根据属性集合筛选，检索微作。根据置顶属性和创建时间倒序，因为添加置顶属性排序，导致ecm底层分页失效，所以自定义分页机制
	 * @param os
	 * @param properties
	 * @param values
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public Pagination getWZsOfCasePage(ObjectStore os, String[] properties, Object[] values, int pageNo, int pageSize) {

		Assert.notEmpty(properties);
		Assert.notEmpty(values);

		StringBuilder buf = createWZSelectQueryBuilder();
		buf.append(" WHERE 1=1 ");
		boolean multiValue = false;
		for (int i = 0; i < properties.length; i++) {

			// 分两种类型判断：字符和数字
			if (values[i] instanceof String) {
				if (!multiValue) {
					buf.append(" AND " + properties[i] + " ='");
					buf.append(values[i]);
					buf.append("'");
					multiValue = true;
					continue;
				}
				buf.append(" AND " + properties[i] + " ='");
				buf.append(values[i]);
				buf.append("'");

			} else {
				if (!multiValue) {
					buf.append(" AND " + properties[i] + " =" + values[i]);
					multiValue = true;
					continue;
				}
				buf.append(" AND " + properties[i] + " =" + values[i]);
			}
		}
		buf.append(" ORDER BY XZ_Sticky DESC, DateCreated DESC");
		SearchScope ss = new SearchScope(os);
		SearchSQL sql = new SearchSQL(buf.toString());
		// 设置为false的情况下，才能获取到总条目
		IndependentObjectSet oset = ss.fetchObjects(sql, pageSize, null, Boolean.FALSE);
		List<?> queryWzList = ((SubSetImpl) oset).getList();
		int totalCount = queryWzList.size();
		// 再次分页查询，设置continuable=true
		// PageIterator pi = ss.fetchObjects(sql, pageSize, null, Boolean.TRUE).pageIterator();
		//pi.setPageSize(pageSize);
		int start = (pageNo - 1) * pageSize;
		int end = pageNo * pageSize;

		if( end > totalCount) {
			end = totalCount;
		}
		List<WzInfoDTO> wzDTOs = new ArrayList<WzInfoDTO>();
		WzInfoDTO temp = null;
		for(int i = start; i< end; i++) {
			temp =  this.document2WzDto(os,  (Document)queryWzList.get(i), true, true);//this.document2WzDto(os, (Document)queryWzList.get(i));
			if(null == temp) continue;
			
			wzDTOs.add(temp);
		}
		return new Pagination(pageNo, pageSize, totalCount, wzDTOs);
	}

	/**
	 * 根据微作id获取微作实体
	 * @param os
	 * @param wzId
	 * @return
	 */
    public Document getWz(ObjectStore os, String wzId) {
		
		/*StringBuilder buf = createWZSelectQueryBuilder();
		buf.append(" WHERE Id = '"+wzId+"'");
		
		SearchScope ss = new SearchScope(os);
		SearchSQL sql = new SearchSQL(buf.toString());
		IndependentObjectSet oset = ss.fetchObjects(sql, null,null, Boolean.FALSE);
		Iterator iterator = oset.iterator();
		Document wz = null;
		while (iterator.hasNext()) {
			wz = (Document) iterator.next();
			break;
		}
	    return wz;*/
    	List<Document> wzList = this.getWzs(os, new String[]{wzId});
    	if(null == wzList || wzList.isEmpty()){
    		return null;
    	}
    	return wzList.get(0);
	}
    /**
     * 获取微作关联的文件列表
     * @param os
     * @param wzId
     * @return
     */
    public StringList getWzRefFiles(ObjectStore os, String wzId) {
    	
    	List<Document> wzList = this.getWzs(os, new String[]{wzId});
    	if(null == wzList || wzList.isEmpty()){
    		return null;
    	}
    	Document doc =  wzList.get(0);
    	return doc.getProperties().getStringListValue(this.extPropWZConf.getAssociateFileLink());
    }
    public List<Document> getWzs(ObjectStore os, String[] wzIds) {
		
    	Assert.notEmpty(wzIds);
		
		StringBuilder buf = createWZSelectQueryBuilder();
		boolean flag = true;
		buf.append("  WHERE Id in (");
		for(String wzId : wzIds) {
			if(flag) {
				buf.append("'"+wzId+"'");
				flag = false;
				continue;
			}
			buf.append(",'"+wzId+"'");
		}
		buf.append(")");

		SearchScope ss = new SearchScope(os);
		SearchSQL sql = new SearchSQL(buf.toString());
		IndependentObjectSet oset = ss.fetchObjects(sql, null,null, Boolean.FALSE);
		Iterator iterator = oset.iterator();
		List<Document> wzList = new ArrayList<Document>();
		Document tempWz = null;
		while (iterator.hasNext()) {
			tempWz = (Document) iterator.next();
			if(null == tempWz) {
				continue;
			}
			wzList.add(tempWz);
		}
	    return wzList;
	}
    /**
     * 根据微作id获取微作DTO
     * @param os
     * @param wzId
     * @return
     */
    public WzInfoDTO getWzInfoDTO(ObjectStore os, String wzId) {
		
		Document wz = getWz(os, wzId);
		
		if(wz != null){
			return this.document2WzDto(os, wz, true, true);
		}
		return null;
	}
    /**
     * 根据微作id获取微作DTO，缩略图返回字节流
     * @param os
     * @param wzId
     * @return
     */
    public WzInfoDTO getWzInfoDTOThumbnailByte(ObjectStore os, String wzId) {
		
  		Document wz = getWz(os, wzId);
  		
  		if(wz != null){
  			return this.document2WzDto(os, wz, true, true, true, true);
  		}
  		return null;
  	}
    /**
     * 
     * @param os
     * @param wzId
     * @param hasFileLink 是否获取文件链接
     * @return
     */
    public WzInfoDTO getWzInfoDTO(ObjectStore os, String wzId, boolean hasFileLink) {
		
		/*Document wz = getWz(os, wzId);
		
		if(wz != null){
			return this.document2WzDto(os, wz, hasFileLink);
		}
		return null;*/
    	List<WzInfoDTO> wzDTOs = this.getWzInfosDTO(os, new String[]{wzId}, hasFileLink);
    	if(null == wzDTOs || wzDTOs.isEmpty())
    		return null;
    	return wzDTOs.get(0);
	}
    /**
     * 批量获取
     * @param os
     * @param wzIds
     * @param hasFileLink
     * @return
     */
    public List<WzInfoDTO> getWzInfosDTO(ObjectStore os, String[] wzIds, boolean hasFileLink) {
		
		List<Document> wzDocs = getWzs(os, wzIds);
		List<WzInfoDTO> wzDTOs = new ArrayList<>(wzDocs.size());
		if(wzDocs != null && !wzDocs.isEmpty()){
			WzInfoDTO tempDTO = null;
			for(Document doc : wzDocs) {
				tempDTO = this.document2WzDto(os, doc, true, hasFileLink);
				if(null == tempDTO)
					continue;
				wzDTOs.add(tempDTO);
			}
		}
		return wzDTOs;
	}
    /**
     * 根据id删除微作
     * @param os
     * @param resId
     */
	public void deleteWzById(ObjectStore os, String resId) {

		Document wz = this.getWz(os, resId);
		if(null == wz) {
			return;
		}
		wz.delete();
		wz.save(RefreshMode.REFRESH);
	}
}
