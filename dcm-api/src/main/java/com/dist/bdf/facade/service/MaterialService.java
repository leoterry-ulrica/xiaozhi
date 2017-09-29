package com.dist.bdf.facade.service;

import java.util.Date;
import java.util.List;

import com.dist.bdf.base.exception.BusinessException;
import com.dist.bdf.base.page.Pagination;
import com.dist.bdf.model.dto.dcm.DocumentDTO;
import com.dist.bdf.model.dto.dcm.PageParaDTO;
import com.dist.bdf.model.dto.system.MaterialMoveRequestDTO;
import com.dist.bdf.model.dto.system.MaterialParaDTO;
import com.dist.bdf.model.dto.system.MaterialSummaryDTO;
import com.dist.bdf.model.dto.system.NewFolderPropertyDTO;
import com.dist.bdf.model.dto.system.TaskMaterialDTO;
import com.dist.bdf.model.entity.system.DcmTaskMaterial;

/**
 * 资料服务
 * 
 * @author weifj
 *
 */
public interface MaterialService {

	/**
	 * 获取通用资料信息
	 * @param info
	 * @return
	 */
	Object getMaterialInfo(MaterialParaDTO info);

	/**
	 *  根据不同的空间域编码，获取资料信息
	 * @param domainTypeCode
	 * @param json
	 * @return
	 */
	Object getMaterialInfo(String domainTypeCode,String json);
	/**
	 * 上传文件
	 * @param userId
	 * @param password
	 * @param parentFolderId
	 * @param propertiesEx
	 * @param files
	 * @return
	 */
	//Object uploadFiles(String userId, String password, String parentFolderId,  PropertiesExDTO propertiesEx, List<M2ultipartFileDTO> files);
	/**
	 * 用户创建文件夹
	 * @param userId
	 * @param password
	 * @param type 类型（个人资料-1，项目资料-2，所级资料-3，院级资料-4）
	 * @param parentFolderId
	 * @param folderName
	 * @return
	 */
	Object createFolder(String userId, String password, String parentFolderId, String folderName) throws BusinessException;
	/**
	 * 创建文件夹
	 * @param property 属性DTO
	 * @return
	 */
	Object createFolder(NewFolderPropertyDTO property);

	/**
	 * 通用的服务接口，获取文件夹下的子文件夹和文档
	 * @param folderId 文件夹标识符
	 * @return
	 */
	@Deprecated
	Object getFoldersAndDocuments(String folderId);
	/**
	 * 通用的服务接口，获取文件夹下的子文件夹和文档
	 * @param realm 域
	 * @param folderId
	 * @return
	 */
	Object getFoldersAndDocuments(String realm, String folderId);
	/**
	 * 通用的服务接口，获取文件夹下的子文件夹和文档
	 * @param realm 机构域
	 * @param userId 用户id
	 * @param pwd 用户密码
	 * @param folderId 文件夹id
	 * @return
	 */
	Object getFoldersAndDocuments(String realm, String userId, String pwd, String folderId);
	
	/**
	 * 删除文件夹
	 * @param userId
	 * @param password
	 * @param folderId
	 * @return
	 */
	@Deprecated
	Object deleteFolderById(String userId, String password, String folderId);
	/**
	 * 删除文件夹
	 * @param realm
	 * @param userId
	 * @param password
	 * @param folderId
	 * @return
	 */
	Object deleteFolderById(String realm, String userId, String password, String folderId);
	/**
	 * 删除文件夹并返回文档的系列id集合和id集合
	 * @param realm
	 * @param userId
	 * @param password
	 * @param folderId
	 * @return
	 */
	Object deleteFolderByIdAndReturnIds(String realm, String userId, String password, String folderId);
	/**
	 * 更改文件夹，只能更新名字
	 * @param userId 用户id
	 * @param password 用户密码
	 * @param folderId 文件夹id
	 * @param newFolderName 新文件夹名称
	 * @return
	 */
	@Deprecated
	Object updateFolder(String userId, String password, String folderId, String newFolderName);
	/**
	 * 更改文件夹，只能更新名字
	 * @param realm 机构域
	 * @param userId 用户标识符
	 * @param password 用户密码
	 * @param folderId
	 * @param newFolderName
	 * @return
	 */
	@Deprecated
	Object updateFolder(String realm, String userId, String password, String folderId, String newFolderName);
	/**
	 * 重命名文件夹名称
	 * @param realm
	 * @param folderId
	 * @param newFolderName
	 * @return
	 */
	Object renameFolderName(String realm, String folderId, String newFolderName);
	/**
	 * 删除文件
	 * @param userId 用户id
	 * @param password
	 * @param docId 文件id
	 * @return
	 */
	@Deprecated
	Object deleteDoc(String userId, String password, String docId);
	/**
	 * 删除文件
	 * @param realm
	 * @param userId
	 * @param password
	 * @param docId
	 * @return
	 */
	Object deleteDoc(String realm, String userId, String password, String docId);
	/**
	 * 根据版本系列id删除文件
	 * @param userId
	 * @param password
	 * @param versionSeriesId
	 * @return
	 */
	Object deleteDocByVId(String userId, String password, String versionSeriesId);
	/**
	 * 根据版本系列id删除文件
	 * @param realm
	 * @param userId
	 * @param password
	 * @param versionSeriesId
	 * @return
	 */
	Object deleteDocByVId(String realm, String userId, String password, String versionSeriesId);
	

