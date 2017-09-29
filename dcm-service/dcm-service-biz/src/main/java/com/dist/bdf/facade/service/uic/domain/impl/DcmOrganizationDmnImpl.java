
package com.dist.bdf.facade.service.uic.domain.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.dist.bdf.base.dao.GenericDAO;
import com.dist.bdf.base.domain.GenericDmnImpl;
import com.dist.bdf.base.exception.BusinessException;
import com.dist.bdf.facade.service.uic.dao.DcmDicOrgExtDAO;
import com.dist.bdf.facade.service.uic.dao.DcmOrgUserDAO;
import com.dist.bdf.facade.service.uic.dao.DcmOrganizationDAO;
import com.dist.bdf.facade.service.uic.dao.DcmRoleDAO;
import com.dist.bdf.facade.service.uic.dao.DcmUserDAO;
import com.dist.bdf.facade.service.uic.dao.DcmUserdomainroleDAO;
import com.dist.bdf.facade.service.uic.domain.DcmOrganizationDmn;
import com.dist.bdf.facade.service.uic.domain.DcmUserDmn;
import com.dist.bdf.common.constants.DomainType;
import com.dist.bdf.model.dto.system.Org2UsersDTO;
import com.dist.bdf.model.dto.system.OrgDTO;
import com.dist.bdf.model.dto.system.user.UserOrgRolesUpdateDTO;
import com.dist.bdf.model.entity.system.DcmDicOrgExt;
import com.dist.bdf.model.entity.system.DcmOrgUser;
import com.dist.bdf.model.entity.system.DcmOrganization;
import com.dist.bdf.model.entity.system.DcmRole;
import com.dist.bdf.model.entity.system.DcmUser;
import com.dist.bdf.model.entity.system.DcmUserdomainrole;

/**
 * 组织机构领域
 * @author weifj
 * @version 1.0，2016/1/12，创建
 * @version 1.1，2016/1/20，添加业务方法
 * @version 1.2，2016/03/08，添加方法listDirectUsersOfOrg(Long orgId)具体业务实现
 * @version 1.3，2016/03/09，weifj
 *    1. 添加方法：List<DcmUser> listUsersOfOrg(Long orgId)具体实现
 *    2. 添加方法：List<DcmOrganization> listOrgsOfOrg(Long orgId)具体实现
 */
@Service
@Transactional(propagation = Propagation.REQUIRED) 
public class DcmOrganizationDmnImpl extends GenericDmnImpl<DcmOrganization, Long> implements DcmOrganizationDmn {

	@Autowired
	private DcmOrganizationDAO orgDao;
	//private GenericDAOImpl<DcmOrganization, Long> orgDao;
	@Autowired
	private DcmOrgUserDAO orgUserDao;
	//private GenericDAOImpl<DcmOrgUser, Long> orgUserDao;
	@Autowired
	private DcmUserDAO userDao;
	//private GenericDAOImpl<DcmUser, Long> userDao;
	@Autowired
	private DcmUserdomainroleDAO userDomainRoleDao;
	//private GenericDAOImpl<DcmUserdomainrole, Long> userDomainRoleDao;
	@Autowired
	private DcmUserDmn userDmn;
	@Autowired
	private DcmDicOrgExtDAO dicOrgExtDao;
	@Autowired
	private DcmRoleDAO roleDao;

	@Override
	public GenericDAO<DcmOrganization, Long> getDao() {

		return orgDao;
	}

	@Override
	public Object editOrg(OrgDTO orgDto) {
	
		return null;
	}

	@Override
	public Object deleteOrg(DcmOrganization org) {
		
		logger.debug(">>>删除用户-域-角色的记录......");
		this.userDomainRoleDao.removeByProperty("domainCode", org.getOrgCode());
		logger.debug(">>>删除用户-机构的记录......");
		this.orgUserDao.removeByProperty("orgId", org.getId());
		logger.debug(">>>删除机构实体的记录......");
		this.orgDao.remove(org.getId());
		
		return true;
		
	}

