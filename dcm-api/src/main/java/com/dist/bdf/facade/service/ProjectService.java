package com.dist.bdf.facade.service;

import java.util.List;
import java.util.Map;

import com.dist.bdf.base.exception.BusinessException;
import com.dist.bdf.base.page.Pagination;
import com.dist.bdf.model.dto.dcm.CaseDTO;
import com.dist.bdf.model.dto.dcm.CaseTypeDTO;
import com.dist.bdf.model.dto.dcm.DocumentDTO;
import com.dist.bdf.model.dto.sga.ImgInfo;
import com.dist.bdf.model.dto.system.ChannelDTO;
import com.dist.bdf.model.dto.system.GroupSummaryDTO;
import com.dist.bdf.model.dto.system.HotProjectSummaryDTO;
import com.dist.bdf.model.dto.system.M2ultipartFileDTO;
import com.dist.bdf.model.dto.system.NaotuTeamAddDTO;
import com.dist.bdf.model.dto.system.ProjectStatDTO;
import com.dist.bdf.model.dto.system.ProjectSummaryDTO;
import com.dist.bdf.model.dto.system.ProjectTeamDTO;
import com.dist.bdf.model.dto.system.TaskAddDTO;
import com.dist.bdf.model.dto.system.TaskResponseDTO;
import com.dist.bdf.model.dto.system.TaskSummaryFilterDTO;
import com.dist.bdf.model.dto.system.WzInfoDTO;
import com.dist.bdf.model.dto.system.WzInfoPageDTO;
import com.dist.bdf.model.dto.system.WzInfoParaDTO;
import com.dist.bdf.model.dto.system.WzInfoParaWebDTO;
import com.dist.bdf.model.dto.system.WzInfoSendToUserDTO;
import com.dist.bdf.model.dto.system.team.TeamLeaderRequestDTO;
import com.dist.bdf.model.dto.system.workgroup.WorkGroupAddDTO;
import com.dist.bdf.model.dto.system.workgroup.WorkGroupOrgAddDTO;
import com.dist.bdf.model.entity.system.DcmDicChannel;
import com.dist.bdf.model.entity.system.DcmTeam;
import com.dist.bdf.model.entity.system.DcmWgOrg;
import com.dist.bdf.model.entity.system.DcmWorkGroup;

/**
 * 
 * @author weifj
 * @version 1.0，2016/05/11，weifj，创建提供项目功能的服务，其它模块的涉及到项目本身的，后续也会迁移到此模块中
 */
public interface ProjectService {

	/**
	 * 获取所有case类型
	 * @return
	 */
	List<CaseTypeDTO> getCaseTypes();
	/**
	 * 获取case实例
	 * @param typePrefix
	 * @return
	 */
	List<CaseDTO> getCasesByType(String typePrefix);
	/**
	 * 获取所有的案例数据
	 * @return
	 */
	Object getAllCases();

