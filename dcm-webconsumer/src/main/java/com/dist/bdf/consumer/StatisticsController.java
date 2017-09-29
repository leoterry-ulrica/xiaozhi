package com.dist.bdf.consumer;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.servlet.ServletOutputStream;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dist.bdf.base.controller.BaseController;
import com.dist.bdf.base.office.excel.ExcelDataFormatter;
import com.dist.bdf.base.office.excel.ExcelUtils;
import com.dist.bdf.base.office.freemarker.DocumentHandler;
import com.dist.bdf.base.page.Pagination;
import com.dist.bdf.base.result.Result;
import com.dist.bdf.base.utils.DateUtil;
import com.dist.bdf.base.utils.StringUtil;
import com.dist.bdf.base.utils.UUIDGenerator;
import com.dist.bdf.facade.service.MaterialService;
import com.dist.bdf.facade.service.ProjectService;
import com.dist.bdf.facade.service.UserOrgService;
import com.dist.bdf.common.constants.GlobalSystemParameters;
import com.dist.bdf.model.dto.dcm.CaseDTO;
import com.dist.bdf.model.dto.system.ProjectExcelDTO;
import com.dist.bdf.model.dto.system.ProjectSummaryFilterDTO;
import com.dist.bdf.model.dto.system.TaskAndProjectRespDTO;
import com.dist.bdf.model.dto.system.TaskSummaryFilterDTO;
import com.dist.bdf.model.dto.system.iso.ISOIndexDTO;
import com.dist.bdf.model.dto.system.iso.ISOProjectInfoDTO;
import com.dist.bdf.model.dto.system.user.UserSimpleDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api(tags = { "API-统计服务模块" }, description = "StatisticsController")
@RestController
@RequestMapping(value = "/rest/sysservice")
//@CrossOrigin(origins = "*")
public class StatisticsController extends BaseController {

	@Autowired
	private ProjectService projectService;
	@Autowired
	private UserOrgService userService;
	@Autowired
	@Qualifier("CommonMaterialService")
	private MaterialService commonMaterialService;

	// ======>微作模块end
	@Deprecated
	@ApiOperation(value = "获取项目统计信息", response = Result.class, notes = "getStatInfo")
	@RequestMapping(value = "/getProjectStatInfo/{caseIdentifier}", method = RequestMethod.GET)
	public Result getStatInfo(
			@ApiParam(value = "案例标识，如：XZ_CASETYPE_JYXM_000000100008") @PathVariable String caseIdentifier) {

		Object result = this.projectService.getStatInfo(caseIdentifier);
		return super.successResult(result);
	}

	@ApiOperation(value = "根据case id获取项目统计信息", response = Result.class, notes = "getStatInfo")
	@RequestMapping(value = "/getProjectStatInfo/{realm}/{caseId}", method = RequestMethod.GET)
	public Result getStatInfo(@ApiParam(value = "域") @PathVariable String realm,
			@ApiParam(value = "案例id，如：{C0E2AD57-0000-CD16-84DA-F50B03C0A9C6}") @PathVariable String caseId) {

		Object result = this.projectService.getStatInfo(realm, caseId);
		return super.successResult(result);
	}

	@ApiOperation(value = "根据case id获取项目统计信息", response = Result.class, notes = "getStatInfoEx")
	@RequestMapping(value = "/v1/projectStatInfo/{realm}/{caseId}/{size}", method = RequestMethod.GET)
	public Result getStatInfoEx(@ApiParam(value = "域", required = true) @PathVariable String realm,
			@ApiParam(value = "案例id，如：{C0E2AD57-0000-CD16-84DA-F50B03C0A9C6}", required = true) @PathVariable String caseId,
			@ApiParam(value = "设置返回记录大小", required = true) @PathVariable int size) {

		return super.successResult(this.projectService.getStatInfoEx(realm, caseId, size));
	}

