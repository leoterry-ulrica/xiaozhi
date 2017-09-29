package com.dist.bdf.consumer.sga;

import java.io.File;
import java.util.List;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.dist.bdf.base.controller.BaseController;
import com.dist.bdf.base.exception.BusinessException;
import com.dist.bdf.base.result.Result;
import com.dist.bdf.base.utils.Base64Utils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import com.dist.bdf.facade.service.sga.SgaCompanyService;
import com.dist.bdf.common.conf.imgserver.ImgServerConf;
import com.dist.bdf.common.constants.GlobalSystemParameters;
import com.dist.bdf.model.dto.sga.CompanyInfoAddDTO;
import com.dist.bdf.model.dto.sga.CompanyInfoResponseDTO;
import com.dist.bdf.model.dto.sga.ImgInfo;
import com.dist.bdf.model.entity.sga.SgaCompany;

@Api(tags = { "API-公共：企业信息模块" }, description = "SgaCompanyController")
@RestController
@RequestMapping(value = "/rest/sysservice/sga")
//@CrossOrigin(origins = "*")
public class SgaCompanyController extends BaseController {

	@Autowired
	private SgaCompanyService sgaComService;
	@Autowired
	private ImgServerConf imgServerConf;
	/**
	 * 内部获取企业logo信息
	 * 
	 * @param ci
	 * @return
	 */
	/*private String getComLogoURLInner(SgaComInfo ci) {
		String url = super.getBaseURL();
		if (StringUtils.isEmpty(ci.getLogo())) {
			url = super.getBaseURL() + GlobalSystemParameters.DefaultLogoPath + "/"
					+ StringUtil.getPYIndexStr(ci.getShortName(), false).substring(0, 1).toLowerCase() + ".jpg";
		} else if (new File(super.getContextPath() + ci.getLogo()).exists()) {
			url = super.getBaseURL() + ci.getLogo();
		} else {
			url = super.getBaseURL() + GlobalSystemParameters.DefaultLogoPath + "/"
					+ StringUtil.getPYIndexStr(ci.getShortName(), false).substring(0, 1).toLowerCase() + ".jpg";
		}
		return url;
	}*/
	/**
	 * 获取logo链接
	 * @param name
	 * @param logo
	 * @return
	 */
	private String getCompanyLogoURLInner(String name, String logo) {
		String url = super.getBaseURL();
		if (StringUtils.isEmpty(logo)) {
			url = super.getBaseURL() + GlobalSystemParameters.DEFAULT_LOGO_PATH + "/"
					+ "logo.jpg";//StringUtil.getPYIndexStr(name, false).substring(0, 1).toLowerCase() + ".jpg";
		} else if (logo.contains("/fs/")) {
			url = this.imgServerConf.getServerURI().endsWith("/") ?  this.imgServerConf.getServerURI() + logo : this.imgServerConf.getServerURI() + "/" + logo;
		}  else if (new File(super.getContextPath() + logo).exists()) {
			url = super.getBaseURL() + logo;
		} else {
			if(logo.toLowerCase().startsWith("http")){
				return logo;
			}
			url = super.getBaseURL() + GlobalSystemParameters.DEFAULT_LOGO_PATH + "/"
					+ "logo.jpg";//StringUtil.getPYIndexStr(name, false).substring(0, 1).toLowerCase() + ".jpg";
		}
		return url;
	}

