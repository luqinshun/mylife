package com.ep.activiti.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.HistoryService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricProcessInstanceQuery;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricTaskInstanceQuery;
import org.activiti.engine.history.HistoricVariableInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ep.activiti.entity.CustomHisProIns;
import com.ep.activiti.entity.CustomHisTaskIns;
import com.ep.activiti.logUtil.LogUtil;
import com.ep.activiti.service.HistoricService;

@Controller
@RequestMapping("history")
public class HistoricController {

	final String ALL = "all";
	final String ONGOING = "ongoing";
	final String END = "end";
	final String CONDITIONUSER = "ConditionUser";
	
	@Autowired
	private HistoryService historyService;
	@Autowired
	private HistoricService historicService;
	@Autowired
	private LogUtil logUtil;

	/**
	 * 查询由指定用户发起的所有的历史流程
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "userAll/{userId}/{state}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> getUserHistoryProcess(
			@PathVariable("userId") String userId,@PathVariable("state") String state,
			@RequestParam("page") int page,@RequestParam("rows") int rows) {
		try {
			Map<String,Object> map = new HashMap<String,Object>();
			
			HistoricProcessInstanceQuery query = historyService
					.createHistoricProcessInstanceQuery().startedBy(userId).orderByProcessInstanceStartTime().desc();
			int total = query.list().size();
			int firstResult = (page-1)*rows;
			List<HistoricProcessInstance> historicProInsList = query.listPage(firstResult, rows);
			if(total != 0){
				if(ALL.equals(state)){
					List<CustomHisProIns> list = new ArrayList<CustomHisProIns>();
					for (HistoricProcessInstance historicProcessInstance : historicProInsList) {
						CustomHisProIns customHisProIns = new CustomHisProIns();
						customHisProIns.setHisProInsId(historicProcessInstance.getId());
						customHisProIns.setProcessInstanceId(historicProcessInstance
								.getId());
						customHisProIns.setBusinessKey(historicProcessInstance
								.getBusinessKey());
						customHisProIns.setProcessDefinitionId(historicProcessInstance
								.getProcessDefinitionId());
						if (historicProcessInstance.getStartTime() != null) {
							customHisProIns.setStartTime(new SimpleDateFormat(
									"yyyy-MM-dd HH:mm:ss")
									.format(historicProcessInstance.getStartTime()));
						}
						if (historicProcessInstance.getEndTime() != null) { 	
							customHisProIns.setEndTime(new SimpleDateFormat(
									"yyyy-MM-dd HH:mm:ss")
									.format(historicProcessInstance.getEndTime()));
						}
						if (historicProcessInstance.getDurationInMillis() != null) {
							long duration = historicProcessInstance.getDurationInMillis();
							customHisProIns.setDuration(formatTime(duration));
							customHisProIns.setStatus("已结束");
						}else{
							customHisProIns.setStatus("未结束");
						}
						list.add(customHisProIns);
					}
					map.put("total", total);
					map.put("rows", list);
					return ResponseEntity.ok(map);
				}
				if(ONGOING.equals(state)){
					List<CustomHisProIns> list = new ArrayList<CustomHisProIns>();
					for (HistoricProcessInstance historicProcessInstance : historicProInsList) {
						if(historicProcessInstance.getEndTime() == null){
							CustomHisProIns customHisProIns = new CustomHisProIns();
							customHisProIns.setHisProInsId(historicProcessInstance.getId());
							customHisProIns.setProcessInstanceId(historicProcessInstance
									.getId());
							customHisProIns.setBusinessKey(historicProcessInstance
									.getBusinessKey());
							customHisProIns.setProcessDefinitionId(historicProcessInstance
									.getProcessDefinitionId());
							if (historicProcessInstance.getStartTime() != null) {
								customHisProIns.setStartTime(new SimpleDateFormat(
										"yyyy-MM-dd HH:mm:ss")
										.format(historicProcessInstance.getStartTime()));
							}
							customHisProIns.setStatus("未结束");
							list.add(customHisProIns);
						}
					}
					map.put("total", list.size());
					map.put("rows", list);
					return ResponseEntity.ok(map);
				}
				if(END.equals(state)){
					List<CustomHisProIns> list = new ArrayList<CustomHisProIns>();
					for (HistoricProcessInstance historicProcessInstance : historicProInsList) {
						if(historicProcessInstance.getEndTime() != null){
							CustomHisProIns customHisProIns = new CustomHisProIns();
							customHisProIns.setHisProInsId(historicProcessInstance.getId());
							customHisProIns.setProcessInstanceId(historicProcessInstance
									.getId());
							customHisProIns.setBusinessKey(historicProcessInstance
									.getBusinessKey());
							customHisProIns.setProcessDefinitionId(historicProcessInstance
									.getProcessDefinitionId());
							if (historicProcessInstance.getStartTime() != null) {
								customHisProIns.setStartTime(new SimpleDateFormat(
										"yyyy-MM-dd HH:mm:ss")
										.format(historicProcessInstance.getStartTime()));
							}
							if (historicProcessInstance.getEndTime() != null) {
								customHisProIns.setEndTime(new SimpleDateFormat(
										"yyyy-MM-dd HH:mm:ss")
										.format(historicProcessInstance.getEndTime()));
							}
							if (historicProcessInstance.getDurationInMillis() != null) {
								long duration = historicProcessInstance.getDurationInMillis();
								customHisProIns.setDuration(formatTime(duration));
							}
							customHisProIns.setStatus("已结束");
							list.add(customHisProIns);
						}
					}
					map.put("total", list.size());
					map.put("rows", list);
					return ResponseEntity.ok(map);
				}
				
			}else{
				List<HistoricProcessInstance> list = new ArrayList<HistoricProcessInstance>();
				map.put("total", total);
				map.put("rows", list);
				return ResponseEntity.ok(map);
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}
	
	/**
	 * 查询所有历史流程信息
	 * @param state
	 * @return
	 */
	@RequestMapping(value = "all/{userId}/{state}",method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> getAllHistoryProcess(@PathVariable("userId") String userId,
			@PathVariable("state") String state,@RequestParam("page") int page,
			@RequestParam("rows") int rows){
		
		try {
			Map<String,Object> map = new HashMap<String,Object>();
			List<HistoricProcessInstance> historicProInsList = new ArrayList<HistoricProcessInstance>();
			int firstResult = (page-1)*rows;
			int total;
			if(CONDITIONUSER.equals(userId)){
				HistoricProcessInstanceQuery query = historyService
						.createHistoricProcessInstanceQuery().orderByProcessInstanceStartTime().desc();
				total= query.list().size();
				historicProInsList = query.listPage(firstResult, rows);
			}else{
				HistoricProcessInstanceQuery query = historyService
						.createHistoricProcessInstanceQuery().startedBy(userId).orderByProcessInstanceStartTime().desc();
				total = query.list().size();
				historicProInsList = query.listPage(firstResult, rows);
			}
			
			if(total != 0){
				if(ALL.equals(state)){
					List<CustomHisProIns> list = new ArrayList<CustomHisProIns>();
					for (HistoricProcessInstance historicProcessInstance : historicProInsList) {
						CustomHisProIns customHisProIns = new CustomHisProIns();
						customHisProIns.setHisProInsId(historicProcessInstance.getId());
						customHisProIns.setProcessInstanceId(historicProcessInstance
								.getId());
						customHisProIns.setBusinessKey(historicProcessInstance
								.getBusinessKey());
						customHisProIns.setProcessDefinitionId(historicProcessInstance
								.getProcessDefinitionId());
						if (historicProcessInstance.getStartTime() != null) {
							customHisProIns.setStartTime(new SimpleDateFormat(
									"yyyy-MM-dd HH:mm:ss")
									.format(historicProcessInstance.getStartTime()));
						}
						if (historicProcessInstance.getEndTime() != null) {
							customHisProIns.setEndTime(new SimpleDateFormat(
									"yyyy-MM-dd HH:mm:ss")
									.format(historicProcessInstance.getEndTime()));
						}
						if (historicProcessInstance.getDurationInMillis() != null) {
							long duration = historicProcessInstance.getDurationInMillis();
							customHisProIns.setDuration(formatTime(duration));
							customHisProIns.setStatus("已结束");
						}else{
							customHisProIns.setStatus("未结束");
						}
						customHisProIns.setStartUser(historicProcessInstance.getStartUserId());
						list.add(customHisProIns);
					}
					map.put("total", total);
					map.put("rows", list);
					return ResponseEntity.ok(map);
				}
				if(ONGOING.equals(state)){
					List<CustomHisProIns> list = new ArrayList<CustomHisProIns>();
					for (HistoricProcessInstance historicProcessInstance : historicProInsList) {
						if(historicProcessInstance.getEndTime() == null){
							CustomHisProIns customHisProIns = new CustomHisProIns();
							customHisProIns.setHisProInsId(historicProcessInstance.getId());
							customHisProIns.setProcessInstanceId(historicProcessInstance
									.getId());
							customHisProIns.setBusinessKey(historicProcessInstance
									.getBusinessKey());
							customHisProIns.setProcessDefinitionId(historicProcessInstance
									.getProcessDefinitionId());
							if (historicProcessInstance.getStartTime() != null) {
								customHisProIns.setStartTime(new SimpleDateFormat(
										"yyyy-MM-dd HH:mm:ss")
										.format(historicProcessInstance.getStartTime()));
							}
							customHisProIns.setStatus("未结束");
							customHisProIns.setStartUser(historicProcessInstance.getStartUserId());
							list.add(customHisProIns);
						}
					}
					map.put("total", list.size());
					map.put("rows", list);
					return ResponseEntity.ok(map);
				}
				if(END.equals(state)){
					List<CustomHisProIns> list = new ArrayList<CustomHisProIns>();
					for (HistoricProcessInstance historicProcessInstance : historicProInsList) {
						if(historicProcessInstance.getEndTime() != null){
							CustomHisProIns customHisProIns = new CustomHisProIns();
							customHisProIns.setHisProInsId(historicProcessInstance.getId());
							customHisProIns.setProcessInstanceId(historicProcessInstance
									.getId());
							customHisProIns.setBusinessKey(historicProcessInstance
									.getBusinessKey());
							customHisProIns.setProcessDefinitionId(historicProcessInstance
									.getProcessDefinitionId());
							if (historicProcessInstance.getStartTime() != null) {
								customHisProIns.setStartTime(new SimpleDateFormat(
										"yyyy-MM-dd HH:mm:ss")
										.format(historicProcessInstance.getStartTime()));
							}
							if (historicProcessInstance.getEndTime() != null) {
								customHisProIns.setEndTime(new SimpleDateFormat(
										"yyyy-MM-dd HH:mm:ss")
										.format(historicProcessInstance.getEndTime()));
							}
							if (historicProcessInstance.getDurationInMillis() != null) {
								long duration = historicProcessInstance.getDurationInMillis();
								customHisProIns.setDuration(formatTime(duration));
							}
							customHisProIns.setStatus("已结束");
							customHisProIns.setStartUser(historicProcessInstance.getStartUserId());
							list.add(customHisProIns);
						}
					}
					map.put("total", list.size());
					map.put("rows", list);
					return ResponseEntity.ok(map);
				}
			}else{
				List<CustomHisProIns> list = new ArrayList<CustomHisProIns>();
				map.put("total", total);
				map.put("rows", list);
				return ResponseEntity.ok(map);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}
	
	/**
	 * 查询指定历史流程实例下的详细历史任务办理情况
	 * @param processInstanceId
	 * @return
	 */
	@RequestMapping(value = "detail/{processInstanceId}",method = RequestMethod.GET)
	public ResponseEntity<List<CustomHisTaskIns>> getDetailHisTaskIns(@PathVariable("processInstanceId") String processInstanceId){
		try {
			@SuppressWarnings("deprecation")
			HistoricTaskInstanceQuery query = historyService.createHistoricTaskInstanceQuery().processInstanceId(processInstanceId).orderByHistoricTaskInstanceStartTime().desc();
			List<HistoricTaskInstance> hisTaskInsList = query.list();
			
			List<CustomHisTaskIns> list = new ArrayList<CustomHisTaskIns>();
			for (HistoricTaskInstance historicTaskInstance : hisTaskInsList) {
				CustomHisTaskIns customHisTaskIns = new CustomHisTaskIns();
				String taskId = historicTaskInstance.getId();
				customHisTaskIns.setHisTaskInsId(taskId);
				customHisTaskIns.setProcessDefinitionId(historicTaskInstance.getProcessDefinitionId());
				customHisTaskIns.setProcessInstanceId(historicTaskInstance.getProcessInstanceId());
				customHisTaskIns.setTaskDefinitionKey(historicTaskInstance.getTaskDefinitionKey());
				customHisTaskIns.setTaskName(historicTaskInstance.getName());
				customHisTaskIns.setStartTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(historicTaskInstance.getStartTime()));
				StringBuffer status = new StringBuffer();
				if(historicTaskInstance.getClaimTime() != null){
					customHisTaskIns.setClaimTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(historicTaskInstance.getClaimTime()));
					status.append("已签收");
				}else{
					status.append("未签收");
				}
				if(historicTaskInstance.getEndTime() != null){
					customHisTaskIns.setEndTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(historicTaskInstance.getEndTime()));
					status.append("，已办理");
				}else{
					status.append("，未办理");
				}
				if(historicTaskInstance.getDurationInMillis() != null){
					long duration = historicTaskInstance.getDurationInMillis();
					customHisTaskIns.setDutation(formatTime(duration));
				}
				if(historicTaskInstance.getAssignee() != null){
					customHisTaskIns.setAssignee(historicTaskInstance.getAssignee());
				}
				customHisTaskIns.setStatus(status);
				
				HistoricVariableInstance singleResult = historyService.createHistoricVariableInstanceQuery().
						processInstanceId(processInstanceId).variableName(taskId).singleResult();
				if(singleResult != null){
					String description = (String) singleResult.getValue();
					customHisTaskIns.setDescription(description);
				}
				list.add(customHisTaskIns);
			}
			return ResponseEntity.ok(list);	
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}
	
	/**
	 * 查询指定用户办理过的所有历史任务
	 * @param userId
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping(value = "hisUserTask/{userId}",method = RequestMethod.GET)
	public ResponseEntity<Map<String,Object>> getHisTaskByUser(@PathVariable("userId") String userId,
			@RequestParam("page") int page,@RequestParam("rows") int rows){
		try {
			Map<String,Object> map = historicService.getHisTaskByUser(userId,page,rows);
			return ResponseEntity.ok(map);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logUtil.writeLog("error", "查询用户办理过的历史任务出错", e);
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}

	/**
	 * 将毫秒数转换为时间
	 * @param ms
	 * @return
	 */
	public String formatTime(Long ms) {
		Integer ss = 1000;
		Integer mi = ss * 60;
		Integer hh = mi * 60;
		Integer dd = hh * 24;

		Long day = ms / dd;
		Long hour = (ms - day * dd) / hh;
		Long minute = (ms - day * dd - hour * hh) / mi;
		Long second = (ms - day * dd - hour * hh - minute * mi) / ss;

		StringBuffer sb = new StringBuffer();
		if (day > 0) {
			sb.append(day + "天");
		}else{
			sb.append("0天");
		}
		if (hour > 0) {
			sb.append(hour + "小时");
		}else{
			sb.append("0小时");
		}
		if (minute > 0) {
			sb.append(minute + "分");
		}else{
			sb.append("0分");
		}
		if (second > 0) {
			sb.append(second + "秒");
		}else{
			sb.append("0秒");
		}
		return sb.toString();
	}
}
