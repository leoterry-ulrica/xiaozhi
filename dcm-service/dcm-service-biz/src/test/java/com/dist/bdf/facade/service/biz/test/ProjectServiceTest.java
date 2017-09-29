package com.dist.bdf.facade.service.biz.test;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.testng.annotations.Test;

import com.alibaba.fastjson.JSONObject;
import com.dist.bdf.base.page.Pagination;
import com.dist.bdf.common.conf.ecm.ECMConf;
import com.dist.bdf.facade.service.GroupService;
import com.dist.bdf.facade.service.ProjectService;
import com.dist.bdf.model.dto.dcm.CaseDTO;
import com.dist.bdf.model.dto.dcm.CaseTypeDTO;
import com.dist.bdf.model.dto.system.GroupSummaryDTO;
import com.dist.bdf.model.dto.system.ProjectStatDTO;
import com.dist.bdf.model.dto.system.TaskSummaryFilterDTO;
import com.dist.bdf.model.dto.system.WzInfoPageDTO;
import com.dist.bdf.model.dto.system.page.PageProjectPara;
/**
 * 项目服务单元测试
 * @author weifj
 *
 */
@ContextConfiguration("classpath:/spring/spring.xml")
@Transactional //事务自动回滚
@ActiveProfiles("product-cloud")
public class ProjectServiceTest extends AbstractTransactionalTestNGSpringContextTests {

	private static Logger LOG = LoggerFactory.getLogger(ProjectServiceTest.class);
	
	@Autowired
	private ProjectService projectService;
	@Autowired
	private GroupService groupService;
	@Autowired
	private ECMConf ecmConf;
	
	/**
	 * 分页获取其它人的项目信息
	 */
	/*@Test
	public void testGetTaskSummary() {
		
		Pagination page = this.projectService.getTaskSummary("44DD6C36-D854-40E8-BFD0-DCACB5EFA0CA", 1, 20);
		logger.info(">>>结果信息："+JSONObject.toJSONString(page));
		Assert.notNull(page);
	}*/
	/**
	 * 获取任务汇总信息
	 */
	@Test
	public void testGetTaskSummaryByFilterCase1() {
		
		/*String json = "{\"associatePerson\":[],\"beginTime\":null,\"businessType\":[],\"endTime\":null,\"localSupport\":[],\"pageNo\":1,\"pageSize\":15,\"prjAssistant\":[],\"prjManager\":[\"44DD6C36-D854-40E8-BFD0-DCACB5EFA0CA\"],\"projectNo\":[],\"status\":[],\"taskType\":[],\"userCode\":\"1B402E24-D8D4-41DC-AF77-884F896446C9\"}";
		//"{\"associatePerson\":[],\"beginTime\":null,\"businessType\":[],\"endTime\":null,\"localSupport\":[],\"pageNo\":1,\"pageSize\":15,\"prjAssistant\":[],\"prjManager\":[\"0D3B94E8-E961-4F53-B5B2-27997621417D\"],\"projectNo\":[],\"status\":[],\"taskType\":[],\"userCode\":\"1B402E24-D8D4-41DC-AF77-884F896446C9\"}";
		//"{\"associatePerson\":[],\"beginTime\":null,\"businessType\":[],\"endTime\":1490007418000,\"locSupport\":[],\"pageNo\":1,\"pageSize\":20,\"prjAssistant\":[],\"prjManager\":[],\"projectNo\":[],\"status\":[\"585d68\"],\"taskType\":[\"方案\"],\"userCode\":\"44DD6C36-D854-40E8-BFD0-DCACB5EFA0CA\"}";
	    TaskSummaryFilterDTO dto = JSONObject.parseObject(json, TaskSummaryFilterDTO.class);
	    Object result = this.projectService.getTaskSummary(dto);
	    logger.info(">>>计算完毕："+JSONObject.toJSONString(result));
	    Assert.notNull(result);*/
	}
	@Test
	public void testGetTaskSummaryByFilterCase2() {
		
	/*	String json = "{\"associatePerson\":[],\"beginTime\":null,\"businessType\":[\"住建\"],\"endTime\":null,\"localSupport\":[],\"pageNo\":1,\"pageSize\":15,\"prjAssistant\":[],\"prjManager\":[],\"projectNo\":[],\"status\":[],\"taskType\":[],\"userCode\":\"1B402E24-D8D4-41DC-AF77-884F896446C9\",\"realm\":\"thupdi\"}";
		//"{\"associatePerson\":[],\"beginTime\":null,\"businessType\":[],\"endTime\":null,\"localSupport\":[],\"pageNo\":1,\"pageSize\":15,\"prjAssistant\":[],\"prjManager\":[\"0D3B94E8-E961-4F53-B5B2-27997621417D\"],\"projectNo\":[],\"status\":[],\"taskType\":[],\"userCode\":\"1B402E24-D8D4-41DC-AF77-884F896446C9\"}";
		//"{\"associatePerson\":[],\"beginTime\":null,\"businessType\":[],\"endTime\":1490007418000,\"locSupport\":[],\"pageNo\":1,\"pageSize\":20,\"prjAssistant\":[],\"prjManager\":[],\"projectNo\":[],\"status\":[\"585d68\"],\"taskType\":[\"方案\"],\"userCode\":\"44DD6C36-D854-40E8-BFD0-DCACB5EFA0CA\"}";
	    TaskSummaryFilterDTO dto = JSONObject.parseObject(json, TaskSummaryFilterDTO.class);
	    Object result = this.projectService.getTaskSummary(dto);
	    logger.info(">>>计算完毕："+JSONObject.toJSONString(result));
	    Assert.notNull(result);*/
	}
	@Test
	public void testGetTaskSummaryByFilterCase3() {
		/*
		String json = "{\"associatePerson\":[],\"beginTime\":null,\"businessType\":[],\"endTime\":null,\"localSupport\":[],\"pageNo\":1,\"pageSize\":15,\"prjAssistant\":[],\"prjManager\":[],\"projectNo\":[],\"status\":[\"00C800\"],\"taskType\":[],\"userCode\":\"1B402E24-D8D4-41DC-AF77-884F896446C9\",\"realm\":\"thupdi\"}";
	    TaskSummaryFilterDTO dto = JSONObject.parseObject(json, TaskSummaryFilterDTO.class);
	    Object result = this.projectService.getTaskSummary(dto);
	    logger.info(">>>计算完毕："+JSONObject.toJSONString(result));
	    Assert.notNull(result);*/
	}
	