	/**
	 * 内部获取企业背景图url
	 * 
	 * @param ci
	 * @return
	 */
	/*private String getComImgURLInner(SgaComInfo ci) {
		String url = super.getBaseURL();
		if (StringUtils.isEmpty(ci.getImg())) {
			url = super.getBaseURL() + GlobalSystemParameters.DefaultImgPath + "/"
					+ StringUtil.getPYIndexStr(ci.getShortName(), false).substring(0, 1).toLowerCase() + ".jpg";
		} else if (new File(super.getContextPath() + ci.getImg()).exists()) {
			url = super.getBaseURL() + ci.getImg();
		} else {
			url = super.getBaseURL() + GlobalSystemParameters.DefaultImgPath + "/"
					+ StringUtil.getPYIndexStr(ci.getShortName(), false).substring(0, 1).toLowerCase() + ".jpg";
		}
		return url;
	}*/
	/**
	 * 获取背景图链接
	 * @param name
	 * @param img
	 * @return
	 */
	private String getCompanyImgURLInner(String name, String img) {
		String url = super.getBaseURL();
		if (StringUtils.isEmpty(img)) {
			url = super.getBaseURL() + GlobalSystemParameters.DEFAULT_IMG_PATH + "/"
					+ "img.jpg";//StringUtil.getPYIndexStr(name, false).substring(0, 1).toLowerCase() + ".jpg";
		} else if (img.contains("/fs/")) {
			url = this.imgServerConf.getServerURI().endsWith("/") ?  this.imgServerConf.getServerURI() + img : this.imgServerConf.getServerURI() + "/" + img;
		} else if (new File(super.getContextPath() + img).exists()) {
			url = super.getBaseURL() + img;
		} else {
			if(img.toLowerCase().startsWith("http")){
				return img;
			}
			url = super.getBaseURL() + GlobalSystemParameters.DEFAULT_IMG_PATH + "/"
					+ "img.jpg";//StringUtil.getPYIndexStr(name, false).substring(0, 1).toLowerCase() + ".jpg";
		}
		return url;
	}

	/**
	 * 保存企业logo
	 * 
	 * @param logoInfo
	 * @return
	 */
	/*
	 * private Result setCompanyLogoInner(String logoInfo) { try { JSONObject
	 * jsonObj = JSONObject.parseObject(logoInfo); Long comId =
	 * jsonObj.getLong("id"); String type = jsonObj.getString("type"); String
	 * suffix = jsonObj.getString("suffix");
	 * 
	 * logger.info(">>>comId:[{}], mime-type:[{}], suffix:[{}] ", comId, type,
	 * suffix);
	 * 
	 * String newFileName = System.currentTimeMillis() + (suffix.startsWith(".")
	 * ? suffix : "." + suffix); String localpath =
	 * super.getContextPath(GlobalSystemParameters.LogoPath) + "/" +
	 * newFileName;
	 * 
	 * Base64Utils.decodeToFile(localpath, jsonObj.getString("content"));
	 * 
	 * if (!new File(localpath).exists()) throw new BusinessException("上传文件失败");
	 * 
	 * String logoRelativePath = GlobalSystemParameters.LogoPath + "/" +
	 * newFileName;
	 * 
	 * // 删除旧的头像 CompanyInfoResponseDTO comInfo =
	 * this.sgaComService.getComInfoById(comId); if (null == comInfo) { throw
	 * new BusinessException(String.format("id为[%s]的企业不存在", comInfo)); }
	 * 
	 * if (!StringUtils.isEmpty(comInfo.getLogo()) && -1 ==
	 * comInfo.getLogo().toLowerCase().indexOf("default")) { File preAvatarPath
	 * = new File(super.getContextPath() + "/" + comInfo.getLogo()); if
	 * (preAvatarPath.exists()) { // 删除原来的头像 boolean result =
	 * preAvatarPath.delete(); if (!result) {
	 * logger.info(">>>删除企业原logo文件失败：[{}]", preAvatarPath); } } }
	 * 
	 * String logoURL = this.sgaComService.changeLogo(comId, logoRelativePath);
	 * logoURL = super.getBaseURL() + logoRelativePath; System.out.println(
	 * ">>>logo url：" + logoURL); return super.successResult(logoURL);
	 * 
	 * } catch (Exception ex) { ex.printStackTrace(); }
	 * logger.info(">>>上传文件失败......"); return super.failResult("上传文件失败"); }
	 */

