
package com.dist.bdf.facade.service.uic.impl;

import java.util.List;

import javax.annotation.PostConstruct;

import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dist.bdf.model.dto.system.RoleDTO;
import com.dist.bdf.model.entity.system.DcmRole;
import com.dist.bdf.base.exception.BusinessException;
import com.dist.bdf.base.utils.DistThreadManager;
import com.dist.bdf.facade.service.RoleService;
/*import com.dist.bdf.facade.service.cache.DistributedCacheService;*/
import com.dist.bdf.facade.service.uic.domain.DcmRoleDmn;
import com.dist.bdf.manager.cache.CacheStrategy;
import com.dist.bdf.common.conf.common.GlobalConf;
import com.dist.bdf.common.constants.CacheKey;
import com.dist.bdf.common.constants.RoleConstants;

/**
 * @author weifj
 * @version 1.0，2016/03/01，weifj，创建服务实现类
 */
@Service("RoleService")
@Transactional(propagation = Propagation.REQUIRED)
public class RoleServiceImpl implements RoleService {

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	//private volatile static Map<String, DcmRole> CacheRoleCodeMap = new HashMap<String, DcmRole>();
	//private volatile static Map<String, DcmRole> CacheRoleNameMap = new HashMap<String, DcmRole>();
	
	@Autowired
	private GlobalConf globalConf;
	@Autowired
	private DcmRoleDmn roleDmn;
/*	@Autowired
	private DistributedCacheService disCacheService;*/
	@Autowired
	private CacheStrategy redisCache;
		
    public void cacheRole(DcmRole role) {
		
		this.redisCache.set(CacheKey.PREFIX_ROLE + role.getId(), role, 0L);
		this.redisCache.set(CacheKey.PREFIX_ROLE + role.getRoleCode(), role, 0L);
	}

	@PostConstruct
	private synchronized void initRoleInfo() {
	
		if(!this.globalConf.openCache())
			return;
		
		DistThreadManager.MyCacheThreadPool.execute(new Runnable() {
			
			@Override
			public void run() {
				
				long start = System.currentTimeMillis();
				
				logger.info(">>>开始初始化角色信息......");

				redisCache.removePattern(CacheKey.PREFIX_ROLE_PATTERN);
				List<DcmRole> roles = RoleServiceImpl.this.listAllRoles();
				for(DcmRole r : roles){
					
					cacheRole(r);
				}
				long end = System.currentTimeMillis();
				logger.info(">>>完成初始化用户信息，共消耗 [{}] 毫秒。", end - start);
				
			}
		});
		
	}
	
	@Override
	public List<DcmRole> listAllRoles() {
		
		return this.roleDmn.find();
	}
	@Override
	public List<DcmRole> getRolesOfProject(){
		return this.roleDmn.findByProperties(new String[]{"roleType", "realm"}, new Object[]{RoleConstants.RoleType.T_Project, "common"} );
		// return this.roleDmn.getRolesOfProject();
	}
	@Override
	public List<DcmRole> getRolesOfProject(String realm){
		return this.roleDmn.findByProperties(new String[]{"roleType", "realm"}, new Object[]{RoleConstants.RoleType.T_Project, realm} , "sortId", true);
	}
	@Override
	public List<DcmRole> getRolesOfTeam(String realm){
		return this.roleDmn.findByProperties(new String[]{"roleType", "realm"}, new Object[]{RoleConstants.RoleType.T_Team, realm}, "sortId", true);
	}
	@Override
	public List<DcmRole> getRolesOfOrg() {
		
		return this.roleDmn.findByProperty("roleType", new Object[]{RoleConstants.RoleType.T_Institute, RoleConstants.RoleType.T_Department });
	}
	@Override
	public List<DcmRole> getRolesOfOrg(long orgType) {
		
		return this.roleDmn.findByProperties(new String[]{"roleType", "realm"}, new Object[]{orgType, "common"} );
	}
	@Override
	public List<DcmRole> getRolesOfOrg(String realm, long orgType) {
		return this.roleDmn.findByProperties(new String[]{"roleType", "realm"}, new Object[]{orgType, realm} );
	}
	@Override
	public DcmRole addRole(RoleDTO dto) throws BusinessException {
		
		if(this.roleDmn.existsedByProperty("roleCode", dto.getRoleCode(), false)) throw new BusinessException("角色编码 [{0}] 已存在，不能重复添加。", dto.getRoleCode());
		
		if(this.roleDmn.existsedByProperty("roleName", dto.getRoleName(), false)) throw new BusinessException("角色名称 [{0}] 已存在，不能重复添加。", dto.getRoleName());
		
		DcmRole role = this.roleDmn.addRole(dto.getRoleCode(), dto.getRoleName());
		if(this.globalConf.openCache())
		   cacheRole(role);

		return role;
	}
	@Override
	public DcmRole updateRole(RoleDTO dto) {
		
		DcmRole role = this.roleDmn.updateRoleById(dto.getId(), dto.getRoleCode(), dto.getRoleName());
		
		if(this.globalConf.openCache())
			   cacheRole(role);

		return role;
	}
	@Override
	public boolean delRoles(Object... idArray) {
		
		Assert.assertNotNull(idArray);
		
		for(Object id : idArray){
			this.roleDmn.removeById(Long.valueOf(id.toString()));
		}
		return true;
	}
	
	@Override
	public boolean delRoles(List<Long> idArray) {
		
		for(Long id : idArray){
			this.roleDmn.removeById(id);
		}
		return true;
	}
	
	@Override
	public DcmRole getRoleByCode(String code) {
		
		DcmRole role = this.globalConf.openCache()? (DcmRole)this.redisCache.get(CacheKey.PREFIX_ROLE + code):null;
		if(role != null){
			this.logger.info(">>>getRoleByCode，缓存获取到角色实体，编码 [{}]", code);
			return role;
		}
		return this.roleDmn.getRoleByCode(code);
	}
	@Override
	public DcmRole getRoleByCode(long type, String code) {
		return this.roleDmn.findUniqueByProperties(new String[]{"roleCode", "roleType"}, new Object[]{code, type});
	}
	
	@Override
	public DcmRole getRoleById(Long id) {
		
		DcmRole role = this.globalConf.openCache()? (DcmRole) this.redisCache.get(CacheKey.PREFIX_ROLE + id):null;
		if(role != null){
			this.logger.info(">>>getRoleById，缓存获取到角色实体，id [{}]", id);
			return role;
		}
		return this.roleDmn.getRoleById(id);
	}
	
	@Override
	public boolean addRole(String roleCode, String roleName, Long roleType) {
		
		DcmRole role  = new DcmRole();
		role.setRoleCode(roleCode);
		role.setRoleName(roleName);
		role.setRoleType(roleType);
		
		this.roleDmn.add(role);
		
		return true;
	}
	
}
