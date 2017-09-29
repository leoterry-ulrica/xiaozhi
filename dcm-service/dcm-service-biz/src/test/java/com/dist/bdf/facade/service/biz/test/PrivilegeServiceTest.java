package com.dist.bdf.facade.service.biz.test;

import static org.testng.Assert.assertNotNull;

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
import com.dist.bdf.facade.service.PrivilegeService;
import com.dist.bdf.model.dto.system.UserResPrivRequestDTO;

/**
 * 权限服务单元测试
 * @author weifj
 *
 */
@ContextConfiguration("classpath:/spring/spring.xml")
@Transactional //事务自动回滚
@ActiveProfiles("product-cloud")
public class PrivilegeServiceTest extends AbstractTransactionalTestNGSpringContextTests {

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private PrivilegeService privService;
	
	/**
	 * 测试用户对文档的权限编码
	 */
	@Test
	public void testGetPrivCodes() {
		
		/*UserResPrivRequestDTO dto = new UserResPrivRequestDTO();
		dto.setFrom(0L);
		dto.setRealm("thupdi");
		dto.setResId("{30A0C55A-0000-CB12-8A1C-0FFD77E138ED}");
		dto.setUser("3BE4762B-69BA-4C24-9400-8CE7C626688B");
		dto.setUserType(1);
		
		@SuppressWarnings("unchecked")
		List<String> codes = (List<String>) this.privService.getDocPrivCodesV1(dto);
		logger.debug(codes.toString());
		Assert.notEmpty(codes);*/
		
		String json = "{\"realm\":\"jnup\",\"user\":\"30E2BD75-2B00-47D7-805B-3F3AF21B1F34\",\"resId\":\"{400B0D5E-0000-C41F-B7C7-FEDC4F58932A}\",\"from\":0,\"parentId\":null}";
		UserResPrivRequestDTO dto = JSONObject.parseObject(json, UserResPrivRequestDTO.class);
		Object result = this.privService.getDocPrivCodesV1(dto);
		Assert.notNull(result);
	}
}
