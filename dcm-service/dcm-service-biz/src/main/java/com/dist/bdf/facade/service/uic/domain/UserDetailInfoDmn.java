package com.dist.bdf.facade.service.uic.domain;

public interface UserDetailInfoDmn {

	/**
	 * 保存详情
	 * @param detail
	 * @return
	 */
	Object doSave(Object detail);
	/**
	 * 删除详情
	 * @param refId，关联的外键id，可能是用户id，可能是其它详情实体的id
	 * @return
	 */
	Object delete(Long refId);
}
