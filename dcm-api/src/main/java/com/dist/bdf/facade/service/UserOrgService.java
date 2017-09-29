
package com.dist.bdf.facade.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.dist.bdf.base.exception.BusinessException;
import com.dist.bdf.base.page.PageParams;
import com.dist.bdf.base.page.Pagination;
import com.dist.bdf.model.dto.dcm.FileContentLocalDTO;
import com.dist.bdf.model.dto.ldap.LdapPerson;
import com.dist.bdf.model.dto.ldap.LdapTree;
import com.dist.bdf.model.dto.sga.ImgInfo;
import com.dist.bdf.model.dto.system.DcmOrganizationDTO;
import com.dist.bdf.model.dto.system.Org2UsersDTO;
import com.dist.bdf.model.dto.system.OrgBasicInfoUpdateDTO;
import com.dist.bdf.model.dto.system.OrgDTO;
import com.dist.bdf.model.dto.system.OrgDelRequestDTO;
import com.dist.bdf.model.dto.system.OrgPositionAddDTO;
import com.dist.bdf.model.dto.system.OrgTeamAddDTO;
import com.dist.bdf.model.dto.system.OrgTypeAddDTO;
import com.dist.bdf.model.dto.system.OrgUserDTO;
import com.dist.bdf.model.dto.system.user.BaseUserDTO;
import com.dist.bdf.model.dto.system.user.UserAddRequestDTO;
import com.dist.bdf.model.dto.system.user.UserDTO;
import com.dist.bdf.model.dto.system.user.UserDelRequestDTO;
import com.dist.bdf.model.dto.system.user.UserDetailDTO;
import com.dist.bdf.model.dto.system.user.UserDetailRequestDTO;
import com.dist.bdf.model.dto.system.user.UserPersonUpdateRequestDTO;
import com.dist.bdf.model.dto.system.user.UserSimpleDTO;
import com.dist.bdf.model.dto.system.user.UserUpdateRequestDTO;
import com.dist.bdf.model.entity.system.DcmDicOrgExt;
import com.dist.bdf.model.entity.system.DcmOrganization;
import com.dist.bdf.model.entity.system.DcmUser;

/**
 * 用户机构服务
 * @author weifj
 * @version 1.0，2016/03/01，weifj，创建用户和机构服务
 * @version 1.1，2016/03/08，weifj
 *    1. 添加方法：delUsersFromOrg(OrgUserDto dto)，从机构删除一组人；
 *    2. 添加方法：List<DcmUser> listDirectUsersOfOrg(Long orgId)，获取机构下的直接人员列表
 *  @version 1.2，2016/03/09，weifj
 *     1. 添加方法：List<DcmUser> listUsersOfOrg(Long orgId)，获取机构下所有人员
 *  @version 1.3,2016/03/10，weifj
 *     1. 添加方法：DcmUser modifyUser(UserDto userDto)，修改用户信息
 */
public interface UserOrgService {

	/**
	 * 添加用户
	 * @param user
	 * @return
	 */
	DcmUser addUser(String contextPath, String baseURL, UserAddRequestDTO userDto)  throws BusinessException;
	/**
	 * 修改用户的基本信息，但不能修改id、登录名、是否内置用户、领域类型
	 * @param userDto
	 * @return
	 * @throws BusinessException
	 */
	DcmUser updateUserBasicInfo(UserUpdateRequestDTO userDto)  throws BusinessException;
	/**
	 * 根据登录名获取用户信息
	 * @param loginName
	 * @return
	 */
	DcmUser getUserEntityByLoginName(String loginName);
	/**
	 * 
	 * @param dn
	 * @return
	 */
	DcmUser getUserEntityByDN(String dn);
	/**
	 * 根据用户id获取用户实体
	 * @param id
	 * @return
	 */
	DcmUser getUserEntityById(Long id);
	/**
	 * 根据登录名获取用户信息，包括用户头像URL
	 * 	@param contextPath 上下文物理路径
	 * @param baseURL
	 * @param loginName
	 * @return
	 */
	UserDTO getUserByLoginName(String contextPath, String baseURL, String loginName);
	/**
	 * 根据登录名获取用户信息，包括用户头像URL
	 * @param contextPath
	 * @param baseURL
	 * @param loginName
	 * @param clientIP 客戶端請求真实ip
	 * @return
	 */
	UserDTO getUserByLoginNameEx(String contextPath, String baseURL, String loginName, String clientIP);
	/**
	 * 根据机构域和登录名获取用户信息，包括用户头像URL
	 * @param contextPath
	 * @param baseURL
	 * @param realm 机构域，例如：数慧，dist，为了支持多租户
	 * @param loginName
	 * @return
	 */
	UserDTO getUserByLoginName(String contextPath, String baseURL, String realm, String loginName);
	
