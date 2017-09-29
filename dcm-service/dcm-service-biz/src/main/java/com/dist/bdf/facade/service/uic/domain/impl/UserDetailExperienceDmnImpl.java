package com.dist.bdf.facade.service.uic.domain.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dist.bdf.facade.service.uic.dao.DcmAttachmentDAO;
import com.dist.bdf.facade.service.uic.dao.DcmUserWorkExperienceDAO;
import com.dist.bdf.facade.service.uic.domain.UserDetailInfoDmn;
import com.dist.bdf.model.dto.system.user.UserWorkExperienceDTO;
import com.dist.bdf.model.entity.system.DcmAttachment;
import com.dist.bdf.model.entity.system.DcmUserWorkExperience;

@Component("UserDetailExperienceDmn")
@Transactional(propagation = Propagation.REQUIRED) 
public class UserDetailExperienceDmnImpl implements UserDetailInfoDmn {

	@Autowired
	@Qualifier("UserDetailPrjExperienceDmn")
	private UserDetailInfoDmn  userDetailPrjExperienceDmn;
	@Autowired
	private DcmUserWorkExperienceDAO userWorkExpDAO;
	//private GenericDAOImpl<DcmUserWorkExperience, Long> userWorkExpDAO;
	@Autowired
	private DcmAttachmentDAO userAttachmentDAO;
	//private GenericDAOImpl<DcmAttachment, Long> userAttachmentDAO;
	
	@Override
	public Object doSave(Object detail) {
		
		UserWorkExperienceDTO dto = (UserWorkExperienceDTO) detail;
		DcmUserWorkExperience workExp = userWorkExpDAO.save(dto.getInfo());
		List<DcmAttachment> attas = dto.getAttachments();
		if(attas != null && !attas.isEmpty()){
			for(DcmAttachment atta : attas){
				atta.setReferenceId(workExp.getId());
				userAttachmentDAO.save(atta);
			}
		}
		// 项目经历
	/*	List<UserPrjExperienceDTO> projects = dto.getProjects();
		if(projects != null && !projects.isEmpty()){
			List<UserPrjExperienceDTO> newProjects = new ArrayList<UserPrjExperienceDTO>(projects.size());
			for(UserPrjExperienceDTO p : projects){
				p.getInfo().setRefWorkExperienceId(workExp.getId());
				UserPrjExperienceDTO prjDTO = (UserPrjExperienceDTO) this.userDetailPrjExperienceDmn.doSave( p);
				newProjects.add(prjDTO);
			}
			dto.setProjects(newProjects);
		}*/
		dto.setInfo(workExp);

		dto.setAttachments(attas);
		
		return dto;
	}

	@Override
	public Object delete(Long refId) {
		
		List<DcmUserWorkExperience> list = this.userWorkExpDAO.findByProperty("userId", refId);
		if(list != null && !list.isEmpty()){
			for(DcmUserWorkExperience entity : list){
				// 删除工作经历附件
				this.userAttachmentDAO.removeByProperty("referenceId", entity.getId());
				// 删除项目经历
				this.userDetailPrjExperienceDmn.delete(entity.getId());
				// 删除工作经历实体本身
				this.userWorkExpDAO.remove(entity);
			}
			
		}
		
		return true;
	}

}
