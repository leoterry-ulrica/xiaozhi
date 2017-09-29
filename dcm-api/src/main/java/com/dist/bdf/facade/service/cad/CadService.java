package com.dist.bdf.facade.service.cad;

import com.dist.bdf.base.page.Pagination;
import com.dist.bdf.model.dto.system.page.PageSimple;
import com.dist.bdf.model.entity.cad.TemplateEntity;
import java.util.List;

/**
 * cad相关服务
 * @author weifj
 *
 */
public interface CadService {

	/**
	 * 新增或更新模板元数据
	 * @param entity
	 * @return
	 */
	TemplateEntity saveOrUpdateTemplateMetadata(TemplateEntity entity);
	/**
	 * 获取所有模板元数据信息，XML格式
	 * @return
	 */
	String listTemplateMetadataXML();
	/**
	 * 获取模板元数据信息
	 * @return
	 */
	List<TemplateEntity> listTemplateMetadata();
	/**
	 * 删除模板数据，包括元数据和文件数据
	 * @param realm
	 * @param templateId
	 * @param docId
	 * @return
	 */
	Object deleteTemplate(String realm, String templateId, String docId);
	/**
	 * 
	 * @param realm
	 * @param templateId
	 * @param docId
	 * @param docVId
	 * @return
	 */
	Object deleteTemplate(String realm, String templateId, String docId, String docVId);
	/**
	 * 获取个人收藏的cad模板
	 * @param page
	 * @return
	 */
	Object getFavoriteOfCadTemplate(PageSimple page);
	/**
	 * 分页获取cad模板文件信息
	 * @param realm
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	Pagination listTemplates(String realm, int pageNo, int pageSize);
	/**
	 * 获取所有cad模板，带有收藏标识
	 * @param realm
	 * @param userCode
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	Pagination listTemplates(String realm, String userCode, int pageNo, int pageSize);
	/**
	 * 提供给报建通，获取个人收藏的cad模板信息
	 * @param userCode 用户编码
	 * @return
	 */
	String getFavoriteOfCadTemplateForBJT(String userCode);
}
