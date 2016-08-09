package com.ep.activiti.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ep.activiti.entity.HisUserTask;
import com.ep.activiti.mapper.HistoricMapper;
import com.ep.activiti.service.HistoricService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service
public class HistoricServiceImpl implements HistoricService{

	@Autowired
	private HistoricMapper historicMapper;
	public HistoricMapper getHistoricMapper() {
		return historicMapper;
	}
	public void setHistoricMapper(HistoricMapper historicMapper) {
		this.historicMapper = historicMapper;
	}


	@Override
	public Map<String, Object> getHisTaskByUser(String userId, int page,
			int rows) {
		Map<String, Object> map = new HashMap<String, Object>();
		PageHelper.startPage(page, rows);
		List<HisUserTask> list = historicMapper.getHisTaskByUser(userId);
		PageInfo<HisUserTask> pageInfo = new PageInfo<HisUserTask>(list);
		map.put("total", pageInfo.getTotal());
		map.put("rows", pageInfo.getList());
		return map;
	}

}
