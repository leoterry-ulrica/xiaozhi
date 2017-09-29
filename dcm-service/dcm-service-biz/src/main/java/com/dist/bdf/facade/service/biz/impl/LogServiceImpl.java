package com.dist.bdf.facade.service.biz.impl;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dist.bdf.facade.service.LogService;
import com.dist.bdf.facade.service.biz.domain.system.DcmProcessLogDmn;
import com.dist.bdf.model.dto.system.TaskProcessLogDTO;
import com.dist.bdf.model.entity.system.DcmProcessLog;


@Service("LogService")
public class LogServiceImpl implements LogService {

	@Autowired
	private DcmProcessLogDmn processLogDmn;
	
	@Override
	public Object addTaskProcessLog(TaskProcessLogDTO dto) {
		
		DcmProcessLog log = new DcmProcessLog();
		log.setActionType(dto.getActionType());
		log.setComments(dto.getComments());
		log.setDateCreated(new Date());
		log.setFromUser(dto.getFromUser());
		log.setToUser(StringUtils.join(dto.getToUser(),','));
		log.setTaskId(dto.getTaskId());
		log.setOperation(dto.getOperation());
		log.setIp(dto.getIp());
		
		return processLogDmn.add(log);
	}

}
