package com.dist.bdf.facade.service.sga.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.dist.bdf.base.exception.BusinessException;
import com.dist.bdf.base.security.AesCbcUtil;
import com.dist.bdf.base.security.DesEncryptAES;
import com.dist.bdf.base.security.DesEncryptRSA;
import com.dist.bdf.base.utils.DistThreadManager;
import com.dist.bdf.base.utils.HttpRequestHelper;
import com.dist.bdf.base.utils.URLEncode;
import com.dist.bdf.base.utils.UUIDGenerator;
import com.dist.bdf.base.utils.mail.MailUtil;
import com.dist.bdf.facade.service.sga.SgaCompanyService;
import com.dist.bdf.facade.service.sga.SgaUserService;
import com.dist.bdf.facade.service.sga.dao.SgaUserAttachmentDAO;
import com.dist.bdf.facade.service.sga.domain.SgaCompanyDmn;
import com.dist.bdf.facade.service.sga.domain.SgaInviteQueueDmn;
import com.dist.bdf.facade.service.sga.domain.SgaProjectDmn;
import com.dist.bdf.facade.service.sga.domain.SgaUserDmn;
import com.dist.bdf.manager.cache.CacheStrategy;
import com.dist.bdf.manager.wechat.WechatManager;
import com.dist.bdf.common.conf.common.GlobalConf;
import com.dist.bdf.common.conf.common.MailConf;
import com.dist.bdf.common.conf.wechat.WechatConf;
import com.dist.bdf.common.constants.AttachmentTypeConstants;
import com.dist.bdf.common.constants.CacheKey;
import com.dist.bdf.common.constants.RegisterType;
import com.dist.bdf.model.dto.sga.UserRegisterSimpleDTO;
import com.dist.bdf.model.dto.sga.UserResponseDTO;
import com.dist.bdf.model.dto.system.user.UserSimpleDTO;
import com.dist.bdf.model.dto.wechat.AccessToken;
import com.dist.bdf.model.dto.wechat.UserInfo;
import com.dist.bdf.model.dto.wechat.WxappEncryptedUserInfo;
import com.dist.bdf.model.dto.sga.ConfirmlinkDTO;
import com.dist.bdf.model.dto.sga.SignupDTO;
import com.dist.bdf.model.dto.sga.UserAttachRepDTO;
import com.dist.bdf.model.dto.sga.UserAttachmentDTO;
import com.dist.bdf.model.dto.sga.UserBasicInfoRequestDTO;
import com.dist.bdf.model.dto.sga.UserEmailValidDTO;
import com.dist.bdf.model.entity.sga.SgaCompany;
import com.dist.bdf.model.entity.sga.SgaComUser;
import com.dist.bdf.model.entity.sga.SgaInviteQueue;
import com.dist.bdf.model.entity.sga.SgaPrjUser;
import com.dist.bdf.model.entity.sga.SgaProject;
import com.dist.bdf.model.entity.sga.SgaUser;
import com.dist.bdf.model.entity.sga.SgaUserAttachment;

/**
 * 
 * @author weifj
 *
 */
@Service("SgaUserService")
@Transactional(propagation = Propagation.REQUIRED)
public class SgaUserServiceImpl implements SgaUserService {

	private static Logger logger = LoggerFactory.getLogger(SgaUserServiceImpl.class);
	/**
	 * 缓存用户前缀
	 */
	// private static String cache_user_prefix = "sga_user_";

	@Autowired
	private SgaUserDmn sgaUserDmn;
	@Autowired
	private SgaInviteQueueDmn sgaInviteQueueDmn;
	@Autowired
	private SgaProjectDmn sgaProjectDmn;
	@Autowired
	private SgaCompanyDmn sgaComInfoDmn;
	@Autowired
	private GlobalConf globalConf;
	@Autowired
	private MailConf mailConf;
	/*
	 * @Autowired private DistributedCacheService disCacheService;
	 */
	@Autowired
	private WechatConf sgaConf;
	@Autowired
	private CacheStrategy redisCache;
	@Autowired
	private Mapper dozerMapper;
	@Autowired
	private SgaCompanyService sgaComService;

