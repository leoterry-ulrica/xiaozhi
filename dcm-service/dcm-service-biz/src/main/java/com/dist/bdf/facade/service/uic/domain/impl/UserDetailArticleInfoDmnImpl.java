package com.dist.bdf.facade.service.uic.domain.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dist.bdf.facade.service.uic.dao.DcmAttachmentDAO;
import com.dist.bdf.facade.service.uic.dao.DcmUserArticleInfoDAO;
import com.dist.bdf.facade.service.uic.domain.UserDetailInfoDmn;
import com.dist.bdf.model.dto.system.user.UserArticleInfoDTO;
import com.dist.bdf.model.entity.system.DcmAttachment;
import com.dist.bdf.model.entity.system.DcmUserArticleInfo;

@Component("UserDetailArticleInfoDmn")
@Transactional(propagation = Propagation.REQUIRED) 
public class UserDetailArticleInfoDmnImpl implements UserDetailInfoDmn {


	@Autowired
	private DcmUserArticleInfoDAO userArticleInfoDAO;
	//private GenericDAOImpl<DcmUserArticleInfo, Long> userArticleInfoDAO;
	@Autowired
	private DcmAttachmentDAO userAttachmentDAO;
	//private GenericDAOImpl<DcmAttachment, Long> userAttachmentDAO;
	
	@Override
	public Object doSave(Object detail) {
		
		UserArticleInfoDTO dto = (UserArticleInfoDTO) detail;
		DcmUserArticleInfo entity = userArticleInfoDAO.save(dto.getInfo());
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
		
		List<DcmUserArticleInfo> list = this.userArticleInfoDAO.findByProperty("userId", refId);
		if(list != null && !list.isEmpty()){
			for(DcmUserArticleInfo entity : list){
				this.userAttachmentDAO.removeByProperty("referenceId", entity.getId());
				this.userArticleInfoDAO.remove(entity);
			}
			
		}
		
		return true;
	}

}