	@Override
	public Object deleteOrgByCode(String code) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addUsers(Long orgId, Object[] userIdArray) {
		
		Assert.noNullElements(userIdArray);
		
		for(Object userId : userIdArray){
			DcmOrgUser orguser = this.orgUserDao.findUniqueByProperties(new String[]{"userId","orgId"}, new Object[]{userId, orgId});
			
			if(orguser != null) continue;
			
			DcmOrgUser newOrgUser = new DcmOrgUser();
			newOrgUser.setOrgId(orgId);
			newOrgUser.setUserId(Long.valueOf(userId.toString()));
			this.orgUserDao.save(newOrgUser);
		}
	}

	@Override
	public void deleteUser(Long orgId, Object[] userIdArray) {
		
		for(Object userId : userIdArray){
			this.orgUserDao.removeByProperties(new String[]{"orgId","userId"}, new Object[]{orgId, userId});
		}
	
	}

	@Override
	public void deleteUser(Long[] userIds) {
		
		this.orgUserDao.removeByProperty("userId", userIds);
	}
	@Override
	public List<DcmOrgUser> getOrgUserRef(Long orgId) {
		
		return this.orgUserDao.findByProperty("orgId", orgId);
	}
	@Override
	public List<DcmOrganization> getAllSimpleOrg() {
		
		return this.orgDao.findByProperty("domainType", new Object[]{"Domain_Institute", "Domain_Department"});
		
	}

	@Override
	public  List<DcmUser> getUsers(Long orgId) {
		
		List<DcmOrgUser> list = this.orgUserDao.findByProperty("orgId", orgId);
		Long[] userIds = new Long[list.size()];
		for(int i=0;i<list.size();i++){
			userIds[i] = list.get(i).getUserId();
		}
		
		if(null == userIds || 0 == userIds.length) return null;
		
				
		return this.userDmn.getValidUsers(userIds);//.find(userIds);
	}
	@Override
	public List<DcmUser> getUsersByOrgCode(String orgCode) {
		
		DcmOrganization org = this.orgDao.findUniqueByProperty("orgCode", orgCode);
		if(null == org) return null;
		
		return this.getUsers(org.getId());
	}

	@Override
	public void deleteOrgByName(String name) {
		
		DcmOrganization org = this.isExistOrgNameInner(name);
		if(null == org) this.logger.warn(String.format(">>>没有找到名称为【%s】的机构信息......", name));
		// 删除域权限表信息
		this.userDomainRoleDao.removeByProperty("domainCode", org.getOrgCode());
		// 删除机构信息
		this.orgDao.removeByProperty("orgName", name);
	}

	private DcmOrganization isExistOrgNameInner(String name){
		
		return this.orgDao.findUniqueByProperty("orgName", name);
	}
	@Override
	public boolean isExistOrgName(String name) {
		
		DcmOrganization org = this.isExistOrgNameInner(name);
		return (org != null);
	}

	private DcmOrganization isExistOrgCodeInner(String code){
		
		return this.orgDao.findUniqueByProperty("orgCode", code);
	}
	@Override
	public boolean isExistOrgCode(String code) {
		
		DcmOrganization org = isExistOrgCodeInner(code);
		return (org != null);
		
	}
	@Override
	public List<DcmOrganization> listDirectChildOrgs(Long orgId) {
		
		return this.orgDao.findByProperty("parentId", orgId);
		
	}
	@Override
	public List<DcmUser> listDirectUsersOfOrg(Long orgId) {

		List<DcmUser> users = new ArrayList<DcmUser>();
		List<DcmOrgUser> list = this.orgUserDao.findByProperty("orgId", orgId);
		for(DcmOrgUser ou : list){
			DcmUser user = this.userDao.find(ou.getUserId());
			if(user!=null && user.getCurrentStatus().equals(1L)) {
				users.add(user);
			}
		}
		
		return users;
	}
	@Override
	public List<DcmUser> listUsersOfOrg(Long orgId) {
		
       List<DcmUser> users = new ArrayList<DcmUser>();
		
		List<DcmOrganization> childOrgs = this.listOrgsOfOrg(orgId);
		for(DcmOrganization subOrg : childOrgs){
			 List<DcmUser> orgUsers = this.listDirectUsersOfOrg(subOrg.getId());
		       if(orgUsers!=null && orgUsers.size() > 0){
					
					for(DcmUser subUser : orgUsers){
						if(subUser.getCurrentStatus().equals(1L)){
							users.add(subUser);
						}
						
					}
				}
		}
		return users;
	}

