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

import com.dist.bdf.base.page.Pagination;
import com.dist.bdf.facade.service.InformationService;

/**
 * 资讯服务单元测试
 * @author weifj
 *
 */
@ContextConfiguration("classpath:/spring/spring.xml")
@Transactional //事务自动回滚
@ActiveProfiles("product-cloud")
public class InformationServiceTest extends AbstractTransactionalTestNGSpringContextTests {

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private InformationService infoService;
	
	/**
	 * 测试获取所有资讯信息
	 */
	@Test
	public void testListInformations() {
		logger.info(">>>测试分页获取所有资讯信息，页码：{}，每页：{}，关键字：{}", 1, 5, "");
	    Pagination page = this.infoService.searchInformations(1, 5, "");
		Assert.notNull(page);
		logger.info(">>>分页检索数据："+ page);
	}
	/**
	 * 测试资料类型的资讯
	 */
	@Test
	public void testGetInformationOfMaterial() {
		logger.info(">>>测试分页获取资料类型的资讯信息，页码：{}，每页：{}，关键字：{}", 1, 5, "");
	    Pagination page = this.infoService.searchInformationOfMaterial(1, 5, "");
		Assert.notNull(page);
		logger.info(">>>分页检索数据："+ page);
	}
	/**
	 * 测试新闻类型的资讯
	 */
	@Test
	public void testGetInformationOfNews() {
		logger.info(">>>测试分页获取新闻类型的资讯信息，页码：{}，每页：{}，关键字：{}", 1, 5, "");
	    Pagination page = this.infoService.searchInformationOfNews(1, 5, "");
		Assert.notNull(page);
		logger.info(">>>分页检索数据："+ page);
	}
}
