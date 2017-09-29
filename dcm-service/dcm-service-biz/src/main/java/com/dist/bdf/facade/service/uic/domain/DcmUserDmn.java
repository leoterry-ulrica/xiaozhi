
package com.dist.bdf.facade.service.uic.domain;

import java.util.List;

import com.dist.bdf.base.domain.GenericDmn;
import com.dist.bdf.base.page.PageParams;
import com.dist.bdf.base.page.Pagination;
import com.dist.bdf.model.entity.system.DcmAttachment;
import com.dist.bdf.model.entity.system.DcmUser;
import com.dist.bdf.model.entity.system.DcmUserArticleInfo;
import com.dist.bdf.model.entity.system.DcmUserCertificateInfo;
import com.dist.bdf.model.entity.system.DcmUserEducation;
import com.dist.bdf.model.entity.system.DcmUserWorkExperience;
import com.dist.bdf.model.entity.system.DcmUserLanguage;
import com.dist.bdf.model.entity.system.DcmUserOtherInfo;
import com.dist.bdf.model.entity.system.DcmUserPrjExperience;
import com.dist.bdf.model.entity.system.DcmUserTitleInfo;
import com.dist.bdf.model.entity.system.DcmUserTraining;

/**
 *  
 * @author weifj
 * @version 1.0，2015/06/07，创建
 */
public interface DcmUserDmn extends GenericDmn<DcmUser, Long> {

	/**
	 * 添加用户
	 * @param userName
	 * @param loginName
	 * @param userCode
	 * @param userPwd
	 * @param sex
	 * @param domainType
	 * @return
	 */
	DcmUser addUser(DcmUser user);
	/**
	 * 根据登录名查找用户信息，如果不存在，则抛出异常
	 * @param loginName
	 * @return
	 */
	DcmUser findByLoginName(String loginName);
	/**
	 * 根据登录名查找用户信息，并不抛出异常
	 * @param loginName
	 * @return
	 */
	DcmUser findByLoginNameSimply(String loginName);
	/**
	 * 根据dn查找用户
	 * @param dn
	 * @return
	 */
	DcmUser findByDN(String dn);

	/**
	 * 根据用户名查找用户信息
	 * @param userName
	 * @return
	 */
	DcmUser findByName(String userName);

	/**
	 * 查找所有用户信息
	 * @return
	 */
	List<DcmUser> list();

	/**
	 * 分页查找用户
	 * @param pageParam
	 * @return
	 */
	List<DcmUser> page(PageParams pageParam);

	/**
	 * 判断用户是否已存在
	 * @param user
	 * @return
	 * @throws Exception
	 */
	boolean IsExistUser(DcmUser user) throws Exception;
	/**
	 * 根据登录名判断是否存在用户信息
	 * @param loginName
	 * @return
	 * @throws Exception
	 */
	boolean IsExistUser(String loginName);
	/**
	 *  除了本身，判断用户是否已存在
	 * @param user
	 * @return
	 * @throws Exception
	 */
	boolean IsExistUserElse(DcmUser user) throws Exception;

	void removeLogicByIds(Long... ids);
	/**
	 * 获取用户的头像链接
	 * @param contextPath 上下文物理路径
	 * @param baseURL http请求地址
	 * @param user 用户信息
	 * @return
	 */
	String getUserAvatarLink(String contextPath, String baseURL, DcmUser user);
	/**
	 * 获取用户的头像链接
	 * @param contextPath
	 * @param baseURL
	 * @param user
	 * @param reset 标识是否重设，true说明不管原来头像是什么，都会重新设置
	 * @return
	 */
	String getUserAvatarLink(String contextPath, String baseURL, DcmUser user, boolean reset);
	/**
	 * 获取用户姓氏
	 * @param userName 用户名
	 * @return
	 */
	String getUserLastName(String userName);
	/**
	 * 获取用户的学术成果
	 * @param refUserId 外键用户id
	 * @return
	 */
	List<DcmUserArticleInfo> getArticleInfos(Long refUserId);
	/**
	 * 获取用户的执业资格
	 * @param refUserId 外键用户id
	 * @return
	 */
	List<DcmUserCertificateInfo> getCertificateInfos(Long refUserId);
	/**
	 * 获取用户的教育经历
	 * @param refUserId 外键用户id
	 * @return
	 */
	List<DcmUserEducation> getEducations(Long refUserId);
	/**
	 * 获取用户的工作经历
	 * @param refUserId 外键用户id
	 * @return
	 */
	List<DcmUserWorkExperience> getWorkExperiences(Long refUserId);
	/**
	 * 获取项目经历
	 * @param refWorkExperienceId
	 * @return
	 */
	List<DcmUserPrjExperience> getPrjExperiences(Long refWorkExperienceId);
	/**
	 * 获取用户的语言水平
	 * @param refUserId 外键用户id
	 * @return
	 */
	List<DcmUserLanguage> getLanguages(Long refUserId);
	
	/**
	 * 获取用户的职称信息
	 * @param refUserId 外键用户id
	 * @return
	 */
	List<DcmUserTitleInfo> getTitleInfos(Long refUserId);
	/**
	 * 获取用户的培训经历
	 * @param refUserId 外键用户id
	 * @return
	 */
	List<DcmUserTraining> getTrainings(Long refUserId);
	/**
	 * 获取用户的其它附件信息
	 * @param refUserId
	 * @return
	 */
	List<DcmUserOtherInfo> getOtherAttachmentInfo(Long refUserId);
	/**
	 * 获取附件通用方法
	 * @param refId
	 * @return
	 */
	List<DcmAttachment> getAttachmentInfoByRefId(Long refId);
	
	/**
	 * 获取所有有效的用户信息
	 * @return
	 */
	List<DcmUser> getAllValidUsers();
	/**
	 * 根据id获取有效用户
	 * @param ids
	 * @return
	 */
	List<DcmUser> getValidUsers(Long...ids);
	/**
	 * 根据编码获取用户实体
	 * @param code
	 * @return
	 */
	DcmUser findByCode(String code);
	/**
	 * 根据登录名的前缀进行查询
	 * @param prefix
	 * @return
	 */
	List<DcmUser> findByLoginnameStartMatch(String prefix);
	/**
	 * 根据域获取用户
	 * @param realm
	 * @return
	 */
	List<DcmUser> findByRealm(String realm);
	/**
	 * 根据名称，任意匹配关键字
	 * @param keyword
	 */
	List<DcmUser> findByNameMatch(String keyword);
	/**
	 * 根据名称，任意匹配关键字，分页返回数据
	 * @param pageNo 页码，从1开始
	 * @param pageSize 每页大小
	 * @param keyword 关键字
	 * @return
	 */
	Pagination findByNameMatch(int pageNo, int pageSize, String keyword);
	
	Pagination findByNameMatch(String realm, int pageNo, int pageSize, String keyword);
	/**
	 * 团队被删除后，需要把个人的团队信息设置为“无”
	 * @param name
	 * @return
	 */
	boolean setTeamNull(String name);
	/**
	 * 获取头像链接
	 * @param imgServerURI 图片服务器uri
	 * @param contextPath
	 * @param baseURL
	 * @param user
	 * @return
	 */
	String getUserAvatarLink(String imgServerURI, String contextPath, String baseURL, DcmUser user);
}
