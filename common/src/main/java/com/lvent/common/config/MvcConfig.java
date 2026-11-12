package com.lvent.common.config;

import com.lvent.common.interceptor.GlobalInterceptor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// Spring Boot 4.x 以上替换为Auto
// @Configuration
@AutoConfiguration
// 它的判断逻辑非常直接，就是检查类路径上是否存在 DispatcherServlet.class这个类
// @ConditionalOnClass(DispatcherServlet.class)
// 它的判断逻辑更智能，它不只看类路径，而是会去判断当前应用的类型。OnWebApplicationCondition 这个条件评估器会检查应用上下文是否是一个真正的 Servlet Web 应用。
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class MvcConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new GlobalInterceptor());
    }
}
