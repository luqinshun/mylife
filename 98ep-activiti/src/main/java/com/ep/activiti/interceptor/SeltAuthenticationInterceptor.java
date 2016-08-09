package com.ep.activiti.interceptor;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.ep.activiti.service.AuthenticationService;

public class SeltAuthenticationInterceptor extends HandlerInterceptorAdapter{

	@Autowired
	private AuthenticationService authenticationService;
	
	public boolean preHandle(HttpServletRequest request, 
			HttpServletResponse response, Object handler) throws Exception{
		boolean flag = authenticationService.selfCheckLogin(request,response);
		if(flag){
			return true;
		}else{
			return false;
		}
	}
	
}