	/**
	 * 缓存用户
	 * 
	 * @param u
	 */
	/*
	 * private void cacheUser(SgaUser u) {
	 * 
	 * this.disCacheService.cacheShare(cache_user_prefix + u.getId(), u);
	 * this.disCacheService.cacheShare(cache_user_prefix + u.getEmail(), u);
	 * 
	 * }
	 */
	/**
	 * 删除用户缓存
	 * 
	 * @param u
	 */
	/*
	 * private void deleteUserFromCache(SgaUser u) {
	 * 
	 * this.disCacheService.removeFromShare(cache_user_prefix + u.getId());
	 * this.disCacheService.removeFromShare(cache_user_prefix + u.getEmail());
	 * 
	 * }
	 */
	@PostConstruct
	private void refreshUsers() {

		if (globalConf.openCache()) {
			DistThreadManager.MyCacheThreadPool.execute(new Runnable() {

				@Override
				public void run() {
					List<SgaUser> users = SgaUserServiceImpl.this.sgaUserDmn.find();
					if (users != null && !users.isEmpty()) {
						for (SgaUser u : users) {
							redisCache.set(CacheKey.PREFIX_SGA_USER + u.getSysCode(), u);
							// disCacheService.cacheUser(u);
						}
					}
				}
			});
		}
	}

	/**
	 * 进行一系列的逻辑，最终获取邮件的正文内容
	 * 
	 * @param serviceURI
	 * @param dto
	 * @return
	 */
	private String getMailContent(String serviceURI, SignupDTO dto) throws BusinessException {

		SgaProject project = null;
		// 注册用户信息
		SgaUser user = new SgaUser();
		user.setEmail(dto.getEmail());
		user.setStatus(0);
		user.setCreateTime(new Date());
		user.setSysCode(UUIDGenerator.getUUID(false, true, false));
		user.setRegisterType(dto.getRegisterType());
		this.sgaUserDmn.add(user);
		// 企业与用户
		SgaComUser comuser = new SgaComUser();
		comuser.setCid(dto.getComId());
		comuser.setStatus(0);
		comuser.setUserId(user.getId());
		this.sgaComInfoDmn.addComUser(comuser);
		if (1 == dto.getRegisterType() || 2 == dto.getRegisterType()) {
			// 获取项目信息
			project = this.sgaProjectDmn.findUniqueByProperty("sysCode", dto.getProjectId());
			if (null == project)
				throw new BusinessException("找不到项目 [{0}]数据", dto.getProjectId());
			// 项目与成员绑定
			SgaPrjUser prjuser = new SgaPrjUser();
			prjuser.setCreateTime(new Date());
			prjuser.setPid(project.getId());
			prjuser.setStatus(0); // 待审核状态
			prjuser.setUserId(user.getId());
			this.sgaProjectDmn.add(project);
		}
		// 加入队列，为了检验后续链接的有效性
		SgaInviteQueue inviteQueue = new SgaInviteQueue();
		if (0 == dto.getRegisterType()) {
			inviteQueue.setSysCode(user.getSysCode());
		} else if (1 == dto.getRegisterType() || 2 == dto.getRegisterType()) {
			inviteQueue.setSysCode(project.getSysCode());
		} else if (3 == dto.getRegisterType()) {
			SgaCompany cominfo = this.sgaComInfoDmn.loadById(dto.getComId());
			if (cominfo != null)
				inviteQueue.setSysCode(cominfo.getSysCode());
		}

		inviteQueue.setCreateTime(new Date());
		inviteQueue.setExpires(new Date(inviteQueue.getCreateTime().getTime() + this.globalConf.getExpiresValue())); // 一分钟过期
		inviteQueue.setRegisterType(dto.getRegisterType());
		inviteQueue.setMark(0);
		sgaInviteQueueDmn.add(inviteQueue);

		return String.format("<html><body>小智平台账号注册<br/>" + "<a href=\"%s\">点击链接完成确认</a>" + "</body></html>",
				String.format("%s?uid=%s&queueId=%s&registerType=%s&comId=%s&projectId=%s&redirect=%s",
						URLEncode.encodeURI(serviceURI), user.getId(), inviteQueue.getId(), dto.getRegisterType(),
						dto.getComId(), dto.getProjectId(), "sga.userregister.redirect.uri"));// URLEncode.encodeURI(sgaConf.getUserRegisterRedirectURI())
	}

