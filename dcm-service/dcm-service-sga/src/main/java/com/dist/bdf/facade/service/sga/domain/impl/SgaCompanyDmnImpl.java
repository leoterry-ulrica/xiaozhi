package com.dist.bdf.facade.service.sga.domain.impl;


import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dist.bdf.base.dao.GenericDAO;
import com.dist.bdf.base.domain.GenericDmnImpl;
import com.dist.bdf.common.constants.UserStatus;
import com.dist.bdf.facade.service.sga.dao.SgaComDetailDAO;
import com.dist.bdf.facade.service.sga.dao.SgaCompanyDAO;
import com.dist.bdf.facade.service.sga.dao.SgaComAuditDAO;
import com.dist.bdf.facade.service.sga.dao.SgaComUserDAO;
import com.dist.bdf.facade.service.sga.dao.SgaPrjUserDAO;
import com.dist.bdf.facade.service.sga.dao.SgaProjectDAO;
import com.dist.bdf.facade.service.sga.domain.SgaCompanyDmn;
import com.dist.bdf.model.entity.sga.SgaComDetail;
import com.dist.bdf.model.entity.sga.SgaCompany;
import com.dist.bdf.model.entity.sga.SgaComAudit;
import com.dist.bdf.model.entity.sga.SgaComUser;
import com.dist.bdf.model.entity.sga.SgaProject;

/**
 * 企业领域
 * @author weifj
 *
 */
@Service
@Transactional(propagation = Propagation.REQUIRED) 
public class SgaCompanyDmnImpl extends GenericDmnImpl<SgaCompany, Long> implements SgaCompanyDmn {

	@Autowired
	private SgaCompanyDAO comInfoDAO;
	@Autowired
	private SgaComDetailDAO comDetailDAO;
	@Autowired
	private SgaComUserDAO comUserDAO;
	@Autowired
	private SgaComAuditDAO comStatDAO;
	@Autowired
	private SgaProjectDAO projectDAO;
	@Autowired
	private SgaPrjUserDAO projectUserDAO;
	
	private ReadWriteLock lock = new ReentrantReadWriteLock();
	
	@Override
	public GenericDAO<SgaCompany, Long> getDao() {
		
		return comInfoDAO;
	}

	@Override
	public Object addComUser(SgaComUser cu) {
		this.comUserDAO.save(cu);
		return cu;
	}
	@Override
	public Object addOrUpdateComDesc(Long comId, String desc) {
		
		SgaComDetail cd = this.comDetailDAO.findUniqueByProperty("cid", comId);
		if(null == cd){
			cd = new SgaComDetail();
			cd.setCid(comId);
		}
		
		cd.setDescription(desc);
		this.comDetailDAO.saveOrUpdate(cd);
		
		return cd;
	
	}

	@Override
	public Object getComDesc(Long comId) {

		return this.comDetailDAO.findUniqueByProperty("cid", comId);
	}

	@Override
	public SgaComUser getComUser(Long comId, Long uid) {
		
		return this.comUserDAO.findUniqueByProperties(new String[]{"cid", "userId"}, new Object[]{comId, uid});
	}
	@Override
	public SgaComUser modify(SgaComUser cu) {
		
		return this.comUserDAO.update(cu);
	}
	@Override
	public SgaCompany getComByRealm(String realmName) {
		
		return this.comInfoDAO.findUniqueByProperty("realm", realmName);
	}
	@Override
	public SgaComAudit getStatByComId(Long comId) {
		
		return this.comStatDAO.findUniqueByProperty("cid", comId);
	}

	/*@Override
	public int operateComStatProjectCount(boolean isAdd, Long comId) {

		Lock writeLock = lock.writeLock();

		try {
			writeLock.lock();
			SgaComStatistics cs = this.comStatDAO.findUniqueByProperty("cid", comId);
			if (null == cs) {
				cs = new SgaComStatistics();
				cs.setCid(comId);
			}
			cs.setProjectCount(
					isAdd ? cs.getProjectCount() + 1 : (0 == cs.getProjectCount()) ? 0 : cs.getProjectCount() - 1);

			this.comStatDAO.saveOrUpdate(cs);

			return cs.getProjectCount();
		} catch (Exception ex) {
			logger.error(ex.getMessage());
		} finally {
			writeLock.unlock();
		}
		return 0;
	}*/

