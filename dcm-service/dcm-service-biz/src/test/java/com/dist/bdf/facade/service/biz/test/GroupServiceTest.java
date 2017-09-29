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
import com.dist.bdf.common.conf.ecm.ECMConf;
import com.dist.bdf.facade.service.GroupService;
import com.dist.bdf.model.dto.system.page.PageProjectPara;
import com.dist.bdf.model.entity.system.DcmGroup;
import com.dist.bdf.model.entity.system.DcmRole;

/**
 * 组服务单元测试
 * @author weifj
 *
 */
@ContextConfiguration("classpath:/spring/spring.xml")
@Transactional //事务自动回滚
@ActiveProfiles("product-cloud")
public class GroupServiceTest extends AbstractTransactionalTestNGSpringContextTests {

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private GroupService groupService;
	@Autowired
	private ECMConf ecmConf;
	
	/**
	 * 分页获取其它人的项目信息
	 */
	@Test
	public void testSearchOthersProjectsByPage() {
	/*	
		String parajson = "{\"userCode\":\"44DD6C36-D854-40E8-BFD0-DCACB5EFA0CA\",\"pageSize\":10,\"pageNo\":1,\"keyword\":\"*\",\"realm\":\"thupdi\",\"isMerge\":true}";
		logger.info(">>>参数信息："+parajson);
		PageProjectPara para = JSONObject.parseObject(parajson, PageProjectPara.class);
		Object result = this.groupService.searchOthersProjectsByPage(para, this.ecmConf.getProjectTypeCommon().split(";"));
		logger.info(">>>结果信息："+JSONObject.toJSONString(result));
		Assert.notNull(result);*/
	}
	/**
	 * 测试获取项目组
	 */
	@Test
	public void testListProjectGroups() {
		
		List<DcmGroup> groups = this.groupService.listProjectGroups();
		logger.info(">>>检索到项目组信息：{}", JSONObject.toJSON(groups));
		Assert.notEmpty(groups);
	}
	/**
	 * 测试获取讨论组
	 */
	@Test
	public void testListDiscussionGroups() {
		
		List<DcmGroup> groups = this.groupService.listDiscussionGroups();
		logger.info(">>>检索到讨论组信息：{}", JSONObject.toJSON(groups));
		Assert.notNull(groups);
	}
	/**
	 * 测试根据guid获取组信息
	 */
	@Test
	public void testGetGroupByGuid() {
		
		/*DcmGroup group = this.groupService.getGroupByGuid("{C0D31E58-0000-CF11-85FF-1F78FF43F4E7}");
		logger.info(">>>检索到组信息：{}", JSONObject.toJSON(group));
		Assert.notNull(group);*/
	}

	/**
	 * 测试获取用户在项目组中的角色信息
	 */
	@Test
	public void testGetRolesInProject() {
		
	/*	List<DcmRole> roles = this.groupService.getRolesInProject("{C0D31E58-0000-CF11-85FF-1F78FF43F4E7}", 167743L);
		logger.info(">>>检索到角色信息：{}", JSONObject.toJSON(roles));
		Assert.notNull(roles);*/
	}
	
}
