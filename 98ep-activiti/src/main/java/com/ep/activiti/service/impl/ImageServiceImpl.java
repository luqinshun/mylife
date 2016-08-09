package com.ep.activiti.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.JavaType;
import org.glassfish.jersey.client.ClientConfig;
import org.springframework.stereotype.Service;

import com.ep.activiti.common.SysUrlUtil;
import com.ep.activiti.entity.User;
import com.ep.activiti.service.ImageService;

@Service
public class ImageServiceImpl implements ImageService {

	private final Client client = ClientBuilder.newClient(new ClientConfig());

	public List<String> getHighLightedFlows(
			ProcessDefinitionEntity processDefinitionEntity,
			List<HistoricActivityInstance> historicActivityInstances) {
		List<String> highFlows = new ArrayList<String>();
		List<String> ids = new ArrayList<String>();
		for (HistoricActivityInstance historicActivityInstance : historicActivityInstances) {
			ids.add(historicActivityInstance.getActivityId());
		}
		for (HistoricActivityInstance historicActivityInstance : historicActivityInstances) {
			String activityId = historicActivityInstance.getActivityId();
			ActivityImpl activity = processDefinitionEntity
					.findActivity(activityId);
			List<PvmTransition> list = activity.getOutgoingTransitions();
			if (list.size() == 1) {
				for (PvmTransition pvmTransition : list) {
					highFlows.add(pvmTransition.getId());
				}
			} else if (list.size() != 0 && list.size() > 1) {
				for (PvmTransition pvmTransition : list) {
					ActivityImpl activityImpl = (ActivityImpl) pvmTransition
							.getDestination();
					String id = activityImpl.getId();
					if (ids.contains(id)) {
						highFlows.add(pvmTransition.getId());
					}
				}
			}
		}

		return highFlows;
	}

	/*
	 * public List<String> getHighLightedFlows(ProcessDefinitionEntity
	 * processDefinitionEntity,List<HistoricActivityInstance>
	 * historicActivityInstances) {
	 * 
	 * List<String> highFlows = new ArrayList<String>(); // 用以保存高亮的线flowId
	 * for(int i = 0; i < historicActivityInstances.size() - 1; i++) {
	 * //对历史流程节点进行遍历 ActivityImpl activityImpl =
	 * processDefinitionEntity.findActivity
	 * (historicActivityInstances.get(i).getActivityId()); //得到节点定义的详细信息
	 * List<ActivityImpl> sameStartTimeNodes = new ArrayList<ActivityImpl>();
	 * //用以保存后需开始时间相同的节点 ActivityImpl sameActivityImpl1 =
	 * processDefinitionEntity.findActivity(historicActivityInstances.get(i +
	 * 1).getActivityId()); //将后面第一个节点放在时间相同节点的集合里
	 * sameStartTimeNodes.add(sameActivityImpl1); for(int j = i + 1; j <
	 * historicActivityInstances.size() - 1; j++) { HistoricActivityInstance
	 * activityImpl1 = historicActivityInstances.get(j); //后续第一个节点
	 * HistoricActivityInstance activityImpl2 = historicActivityInstances.get(j
	 * + 1); if
	 * (activityImpl1.getStartTime().equals(activityImpl2.getStartTime())) {
	 * //如果第一个节点和第二个节点开始时间相同保存 ActivityImpl sameActivityImpl2 =
	 * processDefinitionEntity.findActivity(activityImpl2.getActivityId());
	 * sameStartTimeNodes.add(sameActivityImpl2); }else{ // 有不相同跳出循环 break; } }
	 * List<PvmTransition> pvmTransitions =
	 * activityImpl.getOutgoingTransitions(); //取出节点的所有出去的线 for(PvmTransition
	 * pvmTransition : pvmTransitions) { //对所有的线进行遍历 ActivityImpl
	 * pvmActivityImpl = (ActivityImpl) pvmTransition.getDestination();
	 * //如果取出的线的目标节点存在时间相同的节点里，保存该线的id，进行高亮显示
	 * if(sameStartTimeNodes.contains(pvmActivityImpl)) {
	 * highFlows.add(pvmTransition.getId()); } } } return highFlows; }
	 */

	@Override
	public Map<String, String> getUser(List<String> userIds) {
		try {
			Map<String, String> map = new HashMap<String, String>();
			String userIdsStr = userIds.toString().substring(1,
					userIds.toString().indexOf("]"));
			WebTarget target = client.target(
					SysUrlUtil.getValue("api_url_98ep"))
					.path("/PassportProvider.svc/GetUserNameByIdString/"
							+ userIdsStr);
			String result = target.request().get().readEntity(String.class);
			ObjectMapper mapper = new ObjectMapper();
			List<User> assigneeList = mapper.readValue(result,
					getCollectionType(mapper, List.class, User.class));
			for (User user : assigneeList) {
				map.put("" + user.getUserId(), user.getName());
			}
			return map;
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取泛型的Collection Type
	 * 
	 * @param collectionClass
	 *            泛型的Collection
	 * @param elementClasses
	 *            元素类
	 * @return JavaType Java类型
	 * @since 1.0
	 */
	public static JavaType getCollectionType(ObjectMapper mapper,
			Class<?> collectionClass, Class<?>... elementClasses) {
		return mapper.getTypeFactory().constructParametricType(collectionClass,
				elementClasses);
	}

}