	@ApiOperation(value = "生成项目统计word文档", response = Result.class, notes = "generateProjStatDoc")
	@RequestMapping(value = "generateProjStatDoc/{parajson:.+}", method = { RequestMethod.GET, RequestMethod.POST })
	public Result exportProjStatToDoc(@PathVariable String parajson) throws Exception {

		Map<String, Object> data = new HashMap<String, Object>();

		JSONObject jsonObj = JSONObject.parseObject(parajson);
		// JSONObject infoObj = jsonObj.getJSONObject("info");
		String caseId = jsonObj.getString("caseId");
		CaseDTO caseDTO = this.projectService.getCaseByIdentifier(caseId);

		ISOProjectInfoDTO projInfo = new ISOProjectInfoDTO();
		// 注意：处理文本中的特殊字符，因为需要插入到word的XML文档中
		projInfo.setXMXX_XMMC(StringUtil.isNullOrEmpty(caseDTO.getProjectName()) ? ""
				: StringUtil.encodeSpecialString(caseDTO.getProjectName()));
		projInfo.setXMXX_RY_XMFZR(StringUtil.isNullOrEmpty(caseDTO.getProjectManager()) ? ""
				: StringUtil.encodeSpecialString(caseDTO.getProjectManager()));
		projInfo.setXMXX_TB_YBBM(StringUtil.isNullOrEmpty(caseDTO.getDepartmentYB()) ? ""
				: StringUtil.encodeSpecialString(caseDTO.getDepartmentYB()));
		projInfo.setXMXX_XMGHLB(StringUtil.isNullOrEmpty(caseDTO.getBusiness()) ? ""
				: StringUtil.encodeSpecialString(caseDTO.getBusiness()));
		projInfo.setXMXX_XMGM2(
				StringUtil.isNullOrEmpty(caseDTO.getScale()) ? "" : StringUtil.encodeSpecialString(caseDTO.getScale()));
		projInfo.setXMXX_XMHJYAQMB(StringUtil.isNullOrEmpty(caseDTO.getSafetyGoal()) ? ""
				: StringUtil.encodeSpecialString(caseDTO.getSafetyGoal()));
		projInfo.setCreateTime(DateUtil.toDateStr((new Date(caseDTO.getDateCreated()))));

		projInfo.setXMXX_XMQY(StringUtil.isNullOrEmpty(caseDTO.getProjectRegion()) ? ""
				: StringUtil.encodeSpecialString(caseDTO.getProjectRegion()));
		projInfo.setXMXX_YBYX_MXZBGCDZ(StringUtil.isNullOrEmpty(caseDTO.getProjectAddress()) ? ""
				: StringUtil.encodeSpecialString(caseDTO.getProjectAddress()));
		projInfo.setXMXX_YBYX_XMJBQK(StringUtil.isNullOrEmpty(caseDTO.getBasicInfo()) ? ""
				: StringUtil.encodeSpecialString(caseDTO.getBasicInfo()));
		projInfo.setXMXX_YBYX_ZBDW2(StringUtil.isNullOrEmpty(caseDTO.getUnitZB()) ? "" : caseDTO.getUnitZB());
		projInfo.setXMXX_YBYX_ZBJG(StringUtil.isNullOrEmpty(caseDTO.getOrgZB()) ? "" : caseDTO.getOrgZB());
		data.put("info", projInfo);

		JSONArray array = jsonObj.getJSONArray("stats");
		List<ISOIndexDTO> index = new ArrayList<ISOIndexDTO>(array.size());
		int top = (array.size() < 5) ? array.size() : 5;
		List<ISOIndexDTO> indexTop = new ArrayList<ISOIndexDTO>(top);

		for (int i = 0; i < array.size(); i++) {
			ISOIndexDTO indexDTO = new ISOIndexDTO();
			JSONObject indexJSObj = array.getJSONObject(i);
			JSONObject roleObj = indexJSObj.getJSONObject("role");
			JSONObject indexObj = indexJSObj.getJSONObject("index");
			indexDTO.setCountOfDoc(indexObj.getIntValue("countOfDoc"));
			indexDTO.setCountOfLike(indexObj.getIntValue("countOfLike"));
			indexDTO.setCountOfWz(indexObj.getIntValue("countOfWz"));
			indexDTO.setRoleName(roleObj.getString("roleName"));

			index.add(indexDTO);
		}

		Collections.sort(index);
		// 列出前五名
		for (int i = 0; i < index.size(); i++) {

			index.get(i).setId(i + 1);
			if (i < top) {
				indexTop.add(index.get(i));
			}
		}

		data.put("indexTop", indexTop);
		data.put("index", index);
		String path = super.getContextPath(GlobalSystemParameters.DIR_FREEMARKER);
		DocumentHandler dh = new DocumentHandler();
		dh.createDoc(path, System.currentTimeMillis() + ".doc", "utf-8", "/template", "projectstats.ftl", data);
		File newFile = new File(dh.getReturnFileFullPath());
		if (newFile.exists()) {

			InputStream in = new FileInputStream(newFile);
			ServletOutputStream toClient = null;
			try {
				String contentType = "application/x-msdownload";
				toClient = response.getOutputStream();
				response.setContentType(contentType);
				LOG.info(">>>生成的文件名：[{}]", newFile.getName());
				response.setHeader("Content-disposition", "attachment;filename=" + newFile.getName());
				response.setContentLength(((Long) newFile.length()).intValue());

				byte[] buffer = new byte[1024 * 10];
				int length = 0;
				while ((length = in.read(buffer, 0, buffer.length)) != -1) {
					toClient.write(buffer, 0, length);
				}

			} catch (Exception ex) {
				LOG.error(ex.getMessage(), ex);
			} finally {
				try {
					// 关闭流
					if (toClient != null) {
						toClient.close();
						toClient.flush();
					}
					if (in != null)
						in.close();
					if (!newFile.delete()) {
						LOG.info(">>>删除临时文件失败：[{}]", newFile.getName());
					} else {
						LOG.info(">>>成功删除临时文件：[{}]", newFile.getName());
					}

				} catch (IOException io) {
					LOG.error(io.getMessage(), io);
					// io.printStackTrace();
				}
			}
		}
		return super.successResult("生成完成。");
	}

