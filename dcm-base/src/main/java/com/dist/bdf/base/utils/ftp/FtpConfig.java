package com.dist.bdf.base.utils.ftp;

import java.io.Serializable;

/**
 * 
    * @ClassName: FtpCfg
    * @Description: ftp配置
    * @author weifj
    * @date 2017年6月30日
    *
 */
public class FtpConfig implements Cloneable, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 服务器
	 */
	private String server;
	/**
	 * 端口
	 */
	private int port;
	/**
	 * 用户名
	 */
	private String userName;
	/**
	 * 用户密码
	 */
	private String userPwd;
	/**
	 * 下载路径，相对路径，相对appHome
	 */
	private String downloadPath;
	/**
	 * ftp服务器路径
	 */
	private String serverPath;
	/**
	 * 配置应用程序部署的根目录
	 */
	private String appHome;
	
	public FtpConfig(){
		
	}
	public FtpConfig(String server, int port, String userName, String userPwd, String downloadPath, String serverPath){
		
		this.server = server;
		this.port = port;
		this.userName = userName;
		this.userPwd = userPwd;
		this.downloadPath = downloadPath;
		this.serverPath = serverPath;
		
	}
	public String getAppHome() {
		return appHome;
	}
	public void setAppHome(String appHome) {
		this.appHome = appHome;
	}
	
	public String getServerPath() {
		return serverPath;
	}
	public void setServerPath(String severPath) {
		this.serverPath = severPath;
	}
	public String getDownloadPath() {
		return downloadPath;
	}
	public void setDownloadPath(String downloadPath) {
		this.downloadPath = downloadPath;
	}
	
	public String getServer() {
		return server;
	}
	public void setServer(String server) {
		this.server = server;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserPwd() {
		return userPwd;
	}
	public void setUserPwd(String userPwd) {
		this.userPwd = userPwd;
	}
	
    public Object clone()    
    {    
        Object o=null;    
       try    
        {    
        o=(FtpConfig)super.clone();//Object 中的clone()识别出你要复制的是哪一个对象。    
        }    
       catch(CloneNotSupportedException e)    
        {    
            System.out.println(e.toString());    
        }    
       return o;    
    }  
}
