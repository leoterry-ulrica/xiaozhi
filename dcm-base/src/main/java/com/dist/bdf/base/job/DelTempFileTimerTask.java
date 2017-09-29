package com.dist.bdf.base.job;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dist.bdf.base.utils.FileUtil;
import com.dist.bdf.base.utils.StringUtil;

public class DelTempFileTimerTask {

	private Logger logger = LoggerFactory.getLogger(getClass());
	/**
	 * 清理目录，多个目录用分号";"分隔
	 */
	private String directories;

	public String getDirectories() {
		return directories;
	}

	public void setDirectories(String directories) {
		this.directories = directories;
	}
	
    public void execute() {

		if(StringUtil.isNullOrEmpty(directories)) {
			return;
		}
    	String[] dirArray = directories.split(";");
    	
		for(String tempDir : dirArray) {
			if(!FileUtil.isAbsolutePath(tempDir)) {
				tempDir = new File("").getAbsolutePath()+"/"+tempDir;
			}
			logger.info("清理目录：[{}]", tempDir);

			FileUtil.delAllFile(tempDir);
		}
		
	}
}
