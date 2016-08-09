package com.ep.activiti.mapper;

import java.util.List;

import com.ep.activiti.entity.TaskListenerHisAct;

/**
 * 历史任务查询接口
 *
 */
public interface TaskHisActMapper {

	/**
	 * 通过流程实例获取相应的历史任务数据
	 * 
	 * @param processInstanceId
	 * @return
	 * @throws Exception
	 */
	public List<TaskListenerHisAct> getListenerHisTask(String processInstanceId)
			throws Exception;

}
