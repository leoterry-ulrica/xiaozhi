package com.dist.bdf.facade.service.uic.domain.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dist.bdf.facade.service.uic.dao.DcmAttachmentDAO;
import com.dist.bdf.facade.service.uic.dao.DcmUserTitleInfoDAO;
import com.dist.bdf.facade.service.uic.domain.UserDetailInfoDmn;
import com.dist.bdf.model.dto.system.user.UserTitleInfoDTO;
import com.dist.bdf.model.entity.system.DcmAttachment;
import com.dist.bdf.model.entity.system.DcmUserTitleInfo;

@Component("UserDetailTitleDmn")
@Transactional(propagation = Propagation.REQUIRED) 
public class UserDetailTitleDmnImpl implements UserDetailInfoDmn {

	@Autowired
	private DcmUserTitleInfoDAO userTitleDAO;
	//private GenericDAOImpl<DcmUserTitleInfo, Long> userTitleDAO;
	@Autowired
	private DcmAttachmentDAO userAttachmentDAO;
	//private GenericDAOImpl<DcmAttachment, Long> userAttachmentDAO;
	
	@Override
	public Object doSave(Object detail) {
		
		UserTitleInfoDTO dto = (UserTitleInfoDTO) detail;
		DcmUserTitleInfo entity = userTitleDAO.save(dto.getInfo());
		List<DcmAttachment> attas = dto.getAttachments();
		if(attas != null && !attas.isEmpty()){
			for(DcmAttachment atta : attas){
				atta.setReferenceId(entity.getId());
				userAttachmentDAO.save(atta);
			}
		}
	
		dto.setInfo(entity);
		dto.setAttachments(attas);
		
		return dto;
	}

	@Override
	public Object delete(Long refId) {
		
		List<DcmUserTitleInfo> list = this.userTitleDAO.findByProperty("userId", refId);
		if(list != null && !list.isEmpty()){
			for(DcmUserTitleInfo entity : list){
				this.userAttachmentDAO.removeByProperty("referenceId", entity.getId());
				this.userTitleDAO.remove(entity);
			}
			
		}
		
		return true;
	}

}
