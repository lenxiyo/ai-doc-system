package com.lvent.gateway.filter;

import com.lvent.common.exception.UnAuthException;
import com.lvent.gateway.config.AuthProperties;
import com.lvent.gateway.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AuthFilter implements GlobalFilter, Ordered {

    private final AuthProperties authProperties;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, @NonNull GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        // 需要排除登录等路径
        // 通过注解配置yaml文件中的排除路径到类中
        if (isExclude(request.getPath().toString())) {
            return chain.filter(exchange);
        }
        // 校验token
        HttpHeaders headers = request.getHeaders();
        // 拿到名称为Authorization的请求头
        List<String> authorization = headers.get("Authorization");
        String token = null;
        // 校验拿到token
        if (authorization != null && !authorization.isEmpty()) {
            token = authorization.get(0);
        }
        // 拿到用户信息
        Integer userId;
        try {
            userId = JwtUtil.validateToken(token);
            // TODO 后续增加全局异常处理器，现阶段异常不做处理（微服务内每个微服务的全局异常处理器只对本微服务生效，可以定义在common模块）
        } catch (UnAuthException e){
            // 如果token为空，则返回未登录
            ServerHttpResponse response = exchange.getResponse();
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            // 直接返回complete
            return response.setComplete();
        }
        String userInfo = userId.toString();
        ServerWebExchange serverWebExchange = exchange
                .mutate()
                // builder 是 ServerHttpRequest.Builder，应该用 builder.header("user_id", userInfo)，而不是调用外面的 headers.add(...)（那是旧的请求头，对 mutated request 无效）
                // Java 的 lambda（以及匿名内部类）只能捕获 final 或 effectively final 的局部变量，
                .request(builder -> builder.header("userInfo",userInfo))
                .build();
        return chain.filter(serverWebExchange);
    }

    private boolean isExclude(String path) {
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        List<String> excludePaths = authProperties.getExcludePath();
        for (String excludePath : excludePaths) {
            if (antPathMatcher.match(excludePath,path)){
                return true;
            }
        }
        return false;
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
