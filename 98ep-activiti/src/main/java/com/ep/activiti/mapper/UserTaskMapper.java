package com.ep.activiti.mapper;

import java.util.List;

import com.ep.activiti.entity.UserTask;

public interface UserTaskMapper {

	List<UserTask> getAllTask();

	List<UserTask> getPageUserTask(String userId);

}
