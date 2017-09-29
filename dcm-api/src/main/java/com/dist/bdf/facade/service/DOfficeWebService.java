package com.dist.bdf.facade.service;

import com.dist.bdf.base.exception.BusinessException;
import com.dist.bdf.model.dto.dcm.OfficeFileContentDTO;

/**
 * 提供office在线浏览服务
 * @author weifj
 * @version 1.0，2016/04/09，weifj，创建服务
 */
public interface DOfficeWebService {

	/**
	 * 获取文件的基本信息
	 * @param docId
	 * @return json格式数据
	 */
	public String checkFileInfo(String docId);
	
	/**
	 * 获取文件信息，大小和文件流
	 * @param docId
	 * @return
	 */
	public OfficeFileContentDTO getFile(String docId)  throws BusinessException;


}
