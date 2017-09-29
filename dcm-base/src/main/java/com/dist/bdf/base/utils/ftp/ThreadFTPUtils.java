package com.dist.bdf.base.utils.ftp;
import java.io.File;  
import java.io.FileInputStream;  
import java.io.FileOutputStream;  
import java.io.IOException;  
import java.io.InputStream;  
import java.io.OutputStream;  
import java.io.PrintWriter;  
import java.net.SocketException;  
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
  
import javax.servlet.http.HttpServletResponse;  
  
import org.apache.commons.net.PrintCommandListener;  
import org.apache.commons.net.ftp.FTP;  
import org.apache.commons.net.ftp.FTPClient;  
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;  
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;  

public class ThreadFTPUtils  implements Runnable  
{  
	/** 本地字符编码 */
	private static String LOCAL_CHARSET = "GBK";
	// FTP协议里面，规定文件名编码为iso-8859-1
	// private static String SERVER_CHARSET = "ISO-8859-1";
	private static Logger LOG = LoggerFactory.getLogger(ThreadFTPUtils.class);
	
    private FtpConfig ftpConfig;
      
    private FTPClient ftpClient = new FTPClient();  
      
    private FTPType ftpType;  
    
    private String local;   //本地文件或文件名  
    
    private String remote;  //远程文件或路径
      
    public FTPType getFtpType()  
    {  
        return ftpType;  
    }  
  
    public void setFtpType(FTPType ftpType)  
    {  
        this.ftpType = ftpType;  
    }  
  
    public static enum FTPType{  
          
        UPLOAD(0),DOWNLOAD(1),RENAME(2),DELETE(3);  
          
        private int type;  
          