	@Override
	public Object sendMailConfirmation(String serviceURI, SignupDTO dto) throws BusinessException {

		if (!this.checkEmailValid(dto.getEmail()))
			throw new BusinessException("email [{0}]已注册", dto.getEmail());

		try {
			// 发邮件
			MailUtil.sendTextMessage(this.mailConf.getMailSmtpAuth(), this.mailConf.getMailDebug(),
					this.mailConf.getMailSmtpTimeout(), this.mailConf.getUserName(), this.mailConf.getPassword(),
					"小智平台用户账号确认", getMailContent(serviceURI, dto), dto.getEmail());

			logger.info(">>>邮件发送成功");

			return true;

		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
		}
		return false;
	}

	@Override
	public UserEmailValidDTO clickConfirmationLink(ConfirmlinkDTO linkdto) throws BusinessException {

		SgaInviteQueue iq = this.sgaInviteQueueDmn.loadById(linkdto.getQueueId());
		if (null == iq)
			throw new BusinessException("找不到请求队列 [{0}]数据", linkdto.getQueueId());

		if (iq.getExpires().before(new Date())) {
			iq.setMark(4);
			this.sgaInviteQueueDmn.modify(iq);
			throw new BusinessException("验证链接已过期");
		}
		// 用户状态
		SgaUser user = this.sgaUserDmn.loadById(linkdto.getUid());
		user.setStatus(1);
		this.sgaUserDmn.modify(user);

		UserEmailValidDTO dto = this.dozerMapper.map(user, UserEmailValidDTO.class);
		if (iq.getMark() != 0) {
			dto.setMark(iq.getMark());
			return dto;
		} else {
			// 如果是新创建的，设置状态为1，表示正常接受邀请，未完成注册（只是点击链接）
			iq.setMark(1);
			dto.setMark(iq.getMark());
			this.sgaInviteQueueDmn.modify(iq);
		}
		return dto;
	}

	@Override
	public Object registerFromValidLink(UserRegisterSimpleDTO info) throws BusinessException {

		if (!checkEmailValid(info.getEmail()))
			throw new BusinessException("email [{0}]已注册", info.getEmail());

		try {
			SgaUser user = this.sgaUserDmn.findById(info.getUid());
			user.setStatus(1);
			// aes和rsa相结合
			String aesEncrypt = DesEncryptAES.getInstance()
					.encrypt(((DesEncryptRSA) DesEncryptRSA.getInstance()).decryptReverse(info.getUserPwd()));
			user.setUserPwd(aesEncrypt);
			this.sgaUserDmn.modify(user);
			SgaInviteQueue iq = this.sgaInviteQueueDmn.loadById(info.getQueueId());
			iq.setMark(2);
			this.sgaInviteQueueDmn.modify(iq);
			SgaComUser cu = this.sgaComInfoDmn.getComUser(info.getComId(), info.getUid());
			cu.setStatus(1);
			this.sgaComInfoDmn.modify(cu);

			return this.dozerMapper.map(user, UserResponseDTO.class);// userModel2DTO(user);

			// return dto;

		} catch (Exception e) {

			logger.error(e.getMessage());
			throw new BusinessException("注册失败，详情：" + e.getMessage());
		}
	}

