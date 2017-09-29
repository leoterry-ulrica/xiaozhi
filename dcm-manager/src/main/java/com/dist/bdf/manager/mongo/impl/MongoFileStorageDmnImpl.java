package com.dist.bdf.manager.mongo.impl;

import java.io.InputStream;
import java.util.List;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;

import com.dist.bdf.base.utils.FileUtil;
import com.dist.bdf.manager.mongo.MongoFileStorageDmn;
import com.dist.bdf.model.dto.sga.ImgInfo;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.gridfs.GridFSDBFile;
/**
 * 文件存储于mongodb
 * @author weifj
 *
 */
/*@Repository*/
public class MongoFileStorageDmnImpl implements MongoFileStorageDmn {

/*	@Autowired*/
	private GridFsTemplate gridFsTemplate;

	@Override
	public String store(InputStream inputStream, String fileName, String contentType, DBObject metaData) {
		
		return this.gridFsTemplate.store(inputStream, fileName, contentType, metaData).getId().toString();
	}
	@Override
	public GridFSDBFile getById(String id) {
		return this.gridFsTemplate.findOne(new Query(Criteria.where("_id").is(id)));
	}
	@Override
	public GridFSDBFile getByFilename(String fileName) {
		return this.gridFsTemplate.findOne(new Query(Criteria.where("filename").is(fileName)));
	}
	@Override
	public GridFSDBFile getByCode(String code) {
		return this.gridFsTemplate.findOne(new Query(Criteria.where("metadata.code").is(code)));
	}
	@Override
	public List<?> findAll() {
		return this.gridFsTemplate.find(null);
	}
	@Override
	public void deleteByCode(String code) {
		 this.gridFsTemplate.delete(new Query(Criteria.where("metadata.code").is(code)));
	}
	@Override
	public void deleteByFields(String[] keys, Object[] values) {
		
		Query query = new Query();
		for (int i = 0; i < keys.length; i++) {
			query.addCriteria(Criteria.where(keys[i]).is(values[i]));
		}
		this.gridFsTemplate.delete(query);
	}
	public GridFsTemplate getGridFsTemplate() {
		return gridFsTemplate;
	}
	public void setGridFsTemplate(GridFsTemplate gridFsTemplate) {
		this.gridFsTemplate = gridFsTemplate;
	}
	@Override
    public String storeToMongo(ImgInfo imgInfo, String type) {
		
        String newFileName = System.currentTimeMillis() + (imgInfo.getSuffix().startsWith(".")? imgInfo.getSuffix() : "."+imgInfo.getSuffix());
		
		DBObject metaData = new BasicDBObject();
    	metaData.put("suffix", imgInfo.getSuffix());
    	// 花括号跟mongo查询语法有冲突问题，故去掉
    	metaData.put("code", imgInfo.getId().replace("{", "").replace("}", ""));
    	metaData.put("type", type);
    	
    	return this.store(FileUtil.base64ToInputStream(imgInfo.getContent()) , newFileName, imgInfo.getType(), metaData);
	}
	
}