	//UserDTO getUserByDN(String contextPath, String baseURL, String dn);
	/**
	 * 获取用户列表
	 * @return
	 * @throws BusinessException
	 */
	List<DcmUser> listAllUsers() throws BusinessException;
	/**
	 * 通过域查询用户信息
	 * @param realm
	 * @return
	 * @throws BusinessException
	 */
	List<DcmUser> listAllUsers(String realm) throws BusinessException;
	/**
	 * 获取用户列表，用户信息包含头像URL
	 * @param contextPath 上下文物理路径
	 * @param baseURL
	 * @return
	 * @throws BusinessException
	 */
	List<UserDTO> listAllUsers(String contextPath, String baseURL) throws BusinessException;
	/**
	 * 获取简单用户信息
	 * @param contextPath
	 * @param baseURL
	 * @return
	 */
	List<UserSimpleDTO> listSimpleUsers(String contextPath, String baseURL) ;
	/**
	 * 根据域，获取用户信息
	 * @param contextPath
	 * @param baseURL
	 * @param realm
	 * @return
	 * @throws BusinessException
	 */
	List<UserSimpleDTO> listSimpleUsers(String contextPath, String baseURL, String realm);
	/**
	 * 分页查询用户信息
	 * @param pageParam
	 * @param user
	 * @return
	 * @deprecated (已过时)
	 */
	@Deprecated
	Pagination pageUsers(PageParams pageParam, UserDTO user);
	/**
	 * 注意只是设置了删除标记
	 * @param userIds
	 */
	@Deprecated
	boolean delUsers(Object[] userIds);
	/**
	 * 添加院/所信息
	 * @param orgDto
	 * @return
	 */
	DcmOrganization addOrg(OrgDTO orgDto) throws BusinessException;
	/**
	 * 获取机构列表
	 * @return
	 */
	Object listOrgsInCache();
	/**
	 * 根据机构编码获取缓存中的机构实体DTO
	 * @param orgCode
	 * @return
	 */
	DcmOrganizationDTO getOrgDTOByCodeInCache(String orgCode);
	/**
	 * 根据机构编码获取缓存中的机构实体
	 * @param orgCode
	 * @return
	 */
	Map<Object, DcmOrganization> getOrgByCodeInCache(Object[] orgCodes);
	/**
	 * 根据编码获取机构实体，优先从缓存获取
	 * @param code
	 * @return
	 */
	DcmOrganization getOrgByCode(String code);
	/**
	 * 根据机构序列id获取实体，优先从缓存获取
	 * @param orgId
	 * @return
	 */
	DcmOrganization getOrgById(Long orgId);
	/**
	 * 添加一组人到指定机构
	 * @param dto
	 */
	@Deprecated
	void addUsersToOrg(OrgUserDTO dto);
	/**
	 * 获取机构下的直接子机构
	 * @param orgId
	 * @return
	 */
	List<DcmOrganization> listDirectChildOrgs(Long orgId);
	/**
	 * 从组中删除一组人
	 * @param dto
	 */
	void delUsersFromOrg(OrgUserDTO dto) throws BusinessException;
	/**
	 * 获取指定机构下的直接人员
	 * @param orgId 机构id
	 * @return
	 */
	List<UserDTO> listDirectUsersOfOrg(Long orgId);
	List<UserDTO> listDirectUsersOfOrg(String contextPath, String baseURL, Long orgId);
	/**
	 * 获取机构下的所有人员信息
	 * @param orgId 机构id
	 * @return
	 */
	List<DcmUser> listUsersOfOrg(Long orgId);
	/**
	 * 上传用户头像
	 * @param contextPath 上下文路径
	 * @param baseURL 请求的基础URL
	 * @param loginName 登录名
	 * @param avatarRelativePath 头像相对路径
	 * @return
	 */
	String uploadUserAvatar(String contextPath, String baseURL, String loginName, String avatarRelativePath) throws BusinessException;
	/**
	 * 
	 * @param contextPath
	 * @param baseURL
	 * @param userId
	 * @param avatarRelativePath
	 * @return
	 */
	String updateUserAvatar(DcmUser user, String baseURL, String avatarRelativePath);
	/**
	 * 更新头像
	 * @param userCode
	 * @param baseURL
	 * @param avatarRelativePath
	 * @return
	 */
	String updateUserAvatarEx(String userCode, String baseURL, String avatarRelativePath);
	/**
	 * 获取机构和用户的树状信息，如果缓存中存在，则直接从缓存获取
	 * @param loadUser 是否加载用户
	 * @return
	 */
	List<Org2UsersDTO> getOrgUserTree(boolean loadUser);
	/**
	 * 根据院的唯一标识名称，获取机构用户树状信息
	 * @param loadUser
	 * @param instituteUniqueName
	 * @return
	 */
	List<Org2UsersDTO> getOrgUserTree(boolean loadUser, String instituteUniqueName);
	/**
	 * 获取当前用户所在机构的目录树
	 * @param loginName
	 * @param loadUser
	 * @return
	 */
	List<Org2UsersDTO> getCurrOrgUserTree(String loginName, boolean loadUser);
	/**
	 * 根据用户序列id
	 * @param userSeqId
	 * @param loadUser
	 * @return
	 */
	List<Org2UsersDTO> getCurrOrgUserTree(Long userSeqId, boolean loadUser);
	/**
	 * 验证用户信息（如果开启ldap验证，则优先ldap信息验证）
	 * @param loginName 登录名
	 * @param password 密码
	 * @return
	 */
	boolean checkUserInfo(String loginName, String password);
	/**
	 * 检测新增的用户登录名是否合法
	 * @param loginName
	 * @return
	 */
	boolean checkNewUserLoginNameValid(String loginName);
	/**
	 * 
	 * @param loginName
	 * @param rootOrgRealm 院的域，例如：数慧，dist
	 * @return
	 */
	boolean checkNewUserLoginNameValid(String rootOrgRealm, String loginName);
	 //<=====================LDAP-TDS Begin======================