	@Override
	public Object inviteRegister(UserRegisterSimpleDTO info) throws BusinessException {

		if (!checkEmailValid(info.getEmail()))
			throw new BusinessException("email [{0}]已注册", info.getEmail());

		try {
			SgaUser userModel = this.dozerMapper.map(info, SgaUser.class);
			userModel.setSysCode(UUIDGenerator.getUUID(false, true, false));
			// userModel.setLoginName(info.getUserName());
			userModel.setStatus(0);
			userModel.setCreateTime(new Date());
			userModel.setRegisterType(1);

			this.sgaUserDmn.add(userModel);

			return userModel;

		} catch (Exception e) {

			logger.error(e.getMessage());
			throw new BusinessException("邀请注册失败，详情：" + e.getMessage());
		}
	}

	@Override
	public boolean checkEmailValid(String email) {

		return null == this.sgaUserDmn.getByEmail(email) ? true : false;

	}

	@Override
	public List<UserResponseDTO> listAllRegisterUsers() {

		List<SgaUser> users = this.sgaUserDmn.find();
		return userModel2DTO(users);

	}

	private List<UserResponseDTO> userModel2DTO(List<SgaUser> users) {
		if (users != null && !users.isEmpty()) {
			List<UserResponseDTO> dtos = new ArrayList<UserResponseDTO>();
			try {

				for (SgaUser u : users) {
					UserResponseDTO subDto = this.dozerMapper.map(u, UserResponseDTO.class);// userModel2DTO(u);
					dtos.add(subDto);
				}
				return dtos;
			} catch (Exception ex) {
				logger.error(ex.getMessage(), ex);
			}
		}
		return null;
	}

	@Deprecated
	private UserResponseDTO userModel2DTO(SgaUser u) throws IllegalAccessException, InvocationTargetException {

		Assert.notNull(u);

		UserResponseDTO subDto = new UserResponseDTO();
		subDto.setAvatar(u.getAvatar());
		subDto.setCreateTime(u.getCreateTime());
		subDto.setEmail(u.getEmail());
		subDto.setId(u.getId());
		// subDto.setLastTime(u.getLastTime());
		subDto.setLoginName(u.getLoginName());
		subDto.setPosition(u.getPosition());
		// subDto.setRegisterType(u.getRegisterType());
		subDto.setSex(u.getSex());
		subDto.setStatus(u.getStatus());
		subDto.setTelephone(u.getTelephone());
		subDto.setUnit(u.getUnit());
		subDto.setUserName(u.getUserName());
		subDto.setWechat(u.getWechat());
		subDto.setSysCode(u.getSysCode());
		// BeanUtils.copyProperties(subDto, u);
		return subDto;
	}

	@Override
	public List<UserResponseDTO> listValidUsers() {

		List<SgaUser> users = this.sgaUserDmn.findByProperty("status", 1, "email", true);

		return userModel2DTO(users);
	}

	@Override
	public List<UserResponseDTO> listValidUsersByRealm(String realm) {

		SgaCompany com = this.sgaComInfoDmn.getComByRealm(realm);
		if (null == com) {
			throw new BusinessException("没有找到对应的企业实体，realm[" + realm + "]");
		}
		List<SgaComUser> comUsers = this.sgaComInfoDmn.getComUserRef(com.getId());
		if (null == comUsers || 0 == comUsers.size()) {
			return null;
		}
		List<UserResponseDTO> users = new ArrayList<>(comUsers.size());
		UserResponseDTO tempUser = null;
		for (SgaComUser cu : comUsers) {
			tempUser = this.getUserById(cu.getUserId());
			if (null == tempUser) {
				continue;
			}
			users.add(tempUser);
		}
		return users;
	}

