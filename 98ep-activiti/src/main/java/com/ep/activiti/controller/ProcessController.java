package com.ep.activiti.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.ep.activiti.entity.UserProcess;
import com.ep.activiti.logUtil.LogUtil;
import com.ep.activiti.service.ProcessService;

@Controller
@RequestMapping(value = "process")
public class ProcessController {

	@Autowired
	private RepositoryService repositoryService;
	@Autowired
	private IdentityService identityService;
	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	private HistoryService historyService;
	@Autowired
	private TaskService taskService;
	@Autowired
	private ProcessService processService;
	@Autowired
	private LogUtil logUtil;

	private static final String SYS_TASK_DEF_KEY = "confirmOrder";
	private static final String SYS_ASSIGNEE = "system_auto";
	private static final String COMMON_TENANT = "common";

	private int taskCount = 0;

	/**
	 * 
	 * @param tenantId
	 *            租户id
	 * @param businessKey
	 * @param variables
	 * @return
	 */
	@RequestMapping(value = "start/{userId}/{tenantId}/{businessKey}/{level}", method = RequestMethod.POST)
	public ResponseEntity<Void> startProcess(
			@PathVariable("userId") String userId,
			@PathVariable("tenantId") String tenantId,
			@PathVariable("businessKey") String businessKey,
			@PathVariable("level") String level
	/* ,@RequestBody Map<String,Object> variables */) {

		try {
			ProcessDefinitionQuery processDefinitionQuery = repositoryService
					.createProcessDefinitionQuery();
			processDefinitionQuery.processDefinitionTenantId(tenantId);

			List<ProcessDefinition> processDefinitionList = processDefinitionQuery
					.list();
			String modelKey = "order_" + level;
			for (ProcessDefinition processDefinition : processDefinitionList) {
				if (modelKey.equals(processDefinition.getKey())) {
					break;
				}
			}

			/*
			 * Map<String,Object> variables = new HashMap<String,Object>();
			 * variables.put("money", 11000);
			 */

			// 设置流程发起人
			identityService.setAuthenticatedUserId(userId);
			// runtimeService.startProcessInstanceByKeyAndTenantId(modelKey,
			// businessKey, variables, tenantId);
			runtimeService.startProcessInstanceByKeyAndTenantId(modelKey,
					businessKey, tenantId);

			/*
			 * List<HistoricProcessInstance> list =
			 * historyService.createHistoricProcessInstanceQuery
			 * ().startedBy(userId).list(); for (HistoricProcessInstance
			 * historicProcessInstance : list) { List<HistoricTaskInstance>
			 * list2 =
			 * historyService.createHistoricTaskInstanceQuery().processInstanceId
			 * (historicProcessInstance.getId()).list(); if(list2.size() == 1){
			 * for (HistoricTaskInstance historicTaskInstance : list2) {
			 * if(historicTaskInstance
			 * .getTaskDefinitionKey().equals("confirmOrder")){
			 * taskService.claim(historicTaskInstance.getId(), "system");
			 * taskService.complete(historicTaskInstance.getId()); } } } }
			 */
			List<Task> list = taskService.createTaskQuery()
					.taskDefinitionKey(SYS_TASK_DEF_KEY).list();
			for (Task task : list) {
				taskService.claim(task.getId(), SYS_ASSIGNEE);
				taskService.complete(task.getId());
			}

			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		} catch (Exception e) {
			logUtil.writeLog("error", "启动某个用户专属流程出错", e);
		}

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
				null);
	}

	/**
	 * 启动通用
	 */
	@RequestMapping(value = "start/{userId}/{businessKey}/{level}", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Boolean>> startProcess(
			@PathVariable("userId") String userId,
			@PathVariable("businessKey") String businessKey,
			@PathVariable("level") String level
	/* ,@RequestBody Map<String,Object> variables */) {
		Map<String, Boolean> map = new HashMap<String, Boolean>();
		try {
			ProcessDefinitionQuery processDefinitionQuery = repositoryService
					.createProcessDefinitionQuery();
			processDefinitionQuery.processDefinitionTenantId(COMMON_TENANT);

			String modelKey = "order_" + level;
			List<ProcessDefinition> processDefinitionList = processDefinitionQuery
					.processDefinitionKey(modelKey).list();
			if (processDefinitionList.size() != 0) {
				// 设置流程发起人
				identityService.setAuthenticatedUserId(userId);
				runtimeService.startProcessInstanceByKeyAndTenantId(modelKey,
						businessKey, COMMON_TENANT);

				List<Task> list = taskService.createTaskQuery()
						.taskDefinitionKey(SYS_TASK_DEF_KEY).list();
				if (list.size() != 0) {
					for (Task task : list) {
						taskService.claim(task.getId(), SYS_ASSIGNEE);
						taskService.complete(task.getId());
					}
				}
				map.put("status", true);
				return ResponseEntity.ok(map);
			}
			map.put("status", false);
			return ResponseEntity.ok(map);
		} catch (Exception e) {
			logUtil.writeLog("error", "启动下单审核流程出错", e);
		}
		map.put("status", false);
		return ResponseEntity.ok(map);
	}

	/**
	 * 获得由指定用户发起的所有流程
	 * 
	 * @param userId
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping(value = "userProcess/{userId}", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> getAllProcessStartByUser(
			@PathVariable("userId") String userId,
			@RequestParam("page") int page, @RequestParam("rows") int rows) {
		try {

			Map<String, Object> map = processService.getAllProcessStartByUser(
					userId, page, rows);
			return ResponseEntity.ok(map);
		} catch (Exception e) {
			logUtil.writeLog("error", "查询用户发起的所有流程出错", e);
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
				null);
	}

	/**
	 * 根据流程实例id获取流程信息
	 * 
	 * @param processInstanceId
	 * @return
	 */
	@RequestMapping(value = "detail/{processInstanceId}", method = RequestMethod.GET)
	public ResponseEntity<UserProcess> getProcessByProId(
			@PathVariable("processInstanceId") String processInstanceId) {
		try {
			UserProcess userProcess = processService
					.getProcessByProId(processInstanceId);
			return ResponseEntity.ok(userProcess);
		} catch (Exception e) {
			logUtil.writeLog("error", "查询流程详细信息出错", e);
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
				null);
	}

	/**
	 * 订单生成失败时流程的回滚和订单的作废
	 * 
	 * @param businessKey
	 * @return
	 * @throws Exception
	 */

	@RequestMapping(value = "end/{businessKey}", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Boolean>> endProcess(
			@PathVariable("businessKey") String businessKey) throws Exception {
		Map<String, Boolean> result = new HashMap<String, Boolean>();
		try {
			UserProcess userProcess = processService
					.getProcessByBusiness(businessKey);
			if (userProcess != null) {
				String proInstId = userProcess.getProcessInstanceId();
				runtimeService
						.deleteProcessInstance(proInstId, "订单生成失败工作流流程回滚");
				result.put("status", true);
			} else {
				result.put("status", false);
			}

		} catch (Exception e) {
			result.put("status", false);
			e.printStackTrace();
			logUtil.writeLog("error", "订单生成失败时流程的回滚", e);
		}
		return ResponseEntity.ok(result);

	}

	/**
	 * 流程审核自动完成接口
	 * 
	 * @param processInstanceId
	 * @return
	 */
	@RequestMapping(value = "passAll/{userId}/{businessKey}", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Boolean>> passAllTask(
			@PathVariable("userId") String userId,
			@PathVariable("businessKey") String businessKey) {
		Map<String, Boolean> map = new HashMap<String, Boolean>();
		try {
			ProcessDefinitionQuery processDefinitionQuery = repositoryService
					.createProcessDefinitionQuery();
			processDefinitionQuery.processDefinitionTenantId(COMMON_TENANT);

			String modelKey = "order_4";
			List<ProcessDefinition> processDefinitionList = processDefinitionQuery
					.processDefinitionKey(modelKey).list();
			if (processDefinitionList.size() != 0) {
				// 设置流程发起人
				identityService.setAuthenticatedUserId(userId);
				runtimeService.startProcessInstanceByKeyAndTenantId(modelKey,
						businessKey, COMMON_TENANT);
				// 自动审批流程，递归调用
				Task task = taskService.createTaskQuery()
						.processInstanceBusinessKey(businessKey).singleResult();
				if (task != null) {
					taskService.claim(task.getId(), SYS_ASSIGNEE);
					taskService.complete(task.getId());
				}
				map.put("status", true);
				return ResponseEntity.ok(map);
			}
			map.put("status", false);
			return ResponseEntity.ok(map);
		} catch (Exception e) {
			logUtil.writeLog("error", "自动审核流程异常", e);
		}
		map.put("status", false);
		return ResponseEntity.ok(map);
	}

}
