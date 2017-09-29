package com.dist.bdf.facade.service.biz.priv.impl;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.dist.bdf.facade.service.biz.domain.system.DcmShareDmn;
import com.dist.bdf.facade.service.biz.priv.DocPrivService;
import com.dist.bdf.manager.ecm.security.PrivilegeFactory;
import com.dist.bdf.model.entity.system.DcmShare;
import com.dist.bdf.model.entity.system.DcmUser;

public abstract class AbstractDocPrivServiceImpl implements DocPrivService {

	private final Logger logger = (Logger) LoggerFactory.getLogger(getClass());
	@Autowired
	private DcmShareDmn shareDmn;
	
	@Deprecated
	public abstract Long getMasks(DcmUser user, String versionSeriesId, String domainCode);

	/**
	 * 因为每一种类型的文档都有可能被共享
	 * 所以先检索一下是否被共享
	 * @param user
	 * @param id
	 * @return 返回权限mask值
	 */
	protected Long getSharePrivMasks(DcmUser user, String id) {
		
		// 判断是否直接共享给当前用户
		DcmShare share = (DcmShare) this.shareDmn.getUniqueShareInfoByResIdAndTarget(id, new String[]{user.getUserCode(), user.getLoginName()});
		if (share != null) {
			logger.info(">>>资源[{}] 已共享给当前用户[{}]，拥有权限[{}]。", id, user.getLoginName(), share.getPrivCodes());	
			return PrivilegeFactory.getMasks(share.getPrivCodes().split(","));
		}
		return PrivilegeFactory.Priv_None.getMask();
	}
	/**
	 * 返回共享权限编码集合
	 * @param user
	 * @param id
	 */
    protected String[] getSharePrivCodes(DcmUser user, String id) {
		
		// 判断是否直接共享给当前用户
		DcmShare share = (DcmShare) this.shareDmn.getUniqueShareInfoByResIdAndTarget(id, new String[]{user.getUserCode(), user.getLoginName()});
		if (share != null) {
			logger.info(">>>资源[{}] 已共享给当前用户[{}]，拥有权限[{}]。", id, user.getLoginName(), share.getPrivCodes());	
			return share.getPrivCodes().split(",");
		}
		return null;
	}
}
