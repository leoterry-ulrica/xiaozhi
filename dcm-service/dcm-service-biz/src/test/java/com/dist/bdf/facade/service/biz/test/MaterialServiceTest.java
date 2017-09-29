package com.dist.bdf.facade.service.biz.test;

import java.util.Calendar;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.testng.annotations.Test;

import com.alibaba.fastjson.JSONObject;
import com.dist.bdf.base.page.Pagination;
import com.dist.bdf.facade.service.MaterialService;
import com.dist.bdf.facade.service.ProjectMaterialService;
import com.dist.bdf.model.dto.dcm.PageParaDTO;
import com.filenet.engine.is.fntypes.Log;

/**
 * 材料服务单元测试
 * @author weifj
 *
 */
@ContextConfiguration("classpath:/spring/spring.xml")
@Transactional //事务自动回滚
@ActiveProfiles("product-cloud")
public class MaterialServiceTest extends AbstractTransactionalTestNGSpringContextTests {

	private static Logger LOG = LoggerFactory.getLogger(MaterialServiceTest.class);
	@Autowired
	@Qualifier("CommonMaterialService")
	private MaterialService materialService;
	@Autowired
	private ProjectMaterialService projectMaterialService;
	
	/**
	 * 测试按照匹配热度，进行全文检索
	 * (timeOut = 10000)
	 */
	@Test
	public void testGetDocInfoById() {
		
		/*DocumentDTO dto = this.materialService.getDocInfoById("thupdi", "{5768B400-0199-402A-8E36-F12FD934D6F7}", "FA8458EB-9D85-42D6-A234-8D22489C6859");
		Assert.notNull(dto);
		logger.info(">>>结果：{}", JSONObject.toJSONString(dto));*/
	}
	@Test
	public void testRenameDocName() {
		
		//this.materialService.renameDocName("thupdi", "{5768B400-0199-402A-8E36-F12FD934D6F7}", "Aspera培训内容总结 - 副本 - 副本 (16) -新.docx");
	}
	@Test
	public void testGetProjectFoldersPage() {
		
		Pagination page = this.projectMaterialService.getProjectFoldersPage(3, 1, "", "123");
		Assert.notEmpty(page.getData());
	}
	@Test
	public void testGetProjectDocsPage() {
		
		Pagination page = this.projectMaterialService.getProjectDocsPage(3, 1, "", "123");
		Assert.notEmpty(page.getData());
	}
	@Test
	public void testGetSubDocsPage() {
		String json = "{\"folderId\":\"{10636A5A-0000-C510-82BD-CDF9B1E874A5}\",\"pageNo\":1,\"pageSize\":60,\"realm\":\"thupdi\"}";
		PageParaDTO dto = JSONObject.parseObject(json, PageParaDTO.class);
		Pagination page = this.materialService.getSubDocsPage(dto);
		LOG.info(page.toJsonString());
	}
	@Test
	public void testGetBasicMaterialStat() {
		
		Calendar date = Calendar.getInstance();
		Date endTime = new Date();
		date.setTime(endTime);
		date.set(Calendar.DATE, date.get(Calendar.DATE) - 10);
		Object result = this.materialService.getBasicMaterialStat("jnup", date.getTime(), endTime);
		Assert.notNull(result);
	}
}
