package com.taotao.sso.service;

import java.util.Date;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.taotao.common.service.RedisService;
import com.taotao.common.utils.JsonUtils;
import com.taotao.sso.mapper.UserMapper;
import com.taotao.sso.pojo.User;

@Service
public class UserService {
	@Autowired
	private RedisService redisService;
    @Autowired
    private UserMapper userMapper;
    
    public Boolean checkData(String param, Integer type) {
        User record = new User();
        switch (type) {
        case 1:
            record.setUsername(param);
            break;
        case 2:
            record.setPhone(param);
            break;
        case 3:
            record.setEmail(param);
            break;
        default:
            // 参数有误，返回null
            return null;
        }
        return this.userMapper.selectCount(record) == 0;
    }

	public Boolean register(User user) {
		//初始化用户信息
		
		user.setId(null);
		user.setCreated(new Date());
		user.setUpdated(user.getCreated());
		
		//对密码进行加密
		user.setPassword(DigestUtils.md5Hex(user.getPassword()));
		int con = this.userMapper.insert(user);
		return con==1;
	}

	public String login(String username, String password) throws JsonProcessingException {
		User record=new User();
		record.setUsername(username);
		//查找用户
		User user = this.userMapper.selectOne(record);
		if(user==null){
			//如果没有用户，证明用户名错误，直接返回
			return null;
		}
		if(!user.getPassword().equals(DigestUtils.md5Hex(password))){
			//如果查询到的密码和用户输入的密码不一致,返回null
			return null;
		}
		//登陆成功，生成token
		String token = DigestUtils.md5Hex(username+System.currentTimeMillis());
		//吧用户信息保存到缓存中
		this.redisService.set("TOKEN_"+token, JsonUtils.toString(user), 1800);
		
		return token;
	}

	public User queryByToken(String token) {
		String key="TOKEN_"+token;
		String jsonData = this.redisService.get(key);
		if(StringUtils.isEmpty(jsonData)){
			//如果是空，返回null
			return null;
		}
		try {
			this.redisService.expire(key, 1800);
			return JsonUtils.toBean(jsonData, User.class);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
