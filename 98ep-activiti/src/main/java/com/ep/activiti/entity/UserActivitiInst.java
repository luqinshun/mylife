package com.ep.activiti.entity;

import java.util.Date;

/**
 * @author LiXiangkai 流程节点表
 */
public class UserActivitiInst {
	private String id;// 历史工作流实例表主键
	private String processInstanceId;// 流程实例ID
	private String processDefinitionId;// 流程定义ID
	private String activitiId;// 流程节点ID
	private String activitiType;// 流程节点类型，如UserTask,ServiceTask,StartEvent
	private String assignee;// 流程执行者
	private String endTime;// 结束时间
	private String taskId;// 任务编号
	private String taskName;// 任务名称
	private String assigneeName;// 流程执行者名称

	private String executionId;// 流程任务执行对象
	private String callProcInstId;
	private Date startTime;// 创建时间
	private Long duration;// 任务执行所花时间
	private String tentantId;// 使用范围一般为common

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	public String getProcessDefinitionId() {
		return processDefinitionId;
	}

	public void setProcessDefinitionId(String processDefinitionId) {
		this.processDefinitionId = processDefinitionId;
	}

	public String getActivitiId() {
		return activitiId;
	}

	public void setActivitiId(String activitiId) {
		this.activitiId = activitiId;
	}

	public String getActivitiType() {
		return activitiType;
	}

	public void setActivitiType(String activitiType) {
		this.activitiType = activitiType;
	}

	public String getAssignee() {
		return assignee;
	}

	public void setAssignee(String assignee) {
		this.assignee = assignee;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getAssigneeName() {
		return assigneeName;
	}

	public void setAssigneeName(String assigneeName) {
		this.assigneeName = assigneeName;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public String getExecutionId() {
		return executionId;
	}

	public void setExecutionId(String executionId) {
		this.executionId = executionId;
	}

	public String getCallProcInstId() {
		return callProcInstId;
	}

	public void setCallProcInstId(String callProcInstId) {
		this.callProcInstId = callProcInstId;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Long getDuration() {
		return duration;
	}

	public void setDuration(Long duration) {
		this.duration = duration;
	}

	public String getTentantId() {
		return tentantId;
	}

	public void setTentantId(String tentantId) {
		this.tentantId = tentantId;
	}

}
