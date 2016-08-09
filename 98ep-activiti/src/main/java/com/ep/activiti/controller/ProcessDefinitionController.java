package com.ep.activiti.controller;

import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.ep.activiti.entity.CustomProcessDefinition;
import com.ep.activiti.logUtil.LogUtil;

@Controller
@RequestMapping("processDefinition")
public class ProcessDefinitionController {

	@Autowired
	private RepositoryService repositoryService;
	@Autowired
	private LogUtil logUtil;
	
	/**
	 * 获取流程定义列表
	 * @param modelKey
	 * @param tenantId
	 * @return
	 */
	@RequestMapping(value = "list/{name}/{tenantId}",method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> processDefinitionList(@PathVariable("name") String name,
			@PathVariable("tenantId") String tenantId,@RequestParam(value = "page",required = false) int page,@RequestParam(value = "rows",required = false) int rows){
		
		try {
			String newName = URLDecoder.decode(name,"UTF-8").trim();
			String resourceName = newName + ".bpmn20.xml";
			int firstResult = (page-1)*rows;
			ProcessDefinitionQuery query = repositoryService.createProcessDefinitionQuery().processDefinitionResourceName(resourceName).processDefinitionTenantId(tenantId);
			int total = query.list().size();
			List<ProcessDefinition> processDefinitionList = query.listPage(firstResult, rows);
			Map<String,Object> map = new HashMap<String,Object>();
			if(total == 0){
				map.put("total", total);
				return ResponseEntity.ok(map);
			}
			List<CustomProcessDefinition> list = new ArrayList<CustomProcessDefinition>();
			for (ProcessDefinition processDefinition : processDefinitionList) {
				CustomProcessDefinition customProcessDefinition = new CustomProcessDefinition();
				customProcessDefinition.setProcessDefinitionId(processDefinition.getId());
				customProcessDefinition.setDeploymentId(processDefinition.getDeploymentId());
				customProcessDefinition.setName(processDefinition.getName());
				customProcessDefinition.setKey(processDefinition.getKey());
				customProcessDefinition.setVersion(processDefinition.getVersion());
				
				Deployment deployment = repositoryService.createDeploymentQuery().
						deploymentId(processDefinition.getDeploymentId()).singleResult();
				customProcessDefinition.setDeployTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(deployment.getDeploymentTime()));
				
				list.add(customProcessDefinition);
			}
			map.put("total", total);
			map.put("rows", list);
			return ResponseEntity.ok(map);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logUtil.writeLog("error", "查询指定用户流程定义列表出错", e);
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	} 
	
}