    /**
     * 批量创建ldap用户
     *
     * @param UserList
     * @return
     */
    void batchAddUserToLdap(List<DcmUser> UserList);

    /**
     * 获取ldap中所有人员信息
     * @return
     */
     Collection<LdapPerson> getLdapPersons();
   
    /**
     * 获取ldap中所有组和组下的成员
     * @return
     */
    Collection<LdapTree> getLdapGroups();
    /**
     * 从ldap中同步用户信息
     * @return 更新条目
     */
    @Deprecated
    void  syncUserInfoFromLdap();
    /**
     * 从ldap中同步机构信息
     */
    @Deprecated
    void syncOrganizationInfoFromLdap();
    /**
     * 同步机构和用户信息
     */
    void syncFromLdap();
    /**
     * 重写ldap同步服务接口
     */
    @Deprecated
    void syncFromLdapEx();
    /**
     * 同步云版本的ldap
     */
    @Deprecated
    void syncFromLdapForCloud();
    /**
     * 同步云版本的ldap第二版
     */
    void syncFromLdapForCloudEx();
    /**
     * 把系统的用户信息同步到ldap中
     */
    @Deprecated
    void syncToLdap();
    /**
     * 同步指定的院信息
     * 把系统的用户信息和机构信息同步到ldap中
     * @param realm
     */
    void syncToLdapEx(String realm);
	
