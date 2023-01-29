package com.liushengpei.config;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static com.liushengpei.util.ConstantToolUtil.SESSION_USER;

/*
 * 登录拦截器
 * */
@Component
public class LoginIntercept implements HandlerInterceptor {

    @Order(1)
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        //获取登录信息
        Object sessionAttribute = session.getAttribute(SESSION_USER);
        //此成员信息为空则进行拦截
        if (sessionAttribute == null) {
            return false;
        }
        return true;
    }
}
