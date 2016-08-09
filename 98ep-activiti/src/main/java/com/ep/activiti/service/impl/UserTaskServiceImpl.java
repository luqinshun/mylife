package com.ep.activiti.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ep.activiti.entity.UserActivitiInst;
import com.ep.activiti.entity.UserTask;
import com.ep.activiti.mapper.UserActivitiInstMapper;
import com.ep.activiti.mapper.UserTaskMapper;
import com.ep.activiti.service.UserTaskService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service
@Transactional(readOnly = true)
public class UserTaskServiceImpl implements UserTaskService {

	@Autowired
	private UserTaskMapper userTaskMapper;

	@Autowired
	private UserActivitiInstMapper actInstMapper;

	public UserActivitiInstMapper getActInstMapper() {
		return actInstMapper;
	}

	public void setActInstMapper(UserActivitiInstMapper actInstMapper) {
		this.actInstMapper = actInstMapper;
	}

	public UserTaskMapper getUserTaskMapper() {
		return userTaskMapper;
	}

	public void setUserTaskMapper(UserTaskMapper userTaskMapper) {
		this.userTaskMapper = userTaskMapper;
	}

	@Override
	public Map<String, Object> getAllTask(int page, int rows) {
		Map<String, Object> map = new HashMap<String, Object>();
		PageHelper.startPage(page, rows);
		List<UserTask> list = userTaskMapper.getAllTask();
		PageInfo<UserTask> pageInfo = new PageInfo<UserTask>(list);
		map.put("total", pageInfo.getTotal());
		map.put("rows", pageInfo.getList());
		return map;
	}

	@Override
	public Map<String, Object> getPageUserTask(String userId, int page, int rows) {
		Map<String, Object> map = new HashMap<String, Object>();
		PageHelper.startPage(page, rows);
		List<UserTask> list = userTaskMapper.getPageUserTask(userId);
		PageInfo<UserTask> pageInfo = new PageInfo<UserTask>(list);
		map.put("total", pageInfo.getTotal());
		map.put("rows", pageInfo.getList());
		return map;
	}

	@Override
	public Map<String, Object> searchTask(String searchKey, int page, int rows)
			throws Exception {
		Map<String, String> params = new HashMap<String, String>();
		Map<String, Object> results = new HashMap<String, Object>();

		if (searchKey == null || "".endsWith(searchKey)) {
			params.put("userName", "");
		} else {
			if (searchKey.matches("\\d*")) {// 判断为任务编号
				params.put("taskId", searchKey);
			} else {// 判断为审批人
				params.put("userName", searchKey);
			}
		}
		PageHelper.startPage(page, rows);
		List<UserActivitiInst> taskList = actInstMapper.searchCheckTask(params);

		PageInfo<UserActivitiInst> pageInfo = new PageInfo<UserActivitiInst>(
				taskList);
		results.put("total", pageInfo.getTotal());
		results.put("rows", pageInfo.getList());
		return results;
	}

}
