
package com.dist.bdf.facade.service.biz.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ForkJoinPool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSONObject;
import com.dist.bdf.base.exception.BusinessException;
import com.dist.bdf.base.page.Pagination;
import com.dist.bdf.base.utils.ImageUtils;
import com.dist.bdf.common.conf.ecm.ECMConf;
import com.dist.bdf.facade.service.EcmMgmtService;
import com.dist.bdf.facade.service.GroupService;
import com.dist.bdf.facade.service.UserOrgService;
import com.dist.bdf.facade.service.biz.domain.dcm.CaseBusinessConf;
import com.dist.bdf.facade.service.biz.domain.dcm.DocAndFolderDmn;
import com.dist.bdf.facade.service.biz.domain.dcm.TeamSpaceDmn;
import com.dist.bdf.facade.service.biz.task.ComputeDocSocialDataTask;
import com.dist.bdf.manager.ecm.security.ConnectionService;
import com.dist.bdf.manager.ecm.utils.DistSocialUtil;
import com.dist.bdf.manager.ecm.utils.DocumentUtil;
import com.dist.bdf.manager.ecm.utils.SearchEngine;
import com.dist.bdf.model.dto.dcm.DocumentDTO;
import com.dist.bdf.model.dto.dcm.DocumentSortDTO;
import com.dist.bdf.model.dto.dcm.FileContentDTO;
import com.dist.bdf.model.dto.dcm.FileContentLocalDTO;
import com.dist.bdf.model.dto.dcm.PageParaDTO;
import com.dist.bdf.model.dto.dcm.TeamSpaceDTO;
import com.filenet.api.core.Document;
import com.ibm.ecm.util.p8.P8Connection;

/**
 * @author weifj
 * @version 1.0，2016/03/22，weifj，创建case服务实现层
 *
 */
@Service("EcmMgmtService")
@Transactional(propagation = Propagation.REQUIRED)
public class EcmMgmtServiceImpl implements EcmMgmtService {

	private static Logger LOG = LoggerFactory.getLogger(EcmMgmtServiceImpl.class);

	@Autowired
	private TeamSpaceDmn teamspaceDmn;
	@Autowired
	private DocAndFolderDmn docAndFolderDmn;
	@Autowired
	private ECMConf ecmConf;
	@Autowired
	private DocumentUtil documentUtil;
	@Autowired
	private ConnectionService connService;
	@Autowired
	private DocumentUtil docUtil;
	@Autowired
	private SearchEngine searchEngine;
	@Autowired
	private GroupService groupService;
	@Autowired
	private UserOrgService userService;
	@Autowired
	private DistSocialUtil socialUtil;

	@Override
	public String createTeamspace(String userName, String userPwd, TeamSpaceDTO tsTemplateDto) {

		return teamspaceDmn.createTeamspace(userName, userPwd, tsTemplateDto);
	}

	@Override
	public List<TeamSpaceDTO> getAllTeamspaceTemplate() {

		return this.teamspaceDmn.searchAllTemplate();
	}

	/*
	 * @Override public Pagination fullTextSearchOfDocument(String keyword) {
	 * 
	 * return (Pagination)
	 * docAndFolderDmn.fullTextSearchOfDoc(1,ecmConf.getPageSize(),keyword); }
	 */
	/*
	 * @Override public Pagination fullTextSearchOfDocument(int pageNo,int
	 * pageSize, String keyword) {
	 * 
	 * return (Pagination)
	 * docAndFolderDmn.fullTextSearchOfDoc(pageNo,pageSize,keyword); }
	 */
	/*
	 * @Override public Pagination fullTextSearchOfDocument(PageParaDTO
	 * pageInfo) {
	 * 
	 * return (Pagination)
	 * docAndFolderDmn.fullTextSearchOfDoc(pageInfo.getPageNo(),pageInfo.
	 * getPageSize(),pageInfo.getKeyword()); }
	 */
	@Override
	public Pagination fullTextSearchOfDocumentCloud(PageParaDTO pageInfo) {

		P8Connection p8conn = this.connService.getP8Connection(this.ecmConf.getTargetObjectStore(pageInfo.getRealm()));

		return this.searchEngine.fullTextSearch(p8conn.getObjectStore(), ecmConf.getDefaultDocumentClass(), false, null,
				null, null, null, pageInfo.getKeyword(), pageInfo.getPageNo(), pageInfo.getPageSize(), true); // this.docAndFolderDmn.fullTextSearchOfDoc(p8conn.getObjectStore(),
																												// pageInfo.getPageNo(),
																												// pageInfo.getPageSize(),
																												// pageInfo.getKeyword());
	}

