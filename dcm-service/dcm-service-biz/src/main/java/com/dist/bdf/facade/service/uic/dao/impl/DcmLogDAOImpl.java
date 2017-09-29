
package com.dist.bdf.facade.service.uic.dao.impl;

import org.springframework.stereotype.Repository;

import com.dist.bdf.base.dao.hibernate.GenericDAOImpl;
import com.dist.bdf.facade.service.uic.dao.DcmLogDAO;
import com.dist.bdf.model.entity.system.DcmLog;

/**
 * @author weifj
 * @version 1.0，2016/06/23，weifj，创建
 *
 */
@Repository
public class DcmLogDAOImpl extends GenericDAOImpl<DcmLog, Long> implements DcmLogDAO {

}
