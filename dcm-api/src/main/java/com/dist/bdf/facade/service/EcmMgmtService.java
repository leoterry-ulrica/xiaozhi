
package com.dist.bdf.facade.service;

import java.util.List;
import java.util.Map;

import com.dist.bdf.base.exception.BusinessException;
import com.dist.bdf.base.page.Pagination;
import com.dist.bdf.model.dto.dcm.FileContentDTO;
import com.dist.bdf.model.dto.dcm.PageParaDTO;
import com.dist.bdf.model.dto.dcm.TeamSpaceDTO;


/**
 * 添加ecm服务，涵盖case、content、teamspace等等
 * @author weifj
 * @version 1.0，2016/03/22，weifj，创建案例管理服务
 * @version 1.1，2016/04/07，weifj，修改服务类型：EcmMgmtService
 */
public interface EcmMgmtService {


	/**
	 * 创建teamspace
	 * @param userName
	 * @param userPwd
	 * @param tsTemplateDTO
	 * @return
	 */
	String createTeamspace(String userName, String userPwd, TeamSpaceDTO tsTemplateDTO);
	/**
	 * 获取所有团队空间模板
	 * @return
	 */
   List<TeamSpaceDTO> getAllTeamspaceTemplate();
   /**
    * 根据关键字，全文搜索文档类，不包括文件夹
    * @param keyword
    * @return
    */
   //Object fullTextSearchOfDocument(String keyword);
   //Object fullTextSearchOfDocument(int pageNo, int pageSize,String keyword);
   //Object fullTextSearchOfDocument(PageParaDTO pageInfo);
   /**
    * 云版本分页检索文档
    * @param pageInfo
    * @return
    */
   Object fullTextSearchOfDocumentCloud(PageParaDTO pageInfo);
   /**
    * 按照匹配度排序分页
    * @param pageInfo
    * @return
    */
   Pagination fullTextSearchOfDocumentRank(PageParaDTO pageInfo);
   /**
    * 精确查询文种的项目资料
    * @param value 文种，如：招投标资料、合同资料、调研资料等等
    * @return
    */
   Object preciseQuery(PageParaDTO pageInfo);
   //Object preciseQueryByFileType(String realm, String value);
   //Object preciseQueryByFileType(String realm, int pageNo, int pageSize,String value);
   Object preciseQueryByFileType(PageParaDTO pageInfo);
   /**
    * 精确查询所属区域的项目资料
    * @param value
    * @return
    */
   //Object preciseQueryByRegion(String value);
   //Object preciseQueryByRegion(int pageNo, int pageSize,String value);
   Object preciseQueryByRegion(PageParaDTO pageInfo);
   
   /**
    * 精确查询所属组织的项目资料
    * @param value
    * @return
    */
   //Object preciseQueryByOrganization(String value);
   //Object preciseQueryByOrganization(int pageNo, int pageSize, String value);
   Object preciseQueryByOrganization(PageParaDTO pageInfo);
   /**
    * 精确查询业务类型的项目资料
    * @param value
    * @return
    */
   //Object preciseQueryByBusiness(String value);
   //Pagination preciseQueryByBusiness(int pageNo, int pageSize,String value);
   Pagination preciseQueryByBusiness(PageParaDTO pageInfo);
  
   /**
    * 获取文档内容二进制流
    * @param docId
    * @return
    */
 /*  @Deprecated
   Object getDocContentStream(String docId);*/
   byte[] getDocContentStream(String realm, String docId)  throws BusinessException;
   /**
    * 获取文档内容二进制流
    * @param realm
    * @param userId
    * @param pwd
    * @param docId
    * @return
    */
   Object getDocContentStream(String realm, String userId, String pwd, String docId)  throws BusinessException;
   /**
    * 用于断点下载
    * @param docId
    * @param position 起始下载下标索引
    * @return
    */
   byte[] getDocContentStreamBreakpoint(String docId, Long position)  throws BusinessException;
   /**
    * 断点下载
    * @param docId
    * @param pos 开始读取位置
    * @return
    */
   FileContentDTO getDocContentBreakpoint(String docId, Long pos);

   /**
    * 根据案例类型名称获取下面的环节
    * @param name
    * @return
    */
   Object getTachesByCaseType(String name);
   /**
    * 根据文档的版本系列id数组获取图片数据流数组
    * @param realm
    * @param vid
    * @return
    */
   Map<String, byte[]> getImgContentStreamByVID(String realm, List<String> vids);
  

}
