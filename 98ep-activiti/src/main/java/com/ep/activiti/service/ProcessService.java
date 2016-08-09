package com.ep.activiti.service;

import java.util.Map;

import com.ep.activiti.entity.UserProcess;

public interface ProcessService {

	Map<String, Object> getAllProcessStartByUser(String userId, int page,
			int rows);

	UserProcess getProcessByProId(String processInstanceId);

	public UserProcess getProcessByBusiness(String businessKey)
			throws Exception;
}
