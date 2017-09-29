package com.dist.bdf.facade.service.uic.domain.impl;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dist.bdf.base.dao.GenericDAO;
import com.dist.bdf.base.domain.GenericDmnImpl;
import com.dist.bdf.base.exception.BusinessException;
import com.dist.bdf.base.page.PageParams;
import com.dist.bdf.base.page.Pagination;
import com.dist.bdf.base.utils.PinyinToolkit;
import com.dist.bdf.base.utils.StringUtil;
import com.dist.bdf.facade.service.uic.dao.DcmAttachmentDAO;
import com.dist.bdf.facade.service.uic.dao.DcmUserArticleInfoDAO;
import com.dist.bdf.facade.service.uic.dao.DcmUserCertificateInfoDAO;
import com.dist.bdf.facade.service.uic.dao.DcmUserDAO;
import com.dist.bdf.facade.service.uic.dao.DcmUserEducationDAO;
import com.dist.bdf.facade.service.uic.dao.DcmUserLanguageDAO;
import com.dist.bdf.facade.service.uic.dao.DcmUserOtherInfoDAO;
import com.dist.bdf.facade.service.uic.dao.DcmUserPrjExperienceDAO;
import com.dist.bdf.facade.service.uic.dao.DcmUserTitleInfoDAO;
import com.dist.bdf.facade.service.uic.dao.DcmUserTrainingDAO;
import com.dist.bdf.facade.service.uic.dao.DcmUserWorkExperienceDAO;
import com.dist.bdf.facade.service.uic.domain.DcmUserDmn;
import com.dist.bdf.common.constants.GlobalSystemParameters;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;


/**
 * 
 * @author weifj
 * @create 2015-06-06
 */
@Service
@Transactional(propagation = Propagation.REQUIRED) 
public class DcmUserDmnImpl extends GenericDmnImpl<DcmUser, Long>implements DcmUserDmn {

	@Autowired
	private DcmUserDAO userDao;
	//private GenericDAOImpl<DcmUser, Long> userDao;
	@Autowired
	private DcmUserEducationDAO userEduDAO;
	//private GenericDAOImpl<DcmUserEducation, Long> userEduDAO;
	@Autowired
	private DcmUserWorkExperienceDAO userWorkExpDAO;
	//private GenericDAOImpl<DcmUserWorkExperience, Long> userWorkExpDAO;
	@Autowired
	private DcmUserPrjExperienceDAO userPrjExpDAO;
	//private GenericDAOImpl<DcmUserPrjExperience, Long> userPrjExpDAO;
	@Autowired
	private DcmUserCertificateInfoDAO userCertDAO;
	//private GenericDAOImpl<DcmUserCertificateInfo, Long> userCertDAO;
	@Autowired
	private DcmUserTitleInfoDAO userTitleInfoDAO;
	//private GenericDAOImpl<DcmUserTitleInfo, Long> userTitleInfoDAO;
	@Autowired
	private DcmUserTrainingDAO userTrainingDAO;
	//private GenericDAOImpl<DcmUserTraining, Long> userTrainingDAO;
	@Autowired
	private DcmUserLanguageDAO userLanguageDAO;
	//private GenericDAOImpl<DcmUserLanguage, Long> userLanguageDAO;
	@Autowired
	private DcmUserArticleInfoDAO userArticleDAO;
	//private GenericDAOImpl<DcmUserArticleInfo, Long> userArticleDAO;
	@Autowired
	private DcmUserOtherInfoDAO userOtherInfoDAO;
	//private GenericDAOImpl<DcmUserOtherInfo, Long> userOtherInfoDAO;
	@Autowired
	private DcmAttachmentDAO attachmentDAO;
	//private GenericDAOImpl<DcmAttachment, Long> attachmentDAO;
	
	@Override
	public GenericDAO<DcmUser, Long> getDao() {
		return userDao ;
	}

	@Override
	public DcmUser addUser(DcmUser user) {
	
		user.setCurrentStatus(Long.valueOf(1));
		user.setIsBuildin(0L);

		return super.add(user);
	}

