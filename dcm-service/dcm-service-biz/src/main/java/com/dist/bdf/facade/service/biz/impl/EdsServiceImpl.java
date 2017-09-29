package com.dist.bdf.facade.service.biz.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dist.bdf.base.exception.BusinessException;
import com.dist.bdf.base.utils.DateUtil;
import com.dist.bdf.base.utils.DistThreadManager;
import com.dist.bdf.base.utils.StringUtil;
import com.dist.bdf.facade.service.EdsService;
import com.dist.bdf.facade.service.UserOrgService;
import com.dist.bdf.facade.service.biz.domain.system.DcmMaterialDimensionDmn;
import com.dist.bdf.facade.service.biz.domain.system.DcmRegionDmn;
import com.dist.bdf.manager.cache.CacheStrategy;
import com.dist.bdf.common.conf.common.GlobalConf;
import com.dist.bdf.common.conf.ecm.ECMConf;
import com.dist.bdf.common.conf.ecm.ExtPropertyCaseConf;
import com.dist.bdf.common.constants.CacheKey;
import com.dist.bdf.common.constants.GlobalSystemParameters;
/*import com.dist.bdf.facade.service.cache.DistributedCacheService;*/
import com.dist.bdf.model.dto.system.MaterialDimensionDTO;
import com.dist.bdf.model.dto.system.RegionTreeDTO;
import com.dist.bdf.model.dto.system.eds.EdsResultDTO;
import com.dist.bdf.model.dto.system.eds.Property;
import com.dist.bdf.model.entity.system.DcmMaterialDimension;
import com.dist.bdf.model.entity.system.DcmOrganization;
import com.dist.bdf.model.entity.system.DcmUser;

import io.github.xdiamond.client.XDiamondConfig;

/**
 * eds服务
 * 对于下拉多值属性，只需要在case builder中设置属性控件为多值即可，数据源类型为Map
 * @author weifj
 *
 */
@Service("EdsService")
@Transactional(propagation = Propagation.REQUIRED)
public class EdsServiceImpl implements EdsService {

	//private volatile static List<RegionTreeDTO> CacheRegionTree = new LinkedList<RegionTreeDTO>();

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private DcmRegionDmn regionDmn;
	@Autowired
	private DcmMaterialDimensionDmn mdDmn;
	@Autowired
	private UserOrgService userService;
/*	@Autowired
	private DistributedCacheService disCacheService;*/
	@Autowired
	@Qualifier("ExtPropertyCaseConf")
	private ExtPropertyCaseConf extPropCaseConf;
	@Autowired
	private XDiamondConfig xconf;
	@Autowired
	private GlobalConf globalConf;
	@Autowired
	private ECMConf ecmConf;
	@Autowired
	private CacheStrategy redisCache;
	
	/**
	 * 根据目标存储库标识反向查找院的名称
	 * @param repositoryId
	 * @return
	 */
	private String getInstituteName(String repositoryId){
		
		String instituteName = this.xconf.getProperty("repositoryId."+repositoryId);
		if(StringUtil.isNullOrEmpty(instituteName)){
			
			throw new BusinessException("找不到[{0}]对应的院名称配置信息......", repositoryId);
		}
		return instituteName;
	}

