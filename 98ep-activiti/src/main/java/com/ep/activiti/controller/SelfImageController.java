package com.ep.activiti.controller;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;

import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.Execution;
import org.activiti.image.impl.DefaultProcessDiagramGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ep.activiti.service.ImageService;


@Controller
@RequestMapping("selfImage")
public class SelfImageController {

	@Autowired
	RepositoryService repositoryService;
	@Autowired
	HistoryService historyService;
	@Autowired
	RuntimeService runtimeService;
	@Autowired
	ProcessEngineConfiguration processEngineConfiguration;
	@Autowired
	ProcessEngine processEngine;
	@Autowired
	private ImageService imageService;
	
	private static final String SYS_ASSIGNEEID = "system_auto";
	private static final String SYS_ASSIGNEE = "系统";
	private static final String SYS_TASK_DEF_KEY = "confirmOrder";
	
	
	/**
	 * 读取流程图
	 * @param processDefinitionId
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = "read/{processDefinitionId}",method = RequestMethod.GET)
	public void readImage(@PathVariable("processDefinitionId") String processDefinitionId,
			HttpServletResponse response) throws IOException{
		// 设置页面不缓存
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		
		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(processDefinitionId).singleResult();
		String deploymentId = processDefinition.getDeploymentId();
		String resourceName = processDefinition.getDiagramResourceName();
		InputStream resourceStream = repositoryService.getResourceAsStream(deploymentId, resourceName);
		
		byte[] b = new byte[1024];
        int len = -1;
        while ((len = resourceStream.read(b, 0, 1024)) != -1) {
            response.getOutputStream().write(b, 0, len);
        }
	}
	
	/**
	 * 得到运行中的流程跟踪图
	 * @param processDefinitionId
	 * @param processInstanceId
	 * @param output
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = "track/{processDefinitionId}/{processInstanceId}",method = RequestMethod.GET)
	public void readImageHistoriy(@PathVariable("processDefinitionId")String processDefinitionId,
			@PathVariable("processInstanceId") String processInstanceId,OutputStream output,HttpServletResponse response) throws IOException{
		
		// 设置页面不缓存
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);
        
        List<HistoricActivityInstance> taskList = historyService.createHistoricActivityInstanceQuery().
        		processInstanceId(processInstanceId).orderByHistoricActivityInstanceId().asc().list();
        
        List<String> activeActivityIds = new ArrayList<String>();
        /*HistoricActivityInstance historicActivityInstance = taskList.get(taskList.size()-1);
        if(!historicActivityInstance.getActivityType().equals("endEvent")){
        	activeActivityIds.add(historicActivityInstance.getActivityId());
        }*/
        Execution singleResult = runtimeService.createExecutionQuery().processInstanceId(processInstanceId).singleResult();
        if(singleResult != null){
        	activeActivityIds.add(singleResult.getActivityId());
        }
        List<String> userIds = new ArrayList<String>();
        for (HistoricActivityInstance HAI : taskList) {
			if(!HAI.getActivityType().equals("startEvent") && 
					!HAI.getActivityType().equals("endEvent") && 
					!HAI.getActivityType().equals("serviceTask") && 
					!HAI.getActivityId().equals(SYS_TASK_DEF_KEY)){
				userIds.add(HAI.getAssignee());
			}
		}
        //获得所有用户名称map
        Map<String,String> userMap = imageService.getUser(userIds);
		
        ProcessDefinition processDefinition = repositoryService.getProcessDefinition(processDefinitionId);
		ProcessDefinitionEntity definitionEntity = (ProcessDefinitionEntity) processDefinition;
		
        
        Context.setProcessEngineConfiguration((ProcessEngineConfigurationImpl) processEngine.getProcessEngineConfiguration());
        String activityFontName = processEngine.getProcessEngineConfiguration().getActivityFontName();
        String labelFontName = processEngine.getProcessEngineConfiguration().getLabelFontName();
        
        
        List<String> highLightedFlows = imageService.getHighLightedFlows(definitionEntity, taskList);
        
        InputStream imageStream = new DefaultProcessDiagramGenerator().generateDiagram(bpmnModel, "png", activeActivityIds, highLightedFlows, activityFontName, labelFontName, null, 1.0);
        
		//得到画笔
		BufferedImage bufferedImage = ImageIO.read(imageStream);
		Graphics graphics = bufferedImage.getGraphics();
		graphics.setColor(Color.black);
		graphics.setFont(new Font("微软雅黑", Font.PLAIN, 25));
		graphics.drawString("说明", 70, 50);
		graphics.setFont(new Font("微软雅黑", Font.PLAIN, 15));
		graphics.drawString("*红色框表示当前待办理的任务", 70, 75);
		graphics.drawString("*如果红色图标中既没有办理人也没有办理时间，则表示改任务还未被签收", 70, 95);
		graphics.drawString("*如果红色图标中只有办理人而没有办理时间，则表示该任务已被签收，但还未办理", 70, 115);
		
		
		//遍历历史任务列表
		SimpleDateFormat sdf = new SimpleDateFormat("yy/MM/dd HH:mm");
		for (HistoricActivityInstance task : taskList) {
			if(!task.getActivityType().equals("startEvent") && !task.getActivityType().equals("endEvent")){
				ActivityImpl activity = definitionEntity.findActivity(task.getActivityId());
				int x = activity.getX();
				int y = activity.getY();
				//查询历史任务办理人
				String assignee = task.getAssignee();
				graphics.setFont(new Font("微软雅黑", Font.PLAIN, 15));
				if(assignee != null){
					if(assignee.equals(SYS_ASSIGNEEID)){
						graphics.drawString(SYS_ASSIGNEE, x+22, y+18);
					}else{
						graphics.drawString(userMap.get(assignee), x+22, y+18);
					}
				}
				
				if(task.getEndTime() != null){
					graphics.setFont(new Font("微软雅黑", Font.PLAIN, 12));
					graphics.drawString(sdf.format(task.getEndTime()), x+3, y+33);
				}
			}
		}
		//执行绘图
		graphics.dispose();
		//
		ImageIO.write(bufferedImage, "png", output);
	}
	
	
}    