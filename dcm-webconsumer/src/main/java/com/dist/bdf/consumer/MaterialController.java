package com.dist.bdf.consumer;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.dist.bdf.base.controller.BaseController;
import com.dist.bdf.base.result.Result;
import com.dist.bdf.base.utils.DistThreadManager;
import com.dist.bdf.base.utils.FileUtil;
import com.dist.bdf.facade.service.DepartmentMaterialService;
import com.dist.bdf.facade.service.EcmMgmtService;
import com.dist.bdf.facade.service.EdsService;
import com.dist.bdf.facade.service.InstituteMaterialService;
import com.dist.bdf.facade.service.MaterialService;
import com.dist.bdf.facade.service.PersonalMaterialService;
import com.dist.bdf.facade.service.ProjectMaterialService;
import com.dist.bdf.facade.service.SocialService;
import com.dist.bdf.common.constants.DomainType;
import com.dist.bdf.common.constants.GlobalSystemParameters;
import com.dist.bdf.common.constants.MaterialDimension;
import com.dist.bdf.model.dto.dcm.DocRenameDTO;
import com.dist.bdf.model.dto.dcm.FolderRenameDTO;
import com.dist.bdf.model.dto.dcm.PageParaDTO;
import com.dist.bdf.model.dto.system.DownloadParaDTO;
import com.dist.bdf.model.dto.system.FavoriteParaDTO;
import com.dist.bdf.model.dto.system.MaterialMoveRequestDTO;
import com.dist.bdf.model.dto.system.MaterialParaDTO;
import com.dist.bdf.model.dto.system.NewFolderPropertyDTO;
import com.dist.bdf.model.dto.system.TaskMaterialDTO;
import com.dist.bdf.model.dto.system.page.PageSimple;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api(tags = { "API-资料服务模块" }, description = "MaterialController")
@RestController
@RequestMapping(value = "/rest/sysservice")
//@CrossOrigin(origins = "*")
public class MaterialController extends BaseController {

	@Autowired
	private MaterialService materialService;
	@Autowired
	private PersonalMaterialService perMaterialService;
	@Autowired
	private EcmMgmtService ecmMgmtService;
	@Autowired
	private DepartmentMaterialService departmentMaterialService;
	@Autowired
	private InstituteMaterialService instituteMaterialService;
	@Autowired
	private EdsService edsService;
	@Autowired
	private SocialService socialService;
	@Autowired
	private MaterialService commonMaterialService;
	@Autowired
	private ProjectMaterialService projectMaterialService;

	@ApiOperation(value = "针对所或者院的管理角色，获取机构根目录。", notes = "getOrgRootFoldersByAdmin")
	@RequestMapping(value = "/getOrgRootFoldersByAdmin", method = { RequestMethod.GET, RequestMethod.POST })
	public Result getOrgRootFoldersByAdmin(
			@ApiParam(value = "文件夹属性[properties]通过post方式传入，属性key包括[user : 用户dn, type : 类型（所级资料-3，院级资料-4）") @RequestParam String properties) {

		// String jsonStr = super.request.getParameter("properties");
		LOG.info(">>>" + properties);

		Object result = null;

		JSONObject propertiesObj = JSONObject.parseObject(properties);
		String userId = propertiesObj.getString("user");// super.request.getParameter("userId");
		// String password = propertiesObj.getString("pwd");
		String realm = propertiesObj.getString("realm");
		int type = propertiesObj.getIntValue("type");

		String domainTypeCode = DomainType.getDomainTypeCode(type);
		switch (domainTypeCode) {
		case DomainType.DEPARTMENT:
			// 所级空间
			result = this.departmentMaterialService.getRootFolderAdmin(realm, userId);
			break;
		case DomainType.INSTITUTE:
			// 院级空间
			result = this.instituteMaterialService.getRootFolderAdmin(realm, userId);
			break;
		default:
			break;
		}

		return super.successResult(result);
	}

	@ApiOperation(value = "针对所或者院的管理角色，获取机构根目录，及部门团队目录。", notes = "getOrgRootAndTeamFoldersByAdmin")
	@RequestMapping(value = "/folders/orgAndTeam/{userId}/{realm}/{type}", method = { RequestMethod.GET })
	public Result getOrgRootAndTeamFoldersByAdmin(
			@ApiParam(value = "用户标识，目前使用用户编码", required = true) @PathVariable String userId,
			@ApiParam(value = "域，如thupdi", required = true) @PathVariable String realm,
			// TODO 类型使用数字合理，还是编码合理？
			@ApiParam(value = "类型（所级资料-3，院级资料-4）", required = true) @PathVariable Integer type) {

		Object result = null;

		String domainTypeCode = DomainType.getDomainTypeCode(type);
		switch (domainTypeCode) {
		case DomainType.DEPARTMENT:
			// 所级空间
			result = this.departmentMaterialService.getRootAndTeamFolderAdmin(realm, userId);
			break;
		case DomainType.INSTITUTE:
			// 院级空间
			result = this.instituteMaterialService.getRootFolderAdminEx(realm, userId);
			break;
		default:
			break;
		}

		return super.successResult(result);
	}

	@ApiOperation(value = "创建文件夹", notes = "createFolder")
	@RequestMapping(value = "/createFolder", method = RequestMethod.POST)
	public Result createFolder(@ApiParam(value = "文件夹属性[properties]通过post方式传入，属性key包括"
			+ "[user : 用户,pwd : 密码,type : 类型（个人资料-1，项目资料-2，所级资料-3，院级资料-4）,parentFolderId : 父文件夹id,folderName : 新文件夹名称]，创建文件夹。") @RequestParam String properties) {

		// String jsonStr =
		// properties;//super.request.getParameter("properties");
		LOG.info(properties);
		JSONObject propertiesObj = JSONObject.parseObject(properties);
		String userId = propertiesObj.getString("user");// super.request.getParameter("userId");
		String password = propertiesObj.getString("pwd");
		String realm = propertiesObj.getString("realm");
		int type = propertiesObj.getIntValue("type");
		String parentFolderId = propertiesObj.getString("parentFolderId");
		String folderName = propertiesObj.getString("folderName");
		NewFolderPropertyDTO property = new NewFolderPropertyDTO(realm, userId, password, type, parentFolderId,
				folderName);
		Object result = this.materialService.createFolder(property);
		/*
		 * String domainTypeCode = DomainType.getDomainTypeCode(type); switch
		 * (domainTypeCode) { case DomainType.Person:
		 * 
		 * break; case DomainType.Department: // 所级空间 result =
		 * this.materialService.createFolder(property); break; case
		 * DomainType.Institute: // 院级空间 result =
		 * this.materialService.createFolder(property); break; default: break; }
		 */

		return super.successResult(result);
	}