	@SuppressWarnings("unchecked")
	@Override
	@PostConstruct
	public synchronized List<RegionTreeDTO> refreshRegionTreeCache() {

		Date start = new Date();
		logger.info(">>>正在初始化行政区域信息，开始时间：{}", DateUtil.toDateTimeStr(start));
		
		List<RegionTreeDTO> cacheRegionTree = this.globalConf.openCache()? (List<RegionTreeDTO>) this.redisCache.getList(CacheKey.PREFIX_REGION_TREE + GlobalSystemParameters.SERIALIZABLE_REGION_GUID):null;
		if(cacheRegionTree != null && !cacheRegionTree.isEmpty()){
			logger.info(">>>在缓存中已找到区域的信息......");
			return cacheRegionTree; // 啥时候进行刷新
		}
		
		String basePath = (new File("").getAbsolutePath());
		logger.info(">>>目前工作目录：" + basePath);
		String serializablePath = new StringBuilder().append(basePath).append(File.separatorChar).append(GlobalSystemParameters.SERIALIZABLE_PATH).toString();
        File seriDir = new File(serializablePath);
		if(!seriDir.exists()){
			seriDir.mkdirs();
		}
		File seriFile = new File(new StringBuilder().append(seriDir.getAbsolutePath()).append(File.separatorChar).append(GlobalSystemParameters.SERIALIZABLE_REGION_GUID).toString());
		if(seriFile.exists()){
			logger.info(">>>找到本地序列化文件：[{}]", seriFile.getAbsolutePath());
			ObjectInputStream in = null;
			try{
			in = new ObjectInputStream(new FileInputStream(seriFile.getAbsolutePath()));
			cacheRegionTree = (List<RegionTreeDTO>) in.readObject();
			if(cacheRegionTree != null && this.globalConf.openCache()){
				boolean isSucceed = (boolean) this.redisCache.setList(CacheKey.PREFIX_REGION_TREE + GlobalSystemParameters.SERIALIZABLE_REGION_GUID, cacheRegionTree);
				logger.info(">>>缓存行政区域数据状态："+isSucceed);
				return cacheRegionTree;
			}
			}catch(Exception ex){
				//ex.printStackTrace();
				logger.error(">>>反序列化文件失败，信息：[{}]", ex.getMessage());
			
			} finally {
				if(in != null){
					try {
						in.close();
					} catch (IOException e) {
						logger.error(">>>关闭输入文件流失败，信息：[{}]", e.getMessage());
					}
				}
			}
		}
        
		final List<RegionTreeDTO> newRegionTree = new LinkedList<RegionTreeDTO>();
		List<String> provinces = this.regionDmn.getProvinces();

		for (String p : provinces) {
			RegionTreeDTO provMd = new RegionTreeDTO();
			provMd.setName(p);
			List<RegionTreeDTO> citiesMd = new LinkedList<RegionTreeDTO>();
			List<String> cities = this.regionDmn.getCities(p);
			for (String c : cities) {
				RegionTreeDTO cityMd = new RegionTreeDTO();
				cityMd.setName(c);
				List<RegionTreeDTO> countiesMd = new LinkedList<RegionTreeDTO>();
				List<String> counties = this.regionDmn.getCounties(p, c);
				for (String ct : counties) {
					RegionTreeDTO countyMd = new RegionTreeDTO();
					countyMd.setName(ct);
					countiesMd.add(countyMd);
				}
				cityMd.setChildren(countiesMd);
				citiesMd.add(cityMd);

			}
			provMd.setChildren(citiesMd);
			newRegionTree.add(provMd);
		}

		if(this.globalConf.openCache())
		   this.redisCache.set(CacheKey.PREFIX_REGION_TREE + GlobalSystemParameters.SERIALIZABLE_REGION_GUID,newRegionTree);
		logger.info(">>>异步持久化行政区域信息到本地......");
		asyncSeriLocalFile(seriFile, newRegionTree);
		
		Date end = new Date();
		logger.info("----------完成初始化行政区域信息，结束时间：{}，共花费：{} 毫秒", DateUtil.toDateTimeStr(end),
				end.getTime() - start.getTime());
		
		return newRegionTree;
	}

	/**
	 * 序列化到本地文件
	 * @param seriFile
	 * @param newRegionTree
	 */
	private void asyncSeriLocalFile(final File seriFile, final List<RegionTreeDTO> newRegionTree) {
		
		DistThreadManager.MyCacheThreadPool.execute(new Runnable() {
			
			@Override
			public void run() {
				ObjectOutputStream out = null;
				try{
				  out = new ObjectOutputStream(new FileOutputStream(seriFile.getAbsolutePath()));
			      out.writeObject(newRegionTree); 
			      
				}catch(Exception ex){
					//ex.printStackTrace();
					logger.error(">>>序列化文件失败，信息：[{}]", ex.getMessage());
				} finally {
					if(out != null){
						try {
							out.close();
						} catch (IOException e) {
							logger.error(">>>关闭输出文件流失败，信息：[{}]", e.getMessage());
						}
					}
				}
			}
		});
		
	}


	private Map<String, String> getProvinceMapData() {

		List<String> provinces = this.regionDmn.getProvinces();
		Map<String, String> provinceMapData = new LinkedHashMap<String, String>();

		for (String pro : provinces) {
			provinceMapData.put(pro, pro);
		}
		return provinceMapData;
	}

	private Map<String, String> getCityMapData(String province) {

		Map<String, String> cityMapData = new LinkedHashMap<String, String>();
		List<String> cities = this.regionDmn.getCities(province);
		for (String ct : cities) {
			cityMapData.put(ct, ct);
		}
		return cityMapData;
	}

	private Map<String, String> getCountyMapData(String province, String city) {

		Map<String, String> countyMapData = new LinkedHashMap<String, String>();
		List<String> counties = this.regionDmn.getCounties(province, city);
		for (String cty : counties) {
			countyMapData.put(cty, cty);
		}
		return countyMapData;
	}