	/**
	 * 根据案例标识获取案例信息
	 * @param caseIdentifier
	 * @return
	 */
	@Deprecated
	CaseDTO getCaseByIdentifier(String caseIdentifier);
	/**
	 * 根据案例标识获取案例信息
	 * @param userId
	 * @param pwd
	 * @param caseIdentifier
	 * @return
	 */
	CaseDTO getCaseByIdentifier(String realm, String userId, String pwd, String caseIdentifier);
	/**
	 * 根据案例id获取案例信息
	 * @param realm
	 * @param userId
	 * @param pwd
	 * @param caseId
	 * @return
	 */
	CaseDTO getCaseById(String realm, String userId, String pwd, String caseId);
	/**
	 * 
	 * @param realm
	 * @param caseId
	 * @return
	 */
	CaseDTO getCaseById(String realm, String caseId);
	/**
	 * 获取项目的资料包
	 * @param caseIdentifier 案例标识
	 * @return
	 */
	@Deprecated
	Object getPackages(String caseIdentifier);
	/**
	 * 获取项目的资料包
	 * @param realm
	 * @param caseIdentifier
	 * @return
	 */
	List<DocumentDTO> getPackages(String realm, String caseIdentifier);
	/**
	 * 获取项目的资料包
	 * @param realm 域
	 * @param userId
	 * @param pwd
	 * @param caseGuid 案例id
	 * @return
	 */
	List<DocumentDTO> getPackages(String realm, String userId, String pwd, String caseIdentifier);
	/**
	 * 获取项目的统计信息
	 * @param caseIdentifier
	 * @return
	 */
	Object getStatInfo(String caseIdentifier);
	/**
	 * 根据域和案例id获取统计信息
	 * @param realm
	 * @param caseId
	 * @return
	 */
	Object getStatInfo(String realm, String caseId);
	/**
	 * 删除项目
	 * @param caseIdentifier 案例标识
	 * @return
	 */
	Object deleteProject(String caseIdentifier);
	/**
	 * 根据guid删除项目，以及项目相关
	 * @param guid
	 * @return
	 */
	Object deleteProjectByGuid(String guid);
	/**
	 * 获取个人的项目
	 * @param userId 用户登录名
	 * @return
	 */
	@Deprecated
	Object getMyProjects(String userId);
	/**
	 * 获取个人的项目
	 * @param realm 域
	 * @param userId
	 * @param pwd
	 * @return
	 */
	Object getMyProjects(String realm, String userId, String pwd);
	/**
	 * 获取个人的项目
	 * @param realm
	 * @param userId
	 * @return
	 */
	Object getMyProjects(String realm, String userId);
	
	/**
	 * 添加子任务
	 * @param caseIdentifier 案例标识
	 * @param taskId 子任务id
	 * @param parentTaskId 父任务id，特指项目的管理节点
	 * @param taskName 任务名称
	 */
	void addTask(String caseIdentifier, String taskId, String parentTaskId, String taskName);
	/**
	 * 根据任务id删除实体
	 * @param taskId
	 */
	void deleteTaskById(String taskId);
	/**
	 * 根据案例标识删除任务
	 * @param caseIdentifier
	 */
	void deleteTaskByCaseIdentifier(String caseIdentifier);
	/**
	 * 根据管理节点删除子任务
	 * @param parentTaskId
	 */
	void deleteTasksByParentId(String parentTaskId);
	/**
	 * 根据案例标识获取子任务，并根据时间排序
	 * @param caseIdentifier 案例标识
	 * @return
	 */
	Object getSubTasks(String caseIdentifier);	/**
	 * 获取所有频道
	 * @return
	 */
	Object listChannels();
	/**
	 * 根据案例获取频道
	 * @param caseId
	 * @return
	 */
	Object listChannels(String caseId);
	/**
	 * 添加频道
	 * @param code 频道编码
	 * @param name 频道名字
	 * @param isBuildIn 是否内置，1：内置；0：自定义
	 * @return
	 */
	@Deprecated
	Object addChannel(String code, String name, Long isBuildIn)  throws BusinessException;
	/**
	 * 添加频道
	 * @param dto
	 * @param isBuildIn
	 * @return
	 */
	DcmDicChannel addChannel(ChannelDTO dto, Long isBuildIn) throws BusinessException;
	/**
	 * 修改频道
	 * @param id
	 * @param name 新名字
	 * @return
	 */
	Object updateChannel(Long id, String newName) throws BusinessException;
	/**
	 * 删除频道
	 * @param id
	 * @return
	 */
	Object deleteChannel(Long id);
	/**
	 * 删除跟项目相关的频道
	 * @param caseId
	 */
	void deleteProjectChannel(String caseId);
	/**
	 * 提供给项目广规院使用
	 * @param realm
	 * @param userId
	 * @return
	 */
	Object getMyProjectsGZ(String realm, String userId);
	/**
	 * 根据域和案例标识，获取案例信息
	 * @param realm
	 * @param caseIdentifier
	 * @return
	 */
	CaseDTO getCaseByIdentifier(String realm, String caseIdentifier);
	/**
	 * 根据域和案例标识，获取案例中的“合作项目”文件夹
	 * @param realm
	 * @param caseId 案例id
	 * @return
	 */
	Object getCooperationPackage(String realm, String caseId);
	/**
	 * 添加团队，不做重复性检查
	 * @param caseId
	 * @param name
	 * @return
	 */
	DcmTeam addTeam(String caseId, String name);
	/**
	 * 修改团队名称
	 * @param teamId
	 * @param name
	 * @return
	 */
	boolean modifyTeamName(Long teamId, String newName);
	/**
	 * 添加团队成员
	 * @param teamId
	 * @param userId
	 * @return
	 */
	boolean addTeamUser(Long teamId, Long userId);
	/**
	 * 添加团队成员
	 * @param caseId
	 * @param teamId
	 * @param userId
	 * @return
	 */
	boolean addTeamUser(String caseId, Long teamId, Long userId);
	/**
	 * 根据案例id获取项目的团队信息
	 * @param caseId
	 * @return
	 */
	List<ProjectTeamDTO> getTeams(String caseId);
	/**
	 * 删除团队成员
	 * @param teamId
	 * @param userId
	 * @return
	 */
	void deleteTeamUsers(Long teamId, Long[] userIds);
	/**
	 * 删除团队
	 * @param teamId
	 * @return
	 */
	void deleteTeam(Long teamId);
	/**
	 * 根据用户标识，删除与所有团队的关联关系
	 * @param userIds
	 */
	@Deprecated
	void removeUsersFromTeam(Long[] userIds);
	/**
	 * 设置团队队长
	 * @param dto
	 * @return
	 */
	Object setTeamLeader(TeamLeaderRequestDTO dto);
	/**
	 * 记录任务
	 * @param dto
	 */
	void saveOrUpdateTask(TaskAddDTO dto);
	/**
	 * 根据案例id获取任务属性信息
	 * @param caseId
	 * @return
	 */
	Object getTasksByCaseId(String caseId);
	
