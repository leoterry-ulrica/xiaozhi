package com.dist.bdf.manager.ecm.utils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.dist.bdf.base.utils.DistThreadManager;
import com.dist.bdf.common.conf.ecm.ECMConf;
import com.dist.bdf.common.conf.ecm.ExtPropertyConf;
import com.dist.bdf.model.dto.dcm.SocialSummaryDTO;
import com.filenet.api.collection.IndependentObjectSet;
import com.filenet.api.collection.RepositoryRowSet;
import com.filenet.api.constants.RefreshMode;
import com.filenet.api.core.CmAbstractPersistable;
import com.filenet.api.core.Document;
import com.filenet.api.core.IndependentObject;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.property.Property;
import com.filenet.api.query.SearchSQL;
import com.filenet.api.query.SearchScope;
import com.filenet.api.util.Id;
import com.filenet.apiimpl.query.RepositoryRowImpl;

/**
 * 数慧统计信息服务工具
 * @author weifj
 * @version 1.0，2016/06/17，weifj，创建
 */
@Component
public class DistSocialUtil extends P8SocialUtil {

	private static Logger logger = LoggerFactory.getLogger(DistSocialUtil.class);
	@Autowired
	private ECMConf ecmConf;
	@Autowired
	@Qualifier("ExtPropertyConf")
	private ExtPropertyConf extPropConf;

	/**
	 * 创建查询构建器
	 * @param documentId
	 * @return
	 */
	private StringBuilder createSocialSelectQueryBuilder(String documentId) {
		
		Set<String> socialSummaryPropertySet = new HashSet<String>(Arrays.asList(new String[] { "Id, ClbDownloadCount", this.extPropConf.getFavorites(), this.extPropConf.getUpvoteCount(), "ClbDocumentId", this.extPropConf.getObjectIdType()}));

		
		StringBuilder buf = new StringBuilder();
	    buf.append("SELECT ");
	    buf.append(StringUtils.join(socialSummaryPropertySet, ','));
	    buf.append(" FROM ");
	    buf.append(ecmConf.getSummaryDataDefaultClassId());
	    buf.append(" WHERE ");
	    buf.append(" ClbDocumentId = ");
	    buf.append("Object("+documentId+")");
		return buf;
	}
	/**
	 * 查询指定文档的统计数据
	 * @param document
	 * @return
	 */
	protected IndependentObject retrieveSummaryData(Document document) {
		
	    ObjectStore os = document.getObjectStore();
	    String documentId = document.get_Id().toString();
	    StringBuilder buf = createSocialSelectQueryBuilder(documentId);
	    SearchSQL search = new SearchSQL(buf.toString());
	    SearchScope searchScope = new SearchScope(os);
	   
	    IndependentObjectSet rows = searchScope.fetchObjects(search, null, null, Boolean.FALSE);

	    IndependentObject summaryData = null;
	    Iterator it = rows.iterator();
	    if (it.hasNext()) {
	      summaryData = (IndependentObject)it.next();
	    }
	    return summaryData;
	  }
	/**
	 * 查询指定文档的social数据
	 * @param os
	 * @param documentId
	 * @return
	 */
	protected IndependentObject retrieveSummaryData(ObjectStore os, Id documentId) {
	    
	    /*StringBuilder buf = createSocialSelectQueryBuilder(documentId.toString());
	    SearchSQL search = new SearchSQL(buf.toString());
	    SearchScope searchScope = new SearchScope(os);
	   
	    IndependentObjectSet rows = searchScope.fetchObjects(search, null, null, Boolean.FALSE);
	    
	    IndependentObject summaryData = null;
	    Iterator it = rows.iterator();
	    if (it.hasNext()) {
	      summaryData = (IndependentObject)it.next();
	    }
	    return summaryData;*/
		
		return retrieveSummaryData(os, documentId.toString());
	  }
	/**
	 * 查询指定文档的social数据
	 * @param os
	 * @param documentId
	 * @return
	 */
      protected IndependentObject retrieveSummaryData(ObjectStore os, String documentId) {
	    
	    StringBuilder buf = createSocialSelectQueryBuilder(documentId);
	    SearchSQL search = new SearchSQL(buf.toString());
	    SearchScope searchScope = new SearchScope(os);
	   
	    IndependentObjectSet rows = searchScope.fetchObjects(search, null, null, Boolean.FALSE);

	    IndependentObject summaryData = null;
	    Iterator it = rows.iterator();
	    if (it.hasNext()) {
	      summaryData = (IndependentObject)it.next();
	    }
	    return summaryData;
	  }

