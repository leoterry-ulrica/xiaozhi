package com.dist.bdf.facade.service.biz.impl;

import org.springframework.stereotype.Service;

import com.dist.bdf.base.security.DesEncryptRSA;
import com.dist.bdf.facade.service.security.SecurityService;

@Service("SecurityService")
public class SecurityServiceImpl implements SecurityService {

	@Override
	public String encryptRSA(String text) {
		
		return DesEncryptRSA.getInstance().encrypt(text);
	}

	@Override
	public String decryptRSA(String text) {
		// TODO Auto-generated method stub
		return null;
	}

}