	@Deprecated
	@ApiOperation(value = "任务汇总，附带过滤器（考虑到参数比较长，使用POST方式）。", response = Result.class, notes = "getTaskSummary")
	@RequestMapping(value = "/v1/tasks/summary", method = { RequestMethod.POST })
	public Result getTaskSummaryByFilter(@RequestBody TaskSummaryFilterDTO filterInfo) {

		return super.successResult(this.projectService.getTaskSummary(filterInfo));
	}

	@ApiOperation(value = "任务汇总，附带过滤器（考虑到参数比较长，使用POST方式）。", response = Result.class, notes = "getTaskSummaryByFilterEx")
	@RequestMapping(value = "/v2/tasks/summary", method = { RequestMethod.POST })
	public Result getTaskSummaryByFilterEx(@RequestBody TaskSummaryFilterDTO filterInfo) {

		return super.successResult(this.projectService.getTaskSummaryEx(filterInfo));
	}
	@ApiOperation(value = "根据权限进行全局任务汇总，还是所在部门任务汇总，附带过滤器（考虑到参数比较长，使用POST方式）。", response = Result.class, notes = "getTaskSummaryByFilterNew")
	@RequestMapping(value = "/v3/tasks/summary", method = { RequestMethod.POST })
	public Result getTaskSummaryByFilterNew(@RequestBody TaskSummaryFilterDTO filterInfo) {

		return super.successResult(this.projectService.getTaskSummaryNew(filterInfo));
	}