	@ApiOperation(value = "根据id删除文件夹。", notes = "deleteFolderById")
	@RequestMapping(value = "/deleteFolderById", method = { RequestMethod.DELETE, RequestMethod.POST })
	public Result deleteFolderById(
			@ApiParam(value = "操作属性[properties]通过post方式传入，根据文件夹id删除实体") @RequestParam String properties) {

		// String jsonStr = super.request.getParameter("properties");
		LOG.info("正在删除文件夹，信息：[{}]", properties);
		JSONObject propertiesObj = JSONObject.parseObject(properties);
		String userId = propertiesObj.getString("user");// super.request.getParameter("userId");
		String password = propertiesObj.getString("pwd");
		String folderId = propertiesObj.getString("folderId");
		String realm = propertiesObj.getString("realm");

		Object result = this.materialService.deleteFolderByIdAndReturnIds(realm, userId, password, folderId);
		return super.successResult(result);
	}

	@Deprecated
	@ApiOperation(value = "重命名文件夹", notes = "updateFolderName")
	@RequestMapping(value = "/updateFolderName", method = { RequestMethod.PUT, RequestMethod.POST })
	public Result updateFolderName(
			@ApiParam(value = "操作属性[properties]通过post方式传入，根据文件夹id更新文件夹名称。") @RequestParam String properties) {

		// String jsonStr = super.request.getParameter("properties");
		LOG.info(properties);
		JSONObject propertiesObj = JSONObject.parseObject(properties);
		String userId = propertiesObj.getString("user");// super.request.getParameter("userId");
		String password = propertiesObj.getString("pwd");
		String folderId = propertiesObj.getString("folderId");
		String newFolderName = propertiesObj.getString("newName");
		String realm = propertiesObj.getString("realm");

		return super.successResult(this.materialService.updateFolder(realm, userId, password, folderId, newFolderName));
	}

	@Deprecated
	@ApiOperation(value = "重命名文件夹（已过时，切换到/v1/folder/rename）", notes = "renameFolderName")
	@RequestMapping(value = "/folder/rename", method = { RequestMethod.PUT, RequestMethod.POST })
	public Result renameFolderName(
			@ApiParam(value = "操作属性[properties]通过post方式传入，根据文件夹id更新文件夹名称。") @RequestParam String properties) {

		// String jsonStr = super.request.getParameter("properties");
		LOG.info(properties);
		JSONObject propertiesObj = JSONObject.parseObject(properties);
		// String userId = propertiesObj.getString("user");//
		// super.request.getParameter("userId");
		// String password = propertiesObj.getString("pwd");
		String folderId = propertiesObj.getString("folderId");
		String newFolderName = propertiesObj.getString("newName");
		String realm = propertiesObj.getString("realm");

		return super.successResult(this.materialService.renameFolderName(realm, folderId, newFolderName));
	}
	
	@ApiOperation(value = "重命名文件夹", notes = "renameFolderNameV1")
	@RequestMapping(value = "/v1/folder/rename", method = { RequestMethod.PUT, RequestMethod.POST })
	public Result renameFolderNameV1(
			@ApiParam(value = "文件夹重命名相关属性。") 
			@RequestBody FolderRenameDTO info) {

		// String jsonStr = super.request.getParameter("properties");
		// logger.info(properties);
		// JSONObject propertiesObj = JSONObject.parseObject(properties);
		// String userId = propertiesObj.getString("user");//
		// super.request.getParameter("userId");
		// String password = propertiesObj.getString("pwd");
	   /*	String folderId = propertiesObj.getString("folderId");
		String newFolderName = propertiesObj.getString("newName");
		String realm = propertiesObj.getString("realm");*/

		return super.successResult(this.materialService.renameFolderName(info.getRealm(), info.getFolderId(), info.getNewName()));
	}
	@ApiOperation(value = "重命名文件", notes = "renameDocName")
	@RequestMapping(value = "/v1/doc/rename", method = { RequestMethod.PUT, RequestMethod.POST })
	public Result renameDocName(
			@ApiParam(value = "文件重命名相关属性。") 
			@RequestBody DocRenameDTO info) {

		return super.successResult(this.materialService.renameDocName(info.getRealm(), info.getDocId(), info.getNewName()));
	}
	
	@ApiOperation(value = "根据id删除文档", notes = "deleteDocById")
	@RequestMapping(value = "/deleteDocById", method = { RequestMethod.DELETE, RequestMethod.POST })
	public Result deleteDocById(
			@ApiParam(value = "操作属性[properties]通过post方式传入，根据文件id删除实体") @RequestParam String properties) {

		// String jsonStr = super.request.getParameter("properties");
		LOG.info(properties);
		JSONObject propertiesObj = JSONObject.parseObject(properties);
		String userId = propertiesObj.getString("user");// super.request.getParameter("userId");
		// TODO 密码需要改进，进行加密
		String password = propertiesObj.getString("pwd");
		String docId = propertiesObj.getString("docId");
		String realm = propertiesObj.getString("realm");

		Object result = this.materialService.deleteDoc(realm, userId, password, docId);
		return super.successResult(result);
	}
	
	@ApiOperation(value = "管理员权限，根据id删除文档", notes = "deleteDocByIdAdmin")
	@RequestMapping(value = "/v1/docs/admin/{realm}/{id}", method = { RequestMethod.DELETE })
	public Result deleteDocByIdAdmin(
			@ApiParam(value = "域，如：数慧，dist") 
			@PathVariable String realm,
			@ApiParam(value = "文档唯一id") 
			@PathVariable String id) {

		return super.successResult(this.materialService.deleteDocAdmin(realm, id));
	}

