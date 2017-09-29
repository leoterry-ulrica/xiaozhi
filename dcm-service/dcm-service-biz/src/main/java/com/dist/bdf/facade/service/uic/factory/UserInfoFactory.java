package com.dist.bdf.facade.service.uic.factory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.dist.bdf.base.exception.BusinessException;
import com.dist.bdf.facade.service.uic.domain.UserDetailInfoDmn;

@Component
public class UserInfoFactory {

	@Autowired
	@Qualifier(value = "UserDetailArticleInfoDmn")
	private UserDetailInfoDmn userDetailArticleInfoDmn;
	@Autowired
	@Qualifier(value = "UserDetailCertificateInfoDmn")
	private UserDetailInfoDmn userDetailCertificateInfoDmn;
	@Autowired
	@Qualifier(value = "UserDetailEducationDmn")
	private UserDetailInfoDmn userDetailEducationDmn;
	@Autowired
	@Qualifier(value = "UserDetailExperienceDmn")
	private UserDetailInfoDmn userDetailExperienceDmn;
	@Autowired
	@Qualifier(value = "UserDetailPrjExperienceDmn")
	private UserDetailInfoDmn userDetailPrjExperienceDmn;
	@Autowired
	@Qualifier(value = "UserDetailLanguagesDmn")
	private UserDetailInfoDmn userDetailLanguagesDmn;
	@Autowired
	@Qualifier(value = "UserDetailOtherDmn")
	private UserDetailInfoDmn userDetailOtherDmn;
	@Autowired
	@Qualifier(value = "UserDetailTitleDmn")
	private UserDetailInfoDmn userDetailTitleDmn;
	@Autowired
	@Qualifier(value = "UserDetailTrainingDmn")
	private UserDetailInfoDmn userDetailTrainingDmn;
	
	public UserDetailInfoDmn getDetailDmn(String flag) {
		
		UserDetailInfoDmn instance = null;
		
		switch (flag) {
		case "ar": // 学术成果
			instance = this.userDetailArticleInfoDmn;
			break;
		case "cer": // 执业资格
			instance = this.userDetailCertificateInfoDmn;
			break;
		case "edu": // 教育
			instance = this.userDetailEducationDmn;	
			break;	
		case "wexp": // 工作经历
			instance = this.userDetailExperienceDmn;	
			break;	
		case "pexp": // 项目经历
			instance = this.userDetailPrjExperienceDmn;
			break;	
		case "lang": // 语言等级
			instance = this.userDetailLanguagesDmn;
			break;	
		case "o": // 其它信息
			instance = this.userDetailOtherDmn;
			break;	
		case "ti": // 职称信息
			instance = this.userDetailTitleDmn;
			break;	
		case "tr": // 培训经历
			instance = this.userDetailTrainingDmn;
			break;	
		default:
			throw new BusinessException("没有找到对应的用户详情领域对象。");
			
		}
		return instance;
	}
}