	@Override
	public DcmUser findByLoginName(String loginName) {
		
		DcmUser user = this.getDao().findUniqueByProperties(new String[] { "loginName", "currentStatus" },
				new Object[] { loginName, 1L });
		if (user == null)
			throw new BusinessException("不存在登录名为[{0}]的用户", loginName);

		return user;
	}
	@Override
	public DcmUser findByLoginNameSimply(String loginName){
		
		DcmUser user = this.getDao().findUniqueByProperties(new String[] { "loginName", "currentStatus" },
				new Object[] { loginName, 1L });
		if (user == null)
			System.err.println(String.format("不存在登录名为[%s]的用户", loginName));

		return user;
		
	}
	
	@Override
	public DcmUser findByDN(String dn) {
		
		return this.userDao.findUniqueByPropertyIgnoreCase("dn", dn);
	}
	
	@Override
	public DcmUser findByCode(String code) {
		
		return this.userDao.findUniqueByPropertyIgnoreCase("userCode", code);
	}

	@Override
	public DcmUser loadById(Long id) {

		return super.findById(id);
	}


	@Override
	public DcmUser findByName(String userName) {
		// TODO 当用户名有重复时，此方法存在问题（只返回了数据库中最前面的一个用户）
		return this.getDao().findUniqueByProperties(new String[] { "userName", "currentStatus" }, new Object[] { userName, 1 });
	}

	@Override
	public List<DcmUser> list() {
		// TODO Auto-generated method stub
		return this.getDao().findByProperty("currentStatus", 1L);
	}

	@Override
	public List<DcmUser> page(PageParams pageParam) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 删除用户列表，并不是从物理上删除，只是设置用户的状态信息
	 */
	@Override
	public void removeLogicByIds(Long... ids) {

		DcmUser[] users = new DcmUser[ids.length];
		for (int i = 0; i < ids.length; i++) {
			DcmUser user = this.userDao.find(ids[i]);
			if (user.getIsBuildin().equals(1)) {
				// 如果是系统用户，则不做删除
				continue;
			}
			user.setCurrentStatus(Long.valueOf(0));
			users[i] = user;
		}

		this.userDao.updateEntities(users);
	}

	@Override
	public boolean IsExistUser(DcmUser user) throws Exception {
		Assert.notNull(user);

		DcmUser userQuery = null;
		if (user.getLoginName()!=null){
		    userQuery = this.userDao.findUniqueByProperties(new String[]{"loginName","currentStatus"}, new Object[]{user.getLoginName(),1});

			if (userQuery!=null){
				throw new Exception(String.format("登录名%s已存在", user.getLoginName()));
			}
		}
		if (user.getUserName()!=null){
			userQuery = this.userDao.findUniqueByProperties(new String[]{"userName","currentStatus"}, new Object[]{user.getUserName(),1});
			if (userQuery!=null){
				throw new Exception(String.format("用户名%s已存在", user.getUserName()));
			}
		}
		return false;
	}

	@Override
	public boolean IsExistUser(String loginName) {
		Assert.notNull(loginName);

		 DcmUser userQuery = this.userDao.findUniqueByProperties(new String[]{"loginName","currentStatus"}, new Object[]{loginName,1});

		 if (userQuery!=null){
				return true;
		}
		return false;
	}

	@Override
	public boolean IsExistUserElse(DcmUser user) throws Exception {

		Assert.notNull(user);
		// 先判断用户是否已存在，登录名唯一
		DcmUser userQuery = (DcmUser) this.userDao
				.queryUnique(String.format("from %s where id<>%s and loginName='%s' and currentStatus=1",
						user.getClass().getSimpleName(), user.getId(), user.getLoginName()), new Object[] {});
		if (userQuery != null) {
			throw new Exception(String.format("登录名 [%s] 已存在", user.getLoginName()));
		}
		return false;
	}
	@Override
	public String getUserAvatarLink(String contextPath, String baseURL, DcmUser user) {
		
		String realLoginName = "";
		String[] temp = user.getLoginName().split("_");
		if(2 == temp.length){// 说明命名规范存在前缀
			realLoginName = temp[1];
		}else {
			realLoginName = user.getLoginName();
		}
		String link = "";
		if (StringUtil.isNullOrEmpty(user.getAvatar())) {
			link = baseURL + GlobalSystemParameters.DEFAULT_AVATAR + File.separator + StringUtil.getPYIndexStr(realLoginName, false).substring(0, 1).toLowerCase() +(StringUtil.isNullOrEmpty(user.getSex())? "0":(user.getSex().equals("男")? "m":"f"))+ ".jpg";
		} else if (user.getAvatar().contains("/fs/") || (new File(contextPath + user.getAvatar()).isFile() && new File(contextPath + user.getAvatar()).exists())) {
			link = baseURL + user.getAvatar();
		} else {
			link = baseURL + GlobalSystemParameters.DEFAULT_AVATAR + File.separator + StringUtil.getPYIndexStr(realLoginName, false).substring(0, 1).toLowerCase() +(StringUtil.isNullOrEmpty(user.getSex())? "0":(user.getSex().equals("男")? "m":"f"))+ ".jpg";
		}
		
		return link;
	}
	