	/**
	 * 获取属性的key-value对象
	 * 
	 * @param properties
	 * @return
	 */
	private Map<String, String> getKeyMapData(JSONArray properties) {

		Map<String, String> map = new HashMap<String, String>();

		for (int i = 0; i < properties.size(); i++) {
			JSONObject o = properties.getJSONObject(i);

			map.put(o.getString("symbolicName"), o.getString("value"));
		}

		return map;
	}

	/**
	 * 获取机构下用户
	 * 
	 * @param departmentId
	 * @return
	 */
	private Map<String, String> getUsersOfOrg(String departmentId) {

		List<DcmUser> users = this.userService.getUsersByOrgCode(departmentId);
		if (users != null && !users.isEmpty()) {
			Map<String, String> usersOfOrg = new LinkedHashMap<String, String>();
			for (DcmUser u : users) {

				usersOfOrg.put(u.getLoginName(), u.getUserName());
			}
			return usersOfOrg;
		}
		return null;
	}

	/**
	 * 获取部门map对象
	 * 
	 * @return
	 */
	private Map<String, String> getDepartmentMapData() {

		List<DcmOrganization> orgs = this.userService.getAllDepartment();
		if (orgs != null && !orgs.isEmpty()) {
			Map<String, String> data = new LinkedHashMap<String, String>();
			for (DcmOrganization o : orgs) {
				data.put(o.getOrgCode(), o.getAlias());
			}
			return data;
		}
		return null;
	}

	/**
	 * UsersChoiceList，自定义名称，唯一性即可，不能与ECM构建的属性相同 for (int i = 0; i <
	 * properties.size(); i++) { JSONObject o = properties.getJSONObject(i); if
	 * (o.get("symbolicName").equals("XMXX_XMQY_SHENG")) { province =
	 * o.get("value").toString(); logger.info("--->eds，找到省，value：[{}]",
	 * province); } else if (o.get("symbolicName").equals("XMXX_XMQY_SHI")) {
	 * city = o.get("value").toString(); logger.info("--->eds，找到市，value：[{}]",
	 * city); } else if (o.get("symbolicName").equals("XMXX_XMQY_XIAN")) {
	 * county = o.get("value").toString(); logger.info("--->eds，找到县，value：[{}]",
	 * county); } else if (o.get("symbolicName").equals("XMXX_RY_XMFZR")) {
	 * xmfzr = o.get("value").toString();
	 * logger.info("--->eds，找到项目负责人，value：[{}]", xmfzr); } else if
	 * (o.get("symbolicName").equals("XMXX_RY_XMTBFZR")) { xmtbfzr =
	 * o.getString("value"); logger.info("--->eds，找到项目投标负责人，value：[{}]",
	 * xmtbfzr); } else if (o.get("symbolicName").equals("XMXX_XMQTBM")) { qtbm
	 * = o.getString("value"); logger.info("--->eds，找到牵头部门，value：[{}]", qtbm); }
	 * }
	 */

	/**
	 * 牵头部门
	 * 
	 * @param keyMapData
	 */
	private void setEdsTopDepartment(EdsResultDTO eds, Map<String, String> orgMapData, Map<String, String> keyMapData) {

		//String qtbm = keyMapData.containsKey("XMXX_XMQTBM") ? keyMapData.get("XMXX_XMQTBM") : "";
		String qtbm = keyMapData.containsKey(this.extPropCaseConf.getQtbm()) ? keyMapData.get(this.extPropCaseConf.getQtbm()) : "";
		logger.info(">>>eds，找到牵头部门，value：[{}]", qtbm);
		eds.addProperty(new Property(this.extPropCaseConf.getQtbm(), false, true, false, "QTOrgChoiceList", orgMapData, qtbm));
	}

