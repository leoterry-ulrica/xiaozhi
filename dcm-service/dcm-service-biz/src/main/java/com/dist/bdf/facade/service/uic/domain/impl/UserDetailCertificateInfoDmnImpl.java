package com.dist.bdf.facade.service.uic.domain.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dist.bdf.facade.service.uic.dao.DcmAttachmentDAO;
import com.dist.bdf.facade.service.uic.dao.DcmUserCertificateInfoDAO;
import com.dist.bdf.facade.service.uic.domain.UserDetailInfoDmn;
import com.dist.bdf.model.dto.system.user.UserCertificateInfoDTO;
import com.dist.bdf.model.entity.system.DcmAttachment;
import com.dist.bdf.model.entity.system.DcmUserCertificateInfo;

@Component("UserDetailCertificateInfoDmn")
@Transactional(propagation = Propagation.REQUIRED) 
public class UserDetailCertificateInfoDmnImpl implements UserDetailInfoDmn {

	@Autowired
	private DcmUserCertificateInfoDAO userCertDAO;
	//private GenericDAOImpl<DcmUserCertificateInfo, Long> userCertDAO;
	
	@Autowired
	private DcmAttachmentDAO userAttachmentDAO;
	//private GenericDAOImpl<DcmAttachment, Long> userAttachmentDAO;
	
	@Override
	public Object doSave(Object detail) {
		UserCertificateInfoDTO dto = (UserCertificateInfoDTO) detail;
		DcmUserCertificateInfo entity = this.userCertDAO.save(dto.getInfo());
		List<DcmAttachment> attas = dto.getAttachments();
		this.userAttachmentDAO.save(dto.getAttachments());
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
		
		List<DcmUserCertificateInfo> list = this.userCertDAO.findByProperty("userId", refId);
		if(list != null && !list.isEmpty()){
			for(DcmUserCertificateInfo entity : list){
				this.userAttachmentDAO.removeByProperty("referenceId", entity.getId());
				this.userCertDAO.remove(entity);
			}
		}
		
		return true;
	}

}
