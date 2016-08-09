package com.ep.activiti.entity;

public class CustomHisTaskIns {
	private String hisTaskInsId;
	private String processDefinitionId;
	private String processInstanceId;
	private String taskDefinitionKey;
	private String taskName;
	private String startTime;
	private String claimTime;
	private String endTime;
	private String dutation;
	private String assignee;
	private StringBuffer status;
	private String description;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public StringBuffer getStatus() {
		return status;
	}

	public void setStatus(StringBuffer status2) {
		this.status = status2;
	}

	public String getAssignee() {
		return assignee;
	}

	public void setAssignee(String assignee) {
		this.assignee = assignee;
	}

	public String getHisTaskInsId() {
		return hisTaskInsId;
	}

	public void setHisTaskInsId(String hisTaskInsId) {
		this.hisTaskInsId = hisTaskInsId;
	}

	public String getProcessDefinitionId() {
		return processDefinitionId;
	}

	public void setProcessDefinitionId(String processDefinitionId) {
		this.processDefinitionId = processDefinitionId;
	}

	public String getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	public String getTaskDefinitionKey() {
		return taskDefinitionKey;
	}

	public void setTaskDefinitionKey(String taskDefinitionKey) {
		this.taskDefinitionKey = taskDefinitionKey;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getClaimTime() {
		return claimTime;
	}

	public void setClaimTime(String claimTime) {
		this.claimTime = claimTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getDutation() {
		return dutation;
	}

	public void setDutation(String dutation) {
		this.dutation = dutation;
	}
}
