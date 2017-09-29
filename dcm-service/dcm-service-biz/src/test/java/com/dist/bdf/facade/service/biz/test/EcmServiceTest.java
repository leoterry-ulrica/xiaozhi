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
import com.dist.bdf.base.page.Pagination;
import com.dist.bdf.facade.service.EcmMgmtService;
import com.dist.bdf.model.dto.dcm.PageParaDTO;

/**
 * CAD服务单元测试
 * @author weifj
 *
 */
@ContextConfiguration("classpath:/spring/spring.xml")
@Transactional //事务自动回滚
@ActiveProfiles("product-cloud")
public class EcmServiceTest extends AbstractTransactionalTestNGSpringContextTests {

	private Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private EcmMgmtService ecmMgmtService;
	
	/**
	 * 测试按照匹配热度，进行全文检索
	 * (timeOut = 10000)
	 */
	@Test
	public void testFullTextSearchOfDocumentRank() {
		
		/*String requestJSON = "{\"realm\":\"thupdi\",\"pageNo\":1,\"pageSize\":20,\"keyword\":\"规划\"}";
		PageParaDTO pageDTO = JSONObject.parseObject(requestJSON, PageParaDTO.class);
		Pagination page = this.ecmMgmtService.fullTextSearchOfDocumentRank(pageDTO);
		Assert.notNull(page);
		logger.info(">>>结果：{}", JSONObject.toJSONString(page));*/
	}
}
