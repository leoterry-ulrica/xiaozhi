package com.dist.bdf.facade.service.uic.domain.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dist.bdf.facade.service.uic.dao.DcmAttachmentDAO;
import com.dist.bdf.facade.service.uic.dao.DcmUserPrjExperienceDAO;
import com.dist.bdf.facade.service.uic.domain.UserDetailInfoDmn;
import com.dist.bdf.model.dto.system.user.UserPrjExperienceDTO;
import com.dist.bdf.model.entity.system.DcmAttachment;
import com.dist.bdf.model.entity.system.DcmUserPrjExperience;

@Component("UserDetailPrjExperienceDmn")
@Transactional(propagation = Propagation.REQUIRED) 
public class UserDetailPrjExperienceDmnImpl implements UserDetailInfoDmn {

	@Autowired
	private DcmUserPrjExperienceDAO userPrjExpDAO;
	//private GenericDAOImpl<DcmUserPrjExperience, Long> userPrjExpDAO;
	@Autowired
	private DcmAttachmentDAO userAttachmentDAO;
	//private GenericDAOImpl<DcmAttachment, Long> userAttachmentDAO;
	
	@Override
	public Object doSave(Object detail) {
		
		UserPrjExperienceDTO dto = (UserPrjExperienceDTO) detail;
		DcmUserPrjExperience entity = userPrjExpDAO.save(dto.getInfo());
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
		
		List<DcmUserPrjExperience> list = this.userPrjExpDAO.findByProperty("refWorkExperienceId", refId);
		if(list != null && !list.isEmpty()){
			for(DcmUserPrjExperience entity : list){
				this.userAttachmentDAO.removeByProperty("referenceId", entity.getId());
				this.userPrjExpDAO.remove(entity);
			}
			
		}
		
		return true;
	}

}
