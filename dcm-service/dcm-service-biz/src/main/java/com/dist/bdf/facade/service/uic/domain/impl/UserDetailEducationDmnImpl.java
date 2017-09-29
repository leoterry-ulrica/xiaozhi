package com.dist.bdf.facade.service.uic.domain.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dist.bdf.facade.service.uic.dao.DcmAttachmentDAO;
import com.dist.bdf.facade.service.uic.dao.DcmUserEducationDAO;
import com.dist.bdf.facade.service.uic.domain.UserDetailInfoDmn;
import com.dist.bdf.model.dto.system.user.UserEducationDTO;
import com.dist.bdf.model.entity.system.DcmAttachment;
import com.dist.bdf.model.entity.system.DcmUserEducation;

@Component("UserDetailEducationDmn")
@Transactional(propagation = Propagation.REQUIRED) 
public class UserDetailEducationDmnImpl implements UserDetailInfoDmn {


	@Autowired
	private DcmUserEducationDAO userEduDAO;
	//private GenericDAOImpl<DcmUserEducation, Long> userEduDAO;
	
	@Autowired
	private DcmAttachmentDAO userAttachmentDAO;
	//private GenericDAOImpl<DcmAttachment, Long> userAttachmentDAO;
	
	@Override
	public Object doSave(Object detail) {
		
		UserEducationDTO dto = (UserEducationDTO) detail;
		DcmUserEducation entity = userEduDAO.save(dto.getInfo());
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
		
		List<DcmUserEducation> list = this.userEduDAO.findByProperty("userId", refId);
		if(list != null && !list.isEmpty()){
			for(DcmUserEducation entity : list){
				this.userAttachmentDAO.removeByProperty("referenceId", entity.getId());
				this.userEduDAO.remove(entity);
			}
			
		}
		
		return true;
	}

}
