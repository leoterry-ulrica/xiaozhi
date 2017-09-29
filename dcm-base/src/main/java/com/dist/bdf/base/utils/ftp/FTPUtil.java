package com.dist.bdf.base.utils.ftp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

/**
 * FTP 工具类. 可以完成对目录创建的创建、修改、删除,对文件的上传下载等操作.
 * @author weifj
 * @since 1.4
 */

public class FTPUtil {
	
	/**
	 * FTP客户端
	 */
	private FTPClient ftp;
	public static final int BINARY_FILE_TYPE = FTP.BINARY_FILE_TYPE;
	public static final int ASCII_FILE_TYPE = FTP.ASCII_FILE_TYPE;

	/**
	 * 初始化客户端并完成对服务端的连接
	 * @param ftpConfig ftp配置类
	 * @throws SocketException
	 * @throws IOException
	 */
	public void connectServer(FtpConfig ftpConfig) throws SocketException,
			IOException {
		String server = ftpConfig.getServer();
		int port = ftpConfig.getPort();
		String user = ftpConfig.getUserName();
		String password = ftpConfig.getUserPwd();
		String location = ftpConfig.getServerPath();
		connectServer(server, port, user, password, location);
	}
	
	/**
	 * 初始化客户端并完成对服务端的连接
	 * @param server 服务端地址
	 * @param port 端口号
	 * @param username 用户名
	 * @param password 密码
	 * @param path 远程路径 值可以为空
	 * @throws SocketException
	 * @throws IOException
	 */
	public void connectServer(String server, int port, String username,
			String password, String path)  {
		if(null == path){
			path = "";
		}
		try {
			ftp = new FTPClient();
			//下面四行代码必须要，而且不能改变编码格式，否则不能正确下载中文文件
			// 如果使用serv-u发布ftp站点，则需要勾掉“高级选项”中的“对所有已收发的路径和文件名使用UTF-8编码”
			ftp.setControlEncoding("GBK");
			FTPClientConfig conf = new FTPClientConfig(FTPClientConfig.SYST_NT);
			conf.setServerLanguageCode("zh");
			ftp.configure(conf);
			ftp.connect(server, port);
			// 设置被动模式
			ftp.enterLocalPassiveMode();
			ftp.setDataTimeout(120000);

			if (!FTPReply.isPositiveCompletion(ftp.getReplyCode())) {
				ftp.disconnect();
				System.out.println(server + " 拒绝连接");
			}
			boolean flag = ftp.login(username, password);
			if (flag) {
				System.out.println("FTP登录成功。");
			} else {
				System.out.println("FTP登录失败。");
			}
			System.out.println(ftp.printWorkingDirectory());

			if (path.length() != 0) {
				//String str = new String(path.getBytes("GBK"),FTP.DEFAULT_CONTROL_ENCODING);
			    if (path != null && !"".equals(path.trim())) {
	                String[] pathes = path.split("/");
	                for (String onepath : pathes) {
	                    if (onepath == null || "".equals(onepath.trim())) {
	                        continue;
	                    }
	                    //onepath=new String(onepath.getBytes("GBK"), FTP.DEFAULT_CONTROL_ENCODING);                    
	                    if (!ftp.changeWorkingDirectory(onepath)) {
	                    	 ftp.makeDirectory(onepath);
	                    	boolean flagDir = ftp.changeWorkingDirectory(onepath);
	                    	System.out.println(ftp.printWorkingDirectory());
	                    	if (flagDir) {
	        					System.out.println("成功连接ftp目录：" + path);
	        				} else {
	        					System.out.println("未能连接ftp目录：" + path);
	        				}
	                    }
	                }
	            }
			}
			System.out.println(ftp.printWorkingDirectory());
		}catch(SocketException e){
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 设置ftp的文件传输类型
	 * @param fileType 如:FTP.BINARY_FILE_TYPE
	 * @throws IOException
	 */
	public void setFileType(int fileType) throws IOException {
		ftp.setFileType(fileType);
	}
	
	/**
	 * 关闭ftp连接
	 * @throws IOException
	 */
	public void closeServer() throws IOException {
		if (ftp != null && ftp.isConnected()) {
			ftp.logout();
			ftp.disconnect();
		}
	}
	
	/**
	 * 改变ftp的工作目录
	 * @param path
	 * @return
	 * @throws IOException
	 */
	public boolean changeDirectory(String path) throws IOException {
		return ftp.changeWorkingDirectory(path);
	}
	
	/**
	 * 在服务端创建目录
	 * @param pathName 可以是相对目录或绝对目录
	 * @return
	 * @throws IOException
	 */
	public boolean createDirectory(String pathName) throws IOException {
		return ftp.makeDirectory(pathName);
	}
	
	/**
	 * 删除一个FTP服务器上的目录（如果为空）
	 * @param path 目录路径
	 * @return
	 * @throws IOException
	 */
	public boolean removeDirectory(String path) throws IOException {
		return ftp.removeDirectory(path);
	}

	/**
	 * 删除一个FTP服务器上的目录
	 * @param path 目录路径
	 * @param isAll 是否删除所有子目录和文件,如果有
	 * @return
	 * @throws IOException
	 */
	public boolean removeDirectory(String path, boolean isAll)
			throws IOException {

		if (!isAll) {
			return removeDirectory(path);
		}
		//遍历子目录和文件
		FTPFile[] ftpFileArr = ftp.listFiles(path);
		if (ftpFileArr == null || ftpFileArr.length == 0) {
			return removeDirectory(path);
		}

		for (int i = 0; i < ftpFileArr.length; i++) {
			FTPFile ftpFile = ftpFileArr[i];
			String name = ftpFile.getName();
			if (ftpFile.isDirectory()) {
				removeDirectory(path + "/" + name, true);
			} else if (ftpFile.isFile()) {
				deleteFile(path + "/" + name);
			} else if (ftpFile.isSymbolicLink()) {
				
			} else if (ftpFile.isUnknown()) {
				
			}
		}
		return removeDirectory(path);
	}
	
	/**
	 * 返回给定目录下的文件
	 * @param path
	 * @return FTPFile组成的集合
	 * @throws IOException
	 */
	public List<String> getFileList(String path) throws IOException {

		FTPFile[] ftpFiles = ftp.listFiles(path);

		List<String> retList = new ArrayList<String>();
		if (ftpFiles == null || ftpFiles.length == 0) {
			return retList;
		}
		for (int i = 0; i < ftpFiles.length; i++) {
			FTPFile ftpFile = ftpFiles[i];
			if (ftpFile.isFile()) {
				retList.add(ftpFile.getName());
			}
		}
		return retList;
	}
	/**
	 * 递归遍历文件夹以及子文件夹的文件
	 * @param pathName 
	 * @param filePathList
	 * @throws IOException
	 */
	public void getFileListRecursion(String pathName, List<String> filePathList) throws IOException {
		
		if(null == pathName){
			pathName = "/";
		}else{
			pathName = pathName.replace("\\", "/");
		}
		if(!pathName.startsWith("/")){
			pathName ="/"+pathName;
		}
		if(!pathName.endsWith("/")){
			pathName = pathName + "/";
		}
		
		ftp.changeWorkingDirectory(pathName);
		FTPFile[] files = ftp.listFiles();
		for (int i = 0; i < files.length; i++) {
			
			if (files[i].isFile()) {
				System.out.println("得到文件:" + files[i].getName());
				filePathList.add(ftp.printWorkingDirectory()+"/"+files[i].getName());
			} else if (files[i].isDirectory()) {
				getFileListRecursion(pathName + files[i].getName() + "/", filePathList);
			}
		}
	}
	
	/**
	 * 删除文件
	 * @param pathName 文件名
	 * @return 删除结果,是否成功.
	 * @throws IOException
	 */
	public boolean deleteFile(String pathName) throws IOException {
		return ftp.deleteFile(pathName);
	}
	
	/**
	 * 上传文件,并重命名.
	 * @param filePathName 上传的文件,包含目录的文件名
	 * @param newName 新的文件名
	 * @return 上传结果,是否成功.
	 * @throws IOException
	 */
	public boolean uploadFile(String filePathName, String newName)
			throws IOException {
		boolean flag = false;
		InputStream iStream = null;
		try {
			System.out.println("ftp new file name : "+newName);
			ftp.setControlEncoding("GBK");
			iStream = new FileInputStream(filePathName);
			ftp.setFileType(FTPClient.BINARY_FILE_TYPE);  
			ftp.enterLocalPassiveMode();
			ftp.storeFile(new String(filePathName.getBytes("GBK"),"iso-8859-1"),iStream);
			flag = ftp.storeFile(newName, iStream);
		} catch (IOException e) {
			flag = false;
			return flag;
		} finally {
			if (iStream != null) {
				iStream.close();
			}
		}
		return flag;
	}
	
	/**
	 * 上传文件
	 * @param fileName 上传的文件,包含目录的文件名
	 * @return 上传结果,是否成功.
	 * @throws IOException
	 */
	public boolean uploadFile(String filePathName) throws IOException {
		
		System.out.println("传入的文件："+filePathName);
		
		return uploadFile(filePathName, new File(filePathName).getName());
	}
	
	/**
	 * 上传文件,从InputStream
	 * @param iStream 文件流
	 * @param newName 新的文件名
	 * @return 上传结果,是否成功.
	 * @throws IOException
	 */
	public boolean uploadFile(InputStream iStream, String newName)
			throws IOException {
		boolean flag = false;
		try {
			ftp.setFileType(BINARY_FILE_TYPE);  
			flag = ftp.storeFile(newName, iStream);
		} catch (IOException e) {
			flag = false;
			return flag;
		} finally {
			if (iStream != null) {
				iStream.close();
			}
		}
		return flag;
	}
	
	/**
	 * 下载文件
	 * @param remoteFileName 远程文件名，路径开头不能以“/”或者“\\”开始
	 * @param localFileName 本地文件
	 * @return 返回操作结果
	 * @throws IOException
	 */
	public boolean download(String remoteFileName, String localFileName)
			throws IOException {

	/*		if(!remoteFileName.startsWith("/")){
			remoteFileName = "/"+remoteFileName;
		}*/
		/*remoteFileName = remoteFileName.replace("\\", "/");
		System.out.println(ftp.printWorkingDirectory());
	
		String dir = remoteFileName.substring(0, remoteFileName.lastIndexOf("/"));
	
		String[] dirs = dir.split("/");
		for(String temp : dirs){
			ftp.changeWorkingDirectory(temp);
		}
		String[] files = ftp.listNames();
		remoteFileName = remoteFileName.substring(remoteFileName.lastIndexOf("/")+1);*/
		boolean flag = false;
		File outfile = new File(localFileName);
		OutputStream oStream = null;
		try {
			oStream = new FileOutputStream(outfile);
			ftp.setControlEncoding("GBK");
			ftp.setFileType(FTPClient.BINARY_FILE_TYPE);  
			//remoteFileName = new String(remoteFileName.getBytes("gbk"),"iso-8859-1");

			flag = ftp.retrieveFile(remoteFileName, oStream);
			if(flag){
				System.out.println("成功下载文件："+localFileName);
			}else
			{
				System.out.println("下载文件失败。文件："+remoteFileName);
			}
		} catch (IOException e) {
			e.printStackTrace();
			flag = false;
			return flag;
		} finally {
			oStream.close();
		}
		return flag;
	}
	
	/**
	 * 下载文件,返回InputStream
	 * @param sourceFileName 远程文件
	 * @return InputStream
	 * @throws IOException
	 */
	public InputStream downFile(String sourceFileName) throws IOException {
		return ftp.retrieveFileStream(sourceFileName);
	}

	public FTPClient getFtp() {
		return ftp;
	}

	public void setFtp(FTPClient ftp) {
		this.ftp = ftp;
	}
	
	/**
	 * 获取ftp子目录信息
	 * @param ftp ftp客户端
	 * @param relativeDir 相对目录
	 * @param level 级别，level=0表示当前目录以及所有子目录内容；level=1表示获取一级目录内容
	 * @throws IOException
	 */
	public List<FTPDir> getSubDirectory(FTPClient ftp, String relativeDir, int level) throws IOException {
		
		List<FTPDir> filePathList = new ArrayList<>();
		if(null == relativeDir){
			relativeDir = "/";
		}else{
			relativeDir = relativeDir.replace("\\", "/");
		}
		if(!relativeDir.startsWith("/")) {
			relativeDir ="/"+relativeDir;
		}
		if(!relativeDir.endsWith("/")) {
			relativeDir = relativeDir + "/";
		}
		boolean flag = ftp.changeWorkingDirectory(relativeDir);
		if (!flag) {
			return filePathList;
		}
		FTPFile[] files = ftp.listFiles();
		if (files == null || files.length == 0) {
			ftp.changeToParentDirectory();
			return filePathList;
		}
		for (int i = 0; i < files.length; i++) {
			FTPDir vo = new FTPDir();
			if (files[i].isFile()) {
				vo.setType("file");
				vo.setFullPath(relativeDir+files[i].getName());
				vo.setLabel(files[i].getName());
				vo.setExtension(files[i].getName().substring(files[i].getName().lastIndexOf(".")+1));
				vo.setChildren(null);
				filePathList.add(vo);
				
			} else if (files[i].isDirectory()) {
				vo.setType("folder");
				vo.setFullPath(relativeDir+files[i].getName());
				vo.setLabel(files[i].getName());
				if(0 == level) {
					vo.setChildren(getSubDirectory(ftp, relativeDir + files[i].getName() + "/", level));
				}
				filePathList.add(vo);
			}
		}
		return filePathList;
	}
	/**
	 * 获取ftp子目录信息
	 * @param relativeDir 相对目录
	 * @param level 级别，level=0表示当前目录以及所有子目录内容；level=1表示获取一级目录内容
	 * @throws IOException
	 */
	public List<FTPDir> getSubDirectory(String relativeDir, int level) throws IOException {
		
		return this.getSubDirectory(ftp, relativeDir, level);
	}
}