package com.ep.activiti.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Path;

import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.GraphicInfo;
import org.activiti.bpmn.model.Process;
import org.activiti.editor.constants.ModelDataJsonConstants;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.ActivitiException;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.Model;
import org.activiti.engine.repository.ModelQuery;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.alibaba.fastjson.JSON;
import com.ep.activiti.entity.CustomModel;
import com.ep.activiti.entity.ModelMetaInfo;
import com.ep.activiti.logUtil.LogUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Controller
@RequestMapping("model")
public class ModelController {

	final static String MODEL_ID = "modelId";
	final static String MODEL_NAME = "name";
	final static String MODEL_REVISION = "revision";
	final static String MODEL_DESCRIPTION = "description";

	@Autowired
	RepositoryService repositoryService;

	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private LogUtil logUtil;

	/**
	 * 保存模板图片
	 * @param modelId
	 * @param json_xml
	 * @param svg_xml
	 * @param name
	 * @param description
	 */
	@RequestMapping(value = "save/{modelId}", method = RequestMethod.PUT)
	@ResponseStatus(value = HttpStatus.OK)
	public void saveModel(@PathVariable String modelId,
			@RequestParam("json_xml") String json_xml,
			@RequestParam("svg_xml") String svg_xml,
			@RequestParam("name") String name,
			@RequestParam("description") String description) {
		try {

			Model model = repositoryService.getModel(modelId);

			ObjectNode modelJson = (ObjectNode) objectMapper.readTree(model
					.getMetaInfo());

			modelJson.put(MODEL_NAME, name);
			modelJson.put(MODEL_DESCRIPTION, description);
			model.setMetaInfo(modelJson.toString());
			model.setName(name);

			repositoryService.saveModel(model);

			repositoryService.addModelEditorSource(model.getId(),
					json_xml.getBytes("utf-8"));

			InputStream svgStream = new ByteArrayInputStream(
					svg_xml.getBytes("utf-8"));
			TranscoderInput input = new TranscoderInput(svgStream);

			PNGTranscoder transcoder = new PNGTranscoder();
			// Setup output
			ByteArrayOutputStream outStream = new ByteArrayOutputStream();
			TranscoderOutput output = new TranscoderOutput(outStream);

			// Do the transformation
			transcoder.transcode(input, output);
			final byte[] result = outStream.toByteArray();
			repositoryService.addModelEditorSourceExtra(model.getId(), result);
			outStream.close();

		} catch (Exception e) {
			logUtil.writeLog("error", "保存模板出错", e);
		}
	}

