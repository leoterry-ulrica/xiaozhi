package com.dist.bdf.facade.service.uic.domain.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dist.bdf.facade.service.uic.dao.DcmAttachmentDAO;
import com.dist.bdf.facade.service.uic.dao.DcmUserLanguageDAO;
import com.dist.bdf.facade.service.uic.domain.UserDetailInfoDmn;
import com.dist.bdf.model.dto.system.user.UserLanguageDTO;
import com.dist.bdf.model.entity.system.DcmAttachment;
import com.dist.bdf.model.entity.system.DcmUserLanguage;

@Component("UserDetailLanguagesDmn")
@Transactional(propagation = Propagation.REQUIRED) 
public class UserDetailLanguagesDmnImpl implements UserDetailInfoDmn {

	@Autowired
	private DcmUserLanguageDAO userLanguageDAO;
	//private GenericDAOImpl<DcmUserLanguage, Long> userLanguageDAO;
	@Autowired
	private DcmAttachmentDAO userAttachmentDAO;
	//private GenericDAOImpl<DcmAttachment, Long> userAttachmentDAO;
	
	@Override
	public Object doSave(Object detail) {
		
		UserLanguageDTO dto = (UserLanguageDTO) detail;
		DcmUserLanguage entity = userLanguageDAO.save(dto.getInfo());
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
		
		List<DcmUserLanguage> list = this.userLanguageDAO.findByProperty("userId", refId);
		if(list != null && !list.isEmpty()){
			for(DcmUserLanguage entity : list){
				this.userAttachmentDAO.removeByProperty("referenceId", entity.getId());
				this.userLanguageDAO.remove(entity);
			}
			
		}
		
		return true;
	}

}