	@Override
	public List<UserSimpleDTO> listValidUsersByRealmPublic(final String realm) {

		@SuppressWarnings("unchecked")
		List<UserSimpleDTO> users = this.globalConf.openCache()? (List<UserSimpleDTO>) this.redisCache.getList(CacheKey.CACHEKEY_REALM_SGA_USER + realm):null;
		if(users != null && !users.isEmpty()) {
			logger.info(">>>获取到外部用户缓存数据");
			return users;
		}
		SgaCompany com = this.sgaComInfoDmn.getComByRealm(realm);
		if (null == com) {
			throw new BusinessException("没有找到对应的企业实体，realm[" + realm + "]");
		}
		List<SgaComUser> comUsers = this.sgaComInfoDmn.getComUserRef(com.getId());
		if (null == comUsers || 0 == comUsers.size()) {
			return null;
		}
		users = new ArrayList<>(comUsers.size());
		UserResponseDTO tempUser = null;
		UserSimpleDTO simpleUser = null;
		for (SgaComUser cu : comUsers) {
			tempUser = this.getUserById(cu.getUserId());
			if (null == tempUser) {
				continue;
			}
			simpleUser = new UserSimpleDTO();
			simpleUser.setId(tempUser.getId());
			simpleUser.setLoginName(tempUser.getLoginName());
			simpleUser.setUserName(tempUser.getUserName());
			simpleUser.setSex("m".equalsIgnoreCase(tempUser.getSex()) ? "男" : "女");
			simpleUser.setUserCode(tempUser.getSysCode());
			simpleUser.setAvatar(tempUser.getAvatar());
			users.add(simpleUser);
		}
		if(users != null && this.globalConf.openCache()){
			final List<UserSimpleDTO> finalUsers = users;
			DistThreadManager.MyCacheThreadPool.execute(new Runnable() {
				
				@Override
				public void run() {
					redisCache.setList(CacheKey.CACHEKEY_REALM_SGA_USER + realm, finalUsers);
				}
			});
		}
		return users;
	}
	
