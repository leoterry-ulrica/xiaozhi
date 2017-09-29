package com.dist.bdf.consumer.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.testng.annotations.Test;

import com.dist.bdf.test.BdfControllerIntegrationRestTestUsingTestNG;

@ContextConfiguration(locations = {"classpath:/spring/spring.xml", "classpath:/spring/spring-mvc.xml"})
@ActiveProfiles("product-cloud")
public class ProjectControllerTest extends BdfControllerIntegrationRestTestUsingTestNG {

	private static Logger LOG = LoggerFactory.getLogger(ProjectControllerTest.class);
	/**
	 * 根据唯一登录名获取用户信息
	 * (timeOut = 10000)
	 */
	@Test
	public void testExportTaskSummaryToExcel() {
		
/*		try {
			String requestJSON = "{\"associatePerson\":[],\"beginTime\":null,\"businessType\":[],\"endTime\":null,\"localSupport\":[],\"pageNo\":1,\"pageSize\":15,\"prjAssistant\":[],\"prjManager\":[],\"projectNo\":[],\"status\":[],\"taskType\":[],\"publisher\":[],\"userCode\":\"1B402E24-D8D4-41DC-AF77-884F896446C9\",\"realm\":\"thupdi\"}";
			ResultActions ra = super.testPostOk("/rest/sysservice/v1/task/summaryexp/excel", requestJSON);
			Assert.notNull(ra);
			logger.info(">>>结果：{}", ra.andReturn().toString());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}*/
	}
	
}