	@ApiOperation(value = "根据版本系列id删除文档", notes = "deleteDocByVId")
	@RequestMapping(value = "/deleteDocByVId", method = { RequestMethod.DELETE, RequestMethod.POST })
	public Result deleteDocByVId(
			@ApiParam(value = "操作属性[properties]通过post方式传入，根据文件的版本系列id（vid）删除实体。") @RequestParam String properties) {

		// String jsonStr = super.request.getParameter("properties");
		LOG.info(properties);
		JSONObject propertiesObj = JSONObject.parseObject(properties);
		String userId = propertiesObj.getString("user");// super.request.getParameter("userId");
		String password = propertiesObj.getString("pwd");
		String docId = propertiesObj.getString("docId"); // 此处是获取版本系列id
		String realm = propertiesObj.getString("realm");

		Object result = this.materialService.deleteDocByVId(realm, userId, password, docId);
		return super.successResult(result);
	}
	
	@ApiOperation(value = "管理员权限，根据版本系列id删除文档", notes = "deleteDocByVIdAdmin")
	@RequestMapping(value = "/v1/docs/admin/vid/{realm}/{vid}", method = { RequestMethod.DELETE})
	public Result deleteDocByVIdAdmin(
			@ApiParam(value = "域，如：数慧，dist") 
			@PathVariable String realm,
			@ApiParam(value = "文档唯一id") 
			@PathVariable String vid) {

		Object result = this.materialService.deleteDocByVIdAdmin(realm, vid);
		return super.successResult(result);
	}

	@ApiOperation(value = "获取个人资料信息", notes = "getPersonalMaterialInfo")
	@RequestMapping(value = "getPersonalMaterialInfo/{parajson}", method = RequestMethod.GET)
	public Result getPersonalMaterialInfo(@PathVariable String parajson) {

		LOG.info("getMaterialInfo, para : " + parajson);
		Object obj = this.materialService.getMaterialInfo(DomainType.PERSON, parajson);
		return super.successResult(obj);
	}

	@Deprecated
	@ApiOperation(value = "获取个人资料信息。（已过时，切换到接口/material/person）", notes = "getPersonalMaterialInfoPost")
	@RequestMapping(value = "getPersonalMaterialInfo", method = RequestMethod.POST)
	public Result getPersonalMaterialInfoPost(@RequestBody String parajson) {

		LOG.info("getMaterialInfo, para : " + parajson);
		Object obj = this.materialService.getMaterialInfo(DomainType.PERSON, parajson);
		return super.successResult(obj);
	}

	@ApiOperation(value = "获取个人资料信息，为了应对传入参数长度过长，使用了POST替代了GET", notes = "getPersonalMaterialInfoPostEx")
	@RequestMapping(value = "/material/person", method = RequestMethod.POST)
	public Result getPersonalMaterialInfoPostEx(@RequestBody String parajson) {

		LOG.info("getMaterialInfo, para : " + parajson);
		Object obj = this.materialService.getMaterialInfoEx(DomainType.PERSON, parajson);
		return super.successResult(obj);
	}

	@ApiOperation(value = "获取所级资料信息", notes = "getDepartmentMaterialInfo")
	@RequestMapping(value = "getDepartmentMaterialInfo/{parajson}", method = RequestMethod.GET)
	public Result getDepartmentMaterialInfo(@PathVariable String parajson) {

		LOG.info("getDepartmentMaterialInfo, para : " + parajson);
		Object obj = this.materialService.getMaterialInfo(DomainType.DEPARTMENT, parajson);
		return super.successResult(obj);

	}

	@ApiOperation(value = "获取所级资料信息", notes = "getDepartmentMaterialInfoPost")
	@RequestMapping(value = "getDepartmentMaterialInfo", method = RequestMethod.POST)
	public Result getDepartmentMaterialInfoPost(@RequestBody String parajson) {

		LOG.info("getDepartmentMaterialInfo, para : " + parajson);
		Object obj = this.materialService.getMaterialInfo(DomainType.DEPARTMENT, parajson);
		return super.successResult(obj);

	}

	@ApiOperation(value = "获取院级资料信息", notes = "getInstituteMaterialInfo")
	@RequestMapping(value = "getInstituteMaterialInfo/{parajson}", method = RequestMethod.GET)
	public Result getInstituteMaterialInfo(@PathVariable String parajson) {

		LOG.info("getInstituteMaterialInfo, para : " + parajson);
		Object obj = this.materialService.getMaterialInfo(DomainType.INSTITUTE, parajson);
		return super.successResult(obj);
	}

	@ApiOperation(value = "获取院级资料信息", notes = "getInstituteMaterialInfoPost")
	@RequestMapping(value = "getInstituteMaterialInfo", method = RequestMethod.POST)
	public Result getInstituteMaterialInfoPost(@RequestBody String parajson) {

		LOG.info("getInstituteMaterialInfo, para : " + parajson);
		Object obj = this.materialService.getMaterialInfo(DomainType.INSTITUTE, parajson);
		return super.successResult(obj);
	}

	@Deprecated
	@ApiOperation(value = "获取项目资料信息。（废弃，切换到/material/project）", notes = "getProjectMaterialInfo")
	@RequestMapping(value = "getProjectMaterialInfo/{parajson}", method = { RequestMethod.GET })
	public Result getProjectMaterialInfo(@PathVariable String parajson) {

		LOG.info("getProjectMaterialInfo, para : " + parajson);
		Object obj = this.materialService.getMaterialInfo(DomainType.PROJECT, parajson);
		return super.successResult(obj);
	}

