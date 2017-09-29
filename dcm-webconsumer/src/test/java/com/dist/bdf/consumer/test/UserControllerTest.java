package com.dist.bdf.consumer.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.Assert;
import org.testng.annotations.Test;

import com.dist.bdf.test.BdfControllerIntegrationRestTestUsingTestNG;

@ContextConfiguration(locations = {"classpath:/spring/spring.xml", "classpath:/spring/spring-mvc.xml"})
@ActiveProfiles("product-cloud")
public class UserControllerTest extends BdfControllerIntegrationRestTestUsingTestNG {

	private Logger logger = LoggerFactory.getLogger(UserControllerTest.class);
	/**
	 * 根据唯一登录名获取用户信息
	 * (timeOut = 3000)
	 * @throws Exception 
	 */
	@Test
	public void testGetUserByLoginname() throws Exception {
		
	/*	ResultActions ra = super.testGetOk("/rest/sysservice/getUserByLoginName/thupdi_weifj");
		Assert.notNull(ra);
		logger.info(">>>结果：{}", ra.andReturn().toString());*/
	}
	/**
	 * 测试根据用户编码获取相关企业下的所有用户信息
	 * @throws Exception
	 */
	@Test
	public void testListSimpleInnerAndExternalUsersByUserCode() throws Exception {
		
		/*ResultActions ra = super.testGetOk("/rest/sysservice/users/company/3BE4762B-69BA-4C24-9400-8CE7C626688B");
		Assert.notNull(ra);
		logger.info(">>>结果：{}", ra.andReturn().toString());*/
	}
}
