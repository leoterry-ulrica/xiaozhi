package com.dist.bdf.facade.service.sga;

import java.util.Date;
import java.util.List;

import com.dist.bdf.base.exception.BusinessException;
import com.dist.bdf.model.dto.sga.CooProjectAddDTO;
import com.dist.bdf.model.dto.sga.CooProjectResponseDTO;
import com.dist.bdf.model.dto.sga.FileRecordDTO;
import com.dist.bdf.model.dto.sga.ImgInfo;
import com.dist.bdf.model.dto.sga.InvitationInfoDTO;
import com.dist.bdf.model.dto.sga.PrjUserRequestDTO;
import com.dist.bdf.model.dto.sga.PrjUserStatusPutDTO;
import com.dist.bdf.model.dto.sga.ProjectUpdateDTO;
import com.dist.bdf.model.dto.sga.UserResponseDTO;
import com.dist.bdf.model.entity.sga.SgaPrjUser;
import com.dist.bdf.model.entity.sga.SgaPrjWz;
import com.dist.bdf.model.entity.sga.SgaProject;

/**
 * 公共项目服务接口
 * 
 * @author weifj
 *
 */
public interface SgaProjectService {

	/**
	 * 获取所有合作项目信息
	 * @return
	 */
	List<SgaProject> getAllProject();
	/**
	 * 创建项目
	 * @param dto
	 * @return
	 */
	Object saveOrUpdate(CooProjectAddDTO dto) throws BusinessException;
	/**
	 * 更新项目基本信息
	 * @param dto
	 * @return
	 */
	Object updateBasicInfo(ProjectUpdateDTO dto);
	/**
	 * 单独修改项目状态
	 * @param caseId ecm中案例id
	 * @param mark 标记，0：关闭；1：招募中；2：合作中；3：合作结束
	 * @return
	 */
	Object changeStatus(String caseId, int mark) throws BusinessException;
	/**
	 * 修改海报
	 * @param caseId
	 * @param poster
	 * @return
	 */
	String changePoster(String caseId, String poster) throws BusinessException;
	/**
	 * 更新海报
	 * @param caseId
	 * @param img
	 * @return
	 */
	Object updatePoster(String caseId, String img) throws BusinessException;
	/**
	 * 更新报名人数
	 * @param caseId
	 * @return
	 */
	Object updateRegisterCount(String caseId) throws BusinessException;
	/**
	 * 更新加入人数
	 * @param caseId
	 * @param opType 操作类型，-1：减1；1：加1
	 * @return
	 */
	Object updateJoinInCount(String caseId, int opType) throws BusinessException;
	/**
	 * 发送邀请
	 * @param dto 
	 * @return
	 */
	Object sendInvitation(InvitationInfoDTO dto);
	/**
	 * 根据项目状态过滤项目
	 * @param status 0：关闭；1：招募中；2：合作中；3：合作结束
	 * @return
	 */
	List<SgaProject> getProjectsByStatus(int status);
	/**
	 * 根据企业id获取项目
	 * @param companyId
	 * @return
	 */
	List<SgaProject> getProjectsByCompanyId(Long companyId);
	/**
	 * 根据企业的标识名称，获取企业下所有项目信息
	 * @param name
	 * @return
	 */
	@Deprecated
	List<CooProjectResponseDTO> getProjectsByCompanyRealm(String name) throws BusinessException;
	/**
	 * 添加成员到项目组
	 * @param caseId 对应case 的id号
	 * @param userId 用户id
	 * @param status 状态，此时状态应该以下三种：0：待审核；1：参与；2：待定
	 * @param description 添加的理由
	 * @param direction 方向
	 * @return
	 */
	// Object addUserToProject(String caseId, Long userId, int status, String description, String direction) throws BusinessException;
	/**
	 * 更新项目组成员的状态
	 * @param caseId 对应case 的id号
	 * @param userId
	 * @param status
	 * @param description 更新的理由
	 * @return
	 */
	Object updateUserStatusInProject(String caseId, Long userId, int status, String description);
	/**
	 * 根据项目唯一标识，获取项目中已报名人员，包括审核不通过的
	 * @param caseId 对应case 的id号
	 * @return
	 */
	List<UserResponseDTO> getRegisterUsers(String caseId) throws BusinessException;
	/**
	 * 根据项目唯一标识，获取项目中已招募人员
	 * @param caseId 对应case 的id号
	 * @return
	 */
	List<UserResponseDTO> getJoinInUsers(String caseId) throws BusinessException;
	/**
	 * 根据项目id获取项目信息
	 * @param comId
	 * @return
	 */
	Object getProjectById(Long id) throws BusinessException;
	/**
	 * 根据项目案例标识获取项目信息
	 * @param caseId
	 * @return
	 */
	CooProjectResponseDTO getProjectByCaseId(String caseId) throws BusinessException;
	/**
	 * 记录文件信息，包括合作者上传和下载
	 * @param dto
	 * @return
	 */
	Object recordUploadFile(FileRecordDTO dto);
	/**
	 * 记录微作的信息
	 * @param caseId 案例id
	 * @param wzId 微作id
	 * @param creator 创建者
	 * @return
	 */
	boolean recordWz(String caseId, String wzId, String creator, Date createTime);
	/**
	 * 根据微作id删除项目与微作的关联
	 * @param wzId
	 * @return
	 */
	void deletePrjWzRecordByWzId(String wzId);
	/**
	 * 报名项目，但默认状态都是待审核
	 * @param dto
	 */
	Object joinInProject(PrjUserRequestDTO dto);
	/**
	 * 获取跟用户相关的项目信息
	 * @param userId 用户序列id
	 * @return
	 */
	List<CooProjectResponseDTO> getProjectsByUserId(Long userId);
	/**
	 * 根据企业的标识名称和用户id，获取企业下所有项目信息
	 * @param realm
	 * @param userId
	 * @return
	 */
	List<CooProjectResponseDTO> getProjectsByCompanyRealm(String realm, Long userId);
	/**
	 * 获取项目中已经报名的用户信息
	 * @param caseId
	 * @return
	 */
	List<UserResponseDTO> listRegisterUsers(String caseId);
	/**
	 * 设置用户在项目中的状态
	 * @param caseId
	 * @param userId
	 * @return
	 */
	Object setUserStatusInProject(PrjUserStatusPutDTO dto);
	/**
	 * 根据微作id获取关联信息
	 * @param guid
	 * @return
	 */
	SgaPrjWz getRefWzById(String wzId);
	/**
	 * 删除项目
	 * @param caseId
	 */
	void deleteProjectByCode(String caseId);
	/**
	 * 获取项目关联用户
	 * @param projectCode
	 * @param userId
	 * @return
	 */
	SgaPrjUser getPrjRefUser(String projectCode, Long userId);
	/**
	 * 根据项目编码删除背景图
	 * @param code
	 */
	void deletePosterByProjectCode(String code);
	/**
	 * 设置海报
	 * @param imgInfo
	 * @return 返回图片相对路径
	 */
	String updatePoster(ImgInfo imgInfo);
}