	/**
	 * 配合部门
	 * 
	 * @param keyMapData
	 */
	private void setEdsPHDepartment(EdsResultDTO eds, Map<String, String> orgMapData, Map<String, String> keyMapData) {

		//String qtbm = keyMapData.containsKey("XMXX_XMQTBM") ? keyMapData.get("XMXX_XMQTBM") : "";
		String phbm = keyMapData.containsKey(this.extPropCaseConf.getPhbm()) ? keyMapData.get(this.extPropCaseConf.getPhbm()) : "";
		logger.info(">>>eds，找到配合部门，value：[{}]", phbm);
		eds.addProperty(new Property(this.extPropCaseConf.getPhbm(), false, false, false, "PHOrgChoiceList", orgMapData, phbm));
	}
	/**
	 * 其它配合部门
	 * 
	 * @param keyMapData
	 */
	private void setEdsQTPHDepartment(EdsResultDTO eds, Map<String, String> orgMapData, Map<String, String> keyMapData) {

		if(!keyMapData.containsKey(this.extPropCaseConf.getQtphbm())){
			return;
		}
		//String qtbm = keyMapData.containsKey("XMXX_XMQTBM") ? keyMapData.get("XMXX_XMQTBM") : "";
		String phbm = keyMapData.containsKey(this.extPropCaseConf.getQtphbm()) ? keyMapData.get(this.extPropCaseConf.getQtphbm()) : "";
		logger.info(">>>eds，找到其它配合部门，value：[{}]", phbm);
		eds.addProperty(new Property(this.extPropCaseConf.getQtphbm(), false, false, false, "QTPHOrgChoiceList", orgMapData, phbm));
	}
	/**
	 * 应标部门
	 * 
	 * @param keyMapData
	 */
	@Deprecated
	private void setEdsYBDepartment(EdsResultDTO eds, Map<String, String> orgMapData, Map<String, String> keyMapData) {

		String ybbm = keyMapData.containsKey("XMXX_TB_YBBM") ? keyMapData.get("XMXX_TB_YBBM") : "";
		logger.info(">>>eds，找到应标部门，value：[{}]", ybbm);
		if (StringUtils.hasLength(ybbm)) {
			
			eds.addProperty(new Property("XMXX_TB_YBBM", false, true, false, "YBOrgChoiceList", orgMapData, ybbm));
		}
		
	}

	/**
	 * 项目投标负责人
	 * 
	 * @param eds
	 * @param userMapData
	 * @param keyMapData
	 */
	@Deprecated
	private void setEdsTBProjectManager(EdsResultDTO eds, Map<String, String> userMapData,
			Map<String, String> keyMapData) {
		String xmtbfzr = keyMapData.containsKey("XMXX_RY_XMTBFZR") ? keyMapData.get("XMXX_RY_XMTBFZR") : "";
		logger.info("--->eds，找到项目投标负责人，value：[{}]", xmtbfzr);
		if (StringUtils.hasLength(xmtbfzr)) {
			// 项目投标负责人
			eds.addProperty(new Property("XMXX_RY_XMTBFZR", false, true, false, "UsersChoiceList", userMapData, xmtbfzr));
		}
		
	}

	/**
	 * 项目负责人
	 * 
	 * @param eds
	 * @param userMapData
	 * @param keyMapData
	 */
	private void setEdsProjectManager(EdsResultDTO eds, Map<String, String> userMapData,
			Map<String, String> keyMapData) {
		
		//String xmfzr = keyMapData.containsKey("XMXX_RY_XMFZR") ? keyMapData.get("XMXX_RY_XMFZR") : "";
		String xmfzr = keyMapData.containsKey(this.extPropCaseConf.getProjectLeader()) ? keyMapData.get(this.extPropCaseConf.getProjectLeader()) : "";
		logger.info(">>>eds，找到项目负责人，value：[{}]", xmfzr);
		
		Map<String, String> userMapDataNew = userMapData;
		if(keyMapData.containsKey(this.extPropCaseConf.getProjectAssistant())){
			String xmzl = keyMapData.get(this.extPropCaseConf.getProjectAssistant());
			if(StringUtils.hasLength(xmzl) && !xmzl.equalsIgnoreCase("null")){
				userMapDataNew = new HashMap<String, String>();
				userMapDataNew.putAll(userMapData);
				userMapDataNew.remove(xmzl);
			}
		}
		
		// 项目负责人
		eds.addProperty(new Property(this.extPropCaseConf.getProjectLeader(), false, true, true, "UsersChoiceListPM", userMapDataNew, xmfzr));
		
	}
	/**
	 * 项目助理（非必填）
	 * @param eds
	 * @param userMapData
	 * @param keyMapData
	 */
	private void setEdsProjectAssistant(EdsResultDTO eds, Map<String, String> userMapData,
			Map<String, String> keyMapData) {

		if(!keyMapData.containsKey(this.extPropCaseConf.getProjectAssistant())){
			return;
		}
		String xmzl = keyMapData.get(this.extPropCaseConf.getProjectAssistant());
		logger.info(">>>eds，找到项目助理，value：[{}]", xmzl);
		
		Map<String, String> userMapDataNew = userMapData;
		String xmfzr = keyMapData.get(this.extPropCaseConf.getProjectLeader());
		if(StringUtils.hasLength(xmfzr) && !xmfzr.equalsIgnoreCase("null")){
			userMapDataNew = new HashMap<String, String>();
			userMapDataNew.putAll(userMapData);
			userMapDataNew.remove(xmfzr);
		}
		
		// 项目助理
		eds.addProperty(new Property(this.extPropCaseConf.getProjectAssistant(), false, false, true,
							"UsersChoiceListPA", userMapDataNew, xmzl));
		/*if (StringUtils.hasLength(xmzl)) {
			
		}*/
	}

