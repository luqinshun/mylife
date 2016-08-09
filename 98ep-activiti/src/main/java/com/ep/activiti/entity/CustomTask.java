package com.ep.activiti.entity;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.joda.time.convert.Converter;

public class CustomTask {

	private String id;//任务名id	
	private String name;//任务名称	
	private String createTime;//创建时间  
	private String processDefinitionId;//流程定义id
	private String processInstanceId;//流程实例id
	private String businessKey;//业务key
	private String assignee;//签收人，为空：未签收，不为空：已签收
	private String authenticatedUserId;//发起人
	
	public String getAuthenticatedUserId() {
		return authenticatedUserId;
	}
	public void setAuthenticatedUserId(String authenticatedUserId) {
		this.authenticatedUserId = authenticatedUserId;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	public String getBusinessKey() {
		return businessKey;
	}
	public void setBusinessKey(String businessKey) {
		this.businessKey = businessKey;
	}
	
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getAssignee() {
		return assignee;
	}
	public void setAssignee(String assignee) {
		this.assignee = assignee;
	}
	@Override
	public String toString() {
		return "CustomTask [id=" + id + ", name=" + name + ", createTime="
				+ createTime + ", processDefinitionId=" + processDefinitionId
				+ ", processInstanceId=" + processInstanceId + ", businessKey="
				+ businessKey + ", assignee=" + assignee
				+ ", authenticatedUserId=" + authenticatedUserId + "]";
	}
	
	
	
}