	 //=====================LDAP-TDS End======================>
    
    /**
     * 用户验证
     * @param contextPath
     * @param baseURL
     * @param userId
     * @param password
     * @return
     */
    UserDTO authenticateUser(String contextPath, String baseURL, String userId, String password) throws BusinessException;
    /**
     * 指定院的域，进行用户验证
     * @param contextPath
     * @param baseURL
     * @param orgRealm 机构域，如数慧，即o=dist
     * @param userId
     * @param password
     * @return
     */
    UserDTO authenticateUserFromLDAP(String contextPath, String baseURL, String orgRealm, String userId, String password) throws BusinessException;
    /**
     * 指定院的域，进行用户验证
     * @param contextPath
     * @param baseURL
     * @param orgRealm 机构域，如数慧，即o=dist
     * @param userId
     * @param password
     * @param clientIP 客户端请求ip
     * @return
     */
    UserDTO authenticateUserFromLDAP(String contextPath, String baseURL, String orgRealm, String userId, String password, String clientIP) throws BusinessException;
    /**
     * 获取工作组信息，包括人员
     * @return
     */
    Object getWorkgroupInfo();
    /**
     * 根据机构域获取工作组
     * @param orgRealm 机构域，例如：数慧，dist
     * @param workgroupName 工作组名称
     * @return
     */
    Object getWorkgroupInfo(String orgRealm, String workgroupName);
    /**
     * 从缓存，根据登录名获取用户信息
     * @param loginName
     * @return
     */
    DcmUser getUserByLoginNameInCache(String loginName);
    /**
     * 从缓存中根据用户id获取用户信息
     * @param id
     * @return
     */
    DcmUser getUserByIdInCache(Long id);
    
    // 用户信息中心，以后需要单独成为模块
   
    /**
     * 更新用户详细信息
     * @param detailInfoDTO
     * @return
     */
    Object updateUserDetail(UserDetailRequestDTO detailInfoDTO)  throws BusinessException;
    /**
     * 根据登录名获取所属机构信息
     * @param loginName 登录名
     * @return
     */
    List<DcmOrganization> getOrgsOfUser(String loginName);
    /**
     * 刷新用户机构缓存信息
     */
    void refreshUserOrgCacheInfos();
    
    /**
     * 根据用户id获取详细信息
     * @param userId
     * @return
     */
    UserDetailDTO getUserDetailById(Long userId);
    
    /**
	 * 获取用户的学术成果
	 * @param refUserId 外键用户id
	 * @return
	 */
	Object getArticleInfos(Long refUserId);
	/**
	 * 获取用户的执业资格
	 * @param refUserId 外键用户id
	 * @return
	 */
	Object getCertificateInfos(Long refUserId);
	/**
	 * 获取用户的教育经历
	 * @param refUserId 外键用户id
	 * @return
	 */
	Object getEducations(Long refUserId);
	/**
	 * 获取用户的工作经历和项目经历
	 * @param refUserId 外键用户id
	 * @return
	 */
	Object getExperiences(Long refUserId);
	/**
	 * 获取用户的语言水平
	 * @param refUserId 外键用户id
	 * @return
	 */
	Object getLanguages(Long refUserId);
	