	@Override
	public SgaComAudit syncComAuditInfo(Long companyId) {

		Lock writeLock = lock.writeLock();
		try {
			writeLock.lock();
			SgaComAudit cs = this.comStatDAO.findUniqueByProperty("cid", companyId);
			if (null == cs) {
				cs = new SgaComAudit();
				cs.setCid(companyId);
			}
			List<SgaComUser> comUsers = this.comUserDAO.findByProperty("cid", companyId);
			if(null == comUsers || comUsers.isEmpty()) {
				cs.setRegisterCount(0);
			} else {
				cs.setRegisterCount(comUsers.size());
				AtomicInteger joininCounter = new AtomicInteger(0);
				for(SgaComUser com2user : comUsers) {
					if(UserStatus.PROJECT_JOININ == com2user.getStatus()){
						joininCounter.incrementAndGet();
					}
				}
				cs.setJoininCount(joininCounter.get());
			}
			List<SgaProject> projects = this.projectDAO.findByProperty("cid", companyId);
			if(null == projects || projects.isEmpty()) {
				logger.info(">>>没有找到与企业[id:{}]相关的项目信息。", companyId);
				cs.setProjectCount(0);
			} else {
				logger.info(">>>找到企业[id:{}] {} 个项目信息。", companyId, projects.size());
				cs.setProjectCount(projects.size());
			}
			return this.comStatDAO.saveOrUpdate(cs);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
		} finally {
			writeLock.unlock();
		}
		return null;
	}
	
	/*@Override
	public int operateComStatRegisterCount(boolean isAdd, Long comId) {

		Lock writeLock = lock.writeLock();
		try {
			writeLock.lock();
			SgaComStatistics cs = this.comStatDAO.findUniqueByProperty("cid", comId);
			if (null == cs) {
				cs = new SgaComStatistics();
				cs.setCid(comId);
			}
			cs.setRegisterCount(
					isAdd ? cs.getRegisterCount() + 1 : (0 == cs.getRegisterCount()) ? 0 : cs.getRegisterCount() - 1);

			this.comStatDAO.saveOrUpdate(cs);

			return cs.getRegisterCount();
			
		} catch (Exception ex) {
			logger.error(ex.getMessage());
		} finally {
			writeLock.unlock();
		}
		return 0;
	}*/

	/*@Override
	public int operateComStatJoininCount(boolean isAdd, Long comId) {

		Lock writeLock = lock.writeLock();
		try {
			writeLock.lock();
			SgaComStatistics cs = this.comStatDAO.findUniqueByProperty("cid", comId);
			if (null == cs) {
				cs = new SgaComStatistics();
				cs.setCid(comId);
			}
			cs.setJoininCount(
					isAdd ? cs.getJoininCount() + 1 : (0 == cs.getJoininCount()) ? 0 : cs.getJoininCount() - 1);

			this.comStatDAO.saveOrUpdate(cs);

			return cs.getJoininCount();
		} catch (Exception ex) {
			logger.error(ex.getMessage());
		} finally {
			writeLock.unlock();
		}
		return 0;
	}*/
	@Override
	public boolean existUser(Long companyId, Long userId) {
		
		SgaComUser com2user = this.comUserDAO.findUniqueByProperties(new String[]{"cid","userId"}, new Object[]{companyId, userId});
		
		return null != com2user;
	}
	@Override
	public void saveOrUpdate(SgaComUser comUser) {
		this.comUserDAO.saveOrUpdate(comUser);
	}
	@Override
	public List<SgaComUser> getComUserRef(Long cid) {
		
		return this.comUserDAO.findByProperty("cid", cid);
	}
	@Override
	public List<SgaComUser> getComRefByUserId(Long userId) {
		return this.comUserDAO.findByProperty("userId", userId);
	}
}