	/*
	 * private Result setCompanyLogoInner(ImgInfo logoInfo) { try { //
	 * JSONObject jsonObj = JSONObject.parseObject(logoInfo); Long comId =
	 * Long.parseLong(logoInfo.getId().toString());// jsonObj.getLong("id");
	 * String type = logoInfo.getType();//jsonObj.getString("type"); String
	 * suffix = logoInfo.getSuffix();//jsonObj.getString("suffix");
	 * 
	 * logger.info(">>>comId:[{}], mime-type:[{}], suffix:[{}] ", comId, type,
	 * suffix);
	 * 
	 * String newFileName = System.currentTimeMillis() + (suffix.startsWith(".")
	 * ? suffix : "." + suffix); String localpath =
	 * super.getContextPath(GlobalSystemParameters.LogoPath) + "/" +
	 * newFileName;
	 * 
	 * Base64Utils.decodeToFile(localpath, logoInfo.getContent());//
	 * jsonObj.getString("content")
	 * 
	 * if (!new File(localpath).exists()) throw new BusinessException("上传文件失败");
	 * 
	 * String logoRelativePath = GlobalSystemParameters.LogoPath + "/" +
	 * newFileName;
	 * 
	 * // 删除旧的头像 CompanyInfoResponseDTO comInfo =
	 * this.sgaComService.getComInfoById(comId); if (null == comInfo) { throw
	 * new BusinessException(String.format("id为[%s]的企业不存在", comInfo)); }
	 * 
	 * if (!StringUtils.isEmpty(comInfo.getLogo()) && -1 ==
	 * comInfo.getLogo().toLowerCase().indexOf("default")) { File preAvatarPath
	 * = new File(super.getContextPath() + "/" + comInfo.getLogo()); if
	 * (preAvatarPath.exists()) { // 删除原来的头像 boolean result =
	 * preAvatarPath.delete(); if (!result) {
	 * logger.info(">>>删除企业原logo文件失败：[{}]", preAvatarPath); } } }
	 * 
	 * String logoURL = this.sgaComService.changeLogo(comId, logoRelativePath);
	 * logoURL = super.getBaseURL() + logoRelativePath; System.out.println(
	 * ">>>logo url：" + logoURL); return super.successResult(logoURL);
	 * 
	 * } catch (Exception ex) { ex.printStackTrace(); }
	 * logger.info(">>>上传文件失败......"); return super.failResult("上传文件失败"); }
	 */

