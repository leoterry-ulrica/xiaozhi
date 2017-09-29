package com.dist.bdf.facade.service;

import com.dist.bdf.model.dto.system.DownloadParaDTO;
import com.dist.bdf.model.dto.system.FavoriteParaDTO;
import com.dist.bdf.model.dto.system.page.PageSimple;

/**
 * 
 * @author weifj
 * @version 1.0，2016/04/27，weifj，创建个人资料服务
 */
public interface PersonalMaterialService {

	/**
	 * 添加收藏
	 * @param dto
	 * @return
	 */
	Object saveFavorite(FavoriteParaDTO dto);
	/**
	 * 添加收藏
	 * @param dto
	 * @return
	 */
	Object saveFavoriteEx(FavoriteParaDTO dto);
	/**
	 * 根据资源id删除资料
	 * @param jsonString
	 * @return
	 */
	Object deleteMaterial(String jsonString);
	/**
	 * 获取个人收藏夹
	 * @param user 用户登录名
	 * @return
	 */
	@Deprecated
	Object getFavorite(String json);
	Object getFavoriteEx(PageSimple pageInfo);
	/**
	 * 获取下载日志
	 * @param json
	 * @return
	 */
	Object getDownloadLogs(PageSimple pageInfo); 
	/**
	 * 删除个人下载记录
	 * @param resIds
	 * @return
	 */
	Object delDownloadLogsByResIds(DownloadParaDTO dto);
	/**
	 * 获取个人包
	 * @param realm 机构域
	 * @param userId 登录名
	 * @return
	 */
	@Deprecated
	Object getPersonalPackages(String realm, String userId, String pwd);
	
	Object getPersonalPackages(String realm, String userId);
	
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

}