	/**
	 * 县
	 * 
	 * @param eds
	 * @param keyMapData
	 * @param province
	 * @param city
	 */
	private void setEdsCounty(EdsResultDTO eds, Map<String, String> keyMapData, String province, String city) {
		
		//String county = keyMapData.containsKey("XMXX_XMQY_XIAN") ? keyMapData.get("XMXX_XMQY_XIAN") : "";
		String county = keyMapData.containsKey(extPropCaseConf.getCounty()) ? keyMapData.get(extPropCaseConf.getCounty()) : "";
		logger.info(">>>eds，找到县，value：[{}]", county);
		if (!StringUtil.isNullOrEmpty(city) && !city.equalsIgnoreCase("null")) {
			Map<String, String> countyMapData = getCountyMapData(province, city);
			eds.addProperty(
					new Property(extPropCaseConf.getCounty(), false, false, false, "CountyChoiceList", countyMapData, county));
		} else {
			eds.addProperty(new Property(extPropCaseConf.getCounty(), false, false, false, "CountyChoiceList", null, null));
		}
	}

	/**
	 * 市
	 * 
	 * @param eds
	 * @param keyMapData
	 * @param province
	 * @return
	 */
	private String setEdsCity(EdsResultDTO eds, Map<String, String> keyMapData, String province) {
		
		//String city = keyMapData.containsKey("XMXX_XMQY_SHI") ? keyMapData.get("XMXX_XMQY_SHI") : "";
		String city = keyMapData.containsKey(extPropCaseConf.getCity()) ? keyMapData.get(extPropCaseConf.getCity()) : "";
		logger.info(">>>eds，找到市，value：[{}]", city);
		if (!StringUtil.isNullOrEmpty(province) && !province.equalsIgnoreCase("null")) {
			Map<String, String> cityMapData = getCityMapData(province);
			eds.addProperty(new Property(extPropCaseConf.getCity(), false, false, true, "CityChoiceList", cityMapData, city));
		} else {
			eds.addProperty(new Property(extPropCaseConf.getCity(), false, false, true, "CityChoiceList", null, null));
		}
		return city;
	}

