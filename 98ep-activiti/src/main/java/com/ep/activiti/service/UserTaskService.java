package com.ep.activiti.service;

import java.util.Map;

public interface UserTaskService {

	public Map<String, Object> getAllTask(int page, int rows);

	public Map<String, Object> getPageUserTask(String userId, int page, int rows);

	/**
	 * 通过条件查询相应任务
	 * 
	 * @param searchKey
	 * @param page
	 * @param rows
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> searchTask(String searchKey, int page, int rows)
			throws Exception;
}