	@ApiOperation(value = "任务汇总导出excel，返回下载URL。", response = Result.class, notes = "exportTaskSummaryToExcel")
	@RequestMapping(value = "/v1/task/summaryexp/excel", method = { RequestMethod.POST })
	public Result exportTaskSummaryToExcel(@RequestBody TaskSummaryFilterDTO filterInfo) throws Exception {

		// 构造数据对象
		Pagination pageData = this.projectService.getTaskSummaryEx(filterInfo);
		ExcelDataFormatter edf = new ExcelDataFormatter();
		Map<String, String> localSupportFormatMap = new HashMap<String, String>();
		localSupportFormatMap.put("0", "是");
		localSupportFormatMap.put("1", "否");
		edf.set("localSupport", localSupportFormatMap);
		Map<String, String> solutionFormatMap = new HashMap<String, String>();
		solutionFormatMap.put("0", "是");
		solutionFormatMap.put("1", "否");
		edf.set("solutionTemp", solutionFormatMap);
		Map<String, String> hardwareReqFormatMap = new HashMap<String, String>();
		hardwareReqFormatMap.put("0", "是");
		hardwareReqFormatMap.put("1", "否");
		edf.set("hardwareReq", hardwareReqFormatMap);
		Map<String, String> statusFormatMap = new HashMap<String, String>();
		statusFormatMap.put("585d68", "待接受");
		statusFormatMap.put("0032FF", "进行中");
		statusFormatMap.put("00C800", "已完成");
		statusFormatMap.put("FF6432", "停止");
		edf.set("status", statusFormatMap);
		LOG.info(">>>获取用户列表");
		List<UserSimpleDTO> userDTOs = this.userService.listSimpleUsers("", "", filterInfo.getRealm());
		if (userDTOs != null && !userDTOs.isEmpty()) {

			// key：用户编码，value：用户别名
			Map<String, String> userCode2nameMap = new HashMap<String, String>();
			// key：用户登录名，value：用户别名
			Map<String, String> userLoginname2nameMap = new HashMap<String, String>();
			for (UserSimpleDTO u : userDTOs) {
				if (!userCode2nameMap.containsKey(u.getUserCode())) {
					userCode2nameMap.put(u.getUserCode(), u.getUserName());
				}
				if (!userLoginname2nameMap.containsKey(u.getLoginName())) {
					userCode2nameMap.put(u.getLoginName(), u.getUserName());
				}
			}

			edf.set("projectManager", userCode2nameMap);
			edf.set("projectAssistant", userCode2nameMap);
			edf.set("associatePerson", userCode2nameMap);

			edf.set("publisher", userLoginname2nameMap);
		}
		// Workbook workbook = ExcelUtils.getWorkBook(pageData.getData(), edf);
		String path = super.getContextPath(GlobalSystemParameters.DIR_FREEMARKER);
		File outFile = new File(path + "/" + UUID.randomUUID().toString() + ".xlsx");
		/*
		 * logger.info(">>>根据index排序");
		 * Collections.sort((List<TaskAndProjectRespDTO>) pageData.getData());
		 */
		LOG.info(">>>写入本地文件");
		ExcelUtils.writeToFile(pageData.getData(), edf, outFile.getAbsolutePath(), "项目任务汇总",
				TaskAndProjectRespDTO.class);
		if (outFile.exists()) {
			LOG.info(">>>生成的文件：[{}]", outFile.getAbsolutePath());
			return super.successResult(
					super.getBaseURL() + GlobalSystemParameters.DIR_FREEMARKER + "/" + outFile.getName());
		}
		return super.failResult("excel生成失败");
	}
	@ApiOperation(value = "项目信息汇总导出excel，返回下载URL。", response = Result.class, notes = "exportProjectSummaryToExcel")
	@RequestMapping(value = "/v1/project/summaryexp/excel", method = { RequestMethod.POST })
	public Result exportProjectSummaryToExcel(@RequestBody ProjectSummaryFilterDTO filterInfo) throws Exception {

		// 构造数据对象
		Pagination pageData = this.projectService.getProjectInfoSummary(filterInfo.getRealm(), filterInfo.getUserCode(), filterInfo.getPageNo(), filterInfo.getPageSize(), new String[]{"XZ_CASETYPE_JYXM"});
		ExcelDataFormatter edf = new ExcelDataFormatter();
		Map<String, String> sfzb = new HashMap<String, String>();
		sfzb.put("true", "是");
		sfzb.put("false", "否");
		edf.set("zbjg", sfzb);
		String path = super.getContextPath(GlobalSystemParameters.DIR_FREEMARKER);
		File outFile = new File(path + "/" + UUID.randomUUID().toString() + ".xlsx");
		/*
		 * logger.info(">>>根据index排序");
		 * Collections.sort((List<TaskAndProjectRespDTO>) pageData.getData());
		 */
		LOG.info(">>>写入本地文件");
		ExcelUtils.writeToFile(pageData.getData(), edf, outFile.getAbsolutePath(), "项目信息汇总",
				ProjectExcelDTO.class);
		if (outFile.exists()) {
			LOG.info(">>>生成的文件：[{}]", outFile.getAbsolutePath());
			return super.successResult(
					super.getBaseURL() + GlobalSystemParameters.DIR_FREEMARKER + "/" + outFile.getName());
		}
		return super.failResult("excel生成失败");
	}