	@Override
	public String getUserAvatarLink(String imgServerURI, String contextPath, String baseURL, DcmUser user) {

		String realLoginName = "";
		String[] temp = user.getLoginName().split("_");
		if (2 == temp.length) {// 说明命名规范存在前缀
			realLoginName = temp[1];
		} else {
			realLoginName = user.getLoginName();
		}
		String link = "";
		if (StringUtil.isNullOrEmpty(user.getAvatar())) {
			link = baseURL + GlobalSystemParameters.DEFAULT_AVATAR + File.separator
					+ StringUtil.getPYIndexStr(realLoginName, false).substring(0, 1).toLowerCase()
					+ (StringUtil.isNullOrEmpty(user.getSex()) ? "0" : (user.getSex().equals("男") ? "m" : "f"))
					+ ".jpg";
		} else if (user.getAvatar().contains("/fs/")) {
			if (StringUtils.isEmpty(imgServerURI)) {
				link = baseURL + user.getAvatar();
			} else if (!StringUtils.isEmpty(baseURL)) {
				link = imgServerURI.endsWith("/") ? imgServerURI + user.getAvatar() : imgServerURI + "/" + user.getAvatar();
			}
		} else if ((new File(contextPath + user.getAvatar()).isFile()
				&& new File(contextPath + user.getAvatar()).exists())) {
			link = baseURL + user.getAvatar();
		} else {
			link = baseURL + GlobalSystemParameters.DEFAULT_AVATAR + File.separator
					+ StringUtil.getPYIndexStr(realLoginName, false).substring(0, 1).toLowerCase()
					+ (StringUtil.isNullOrEmpty(user.getSex()) ? "0" : (user.getSex().equals("男") ? "m" : "f"))
					+ ".jpg";
		}
		return link;
	}

	@Override
	public String getUserAvatarLink(String contextPath, String baseURL, DcmUser user, boolean reset) {

		String realLoginName = "";
		String[] temp = user.getLoginName().split("_");
		if(2 == temp.length){// 说明命名规范存在前缀
			
			realLoginName = temp[1];
		}else {
			realLoginName = user.getLoginName();
		}
		
		if (reset) {

			return  baseURL + GlobalSystemParameters.DEFAULT_AVATAR + File.separator
					+ StringUtil.getPYIndexStr(realLoginName, false).substring(0, 1).toLowerCase()
					+ (StringUtil.isNullOrEmpty(user.getSex()) ? "0" : (user.getSex().equals("男") ? "m" : "f"))
					+ ".jpg";
		} else {

			return this.getUserAvatarLink(contextPath, baseURL, user);
		}

	}
	@Override
	public String getUserLastName(String userName) {
		
		// 判断第一个字符是否中文
		String firstChar = userName.substring(0, 1);
		if(StringUtil.isChineseChar(firstChar)){
			return PinyinToolkit.cn2Spell(firstChar);
			
		}else{
			return firstChar;
		}
	}

	@Override
	public List<DcmUserArticleInfo> getArticleInfos(Long refUserId) {

		return this.userArticleDAO.findByProperty("userId", refUserId, "publishTime", false);
	}

	@Override
	public List<DcmUserCertificateInfo> getCertificateInfos(Long refUserId) {
		
		return this.userCertDAO.findByProperty("userId", refUserId, "getTime", false);
	}

	@Override
	public List<DcmUserEducation> getEducations(Long refUserId) {
		
		return this.userEduDAO.findByProperty("userId", refUserId, "timeEnd", false);
	}

