package com.dist.bdf.facade.service.sga.domain.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dist.bdf.base.dao.GenericDAO;
import com.dist.bdf.base.domain.GenericDmnImpl;
import com.dist.bdf.base.exception.BusinessException;
import com.dist.bdf.common.constants.AttachmentTypeConstants;
import com.dist.bdf.facade.service.sga.dao.SgaUserAttachmentDAO;
import com.dist.bdf.facade.service.sga.dao.SgaUserDAO;
import com.dist.bdf.facade.service.sga.domain.SgaUserDmn;
import com.dist.bdf.model.dto.sga.UserAttachmentDTO;
import com.dist.bdf.model.entity.sga.SgaUser;
import com.dist.bdf.model.entity.sga.SgaUserAttachment;

@Service
@Transactional(propagation = Propagation.REQUIRED) 
public class SgaUserDmnImpl extends GenericDmnImpl<SgaUser, Long> implements SgaUserDmn {

	@Autowired
	private SgaUserDAO userDAO;
	@Autowired
	private SgaUserAttachmentDAO userAttaDAO;
	@Autowired
	private Mapper dozerMapper;
	
	@Override
	public GenericDAO<SgaUser, Long> getDao() {
		
		return userDAO;
	}
	@Override
	public SgaUser getByEmail(String email) {
		
		Map<String, Object[]> map = new HashMap<String, Object[]>();
		map.put("email", new Object[]{email});
		map.put("status", new Object[]{0,1});
		
		List<SgaUser> users = this.userDAO.findByProperties(map);
		if(users != null && !users.isEmpty()){
			return users.get(0);
		}
		return null;
	}
	@Override
	public SgaUser getByCode(String userCode) {
		
		return this.userDAO.findUniqueByProperty("sysCode", userCode);
	}
	@Override
	public SgaUserAttachment saveOrUpdateUserAttachement(UserAttachmentDTO attacheInfo) {
		
		SgaUser user = this.getByCode(attacheInfo.getUserCode());
		if(null == user) {
			throw new BusinessException("用户编码[{0}]不存在", attacheInfo.getUserCode());
		}
		SgaUserAttachment user2attach = null;
		if(AttachmentTypeConstants.ATTACHMENT_TYPE_RESUME == attacheInfo.getAttachType()) {
			user2attach = this.userAttaDAO.findUniqueByProperties(new String[]{"userId", "attachType"}, new Object[]{user.getId(), attacheInfo.getAttachType()});
		} else {
			user2attach = this.userAttaDAO.findUniqueByProperties(new String[]{"userId", "attachVId", "attachId"}, new Object[]{user.getId(), attacheInfo.getAttachVId(), attacheInfo.getAttachId()});
		}
		
		if(null == user2attach) {
			user2attach = this.dozerMapper.map(attacheInfo, SgaUserAttachment.class);
			user2attach.setUserId(user.getId());
		} else {
			this.dozerMapper.map(attacheInfo, user2attach);
		}
	
		return this.userAttaDAO.saveOrUpdate(user2attach);
	}
	@Override
	public List<SgaUserAttachment> getAttachmentsByUserId(Long userId, Integer attachType) {
		
		return this.userAttaDAO.findByProperties(new String[]{"userId", "attachType"}, new Object[]{userId, attachType});
	}
}
