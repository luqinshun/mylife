package com.ep.activiti.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.User;
import org.activiti.engine.identity.UserQuery;
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

import com.ep.activiti.entity.CustomUser;
import com.ep.activiti.service.CustomIdentityService;



@Controller
@RequestMapping("identity")
public class IdentityController {

	@Autowired
	IdentityService identityService;
	@Autowired
	private CustomIdentityService customIdentityService;
	
	@RequestMapping(value = "startUserList",method = RequestMethod.GET)
	public ResponseEntity<List<CustomUser>> getProStartUserList(){
		try {
			List<CustomUser> list = customIdentityService.getProStartUserList();
			return ResponseEntity.ok(list);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}
	
	/**
	 * 用户列表
	 * @param request
	 * @return
	 *//*
	@RequestMapping(value = "user/list",method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<List<com.ep.activiti.entity.CustomUser>> userList(){
		
		try {
			UserQuery query = identityService.createUserQuery();
			
			//查询获得activiti自带的User列表
			List<User> list = query.list();
			
			//创建自定义user列表
			List<com.ep.activiti.entity.CustomUser> userList = new ArrayList<com.ep.activiti.entity.CustomUser>(); 
			for (User user : list) {
				String id = user.getId();
				//获得每个userId并付给自定义user
				com.ep.activiti.entity.CustomUser newUser = new com.ep.activiti.entity.CustomUser();
				newUser.setUserId(id);
				userList.add(newUser);
			}
			return ResponseEntity.ok(userList);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}
	
	*//**
	 * 保存用户
	 * @param userId 用户id
	 * @param password 用户密码
	 * @param redirectAttributes
	 * @return
	 *//*
	@RequestMapping(value = "user/save", method = RequestMethod.POST)
	public ResponseEntity<Map<String,Object>> saveUser(@RequestParam(value = "userId",required = true) String userId,
													   @RequestParam(value = "password",required = true) String password){
		
		User user = identityService.createUserQuery().userId(userId).singleResult();
		
		Map<String, Object> map = new HashMap<String,Object>();
		if(user == null){
			user = identityService.newUser(userId);
		}else{
			map.put("message", "用户已存在");
			return ResponseEntity.ok(map);
		}
		
		if(StringUtils.isNoneBlank(password)){
			user.setPassword(password);
		}
		
		identityService.saveUser(user);
		return ResponseEntity.status(HttpStatus.CREATED).body(null);
	}
	
	*//**
	 * 删除用户
	 * @param userId
	 * @param redirectAttributes
	 * @return
	 *//*
	@RequestMapping(value = "user/delete/{userId}",method = RequestMethod.GET)
	public ResponseEntity<Map<String,Object >> deleteUser(@PathVariable("userId") String userId){
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			identityService.deleteUser(userId);
			map.put("message", "删除成功");
			return ResponseEntity.ok(map);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}
	
	@RequestMapping(value = "manage/{userId}",method = RequestMethod.POST)
	public ResponseEntity<Map<String,String>> manageUser(@PathVariable("userId") String userId,HttpServletRequest request){
		try {
			request.getSession().setAttribute("manageUserId", userId);
			Map<String,String> map = new HashMap<String,String>();
			if(userId.equals(request.getSession().getAttribute("manageUserId"))){
				map.put("key","success");
				return ResponseEntity.ok(map);
			}
			map.put("key", "fail");
			return ResponseEntity.ok(map);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}
	
	@RequestMapping("getUserId")
	public ResponseEntity<Map<String,String>> getUserId(HttpServletRequest request){
		try {
			String userId = (String) request.getSession().getAttribute("manageUserId");
			Map<String,String> map = new HashMap<String,String>();
			map.put("key", userId);
			return ResponseEntity.ok(map);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}*/
}