	@Override
	public Pagination fullTextSearchOfDocumentRank(PageParaDTO pageInfo) {

		// P8Connection p8conn =
		// this.connService.getP8Connection(this.ecmConf.getTargetObjectStore(pageInfo.getRealm()),
		// pageInfo.getUserId(), pageInfo.getPassword());
		String targetObjectStore = this.ecmConf.getTargetObjectStore(pageInfo.getRealm());
		P8Connection p8conn = this.connService.getP8Connection(targetObjectStore);
		// 第一层排序，根据关键字的匹配度
		Pagination pageData = this.searchEngine.fullTextSearchOfDocRank(p8conn.getObjectStore(), pageInfo.getPageNo(),
				pageInfo.getPageSize(), pageInfo.getKeyword());
		LOG.info(">>>ce全文检索结果：{}", JSONObject.toJSONString(pageData));
		
		// 第二层排序，名字匹配度优先，然后文档的下载数和收藏数之和进行排序，如果下载数和收藏数之和相等，则根据文档的创建时间
		@SuppressWarnings("unchecked")
		List<DocumentDTO> docs = (List<DocumentDTO>) pageData.getData();

		if (null == docs || 0 == docs.size()) {
			LOG.info(">>>没有检索到任何数据，关键词：[{}]", pageInfo.getKeyword());
			return pageData;
		}

		final ForkJoinPool pool = new ForkJoinPool(6);
		final ComputeDocSocialDataTask task = new ComputeDocSocialDataTask(targetObjectStore, this.connService,
				this.socialUtil, this.userService, this.groupService, docs, pageInfo.getKeyword());

		List<DocumentSortDTO> docSortDTOs = pool.invoke(task);
		LOG.info(">>>计算完成后结果集大小：[{}]", docSortDTOs.size());
		pageData.setData(docSortDTOs);
		LOG.info(">>>根据设定的排序规则，对结果集进行重新排序...");
		// TODO 直接排序不起作用，未找到原因
		// Collections.sort(docSortDTOs);
		Collections.sort(docSortDTOs, new Comparator<DocumentSortDTO>() {
	            @Override
	            public int compare(DocumentSortDTO first, DocumentSortDTO second) {
	            	// 注意前后参数
	                return second.compareTo(first);
	            }
	        });
		LOG.info(">>>排序后结果：{}", JSONObject.toJSONString(pageData));
		return pageData;
		// return (Pagination)
		// docAndFolderDmn.fullTextSearchOfDocRank(this.ecmConf.getTargetObjectStore(pageInfo.getRealm()),
		// pageInfo.getPageNo(),pageInfo.getPageSize(),pageInfo.getKeyword());

	}

	@Override
	public Object preciseQuery(PageParaDTO pageInfo) {

		Object result = "";
		switch (pageInfo.getSearchType()) {
		case "filetype":
			result = this.preciseQueryByFileType(pageInfo);
			break;
		case "region":
			result = this.preciseQueryByRegion(pageInfo);
			break;
		case "org":
			result = this.preciseQueryByOrganization(pageInfo);
			break;
		case "business":
			result = this.preciseQueryByBusiness(pageInfo);
			break;
		default:
			break;
		}
		return result;
	}

