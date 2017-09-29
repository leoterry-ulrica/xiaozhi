package com.dist.bdf.facade.service.uic.domain.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dist.bdf.facade.service.uic.dao.DcmAttachmentDAO;
import com.dist.bdf.facade.service.uic.dao.DcmUserTrainingDAO;
import com.dist.bdf.facade.service.uic.domain.UserDetailInfoDmn;
import com.dist.bdf.model.dto.system.user.UserTrainingDTO;
import com.dist.bdf.model.entity.system.DcmAttachment;
import com.dist.bdf.model.entity.system.DcmUserTraining;

@Component("UserDetailTrainingDmn")
@Transactional(propagation = Propagation.REQUIRED) 
public class UserDetailTrainingDmnImpl implements UserDetailInfoDmn {

	@Autowired
	private DcmUserTrainingDAO userTrainingDAO;
	//private GenericDAOImpl<DcmUserTraining, Long> userTrainingDAO;
	@Autowired
	private DcmAttachmentDAO userAttachmentDAO;
	//private GenericDAOImpl<DcmAttachment, Long> userAttachmentDAO;
	
	@Override
	public Object doSave(Object detail) {
		
		UserTrainingDTO dto = (UserTrainingDTO) detail;
		DcmUserTraining entity = userTrainingDAO.save(dto.getInfo());
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
		
		List<DcmUserTraining> list = this.userTrainingDAO.findByProperty("userId", refId);
		if(list != null && !list.isEmpty()){
			for(DcmUserTraining entity : list){
				this.userAttachmentDAO.removeByProperty("referenceId", entity.getId());
				this.userTrainingDAO.remove(entity);
			}
			
		}
		
		return true;
	}

}
