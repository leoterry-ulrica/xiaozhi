package com.dist.bdf.manager.ecm.security;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.dist.bdf.common.conf.common.GlobalConf;
import com.dist.bdf.common.conf.ecm.ECMConf;
import com.dist.bdf.common.constants.GlobalSystemParameters;
import com.filenet.api.core.Connection;
import com.filenet.api.core.Domain;
import com.filenet.api.core.Factory;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.security.User;
import com.filenet.api.util.UserContext;
import com.ibm.casemgmt.api.CaseType;
import com.ibm.casemgmt.api.DeployedSolution;
import com.ibm.casemgmt.api.context.CaseMgmtContext;
import com.ibm.casemgmt.api.context.P8ConnectionCache;
import com.ibm.casemgmt.api.context.SimpleP8ConnectionCache;
import com.ibm.casemgmt.api.context.SimpleVWSessionCache;
import com.ibm.ecm.util.p8.P8Connection;

import javax.security.auth.Subject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 建立ECM连接的工具类
 *
 */
@Service
public class ConnectionService {

	private static Logger logger = LoggerFactory.getLogger(ConnectionService.class);

	private DeployedSolution solution;
	private UserContext userContext;
	private Locale preLocale;
	private CaseMgmtContext caseMgmtContext;
	private Connection ceConnection;
	private P8Connection p8Connection;
	private ObjectStore objectStore;
	private Domain domain;
	private static ConcurrentMap<String, P8Connection> p8connMap = new ConcurrentHashMap<String, P8Connection>();
	//private boolean hasInitialize = false;

	private static ECMConf ecmConf;
	@Autowired
	private void setEcmConf(ECMConf ecmConf){
		ConnectionService.ecmConf = ecmConf;
	}
	@Autowired
	private GlobalConf globalConf;
	
	private static volatile Connection singleConnection  = null;  // <<< 这里添加了 volatile 
	
	public static Connection getConnSingleInstance() { 
		
		Connection inst = singleConnection;  // <<< 在这里创建临时变量
	    if (inst == null) {                                                  // 1
	        synchronized (ConnectionService.class) {   // 2
	            inst = singleConnection;                                     // 3
	            if (inst == null) {   
	            	logger.info(">>>创建ce connection实例......");
	                inst = Factory.Connection.getConnection(ConnectionService.ecmConf.getEcmServerSite());           // 5
	                singleConnection = inst;                                      // 6
	            }
	        }
	    }else { 
	    	logger.info(">>>使用ce connection缓存实例......");
	    }
	    return inst;  // <<< 注意这里返回的是临时变量         // 7
	}

	
	private ConnectionService() {
	
	}

	/**
	 * 初始化
	 */
	public void initialize() {

		P8ConnectionCache connCache = new SimpleP8ConnectionCache();
		ceConnection = connCache.getP8Connection(ecmConf.getEcmServerSite());// Factory.Connection.getConnection(ECMConfig.getECMServerSite());
		domain = getCurrDomain();
		objectStore = getOS(domain, ecmConf.getEcmTargetObjectStoreName());
		

		Subject subject = UserContext.createSubject(ceConnection, ecmConf.getEcmUserName(),
				ecmConf.getEcmUserPassword(), ecmConf.getJaas());
		userContext = UserContext.get();
		userContext.pushSubject(subject);
		preLocale = userContext.getLocale();
		userContext.setLocale(preLocale);
		caseMgmtContext = new CaseMgmtContext(new SimpleVWSessionCache(), connCache);
		CaseMgmtContext.set(caseMgmtContext);

		Integer accessRights = objectStore.getAccessAllowed();
		User currUser = Factory.User.fetchCurrent(ceConnection, null);

		String distinguishedName = currUser.get_DistinguishedName();
		String userid = currUser.get_ShortName();
		p8Connection = new P8Connection(userid, distinguishedName, ceConnection, subject, domain, objectStore,
				currUser.get_Name(), currUser.get_DisplayName(), "", ecmConf.getEcmServerSite(), false, null);
		p8Connection.setPermissions(accessRights.intValue());

	}

	/**
	 * 释放资源
	 */
	public void release() {
		
		CaseMgmtContext.set(caseMgmtContext);
		userContext.setLocale(preLocale);
		userContext.popSubject();
	}

