package com.dist.bdf.manager.ecm.utils;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.dist.bdf.base.page.Pagination;
import com.dist.bdf.base.utils.StringUtil;
import com.dist.bdf.base.utils.sizeconverter.ByteSizeUnit;
import com.dist.bdf.base.utils.sizeconverter.ByteSizeValue;
import com.dist.bdf.common.conf.ecm.ECMConf;
import com.dist.bdf.common.conf.ecm.ExtPropertyCaseConf;
import com.dist.bdf.common.conf.ecm.ExtPropertyConf;
import com.dist.bdf.common.conf.ecm.ExtPropertyMaterialConf;
import com.dist.bdf.manager.ecm.define.DataType;
import com.dist.bdf.manager.ecm.define.SimplePropertyFilter;
import com.dist.bdf.model.dto.dcm.DocumentDTO;
import com.dist.bdf.model.dto.system.MaterialSummaryDTO;
import com.filenet.api.collection.DocumentSet;
import com.filenet.api.collection.IndependentObjectSet;
import com.filenet.api.collection.PageIterator;
import com.filenet.api.collection.RepositoryRowSet;
import com.filenet.api.constants.ClassNames;
import com.filenet.api.constants.JoinComparison;
import com.filenet.api.constants.JoinOperator;
import com.filenet.api.core.Document;
import com.filenet.api.core.Factory;
import com.filenet.api.core.Folder;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.query.SearchSQL;
import com.filenet.api.query.SearchScope;
import com.filenet.api.util.Id;
import com.filenet.apiimpl.core.SubSetImpl;

/**
 * 
 * 检索
 * 
 * @author weifj
 * @version 1.0，2016/04/20，weifj，创建检索工具类
 * @version 1.1,2016/04/21，weifj，添加分页查询
 * 
 */
@Component
public class SearchEngine {

	private static Logger logger = LoggerFactory.getLogger(SearchEngine.class);
	
	@Autowired
	private ECMConf ecmConf;
	@Autowired
	private FolderUtil folderUtil;
	@Autowired
	private DocumentUtil docUtil;
	@Autowired
	private ExtPropertyMaterialConf extPropMaterialConf;
	@Autowired
	@Qualifier("ExtPropertyConf")
	private ExtPropertyConf extPropConf;
	@Autowired
	@Qualifier("ExtPropertyCaseConf")
	private ExtPropertyCaseConf extPropCaseConf;

	/**
	 * 全文检索
	 * 
	 * @param os
	 * @param symbolicClassName
	 * @param includeClasses
	 * @param folderName，指定文件夹路径，如果全局，则传入null
	 * @param version
	 * @param fileTypefilter
	 * @param nameCondition
	 * @param keyword
	 * @param searchable，是否可检索。如果可检索，说明不属于个人文档。目前个人文档是不允许检索的。
	 * @return
	 */
	public <T> T fullTextSearch(ObjectStore os, String symbolicClassName, boolean includeClasses, String folderName,
			Integer version, String[] fileTypefilter, String nameCondition, String keyword, boolean searchable) {

		return fullTextSearch(os, symbolicClassName, includeClasses, folderName, version, fileTypefilter, nameCondition,
				keyword, 1, ecmConf.getSearchMaxRecords(), searchable);

	}

	/*
	 * private SearchSQL createSummaryDataSearchSQL(ObjectStore os, String
	 * symbolicClassName, boolean includeClasses, String folderName, Integer
	 * version, String[] fileTypefilter, String nameCondition,String keyword,
	 * int pageNo, int pageSize, boolean searchable) {
	 * 
	 * SearchSQL sqlObject = new SearchSQL();
	 * sqlObject.setMaxRecords(ecmConf.getSearchMaxRecords());
	 * 
	 * // 如果是文件夹，那么没有版本和文件类型过滤,只有按照名称进行搜索了 if
	 * ("Folder".equals(symbolicClassName)) {
	 * 
	 * sqlObject.setFromClauseInitialValue("Folder", "f", includeClasses);
	 * sqlObject.setSelectList("f.*"); if(nameCondition != null){
	 * sqlObject.setWhereClause("f.FolderName like '%" + nameCondition + "%'");
	 * }
	 * 
	 * if(folderName != null){ sqlObject.setFolderRestriction("o", folderName);
	 * }
	 * 
	 * return sqlObject;
	 * 
	 * } else {
	 * 
	 * String whereCause = "";
	 * sqlObject.setFromClauseInitialValue(symbolicClassName, "o",
	 * includeClasses); sqlObject.setSelectList("o.*"); if(folderName != null){
	 * sqlObject.setFolderRestriction("o", folderName); } //版本 if(nameCondition
	 * != null){ whereCause +=" o.DocumentTitle like '%" + nameCondition + "%' "
	 * ; } if(version !=null){ whereCause +=
	 * (StringUtil.isNullOrEmpty(whereCause) ? "":" and ")+
	 * " o.VersionStatus = "+version+" "; } // 是否可被检索 whereCause +=
	 * (StringUtil.isNullOrEmpty(whereCause) ? "":" and ")+" "
	 * +this.extPropMaterialConf.getSearchable()+"="+((searchable)?
	 * "True":"False"); // 关联社交统计数据 sqlObject.setFromClauseAdditionalJoin(
	 * JoinOperator.INNER, "DistSummaryData ", "dsd", "o.Id",
	 * JoinComparison.EQUAL, "dsd.ClbDocumentId", includeClasses); if(keyword
	 * !=null){ // 全文检索 sqlObject.setContainsRestriction(symbolicClassName,
	 * keyword); }
	 * 
	 * // 设置排序 sqlObject.setOrderByClause(" ClbDownloadCount DESC, "
	 * +this.extPropWZConf.getFavorites()+" DESC"); if(fileTypefilter !=null){
	 * String mimetypes = "("; for(int i = 0 ; i < fileTypefilter.length;i++){
	 * mimetypes +="'"+fileTypefilter[i]+"'"; if(i < fileTypefilter.length-1){
	 * mimetypes += ","; } } mimetypes += ")"; whereCause +=
	 * ("".equals(whereCause) ? "":" and ")+ " o.MimeType in "+mimetypes; }
	 * 
	 * if(!StringUtil.isNullOrEmpty(whereCause)){
	 * 
	 * sqlObject.setWhereClause(whereCause); }
	 * 
	 * return sqlObject; }
	 * 
	 * }
	 */
	/**
	 * 创建查询SQL的对象
	 * 
	 * @param os
	 * @param symbolicClassName
	 * @param includeSubClasses
	 * @param folderName
	 * @param version
	 * @param fileTypefilter
	 * @param nameCondition
	 * @param keyword
	 * @param pageNo
	 * @param pageSize
	 * @param searchable
	 * @return
	 */
	private SearchSQL createFullTextSearchSQLObject(ObjectStore os, String symbolicClassName, boolean includeSubClasses,
			String folderName, Integer version, String[] fileTypefilter,
			String nameCondition, String keyword,boolean searchable) {

		SearchSQL sqlObject = new SearchSQL();
		sqlObject.setMaxRecords(ecmConf.getSearchMaxRecords());

		// 如果是文件夹，那么没有版本和文件类型过滤,只有按照名称进行搜索了
	/*	if ("Folder".equals(symbolicClassName)) {

			sqlObject.setFromClauseInitialValue("Folder", "f", includeClasses);
			sqlObject.setSelectList("f.*");
			if (nameCondition != null) {
				sqlObject.setWhereClause("f.FolderName like '%" + nameCondition + "%'");
			}

			if (folderName != null) {
				sqlObject.setFolderRestriction("o", folderName);
			}

			return sqlObject;

		} else {
		}*/


		String whereCause = "";
		sqlObject.setFromClauseInitialValue(symbolicClassName, "o", includeSubClasses);
		sqlObject.setSelectList("o.*");
		if (folderName != null) {
			sqlObject.setFolderRestriction("o", folderName);
		}
		// 版本
		if (nameCondition != null) {
			whereCause += " o.DocumentTitle like '%" + nameCondition + "%' ";
		}
		if (version != null) {
			whereCause += (StringUtil.isNullOrEmpty(whereCause) ? "" : " and ") + " o.VersionStatus = " + version
					+ " ";
		}
		// 是否可被检索
		whereCause += (StringUtil.isNullOrEmpty(whereCause) ? "" : " and ") + " "
				+ this.extPropMaterialConf.getSearchable() + "=" + ((searchable) ? "True" : "False");
		if (!StringUtils.isEmpty(keyword)) {

			sqlObject.setFromClauseAdditionalJoin(JoinOperator.INNER, "ContentSearch ", "cs", "o.This",
					JoinComparison.EQUAL, "cs.QueriedObject", includeSubClasses);
			whereCause += ("".equals(whereCause) ? "" : " and ") + " CONTAINS(o.*, '*" + keyword + "*')";

		}

		if (fileTypefilter != null) {
			String mimetypes = "(";
			for (int i = 0; i < fileTypefilter.length; i++) {
				mimetypes += "'" + fileTypefilter[i] + "'";
				if (i < fileTypefilter.length - 1) {
					mimetypes += ",";
				}
			}
			mimetypes += ")";
			whereCause += ("".equals(whereCause) ? "" : " and ") + " o.MimeType in " + mimetypes;
		}

		if (!StringUtil.isNullOrEmpty(whereCause)) {
			sqlObject.setWhereClause(whereCause);
		}

		return sqlObject;

	}
	/**
	 * 构建全文检索SQL，并通过rank排序
	 * @param os
	 * @param symbolicClassName
	 * @param includeClasses
	 * @param keyword
	 * @param searchable
	 * @return
	 */
	private SearchSQL createFullTextSearchSQLObject(ObjectStore os, 
			String symbolicClassName, 
			boolean includeClasses,
			String keyword,
			boolean searchable) {

		SearchSQL sqlObject = new SearchSQL();
		sqlObject.setMaxRecords(ecmConf.getSearchMaxRecords());

		String whereCause = "";
		sqlObject.setFromClauseInitialValue(symbolicClassName, "o", includeClasses);
		sqlObject.setSelectList("o.*, cs.Rank"); // TODO 此处需要优化，只查询有效列

		// 是否可被检索
		whereCause += (StringUtil.isNullOrEmpty(whereCause) ? "" : " and ") + " "
				+ this.extPropMaterialConf.getSearchable() + "=" + ((searchable) ? "True" : "False");
		if (!StringUtil.isNullOrEmpty(keyword)) {

			sqlObject.setFromClauseAdditionalJoin(JoinOperator.INNER, "ContentSearch ", "cs", "o.This",
					JoinComparison.EQUAL, "cs.QueriedObject", includeClasses);
			whereCause += ("".equals(whereCause) ? "" : " and ") + " CONTAINS(o.*, '*" + keyword + "*')";

		}

		if (!StringUtil.isNullOrEmpty(whereCause)) {
			sqlObject.setWhereClause(whereCause);
		}

		sqlObject.setOrderByClause("o.DateLastModified DESC,cs.Rank DESC "); // 最后修改时间和匹配度
		return sqlObject;
	}