	@Deprecated
	@ApiOperation(value = "获取项目资料信息。（废弃，切换到/material/project）", notes = "getProjectMaterialInfoPost")
	@RequestMapping(value = "getProjectMaterialInfo", method = { RequestMethod.POST })
	// @CrossOrigin(origins="*") // PS：需要跟CORS结合使用，才解决了跨域问题
	public Result getProjectMaterialInfoPost(@RequestBody String parajson) {

		LOG.info("getProjectMaterialInfo, para : " + parajson);
		Object obj = this.materialService.getMaterialInfo(DomainType.PROJECT, parajson);
		return super.successResult(obj);
	}

	@ApiOperation(value = "获取项目资料信息，为了应对传入参数长度过长，使用了POST替代了GET", notes = "getProjectMaterialInfoPostEx")
	@RequestMapping(value = "/material/project", method = { RequestMethod.POST })
	public Result getProjectMaterialInfoPostEx(@RequestBody String parajson) {

		LOG.info("getProjectMaterialInfo, para : " + parajson);
		Object obj = this.materialService.getMaterialInfoEx(DomainType.PROJECT, parajson);
		return super.successResult(obj);
	}

	@ApiOperation(value = "添加个人资料收藏（目前还在使用中，后续切换到/v1/material/favourite）", notes = "saveMaterialFavorite")
	@RequestMapping(value = "savePersonalMaterialFavorite/{parajson}", method = { RequestMethod.POST,
			RequestMethod.GET })
	public Result saveMaterialFavorite(@PathVariable String parajson) throws Exception {

		FavoriteParaDTO dto = JSONObject.parseObject(parajson, FavoriteParaDTO.class);
		Object obj = this.perMaterialService.saveFavorite(dto);
		return super.successResult(obj);
	}
	
	@ApiOperation(value = "添加或取消个人资料收藏", notes = "saveMaterialFavoriteEx")
	@RequestMapping(value = "/v1/material/favorite", method = { RequestMethod.POST, RequestMethod.PUT })
	public Result saveMaterialFavoriteEx(@RequestBody FavoriteParaDTO dto) throws Exception {

		Object obj = this.perMaterialService.saveFavoriteEx(dto);
		return super.successResult(obj);
	}

	@ApiOperation(value = "删除资料", notes = "deleteMaterial")
	@RequestMapping(value = "deletePersonalMaterial/{parajson}", method = { RequestMethod.DELETE, RequestMethod.GET })
	public Result deleteMaterial(@PathVariable String parajson) {
		
		Object obj = this.perMaterialService.deleteMaterial(parajson);
		return super.successResult(obj);
	}

	/**
	 * 获取个人收藏资料
	 * 
	 * @param user
	 * @return
	 */
	/*
	 * @RequestMapping(value="getPersonalFavoriteDeprecated/{parajson}")
	 * 
	 * @Deprecated public Result getFavorite(@PathVariable String parajson){
	 * 
	 * try { Object result = this.perMaterialService.getFavorite(parajson);
	 * return super.successResult(result);
	 * 
	 * } catch (Exception ex) { return super.failResultException(ex); } }
	 */

	@ApiOperation(value = "获取个人收藏夹", notes = "getFavoriteEx")
	@RequestMapping(value = "getPersonalFavorite/{parajson}", method = RequestMethod.GET)
	public Result getFavoriteEx(@ApiParam(value = "用户信息，分页查询的设置JSON信息", required = true) @PathVariable String parajson)
			throws Exception {

		PageSimple page = JSONObject.parseObject(parajson, PageSimple.class);
		Object result = this.perMaterialService.getFavoriteEx(page);
		return super.successResult(result);
	}

	@ApiOperation(value = "获取个人下载日志", notes = "getDownloadLogs")
	@RequestMapping(value = "getPersonalDownloadLogs/{parajson}", method = RequestMethod.GET)
	public Result getDownloadLogs(
			@ApiParam(value = "用户信息，分页查询的设置JSON信息", required = true) @PathVariable String parajson) throws Exception {

		PageSimple page = JSONObject.parseObject(parajson, PageSimple.class);
		Object result = this.perMaterialService.getDownloadLogs(page);
		return super.successResult(result);
	}

	@ApiOperation(value = "根据资源id集合，删除个人下载日志记录", notes = "delDownloadLogs")
	@RequestMapping(value = "log/per.download.del/{parajson}", method = RequestMethod.DELETE)
	public Result delDownloadLogs(
			@ApiParam(value = "当前登录用户和资源id集合组成的JSON信息", required = true) @PathVariable String parajson)
			throws Exception {

		DownloadParaDTO dto = JSONObject.parseObject(parajson, DownloadParaDTO.class);
		Object result = this.perMaterialService.delDownloadLogsByResIds(dto);
		return super.successResult(result);
	}

	@Deprecated
	@ApiOperation(value = "获取个人资料包信息", notes = "getPersPcks")
	@RequestMapping(value = "getPersPcks/{realm}/{userId}/{pwd}", method = RequestMethod.GET)
	public Result getPersPcks(@ApiParam(value = "机构域", required = true) @PathVariable String realm,
			@ApiParam(value = "用户id，一般是登录名", required = true) @PathVariable String userId,
			@ApiParam(value = "用户密码", required = true) @PathVariable String pwd) throws Exception {

		Object result = this.perMaterialService.getPersonalPackages(realm, userId, pwd);
		return super.successResult(result);
	}

