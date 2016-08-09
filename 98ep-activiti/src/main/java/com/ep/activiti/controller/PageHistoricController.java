package com.ep.activiti.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.HistoryService;
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

import com.ep.activiti.entity.CustomHisTaskIns;

@Controller
@RequestMapping("pageHistory")
public class PageHistoricController {

	@Autowired
	private HistoryService historyService;
	
	/**
	 * 查询指定历史流程实例下的详细历史任务办理情况
	 * @param processInstanceId
	 * @return
	 */
	@RequestMapping(value = "detail/{processInstanceId}",method = RequestMethod.GET)
	public ResponseEntity<Map<String,Object>> getDetailHisTaskIns(@PathVariable("processInstanceId") String processInstanceId,
			@RequestParam(value = "page",required = false) int page,@RequestParam(value = "rows",required = false) int rows){
		try {
			Map<String,Object> map = new HashMap<String,Object>();
			@SuppressWarnings("deprecation")
			HistoricTaskInstanceQuery query = historyService.createHistoricTaskInstanceQuery().processInstanceId(processInstanceId).orderByHistoricTaskInstanceStartTime().asc();
			int total = query.list().size();
			int firstResult = (page-1)*rows;
			List<HistoricTaskInstance> hisTaskInsList = query.listPage(firstResult, rows);
			if(total != 0){
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
				map.put("total", total);
				map.put("rows", list);
				return ResponseEntity.ok(map);
			}else{
				List<CustomHisTaskIns> list = new ArrayList<CustomHisTaskIns>();
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
