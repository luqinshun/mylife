package com.ep.activiti.service;

import java.util.Map;

public interface HistoricService {

	Map<String, Object> getHisTaskByUser(String userId, int page, int rows);

}