	 /**
	    * 获取案例下的微作
	    * @param caseIdentifier 案例标识
	    * @param pageNo 页码
	    * @param pageSize 每页大小
	    * @return
	    */
	Object getWZOfCase(String caseIdentifier, int pageNo, int pageSize);
	 /**
	    * 根据案例id获取其微作列表
	    * @param pageInfo
	    * @return
	    */
	Pagination getWZsOfCaseById(WzInfoPageDTO pageInfo);
	/**
	 * 根据微作id，获取微作对象
	 * @param realm 域，desktop值
	 * @param id
	 * @return
	 */
	WzInfoDTO getWZById(String realm, String id);
	/**
	    * 通过移动设备，创建微作
	    * @param WzInfoDTO 微作信息
	    * @param docsStream 文件流集合
	    * @return
	    */
	WzInfoDTO createWZForMobile(WzInfoParaDTO infoDTO, List<M2ultipartFileDTO> files);
	   /**
	    * 通过web端，创建微作
	    * @param infoDTO
	    * @return
	    */
	Object createWZForWeb(WzInfoParaWebDTO infoDTO);
	/**
	 * 更新微作评论数，并返回当前评论数
	 * @param userId
	 * @param password
	 * @param wzId 微作id
	 * @param op 操作类型，增加：1；减少：0
	 * @return
	 */
	/*int updateWzCommentsCount(String userId, String password, String wzId, int op);*/
	/**
	 * 更新微作评论数，并返回当前评论数
	 * @param wzId 微作id
	 * @param op 操作类型，增加：1；减少：0
	 * @return 
	 */
	int updateWzCommentsCount(String realm, String wzId, int op);
	/**
	 * 根据任务 id获取属性
	 * @param taskId
	 * @return
	 */
	TaskResponseDTO getTaskById(String taskId);
	/**
	 * 通过邮箱，发送微作内容给相关人
	 * @param dto
	 * @return
	 */
	Object sendWzToUserByEmail(WzInfoSendToUserDTO dto);
	/**
	 * 根据类型筛选微作信息
	 * @param wzType 0：普通微作，1：调研微作，2：任务微作
	 * @param pageInfo
	 * @return
	 */
	Pagination getWZsOfCaseById(int wzType, WzInfoPageDTO pageInfo);
	Object getWZsOfCaseByIdForMobile(int wzType, WzInfoPageDTO pageInfo, String baseURL, String contextPath);
	/**
	 * 获取个人的任务汇总，包括管辖范围内的任务
	 * @param userCode
	 * @param pageNo 
	 * @param pageSize
	 * @return
	 */
	// Pagination getTaskSummary(String userCode, int pageNo, int pageSize);
	/**
	 * 获取个人的任务汇总，包括管辖范围内的任务
	 * @param filterInfo
	 * @return
	 */
	Pagination getTaskSummary(TaskSummaryFilterDTO filterInfo);
	/**
	 * 分页模糊检索项目名称，任意匹配
	 * @param realm
	 * @param pageNo
	 * @param pageSize
	 * @param keyword
	 * @return
	 */
	Pagination fuzzySearchProjectName(String realm, Integer pageNo, Integer pageSize, String keyword);
	/**
	 * 删除团队下的人员
	 * @param users key：用户编码，value：0，内部；1：外部
	 */
	void removeUsersFromTeam(Map<String, Integer> users);
	/**
	 * 更新微作点赞数
	 * @param realm
	 * @param wzId
	 * @param op
	 * @return
	 */
	int updateWzUpvoteCount(String realm, String wzId, int op);
	/**
	 * 更新微作收藏数
	 * @param realm
	 * @param wzId
	 * @param op
	 * @return
	 */
	int updateWzFavoriteCount(String realm, String wzId, int op);
	/**
	 * 根据id删除微作实体
	 * @param resId
	 */
	void deleteWzById(String realm, String resId);
	/**
	 * 扩展类，获取任务汇总
	 * @param filterInfo
	 * @return
	 */
	Pagination getTaskSummaryEx(TaskSummaryFilterDTO filterInfo);
	/**
	 * 获取项目汇总
	 * @param userId
	 * @return
	 */
	List<ProjectSummaryDTO> getProjectSummary(String realm, String userCode);
	/**
	 * 获取项目活跃度信息
	 * @param realm
	 * @param caseId
	 * @param size 数组大小
	 * @return
	 */
	List<ProjectStatDTO> getStatInfoEx(String realm, String caseId, int size);
	/**
	 * 更新任务发布时间
	 * @param realm
	 * @return
	 */
	void syncTaskPublishTime(String realm);
	/**
	 * 发送微信小程序模板消息
	 * @param projectCode
	 * @param userCode
	 * @throws BusinessException
	 */
	void sendWechatTemplateMsg(String projectCode, String userCode) throws BusinessException;
	/**
	 * 获取项目中用户信息
	 * @param caseId 
	 * @param contextPath
	 * @param baseURL
	 * @return
	 */
	Object getProjectUsers(String caseId, String contextPath, String baseURL);
	/**
	 * 根据项目id，获取项目汇总
	 * @param caseIds
	 * @return
	 */
	Object getProjectSummaryById(String[] caseIds);
	/**
	 * 检测用户在项目组的有效性
	 * @param caseId
	 * @param userCode
	 * @return
	 */
	boolean checkUserInProject(String caseId, String userCode);
	/**
	 * 没有case的微作，没有缩略图
	 * @param wzType
	 * @param pageInfo
	 * @return
	 */
	Pagination getWZsOfCaseNoThumbById(int wzType, WzInfoPageDTO pageInfo);
	
