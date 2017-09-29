package com.dist.bdf.facade.service.biz.domain.dcm;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.dist.bdf.base.utils.DocumentUtil;

/**
 * 业务配置
 * @author weifj
 * @version 1.0，2016/04/30，weifj，创建业务配置解析
 */
public class CaseBusinessConf {

	private static String defaultSetting = "tache.xml";
	private static Map<String, List<String>> caseTypeAndTacheMap = new HashMap<String, List<String>>();
	private static Logger logger = LoggerFactory.getLogger(CaseBusinessConf.class);
	
    static {
        loadSetting(defaultSetting);
    }	
    private static void loadSetting(String setting){
    	try{
    	caseTypeAndTacheMap.clear();
    	InputStream in = CaseBusinessConf.class.getClassLoader().getResourceAsStream("/"+defaultSetting);

    	Document doc = DocumentUtil.buildDocument(in); 
    	NodeList caseTypes = doc.getElementsByTagName("caseType");
    	if(caseTypes != null && caseTypes.getLength()>0){
    		for(int i=0;i<caseTypes.getLength();i++){
    			Element ele =  (Element) caseTypes.item(i);
    			String caseTypeName = ele.getAttribute("name");
    			NodeList taches = ele.getElementsByTagName("tache");
    			if(taches != null && taches.getLength()>0){
    				List<String> tacheList = new ArrayList<String>();
    				for(int j=0;j<taches.getLength();j++){
    					tacheList.add(((Element)taches.item(j)).getAttribute("name"));
    				}
    				caseTypeAndTacheMap.put(caseTypeName, tacheList);
    			}
    		}
    	}
    	}catch(Exception ex){
    		logger.error(ex.getMessage());
    	}
    }
    /**
     * 获取案例类型下的环节
     * @param caseType
     * @return
     */
    public static List<String> getTaches(String caseType){
    	return caseTypeAndTacheMap.get(caseType);
    }
    /**
     * 获取案例类型
     * @return
     */
    public static Set<String> getCaseTypes(){
    	
    	return caseTypeAndTacheMap.keySet();
    }
}
