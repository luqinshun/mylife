package com.ep.activiti.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.ep.activiti.entity.CustomUser;
import com.ep.activiti.mapper.IdentityMapper;
import com.ep.activiti.service.CustomIdentityService;

public class CustomIdentityServiceImpl implements CustomIdentityService {

	@Autowired
	private IdentityMapper identityMapper;
	public void setIdentityMapper(IdentityMapper identityMapper) {
		this.identityMapper = identityMapper;
	}
	public IdentityMapper getIdentityMapper() {
		return identityMapper;
	}
	
	@Override
	public List<CustomUser> getProStartUserList() {
		List<CustomUser> list = identityMapper.getProStartUserList();
		return list;
	}

}
