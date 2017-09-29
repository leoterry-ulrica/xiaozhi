package com.dist.bdf.facade.service.biz.impl;

import org.springframework.stereotype.Service;

import com.dist.bdf.base.security.DesEncryptRSA;
import com.dist.bdf.facade.service.security.SecurityParaService;
import com.dist.bdf.model.dto.security.RSAPubModulusExponentDTO;

@Service("SecurityParaService")
public class SecurityParaServiceImpl implements SecurityParaService {

	
	@Override
	public String getDefaultPublicModulusHex() {
	
		return ((DesEncryptRSA)DesEncryptRSA.getInstance()).getPublicModulus(true);
	}

	@Override
	public String getDefaultPublicExponentHex() {

		return ((DesEncryptRSA)DesEncryptRSA.getInstance()).getPublicExponent(true);
	}

	@Override
	public Object getDefaultPublicModulusAndExponentHex() {
	
		RSAPubModulusExponentDTO dto = new RSAPubModulusExponentDTO();
		dto.setModulus(((DesEncryptRSA)DesEncryptRSA.getInstance()).getPublicModulus(true));
		dto.setExponent(((DesEncryptRSA)DesEncryptRSA.getInstance()).getPublicExponent(true));
		
		return dto;
	}

}