	/**
	 *  添加下载数
	 * @param documentId
	 * @return 返回最新的下载总数
	 */
	int addDownloadCountOfSummaryData(String documentId);
	/**
	 * 添加下载数
	 * @param realm
	 * @param documentId
	 * @return
	 */
	int addDownloadCountOfSummaryData(String realm, String documentId);
	/**
	 * 添加收藏数
	 * @param documentId
	 * @return 返回最新的收藏总数
	 */
	int addFavoriteCountOfSummaryData(String documentId, boolean isFavorite);
	/**
	 * 
	 * @param realm
	 * @param documentId
	 * @param isFavorite
	 * @return
	 */
	int addFavoriteCountOfSummaryData(String realm, String documentId, boolean isFavorite);
	/**
	 * 添加点赞数
	 * @param documentId
	 * @return 返回最新的点赞总数
	 */
	int addLikeCountOfSummaryData(String documentId, boolean isLike);

	/**
	 * 添加任务和材料的关联
	 * @param dto
	 */
	void addTaskMaterial(TaskMaterialDTO dto);
	
	/**
	 * 根据材料ID删除任务下的附件关联关系
	 * @param materialId
	 */
	void deleteTaskMaterialById(String materialId);
	/**
	 * 根据任务id获取材料列表
	 * @param taskId
	 * @return
	 */
	List<DcmTaskMaterial> getMaterialByTaskId(String taskId);
	/**
	 * 移动文件夹或者文件
	 * @param info
	 * @return
	 */
	Object doMove(MaterialMoveRequestDTO info);
	/**
	 * 指定父文件夹，分页检索子文件夹
	 * @param dto 属性：folderId、pageNo、realm
	 * @return
	 */
	Pagination getSubFoldersPage(PageParaDTO dto);
	
	/**
	 * 指定父文件夹，分页检索子文件
	 * @param dto 属性：folderId、pageNo、realm
	 * @return
	 */
	Pagination getSubDocsPage(PageParaDTO dto);
	/**
	 * 根据版本系列id，获取文档的所有版本
	 * @param realm
	 * @param versionSeriesId
	 * @return
	 */
	Object getDocVersionsByVId(String realm, String versionSeriesId);

	Object getMaterialInfoEx(String domainTypeCode, String json);
	/**
	 * 根据id获取文件信息，包括文件是否被收藏，文件的版本系列号等等
	 * @param realm 域
	 * @param docId 文档id
	 * @param userId 用户id
	 * @return
	 */
	DocumentDTO getDocInfoById(String realm, String docId, String userId);
	/**
	 * 管理员身份，根据文档唯一id，删除文档
	 * @param realm 域
	 * @param docId 文档唯一id
	 * @return
	 */
	Object deleteDocAdmin(String realm, String docId);
	/**
	 * 管理员身份，根据文档唯一id，删除文档
	 * @param realm 域
	 * @param versionSeriesId 文档版本系列id
	 * @return
	 */
	Object deleteDocByVIdAdmin(String realm, String versionSeriesId);
	/**
	 * 添加点赞数
	 * @param realm
	 * @param guid
	 * @param isTop
	 * @return
	 */
	int addLikeCountOfSummaryData(String realm, String guid, boolean isTop);
	/**
	 * 重命名文档
	 * @param realm
	 * @param docId
	 * @param newName
	 * @return
	 */
	Boolean renameDocName(String realm, String docId, String newName);
	/**
	 * 根据版本系列id获取文档信息
	 * @param realm
	 * @param vid
	 * @param userId
	 * @return
	 */
	DocumentDTO getDocInfoByVId(String realm, String vid, String userId);
	/**
	 * 根据版本系列id和objectStore名称获取文档信息，并带有缩略图
	 * @param objectStoreName 存储库唯一标识
	 * @param vid
	 * @param userId
	 * @return
	 */
	DocumentDTO getDocInfoByVIdOs(String objectStoreName, String vid, String userId);
	/**
	 * 根据域获取资料汇总信息
	 * @param realm
	 * @return
	 */
	MaterialSummaryDTO getMaterialSummaryByRealm(String realm);
	/**
	 * 获取租户的基础资料（部门资料和院级资料）统计信息，包括数量和大小
	 * @param realm 域
	 * @param beginTime 开始时间
	 * @param endTime 结束时间
	 * @return
	 */
	Object getBasicMaterialStat(String realm, Date beginTime, Date endTime);
}
