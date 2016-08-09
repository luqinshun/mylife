package com.ep.activiti.service;




import java.util.Map;

import com.ep.activiti.entity.CustomTask;


public interface CustomTaskService {

	public Map<String,Object> queryUserTask(String userId,int index,int size);

	public CustomTask getTaskDetail(String taskId);
	
	public String getBusinessKey(String processInstanceId);
}