	@Override
	public List<DcmOrganization> listOrgsOfOrg(Long orgId) {
		
		List<DcmOrganization> orgs = new ArrayList<DcmOrganization>();
    	recursionOrgsOfOrg(orgId, orgs);
    	
    	return orgs;
	}
	@Override
	public List<DcmOrganization> listSubDepartmentsByInstituteUniqueName(String name) {
		
		DcmOrganization org = this.getInstituteByUniqueName(name);
		if(null == org) return null;
		
		return this.listOrgsOfOrg(org.getId());
	}
	@Override
	public List<DcmOrganization> listSubDepartmentsByInstituteUniqueName(DcmOrganization institute) {
		
	    if(null == institute) return null;
		
		return this.listOrgsOfOrg(institute.getId());
	}
	/**
	 * 递归查询机构
	 * @param parentOrgId
	 * @param orgs
	 */
	private void  recursionOrgsOfOrg(Long parentOrgId, List<DcmOrganization> orgs){
	
		List<DcmOrganization> childOrgs = this.orgDao.findByProperty("parentId", parentOrgId, "alias", true);
		if(childOrgs != null && childOrgs.size()>0){
			for(DcmOrganization subOrg : childOrgs){
				orgs.add(subOrg);
				recursionOrgsOfOrg(subOrg.getId(),orgs);
			}
		}
	}
	@Override
	public List<DcmOrganization> getOrgsByUserId(Long userId) {
		
		List<DcmOrgUser> ouList = this.orgUserDao.findByProperty("userId", userId);
		if(null == ouList || 0 == ouList.size()) return null;
		
		List<DcmOrganization> orgs = new ArrayList<DcmOrganization>();
		for(DcmOrgUser o2u : ouList){
			
			DcmOrganization o = this.orgDao.findUniqueByProperty("id", o2u.getOrgId());
			if(o != null) orgs.add(o);
		}
		
		return orgs;
	}
	
	@Override
	public List<Org2UsersDTO> getAllOrgAndUser(boolean loadUser){
		
		// 链表存储，
		// 在插入或删除操作，相比ArrayList效率高
		// ArrayList底层是数组存储方式
		// LinkedList底层是链表存储方式，记录前序节点和后续节点
		List<Org2UsersDTO> link = new LinkedList<Org2UsersDTO>();
		
		List<DcmOrganization> orgs = this.getAllSimpleOrg();
		for(DcmOrganization org : orgs){
			
			if(org.getDomainType().equalsIgnoreCase("root")) continue;
			
			Org2UsersDTO dto = toOrgUsersDTO(loadUser, org);
		
			link.add(dto);
		}
		return link;
	}
	@Override
	public List<Org2UsersDTO> getAllOrgAndUser(boolean loadUser, String instituteUniqueName) {
		
		// 链表存储，
		// 在插入或删除操作，相比ArrayList效率高
		// ArrayList底层是数组存储方式
		// LinkedList底层是链表存储方式，记录前序节点和后续节点
		List<Org2UsersDTO> link = new LinkedList<Org2UsersDTO>();
		
		DcmOrganization ins = this.getInstituteByUniqueName(instituteUniqueName);
		if(null == ins) return null;
		
		List<DcmOrganization> orgs = this.listSubDepartmentsByInstituteUniqueName(ins);
		orgs.add(ins);
		
		for(DcmOrganization org : orgs){
			
			if(org.getDomainType().equalsIgnoreCase("root")) continue;
			
			Org2UsersDTO dto = toOrgUsersDTO(loadUser, org);
		
			link.add(dto);
		}
		return link;
		
	}
	