	/**
	 * 省
	 * 
	 * @param eds
	 * @param keyMapData
	 * @return
	 */
	private String setEdsProvince(EdsResultDTO eds, Map<String, String> keyMapData) {
		
		//String province = keyMapData.containsKey("XMXX_XMQY_SHENG") ? keyMapData.get("XMXX_XMQY_SHENG") : "";
		String province = keyMapData.containsKey(extPropCaseConf.getProvince()) ? keyMapData.get(extPropCaseConf.getProvince()) : "";
		logger.info(">>>eds，找到省，value：[{}]", province);
		Map<String, String> provinceMapData = getProvinceMapData();
		eds.addProperty(
				new Property(extPropCaseConf.getProvince(), false, false, true, "ProvinceChoiceList", provinceMapData, province));
		return province;
	}
	/**
	 * 设置业务类别（非必填）
	 * @param eds
	 * @param keyMapData
	 * @param realm 域，如thupdi，即desktop值
	 */
	private void setBusinessType(EdsResultDTO eds, Map<String, String> keyMapData, String realm) {

		if(!keyMapData.containsKey(extPropCaseConf.getBusinessType())){
			return;
		}
		String businessType = keyMapData.get(extPropCaseConf.getBusinessType());
		logger.info(">>>eds，找到业务类别，value：[{}]", businessType);

		Map<String, String> businessTypeMap = null;
		String businessTypeDic = this.ecmConf.getBusinessType(realm);
		if (StringUtils.hasLength(businessTypeDic)) {
			String[] strs = businessTypeDic.split(";");
			businessTypeMap = new LinkedHashMap<String, String>();
			for (String s : strs) {
				if (!StringUtils.hasLength(s)) {
					continue;
				}
				businessTypeMap.put(s, s);
			}
		}
		eds.addProperty(new Property(extPropCaseConf.getBusinessType(), false, false, false, "BusinessTypeChoiceList",
				businessTypeMap, businessType));
	}
	@Override
	public EdsResultDTO getEdsDataJYXM(String repositoryId, String propertiesJson) {

		EdsResultDTO eds = new EdsResultDTO();

		String uniqueName = getInstituteName(repositoryId);
		// 获取用户的key-value数据
		Map<String, String> userMapData = getUserMapData(uniqueName);
		// 获取部门组织key-value数据
		Map<String, String> orgMapData = getAllOrgMapData(uniqueName);

		JSONArray properties = JSONArray.parseArray(propertiesJson);
		Map<String, String> keyMapData = getKeyMapData(properties);

		// 省
		String province = setEdsProvince(eds, keyMapData);
		// 市
		String city = setEdsCity(eds, keyMapData, province);
		// 县
		setEdsCounty(eds, keyMapData, province, city);

		// 项目负责人
		this.setEdsProjectManager(eds, userMapData, keyMapData);
		// 项目投标负责人
		//setEdsTBProjectManager(eds, userMapData, keyMapData);
		// 牵头部门
		this.setEdsTopDepartment(eds, orgMapData, keyMapData);
		// 应标部门
		//setEdsYBDepartment(eds, orgMapData, keyMapData);
		// 配合部门
		this.setEdsPHDepartment(eds, orgMapData, keyMapData);
		// 其它配合部门
		this.setEdsQTPHDepartment(eds, orgMapData, keyMapData);
		// 项目助理
		this.setEdsProjectAssistant(eds, userMapData, keyMapData);
		// 业务类别字典
		this.setBusinessType(eds, keyMapData, this.xconf.getProperty("repositoryId."+repositoryId));

		return eds;
	}
	@Override
	public EdsResultDTO getEdsDataTDXM(String repositoryId, String propertiesJson) {
		EdsResultDTO eds = new EdsResultDTO();

		String uniqueName = getInstituteName(repositoryId);
		// 获取用户的key-value数据
		Map<String, String> userMapData = getUserMapData(uniqueName);
		// 获取部门组织key-value数据
		Map<String, String> orgMapData = getAllOrgMapData(uniqueName);

		JSONArray properties = JSONArray.parseArray(propertiesJson);
		Map<String, String> keyMapData = getKeyMapData(properties);

		// 项目负责人
		this.setEdsProjectManager(eds, userMapData, keyMapData);
		// 牵头部门
		this.setEdsTopDepartment(eds, orgMapData, keyMapData);
		// 配合部门
		this.setEdsPHDepartment(eds, orgMapData, keyMapData);
		// 项目助理
		this.setEdsProjectAssistant(eds, userMapData, keyMapData);

		return eds;
	}
	@Override
	public EdsResultDTO getEdsDataHZXM(String repositoryId, String propertiesJson) {

		EdsResultDTO eds = new EdsResultDTO();

		String uniqueName = getInstituteName(repositoryId);
		// 获取用户的key-value数据
		Map<String, String> userMapData = getUserMapData(uniqueName);
		// 获取部门组织key-value数据
		Map<String, String> orgMapData = getAllOrgMapData(uniqueName);

		JSONArray properties = JSONArray.parseArray(propertiesJson);
		Map<String, String> keyMapData = getKeyMapData(properties);

		// 项目负责人
		this.setEdsProjectManager(eds, userMapData, keyMapData);
		// 项目助理
		this.setEdsProjectAssistant(eds, userMapData, keyMapData);
		// 牵头部门
		this.setEdsTopDepartment(eds, orgMapData, keyMapData);
		// 其它配合部门（代表着合作项目中的配合部门，来源于经营项目的其它配合部门）
		this.setEdsQTPHDepartment(eds, orgMapData, keyMapData);
		// 业务类别字典
		this.setBusinessType(eds, keyMapData, this.xconf.getProperty("repositoryId." + repositoryId));

		return eds;
	}
	@Override
	public EdsResultDTO getEdsDataTDGL(String repositoryId, String propertiesJson) {

		EdsResultDTO eds = new EdsResultDTO();

		String uniqueName = getInstituteName(repositoryId);
		// 获取用户的key-value数据
		Map<String, String> userMapData = getUserMapData(uniqueName);
		// 获取部门组织key-value数据
		Map<String, String> orgMapData = getAllOrgMapData(uniqueName);

		JSONArray properties = JSONArray.parseArray(propertiesJson);
		Map<String, String> keyMapData = getKeyMapData(properties);

		// 牵头部门
		this.setEdsTopDepartment(eds, orgMapData, keyMapData);
		// 配合部门
		this.setEdsPHDepartment(eds, orgMapData, keyMapData);
		// 项目负责人
		this.setEdsProjectManager(eds, userMapData, keyMapData);
		// 项目助理
		this.setEdsProjectAssistant(eds, userMapData, keyMapData);

		return eds;
	}

