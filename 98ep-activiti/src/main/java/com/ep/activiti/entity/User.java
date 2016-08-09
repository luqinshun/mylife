package com.ep.activiti.entity;

import org.codehaus.jackson.annotate.JsonProperty;

public class User {

	@JsonProperty(value = "Name")
	private String name;
	@JsonProperty(value = "UserId")
	private int userId;
	@JsonProperty(value = "UserName")
	private String userName;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
}