	@Override
    public List<Org2UsersDTO> getAllOrgAndUser(List<DcmOrganization> orgs ,boolean loadUser){
		
		// 链表存储，
		// 在插入或删除操作，相比ArrayList效率高
		// ArrayList底层是数组存储方式
		// LinkedList底层是链表存储方式，记录前序节点和后续节点
		List<Org2UsersDTO> link = new ArrayList<Org2UsersDTO>();
		
		for(DcmOrganization org : orgs){
			
			if(org.getDomainType().equalsIgnoreCase("root")) continue;
			
			Org2UsersDTO dto = toOrgUsersDTO(loadUser, org);
		
			link.add(dto);
		}
		return link;
	}

	private Org2UsersDTO toOrgUsersDTO(boolean loadUser, DcmOrganization org) {
		Org2UsersDTO dto = new Org2UsersDTO();
		dto.setId(org.getId());
		dto.setParentId(org.getParentId());
		dto.setOrderId(org.getOrderId());
		dto.setOrgCode(org.getOrgCode());
		dto.setOrgName(org.getOrgName());
		dto.setAlias(org.getAlias());
		dto.setOrgType(org.getOrgType());
		if(loadUser){
			List<DcmUser> users = this.getUsers(org.getId());
		    if(users != null)
			   dto.setUsers(users);
		}
		return dto;
	}
	@Override
	public void clearAllOrg2User() {
		this.orgUserDao.removeAll();
	}
	@Override
	public void addOrg2User(Collection<DcmOrgUser> coll) {
		
		Assert.notEmpty(coll);
		
		for(DcmOrgUser orguser : coll){
			addOrg2User(orguser);
		}

	}
	@Override
	public void addOrg2User(DcmOrgUser orguser) {
		
		this.orgUserDao.save(orguser);
	}
	@Override
	public List<DcmOrganization> getAllDepartment() {
		
		return this.getDao().findByProperty("domainType", "Domain_Department", "orderId",true);
	}
	@Override
	public List<DcmOrganization> getAllInstitute() {
		
		return this.getDao().findByProperty("domainType", "Domain_Institute", "orderId",true);
	}
	@Override
	public DcmOrganization getOrgByCode(String code) {

		return this.getDao().findUniqueByProperty("orgCode", code);
	}
	@Override
	public DcmOrganization getInstituteByUniqueName(String name) {
		
		return this.getDao().findUniqueByProperties(new String[]{"domainType","orgName"}, new Object[]{DomainType.INSTITUTE, name});
	}
	
    public boolean existOrg2User(Long orgId, Long userId) {
    	
    	List<DcmOrgUser> list = this.orgUserDao.findByProperties(new String[] { "orgId", "userId" },
    			new Object[] { orgId, userId});
    	
    	return list !=null && !list.isEmpty();
    }
    
