package com.dist.bdf.facade.service;

import java.util.List;

import com.dist.bdf.model.dto.system.RegionTreeDTO;
import com.dist.bdf.model.dto.system.eds.EdsResultDTO;

/**
 * eds（外部数据源）服务
 * @author weifj
 * @version 1.0，2016/04/11，weifj，创建服务接口
 *
 */
public interface EdsService {

	/**
	 *  提供经营类项目eds
	 * @param repositoryId 目标存储库标识
	 * @param propertiesJson 表单属性集合
	 * @return
	 */
    public EdsResultDTO getEdsDataJYXM(String repositoryId, String propertiesJson);
    /**
     * 提供团队项目eds
     * @param repositoryId
     * @param propertiesJson
     * @return
     */
    public EdsResultDTO getEdsDataTDXM(String repositoryId, String propertiesJson);
    /**
     * 提供应标意向申请eds
     * @param propertiesJson
     * @return
     */
    public EdsResultDTO getEdsDataYXSQ(String propertiesJson);
    /**
     * 获取资料一级维度
     * @return
     */
    public Object getMaterialDimensions(Long parentId);
    /**
     * 获取一级菜单数据
     * @param typeName
     * @return
     */
    public Object getSuperMaterialData(String typeName);
    /**
     * 刷新行政区域树缓存信息
     * @return 
     */
    List<RegionTreeDTO> refreshRegionTreeCache();
	public EdsResultDTO getEdsDataGZ(String repositoryId, String string);
	/**
	 * 提供给团队管理类型项目eds
	 * @param repositoryId
	 * @param propertiesJson
	 * @return
	 */
	EdsResultDTO getEdsDataTDGL(String repositoryId, String propertiesJson);
	/**
	 * 提供给合作类项目eds
	 * @param repositoryId
	 * @param propertiesJson
	 * @return
	 */
	EdsResultDTO getEdsDataHZXM(String repositoryId, String propertiesJson);
	
  
}
