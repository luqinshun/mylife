package com.ep.activiti.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricProcessInstanceQuery;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.runtime.ProcessInstanceQuery;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.ep.activiti.entity.CustomTask;
import com.ep.activiti.entity.UserTask;
import com.ep.activiti.logUtil.LogUtil;
import com.ep.activiti.service.AdminUserService;
import com.ep.activiti.service.CustomTaskService;
import com.ep.activiti.service.UserTaskService;

@Controller
@RequestMapping(value = "/task")
public class TaskController {

	@Autowired
	private TaskService taskService;
	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	private FormService formService;
	@Autowired
	private RepositoryService repositoryService;
	@Autowired
	private HistoryService historyService;
	@Autowired
	private CustomTaskService customTaskService;
	@Autowired
	private UserTaskService userTaskService;
	@Autowired
	private LogUtil logUtil;
	@Autowired
	private AdminUserService adminUserService;

	private static final String resource_name = ".bpmn20.xml";

	/**
	 * 线上接口，查询指定用的任务
	 * 
	 * @param userId
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping(value = "userTask/{userId}", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> getUserTask(
			@PathVariable("userId") String userId,
			@RequestParam("page") int page, @RequestParam("rows") int rows) {
		try {
			Map<String, Object> map = customTaskService.queryUserTask(userId,
					page, rows);
			return ResponseEntity.ok(map);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logUtil.writeLog("error", "查询用户任务出错", e);
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
				null);
	}

	/**
	 * 线上接口，查询指定任务详情
	 * 
	 * @param taskId
	 * @return
	 */
	@RequestMapping(value = "detail/{taskId}", method = RequestMethod.GET)
	public ResponseEntity<CustomTask> getTaskDetail(
			@PathVariable("taskId") String taskId) {

		try {
			CustomTask task = customTaskService.getTaskDetail(taskId);
			if (task != null) {
				return ResponseEntity.ok(task);
			} else {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
						.body(null);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logUtil.writeLog("error", "查询任务详情出错", e);
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
				null);
	}

	/**
	 * 管理系统接口，获得指定用户的任务
	 * 
	 * @param userId
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping(value = "pageUserTask/{userId}", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> getPageUserTask(
			@PathVariable("userId") String userId,
			@RequestParam("page") int page, @RequestParam("rows") int rows) {
		try {
			Map<String, Object> map = userTaskService.getPageUserTask(userId,
					page, rows);
			return ResponseEntity.ok(map);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logUtil.writeLog("error", "管理系统查询用户任务出错", e);
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
				null);
	}

	/**
	 * 管理系统接口，查询所有的任务
	 * 
	 * @return
	 */
	@RequestMapping(value = "allTask", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> getAllTask(
			@RequestParam("page") int page, @RequestParam("rows") int rows) {
		try {
			Map<String, Object> map = userTaskService.getAllTask(page, rows);
			return ResponseEntity.ok(map);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logUtil.writeLog("error", "查询所有任务出错", e);
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
				null);
	}

	/**
	 * 流程图不设审批人，查询指定tenantId下的所有任务
	 * 
	 * @param tenantId
	 * @return
	 */
	@RequestMapping(value = "tenantTask/{tenantId}", method = RequestMethod.GET)
	public ResponseEntity<List<UserTask>> getTaskList(
			@PathVariable("tenantId") String tenantId) {

		try {
			List<Task> taskList = taskService.createTaskQuery()
					.taskTenantId(tenantId).list();
			List<UserTask> list = new ArrayList<UserTask>();
			if (taskList != null) {
				for (Task userTask : taskList) {
					UserTask task = new UserTask();
					// 给自定义任务赋值
					String taskId = userTask.getId();
					task.setId(taskId);
					task.setName(userTask.getName());

					task.setTaskDefinitionKey(userTask.getTaskDefinitionKey());

					task.setCreateTime(new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss").format(userTask
							.getCreateTime()));

					task.setTenantId(userTask.getTenantId());

					String assignee = userTask.getAssignee();

					String processDefinitionId = userTask
							.getProcessDefinitionId();
					task.setProcessDefinitionId(processDefinitionId);

					// 查询出任务所属的模型key并赋值
					ProcessDefinitionQuery processDefinitionQuery = repositoryService
							.createProcessDefinitionQuery()
							.processDefinitionId(processDefinitionId);
					List<ProcessDefinition> processDefinitionListNotClaim = processDefinitionQuery
							.list();
					for (ProcessDefinition processDefinition : processDefinitionListNotClaim) {
						task.setModelKey(processDefinition.getKey());
						String resourceName = processDefinition
								.getResourceName();
						String modelName = resourceName.replace(resource_name,
								"").trim();
						task.setModelName(modelName);
					}

					String processInstanceId = userTask.getProcessInstanceId();
					task.setProcessInstanceId(processInstanceId);

					// 查询流程变量
					Map<String, Object> variablesNotClaim = runtimeService
							.getVariables(processInstanceId);

					// 查询流程发起人
					HistoricProcessInstanceQuery historicProcessInstanceQuery = historyService
							.createHistoricProcessInstanceQuery()
							.processInstanceId(processInstanceId);
					List<HistoricProcessInstance> historicProcessInstanceList = historicProcessInstanceQuery
							.list();
					for (HistoricProcessInstance historicProcessInstance : historicProcessInstanceList) {
						task.setAuthenticatedUserId(historicProcessInstance
								.getStartUserId());
					}

					// 查询出业务key并赋值
					ProcessInstanceQuery processInstanceQuery = runtimeService
							.createProcessInstanceQuery().processInstanceId(
									processInstanceId);
					List<ProcessInstance> processInstanceListNotClaim = processInstanceQuery
							.list();
					for (ProcessInstance processInstance : processInstanceListNotClaim) {
						task.setBusinessKey(processInstance.getBusinessKey());
					}
					list.add(task);
				}
				return ResponseEntity.ok(list);
			}
			List<UserTask> empty = new ArrayList<UserTask>();
			return ResponseEntity.ok(empty);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
				null);
	}

	/**
	 * 签收任务
	 * 
	 * @param userId
	 *            签收用户id
	 * @param taskId
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "claim/{userId}/{id}", method = RequestMethod.POST)
	public ResponseEntity<Void> claimTask(
			@PathVariable("userId") String userId,
			@PathVariable("id") String taskId,
			@RequestParam(value = "userName", required = false) String userName) {
		try {
			taskService.claim(taskId, userId);
			// 修改用户名的名称,防止名称修改，导致数据数据错误
			try {
				if (userName != null && !"".equals(userName)) {
					adminUserService.updateUserName(userName, userId);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logUtil.writeLog("error", "签收任务出错", e);
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
				null);

	}

	/**
	 * 办理任务
	 * 
	 * @param taskId
	 * @param variables
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "complete/{id}", method = RequestMethod.POST)
	public ResponseEntity<Void> completeTask(@PathVariable("id") String taskId,
			@RequestParam("description") String description,
			@RequestParam("pass") boolean pass
	/* ,@RequestBody Map<String, Object> variables */) {
		try {
			/*
			 * //创建进行变量筛选后的map Map<String, Object> map = new HashMap<String,
			 * Object>();
			 * 
			 * Map<String,Object> variables = new HashMap<String,Object>();
			 * variables.put("pass", false);
			 * 
			 * if(variables != null || variables.size()>0){ //
			 * 根据任务id获取创建模板时以表单数据方式设置的流程变量 TaskFormData formData =
			 * formService.getTaskFormData(taskId);
			 * 
			 * // 获取详细的表单项 List<FormProperty> list =
			 * formData.getFormProperties(); for (FormProperty formProperty :
			 * list) { // 获取formProperty的id，和流程变量进行比较 String id =
			 * formProperty.getId();
			 * 
			 * Set<String> keySet = variables.keySet(); for (String key :
			 * keySet) { if (key.equals(id)) { map.put(id, variables.get(key));
			 * } } } }
			 */
			// 完成任务
			// taskService.complete(taskId, map);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put(taskId, description);
			map.put("pass", pass);
			taskService.complete(taskId, map);
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logUtil.writeLog("error", "办理任务出错", e);
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
				null);
	}

	/**
	 * 状态！创建时间！审批时间！审批人！创建人！任务编号！ ||现在只查询审批人和任务编号
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "search", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> searchTask(
			@RequestParam("searchKey") String searchKey,
			@RequestParam("page") int page, @RequestParam("rows") int rows) {
		try {
			searchKey = new String(searchKey.getBytes("iso8859-1"), "utf-8");
			Map<String, Object> taskMap = userTaskService.searchTask(searchKey,
					page, rows);
			return ResponseEntity.ok(taskMap);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
				null);
	}

}
