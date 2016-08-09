package com.ep.activiti.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ep.activiti.entity.UserActivitiInst;
import com.ep.activiti.entity.UserProcess;
import com.ep.activiti.mapper.ProcessMapper;
import com.ep.activiti.mapper.UserActivitiInstMapper;
import com.ep.activiti.service.ProcessService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service
@Transactional(readOnly = true)
public class processServiceImpl implements ProcessService {

	@Autowired
	private ProcessMapper processMapper;

	@Autowired
	private UserActivitiInstMapper actInstMapper;

	public ProcessMapper getProcessMapper() {
		return processMapper;
	}

	public void setProcessMapper(ProcessMapper processMapper) {
		this.processMapper = processMapper;
	}

	public UserActivitiInstMapper getActInstMapper() {
		return actInstMapper;
	}

	public void setActInstMapper(UserActivitiInstMapper actInstMapper) {
		this.actInstMapper = actInstMapper;
	}

	@Override
	public Map<String, Object> getAllProcessStartByUser(String userId,
			int page, int rows) {
		Map<String, Object> map = new HashMap<String, Object>();
		PageHelper.startPage(page, rows);
		List<UserProcess> list = processMapper.getAllProcessStartByUser(userId);
		List<UserProcess> prosess = new ArrayList<UserProcess>();
		// 查询任务节点表，是否审核通过
		for (UserProcess user : list) {
			// 查询出节点表的数据
			// 是否开始审核
			List<UserActivitiInst> isCheck = actInstMapper.getActInstByProcId(
					user.getProcessInstanceId(), "userTask");
			// 开始审核后的状态,默认为0待审核,1审批中，2通过，3驳回
			if (isCheck.size() > 1 && isCheck.get(1).getEndTime() != null) {
				List<UserActivitiInst> actInstList = actInstMapper
						.getActInstByProcId(user.getProcessInstanceId(),
								"serviceTask");
				if (actInstList != null && actInstList.size() > 0) {
					for (UserActivitiInst actInst : actInstList) {
						if ("pass".equals(actInst.getActivitiId())) {
							user.setCheckStatus("2");
							continue;
						} else if ("reject".equals(actInst.getActivitiId())) {
							user.setCheckStatus("3");
							continue;
						} else {
							user.setCheckStatus("1");
						}
					}
				} else {
					user.setCheckStatus("1");
				}
			} else if (isCheck.size() == 1) {
				List<UserActivitiInst> actInstList = actInstMapper
						.getActInstByProcId(user.getProcessInstanceId(),
								"serviceTask");
				if (actInstList != null && actInstList.size() > 0) {// 自动审批订单流程的处理
					user.setCheckStatus("2");
				}
			}
			// 判断是否审核通过
			prosess.add(user);
		}
		PageInfo<UserProcess> pageInfo = new PageInfo<UserProcess>(prosess);
		map.put("total", pageInfo.getTotal());
		map.put("rows", pageInfo.getList());
		return map;
	}

	@Override
	public UserProcess getProcessByProId(String processInstanceId) {
		UserProcess userProcess = processMapper
				.getProcessByProId(processInstanceId);
		return userProcess;
	}

	@Override
	public UserProcess getProcessByBusiness(String businessKey)
			throws Exception {
		UserProcess userProcess = processMapper
				.getProcessByBusiness(businessKey);
		return userProcess;
	}

}