	/**
	 * 全文检索
	 * 
	 * @param os
	 * @param symbolicClassName
	 * @param includeClasses
	 * @param folderName
	 * @param version
	 * @param fileTypefilter
	 * @param nameCondition
	 * @param keyword
	 * @param pageNo
	 * @param pageSize
	 * @param searchable，是否可检索。如果可检索，说明不属于个人文档。目前个人文档是不允许检索的。
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> T fullTextSearch(ObjectStore os, String symbolicClassName, boolean includeClasses, String folderName,
			Integer version, String[] fileTypefilter, String nameCondition, String keyword, int pageNo, int pageSize,
			boolean searchable) {

		SearchScope searchScope = new SearchScope(os);
		SearchSQL sqlObject = this.createFullTextSearchSQLObject(os, symbolicClassName, includeClasses, folderName, version,
				fileTypefilter, nameCondition, keyword, searchable);
		// new SearchSQL();
		// sqlObject.setMaxRecords(ecmConf.getSearchMaxRecords());
		Pagination pagination = null;

		// 如果是文件夹，那么没有版本和文件类型过滤,只有按照名称进行搜索了
		if ("Folder".equals(symbolicClassName)) {

			/*
			 * sqlObject.setFromClauseInitialValue("Folder", "f",
			 * includeClasses); sqlObject.setSelectList("f.*");
			 * 
			 * sqlObject.setWhereClause("f.FolderName like '%" + nameCondition +
			 * "%'");
			 * 
			 * if(folderName != null){ sqlObject.setFolderRestriction("o",
			 * folderName); }
			 */

			// FolderSet folderSet = (FolderSet)
			// searchScope.fetchObjects(sqlObject, pageSize, null, true);
			PageIterator pi = searchScope.fetchObjects(sqlObject, pageSize, null, true).pageIterator();