	/**
	 * 获取用户的职称信息
	 * @param refUserId 外键用户id
	 * @return
	 */
	Object getTitleInfos(Long refUserId);
	/**
	 * 获取用户的培训经历
	 * @param refUserId 外键用户id
	 * @return
	 */
	Object getTrainings(Long refUserId);
	/**
	 * 获取用户的其它附件信息
	 * @param refUserId
	 * @return
	 */
	Object getOtherAttachmentInfo(Long refUserId);
    /**
     * 保存用户的学术成果信息
     * @param info
     * @return
     */
	//Object saveUserDetailArticleInfo(Object info);
	 /**
     * 保存用户的执业资格
     * @param info
     * @return
     */
	//Object saveUserDetailCertificateInfo(Object info);
	 /**
     * 保存用户的教育经历
     * @param info
     * @return
     */
	//Object saveUserDetailEducation(Object info);
	/**
	 * 保存用户的工作经历和项目经历
	 * @param info
	 * @return
	 */
	//Object saveUserDetailExperience(Object info);
	/**
	 * 保存用户的语言等级信息
	 * @param info
	 * @return
	 */
	//Object saveUserDetailLanguage(Object info);
	/**
	 * 保存用户的职称信息
	 * @param info
	 * @return
	 */
	//Object saveUserDetailTitleInfo(Object info);
	/**
	 * 保存用户的培训经历
	 * @param info
	 * @return
	 */
	//Object saveUserDetailTraining(Object info);
	/**
	 * 保存用户的其它信息
	 * @param info
	 * @return
	 */
	//Object saveUserDetailOther(Object info);
	/**
	 * 根据标识，逐层保存用户详情信息
	 * @param flag 标识（ar、cer、edu、wexp、pexp、lang、o、ti、tr）
	 * @param info
	 * @return
	 */
	Object saveUserDetail(String flag, Object info);
	/**
	 * 根据标识，逐层删除用户详情信息
	 * @param flag 标识（ar、cer、edu、wexp、pexp、lang、o、ti、tr）
	 * @param refId
	 * @return
	 */
	Object deleteUserDetail(String flag, Long refId);
	/**
	 * 获取用户所属院
	 * @param loginName
	 * @return
	 */
	DcmOrganization getInstituteByUserLoginName(String loginName);
	/**
	 * 根据用户序列id
	 * @param userSeqId
	 * @return
	 */
	DcmOrganization getInstituteByUserSeqId(Long userSeqId);
	/**
	 * 获取院列表
	 * @return
	 */
	List<DcmOrganization> listInstitutes();
	/**
	 * 获取院下的部门
	 * @param instituteId 院id
	 * @return
	 */
	List<DcmOrganization> listDepartments(Long instituteId);
	/**
	 * 获取用户所在院下的部门列表
	 * @param loginName
	 * @return
	 */
	List<DcmOrganization> listCurrentDepartments(String loginName);
	/**
	 * 根据机构代码，获取机构下的人员
	 * @param orgCode
	 * @return
	 */
	List<DcmUser> getUsersByOrgCode(String orgCode);
	