	@ApiOperation(value = "任务汇总导出excel，输出文件流，客户端直接下载。", response = Result.class, notes = "exportTaskSummaryToExcelEx")
	@RequestMapping(value = "/v2/task/summaryexp/excel", method = { RequestMethod.POST })
	public void exportTaskSummaryToExcelEx(@RequestBody TaskSummaryFilterDTO filterInfo) throws Exception {

		// 构造数据对象
		Pagination pageData = this.projectService.getTaskSummaryEx(filterInfo);
		ExcelDataFormatter edf = new ExcelDataFormatter();
		Map<String, String> localSupportFormatMap = new HashMap<String, String>();
		localSupportFormatMap.put("0", "是");
		localSupportFormatMap.put("1", "否");
		edf.set("localSupport", localSupportFormatMap);
		Map<String, String> solutionFormatMap = new HashMap<String, String>();
		solutionFormatMap.put("0", "是");
		solutionFormatMap.put("1", "否");
		edf.set("solutionTemp", solutionFormatMap);
		Map<String, String> hardwareReqFormatMap = new HashMap<String, String>();
		hardwareReqFormatMap.put("0", "是");
		hardwareReqFormatMap.put("1", "否");
		edf.set("hardwareReq", hardwareReqFormatMap);
		Map<String, String> statusFormatMap = new HashMap<String, String>();
		statusFormatMap.put("585d68", "待接受");
		statusFormatMap.put("0032FF", "进行中");
		statusFormatMap.put("00C800", "已完成");
		statusFormatMap.put("FF6432", "停止");
		edf.set("status", statusFormatMap);
		LOG.info(">>>获取用户列表");
		List<UserSimpleDTO> userDTOs = this.userService.listSimpleUsers("", "", filterInfo.getRealm());
		if (userDTOs != null && !userDTOs.isEmpty()) {
			// key：用户编码，value：用户别名
			Map<String, String> userCode2nameMap = new HashMap<String, String>();
			// key：用户登录名，value：用户别名
			Map<String, String> userLoginname2nameMap = new HashMap<String, String>();
			for (UserSimpleDTO u : userDTOs) {
				if (!userCode2nameMap.containsKey(u.getUserCode())) {
					userCode2nameMap.put(u.getUserCode(), u.getUserName());
				}
				if (!userLoginname2nameMap.containsKey(u.getLoginName())) {
					userCode2nameMap.put(u.getLoginName(), u.getUserName());
				}
			}

			edf.set("projectManager", userCode2nameMap);
			edf.set("projectAssistant", userCode2nameMap);
			edf.set("associatePerson", userCode2nameMap);
			edf.set("publisher", userLoginname2nameMap);
		}
		Workbook workbook = ExcelUtils.getWorkBook(pageData.getData(), edf, "项目任务汇总", TaskAndProjectRespDTO.class);
		OutputStream os = null;
		try {
			response.reset();
			response.setCharacterEncoding("utf-8");
			response.addHeader("Content-disposition", "attachment; filename=" + UUIDGenerator.getUUID(false, false, false) + ".xlsx");
			response.setContentType("application/x-msdownload;charset=utf-8");// 定义输出类型
			os =  new BufferedOutputStream(response.getOutputStream());
			workbook.write(os);
			// 刷新
			os.flush();
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		} finally {
			if (os != null) {
				// 关闭
				os.close();
			}
		}
	}