			int i = 0;
			while (pi.nextPage()) {
				i++;
				if (i == pageNo) {
					Object[] page = pi.getCurrentPage();
					List<DocumentDTO> data = folderUtil.folder2dto(page);
					pagination = new Pagination(pageNo, pageSize, pi.getTotalCount(), data);
					return (T) pagination;
				}
			}

		} else {/*
				 * 
				 * String whereCause = "";
				 * sqlObject.setFromClauseInitialValue(symbolicClassName, "o",
				 * includeClasses); sqlObject.setSelectList("o.*");
				 * if(folderName != null){ sqlObject.setFolderRestriction("o",
				 * folderName); } //版本 if(nameCondition != null){ whereCause +=
				 * " o.DocumentTitle like '%" + nameCondition + "%' "; }
				 * if(version !=null){ whereCause +=
				 * (StringUtil.isNullOrEmpty(whereCause) ? "":" and ")+
				 * " o.VersionStatus = "+version+" "; } // 是否可被检索 whereCause +=
				 * (StringUtil.isNullOrEmpty(whereCause) ? "":" and ")+
				 * " XZ_Searchable="+((searchable)? "True":"False"); if(keyword
				 * !=null){ sqlObject.setFromClauseAdditionalJoin(
				 * JoinOperator.INNER, "ContentSearch ", "cs", "o.This",
				 * JoinComparison.EQUAL, "cs.QueriedObject", includeClasses);
				 * whereCause += ("".equals(whereCause) ? "":" and ")+
				 * " CONTAINS(o.*, '*"+keyword+"*')";
				 * 
				 * }
				 * 
				 * if(fileTypefilter !=null){ String mimetypes = "("; for(int i
				 * = 0 ; i < fileTypefilter.length;i++){ mimetypes
				 * +="'"+fileTypefilter[i]+"'"; if(i < fileTypefilter.length-1){
				 * mimetypes += ","; } } mimetypes += ")"; whereCause +=
				 * ("".equals(whereCause) ? "":" and ")+ " o.MimeType in "
				 * +mimetypes; }
				 * 
				 * if(!StringUtil.isNullOrEmpty(whereCause)){
				 * sqlObject.setWhereClause(whereCause); }
				 */
			// Set the (Boolean) value for the continuable parameter. This
			// indicates
			// whether to iterate requests for subsequent pages of result data
			// when
			// the end of the
			// first page of results is reached. If null or false, only a single
			// page of results is
			// returned.
			// Boolean continuable = false;
			// 设置为false的情况下，才能获取到总条目
			IndependentObjectSet oset = searchScope.fetchObjects(sqlObject, pageSize, null, false);
			// 再次分页查询，设置continuable=true
			PageIterator pi = searchScope.fetchObjects(sqlObject, pageSize, null, true).pageIterator();
			int totalCount = ((SubSetImpl) oset).getList().size();

			int i = 0;
			while (pi.nextPage()) {
				i++;
				if (i == pageNo) {
					Object[] page = pi.getCurrentPage();
					List<DocumentDTO> data = docUtil.document2dto(page);
					pagination = new Pagination(pageNo, pageSize, totalCount, data);
					return (T) pagination;
				}
			}

		}
		pagination = new Pagination(pageNo, pageSize, 0, null);
		return (T) pagination;
	}

	/**
	 * 通用分页检索
	 * 
	 * @param os
	 * @param sqlString
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public <T> T pageSearch(ObjectStore os, String sqlString, int pageNo, int pageSize) {

		SearchSQL sqlObject = new SearchSQL(sqlString);
		return this.pageSearch(os, sqlObject, pageNo, pageSize);
		
	}

	public <T> T pageSearch(ObjectStore os, SearchSQL sqlObject, int pageNo, int pageSize) {

		SearchScope searchScope = new SearchScope(os);
		
		// sqlObject.setMaxRecords(ecmConf.getSearchMaxRecords());
		Pagination pagination = null;

		// 设置为false的情况下，才能获取到总条目
		IndependentObjectSet oset = searchScope.fetchObjects(sqlObject, pageSize, null, false);
		// 再次分页查询，设置continuable=true
		PageIterator pi = searchScope.fetchObjects(sqlObject, pageSize, null, true).pageIterator();
		int totalCount = ((SubSetImpl) oset).getList().size();

		int i = 0;
		while (pi.nextPage()) {
			i++;
			if (i == pageNo) {
				Object[] page = pi.getCurrentPage();
				List<DocumentDTO> data = docUtil.document2dto(page);
				pagination = new Pagination(pageNo, pageSize, totalCount, data);
				return (T) pagination;
			}
		}
		pagination = new Pagination(pageNo, pageSize, 0, null);
		return (T) pagination;
	}
	/**
	 * 
	 * @param os
	 * @param sqlObject
	 * @param symbolicClassName
	 * @param pageNo
	 * @param pageSize
	 * @param needThumbnails 是否需要缩略图
	 * @param thumbnailByte 缩略图是否返回字节流
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> T pageSearch(ObjectStore os, SearchSQL sqlObject, String symbolicClassName, int pageNo, int pageSize, boolean needThumbnails, boolean thumbnailByte) {

		SearchScope searchScope = new SearchScope(os);
		
		// sqlObject.setMaxRecords(ecmConf.getSearchMaxRecords());
		Pagination pagination = null;

		// 设置为false的情况下，才能获取到总条目
		IndependentObjectSet oset = searchScope.fetchObjects(sqlObject, pageSize, null, false);
		// 再次分页查询，设置continuable=true
		PageIterator pi = searchScope.fetchObjects(sqlObject, pageSize, null, true).pageIterator();
		int totalCount = ((SubSetImpl) oset).getList().size();

		int i = 0;
		while (pi.nextPage()) {
			i++;
			if (i == pageNo) {
				Object[] page = pi.getCurrentPage();
				List<DocumentDTO> data = null;
				if(symbolicClassName.equalsIgnoreCase(ClassNames.DOCUMENT) || symbolicClassName.equalsIgnoreCase(this.ecmConf.getDefaultDocumentClass())){
					
					data = this.docUtil.document2dto(page, needThumbnails, thumbnailByte);
					
				}else if(symbolicClassName.equalsIgnoreCase(ClassNames.FOLDER)){
					
					data = this.folderUtil.folder2dto(page);
				}
				
				pagination = new Pagination(pageNo, pageSize, totalCount, data);
				return (T) pagination;
			}
		}
		pagination = new Pagination(pageNo, pageSize, 0, null);
		return (T) pagination;
	}
	/**
	 * 当返回的列来自于不同的类，则需要以行为单位进行获取
	 * @param os
	 * @param sqlObject
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> T pageSearchRows(ObjectStore os, SearchSQL sqlObject, int pageNo, int pageSize) {

		SearchScope searchScope = new SearchScope(os);
	
		Pagination pagination = null;
		int totalCount = 0;
		
		try{
			// 设置为false的情况下，才能获取到总条目
			RepositoryRowSet rows = searchScope.fetchRows(sqlObject, null, null, false);	
			if(null == rows){
				logger.info(">>>没有检索到任何数据......");
				pagination = new Pagination(pageNo, pageSize, 0, null);
				return (T) pagination;
			}
		
			PageIterator global = rows.pageIterator();
			if(global.nextPage()){
				totalCount = global.getCurrentPage().length;
			}
		}catch(Exception ex){
			ex.printStackTrace();
			logger.error(">>>查询条目太多，超出最大10000，设置totalCount=10000。");
			totalCount = 10000;
		}
	
		// 再次分页查询，设置continuable=true
		PageIterator pi = searchScope.fetchRows(sqlObject, pageSize, null, true).pageIterator();
		int i = 0;
		while (pi.nextPage()) {
			i++;
			if (i == pageNo) {
				Object[] page = pi.getCurrentPage();
				List<DocumentDTO> data = docUtil.repositoryRow2dto(page);
				pagination = new Pagination(pageNo, pageSize, totalCount, data);
				return (T) pagination;
			}
		}
		pagination = new Pagination(pageNo, pageSize, 0, null);
		return (T) pagination;
	}
	
	
	/**
	 * 根据某个属性的关键字进行模糊查询，默认使用是否可检索字段【??_Searchable】
	 * 
	 * @param os
	 * @param property
	 * @param keyword
	 * @param searchable，是否可检索。如果可检索，说明不属于个人文档。目前个人文档是不允许检索的。
	 * @return
	 */
	public List<DocumentDTO> searchWithProperty(ObjectStore os, String symbolicClassName, String property,
			String keyword, boolean searchable) {

		Assert.notNull(property);

		return searchWithProperty(os, symbolicClassName, property, keyword, 1, ecmConf.getSearchMaxRecords(),
				searchable);

	}

	/**
	 * 根据一个属性的多个值，获取总条目
	 * 
	 * @param os
	 * @param symbolicClassName
	 * @param property
	 * @param values
	 * @return
	 */
	public int getTotalCount(ObjectStore os, String symbolicClassName, String property, String[] values) {

		// Create a SearchSQL instance and specify the SQL statement (using the
		// helper methods).
		SearchSQL sqlObject = new SearchSQL();
		sqlObject.setSelectList("d.*");
		// sqlObject.setMaxRecords(ecmConf.getSearchMaxRecords());
		sqlObject.setFromClauseInitialValue(symbolicClassName, "d", false);
		StringBuilder whereCauseBuf = new StringBuilder();
		boolean flag = true;
		if(!StringUtils.isEmpty(property) && values!= null && values.length > 0 ) {
			whereCauseBuf.append("d." + property);
			whereCauseBuf.append(" IN (");
			for (String val : values) {
				if (flag) {
					whereCauseBuf.append("'" + val + "'");
					flag = false;
					continue;
				}
				whereCauseBuf.append(" , ");
				whereCauseBuf.append("'" + val + "'");
			}
			whereCauseBuf.append(")");
		}
		sqlObject.setWhereClause(whereCauseBuf.toString());
		
		// Create a SearchScope instance. (Assumes you have the object store
		// object.)
		SearchScope search = new SearchScope(os);

		// Set the (Boolean) value for the continuable parameter. This indicates
		// whether to iterate requests for subsequent pages of result data when
		// the end of the
		// first page of results is reached. If null or false, only a single
		// page of results is
		// returned.
		// Boolean continuable = true;
		// 设置为false的情况下，才能获取到总条目
		IndependentObjectSet oset = search.fetchObjects(sqlObject, null, null, false);

		int totalCount = ((SubSetImpl) oset).getList().size();

		return totalCount;
	}
	/**
	 * 根据复杂属性和时间段进行统计个数
	 * @param os
	 * @param symbolicClassName
	 * @param propertiesValues
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	public int getTotalCount(ObjectStore os, String symbolicClassName, Map<String, Object[]> propertiesValues, Date beginTime, Date endTime) {

		// Create a SearchSQL instance and specify the SQL statement (using the helper methods).
		SearchSQL sqlObject = new SearchSQL();
		sqlObject.setSelectList("d.*");
		// sqlObject.setMaxRecords(ecmConf.getSearchMaxRecords());
		sqlObject.setFromClauseInitialValue(symbolicClassName, "d", false);
		StringBuilder whereCauseBuf = new StringBuilder();
		whereCauseBuf.append(" 1= 1");
		boolean flag = true;
		if(propertiesValues != null && !propertiesValues.isEmpty()) {
			for(Entry<String, Object[]> entry : propertiesValues.entrySet()) {
				whereCauseBuf.append(" and d." + entry.getKey());
				whereCauseBuf.append(" IN (");
				if(entry.getValue() instanceof String[]) {
					for (Object val : entry.getValue()) {
						if (flag) {
							whereCauseBuf.append("'" + val + "'");
							flag = false;
							continue;
						}
						whereCauseBuf.append(" , ");
						whereCauseBuf.append("'" + val + "'");
					}
				} else if(entry.getValue() instanceof Integer[]) {
					for (Object val : entry.getValue()) {
						if (flag) {
							whereCauseBuf.append(val);
							flag = false;
							continue;
						}
						whereCauseBuf.append(" , ");
						whereCauseBuf.append(val);
					}
				}
				whereCauseBuf.append(")");
			}
		}
	    Calendar cal = Calendar.getInstance();
	    cal.setTime(beginTime);
		whereCauseBuf.append(" and DateCreated >= " 
	    + cal.get(Calendar.YEAR) 
	    + cal.get(Calendar.MONTH) 
	    + cal.get(Calendar.DAY_OF_MONTH) 
	    + "T"
	    + cal.get(Calendar.HOUR_OF_DAY) 
	    + cal.get(Calendar.MINUTE)
	    + cal.get(Calendar.SECOND)
	    + "Z");
		cal.setTime(endTime);
		whereCauseBuf.append(" and DateCreated <= " 
			    + cal.get(Calendar.YEAR) 
			    + cal.get(Calendar.MONTH) 
			    + cal.get(Calendar.DAY_OF_MONTH) 
			    + "T"
			    + cal.get(Calendar.HOUR_OF_DAY) 
			    + cal.get(Calendar.MINUTE)
			    + cal.get(Calendar.SECOND)
			    + "Z");
	
		sqlObject.setWhereClause(whereCauseBuf.toString());
		
		// Create a SearchScope instance. (Assumes you have the object store
		// object.)
		SearchScope search = new SearchScope(os);

		// Set the (Boolean) value for the continuable parameter. This indicates
		// whether to iterate requests for subsequent pages of result data when
		// the end of the
		// first page of results is reached. If null or false, only a single
		// page of results is
		// returned.
		// Boolean continuable = true;
		// 设置为false的情况下，才能获取到总条目
		IndependentObjectSet oset = search.fetchObjects(sqlObject, null, null, false);

		int totalCount = ((SubSetImpl) oset).getList().size();

		return totalCount;
	}
	/**
	 * ce查询对时间的格式化
	 * 格式：WHERE [DateCreated] >= 20170912T190000Z AND [DateCreated] <= 20170928T194500Z
	 * @param date
	 * @return
	 */
	 private String dateFormat(Date date){  
	        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");  
	        String dateStr = sdf.format(date);  
	        sdf = new SimpleDateFormat("HHmmss");  
	        dateStr = dateStr + "T" + sdf.format(date) + "Z";  
	        return dateStr;  
	    }  
	/**
	 * 获取资料汇总信息
	 * @param os
	 * @param propertiesValues
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	public MaterialSummaryDTO getMaterialSummaryDTO(ObjectStore os, Map<String, Object[]> propertiesValues,
			Date beginTime, Date endTime) {

		// Create a SearchSQL instance and specify the SQL statement (using the
		// helper methods).
		SearchSQL sqlObject = new SearchSQL();
		sqlObject.setSelectList("d.*");
		// sqlObject.setMaxRecords(ecmConf.getSearchMaxRecords());
		sqlObject.setFromClauseInitialValue(this.ecmConf.getDefaultDocumentClass(), "d", false);
		StringBuilder whereCauseBuf = new StringBuilder();
		whereCauseBuf.append(" 1= 1");
		boolean flag = true;
		if (propertiesValues != null && !propertiesValues.isEmpty()) {
			for (Entry<String, Object[]> entry : propertiesValues.entrySet()) {
				whereCauseBuf.append(" and d." + entry.getKey());
				whereCauseBuf.append(" IN (");
				if (entry.getValue() instanceof String[]) {
					for (Object val : entry.getValue()) {
						if (flag) {
							whereCauseBuf.append("'" + val + "'");
							flag = false;
							continue;
						}
						whereCauseBuf.append(" , ");
						whereCauseBuf.append("'" + val + "'");
					}
				} else if (entry.getValue() instanceof Integer[]) {
					for (Object val : entry.getValue()) {
						if (flag) {
							whereCauseBuf.append(val);
							flag = false;
							continue;
						}
						whereCauseBuf.append(" , ");
						whereCauseBuf.append(val);
					}
				}
				whereCauseBuf.append(")");
			}
		}
		whereCauseBuf.append(" and DateCreated >= " +dateFormat(beginTime));
		whereCauseBuf.append(" and DateCreated <= " + dateFormat(endTime));

		sqlObject.setWhereClause(whereCauseBuf.toString());

		// Create a SearchScope instance. (Assumes you have the object store
		// object.)
		SearchScope search = new SearchScope(os);

		// Set the (Boolean) value for the continuable parameter. This indicates
		// whether to iterate requests for subsequent pages of result data when
		// the end of the
		// first page of results is reached. If null or false, only a single
		// page of results is
		// returned.
		// Boolean continuable = true;
		// 设置为false的情况下，才能获取到总条目
		IndependentObjectSet oset = search.fetchObjects(sqlObject, null, null, false);

		MaterialSummaryDTO dto = new MaterialSummaryDTO();
		List<?> datas = ((SubSetImpl) oset).getList();
		if (datas != null && !datas.isEmpty()) {
			dto.setContentElementCount(Double.valueOf(datas.size()));
			Document doc = null;
			// doc的contentSize属性以字节（bite）为单位
			for (int i = 0; i < datas.size(); i++) {
				doc = (Document) datas.get(i);
				ByteSizeValue byteSizeValue = new ByteSizeValue(doc.get_ContentSize().longValue(), ByteSizeUnit.BYTES);
				dto.setContentElementKBytes(dto.getContentElementKBytes() + byteSizeValue.getKbFrac());
				dto.setContentElementMBytes(dto.getContentElementMBytes() + byteSizeValue.getMbFrac());
				dto.setContentElementGBytes(dto.getContentElementGBytes() + byteSizeValue.getGbFrac());
				dto.setContentElementTBytes(dto.getContentElementTBytes() + byteSizeValue.getTbFrac());
				//formatValue = df.format(contentSizeBite / (1024 * 1024));
				/*if (!"∞".equals(formatValue)) {
					dto.setContentElementMBytes(dto.getContentElementMBytes() + Double.valueOf(formatValue));
				}*/
				/*System.out.println(contentSizeBite / (1024 * 1024 * 1024));
				formatValue = df.format(contentSizeBite / (1024 * 1024 * 1024));
				if (!"∞".equals(formatValue)) {
					dto.setContentElementGBytes(dto.getContentElementGBytes() + Double.valueOf(formatValue));
				}*/
				/*System.out.println(contentSizeBite / (1024 * 1024 * 1024 * 1024));
				formatValue = df.format(contentSizeBite / (1024 * 1024 * 1024 * 1024));
				if (!"∞".equals(formatValue)) {
					dto.setContentElementTBytes(dto.getContentElementTBytes() + Double.valueOf(formatValue));
				}*/
			}
		}
		// %1$替换索引号，-左对齐，重新设置两位小数
		String FORMAT_F = "%1$-1.2f";
		dto.setContentElementKBytes(Double.valueOf(String.format(FORMAT_F, dto.getContentElementKBytes())));
		dto.setContentElementMBytes(Double.valueOf(String.format(FORMAT_F, dto.getContentElementMBytes())));
		dto.setContentElementGBytes(Double.valueOf(String.format(FORMAT_F, dto.getContentElementGBytes())));
		dto.setContentElementTBytes(Double.valueOf(String.format(FORMAT_F, dto.getContentElementTBytes())));
		return dto;
	}

	/**
	 * 根据一个属性的多个值，获取总条目
	 * 
	 * @param os
	 * @param symbolicClassName
	 * @param property
	 * @param values
	 * @param searchableField
	 *            指定【是否可检索】的字段名称
	 * @param searchable
	 * @return
	 */
	public int getTotalCount(ObjectStore os, String symbolicClassName, String property, String[] values,
			String searchableField, boolean searchable) {

		Assert.notNull(property);
		Assert.notEmpty(values);

		// Create a SearchSQL instance and specify the SQL statement (using the
		// helper methods).
		SearchSQL sqlObject = new SearchSQL();
		sqlObject.setSelectList("d.*");
		// sqlObject.setMaxRecords(ecmConf.getSearchMaxRecords());
		sqlObject.setFromClauseInitialValue(symbolicClassName, "d", false);
		StringBuilder whereCauseBuf = new StringBuilder();
		boolean flag = true;
		whereCauseBuf.append("d." + property);
		whereCauseBuf.append(" IN (");
		for (String val : values) {
			if (flag) {
				whereCauseBuf.append("'" + val + "'");
				flag = false;
				continue;
			}
			whereCauseBuf.append(" , ");
			whereCauseBuf.append("'" + val + "'");
		}
		whereCauseBuf.append(")");
		// 是否可被检索
		whereCauseBuf.append(" and " + searchableField + "=" + ((searchable) ? "True" : "False"));

		sqlObject.setWhereClause(whereCauseBuf.toString());

		// Create a SearchScope instance. (Assumes you have the object store
		// object.)
		SearchScope search = new SearchScope(os);

		// Set the (Boolean) value for the continuable parameter. This indicates
		// whether to iterate requests for subsequent pages of result data when
		// the end of the
		// first page of results is reached. If null or false, only a single
		// page of results is
		// returned.
		// Boolean continuable = true;
		// 设置为false的情况下，才能获取到总条目
		IndependentObjectSet oset = search.fetchObjects(sqlObject, null, null, false);

		int totalCount = ((SubSetImpl) oset).getList().size();

		return totalCount;
	}

	/**
	 * 根据多个属性和多个属性值，获取总条目
	 * 
	 * @param os
	 * @param symbolicClassName
	 * @param properties
	 * @param values
	 * @param searchable
	 * @return
	 */
	public int getTotalCount(ObjectStore os, String symbolicClassName, String[] properties, String[] values,
			boolean searchable) {

		Assert.notNull(properties);
		Assert.notEmpty(values);

		// Create a SearchSQL instance and specify the SQL statement (using the
		// helper methods).
		SearchSQL sqlObject = new SearchSQL();
		sqlObject.setSelectList("d.*");
		// sqlObject.setMaxRecords(ecmConf.getSearchMaxRecords());
		sqlObject.setFromClauseInitialValue(symbolicClassName, "d", false);
		StringBuilder whereCauseBuf = new StringBuilder();
		boolean flag = true;

		for (int i = 0; i < values.length; i++) {
			if (flag) {
				whereCauseBuf.append("d." + properties[i] + " = '" + values[i] + "'");
				flag = false;
				continue;
			}
			whereCauseBuf.append(" AND ");
			whereCauseBuf.append("d." + properties[i] + " = '" + values[i] + "'");
		}

		// 是否可被检索
		whereCauseBuf.append(" and "+this.extPropMaterialConf.getSearchable()+"=" + ((searchable) ? "True" : "False"));

		sqlObject.setWhereClause(whereCauseBuf.toString());

		// Create a SearchScope instance. (Assumes you have the object store
		// object.)
		SearchScope search = new SearchScope(os);

		// Set the (Boolean) value for the continuable parameter. This indicates
		// whether to iterate requests for subsequent pages of result data when
		// the end of the
		// first page of results is reached. If null or false, only a single
		// page of results is
		// returned.
		// Boolean continuable = true;
		// 设置为false的情况下，才能获取到总条目
		IndependentObjectSet oset = search.fetchObjects(sqlObject, null, null, false);

		int totalCount = ((SubSetImpl) oset).getList().size();

		return totalCount;
	}

	/**
	 * 根据多属性和值，获取总记录
	 * 
	 * @param os
	 * @param symbolicClassName
	 * @param properties
	 * @param values
	 * @return
	 */
	public int getTotalCount(ObjectStore os, String symbolicClassName, String[] properties, String[] values) {

		Assert.notNull(properties);
		Assert.notEmpty(values);

		// Create a SearchSQL instance and specify the SQL statement (using the
		// helper methods).
		SearchSQL sqlObject = new SearchSQL();
		sqlObject.setSelectList("d.*");
		// sqlObject.setMaxRecords(ecmConf.getSearchMaxRecords());
		sqlObject.setFromClauseInitialValue(symbolicClassName, "d", false);
		StringBuilder whereCauseBuf = new StringBuilder();
		boolean flag = true;

		for (int i = 0; i < values.length; i++) {
			if (flag) {
				whereCauseBuf.append("d." + properties[i] + " = '" + values[i] + "'");
				flag = false;
				continue;
			}
			whereCauseBuf.append(" AND ");
			whereCauseBuf.append("d." + properties[i] + " = '" + values[i] + "'");
		}

		sqlObject.setWhereClause(whereCauseBuf.toString());
		// Create a SearchScope instance. (Assumes you have the object store
		// object.)
		SearchScope search = new SearchScope(os);

		// Set the (Boolean) value for the continuable parameter. This indicates
		// whether to iterate requests for subsequent pages of result data when
		// the end of the
		// first page of results is reached. If null or false, only a single
		// page of results is
		// returned.
		// Boolean continuable = true;
		// 设置为false的情况下，才能获取到总条目
		IndependentObjectSet oset = search.fetchObjects(sqlObject, null, null, false);
		
		int totalCount = ((SubSetImpl) oset).getList().size();

		return totalCount;
	}

	/**
	 * 根据属性，分页查询
	 * 
	 * @param os
	 * @param symbolicClassName
	 * @param property
	 * @param values
	 * @param pageNo
	 *            页码
	 * @param pageSize
	 *            页大小
	 * @param searchable
	 *            是否可检索，默认字段【XZ_Searchable】
	 * @return
	 */
	public <T> T searchWithProperty(ObjectStore os, String symbolicClassName, String property, String[] values,
			int pageNo, int pageSize, boolean searchable) {

		Assert.notNull(property);
		Assert.notEmpty(values);

		Pagination pagination = null;
		// Create a SearchSQL instance and specify the SQL statement (using the
		// helper methods).
		SearchSQL sqlObject = createDocSearchSQL(symbolicClassName, property, values, searchable);

		// Create a SearchScope instance. (Assumes you have the object store
		// object.)
		SearchScope search = new SearchScope(os);

		// Set the (Boolean) value for the continuable parameter. This indicates
		// whether to iterate requests for subsequent pages of result data when
		// the end of the
		// first page of results is reached. If null or false, only a single
		// page of results is
		// returned.
		// Boolean continuable = true;
		// 设置为false的情况下，才能获取到总条目
		IndependentObjectSet oset = search.fetchObjects(sqlObject, null, null, false);
		int totalCount = ((SubSetImpl) oset).getList().size();
		// 再次分页查询，设置continuable=true
		PageIterator pi = search.fetchObjects(sqlObject, pageSize, null, true).pageIterator();
		pi.setPageSize(pageSize);
		int i = 0;
		while (pi.nextPage()) {
			i++;
			if (i == pageNo) {
				Object[] page = pi.getCurrentPage();
				List<DocumentDTO> data = docUtil.document2dto(page);
				pagination = new Pagination(pageNo, pageSize, totalCount, data);
				return (T) pagination;
			}
		}
		pagination = new Pagination(pageNo, pageSize, 0, null);
		return (T) pagination;

	}

	/**
	 * 检索满足条件所有数据
	 * 
	 * @param os
	 * @param symbolicClassName
	 * @param property
	 * @param values
	 * @param searchable
	 * @return List
	 */
	public <T> T searchAllWithProperty(ObjectStore os, String symbolicClassName, String property, String[] values,
			boolean searchable) {

		Assert.notNull(property);
		Assert.notEmpty(values);

		Pagination pagination = null;
		// Create a SearchSQL instance and specify the SQL statement (using the
		// helper methods).
		SearchSQL sqlObject = createDocSearchSQL(symbolicClassName, property, values, searchable);

		// Create a SearchScope instance. (Assumes you have the object store
		// object.)
		SearchScope search = new SearchScope(os);

		// Set the (Boolean) value for the continuable parameter. This indicates
		// whether to iterate requests for subsequent pages of result data when
		// the end of the
		// first page of results is reached. If null or false, only a single
		// page of results is
		// returned.
		// Boolean continuable = true;
		// 设置为false的情况下，才能获取到总条目
		IndependentObjectSet oset = search.fetchObjects(sqlObject, null, null, false);
		return (T) ((SubSetImpl) oset).getList();

	}
	
	public <T> T searchAllWithProperty(ObjectStore os, String symbolicClassName, String property, String[] values) {

		Assert.notNull(property);
		Assert.notEmpty(values);

		Pagination pagination = null;
		// Create a SearchSQL instance and specify the SQL statement (using the
		// helper methods).
		SearchSQL sqlObject = createDocSearchSQL(symbolicClassName, property, values);

		// Create a SearchScope instance. (Assumes you have the object store
		// object.)
		SearchScope search = new SearchScope(os);

		// Set the (Boolean) value for the continuable parameter. This indicates
		// whether to iterate requests for subsequent pages of result data when
		// the end of the
		// first page of results is reached. If null or false, only a single
		// page of results is
		// returned.
		// Boolean continuable = true;
		// 设置为false的情况下，才能获取到总条目
		IndependentObjectSet oset = search.fetchObjects(sqlObject, null, null, false);
		return (T) ((SubSetImpl) oset).getList();

	}
	/**
	 * 
	 * @param symbolicClassName
	 * @param property
	 * @param values
	 * @param searchable
	 * @return
	 */
	private SearchSQL createDocSearchSQL(String symbolicClassName, String property, String[] values, boolean searchable) {
		
		Assert.notNull(property);
		
		SearchSQL sqlObject = new SearchSQL();
		sqlObject.setSelectList("d.*");
		sqlObject.setMaxRecords(ecmConf.getSearchMaxRecords());
		sqlObject.setFromClauseInitialValue(symbolicClassName, "d", false);
		StringBuilder whereCauseBuf = new StringBuilder();
		boolean flag = true;
		
		whereCauseBuf.append("d." + property);
		whereCauseBuf.append(" IN (");
		for (String val : values) {
			if (flag) {
				whereCauseBuf.append("'" + val + "'");
				flag = false;
			}
			whereCauseBuf.append(" , ");
			whereCauseBuf.append("'" + val + "'");
		}
		whereCauseBuf.append(")");
		// 是否可被检索
		whereCauseBuf
				.append(" and " + this.extPropMaterialConf.getSearchable() + "=" + ((searchable) ? "True" : "False"));

		sqlObject.setWhereClause(whereCauseBuf.toString());
		return sqlObject;
	}
	
    private SearchSQL createDocSearchSQL(String symbolicClassName, String property, String[] values) {
		
		Assert.notNull(property);
		
		SearchSQL sqlObject = new SearchSQL();
		sqlObject.setSelectList("d.*");
		sqlObject.setMaxRecords(ecmConf.getSearchMaxRecords());
		sqlObject.setFromClauseInitialValue(symbolicClassName, "d", false);
		StringBuilder whereCauseBuf = new StringBuilder();
		boolean flag = true;
		
		whereCauseBuf.append("d." + property);
		whereCauseBuf.append(" IN (");
		for (String val : values) {
			if (flag) {
				whereCauseBuf.append("'" + val + "'");
				flag = false;
			}
			whereCauseBuf.append(" , ");
			whereCauseBuf.append("'" + val + "'");
		}
		whereCauseBuf.append(")");

		sqlObject.setWhereClause(whereCauseBuf.toString());
		return sqlObject;
	}
    
    /**
     * 创建查询文件夹内容的SQL对象
     * @param symbolicClassName 类标识：Folder和Document
     * @param folderId 父文件夹
     * @return
     */
    private SearchSQL createSubObjSearchSQLInFolder(String symbolicClassName, String folderId) {
    	
    	SearchSQL sqlObject = new SearchSQL();
		sqlObject.setSelectList("obj.*");
		sqlObject.setMaxRecords(ecmConf.getSearchMaxRecords());
		sqlObject.setFromClauseInitialValue(symbolicClassName, "obj", true); // 包括子类，因为案例文件夹继承于文件夹
		
		String whereCause = " this inFolder '"+folderId+"'";
		
		sqlObject.setWhereClause(whereCause);
		return sqlObject;
    }
	
	@SuppressWarnings("unchecked")
	public <T> T searchWithProperty(ObjectStore os, String symbolicClassName, String property, String keyword,
			int pageNo, int pageSize, boolean searchable) {

		Assert.notNull(property);
		Pagination pagination = null;
		// Create a SearchSQL instance and specify the SQL statement (using the
		// helper methods).
		SearchSQL sqlObject = new SearchSQL();
		sqlObject.setSelectList("d.*");
		sqlObject.setMaxRecords(ecmConf.getSearchMaxRecords());
		sqlObject.setFromClauseInitialValue(symbolicClassName, "d", false);
		String whereCause = "d." + property + " like '%" + keyword + "%'";
		// 是否可被检索
		whereCause += " and " + this.extPropMaterialConf.getSearchable() + "=" + ((searchable) ? "True" : "False");
		sqlObject.setWhereClause(whereCause);

		// Create a SearchScope instance. (Assumes you have the object store
		// object.)
		SearchScope search = new SearchScope(os);

		// Set the (Boolean) value for the continuable parameter. This indicates
		// whether to iterate requests for subsequent pages of result data when
		// the end of the
		// first page of results is reached. If null or false, only a single
		// page of results is
		// returned.
		// Boolean continuable = true;
		// 设置为false的情况下，才能获取到总条目
		IndependentObjectSet oset = search.fetchObjects(sqlObject, null, null, false);
		// 再次分页查询，设置continuable=true
		PageIterator pi = search.fetchObjects(sqlObject, pageSize, null, true).pageIterator();
		int totalCount = ((SubSetImpl) oset).getList().size();
		pi.setPageSize(pageSize);
		int i = 0;
		while (pi.nextPage()) {
			i++;
			if (i == pageNo) {
				Object[] page = pi.getCurrentPage();
				List<DocumentDTO> data = docUtil.document2dto(page);
				pagination = new Pagination(pageNo, pageSize, totalCount, data);
				return (T) pagination;
			}
		}
		pagination = new Pagination(pageNo, pageSize, 0, null);
		return (T) pagination;

	}

	/**
	 * 查找子文件夹
	 * 
	 * @param os
	 * @param parentContainerId，容器id，可以是文件夹id，可以是域的根目录
	 * @return
	 */
	public List<DocumentDTO> findSubFolders(ObjectStore os, String parentContainerId) {

		if (StringUtil.isNullOrEmpty(parentContainerId)) {

			List<DocumentDTO> documentDTOs = new ArrayList<DocumentDTO>();
			DocumentDTO root = new DocumentDTO();
			root.setId(os.get_RootFolder().get_Id().toString());
			root.setText(os.get_RootFolder().get_Name());
			root.setChildren(folderUtil.folder2dto(os.get_TopFolders()));
			documentDTOs.add(root);

			return documentDTOs;
		} else {

			return folderUtil
					.folder2dto(Factory.Folder.fetchInstance(os, new Id(parentContainerId), null).get_SubFolders());
		}
	}

	/**
	 * 查找子文件夹
	 * 
	 * @param os
	 * @param parentFolder
	 *            父文件夹
	 * @return
	 */
	public List<DocumentDTO> findSubFolders(ObjectStore os, Folder parentFolder) {

		return folderUtil.folder2dto(parentFolder.get_SubFolders());
	}

	/**
	 * 根据文件夹id，检索文件夹下的文档和文件夹
	 * 
	 * @param os
	 * @param folderId
	 * @return
	 */
	public List<DocumentDTO> findSubFoldersAndDocumentsByFolderId(ObjectStore os, String folderId) {
		// folder
		Folder folder = Factory.Folder.fetchInstance(os, new Id(folderId), null);
		// document
		DocumentSet documentSet = folder.get_ContainedDocuments();

		List<DocumentDTO> documentDTOs = docUtil.document2dto(documentSet);

		// search children folders
		List<DocumentDTO> folderDTOS = folderUtil.folder2dto(folder.get_SubFolders());

		documentDTOs.addAll(folderDTOS);

		return documentDTOs;
	}

	/**
	 * 根据文件夹路径，获取文档和文件夹
	 * 
	 * @param os
	 * @param pathAndFolderName
	 * @return
	 */
	public List<DocumentDTO> findSubFoldersAndDocumentsByFolderPath(ObjectStore os, String pathAndFolderName) {

		// folder
		Folder folder = this.folderUtil.loadAndCreateByPath(os, pathAndFolderName);
		
		return this.findSubFoldersAndDocumentsByFolderPath(os, folder);
		// document
		/*DocumentSet documentSet = folder.get_ContainedDocuments();

		List<DocumentDTO> documentDTOs = docUtil.document2dto(documentSet);

		// search children folders
		List<DocumentDTO> folderDTOS = folderUtil.folder2dto(folder.get_SubFolders());

		documentDTOs.addAll(folderDTOS);*/

		//return documentDTOs;
	}
	/**
	 * 根据父文件夹，获取文档和文件夹
	 * @param os
	 * @param parentFolder
	 * @return
	 */
	public List<DocumentDTO> findSubFoldersAndDocumentsByFolderPath(ObjectStore os, Folder parentFolder) {

		// Folder folder = Factory.Folder.fetchInstance(os, pathAndFolderName,
		// null);
		// document
		DocumentSet documentSet = parentFolder.get_ContainedDocuments();

		List<DocumentDTO> documentDTOs = docUtil.document2dto(documentSet);

		// search children folders
		List<DocumentDTO> folderDTOS = folderUtil.folder2dto(parentFolder.get_SubFolders());

		documentDTOs.addAll(folderDTOS);

		return documentDTOs;
	}
	
	public List<DocumentDTO> findSubFoldersAndDocumentsByFolderPath(ObjectStore os, Folder parentFolder, int pageNo, int pageSize) {
		
		return null;
	}

	/**
	 * 创建根据创建时间和rank排序的全文检索构造器
	 * 
	 * @return
	 */