	@Override
	public EdsResultDTO getEdsDataGZ(String repositoryId, String propertiesJson) {

		EdsResultDTO eds = new EdsResultDTO();

		String uniqueName = getInstituteName(repositoryId);
		// 获取用户的key-value数据
		Map<String, String> userMapData = getUserMapDataGZ(uniqueName);
		// 获取部门组织key-value数据
		Map<String, String> orgMapData = getAllOrgMapData(uniqueName);

		JSONArray properties = JSONArray.parseArray(propertiesJson);
		Map<String, String> keyMapData = getKeyMapData(properties);

		// 省
		String province = setEdsProvince(eds, keyMapData);
		// 市
		String city = setEdsCity(eds, keyMapData, province);
		// 县
		setEdsCounty(eds, keyMapData, province, city);

		// 项目负责人
		this.setEdsProjectManager(eds, userMapData, keyMapData);
		// 项目助理
		this.setEdsProjectAssistant(eds, userMapData, keyMapData);
		// 项目投标负责人
		//setEdsTBProjectManager(eds, userMapData, keyMapData);
		// 牵头部门
		this.setEdsTopDepartment(eds, orgMapData, keyMapData);
		// 应标部门
		//setEdsYBDepartment(eds, orgMapData, keyMapData);
		// 配合部门
		this.setEdsPHDepartment(eds, orgMapData, keyMapData);

		return eds;
	}


	/**
	 * 获取所有机构（包括院和所）的key-value数据
	 * 
	 * @return
	 */
	private Map<String, String> getAllOrgMapData(String instituteUniqueName) {

		List<DcmOrganization> orgs = this.userService.getDepartmentsOfInstitute(instituteUniqueName);

		if (orgs != null && !orgs.isEmpty()) {
			Map<String, String> orgMap = new HashMap<String, String>();
			for (DcmOrganization o : orgs) {
				orgMap.put(o.getOrgCode(), o.getAlias());
			}
			return orgMap;
		}
		return null;
	}