	/**
	 * 获取连接的ObjectStore
	 *
	 * @return 当前连接的ObjectStore
	 * @author HeShun
	 */
	public ObjectStore getOS(String userName, String password, String domainName, String objectStoreName) {
		return Factory.ObjectStore.fetchInstance(getCurrDomain(userName, password, domainName), objectStoreName, null);
	}

	public ObjectStore getOS(Domain domain,  String objectStoreName) {
		
		return Factory.ObjectStore.fetchInstance(domain, objectStoreName, null);
	}
	public Domain getCurrDomain() {
		Domain domain = Factory.Domain.fetchInstance(
				getCurrConnection(ecmConf.getEcmUserName(), ecmConf.getEcmUserPassword()),
				ecmConf.getEcmDomainName(), null);

		return domain;
	}

	/**
	 * 获取当前连接的Domain
	 *
	 * @return 当前连接的Domain
	 * @author HeShun
	 */
	public Domain getCurrDomain(String userName, String password, String domainName) {

		Domain domain = Factory.Domain.fetchInstance(getCurrConnection(userName, password), domainName, null);

		return domain;
	}

	/**
	 * 获取当前连接的Connection
	 *
	 * @return 当前连接的Connection
	 * @author HeShun
	 */
	public Connection getCurrConnection(String userName, String password) {

		String uri = ecmConf.getEcmServerSite();

		// 在开发调试模式下，需要设置以下参数。
		if (globalConf.isDebugMode()) {
			System.setProperty("java.security.auth.login.config", "jaas.conf.wsi");
			System.setProperty("java.naming.factory.initial", "com.ibm.websphere.naming.WsnInitialContextFactory");
			System.setProperty("filenet.pe.bootstrap.ceuri", uri);
		}
		// Make connection.
		Connection conn = Factory.Connection.getConnection(uri);
		Subject subject = UserContext.createSubject(conn, userName, password, null);
		UserContext.get().pushSubject(subject);
		return conn;
	}