	/**
	 * 获取院下的所有用户
	 * @param instituteUniqueName
	 * @return
	 */
	List<DcmUser> getUsersByInstituteName(String instituteUniqueName);
	/**
	 * 获取所有部门
	 * @return
	 */
	List<DcmOrganization> getAllDepartment();
	/**
	 * 根据院的唯一名称获取下面的所有部门信息，不包括院本身
	 * @param instituteUniqueName
	 * @return
	 */
	List<DcmOrganization> getDepartmentsOfInstitute(String instituteUniqueName);
	/**
	 * 根据院的唯一名称获取院以及院下的所有部门
	 * @param instituteUniqueName
	 * @return
	 */
	List<DcmOrganization> getDepartmentsAndInstitute(String instituteUniqueName);
	/**
	 * 获取用户头像链接
	 * @param contextPath
	 * @param baseURL
	 * @param user
	 * @return
	 */
	String getUserAvatarLink(String contextPath, String baseURL, DcmUser user);
	/**
	 * 获取用户id所在机构列表
	 * @param id
	 * @return
	 */
	List<DcmOrganization> getOrgsByUserId(Long id);
	/**
	 * 测试分布式缓存，添加
	 * @param key
	 * @param value
	 * @return
	 *//*
	Element addCache(String key, String value);
	*//**
	 * 测试分布式缓存，获取
	 * @param key
	 * @return
	 *//*
	Object getCache(String key);
	*//**
	 * 测试分布式缓存，删除
	 * @param key
	 *//*
	void deleteCache(String key);*/
	/**
	 * 根据机构域和登录名，获取用户在ldap中dn
	 * @param realm 机构域
	 * @param loginName 登录名
	 * @return
	 */
	BaseUserDTO getUserDNByRealmLoginname(String realm, String loginName) throws BusinessException;
	/**
	 * 修改用户密码
	 * @param userId 用户的序列id
	 * @param oldPwd
	 * @param newPwd
	 * @return
	 */
	boolean changeUserPwd(String userId, String oldPwd, String newPwd) throws BusinessException;
	/**
	 * 根据编码获取实体
	 * @param code
	 * @return
	 */
	DcmUser getUserEntityByCode(String code);
	/**
	 * 登出
	 * @param realm 域
	 * @param userId 用户id，不包括前缀域
	 * @param clientIP 客户端IP
	 */
	Boolean logout(String userCode, String clientIP);
	/**
	 * 删除用户
	 * @param dto
	 */
	boolean delUsers(UserDelRequestDTO dto);
	/**
	 * 删除机构
	 * @param realm
	 * @param orgId
	 * @return
	 */
	void deleteOrg(OrgDelRequestDTO dto);
	/**
	 * 根据编码获取简单用户信息
	 * @param code
	 * @return
	 */
	UserSimpleDTO getSimpleUserByCode(String code);
	/**
	 * 根据用户编码获取用户基本信息
	 * @param code
	 * @return
	 */
	Object getUserBasicInfoByCode(String code);
	/**
	 * 添加机构下的岗位
	 * @param orgId
	 * @param positionName
	 * @return
	 */
	Object addOrgPosition(Long orgId, String positionName);
	/**
	 * 添加机构下的团队
	 * @param orgId
	 * @param positionName
	 * @return
	 */
	Object addOrgTeam(Long orgId, String teamName);
	/**
	 * 删除机构下的职位
	 * @param id 职位id
	 */
	boolean deleteOrgPosition(Long id);
	/**
	 * 删除机构下的团队
	 * @param id
	 * @return
	 */
	boolean deleteOrgTeam(Long id);
	/**
	 * 修改个人基本信息
	 * @param userDto
	 * @return
	 */
	boolean updatePersonalBasicInfo(UserPersonUpdateRequestDTO userDto);
	/**
	 * 获取机构下的职位信息
	 * @param orgId
	 */
	List<DcmDicOrgExt> getOrgPositions(Long orgId);
	/**
	 * 获取机构下的团队信息
	 * @param orgId
	 * @return
	 */
	List<DcmDicOrgExt> getOrgTeams(Long orgId);
	/**
	 * 重命名机构名称
	 * @param orgCode
	 * @param newName
	 * @return
	 */
	boolean renameOrgName(String orgCode, String newName);
	/**
	 * 超管更新用户的基本信息，包括密码
	 * @param userDto
	 * @return
	 */
	Object updateUserBasicInfoByAdmin(UserUpdateRequestDTO userDto);
	/**
	 * 模糊检索用户信息
	 * @param keyword
	 * @return
	 */
	@Deprecated
	Object fuzzySearchUsers(String keyword);
	/**
	 * 分页模糊检索用户信息
	 * @param pageNo 页码，从1开始
	 * @param pageSize 每页大小
	 * @param keyword 检索关键字
	 * @return
	 */
	Object fuzzySearchUsersPage(int pageNo, int pageSize, String keyword);
	/**
	 * 获取简单用户信息，包括外部和内部
	 * @param contextPath
	 * @param baseURL
	 * @param realm
	 * @return
	 */
	// List<UserSimpleDTO> listSimpleInnerAndExternalUsersEx(String contextPath, String baseURL, String realm);
	/**
	 * 模糊查询域下的用户信息
	 * @param realm
	 * @param pageNo
	 * @param pageSize
	 * @param keyword
	 * @return
	 */
	Object fuzzySearchUsersPage(String realm, int pageNo, int pageSize, String keyword);
	/**
	 * 添加用户接口扩展
	 * @param contextPath
	 * @param baseURL
	 * @param userDto
	 * @return
	 */
	Object addUserEx(String contextPath, String baseURL, UserAddRequestDTO userDto);
	/**
	 * 超管更新用户信息
	 * @param userDto
	 * @return
	 */
	Object updateUserBasicInfoByAdminEx(UserUpdateRequestDTO userDto);
	/**
	 * 根据用户编码获取用户
	 * @param contextPath
	 * @param baseURL
	 * @param userCode
	 * @param ipAddr
	 * @return
	 */
	UserDTO getUserByCode(String contextPath, String baseURL, String userCode, String ipAddr);
	/**
	 * 根据用户编码删除存储在mongodb中的头像
	 * @param userId
	 */
	void deleteAvatarInMongoByUserCode(String userId);
	/**
	 * 存储头像文件到mongo，并返回文件_id
	 * @param imgInfo
	 * @return
	 */
	String storeAvatarToMongo(ImgInfo imgInfo);
	/**
	 * 根据mongodb中主键_id获取头像文件信息
	 * @param objectId
	 * @return
	 */
	FileContentLocalDTO getAvatarFromMongo(String objectId);
	/**
	 * 获取用户头像链接
	 * @param serverURI
	 * @param contextPath
	 * @param baseURL
	 * @param u
	 * @return
	 */
	String getUserAvatarLink(String imgServerURI, String contextPath, String baseURL, DcmUser user);
	/**
	 * 根据唯一名称获取机构
	 * @param name
	 * @return
	 */
	DcmOrganization getInstituteByUniqueName(String name);
	/**
	 * 
	 * @param dto
	 * @return
	 */
	Object addOrgPosition(OrgPositionAddDTO dto);
	/**
	 * 根据机构编码删除岗位实体
	 * @param orgCode
	 * @return
	 */
	boolean deleteOrgPosition(String orgCode);
	/**
	 * 添加机构团队
	 * @param dto
	 * @return
	 */
	Object addOrgTeam(OrgTeamAddDTO dto);
	/**
	 * 根据机构编码获取岗位信息
	 * @param orgCode
	 * @return
	 */
	List<DcmDicOrgExt> getOrgPositions(String orgCode);
	/**
	 * 获取机构下的团队
	 * @param orgCode
	 * @return
	 */
	List<DcmDicOrgExt> getOrgTeams(String orgCode);
	/**
	 * 同步指定域的用户和机构信息
	 * @param realm
	 */
	void syncFromLdapForCloudEx(String realm);
	/**
	 * 添加机构类型
	 * @param dto
	 * @return
	 */
	Object addOrgType(OrgTypeAddDTO dto);
	/**
	 * 获取机构类型
	 * @param orgCode
	 * @return
	 */
	List<DcmDicOrgExt> getOrgTypes(String orgCode);
	/**
	 * 更新机构基本信息
	 * @param dto
	 * @return
	 */
	Boolean updateOrgBasicInfo(OrgBasicInfoUpdateDTO dto);
	/**
	 * 根据域获取机构列表
	 * @param realm
	 * @return
	 */
	List<DcmOrganization> getOrgsByRealm(String realm);
	/**
	 * 根据域获取机构列表
	 * @param realm
	 * @param orgType，如中方、外方
	 * @return
	 */
	Pagination getOrgsByRealm(String realm, String orgType);
	/**
	 * 删除机构类型字典
	 * @param id
	 * @return
	 */
	boolean deleteOrgType(Long id);
	/**
	 * 用户是否存在指定机构下
	 * @param userId
	 * @param orgCode
	 * @return
	 */
	Boolean existOrg2User(Long userId, String orgCode);
}