    @Override
	public void updateOrgAndRoleUser(Long userId, DcmOrganization preOrg, DcmOrganization newOrg, DcmRole preRole,
			DcmRole newRole) {

		DcmOrgUser ou = null;
		DcmUserdomainrole udr = null;
		if (null == preOrg) {

			logger.debug(">>>用户 [{}]原本不在部门中......", userId);
			ou = new DcmOrgUser();
			ou.setOrgId(newOrg.getId());
			ou.setUserId(userId);
			this.orgUserDao.save(ou);

		} else {
			if (!preOrg.getId().equals(newOrg.getId())) {
				ou = this.orgUserDao.findUniqueByProperties(new String[] { "userId", "orgId" },
						new Object[] { userId, preOrg.getId() });
				logger.debug(">>>修改部门，[{}]->[{}]", preOrg.getAlias(), newOrg.getAlias());

				ou.setOrgId(newOrg.getId());
				this.orgUserDao.update(ou);
			}
		}

		if (null == preRole) {

			udr = this.userDomainRoleDao.findUniqueByProperties(
					new String[] { "userId", "domainCode", "domainType" },
					new Object[] { userId, newOrg.getOrgCode(), newOrg.getDomainType() });
			if(null == udr){
				udr = new DcmUserdomainrole();
				udr.setCreateTime(new Date());
				udr.setDomainCode(newOrg.getOrgCode());
				udr.setDomainType(newOrg.getDomainType());
				udr.setRoleCode(newRole.getRoleCode());
				udr.setUserId(userId);
				this.userDomainRoleDao.save(udr);
				logger.info(">>>用户[{}]-空间域[{}]-角色[{}]不存在，已添加...",userId, newOrg.getOrgCode(), newRole.getRoleCode());
			}	
		} else {
			logger.debug(">>>修改部门中的角色，[{}]->[{}]", preRole, newRole);
			udr = this.userDomainRoleDao.findUniqueByProperties(
					new String[] { "userId", "domainCode", "domainType" },
					new Object[] { userId, preOrg.getOrgCode(), preOrg.getDomainType() });
			if(null == udr){
				return;
			}
			udr.setDomainCode(newOrg.getOrgCode());
			udr.setRoleCode(newRole.getRoleCode());
			udr.setCreateTime(new Date());
			this.userDomainRoleDao.update(udr);
		}
	}
    
    @Override
    public void updateOrgAndRoleUser(Long userId, UserOrgRolesUpdateDTO institute, UserOrgRolesUpdateDTO preDepartment, UserOrgRolesUpdateDTO newDepartment, DcmOrganization preOrg, DcmOrganization newOrg) {
    	
    	DcmOrgUser ou = null;
		if (null == preDepartment.getOrgId() ||  -1 == preDepartment.getOrgId()) {

			logger.debug(">>>用户 [{}]原本不在部门中......", userId);
			ou = new DcmOrgUser();
			ou.setOrgId(newDepartment.getOrgId());
			ou.setUserId(userId);
			this.orgUserDao.save(ou);
			// 所-角色
			updateUserOrgRole(userId, newDepartment);
			
		} else {
			if (!preDepartment.getOrgId().equals(newDepartment.getOrgId())) {
				ou = this.orgUserDao.findUniqueByProperties(new String[] { "userId", "orgId" },
						new Object[] { userId, preDepartment.getOrgId() });
				logger.debug(">>>修改部门，[{}]->[{}]", preOrg.getAlias(), newOrg.getAlias());

				ou.setOrgId(newOrg.getId());
				this.orgUserDao.update(ou);
				// 删除原有机构的角色
				this.userDomainRoleDao.removeByProperties(new String[]{"userId", "domainCode"}, new Object[]{userId,  preOrg.getOrgCode()});
			} else {
				// 没有修改部门，只修改权限
				// 所-角色
				updateUserOrgRole(userId, newDepartment);
			}
		}
		// 院-角色
		updateUserOrgRole(userId, institute);
    }
    
