package com.ep.activiti.mapper;

import java.util.List;

import com.ep.activiti.entity.UserProcess;

public interface ProcessMapper {

	public List<UserProcess> getAllProcessStartByUser(String userId);

	public UserProcess getProcessByProId(String processInstanceId);

	public UserProcess getProcessByBusiness(String businessKey)
			throws Exception;

}
