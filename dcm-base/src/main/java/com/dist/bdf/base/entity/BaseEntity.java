package com.dist.bdf.base.entity;

import java.io.Serializable;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.SequenceGenerator;


/**
 * <pre>
 * 所有实体的基类，所有实体都必须继承该类。
 * 该基类提供主键id，并采用主键自增策略。
 * 子类使用IDE自动生成构造器时，若需要使用id，则需要手动添加代码。
 * </pre>
 * 
 * @author 李其云
 * @version 1.0, 2013-9-24
 */
@SuppressWarnings("serial")
@MappedSuperclass
public class BaseEntity implements Serializable {

/*	使用uuid的缺点是查询性能较差，而且前端进行参数传递时碰到大数据量时会出现参数长度超限的问题
 * 	@GenericGenerator(name = "systemUUID", strategy = "uuid")
	@GeneratedValue(generator = "systemUUID")  */
	
	//TODO 如果性能有问题，需要使用TableGenerator
/*	@GeneratedValue(generator = "idGenerator")     
	@GenericGenerator(name = "idGenerator", strategy = "native")  
	@Column(name = "ID",  unique = true, nullable = false)*/
//	@GeneratedValue(strategy=GenerationType.AUTO)  //JPA用法，与上面两行效果相同     
	/**
	 * <pre>主键
	 * 将主键设置为protect，方便其子类访问。
	 * allocationSize：序列每次增长步长
	 * </pre>
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "idGenerator") 
	@SequenceGenerator(name = "idGenerator",  sequenceName="SEQ_DCM_OID", allocationSize=1)
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