	WzInfoDTO getWZByIdForMobile(String realm, String id, String baseURL, String contextPath);
	Pagination getWZsOfCaseByIdThumbByte(int wzType, WzInfoPageDTO pageInfo);
	/**
	 * 根据id获取微作信息，缩略图返回字节流
	 * @param realm
	 * @param id
	 * @return
	 */
	WzInfoDTO getWZByIdThumbnailByte(String realm, String id);
	/**
	 * 
	    * @Title: getProjectSummary
	    * @Description: 项目汇总信息
	    * @param @param realm
	    * @param @param userCode
	    * @param @param pageNo
	    * @param @param pageSize
	    * @param @param projectTypes
	    * @param @return    参数
	    * @return Pagination    返回类型
	    * @throws
	 */
	Pagination getProjectInfoSummary(String realm, String userCode, int pageNo, int pageSize, String[] projectTypes);
	/**
	 * 获取微作关联的文件系列id列表
	 * @param realm 域
	 * @param wzId 微作id
	 * @return
	 */
	List<String> getRefFileIdsOfWz(String realm, String wzId);
	/**
	 * 获取热门项目的汇总信息
	 * @param realm
	 * @return
	 */
	 List<HotProjectSummaryDTO> getHotProjectSummaryByRealm(String realm);
	 /**
	  * 获取项目组和团队的汇总信息
	  * @param realm
	  * @return
	  */
	 GroupSummaryDTO getGroupsSummaryByRealm(String realm);
	 /**
	  * 根据项目编码删除项目背景图
	  * @param id
	  */
	void deleteImgInMongoByProjectCode(String code);
	/**
	 * 保存项目背景图到mongo
	 * @param imgInfo
	 * @return
	 */
	String storeImgToMongo(ImgInfo imgInfo);
	/**
	 * 更新项目背景图url
	 * @param relativeUrl 相对路径
	 * @return 返回项目url
	 */
	String updateProjectImgURL(String projectGuid, String relativeUrl);
	/**
	 * 更新是否热门项目状态
	 * @param realm
	 * @param caseId
	 * @param status 状态，0：非热门；1：热门
	 * @return 
	 */
	Boolean updateHotProjectStatus(String realm, String caseId, int status);
	/**
	 * 获取项目背景图URL
	 * @param projectGuid
	 * @return
	 */
	Object getProjectImgURL(String projectGuid);
	/**
	 * 添加工作组
	 * @param dto
	 * @return
	 */
	DcmWorkGroup addWorkGroup(WorkGroupAddDTO dto);
	/**
	 * 添加机构到工作组
	 * @param dto
	 * @return
	 */
	DcmWgOrg addOrgToWorkgroup(WorkGroupOrgAddDTO dto);
	/**
	 * 从工作组删除机构关联
	 * @param workgroupId
	 * @param orgGuid
	 */
	void deleteOrgFromWorkgroup(Long workgroupId, String orgGuid);
	/**
	 * 根据项目唯一标识删除相关工作组
	 * @param projectGuid
	 */
	void deleteWorkgroupsByProjectGuid(String projectGuid);
	/**
	 * 根据项目唯一标识获取相关工作组信息
	 * @param projectGuid
	 * @return
	 */
	Object getWorkgroupsByProjectGuid(String projectGuid);
	/**
	 * 删除工作组
	 * @param workgroupId
	 * @return
	 */
	void deleteWorkGroup(Long workgroupId);
	/**
	 * 获取任务汇总新服务，如果有权限PRIV_TASK_SUMMARY，并且scope=1，则是全院下的任务汇总；如果scope=0，则是用户所在部门的任务汇总
	 * @param filterInfo
	 * @return
	 */
	Pagination getTaskSummaryNew(TaskSummaryFilterDTO filterInfo);
	/**
	 * 新增脑图和团队关联
	 * @param dto
	 * @return
	 */
	Object addNaotuTeamRef(NaotuTeamAddDTO dto);
	/**
	 * 分页获取团队组
	 * @param realm
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	Object getTeamGroups(String realm, int pageNo, int pageSize);
	/**
	 * 获取脑图关联的团队信息
	 * @param realm
	 * @param caseId
	 * @param minderId
	 * @return
	 */
	Object getNaotuRefTeams(String realm, String caseId, Integer minderId);
	/**
	 * 删除脑图和团队关联
	 * @param realm
	 * @param caseId
	 * @param minderId
	 * @param nodeId
	 * @param teamId
	 * @return
	 */
	Object deleteNaotuTeamRef(String realm, String caseId, int minderId, String nodeId, String teamId);
	/**
	 * 删除脑图节点，级联删除关联的团队
	 * @param realm
	 * @param caseId
	 * @param minderId
	 * @param nodeId
	 * @param teamId
	 * @return
	 */
	Object deleteNaotuTeamRef(String realm, String caseId, int minderId, String nodeId);
	/**
	 * 删除脑图，级联删除关联的团队
	 * @param realm
	 * @param caseId
	 * @param minderId
	 * @param nodeId
	 * @param teamId
	 * @return
	 */
	Object deleteNaotuTeamRef(String realm, String caseId, int minderId);
}