/*	private StringBuilder createFullTextSelectQueryRankBuilder(String keyword) {

		StringBuilder buf = new StringBuilder();
		buf.append("SELECT  ");
		buf.append(" d.Id,d.VersionSeries,d.DocumentTitle, d.Name,d.Creator,d.DateCreated, ");
		buf.append("d." + this.extPropMaterialConf.getOrganization() + ",");
		buf.append("d." + this.extPropMaterialConf.getSpatialDomain() + ",");
		buf.append("d." + this.extPropMaterialConf.getResourceType() + ",");
		buf.append(" FROM (" + ecmConf.getDefaultDocumentClass()
				+ " d INNER JOIN ContentSearch c ON  d.This = c.QueriedObject) ");
		buf.append(" WHERE CONTAINS(c.Content,'" + keyword + "') AND d." + this.extPropMaterialConf.getSearchable()
				+ "=True ");
		buf.append(" ORDER BY d.DateCreated DESC,c.Rank DESC");
		return buf;

	}*/

	/**
	 * 分页全文检索，并排序
	 * 
	 * @param os
	 * @param pageNo
	 * @param pageSize
	 * @param keyword
	 * @return
	 */
	public Pagination fullTextSearchOfDocRank(ObjectStore os, int pageNo, int pageSize, String keyword) {

		/*upstream my_server_pool {
			server 127.0.0.1:4444 weight=1;
		}*/
		
		// 使用此方法createFullTextSearchSQLObject 设置了最大条目不起作用
        //SearchSQL searchSQL = this.createFullTextSearchSQLObject(os, this.ecmConf.getDefaultDocumentClass(), false, keyword, Boolean.TRUE);
		StringBuilder sb = new StringBuilder();
		sb.append(" SELECT ");
		sb.append(" d.DocumentTitle,d.ContentSize,d.MimeType,d.LastModifier,d.DateLastModified,d.VersionSeries,d.Id,d.Owner,d.VersionSeries,d.Name,d.Creator,d.DateCreated,");
		sb.append(" d."+this.extPropMaterialConf.getOrganization()+",d."+this.extPropMaterialConf.getSpatialDomain()+",d."+this.extPropMaterialConf.getResourceType()+",c.Rank ");
		sb.append(" FROM "+this.ecmConf.getDefaultDocumentClass()+" d ");
		sb.append(" INNER JOIN ContentSearch c ON d.This = c.QueriedObject ");
		sb.append(" WHERE "+this.extPropMaterialConf.getSearchable()+"=True AND CONTAINS(d.*,'"+keyword+"') ");
		sb.append(" ORDER BY d.DateCreated DESC,c.Rank Desc ");
		sb.append(" OPTIONS (FULLTEXTROWLIMIT "+this.ecmConf.getSearchMaxRecords()+")");
       
        SearchSQL searchSQL = new SearchSQL(sb.toString());
		Pagination docs = this.pageSearchRows(os, searchSQL, pageNo, pageSize);

		return docs;
	}

	/**
	 * 检索文档，并按照一定的规则进行排序 规则顺序： 1）按照内容的匹配度：rank 2）文档标题的匹配度； 3）文档的下载和收藏数，最后修改时间；
	 * 
	 * @param os
	 * @param rowlimitSize
	 *            结果集大小
	 * @param keyword
	 *            关键字
	 * @return
	 */
	/*@Deprecated
	public JSONArray searchDocumentRank(ObjectStore os, int rowlimitSize, String keyword) {

		try {

			Hashtable<String, SearchItem> items = new Hashtable<String, SearchItem>();
			// case标识
			Map<String, List<SearchItem>> caseIdentifiers = new HashMap<String, List<SearchItem>>();
			StringBuilder buf = new StringBuilder();
			buf.append("SELECT d.Id,d.VersionSeries,d.Name,d.Creator,d.DateCreated,d.DateLastModified");
			buf.append("d." + this.extPropMaterialConf.getOrganization() + ",");
			buf.append("d." + this.extPropMaterialConf.getSpatialDomain() + ",");
			buf.append("d." + this.extPropMaterialConf.getResourceType() + ",");
			buf.append("c.Rank ");
			buf.append("FROM " + this.ecmConf.getDefaultDocumentClass() + " d ");
			buf.append("INNER JOIN ContentSearch c ON d.This = c.QueriedObject ");
			buf.append("WHERE "+this.extPropMaterialConf.getSearchable()+"=True AND CONTAINS(d.*,'" + keyword + "') ");
			buf.append("ORDER BY d.DateCreated DESC,c.Rank DESC ");
			buf.append("OPTIONS (FULLTEXTROWLIMIT " + rowlimitSize + ")");

			SearchScope ss = new SearchScope(os);
			RepositoryRowSet rows = ss.fetchRows(new SearchSQL(buf.toString()), null, null, false);
			Iterator<?> iterator = rows.iterator();
			RepositoryRowImpl row;
			Properties pros;
			SearchItem item;
			List<SearchItem> list;
			String caseIdentifier;
			StringBuffer sb = new StringBuffer();
			while (iterator.hasNext()) {
				row = (RepositoryRowImpl) iterator.next();
				pros = row.getProperties();
				item = new SearchItem(items, pros, keyword);
				// 如果是项目文档
				if (pros.getStringValue("XZ_ResourceType").equalsIgnoreCase("Res_Pck_Project")) {
					caseIdentifier = pros.getStringValue("XZ_SpatialDomain");
					if (sb.indexOf(caseIdentifier) < 0) {
						sb.append("'" + caseIdentifier + "',");
					}
					if (!caseIdentifiers.containsKey(caseIdentifier)) {
						caseIdentifiers.put(caseIdentifier, new ArrayList<SearchItem>());
					}
					caseIdentifiers.get(caseIdentifier).add(item);
				}
			}
			if (caseIdentifiers.size() > 0) {
				buf = buf.delete(0, buf.length());
				buf.append("SELECT CmAcmCaseIdentifier, ");
				buf.append(this.extPropCaseConf.getProjectName());
				buf.append(" FROM XZ_CASETYPE_JYXM ");
				buf.append(" WHERE CmAcmCaseIdentifier IN(" + sb.substring(0, sb.toString().length() - 1) + ")");

				rows = ss.fetchRows(new SearchSQL(buf.toString()), null, null, false);
				iterator = rows.iterator();
				while (iterator.hasNext()) {
					row = (RepositoryRowImpl) iterator.next();
					caseIdentifier = row.getProperties().getStringValue("CmAcmCaseIdentifier");
					list = caseIdentifiers.get(caseIdentifier);
					for (SearchItem temp : list) {
						// 记录每个项目文档所属的项目（显示项目名称）
						temp.setProjectName(row.getProperties().getStringValue("XZ_XMMC"));
					}
				}

			}

			if (items.size() > 0) {
				sb = sb.delete(0, sb.length());
				Enumeration<String> ids = items.keys();
				while (ids.hasMoreElements()) {
					sb.append("'" + ids.nextElement() + "',");
				}
				buf = buf.delete(0, buf.length());
				buf.append("SELECT ClbDocumentId,ClbDownloadCount,");
				buf.append(this.extPropConf.getFavorites() + ",");
				buf.append(this.extPropConf.getUpvoteCount());
				buf.append("FROM DistSummaryData ");
				buf.append("WHERE ClbDocumentId IN(" + sb.substring(0, sb.toString().length() - 1) + ")");

				rows = ss.fetchRows(new SearchSQL(buf.toString()), null, null, false);
				iterator = rows.iterator();
				while (iterator.hasNext()) {
					row = (RepositoryRowImpl) iterator.next();
					items.get(row.getProperties().getIdValue("ClbDocumentId").toString())
							.setSocialData(row.getProperties());
				}
			}

			return SearchItem.getSortedJson(items);
			
		} catch (Exception e) {
			e.printStackTrace();
			
			return null;
		}
	}*/
	/**
	 * 分页检索子文件夹
	 * @param os
	 * @param folderId
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public Pagination findSubFoldersPage(ObjectStore os, String folderId, int pageNo, int pageSize) {

		SearchSQL sqlObject = createSubObjSearchSQLInFolder(ClassNames.FOLDER, folderId);

		return this.pageSearch(os, sqlObject, ClassNames.FOLDER, pageNo, pageSize, false, false);

	}
	
	/**
	 * 根据类标识，过滤器创建查询子对象SQL，默认以创建时间倒序
	 * @param symbolicClassName
	 * @param folderId
	 * @param filters
	 * @return
	 */
	 private SearchSQL createSubObjSearchSQLInFolder(String symbolicClassName, String folderId, List<SimplePropertyFilter> filters, String orderProperty, Boolean asc, String keyword) {
	    	
	    	SearchSQL sqlObject = new SearchSQL();
			sqlObject.setSelectList("obj.*");
			sqlObject.setMaxRecords(ecmConf.getSearchMaxRecords());
			sqlObject.setFromClauseInitialValue(symbolicClassName, "obj", true); // 包括子类，因为案例文件夹继承于文件夹
			boolean flag = false;
			boolean isString = false;
			StringBuilder whereBuf = new StringBuilder();
			whereBuf.append(this.extPropMaterialConf.getSearchable() + "=True ");
			// XZ_AssociatePerson  INTERSECTS('FA8458EB-9D85-42D6-A234-8D22489C6859') 
			if(filters != null && !filters.isEmpty()) {
				for(SimplePropertyFilter filter : filters) {
					if(null == filter.getValues()){
						// 过滤属性为null的记录
						whereBuf.append(" AND "+ filter.getPropertyName()+" IS NULL ");	
						continue;
					}
					if(filter.getPropertyType().equalsIgnoreCase(DataType.ListOfString)){
						whereBuf.append(" AND "+ filter.getPropertyName()+" INTERSECTS(");	
					}else{
						whereBuf.append(" AND "+ filter.getPropertyName()+" IN(");	
					}
					for(Object val : filter.getValues()){
						if(!isString && val instanceof String){
							isString = true;
						}
						if(!flag){
							whereBuf.append(isString? "'"+val+"'" : val);
							flag = true;
							continue;
						}
						whereBuf.append(isString? ",'"+val+"'" : val);
					}
					whereBuf.append(")");
					flag = false;
					isString = false;
				}
			}
			if (!StringUtils.isEmpty(keyword)) {
				sqlObject.setFromClauseAdditionalJoin(JoinOperator.INNER, "ContentSearch ", "cs", "obj.This",
						JoinComparison.EQUAL, "cs.QueriedObject", true);
				whereBuf.append(" and " + " CONTAINS(obj.*, '*" + keyword + "*')");
			}
			whereBuf.append(" AND this inFolder '"+folderId+"'");
			if(!StringUtils.isEmpty(orderProperty)) {
				sqlObject.setOrderByClause("obj."+orderProperty + (asc? "ASC" : "DESC"));
			} else {
				sqlObject.setOrderByClause("obj.DateCreated DESC");
			}
			
			sqlObject.setWhereClause(whereBuf.toString());
			return sqlObject;
	    }
	 /**
		 * 分页检索子文档
		 * @param os
		 * @param parentFolderId
		 * @param pageNo
		 * @param pageSize
		 * @return
		 */
		public Pagination findSubDocsPage(ObjectStore os, String parentFolderId, int pageNo, int pageSize) {

			SearchSQL sqlObject = createSubObjSearchSQLInFolder(this.ecmConf.getDefaultDocumentClass(), parentFolderId);

			return this.pageSearch(os, sqlObject, this.ecmConf.getDefaultDocumentClass(), pageNo, pageSize, false, false);

		}
	/**
	 * 根据过滤器分页检索子文档，默认以创建时间倒序
	 * @param os
	 * @param parentFolderId
	 * @param filters 如果不需要过滤，可为null
	 * @param pageNo
	 * @param pageSize
	 * @param keyword 关键字，如果不做关键字过滤，则传入空""
	 * @return
	 */
	public Pagination findSubDocsPage(ObjectStore os, String parentFolderId, List<SimplePropertyFilter> filters, int pageNo, int pageSize, String keyword, boolean needThumbnails, boolean thumbnailByte) {

		SearchSQL sqlObject = createSubObjSearchSQLInFolder(this.ecmConf.getDefaultDocumentClass(), parentFolderId, filters, "", null, keyword);

		return this.pageSearch(os, sqlObject, ClassNames.DOCUMENT, pageNo, pageSize, needThumbnails, thumbnailByte);
	}
	/**
 * 根据过滤器分页检索子文档，默认以创建时间倒序
	 * @param os
	 * @param parentFolderId
	 * @param filters 如果不需要过滤，可为null
	 * @param pageNo
	 * @param pageSize
	 * @param keyword 关键字，如果不做关键字过滤，则传入空""
	 * @return
	 */
	public Pagination findSubDocsPageThumbnailByte(ObjectStore os, String parentFolderId, List<SimplePropertyFilter> filters, int pageNo, int pageSize, String keyword) {

		SearchSQL sqlObject = createSubObjSearchSQLInFolder(this.ecmConf.getDefaultDocumentClass(), parentFolderId, filters, "", null, keyword);

		return this.pageSearch(os, sqlObject, ClassNames.DOCUMENT, pageNo, pageSize, true, true);
	}
	/**
	 * 获取最新的一份文档
	 * @param os
	 * @return
	 */
	public Document getNewestDoc(ObjectStore os) {
		SearchSQL sqlObject = new SearchSQL();
		sqlObject.setSelectList("obj.*");
		sqlObject.setMaxRecords(ecmConf.getSearchMaxRecords());
		sqlObject.setFromClauseInitialValue(this.ecmConf.getDefaultDocumentClass(), "obj", true); 
		sqlObject.setMaxRecords(1);
		sqlObject.setOrderByClause("obj.DateLastModified DESC");
	    SearchScope searchScope = new SearchScope(os);
		// sqlObject.setMaxRecords(ecmConf.getSearchMaxRecords());
		Pagination pagination = null;

		// 设置为false的情况下，才能获取到总条目
		IndependentObjectSet oset = searchScope.fetchObjects(sqlObject, 1, null, false);
		// 再次分页查询，设置continuable=true
		PageIterator pi = searchScope.fetchObjects(sqlObject, 1, null, true).pageIterator();
		while(pi.nextPage()) {
			Object[] page = pi.getCurrentPage();
			return (Document) page[0];
		}
		return null;
	}
}
