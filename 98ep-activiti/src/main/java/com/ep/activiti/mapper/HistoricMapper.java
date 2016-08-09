package com.ep.activiti.mapper;

import java.util.List;

import com.ep.activiti.entity.HisUserTask;

public interface HistoricMapper {

	List<HisUserTask> getHisTaskByUser(String userId);

}