	/*
	 * @Override public Pagination preciseQueryByFileType(String realm, String
	 * value) {
	 * 
	 * P8Connection p8conn =
	 * this.connService.getP8Connection(this.ecmConf.getTargetObjectStore(realm)
	 * );
	 * 
	 * return (Pagination)
	 * this.docAndFolderDmn.preciseQueryOfDoc(p8conn.getObjectStore(),
	 * 1,ecmConf.getPageSize(),ecmConf.getFileTypeField(), value); }
	 * 
	 * @Override public Pagination preciseQueryByFileType(String realm, int
	 * pageNo,int pageSize, String value) {
	 * 
	 * P8Connection p8conn =
	 * this.connService.getP8Connection(this.ecmConf.getTargetObjectStore(realm)
	 * );
	 * 
	 * return (Pagination)
	 * docAndFolderDmn.preciseQueryOfDoc(p8conn.getObjectStore(),
	 * pageNo,pageSize,ecmConf.getFileTypeField(), value); }
	 */
	@Override
	public Object preciseQueryByFileType(PageParaDTO pageInfo) {

		P8Connection p8conn = this.connService.getP8Connection(this.ecmConf.getTargetObjectStore(pageInfo.getRealm()));

		return (Pagination) docAndFolderDmn.preciseQueryOfDoc(p8conn.getObjectStore(), pageInfo.getPageNo(),
				pageInfo.getPageSize(), ecmConf.getFileTypeField(), pageInfo.getKeyword());
	}
	/*
	 * @Override public Pagination preciseQueryByRegion(String value) {
	 * 
	 * 
	 * return (Pagination)
	 * docAndFolderDmn.preciseQueryOfDoc(1,ecmConf.getPageSize(),ecmConf.
	 * getRegionField(), value); }
	 */

	/*
	 * @Override public Pagination preciseQueryByRegion(int pageNo,int
	 * pageSize,String value) {
	 * 
	 * return (Pagination)
	 * docAndFolderDmn.preciseQueryOfDoc(pageNo,pageSize,ecmConf.getRegionField(
	 * ), value); }
	 */
	@Override
	public Object preciseQueryByRegion(PageParaDTO pageInfo) {

		P8Connection p8conn = this.connService.getP8Connection(this.ecmConf.getTargetObjectStore(pageInfo.getRealm()));

		return (Pagination) docAndFolderDmn.preciseQueryOfDoc(p8conn.getObjectStore(), pageInfo.getPageNo(),
				pageInfo.getPageSize(), ecmConf.getRegionField(), pageInfo.getKeyword());
	}

	/*
	 * @Override public Pagination preciseQueryByOrganization(String value) {
	 * 
	 * return (Pagination)
	 * docAndFolderDmn.preciseQueryOfDoc(1,ecmConf.getPageSize(),ecmConf.
	 * getOrganizationField(), value); }
	 */

	/*
	 * @Override public Pagination preciseQueryByOrganization(int pageNo,int
	 * pageSize,String value) {
	 * 
	 * return (Pagination)
	 * docAndFolderDmn.preciseQueryOfDoc(pageNo,pageSize,ecmConf.
	 * getOrganizationField(), value); }
	 */
	@Override
	public Object preciseQueryByOrganization(PageParaDTO pageInfo) {

		P8Connection p8conn = this.connService.getP8Connection(this.ecmConf.getTargetObjectStore(pageInfo.getRealm()));

		return (Pagination) docAndFolderDmn.preciseQueryOfDoc(p8conn.getObjectStore(), pageInfo.getPageNo(),
				pageInfo.getPageSize(), ecmConf.getOrganizationField(), pageInfo.getKeyword());
	}