	@Override
	public Object matchUserByEmail(String email) {

		SgaUser user = this.sgaUserDmn.findUniqueByProperty("email", email);
		if (null == user)
			return null;

		try {
			return this.dozerMapper.map(user, UserResponseDTO.class);
			// return this.userModel2DTO(user);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Object matchUserByWechat(String wechat) {

		SgaUser user = this.sgaUserDmn.findUniqueByProperty("wechat", wechat);
		if (null == user)
			return null;

		try {
			return this.dozerMapper.map(user, UserResponseDTO.class);
			// return this.userModel2DTO(user);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	@Override
	public SgaUser getUserEntityById(Long id) {
		try {
			SgaUser user = this.globalConf.openCache() ? (SgaUser) this.redisCache.get(CacheKey.PREFIX_SGA_USER_ID + id)
					: null;
			
			if (null == user) {
				logger.info(">>>从db获取用户[{}]信息", id);
				user = this.sgaUserDmn.loadById(id);
				if (null == user)
					return null;

				if (this.globalConf.openCache()) {
					redisCache.set(CacheKey.PREFIX_SGA_USER_ID + user.getId(), user);
					redisCache.set(CacheKey.PREFIX_SGA_USER + user.getSysCode(), user);
				}
				if(user.getStatus() != 1) {
					logger.info(">>>用户已被删除或者挂起，状态[{}]", user.getStatus());
					return null;
				}
				return user;
			}
			logger.info(">>>从缓存获取用户[{}]信息", id);
			if(user.getStatus() != 1) {
				logger.info(">>>用户已被删除或者挂起，状态[{}]", user.getStatus());
				return null;
			}
			return user;
		} catch (Exception e) {
			logger.error(">>>获取用户失败，详情：" + e.getMessage(), e);
			return null;
		}
	}
	@Override
	public UserResponseDTO getUserById(Long id) {

		SgaUser user = this.getUserEntityById(id);
		if(user != null) {
			return this.dozerMapper.map(user, UserResponseDTO.class);
		}
		return null;
	}

	@Override
	public UserResponseDTO getUserByCode(String code) {

		try {
			SgaUser user = this.globalConf.openCache() ? (SgaUser) this.redisCache.get(CacheKey.PREFIX_SGA_USER + code)
					: null;

			if (null == user) {
				logger.info(">>>从db获取用户[{}]信息", code);
				user = this.sgaUserDmn.getByCode(code);
				if (null == user)
					return null;

				if (this.globalConf.openCache())
					redisCache.set(CacheKey.PREFIX_SGA_USER + user.getSysCode(), user);

				return this.dozerMapper.map(user, UserResponseDTO.class);
				// return this.userModel2DTO(user);
			}
			logger.info(">>>从缓存获取用户[{}]信息", code);
			return this.dozerMapper.map(user, UserResponseDTO.class);
			// return this.userModel2DTO(user);

		} catch (Exception e) {
			logger.error(">>>获取用户失败，详情：" + e.getMessage());
			return null;
		}
	}

	@Override
	public Object loginByEmail(String email, String pwd) throws BusinessException {

		SgaUser user = this.sgaUserDmn.getByEmail(email);

		if (null == user)
			throw new BusinessException("认证失败，详情：指定邮箱[{0}]不存在", email);

		try {
			String aes = DesEncryptAES.getInstance()
					.encrypt(((DesEncryptRSA) DesEncryptRSA.getInstance()).decryptReverse(pwd));
			if (!user.getUserPwd().equals(aes)) {
				throw new BusinessException("认证失败，详情：密码错误");
			}
			UserResponseDTO responseDTO = this.dozerMapper.map(user, UserResponseDTO.class);
			return responseDTO;

		} catch (Exception ex) {
			logger.error(ex.getMessage());
			throw new BusinessException("响应模型转换失败，详情：[{0}]", ex.getMessage());
		}
	}

	@Override
	public UserResponseDTO loginByWechatNoPwd(String code, String state) {

		AccessToken token = WechatManager.getAccessTokenByAuthorizationCode(this.sgaConf.getInvokeAccessTokenURI(),
				DesEncryptRSA.getInstance().decrypt(this.sgaConf.getWxAppIdEncrypt()),
				DesEncryptRSA.getInstance().decrypt(this.sgaConf.getWxAppSecretEncrypt()), code);// this.getWxAccessToken(code);
		UserInfo userInfo = WechatManager.getWxUserInfo(this.sgaConf.getInvokeUserInfoURI(), token.getAccess_token(),
				token.getOpenid());

		return checkUserInfoOfWechat(userInfo);
	}

	@Override
	public UserResponseDTO checkUserInfoOfWechat(UserInfo userInfo) {

		UserResponseDTO dto = new UserResponseDTO();
		SgaUser user = this.sgaUserDmn.findUniqueByProperties(new String[] { "wechat", "status" },
				new Object[] { userInfo.getUnionid(), 1 });
		String nickName = userInfo.getNickName();
		if (null == user) {
			user = new SgaUser();
			user.setLoginName(nickName);
			user.setCreateTime(new Date());
			user.setSysCode(UUIDGenerator.getUUID(true, true, false));
			user.setStatus(1);
			user.setRegisterType(RegisterType.WECHAT);// 微信注册
			user.setSex(1 == userInfo.getSex() ? "m" : "f");
			dto.setIsNew(true);
			user.setLastTime(user.getCreateTime());
		} else {
			user.setLastTime(new Date());
			dto.setIsNew(false);
		}
		try {
			// 微信相对固定信息
			// 每次更新昵称
			user.setUserName(nickName);
			user.setOpenId(userInfo.getOpenId());
			user.setWechat(userInfo.getUnionid());
			user.setAvatar(userInfo.getHeadimgurl());
			this.sgaUserDmn.saveOrUpdate(user);
			this.dozerMapper.map(user, dto);
			final SgaUser cacheUser = user;
			if (globalConf.openCache()) {
				DistThreadManager.MyCacheThreadPool.execute(new Runnable() {

					@Override
					public void run() {
						redisCache.set(CacheKey.PREFIX_SGA_USER_ID + cacheUser.getId(), cacheUser);
						redisCache.set(CacheKey.PREFIX_SGA_USER + cacheUser.getSysCode(), cacheUser);
						List<SgaCompany> companies = sgaComService.getComInfoByUserCode(cacheUser.getSysCode());
						if(companies != null && !companies.isEmpty()) {
							for(SgaCompany company : companies) {
								redisCache.remove(CacheKey.CACHEKEY_REALM_SGA_USER + company.getRealm());
								try {
							      listValidUsersByRealm(company.getRealm());
								} catch(Exception e) {
								}
							}
						}
					}
				});
			}

			return dto;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	public UserResponseDTO checkUserInfoOfWechatapp(WxappEncryptedUserInfo info) {

		UserInfo userInfo = this.decodeUserInfoOfWxapp(info.getEncryptedData(), info.getIv(), info.getCode());

		return checkUserInfoOfWechat(userInfo);
	}

	@Override
	public UserResponseDTO updateUserBasicInfo(UserBasicInfoRequestDTO info) throws BusinessException {

		final SgaUser user = this.sgaUserDmn.findById(info.getId());
		if (null == user)
			throw new BusinessException("用户信息不存在，id[{}]", info.getId());

		try {
			this.dozerMapper.map(info, user);
			this.sgaUserDmn.modify(user);
			if (this.globalConf.openCache()) {
				DistThreadManager.MyCacheThreadPool.execute(new Runnable() {
					
					@Override
					public void run() {
						redisCache.set(CacheKey.PREFIX_SGA_USER_ID + user.getId(), user);
						redisCache.set(CacheKey.PREFIX_SGA_USER + user.getSysCode(), user);
						List<SgaCompany> companies = sgaComService.getComInfoByUserCode(user.getSysCode());
						if(companies != null && !companies.isEmpty()) {
							for(SgaCompany company : companies) {
								redisCache.remove(CacheKey.CACHEKEY_REALM_SGA_USER + company.getRealm());
								try {
							    listValidUsersByRealm(company.getRealm());
								} catch(Exception e) {
								}
							}
						}	
					}
				});
			}
			return this.dozerMapper.map(user, UserResponseDTO.class);
			// return this.userModel2DTO(user);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	@Override
	public UserInfo decodeUserInfoOfWxapp(String encryptedData, String iv, String code) throws BusinessException {

		UserInfo userInfo = null;

		// 小程序唯一标识 (在微信小程序管理后台获取)
		String appIdEncrypt = this.sgaConf.getWxAppAppIdEncrypt();
		logger.info(">>>获取到的微信appId：" + appIdEncrypt);
		// String wxspAppid = DesEncryptRSA.getInstance().decrypt(appIdEncrypt);

		// 小程序的 app secret (在微信小程序管理后台获取)
		String appSecretEncrypt = this.sgaConf.getWxAppAppSecretEncrypt();
		logger.info(">>>获取到的微信appSecret：" + appSecretEncrypt);
		// String wxspSecret =
		// DesEncryptRSA.getInstance().decrypt(appSecretEncrypt);
		// 授权（必填）
		String grant_type = "authorization_code";

		// 1、向微信服务器 使用登录凭证 code 获取 session_key 和 openid
		////////////////
		// 请求参数
		String formatURI = String.format(this.sgaConf.getInvokeJscode2sessionURI(),
				DesEncryptRSA.getInstance().decrypt(appIdEncrypt),
				DesEncryptRSA.getInstance().decrypt(appSecretEncrypt), code, grant_type);
		logger.info(">>>获取 session_key服务URI：" + formatURI);
		/*
		 * String params = "appid=" + wxspAppid + "&secret=" + wxspSecret +
		 * "&js_code=" + code + "&grant_type=" + grant_type;
		 */
		// 发送请求
		// 正常返回的JSON数据包
		/*
		 * { "openid": "OPENID", "session_key": "SESSIONKEY" }
		 */
		// 错误时返回JSON数据包(示例为Code无效)
		/*
		 * { "errcode": 40029, "errmsg": "invalid code" }
		 */
		String sessionKeyJsonStr = HttpRequestHelper.sendGet(formatURI);
		if (StringUtils.isEmpty(sessionKeyJsonStr)) {
			String msg = "code 换取 session_key失败，微信服务器没有返回内容";
			logger.error(">>>" + msg);
			throw new BusinessException(msg);
		}
		// 解析相应内容（转换成json对象）
		JSONObject json = JSONObject.parseObject(sessionKeyJsonStr);
		if (json.get("errcode") != null) {
			String msg = "code 换取 session_key失败，code：" + json.get("errcode") + "，msg：" + json.get("errmsg");
			logger.error(">>>" + msg);
			throw new BusinessException(msg);
		}
		// 获取会话密钥（session_key）
		String session_key = json.get("session_key").toString();
		// 用户的唯一标识（openid）
		// String openid = (String) json.get("openid");

		//////////////// 2、对encryptedData加密数据进行AES解密 ////////////////
		try {
			String result = AesCbcUtil.decrypt(encryptedData, session_key, iv, "UTF-8");
			if (StringUtils.hasLength(result)) {

				userInfo = new UserInfo();
				JSONObject userInfoJSON = JSONObject.parseObject(result);
				userInfo.setOpenId(String.valueOf(userInfoJSON.get("openId")));
				userInfo.setNickName(String.valueOf(userInfoJSON.get("nickName")));
				userInfo.setGender(null == userInfoJSON.get("gender") ? 0
						: Integer.parseInt(userInfoJSON.get("gender").toString()));
				userInfo.setCity(String.valueOf(userInfoJSON.get("city")));
				userInfo.setProvince(String.valueOf(userInfoJSON.get("province")));
				userInfo.setCountry(String.valueOf(userInfoJSON.get("country")));
				userInfo.setHeadimgurl(String.valueOf(userInfoJSON.get("avatarUrl")));
				userInfo.setUnionid(String.valueOf(userInfoJSON.get("unionId")));

				return userInfo;
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		return userInfo;
	}

	@Override
	public List<SgaUserAttachment> saveOrUpdateUserAttachementMetadata(List<UserAttachmentDTO> attacheInfos) {

		List<SgaUserAttachment> attachements = new ArrayList<SgaUserAttachment>();
		SgaUserAttachment tempAttach = null;
		for (UserAttachmentDTO dto : attacheInfos) {
			if ((tempAttach = this.sgaUserDmn.saveOrUpdateUserAttachement(dto)) != null) {
				attachements.add(tempAttach);
			}
		}
		return attachements;
	}

	@Override
	public List<SgaUserAttachment> getUserResumes(String userCode) {

		UserResponseDTO user = this.getUserByCode(userCode);
		if (null == user) {
			throw new BusinessException("用户信息不存在，code[{}]", userCode);
		}
		return this.sgaUserDmn.getAttachmentsByUserId(user.getId(), AttachmentTypeConstants.ATTACHMENT_TYPE_RESUME);
	}
	@Override
	public UserAttachRepDTO getUserResumeInfo(String userCode) {
		
		UserResponseDTO dto = this.getUserByCode(userCode);
		if(null == dto) return null;
		
		List<SgaUserAttachment> resumes = this.sgaUserDmn.getAttachmentsByUserId(dto.getId(), 0);
		if(null == resumes || resumes.isEmpty()) {
			return null;
		}
		return this.dozerMapper.map(resumes.get(0), UserAttachRepDTO.class);
	}
}