	@Test
	public void testGetTaskSummaryByFilterCase4() {
		
		/*String json = "{\"associatePerson\":[],\"beginTime\":null,\"businessType\":[],\"endTime\":null,\"localSupport\":[],\"pageNo\":1,\"pageSize\":20,\"prjAssistant\":[],\"prjManager\":[],\"projectNo\":[],\"status\":[],\"taskType\":[],\"userCode\":\"1B402E24-D8D4-41DC-AF77-884F896446C9\",\"realm\":\"thupdi\"}";
	    TaskSummaryFilterDTO dto = JSONObject.parseObject(json, TaskSummaryFilterDTO.class);
	    Object result = this.projectService.getTaskSummary(dto);
	    logger.info(">>>计算完毕："+JSONObject.toJSONString(result));
	    Assert.notNull(result);*/
	}
	@Test
	public void testGetTaskSummaryByFilterCase5() {
	/*	
		String json = "{\"associatePerson\":[],\"beginTime\":null,\"businessType\":[],\"endTime\":null,\"localSupport\":[],\"pageNo\":1,\"pageSize\":15,\"prjAssistant\":[],\"prjManager\":[],\"projectNo\":[],\"status\":[],\"taskType\":[],\"publisher\":[\"thupdi_jiayzh\"],\"userCode\":\"1B402E24-D8D4-41DC-AF77-884F896446C9\",\"realm\":\"thupdi\"}";
	    TaskSummaryFilterDTO dto = JSONObject.parseObject(json, TaskSummaryFilterDTO.class);
	    Object result = this.projectService.getTaskSummary(dto);
	    logger.info(">>>计算完毕："+JSONObject.toJSONString(result));
	    Assert.notNull(result);*/
	}
	/**
	 * 测试没有任务的情况
	 */
	@Test
	public void testGetTaskSummaryByFilterCase6() {
		
		/*String json = "{\"associatePerson\":[],\"beginTime\":null,\"businessType\":[],\"endTime\":null,\"localSupport\":[],\"pageNo\":1,\"pageSize\":15,\"prjAssistant\":[],\"prjManager\":[],\"projectNo\":[],\"status\":[],\"taskType\":[],\"publisher\":[\"thupdi_weifj\"],\"userCode\":\"FA8458EB-9D85-42D6-A234-8D22489C6859\",\"realm\":\"thupdi\"}";
	    TaskSummaryFilterDTO dto = JSONObject.parseObject(json, TaskSummaryFilterDTO.class);
	    Object result = this.projectService.getTaskSummary(dto);
	    logger.info(">>>计算完毕："+JSONObject.toJSONString(result));
	    Assert.notNull(result);*/
	}
	/**
	 * 
	 */
	@Test
    public void testGetTaskSummaryByFilterCase7() {
		
	/*	String json = "{\"associatePerson\":[],\"beginTime\":null,\"businessType\":[],\"endTime\":null,\"localSupport\":[],\"pageNo\":1,\"pageSize\":15,\"prjAssistant\":[],\"prjManager\":[\"46322BE5-DF3B-4F7C-BBB3-F5D19AE74D0B\"],\"projectNo\":[],\"status\":[],\"taskType\":[],\"publisher\":[],\"userCode\":\"FA8458EB-9D85-42D6-A234-8D22489C6859\",\"realm\":\"thupdi\"}";
	    TaskSummaryFilterDTO dto = JSONObject.parseObject(json, TaskSummaryFilterDTO.class);
	    Object result = this.projectService.getTaskSummaryEx(dto);
	    logger.info(">>>计算完毕："+JSONObject.toJSONString(result));
	    Assert.notNull(result);*/
	}
	@Test
    public void testGetTaskSummaryByFilterCase8() {
		
		/*String json = "{\"associatePerson\":[],\"beginTime\":null,\"businessType\":[],\"endTime\":null,\"localSupport\":[],\"pageNo\":1,\"pageSize\":15,\"prjAssistant\":[],\"prjManager\":[],\"projectNo\":[\"{E0906A5A-0000-C41E-B210-4D8BB1DA5BC0}\"],\"status\":[],\"taskType\":[],\"publisher\":[],\"userCode\":\"FA8458EB-9D85-42D6-A234-8D22489C6859\",\"realm\":\"thupdi\"}";
	    TaskSummaryFilterDTO dto = JSONObject.parseObject(json, TaskSummaryFilterDTO.class);
	    Object result = this.projectService.getTaskSummaryEx(dto);
	    logger.info(">>>计算完毕："+JSONObject.toJSONString(result));
	    Assert.notNull(result);*/
	}
	@Test
    public void testGetTaskSummaryByFilterCase9() {
		
		String json = "{\"associatePerson\":[],\"beginTime\":null,\"businessType\":[],\"endTime\":null,\"localSupport\":[],\"pageNo\":1,\"pageSize\":15,\"prjAssistant\":[],\"prjManager\":[],\"projectNo\":[],\"status\":[],\"taskType\":[],\"publisher\":[],\"publishBeginTime\":null,\"publishEndTime\":null,\"userCode\":\"FEC8CBF9-0717-4637-9FE9-B5FD9FD3E207\",\"realm\":\"jnup\"}";
	    TaskSummaryFilterDTO dto = JSONObject.parseObject(json, TaskSummaryFilterDTO.class);
	    Object result = this.projectService.getTaskSummaryNew(dto);
	    logger.info(">>>计算完毕："+JSONObject.toJSONString(result));
	    Assert.notNull(result);
	}
	/**
	 * 获取跟个人相关的项目
	 */
	@Test
	public void testSearchMycommonProjectsByPage() {
		/*String json = "{\"userCode\":\"FA8458EB-9D85-42D6-A234-8D22489C6859\",\"pageSize\":10,\"pageNo\":1,\"keyword\":\"测试\",\"realm\":\"thupdi\"}";
		PageProjectPara para = JSONObject.parseObject(json, PageProjectPara.class);
		Object result = this.groupService.searchProjectsByPage(para, this.ecmConf.getProjectTypeCommon().split(";"), Boolean.TRUE);// "XZ_CASETYPE_JYXM","XZ_CASETYPE_HZXM"
		logger.info(">>>"+JSONObject.toJSONString(result));
		Assert.notNull(result);*/
	}
	/**
	 * 测试获取跟个人无关的项目
	 */
	@Test
	public void testSearchOtherscommonProjectsByPage() {
		/*String json = "{\"userCode\":\"FA8458EB-9D85-42D6-A234-8D22489C6859\",\"pageSize\":10,\"pageNo\":1,\"keyword\":\"测试\",\"realm\":\"thupdi\"}";
		PageProjectPara para = JSONObject.parseObject(json, PageProjectPara.class);
		Object result = this.groupService.searchOthersProjectsByPage(para, this.ecmConf.getProjectTypeCommon().split(";"));// "XZ_CASETYPE_JYXM","XZ_CASETYPE_HZXM"
		logger.info(">>>"+JSONObject.toJSONString(result));
		Assert.notNull(result);*/
	}
	/**
	 * 模糊检索项目
	 */
	@Test
	public void testFuzzySearchProjectName() {
		
	/*	Pagination page = this.projectService.fuzzySearchProjectName("thupdi",1, 5, "airbnb");
		logger.info(">>>模糊检索项目到信息："+JSONObject.toJSONString(page));
		Assert.notNull(page);*/
	}
	/**
	 * 获取案例类型
	 */
	@Test
	public void testGetCaseTypes() {
		
		List<CaseTypeDTO> caseTypes  = this.projectService.getCaseTypes();
		logger.info(">>>案例类型："+JSONObject.toJSONString(caseTypes));
		Assert.notEmpty(caseTypes);
	}
	/**
	 * 测试精确检索案例
	 */
	@Test
	public void testGetCaseById() {
		
	/*	CaseDTO caseDTO  = this.projectService.getCaseById("thupdi","{C0D31E58-0000-CF11-85FF-1F78FF43F4E7}");
		logger.info(">>>检索案例：[{}]，结果：[{}]", "{C0D31E58-0000-CF11-85FF-1F78FF43F4E7}",JSONObject.toJSONString(caseDTO));
		Assert.notNull(caseDTO);*/
	}
	/**
	 * 测试获取项目包
	 */
	@Test
	public void testGetPackages() {
		
		/*List<DocumentDTO> docs  = this.projectService.getPackages("thupdi","XZ_CASETYPE_JYXM_000000100002");
		logger.info(">>>域：[{}]，检索案例：[{}]，结果：[{}]", "thupdi","XZ_CASETYPE_JYXM_000000100002",JSONObject.toJSONString(docs));*/
	}
	