	/*
	 * @Override public Pagination preciseQueryByBusiness(String value) {
	 * 
	 * return (Pagination)
	 * docAndFolderDmn.preciseQueryOfDoc(1,ecmConf.getPageSize(),ecmConf.
	 * getBusinessField(), value); }
	 * 
	 * @Override public Pagination preciseQueryByBusiness(int pageNo,int
	 * pageSize,String value) {
	 * 
	 * return (Pagination)
	 * docAndFolderDmn.preciseQueryOfDoc(pageNo,pageSize,ecmConf.
	 * getBusinessField(), value); }
	 */
	@Override
	public Pagination preciseQueryByBusiness(PageParaDTO pageInfo) {

		P8Connection p8conn = this.connService.getP8Connection(this.ecmConf.getTargetObjectStore(pageInfo.getRealm()));

		return (Pagination) docAndFolderDmn.preciseQueryOfDoc(p8conn.getObjectStore(), pageInfo.getPageNo(),
				pageInfo.getPageSize(), ecmConf.getBusinessField(), pageInfo.getKeyword());
	}

	/*
	 * @Deprecated
	 * 
	 * @Override public byte[] getDocContentStream(String docId) {
	 * 
	 * this.connService.initialize(); Document doc =
	 * docUtil.loadById(this.connService.getDefaultOS(), docId); Double
	 * contentSize = doc.get_ContentSize(); if(null == contentSize){ throw new
	 * BusinessException("文档id："+docId+" 在存储库中不存在。"); } int size =
	 * doc.get_ContentSize().intValue(); //ContentElementList contents =
	 * doc.get_ContentElements();
	 * 
	 * InputStream is = doc.accessContentStream(0); Iterator it =
	 * contents.iterator(); while(it.hasNext()){ ContentTransfer ct =
	 * (ContentTransfer) it.next(); is = ct.accessContentStream(); break; }
	 * if(null == is){ this.connService.release(); throw new BusinessException(
	 * "contentStream is null"); }
	 * 
	 * // 以二进制的形式 byte[] buffer = new
	 * byte[size];//不能使用fis.available()，这个方法获取的大小并不是源文件大小 try { is.read(buffer);
	 * is.close();
	 * 
	 * } catch (IOException e) {
	 * 
	 * e.printStackTrace(); } this.connService.release();
	 * 
	 * return buffer; }
	 */
	@Override
	public byte[] getDocContentStream(String realm, String docId) throws BusinessException {

		P8Connection p8conn = this.connService.getP8Connection(this.ecmConf.getTargetObjectStore(realm));

		Document doc = docUtil.loadById(p8conn.getObjectStore(), docId);
		Double contentSize = doc.get_ContentSize();
		if (null == contentSize) {
			throw new BusinessException("文档id：" + docId + " 在存储库中不存在。");
		}
		int size = doc.get_ContentSize().intValue();

		InputStream is = doc.accessContentStream(0);

		if (null == is) {
			// this.connService.release();
			throw new BusinessException("contentStream is null");
		}

		// 以二进制的形式
		byte[] buffer = new byte[size];// 不能使用fis.available()，这个方法获取的大小并不是源文件大小
		try {
			is.read(buffer);
			is.close();
		} catch (IOException e) {
			LOG.error(e.getMessage(), e);
			throw new BusinessException(e.getMessage());
		}
		return buffer;
	}

	@Override
	public Object getDocContentStream(String realm, String userId, String pwd, String docId) throws BusinessException {

		P8Connection p8conn = this.connService.getP8Connection(this.ecmConf.getTargetObjectStore(realm), userId, pwd);

		Document doc = docUtil.loadById(p8conn.getObjectStore(), docId);
		Double contentSize = doc.get_ContentSize();
		if (null == contentSize) {
			throw new BusinessException("文档id：" + docId + " 在存储库中不存在。");
		}
		int size = doc.get_ContentSize().intValue();

		InputStream is = doc.accessContentStream(0);

		if (null == is) {
			this.connService.release();
			throw new BusinessException("contentStream is null");
		}

		// 以二进制的形式
		byte[] buffer = new byte[size];// 不能使用fis.available()，这个方法获取的大小并不是源文件大小
		try {
			is.read(buffer);
			is.close();
		} catch (IOException e) {
			LOG.error(e.getMessage(), e);
		}
		return buffer;
	}

