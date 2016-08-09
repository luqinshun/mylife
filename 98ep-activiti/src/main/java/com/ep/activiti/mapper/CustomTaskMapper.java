package com.ep.activiti.mapper;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ep.activiti.entity.CustomTask;

@Repository("customTaskMapper")
public interface CustomTaskMapper {
	
	List<CustomTask> getUserTask(String userId);

	CustomTask getTaskDetail(String taskId);

	String getBusinessKey(String processInstanceId);
}
