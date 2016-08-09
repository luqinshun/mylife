package com.ep.activiti.serviceTask;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.glassfish.jersey.client.ClientConfig;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.ep.activiti.common.SysUrlUtil;
import com.ep.activiti.logUtil.LogUtil;

public class PassServiceTask implements JavaDelegate {

	@Autowired
	private TaskService taskService;
	@Autowired
	private LogUtil logUtil;

	private static final String url = "/BOrderProvider.svc/UpdateOrderStateForBCustomer";
	private final Client client = ClientBuilder.newClient(new ClientConfig());
	private static final boolean flag = true;

	@SuppressWarnings("deprecation")
	@Override
	public void execute(DelegateExecution execution) throws Exception {
		try {
			String businessKey = execution.getBusinessKey();
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("ordercode", businessKey);
			map.put("flag", flag);
			Response response = client
					.target(SysUrlUtil.getValue("api_url_98ep"))
					.path(url)
					.request(MediaType.APPLICATION_JSON_TYPE)
					.post(Entity.entity(JSON.toJSONString(map),
							MediaType.APPLICATION_JSON_TYPE));
			String readEntity = response.readEntity(String.class);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logUtil.writeLog("error", "审核通过回调出错", e);
		}

	}

}
