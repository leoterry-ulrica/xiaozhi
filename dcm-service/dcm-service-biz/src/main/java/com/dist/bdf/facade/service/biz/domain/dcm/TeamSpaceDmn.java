package com.dist.bdf.facade.service.biz.domain.dcm;

import java.util.List;

import com.dist.bdf.model.dto.dcm.TeamSpaceDTO;
import com.dist.bdf.model.dto.dcm.TeamSpaceDetailDTO;


/**
 * 创建团队空间领域
 * @author weifj
 * @version 1.0，2016/03/29，weifj，创建
 */
public interface TeamSpaceDmn {

	/**
	 * 查询所有的团队空间模板
	 * @return
	 */
	public List<TeamSpaceDTO> searchAllTemplate();
	/**
	 * 检索所有团队空间实例的简单信息
	 * @return
	 */
	public List<TeamSpaceDTO> searchAllInstance();
	/**
	 * 检索所有的团队空间实例
	 * @return
	 */
	public List<TeamSpaceDetailDTO> searchAllInstanceDetail();
	/**
	 * 根据teamspace模板创建teamspace实例
	 * @param userName 当前登录用户名
	 * @param userPwd 当前登录用户密码
	 * @param tsTemplateDto 模板信息
	 * @return
	 */
	public String createTeamspace(String userName, String userPwd, TeamSpaceDTO tsTemplateDto);
	
	
}