	@Override
	public Map<String, byte[]> getImgContentStreamByVID(String realm, List<String> vids) {
		
		Assert.notEmpty(vids);
		
		P8Connection p8conn = this.connService.getP8Connection(this.ecmConf.getTargetObjectStore(realm));
		FileContentLocalDTO dto = null;
		Map<String, byte[]> streams = new LinkedHashMap<String, byte[]>();
		for(String vid : vids) {
			dto = this.documentUtil.loadLocalByVId(p8conn.getObjectStore(), vid);
			if(null == dto) {
				continue;
			}
			if(!dto.getContentType().startsWith("image/")) {
				continue;
			}
			Long contentSize = dto.getLsize();
			if (null == contentSize) {
				LOG.warn("文档系列id：" + vid + " 在存储库中不存在。");
				continue;
			}
			int size = dto.getSize();
			InputStream is = dto.getContentInputStream();
			if (null == is) {
				continue;
			}
			// 以二进制的形式
			byte[] buffer = new byte[size];// 不能使用fis.available()，这个方法获取的大小并不是源文件大小
			try {
				is.read(buffer);
				is.close();
				streams.put(vid, buffer);
			} catch (IOException e) {
				LOG.error(e.getMessage(), e);
			}
		}
		return streams;
	}
	@Override
	public byte[] getDocContentStreamBreakpoint(String docId, Long position) throws BusinessException {

		this.connService.initialize();
		Document doc = this.documentUtil.loadById(this.connService.getDefaultOS(), docId);
		Double contentSize = doc.get_ContentSize();
		if (null == contentSize) {
			throw new BusinessException("文档id：" + docId + " 在存储库中不存在。");
		}
		int size = doc.get_ContentSize().intValue();
		// ContentElementList contents = doc.get_ContentElements();

		InputStream is = doc.accessContentStream(0);
		/*
		 * Iterator it = contents.iterator(); while (it.hasNext()) {
		 * ContentTransfer ct = (ContentTransfer) it.next(); is =
		 * ct.accessContentStream(); break; }
		 */
		if (null == is) {
			this.connService.release();
			throw new BusinessException("contentStream is null");
		}
		// 以二进制的形式
		byte[] buffer = new byte[size - position.intValue() + 1];// 不能使用fis.available()，这个方法获取的大小并不是源文件大小
		try {
			is.skip(position);
			is.read(buffer);
			is.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
		this.connService.release();
		return buffer;

	}

	@Override
	public FileContentDTO getDocContentBreakpoint(String docId, Long pos) {

		this.connService.initialize();
		FileContentDTO dto = new FileContentDTO();
		Document doc = this.docUtil.loadById(this.connService.getDefaultOS(), docId);
		dto.setSize(doc.get_ContentSize().intValue());
		dto.setLsize(doc.get_ContentSize().longValue());
		dto.setDocName(doc.get_Name());
		// ContentElementList contents = doc.get_ContentElements();

		InputStream is = doc.accessContentStream(0);
		/*
		 * Iterator it = contents.iterator(); while (it.hasNext()) {
		 * ContentTransfer ct = (ContentTransfer) it.next(); is =
		 * ct.accessContentStream(); break; }
		 */
		/*
		 * if (-1 == last) { last = dto.getLsize() - 1; }
		 */
		// Long rangLength = last - pos + 1;// 总共需要读取的字节

		byte[] buffer = new byte[dto.getSize() - pos.intValue() + 1];
		try {
			is.skip(pos); // 跳过已经下载的部分，进行后续下载
			is.read(buffer);
			// is.close();
			dto.setContentStream(buffer);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return dto;

	}

	@Override
	public List<String> getTachesByCaseType(String name) {

		return CaseBusinessConf.getTaches(name);
	}

}
