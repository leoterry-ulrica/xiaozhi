
package com.dist.bdf.facade.service.sga.dao.impl;

import org.springframework.stereotype.Repository;

import com.dist.bdf.base.dao.hibernate.GenericDAOImpl;
import com.dist.bdf.facade.service.sga.dao.SgaUserDAO;
import com.dist.bdf.model.entity.sga.SgaUser;

/**
 * @author weifj
 * @version 1.0，2016/11/22，weifj，创建
 *
 */
@SuppressWarnings("unchecked")
@Repository
public class SgaUserDAOImpl extends GenericDAOImpl<SgaUser, Long> implements SgaUserDAO {

}
