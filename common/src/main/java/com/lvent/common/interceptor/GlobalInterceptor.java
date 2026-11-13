package com.lvent.common.interceptor;

import com.lvent.common.utils.UserContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;import org.springframework.web.servlet.HandlerInterceptor;import org.springframework.web.servlet.ModelAndView;

@Slf4j
public class GlobalInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String userInfo = request.getHeader("userInfo");
        if (userInfo != null && !userInfo.isEmpty()) {
            UserContext.setUser(Integer.valueOf(userInfo));
        }
        log.info("本次请求的微服务为:{},请求的用户id为:{}",request.getRequestURI(),UserContext.getUser());
        // 无论是否存在用户信息，全部放行
        // 本拦截器只做提取，用于从请求头中提取用户信息，而对于没有用户信息的
        // 为了防止tomcat线程复用导致ThreadLocal中存入了上一次请求中的数据，所以需要在afterCompletion方法中清理ThreadLocal
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
        // 清理用户资源
        log.info("清理用户信息");
        UserContext.removeUser();
    }}