	@Test
	public void testGetWZsOfCaseById() {
		
	/*	String info = "{\"realm\":\"thupdi\", \"caseId\":\"{8067AF5B-0000-C413-9485-DB8FBCD83B6E}\",\"pageNo\":2,\"pageSize\":3, \"realm\":\"thupdi\"}";
		WzInfoPageDTO pageInfo = JSONObject.parseObject(info, WzInfoPageDTO.class);
		Pagination page = this.projectService.getWZsOfCaseById(pageInfo);
		Assert.notNull(page);*/
	}
	/**
	 * 测试项目的活跃度
	 */
	//@Test
	public void testGetStatInfoEx() {
	
		/*String realm = "thupdi";
		String projectId = "{E0906A5A-0000-C41E-B210-4D8BB1DA5BC0}";
		logger.info(">>>域：{}，项目id：{}", realm,  projectId);
		List<ProjectStatDTO> stats = this.projectService.getStatInfoEx(realm, projectId, 3);
		for(ProjectStatDTO s : stats) {
			logger.info(s.getUserCode());
		}
		Assert.notEmpty(stats);*/
	}
	@Test
	public void testGetGroupsSummaryByRealm() {
		
		GroupSummaryDTO dto = this.projectService.getGroupsSummaryByRealm("thupdi");
		Assert.notNull(dto);
		LOG.info(dto.toString());
	}
}
