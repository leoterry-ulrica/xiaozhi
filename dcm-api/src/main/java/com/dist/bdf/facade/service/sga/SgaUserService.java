package com.dist.bdf.facade.service.sga;

import com.dist.bdf.model.dto.sga.UserRegisterSimpleDTO;
import com.dist.bdf.model.dto.sga.UserResponseDTO;
import com.dist.bdf.model.dto.system.user.UserSimpleDTO;
import com.dist.bdf.model.dto.wechat.AccessToken;
import com.dist.bdf.model.dto.wechat.UserInfo;
import com.dist.bdf.model.dto.wechat.WxappEncryptedUserInfo;
import com.dist.bdf.model.entity.sga.SgaUser;
import com.dist.bdf.model.entity.sga.SgaUserAttachment;

import java.util.List;

import com.dist.bdf.base.exception.BusinessException;
import com.dist.bdf.model.dto.sga.ConfirmlinkDTO;
import com.dist.bdf.model.dto.sga.SignupDTO;
import com.dist.bdf.model.dto.sga.UserAttachRepDTO;
import com.dist.bdf.model.dto.sga.UserAttachmentDTO;
import com.dist.bdf.model.dto.sga.UserBasicInfoRequestDTO;
import com.dist.bdf.model.dto.sga.UserEmailValidDTO;

/**
 * 
 * 用户服务
 * @author weifj
 * 
 *
 */
public interface SgaUserService {

	/**
	 * 自主注册，需要发送确认函
	 * @param serviceURI 服务注册地址
	 * @param redirectURI 重定向地址
	 * @param registerType 注册类型。0：默认自主注册；1：项目邀请；2：企业邀请
	 * @param mail 邮箱地址
	 * @return
	 */
	Object sendMailConfirmation(String serviceURI, SignupDTO dto) throws BusinessException;
	/**
	 * 只是点击确认链接
	 * @param userId
	 * @param queueId
	 * @return
	 */
	UserEmailValidDTO clickConfirmationLink(ConfirmlinkDTO linkdto) throws BusinessException;
	/**
	 * 用户注册，默认注册方式，通过点击验证链接进行注册
	 * 说明此时用户已存在服务器端
	 * @param into
	 * @return 返回邮箱
	 */
	Object registerFromValidLink(UserRegisterSimpleDTO info) throws BusinessException;
	/**
	 * 邀请注册
	 * @param info
	 * @return
	 */
	Object inviteRegister(UserRegisterSimpleDTO info) throws BusinessException;
	/**
	 * 检测email的有效性
	 * @param email
	 * @return true/false
	 */
	boolean checkEmailValid(String email);
	/**
	 * 获取所有已注册的用户
	 * @return
	 */
	List<UserResponseDTO> listAllRegisterUsers();
	/**
	 * 获取所有合法账号
	 * @return
	 */
	List<UserResponseDTO> listValidUsers();
	/**
	 * 获取域下的有效账号
	 * @param realm
	 * @return
	 */
	List<UserResponseDTO> listValidUsersByRealm(String realm);
	/**
	 * 根据邮箱匹配
	 * @param email
	 * @return
	 */
	Object matchUserByEmail(String email);
	/**
	 * 根据微信号WeChat匹配
	 * @param wechat
	 * @return
	 */
	Object matchUserByWechat(String wechat);
	/**
	 * 通过序列id获取用户信息
	 * @param id
	 * @return
	 */
	UserResponseDTO getUserById(Long id);
	/**
	 * 根据邮箱登录
	 * @param email
	 * @param pwd
	 * @return
	 */
	Object loginByEmail(String email, String pwd) throws BusinessException;
	/**
	 * 使用微信登录，不做密码的验证，因为用户的安全认证是通过腾讯微信的安全通道
	 * @param code code作为换取access_token的票据，每次用户授权带上的code将不一样，code只能使用一次，5分钟未被使用自动过期。
	 * @param state 状态值（可自定义）
	 * @return
	 */
	UserResponseDTO loginByWechatNoPwd(String code, String state);
	
	/**
	 * 更新用户基本信息
	 * @param info
	 * @return
	 */
	UserResponseDTO updateUserBasicInfo(UserBasicInfoRequestDTO info);
	/**
	 * 检验微信用户信息，如果不存在，则新增；否则返回信息。
	 * @param userInfo
	 * @return
	 */
	Object checkUserInfoOfWechat(UserInfo userInfo);
	/**
     * 解密微信用户敏感数据
     *
     * @param encryptedData 明文,加密数据
     * @param iv            加密算法的初始向量
     * @param code          用户允许登录后，回调内容会带上 code（有效期五分钟），开发者需要将 code 发送到开发者服务器后台，使用code 换取 session_key api，将 code 换成 openid 和 session_key
     * @return
     */
	UserInfo decodeUserInfoOfWxapp(String encryptedData, String iv, String code);
	/**
	 * 提供给微信小程序，检验微信用户信息，如果不存在，则新增；否则返回信息。
	 * @param info
	 * @return
	 */
	Object checkUserInfoOfWechatapp(WxappEncryptedUserInfo info);
	/**
	 * 根据用户编码获取用户
	 * @param code
	 * @return
	 */
	UserResponseDTO getUserByCode(String code);
	/**
	 * 保存用户附件元数据
	 * @param attacheInfos
	 * @return
	 */
	List<SgaUserAttachment> saveOrUpdateUserAttachementMetadata(List<UserAttachmentDTO> attacheInfos);
	/**
	 * 根据用户编码获取简历附件元数据
	 * @param userCode
	 * @return
	 */
	List<SgaUserAttachment> getUserResumes(String userCode);
	/**
	 * 获取有效用户，被其它子模块RPC调用
	 * @param realm
	 * @return
	 */
	List<UserSimpleDTO> listValidUsersByRealmPublic(String realm);
	/**
	 * 通过id获取用户实体
	 * @param id
	 * @return
	 */
	SgaUser getUserEntityById(Long id);
	/**
	 * 获取用户简历信息
	 * @param userCode
	 * @return
	 */
	UserAttachRepDTO getUserResumeInfo(String userCode);

}