    @Override
    public void updateOrgAndRoleUser(Integer userType, DcmUser user, UserOrgRolesUpdateDTO institute, UserOrgRolesUpdateDTO preDepartment, UserOrgRolesUpdateDTO newDepartment, DcmOrganization preOrg, DcmOrganization newOrg) {
    	
    	DcmOrgUser ou = null;
		if (null == preDepartment.getOrgId() ||  -1 == preDepartment.getOrgId()) {

			logger.debug(">>>用户 [{}]原本不在部门中......", user.getUserName());
			ou = new DcmOrgUser();
			ou.setOrgId(newDepartment.getOrgId());
			ou.setUserId(user.getId());
			this.orgUserDao.save(ou);
			// 所-角色
			updateUserOrgRole(0, user, newDepartment);
			
		} else {
			if (!preDepartment.getOrgId().equals(newDepartment.getOrgId())) {
				ou = this.orgUserDao.findUniqueByProperties(new String[] { "userId", "orgId" },
						new Object[] { user.getId(), preDepartment.getOrgId() });
				logger.debug(">>>修改部门，[{}]->[{}]", preOrg.getAlias(), newOrg.getAlias());

				ou.setOrgId(newOrg.getId());
				this.orgUserDao.update(ou);
				// 删除原有机构的角色
				this.userDomainRoleDao.removeByProperties(new String[]{"userCode", "domainCode"}, new Object[]{user.getUserCode(),  preOrg.getOrgCode()});
			} else {
				// 没有修改部门，只修改权限
				// 所-角色
				updateUserOrgRole(0, user, newDepartment);
			}
		}
		// 院-角色
		updateUserOrgRole(0, user, institute);
    }

	private void updateUserOrgRole(Long userId, UserOrgRolesUpdateDTO org) {
		DcmUserdomainrole udr = null;
		DcmRole role = null;
		if(org.getOrgId()!= -1 && org.getRoleIdMode() != null && !org.getRoleIdMode().isEmpty()) {
			DcmOrganization instituteOrg = this.orgDao.find(org.getOrgId());
			if(instituteOrg != null) {
				Iterator<Entry<Long, Integer>> entries = org.getRoleIdMode().entrySet().iterator();  
				while (entries.hasNext()) {  
					Entry<Long, Integer> next = entries.next();
					role = this.roleDao.find(next.getKey());
					if(0 == next.getValue()) {
						// 删除角色
						this.userDomainRoleDao.removeByProperties(new String[]{"userId", "domainCode", "domainType","roleCode"}, new Object[]{userId,  instituteOrg.getOrgCode(), instituteOrg.getDomainType(), role.getRoleCode() });
					} else if(1 == next.getValue()) {
						// 添加角色
						udr = this.userDomainRoleDao.findUniqueByProperties(
								new String[] { "userId", "domainCode", "domainType", "roleCode"},
								new Object[] { userId, instituteOrg.getOrgCode(), instituteOrg.getDomainType(), role.getRoleCode() });
						if(null == udr) {
							udr = new DcmUserdomainrole();
							udr.setCreateTime(new Date());
							udr.setDomainCode(instituteOrg.getOrgCode());
							udr.setDomainType(instituteOrg.getDomainType());
							udr.setRoleCode(role.getRoleCode());
							udr.setUserId(userId);
							this.userDomainRoleDao.save(udr);
							logger.info(">>>用户[{}]-空间域[{}]-角色[{}]不存在，已添加...",userId, instituteOrg.getOrgCode(), role.getRoleCode());
						}	
					}
				}
			}
		}
	}
	private void updateUserOrgRole(Integer userType, DcmUser user, UserOrgRolesUpdateDTO org) {
		DcmUserdomainrole udr = null;
		DcmRole role = null;
		if(org.getOrgId()!= -1 && org.getRoleIdMode() != null && !org.getRoleIdMode().isEmpty()) {
			DcmOrganization orgObject = this.orgDao.find(org.getOrgId());
			if(orgObject != null) {
				Iterator<Entry<Long, Integer>> entries = org.getRoleIdMode().entrySet().iterator();  
				while (entries.hasNext()) {  
					Entry<Long, Integer> next = entries.next();
					role = this.roleDao.find(next.getKey());
					if(0 == next.getValue()) {
						// 删除角色
						this.userDomainRoleDao.removeByProperties(new String[]{"userCode", "domainCode", "domainType","roleCode"}, new Object[]{user.getUserCode(),  orgObject.getOrgCode(), orgObject.getDomainType(), role.getRoleCode() });
					} else if(1 == next.getValue()) {
						// 添加角色
						udr = this.userDomainRoleDao.findUniqueByProperties(
								new String[] { "userCode", "domainCode", "domainType", "roleCode"},
								new Object[] { user.getUserCode(), orgObject.getOrgCode(), orgObject.getDomainType(), role.getRoleCode() });
						if(null == udr) {
							udr = new DcmUserdomainrole();
							udr.setCreateTime(new Date());
							udr.setDomainCode(orgObject.getOrgCode());
							udr.setDomainType(orgObject.getDomainType());
							udr.setRoleCode(role.getRoleCode());
							udr.setUserId(user.getId());
							udr.setUserCode(user.getUserCode());
							udr.setUserType(userType);
							this.userDomainRoleDao.save(udr);
							logger.info(">>>用户[{}]-空间域[{}]-角色[{}]不存在，已添加...",user.getUserName(), orgObject.getOrgCode(), role.getRoleCode());
						}	
					}
				}
			}
		}
	}