	@ApiOperation(value = "获取个人资料包信息", notes = "getPersPcks")
	@RequestMapping(value = "getPersPcks/{realm}/{userId}", method = RequestMethod.GET)
	public Result getPersPcks(@ApiParam(value = "机构域", required = true) @PathVariable String realm,
			@ApiParam(value = "用户id，一般是用户dn", required = true) @PathVariable String userId) throws Exception {

		return super.successResult(this.perMaterialService.getPersonalPackages(realm, userId));
	}
	/**
	 * TODO 为何此方法与getPersPcks一样？
	 * @param realm
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	@Deprecated
	@ApiOperation(value = "分页获取个人资料包信息", notes = "getPersPcksPage")
	@RequestMapping(value = "pers/pcks/page/{realm}/{userId}", method = RequestMethod.GET)
	public Result getPersPcksPage(@ApiParam(value = "机构域", required = true) @PathVariable String realm,
			@ApiParam(value = "用户id，一般是用户dn", required = true) @PathVariable String userId) throws Exception {

		return super.successResult( this.perMaterialService.getPersonalPackages(realm, userId));
	}

	@Deprecated
	@ApiOperation(value = "获取资料信息，包括当前用户是否有下载权限和是否已收藏", notes = "getMaterialCommonInfoOld")
	@RequestMapping(value = "getMaterialInfo/{parajson:.+}", method = RequestMethod.GET)
	public Result getMaterialCommonInfoOld(
			@ApiParam(value = "json格式的资料信息", required = true) @PathVariable String parajson) throws Exception {

		MaterialParaDTO dto = JSONObject.parseObject(parajson, MaterialParaDTO.class);// (MaterialParaDTO)
																						// JSONUtil.toObject(parajson,
																						// MaterialParaDTO.class);
		Object result = this.materialService.getMaterialInfo(dto);
		return super.successResult(result);

	}

	@ApiOperation(value = "获取资料信息，包括当前用户是否有下载权限和是否已收藏", notes = "getMaterialCommonInfo")
	@RequestMapping(value = "getMaterialInfoPost", method = RequestMethod.POST)
	public Result getMaterialCommonInfo(@ApiParam(value = "json格式的资料信息", required = true) @RequestBody String parajson)
			throws Exception {

		MaterialParaDTO dto = JSONObject.parseObject(parajson, MaterialParaDTO.class);// (MaterialParaDTO)
																						// JSONUtil.toObject(parajson,
																						// MaterialParaDTO.class);
		Object result = this.materialService.getMaterialInfo(dto);
		return super.successResult(result);

	}

	/*
	 * @ApiOperation(value="getMaterialPackages", notes = "获取资料包信息，包括文件夹和文件。")
	 * 
	 * @RequestMapping(value="getMaterialPackages/{folderId}", method =
	 * RequestMethod.GET) public Result getMaterialPackages(
	 * 
	 * @ApiParam(value="文件夹标识，如：{70699E54-0000-C61B-AADA-A0402F6D8E29}")
	 * 
	 * @PathVariable String folderId){
	 * 
	 * return
	 * super.successResult(this.materialService.getFoldersAndDocuments(folderId)
	 * ); }
	 */
	@Deprecated
	@ApiOperation(value = "获取资料包信息，包括文件夹和文件", notes = "getMaterialPackages")
	@RequestMapping(value = "getMaterialPackages/{realm}/{userId}/{pwd}/{folderId}", method = RequestMethod.GET)
	public Result getMaterialPackages(@ApiParam(value = "机构域，如：数慧，dist") @PathVariable String realm,
			@ApiParam(value = "用户标识符，目前使用ldap中的dn") @PathVariable String userId,
			@ApiParam(value = "用户密码") @PathVariable String pwd,
			@ApiParam(value = "文件夹标识，如：{70699E54-0000-C61B-AADA-A0402F6D8E29}") @PathVariable String folderId) {

		return super.successResult(this.materialService.getFoldersAndDocuments(realm, userId, pwd, folderId));
	}

	@ApiOperation(value = "获取资料包信息，包括文件夹和文件", notes = "getMaterialPackages")
	@RequestMapping(value = "getMaterialPackages/{realm}/{folderId}", method = RequestMethod.GET)
	public Result getMaterialPackages(@ApiParam(value = "机构域，如：数慧，dist") @PathVariable String realm,
			@ApiParam(value = "文件夹标识，如：{70699E54-0000-C61B-AADA-A0402F6D8E29}") @PathVariable String folderId) {

		return super.successResult(this.materialService.getFoldersAndDocuments(realm, folderId));
	}

	/*
	 * @ApiOperation(value = "getDocURL", notes = "根据id获取文件可访问url。")
	 * 
	 * @RequestMapping(value="getDocURL", method = {RequestMethod.POST}) public
	 * Result getDocURL(
	 * 
	 * @ApiParam(value = "文件扩展名，建议不带点号.")
	 * 
	 * @PathVariable String extension,
	 * 
	 * @ApiParam(value = "文档在CE中的唯一标识")
	 * 
	 * @PathVariable String docId) {
	 * 
	 * String extension =
	 * super.request.getParameter("extension");//.getAttribute("").toString();
	 * String docId =
	 * super.request.getParameter("docId");//("docId").toString();
	 * 
	 * String fileName = (extension.startsWith("."))? (docId+extension) :
	 * (docId+"."+extension); String dir = super.getContextPath("/temp"); byte[]
	 * stream = (byte[]) this.ecmMgmtService.getDocContentStream(docId); boolean
	 * result = FileUtil.createFile(stream, dir, fileName); if(result){ return
	 * super.successResult(super.getBaseURL()+"/temp/"+fileName); }
	 * 
	 * return super.failResult("文件生成链接失败。"); }
	 */

	/*
	 * @ApiOperation(value = "getDocURL", notes = "根据id获取文件可访问url。")
	 * 
	 * @RequestMapping(value="getDocURL/{ext}/{docId:\\{.+\\}}", method =
	 * {RequestMethod.GET}) public Result getDocURL(
	 * 
	 * @ApiParam(value = "文件扩展名，建议不带点号.")
	 * 
	 * @PathVariable String ext,
	 * 
	 * @ApiParam(value = "文档在CE中的唯一标识")
	 * 
	 * @PathVariable String docId) {
	 * 
	 * //String ext =
	 * super.request.getParameter("extension");//.getAttribute("").toString();
	 * //String docId =
	 * super.request.getParameter("docId");//("docId").toString();
	 * 
	 * String fileName = (ext.startsWith("."))? (docId+ext) : (docId+"."+ext);
	 * String dir = super.getContextPath(RelativeDirectory.DIR_TEMP); if(new
	 * File(dir+"\\"+fileName).exists()){ logger.info("找到缓存文件：[{}]", fileName);
	 * return
	 * super.successResult(super.getBaseURL()+RelativeDirectory.DIR_TEMP+"/"+
	 * fileName); }
	 * 
	 * byte[] stream = (byte[]) this.ecmMgmtService.getDocContentStream(docId);
	 * boolean result = FileUtil.createFile(stream, dir, fileName); if(result){
	 * return
	 * super.successResult(super.getBaseURL()+RelativeDirectory.DIR_TEMP+"/"+
	 * fileName); }
	 * 
	 * return super.failResult("文件生成链接失败。"); }
	 */

