package com.dist.bdf.manager.mongo;

import java.io.InputStream;
import java.util.List;

import com.dist.bdf.model.dto.sga.ImgInfo;
import com.mongodb.DBObject;
import com.mongodb.gridfs.GridFSDBFile;

public interface MongoFileStorageDmn {

	/**
	 * 保存文件
	 * @param inputStream
	 * @param fileName
	 * @param contentType
	 * @param metaData
	 * @return 返回mongo id
	 */
	public String store(InputStream inputStream, String fileName, String contentType, DBObject metaData);
	/**
	 * 根据id查找
	 * @param id
	 * @return
	 */
	public GridFSDBFile getById(String id);
	/**
	 * 根据文件名查找
	 * @param filename
	 * @return
	 */
	public GridFSDBFile getByFilename(String filename);
	/**
	 * 根据编码查找
	 * @param code
	 * @return
	 */
	public GridFSDBFile getByCode(String code);
	
	public void deleteByCode(String code);
	/**
	 * 检索所有
	 * @return
	 */
	public List<?> findAll();
	/**
	 * 根据字段删除
	 * @param keys
	 * @param values
	 */
	void deleteByFields(String[] keys, Object[] values);
	/**
	 * 保存到mongo
	 * @param imgInfo
	 * @param type
	 * @return 返回mongo id
	 */ 
	String storeToMongo(ImgInfo imgInfo, String type);

}