	@ApiOperation(value = "项目汇总", response = Result.class, notes = "getProjectSummary")
	@RequestMapping(value = "/v1/projects/summary/{realm}/{userId}", method = { RequestMethod.GET })
	public Result getProjectSummary(@ApiParam(value = "域", required = true) @PathVariable String realm,
			@ApiParam(value = "用户唯一编码", required = true) @PathVariable String userId) {

		return super.successResult(this.projectService.getProjectSummary(realm, userId));
	}

	@ApiOperation(value = "根据项目id，获取项目汇总", response = Result.class, notes = "getProjectSummaryById")
	@RequestMapping(value = "/v1/projects/summary.filter", method = { RequestMethod.POST })
	public Result getProjectSummaryById(
			@ApiParam(value = "项目汇总请求参数，案例id数组", required = true) @RequestBody String[] caseIds) {

		if (null == caseIds || 0 == caseIds.length) {
			return super.failResult("没有传入任何有效项目数据");
		}
		return super.successResult(this.projectService.getProjectSummaryById(caseIds));
	}
	@ApiOperation(value = "根据域，获取资料汇总信息。", response = Result.class, notes = "getMaterialSummaryByRealm")
	@RequestMapping(value = "/v1/material/summary/{realm}", method = { RequestMethod.GET })
	public Result getMaterialSummaryByRealm(@PathVariable String realm) {
		return super.successResult(this.commonMaterialService.getMaterialSummaryByRealm(realm));
	}
	@ApiOperation(value = "根据域，获取热门项目信息。", response = Result.class, notes = "getHotProjectSummaryByRealm")
	@RequestMapping(value = "/v1/hotprojects/summary/{realm}", method = { RequestMethod.GET })
	public Result getHotProjectSummaryByRealm(@PathVariable String realm) {
		return super.successResult(this.projectService.getHotProjectSummaryByRealm(realm));
	}
	@ApiOperation(value = "根据域，获取运营统计（项目、团队和机构）信息。", response = Result.class, notes = "getGroupsSummaryByRealm")
	@RequestMapping(value = "/v1/groups/summary/{realm}", method = { RequestMethod.GET })
	public Result getGroupsSummaryByRealm(@PathVariable String realm) {
		return super.successResult(this.projectService.getGroupsSummaryByRealm(realm));
	}
	@ApiOperation(value = "根据域，获取基础资料统计信息。", response = Result.class, notes = "getBasicMaterialSummaryByRealm")
	@RequestMapping(value = "/v1/basicmaterial/summary/{realm}/{beginTime}/{endTime}", method = { RequestMethod.GET })
	public Result getBasicMaterialSummaryByRealm(
			@ApiParam(value = "域")
			@PathVariable String realm, 
			@ApiParam(value = "开始时间")
			@PathVariable long beginTime,
			@ApiParam(value = "结束时间")
			@PathVariable long endTime) {
		return super.successResult(this.commonMaterialService.getBasicMaterialStat(realm, new Date(beginTime), new Date(endTime)));
	}
}