      /**
       * 检索文档的统计数据：下载数，点赞数和收藏数
       * @param os
       * @param documentId
       * @return
       */
      public SocialSummaryDTO retrieveSocialData(ObjectStore os , String documentId) {
    	  
    	  SearchScope ss = new SearchScope(os);
          StringBuilder sb = new StringBuilder();
          sb.append(" SELECT ClbDocumentId,ClbDownloadCount,");
          sb.append(this.extPropConf.getFavorites()+",");
          sb.append(this.extPropConf.getUpvoteCount());
          sb.append(" FROM "+this.ecmConf.getSummaryDataDefaultClassId());
          sb.append(" WHERE ClbDocumentId ="+documentId);

    	  RepositoryRowSet rows = ss.fetchRows(new SearchSQL(sb.toString()), null, null, false);
    	  Iterator iterator = rows.iterator();
          while (iterator.hasNext()){
        	  
        	  RepositoryRowImpl row = (RepositoryRowImpl)iterator.next();
        	  SocialSummaryDTO dto = new SocialSummaryDTO();
        	  dto.setDocId(documentId);
        	  dto.setDownloadCount(row.getProperties().getInteger32Value("ClbDownloadCount"));
        	  dto.setFavorites(row.getProperties().getInteger32Value(this.extPropConf.getFavorites()));
        	  dto.setUpvoteCount(row.getProperties().getInteger32Value(this.extPropConf.getUpvoteCount()));
              
        	  return dto;
          }
          
    	return null;
    	  
      }
      /**
       * 刷新内存属性值，每次添加都自增1
       * @param properties
       * @param result
       */
      private void refreshPropertiesValue(Map<String, Object> properties, IndependentObject result, boolean isAdd) {
    	  
  		setPropertyValue(properties, result, "ClbDownloadCount", true);
  		setPropertyValue(properties, result, this.extPropConf.getFavorites(), isAdd);
  		setPropertyValue(properties, result, this.extPropConf.getUpvoteCount(), isAdd);
  	
  	}
	private void setPropertyValue(Map<String, Object> properties, IndependentObject result, String propertyName, boolean isAdd) {
		int count;
		Property property;
		Object value;
		if(properties.containsKey(propertyName)){
  			
			if(null == result){
				properties.put(propertyName, (isAdd)? 1:0);// 如果添加，则新增1
				return;
			}
  			property = result.getProperties().find(propertyName);
  			if(null == property){
  				logger.warn("类中没有找到属性 [{}] 的定义。", propertyName);
  				// 说明类的定义没有这个属性
  				properties.remove(propertyName);
  				//properties.put(propertyName, 0);
  			}else{
  				value = property.getObjectValue();
  				if(null == value){
  					properties.put(propertyName, (isAdd)? 1:0);
  				}else{
  					count = result.getProperties().getInteger32Value(propertyName);
  					properties.put(propertyName, (isAdd)? ++count: (count == 0)? 0 : --count);
  				}
  			}
  		}else{
  			if(null == result){
				properties.put(propertyName, 0);
				return;
			}
  		}
	}
    
	/**
	 * social数据，检测是否已存在，如果没有，则直接添加，否则进行更行。
	 * @param os
	 * @param documentId 文档标识符
	 * @param properties 除了文档标识符的其它属性
	 * @return
	 */
	public CmAbstractPersistable addSocialDatum(ObjectStore os, Id documentId, Map<String, Object> properties, boolean isAdd) {

		IndependentObject result = this.retrieveSummaryData(os, documentId);
		refreshPropertiesValue(properties, result, isAdd);
		
		if(result != null){
			String id = result.getProperties().getIdValue("Id").toString();
	
			return super.editSocialDatum(os, id, this.ecmConf.getSummaryDataDefaultClassId(), properties);
		}else{
			if(isAdd){
				return super.addSocialDatum(os, ecmConf.getSummaryDataDefaultClassId(), documentId.toString(), properties);
			}
			return null;
		}
		
	}
	/**
	 * 刪除统计数据
	 * @param os
	 * @param documentId
	 * @return
	 */
	public boolean deleteSocialDatum(ObjectStore os, Id documentId) {
	
		return deleteSocialDatum(os, documentId.toString());
	}
	
	/**
	 * 删除统计数据
	 * @param os
	 * @param documentId
	 * @return
	 */
	public boolean deleteSocialDatum(ObjectStore os, String documentId) {
		
		IndependentObject result = this.retrieveSummaryData(os, documentId);
		if(null == result) { 
			logger.warn(">>>没有找到对应的文档 [{}]", documentId);
			return false;
		}
			
		CmAbstractPersistable datum = (CmAbstractPersistable) result;
		
		datum.delete();
		datum.save(RefreshMode.NO_REFRESH);
		
		return true;
	}
	/**
	 * 线程删除统计数据
	 * @param os
	 * @param documentId
	 */
	public void deleteSocialDatumThread(final ObjectStore os, final String documentId) {

		logger.info(">>>启动异步删除统计数据......");
		
		DistThreadManager.MyCacheThreadPool.execute(new Runnable() {

			@Override
			public void run() {
				
				IndependentObject result = DistSocialUtil.this.retrieveSummaryData(os, documentId);
				if (null == result) {
					logger.warn(">>>没有找到对应的文档 [{}]", documentId);
					return;
				}

				CmAbstractPersistable datum = (CmAbstractPersistable) result;

				datum.delete();
				datum.save(RefreshMode.NO_REFRESH);

			}
		});
		
	}

	/**
	 * 添加social数据，检测是否已存在，如果没有，则直接添加，否则进行更行。
	 * @param os
	 * @param documentId 文档字符串id
	 * @param properties 除了文档标识符的其它属性
	 * @return
	 */
	public CmAbstractPersistable addSocialDatum(ObjectStore os, String documentId, Map<String, Object> properties, boolean isAdd) {

		return this.addSocialDatum(os, new Id(documentId), properties, isAdd);
		
	}
}