	@Override
	public List<DcmUserWorkExperience> getWorkExperiences(Long refUserId) {
		
		return this.userWorkExpDAO.findByProperty("userId", refUserId, "timeEnd", false);
	}

	@Override
	public List<DcmUserPrjExperience> getPrjExperiences(Long refWorkExperienceId) {
		
		return this.userPrjExpDAO.findByProperty("refWorkExperienceId", refWorkExperienceId, "timeStart", false);
	}
	@Override
	public List<DcmUserLanguage> getLanguages(Long refUserId) {
	
		return this.userLanguageDAO.findByProperty("userId", refUserId);
	}

	@Override
	public List<DcmUserTitleInfo> getTitleInfos(Long refUserId) {
		
		return this.userTitleInfoDAO.findByProperty("userId", refUserId, "getTime", false);
	}

	@Override
	public List<DcmUserTraining> getTrainings(Long refUserId) {
	
		return this.userTrainingDAO.findByProperty("userId", refUserId, "trainTime", false);
	}

	@Override
	public List<DcmUserOtherInfo> getOtherAttachmentInfo(Long refUserId) {
		
		return this.userOtherInfoDAO.findByProperty("userId", refUserId);
	}

	@Override
	public List<DcmAttachment> getAttachmentInfoByRefId(Long refId) {
		
		return this.attachmentDAO.findByProperty("referenceId", refId);
	}
	
	@Override
	public List<DcmUser> getAllValidUsers() {
		
		return this.userDao.findByProperty("currentStatus", 1L);
	}
	
	@Override
	public List<DcmUser> getValidUsers(Long...ids) {
		
		if(null == ids || 0 == ids.length) return null;
		
		/*Collection<Serializable> userIds = Arrays.asList(ids);
		List<DcmUser> users = this.disCacheService.getUsers(userIds);
		if(null == users || users.isEmpty()) {*/
			Map<String, Object[]> propertiesValuesMap = new HashMap<String, Object[]>();
			propertiesValuesMap.put("id", ids);
			propertiesValuesMap.put("currentStatus", new Object[]{1L});
			List<DcmUser> users = this.userDao.findByProperties(propertiesValuesMap);//.find(ids);
			//List<DcmUser> validUsers = new ArrayList<DcmUser>();
			/*if(users != null && !users.isEmpty()) {
				for(DcmUser u : users){
					if(u.getCurrentStatus().equals(0L)) continue;
					
					validUsers.add(u);
				}
			}*/
			return users;
		//}
		/*logger.debug("从缓存中获取到用户信息， 用户序列id集合：[{}]......", userIds);
		return users;*/
		
	}
	
	
	@Override
	public List<DcmUser> findByLoginnameStartMatch(String prefix) {
		
		return this.userDao.findByPropertiesStartMatch(new String[]{"loginName"}, new Object[]{prefix});
	}
	
	@Override
	public List<DcmUser> findByRealm(String realm) {
		
		return this.userDao.findByProperties(new String[]{"currentStatus","realm"}, new Object[]{1L, realm});
	}
	@Override
	public List<DcmUser> findByNameMatch(String keyword) {
		
		return this.userDao.findByPropertiesMatch(new String[]{"loginName","userName"}, new Object[]{keyword, keyword});
	}
	@Override
	public Pagination findByNameMatch(int pageNo, int pageSize, String keyword) {
		
		return this.userDao.findByPropertiesMatch(pageNo, pageSize, new String[]{"loginName","userName"}, new Object[]{keyword, keyword});
	}
	@Override
	public Pagination findByNameMatch(String realm, int pageNo, int pageSize, String keyword) {
		
		Map<String, Object[]> equalProperties = new HashMap<String, Object[]>();
		equalProperties.put("realm", new String[]{realm});
		equalProperties.put("currentStatus", new Long[]{1L});
		
		Map<String, Object[]> likeProperties = new HashMap<String, Object[]>();
		//likeProperties.put("loginName", new String[]{keyword});
		likeProperties.put("userName", new String[]{keyword});
		
		return this.userDao.findByProperties(pageNo, pageSize, equalProperties, likeProperties,"userName",true);
	}
	@Override
	public boolean setTeamNull(String name) {
		
		this.userDao.update(String.format("update %s set teamName = '无' where teamName = '%s'", DcmUser.class.getSimpleName(), name));
		return true;
	}
}
