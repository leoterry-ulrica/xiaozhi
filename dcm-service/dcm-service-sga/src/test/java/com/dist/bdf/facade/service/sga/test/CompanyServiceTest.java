package com.dist.bdf.facade.service.sga.test;

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

import com.dist.bdf.facade.service.sga.SgaCompanyService;
import com.dist.bdf.model.dto.sga.CompanyInfoResponseDTO;
import com.dist.bdf.model.entity.sga.SgaComAudit;

/**
 * 企业服务单元测试
 * @author weifj
 *
 */
@ContextConfiguration("classpath:/spring/spring.xml")
@Transactional //事务自动回滚
@ActiveProfiles("product-cloud")
public class CompanyServiceTest extends AbstractTransactionalTestNGSpringContextTests {

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private SgaCompanyService companyService;
	
	/**
	 * 测试同步企业审计信息
	 */
	@Test
	public void testSyncCompanyAudit() {
		logger.info(">>>测试同步企业审计信息");
	/*	SgaComAudit stat = this.companyService.syncCompanyAudit("thupdi");
		Assert.notNull(stat);
		logger.info(">>>结果：{}", stat);*/
	}
	/**
	 * 测试获取有效企业信息
	 */
	@Test
	public void testListValidCompany() {
		logger.info(">>>测试获取有效企业信息");
		/*List<CompanyInfoResponseDTO> companies = this.companyService.listValidCompany();
		Assert.notEmpty(companies);
		logger.info(">>>结果：{}", companies);*/
	}
}
