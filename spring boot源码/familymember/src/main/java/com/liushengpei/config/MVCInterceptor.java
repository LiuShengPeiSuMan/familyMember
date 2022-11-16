package com.liushengpei.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/*
 * 拦截管理
 * */
@Configuration
public class MVCInterceptor implements WebMvcConfigurer {

    @Autowired
    private LoginIntercept intercept;

    /*
     * 添加拦截管理
     * */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //添加自定义拦截器类
        InterceptorRegistration interceptorRegistration = registry.addInterceptor(intercept);
        //拦截所有请求
        interceptorRegistration.addPathPatterns("/**");
        //对指定请求放行
        interceptorRegistration.excludePathPatterns("/**");
    }
}
