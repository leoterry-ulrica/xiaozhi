package com.dist.bdf.consumer.wopi;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.dist.bdf.base.exception.BusinessException;
import com.dist.bdf.base.utils.DocumentUtil;
import com.dist.bdf.common.conf.officeonline.OfficeonlineConf;

import io.github.xdiamond.client.XDiamondConfig;

/**
 * officeonline工具类
 * @author weifj
 * @version 1.0。2016/04/11，weifj，创建viewer工具类型
 */
@Component
public class OfficeOnlineViewer {

	@Autowired
	private XDiamondConfig xconf;

/*	
	private static Map<String, String> extensionViewer= new HashMap<String, String>();
	private static String defaultViewerConfig = "officeonline/viewermapping.xml";
	
	static {
		try{
		ClassPathResource resource = new ClassPathResource(defaultViewerConfig);
		Document doc = DocumentUtil.buildDocument(resource.getFile().getAbsolutePath());
		Element root = doc.getDocumentElement();
		NodeList nodes = root.getElementsByTagName("action");
		for(int i=0;i<nodes.getLength();i++){
			String view = ((Element)nodes.item(i)).getAttribute("name");
			String urlsrc = ((Element)nodes.item(i)).getAttribute("urlsrc");
			Node extNode = ((Element)nodes.item(i)).getElementsByTagName("extfilter").item(0);
			NodeList extValues = ((Element)extNode).getElementsByTagName("value");
			for(int j=0;j<extValues.getLength();j++){
				String extValue = extValues.item(j).getTextContent();
				String key = new StringBuilder().append(view).append(".").append(extValue).toString();
				if(!extensionViewer.containsKey(key)){
					extensionViewer.put(key, urlsrc);
				}
			}
		}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
	}
	*//**
	 * 获取viewer地址
	 * @param extension
	 * @return
	 *//*
	public static String getViewer(String extension) {
		
		StringBuilder buf = new StringBuilder();
		buf.append("view");
		buf.append(".");
		buf.append(extension);
		if(extensionViewer.containsKey(buf.toString())){
			return extensionViewer.get(buf.toString());
		}
		return "";
	}
	public static void main(String[]args){
		
		OfficeOnlineViewer view = new OfficeOnlineViewer();
	}*/
}
