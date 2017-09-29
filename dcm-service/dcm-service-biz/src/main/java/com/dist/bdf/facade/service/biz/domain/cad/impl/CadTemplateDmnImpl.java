package com.dist.bdf.facade.service.biz.domain.cad.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dist.bdf.facade.service.biz.domain.cad.CadTemplateDmn;
import com.dist.bdf.common.constants.MongoFieldConstants;
import com.dist.bdf.model.entity.cad.TemplateEntity;

/**
 * cad的模板
 * 
 * @author weifj
 *
 */
@Service
@Transactional(propagation = Propagation.REQUIRED)
public class CadTemplateDmnImpl implements CadTemplateDmn {

	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public TemplateEntity saveMetadata(TemplateEntity entity) {

		this.mongoTemplate.save(entity);//.insert(entity);
		return entity;
	}

	@Override
	public void deleteMetadataByName(String templateName) {

		 this.mongoTemplate.remove(new Query(Criteria.where(MongoFieldConstants.NAME).is(templateName)), TemplateEntity.class);
	}

	@Override
	public TemplateEntity getMetadataByName(String templateName) {

		Query query = new Query();
		query.addCriteria(new Criteria(MongoFieldConstants.NAME).is(templateName));
		return this.mongoTemplate.findOne(query, TemplateEntity.class);
	}
	@Override
	public boolean isExistByName(String templateName) {

		Query query = new Query();
		query.addCriteria(new Criteria(MongoFieldConstants.NAME).is(templateName));
		return this.mongoTemplate.count(query, TemplateEntity.class) > 0;
	}
	@Override
	public List<TemplateEntity> listTemplateMetadata() {
		
		return this.mongoTemplate.findAll(TemplateEntity.class);
	}
	@Override
	public List<TemplateEntity> getTemplateMetadata(Object... versionIds) {
		
		return this.mongoTemplate.find(new Query(Criteria.where(MongoFieldConstants.CE_VERSION_ID).in(versionIds)), TemplateEntity.class);
	}
	@Override
	public void deleteMetadataById(String templateId) {
		
		 this.mongoTemplate.remove(new Query(Criteria.where(MongoFieldConstants.ID).is(templateId)), TemplateEntity.class);
	}
}
