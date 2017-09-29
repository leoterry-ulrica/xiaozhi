package com.dist.bdf.facade.service.biz.task;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.RecursiveTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dist.bdf.facade.service.biz.domain.system.DcmSocialResourceDmn;
import com.dist.bdf.common.constants.ResourceConstants;
import com.dist.bdf.manager.ecm.utils.DocumentUtil;
import com.dist.bdf.model.dto.system.MaterialDTO;
import com.dist.bdf.model.entity.system.DcmSocialResource;
import com.filenet.api.core.Document;

public class ComputeResPrivTask extends RecursiveTask<Map<String, MaterialDTO>> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Logger logger = LoggerFactory.getLogger(getClass());
	private static final int SEQUENTIAL_THRESHOLD = 5;

	/**
	 * 版本系列id集合
	 */
	private final String[] vIds;
	private final int start;
	private final int end;
	private String userId;
	private DocumentUtil docAndFolderDmn;
	private DcmSocialResourceDmn socialResDmn;
	private final Map<String, DcmSocialResource> socialMap;
    private final Map<String, Document> docVidMap;
	
	public ComputeResPrivTask(String userId, String[] vIdArray, Map<String, DcmSocialResource> socialMap, Map<String, Document> docVidMap, DcmSocialResourceDmn socialResDmn, DocumentUtil docAndFolderDmn) {

		this.start = 0;
		this.end = vIdArray.length;
		this.socialMap = socialMap;
		this.docVidMap = docVidMap;
		vIds = vIdArray;
		this.userId = userId;
		this.docAndFolderDmn = docAndFolderDmn;
		this.socialResDmn = socialResDmn;
	}

	public ComputeResPrivTask(String userId, String[] vIdArray, Map<String, DcmSocialResource> socialMap, Map<String, Document> docVidMap, int start, int end, DcmSocialResourceDmn socialResDmn, DocumentUtil docAndFolderDmn) {

		this.start = start;
		this.end = end;
		this.vIds = vIdArray;
		this.userId = userId;
		this.docAndFolderDmn = docAndFolderDmn;
		this.socialResDmn = socialResDmn;
		this.socialMap = socialMap;
		this.docVidMap = docVidMap;
	}

	@Override
	protected Map<String, MaterialDTO> compute() {

		if(this.socialMap.isEmpty()){
			return computeDirectly();
		}
		final int length = end - start;
		if (length < SEQUENTIAL_THRESHOLD) {
			return computeDirectly();
		}
		Map<String, MaterialDTO> allMap = new HashMap<String, MaterialDTO>();
		final int split = length / 2;
		final ComputeResPrivTask left = new ComputeResPrivTask(this.userId, this.vIds, this.socialMap, this.docVidMap, this.start, this.start + split,
				this.socialResDmn, this.docAndFolderDmn);
		left.fork();
		final ComputeResPrivTask right = new ComputeResPrivTask(this.userId, this.vIds, this.socialMap, this.docVidMap, this.start + split, this.end,
				this.socialResDmn,this.docAndFolderDmn);
		allMap.putAll(right.compute()); // 右边任务继续拆分
		allMap.putAll(left.join());
		return allMap;
	}

	private Map<String, MaterialDTO> computeDirectly() {

		Map<String, MaterialDTO> map = new HashMap<String, MaterialDTO>();
		Date dateStart = new Date();
		for (int i = start; i < end; i++) {
			String vId = vIds[i];
			MaterialDTO dto = null;
			try {
			
				int type = -1;
				DcmSocialResource sr = this.socialMap.get(vId);
				if(null == sr){
					Document doc = this.docVidMap.get(vId);//(Document) this.docAndFolderDmn.loadDocByVersionSeriesId(vId);
					type = ResourceConstants.ResourceType.getClientResType(this.docAndFolderDmn.getDocResTypeCode(doc));
				}else {
					type = ResourceConstants.ResourceType.getClientResType(sr.getResTypeCode());
				}
				boolean isFavorite = this.socialResDmn.isFavorite(sr);
				dto = new MaterialDTO(type, false, isFavorite);

			} catch (Exception ex) {
				logger.error(">>>无法获取资源id[{}]的信息。", vId);
				ex.printStackTrace();
				dto = new MaterialDTO(-1, false, false);
			}
			map.put(vId, dto);
		}
		Date dateEnd = new Date();
		logger.info(">>>任务消耗时间：[{}] ms", (dateEnd.getTime() - dateStart.getTime()));
		return map;
	}

}
