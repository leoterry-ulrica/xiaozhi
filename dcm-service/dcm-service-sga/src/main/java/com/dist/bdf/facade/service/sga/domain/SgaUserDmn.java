package com.dist.bdf.facade.service.sga.domain;

import java.util.List;

import com.dist.bdf.base.domain.GenericDmn;
import com.dist.bdf.model.dto.sga.UserAttachmentDTO;
import com.dist.bdf.model.entity.sga.SgaUser;
import com.dist.bdf.model.entity.sga.SgaUserAttachment;


public interface SgaUserDmn extends GenericDmn<SgaUser, Long> {

	/**
	 * 根据邮件地址获取用户信息
	 * @param email
	 * @return
	 */
	SgaUser getByEmail(String email);
	/**
	 * 根据用户编码获取用户信息
	 * @param userCode
	 * @return
	 */
	SgaUser getByCode(String userCode);
	/**
	 * 保存用户附件
	 * @param attacheInfo
	 * @return
	 */
	SgaUserAttachment saveOrUpdateUserAttachement(UserAttachmentDTO attacheInfo);
	/**
	 * 根据用户序列id获取附件
	 * @param userId
	 * @return
	 */
	List<SgaUserAttachment> getAttachmentsByUserId(Long userId, Integer attachType);
}
