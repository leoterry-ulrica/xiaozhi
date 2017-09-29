package com.dist.bdf.facade.service.biz.domain.cad;

import java.util.List;

import com.dist.bdf.model.entity.cad.TemplateEntity;

public interface CadTemplateDmn {

	/**
	 * 保存模板元数据
	 * @param entity
	 * @return 无
	 */
	TemplateEntity saveMetadata(TemplateEntity entity);
	/**
	 * 根据模板名称删除元数据
	 * @param templateName
	 * @return 返回删除条目数
	 */
	void deleteMetadataByName(String templateName);
	/**
	 * 根据模板名称获取实体
	 * @param templateName
	 * @return
	 */
	TemplateEntity getMetadataByName(String templateName);
	/**
	 * 是否已存在模板元数据
	 * @param templateName
	 * @return
	 */
	boolean isExistByName(String templateName);
	/**
	 * 获取所有模板元数据信息
	 */
	List<TemplateEntity> listTemplateMetadata();
	/**
	 * 根据模板id删除
	 * @param templateId
	 */
	void deleteMetadataById(String templateId);
	/**
	 * 根据版本系列id获取模板信息
	 * @param versionIds
	 * @return
	 */
	List<TemplateEntity> getTemplateMetadata(Object... versionIds);
}
