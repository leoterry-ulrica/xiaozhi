package com.dist.bdf.model.entity.cad;

import java.io.Serializable;
import java.util.Arrays;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.dist.bdf.common.constants.MongoFieldConstants;
import com.fasterxml.jackson.annotation.JsonIgnore;


@Document(collection = "cad.template")
public class TemplateEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@JsonIgnore
	@Id
	private ObjectId id;
	@Field(MongoFieldConstants.NAME)
	private String name;
	@Field(MongoFieldConstants.MD5_CODE)
	private String md5Code;
	@Field(MongoFieldConstants.EXTENSION_NAME)
	private String suffix;
	@Field(MongoFieldConstants.CURRENT_PROPOSAL_FILE)
	private String currentProposalFile;
	@Field(MongoFieldConstants.PROPOSAL_COMPLEX)
	private ProposalEntity[] proposals;
	@Field(MongoFieldConstants.CE_DOC_ID)
	private String docId;
	@Field(MongoFieldConstants.CE_VERSION_ID)
	private String docVId;
	@Field(MongoFieldConstants.REALM)
	private String realm;

	/**
	 * 获取 主键id 
	 * @return the id
	 */
	public ObjectId getId() {
		return id;
	}

	/**
	 * 设置 主键id
	 * @param id the id to set
	 */
	public void setId(ObjectId id) {
		this.id = id;
	}

	/**
	 * 获取 模板名称
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * 设置 模板名称
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 获取 方案列表 
	 * @return the proposals
	 */
	public ProposalEntity[] getProposals() {
		return proposals;
	}

	/**
	 * 设置 方案列表 
	 * @param proposals the proposals to set
	 */
	public void setProposals(ProposalEntity[] proposals) {
		this.proposals = proposals;
	}

	/**
	 * 获取 #{bare_field_comment} 
	 * @return the md5Code
	 */
	public String getMd5Code() {
		return md5Code;
	}

	/**
	 * 设置 #{bare_field_comment}
	 * @param md5Code the md5Code to set
	 */
	public void setMd5Code(String md5Code) {
		this.md5Code = md5Code;
	}

	/**
	 * 获取 #{bare_field_comment} 
	 * @return the suffix
	 */
	public String getSuffix() {
		return suffix;
	}

	/**
	 * 设置 #{bare_field_comment}
	 * @param suffix the suffix to set
	 */
	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	/**
	 * 获取 #{bare_field_comment} 
	 * @return the currentProposalFile
	 */
	public String getCurrentProposalFile() {
		return currentProposalFile;
	}

	/**
	 * 设置 #{bare_field_comment}
	 * @param currentProposalFile the currentProposalFile to set
	 */
	public void setCurrentProposalFile(String currentProposalFile) {
		this.currentProposalFile = currentProposalFile;
	}

	/**
	 * 获取 关联CE文档id
	 * @return the docId
	 */
	public String getDocId() {
		return docId;
	}

	/**
	 * 设置 关联CE文档id
	 * @param docId the docId to set
	 */
	public void setDocId(String docId) {
		this.docId = docId;
	}

	@Override
	public String toString() {
		return "TemplateEntity [id=" + id + ", name=" + name + ", md5Code=" + md5Code + ", suffix=" + suffix
				+ ", currentProposalFile=" + currentProposalFile + ", proposals=" + Arrays.toString(proposals)
				+ ", docId=" + docId + "]";
	}

	/**
	 * 获取 域
	 * @return the realm
	 */
	public String getRealm() {
		return realm;
	}

	/**
	 * 设置 域
	 * @param realm the realm to set
	 */
	public void setRealm(String realm) {
		this.realm = realm;
	}

	public String getDocVId() {
		return docVId;
	}

	public void setDocVId(String docVId) {
		this.docVId = docVId;
	}
}
