
package com.dist.bdf.facade.service.biz.dao.impl;

import org.springframework.stereotype.Repository;

import com.dist.bdf.base.dao.hibernate.GenericDAOImpl;
import com.dist.bdf.facade.service.biz.dao.DcmProcessLogDAO;
import com.dist.bdf.model.entity.system.DcmProcessLog;

/**
 * @author weifj
 * @version 1.0，2016/06/23，weifj，创建
 *
 */
@Repository
public class DcmProcessLogDAOImpl extends GenericDAOImpl<DcmProcessLog, Long> implements DcmProcessLogDAO {

}