	/**
	 * 获取当前使用的解决方案
	 * 
	 * @return
	 */
	public DeployedSolution getCurrSolution() {

		if (solution != null) {
			return solution;
		}
		try {
			initialize();
			List<DeployedSolution> solutions = DeployedSolution.fetchSolutions(ecmConf.getEcmServerSite(),
					new String[] { ecmConf.getEcmTargetObjectStoreName() });
			for (DeployedSolution s : solutions) {
				System.out.println(s.getPrefix() + " : " + s.getSolutionName());
				if (s.getPrefix().equalsIgnoreCase(ecmConf.getSolutionPrefix())) {
					solution = s;
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return solution;
	}

	/*
	 * 获取当前p8 链接
	 */
	public P8Connection getDefaultP8Connection() {

		return p8Connection;
	}

	/**
	 * 获取默认的TOS
	 * @param userName
	 * @param userPwd
	 * @return
	 */
	public P8Connection getP8Connection(String userName, String userPwd) {

		return this.getP8Connection(ecmConf.getEcmTargetObjectStoreName(), userName, userPwd);
	}
	
	/**
	 * 获取指定tos名字的连接，使用超管用户
	 * @param targetObjectStore
	 * @return
	 */
	public P8Connection getP8Connection(String targetObjectStore) {

		return this.getP8Connection(targetObjectStore, ecmConf.getEcmUserName(),
				ecmConf.getEcmUserPassword());
	}
	/**
	 * 不使用缓存
	 * @param targetObjectStore
	 * @return
	 */
	public P8Connection newP8Connection(String targetObjectStore) {

		Connection ceCnn = getConnSingleInstance();//Factory.Connection.getConnection(serverName);
		UserContext uc = UserContext.get();
		// 协议（protocol）：FileNetP8WSI
		Subject subject = UserContext.createSubject(ceCnn, ecmConf.getEcmUserName(),
				ecmConf.getEcmUserPassword(), "FileNetP8WSI");
		uc.pushSubject(subject);// 这是上下文安全性设置，否则出现安全性异常
		Domain domain = Factory.Domain.getInstance(ceCnn, ecmConf.getEcmDomainName());// 当目前只有一个域的时候，可以不填name的值

		ObjectStore objectStore = Factory.ObjectStore.fetchInstance(domain, targetObjectStore, null);
		System.out.println("Obtained realm：" + targetObjectStore);

		Integer accessRights = objectStore.getAccessAllowed();
		User user = Factory.User.fetchCurrent(ceCnn, null);
		logger.info(">>>当前连接用户："+user.get_DisplayName());
		String distinguishedName = user.get_DistinguishedName();
		String userid = user.get_ShortName();
		// 应用服务器类型
		//String appServerType = "websphere";

		P8Connection p8conn = new P8Connection(userid, distinguishedName, ceCnn, subject, domain, objectStore,
				user.get_Name(), user.get_DisplayName(), "", ConnectionService.ecmConf.getEcmServerSite(), false, null);
		p8conn.setPermissions(accessRights.intValue());
		
		P8ConnectionCache connCache = new SimpleP8ConnectionCache();
		connCache.getP8Connection(ecmConf.getEcmServerSite());
		caseMgmtContext = new CaseMgmtContext(new SimpleVWSessionCache(), connCache);
		CaseMgmtContext.set(caseMgmtContext);
		return p8conn;
	}

	public P8Connection getP8Connection(String targetObjectStore, String userId, String userPwd) {
		
		P8Connection p8conn = null;
		String key = GlobalSystemParameters.ECM_TOS+targetObjectStore+"_"+GlobalSystemParameters.ECM_USERID+userId;
		
		if(p8connMap.containsKey(key)){
			
			logger.info(">>>使用p8 connection缓存连接，id[{}]......", key);
			
			p8conn = p8connMap.get(key);
			UserContext uc = UserContext.get();
			Subject subject = p8conn.getSubject();
			uc.pushSubject(subject);
			
			return p8conn;
		}
		
		//String serverName = ecmConf.getEcmServerSite();
		Connection ceCnn = getConnSingleInstance();//Factory.Connection.getConnection(serverName);
		UserContext uc = UserContext.get();
		// 协议（protocol）：FileNetP8WSI
		Subject subject = UserContext.createSubject(ceCnn, userId, userPwd, "FileNetP8WSI");
		uc.pushSubject(subject);// 这是上下文安全性设置，否则出现安全性异常
		Domain domain = Factory.Domain.getInstance(ceCnn, ecmConf.getEcmDomainName());// 当目前只有一个域的时候，可以不填name的值

		ObjectStore objectStore = Factory.ObjectStore.fetchInstance(domain, targetObjectStore, null);
		System.out.println("Obtained realm：" + targetObjectStore);

		Integer accessRights = objectStore.getAccessAllowed();
		User user = Factory.User.fetchCurrent(ceCnn, null);
		logger.info(">>>当前连接用户："+user.get_DisplayName());
		String distinguishedName = user.get_DistinguishedName();
		String userid = user.get_ShortName();
		// 应用服务器类型
		//String appServerType = "websphere";

		p8conn = new P8Connection(userid, distinguishedName, ceCnn, subject, domain, objectStore,
				user.get_Name(), user.get_DisplayName(), "", ConnectionService.ecmConf.getEcmServerSite(), false, null);
		p8conn.setPermissions(accessRights.intValue());
		
		logger.info(">>>创建p8 connection连接，id[{}]，加入缓存......",key);
		p8connMap.put(key, p8conn);
		
		return p8conn;
	}
	
	/**
	 * 获取当前的链接
	 * 
	 * @return
	 */
	public Connection getDefaultCEConnection() {

		return getConnSingleInstance();//ceConnection;// getCurrConnection(ECMConfig.getECMServerName(),
							// ECMConfig.getECMServerPassword());
	}

	/**
	 * 获取当前对象存储库
	 * 
	 * @return
	 */
	public ObjectStore getDefaultOS() {
		// 每次对CE的请求都必须重新获取os，否则会抛出EngineRuntimeException : Access to the Content
		// Engine was not allowed
		// because the Content Engine API library or the Web Service Interface
		// (WSI) Listener could not find the
		// required security context information. No security context was found.
		return objectStore;// getCurrOS(ECMConfig.getECMServerName(),
							// ECMConfig.getECMServerPassword());

	}
	
	public static void main(String[] args) {

		ConnectionService cs = new ConnectionService();
		DeployedSolution sol = cs.getCurrSolution();
		List<CaseType> casetypes = sol.getCaseTypes();
		for (CaseType ct : casetypes) {
			System.out.println(ct.getName());
		}

	}
}