    @Override
    public DcmDicOrgExt addDicInfo(Long orgId, Integer type, String name) {
    	
    	DcmDicOrgExt dicOrg = this.dicOrgExtDao.findUniqueByProperties(new String[]{"type","orgId","name"}, new Object[]{type, orgId, name});
    	if(dicOrg != null) {
    		logger.warn(">>>名称："+name+"已存在");
    		return null;
    	}
    	dicOrg = new DcmDicOrgExt();
    	dicOrg.setOrgId(orgId);
    	dicOrg.setName(name);
    	dicOrg.setType(type);
    	this.dicOrgExtDao.saveEntity(dicOrg);
    	return dicOrg;
    }
    @Override
    public DcmDicOrgExt addDicInfo(String orgCode, Integer type, String name) {
    	
    	DcmDicOrgExt dicOrg = this.dicOrgExtDao.findUniqueByProperties(new String[]{"type","orgCode","name"}, new Object[]{type, orgCode, name});
    	if(dicOrg != null) {
    		throw new BusinessException("名称："+name+"已存在");
    	}
    	dicOrg = new DcmDicOrgExt();
    	dicOrg.setOrgCode(orgCode);
    	dicOrg.setName(name);
    	dicOrg.setType(type);
    	this.dicOrgExtDao.saveEntity(dicOrg);
    	return dicOrg;
    }
    
    @Override
    public boolean deleteDic(Long id) {
    	return this.dicOrgExtDao.removeById(id);
    }
    @Override
    public boolean deleteDic(Integer type, String orgCode) {
    	
    	this.dicOrgExtDao.removeByProperties(new String[]{"type", "orgCode"}, new Object[]{type, orgCode});
    	return true;
    }
    @Override
    public List<DcmDicOrgExt> getDics(Long orgId, Integer type) {
    	
    	return this.dicOrgExtDao.findByProperties(new String[]{"type","orgId"}, new Object[]{type, orgId});
    }
    @Override
    public List<DcmDicOrgExt> getDics(String orgCode, Integer type) {
    	return this.dicOrgExtDao.findByProperties(new String[]{"type","orgCode"}, new Object[]{type, orgCode});
    }
    @Override
    public DcmDicOrgExt getOrgDicById(Long id) {
    	
    	DcmDicOrgExt dic = this.dicOrgExtDao.find(id);
    	return dic;
    }
    @Override
    public void removeAll(String realm) {
    	List<Org2UsersDTO> org2users = this.getAllOrgAndUser(false, realm);
    	if(null == org2users || org2users.isEmpty()) {
    		logger.warn(String.format("域[%s]下面没有找到相应机构信息", realm));
    		return;
    	}
    	DcmOrganization org = null;
    	for(Org2UsersDTO o2u : org2users) {
    		org = this.getOrgByCode(o2u.getOrgCode());
    		if(null == org) continue;
    		
    		this.orgUserDao.removeByProperty("orgId", org.getId());
    		this.orgDao.remove(org);
    	}
    }
}
