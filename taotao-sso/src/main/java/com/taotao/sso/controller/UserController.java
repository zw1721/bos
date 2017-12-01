package com.taotao.sso.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mysql.fabric.xmlrpc.base.Array;
import com.taotao.common.utils.CookieUtils;
import com.taotao.common.vo.TaotaoResult;
import com.taotao.sso.pojo.User;
import com.taotao.sso.service.UserService;

@Controller
@RequestMapping("user")
public class UserController {
    private static final String COOKIE_NAME="TT_TOKEN";
    @Autowired
    private UserService userService;

    @RequestMapping(value = "register", method = RequestMethod.GET)
    public String toRegister() {
        return "register";
    }

    /**
     * 校验数据是否可用
     * @param param
     * @param type
     * @return
     */
    @RequestMapping(value = "check/{param}/{type}", method = RequestMethod.GET)
    public ResponseEntity<Boolean> checkData(@PathVariable("param") String param,
            @PathVariable("type") Integer type) {
        try {
            // 数据校验
            Boolean boo = this.userService.checkData(param,type);
            if( boo == null){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }
            return ResponseEntity.ok(boo);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    @RequestMapping(value="doRegister",method=RequestMethod.POST)
    @ResponseBody
    public TaotaoResult doRegister(@Valid User user,BindingResult result){
    	//valid开启校验，bindingResult 把异常信息封装到这个对象中
    	if(result.hasErrors()){
    		//准备收集错误对象
    		List<String>  msg=new ArrayList<>();
    		//获取错误信息
    		List<ObjectError> allErrors = result.getAllErrors();
    		for (ObjectError objectError : allErrors) {
				msg.add(objectError.getDefaultMessage());
			}
    	
    	}
    	
    	try {
			//注册用户
			Boolean boo = this.userService.register(user);
			if(boo){
				//注册成功，返回200
				return TaotaoResult.ok();
			}
			//注册失败，返回400，参数异常
			return TaotaoResult.build(400, "注册失败，请核对信息");
		} catch (Exception e) {
			
			e.printStackTrace();
			return TaotaoResult.build(500, "服务器异常");
		}
    }
    @RequestMapping(value="login",method=RequestMethod.GET)
    public String toLogin(){
    	return "login";
    }
    
    @RequestMapping(value="doLogin",method=RequestMethod.POST)
    @ResponseBody
    public TaotaoResult doLogin(@RequestParam("username")String username,
    		@RequestParam("password")String password,
    		HttpServletRequest request,
    		HttpServletResponse response){
    	try {
			//尝试登陆，并获取token值
			String token  = this.userService.login(username,password);
			if(StringUtils.isEmpty(token)){
				//如果不为空，则表示登陆成功
				  // 登录失败
                return TaotaoResult.build(400, "用户名或密码错误");
			}else{
				  // 成功，将token写入cookie
                CookieUtils.setCookie(request, response, COOKIE_NAME, token);
                // 返回成功
                return TaotaoResult.ok();
			}
		
		} catch (Exception e) {
			e.printStackTrace();
			//出现异常,不能返回，需要处理异常，并记录参数
			return TaotaoResult.build(500, "未知错误");
		}
			
    }
    @RequestMapping(value="{token}",method=RequestMethod.GET )
    public ResponseEntity<User> queryByToken(@PathVariable("token")String token ){
    	
    	try {
			//获取用户信息
			User user = this.userService.queryByToken(token);
			if(user==null){
				//登陆超时
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
			}
			//有，返回user
			
			return ResponseEntity.ok(user);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
    }
}
