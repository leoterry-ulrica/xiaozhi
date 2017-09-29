package com.dist.bdf.facade.service.uic.domain.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dist.bdf.facade.service.uic.dao.DcmAttachmentDAO;
import com.dist.bdf.facade.service.uic.dao.DcmUserOtherInfoDAO;
import com.dist.bdf.facade.service.uic.domain.UserDetailInfoDmn;
import com.dist.bdf.model.dto.system.user.UserOtherInfoDTO;
import com.dist.bdf.model.entity.system.DcmAttachment;
import com.dist.bdf.model.entity.system.DcmUserOtherInfo;

@Component("UserDetailOtherDmn")
@Transactional(propagation = Propagation.REQUIRED) 
public class UserDetailOtherDmnImpl implements UserDetailInfoDmn {

	@Autowired
	private DcmUserOtherInfoDAO userOtherInfoDAO;
	//private GenericDAOImpl<DcmUserOtherInfo, Long> userOtherInfoDAO;
	@Autowired 
	private DcmAttachmentDAO userAttachmentDAO;
	//private GenericDAOImpl<DcmAttachment, Long> userAttachmentDAO;
	
	@Override
	public Object doSave(Object detail) {
		
		UserOtherInfoDTO dto = (UserOtherInfoDTO) detail;
		DcmUserOtherInfo entity = this.userOtherInfoDAO.save(dto.getInfo());
		
		List<DcmAttachment> attas =  dto.getAttachments();
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
		
		List<DcmUserOtherInfo> list = this.userOtherInfoDAO.findByProperty("userId", refId);
		if(list != null && !list.isEmpty()){
			for(DcmUserOtherInfo entity : list){
				this.userAttachmentDAO.removeByProperty("referenceId", entity.getId());
				this.userOtherInfoDAO.remove(entity);
			}
			
		}
		
		return true;
	}

}
