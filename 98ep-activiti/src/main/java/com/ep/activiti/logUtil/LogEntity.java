package com.ep.activiti.logUtil;

import com.alibaba.fastjson.annotation.JSONField;

public class LogEntity {

	@JSONField(name = "LogType")
	private String logType;
	@JSONField(name = "Title")
	private String title;
	@JSONField(name = "Message")
	private String message;
	@JSONField(name = "DateTime")
	private String dateTime;
	@JSONField(name = "Source")
	private String source;
	@JSONField(name = "Ip")
	private String ip;
	public String getLogType() {
		return logType;
	}
	public void setLogType(String logType) {
		this.logType = logType;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getDateTime() {
		return dateTime;
	}
	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	
	
}
