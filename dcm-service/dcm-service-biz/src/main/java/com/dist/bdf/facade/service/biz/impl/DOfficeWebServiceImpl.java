package com.dist.bdf.facade.service.biz.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dist.bdf.base.exception.BusinessException;
import com.dist.bdf.facade.service.DOfficeWebService;
import com.dist.bdf.manager.ecm.security.ConnectionService;
import com.dist.bdf.manager.ecm.utils.DocumentUtil;
import com.dist.bdf.model.dto.dcm.OfficeFileContentDTO;
import com.filenet.api.collection.ContentElementList;
import com.filenet.api.core.ContentTransfer;
import com.filenet.api.core.Document;

import net.sf.json.JSONObject;

@Service("DOfficeWebService")
public class DOfficeWebServiceImpl implements DOfficeWebService {

	@Autowired
	private ConnectionService connService;
	@Autowired
	private DocumentUtil docUtil;
	
	@Override
	public String checkFileInfo(String docId) {
	
		this.connService.initialize();
		
		Document doc = docUtil.loadById(this.connService.getDefaultOS(), docId);
	
		String baseFileName = doc.get_Name();     //文件名
	    String ownerId =  doc.get_Owner();//"admin"; //文件所有者的唯一编号
	    long size = doc.get_ContentSize().longValue();  //文件大小，以bytes为单位
	    long version = doc.get_DateLastModified().getTime();  //文件版本号，文件最后被修改时间值

	    /*OfficeFileInfoDTO dto = new OfficeFileInfoDTO();
	    dto.setAllowExternalMarketplace("true");
	    dto.setBaseFileName(baseFileName);
	    dto.setOwnerId(ownerId);
	    dto.setSize(String.valueOf(size));
	    dto.setVersion(String.valueOf(version));*/
	    
	    JSONObject metadataJson = new JSONObject();
	    metadataJson.put("BaseFileName", baseFileName);
	    metadataJson.put("OwnerId", ownerId);
	    metadataJson.put("Size", size);
	    metadataJson.put("AllowExternalMarketplace", true);
	    metadataJson.put("Version", version);
	    
	    //return JSONUtil.toJSONString(dto);
	    this.connService.release();
	    
	    return metadataJson.toString();
	    /*return "{\"BaseFileName\":\"" + baseFileName + "\",\"OwnerId\":\"" + ownerId + "\",\"Size\":\"" + size
	                        + "\",\"AllowExternalMarketplace\":\"" + true + "\",\"Version\":\"" + version + "\"}";*/
	}

	@Override
	public OfficeFileContentDTO getFile(String docId)  throws BusinessException {
		
		this.connService.initialize();
		OfficeFileContentDTO dto = new OfficeFileContentDTO();
		Document doc = this.docUtil.loadById(this.connService.getDefaultOS(), docId);
		dto.setSize(doc.get_ContentSize().intValue());
		dto.setLsize(doc.get_ContentSize().longValue());
		dto.setDocName(doc.get_Name());
        ContentElementList contents = doc.get_ContentElements();
		
        InputStream is = null;
		Iterator<?> it = contents.iterator();
		while(it.hasNext()){
			ContentTransfer ct = (ContentTransfer) it.next();
		    is = ct.accessContentStream();
			//dto.setContentStream(is);
			break;	
		}
		if(null == is){
			throw new BusinessException("contentStream is null");
		}

		// 以二进制的形式
		byte[] buffer = new byte[dto.getSize()];//不能使用fis.available()，这个方法获取的大小并不是源文件大小
		try {
			is.read(buffer);
			is.close();
			dto.setContentStream(buffer);
		} catch (IOException e) {
		
			e.printStackTrace();
		}
		
		return dto;
	}

}
