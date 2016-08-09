package com.ep.activiti.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.ep.activiti.entity.UserActivitiInst;

public interface UserActivitiInstMapper {
	/**
	 * 查询历史节点表中的数据
	 * 
	 * @param procInstId
	 * @return
	 */
	public List<UserActivitiInst> getActInstByProcId(
			@Param("proceInstId") String processInstanceId,
			@Param("type") String type);

	/**
	 * 通过条件查询任务
	 * 
	 * @param params
	 * @return
	 */
	public List<UserActivitiInst> searchCheckTask(Map<String, String> params);
}
