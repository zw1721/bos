package com.taotao.web.service;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.common.httpclient.BeanResponseHandler;
import com.taotao.common.service.ApiService;
import com.taotao.web.vo.User;
@Service
public class UserService {
	@Autowired
	private ApiService apiService;
	@Value("${SSO_TAOTAO_BASE_URL}")
	public String SSO_TAOTAO_BASE_URL;
	//根据token查询用户信息
	@RequestMapping()
	public User queryUserByToken(String token){
		
		try {
			//通过httpclient访问sso查询用户信息
			String uri=SSO_TAOTAO_BASE_URL+"/user/"+token+".html";
			 String userJson = this.apiService.doGet(uri, null);
			
			//反序列化
			 return new ObjectMapper().readValue(userJson, User.class);
		}catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}