	@ApiOperation(value = "根据id获取文件可访问url", notes = "getDocURL")
	@RequestMapping(value = "getDocURL/{realm}/{ext}/{docId:\\{.+\\}}", method = { RequestMethod.GET })
	public Result getDocURL(@ApiParam(value = "域", required = true) @PathVariable String realm,
			@ApiParam(value = "文件扩展名，建议不带点号.", required = true) @PathVariable String ext,
			@ApiParam(value = "文档在CE中的唯一标识", required = true) @PathVariable String docId) {

		String fileName = (ext.startsWith(".")) ? (docId + ext) : (docId + "." + ext);
		String dir = super.getContextPath(GlobalSystemParameters.DIR_TEMP);

		if (new File(dir + File.separatorChar + fileName).exists()) {
			LOG.info(">>>找到缓存文件：[{}]", fileName);
			return super.successResult(
					super.getBaseURL() + GlobalSystemParameters.DIR_TEMP + File.separatorChar + fileName);
		}

		byte[] stream = (byte[]) this.ecmMgmtService.getDocContentStream(realm, docId);
		boolean result = FileUtil.createFile(stream, dir, fileName);
		if (result) {
			return super.successResult(
					super.getBaseURL() + GlobalSystemParameters.DIR_TEMP + File.separatorChar + fileName);
		}

		return super.failResult("文件生成链接失败。");
	}

	@ApiOperation(value = "根据id获取文件流", notes = "getDocContentStream")
	@RequestMapping(value = "getDocContentStream/{realm}/{docId:\\{.+\\}}", method = { RequestMethod.GET })
	public void getDocContentStream(@ApiParam(value = "域") @PathVariable String realm,
			@ApiParam(value = "ce中文档id") @PathVariable String docId) {

		OutputStream toClient = null;
		try {
			String contentType = "application/octet-stream";
			byte[] source = (byte[]) this.ecmMgmtService.getDocContentStream(realm, docId);
			toClient = new BufferedOutputStream(response.getOutputStream());
			response.setContentType(contentType);
			response.setContentLength(source.length);

			toClient.write(source);
			toClient.flush();

		} catch (Exception ex) {
			LOG.error(ex.getMessage(), ex);
		} finally {
			try {
				// 关闭流
				if (toClient != null)
					toClient.close();
			} catch (IOException io) {
				LOG.error(io.getMessage(), io);
			}
		}
	}

	@Deprecated
	@ApiOperation(value = "根据id下载文件流（已过时）", notes = "downloadMaterial")
	@RequestMapping(value = "downloadMaterial/{realm}/{docId:\\{.+\\}}", method = { RequestMethod.GET })
	public void downloadMaterial(@ApiParam(value = "域") @PathVariable String realm,
			@ApiParam(value = "ce中文档id") @PathVariable final String docId) {

		this.getDocContentStream(realm, docId);
		// 修改下载数
		DistThreadManager.MyCacheThreadPool.execute(new Runnable() {

			@Override
			public void run() {
				MaterialController.this.materialService.addDownloadCountOfSummaryData(docId);
			}
		});

	}

	@ApiOperation(value = "添加下载数，返回最新的下载总数", notes = "addDownloadCount")
	@RequestMapping(value = "addDownloadCount/{docId:\\{.+\\}}", method = { RequestMethod.GET, RequestMethod.POST })
	public Result addDownloadCount(@ApiParam(value = "文档id") @PathVariable String documentId) {

		return super.successResult(this.materialService.addDownloadCountOfSummaryData(documentId));
	}

	@ApiOperation(value = "添加或者减少收藏数，返回最新的收藏总数", notes = "addFavoriteCount")
	@RequestMapping(value = "saveFavoriteCount/{isFavorite}/{docId:\\{.+\\}}", method = { RequestMethod.GET,
			RequestMethod.POST })
	public Result addFavoriteCount(@ApiParam(value = "是否收藏，1：真；0：假") @PathVariable String isFavorite,
			@ApiParam(value = "文档id") @PathVariable String documentId) {

		return super.successResult(this.materialService.addFavoriteCountOfSummaryData(documentId,
				(isFavorite.equals("1")) ? Boolean.TRUE : Boolean.FALSE));
	}

	@ApiOperation(value = "添加或者减少点赞数，返回最新的点赞总数", notes = "addLikeCount")
	@RequestMapping(value = "saveLikeCount/{isLike}/{docId:\\{.+\\}}", method = { RequestMethod.GET,
			RequestMethod.POST })
	public Result addLikeCount(@ApiParam(value = "是否点赞，1：真；0：假") @PathVariable String isLike,
			@ApiParam(value = "文档id") @PathVariable String documentId) {

		return super.successResult(this.materialService.addLikeCountOfSummaryData(documentId,
				(isLike.equals("1")) ? Boolean.TRUE : Boolean.FALSE));
	}

	@ApiOperation(value = "获取任务关联的材料信息", notes = "getMaterialByTask")
	@RequestMapping(value = "task.material.get/{taskId}", method = { RequestMethod.GET })
	public Result getMaterialByTask(@PathVariable @ApiParam(value = "任务id") String taskId) {

		return super.successResult(this.materialService.getMaterialByTaskId(taskId));

	}

	@ApiOperation(value = "添加任务和材料的关联", notes = "addTaskMaterial")
	@RequestMapping(value = "task.material.add/{parajson}", method = { RequestMethod.POST })
	public Result addTaskMaterial(
			@PathVariable @ApiParam(value = "json参数，示例：{\"taskId\":\"123\", \"materialIds\":[\"0101\",\"0102\",\"0103\"]}") String parajson)
			throws Exception {

		TaskMaterialDTO dto = JSONObject.parseObject(parajson, TaskMaterialDTO.class);
		this.materialService.addTaskMaterial(dto);
		return super.successResult("添加成功");
	}