	/**
	 * 获取用户map对象
	 * 
	 * @return
	 */
	private Map<String, String> getUserMapData(String instituteUniqueName) {

		Map<String, String> data = new LinkedHashMap<String, String>();

		try {
			List<DcmUser> users = this.userService.getUsersByInstituteName(instituteUniqueName);

			for (DcmUser u : users) {
			   data.put(u.getUserCode(), u.getUserName());
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return data;
	}
	
	private Map<String, String> getUserMapDataGZ(String instituteUniqueName) {

		Map<String, String> data = new LinkedHashMap<String, String>();

		try {
			List<DcmUser> users = this.userService.getUsersByInstituteName(instituteUniqueName);

			for (DcmUser u : users) {
			   data.put(u.getLoginName(), u.getUserName());
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return data;
	}
	/**
	 * 
	 * for (int i=0;i<properties.size();i++){ JSONObject
	 * o=properties.getJSONObject(i); if
	 * (o.get("symbolicName").equals("XMXX_XMQY_SHENG")){
	 * province=o.get("value").toString(); logger.info("--->eds，找到省，value：[{}]",
	 * province); }else if (o.get("symbolicName").equals("XMXX_XMQY_SHI")){
	 * city=o.get("value").toString(); logger.info("--->eds，找到市，value：[{}]",
	 * city); }else if(o.get("symbolicName").equals("XMXX_XMQY_XIAN")){ county =
	 * o.get("value").toString(); logger.info("--->eds，找到县，value：[{}]", county);
	 * }else if(o.get("symbolicName").equals("XMXX_YBYX_SQBM")){
	 * departmentId=o.get("value").toString();
	 * logger.info("--->eds，找到已选择部门，编码：[{}]", departmentId); }else
	 * if(o.get("symbolicName").equals("XMXX_YBYX_SQR")){
	 * sqr=o.get("value").toString();
	 * logger.info("--->eds，找到已选择申请人员，value：[{}]", sqr); } }
	 * 
	 * @param propertiesJson
	 * @return
	 */
	@Override
	public EdsResultDTO getEdsDataYXSQ(String propertiesJson) {

		JSONArray properties = JSONArray.parseArray(propertiesJson);
		Map<String, String> keyMapData = getKeyMapData(properties);

		EdsResultDTO eds = new EdsResultDTO();
		Map<String, String> departmentMapData = getDepartmentMapData();

		// 省
		/*
		 * String province = keyMapData.containsKey("XMXX_XMQY_SHENG")?
		 * keyMapData.get("XMXX_XMQY_SHENG") : "";
		 * logger.info("--->eds，找到省，value：[{}]", province); Map<String, String>
		 * data = this.getProvinceMapData(); eds.addProperty(new
		 * Property("XMXX_XMQY_SHENG", false,false,true,"ProvinceChoiceList",
		 * data, province));
		 */
		String province = setEdsProvince(eds, keyMapData);
		// 市
		String city = setEdsCity(eds, keyMapData, province);
		/*
		 * String city = keyMapData.containsKey("XMXX_XMQY_SHI")?
		 * keyMapData.get("XMXX_XMQY_SHI") : "";
		 * logger.info("--->eds，找到市，value：[{}]", city); if
		 * (!StringUtil.isNullOrEmpty(province) &&
		 * !province.equalsIgnoreCase("null")){
		 * 
		 * eds.addProperty(new Property("XMXX_XMQY_SHI", false, false, true,
		 * "CityChoiceList", this.getCityMapData(province), city)); }else{
		 * eds.addProperty(new
		 * Property("XMXX_XMQY_SHI",false,false,true,"CityChoiceList", null,
		 * null)); }
		 */
		// 县
		setEdsCounty(eds, keyMapData, province, city);
		/*
		 * String county = keyMapData.containsKey("XMXX_XMQY_XIAN")?
		 * keyMapData.get("XMXX_XMQY_XIAN") : "";
		 * logger.info("--->eds，找到县，value：[{}]", county); if
		 * (!StringUtil.isNullOrEmpty(city) && !city.equalsIgnoreCase("null")){
		 * 
		 * eds.addProperty(new Property("XMXX_XMQY_XIAN", false, false, false,
		 * "CountyChoiceList", this.getCountyMapData(province, city),county));
		 * }else{ eds.addProperty(new
		 * Property("XMXX_XMQY_XIAN",false,false,false,"CountyChoiceList",null,
		 * null)); }
		 */
		// 申请部门
		String departmentId = setEdsSQDepartment(keyMapData, eds, departmentMapData);

		// 申请人
		setEdsSQR(keyMapData, eds, departmentId);

		return eds;
	}

	
	/**
	 * 申请人
	 * 
	 * @param keyMapData
	 * @param eds
	 * @param departmentId
	 */
	private void setEdsSQR(Map<String, String> keyMapData, EdsResultDTO eds, String departmentId) {
		String sqr = keyMapData.containsKey("XMXX_YBYX_SQR") ? keyMapData.get("XMXX_YBYX_SQR") : "";
		logger.info(">>>eds，找到已选择申请人员，value：[{}]", sqr);

		// 部门下人员
		if (!StringUtil.isNullOrEmpty(departmentId) && !departmentId.equalsIgnoreCase("null")) {
			Map<String, String> usersOfOrg = getUsersOfOrg(departmentId);
			eds.addProperty(new Property("XMXX_YBYX_SQR", false, true, false, "UserChoiceList", usersOfOrg, sqr));
		} else {
			eds.addProperty(new Property("XMXX_YBYX_SQR", false, true, false, "UserChoiceList", null, null));
		}
	}

	/**
	 * 申请部门
	 * 
	 * @param keyMapData
	 * @param eds
	 * @param departmentMapData
	 * @return
	 */
	private String setEdsSQDepartment(Map<String, String> keyMapData, EdsResultDTO eds,
			Map<String, String> departmentMapData) {
		String departmentId = keyMapData.containsKey("XMXX_YBYX_SQBM") ? keyMapData.get("XMXX_YBYX_SQBM") : "";
		logger.info(">>>eds，找到已选择部门，编码：[{}]", departmentId);
		eds.addProperty(
				new Property("XMXX_YBYX_SQBM", false, true, true, "OrgChoiceList", departmentMapData, departmentId));
		return departmentId;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MaterialDimensionDTO> getMaterialDimensions(Long parentId) {

		List<MaterialDimensionDTO> results = new LinkedList<MaterialDimensionDTO>();
		List<DcmMaterialDimension> list = (List<DcmMaterialDimension>) mdDmn.getSubLevelByParentId(parentId);
		if (list != null && list.size() > 0) {
			for (DcmMaterialDimension md : list) {
				MaterialDimensionDTO dto = new MaterialDimensionDTO().clone(md);
				dto.setChildren(this.getMaterialDimensions(md.getId()));
				results.add(dto);
			}
		}

		return results;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<RegionTreeDTO> getSuperMaterialData(String typeName) {

		Object cacheValue = this.globalConf.openCache()? this.redisCache.getList(CacheKey.PREFIX_REGION_TREE + GlobalSystemParameters.SERIALIZABLE_REGION_GUID):null;
		
		if (null == cacheValue) {
			return this.refreshRegionTreeCache();
		}
		logger.info(">>>直接返回行政区域数据缓存");
		return (List<RegionTreeDTO>) cacheValue;

		/*
		 * DcmMaterialDimension md = this.mdDmn.findUniqueByProperties(new
		 * String[]{"parentId","name"}, new Object[]{-1L, typeName}); if(null ==
		 * md) throw new BusinessException("缺少 [{0}] 的数据", typeName);
		 * 
		 * return this.getMaterialDimensions(md.getId());
		 */
	}

}
