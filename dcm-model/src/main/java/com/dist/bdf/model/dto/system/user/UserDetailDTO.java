package com.dist.bdf.model.dto.system.user;

import java.io.Serializable;
import java.util.List;

import com.dist.bdf.model.entity.system.DcmAttachment;

/**
 * 用户详细信息
 * @author weifj
 *
 */
public class UserDetailDTO  implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long userId;
	/**
	 * 学术成果
	 */
	private List<UserArticleInfoDTO> articleInfos;
	/**
	 * 执业资格
	 */
	private List<UserCertificateInfoDTO> certificateInfos;
	/**
	 * 教育背景
	 */
	private List<UserEducationDTO> educations;
	/**
	 * 工作经验和项目经验
	 */
	private List<UserWorkExperienceDTO> experiences;
	/**
	 * 语言水平
	 */
	private List<UserLanguageDTO> languages;
	/**
	 * 职称信息
	 */
	private List<UserTitleInfoDTO> titleInfos;
	/**
	 * 培训经历
	 */
	private List<UserTrainingDTO> trainings;
	/**
	 * 其它附件信息
	 */
	private List<UserOtherInfoDTO> others;
	
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	public List<UserArticleInfoDTO> getArticleInfos() {
		return articleInfos;
	}
	public void setArticleInfos(List<UserArticleInfoDTO> articleInfos) {
		this.articleInfos = articleInfos;
	}
	public List<UserCertificateInfoDTO> getCertificateInfos() {
		return certificateInfos;
	}
	public void setCertificateInfos(List<UserCertificateInfoDTO> certificateInfos) {
		this.certificateInfos = certificateInfos;
	}
	public List<UserEducationDTO> getEducations() {
		return educations;
	}
	public void setEducations(List<UserEducationDTO> educations) {
		this.educations = educations;
	}
	public List<UserWorkExperienceDTO> getExperiences() {
		return experiences;
	}
	public void setExperiences(List<UserWorkExperienceDTO> workExperiences) {
		this.experiences = workExperiences;
	}
	public List<UserLanguageDTO> getLanguages() {
		return languages;
	}
	public void setLanguages(List<UserLanguageDTO> languages) {
		this.languages = languages;
	}
	public List<UserTitleInfoDTO> getTitleInfos() {
		return titleInfos;
	}
	public void setTitleInfos(List<UserTitleInfoDTO> titleInfos) {
		this.titleInfos = titleInfos;
	}
	public List<UserTrainingDTO> getTrainings() {
		return trainings;
	}
	public void setTrainings(List<UserTrainingDTO> trainings) {
		this.trainings = trainings;
	}
	public List<UserOtherInfoDTO> getOthers() {
		return others;
	}
	public void setOthers(List<UserOtherInfoDTO> others) {
		this.others = others;
	}


	
}