	@ApiOperation(value = "根据材料ID删除任务下的附件关联关系", notes = "deleteTaskMaterialById")
	@RequestMapping(value = "task.material.delete/{materialId}", method = { RequestMethod.DELETE, RequestMethod.POST })
	public Result deleteTaskMaterialById(@PathVariable @ApiParam(value = "材料id") String materialId) {

		this.materialService.deleteTaskMaterialById(materialId);
		return super.successResult("删除成功");

	}

	@ApiOperation(value = "获取资料维度", notes = "getMaterialDimension")
	@RequestMapping(value = "/getMaterialDimensions/{parentId}", method = RequestMethod.GET)
	public Result getMaterialDimension(@ApiParam(value = "父级菜单的id值") @PathVariable Long parentId) {

		Object result = this.edsService.getMaterialDimensions(parentId);
		return super.successResult(result);
	}

	@ApiOperation(value = "获取文种的一级菜单", notes = "getMaterialFileType")
	@RequestMapping(value = "/getMaterialFileType", method = { RequestMethod.GET, RequestMethod.POST })
	public Result getMaterialFileType() {

		Object result = this.edsService.getSuperMaterialData(MaterialDimension.FILE_TYPE);
		return super.successResult(result);
	}

	@ApiOperation(value = "获取区域的一级菜单", notes = "getMaterialRegion")
	@RequestMapping(value = "/getMaterialRegion", method = { RequestMethod.GET, RequestMethod.POST })
	public Result getMaterialRegion() {

		Object result = this.edsService.getSuperMaterialData(MaterialDimension.REGION);
		return super.successResult(result);
	}

	@ApiOperation(value = "获取机构的一级菜单", notes = "getMaterialOrg")
	@RequestMapping(value = "/getMaterialOrg", method = { RequestMethod.GET, RequestMethod.POST })
	public Result getMaterialOrg() {

		Object result = this.edsService.getSuperMaterialData(MaterialDimension.ORG);
		return super.successResult(result);
	}

	@ApiOperation(value = "获取业务类型的一级菜单", notes = "getMaterialBusiness")
	@RequestMapping(value = "/getMaterialBusiness", method = { RequestMethod.GET, RequestMethod.POST })
	public Result getMaterialBusiness() {

		Object result = this.edsService.getSuperMaterialData(MaterialDimension.BUSINESS);
		return super.successResult(result);
	}

	@ApiOperation(value = "材料移动，包括文件夹和文件", notes = "moveMaterial")
	@RequestMapping(value = "material/move", method = { RequestMethod.POST })
	public Result moveMaterial(@RequestBody String infojson) throws Exception {

		MaterialMoveRequestDTO dto = JSONObject.parseObject(infojson, MaterialMoveRequestDTO.class);

		Object result = this.materialService.doMove(dto);
		return super.successResult(result);
	}

	@ApiOperation(value = "记录下载的资源", notes = "downloadRes")
	@RequestMapping(value = "downloadRes/{parajson}", method = RequestMethod.GET)
	public Result downloadRes(@PathVariable String parajson) throws Exception {

		final DownloadParaDTO dto = JSONObject.parseObject(parajson, DownloadParaDTO.class);
		boolean result = this.socialService.downloadRes(dto);
		if (result) {
			// 修改下载数
			DistThreadManager.MyCacheThreadPool.execute(new Runnable() {

				@Override
				public void run() {

					MaterialController.this.commonMaterialService.addDownloadCountOfSummaryData(dto.getRealm(),
							dto.getId());
				}
			});

		}

		return super.successResult(result);

	}

	// =====>检索模块begin

	/*
	 * @ApiOperation(value="fullTextSearchOfDoc", notes="全文检索文档类。")
	 * 
	 * @RequestMapping(value = "fullTextSearchOfDoc/{keyword}", method =
	 * RequestMethod.GET) public Result fullTextSearchOfDoc(@PathVariable String
	 * keyword) { try { Object list =
	 * this.ecmMgmtService.fullTextSearchOfDocument(keyword);
	 * 
	 * return super.successResult(list);
	 * 
	 * } catch (Exception ex) {
	 * 
	 * return super.failResultException(ex); } }
	 */
	@ApiOperation(value = "全文检索文档类，并根据关键字匹配度排序", notes = "fullTextSearchPageOfDocRank")
	@RequestMapping(value = "doc/fulltext.page/sort/{parajson}", method = RequestMethod.GET)
	public Result fullTextSearchPageOfDocRank(
			@ApiParam(value = "参数json，包括：页码（pageNo）、每页大小（pageSize）、关键字（keyword）、域（realm）") @PathVariable String parajson)
			throws Exception {

		PageParaDTO dto = JSONObject.parseObject(parajson, PageParaDTO.class);
		Object list = this.ecmMgmtService.fullTextSearchOfDocumentRank(dto);

		return super.successResult(list);
	}

	/*
	 * @ApiOperation(value="fullTextSearchPageOfDoc", notes = "分页全文检索")
	 * 
	 * @RequestMapping(value = "fullTextSearchPageOfDoc/{parajson}", method =
	 * RequestMethod.GET) public Result fullTextSearchPageOfDoc(
	 * 
	 * @ApiParam(value="分页设置的JSON格式信息，包括页码（pageNo）、每页大小（pageSize）和关键字（keyword）")
	 * 
	 * @PathVariable String parajson) throws Exception {
	 * 
	 * PageParaDTO dto = (PageParaDTO) JSONUtil.toObject(parajson,
	 * PageParaDTO.class);
	 * 
	 * Object list = this.ecmMgmtService.fullTextSearchOfDocument(dto);
	 * 
	 * return super.successResult(list); }
	 */