	private Result setCompanyLogoInner(String id, String type, String suffix, String content) {
		try {
			// JSONObject jsonObj = JSONObject.parseObject(logoInfo);
			Long comId = Long.parseLong(id);// jsonObj.getLong("id");
			// String type = logoInfo.getType();//jsonObj.getString("type");
			// String suffix =
			// logoInfo.getSuffix();//jsonObj.getString("suffix");

			LOG.info(">>>comId:[{}], mime-type:[{}], suffix:[{}] ", comId, type, suffix);

			String newFileName = System.currentTimeMillis() + (suffix.startsWith(".") ? suffix : "." + suffix);
			String localpath = super.getContextPath(GlobalSystemParameters.LOGO_PATH) + "/" + newFileName;

			Base64Utils.decodeToFile(localpath, content);// jsonObj.getString("content")

			if (!new File(localpath).exists())
				throw new BusinessException("上传文件失败");

			String logoRelativePath = GlobalSystemParameters.LOGO_PATH + "/" + newFileName;

			// 删除旧的头像
			CompanyInfoResponseDTO comInfo = this.sgaComService.getComInfoById(comId);
			if (null == comInfo) {
				throw new BusinessException(String.format("id为[%s]的企业不存在", comInfo));
			}

			if (!StringUtils.isEmpty(comInfo.getLogo()) && -1 == comInfo.getLogo().toLowerCase().indexOf("default")) {
				File preAvatarPath = new File(super.getContextPath() + "/" + comInfo.getLogo());
				if (preAvatarPath.exists()) {
					// 删除原来的头像
					boolean result = preAvatarPath.delete();
					if (!result) {
						LOG.info(">>>删除企业原logo文件失败：[{}]", preAvatarPath);
					}
				}
			}

			String logoURL = this.sgaComService.changeLogo(comId, logoRelativePath);
			logoURL = super.getBaseURL() + logoRelativePath;
			System.out.println(">>>logo url：" + logoURL);
			return super.successResult(logoURL);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		LOG.info(">>>上传文件失败......");
		return super.failResult("上传文件失败");
	}

	/**
	 * 
	 * @param imgInfo
	 * @return
	 */
	private Result setCompanyImgInner(String imgInfo) {
		try {
			JSONObject jsonObj = JSONObject.parseObject(imgInfo);
			Long comId = jsonObj.getLong("id");
			String type = jsonObj.getString("type");
			String suffix = jsonObj.getString("suffix");

			LOG.info(">>>comId:[{}], mime-type:[{}], suffix:[{}] ", comId, type, suffix);

			String newFileName = System.currentTimeMillis() + (suffix.startsWith(".") ? suffix : "." + suffix);
			String localpath = super.getContextPath(GlobalSystemParameters.IMG_PATH) + "/" + newFileName;

			Base64Utils.decodeToFile(localpath, jsonObj.getString("content"));

			if (!new File(localpath).exists())
				throw new BusinessException("上传文件失败");

			String logoRelativePath = GlobalSystemParameters.IMG_PATH + "/" + newFileName;

			// 删除旧的头像
			CompanyInfoResponseDTO comInfo = this.sgaComService.getComInfoById(comId);
			if (null == comInfo) {
				throw new BusinessException(String.format("id为[%s]的企业不存在", comInfo));
			}

			if (!StringUtils.isEmpty(comInfo.getImg()) && -1 == comInfo.getImg().toLowerCase().indexOf("default")) {
				File preAvatarPath = new File(super.getContextPath() + "/" + comInfo.getImg());
				if (preAvatarPath.exists()) {
					// 删除原来的头像
					boolean result = preAvatarPath.delete();
					if (!result) {
						LOG.info(">>>删除企业原img文件失败：[{}]", preAvatarPath);
					}
				}
			}

			String logoURL = this.sgaComService.changeImg(comId, logoRelativePath);
			logoURL = super.getBaseURL() + logoRelativePath;
			System.out.println(">>>logo url：" + logoURL);
			return super.successResult(logoURL);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		LOG.info(">>>上传文件失败......");
		return super.failResult("上传文件失败");
	}

	private Result setCompanyImgInner(String id, String type, String suffix, String content) {
		try {
			// JSONObject jsonObj = JSONObject.parseObject(imgInfo);
			Long comId = Long.parseLong(id);// jsonObj.getLong("id");
			// String type = imgInfo.getType(); // jsonObj.getString("type");
			// String suffix = imgInfo.getSuffix(); //
			// jsonObj.getString("suffix");

			LOG.info(">>>comId:[{}], mime-type:[{}], suffix:[{}] ", comId, type, suffix);

			String newFileName = System.currentTimeMillis() + (suffix.startsWith(".") ? suffix : "." + suffix);
			String localpath = super.getContextPath(GlobalSystemParameters.IMG_PATH) + "/" + newFileName;

			Base64Utils.decodeToFile(localpath, content); // jsonObj.getString("content")

			if (!new File(localpath).exists())
				throw new BusinessException("上传文件失败");

			String logoRelativePath = GlobalSystemParameters.IMG_PATH + "/" + newFileName;

			// 删除旧的头像
			CompanyInfoResponseDTO comInfo = this.sgaComService.getComInfoById(comId);
			if (null == comInfo) {
				throw new BusinessException(String.format("id为[%s]的企业不存在", comInfo));
			}

			if (!StringUtils.isEmpty(comInfo.getImg()) && -1 == comInfo.getImg().toLowerCase().indexOf("default")) {
				File preAvatarPath = new File(super.getContextPath() + "/" + comInfo.getImg());
				if (preAvatarPath.exists()) {
					// 删除原来的头像
					boolean result = preAvatarPath.delete();
					if (!result) {
						LOG.info(">>>删除企业原img文件失败：[{}]", preAvatarPath);
					}
				}
			}

			String logoURL = this.sgaComService.changeImg(comId, logoRelativePath);
			logoURL = super.getBaseURL() + logoRelativePath;
			System.out.println(">>>logo url：" + logoURL);
			return super.successResult(logoURL);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		LOG.info(">>>上传文件失败......");
		return super.failResult("上传文件失败");
	}

	@Deprecated
	@ApiOperation(value = "根据id获取企业logo url。(有安全隐患，后续版本不再支持)", notes = "getComLogoURL")
	@RequestMapping(value = "/company/logo/{id}", method = { RequestMethod.GET })
	public Result getCompanyLogoURL(@PathVariable @ApiParam(value = "企业id") Long id) {

		CompanyInfoResponseDTO ci = this.sgaComService.getComInfoById(id);
		if (null == ci)
			return super.failResult("没有找到相应的企业信息，id [" + id + "]");

		String url = getCompanyLogoURLInner(ci.getName(), ci.getLogo());
		return super.successResult(url);
	}
	@ApiOperation(value = "根据编码获取企业logo url。", notes = "getLogoURLByCompanyCode")
	@RequestMapping(value = "/company/logo/code/{code}", method = { RequestMethod.GET })
	public Result getLogoURLByCompanyCode(@PathVariable @ApiParam(value = "企业编码") String code) {

		SgaCompany company = this.sgaComService.getComInfoByCode(code);
		if (null == company)
			return super.failResult("没有找到相应的企业信息，code [" + company + "]");

		String url = getCompanyLogoURLInner(company.getName(), company.getLogo());
		return super.successResult(url);
	}

	@Deprecated
	@ApiOperation(value = "根据id获取企业图片 url。(有安全隐患，后续版本不再支持)", notes = "getComImgURL")
	@RequestMapping(value = "/company/img/{id}", method = { RequestMethod.GET })
	public Result getCompanyImgURL(@PathVariable @ApiParam(value = "企业id") Long id) {

		CompanyInfoResponseDTO ci = this.sgaComService.getComInfoById(id);
		if (null == ci)
			return super.failResult("没有找到相应的企业信息，id [" + id + "]");

		String url = getCompanyImgURLInner(ci.getName(), ci.getImg());
		return super.successResult(url);
	}
	@ApiOperation(value = "根据企业编码获取企业图片 url。", notes = "getImgURLByCompanyCode")
	@RequestMapping(value = "/company/img/code/{code}", method = { RequestMethod.GET })
	public Result getImgURLByCompanyCode(@PathVariable @ApiParam(value = "企业编码") String code) {

		SgaCompany company = this.sgaComService.getComInfoByCode(code);
		if (null == company)
			return super.failResult("没有找到相应的企业信息，code [" + code + "]");

		return super.successResult(getCompanyImgURLInner(company.getName(), company.getImg()));
	}

	@ApiOperation(value = "根据企业id，获取有效的企业信息。", notes = "getCompanyById")
	@RequestMapping(value = "/company/id/{id}", method = { RequestMethod.GET })
	public Result getCompanyById(@ApiParam(value = "企业的序列id号") @PathVariable Long id) {

		CompanyInfoResponseDTO cir = this.sgaComService.getComInfoById(id);
		cir.setImg(this.getCompanyImgURLInner(cir.getName(), cir.getImg()));
		cir.setLogo(this.getCompanyLogoURLInner(cir.getName(), cir.getLogo()));

		return super.successResult(cir);
	}
	
	@ApiOperation(value = "根据企业的域realm，获取有效的企业信息", notes = "getCompanyByRealm")
	@RequestMapping(value = "/company/realm/{realm}", method = { RequestMethod.GET })
	public Result getCompanyByRealm(@ApiParam(value = "企业的域") @PathVariable String realm) {

		CompanyInfoResponseDTO cir = this.sgaComService.getComInfoByRealm(realm);	  
		cir.setImg(this.getCompanyImgURLInner(cir.getName(), cir.getImg()));
		cir.setLogo(this.getCompanyLogoURLInner(cir.getName(), cir.getLogo()));

		return super.successResult(cir);
	}
	
	@ApiOperation(value = "根据企业的域realm，获取公开的企业信息。", notes = "getPubCompanyByRealm")
	@RequestMapping(value = "/company/public/realm/{realm}", method = { RequestMethod.GET })
	public Result getPubCompanyByRealm(
			@ApiParam(value = "企业的域") 
			@PathVariable String realm){

		CompanyInfoResponseDTO cir = this.sgaComService.getPubComInfoByRealm(realm);	
		cir.setImg(this.getCompanyImgURLInner(cir.getName(), cir.getImg()));
		cir.setLogo(this.getCompanyLogoURLInner(cir.getName(), cir.getLogo()));

		return super.successResult(cir);
	}


	@ApiOperation(value = "获取有效的企业信息。", notes = "listValidCompany")
	@RequestMapping(value = "/company/valid", method = { RequestMethod.GET })
	public Result listValidCompany() throws Exception {

		List<CompanyInfoResponseDTO> cominfos = (List<CompanyInfoResponseDTO>) this.sgaComService.listValidCompany();
		if(null == cominfos || cominfos.isEmpty()) {
			return super.successResult("没有找到有效的企业信息数据");
		}
		for(CompanyInfoResponseDTO com : cominfos) {
			com.setLogo(this.getCompanyLogoURLInner(com.getName(), com.getLogo()));
			com.setImg(this.getCompanyImgURLInner(com.getName(), com.getImg()));
		}
	
		return super.successResult(cominfos);
	}
	
	@ApiOperation(value = "获取公开的企业信息。", notes = "listPublicCompany")
	@RequestMapping(value = "/company/public", method = { RequestMethod.GET })
	public Result listPublicCompany() throws Exception {

		List<CompanyInfoResponseDTO> comInfos = (List<CompanyInfoResponseDTO>) this.sgaComService.listPublicCompany();
		if(null == comInfos || comInfos.isEmpty()) {
			return super.successResult("没有找到相关企业信息数据");
		}
		for(CompanyInfoResponseDTO com : comInfos) {
			com.setLogo(this.getCompanyLogoURLInner(com.getName(), com.getLogo()));
			com.setImg(this.getCompanyImgURLInner(com.getName(), com.getImg()));
		}

		return super.successResult(comInfos);
	}

	@ApiOperation(value = "添加或修改企业信息。", notes = "addOrUpdateCompany")
	@RequestMapping(value = "/company", method = { RequestMethod.POST, RequestMethod.PUT  })
	public Result addOrUpdateCompany(@RequestBody @Valid @ApiParam(value = "企业信息") CompanyInfoAddDTO dto,
			BindingResult result) {

		if (result.hasErrors())
			return super.failResult(result.toString());

		SgaCompany com = (SgaCompany) this.sgaComService.addOrUpdate(dto);
		Result resultObj = null;

		if (dto.getLogoInfo() != null && !StringUtils.isEmpty(dto.getLogoInfo().getContent())) {
			JSONObject logoJson = JSONObject.parseObject(JSONObject.toJSONString(dto.getLogoInfo()));
			logoJson.put("id", com.getId());
			resultObj = this.setCompanyLogoInner(com.getId().toString(), logoJson.getString("type"),
					logoJson.getString("suffix"), logoJson.getString("content"));
			if (resultObj.getStatus().equalsIgnoreCase("success"))
				com.setLogo(resultObj.getData().toString());
		}

		if (dto.getImgInfo() != null && !StringUtils.isEmpty(dto.getImgInfo().getContent())) {
			JSONObject imgJson = JSONObject.parseObject(JSONObject.toJSONString(dto.getImgInfo()));
			imgJson.put("id", com.getId());
			resultObj = this.setCompanyImgInner(imgJson.toJSONString());
			if (resultObj.getStatus().equalsIgnoreCase("success"))
				com.setImg(resultObj.getData().toString());
		}

		return super.successResult(com);
	}
	
	@ApiOperation(value = "添加或修改企业信息。", notes = "addOrUpdateCompanyV1")
	@RequestMapping(value = "/v1/company", method = { RequestMethod.POST, RequestMethod.PUT  })
	public Result addOrUpdateCompanyV1(@RequestBody @Valid @ApiParam(value = "企业信息") CompanyInfoAddDTO dto,
			BindingResult result) {

		if (result.hasErrors())
			return super.failResult(result.toString());

		SgaCompany com = (SgaCompany) this.sgaComService.addOrUpdate(dto);
		if (dto.getLogoInfo() != null && !StringUtils.isEmpty(dto.getLogoInfo().getContent())) {
			this.sgaComService.deleteLogoByCompanyCode(com.getSysCode());
			dto.getLogoInfo().setId(com.getSysCode());
			String relativePath = this.sgaComService.updateImg(dto.getLogoInfo());
			com.setLogo(this.getCompanyLogoURLInner("", relativePath));
		}

		if (dto.getImgInfo() != null && !StringUtils.isEmpty(dto.getImgInfo().getContent())) {
			this.sgaComService.deleteImgByCompanyCode(com.getSysCode());
			dto.getImgInfo().setId(com.getSysCode());
			String relativePath = this.sgaComService.updateImg(dto.getImgInfo());
			com.setImg(this.getCompanyImgURLInner("", relativePath));
		}

		return super.successResult(com);
	}

	/*@ApiOperation(value = "/company", notes = "修改企业信息。")
	@RequestMapping(value = "/company", method = { RequestMethod.PUT })
	public Result updateCompany(@RequestBody @Valid @ApiParam(value = "企业信息") CompanyInfoUpdateDTO dto,
			BindingResult result) {

		if (result.hasErrors())
			return super.failResult(result.toString());

		SgaComInfo com = (SgaComInfo) this.sgaComService.updateBasicInfo(dto);

		Result resultObj = null;

		if (dto.getLogoInfo() != null && !StringUtils.isEmpty(dto.getLogoInfo().getContent())) {
			JSONObject logoJson = JSONObject.parseObject(JSONObject.toJSONString(dto.getLogoInfo()));
			logoJson.put("id", com.getId());
			resultObj = this.setCompanyLogoInner(com.getImg().toString(), logoJson.getString("type"),
					logoJson.getString("suffix"), logoJson.getString("content"));
			if (resultObj.getStatus().equalsIgnoreCase("success"))
				com.setLogo(resultObj.getData().toString());
		}

		if (dto.getImgInfo() != null && !StringUtils.isEmpty(dto.getImgInfo().getContent())) {
			JSONObject imgJson = JSONObject.parseObject(JSONObject.toJSONString(dto.getImgInfo()));
			imgJson.put("id", com.getId());
			resultObj = this.setCompanyImgInner(imgJson.toJSONString());
			if (resultObj.getStatus().equalsIgnoreCase("success"))
				com.setImg(resultObj.getData().toString());
		}

		return super.successResult(com);
	}
*/
	@Deprecated
	@ApiOperation(value = "设置企业logo", notes = "uploadCompanyLogo")
	@RequestMapping(value = "/company/logo", method = { RequestMethod.POST, RequestMethod.PUT })
	public Result uploadCompanyLogo(
			@ApiParam(value = "json数据，{\"id\":企业序列id号,\"type\":\"image/jpeg\",\"suffix\":\"jpg\",\"content\":\"base64值\"}", required = true) @RequestBody ImgInfo logoInfo) {
		// String logoInfo) {

		Assert.notNull(logoInfo.getId());
		Assert.hasLength(logoInfo.getContent());
		Assert.hasLength(logoInfo.getSuffix());

		return setCompanyLogoInner(logoInfo.getId(), logoInfo.getType(), logoInfo.getSuffix(), logoInfo.getContent());
	}
	@ApiOperation(value = "设置企业logo", notes = "uploadCompanyLogoV1")
	@RequestMapping(value = "/v1/company/logo", method = { RequestMethod.POST, RequestMethod.PUT })
	public Result uploadCompanyLogoV1(
			@ApiParam(value = "json数据，{\"id\":企业编码，如：85647EEF-00F5-44C9-A372-E2D0B98659DE,\"type\":\"image/jpeg\",\"suffix\":\"jpg\",\"content\":\"base64值\"}", required = true) 
			@Valid
			@RequestBody ImgInfo imgInfo, BindingResult result) {

		if(result.hasErrors()) {
			return super.errorResult("参数验证失败，详情："+result.toString());
		}
		this.sgaComService.deleteLogoByCompanyCode(imgInfo.getId());
		String relativePath = this.sgaComService.updateLogo(imgInfo);
		if(StringUtils.isEmpty(relativePath)) {
			return super.failResult("企业logo修改失败");
		}
		return super.successResult(this.imgServerConf.getServerURI() + relativePath);
	}
	@Deprecated
	@ApiOperation(value = "设置企业图片", notes = "uploadCompanyImg")
	@RequestMapping(value = "/company/img", method = { RequestMethod.POST, RequestMethod.PUT })
	public Result uploadCompanyImg(
			@ApiParam(value = "json数据，{\"id\":企业序列id号,\"type\":\"image/jpeg\",\"suffix\":\"jpg\",\"content\":\"base64值\"}", required = true) @RequestBody ImgInfo imgInfo) {

		Assert.notNull(imgInfo.getId());
		Assert.hasLength(imgInfo.getContent());
		Assert.hasLength(imgInfo.getSuffix());

		return setCompanyImgInner(imgInfo.getId(), imgInfo.getType(), imgInfo.getSuffix(), imgInfo.getContent());
	}
	
	@ApiOperation(value = "设置企业图片", notes = "uploadCompanyImgV1")
	@RequestMapping(value = "/v1/company/img", method = { RequestMethod.POST, RequestMethod.PUT })
	public Result uploadCompanyImgV1(
			@ApiParam(value = "json数据，{\"id\":企业编码，如：85647EEF-00F5-44C9-A372-E2D0B98659DE,\"type\":\"image/jpeg\",\"suffix\":\"jpg\",\"content\":\"base64值\"}", required = true) 
			@Valid
			@RequestBody ImgInfo imgInfo, BindingResult result) {

		if(result.hasErrors()) {
			return super.errorResult("参数验证失败，详情："+result.toString());
		}
		this.sgaComService.deleteImgByCompanyCode(imgInfo.getId());
		String relativePath = this.sgaComService.updateImg(imgInfo);
		if(StringUtils.isEmpty(relativePath)) {
			return super.failResult("企业背景图修改失败");
		}
		return super.successResult(this.imgServerConf.getServerURI() + relativePath);
	}

	@ApiOperation(value = "根据id删除企业", notes = "deleteCompanyById")
	@RequestMapping(value = "/company/id/{id}", method = { RequestMethod.DELETE })
	public Result deleteCompanyById(@ApiParam(value = "企业的序列id号") @PathVariable Long id) {

		return super.successResult(this.sgaComService.deleteCompanyById(id));
	}
	
	@ApiOperation(value = "根据企业域（realm）修改企业状态", notes = "changeCompanyStatusByRealm")
	@RequestMapping(value = "/company/status/{realm}/{status}", method = { RequestMethod.PUT})
	public Result changeCompanyStatusByRealm(
			@ApiParam(value = "企业的域（realm）") 
			@PathVariable String realm, 
			@ApiParam(value = "企业状态。-1：删除:0：关闭；1：正常；") 
			@PathVariable int status) {

		return super.successResult(this.sgaComService.changeCompanyStatusByRealm(realm, status));
	}
	@ApiOperation(value = "根据企业域（realm）同步企业审计信息", notes = "syncCompanyAuditByRealm")
	@RequestMapping(value = "/company/audit/sync/{realm}", method = { RequestMethod.POST, RequestMethod.PUT})
	public Result syncCompanyAuditByRealm(
			@ApiParam(value = "企业的域（realm）") 
			@PathVariable String realm) {

		return super.successResult(this.sgaComService.syncCompanyAudit(realm));
	}
}
