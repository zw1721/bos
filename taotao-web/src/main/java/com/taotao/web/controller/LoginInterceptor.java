package com.taotao.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.taotao.common.utils.CookieUtils;
import com.taotao.web.service.UserService;
import com.taotao.web.utils.UserThreadLocal;
import com.taotao.web.vo.User;

//拦截器对状态进行校验
public class LoginInterceptor implements HandlerInterceptor{
	
	@Autowired
	private UserService userService;
	private static final String COOKIENAME="TT_TOKEN";
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		//获取用户的token
		String token = CookieUtils.getCookieValue(request, COOKIENAME);
		if(StringUtils.isEmpty(token)){
			//如果是空，直接返回false,并反到登陆页面
			response.sendRedirect(userService.SSO_TAOTAO_BASE_URL+"/user/login.html");
			return false;
		}
		//从sso中获取用户信息
		User user = this.userService.queryUserByToken(token);
		if(user==null){
			//如果用户为空，说明登陆超时，重新登陆
			response.sendRedirect(userService.SSO_TAOTAO_BASE_URL+"/user/login.html");
			return false;
		}
		//用户已经登陆，保存用户信息
		UserThreadLocal.set(user);
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
		UserThreadLocal.remove();
		
	}

}