	@ApiOperation(value = "分页全文检索", notes = "fullTextSearchPageOfDocForCloud")
	@RequestMapping(value = "doc/fulltext.page/{parajson}", method = RequestMethod.GET)
	public Result fullTextSearchPageOfDocForCloud(
			@ApiParam(value = "分页设置的JSON格式信息，包括页码（pageNo）、每页大小（pageSize）、关键字（keyword）、机构域（realm）") @PathVariable String parajson)
			throws Exception {

		PageParaDTO dto = JSONObject.parseObject(parajson, PageParaDTO.class);

		Object list = this.ecmMgmtService.fullTextSearchOfDocumentCloud(dto);

		return super.successResult(list);
	}

	@ApiOperation(value = "精确查询文档", notes = "preciseSearchOfDoc")
	@RequestMapping(value = "/preciseSearchOfDoc/**", method = RequestMethod.GET)
	public Result preciseSearchOfDoc(HttpServletRequest request) {
		String parajson = extractPathFromPattern(request);
		PageParaDTO dto = JSONObject.parseObject(parajson, PageParaDTO.class);
		Object list = this.ecmMgmtService.preciseQuery(dto);
		return super.successResult(list);
	}

	// <=====检索模块end

	@ApiOperation(value = "指定父文件夹，分页检索子文件夹", notes = "getSubFoldersPage")
	@RequestMapping(value = "/folder/sub/page", method = RequestMethod.POST)
	public Result getSubFoldersPage(
			@ApiParam(value = "JSON参数，属性：folderId、pageNo、pageSize、realm") @RequestBody PageParaDTO dto) {

		return super.successResult(this.materialService.getSubFoldersPage(dto));
	}

	@ApiOperation(value = "指定父文件夹，分页检索子文件", notes = "getSubDocsPage")
	@RequestMapping(value = "/folder/subdoc/page", method = RequestMethod.POST)
	public Result getSubDocsPage(
			@ApiParam(value = "JSON参数，属性：folderId、pageNo、pageSize、realm", required = true) @RequestBody PageParaDTO dto) {

		return super.successResult(this.materialService.getSubDocsPage(dto));
	}

	@ApiOperation(value = "根据版本系列id，获取文档的所有版本信息", notes = "getDocVersions")
	@RequestMapping(value = "/doc/versions/vid/{realm}/{vid}", method = RequestMethod.GET)
	public Result getDocVersions(@ApiParam(value = "realm，域") @PathVariable String realm,
			@ApiParam(value = "vid，版本系列id", required = true) @PathVariable String vid) {

		return super.successResult(this.materialService.getDocVersionsByVId(realm, vid));
	}
	@ApiOperation(value = "根据文件唯一id，获取文档的基本信息", notes = "getDocInfo")
	@RequestMapping(value = "/doc/info/{realm}/{docId}/{userId}", method = RequestMethod.GET)
	public Result getDocInfo(
			@ApiParam(value = "域", required = true) @PathVariable String realm,
			@ApiParam(value = "文件唯一id", required = true) @PathVariable String docId,
			@ApiParam(value = "用户id", required = true) @PathVariable String userId) {

		return super.successResult(this.materialService.getDocInfoById(realm, docId, userId));
	}
	@ApiOperation(value = "根据文件版本系列id，获取最新文档的基本信息", notes = "getDocInfoByVId")
	@RequestMapping(value = "/v1/doc/info/vid/{realm}/{vid}/{userId}", method = RequestMethod.GET)
	public Result getDocInfoByVId(
			@ApiParam(value = "域", required = true) @PathVariable String realm,
			@ApiParam(value = "文件版本系列id", required = true) @PathVariable String vid,
			@ApiParam(value = "用户id", required = true) @PathVariable String userId) {

		return super.successResult(this.materialService.getDocInfoByVId(realm, vid, userId));
	}
	@ApiOperation(value = "分页获取项目文件夹", notes = "getProjectFoldersPage")
	@RequestMapping(value = "/v1/project/folders/{pageSize}/{pageNo}/{projectId}", method = RequestMethod.GET)
	public Result getProjectFoldersPage(@ApiParam(value = "每页大小") @PathVariable int pageSize,
			@ApiParam(value = "页码") @PathVariable int pageNo,
			@ApiParam(value = "项目唯一编号") @PathVariable String projectId) {

		return super.successResult(this.projectMaterialService.getProjectFoldersPage(pageSize, pageNo, "", projectId));
	}
	
	@ApiOperation(value = "分页获取项目文件夹下子文件夹", notes = "getProjectSubFoldersPage")
	@RequestMapping(value = "/v1/project/subfolders/{pageSize}/{pageNo}/{parentFolderId}", method = RequestMethod.GET)
	public Result getProjectSubFoldersPage(
			@ApiParam(value = "每页大小") @PathVariable int pageSize,
			@ApiParam(value = "页码") @PathVariable int pageNo,
			@ApiParam(value = "父文件夹id") @PathVariable String parentFolderId) {

		return super.successResult(
				this.projectMaterialService.getProjectFoldersPage(pageSize, pageNo, "", parentFolderId));
	}
	@ApiOperation(value = "分页获取项目文件夹下的文档", notes = "getProjectDocsPage")
	@RequestMapping(value = "/v1/project/docs/{pageSize}/{pageNo}/{projectId}", method = RequestMethod.GET)
	public Result getProjectDocsPage(@ApiParam(value = "每页大小") @PathVariable int pageSize,
			@ApiParam(value = "页码") @PathVariable int pageNo,
			@ApiParam(value = "项目唯一编号") @PathVariable String projectId) {
		
		return super.successResult(this.projectMaterialService.getProjectDocsPage(pageSize, pageNo, "", projectId));
	}
	@ApiOperation(value = "分页获取项目子文件夹的文档", notes = "getProjectSubDocsPage")
	@RequestMapping(value = "/v1/project/subdocs/{pageSize}/{pageNo}/{parentFolderId}", method = RequestMethod.GET)
	public Result getProjectSubDocsPage(@ApiParam(value = "每页大小") @PathVariable int pageSize,
			@ApiParam(value = "页码") @PathVariable int pageNo,
			@ApiParam(value = "父文件夹id") @PathVariable String parentFolderId) {
		
		return super.successResult(this.projectMaterialService.getProjectSubDocsPage(pageSize, pageNo, "", parentFolderId));
	}
}
