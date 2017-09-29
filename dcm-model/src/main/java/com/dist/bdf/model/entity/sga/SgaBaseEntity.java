package com.dist.bdf.model.entity.sga;

import java.io.Serializable;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.SequenceGenerator;

@MappedSuperclass
public class SgaBaseEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * <pre>主键
	 * 将主键设置为protect，方便其子类访问。
	 * allocationSize：序列每次增长步长
	 * </pre>
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "idGenerator") 
	@SequenceGenerator(name = "idGenerator",  sequenceName="SEQ_SGA_OID", allocationSize=1)
	protected Long id;

	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 */
	public void setId(Long id) {
		this.id = id;
	}
	
}