        public int getType()  
        {  
            return type;  
        }  
        public void setType(int type)  
        {  
            this.type = type;  
        }  
        FTPType(int type){  
            this.type=type;  
        }  
    }  
  
  
    /** 
     * 对象构造 设置将过程中使用到的命令输出到控制台 
     */  
    public ThreadFTPUtils(String ip,int port,String username,String password,String local,String remote,FTPType ftpType)  
    {  
    	ftpConfig=new FtpConfig(ip, port, username, password,"",""); 
    	this.local = local;
    	this.remote = remote;
        this.ftpType=ftpType;  
        // 支持中文
        this.ftpClient.setControlEncoding("GBK");
    	FTPClientConfig conf = new FTPClientConfig(FTPClientConfig.SYST_NT);
		conf.setServerLanguageCode("zh");
		this.ftpClient.configure(conf);
        this.ftpClient.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));  
    }  
  
    public ThreadFTPUtils(FtpConfig ftpConfig, String local,String remote,FTPType ftpType)  
    {  
    	this.ftpConfig= ftpConfig;
    	this.local = local;
    	this.remote = remote;
        this.ftpType=ftpType;  
        // 支持中文
        this.ftpClient.setControlEncoding("GBK");
    	FTPClientConfig conf = new FTPClientConfig(FTPClientConfig.SYST_NT);
		conf.setServerLanguageCode("zh");
		this.ftpClient.configure(conf);
        this.ftpClient.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));  
    }  
    public ThreadFTPUtils(FtpConfig ftpConfig)  
    {  
    	this.ftpConfig= ftpConfig;
    
        // 支持中文
        this.ftpClient.setControlEncoding("GBK");
    	FTPClientConfig conf = new FTPClientConfig(FTPClientConfig.SYST_NT);
		conf.setServerLanguageCode("zh");
		this.ftpClient.configure(conf);
        this.ftpClient.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));  
    }  
    /** 
     *  
     * java编程中用于连接到FTP服务器 
     *  
     * @param hostname 
     *            主机名 
     *  
     * @param port 
     *            端口 
     *  
     * @param username 
     *            用户名 
     *  
     * @param password 
     *            密码 
     *  
     * @param serverPath 
     *            服务路径
     *  
     * @return 是否连接成功 
     *  
     * @throws IOException 
     */  
  
    public boolean connect(String hostname, int port, String username, String password, String serverPath) throws IOException  
    {  
    	if(null == serverPath){
    		serverPath = "";
		}
    	serverPath = serverPath.trim();
    	serverPath = serverPath.replace("\\", "/");

        ftpClient.connect(hostname, port);  
        if (FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
        	
        if (ftpClient.login(username, password))  
        {  
        	   if (FTPReply.isPositiveCompletion(ftpClient.sendCommand("OPTS UTF8", "ON")))  
               {  
               	// 开启服务器对UTF-8的支持，如果服务器支持就用UTF-8编码，否则就使用本地编码（GBK）.
               	LOCAL_CHARSET = "UTF-8";	
               }  
               ftpClient.setControlEncoding(LOCAL_CHARSET);
               
        	if (serverPath.length() != 0) {
        		   String[] pathes = serverPath.split("/");
	                for (String onepath : pathes) {
	                    if (onepath == null || "".equals(onepath.trim())) {
	                        continue;
	                    }
                
	                    if (!this.ftpClient.changeWorkingDirectory(onepath)) {
	                    	this.ftpClient.makeDirectory(onepath);
	                    	boolean flagDir = this.ftpClient.changeWorkingDirectory(onepath);
	                    	System.out.println(this.ftpClient.printWorkingDirectory());
	                    	if (flagDir) {
	        					System.out.println("成功连接ftp目录：" + serverPath);
	        				} else {
	        					System.out.println("未能连接ftp目录：" + serverPath);
	        					return false;
	        				}
	                    }
	                }
			}
        }
            return true;  
        }  
        disconnect();  
        return false;  
    }  
  
    /** 
     * 删除远程FTP文件 
     *  
     * @param remote 
     *            远程文件路径 
     * @return 
     * @throws IOException 
     */  
    public FTPStatus delete(String remote) throws IOException  
    {  
        ftpClient.enterLocalPassiveMode();  
  
        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);  
  
        FTPStatus result = null;  
  
        FTPFile[] files = ftpClient.listFiles(remote);  
        if (files.length == 1)  
        {  
            boolean status = ftpClient.deleteFile(remote);  
            result = status ? FTPStatus.Delete_Remote_Success : FTPStatus.Delete_Remote_Faild;  
        }  
        else  
        {  
            result = FTPStatus.Not_Exist_File;  
        }  
        LOG.info("FTP服务器文件删除标识："+result);  
        return result;  
    }  
      
    /** 
     * 重命名远程FTP文件 
     *  
     * @param name 
     *            新远程文件名称(路径-必须保证在同一路径下) 
     *             
     * @param remote 
     *            远程文件路径 
     *             
     * @return  是否成功 
     *  
     * @throws IOException 
     */  
    public FTPStatus rename(String name,String remote) throws IOException  
    {  
        ftpClient.enterLocalPassiveMode();  
  
        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);  
  
        FTPStatus result = null;  
  
        FTPFile[] files = ftpClient.listFiles(remote);  
        if (files.length == 1)  
        {  
            boolean status = ftpClient.rename(remote, name);  
            result = status ? FTPStatus.Remote_Rename_Success : FTPStatus.Remote_Rename_Faild;  
        }  
        else  
        {  
            result = FTPStatus.Not_Exist_File;  
        }  
        LOG.info("FTP服务器文件名更新标识："+result);  
        return result;  
    }  
      
    /** 
     *  
     * 从FTP服务器上下载文件 
     *  
     * @param fileName 
     *            下载文件的名字(包括后缀名) 
     *  
     * @param remote 
     *            远程文件路径 
     *  
     * @param local 
     *            本地文件路径 
     *  
     * @return 是否成功 
     *  
     * @throws IOException 
     */  
  
    public FTPStatus download(String fileName,String remote,HttpServletResponse response) throws IOException  
    {  
        // 开启输出流弹出文件保存路径选择窗口  
        response.setContentType("application/octet-stream");  
          
        response.setContentType("application/OCTET-STREAM;charset=UTF-8");  
          
        response.setHeader("Content-Disposition", "attachment;filename=" +fileName);  
  
        ftpClient.enterLocalPassiveMode();  
  
        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);  
          
        FTPStatus result;  
          
        OutputStream out = response.getOutputStream();  
          
        boolean status = ftpClient.retrieveFile(remote, out);  
          
        result=status?FTPStatus.Download_From_Break_Success:FTPStatus.Download_From_Break_Faild;  
          
        LOG.info("FTP服务器文件下载标识："+result);  
          
        out.close();  
          
        return result;  
    }  
  
    /** 
     *  
     * 从FTP服务器上下载文件 
     *  
     * @param remote 
     *            远程文件路径 
     *  
     * @param local 
     *            本地文件路径 
     *  
     * @return 是否成功 
     *  
     * @throws IOException 
     */  
    public FTPStatus download(String remote, String local) throws IOException  
    {  
/*    	if(remote.startsWith("/")){
    		// 去掉前面的“/”
    		remote = remote.substring(1);
    	}*/
        ftpClient.enterLocalPassiveMode();  
  
        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);  
  
        FTPStatus result;  
  
        File f = new File(local); 
        // 目录不存在，则创建。
        String newLocal = local.replace("\\", "/");
        String dir = local.substring(0,newLocal.lastIndexOf("/"));
        File dirFile = new File(dir);
        if(!dirFile.exists()){
        	dirFile.mkdirs();
        }

        System.out.println("当前ftp工作目录："+ftpClient.printWorkingDirectory());
        FTPFile[] files = ftpClient.listFiles(remote);  

        if (files.length > 1)  
        {  
        	LOG.info("远程文件不唯一");  
            return FTPStatus.File_Not_Unique;  
        }else if(0 == files.length){
        	LOG.info("远程文件不存在");  
              return FTPStatus.Not_Exist_File;  
        }
        if (f.exists())  
        {  
            OutputStream out = new FileOutputStream(f, true);  
            LOG.info("本地文件大小为:" + f.length());  
            Long remoteFileLastModifiedTime = files[0].getTimestamp().getTimeInMillis();
            Long localFileLastModifiedTime = f.lastModified();
            
            if(remoteFileLastModifiedTime > localFileLastModifiedTime){
            	   // 下载最新文件
            	   boolean status = ftpClient.retrieveFile(remote, out);  
                   result=status?FTPStatus.Download_From_Break_Success:FTPStatus.Download_From_Break_Faild;  
                   out.close();  
                   
            }else{
            	// 否则不再重复下载
            	 result= FTPStatus.Download_From_Break_Success;
            }
  
        } else  
        {  
            OutputStream out = new FileOutputStream(f);  
            boolean status = ftpClient.retrieveFile(remote, out);  
            result=status?FTPStatus.Download_From_Break_Success:FTPStatus.Download_From_Break_Faild;  
            out.close();  
        }  
  
        return result;  
  
    }  
  
    /** 
     *  
     * 上传文件到FTP服务器，支持断点续传 
     *  
     * @param local 
     *            本地文件名称，绝对路径 
     *  
     * @param remote 
     *            远程文件路径，使用/home/directory1/subdirectory/file.ext 
     *            按照Linux上的路径指定方式，支持多级目录嵌套，支持递归创建不存在的目录结构 
     *  
     * @return 上传结果 
     *  
     * @throws IOException 
     */  
  
    @SuppressWarnings("resource")  
    public FTPStatus upload(String local, String remote) throws IOException  
    {  
        // 设置PassiveMode传输  
        ftpClient.enterLocalPassiveMode();  
  
        // 设置以二进制流的方式传输  
        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);  
  
        FTPStatus result;  
  
        // 对远程目录的处理  
        String remoteFileName = remote;  
  
        if (remote.contains("/"))  
        {  
  
            remoteFileName = remote.substring(remote.lastIndexOf("/") + 1);  
  
            String directory = remote.substring(0, remote.lastIndexOf("/") + 1);  
  
            if (!directory.equalsIgnoreCase("/") && !ftpClient.changeWorkingDirectory(directory))  
            {  
  
                // 如果远程目录不存在，则递归创建远程服务器目录  
  
                int start = 0;  
  
                int end = 0;  
  
                if (directory.startsWith("/"))  
                {  
  
                    start = 1;  
  
                } else  
                {  
  
                    start = 0;  
  
                }  
  
                end = directory.indexOf("/", start);  
  
                while (true)  
                {  
  
                    String subDirectory = remote.substring(start, end);  
  
                    if (!ftpClient.changeWorkingDirectory(subDirectory))  
                    {  
  
                        if (ftpClient.makeDirectory(subDirectory))  
                        {  
  
                            ftpClient.changeWorkingDirectory(subDirectory);  
  
                        } else  
                        {  
                        	LOG.info("创建目录失败");  
                            return FTPStatus.Create_Directory_Fail;  
                        }  
  
                    }  
  
                    start = end + 1;  
  
                    end = directory.indexOf("/", start);  
  
                    // 检查所有目录是否创建完毕  
  
                    if (end <= start)  
                    {  
  
                        break;  
  
                    }  
  
                }  
  
            }  
  
        }  
  
        // 检查远程是否存在文件  
  
        FTPFile[] files = ftpClient.listFiles(remoteFileName);  
  
        if (files.length == 1)  
        {  
  
            long remoteSize = files[0].getSize();  
  
            File f = new File(local);  
  
            long localSize = f.length();  
  
            if (remoteSize == localSize)  
            {  
  
                return FTPStatus.File_Exits;  
  
            } else if (remoteSize > localSize)  
            {  
  
                return FTPStatus.Remote_Bigger_Local;  
  
            }  
  
            // 尝试移动文件内读取指针,实现断点续传  
  
            InputStream is = new FileInputStream(f);  
  
            if (is.skip(remoteSize) == remoteSize)  
            {  
  
                ftpClient.setRestartOffset(remoteSize);  
  
                if (ftpClient.storeFile(remote, is))  
                {  
  
                    return FTPStatus.Upload_From_Break_Success;  
  
                }  
  
            }  
  
            // 如果断点续传没有成功，则删除服务器上文件，重新上传  
  
            if (!ftpClient.deleteFile(remoteFileName))  
            {  
  
                return FTPStatus.Delete_Remote_Faild;  
  
            }  
  
            is = new FileInputStream(f);  
  
            if (ftpClient.storeFile(remote, is))  
            {  
  
                result = FTPStatus.Upload_New_File_Success;  
  
            } else  
            {  
  
                result = FTPStatus.Upload_New_File_Failed;  
  
            }  
  
            is.close();  
  
        } else  
        {  
  
            InputStream is = new FileInputStream(local);  
  
            if (ftpClient.storeFile(remoteFileName, is))  
            {  
  
                result = FTPStatus.Upload_New_File_Success;  
  
            } else  
            {  
  
                result = FTPStatus.Upload_New_File_Failed;  
  
            }  
  
            is.close();  
        }  
  
        return result;  
  
    }  
  
    /** 
     *  
     * 断开与远程服务器的连接 
     *  
     * @throws IOException 
     */  
  
    public void disconnect() throws IOException  
    {  
  
        if (ftpClient.isConnected())  
        {  
            ftpClient.disconnect();  
        }  
  
    }  
  
  
    @Override  
    public void run()  
    {  
        boolean status=false;  
        // 建立FTP连接  
        try  
        {  
        	status = this.connect(this.ftpConfig.getServer(), this.ftpConfig.getPort(), this.ftpConfig.getUserName(), this.ftpConfig.getUserPwd(), this.ftpConfig.getServerPath());
           // ftpClient.connect(this.ftpConfig.getServer(), this.ftpConfig.getPort());  
  
         /*   if (FTPReply.isPositiveCompletion(ftpClient.getReplyCode()))  
            {  
  
                if (ftpClient.login(this.ftpConfig.getUserName(), this.ftpConfig.getUserPwd()))  
                {  
                    status=true;  
                }  
            }else{  
                try  
                {  
                    disconnect();  
                } catch (IOException e)  
                {  
                      
                    e.printStackTrace();  
                }  
            }  */
        } catch (SocketException e1)  
        {     
            e1.printStackTrace();  
        } catch (IOException e1)  
        {  
            e1.printStackTrace();  
        }  
        // FTP连接成功后执行相应的操作  
        if(status){  
        	
        /*	try {
				this.remote = new String(this.remote.getBytes(LOCAL_CHARSET),SERVER_CHARSET);
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}*/
        	
            FTPStatus result=null;  
            if(this.ftpType==FTPType.UPLOAD)  
            {  
                try  
                {  
                    result=this.upload(this.getLocal(), this.getRemote());// 上传文件  
                } catch (IOException e)  
                {  
                	LOG.info("FTP上传文件异常：" + e.getMessage());  
                }  
            }else if(this.ftpType==FTPType.DOWNLOAD)  
            {  
                try  
                {  
                    result=this.download(this.getRemote(), this.getLocal());// 下载文件  
                } catch (IOException e)  
                {  
                	LOG.info("FTP下载文件异常：" + e.getMessage());  
                }  
            }else if(this.ftpType==FTPType.RENAME)  
            {  
                try  
                {  
                    result=this.rename(this.getLocal(), this.getRemote());// 修改名称  
                } catch (IOException e)  
                {  
                	LOG.info("FTP修改文件名称异常：" + e.getMessage());  
                }             
            }else if(this.ftpType==FTPType.DELETE)  
            {  
                try  
                {  
                    result=this.delete(this.getRemote());                    // 删除文件  
                } catch (IOException e)  
                {  
                	LOG.info("FTP删除文件异常：" + e.getMessage());  
                }  
            }  
            try  
            {  
                disconnect();  
            } catch (IOException e)  
            {  
            	LOG.info("FTP连接释放异常：" + e.getMessage());  
            }  
            LOG.info("FTP操作状态码:"+result);  
        }  
          
    }  
    
	public boolean batchDownloadFile(List<String> remoteFiles) {

		ExecutorService exe = Executors.newFixedThreadPool(50);
		ThreadFTPUtils myFtp = null;
		Thread thread = null;

		for (int i = 0; i < remoteFiles.size(); i++) {

			this.local = this.ftpConfig.getDownloadPath() + "/" + remoteFiles.get(i);
			this.remote = remoteFiles.get(i);
			myFtp = new ThreadFTPUtils(this.ftpConfig, this.local,
					remoteFiles.get(i), FTPType.DOWNLOAD);
			thread = new Thread(myFtp);
			exe.execute(thread);

		}

		exe.shutdown();
		while (true) {
			if (exe.isTerminated()) {
				System.out.println("所有线程结束！");
				return true;
			}
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public boolean batchDownloadFile(List<String> remoteFiles, List<String> localFiles) {

		ExecutorService exe = Executors.newFixedThreadPool(100);
		ThreadFTPUtils myFtp = null;
		Thread thread = null;

		for (int i = 0; i < remoteFiles.size(); i++) {

			this.local = this.ftpConfig.getDownloadPath() + "/" + localFiles.get(i);
			this.remote = remoteFiles.get(i);
			myFtp = new ThreadFTPUtils(this.ftpConfig, this.local,
					remoteFiles.get(i), FTPType.DOWNLOAD);
			thread = new Thread(myFtp);
			exe.execute(thread);

		}

		exe.shutdown();
		while (true) {
			if (exe.isTerminated()) {
				System.out.println("所有线程结束！");
				return true;
			}
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public String getLocal() {
		return local;
	}

	public void setLocal(String local) {
		this.local = local;
	}

	public String getRemote() {
		return remote;
	}

	public void setRemote(String remote) {
		this.remote = remote;
	}  
  
}  
      