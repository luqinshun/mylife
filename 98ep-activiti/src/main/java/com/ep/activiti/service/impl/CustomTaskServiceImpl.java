package com.ep.activiti.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ep.activiti.entity.CustomTask;
import com.ep.activiti.mapper.CustomTaskMapper;
import com.ep.activiti.service.CustomTaskService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service
public class CustomTaskServiceImpl implements CustomTaskService {

	@Autowired
	private CustomTaskMapper customTaskMapper;

	public CustomTaskMapper getCustomTaskMapper() {
		return customTaskMapper;
	}

	public void setCustomTaskMapper(CustomTaskMapper customTaskMapper) {
		this.customTaskMapper = customTaskMapper;
	}

	@Override
	public Map<String, Object> queryUserTask(String userId, int page, int rows) {

		Map<String, Object> map = new HashMap<String, Object>();
		PageHelper.startPage(page, rows);
		List<CustomTask> list = customTaskMapper.getUserTask(userId);
		List<CustomTask> filterList = new ArrayList<CustomTask>();
		for (CustomTask customTask : list) {
			if (customTask.getAssignee() == null
					|| userId.equals(customTask.getAssignee())) {
				filterList.add(customTask);
			}
		}
		PageInfo<CustomTask> pageInfo = new PageInfo<CustomTask>(filterList);
		map.put("total", pageInfo.getTotal());
		map.put("rows", pageInfo.getList());
		return map;
	}

	@Override
	public CustomTask getTaskDetail(String taskId) {

		return customTaskMapper.getTaskDetail(taskId);
	}

	@Override
	public String getBusinessKey(String processInstanceId) {
		return customTaskMapper.getBusinessKey(processInstanceId);
	}

}
