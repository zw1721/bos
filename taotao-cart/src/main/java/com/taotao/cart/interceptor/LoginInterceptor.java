package com.taotao.cart.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.taotao.common.utils.CookieUtils;
import com.taotao.sso.pojo.User;
import com.taotao.sso.service.UserService;
import com.taotao.web.threadlocal.UserThreadLocal;

/**
 * 登录状态校验的拦截器
 * 
 * @author huge
 *
 */
public class LoginInterceptor implements HandlerInterceptor {

    private static final String COOKIE_NAME = "TT_TOKEN";

    @Autowired
    public UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        // 获取用户的cookie
        String token = CookieUtils.getCookieValue(request, COOKIE_NAME);
        if (StringUtils.isBlank(token)) {
            return true;
        }
        // 获取Redis中的用户信息
        User user = this.userService.queryUserByToken(token);
        if (user == null) {
            return true;
        }
        // 保存用户信息到线程域
        UserThreadLocal.set(user);
        // 登录状态，放行
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
            Exception ex) throws Exception {
        // 方法结束后，把User清空
        UserThreadLocal.remove();
    }
}