	/**
	 * 在用户登录状态下创建模板
	 * @param name
	 * @param key
	 * @param description
	 * @param tenantId
	 * @return
	 */
	@SuppressWarnings("deprecation")
	@RequestMapping(value = "create/{tenantId}", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> createModel(
			@PathVariable("tenantId") String tenantId,
			@RequestParam("name") String name, @RequestParam("key") String key,
			@RequestParam("description") String description
			) {

		Model ModelExist = repositoryService.createModelQuery().modelKey(key)
				.modelTenantId(tenantId).singleResult();
		if (ModelExist != null) {
			Map<String, Object> tips = new HashMap<String, Object>();
			tips.put("key", "error");
			return ResponseEntity.ok(tips);
		}
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			ObjectNode editorNode = objectMapper.createObjectNode();
			editorNode.put("id", "canvas");
			editorNode.put("resourceId", "canvas");
			ObjectNode stencilSetNode = objectMapper.createObjectNode();
			stencilSetNode.put("namespace",
					"http://b3mn.org/stencilset/bpmn2.0#");
			editorNode.put("stencilset", stencilSetNode);
			Model modelData = repositoryService.newModel();

			ObjectNode modelObjectNode = objectMapper.createObjectNode();
			modelObjectNode.put(ModelDataJsonConstants.MODEL_NAME, name);
			modelObjectNode.put(ModelDataJsonConstants.MODEL_REVISION, 1);
			description = StringUtils.defaultString(description);
			modelObjectNode.put(ModelDataJsonConstants.MODEL_DESCRIPTION,
					description);

			modelData.setMetaInfo(modelObjectNode.toString());
			modelData.setName(name);
			modelData.setTenantId(tenantId);
			modelData.setKey(StringUtils.defaultString(key));

			repositoryService.saveModel(modelData);
			repositoryService.addModelEditorSource(modelData.getId(),
					editorNode.toString().getBytes("utf-8"));
			Map<String, Object> modelId = new HashMap<String, Object>();
			modelId.put("key", "id");
			modelId.put("modelId", modelData.getId());
			return ResponseEntity.ok(modelId);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			logUtil.writeLog("error", "用户系统下创建模板出错", e);
		}

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
				null);

	}
	
	/**
	 * 后台管理员给指定客户创建模板
	 * @param name
	 * @param key
	 * @param description
	 * @param tenantId
	 * @return
	 */
	@SuppressWarnings("deprecation")
	@RequestMapping(value = "create", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> adminCreateModel(
			@RequestParam("tenantId") String tenantId,
			@RequestParam("name") String name, @RequestParam("key") String key,
			@RequestParam("description") String description
			) {

		Model ModelExist = repositoryService.createModelQuery().modelKey(key)
				.modelTenantId(tenantId).singleResult();
		if (ModelExist != null) {
			Map<String, Object> tips = new HashMap<String, Object>();
			tips.put("key", "error");
			return ResponseEntity.ok(tips);
		}
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			ObjectNode editorNode = objectMapper.createObjectNode();
			editorNode.put("id", "canvas");
			editorNode.put("resourceId", "canvas");
			ObjectNode stencilSetNode = objectMapper.createObjectNode();
			stencilSetNode.put("namespace",
					"http://b3mn.org/stencilset/bpmn2.0#");
			editorNode.put("stencilset", stencilSetNode);
			Model modelData = repositoryService.newModel();

			ObjectNode modelObjectNode = objectMapper.createObjectNode();
			modelObjectNode.put(ModelDataJsonConstants.MODEL_NAME, name);
			modelObjectNode.put(ModelDataJsonConstants.MODEL_REVISION, 1);
			description = StringUtils.defaultString(description);
			modelObjectNode.put(ModelDataJsonConstants.MODEL_DESCRIPTION,
					description);

			modelData.setMetaInfo(modelObjectNode.toString());
			modelData.setName(name);
			modelData.setTenantId(tenantId);
			modelData.setKey(StringUtils.defaultString(key));

			repositoryService.saveModel(modelData);
			repositoryService.addModelEditorSource(modelData.getId(),
					editorNode.toString().getBytes("utf-8"));
			Map<String, Object> modelId = new HashMap<String, Object>();
			modelId.put("key", "id");
			modelId.put("modelId", modelData.getId());
			return ResponseEntity.ok(modelId);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			logUtil.writeLog("error", "后台给指定用户的创建模板出错", e);
		}

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
				null);

	}
	
	/**
	 * 查询所有的模板信息
	 * @return
	 */
	@RequestMapping(value = "getAll",method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> getAllModel(@RequestParam("page")int page,@RequestParam("rows") int rows){
		try {
			ModelQuery modelQuery = repositoryService.createModelQuery().orderByCreateTime().desc();
			int total = modelQuery.list().size();
			
			int firstResult = (page-1)*rows;
			List<Model> modelList = modelQuery.listPage(firstResult, rows);
			List<CustomModel> list = new ArrayList<CustomModel>();
			Map<String,Object> map = new HashMap<String,Object>();
			if(modelList != null){
				for (Model model : modelList) {
					CustomModel customModel = new CustomModel();
					customModel.setModelId(model.getId());
					customModel.setModelName(model.getName());
					customModel.setModelKey(model.getKey());
					customModel.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(model.getCreateTime()));
					customModel.setLastUpdateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(model.getLastUpdateTime()));
					String metaInfo = model.getMetaInfo();
					ModelMetaInfo modelMetaInfo = JSON.parseObject(metaInfo,ModelMetaInfo.class);
					String description = modelMetaInfo.getDescription();
					customModel.setDescription(description);
					customModel.setTenantId(model.getTenantId());
					list.add(customModel);
				}
				map.put("total", total);
				map.put("rows", list);
				return ResponseEntity.ok(map);
			}
			map.put("total",0);
			map.put("rows", list);
			return ResponseEntity.ok(map);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logUtil.writeLog("error", "查询所有模板出错", e);
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}

	/**
	 * 查询指定租户模板
	 * @param tenantId
	 * @return
	 */
	@RequestMapping(value = "userModel/{tenantId}", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<Map<String, Object>> getUserModel(
			@PathVariable("tenantId") String tenantId,@RequestParam("page") int page,
			@RequestParam("rows") int rows) {

		try {
			// 查询指定租户拥有的模板
			ModelQuery modelQuery = repositoryService.createModelQuery()
					.modelTenantId(tenantId).orderByCreateTime().desc();
			int total = modelQuery.list().size();
			int firstResult = (page-1)*rows;
			List<Model> modelList = modelQuery.listPage(firstResult, rows);
			List<CustomModel> customModelList = new ArrayList<CustomModel>();
			Map<String,Object> map = new HashMap<String,Object>();
			if(modelList != null){
				// 遍历模板集合，并构造自定义模板
				for (Model model : modelList) {
					CustomModel customModel = new CustomModel();
					customModel.setModelId(model.getId());
					customModel.setModelName(model.getName());
					customModel.setModelKey(model.getKey());
					customModel.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(model.getCreateTime()));
					customModel.setLastUpdateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(model.getLastUpdateTime()));
					String metaInfo = model.getMetaInfo();
					ModelMetaInfo modelMetaInfo = JSON.parseObject(metaInfo,ModelMetaInfo.class);
					String description = modelMetaInfo.getDescription();
					customModel.setDescription(description);
					customModel.setTenantId(model.getTenantId());
					//将构造的自定义模板添加进模板列表
					customModelList.add(customModel);
				}
				map.put("total", total);
				map.put("rows", customModelList);
				return ResponseEntity.ok(map);
			}
			map.put("total", 0);
			map.put("rows", customModelList);
			return ResponseEntity.ok(map);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logUtil.writeLog("error", "查询用户模板出错",e);
		}

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);

	}
	
	/**
	 * 删除指定模板
	 * @param modelId
	 * @return
	 */
	@RequestMapping(value = "delete/{modelId}",method = RequestMethod.DELETE)
	public ResponseEntity<Map<String,Object>> deleteModel(@PathVariable("modelId") String modelId){
		
		try {
			repositoryService.deleteModel(modelId);
			Map<String,Object> tip = new HashMap<String,Object>();
			tip.put("key", "success");
			return ResponseEntity.ok(tip);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}
	
	/**
	 * 部署指定的流程模板
	 * @param modelId
	 * @return
	 */
	@RequestMapping(value = "deploy/{modelId}",method = RequestMethod.POST)
	public ResponseEntity<Map<String,Object>> deployModel(@PathVariable("modelId") String modelId){
		
		Map<String,Object> map = new HashMap<String,Object>();
		
		try {
			Model modelData = repositoryService.getModel(modelId);
			ObjectNode modelNode = (ObjectNode) new ObjectMapper().readTree(repositoryService.getModelEditorSource(modelData.getId()));
			byte[] bpmnBytes = null;

			BpmnModel model = new BpmnJsonConverter().convertToBpmnModel(modelNode);
			Map<String, List<GraphicInfo>> flowLocationMap = model.getFlowLocationMap();
			Map<String, GraphicInfo> locationMap = model.getLocationMap();
			List<Process> processes = model.getProcesses();
			
			if(flowLocationMap.isEmpty() || locationMap.isEmpty() || processes.isEmpty()){
				map.put("key", "error");
				return ResponseEntity.ok(map);
			}
			bpmnBytes = new BpmnXMLConverter().convertToXML(model);

			String processName = modelData.getName() + ".bpmn20.xml";
			Deployment deployment = repositoryService.createDeployment().
					tenantId(modelData.getTenantId()).
					name(modelData.getName()).
					addString(processName, new String(bpmnBytes,"UTF-8")).deploy();
			
			String deployId = deployment.getId();
			if(deployId != null && !deployId.equals("")){
				map.put("key", "success");
				map.put("deployId", deployId);
				return ResponseEntity.ok(map);
			}else{
				map.put("key", "fali");
				return ResponseEntity.ok(map);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logUtil.writeLog("error", "部署流程出错", e);
		}
		
		map.put("key", "error");
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(map);
		
	}
	
	/**
	 * 根据modelId获得tenantId
	 * @param modelId
	 * @return
	 */
	@RequestMapping(value = "getTenantId/{modelId}",method = RequestMethod.GET)
	public ResponseEntity<Map<String,String>> getTenantId(@PathVariable("modelId") String modelId){
		try {
			Model model = repositoryService.createModelQuery().modelId(modelId).singleResult();
			String tenantId = model.getTenantId();
			Map<String,String> map = new HashMap<String,String>();
			map.put("tenantId", tenantId);
			return ResponseEntity.ok(map);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logUtil.writeLog("error", "通过模型查询tenantId出错", e);
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}
	
	/**
	 * 导出模型的bpmn文件
	 * @param modelId
	 * @param response
	 */
	@RequestMapping("export/{modelId}")
	public void export(@PathVariable("modelId") String modelId,
			HttpServletResponse response){
		try {
			Model modelData = repositoryService.getModel(modelId);
			BpmnJsonConverter jsonConverter = new BpmnJsonConverter();
			byte[] modelEditorSource = repositoryService.getModelEditorSource(modelData.getId());

			JsonNode editorNode = new ObjectMapper().readTree(modelEditorSource);
			BpmnModel bpmnModel = jsonConverter.convertToBpmnModel(editorNode);

			// 处理异常
			if (bpmnModel.getMainProcess() == null) {
			    response.setStatus(HttpStatus.UNPROCESSABLE_ENTITY.value());
			    response.getOutputStream().println("no main process, can't export the resource");
			    response.flushBuffer();
			    return;
			}

			String filename = "";
			byte[] exportBytes = null;

			String mainProcessId = bpmnModel.getMainProcess().getId();

		    BpmnXMLConverter xmlConverter = new BpmnXMLConverter();
		    exportBytes = xmlConverter.convertToXML(bpmnModel);

		    //filename = mainProcessId + ".bpmn20.xml";
		    filename = mainProcessId + ".bpmn";

			ByteArrayInputStream in = new ByteArrayInputStream(exportBytes);
			IOUtils.copy(in, response.getOutputStream());

			response.setHeader("Content-Disposition", "attachment; filename=" + filename);
			response.flushBuffer();
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
}
