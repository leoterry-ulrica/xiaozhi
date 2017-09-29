package com.dist.bdf.facade.service.biz.test;

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
import com.dist.bdf.facade.service.cad.CadService;
import com.dist.bdf.model.entity.cad.TemplateEntity;

/**
 * CAD服务单元测试
 * @author weifj
 *
 */
@ContextConfiguration("classpath:/spring/spring.xml")
@Transactional //事务自动回滚
@ActiveProfiles("product-cloud")
public class CadServiceTest extends AbstractTransactionalTestNGSpringContextTests {

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private CadService cadService;
	
	/**
	 * 测试更新或添加cad模板元数据
	 */
	@Test
	public void testSaveOrUpdateTemplateMetadata() {
		/*logger.info(">>>测试更新或添加cad模板元数据");
		String params = "{\"currentProposalFile\":\"string\",\"docId\":\"NO_FILE_UPLOAD\",\"md5Code\":\"string\",\"name\":\"淮安市控制性详细规划标准模板\",\"proposals\":[{\"dbName\":\"string\",\"dbPassword\":\"string\",\"dbType\":\"string\",\"dbUser\":\"string\",\"description\":\"string\",\"loginPassword\":\"string\",\"loginUser\":\"string\",\"name\":\"string\",\"server\":\"string\"}],\"suffix\":\"zip\", \"realm\":\"thupdi\"}";
		TemplateEntity templateEntity = JSONObject.parseObject(params, TemplateEntity.class);
		this.cadService.saveOrUpdateTemplateMetadata(templateEntity);
		Assert.notNull(templateEntity);*/
	}
}
