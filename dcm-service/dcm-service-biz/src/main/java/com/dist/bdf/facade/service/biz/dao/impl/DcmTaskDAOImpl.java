
package com.dist.bdf.facade.service.biz.dao.impl;

import org.springframework.stereotype.Repository;

import com.dist.bdf.base.dao.hibernate.GenericDAOImpl;
import com.dist.bdf.facade.service.biz.dao.DcmTaskDAO;
import com.dist.bdf.model.entity.system.DcmTask;

/**
 * @author weifj
 * @version 1.0，2016/06/23，weifj，创建
 *
 */
@Repository
public class DcmTaskDAOImpl extends GenericDAOImpl<DcmTask, Long> implements DcmTaskDAO {

}
